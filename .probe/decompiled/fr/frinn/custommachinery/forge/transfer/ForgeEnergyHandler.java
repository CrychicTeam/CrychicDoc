package fr.frinn.custommachinery.forge.transfer;

import com.google.common.collect.Maps;
import fr.frinn.custommachinery.common.component.EnergyMachineComponent;
import fr.frinn.custommachinery.common.util.transfer.ICommonEnergyHandler;
import fr.frinn.custommachinery.impl.component.config.RelativeSide;
import fr.frinn.custommachinery.impl.component.config.SideMode;
import java.util.Map;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.Nullable;

public class ForgeEnergyHandler implements ICommonEnergyHandler {

    private final EnergyMachineComponent component;

    private final SidedEnergyStorage generalStorage;

    private final LazyOptional<IEnergyStorage> generalCapability;

    private final Map<Direction, SidedEnergyStorage> sidedStorages = Maps.newEnumMap(Direction.class);

    private final Map<Direction, LazyOptional<IEnergyStorage>> sidedCapabilities = Maps.newEnumMap(Direction.class);

    private final Map<Direction, BlockEntity> neighbourStorages = Maps.newEnumMap(Direction.class);

    public ForgeEnergyHandler(EnergyMachineComponent component) {
        this.component = component;
        this.generalStorage = new SidedEnergyStorage(() -> SideMode.BOTH, component);
        this.generalCapability = LazyOptional.of(() -> this.generalStorage);
        for (Direction direction : Direction.values()) {
            SidedEnergyStorage storage = new SidedEnergyStorage(() -> component.getConfig().getSideMode(direction), component);
            this.sidedStorages.put(direction, storage);
            this.sidedCapabilities.put(direction, LazyOptional.of(() -> storage));
        }
    }

    @Override
    public void configChanged(RelativeSide side, SideMode oldMode, SideMode newMode) {
        if (oldMode.isNone() != newMode.isNone()) {
            Direction direction = side.getDirection((Direction) this.component.getManager().getTile().m_58900_().m_61143_(BlockStateProperties.HORIZONTAL_FACING));
            ((LazyOptional) this.sidedCapabilities.get(direction)).invalidate();
            this.sidedCapabilities.put(direction, LazyOptional.of(() -> (IEnergyStorage) this.sidedStorages.get(direction)));
            if (oldMode.isNone()) {
                this.component.getManager().getLevel().updateNeighborsAt(this.component.getManager().getTile().m_58899_(), this.component.getManager().getTile().m_58900_().m_60734_());
            }
        }
    }

    @Override
    public void invalidate() {
        this.generalCapability.invalidate();
        this.sidedCapabilities.values().forEach(LazyOptional::invalidate);
    }

    @Override
    public void tick() {
        for (Direction side : Direction.values()) {
            if (this.component.getConfig().getSideMode(side) != SideMode.NONE) {
                LazyOptional<IEnergyStorage> neighbour;
                if (this.neighbourStorages.get(side) != null && !((BlockEntity) this.neighbourStorages.get(side)).isRemoved()) {
                    neighbour = ((BlockEntity) this.neighbourStorages.get(side)).getCapability(ForgeCapabilities.ENERGY, side.getOpposite());
                } else {
                    this.neighbourStorages.put(side, this.component.getManager().getLevel().getBlockEntity(this.component.getManager().getTile().m_58899_().relative(side)));
                    if (this.neighbourStorages.get(side) == null) {
                        continue;
                    }
                    neighbour = ((BlockEntity) this.neighbourStorages.get(side)).getCapability(ForgeCapabilities.ENERGY, side.getOpposite());
                }
                neighbour.ifPresent(storage -> {
                    if (this.component.getConfig().isAutoInput() && this.component.getConfig().getSideMode(side).isInput() && this.component.getEnergy() < this.component.getCapacity()) {
                        this.move(storage, (IEnergyStorage) this.sidedStorages.get(side), Integer.MAX_VALUE);
                    }
                    if (this.component.getConfig().isAutoOutput() && this.component.getConfig().getSideMode(side).isOutput() && this.component.getEnergy() > 0L) {
                        this.move((IEnergyStorage) this.sidedStorages.get(side), storage, Integer.MAX_VALUE);
                    }
                });
            }
        }
    }

    public LazyOptional<IEnergyStorage> getCapability(@Nullable Direction side) {
        if (side == null) {
            return this.generalCapability.cast();
        } else {
            return !this.component.getConfig().getSideMode(side).isInput() && !this.component.getConfig().getSideMode(side).isOutput() ? LazyOptional.empty() : ((LazyOptional) this.sidedCapabilities.get(side)).cast();
        }
    }

    private void move(IEnergyStorage from, IEnergyStorage to, int maxAmount) {
        int maxExtracted = from.extractEnergy(maxAmount, true);
        if (maxExtracted > 0) {
            int maxInserted = to.receiveEnergy(maxExtracted, true);
            if (maxInserted > 0) {
                from.extractEnergy(maxInserted, false);
                to.receiveEnergy(maxExtracted, false);
            }
        }
    }
}
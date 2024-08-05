package fr.frinn.custommachinery.forge.transfer;

import com.google.common.collect.Maps;
import fr.frinn.custommachinery.common.component.handler.ItemComponentHandler;
import fr.frinn.custommachinery.common.util.transfer.ICommonItemHandler;
import fr.frinn.custommachinery.impl.component.config.RelativeSide;
import fr.frinn.custommachinery.impl.component.config.SideMode;
import java.util.Map;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.Nullable;

public class ForgeItemHandler implements ICommonItemHandler {

    private final ItemComponentHandler handler;

    private final IItemHandler generalHandler;

    private final LazyOptional<IItemHandler> capability;

    private final Map<Direction, SidedItemHandler> sidedHandlers = Maps.newEnumMap(Direction.class);

    private final Map<Direction, LazyOptional<IItemHandler>> sidedWrappers = Maps.newEnumMap(Direction.class);

    private final Map<Direction, BlockEntity> neighbourStorages = Maps.newEnumMap(Direction.class);

    public ForgeItemHandler(ItemComponentHandler handler) {
        this.handler = handler;
        this.generalHandler = new SidedItemHandler(null, handler);
        this.capability = LazyOptional.of(() -> this.generalHandler);
        for (Direction direction : Direction.values()) {
            SidedItemHandler sided = new SidedItemHandler(direction, handler);
            this.sidedHandlers.put(direction, sided);
            this.sidedWrappers.put(direction, LazyOptional.of(() -> sided));
        }
    }

    public LazyOptional<IItemHandler> getCapability(@Nullable Direction side) {
        if (side == null) {
            return this.capability.cast();
        } else {
            return this.handler.getComponents().stream().anyMatch(component -> !component.getConfig().getSideMode(side).isNone()) ? ((LazyOptional) this.sidedWrappers.get(side)).cast() : LazyOptional.empty();
        }
    }

    @Override
    public void configChanged(RelativeSide side, SideMode oldMode, SideMode newMode) {
        if (oldMode.isNone() != newMode.isNone()) {
            Direction direction = side.getDirection((Direction) this.handler.getManager().getTile().m_58900_().m_61143_(BlockStateProperties.HORIZONTAL_FACING));
            ((LazyOptional) this.sidedWrappers.get(direction)).invalidate();
            this.sidedWrappers.put(direction, LazyOptional.of(() -> (IItemHandler) this.sidedHandlers.get(direction)));
            if (oldMode.isNone()) {
                this.handler.getManager().getLevel().updateNeighborsAt(this.handler.getManager().getTile().m_58899_(), this.handler.getManager().getTile().m_58900_().m_60734_());
            }
        }
    }

    @Override
    public void invalidate() {
        this.capability.invalidate();
        this.sidedWrappers.values().forEach(LazyOptional::invalidate);
    }

    @Override
    public void tick() {
        for (Direction side : Direction.values()) {
            if (!this.handler.getComponents().stream().allMatch(component -> component.getConfig().getSideMode(side) == SideMode.NONE)) {
                LazyOptional<IItemHandler> neighbour;
                if (this.neighbourStorages.get(side) != null && !((BlockEntity) this.neighbourStorages.get(side)).isRemoved()) {
                    neighbour = ((BlockEntity) this.neighbourStorages.get(side)).getCapability(ForgeCapabilities.ITEM_HANDLER, side.getOpposite());
                } else {
                    this.neighbourStorages.put(side, this.handler.getManager().getLevel().getBlockEntity(this.handler.getManager().getTile().m_58899_().relative(side)));
                    if (this.neighbourStorages.get(side) == null) {
                        continue;
                    }
                    neighbour = ((BlockEntity) this.neighbourStorages.get(side)).getCapability(ForgeCapabilities.ITEM_HANDLER, side.getOpposite());
                }
                neighbour.ifPresent(storage -> ((SidedItemHandler) this.sidedHandlers.get(side)).getSlotList().forEach(slot -> {
                    if (slot.getComponent().getConfig().isAutoInput() && slot.getComponent().getConfig().getSideMode(side).isInput() && slot.getComponent().getItemStack().getCount() < slot.getComponent().getCapacity()) {
                        this.moveStacks(storage, slot, Integer.MAX_VALUE);
                    }
                    if (slot.getComponent().getConfig().isAutoOutput() && slot.getComponent().getConfig().getSideMode(side).isOutput() && !slot.getComponent().getItemStack().isEmpty()) {
                        this.moveStacks(slot, storage, Integer.MAX_VALUE);
                    }
                }));
            }
        }
    }

    private void moveStacks(IItemHandler from, IItemHandler to, int maxAmount) {
        for (int i = 0; i < from.getSlots(); i++) {
            ItemStack canExtract = from.extractItem(i, maxAmount, true);
            if (!canExtract.isEmpty()) {
                ItemStack canInsert = ItemHandlerHelper.insertItemStacked(to, canExtract, false);
                if (canInsert.isEmpty()) {
                    from.extractItem(i, maxAmount, false);
                } else {
                    from.extractItem(i, canExtract.getCount() - canInsert.getCount(), false);
                }
            }
        }
    }
}
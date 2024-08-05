package fr.frinn.custommachinery.common.component;

import fr.frinn.custommachinery.api.component.ComponentIOMode;
import fr.frinn.custommachinery.api.component.IMachineComponentManager;
import fr.frinn.custommachinery.api.component.MachineComponentType;
import fr.frinn.custommachinery.common.init.Registration;
import fr.frinn.custommachinery.impl.component.AbstractMachineComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

public class PositionMachineComponent extends AbstractMachineComponent {

    public PositionMachineComponent(IMachineComponentManager manager) {
        super(manager, ComponentIOMode.NONE);
    }

    @Override
    public MachineComponentType<PositionMachineComponent> getType() {
        return (MachineComponentType<PositionMachineComponent>) Registration.POSITION_MACHINE_COMPONENT.get();
    }

    public BlockPos getPosition() {
        return this.getManager().getTile().m_58899_();
    }

    public Biome getBiome() {
        return (Biome) this.getManager().getLevel().m_204166_(this.getPosition()).value();
    }

    public ResourceKey<Level> getDimension() {
        return this.getManager().getLevel().dimension();
    }
}
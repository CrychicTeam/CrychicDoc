package fr.frinn.custommachinery.common.component;

import fr.frinn.custommachinery.api.component.ComponentIOMode;
import fr.frinn.custommachinery.api.component.IMachineComponentManager;
import fr.frinn.custommachinery.api.component.ITickableComponent;
import fr.frinn.custommachinery.api.component.MachineComponentType;
import fr.frinn.custommachinery.common.init.Registration;
import fr.frinn.custommachinery.impl.component.AbstractMachineComponent;
import net.minecraft.world.level.LightLayer;

public class LightMachineComponent extends AbstractMachineComponent implements ITickableComponent {

    private boolean emmitLight = false;

    public LightMachineComponent(IMachineComponentManager manager) {
        super(manager, ComponentIOMode.BOTH);
    }

    @Override
    public MachineComponentType<LightMachineComponent> getType() {
        return (MachineComponentType<LightMachineComponent>) Registration.LIGHT_MACHINE_COMPONENT.get();
    }

    @Override
    public void clientTick() {
        if (this.getManager().getLevel().getGameTime() % 20L == 0L) {
            this.emmitLight = this.getManager().getTile().getAppearance().getLightLevel() > 0;
            this.getManager().getLevel().getLightEngine().checkBlock(this.getManager().getTile().m_58899_());
        }
    }

    public int getMachineLight() {
        return this.emmitLight ? this.getManager().getTile().getAppearance().getLightLevel() : 0;
    }

    public int getSkyLight() {
        return this.getManager().getLevel().m_45517_(LightLayer.SKY, this.getManager().getTile().m_58899_());
    }

    public int getBlockLight() {
        return this.getManager().getLevel().m_45517_(LightLayer.BLOCK, this.getManager().getTile().m_58899_());
    }
}
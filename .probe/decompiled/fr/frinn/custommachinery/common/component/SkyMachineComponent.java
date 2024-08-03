package fr.frinn.custommachinery.common.component;

import fr.frinn.custommachinery.api.component.ComponentIOMode;
import fr.frinn.custommachinery.api.component.IMachineComponentManager;
import fr.frinn.custommachinery.api.component.MachineComponentType;
import fr.frinn.custommachinery.common.init.Registration;
import fr.frinn.custommachinery.impl.component.AbstractMachineComponent;

public class SkyMachineComponent extends AbstractMachineComponent {

    public SkyMachineComponent(IMachineComponentManager manager) {
        super(manager, ComponentIOMode.NONE);
    }

    @Override
    public MachineComponentType<SkyMachineComponent> getType() {
        return (MachineComponentType<SkyMachineComponent>) Registration.SKY_MACHINE_COMPONENT.get();
    }

    public boolean canSeeSky() {
        return this.getManager().getLevel().m_45527_(this.getManager().getTile().m_58899_().above());
    }
}
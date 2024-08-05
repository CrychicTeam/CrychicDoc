package fr.frinn.custommachinery.api.component;

import fr.frinn.custommachinery.api.codec.NamedCodec;
import fr.frinn.custommachinery.impl.codec.RegistrarCodec;

public interface IMachineComponentTemplate<T extends IMachineComponent> {

    NamedCodec<IMachineComponentTemplate<? extends IMachineComponent>> CODEC = RegistrarCodec.MACHINE_COMPONENT.dispatch(IMachineComponentTemplate::getType, MachineComponentType::getCodec, "Machine Component");

    MachineComponentType<T> getType();

    String getId();

    boolean canAccept(Object var1, boolean var2, IMachineComponentManager var3);

    T build(IMachineComponentManager var1);
}
package fr.frinn.custommachinery.api.component.variant;

import fr.frinn.custommachinery.api.ICustomMachineryAPI;
import fr.frinn.custommachinery.api.codec.NamedCodec;
import fr.frinn.custommachinery.api.component.IMachineComponent;
import fr.frinn.custommachinery.api.component.MachineComponentType;
import fr.frinn.custommachinery.impl.codec.NamedMapCodec;
import fr.frinn.custommachinery.impl.codec.RegistrarCodec;
import java.util.function.Supplier;
import net.minecraft.resources.ResourceLocation;

public interface IComponentVariant {

    static <C extends IMachineComponent> NamedMapCodec<IComponentVariant> codec(Supplier<MachineComponentType<C>> type) {
        return RegistrarCodec.CM_LOC_CODEC.<IComponentVariant>dispatch("variant", IComponentVariant::getId, id -> ICustomMachineryAPI.INSTANCE.getVariantCodec((MachineComponentType<C>) type.get(), id), "Machine component variant").aliases("varient");
    }

    ResourceLocation getId();

    NamedCodec<? extends IComponentVariant> getCodec();
}
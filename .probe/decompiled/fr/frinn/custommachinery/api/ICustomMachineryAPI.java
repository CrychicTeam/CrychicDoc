package fr.frinn.custommachinery.api;

import dev.architectury.registry.registries.Registrar;
import fr.frinn.custommachinery.api.codec.NamedCodec;
import fr.frinn.custommachinery.api.component.IMachineComponent;
import fr.frinn.custommachinery.api.component.MachineComponentType;
import fr.frinn.custommachinery.api.component.variant.IComponentVariant;
import fr.frinn.custommachinery.api.crafting.ProcessorType;
import fr.frinn.custommachinery.api.guielement.GuiElementType;
import fr.frinn.custommachinery.api.machine.MachineAppearanceProperty;
import fr.frinn.custommachinery.api.network.DataType;
import fr.frinn.custommachinery.api.requirement.RequirementType;
import fr.frinn.custommachinery.api.utils.ICMConfig;
import java.lang.reflect.InvocationTargetException;
import net.minecraft.Util;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

public interface ICustomMachineryAPI {

    ICustomMachineryAPI INSTANCE = Util.make(() -> {
        try {
            return (ICustomMachineryAPI) Class.forName("fr.frinn.custommachinery.impl.CustomMachineryAPI").getDeclaredConstructor().newInstance();
        } catch (IllegalAccessException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException var1) {
            throw new IllegalStateException("Couldn't create Custom Machinery API instance", var1);
        }
    });

    String modid();

    ResourceLocation rl(String var1);

    Logger logger();

    ICMConfig config();

    Registrar<MachineComponentType<?>> componentRegistrar();

    Registrar<GuiElementType<?>> guiElementRegistrar();

    Registrar<RequirementType<?>> requirementRegistrar();

    Registrar<MachineAppearanceProperty<?>> appearancePropertyRegistrar();

    Registrar<DataType<?, ?>> dataRegistrar();

    Registrar<ProcessorType<?>> processorRegistrar();

    <T> Registrar<T> registrar(ResourceKey<Registry<T>> var1);

    @Nullable
    <C extends IMachineComponent> NamedCodec<IComponentVariant> getVariantCodec(MachineComponentType<C> var1, ResourceLocation var2);
}
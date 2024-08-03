package io.github.edwinmindcraft.origins.common.registry;

import io.github.apace100.origins.command.LayerArgumentType;
import io.github.apace100.origins.command.OriginArgumentType;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraftforge.registries.RegistryObject;

public class OriginArgumentTypes {

    public static final RegistryObject<ArgumentTypeInfo<OriginArgumentType, ?>> ORIGIN = OriginRegisters.ARGUMENT_TYPES.register("origin", () -> SingletonArgumentInfo.contextFree(OriginArgumentType::origin));

    public static final RegistryObject<ArgumentTypeInfo<LayerArgumentType, ?>> LAYER = OriginRegisters.ARGUMENT_TYPES.register("layer", () -> SingletonArgumentInfo.contextFree(LayerArgumentType::layer));

    public static void bootstrap() {
    }

    public static void initialize() {
        ArgumentTypeInfos.registerByClass(OriginArgumentType.class, ORIGIN.get());
        ArgumentTypeInfos.registerByClass(LayerArgumentType.class, LAYER.get());
    }
}
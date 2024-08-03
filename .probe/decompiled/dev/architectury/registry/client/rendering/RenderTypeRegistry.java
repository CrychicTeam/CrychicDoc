package dev.architectury.registry.client.rendering;

import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.injectables.annotations.ExpectPlatform.Transformed;
import dev.architectury.registry.client.rendering.forge.RenderTypeRegistryImpl;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public final class RenderTypeRegistry {

    private RenderTypeRegistry() {
    }

    @ExpectPlatform
    @Transformed
    public static void register(RenderType type, Block... blocks) {
        RenderTypeRegistryImpl.register(type, blocks);
    }

    @ExpectPlatform
    @Transformed
    public static void register(RenderType type, Fluid... fluids) {
        RenderTypeRegistryImpl.register(type, fluids);
    }
}
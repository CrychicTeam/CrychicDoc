package dev.ftb.mods.ftblibrary.util.client.forge;

import dev.architectury.fluid.FluidStack;
import dev.architectury.hooks.fluid.FluidStackHooks;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;

public class ClientUtilsImpl {

    public static ResourceLocation getStillTexture(FluidStack stack) {
        net.minecraftforge.fluids.FluidStack forgeStack = new net.minecraftforge.fluids.FluidStack(stack.getFluid(), (int) stack.getAmount(), stack.getTag());
        TextureAtlasSprite stillTexture = FluidStackHooks.getStillTexture(forgeStack.getFluid());
        return stillTexture == null ? null : stillTexture.contents().name();
    }

    public static int getFluidColor(FluidStack stack) {
        net.minecraftforge.fluids.FluidStack forgeStack = new net.minecraftforge.fluids.FluidStack(stack.getFluid(), (int) stack.getAmount(), stack.getTag());
        return FluidStackHooks.getColor(forgeStack.getFluid());
    }
}
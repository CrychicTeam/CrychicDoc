package dev.architectury.hooks.fluid.forge;

import dev.architectury.fluid.FluidStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import org.jetbrains.annotations.Nullable;

public class FluidStackHooksImpl {

    public static Component getName(FluidStack stack) {
        return stack.getFluid().getFluidType().getDescription(FluidStackHooksForge.toForge(stack));
    }

    public static String getTranslationKey(FluidStack stack) {
        return stack.getFluid().getFluidType().getDescriptionId(FluidStackHooksForge.toForge(stack));
    }

    public static FluidStack read(FriendlyByteBuf buf) {
        return FluidStackHooksForge.fromForge(net.minecraftforge.fluids.FluidStack.readFromPacket(buf));
    }

    public static void write(FluidStack stack, FriendlyByteBuf buf) {
        FluidStackHooksForge.toForge(stack).writeToPacket(buf);
    }

    public static FluidStack read(CompoundTag tag) {
        return FluidStackHooksForge.fromForge(net.minecraftforge.fluids.FluidStack.loadFluidStackFromNBT(tag));
    }

    public static CompoundTag write(FluidStack stack, CompoundTag tag) {
        return FluidStackHooksForge.toForge(stack).writeToNBT(tag);
    }

    public static long bucketAmount() {
        return 1000L;
    }

    @OnlyIn(Dist.CLIENT)
    @Nullable
    public static TextureAtlasSprite getStillTexture(@Nullable BlockAndTintGetter level, @Nullable BlockPos pos, FluidState state) {
        if (state.getType() == Fluids.EMPTY) {
            return null;
        } else {
            ResourceLocation texture = IClientFluidTypeExtensions.of(state).getStillTexture(state, level, pos);
            return (TextureAtlasSprite) Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(texture);
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Nullable
    public static TextureAtlasSprite getStillTexture(FluidStack stack) {
        if (stack.getFluid() == Fluids.EMPTY) {
            return null;
        } else {
            ResourceLocation texture = IClientFluidTypeExtensions.of(stack.getFluid()).getStillTexture(FluidStackHooksForge.toForge(stack));
            return (TextureAtlasSprite) Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(texture);
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Nullable
    public static TextureAtlasSprite getStillTexture(Fluid fluid) {
        if (fluid == Fluids.EMPTY) {
            return null;
        } else {
            ResourceLocation texture = IClientFluidTypeExtensions.of(fluid).getStillTexture();
            return (TextureAtlasSprite) Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(texture);
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Nullable
    public static TextureAtlasSprite getFlowingTexture(@Nullable BlockAndTintGetter level, @Nullable BlockPos pos, FluidState state) {
        if (state.getType() == Fluids.EMPTY) {
            return null;
        } else {
            ResourceLocation texture = IClientFluidTypeExtensions.of(state).getFlowingTexture(state, level, pos);
            return (TextureAtlasSprite) Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(texture);
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Nullable
    public static TextureAtlasSprite getFlowingTexture(FluidStack stack) {
        if (stack.getFluid() == Fluids.EMPTY) {
            return null;
        } else {
            ResourceLocation texture = IClientFluidTypeExtensions.of(stack.getFluid()).getFlowingTexture(FluidStackHooksForge.toForge(stack));
            return (TextureAtlasSprite) Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(texture);
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Nullable
    public static TextureAtlasSprite getFlowingTexture(Fluid fluid) {
        if (fluid == Fluids.EMPTY) {
            return null;
        } else {
            ResourceLocation texture = IClientFluidTypeExtensions.of(fluid).getFlowingTexture();
            return (TextureAtlasSprite) Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(texture);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static int getColor(@Nullable BlockAndTintGetter level, @Nullable BlockPos pos, FluidState state) {
        return state.getType() == Fluids.EMPTY ? -1 : IClientFluidTypeExtensions.of(state).getTintColor(state, level, pos);
    }

    @OnlyIn(Dist.CLIENT)
    public static int getColor(FluidStack stack) {
        return stack.getFluid() == Fluids.EMPTY ? -1 : IClientFluidTypeExtensions.of(stack.getFluid()).getTintColor(FluidStackHooksForge.toForge(stack));
    }

    @OnlyIn(Dist.CLIENT)
    public static int getColor(Fluid fluid) {
        return fluid == Fluids.EMPTY ? -1 : IClientFluidTypeExtensions.of(fluid).getTintColor();
    }

    public static int getLuminosity(FluidStack fluid, @Nullable Level level, @Nullable BlockPos pos) {
        return fluid.getFluid().getFluidType().getLightLevel(FluidStackHooksForge.toForge(fluid));
    }

    @Deprecated(forRemoval = true)
    public static int getLuminosity(Fluid fluid, @Nullable Level level, @Nullable BlockPos pos) {
        if (level != null && pos != null) {
            FluidState state = level.getFluidState(pos);
            return fluid.getFluidType().getLightLevel(state, level, pos);
        } else {
            return fluid.getFluidType().getLightLevel();
        }
    }

    public static int getTemperature(FluidStack fluid, @Nullable Level level, @Nullable BlockPos pos) {
        return fluid.getFluid().getFluidType().getTemperature(FluidStackHooksForge.toForge(fluid));
    }

    public static int getTemperature(Fluid fluid, @Nullable Level level, @Nullable BlockPos pos) {
        if (level != null && pos != null) {
            FluidState state = level.getFluidState(pos);
            return fluid.getFluidType().getTemperature(state, level, pos);
        } else {
            return fluid.getFluidType().getTemperature();
        }
    }

    public static int getViscosity(FluidStack fluid, @Nullable Level level, @Nullable BlockPos pos) {
        return fluid.getFluid().getFluidType().getViscosity(FluidStackHooksForge.toForge(fluid));
    }

    public static int getViscosity(Fluid fluid, @Nullable Level level, @Nullable BlockPos pos) {
        if (level != null && pos != null) {
            FluidState state = level.getFluidState(pos);
            return fluid.getFluidType().getViscosity(state, level, pos);
        } else {
            return fluid.getFluidType().getViscosity();
        }
    }
}
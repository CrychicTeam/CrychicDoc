package net.mehvahdjukaar.supplementaries.reg.forge;

import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.mehvahdjukaar.supplementaries.Supplementaries;
import net.mehvahdjukaar.supplementaries.common.fluids.FiniteFluid;
import net.mehvahdjukaar.supplementaries.common.items.forge.FiniteFluidBucket;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.common.SoundActions;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class ModFluidsImpl {

    public static final Supplier<FluidType> LUMISENE_FLUID_TYPE = registerFluidType("lumisene", () -> new FluidType(FluidType.Properties.create().descriptionId("block.supplementaries.lumisene").fallDistanceModifier(1.0F).canExtinguish(false).motionScale(0.0).supportsBoating(true).sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL).sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY).sound(SoundActions.FLUID_VAPORIZE, SoundEvents.FIRE_EXTINGUISH).density(3000).viscosity(6000)) {

        @Nullable
        @Override
        public BlockPathTypes getBlockPathType(FluidState state, BlockGetter level, BlockPos pos, @Nullable Mob mob, boolean canFluidLog) {
            return canFluidLog ? super.getBlockPathType(state, level, pos, mob, true) : null;
        }

        @Override
        public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
            consumer.accept(new IClientFluidTypeExtensions() {

                private static final ResourceLocation UNDERWATER_TEXTURE = Supplementaries.res("textures/block/lumisene_underwater.png");

                private static final ResourceLocation STILL_TEXTURE = new ResourceLocation("block/water_still");

                private static final ResourceLocation FLOWING_TEXTURE = new ResourceLocation("block/water_flow");

                @Override
                public ResourceLocation getStillTexture() {
                    return STILL_TEXTURE;
                }

                @Override
                public ResourceLocation getStillTexture(FluidState state, BlockAndTintGetter getter, BlockPos pos) {
                    return IClientFluidTypeExtensions.super.getStillTexture(state, getter, pos);
                }

                @Override
                public ResourceLocation getFlowingTexture() {
                    return FLOWING_TEXTURE;
                }

                @Override
                public ResourceLocation getRenderOverlayTexture(Minecraft mc) {
                    return UNDERWATER_TEXTURE;
                }

                @Override
                public Vector3f modifyFogColor(Camera camera, float partialTick, ClientLevel level, int renderDistance, float darkenWorldAmount, Vector3f fluidFogColor) {
                    return new Vector3f(1.0F, 0.8F, 0.01F);
                }

                @Override
                public void modifyFogRender(Camera camera, FogRenderer.FogMode mode, float renderDistance, float partialTick, float nearDistance, float farDistance, FogShape shape) {
                    RenderSystem.setShaderFogStart(0.1F);
                    RenderSystem.setShaderFogEnd(8.0F);
                }

                @Override
                public int getTintColor(FluidState state, BlockAndTintGetter getter, BlockPos pos) {
                    int x = pos.m_123341_();
                    int y = pos.m_123343_();
                    double frequency = 0.1;
                    double phaseShift = 0.0;
                    double amplitude = 127.0;
                    double center = 128.0;
                    int r = (int) (Math.sin(frequency * (double) x + phaseShift) * amplitude + center);
                    int g = (int) (Math.sin(frequency * (double) y + phaseShift) * amplitude + center);
                    int b = (int) (Math.sin(frequency * Math.sqrt((double) (x * x + y * y)) + phaseShift) * amplitude + center);
                    return FastColor.ARGB32.color(255, r, g, b);
                }
            });
        }
    });

    public static BucketItem createLumiseneBucket() {
        return new FiniteFluidBucket(null, new Item.Properties().stacksTo(1).craftRemainder(Items.BUCKET));
    }

    public static FiniteFluid createLumisene() {
        return new ModFluidsImpl.LumiseneFluid();
    }

    public static Supplier<FluidType> registerFluidType(String name, Supplier<FluidType> fluidSupplier) {
        return RegHelper.register(Supplementaries.res(name), fluidSupplier, ForgeRegistries.Keys.FLUID_TYPES);
    }

    public static void messWithFluidH(BlockAndTintGetter level, Fluid fluid, BlockPos pos, BlockState blockState, FluidState fluidState, CallbackInfoReturnable<Float> cir) {
    }

    public static void messWithAvH(BlockAndTintGetter level, Fluid fluid, float g, float h, float i, BlockPos pos, CallbackInfoReturnable<Float> cir) {
    }

    public static class LumiseneFluid extends FiniteFluid {

        public LumiseneFluid() {
            super(16, null, null);
        }

        public FluidType getFluidType() {
            return (FluidType) ModFluidsImpl.LUMISENE_FLUID_TYPE.get();
        }
    }
}
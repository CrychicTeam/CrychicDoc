package com.simibubi.create;

import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;
import com.simibubi.create.content.decoration.palettes.AllPaletteStoneTypes;
import com.simibubi.create.content.fluids.VirtualFluid;
import com.simibubi.create.content.fluids.potion.PotionFluid;
import com.simibubi.create.foundation.utility.Color;
import com.simibubi.create.infrastructure.config.AllConfigs;
import com.tterrag.registrate.builders.FluidBuilder;
import com.tterrag.registrate.builders.FluidBuilder.FluidTypeFactory;
import com.tterrag.registrate.util.entry.FluidEntry;
import java.util.function.Consumer;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.fluids.FluidInteractionRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

public class AllFluids {

    public static final FluidEntry<PotionFluid> POTION = Create.REGISTRATE.virtualFluid("potion", PotionFluid.PotionFluidType::new, PotionFluid::new).lang("Potion").register();

    public static final FluidEntry<VirtualFluid> TEA = Create.REGISTRATE.virtualFluid("tea").lang("Builder's Tea").tag(new TagKey[] { AllTags.forgeFluidTag("tea") }).register();

    public static final FluidEntry<ForgeFlowingFluid.Flowing> HONEY = ((FluidBuilder) Create.REGISTRATE.standardFluid("honey", AllFluids.SolidRenderedPlaceableFluidType.create(15380015, () -> 0.125F * AllConfigs.client().honeyTransparencyMultiplier.getF())).lang("Honey").properties(b -> b.viscosity(2000).density(1400)).fluidProperties(p -> p.levelDecreasePerBlock(2).tickRate(25).slopeFindDistance(3).explosionResistance(100.0F)).tag(new TagKey[] { AllTags.AllFluidTags.HONEY.tag }).source(ForgeFlowingFluid.Source::new).bucket().tag(new TagKey[] { AllTags.forgeItemTag("buckets/honey") }).build()).register();

    public static final FluidEntry<ForgeFlowingFluid.Flowing> CHOCOLATE = Create.REGISTRATE.standardFluid("chocolate", AllFluids.SolidRenderedPlaceableFluidType.create(6430752, () -> 0.03125F * AllConfigs.client().chocolateTransparencyMultiplier.getF())).lang("Chocolate").tag(new TagKey[] { AllTags.forgeFluidTag("chocolate") }).properties(b -> b.viscosity(1500).density(1400)).fluidProperties(p -> p.levelDecreasePerBlock(2).tickRate(25).slopeFindDistance(3).explosionResistance(100.0F)).register();

    public static void register() {
    }

    public static void registerFluidInteractions() {
        FluidInteractionRegistry.addInteraction(ForgeMod.LAVA_TYPE.get(), new FluidInteractionRegistry.InteractionInformation(((ForgeFlowingFluid.Flowing) HONEY.get()).getFluidType(), fluidState -> fluidState.isSource() ? Blocks.OBSIDIAN.defaultBlockState() : ((Block) AllPaletteStoneTypes.LIMESTONE.getBaseBlock().get()).defaultBlockState()));
        FluidInteractionRegistry.addInteraction(ForgeMod.LAVA_TYPE.get(), new FluidInteractionRegistry.InteractionInformation(((ForgeFlowingFluid.Flowing) CHOCOLATE.get()).getFluidType(), fluidState -> fluidState.isSource() ? Blocks.OBSIDIAN.defaultBlockState() : ((Block) AllPaletteStoneTypes.SCORIA.getBaseBlock().get()).defaultBlockState()));
    }

    @Nullable
    public static BlockState getLavaInteraction(FluidState fluidState) {
        Fluid fluid = fluidState.getType();
        if (fluid.isSame((Fluid) HONEY.get())) {
            return ((Block) AllPaletteStoneTypes.LIMESTONE.getBaseBlock().get()).defaultBlockState();
        } else {
            return fluid.isSame((Fluid) CHOCOLATE.get()) ? ((Block) AllPaletteStoneTypes.SCORIA.getBaseBlock().get()).defaultBlockState() : null;
        }
    }

    private static class SolidRenderedPlaceableFluidType extends AllFluids.TintedFluidType {

        private Vector3f fogColor;

        private Supplier<Float> fogDistance;

        public static FluidTypeFactory create(int fogColor, Supplier<Float> fogDistance) {
            return (p, s, f) -> {
                AllFluids.SolidRenderedPlaceableFluidType fluidType = new AllFluids.SolidRenderedPlaceableFluidType(p, s, f);
                fluidType.fogColor = new Color(fogColor, false).asVectorF();
                fluidType.fogDistance = fogDistance;
                return fluidType;
            };
        }

        private SolidRenderedPlaceableFluidType(FluidType.Properties properties, ResourceLocation stillTexture, ResourceLocation flowingTexture) {
            super(properties, stillTexture, flowingTexture);
        }

        @Override
        protected int getTintColor(FluidStack stack) {
            return -1;
        }

        @Override
        public int getTintColor(FluidState state, BlockAndTintGetter world, BlockPos pos) {
            return 16777215;
        }

        @Override
        protected Vector3f getCustomFogColor() {
            return this.fogColor;
        }

        @Override
        protected float getFogDistanceModifier() {
            return (Float) this.fogDistance.get();
        }
    }

    public abstract static class TintedFluidType extends FluidType {

        protected static final int NO_TINT = -1;

        private ResourceLocation stillTexture;

        private ResourceLocation flowingTexture;

        public TintedFluidType(FluidType.Properties properties, ResourceLocation stillTexture, ResourceLocation flowingTexture) {
            super(properties);
            this.stillTexture = stillTexture;
            this.flowingTexture = flowingTexture;
        }

        @Override
        public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
            consumer.accept(new IClientFluidTypeExtensions() {

                @Override
                public ResourceLocation getStillTexture() {
                    return TintedFluidType.this.stillTexture;
                }

                @Override
                public ResourceLocation getFlowingTexture() {
                    return TintedFluidType.this.flowingTexture;
                }

                @Override
                public int getTintColor(FluidStack stack) {
                    return TintedFluidType.this.getTintColor(stack);
                }

                @Override
                public int getTintColor(FluidState state, BlockAndTintGetter getter, BlockPos pos) {
                    return TintedFluidType.this.getTintColor(state, getter, pos);
                }

                @NotNull
                @Override
                public Vector3f modifyFogColor(Camera camera, float partialTick, ClientLevel level, int renderDistance, float darkenWorldAmount, Vector3f fluidFogColor) {
                    Vector3f customFogColor = TintedFluidType.this.getCustomFogColor();
                    return customFogColor == null ? fluidFogColor : customFogColor;
                }

                @Override
                public void modifyFogRender(Camera camera, FogRenderer.FogMode mode, float renderDistance, float partialTick, float nearDistance, float farDistance, FogShape shape) {
                    float modifier = TintedFluidType.this.getFogDistanceModifier();
                    float baseWaterFog = 96.0F;
                    if (modifier != 1.0F) {
                        RenderSystem.setShaderFogShape(FogShape.CYLINDER);
                        RenderSystem.setShaderFogStart(-8.0F);
                        RenderSystem.setShaderFogEnd(baseWaterFog * modifier);
                    }
                }
            });
        }

        protected abstract int getTintColor(FluidStack var1);

        protected abstract int getTintColor(FluidState var1, BlockAndTintGetter var2, BlockPos var3);

        protected Vector3f getCustomFogColor() {
            return null;
        }

        protected float getFogDistanceModifier() {
            return 1.0F;
        }
    }
}
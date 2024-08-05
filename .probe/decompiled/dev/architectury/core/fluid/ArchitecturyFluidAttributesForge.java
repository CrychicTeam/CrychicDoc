package dev.architectury.core.fluid;

import com.google.common.base.MoreObjects;
import dev.architectury.hooks.fluid.forge.FluidStackHooksForge;
import java.util.function.Consumer;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.common.SoundAction;
import net.minecraftforge.common.SoundActions;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

class ArchitecturyFluidAttributesForge extends FluidType {

    private final ArchitecturyFluidAttributes attributes;

    private final String defaultTranslationKey;

    public ArchitecturyFluidAttributesForge(FluidType.Properties builder, Fluid fluid, ArchitecturyFluidAttributes attributes) {
        super(addArchIntoBuilder(builder, attributes));
        this.attributes = attributes;
        this.defaultTranslationKey = Util.makeDescriptionId("fluid", ForgeRegistries.FLUIDS.getKey(fluid));
    }

    private static FluidType.Properties addArchIntoBuilder(FluidType.Properties builder, ArchitecturyFluidAttributes attributes) {
        builder.lightLevel(attributes.getLuminosity()).density(attributes.getDensity()).temperature(attributes.getTemperature()).rarity(attributes.getRarity()).canConvertToSource(attributes.canConvertToSource()).viscosity(attributes.getViscosity());
        return builder;
    }

    @Override
    public ItemStack getBucket(FluidStack stack) {
        Item item = this.attributes.getBucketItem();
        return item == null ? super.getBucket(stack) : new ItemStack(item);
    }

    @Override
    public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
        consumer.accept(new IClientFluidTypeExtensions() {

            @Override
            public int getTintColor() {
                return ArchitecturyFluidAttributesForge.this.attributes.getColor();
            }

            @Override
            public ResourceLocation getStillTexture() {
                return ArchitecturyFluidAttributesForge.this.attributes.getSourceTexture();
            }

            @Override
            public ResourceLocation getFlowingTexture() {
                return ArchitecturyFluidAttributesForge.this.attributes.getFlowingTexture();
            }

            @Nullable
            @Override
            public ResourceLocation getOverlayTexture() {
                return ArchitecturyFluidAttributesForge.this.attributes.getOverlayTexture();
            }

            @Override
            public ResourceLocation getStillTexture(FluidState state, BlockAndTintGetter getter, BlockPos pos) {
                return ArchitecturyFluidAttributesForge.this.attributes.getSourceTexture(state, getter, pos);
            }

            @Override
            public ResourceLocation getFlowingTexture(FluidState state, BlockAndTintGetter getter, BlockPos pos) {
                return ArchitecturyFluidAttributesForge.this.attributes.getFlowingTexture(state, getter, pos);
            }

            @Nullable
            @Override
            public ResourceLocation getOverlayTexture(FluidState state, BlockAndTintGetter getter, BlockPos pos) {
                return ArchitecturyFluidAttributesForge.this.attributes.getOverlayTexture(state, getter, pos);
            }

            @Override
            public int getTintColor(FluidState state, BlockAndTintGetter getter, BlockPos pos) {
                return ArchitecturyFluidAttributesForge.this.attributes.getColor(state, getter, pos);
            }

            @Override
            public int getTintColor(FluidStack stack) {
                return ArchitecturyFluidAttributesForge.this.attributes.getColor(ArchitecturyFluidAttributesForge.this.convertSafe(stack));
            }

            @Override
            public ResourceLocation getStillTexture(FluidStack stack) {
                return ArchitecturyFluidAttributesForge.this.attributes.getSourceTexture(ArchitecturyFluidAttributesForge.this.convertSafe(stack));
            }

            @Override
            public ResourceLocation getFlowingTexture(FluidStack stack) {
                return ArchitecturyFluidAttributesForge.this.attributes.getFlowingTexture(ArchitecturyFluidAttributesForge.this.convertSafe(stack));
            }

            @Nullable
            @Override
            public ResourceLocation getOverlayTexture(FluidStack stack) {
                return ArchitecturyFluidAttributesForge.this.attributes.getOverlayTexture(ArchitecturyFluidAttributesForge.this.convertSafe(stack));
            }
        });
    }

    @Override
    public int getLightLevel(FluidStack stack) {
        return this.attributes.getLuminosity(this.convertSafe(stack));
    }

    @Override
    public int getLightLevel(FluidState state, BlockAndTintGetter level, BlockPos pos) {
        return this.attributes.getLuminosity(this.convertSafe(state), level, pos);
    }

    @Override
    public int getDensity(FluidStack stack) {
        return this.attributes.getDensity(this.convertSafe(stack));
    }

    @Override
    public int getDensity(FluidState state, BlockAndTintGetter level, BlockPos pos) {
        return this.attributes.getDensity(this.convertSafe(state), level, pos);
    }

    @Override
    public int getTemperature(FluidStack stack) {
        return this.attributes.getTemperature(this.convertSafe(stack));
    }

    @Override
    public int getTemperature(FluidState state, BlockAndTintGetter level, BlockPos pos) {
        return this.attributes.getTemperature(this.convertSafe(state), level, pos);
    }

    @Override
    public int getViscosity(FluidStack stack) {
        return this.attributes.getViscosity(this.convertSafe(stack));
    }

    @Override
    public int getViscosity(FluidState state, BlockAndTintGetter level, BlockPos pos) {
        return this.attributes.getViscosity(this.convertSafe(state), level, pos);
    }

    @Override
    public Rarity getRarity() {
        return this.attributes.getRarity();
    }

    @Override
    public Rarity getRarity(FluidStack stack) {
        return this.attributes.getRarity(this.convertSafe(stack));
    }

    @Override
    public Component getDescription() {
        return this.attributes.getName();
    }

    @Override
    public Component getDescription(FluidStack stack) {
        return this.attributes.getName(this.convertSafe(stack));
    }

    @Override
    public String getDescriptionId() {
        return (String) MoreObjects.firstNonNull(this.attributes.getTranslationKey(), this.defaultTranslationKey);
    }

    @Override
    public String getDescriptionId(FluidStack stack) {
        return (String) MoreObjects.firstNonNull(this.attributes.getTranslationKey(this.convertSafe(stack)), this.defaultTranslationKey);
    }

    @Nullable
    @Override
    public SoundEvent getSound(SoundAction action) {
        return this.getSound((FluidStack) null, action);
    }

    @Nullable
    @Override
    public SoundEvent getSound(@Nullable FluidStack stack, SoundAction action) {
        dev.architectury.fluid.FluidStack archStack = this.convertSafe(stack);
        if (SoundActions.BUCKET_FILL.equals(action)) {
            return this.attributes.getFillSound(archStack);
        } else {
            return SoundActions.BUCKET_EMPTY.equals(action) ? this.attributes.getEmptySound(archStack) : null;
        }
    }

    @Nullable
    @Override
    public SoundEvent getSound(@Nullable Player player, BlockGetter getter, BlockPos pos, SoundAction action) {
        if (getter instanceof BlockAndTintGetter level) {
            if (SoundActions.BUCKET_FILL.equals(action)) {
                return this.attributes.getFillSound(null, level, pos);
            }
            if (SoundActions.BUCKET_EMPTY.equals(action)) {
                return this.attributes.getEmptySound(null, level, pos);
            }
        }
        return this.getSound((FluidStack) null, action);
    }

    @Override
    public boolean canConvertToSource(FluidStack stack) {
        return this.attributes.canConvertToSource();
    }

    @Override
    public boolean canConvertToSource(FluidState state, LevelReader reader, BlockPos pos) {
        return this.attributes.canConvertToSource();
    }

    @Nullable
    public dev.architectury.fluid.FluidStack convertSafe(@Nullable FluidStack stack) {
        return stack == null ? null : FluidStackHooksForge.fromForge(stack);
    }

    @Nullable
    public dev.architectury.fluid.FluidStack convertSafe(@Nullable FluidState state) {
        return state == null ? null : dev.architectury.fluid.FluidStack.create(state.getType(), dev.architectury.fluid.FluidStack.bucketAmount());
    }
}
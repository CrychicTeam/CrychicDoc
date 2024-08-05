package io.redspace.ironsspellbooks.render;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemOverride;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class NBTOverrideItemModel implements BakedModel {

    private final BakedModel original;

    private final ItemOverrides itemOverrides;

    public NBTOverrideItemModel(BakedModel original, ModelBakery loader) {
        this.original = original;
        BlockModel missing = (BlockModel) loader.getModel(ModelBakery.MISSING_MODEL_LOCATION);
        this.itemOverrides = new ItemOverrides(new ModelBaker() {

            public Function<Material, TextureAtlasSprite> getModelTextureGetter() {
                return null;
            }

            public BakedModel bake(ResourceLocation location, ModelState state, Function<Material, TextureAtlasSprite> sprites) {
                return null;
            }

            @Override
            public UnbakedModel getModel(ResourceLocation resourceLocation) {
                return null;
            }

            @Nullable
            @Override
            public BakedModel bake(ResourceLocation resourceLocation, ModelState modelState) {
                return null;
            }
        }, missing, Collections.emptyList()) {

            @Override
            public BakedModel resolve(@NotNull BakedModel original, @NotNull ItemStack itemStack, @Nullable ClientLevel level, @Nullable LivingEntity livingEntity, int seed) {
                if (itemStack.hasTag()) {
                    Optional<ResourceLocation> override = NBTOverrideItemModel.this.getModelFromTag(itemStack, itemStack.getTag());
                    if (override.isPresent()) {
                        ModelManager manager = Minecraft.getInstance().getModelManager();
                        BakedModel missing = manager.getModel(ModelBakery.MISSING_MODEL_LOCATION);
                        BakedModel model = manager.getModel((ResourceLocation) override.get());
                        return model == missing ? original : model;
                    }
                }
                return original;
            }
        };
    }

    abstract Optional<ResourceLocation> getModelFromTag(ItemStack var1, CompoundTag var2);

    @NotNull
    @Override
    public ItemOverrides getOverrides() {
        return this.itemOverrides;
    }

    @NotNull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull RandomSource rand) {
        return this.original.getQuads(state, side, rand);
    }

    @Override
    public boolean useAmbientOcclusion() {
        return this.original.useAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
        return this.original.isGui3d();
    }

    @Override
    public boolean usesBlockLight() {
        return this.original.usesBlockLight();
    }

    @Override
    public boolean isCustomRenderer() {
        return this.original.isCustomRenderer();
    }

    @NotNull
    @Override
    public TextureAtlasSprite getParticleIcon() {
        return this.original.getParticleIcon();
    }

    @Override
    public ItemTransforms getTransforms() {
        return this.original.getTransforms();
    }
}
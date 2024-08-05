package org.violetmoon.quark.addons.oddities.client.model;

import java.util.List;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.quark.addons.oddities.block.TinyPotatoBlock;
import org.violetmoon.quark.addons.oddities.client.render.be.TinyPotatoRenderer;

public record TinyPotatoModel(BakedModel originalModel) implements BakedModel {

    @Override
    public boolean useAmbientOcclusion() {
        return this.originalModel.useAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
        return this.originalModel.isGui3d();
    }

    @Override
    public boolean usesBlockLight() {
        return this.originalModel.usesBlockLight();
    }

    @Override
    public boolean isCustomRenderer() {
        return this.originalModel.isCustomRenderer();
    }

    @NotNull
    @Override
    public TextureAtlasSprite getParticleIcon() {
        return this.originalModel.getParticleIcon();
    }

    @NotNull
    @Override
    public ItemTransforms getTransforms() {
        return this.originalModel.getTransforms();
    }

    @NotNull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull RandomSource rand) {
        return List.of();
    }

    @NotNull
    @Override
    public ItemOverrides getOverrides() {
        return new ItemOverrides() {

            @Override
            public BakedModel resolve(@NotNull BakedModel model, @NotNull ItemStack stack, @Nullable ClientLevel world, @Nullable LivingEntity livingEntity, int seed) {
                return !stack.hasCustomHoverName() && !TinyPotatoBlock.isAngry(stack) ? TinyPotatoModel.this.originalModel : TinyPotatoRenderer.getModelFromDisplayName(stack.getHoverName(), TinyPotatoBlock.isAngry(stack));
            }
        };
    }
}
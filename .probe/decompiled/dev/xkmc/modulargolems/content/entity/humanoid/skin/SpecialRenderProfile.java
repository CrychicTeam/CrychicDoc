package dev.xkmc.modulargolems.content.entity.humanoid.skin;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.xkmc.modulargolems.content.entity.humanoid.HumanoidGolemEntity;
import javax.annotation.Nullable;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;

public record SpecialRenderProfile(boolean slim, @Nullable ResourceLocation texture) implements SpecialRenderSkin {

    @Override
    public void render(HumanoidGolemEntity entity, float f1, float f2, PoseStack stack, MultiBufferSource source, int i) {
        if (this.slim() && PlayerSkinRenderer.SLIM != null) {
            PlayerSkinRenderer.SLIM.render(entity, f1, f2, stack, source, i);
        }
        if (!this.slim() && PlayerSkinRenderer.REGULAR != null) {
            PlayerSkinRenderer.REGULAR.render(entity, f1, f2, stack, source, i);
        }
    }
}
package dev.xkmc.modulargolems.content.entity.humanoid.skin;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.xkmc.modulargolems.content.entity.humanoid.HumanoidGolemEntity;
import net.minecraft.client.renderer.MultiBufferSource;

public interface SpecialRenderSkin {

    void render(HumanoidGolemEntity var1, float var2, float var3, PoseStack var4, MultiBufferSource var5, int var6);
}
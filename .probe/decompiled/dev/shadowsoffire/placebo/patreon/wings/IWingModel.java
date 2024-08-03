package dev.shadowsoffire.placebo.patreon.wings;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;

public interface IWingModel {

    void render(PoseStack var1, MultiBufferSource var2, int var3, AbstractClientPlayer var4, float var5, ResourceLocation var6, PlayerModel<AbstractClientPlayer> var7);
}
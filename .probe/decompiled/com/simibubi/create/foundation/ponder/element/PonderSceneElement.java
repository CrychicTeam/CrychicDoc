package com.simibubi.create.foundation.ponder.element;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.ponder.PonderWorld;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;

public abstract class PonderSceneElement extends PonderElement {

    public abstract void renderFirst(PonderWorld var1, MultiBufferSource var2, PoseStack var3, float var4);

    public abstract void renderLayer(PonderWorld var1, MultiBufferSource var2, RenderType var3, PoseStack var4, float var5);

    public abstract void renderLast(PonderWorld var1, MultiBufferSource var2, PoseStack var3, float var4);
}
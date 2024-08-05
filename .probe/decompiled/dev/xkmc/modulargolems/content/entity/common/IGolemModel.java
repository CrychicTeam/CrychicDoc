package dev.xkmc.modulargolems.content.entity.common;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.xkmc.l2serial.util.Wrappers;
import dev.xkmc.modulargolems.content.core.IGolemPart;
import net.minecraft.client.model.EntityModel;
import net.minecraft.resources.ResourceLocation;

public interface IGolemModel<T extends AbstractGolemEntity<T, P>, P extends IGolemPart<P>, M extends EntityModel<T> & IGolemModel<T, P, M>> {

    default M getThis() {
        return (M) Wrappers.cast(this);
    }

    void renderToBufferInternal(P var1, PoseStack var2, VertexConsumer var3, int var4, int var5, float var6, float var7, float var8, float var9);

    ResourceLocation getTextureLocationInternal(ResourceLocation var1);
}
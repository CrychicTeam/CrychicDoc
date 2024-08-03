package com.simibubi.create.foundation.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.foundation.map.CustomRenderedMapDecoration;
import java.util.Iterator;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.level.saveddata.maps.MapDecoration;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Group;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(targets = { "net.minecraft.client.gui.MapRenderer$MapInstance" })
public class MapRendererMapInstanceMixin {

    @Shadow
    private MapItemSavedData data;

    @Inject(method = { "draw(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ZI)V" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/world/level/saveddata/maps/MapDecoration;render(I)Z", remap = false) }, locals = LocalCapture.CAPTURE_FAILHARD)
    @Group(name = "custom_decoration_rendering", min = 1, max = 1)
    private void create$onDraw(PoseStack poseStack, MultiBufferSource bufferSource, boolean active, int packedLight, CallbackInfo ci, int i, int j, float f, Matrix4f matrix4f, VertexConsumer vertexConsumer, int index, Iterator<MapDecoration> iterator, MapDecoration decoration) {
        if (decoration instanceof CustomRenderedMapDecoration renderer) {
            renderer.render(poseStack, bufferSource, active, packedLight, this.data, index);
        }
    }

    @Inject(method = { "draw(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ZI)V" }, at = { @At(value = "FIELD", target = "net/optifine/reflect/Reflector.ForgeMapDecoration_render:Lnet/optifine/reflect/ReflectorMethod;", opcode = 178, ordinal = 1, remap = false) }, locals = LocalCapture.CAPTURE_FAILHARD)
    @Group(name = "custom_decoration_rendering")
    private void create$onDrawOptifine(PoseStack poseStack, MultiBufferSource bufferSource, boolean active, int packedLight, CallbackInfo ci, int i, int j, float f, Matrix4f matrix4f, VertexConsumer vertexConsumer, int index, Iterator<MapDecoration> iterator, MapDecoration decoration) {
        if (decoration instanceof CustomRenderedMapDecoration renderer) {
            renderer.render(poseStack, bufferSource, active, packedLight, this.data, index);
        }
    }
}
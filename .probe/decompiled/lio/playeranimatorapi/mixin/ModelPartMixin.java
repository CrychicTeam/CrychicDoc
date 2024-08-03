package lio.playeranimatorapi.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import lio.playeranimatorapi.misc.IsVisibleAccessor;
import net.minecraft.client.model.geom.ModelPart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ ModelPart.class })
public abstract class ModelPartMixin implements IsVisibleAccessor {

    @Unique
    public boolean zigysPlayerAnimatorAPI$isVisible = true;

    @Override
    public void zigysPlayerAnimatorAPI$setIsVisible(Boolean value) {
        this.zigysPlayerAnimatorAPI$isVisible = value;
    }

    @Override
    public boolean zigysPlayerAnimatorAPI$getIsVisible() {
        return this.zigysPlayerAnimatorAPI$isVisible;
    }

    @Inject(method = { "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;IIFFFF)V" }, at = { @At("HEAD") })
    private void render(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha, CallbackInfo ci) {
        if (this.zigysPlayerAnimatorAPI$isVisible) {
            ;
        }
    }
}
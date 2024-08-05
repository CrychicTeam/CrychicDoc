package noppes.npcs.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelPart;
import noppes.npcs.ModelPartConfig;
import noppes.npcs.client.ClientProxy;
import noppes.npcs.constants.EnumParts;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ ModelPart.class })
public class ModelRendererMixin {

    public ModelPartConfig cnpcconfig;

    @Inject(at = { @At("HEAD") }, method = { "translateAndRotate" })
    private void translateAndRotatePre(PoseStack mStack, CallbackInfo callbackInfo) {
        this.cnpcconfig = this.getCnpcconfig();
        if (this.cnpcconfig != null) {
            mStack.translate(this.cnpcconfig.transX, this.cnpcconfig.transY, this.cnpcconfig.transZ);
        }
    }

    @Inject(at = { @At("TAIL") }, method = { "translateAndRotate" })
    private void translateAndRotatePost(PoseStack mStack, CallbackInfo callbackInfo) {
        this.cnpcconfig = this.getCnpcconfig();
        if (this.cnpcconfig != null) {
            mStack.scale(this.cnpcconfig.scaleX, this.cnpcconfig.scaleY, this.cnpcconfig.scaleZ);
        }
    }

    private ModelPartConfig getCnpcconfig() {
        if (ClientProxy.data == null) {
            return null;
        } else {
            ModelPart model = (ModelPart) this;
            if (model == ClientProxy.playerModel.f_102810_ || model == ClientProxy.playerModel.jacket || model == ClientProxy.armorLayer.getOuter().body || model == ClientProxy.armorLayer.getInner().body) {
                return ClientProxy.data.getPartConfig(EnumParts.BODY);
            } else if (model == ClientProxy.playerModel.f_102808_ || model == ClientProxy.playerModel.f_102809_ || model == ClientProxy.armorLayer.getOuter().head) {
                return ClientProxy.data.getPartConfig(EnumParts.HEAD);
            } else if (model == ClientProxy.playerModel.f_102814_ || model == ClientProxy.playerModel.leftPants || model == ClientProxy.armorLayer.getOuter().leftLeg || model == ClientProxy.armorLayer.getInner().leftLeg) {
                return ClientProxy.data.getPartConfig(EnumParts.LEG_LEFT);
            } else if (model == ClientProxy.playerModel.f_102813_ || model == ClientProxy.playerModel.rightPants || model == ClientProxy.armorLayer.getOuter().rightLeg || model == ClientProxy.armorLayer.getInner().rightLeg) {
                return ClientProxy.data.getPartConfig(EnumParts.LEG_RIGHT);
            } else if (model == ClientProxy.playerModel.f_102812_ || model == ClientProxy.playerModel.leftSleeve || model == ClientProxy.armorLayer.getOuter().leftArm) {
                return ClientProxy.data.getPartConfig(EnumParts.ARM_LEFT);
            } else {
                return model != ClientProxy.playerModel.f_102811_ && model != ClientProxy.playerModel.rightSleeve && model != ClientProxy.armorLayer.getOuter().rightArm ? null : ClientProxy.data.getPartConfig(EnumParts.ARM_RIGHT);
            }
        }
    }
}
package lio.playeranimatorapi.mixin;

import lio.playeranimatorapi.liolib.ModGeckoLibUtilsClient;
import lio.playeranimatorapi.liolib.PlayerAnimationModel;
import lio.playeranimatorapi.liolib.PlayerAnimationRenderer;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ PlayerModel.class })
public class PlayerModelMixin_LioLibOnly<T extends LivingEntity> {

    @Inject(method = { "setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V" }, at = { @At("TAIL") })
    private void inject(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
        PlayerAnimationRenderer renderer = ModGeckoLibUtilsClient.currentPlayerRenderer;
        if (renderer != null && entity instanceof Player && ((PlayerAnimationModel) renderer.getGeoModel()).allResourcesExist((AbstractClientPlayer) entity)) {
            renderer.setPlayerModel((PlayerModel) this);
            renderer.setupAnim(renderer.getGeoModel().getBakedModel(renderer.getGeoModel().getModelResource((AbstractClientPlayer) entity)));
        }
    }
}
package com.github.alexthe666.citadel.mixin.client;

import com.github.alexthe666.citadel.client.event.EventPosePlayerHand;
import java.util.function.Function;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event.Result;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ HumanoidModel.class })
public abstract class HumanoidModelMixin extends Model {

    public HumanoidModelMixin(Function<ResourceLocation, RenderType> functionResourceLocationRenderType0) {
        super(functionResourceLocationRenderType0);
    }

    @Inject(at = { @At("HEAD") }, remap = true, method = { "Lnet/minecraft/client/model/HumanoidModel;poseRightArm(Lnet/minecraft/world/entity/LivingEntity;)V" }, cancellable = true)
    private void citadel_poseRightArm(LivingEntity entity, CallbackInfo ci) {
        EventPosePlayerHand event = new EventPosePlayerHand(entity, (HumanoidModel) this, false);
        MinecraftForge.EVENT_BUS.post(event);
        if (event.getResult() == Result.ALLOW) {
            ci.cancel();
        }
    }

    @Inject(at = { @At("HEAD") }, remap = true, method = { "Lnet/minecraft/client/model/HumanoidModel;poseLeftArm(Lnet/minecraft/world/entity/LivingEntity;)V" }, cancellable = true)
    private void citadel_poseLeftArm(LivingEntity entity, CallbackInfo ci) {
        EventPosePlayerHand event = new EventPosePlayerHand(entity, (HumanoidModel) this, true);
        MinecraftForge.EVENT_BUS.post(event);
        if (event.getResult() == Result.ALLOW) {
            ci.cancel();
        }
    }
}
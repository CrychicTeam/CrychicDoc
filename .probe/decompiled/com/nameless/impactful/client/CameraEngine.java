package com.nameless.impactful.client;

import com.nameless.impactful.config.ClientConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@OnlyIn(Dist.CLIENT)
public class CameraEngine {

    private static CameraEngine instance;

    private int cameraShakeTime = 0;

    private float cameraShakeStrength = 0.0F;

    private float frequency = 0.0F;

    public static CameraEngine getInstance() {
        return instance;
    }

    public CameraEngine() {
        instance = this;
    }

    public void shakeCamera(int time, float strength, float frequency) {
        if (strength > this.cameraShakeStrength) {
            this.cameraShakeStrength = strength;
            this.cameraShakeTime = time;
            this.frequency = frequency;
        }
    }

    public void shakeCamera(int time, float strength) {
        this.shakeCamera(time, strength, 3.0F);
    }

    public void reset() {
        this.cameraShakeStrength = 0.0F;
        this.frequency = 0.0F;
    }

    @EventBusSubscriber(modid = "impactful", value = { Dist.CLIENT })
    public static class Events {

        @SubscribeEvent
        public static void cameraSetupEvent(ViewportEvent.ComputeCameraAngles event) {
            Player player = Minecraft.getInstance().player;
            if (player != null) {
                if (CameraEngine.instance.cameraShakeTime > 0) {
                    CameraEngine.instance.cameraShakeTime--;
                    float delta = Minecraft.getInstance().getFrameTime();
                    float ticksExistedDelta = (float) player.f_19797_ + delta;
                    float k = CameraEngine.instance.cameraShakeStrength / 4.0F * ClientConfig.SCREEN_SHAKE_AMPLITUDE_MULTIPLY.get().floatValue();
                    float f = CameraEngine.instance.frequency;
                    if (!ClientConfig.DISABLE_SCREEN_SHAKE.get() && !Minecraft.getInstance().isPaused()) {
                        event.setPitch((float) ((double) event.getPitch() + (double) k * Math.cos((double) (ticksExistedDelta * f + 2.0F))));
                        event.setYaw((float) ((double) event.getYaw() + (double) k * Math.cos((double) (ticksExistedDelta * f + 1.0F))));
                        event.setRoll((float) ((double) event.getRoll() + (double) k * Math.cos((double) (ticksExistedDelta * f))));
                    }
                } else if (CameraEngine.instance.cameraShakeStrength != 0.0F) {
                    CameraEngine.instance.reset();
                }
            }
        }
    }
}
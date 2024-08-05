package io.redspace.ironsspellbooks.api.util;

import io.redspace.ironsspellbooks.network.ClientboundSyncCameraShake;
import io.redspace.ironsspellbooks.setup.Messages;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class CameraShakeManager {

    public static final ArrayList<CameraShakeData> cameraShakeData = new ArrayList();

    public static ArrayList<CameraShakeData> clientCameraShakeData = new ArrayList();

    private static final int tickDelay = 5;

    private static final int fadeoutDuration = 20;

    private static final float fadeoutMultiplier = 0.05F;

    @SubscribeEvent
    public static void serverTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.START && !cameraShakeData.isEmpty()) {
            int ticks = event.getServer().getTickCount();
            if (ticks % 5 == 0) {
                ArrayList<CameraShakeData> complete = new ArrayList();
                for (CameraShakeData data : cameraShakeData) {
                    data.tickCount += 5;
                    if (data.tickCount >= data.duration) {
                        complete.add(data);
                    }
                }
                if (!complete.isEmpty()) {
                    cameraShakeData.removeAll(complete);
                    doSync();
                }
            }
        }
    }

    public static void addCameraShake(CameraShakeData data) {
        cameraShakeData.add(data);
        doSync();
    }

    public static void removeCameraShake(CameraShakeData data) {
        if (cameraShakeData.remove(data)) {
            doSync();
        }
    }

    private static void doSync() {
        Messages.sendToAllPlayers(new ClientboundSyncCameraShake(cameraShakeData));
    }

    public static void doSync(ServerPlayer player) {
        Messages.sendToPlayer(new ClientboundSyncCameraShake(cameraShakeData), player);
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void handleCameraShake(ViewportEvent.ComputeCameraAngles event) {
        if (!clientCameraShakeData.isEmpty()) {
            Entity player = event.getCamera().getEntity();
            List<CameraShakeData> closestCameraShakes = clientCameraShakeData.stream().sorted((o1, o2) -> o1.origin.distanceToSqr(player.position()) < o2.origin.distanceToSqr(player.position()) ? -1 : 1).toList();
            CameraShakeData cameraShake = (CameraShakeData) closestCameraShakes.get(0);
            Vec3 closestPos = cameraShake.origin;
            float distanceMultiplier = 1.0F / (cameraShake.radius * cameraShake.radius);
            float fadeout = cameraShake.duration - cameraShake.tickCount > 20 ? 1.0F : (float) (cameraShake.duration - cameraShake.tickCount) * 0.05F;
            float intensity = (float) Mth.clampedLerp(1.0, 0.0, closestPos.distanceToSqr(player.position()) * (double) distanceMultiplier) * fadeout;
            float f = (float) ((double) player.tickCount + event.getPartialTick());
            float yaw = Mth.cos(f * 1.5F) * intensity * 0.35F;
            float pitch = Mth.cos(f * 2.0F) * intensity * 0.35F;
            float roll = Mth.sin(f * 2.2F) * intensity * 0.35F;
            event.setYaw(event.getYaw() + yaw);
            event.setRoll(event.getRoll() + roll);
            event.setPitch(event.getPitch() + pitch);
        }
    }
}
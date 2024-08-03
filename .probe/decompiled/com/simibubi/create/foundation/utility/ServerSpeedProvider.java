package com.simibubi.create.foundation.utility;

import com.simibubi.create.AllPackets;
import com.simibubi.create.foundation.networking.SimplePacketBase;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import com.simibubi.create.infrastructure.config.AllConfigs;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

public class ServerSpeedProvider {

    static int clientTimer = 0;

    static int serverTimer = 0;

    static boolean initialized = false;

    static LerpedFloat modifier = LerpedFloat.linear();

    public static void serverTick() {
        serverTimer++;
        if (serverTimer > getSyncInterval()) {
            AllPackets.getChannel().send(PacketDistributor.ALL.noArg(), new ServerSpeedProvider.Packet());
            serverTimer = 0;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void clientTick() {
        if (!Minecraft.getInstance().hasSingleplayerServer() || !Minecraft.getInstance().isPaused()) {
            modifier.tickChaser();
            clientTimer++;
        }
    }

    public static Integer getSyncInterval() {
        return AllConfigs.server().tickrateSyncTimer.get();
    }

    public static float get() {
        return modifier.getValue();
    }

    public static class Packet extends SimplePacketBase {

        public Packet() {
        }

        public Packet(FriendlyByteBuf buffer) {
        }

        @Override
        public void write(FriendlyByteBuf buffer) {
        }

        @Override
        public boolean handle(NetworkEvent.Context context) {
            context.enqueueWork(() -> {
                if (!ServerSpeedProvider.initialized) {
                    ServerSpeedProvider.initialized = true;
                    ServerSpeedProvider.clientTimer = 0;
                } else {
                    float target = (float) ServerSpeedProvider.getSyncInterval().intValue() / (float) Math.max(ServerSpeedProvider.clientTimer, 1);
                    ServerSpeedProvider.modifier.chase((double) Math.min(target, 1.0F), 0.25, LerpedFloat.Chaser.EXP);
                    ServerSpeedProvider.clientTimer = -1;
                }
            });
            return true;
        }
    }
}
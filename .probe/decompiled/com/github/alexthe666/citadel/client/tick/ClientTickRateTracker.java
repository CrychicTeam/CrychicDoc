package com.github.alexthe666.citadel.client.tick;

import com.github.alexthe666.citadel.server.tick.TickRateTracker;
import com.github.alexthe666.citadel.server.tick.modifier.TickRateModifier;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ClientTickRateTracker extends TickRateTracker {

    public static final Logger LOGGER = LogManager.getLogger("citadel-client-tick");

    private static Map<Minecraft, ClientTickRateTracker> dataMap = new HashMap();

    public Minecraft client;

    private static float MS_PER_TICK = 50.0F;

    public ClientTickRateTracker(Minecraft client) {
        this.client = client;
    }

    public void syncFromServer(CompoundTag tag) {
        this.tickRateModifierList.clear();
        this.fromTag(tag);
    }

    public static ClientTickRateTracker getForClient(Minecraft minecraft) {
        if (!dataMap.containsKey(minecraft)) {
            ClientTickRateTracker tracker = new ClientTickRateTracker(minecraft);
            dataMap.put(minecraft, tracker);
            return tracker;
        } else {
            return (ClientTickRateTracker) dataMap.get(minecraft);
        }
    }

    @Override
    public void masterTick() {
        super.masterTick();
        this.client.timer.msPerTick = this.getClientTickRate();
    }

    public float getClientTickRate() {
        float f = MS_PER_TICK;
        for (TickRateModifier modifier : this.tickRateModifierList) {
            if (modifier.appliesTo(Minecraft.getInstance().level, Minecraft.getInstance().player.m_20185_(), Minecraft.getInstance().player.m_20186_(), Minecraft.getInstance().player.m_20189_())) {
                f *= modifier.getTickRateMultiplier();
            }
        }
        return Math.max(1.0F, f * this.getEntityTickLengthModifier(Minecraft.getInstance().player));
    }

    public float modifySoundPitch(SoundInstance soundInstance) {
        float f = 1.0F;
        for (TickRateModifier modifier : this.tickRateModifierList) {
            if (modifier.appliesTo(Minecraft.getInstance().level, Minecraft.getInstance().player.m_20185_(), Minecraft.getInstance().player.m_20186_(), Minecraft.getInstance().player.m_20189_())) {
                f /= modifier.getTickRateMultiplier();
            }
        }
        return Math.max(1.0F, f * this.getEntityTickLengthModifier(Minecraft.getInstance().player));
    }

    @Override
    public void tickEntityAtCustomRate(Entity entity) {
        if (entity.level().isClientSide && entity.level() instanceof ClientLevel) {
            ((ClientLevel) entity.level()).tickNonPassenger(entity);
        }
    }
}
package com.github.alexthe666.citadel.server.tick;

import com.github.alexthe666.citadel.Citadel;
import com.github.alexthe666.citadel.server.message.SyncClientTickRateMessage;
import com.github.alexthe666.citadel.server.tick.modifier.TickRateModifier;
import com.github.alexthe666.citadel.server.tick.modifier.TickRateModifierType;
import com.github.alexthe666.citadel.server.world.CitadelServerData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ServerTickRateTracker extends TickRateTracker {

    public static final Logger LOGGER = LogManager.getLogger("citadel-server-tick");

    public MinecraftServer server;

    public ServerTickRateTracker(MinecraftServer server) {
        this.server = server;
    }

    public ServerTickRateTracker(MinecraftServer server, CompoundTag tag) {
        this(server);
        this.fromTag(tag);
    }

    public void addTickRateModifier(TickRateModifier modifier) {
        this.tickRateModifierList.add(modifier);
        this.sync();
    }

    @Override
    public void tickEntityAtCustomRate(Entity entity) {
        if (!entity.level().isClientSide && entity.level() instanceof ServerLevel) {
            ((ServerLevel) entity.level()).tickNonPassenger(entity);
        }
    }

    @Override
    protected void sync() {
        Citadel.sendMSGToAll(new SyncClientTickRateMessage(this.toTag()));
    }

    public int getServerTickLengthMs() {
        int i = 50;
        for (TickRateModifier modifier : this.tickRateModifierList) {
            if (modifier.getType() == TickRateModifierType.GLOBAL) {
                i = (int) ((float) i * modifier.getTickRateMultiplier());
            }
        }
        return i <= 0 ? 1 : i;
    }

    public static ServerTickRateTracker getForServer(MinecraftServer server) {
        return CitadelServerData.get(server).getOrCreateTickRateTracker();
    }

    public static void modifyTickRate(Level level, TickRateModifier modifier) {
        if (level instanceof ServerLevel serverLevel) {
            getForServer(serverLevel.getServer()).addTickRateModifier(modifier);
        }
    }
}
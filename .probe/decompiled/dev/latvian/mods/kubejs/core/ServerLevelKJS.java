package dev.latvian.mods.kubejs.core;

import dev.latvian.mods.kubejs.player.EntityArrayList;
import dev.latvian.mods.kubejs.script.ScriptType;
import dev.latvian.mods.rhino.util.RemapPrefixForJS;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.level.storage.ServerLevelData;
import org.jetbrains.annotations.Nullable;

@RemapPrefixForJS("kjs$")
public interface ServerLevelKJS extends LevelKJS, WithPersistentData {

    default ServerLevel kjs$self() {
        return (ServerLevel) this;
    }

    @Override
    default ScriptType kjs$getScriptType() {
        return ScriptType.SERVER;
    }

    @Override
    default EntityArrayList kjs$getEntities() {
        return new EntityArrayList(this.kjs$self(), this.kjs$self().getAllEntities());
    }

    default void kjs$spawnLightning(double x, double y, double z, boolean effectOnly, @Nullable ServerPlayer player) {
        LightningBolt e = EntityType.LIGHTNING_BOLT.create(this.kjs$self());
        e.m_6027_(x, y, z);
        e.setCause(player);
        e.setVisualOnly(effectOnly);
        this.kjs$self().addFreshEntity(e);
    }

    default void kjs$spawnLightning(double x, double y, double z, boolean effectOnly) {
        this.kjs$spawnLightning(x, y, z, effectOnly, null);
    }

    default void kjs$setTime(long time) {
        ((ServerLevelData) this.kjs$self().m_6106_()).setGameTime(time);
    }

    @Override
    default void kjs$spawnParticles(ParticleOptions options, boolean overrideLimiter, double x, double y, double z, double vx, double vy, double vz, int count, double speed) {
        for (ServerPlayer player : this.kjs$self().players()) {
            this.kjs$self().sendParticles(player, options, overrideLimiter, x, y, z, count, vx, vy, vz, speed);
        }
    }
}
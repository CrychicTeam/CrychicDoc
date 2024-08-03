package dev.latvian.mods.kubejs.core.mixin.common;

import dev.latvian.mods.kubejs.core.ServerLevelKJS;
import dev.latvian.mods.rhino.util.HideFromJS;
import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.entity.LevelEntityGetter;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({ ServerLevel.class })
public abstract class ServerLevelMixin implements ServerLevelKJS {

    @Shadow
    @Final
    @HideFromJS
    List<ServerPlayer> players;

    private CompoundTag kjs$persistentData;

    @Override
    public CompoundTag kjs$getPersistentData() {
        if (this.kjs$persistentData == null) {
            String t = this.kjs$self().m_46472_().location().toString();
            this.kjs$persistentData = this.kjs$self().getServer().kjs$getPersistentData().getCompound(t);
            this.kjs$self().getServer().kjs$getPersistentData().put(t, this.kjs$persistentData);
        }
        return this.kjs$persistentData;
    }

    @Shadow
    @HideFromJS
    public abstract List<ServerPlayer> players();

    @Shadow
    @HideFromJS
    protected abstract LevelEntityGetter<Entity> getEntities();
}
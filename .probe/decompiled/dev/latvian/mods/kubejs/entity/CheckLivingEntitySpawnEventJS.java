package dev.latvian.mods.kubejs.entity;

import dev.latvian.mods.kubejs.level.BlockContainerJS;
import dev.latvian.mods.kubejs.typings.Info;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

@Info("Invoked before an entity is spawned into the world.\n\nOnly entities from a `BaseSpawner` or world generation will trigger this event.\n")
public class CheckLivingEntitySpawnEventJS extends LivingEntityEventJS {

    private final LivingEntity entity;

    private final Level level;

    public final double x;

    public final double y;

    public final double z;

    public final MobSpawnType type;

    @Nullable
    public final BaseSpawner spawner;

    public CheckLivingEntitySpawnEventJS(LivingEntity entity, Level level, double x, double y, double z, MobSpawnType type, @Nullable BaseSpawner spawner) {
        this.entity = entity;
        this.level = level;
        this.x = x;
        this.y = y;
        this.z = z;
        this.type = type;
        this.spawner = spawner;
    }

    @Info("The level the entity is being spawned into.")
    @Override
    public Level getLevel() {
        return this.level;
    }

    @Info("The entity being spawned.")
    @Override
    public LivingEntity getEntity() {
        return this.entity;
    }

    @Info("The block the entity is being spawned on.")
    public BlockContainerJS getBlock() {
        return new BlockContainerJS(this.level, BlockPos.containing(this.x, this.y, this.z));
    }

    @Info("The type of spawn.")
    public MobSpawnType getType() {
        return this.type;
    }

    @Info("The spawner that spawned the entity. Can be null if the entity was spawned by worldgen.")
    @Nullable
    public BaseSpawner getSpawner() {
        return this.spawner;
    }
}
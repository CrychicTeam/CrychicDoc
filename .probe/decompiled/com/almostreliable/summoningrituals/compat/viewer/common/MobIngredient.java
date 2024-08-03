package com.almostreliable.summoningrituals.compat.viewer.common;

import com.almostreliable.summoningrituals.platform.Platform;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.SpawnEggItem;

public class MobIngredient {

    private final EntityType<?> mob;

    private final int count;

    private final CompoundTag tag;

    @Nullable
    private Entity entity;

    public MobIngredient(EntityType<?> mob, int count, CompoundTag tag) {
        this.mob = mob;
        this.count = count;
        this.tag = tag;
        ClientLevel level = Minecraft.getInstance().level;
        if (level != null) {
            this.entity = mob.create(level);
            if (this.entity != null && !tag.isEmpty()) {
                this.entity.load(tag);
            }
        }
    }

    public MobIngredient(EntityType<?> mob, int count) {
        this(mob, count, new CompoundTag());
    }

    public Component getDisplayName() {
        return (Component) (this.entity == null ? Component.literal("Unknown Entity") : this.entity.getDisplayName());
    }

    public MutableComponent getRegistryName() {
        return Component.literal(Platform.getId(this.mob).toString());
    }

    public EntityType<?> getEntityType() {
        return this.mob;
    }

    public int getCount() {
        return this.count;
    }

    public CompoundTag getTag() {
        return this.tag;
    }

    @Nullable
    public Entity getEntity() {
        return this.entity;
    }

    @Nullable
    public SpawnEggItem getEgg() {
        return SpawnEggItem.byId(this.mob);
    }
}
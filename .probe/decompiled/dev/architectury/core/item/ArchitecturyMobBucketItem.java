package dev.architectury.core.item;

import java.util.function.Supplier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.MobBucketItem;
import net.minecraft.world.level.material.Fluid;

public class ArchitecturyMobBucketItem extends MobBucketItem {

    public ArchitecturyMobBucketItem(Supplier<? extends EntityType<?>> entity, Supplier<? extends Fluid> fluid, Supplier<? extends SoundEvent> sound, Item.Properties properties) {
        super(entity, fluid, sound, properties);
    }
}
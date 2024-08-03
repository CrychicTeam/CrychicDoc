package net.minecraftforge.event.entity.living;

import java.util.Collection;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraftforge.eventbus.api.Cancelable;

@Cancelable
public class LivingDropsEvent extends LivingEvent {

    private final DamageSource source;

    private final Collection<ItemEntity> drops;

    private final int lootingLevel;

    private final boolean recentlyHit;

    public LivingDropsEvent(LivingEntity entity, DamageSource source, Collection<ItemEntity> drops, int lootingLevel, boolean recentlyHit) {
        super(entity);
        this.source = source;
        this.drops = drops;
        this.lootingLevel = lootingLevel;
        this.recentlyHit = recentlyHit;
    }

    public DamageSource getSource() {
        return this.source;
    }

    public Collection<ItemEntity> getDrops() {
        return this.drops;
    }

    public int getLootingLevel() {
        return this.lootingLevel;
    }

    public boolean isRecentlyHit() {
        return this.recentlyHit;
    }
}
package top.theillusivec4.curios.api.event;

import java.util.Collection;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.Cancelable;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

@Cancelable
public class CurioDropsEvent extends LivingEvent {

    private final DamageSource source;

    private final Collection<ItemEntity> drops;

    private final int lootingLevel;

    private final boolean recentlyHit;

    private final ICuriosItemHandler curioHandler;

    public CurioDropsEvent(LivingEntity entity, ICuriosItemHandler handler, DamageSource source, Collection<ItemEntity> drops, int lootingLevel, boolean recentlyHit) {
        super(entity);
        this.source = source;
        this.drops = drops;
        this.lootingLevel = lootingLevel;
        this.recentlyHit = recentlyHit;
        this.curioHandler = handler;
    }

    public ICuriosItemHandler getCurioHandler() {
        return this.curioHandler;
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
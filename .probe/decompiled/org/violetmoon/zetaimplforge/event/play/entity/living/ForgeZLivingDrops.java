package org.violetmoon.zetaimplforge.event.play.entity.living;

import java.util.Collection;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import org.violetmoon.zeta.event.play.entity.living.ZLivingDrops;

public class ForgeZLivingDrops implements ZLivingDrops {

    private final LivingDropsEvent e;

    public ForgeZLivingDrops(LivingDropsEvent e) {
        this.e = e;
    }

    @Override
    public LivingEntity getEntity() {
        return this.e.getEntity();
    }

    @Override
    public DamageSource getSource() {
        return this.e.getSource();
    }

    @Override
    public Collection<ItemEntity> getDrops() {
        return this.e.getDrops();
    }

    @Override
    public int getLootingLevel() {
        return this.e.getLootingLevel();
    }

    @Override
    public boolean isRecentlyHit() {
        return this.e.isRecentlyHit();
    }

    @Override
    public boolean isCanceled() {
        return this.e.isCanceled();
    }

    @Override
    public void setCanceled(boolean cancel) {
        this.e.setCanceled(cancel);
    }

    public static class Lowest extends ForgeZLivingDrops implements ZLivingDrops.Lowest {

        public Lowest(LivingDropsEvent e) {
            super(e);
        }
    }
}
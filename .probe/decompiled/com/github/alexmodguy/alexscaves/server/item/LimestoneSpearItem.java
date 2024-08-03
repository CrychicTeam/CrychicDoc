package com.github.alexmodguy.alexscaves.server.item;

import com.github.alexmodguy.alexscaves.server.entity.item.LimestoneSpearEntity;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class LimestoneSpearItem extends SpearItem {

    public LimestoneSpearItem(Item.Properties properties) {
        super(properties, 3.0);
    }

    @Override
    public void releaseUsing(ItemStack itemStack, Level level, LivingEntity livingEntity, int i1) {
        if (livingEntity instanceof Player player) {
            int i = this.m_8105_(itemStack) - i1;
            float f = this.getPowerForTime(i);
            if ((double) f > 0.1) {
                LimestoneSpearEntity spearEntity = new LimestoneSpearEntity(level, player, itemStack);
                spearEntity.m_37251_(player, player.m_146909_(), player.m_146908_(), 0.0F, f * 2.5F, 1.0F);
                if (player.getAbilities().instabuild) {
                    spearEntity.f_36705_ = AbstractArrow.Pickup.CREATIVE_ONLY;
                }
                level.m_7967_(spearEntity);
                level.playSound((Player) null, spearEntity, ACSoundRegistry.LIMESTONE_SPEAR_THROW.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
                if (!player.getAbilities().instabuild) {
                    itemStack.shrink(1);
                }
                player.awardStat(Stats.ITEM_USED.get(this));
            }
        }
    }
}
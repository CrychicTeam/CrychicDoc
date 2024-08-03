package net.minecraft.world.item;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;

public class ChorusFruitItem extends Item {

    public ChorusFruitItem(Item.Properties itemProperties0) {
        super(itemProperties0);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack itemStack0, Level level1, LivingEntity livingEntity2) {
        ItemStack $$3 = super.finishUsingItem(itemStack0, level1, livingEntity2);
        if (!level1.isClientSide) {
            double $$4 = livingEntity2.m_20185_();
            double $$5 = livingEntity2.m_20186_();
            double $$6 = livingEntity2.m_20189_();
            for (int $$7 = 0; $$7 < 16; $$7++) {
                double $$8 = livingEntity2.m_20185_() + (livingEntity2.getRandom().nextDouble() - 0.5) * 16.0;
                double $$9 = Mth.clamp(livingEntity2.m_20186_() + (double) (livingEntity2.getRandom().nextInt(16) - 8), (double) level1.m_141937_(), (double) (level1.m_141937_() + ((ServerLevel) level1).getLogicalHeight() - 1));
                double $$10 = livingEntity2.m_20189_() + (livingEntity2.getRandom().nextDouble() - 0.5) * 16.0;
                if (livingEntity2.m_20159_()) {
                    livingEntity2.stopRiding();
                }
                Vec3 $$11 = livingEntity2.m_20182_();
                if (livingEntity2.randomTeleport($$8, $$9, $$10, true)) {
                    level1.m_214171_(GameEvent.TELEPORT, $$11, GameEvent.Context.of(livingEntity2));
                    SoundEvent $$12 = livingEntity2 instanceof Fox ? SoundEvents.FOX_TELEPORT : SoundEvents.CHORUS_FRUIT_TELEPORT;
                    level1.playSound(null, $$4, $$5, $$6, $$12, SoundSource.PLAYERS, 1.0F, 1.0F);
                    livingEntity2.m_5496_($$12, 1.0F, 1.0F);
                    break;
                }
            }
            if (livingEntity2 instanceof Player) {
                ((Player) livingEntity2).getCooldowns().addCooldown(this, 20);
            }
        }
        return $$3;
    }
}
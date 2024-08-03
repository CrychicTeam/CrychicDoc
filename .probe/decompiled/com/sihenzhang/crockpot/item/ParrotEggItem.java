package com.sihenzhang.crockpot.item;

import com.sihenzhang.crockpot.entity.ThrownParrotEgg;
import net.minecraft.Util;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.AbstractProjectileDispenseBehavior;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;

public class ParrotEggItem extends CrockPotBaseItem {

    private final Parrot.Variant variant;

    public ParrotEggItem(Parrot.Variant variant) {
        super(new Item.Properties().stacksTo(16));
        this.variant = variant;
        DispenserBlock.registerBehavior(this, new AbstractProjectileDispenseBehavior() {

            @Override
            protected Projectile getProjectile(Level pLevel, Position pPosition, ItemStack pStack) {
                return Util.make(new ThrownParrotEgg(pLevel, pPosition.x(), pPosition.y(), pPosition.z()), entity -> entity.m_37446_(pStack));
            }
        });
    }

    public Parrot.Variant getVariant() {
        return this.variant;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack stackInHand = pPlayer.m_21120_(pUsedHand);
        pLevel.playSound(null, pPlayer.m_20185_(), pPlayer.m_20186_(), pPlayer.m_20189_(), SoundEvents.EGG_THROW, SoundSource.PLAYERS, 0.5F, 0.4F / (pLevel.getRandom().nextFloat() * 0.4F + 0.8F));
        if (!pLevel.isClientSide) {
            ThrownParrotEgg thrownEgg = new ThrownParrotEgg(pLevel, pPlayer);
            thrownEgg.m_37446_(stackInHand);
            thrownEgg.m_37251_(pPlayer, pPlayer.m_146909_(), pPlayer.m_146908_(), 0.0F, 1.5F, 1.0F);
            pLevel.m_7967_(thrownEgg);
        }
        pPlayer.awardStat(Stats.ITEM_USED.get(this));
        if (!pPlayer.getAbilities().instabuild) {
            stackInHand.shrink(1);
        }
        return InteractionResultHolder.sidedSuccess(stackInHand, pLevel.isClientSide());
    }
}
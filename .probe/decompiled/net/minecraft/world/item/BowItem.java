package net.minecraft.world.item;

import java.util.function.Predicate;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;

public class BowItem extends ProjectileWeaponItem implements Vanishable {

    public static final int MAX_DRAW_DURATION = 20;

    public static final int DEFAULT_RANGE = 15;

    public BowItem(Item.Properties itemProperties0) {
        super(itemProperties0);
    }

    @Override
    public void releaseUsing(ItemStack itemStack0, Level level1, LivingEntity livingEntity2, int int3) {
        if (livingEntity2 instanceof Player $$4) {
            boolean $$5 = $$4.getAbilities().instabuild || EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY_ARROWS, itemStack0) > 0;
            ItemStack $$6 = $$4.getProjectile(itemStack0);
            if (!$$6.isEmpty() || $$5) {
                if ($$6.isEmpty()) {
                    $$6 = new ItemStack(Items.ARROW);
                }
                int $$7 = this.getUseDuration(itemStack0) - int3;
                float $$8 = getPowerForTime($$7);
                if (!((double) $$8 < 0.1)) {
                    boolean $$9 = $$5 && $$6.is(Items.ARROW);
                    if (!level1.isClientSide) {
                        ArrowItem $$10 = (ArrowItem) ($$6.getItem() instanceof ArrowItem ? $$6.getItem() : Items.ARROW);
                        AbstractArrow $$11 = $$10.createArrow(level1, $$6, $$4);
                        $$11.m_37251_($$4, $$4.m_146909_(), $$4.m_146908_(), 0.0F, $$8 * 3.0F, 1.0F);
                        if ($$8 == 1.0F) {
                            $$11.setCritArrow(true);
                        }
                        int $$12 = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.POWER_ARROWS, itemStack0);
                        if ($$12 > 0) {
                            $$11.setBaseDamage($$11.getBaseDamage() + (double) $$12 * 0.5 + 0.5);
                        }
                        int $$13 = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PUNCH_ARROWS, itemStack0);
                        if ($$13 > 0) {
                            $$11.setKnockback($$13);
                        }
                        if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FLAMING_ARROWS, itemStack0) > 0) {
                            $$11.m_20254_(100);
                        }
                        itemStack0.hurtAndBreak(1, $$4, p_289501_ -> p_289501_.m_21190_($$4.m_7655_()));
                        if ($$9 || $$4.getAbilities().instabuild && ($$6.is(Items.SPECTRAL_ARROW) || $$6.is(Items.TIPPED_ARROW))) {
                            $$11.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                        }
                        level1.m_7967_($$11);
                    }
                    level1.playSound(null, $$4.m_20185_(), $$4.m_20186_(), $$4.m_20189_(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F / (level1.getRandom().nextFloat() * 0.4F + 1.2F) + $$8 * 0.5F);
                    if (!$$9 && !$$4.getAbilities().instabuild) {
                        $$6.shrink(1);
                        if ($$6.isEmpty()) {
                            $$4.getInventory().removeItem($$6);
                        }
                    }
                    $$4.awardStat(Stats.ITEM_USED.get(this));
                }
            }
        }
    }

    public static float getPowerForTime(int int0) {
        float $$1 = (float) int0 / 20.0F;
        $$1 = ($$1 * $$1 + $$1 * 2.0F) / 3.0F;
        if ($$1 > 1.0F) {
            $$1 = 1.0F;
        }
        return $$1;
    }

    @Override
    public int getUseDuration(ItemStack itemStack0) {
        return 72000;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack itemStack0) {
        return UseAnim.BOW;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level0, Player player1, InteractionHand interactionHand2) {
        ItemStack $$3 = player1.m_21120_(interactionHand2);
        boolean $$4 = !player1.getProjectile($$3).isEmpty();
        if (!player1.getAbilities().instabuild && !$$4) {
            return InteractionResultHolder.fail($$3);
        } else {
            player1.m_6672_(interactionHand2);
            return InteractionResultHolder.consume($$3);
        }
    }

    @Override
    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return f_43005_;
    }

    @Override
    public int getDefaultProjectileRange() {
        return 15;
    }
}
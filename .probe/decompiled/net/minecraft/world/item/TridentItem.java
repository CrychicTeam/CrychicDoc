package net.minecraft.world.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class TridentItem extends Item implements Vanishable {

    public static final int THROW_THRESHOLD_TIME = 10;

    public static final float BASE_DAMAGE = 8.0F;

    public static final float SHOOT_POWER = 2.5F;

    private final Multimap<Attribute, AttributeModifier> defaultModifiers;

    public TridentItem(Item.Properties itemProperties0) {
        super(itemProperties0);
        Builder<Attribute, AttributeModifier> $$1 = ImmutableMultimap.builder();
        $$1.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(f_41374_, "Tool modifier", 8.0, AttributeModifier.Operation.ADDITION));
        $$1.put(Attributes.ATTACK_SPEED, new AttributeModifier(f_41375_, "Tool modifier", -2.9F, AttributeModifier.Operation.ADDITION));
        this.defaultModifiers = $$1.build();
    }

    @Override
    public boolean canAttackBlock(BlockState blockState0, Level level1, BlockPos blockPos2, Player player3) {
        return !player3.isCreative();
    }

    @Override
    public UseAnim getUseAnimation(ItemStack itemStack0) {
        return UseAnim.SPEAR;
    }

    @Override
    public int getUseDuration(ItemStack itemStack0) {
        return 72000;
    }

    @Override
    public void releaseUsing(ItemStack itemStack0, Level level1, LivingEntity livingEntity2, int int3) {
        if (livingEntity2 instanceof Player $$4) {
            int $$5 = this.getUseDuration(itemStack0) - int3;
            if ($$5 >= 10) {
                int $$6 = EnchantmentHelper.getRiptide(itemStack0);
                if ($$6 <= 0 || $$4.m_20070_()) {
                    if (!level1.isClientSide) {
                        itemStack0.hurtAndBreak(1, $$4, p_43388_ -> p_43388_.m_21190_(livingEntity2.getUsedItemHand()));
                        if ($$6 == 0) {
                            ThrownTrident $$7 = new ThrownTrident(level1, $$4, itemStack0);
                            $$7.m_37251_($$4, $$4.m_146909_(), $$4.m_146908_(), 0.0F, 2.5F + (float) $$6 * 0.5F, 1.0F);
                            if ($$4.getAbilities().instabuild) {
                                $$7.f_36705_ = AbstractArrow.Pickup.CREATIVE_ONLY;
                            }
                            level1.m_7967_($$7);
                            level1.playSound(null, $$7, SoundEvents.TRIDENT_THROW, SoundSource.PLAYERS, 1.0F, 1.0F);
                            if (!$$4.getAbilities().instabuild) {
                                $$4.getInventory().removeItem(itemStack0);
                            }
                        }
                    }
                    $$4.awardStat(Stats.ITEM_USED.get(this));
                    if ($$6 > 0) {
                        float $$8 = $$4.m_146908_();
                        float $$9 = $$4.m_146909_();
                        float $$10 = -Mth.sin($$8 * (float) (Math.PI / 180.0)) * Mth.cos($$9 * (float) (Math.PI / 180.0));
                        float $$11 = -Mth.sin($$9 * (float) (Math.PI / 180.0));
                        float $$12 = Mth.cos($$8 * (float) (Math.PI / 180.0)) * Mth.cos($$9 * (float) (Math.PI / 180.0));
                        float $$13 = Mth.sqrt($$10 * $$10 + $$11 * $$11 + $$12 * $$12);
                        float $$14 = 3.0F * ((1.0F + (float) $$6) / 4.0F);
                        $$10 *= $$14 / $$13;
                        $$11 *= $$14 / $$13;
                        $$12 *= $$14 / $$13;
                        $$4.m_5997_((double) $$10, (double) $$11, (double) $$12);
                        $$4.startAutoSpinAttack(20);
                        if ($$4.m_20096_()) {
                            float $$15 = 1.1999999F;
                            $$4.m_6478_(MoverType.SELF, new Vec3(0.0, 1.1999999F, 0.0));
                        }
                        SoundEvent $$16;
                        if ($$6 >= 3) {
                            $$16 = SoundEvents.TRIDENT_RIPTIDE_3;
                        } else if ($$6 == 2) {
                            $$16 = SoundEvents.TRIDENT_RIPTIDE_2;
                        } else {
                            $$16 = SoundEvents.TRIDENT_RIPTIDE_1;
                        }
                        level1.playSound(null, $$4, $$16, SoundSource.PLAYERS, 1.0F, 1.0F);
                    }
                }
            }
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level0, Player player1, InteractionHand interactionHand2) {
        ItemStack $$3 = player1.m_21120_(interactionHand2);
        if ($$3.getDamageValue() >= $$3.getMaxDamage() - 1) {
            return InteractionResultHolder.fail($$3);
        } else if (EnchantmentHelper.getRiptide($$3) > 0 && !player1.m_20070_()) {
            return InteractionResultHolder.fail($$3);
        } else {
            player1.m_6672_(interactionHand2);
            return InteractionResultHolder.consume($$3);
        }
    }

    @Override
    public boolean hurtEnemy(ItemStack itemStack0, LivingEntity livingEntity1, LivingEntity livingEntity2) {
        itemStack0.hurtAndBreak(1, livingEntity2, p_43414_ -> p_43414_.broadcastBreakEvent(EquipmentSlot.MAINHAND));
        return true;
    }

    @Override
    public boolean mineBlock(ItemStack itemStack0, Level level1, BlockState blockState2, BlockPos blockPos3, LivingEntity livingEntity4) {
        if ((double) blockState2.m_60800_(level1, blockPos3) != 0.0) {
            itemStack0.hurtAndBreak(2, livingEntity4, p_43385_ -> p_43385_.broadcastBreakEvent(EquipmentSlot.MAINHAND));
        }
        return true;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot equipmentSlot0) {
        return equipmentSlot0 == EquipmentSlot.MAINHAND ? this.defaultModifiers : super.getDefaultAttributeModifiers(equipmentSlot0);
    }

    @Override
    public int getEnchantmentValue() {
        return 1;
    }
}
package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.client.render.entity.RenderTideTridentItem;
import com.github.alexthe666.iceandfire.entity.EntityTideTrident;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import java.util.List;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.item.enchantment.ArrowPiercingEnchantment;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.util.NonNullLazy;
import org.jetbrains.annotations.NotNull;

public class ItemTideTrident extends TridentItem {

    public ItemTideTrident() {
        super(new Item.Properties().durability(400));
    }

    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {

            static final NonNullLazy<BlockEntityWithoutLevelRenderer> renderer = NonNullLazy.of(() -> new RenderTideTridentItem(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels()));

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return renderer.get();
            }
        });
    }

    @Override
    public void releaseUsing(@NotNull ItemStack stack, @NotNull Level worldIn, @NotNull LivingEntity entityLiving, int timeLeft) {
        if (entityLiving instanceof Player lvt_5_1_) {
            int lvt_6_1_ = this.m_8105_(stack) - timeLeft;
            if (lvt_6_1_ >= 10) {
                int lvt_7_1_ = EnchantmentHelper.getRiptide(stack);
                if (lvt_7_1_ <= 0 || lvt_5_1_.m_20070_()) {
                    if (!worldIn.isClientSide) {
                        stack.hurtAndBreak(1, lvt_5_1_, player -> player.m_21190_(entityLiving.getUsedItemHand()));
                        if (lvt_7_1_ == 0) {
                            EntityTideTrident lvt_8_1_ = new EntityTideTrident(worldIn, lvt_5_1_, stack);
                            lvt_8_1_.m_37251_(lvt_5_1_, lvt_5_1_.m_146909_(), lvt_5_1_.m_146908_(), 0.0F, 2.5F + (float) lvt_7_1_ * 0.5F, 1.0F);
                            if (lvt_5_1_.getAbilities().instabuild) {
                                lvt_8_1_.f_36705_ = AbstractArrow.Pickup.CREATIVE_ONLY;
                            }
                            worldIn.m_7967_(lvt_8_1_);
                            worldIn.playSound(null, lvt_8_1_, SoundEvents.TRIDENT_THROW, SoundSource.PLAYERS, 1.0F, 1.0F);
                            if (!lvt_5_1_.getAbilities().instabuild) {
                                lvt_5_1_.getInventory().removeItem(stack);
                            }
                        }
                    }
                    lvt_5_1_.awardStat(Stats.ITEM_USED.get(this));
                    if (lvt_7_1_ > 0) {
                        float lvt_8_2_ = lvt_5_1_.m_146908_();
                        float lvt_9_1_ = lvt_5_1_.m_146909_();
                        float lvt_10_1_ = -Mth.sin(lvt_8_2_ * (float) (Math.PI / 180.0)) * Mth.cos(lvt_9_1_ * (float) (Math.PI / 180.0));
                        float lvt_11_1_ = -Mth.sin(lvt_9_1_ * (float) (Math.PI / 180.0));
                        float lvt_12_1_ = Mth.cos(lvt_8_2_ * (float) (Math.PI / 180.0)) * Mth.cos(lvt_9_1_ * (float) (Math.PI / 180.0));
                        float lvt_13_1_ = Mth.sqrt(lvt_10_1_ * lvt_10_1_ + lvt_11_1_ * lvt_11_1_ + lvt_12_1_ * lvt_12_1_);
                        float lvt_14_1_ = 3.0F * ((1.0F + (float) lvt_7_1_) / 4.0F);
                        lvt_10_1_ *= lvt_14_1_ / lvt_13_1_;
                        lvt_11_1_ *= lvt_14_1_ / lvt_13_1_;
                        lvt_12_1_ *= lvt_14_1_ / lvt_13_1_;
                        lvt_5_1_.m_5997_((double) lvt_10_1_, (double) lvt_11_1_, (double) lvt_12_1_);
                        lvt_5_1_.startAutoSpinAttack(20);
                        if (lvt_5_1_.m_20096_()) {
                            float lvt_15_1_ = 1.1999999F;
                            lvt_5_1_.m_6478_(MoverType.SELF, new Vec3(0.0, 1.1999999F, 0.0));
                        }
                        SoundEvent lvt_15_4_;
                        if (lvt_7_1_ >= 3) {
                            lvt_15_4_ = SoundEvents.TRIDENT_RIPTIDE_3;
                        } else if (lvt_7_1_ == 2) {
                            lvt_15_4_ = SoundEvents.TRIDENT_RIPTIDE_2;
                        } else {
                            lvt_15_4_ = SoundEvents.TRIDENT_RIPTIDE_1;
                        }
                        worldIn.playSound(null, lvt_5_1_, lvt_15_4_, SoundSource.PLAYERS, 1.0F, 1.0F);
                    }
                }
            }
        }
    }

    @NotNull
    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(@NotNull EquipmentSlot equipmentSlot) {
        Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        if (equipmentSlot == EquipmentSlot.MAINHAND) {
            builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(f_41374_, "Weapon modifier", 12.0, AttributeModifier.Operation.ADDITION));
            builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(f_41375_, "Weapon modifier", -2.9F, AttributeModifier.Operation.ADDITION));
        }
        return builder.build();
    }

    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return enchantment instanceof ArrowPiercingEnchantment ? true : enchantment.category.canEnchant(stack.getItem());
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        tooltip.add(Component.translatable("item.iceandfire.legendary_weapon.desc").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("item.iceandfire.tide_trident.desc_0").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("item.iceandfire.tide_trident.desc_1").withStyle(ChatFormatting.GRAY));
    }
}
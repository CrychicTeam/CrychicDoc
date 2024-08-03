package com.github.alexthe666.iceandfire.item;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

public class ItemCyclopsEye extends Item {

    public ItemCyclopsEye() {
        super(new Item.Properties().durability(500));
    }

    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return !ItemStack.isSameItem(oldStack, newStack);
    }

    @Override
    public void inventoryTick(ItemStack stack, @NotNull Level world, @NotNull Entity entity, int itemSlot, boolean isSelected) {
        if (stack.getTag() == null) {
            stack.setTag(new CompoundTag());
        } else if (entity instanceof LivingEntity living) {
            if (living.getMainHandItem() == stack || living.getOffhandItem() == stack) {
                double range = 15.0;
                boolean inflictedDamage = false;
                for (Mob LivingEntity : world.m_45976_(Mob.class, new AABB(living.m_20185_() - range, living.m_20186_() - range, living.m_20189_() - range, living.m_20185_() + range, living.m_20186_() + range, living.m_20189_() + range))) {
                    if (!LivingEntity.m_7306_(living) && !LivingEntity.m_7307_(living) && (LivingEntity.getTarget() == living || LivingEntity.m_21188_() == living || LivingEntity instanceof Enemy)) {
                        LivingEntity.m_7292_(new MobEffectInstance(MobEffects.WEAKNESS, 10, 1));
                        inflictedDamage = true;
                    }
                }
                if (inflictedDamage) {
                    stack.getTag().putInt("HurtingTicks", stack.getTag().getInt("HurtingTicks") + 1);
                }
            }
            if (stack.getTag().getInt("HurtingTicks") > 120) {
                stack.hurtAndBreak(1, (LivingEntity) entity, p_220017_1_ -> {
                });
                stack.getTag().putInt("HurtingTicks", 0);
            }
        }
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        tooltip.add(Component.translatable("item.iceandfire.legendary_weapon.desc").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("item.iceandfire.cyclops_eye.desc_0").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("item.iceandfire.cyclops_eye.desc_1").withStyle(ChatFormatting.GRAY));
    }
}
package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.entity.EntityGhostSword;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import com.google.common.collect.Multimap;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class ItemGhostSword extends SwordItem {

    public ItemGhostSword() {
        super(IafItemRegistry.GHOST_SWORD_TOOL_MATERIAL, 5, -1.0F, new Item.Properties());
    }

    public static void spawnGhostSwordEntity(ItemStack stack, Player playerEntity) {
        if (!playerEntity.getCooldowns().isOnCooldown(stack.getItem())) {
            if (playerEntity.m_21120_(InteractionHand.MAIN_HAND) == stack) {
                Multimap<Attribute, AttributeModifier> dmg = stack.getAttributeModifiers(EquipmentSlot.MAINHAND);
                double totalDmg = 0.0;
                for (AttributeModifier modifier : dmg.get(Attributes.ATTACK_DAMAGE)) {
                    totalDmg += modifier.getAmount();
                }
                playerEntity.playSound(SoundEvents.ZOMBIE_INFECT, 1.0F, 1.0F);
                EntityGhostSword shot = new EntityGhostSword(IafEntityRegistry.GHOST_SWORD.get(), playerEntity.m_9236_(), playerEntity, totalDmg * 0.5);
                shot.m_37251_(playerEntity, playerEntity.m_146909_(), playerEntity.m_146908_(), 0.0F, 1.0F, 0.5F);
                playerEntity.m_9236_().m_7967_(shot);
                stack.hurtAndBreak(1, playerEntity, entity -> entity.m_21166_(EquipmentSlot.MAINHAND));
                playerEntity.getCooldowns().addCooldown(stack.getItem(), 10);
            }
        }
    }

    @Override
    public boolean hurtEnemy(@NotNull ItemStack stack, @NotNull LivingEntity targetEntity, @NotNull LivingEntity attacker) {
        return super.hurtEnemy(stack, targetEntity, attacker);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        tooltip.add(Component.translatable("item.iceandfire.legendary_weapon.desc").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("item.iceandfire.ghost_sword.desc_0").withStyle(ChatFormatting.GRAY));
    }
}
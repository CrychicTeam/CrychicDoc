package net.mehvahdjukaar.supplementaries.mixins.forge.self;

import com.google.common.collect.ImmutableSet;
import net.mehvahdjukaar.supplementaries.common.items.WrenchItem;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ WrenchItem.class })
public abstract class SelfWrenchMixin extends Item {

    public SelfWrenchMixin(Item.Properties properties) {
        super(properties);
    }

    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return super.canApplyAtEnchantingTable(stack, enchantment) || ImmutableSet.of(Enchantments.KNOCKBACK).contains(enchantment);
    }

    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        boolean shiftDown = player.m_6144_();
        if (entity instanceof HangingEntity hangingEntity && hangingEntity.getDirection().getAxis().isHorizontal()) {
            Direction dir = hangingEntity.getDirection();
            dir = shiftDown ? dir.getCounterClockWise() : dir.getClockWise();
            hangingEntity.setDirection(dir);
            if (player.m_9236_().isClientSide) {
                WrenchItem.playTurningEffects(hangingEntity.getPos(), shiftDown, Direction.UP, player.m_9236_(), player);
            }
            stack.hurtAndBreak(1, player, p -> p.m_21190_(player.m_7655_()));
            return true;
        }
        if (entity instanceof LivingEntity armorStand && this.m_6880_(stack, player, armorStand, InteractionHand.MAIN_HAND).consumesAction()) {
            return true;
        }
        return super.onLeftClickEntity(stack, player, entity);
    }
}
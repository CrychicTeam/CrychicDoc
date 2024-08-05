package dev.xkmc.modulargolems.content.item.card;

import dev.xkmc.modulargolems.content.item.wand.GolemInteractItem;
import java.util.function.Predicate;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public abstract class TargetFilterCard extends Item implements GolemInteractItem {

    public TargetFilterCard(Item.Properties properties) {
        super(properties);
    }

    public abstract Predicate<LivingEntity> mayTarget(ItemStack var1);

    protected abstract InteractionResultHolder<ItemStack> removeLast(Player var1, ItemStack var2);

    protected InteractionResultHolder<ItemStack> onUse(Player player, ItemStack stack) {
        return InteractionResultHolder.pass(stack);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.m_21120_(hand);
        return player.m_6144_() ? this.removeLast(player, stack) : this.onUse(player, stack);
    }
}
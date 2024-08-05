package com.mna.items.runes;

import com.mna.ManaAndArtifice;
import com.mna.api.items.TieredItem;
import com.mna.api.spells.ICanContainSpell;
import com.mna.items.sorcery.ItemSpell;
import com.mna.spells.crafting.SpellRecipe;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class ItemStoneRune extends TieredItem implements ICanContainSpell {

    public ItemStoneRune() {
        super(new Item.Properties());
    }

    @Override
    public void appendHoverText(ItemStack stack, Level world, List<Component> lines, TooltipFlag flags) {
        super.m_7373_(stack, world, lines, flags);
        ICanContainSpell.super.getSpell(stack).addItemTooltip(stack, world, lines, ManaAndArtifice.instance.proxy.getClientPlayer());
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack held = player.m_21120_(hand);
        InteractionResultHolder<ItemStack> castRes = ItemSpell.castSpellOnUse(held, world, player, hand, this::shouldConsumeMana);
        if (castRes.getResult() == InteractionResult.SUCCESS && !world.isClientSide && player.m_21211_() != held) {
            held.shrink(1);
            if (held.isEmpty() && !player.isCreative()) {
                player.m_21166_(hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
            }
        }
        return castRes;
    }

    @Override
    public int getUseDuration(ItemStack itemStack0) {
        return 99999;
    }

    @Override
    public void onUseTick(Level pLevel, LivingEntity player, ItemStack stack, int count) {
        if (player instanceof Player) {
            ItemSpell.castSpellOnChannelTick(stack, (Player) player, count, this::shouldConsumeChanneledMana);
        }
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entityLiving) {
        if (entityLiving instanceof Player) {
            ((Player) entityLiving).m_21253_();
        }
        stack.shrink(1);
        return stack;
    }

    @Override
    public boolean isFoil(ItemStack itemStack0) {
        return SpellRecipe.stackContainsSpell(itemStack0);
    }

    protected boolean shouldConsumeMana(ItemStack stack) {
        return false;
    }

    protected boolean shouldConsumeChanneledMana(Player player, ItemStack stack) {
        return false;
    }
}
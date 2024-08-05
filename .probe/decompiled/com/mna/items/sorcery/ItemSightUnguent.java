package com.mna.items.sorcery;

import com.mna.api.faction.IFaction;
import com.mna.api.items.IFactionSpecific;
import com.mna.api.items.TieredItem;
import com.mna.effects.EffectInit;
import com.mna.factions.Factions;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class ItemSightUnguent extends TieredItem implements IFactionSpecific {

    final int power;

    public ItemSightUnguent(Item.Properties properties, int power) {
        super(new Item.Properties().durability(10).setNoRepair());
        this.power = power;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        this.ApplyUnguent(worldIn, playerIn, handIn);
        return InteractionResultHolder.pass(playerIn.m_21120_(handIn));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        this.ApplyUnguent(context.getLevel(), context.getPlayer(), context.getHand());
        return InteractionResult.SUCCESS;
    }

    private void ApplyUnguent(Level world, Player player, InteractionHand hand) {
        if (!world.isClientSide) {
            if (this.power > 0) {
                this.usedByPlayer(player);
                player.m_7292_(new MobEffectInstance(EffectInit.ELDRIN_SIGHT.get(), 3600, 0));
            } else {
                player.m_7292_(new MobEffectInstance(EffectInit.WELLSPRING_SIGHT.get(), 12000, 0));
            }
            player.m_21120_(hand).hurtAndBreak(1, player, p -> p.m_21190_(hand));
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(Component.translatable("item.mna.sight_unguent.flavor_text").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
    }

    @Override
    public IFaction getFaction() {
        return this.power == 0 ? null : Factions.COUNCIL;
    }
}
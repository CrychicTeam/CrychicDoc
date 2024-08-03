package com.mna.items.artifice.charms;

import com.mna.ManaAndArtifice;
import com.mna.api.spells.ICanContainSpell;
import com.mna.api.spells.base.ISpellDefinition;
import com.mna.spells.SpellCaster;
import com.mna.spells.crafting.SpellRecipe;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.mutable.MutableInt;
import top.theillusivec4.curios.api.CuriosApi;

public class ItemContingencyCharm extends CharmBaseItem implements ICanContainSpell {

    private static final String NBT_CHARGES = "charm_charges";

    private final ItemContingencyCharm.ContingencyEvent event;

    private final boolean breakOnZeroCharges;

    private final int maxCharges;

    public ItemContingencyCharm(ItemContingencyCharm.ContingencyEvent event, boolean breakOnZeroCharges) {
        this(event, breakOnZeroCharges, 1);
    }

    public ItemContingencyCharm(ItemContingencyCharm.ContingencyEvent event, boolean breakOnZeroCharges, int maxCharges) {
        this.event = event;
        this.breakOnZeroCharges = breakOnZeroCharges;
        this.maxCharges = maxCharges;
    }

    public boolean isFor(ItemContingencyCharm.ContingencyEvent event) {
        return this.event == event;
    }

    public boolean isReady(ItemStack stack, ItemContingencyCharm.ContingencyEvent event) {
        return this.getCharges(stack) > 0 && this.isFor(event);
    }

    @Override
    public boolean consume(ServerPlayer player) {
        MutableInt charges = new MutableInt(0);
        CuriosApi.getCuriosHelper().findCurios(player, this).stream().findFirst().ifPresent(slot -> {
            ISpellDefinition spell = this.getSpell(slot.stack());
            if (spell.isValid()) {
                SpellCaster.PlayerCast(slot.stack(), player, InteractionHand.MAIN_HAND, player.m_20182_(), player.m_20156_(), player.m_9236_(), false);
            }
            charges.setValue(this.consumeCharge(slot.stack(), player));
            if (charges.getValue() <= 0) {
                if (this.breakOnZeroCharges) {
                    slot.stack().hurtAndBreak(1, player, e -> {
                    });
                } else {
                    SpellRecipe.removeSpellFromTag(slot.stack().getTag());
                }
            }
        });
        return false;
    }

    @Override
    public boolean consume(ServerPlayer player, InteractionHand hand) {
        return false;
    }

    private int consumeCharge(ItemStack stack, Player player) {
        int newCharges = this.getCharges(stack) - 1;
        this.setCharges(stack, newCharges);
        return newCharges;
    }

    @Override
    public boolean canAcceptSpell(ItemStack stack, ISpellDefinition spell) {
        if (spell.isChanneled()) {
            return false;
        } else if (this.getCharges(stack) >= this.maxCharges) {
            return false;
        } else {
            ISpellDefinition curSpell = this.getSpell(stack);
            return !curSpell.isValid() || curSpell.isSame(spell, false, true, true);
        }
    }

    @Override
    public ItemStack setSpell(ItemStack stack, ISpellDefinition spell) {
        if (this.containsSpell(stack)) {
            ISpellDefinition curSpell = this.getSpell(stack);
            if (curSpell.isSame(spell, false, true, true)) {
                this.setCharges(stack, this.getCharges(stack) + 1);
                return stack;
            }
        }
        this.setCharges(stack, 1);
        return ICanContainSpell.super.setSpell(stack, spell);
    }

    private void setCharges(ItemStack stack, int charges) {
        stack.getOrCreateTag().putInt("charm_charges", Mth.clamp(charges, 0, this.maxCharges));
    }

    public int getCharges(ItemStack stack) {
        return stack.hasTag() ? stack.getTag().getInt("charm_charges") : 0;
    }

    public static void CheckAndConsumeCharmCharge(ServerPlayer player, ItemContingencyCharm.ContingencyEvent event) {
        CuriosApi.getCuriosHelper().findCurios(player, is -> is.getItem() instanceof ItemContingencyCharm && ((ItemContingencyCharm) is.getItem()).isReady(is, event)).stream().forEach(slot -> ((ItemContingencyCharm) slot.stack().getItem()).consume(player));
    }

    @Override
    public void appendHoverText(ItemStack pStack, Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.m_7373_(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        Player player = ManaAndArtifice.instance.proxy.getClientPlayer();
        if (player != null) {
            ISpellDefinition recipe = this.getSpell(pStack);
            recipe.addItemTooltip(pStack, pLevel, pTooltipComponents, player);
        }
        pTooltipComponents.add(Component.translatable("item.mna.contingency_charm.charges_display", this.getCharges(pStack), this.maxCharges).withStyle(ChatFormatting.AQUA));
        if (this.breakOnZeroCharges) {
            pTooltipComponents.add(Component.translatable("item.mna.contingency_charm.consume_on_trigger").withStyle(ChatFormatting.GRAY));
        }
    }

    public static enum ContingencyEvent {

        LOW_HEALTH,
        DEATH,
        DAMAGE,
        FALL,
        FACTION_RAID,
        LOW_CASTING_RESOURCE,
        SUFFOCATION
    }
}
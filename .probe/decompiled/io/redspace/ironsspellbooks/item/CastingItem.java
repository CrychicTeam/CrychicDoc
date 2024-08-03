package io.redspace.ironsspellbooks.item;

import io.redspace.ironsspellbooks.api.magic.SpellSelectionManager;
import io.redspace.ironsspellbooks.api.spells.SpellData;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.item.weapons.IMultihandWeapon;
import io.redspace.ironsspellbooks.player.ClientMagicData;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class CastingItem extends Item implements IMultihandWeapon {

    public CastingItem(Item.Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.m_21120_(hand);
        SpellSelectionManager spellSelectionManager = new SpellSelectionManager(player);
        SpellSelectionManager.SelectionOption selectionOption = spellSelectionManager.getSelection();
        if (selectionOption != null && !selectionOption.spellData.equals(SpellData.EMPTY)) {
            SpellData spellData = selectionOption.spellData;
            int spellLevel = spellData.getSpell().getLevelFor(spellData.getLevel(), player);
            if (!level.isClientSide()) {
                String castingSlot = hand.ordinal() == 0 ? SpellSelectionManager.MAINHAND : SpellSelectionManager.OFFHAND;
                return spellData.getSpell().attemptInitiateCast(itemStack, spellLevel, level, player, selectionOption.getCastSource(), true, castingSlot) ? InteractionResultHolder.consume(itemStack) : InteractionResultHolder.fail(itemStack);
            } else if (ClientMagicData.isCasting()) {
                return InteractionResultHolder.consume(itemStack);
            } else {
                return ClientMagicData.getPlayerMana() >= spellData.getSpell().getManaCost(spellLevel) && !ClientMagicData.getCooldowns().isOnCooldown(spellData.getSpell()) && ClientMagicData.getSyncedSpellData(player).isSpellLearned(spellData.getSpell()) ? InteractionResultHolder.consume(itemStack) : InteractionResultHolder.pass(itemStack);
            }
        } else {
            return InteractionResultHolder.pass(itemStack);
        }
    }

    @Override
    public int getUseDuration(ItemStack itemStack) {
        return 7200;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.BOW;
    }

    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return slotChanged;
    }

    @Override
    public void releaseUsing(ItemStack itemStack, Level level0, LivingEntity entity, int int1) {
        entity.stopUsingItem();
        Utils.releaseUsingHelper(entity, itemStack, int1);
        super.releaseUsing(itemStack, level0, entity, int1);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }
}
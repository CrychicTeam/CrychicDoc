package io.redspace.ironsspellbooks.item;

import io.redspace.ironsspellbooks.api.item.IScroll;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.magic.SpellSelectionManager;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import io.redspace.ironsspellbooks.api.spells.SpellData;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.player.ClientMagicData;
import io.redspace.ironsspellbooks.util.MinecraftInstanceHelper;
import io.redspace.ironsspellbooks.util.TooltipsUtils;
import java.util.List;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Scroll extends Item implements IScroll {

    public Scroll() {
        super(new Item.Properties().rarity(Rarity.UNCOMMON));
    }

    private AbstractSpell getSpellFromStack(ItemStack itemStack) {
        return ISpellContainer.get(itemStack).getSpellAtIndex(0).getSpell();
    }

    private SpellData getSpellSlotFromStack(ItemStack itemStack) {
        return ISpellContainer.get(itemStack).getSpellAtIndex(0);
    }

    protected void removeScrollAfterCast(ServerPlayer serverPlayer, ItemStack stack) {
        if (!serverPlayer.isCreative()) {
            stack.shrink(1);
        }
    }

    public static void attemptRemoveScrollAfterCast(ServerPlayer serverPlayer) {
        ItemStack potentialScroll = MagicData.getPlayerMagicData(serverPlayer).getPlayerCastingItem();
        if (potentialScroll.getItem() instanceof Scroll scroll) {
            scroll.removeScrollAfterCast(serverPlayer, potentialScroll);
        }
    }

    @NotNull
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.m_21120_(hand);
        SpellData spellSlot = this.getSpellSlotFromStack(stack);
        AbstractSpell spell = spellSlot.getSpell();
        if (level.isClientSide) {
            if (ClientMagicData.isCasting()) {
                return InteractionResultHolder.consume(stack);
            } else {
                return !ClientMagicData.getSyncedSpellData(player).isSpellLearned(spell) ? InteractionResultHolder.pass(stack) : InteractionResultHolder.consume(stack);
            }
        } else {
            String castingSlot = hand.ordinal() == 0 ? SpellSelectionManager.MAINHAND : SpellSelectionManager.OFFHAND;
            return spell.attemptInitiateCast(stack, spell.getLevelFor(spellSlot.getLevel(), player), level, player, CastSource.SCROLL, false, castingSlot) ? InteractionResultHolder.consume(stack) : InteractionResultHolder.fail(stack);
        }
    }

    @Override
    public int getUseDuration(@NotNull ItemStack itemStack) {
        return 7200;
    }

    @NotNull
    @Override
    public UseAnim getUseAnimation(@NotNull ItemStack pStack) {
        return UseAnim.BOW;
    }

    @Override
    public void releaseUsing(@NotNull ItemStack itemStack, @NotNull Level level, @NotNull LivingEntity entity, int ticksUsed) {
        if (this.getSpellFromStack(itemStack).getCastType() != CastType.CONTINUOUS || this.getUseDuration(itemStack) - ticksUsed >= 4) {
            Utils.releaseUsingHelper(entity, itemStack, ticksUsed);
        }
        super.releaseUsing(itemStack, level, entity, ticksUsed);
    }

    @NotNull
    @Override
    public Component getName(@NotNull ItemStack itemStack) {
        return this.getSpellSlotFromStack(itemStack).getDisplayName();
    }

    @Override
    public void appendHoverText(@NotNull ItemStack itemStack, @Nullable Level level, @NotNull List<Component> lines, @NotNull TooltipFlag flag) {
        if (MinecraftInstanceHelper.instance.player() instanceof LocalPlayer localPlayer) {
            lines.addAll(TooltipsUtils.formatScrollTooltip(itemStack, localPlayer));
        }
        super.appendHoverText(itemStack, level, lines, flag);
    }
}
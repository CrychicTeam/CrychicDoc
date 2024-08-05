package com.mna.api.spells;

import com.mna.api.ManaAndArtificeMod;
import com.mna.api.spells.base.ISpellDefinition;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface ICanContainSpell {

    String TRANSCRIBED = "isTranscribedSpellItem";

    default boolean containsSpell(ItemStack stack) {
        return ManaAndArtificeMod.getSpellHelper() == null ? false : ManaAndArtificeMod.getSpellHelper().containsSpell(stack);
    }

    @Nonnull
    @Deprecated(forRemoval = true)
    default ISpellDefinition getSpell(ItemStack stack) {
        return this.getSpell(stack, null);
    }

    @Nonnull
    default ISpellDefinition getSpell(ItemStack stack, @Nullable Player player) {
        return ManaAndArtificeMod.getSpellHelper() == null ? ISpellDefinition.EMPTY : ManaAndArtificeMod.getSpellHelper().parseSpellDefinition(stack, player);
    }

    default ItemStack setSpell(ItemStack stack, ISpellDefinition spell) {
        spell.writeToNBT(stack.getOrCreateTag());
        return stack;
    }

    default boolean canAcceptSpell(ItemStack stack) {
        return true;
    }

    default boolean canAcceptSpell(ItemStack stack, ISpellDefinition spell) {
        return true;
    }

    default void setTranscribedSpell(ItemStack stack) {
        stack.getOrCreateTag().putBoolean("isTranscribedSpellItem", true);
    }

    default boolean isTranscribedSpell(ItemStack stack) {
        return !stack.hasTag() ? false : stack.getOrCreateTag().getBoolean("isTranscribedSpellItem");
    }
}
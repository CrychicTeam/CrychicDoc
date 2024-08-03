package com.mna.factions;

import com.mna.api.affinity.Affinity;
import com.mna.api.faction.BaseFaction;
import com.mna.api.sound.SFX;
import com.mna.gui.GuiTextures;
import com.mna.items.ItemInit;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class NoneFaction extends BaseFaction {

    public NoneFaction(ResourceLocation id) {
        super(id);
    }

    @Override
    public ItemStack getFactionGrimoire() {
        return ItemStack.EMPTY;
    }

    @Override
    public SoundEvent getRaidSound() {
        return SFX.Event.Faction.FACTION_RAID_COUNCIL;
    }

    @Override
    public SoundEvent getHornSound() {
        return null;
    }

    @Override
    public Component getOcculusTaskPrompt(int tier) {
        return Component.literal("Faction Missing - Likely Removed");
    }

    @Override
    public ResourceLocation getFactionIcon() {
        return GuiTextures.Widgets.FACTION_ICON_COUNCIL;
    }

    @Override
    public int[] getManaweaveRGB() {
        return Affinity.ARCANE.getColor();
    }

    @Override
    public ChatFormatting getTornJournalPageFactionColor() {
        return ChatFormatting.OBFUSCATED;
    }

    @Override
    public Item getTokenItem() {
        return ItemInit.__DEBUG.get();
    }
}
package com.mna.factions;

import com.mna.api.capabilities.resource.CastingResourceIDs;
import com.mna.api.faction.BaseFaction;
import com.mna.api.sound.SFX;
import com.mna.api.tools.RLoc;
import com.mna.gui.GuiTextures;
import com.mna.items.ItemInit;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class Council extends BaseFaction {

    public Council() {
        super(CastingResourceIDs.COUNCIL_MANA);
    }

    @Override
    public ItemStack getFactionGrimoire() {
        return new ItemStack(ItemInit.GRIMOIRE_COUNCIL.get());
    }

    @Override
    public SoundEvent getRaidSound() {
        return SFX.Event.Faction.FACTION_RAID_COUNCIL;
    }

    @Override
    public Component getOcculusTaskPrompt(int tier) {
        return Component.translatable("mna:rituals/ancient_council");
    }

    @Override
    public ResourceLocation getFactionIcon() {
        return GuiTextures.Widgets.FACTION_ICON_COUNCIL;
    }

    @Override
    public SoundEvent getHornSound() {
        return SFX.Event.Faction.FACTION_HORN_COUNCIL;
    }

    @Override
    public int[] getManaweaveRGB() {
        return null;
    }

    @Override
    public ChatFormatting getTornJournalPageFactionColor() {
        return ChatFormatting.BLUE;
    }

    @Override
    public Item getTokenItem() {
        return ItemInit.MARK_OF_THE_COUNCIL.get();
    }

    @Override
    public ResourceLocation getSanctumStructure() {
        return RLoc.create("multiblock/council_circle");
    }
}
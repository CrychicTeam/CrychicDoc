package com.mna.factions;

import com.mna.api.affinity.Affinity;
import com.mna.api.capabilities.resource.CastingResourceIDs;
import com.mna.api.config.GeneralConfigValues;
import com.mna.api.faction.BaseFaction;
import com.mna.api.sound.SFX;
import com.mna.api.spells.attributes.Attribute;
import com.mna.api.tools.RLoc;
import com.mna.gui.GuiTextures;
import com.mna.items.ItemInit;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class Demons extends BaseFaction {

    public Demons() {
        super(CastingResourceIDs.BRIMSTONE);
    }

    @Override
    public float getMaxModifierBonus(Attribute attr) {
        switch(attr) {
            case DAMAGE:
                return (float) GeneralConfigValues.DemonBonusDamageMod;
            case RANGE:
                return (float) GeneralConfigValues.DemonBonusRangeMod;
            case WIDTH:
            case RADIUS:
            case HEIGHT:
            case DEPTH:
                return (float) GeneralConfigValues.DemonBonusRadiusMod;
            default:
                return 0.0F;
        }
    }

    @Override
    public float getMinModifierBonus(Attribute attr) {
        return 0.0F;
    }

    @Override
    public ItemStack getFactionGrimoire() {
        return new ItemStack(ItemInit.GRIMOIRE_DEMON.get());
    }

    @Override
    public SoundEvent getRaidSound() {
        return SFX.Event.Faction.FACTION_RAID_DEMONS;
    }

    @Override
    public Component getOcculusTaskPrompt(int tier) {
        return Component.translatable("mna:rituals/burning_hells");
    }

    @Override
    public ResourceLocation getFactionIcon() {
        return GuiTextures.Widgets.FACTION_ICON_DEMONS;
    }

    @Override
    public SoundEvent getHornSound() {
        return SFX.Event.Faction.FACTION_HORN_DEMONS;
    }

    @Override
    public int[] getManaweaveRGB() {
        return Affinity.FIRE.getColor();
    }

    @Override
    public ChatFormatting getTornJournalPageFactionColor() {
        return ChatFormatting.RED;
    }

    @Override
    public Item getTokenItem() {
        return ItemInit.MARK_OF_THE_NETHER.get();
    }

    @Override
    public ResourceLocation getSanctumStructure() {
        return RLoc.create("multiblock/demon_circle");
    }
}
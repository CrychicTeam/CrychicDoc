package com.mna.api.faction;

import com.mna.api.ManaAndArtificeMod;
import com.mna.api.events.FactionAllegianceEvent;
import com.mna.api.spells.attributes.Attribute;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;

public interface IFaction {

    ResourceLocation[] getCastingResources();

    ResourceLocation getCastingResource(Player var1);

    default boolean is(ResourceLocation factionId) {
        return ManaAndArtificeMod.getFactionsRegistry().getValue(factionId) == this;
    }

    default float getMaxModifierBonus(Attribute attr) {
        return 0.0F;
    }

    default float getMinModifierBonus(Attribute attr) {
        return 0.0F;
    }

    default List<IFaction> getEnemyFactions() {
        return ManaAndArtificeMod.getFactionHelper().getFactionsExcept(this);
    }

    default List<IFaction> getAlliedFactions() {
        FactionAllegianceEvent event = new FactionAllegianceEvent(this, Arrays.asList(this));
        MinecraftForge.EVENT_BUS.post(event);
        return event.getAllegiances();
    }

    @Nullable
    default ResourceLocation getSanctumStructure() {
        return null;
    }

    ItemStack getFactionGrimoire();

    Item getTokenItem();

    SoundEvent getRaidSound();

    @Nullable
    SoundEvent getHornSound();

    Component getOcculusTaskPrompt(int var1);

    ResourceLocation getFactionIcon();

    default int getFactionIconTextureSize() {
        return 8;
    }

    @Nullable
    int[] getManaweaveRGB();

    ChatFormatting getTornJournalPageFactionColor();
}
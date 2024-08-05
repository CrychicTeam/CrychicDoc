package com.mna.capabilities.playerdata.progression;

import com.mna.ManaAndArtifice;
import com.mna.Registries;
import com.mna.api.capabilities.IPlayerProgression;
import com.mna.api.faction.IFaction;
import com.mna.factions.Factions;
import java.util.Iterator;
import java.util.Map.Entry;
import net.minecraft.ResourceLocationException;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.registries.IForgeRegistry;

public class PlayerProgressionProvider implements ICapabilitySerializable<Tag> {

    public static final Capability<IPlayerProgression> PROGRESSION = CapabilityManager.get(new CapabilityToken<IPlayerProgression>() {
    });

    private final LazyOptional<IPlayerProgression> holder = LazyOptional.of(PlayerProgression::new);

    final String KEY_TIER = "tier";

    final String KEY_FACTION = "faction";

    final String KEY_FACTION_STANDING = "faction_standing";

    final String KEY_ALLY_COOLDOWN = "ally_cooldown";

    final String KEY_PROGRESSION = "progression_completion";

    final String KEY_RAID_CHANCE = "raid_chance_";

    final String KEY_RAID_STATS = "raid_stats_";

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return PROGRESSION.orEmpty(cap, this.holder);
    }

    @Override
    public Tag serializeNBT() {
        IPlayerProgression instance = this.holder.orElse(null);
        CompoundTag nbt = new CompoundTag();
        nbt.putInt("tier", instance.getTier());
        nbt.putInt("ally_cooldown", instance.getAllyCooldown());
        if (instance.hasAlliedFaction()) {
            nbt.putString("faction", ((IForgeRegistry) Registries.Factions.get()).getKey(instance.getAlliedFaction()).toString());
            nbt.putInt("faction_standing", instance.getFactionStanding());
        }
        ListTag progressionSteps = new ListTag();
        for (ResourceLocation rLoc : instance.getCompletedProgressionSteps()) {
            progressionSteps.add(StringTag.valueOf(rLoc.toString()));
        }
        nbt.put("progression_completion", progressionSteps);
        for (Entry<ResourceKey<IFaction>, IFaction> f : ((IForgeRegistry) Registries.Factions.get()).getEntries()) {
            String key = "raid_chance_" + ((ResourceKey) f.getKey()).location().toString();
            nbt.putDouble(key, instance.getRaidChance((IFaction) f.getValue()));
            key = "raid_stats_" + ((ResourceKey) f.getKey()).location().toString();
            nbt.put(key, instance.getFactionDifficultyStats((IFaction) f.getValue()).writeToNBT());
        }
        return nbt;
    }

    @Override
    public void deserializeNBT(Tag nbt) {
        IPlayerProgression instance = this.holder.orElse(null);
        if (nbt instanceof CompoundTag cnbt) {
            if (cnbt.contains("tier")) {
                instance.setTier(cnbt.getInt("tier"), null);
            }
            if (cnbt.contains("ally_cooldown")) {
                instance.setAllyCooldown(cnbt.getInt("ally_cooldown"));
            }
            if (cnbt.contains("faction")) {
                try {
                    ResourceLocation factionID = new ResourceLocation(cnbt.getString("faction"));
                    instance.setAlliedFaction((IFaction) ((IForgeRegistry) Registries.Factions.get()).getValue(factionID), null);
                } catch (ResourceLocationException var8) {
                    ManaAndArtifice.LOGGER.info("Detected old version data, upgrading player NBT.");
                    String oldFaction = cnbt.getString("faction");
                    switch(oldFaction) {
                        case "ANCIENT_WIZARDS":
                            instance.setAlliedFaction(Factions.COUNCIL, null);
                            break;
                        case "FEY_COURT":
                            instance.setAlliedFaction(Factions.FEY, null);
                            break;
                        case "DEMONS":
                            instance.setAlliedFaction(Factions.DEMONS, null);
                            break;
                        case "UNDEAD":
                            instance.setAlliedFaction(Factions.UNDEAD, null);
                            break;
                        default:
                            ManaAndArtifice.LOGGER.warn("NBT upgrade failed, clearing faction to prevent a crash.  Restore with commands.");
                            instance.setAlliedFaction(null, null);
                    }
                }
            }
            if (cnbt.contains("faction_standing")) {
                instance.setFactionStanding(cnbt.getInt("faction_standing"));
            }
            if (cnbt.contains("progression_completion")) {
                ListTag progressionSteps = cnbt.getList("progression_completion", 8);
                Iterator<Tag> it = progressionSteps.iterator();
                while (it.hasNext()) {
                    instance.addTierProgressionComplete(new ResourceLocation(((Tag) it.next()).getAsString()));
                }
            }
            for (Entry<ResourceKey<IFaction>, IFaction> f : ((IForgeRegistry) Registries.Factions.get()).getEntries()) {
                String key = "raid_chance_" + ((ResourceKey) f.getKey()).location().toString();
                if (cnbt.contains(key)) {
                    instance.setRaidChance((IFaction) f.getValue(), cnbt.getDouble(key));
                }
                key = "raid_stats_" + ((ResourceKey) f.getKey()).location().toString();
                if (cnbt.contains(key)) {
                    instance.getFactionDifficultyStats((IFaction) f.getValue()).readFromNBT(cnbt.getCompound(key));
                }
            }
        } else {
            ManaAndArtifice.LOGGER.error("Progression NBT passed back not an instance of CompoundNBT - save data was NOT loaded!");
        }
    }
}
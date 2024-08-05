package com.mna.capabilities.playerdata.rote;

import com.mna.ManaAndArtifice;
import com.mna.api.capabilities.IPlayerRoteSpells;
import com.mna.api.spells.base.ISpellComponent;
import java.util.Map.Entry;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class PlayerRoteSpellsProvider implements ICapabilitySerializable<Tag> {

    final String KEY_ROTE_SPELL_DATA = "rote_spell_data";

    final String KEY_MASTERY_SPELL_DATA = "mastery_spell_data";

    final String KEY_ID = "id";

    final String KEY_XP = "xp";

    final String KEY_MASTERY = "mastery";

    public static final Capability<IPlayerRoteSpells> ROTE = CapabilityManager.get(new CapabilityToken<IPlayerRoteSpells>() {
    });

    private final LazyOptional<IPlayerRoteSpells> holder = LazyOptional.of(PlayerRoteSpells::new);

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return ROTE.orEmpty(cap, this.holder);
    }

    @Override
    public Tag serializeNBT() {
        IPlayerRoteSpells instance = this.holder.orElse(null);
        CompoundTag nbt = new CompoundTag();
        ListTag roteData = new ListTag();
        ListTag masteryData = new ListTag();
        for (Entry<ISpellComponent, Float> entry : instance.getRoteData().entrySet()) {
            CompoundTag entryData = new CompoundTag();
            entryData.putString("id", ((ISpellComponent) entry.getKey()).getRegistryName().toString());
            entryData.putFloat("xp", (Float) entry.getValue());
            roteData.add(entryData);
        }
        for (Entry<ISpellComponent, Float> entry : instance.getMasteryData().entrySet()) {
            CompoundTag entryData = new CompoundTag();
            entryData.putString("id", ((ISpellComponent) entry.getKey()).getRegistryName().toString());
            entryData.putFloat("mastery", (Float) entry.getValue());
            masteryData.add(entryData);
        }
        nbt.put("rote_spell_data", roteData);
        nbt.put("mastery_spell_data", masteryData);
        return nbt;
    }

    @Override
    public void deserializeNBT(Tag nbt) {
        IPlayerRoteSpells instance = this.holder.orElse(null);
        if (nbt instanceof CompoundTag cnbt) {
            if (cnbt.contains("rote_spell_data")) {
                for (Tag entryINBT : cnbt.getList("rote_spell_data", 10)) {
                    if (entryINBT instanceof CompoundTag) {
                        CompoundTag entryData = (CompoundTag) entryINBT;
                        if (entryData.contains("id") && entryData.contains("xp")) {
                            instance.setRoteXP(new ResourceLocation(entryData.getString("id")), entryData.getFloat("xp"));
                        }
                    }
                }
            }
            if (cnbt.contains("mastery_spell_data")) {
                for (Tag entryINBTx : cnbt.getList("mastery_spell_data", 10)) {
                    if (entryINBTx instanceof CompoundTag) {
                        CompoundTag entryData = (CompoundTag) entryINBTx;
                        if (entryData.contains("id") && entryData.contains("mastery")) {
                            instance.setMastery(new ResourceLocation(entryData.getString("id")), entryData.getFloat("mastery"));
                        }
                    }
                }
            }
        } else {
            ManaAndArtifice.LOGGER.error("Rote Spell NBT passed back not an instance of CompoundNBT - save data was NOT loaded!");
        }
    }
}
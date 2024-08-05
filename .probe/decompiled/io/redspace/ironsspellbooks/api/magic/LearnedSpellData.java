package io.redspace.ironsspellbooks.api.magic;

import io.redspace.ironsspellbooks.api.network.ISerializable;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;

public class LearnedSpellData implements ISerializable {

    public static final String LEARNED_SPELLS = "learnedSpells";

    public final Set<ResourceLocation> learnedSpells = new HashSet();

    public void saveToNBT(CompoundTag compound) {
        if (!this.learnedSpells.isEmpty()) {
            ListTag listTag = new ListTag();
            for (ResourceLocation resourceLocation : this.learnedSpells) {
                listTag.add(StringTag.valueOf(resourceLocation.toString()));
            }
            compound.put("learnedSpells", listTag);
        }
    }

    public void loadFromNBT(CompoundTag compound) {
        ListTag learnedTag = (ListTag) compound.get("learnedSpells");
        if (learnedTag != null && !learnedTag.isEmpty()) {
            for (Tag tag : learnedTag) {
                if (tag instanceof StringTag) {
                    StringTag stringTag = (StringTag) tag;
                    ResourceLocation resourceLocation = new ResourceLocation(stringTag.getAsString());
                    if (((IForgeRegistry) SpellRegistry.REGISTRY.get()).getValue(resourceLocation) != null) {
                        this.learnedSpells.add(resourceLocation);
                    }
                }
            }
        }
    }

    @Override
    public void writeToBuffer(FriendlyByteBuf buf) {
        buf.writeInt(this.learnedSpells.size());
        for (ResourceLocation resourceLocation : this.learnedSpells) {
            buf.writeUtf(resourceLocation.toString());
        }
    }

    @Override
    public void readFromBuffer(FriendlyByteBuf buf) {
        int i = buf.readInt();
        if (i > 0) {
            for (int j = 0; j < i; j++) {
                ResourceLocation resourceLocation = new ResourceLocation(buf.readUtf());
                if (((IForgeRegistry) SpellRegistry.REGISTRY.get()).getValue(resourceLocation) != null) {
                    this.learnedSpells.add(resourceLocation);
                }
            }
        }
    }
}
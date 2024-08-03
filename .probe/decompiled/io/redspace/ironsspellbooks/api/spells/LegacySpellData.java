package io.redspace.ironsspellbooks.api.spells;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import java.util.Objects;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class LegacySpellData {

    public static final String ISB_SPELL = "ISB_spell";

    public static final String LEGACY_SPELL_TYPE = "type";

    public static final String SPELL_ID = "id";

    public static final String SPELL_LEVEL = "level";

    public static final LegacySpellData EMPTY = new LegacySpellData(SpellRegistry.none(), 0);

    public MutableComponent displayName;

    public final AbstractSpell spell;

    public final int spellLevel;

    private LegacySpellData() throws Exception {
        throw new Exception("Cannot create empty spell data.");
    }

    public LegacySpellData(AbstractSpell spell, int level) {
        this.spell = (AbstractSpell) Objects.requireNonNull(spell);
        this.spellLevel = level;
    }

    public static LegacySpellData getSpellData(ItemStack stack) {
        CompoundTag tag = stack.getTagElement("ISB_spell");
        return tag != null ? new LegacySpellData(SpellRegistry.getSpell(new ResourceLocation(tag.getString("id"))), tag.getInt("level")) : EMPTY;
    }

    public static boolean hasSpellData(ItemStack itemStack) {
        CompoundTag tag = itemStack.getTagElement("ISB_spell");
        return tag != null;
    }
}
package io.redspace.ironsspellbooks.api.spells;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import io.redspace.ironsspellbooks.util.MinecraftInstanceHelper;
import java.util.Objects;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class SpellData implements Comparable<SpellData> {

    public static final String SPELL_ID = "id";

    public static final String SPELL_LEVEL = "level";

    public static final String SPELL_LOCKED = "locked";

    public static final SpellData EMPTY = new SpellData(SpellRegistry.none(), 0, false);

    private MutableComponent displayName;

    protected final AbstractSpell spell;

    protected final int spellLevel;

    protected final boolean locked;

    private SpellData() throws Exception {
        throw new Exception("Cannot create empty spell slots.");
    }

    public SpellData(AbstractSpell spell, int level, boolean locked) {
        this.spell = (AbstractSpell) Objects.requireNonNull(spell);
        this.spellLevel = level;
        this.locked = locked;
    }

    public SpellData(AbstractSpell spell, int level) {
        this(spell, level, false);
    }

    public AbstractSpell getSpell() {
        return (AbstractSpell) (this.spell == null ? SpellRegistry.none() : this.spell);
    }

    public int getLevel() {
        return this.spellLevel;
    }

    public boolean isLocked() {
        return this.locked;
    }

    public boolean canRemove() {
        return !this.locked;
    }

    public SpellRarity getRarity() {
        return this.getSpell().getRarity(this.getLevel());
    }

    public Component getDisplayName() {
        if (this.displayName == null) {
            this.displayName = this.getSpell().getDisplayName(MinecraftInstanceHelper.instance.player()).append(" ").append(Component.translatable(ItemRegistry.SCROLL.get().getDescriptionId()));
        }
        return this.displayName;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else {
            return !(obj instanceof SpellData other) ? false : this.spell.equals(other.spell) && this.spellLevel == other.spellLevel;
        }
    }

    public int hashCode() {
        return 31 * this.spell.hashCode() + this.spellLevel;
    }

    public int compareTo(SpellData other) {
        int i = this.spell.getSpellId().compareTo(other.spell.getSpellId());
        if (i == 0) {
            i = Integer.compare(this.spellLevel, other.spellLevel);
        }
        return i;
    }
}
package com.squoshi.irons_spells_js.spell;

import com.squoshi.irons_spells_js.util.ISSKJSUtils;
import dev.latvian.mods.kubejs.typings.Info;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;

public interface AbstractSpellWrapper {

    @Info("    Returns a spell registry object by its ResourceLocation.\n")
    static AbstractSpell of(ISSKJSUtils.SpellHolder spellHolder) {
        return SpellRegistry.getSpell(spellHolder.getLocation());
    }

    @Info("    Returns whether a spell is registered or not.\n")
    static boolean exists(ISSKJSUtils.SpellHolder spellHolder) {
        return SpellRegistry.getSpell(spellHolder.getLocation()) != null;
    }

    @Info("    Returns whether an object is a spell or not.\n")
    static boolean isSpell(Object o) {
        return o instanceof AbstractSpell;
    }

    static AbstractSpellWrapper.SpellStatus checkStatus(ISSKJSUtils.SpellHolder spellHolder) {
        AbstractSpellWrapper.SpellStatus enabled = SpellRegistry.getSpell(spellHolder.getLocation()).isEnabled() ? AbstractSpellWrapper.SpellStatus.ENABLED : AbstractSpellWrapper.SpellStatus.DISABLED;
        return exists(spellHolder) ? enabled : AbstractSpellWrapper.SpellStatus.UNREGISTERED;
    }

    static boolean isEnabled(ISSKJSUtils.SpellHolder spellHolder) {
        return SpellRegistry.getSpell(spellHolder.getLocation()).isEnabled();
    }

    public static enum SpellStatus {

        REGISTERED, UNREGISTERED, ENABLED, DISABLED
    }
}
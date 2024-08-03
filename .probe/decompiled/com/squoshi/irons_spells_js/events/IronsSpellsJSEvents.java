package com.squoshi.irons_spells_js.events;

import dev.latvian.mods.kubejs.bindings.event.PlayerEvents;
import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.event.EventHandler;
import io.redspace.ironsspellbooks.api.events.ChangeManaEvent;
import io.redspace.ironsspellbooks.api.events.SpellOnCastEvent;
import io.redspace.ironsspellbooks.api.events.SpellPreCastEvent;
import io.redspace.ironsspellbooks.api.magic.SpellSelectionManager;

public class IronsSpellsJSEvents {

    public static final EventGroup GROUP = EventGroup.of("ISSEvents");

    public static final EventHandler changeMana = PlayerEvents.GROUP.server("changeMana", () -> ChangeManaEventJS.class);

    public static final EventHandler spellCast = PlayerEvents.GROUP.server("spellOnCast", () -> SpellOnCastEventJS.class);

    public static final EventHandler spellPreCast = PlayerEvents.GROUP.server("spellPreCast", () -> SpellPreCastEventJS.class).hasResult();

    public static final EventHandler spellSelectionManager = PlayerEvents.GROUP.startup("spellSelection", () -> SpellSelectionEventJS.class);

    public static void changeMana(ChangeManaEvent event) {
        if (changeMana.hasListeners() && changeMana.post(new ChangeManaEventJS(event)).interruptFalse()) {
            event.setCanceled(true);
        }
    }

    public static void spellCast(SpellOnCastEvent event) {
        if (spellCast.hasListeners()) {
            spellCast.post(new SpellOnCastEventJS(event));
        }
    }

    public static void spellPreCast(SpellPreCastEvent event) {
        if (spellPreCast.hasListeners() && spellPreCast.post(new SpellPreCastEventJS(event)).interruptFalse()) {
            event.setCanceled(true);
        }
    }

    public static void spellSelectionManager(SpellSelectionManager.SpellSelectionEvent event) {
        if (spellSelectionManager.hasListeners()) {
            spellSelectionManager.post(new SpellSelectionEventJS(event));
        }
    }
}
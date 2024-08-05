package com.mna.api.spells;

import com.mna.api.spells.parts.SpellEffect;
import java.util.HashMap;

public enum SpellCastingResultCode {

    WORLD_CLIENT(true),
    SILENCED(false),
    CAPABILITY_MISSING(false),
    TOO_COMPLEX(false),
    NOT_ENOUGH_MANA(false),
    NOT_UNLOCKED_MAGIC(false),
    INVALID_RECIPE(false),
    INSUFFICIENT_TIER(false),
    CANCELED_BY_EVENT(false),
    FACTION_DENIED(false),
    NO_TARGET(false),
    MISSING_REAGENTS(false),
    SUCCESS(true),
    SPELL_INTERACTIBLE_BLOCK_HIT(true),
    CHANNEL(true),
    SPELL_INTERACTIBLE_ENTITY_HIT(true),
    BLOCKED_BY_CONFIG(false),
    EXCEPTION_THROWN(false),
    NPC_CAST_HANDLER_NOT_IMPLEMENTED(false);

    private boolean success;

    private SpellCastingResultCode(boolean isSuccess) {
        this.success = isSuccess;
    }

    public boolean isConsideredSuccess() {
        return this.success;
    }

    public SpellCastingResult createResult(HashMap<SpellEffect, ComponentApplicationResult> componentResults) {
        return new SpellCastingResult(this, componentResults);
    }

    public SpellCastingResult createResult() {
        return new SpellCastingResult(this, new HashMap());
    }
}
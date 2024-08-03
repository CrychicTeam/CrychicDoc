package com.mna.api.spells;

import com.mna.api.spells.parts.SpellEffect;
import java.util.HashMap;

public class SpellCastingResult {

    private final SpellCastingResultCode code;

    private final HashMap<SpellEffect, ComponentApplicationResult> componentResults;

    public SpellCastingResult(SpellCastingResultCode code, HashMap<SpellEffect, ComponentApplicationResult> componentResults) {
        this.code = code;
        this.componentResults = componentResults;
    }

    public SpellCastingResultCode getCode() {
        return this.code;
    }

    public ComponentApplicationResult getResultFor(SpellEffect c) {
        return this.componentResults.containsKey(c) ? (ComponentApplicationResult) this.componentResults.get(c) : ComponentApplicationResult.NOT_PRESENT;
    }
}
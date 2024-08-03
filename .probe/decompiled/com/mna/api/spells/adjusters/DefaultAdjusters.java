package com.mna.api.spells.adjusters;

public class DefaultAdjusters {

    public static final boolean adjustAlways(SpellAdjustingContext context) {
        return true;
    }

    public static final boolean adjustOnToolTipAndCastOnly(SpellAdjustingContext context) {
        return context.stage == SpellCastStage.CASTING || context.stage == SpellCastStage.SPELL_TOOLTIP;
    }
}
package se.mickelus.tetra.craftingeffect.outcome;

import se.mickelus.tetra.craftingeffect.condition.CraftingEffectCondition;

class EffectPair {

    CraftingEffectCondition requirement = CraftingEffectCondition.any;

    CraftingEffectOutcome outcome;
}
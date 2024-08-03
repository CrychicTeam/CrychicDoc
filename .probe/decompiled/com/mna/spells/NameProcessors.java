package com.mna.spells;

import com.mna.api.affinity.Affinity;
import com.mna.api.sound.SFX;
import com.mna.api.spells.base.ISpellDefinition;
import com.mna.api.spells.collections.Components;
import com.mna.spells.crafting.SpellRecipe;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;

public class NameProcessors {

    private static final String HEALINGSPELL = "healing spell";

    private static final String FART = "fart";

    public static void checkAndAddDisplay(SpellRecipe recipe, String displayName, List<Component> existing) {
        if (displayName.toLowerCase().equals("healing spell") && recipe.getComponents().stream().anyMatch(c -> c.getPart() == Components.HEAL)) {
            existing.add(Component.literal("JUST LET ME DIE BRIAN!"));
        }
    }

    public static SoundEvent checkAndOverrideSound(ISpellDefinition recipe, String spellDisplayName, SoundEvent existing) {
        if (spellDisplayName.toLowerCase().equals("healing spell") && recipe.getComponents().stream().anyMatch(c -> c.getPart() == Components.HEAL)) {
            return SFX.Spell.Cast.BRIAN;
        } else {
            return spellDisplayName.toLowerCase().contains("fart") && recipe.getAffinity().containsKey(Affinity.WIND) ? SFX.Spell.Cast.BRIAN_2 : existing;
        }
    }
}
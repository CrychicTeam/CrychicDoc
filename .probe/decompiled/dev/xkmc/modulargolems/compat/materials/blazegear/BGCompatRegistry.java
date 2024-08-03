package dev.xkmc.modulargolems.compat.materials.blazegear;

import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.xkmc.modulargolems.init.registrate.GolemModifiers;

public class BGCompatRegistry {

    public static final RegistryEntry<BlazingModifier> BLAZING = GolemModifiers.reg("blazing", BlazingModifier::new, "Shoot fireballs when approaching target.");

    public static void register() {
    }
}
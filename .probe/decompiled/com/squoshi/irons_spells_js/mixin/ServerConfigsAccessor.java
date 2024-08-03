package com.squoshi.irons_spells_js.mixin;

import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.config.ServerConfigs;
import net.minecraftforge.common.ForgeConfigSpec;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value = { ServerConfigs.class }, remap = false)
public interface ServerConfigsAccessor {

    @Invoker("createSpellConfig")
    static void invoke$createSpellConfig(AbstractSpell spell) {
        throw new AssertionError();
    }

    @Accessor("BUILDER")
    static ForgeConfigSpec.Builder getBuilder() {
        throw new AssertionError();
    }
}
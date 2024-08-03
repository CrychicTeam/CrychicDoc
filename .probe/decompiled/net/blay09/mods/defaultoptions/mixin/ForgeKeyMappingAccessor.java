package net.blay09.mods.defaultoptions.mixin;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyModifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({ KeyMapping.class })
public interface ForgeKeyMappingAccessor {

    @Accessor
    @Mutable
    void setKeyModifierDefault(KeyModifier var1);
}
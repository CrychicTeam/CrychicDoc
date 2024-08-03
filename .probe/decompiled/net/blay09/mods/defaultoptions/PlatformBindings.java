package net.blay09.mods.defaultoptions;

import net.blay09.mods.balm.api.client.keymappings.KeyModifier;
import net.minecraft.client.KeyMapping;

public abstract class PlatformBindings {

    public static PlatformBindings INSTANCE;

    public abstract void setDefaultKeyModifier(KeyMapping var1, KeyModifier var2);

    public abstract void setKeyModifier(KeyMapping var1, KeyModifier var2);

    public abstract KeyModifier getKeyModifier(KeyMapping var1);

    public abstract KeyModifier getDefaultKeyModifier(KeyMapping var1);
}
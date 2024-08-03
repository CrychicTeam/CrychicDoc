package net.blay09.mods.defaultoptions.keys;

import com.mojang.blaze3d.platform.InputConstants;
import net.blay09.mods.balm.api.client.keymappings.KeyModifier;

public class DefaultKeyMapping {

    public final InputConstants.Key input;

    public final KeyModifier modifier;

    public DefaultKeyMapping(InputConstants.Key input, KeyModifier modifier) {
        this.input = input;
        this.modifier = modifier;
    }
}
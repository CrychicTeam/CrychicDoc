package com.github.einjerjar.mc.keymap.cross.services;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;

public interface IKeybindHelper {

    KeyMapping create(InputConstants.Type var1, int var2, String var3, String var4);
}
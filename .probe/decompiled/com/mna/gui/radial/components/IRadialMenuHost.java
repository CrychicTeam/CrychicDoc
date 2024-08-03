package com.mna.gui.radial.components;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.Screen;

public interface IRadialMenuHost extends IDrawingHelper {

    Screen getScreen();

    Font getFontRenderer();
}
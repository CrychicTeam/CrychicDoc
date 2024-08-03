package de.keksuccino.fancymenu.util.rendering.ui.screen.texteditor;

import net.minecraft.network.chat.Style;
import org.jetbrains.annotations.Nullable;

public abstract class TextEditorFormattingRule {

    public abstract void resetRule(TextEditorScreen var1);

    @Nullable
    public abstract Style getStyle(char var1, int var2, int var3, TextEditorLine var4, int var5, TextEditorScreen var6);
}
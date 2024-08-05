package de.keksuccino.konkrete.mixin.client;

import net.minecraft.client.gui.components.EditBox;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin({ EditBox.class })
public interface IMixinEditBox {

    @Accessor("isEditable")
    boolean getIsEditableKonkrete();

    @Accessor("highlightPos")
    int getHightlightPosKonkrete();

    @Accessor("maxLength")
    int getMaxLengthKonkrete();

    @Invoker("onValueChange")
    void onValueChangeKonkrete(String var1);
}
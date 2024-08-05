package fr.frinn.custommachinery.client.screen.creation.gui;

import fr.frinn.custommachinery.api.guielement.GuiElementType;
import fr.frinn.custommachinery.api.guielement.IGuiElement;
import fr.frinn.custommachinery.client.screen.creation.MachineEditScreen;
import fr.frinn.custommachinery.client.screen.popup.PopupScreen;
import fr.frinn.custommachinery.impl.guielement.AbstractGuiElement;
import java.util.function.Consumer;
import org.jetbrains.annotations.Nullable;

public interface IGuiElementBuilder<T extends IGuiElement> {

    GuiElementType<T> type();

    T make(AbstractGuiElement.Properties var1, @Nullable T var2);

    PopupScreen makeConfigPopup(MachineEditScreen var1, MutableProperties var2, @Nullable T var3, Consumer<T> var4);
}
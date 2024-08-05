package fr.frinn.custommachinery.client.screen.creation.component;

import fr.frinn.custommachinery.api.component.IMachineComponent;
import fr.frinn.custommachinery.api.component.IMachineComponentTemplate;
import fr.frinn.custommachinery.api.component.MachineComponentType;
import fr.frinn.custommachinery.client.screen.creation.MachineEditScreen;
import fr.frinn.custommachinery.client.screen.popup.PopupScreen;
import java.util.function.Consumer;
import net.minecraft.client.gui.GuiGraphics;
import org.jetbrains.annotations.Nullable;

public interface IMachineComponentBuilder<C extends IMachineComponent, T extends IMachineComponentTemplate<C>> {

    MachineComponentType<C> type();

    PopupScreen makePopup(MachineEditScreen var1, @Nullable T var2, Consumer<T> var3);

    void render(GuiGraphics var1, int var2, int var3, int var4, int var5, T var6);
}
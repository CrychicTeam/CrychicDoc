package fr.frinn.custommachinery.client.screen.creation.appearance;

import fr.frinn.custommachinery.api.machine.MachineAppearanceProperty;
import fr.frinn.custommachinery.client.screen.BaseScreen;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;

public interface IAppearancePropertyBuilder<T> {

    Component title();

    MachineAppearanceProperty<T> getType();

    AbstractWidget makeWidget(BaseScreen var1, int var2, int var3, int var4, int var5, Supplier<T> var6, Consumer<T> var7);
}
package org.violetmoon.zeta.client.config.definition;

import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.zeta.client.ZetaClient;
import org.violetmoon.zeta.client.config.screen.AbstractEditBoxInputScreen;
import org.violetmoon.zeta.client.config.widget.PencilButton;
import org.violetmoon.zeta.config.ChangeSet;
import org.violetmoon.zeta.config.ValueDefinition;

public class IntegerClientDefinition implements ClientDefinitionExt<ValueDefinition<Integer>> {

    public String getSubtitle(ChangeSet changes, ValueDefinition<Integer> def) {
        return Integer.toString(changes.get(def));
    }

    public void addWidgets(ZetaClient zc, Screen parent, ChangeSet changes, ValueDefinition<Integer> def, Consumer<AbstractWidget> widgets) {
        Screen newScreen = new AbstractEditBoxInputScreen<Integer>(zc, parent, changes, def) {

            @Nullable
            protected Integer fromString(String string) {
                try {
                    return Integer.parseInt(string);
                } catch (Exception var3) {
                    return null;
                }
            }
        };
        widgets.accept(new PencilButton(zc, 230, 3, b -> Minecraft.getInstance().setScreen(newScreen)));
    }
}
package org.violetmoon.zeta.client.config.definition;

import java.util.function.Consumer;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;
import org.violetmoon.zeta.client.ZetaClient;
import org.violetmoon.zeta.config.ChangeSet;
import org.violetmoon.zeta.config.Definition;

public interface ClientDefinitionExt<T extends Definition> {

    String getSubtitle(ChangeSet var1, T var2);

    void addWidgets(ZetaClient var1, Screen var2, ChangeSet var3, T var4, Consumer<AbstractWidget> var5);

    default String truncate(String in) {
        return in.length() > 30 ? in.substring(0, 27) + "..." : in;
    }

    public static class Default implements ClientDefinitionExt<Definition> {

        @Override
        public String getSubtitle(ChangeSet changes, Definition def) {
            return "";
        }

        @Override
        public void addWidgets(ZetaClient zc, Screen parent, ChangeSet changes, Definition def, Consumer<AbstractWidget> widgets) {
        }
    }
}
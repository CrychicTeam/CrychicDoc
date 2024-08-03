package de.keksuccino.fancymenu.customization.action;

import de.keksuccino.fancymenu.util.properties.PropertyContainer;
import java.util.Map.Entry;
import org.jetbrains.annotations.NotNull;

public interface Executable {

    void execute();

    @NotNull
    String getIdentifier();

    @NotNull
    Executable copy(boolean var1);

    @NotNull
    PropertyContainer serialize();

    default void serializeToExistingPropertyContainer(@NotNull PropertyContainer container) {
        PropertyContainer c = this.serialize();
        for (Entry<String, String> m : c.getProperties().entrySet()) {
            container.putProperty((String) m.getKey(), (String) m.getValue());
        }
    }
}
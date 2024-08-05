package de.keksuccino.fancymenu.customization.background;

import de.keksuccino.fancymenu.customization.layout.editor.LayoutEditorScreen;
import java.util.function.Consumer;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class MenuBackgroundBuilder<T extends MenuBackground> {

    private static final Logger LOGGER = LogManager.getLogger();

    private final String identifier;

    public MenuBackgroundBuilder(String uniqueIdentifier) {
        this.identifier = uniqueIdentifier;
    }

    public boolean isDeprecated() {
        return false;
    }

    public boolean shouldShowUpInEditorBackgroundMenu(@NotNull LayoutEditorScreen editor) {
        return true;
    }

    public abstract void buildNewOrEditInstance(@Nullable Screen var1, @Nullable T var2, @NotNull Consumer<T> var3);

    public void buildNewOrEditInstanceInternal(@Nullable Screen currentScreen, @Nullable MenuBackground backgroundToEdit, Consumer<MenuBackground> backgroundConsumer) {
        try {
            this.buildNewOrEditInstance(currentScreen, (T) backgroundToEdit, (Consumer<T>) backgroundConsumer);
        } catch (Exception var5) {
            var5.printStackTrace();
        }
    }

    public abstract T deserializeBackground(SerializedMenuBackground var1);

    @Nullable
    public T deserializeBackgroundInternal(SerializedMenuBackground serializedMenuBackground) {
        try {
            return this.deserializeBackground(serializedMenuBackground);
        } catch (Exception var3) {
            LOGGER.error("[FANCYMENU] Failed to deserialize menu background: " + this.getIdentifier());
            var3.printStackTrace();
            return null;
        }
    }

    public abstract SerializedMenuBackground serializedBackground(T var1);

    @Nullable
    public SerializedMenuBackground serializedBackgroundInternal(MenuBackground background) {
        try {
            SerializedMenuBackground b = this.serializedBackground((T) background);
            b.putProperty("background_type", this.getIdentifier());
            return b;
        } catch (Exception var3) {
            LOGGER.error("[FANCYMENU] Failed to serialize menu background: " + this.getIdentifier());
            var3.printStackTrace();
            return null;
        }
    }

    @NotNull
    public String getIdentifier() {
        return this.identifier;
    }

    @NotNull
    public abstract Component getDisplayName();

    @Nullable
    public abstract Component[] getDescription();
}
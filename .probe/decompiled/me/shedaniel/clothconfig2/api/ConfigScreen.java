package me.shedaniel.clothconfig2.api;

import java.util.Iterator;
import java.util.function.Consumer;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public interface ConfigScreen {

    void setSavingRunnable(@Nullable Runnable var1);

    void setAfterInitConsumer(@Nullable Consumer<Screen> var1);

    ResourceLocation getBackgroundLocation();

    boolean isRequiresRestart();

    boolean isEdited();

    void saveAll(boolean var1);

    void addTooltip(Tooltip var1);

    boolean matchesSearch(Iterator<String> var1);
}
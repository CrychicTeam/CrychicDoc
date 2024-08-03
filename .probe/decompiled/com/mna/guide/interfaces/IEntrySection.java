package com.mna.guide.interfaces;

import com.google.gson.JsonObject;
import java.util.Collection;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;

public interface IEntrySection {

    Collection<IEntrySection> parse(JsonObject var1, int var2, int var3, int var4, int var5);

    int getHeight(int var1);

    int getWidth(int var1);

    boolean canWrap();

    boolean newPage();

    int getPage();

    void setPage(int var1);

    void setPadding(int var1);

    int getOverrideColor();

    void setOverrideColor(int var1);

    boolean isBaseMna();

    void setNotBaseMna();

    NonNullList<Component> getTooltip();

    void setGuiPos(int var1, int var2);

    @Nullable
    Collection<AbstractWidget> getWidgets(AbstractContainerScreen<?> var1, int var2, int var3, int var4, int var5, Consumer<List<Component>> var6, BiConsumer<String, Boolean> var7, BiConsumer<String, Boolean> var8);
}
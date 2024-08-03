package com.mna.guide.sections;

import com.google.gson.JsonObject;
import com.mna.gui.widgets.guide.ImageWidget;
import com.mna.guide.interfaces.IEntrySection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ImageSection extends SectionBase {

    private ResourceLocation imageTextureLocation;

    private int width;

    private int height;

    private int padding = 5;

    @Override
    public Collection<IEntrySection> parse(JsonObject element, int startY, int maxHeight, int maxWidth, int page) {
        this.imageTextureLocation = new ResourceLocation(element.get("location").getAsString());
        this.width = Math.min(element.get("width").getAsInt(), maxWidth);
        this.height = Math.min(element.get("height").getAsInt(), maxHeight);
        if (startY + this.height > maxHeight) {
            this.setPage(page + 1);
        } else {
            this.setPage(page);
        }
        NonNullList<IEntrySection> output = NonNullList.create();
        output.add(this);
        return output;
    }

    @Override
    public Collection<AbstractWidget> getWidgets(AbstractContainerScreen<?> screen, int sectionX, int sectionY, int maxWidth, int maxHeight, Consumer<List<Component>> tooltipFunction, BiConsumer<String, Boolean> showRecipe, BiConsumer<String, Boolean> showEntry) {
        ArrayList<AbstractWidget> widgets = new ArrayList();
        int x = sectionX + (maxWidth - this.width) / 2;
        widgets.add(new ImageWidget(x, sectionY, this.width, this.height, this.imageTextureLocation, this.tooltip, tooltipFunction));
        return widgets;
    }

    @Override
    public int getHeight(int maxHeight) {
        return this.height + this.padding;
    }

    @Override
    public int getWidth(int maxWidth) {
        return this.width;
    }

    @Override
    public boolean newPage() {
        return false;
    }

    @Override
    public void setPadding(int i) {
        this.padding = i;
    }
}
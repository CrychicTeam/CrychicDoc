package com.mna.guide.sections;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mna.gui.widgets.guide.ItemGridWidget;
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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.mutable.MutableInt;

@OnlyIn(Dist.CLIENT)
public class ItemSection extends SectionBase {

    private float scale;

    private int padding = 2;

    private int items_per_row = 4;

    protected boolean newPage = false;

    private ItemStack[] renderStacks;

    @Override
    public Collection<IEntrySection> parse(JsonObject element, int startY, int maxHeight, int maxWidth, int page) {
        this.setPage(page);
        if (element.has("location")) {
            this.renderStacks = new ItemStack[] { this.parseStackElement(element.get("location")) };
        } else if (element.has("locations")) {
            JsonArray elems = element.getAsJsonArray("locations");
            this.renderStacks = new ItemStack[elems.size()];
            MutableInt count = new MutableInt(0);
            elems.forEach(elem -> this.renderStacks[count.getAndIncrement()] = this.parseStackElement(elem));
        }
        if (element.has("scale")) {
            this.scale = Math.min(element.get("scale").getAsFloat(), 3.0F);
        } else {
            this.scale = 1.0F;
        }
        if (element.has("items_per_row")) {
            this.items_per_row = element.get("items_per_row").getAsInt();
        }
        if (element.has("newPage")) {
            this.newPage = element.get("newPage").getAsBoolean();
        }
        NonNullList<IEntrySection> output = NonNullList.create();
        output.add(this);
        return output;
    }

    private ItemStack parseStackElement(JsonElement element) {
        Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(element.getAsString()));
        return item != null ? new ItemStack(item) : ItemStack.EMPTY;
    }

    @Override
    public Collection<AbstractWidget> getWidgets(AbstractContainerScreen<?> screen, int sectionX, int sectionY, int maxWidth, int maxHeight, Consumer<List<Component>> tooltipFunction, BiConsumer<String, Boolean> showRecipe, BiConsumer<String, Boolean> showEntry) {
        ArrayList<AbstractWidget> widgets = new ArrayList();
        widgets.add(new ItemGridWidget(sectionX, sectionY, maxWidth, this.getHeight(maxHeight), this.scale, this.renderStacks, tooltipFunction));
        return widgets;
    }

    @Override
    public int getHeight(int maxHeight) {
        if (this.renderStacks != null && this.renderStacks.length != 0) {
            int height_from_items = 16 * Math.max(1, (int) Math.floor((double) ((float) this.renderStacks.length / (float) this.items_per_row)));
            int height_from_padding = this.padding * Math.max(0, (int) Math.floor((double) ((float) this.renderStacks.length / (float) this.items_per_row)) - 1);
            return (int) ((float) (height_from_items + height_from_padding) * this.scale) + 5;
        } else {
            return 0;
        }
    }

    @Override
    public int getWidth(int maxWidth) {
        if (this.renderStacks != null && this.renderStacks.length != 0) {
            int row_items = Math.min(this.items_per_row, this.renderStacks.length);
            int width_from_items = row_items * 16;
            int width_from_padding = Math.max(0, row_items - 1) * this.padding;
            return (int) ((float) (width_from_items + width_from_padding) * this.scale);
        } else {
            return 0;
        }
    }

    @Override
    public boolean newPage() {
        return this.newPage;
    }

    @Override
    public void setPadding(int i) {
        this.padding = i;
    }
}
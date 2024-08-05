package com.github.alexmodguy.alexscaves.client.gui.book.widget;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;

public abstract class BookWidget {

    @Expose
    @SerializedName("display_page")
    private int displayPage;

    @Expose
    private BookWidget.Type type;

    @Expose
    private int x;

    @Expose
    private int y;

    @Expose
    private float scale;

    public BookWidget(int displayPage, BookWidget.Type type, int x, int y, float scale) {
        this.displayPage = displayPage;
        this.type = type;
        this.x = x;
        this.y = y;
        this.scale = scale;
    }

    public int getDisplayPage() {
        return this.displayPage;
    }

    public BookWidget.Type getType() {
        return this.type;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public float getScale() {
        return this.scale;
    }

    public abstract void render(PoseStack var1, MultiBufferSource.BufferSource var2, float var3, boolean var4);

    public static enum Type {

        @SerializedName("image")
        IMAGE(ImageWidget.class), @SerializedName("item")
        ITEM(ItemWidget.class), @SerializedName("entity")
        ENTITY(EntityWidget.class), @SerializedName("entity_box")
        ENTITY_BOX(EntityBoxWidget.class), @SerializedName("crafting_recipe")
        CRAFTING_RECIPE(CraftingRecipeWidget.class);

        private final Class<? extends BookWidget> widgetClass;

        private Type(Class<? extends BookWidget> widgetClass) {
            this.widgetClass = widgetClass;
        }

        public Class<? extends BookWidget> getWidgetClass() {
            return this.widgetClass;
        }
    }
}
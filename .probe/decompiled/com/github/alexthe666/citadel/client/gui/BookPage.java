package com.github.alexthe666.citadel.client.gui;

import com.github.alexthe666.citadel.client.gui.data.EntityLinkData;
import com.github.alexthe666.citadel.client.gui.data.EntityRenderData;
import com.github.alexthe666.citadel.client.gui.data.ImageData;
import com.github.alexthe666.citadel.client.gui.data.ItemRenderData;
import com.github.alexthe666.citadel.client.gui.data.LinkData;
import com.github.alexthe666.citadel.client.gui.data.RecipeData;
import com.github.alexthe666.citadel.client.gui.data.TabulaRenderData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.util.GsonHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BookPage {

    public static final Gson GSON = new GsonBuilder().registerTypeAdapter(BookPage.class, new BookPage.Deserializer()).create();

    public String translatableTitle = null;

    private String parent = "";

    private String textFileToReadFrom = "";

    private List<LinkData> linkedButtons = new ArrayList();

    private List<EntityLinkData> linkedEntites = new ArrayList();

    private List<ItemRenderData> itemRenders = new ArrayList();

    private List<RecipeData> recipes = new ArrayList();

    private List<TabulaRenderData> tabulaRenders = new ArrayList();

    private List<EntityRenderData> entityRenders = new ArrayList();

    private List<ImageData> images = new ArrayList();

    private final String title;

    public BookPage(String parent, String textFileToReadFrom, List<LinkData> linkedButtons, List<EntityLinkData> linkedEntities, List<ItemRenderData> itemRenders, List<RecipeData> recipes, List<TabulaRenderData> tabulaRenders, List<EntityRenderData> entityRenders, List<ImageData> images, String title) {
        this.parent = parent;
        this.textFileToReadFrom = textFileToReadFrom;
        this.linkedButtons = linkedButtons;
        this.itemRenders = itemRenders;
        this.linkedEntites = linkedEntities;
        this.recipes = recipes;
        this.tabulaRenders = tabulaRenders;
        this.entityRenders = entityRenders;
        this.images = images;
        this.title = title;
    }

    public static BookPage deserialize(Reader readerIn) {
        return GsonHelper.fromJson(GSON, readerIn, BookPage.class);
    }

    public static BookPage deserialize(String jsonString) {
        return deserialize(new StringReader(jsonString));
    }

    public String getParent() {
        return this.parent;
    }

    public String getTitle() {
        return this.title;
    }

    public String getTextFileToReadFrom() {
        return this.textFileToReadFrom;
    }

    public List<LinkData> getLinkedButtons() {
        return this.linkedButtons;
    }

    public List<EntityLinkData> getLinkedEntities() {
        return this.linkedEntites;
    }

    public List<ItemRenderData> getItemRenders() {
        return this.itemRenders;
    }

    public List<RecipeData> getRecipes() {
        return this.recipes;
    }

    public List<TabulaRenderData> getTabulaRenders() {
        return this.tabulaRenders;
    }

    public List<EntityRenderData> getEntityRenders() {
        return this.entityRenders;
    }

    public List<ImageData> getImages() {
        return this.images;
    }

    public String generateTitle() {
        return this.translatableTitle != null ? I18n.get(this.translatableTitle) : this.title;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Deserializer implements JsonDeserializer<BookPage> {

        public BookPage deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_) throws JsonParseException {
            JsonObject jsonobject = GsonHelper.convertToJsonObject(p_deserialize_1_, "book page");
            LinkData[] linkedPageRead = GsonHelper.getAsObject(jsonobject, "linked_page_buttons", new LinkData[0], p_deserialize_3_, LinkData[].class);
            EntityLinkData[] linkedEntitesRead = GsonHelper.getAsObject(jsonobject, "entity_buttons", new EntityLinkData[0], p_deserialize_3_, EntityLinkData[].class);
            ItemRenderData[] itemRendersRead = GsonHelper.getAsObject(jsonobject, "item_renders", new ItemRenderData[0], p_deserialize_3_, ItemRenderData[].class);
            RecipeData[] recipesRead = GsonHelper.getAsObject(jsonobject, "recipes", new RecipeData[0], p_deserialize_3_, RecipeData[].class);
            TabulaRenderData[] tabulaRendersRead = GsonHelper.getAsObject(jsonobject, "tabula_renders", new TabulaRenderData[0], p_deserialize_3_, TabulaRenderData[].class);
            EntityRenderData[] entityRendersRead = GsonHelper.getAsObject(jsonobject, "entity_renders", new EntityRenderData[0], p_deserialize_3_, EntityRenderData[].class);
            ImageData[] imagesRead = GsonHelper.getAsObject(jsonobject, "images", new ImageData[0], p_deserialize_3_, ImageData[].class);
            String readParent = "";
            if (jsonobject.has("parent")) {
                readParent = GsonHelper.getAsString(jsonobject, "parent");
            }
            String readTextFile = "";
            if (jsonobject.has("text")) {
                readTextFile = GsonHelper.getAsString(jsonobject, "text");
            }
            String title = "";
            if (jsonobject.has("title")) {
                title = GsonHelper.getAsString(jsonobject, "title");
            }
            BookPage page = new BookPage(readParent, readTextFile, Arrays.asList(linkedPageRead), Arrays.asList(linkedEntitesRead), Arrays.asList(itemRendersRead), Arrays.asList(recipesRead), Arrays.asList(tabulaRendersRead), Arrays.asList(entityRendersRead), Arrays.asList(imagesRead), title);
            if (jsonobject.has("title")) {
                page.translatableTitle = GsonHelper.getAsString(jsonobject, "title");
            }
            return page;
        }
    }
}
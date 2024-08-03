package com.mna.guide;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mna.ManaAndArtifice;
import com.mna.guide.interfaces.IEntrySection;
import com.mna.guide.sections.ImageSection;
import com.mna.guide.sections.ItemSection;
import com.mna.guide.sections.RecipeSection;
import com.mna.guide.sections.TextSection;
import com.mna.guide.sections.TitleSection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;

public class GuidebookEntry {

    private static final String KEY_CATEGORY = "category";

    private static final String KEY_TIER = "tier";

    private static final String KEY_INDEX = "index";

    private static final String KEY_SECTIONS = "sections";

    private static final String KEY_SECTION_TYPE = "type";

    private static final String KEY_RELATED_RECIPES = "related_recipes";

    private static final int MAX_RELATED_RECIPES = 10;

    private static final int COLOR_NON_MNA = 6295964;

    private static final HashMap<String, Class<? extends IEntrySection>> typeRegistry = new HashMap<String, Class<? extends IEntrySection>>() {

        {
            this.put("text", TextSection.class);
            this.put("image", ImageSection.class);
            this.put("title", TitleSection.class);
            this.put("item", ItemSection.class);
            this.put("recipe", RecipeSection.class);
        }
    };

    private final NonNullList<IEntrySection> sections;

    private final NonNullList<RelatedRecipe> relatedRecipes;

    private final boolean isValid;

    private final EntryCategory category;

    private final Optional<Integer> tier;

    private final int index;

    private final String name;

    private final String modid;

    int y = 10;

    int x_max = 108;

    int y_max = 168;

    int page = 0;

    public GuidebookEntry(JsonObject entryObject, String name, String modid) {
        boolean valid = true;
        this.name = name;
        this.modid = modid;
        this.sections = NonNullList.create();
        this.relatedRecipes = NonNullList.create();
        valid = this.parseSections(entryObject);
        valid = this.parseRelatedRecipes(entryObject);
        this.category = this.parseCategory(entryObject);
        this.tier = this.parseTier(entryObject);
        this.index = this.parseIndex(entryObject);
        if (this.getFirstTitle() == null) {
            ManaAndArtifice.LOGGER.error(String.format("Entry '%s' of the codex is missing the required title section (must have at least one)", name));
            valid = false;
        }
        this.isValid = valid;
    }

    private boolean parseSections(JsonObject entryObject) {
        boolean added_addedby = this.isBaseMnA();
        if (!entryObject.has("sections")) {
            return false;
        } else {
            for (JsonElement elem : entryObject.get("sections").getAsJsonArray()) {
                if (elem.isJsonObject()) {
                    JsonObject elemObj = elem.getAsJsonObject();
                    if (elemObj.has("type")) {
                        String type = elemObj.get("type").getAsString();
                        Class<? extends IEntrySection> clazz = (Class<? extends IEntrySection>) typeRegistry.get(type);
                        try {
                            IEntrySection section = (IEntrySection) clazz.getConstructor().newInstance();
                            Collection<IEntrySection> parsed_sections = section.parse(elemObj, this.y, this.y_max, this.x_max, this.page);
                            this.sections.addAll(parsed_sections);
                            if (!added_addedby && this.sections.get(0) instanceof TitleSection) {
                                ((TitleSection) this.sections.get(0)).setOverrideColor(6295964);
                                Optional<? extends ModContainer> otherModContainer = ModList.get().getModContainerById(this.modid);
                                if (!otherModContainer.isPresent()) {
                                    ((TitleSection) this.sections.get(0)).getTooltip().add(Component.translatable("item.mna.guide_book.othermod.unknown"));
                                } else {
                                    ((TitleSection) this.sections.get(0)).getTooltip().add(Component.translatable("item.mna.guide_book.othermod", ((ModContainer) otherModContainer.get()).getModInfo().getDisplayName()));
                                }
                                added_addedby = true;
                            }
                            for (IEntrySection s : parsed_sections) {
                                if (!this.isBaseMnA()) {
                                    s.setNotBaseMna();
                                }
                                if (s.getPage() > this.page) {
                                    this.page = s.getPage();
                                    this.y = s.getHeight(this.y_max);
                                } else {
                                    this.y = this.y + s.getHeight(this.y_max);
                                }
                            }
                        } catch (Exception var13) {
                        }
                    }
                }
            }
            this.sections.get(this.sections.size() - 1).setPadding(0);
            return true;
        }
    }

    private boolean parseRelatedRecipes(JsonObject entryObject) {
        if (entryObject.has("related_recipes")) {
            JsonElement rr = entryObject.get("related_recipes");
            if (rr.isJsonArray()) {
                for (JsonElement elem : rr.getAsJsonArray()) {
                    if (elem.isJsonObject()) {
                        JsonObject obj = elem.getAsJsonObject();
                        String type = "crafting";
                        ResourceLocation[] rLocs = new ResourceLocation[1];
                        if (obj.has("type")) {
                            type = obj.get("type").getAsString();
                        }
                        if (obj.has("locations")) {
                            rLocs = this.parseResourceLocations(obj.get("locations").getAsJsonArray());
                        } else {
                            rLocs[0] = new ResourceLocation(obj.get("location").getAsString());
                        }
                        this.relatedRecipes.add(new RelatedRecipe(type, rLocs));
                        if (this.relatedRecipes.size() >= 10) {
                            break;
                        }
                    }
                }
            }
        }
        return true;
    }

    private EntryCategory parseCategory(JsonObject entryObject) {
        if (entryObject.has("category")) {
            String c = entryObject.get("category").getAsString();
            EntryCategory eCat;
            try {
                eCat = EntryCategory.valueOf(c.toUpperCase());
            } catch (Exception var5) {
                eCat = EntryCategory.BASICS;
            }
            return eCat;
        } else {
            return EntryCategory.BASICS;
        }
    }

    private Optional<Integer> parseTier(JsonObject entryObject) {
        return entryObject.has("tier") ? Optional.of(entryObject.get("tier").getAsInt()) : Optional.empty();
    }

    private int parseIndex(JsonObject entryObject) {
        return entryObject.has("index") ? entryObject.get("index").getAsInt() : 0;
    }

    private ResourceLocation[] parseResourceLocations(JsonArray obj) {
        ArrayList<ResourceLocation> rLocs = new ArrayList();
        for (int i = 0; i < obj.size(); i++) {
            JsonElement elem = obj.get(i);
            if (elem.isJsonPrimitive()) {
                rLocs.add(new ResourceLocation(elem.getAsString()));
            }
        }
        return (ResourceLocation[]) rLocs.toArray(new ResourceLocation[0]);
    }

    public int getTier() {
        if (this.tier.isPresent()) {
            return (Integer) this.tier.get();
        } else if (this.relatedRecipes.size() == 0) {
            return 1;
        } else {
            Optional<RelatedRecipe> min = this.relatedRecipes.stream().min(Comparator.comparing(RelatedRecipe::getTier));
            return min.isPresent() ? ((RelatedRecipe) min.get()).getTier() : 1;
        }
    }

    public int getIndex() {
        return this.index;
    }

    public String getName() {
        return this.name;
    }

    @Nullable
    public String getFirstTitle() {
        TitleSection title = (TitleSection) this.sections.stream().filter(s -> s instanceof TitleSection).map(s -> (TitleSection) s).findFirst().get();
        return title == null ? null : title.getText();
    }

    public EntryCategory getCategory() {
        return this.category;
    }

    public boolean isValid() {
        return this.isValid;
    }

    public boolean isBaseMnA() {
        return this.modid == null || "mna".equals(this.modid);
    }

    public String getModId() {
        return this.modid;
    }

    public int getOverrideColor() {
        return this.sections.get(0).getOverrideColor();
    }

    public NonNullList<Component> getTooltip() {
        return this.sections.get(0).getTooltip();
    }

    public NonNullList<IEntrySection> getSections() {
        return this.sections;
    }

    public NonNullList<RelatedRecipe> getRelatedRecipes() {
        return this.relatedRecipes;
    }

    public boolean equals(Object obj) {
        return obj instanceof GuidebookEntry ? ((GuidebookEntry) obj).getName().equals(this.getName()) : false;
    }

    public String toString() {
        return this.getName();
    }
}
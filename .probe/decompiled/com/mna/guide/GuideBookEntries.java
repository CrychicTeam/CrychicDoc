package com.mna.guide;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mna.ManaAndArtifice;
import com.mna.api.guidebook.IGuideBookRegistry;
import com.mna.api.guidebook.RecipeRendererBase;
import com.mna.api.guidebook.RegisterGuidebooksEvent;
import com.mna.guide.recipe.init.RecipeRenderers;
import com.mna.guide.sections.TextSection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.common.MinecraftForge;

public class GuideBookEntries implements IGuideBookRegistry {

    public static final GuideBookEntries INSTANCE = new GuideBookEntries();

    private Map<String, GuidebookEntry> _entries;

    private List<ResourceLocation> guidebookPaths;

    private boolean hasLoaded = false;

    private String lastLanguage = "";

    private boolean lastUnicode = false;

    ResourceLocation _default = new ResourceLocation("mna", "guide/en_us.json");

    ResourceLocation _default_packadds = new ResourceLocation("mna", "guide/pack_adds/en_us.json");

    private GuideBookEntries() {
        this.guidebookPaths = new ArrayList();
        this._entries = new HashMap();
        this.guidebookPaths.add(new ResourceLocation("mna", "guide"));
        MinecraftForge.EVENT_BUS.post(new RegisterGuidebooksEvent(this));
    }

    public GuidebookEntry getEntry(String id) {
        return (GuidebookEntry) this._entries.get(id);
    }

    public Collection<GuidebookEntry> getEntries() {
        return this._entries.values();
    }

    public Collection<GuidebookEntry> getEntries(EntryCategory category) {
        ArrayList<GuidebookEntry> matches = new ArrayList();
        for (GuidebookEntry e : this._entries.values()) {
            if (e.getCategory() == category) {
                matches.add(e);
            }
        }
        return matches;
    }

    public Set<Entry<String, GuidebookEntry>> getAllEntries() {
        return this._entries.entrySet();
    }

    public List<GuideBookEntries.GuidebookSearchResult> searchEntries(String query, int tier, int limit) {
        String lowerQuery = query.toLowerCase();
        Minecraft m = Minecraft.getInstance();
        List<GuidebookEntry> matchingContents = (List<GuidebookEntry>) this._entries.values().stream().filter(e -> e.getTier() <= tier).filter(e -> e.getSections().stream().anyMatch(s -> s instanceof TextSection && ((TextSection) s).getRawText().toLowerCase().contains(lowerQuery))).collect(Collectors.toList());
        HashMap<RelatedRecipe, ItemStack> relatedRecipes = new HashMap();
        this._entries.values().forEach(e -> e.getRelatedRecipes().stream().filter(r -> r.getTier() <= tier).forEach(r -> {
            for (ItemStack is : r.getOutputItems(m.level)) {
                if (!is.isEmpty()) {
                    if (is.isEnchanted()) {
                        Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(is);
                        for (Entry<Enchantment, Integer> ench : map.entrySet()) {
                            if (((Enchantment) ench.getKey()).getFullname((Integer) ench.getValue()).getString().toLowerCase().contains(lowerQuery)) {
                                relatedRecipes.put(r, is);
                                break;
                            }
                        }
                    } else if (is.getHoverName().getString().toLowerCase().contains(lowerQuery)) {
                        relatedRecipes.put(r, is);
                        break;
                    }
                }
            }
        }));
        List<GuideBookEntries.GuidebookSearchResult> results = (List<GuideBookEntries.GuidebookSearchResult>) relatedRecipes.entrySet().stream().map(r -> new GuideBookEntries.GuidebookSearchResult((RelatedRecipe) r.getKey(), (ItemStack) r.getValue())).collect(Collectors.toList());
        results.addAll((Collection) matchingContents.stream().map(e -> new GuideBookEntries.GuidebookSearchResult(e)).collect(Collectors.toList()));
        return (List<GuideBookEntries.GuidebookSearchResult>) results.stream().filter(this.distinctByKey(GuideBookEntries.GuidebookSearchResult::toString)).limit((long) limit).collect(Collectors.toList());
    }

    private <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

    private void readEntries(ResourceManager resourceManager, ResourceLocation rLoc) {
        try {
            Optional<Resource> res = resourceManager.m_213713_(rLoc);
            if (res.isPresent()) {
                InputStream in = ((Resource) res.get()).open();
                if (in != null) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
                    if (reader != null) {
                        JsonElement obj = JsonParser.parseReader(reader);
                        if (obj != null && obj.isJsonObject()) {
                            this.parseJSON(obj.getAsJsonObject(), rLoc.getNamespace());
                        }
                        try {
                            reader.close();
                        } catch (IOException var8) {
                        }
                    }
                }
            }
        } catch (IOException var9) {
            ManaAndArtifice.LOGGER.catching(var9);
        }
    }

    private void parseJSON(JsonObject obj, String modid) {
        for (Entry<String, JsonElement> elem : obj.getAsJsonObject().entrySet()) {
            if (((JsonElement) elem.getValue()).isJsonObject()) {
                this._entries.put((String) elem.getKey(), new GuidebookEntry(((JsonElement) elem.getValue()).getAsJsonObject(), (String) elem.getKey(), modid));
            }
        }
    }

    private void readAndParseGuidebooks(ResourceManager resourceManager) {
        Minecraft mc = Minecraft.getInstance();
        for (ResourceLocation rLoc : this.guidebookPaths) {
            String guidebook_localized = String.format("%s.json", mc.options.languageCode);
            ResourceLocation rLoc_localized = new ResourceLocation(rLoc.getNamespace(), rLoc.getPath() + "/" + guidebook_localized);
            if (rLoc_localized.compareTo(this._default) != 0) {
                this.readEntries(resourceManager, rLoc_localized);
            }
            ResourceLocation rLoc_packAdds = new ResourceLocation(rLoc.getNamespace(), rLoc.getPath() + "/pack_adds/" + guidebook_localized);
            if (rLoc_packAdds.compareTo(this._default_packadds) != 0) {
                this.readEntries(resourceManager, rLoc_packAdds);
            }
        }
    }

    public void reload() {
        Minecraft mc = Minecraft.getInstance();
        if (!this.hasLoaded || this.lastLanguage != mc.options.languageCode || mc.isEnforceUnicode() != this.lastUnicode) {
            ResourceManager resourceManager = mc.getResourceManager();
            this._entries.clear();
            this.readEntries(resourceManager, this._default);
            this.readEntries(resourceManager, this._default_packadds);
            this.readAndParseGuidebooks(resourceManager);
            this.hasLoaded = true;
            this.lastLanguage = mc.options.languageCode;
            this.lastUnicode = mc.isEnforceUnicode();
        }
    }

    @Override
    public void addGuidebookPath(ResourceLocation resourceLocation) {
        this.guidebookPaths.add(resourceLocation);
    }

    @Override
    public void registerRecipeRenderer(String recipe_type, Class<? extends RecipeRendererBase> clazz) {
        RecipeRenderers.registerRecipeRenderer(recipe_type, clazz);
    }

    @Override
    public void registerGuidebookCategory(String id, ResourceLocation icon) {
        EntryCategory.Register(id, icon);
    }

    public class GuidebookSearchResult {

        private GuidebookEntry _entry;

        private RelatedRecipe _recipe;

        private ItemStack _output;

        public GuidebookSearchResult(GuidebookEntry entry) {
            this._entry = entry;
            this._output = ItemStack.EMPTY;
        }

        public GuidebookSearchResult(RelatedRecipe recipe, ItemStack output) {
            this._recipe = recipe;
            this._output = output;
        }

        @Nullable
        public GuidebookEntry getEntry() {
            return this._entry;
        }

        public boolean isEntry() {
            return this._entry != null;
        }

        @Nullable
        public RelatedRecipe getRecipe() {
            return this._recipe;
        }

        public ItemStack getOutputStack() {
            return this._output;
        }

        public boolean isRecipe() {
            return this._recipe != null;
        }

        public boolean equals(Object obj) {
            if (obj instanceof GuideBookEntries.GuidebookSearchResult other) {
                if (this.isEntry() && other.isEntry()) {
                    return this.getEntry().equals(other.getEntry());
                }
                if (this.isRecipe() && other.isRecipe()) {
                    return this.getRecipe().equals(other.getRecipe());
                }
            }
            return false;
        }

        public String toString() {
            return this.isEntry() ? this._entry.toString() : this._recipe.toString();
        }
    }
}
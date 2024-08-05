package vazkii.patchouli.client.book;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.GsonHelper;
import org.jetbrains.annotations.Nullable;
import vazkii.patchouli.client.book.page.PageBlasting;
import vazkii.patchouli.client.book.page.PageCampfireCooking;
import vazkii.patchouli.client.book.page.PageCrafting;
import vazkii.patchouli.client.book.page.PageEmpty;
import vazkii.patchouli.client.book.page.PageEntity;
import vazkii.patchouli.client.book.page.PageImage;
import vazkii.patchouli.client.book.page.PageLink;
import vazkii.patchouli.client.book.page.PageMultiblock;
import vazkii.patchouli.client.book.page.PageQuest;
import vazkii.patchouli.client.book.page.PageRelations;
import vazkii.patchouli.client.book.page.PageSmelting;
import vazkii.patchouli.client.book.page.PageSmithing;
import vazkii.patchouli.client.book.page.PageSmoking;
import vazkii.patchouli.client.book.page.PageSpotlight;
import vazkii.patchouli.client.book.page.PageStonecutting;
import vazkii.patchouli.client.book.page.PageTemplate;
import vazkii.patchouli.client.book.page.PageText;
import vazkii.patchouli.client.book.template.BookTemplate;
import vazkii.patchouli.client.book.template.TemplateComponent;
import vazkii.patchouli.common.base.PatchouliSounds;
import vazkii.patchouli.common.book.Book;
import vazkii.patchouli.common.book.BookRegistry;
import vazkii.patchouli.common.util.SerializationUtil;

public class ClientBookRegistry {

    public final Map<ResourceLocation, Class<? extends BookPage>> pageTypes = new HashMap();

    public final Gson gson = new GsonBuilder().registerTypeHierarchyAdapter(BookPage.class, new ClientBookRegistry.LexiconPageAdapter()).registerTypeHierarchyAdapter(TemplateComponent.class, new ClientBookRegistry.TemplateComponentAdapter()).create();

    public String currentLang;

    public static final ClientBookRegistry INSTANCE = new ClientBookRegistry();

    private ClientBookRegistry() {
    }

    public void init() {
        this.addPageTypes();
    }

    private void addPageTypes() {
        this.pageTypes.put(new ResourceLocation("patchouli", "text"), PageText.class);
        this.pageTypes.put(new ResourceLocation("patchouli", "crafting"), PageCrafting.class);
        this.pageTypes.put(new ResourceLocation("patchouli", "smelting"), PageSmelting.class);
        this.pageTypes.put(new ResourceLocation("patchouli", "blasting"), PageBlasting.class);
        this.pageTypes.put(new ResourceLocation("patchouli", "smoking"), PageSmoking.class);
        this.pageTypes.put(new ResourceLocation("patchouli", "campfire"), PageCampfireCooking.class);
        this.pageTypes.put(new ResourceLocation("patchouli", "smithing"), PageSmithing.class);
        this.pageTypes.put(new ResourceLocation("patchouli", "stonecutting"), PageStonecutting.class);
        this.pageTypes.put(new ResourceLocation("patchouli", "image"), PageImage.class);
        this.pageTypes.put(new ResourceLocation("patchouli", "spotlight"), PageSpotlight.class);
        this.pageTypes.put(new ResourceLocation("patchouli", "empty"), PageEmpty.class);
        this.pageTypes.put(new ResourceLocation("patchouli", "multiblock"), PageMultiblock.class);
        this.pageTypes.put(new ResourceLocation("patchouli", "link"), PageLink.class);
        this.pageTypes.put(new ResourceLocation("patchouli", "relations"), PageRelations.class);
        this.pageTypes.put(new ResourceLocation("patchouli", "entity"), PageEntity.class);
        this.pageTypes.put(new ResourceLocation("patchouli", "quest"), PageQuest.class);
    }

    public void reload() {
        this.currentLang = Minecraft.getInstance().getLanguageManager().getSelected();
        BookRegistry.INSTANCE.reloadContents(Minecraft.getInstance().level);
    }

    public void reloadLocks(boolean suppressToasts) {
        BookRegistry.INSTANCE.books.values().forEach(b -> b.reloadLocks(suppressToasts));
    }

    public void displayBookGui(ResourceLocation bookStr, @Nullable ResourceLocation entryId, int page) {
        Minecraft mc = Minecraft.getInstance();
        this.currentLang = mc.getLanguageManager().getSelected();
        Book book = (Book) BookRegistry.INSTANCE.books.get(bookStr);
        if (book != null) {
            book.getContents().checkValidCurrentEntry();
            if (entryId != null) {
                book.getContents().setTopEntry(entryId, page);
            }
            book.getContents().openLexiconGui(book.getContents().getCurrentGui(), false);
            if (mc.player != null) {
                SoundEvent sfx = PatchouliSounds.getSound(book.openSound, PatchouliSounds.BOOK_OPEN);
                mc.player.playSound(sfx, 1.0F, (float) (0.7 + Math.random() * 0.4));
            }
        }
    }

    public static class LexiconPageAdapter implements JsonDeserializer<BookPage> {

        public BookPage deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if (json instanceof JsonPrimitive prim && prim.isString()) {
                PageText out = new PageText();
                out.setText(prim.getAsString());
                return out;
            }
            JsonObject obj = json.getAsJsonObject();
            String string = GsonHelper.getAsString(obj, "type");
            if (string.indexOf(58) < 0) {
                string = "patchouli:" + string;
            }
            ResourceLocation type = new ResourceLocation(string);
            Class<? extends BookPage> clazz = (Class<? extends BookPage>) ClientBookRegistry.INSTANCE.pageTypes.get(type);
            if (clazz == null) {
                clazz = PageTemplate.class;
            }
            BookPage page = (BookPage) SerializationUtil.RAW_GSON.fromJson(json, clazz);
            page.sourceObject = obj;
            return page;
        }
    }

    public static class TemplateComponentAdapter implements JsonDeserializer<TemplateComponent> {

        public TemplateComponent deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject obj = json.getAsJsonObject();
            JsonPrimitive prim = (JsonPrimitive) obj.get("type");
            ResourceLocation type = new ResourceLocation(prim.getAsString());
            Class<? extends TemplateComponent> clazz = (Class<? extends TemplateComponent>) BookTemplate.componentTypes.get(type);
            if (clazz == null) {
                return null;
            } else {
                TemplateComponent component = (TemplateComponent) SerializationUtil.RAW_GSON.fromJson(json, clazz);
                component.sourceObject = obj;
                return component;
            }
        }
    }
}
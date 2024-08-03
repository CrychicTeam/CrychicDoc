package com.github.alexmodguy.alexscaves.client.gui.book;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.github.alexmodguy.alexscaves.client.gui.book.widget.BookWidget;
import com.github.alexmodguy.alexscaves.server.misc.CaveBookProgress;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.Expose;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import org.apache.commons.io.IOUtils;

public class BookEntry {

    public static final Gson GSON = new GsonBuilder().registerTypeAdapter(BookEntry.class, new BookEntry.Deserializer()).excludeFieldsWithoutExposeAnnotation().create();

    private static Pattern pattern = Pattern.compile("\\{.*?\\}");

    @Expose
    private String translatableTitle;

    @Expose
    private String parent;

    @Expose
    private String textFileToReadFrom;

    @Expose
    private String requiredProgress;

    @Expose
    private BookWidget[] widgets;

    private List<String> entryText = new ArrayList();

    private List<BookLink> bookLinks = new ArrayList();

    private int pageCount = 0;

    public BookEntry(String translatableTitle, String parent, String textFileToReadFrom, String requiredProgress, BookWidget[] widgets) {
        this.translatableTitle = translatableTitle;
        this.parent = parent;
        this.textFileToReadFrom = textFileToReadFrom;
        this.requiredProgress = requiredProgress;
        this.widgets = widgets;
    }

    public static BookEntry deserialize(Reader readerIn) {
        return GsonHelper.fromJson(GSON, readerIn, BookEntry.class);
    }

    public String getTranslatableTitle() {
        return this.translatableTitle;
    }

    public String getParent() {
        return this.parent;
    }

    public List<String> getEntryText() {
        return this.entryText;
    }

    public List<BookLink> getEntryLinks() {
        return this.bookLinks;
    }

    public BookWidget[] getWidgets() {
        return this.widgets;
    }

    public int getPageCount() {
        return this.pageCount;
    }

    public void init(CaveBookScreen screen) {
        this.entryText = this.getRawTextFromFile(this.textFileToReadFrom, screen, 30);
        this.pageCount = (int) Math.ceil((double) ((float) this.entryText.size() / 30.0F));
    }

    private List<String> getRawTextFromFile(String fileName, CaveBookScreen screen, int maxLineSize) {
        String lang = Minecraft.getInstance().getLanguageManager().getSelected().toLowerCase();
        ResourceLocation fileRes;
        try {
            fileRes = new ResourceLocation(CaveBookScreen.getBookFileDirectory() + lang + "/" + fileName);
            InputStream is = Minecraft.getInstance().getResourceManager().m_215595_(fileRes);
            is.close();
        } catch (Exception var20) {
            AlexsCaves.LOGGER.warn("Could not find language file for translation, defaulting to en_us");
            fileRes = new ResourceLocation(CaveBookScreen.getBookFileDirectory() + "en_us/" + fileName);
        }
        List<String> strings = new ArrayList();
        Font font = Minecraft.getInstance().font;
        try {
            BufferedReader bufferedreader = Minecraft.getInstance().getResourceManager().m_215597_(fileRes);
            List<String> readIn = IOUtils.readLines(bufferedreader);
            int currentLineCount = 0;
            for (String readString : readIn) {
                Matcher m = pattern.matcher(readString);
                boolean skipLineEntirely = false;
                boolean noOverflow = false;
                while (m.find()) {
                    String[] found = m.group().split("\\|");
                    if (found.length >= 1) {
                        String linkTo = found[1].substring(0, found[1].length() - 1);
                        int visiblity = screen.getEntryVisiblity(linkTo);
                        String display = "";
                        if (visiblity != 2) {
                            display = visiblity == 0 ? found[0].substring(1) : "???";
                            this.bookLinks.add(new BookLink(currentLineCount, m.start(), display, linkTo, visiblity == 0));
                            readString = m.replaceFirst(display);
                        } else {
                            readString = display;
                            skipLineEntirely = true;
                        }
                        noOverflow = true;
                    }
                }
                if (readString.isEmpty() && !skipLineEntirely) {
                    strings.add(readString);
                    currentLineCount++;
                }
                while (font.width(readString) > maxLineSize) {
                    int spaceScanIndex = 0;
                    int lastSpace;
                    for (lastSpace = -1; spaceScanIndex < readString.length(); spaceScanIndex++) {
                        if (readString.charAt(spaceScanIndex) == ' ' && font.width(readString.substring(0, spaceScanIndex)) > 92) {
                            lastSpace = noOverflow ? readString.length() : spaceScanIndex;
                            break;
                        }
                    }
                    int cutIndex = lastSpace == -1 ? Math.min(maxLineSize, readString.length()) : lastSpace;
                    strings.add(readString.substring(0, cutIndex));
                    currentLineCount++;
                    readString = readString.substring(cutIndex);
                    if (readString.startsWith(" ")) {
                        readString = readString.substring(1);
                    }
                }
                if (!readString.isEmpty()) {
                    strings.add(readString);
                    currentLineCount++;
                }
            }
        } catch (Exception var21) {
            var21.printStackTrace();
        }
        return strings;
    }

    public void mouseOver(CaveBookScreen screen, int page, float mouseX, float mouseY) {
        boolean hoverFlag = false;
        screen.unlockTooltip = false;
        for (BookLink link : this.bookLinks) {
            int minLine = page * 15;
            link.setHovered(false);
            if (link.getLineNumber() >= minLine && link.getLineNumber() <= minLine + 30) {
                String line = (String) this.entryText.get(link.getLineNumber());
                boolean rightPage = link.getLineNumber() > minLine + 15;
                float textStartsX = rightPage ? 0.03F : -0.71F;
                float textsStartsY = -0.38F;
                float wordStartAt = textStartsX + (float) Minecraft.getInstance().font.width(line.substring(0, link.getCharacterStartsAt())) * 0.00475F;
                float wordEndAt = wordStartAt + (float) Minecraft.getInstance().font.width(link.getDisplayText()) * 0.005F;
                float wordTopAt = textsStartsY + (float) (link.getLineNumber() % 15) * 0.0425F;
                float wordBottomAt = wordTopAt + 0.05F;
                if (mouseX > wordStartAt && mouseX < wordEndAt && mouseY > wordTopAt && mouseY < wordBottomAt) {
                    if (link.isEnabled()) {
                        link.setHovered(!hoverFlag);
                        hoverFlag = true;
                    } else {
                        screen.unlockTooltip = true;
                    }
                }
            }
        }
    }

    public boolean consumeMouseClick(CaveBookScreen screen) {
        for (BookLink link : this.bookLinks) {
            int minLine = screen.getEntryPageNumber() * 15;
            if (link.isEnabled() && link.isHovered() && link.getLineNumber() >= minLine && link.getLineNumber() <= minLine + 30) {
                return screen.attemptChangePage(new ResourceLocation(CaveBookScreen.getBookFileDirectory() + link.getLinksTo()), true);
            }
        }
        return false;
    }

    public int getVisibility(CaveBookScreen caveBookScreen) {
        if (this.requiredProgress == null) {
            return 0;
        } else if (caveBookScreen.getCaveBookProgress().isUnlockedFor(this.requiredProgress)) {
            return 0;
        } else {
            CaveBookProgress.Subcategory subcategory = caveBookScreen.getCaveBookProgress().getSubcategoryFromPage(this.requiredProgress);
            return subcategory == CaveBookProgress.Subcategory.SECRETS ? 2 : 1;
        }
    }

    public static class Deserializer implements JsonDeserializer<BookEntry> {

        public BookEntry deserialize(JsonElement mainElement, Type deserializeType, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonobject = GsonHelper.convertToJsonObject(mainElement, "book entry");
            BookWidget[] bookWidgets = new BookWidget[0];
            if (jsonobject.has("widgets")) {
                JsonArray jsonArray = jsonobject.getAsJsonArray("widgets");
                bookWidgets = new BookWidget[jsonArray.size()];
                for (int i = 0; i < jsonArray.size(); i++) {
                    JsonObject widgetJson = jsonArray.get(i).getAsJsonObject();
                    BookWidget.Type type = GsonHelper.getAsObject(widgetJson, "type", context, BookWidget.Type.class);
                    bookWidgets[i] = GsonHelper.convertToObject(widgetJson, "", context, type.getWidgetClass());
                }
            }
            String parent = null;
            if (jsonobject.has("parent")) {
                parent = GsonHelper.getAsString(jsonobject, "parent");
            }
            String text = "";
            if (jsonobject.has("text")) {
                text = GsonHelper.getAsString(jsonobject, "text");
            }
            String title = "";
            if (jsonobject.has("title")) {
                title = GsonHelper.getAsString(jsonobject, "title");
            }
            String progress = null;
            if (jsonobject.has("required_progression")) {
                progress = GsonHelper.getAsString(jsonobject, "required_progression");
            }
            return new BookEntry(title, parent, text, progress, bookWidgets);
        }
    }
}
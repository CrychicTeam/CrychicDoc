package com.mna.guide.sections;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.mna.ManaAndArtifice;
import com.mna.gui.widgets.guide.TextWidget;
import com.mna.guide.interfaces.IEntrySection;
import com.mna.tools.TextConsumer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.NonNullList;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TextSection extends SectionBase {

    protected float SCALE_FACTOR = 1.0F;

    protected boolean CENTER = false;

    protected int PADDING = 5;

    protected int LINE_HEIGHT = 11;

    protected NonNullList<FormattedCharSequence> lines;

    protected String rawText;

    protected boolean formatted = false;

    protected boolean newPage = false;

    protected boolean addPadding = false;

    protected String linkPath = null;

    protected String linkType = null;

    public TextSection() {
        this.lines = NonNullList.create();
        this.rawText = "";
    }

    @Override
    public Collection<IEntrySection> parse(JsonObject element, int startY, int maxHeight, int maxWidth, int page) {
        MutableComponent parsed = null;
        if (element.has("json")) {
            JsonElement subElem = element.get("json");
            if (subElem.isJsonArray()) {
                JsonArray elemArray = subElem.getAsJsonArray();
                if (elemArray.get(0).getAsJsonObject().get("color") != null) {
                    JsonArray newArray = new JsonArray();
                    JsonObject empty = new JsonObject();
                    empty.add("text", new JsonPrimitive(""));
                    newArray.add(empty);
                    for (int i = 0; i < elemArray.size(); i++) {
                        newArray.add(elemArray.get(i));
                    }
                    elemArray = newArray;
                }
                this.rawText = elemArray.toString();
                parsed = Component.Serializer.fromJsonLenient(this.rawText);
                this.formatted = true;
            }
        } else {
            if (!element.has("value")) {
                throw new JsonParseException("Text section must contain either value or json element");
            }
            this.rawText = element.get("value").getAsString();
        }
        if (element.has("newPage")) {
            this.newPage = element.get("newPage").getAsBoolean();
        }
        if (element.has("link")) {
            JsonObject linkelem = element.get("link").getAsJsonObject();
            if (linkelem.has("path") && linkelem.has("type")) {
                this.linkPath = element.get("path").getAsString();
                this.linkType = element.get("type").getAsString();
                if (this.linkType != "recipe" && this.linkType != "entry") {
                    ManaAndArtifice.LOGGER.warn("Codex LINK directive has invalid type " + this.linkType + ".  Will be discarded.");
                    this.linkPath = null;
                    this.linkType = null;
                }
            } else {
                ManaAndArtifice.LOGGER.warn("Codex LINK directive missing path or type node.  Will be discarded.");
            }
        }
        if (this.newPage && startY != 10) {
            page++;
            startY = 10;
            maxHeight = 168;
        }
        Minecraft m = Minecraft.getInstance();
        Font fr = m.font;
        this.LINE_HEIGHT = (int) Math.ceil((double) (9.0F * this.SCALE_FACTOR));
        int maxLineWidth = (int) Math.floor((double) ((float) maxWidth / this.SCALE_FACTOR));
        List<FormattedCharSequence> split_lines = this.formatted ? fr.split(parsed, maxLineWidth) : Language.getInstance().getVisualOrder(fr.getSplitter().splitLines(this.rawText, maxLineWidth, Style.EMPTY));
        return this.createTextLinesRecursive(split_lines, startY, maxHeight, page);
    }

    private Collection<IEntrySection> createTextLinesRecursive(List<FormattedCharSequence> split_lines, int startY, int maxHeight, int page) {
        NonNullList<IEntrySection> output = NonNullList.create();
        output.add(this);
        if (startY + this.LINE_HEIGHT > maxHeight) {
            page++;
            startY = 10;
        }
        this.setPage(page);
        int totalHeight = split_lines.size() * this.LINE_HEIGHT + this.PADDING;
        if (totalHeight + startY <= maxHeight) {
            this.lines.addAll(split_lines);
            this.addPadding = true;
        } else if (totalHeight + startY - this.PADDING <= maxHeight) {
            this.lines.addAll(split_lines);
            this.addPadding = false;
        } else {
            int lines_on_current_page = (int) Math.floor((double) ((maxHeight - startY) / this.LINE_HEIGHT)) - 1;
            this.lines.addAll(split_lines.subList(0, lines_on_current_page));
            List<FormattedCharSequence> subList = split_lines.subList(lines_on_current_page, split_lines.size());
            TextSection newTS = new TextSection();
            output.addAll(newTS.createTextLinesRecursive(subList, 10, 168, page + 1));
        }
        return output;
    }

    @Override
    public Collection<AbstractWidget> getWidgets(AbstractContainerScreen<?> screen, int sectionX, int sectionY, int maxWidth, int maxHeight, Consumer<List<Component>> tooltipFunction, BiConsumer<String, Boolean> showRecipe, BiConsumer<String, Boolean> showEntry) {
        ArrayList<AbstractWidget> widgets = new ArrayList();
        Minecraft m = Minecraft.getInstance();
        Font fr = m.font;
        int y = sectionY;
        for (FormattedCharSequence line : this.lines) {
            int pX = sectionX;
            TextConsumer tc = new TextConsumer();
            line.accept(tc);
            int lineWidth = (int) ((float) fr.width(tc.getString()) * this.SCALE_FACTOR);
            if (this.CENTER) {
                pX = sectionX + (maxWidth - lineWidth) / 2;
            }
            widgets.add(new TextWidget(pX, y, lineWidth, this.LINE_HEIGHT, line, this.overrideColor, this.SCALE_FACTOR, this.tooltip, tooltipFunction, t -> {
                if (this.linkPath != null && this.linkType != null) {
                    if (this.linkType == "recipe") {
                        showRecipe.accept(this.linkPath, true);
                    } else {
                        showEntry.accept(this.linkPath, true);
                    }
                }
            }));
            y += this.LINE_HEIGHT;
        }
        return widgets;
    }

    @Override
    public int getHeight(int maxHeight) {
        int height = this.lines.size() * this.LINE_HEIGHT;
        if (this.addPadding) {
            height += this.PADDING;
        }
        return height;
    }

    @Override
    public int getWidth(int maxWidth) {
        return (int) ((float) maxWidth / this.SCALE_FACTOR);
    }

    @Override
    public boolean canWrap() {
        return true;
    }

    @Override
    public boolean newPage() {
        return this.newPage;
    }

    @Override
    public void setPadding(int i) {
        this.PADDING = i;
    }

    public String getRawText() {
        return this.rawText;
    }
}
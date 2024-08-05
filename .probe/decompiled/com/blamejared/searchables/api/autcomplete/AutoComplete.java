package com.blamejared.searchables.api.autcomplete;

import com.blamejared.searchables.api.SearchableType;
import com.blamejared.searchables.api.TokenRange;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import org.joml.Vector2d;

public class AutoComplete<T> extends AbstractWidget implements Consumer<String> {

    private final SearchableType<T> type;

    private final AutoCompletingEditBox<T> editBox;

    private final Supplier<List<T>> entries;

    private final int suggestionHeight;

    private final int maxSuggestions;

    private List<CompletionSuggestion> suggestions;

    private int displayOffset;

    private int selectedIndex;

    private final Vector2d lastMousePosition;

    private int lastCursorPosition;

    public AutoComplete(SearchableType<T> type, AutoCompletingEditBox<T> editBox, Supplier<List<T>> entries, int x, int y, int width, int suggestionHeight) {
        this(type, editBox, entries, x, y, width, suggestionHeight, 7);
    }

    public AutoComplete(SearchableType<T> type, AutoCompletingEditBox<T> editBox, Supplier<List<T>> entries, int x, int y, int width, int suggestionHeight, int maxSuggestions) {
        super(x, y, width, suggestionHeight * maxSuggestions, Component.empty());
        this.type = type;
        this.editBox = editBox;
        this.entries = entries;
        this.suggestionHeight = suggestionHeight;
        this.maxSuggestions = maxSuggestions;
        this.suggestions = List.of();
        this.displayOffset = 0;
        this.selectedIndex = -1;
        this.lastMousePosition = new Vector2d(0.0, 0.0);
        this.lastCursorPosition = -1;
    }

    public void accept(String value) {
        int position = this.editBox().m_94207_();
        if (this.lastCursorPosition != position) {
            this.displayOffset = 0;
            this.selectedIndex = 0;
            TokenRange replacementRange = this.editBox().completionVisitor().rangeAt(position);
            this.suggestions = this.type.getSuggestionsFor((List<T>) this.entries.get(), value, position, replacementRange);
        }
        this.lastCursorPosition = position;
    }

    @Override
    protected boolean clicked(double xpos, double ypos) {
        return super.clicked(xpos, ypos) && ypos < (double) (this.m_252907_() + this.suggestionHeight * this.shownSuggestions());
    }

    @Override
    public boolean isMouseOver(double xpos, double ypos) {
        return super.isMouseOver(xpos, ypos) && ypos < (double) (this.m_252907_() + this.suggestionHeight * this.shownSuggestions());
    }

    @Override
    public boolean mouseScrolled(double xpos, double ypos, double delta) {
        if (!this.isMouseOver(xpos, ypos) && !this.editBox().m_5953_(xpos, ypos)) {
            return false;
        } else {
            this.displayOffset = (int) Mth.clamp((double) this.displayOffset - delta, 0.0, (double) Math.max(this.suggestions.size() - this.maxSuggestions, 0));
            this.lastMousePosition.set(0.0);
            return true;
        }
    }

    @Override
    public boolean mouseClicked(double mx, double my, int mb) {
        if (super.mouseClicked(mx, my, mb)) {
            this.updateHoveringState(mx, my);
            if (this.selectedIndex != -1) {
                this.insertSuggestion();
            }
            return true;
        } else {
            return false;
        }
    }

    public void insertSuggestion() {
        int index = this.displayOffset + this.selectedIndex;
        if (index >= 0 && index < this.suggestions.size()) {
            CompletionSuggestion suggestion = (CompletionSuggestion) this.suggestions.get(index);
            this.editBox.deleteChars(suggestion.replacementRange());
            this.editBox.m_94164_(suggestion.toInsert());
        }
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mx, int my, float partial) {
        if (this.editBox.m_93696_()) {
            this.updateHoveringState((double) mx, (double) my);
            for (int i = this.displayOffset; i < Math.min(this.displayOffset + this.maxSuggestions, this.suggestions.size()); i++) {
                CompletionSuggestion suggestion = (CompletionSuggestion) this.suggestions.get(i);
                int minX = this.m_252754_() + 2;
                int minY = this.m_252907_() + this.suggestionHeight * (i - this.displayOffset);
                int maxY = minY + this.suggestionHeight;
                boolean hovered = this.selectedIndex != -1 && this.displayOffset + this.selectedIndex == i;
                guiGraphics.fill(RenderType.guiOverlay(), this.m_252754_(), minY, this.m_252754_() + this.m_5711_(), maxY, hovered ? -535752431 : -536870912);
                guiGraphics.drawString(Minecraft.getInstance().font, suggestion.display(), minX, minY + 1, hovered ? (Integer) Objects.requireNonNull(ChatFormatting.YELLOW.getColor()) : -1);
            }
            this.lastMousePosition.set((double) mx, (double) my);
        }
    }

    private void updateHoveringState(double xpos, double ypos) {
        if (!this.lastMousePosition.equals(xpos, ypos)) {
            this.selectedIndex = -1;
            if (this.isMouseOver(xpos, ypos)) {
                int minY = this.m_252907_();
                for (int i = 0; i < this.shownSuggestions(); i++) {
                    int maxY = minY + this.suggestionHeight;
                    if (xpos >= (double) this.m_252754_() && xpos <= (double) (this.m_252754_() + this.m_5711_()) && ypos >= (double) minY && ypos < (double) maxY) {
                        this.selectedIndex = i;
                    }
                    minY = maxY;
                }
            }
        }
    }

    public void scrollUp() {
        this.scrollUp(1);
    }

    public void scrollUp(int amount) {
        this.offsetDisplay(this.selectedIndex - amount);
    }

    public void scrollDown() {
        this.scrollDown(1);
    }

    public void scrollDown(int amount) {
        this.offsetDisplay(this.selectedIndex + amount);
    }

    private void offsetDisplay(int offset) {
        offset = Mth.clamp(offset, 0, this.shownSuggestions() - 1);
        int halfSuggestions = Math.floorDiv(this.maxSuggestions, 2);
        int currentItem = this.displayOffset + offset;
        if (currentItem < this.displayOffset + halfSuggestions) {
            this.displayOffset = Math.max(currentItem - halfSuggestions, 0);
        } else if (currentItem > this.displayOffset + halfSuggestions) {
            this.displayOffset = Math.min(currentItem - halfSuggestions, Math.max(this.suggestions.size() - this.maxSuggestions, 0));
        }
        this.selectedIndex = currentItem - this.displayOffset;
    }

    private int shownSuggestions() {
        return Math.min(this.maxSuggestions, this.suggestions.size());
    }

    public int maxSuggestions() {
        return this.maxSuggestions;
    }

    public AutoCompletingEditBox<T> editBox() {
        return this.editBox;
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput output) {
    }
}
package vazkii.patchouli.client.book.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import vazkii.patchouli.client.book.BookEntry;
import vazkii.patchouli.client.book.gui.button.GuiButtonCategory;
import vazkii.patchouli.client.book.gui.button.GuiButtonEntry;
import vazkii.patchouli.common.book.Book;

public abstract class GuiBookEntryList extends GuiBook {

    public static final int ENTRIES_PER_PAGE = 13;

    public static final int ENTRIES_IN_FIRST_PAGE = 11;

    private BookTextRenderer text;

    protected final List<Button> entryButtons = new ArrayList();

    private List<BookEntry> allEntries;

    private final List<BookEntry> visibleEntries = new ArrayList();

    private EditBox searchField;

    public GuiBookEntryList(Book book, Component title) {
        super(book, title);
    }

    @Override
    public void init() {
        super.init();
        this.text = new BookTextRenderer(this, Component.literal(this.getDescriptionText()), 15, 40);
        this.allEntries = new ArrayList(this.getEntries());
        this.allEntries.removeIf(BookEntry::shouldHide);
        if (this.shouldSortEntryList()) {
            Collections.sort(this.allEntries);
        }
        this.searchField = this.createSearchBar();
        this.buildEntryButtons();
    }

    protected EditBox createSearchBar() {
        EditBox field = new EditBox(this.f_96547_, 160, 170, 90, 12, Component.empty());
        field.setMaxLength(32);
        field.setBordered(false);
        field.setCanLoseFocus(false);
        field.setFocused(true);
        return field;
    }

    protected abstract String getDescriptionText();

    protected abstract Collection<BookEntry> getEntries();

    protected boolean doesEntryCountForProgress(BookEntry entry) {
        return true;
    }

    protected boolean shouldDrawProgressBar() {
        return true;
    }

    protected boolean shouldSortEntryList() {
        return true;
    }

    protected void addSubcategoryButtons() {
    }

    @Override
    void drawForegroundElements(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.drawForegroundElements(graphics, mouseX, mouseY, partialTicks);
        if (this.spread == 0) {
            this.drawCenteredStringNoShadow(graphics, this.m_96636_().getVisualOrderText(), 73, 18, this.book.headerColor);
            this.drawCenteredStringNoShadow(graphics, this.getChapterListTitle(), 199, 18, this.book.headerColor);
            drawSeparator(graphics, this.book, 15, 30);
            drawSeparator(graphics, this.book, 141, 30);
            this.text.render(graphics, mouseX, mouseY);
            if (this.shouldDrawProgressBar()) {
                this.drawProgressBar(graphics, this.book, mouseX, mouseY, this::doesEntryCountForProgress);
            }
        } else if (this.spread % 2 == 1 && this.spread == this.maxSpreads - 1 && this.entryButtons.size() <= 13) {
            drawPageFiller(graphics, this.book);
        }
        if (!this.searchField.getValue().isEmpty()) {
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            drawFromTexture(graphics, this.book, this.searchField.m_252754_() - 8, this.searchField.m_252907_(), 140, 183, 99, 14);
            Component toDraw = Component.literal(this.searchField.getValue()).setStyle(this.book.getFontStyle());
            graphics.drawString(this.f_96547_, toDraw, this.searchField.m_252754_() + 7, this.searchField.m_252907_() + 1, this.book.textColor, false);
        }
        if (this.visibleEntries.isEmpty()) {
            if (!this.searchField.getValue().isEmpty()) {
                this.drawCenteredStringNoShadow(graphics, I18n.get("patchouli.gui.lexicon.no_results"), 199, 80, 3355443);
                graphics.pose().scale(2.0F, 2.0F, 2.0F);
                this.drawCenteredStringNoShadow(graphics, I18n.get("patchouli.gui.lexicon.sad"), 99, 47, 10066329);
                graphics.pose().scale(0.5F, 0.5F, 0.5F);
            } else {
                this.drawCenteredStringNoShadow(graphics, this.getNoEntryMessage(), 199, 80, 3355443);
            }
        }
    }

    protected String getChapterListTitle() {
        return I18n.get("patchouli.gui.lexicon.chapters");
    }

    protected String getNoEntryMessage() {
        return I18n.get("patchouli.gui.lexicon.no_entries");
    }

    @Override
    public boolean mouseClickedScaled(double mouseX, double mouseY, int mouseButton) {
        return this.text.click(mouseX, mouseY, mouseButton) || this.searchField.m_6375_(mouseX - (double) this.bookLeft, mouseY - (double) this.bookTop, mouseButton) || super.mouseClickedScaled(mouseX, mouseY, mouseButton);
    }

    @Override
    public boolean charTyped(char c, int i) {
        String currQuery = this.searchField.getValue();
        if (this.searchField.charTyped(c, i)) {
            if (!this.searchField.getValue().equals(currQuery)) {
                this.buildEntryButtons();
            }
            return true;
        } else {
            return super.m_5534_(c, i);
        }
    }

    @Override
    public boolean keyPressed(int key, int scanCode, int modifiers) {
        String currQuery = this.searchField.getValue();
        if (key == 257) {
            if (this.visibleEntries.size() == 1) {
                this.displayLexiconGui(new GuiBookEntry(this.book, (BookEntry) this.visibleEntries.get(0)), true);
                return true;
            }
        } else if (this.searchField.keyPressed(key, scanCode, modifiers)) {
            if (!this.searchField.getValue().equals(currQuery)) {
                this.buildEntryButtons();
            }
            return true;
        }
        return super.keyPressed(key, scanCode, modifiers);
    }

    public void handleButtonCategory(Button button) {
        this.displayLexiconGui(new GuiBookCategory(this.book, ((GuiButtonCategory) button).getCategory()), true);
    }

    public void handleButtonEntry(Button button) {
        GuiBookEntry.displayOrBookmark(this, ((GuiButtonEntry) button).getEntry());
    }

    @Override
    void onPageChanged() {
        this.buildEntryButtons();
    }

    void buildEntryButtons() {
        this.removeDrawablesIn(this.entryButtons);
        this.entryButtons.clear();
        this.visibleEntries.clear();
        String query = this.searchField.getValue().toLowerCase();
        this.allEntries.stream().filter(e -> e.isFoundByQuery(query)).forEach(this.visibleEntries::add);
        this.maxSpreads = 1;
        int count = this.visibleEntries.size();
        count -= 11;
        if (count > 0) {
            this.maxSpreads = this.maxSpreads + (int) Math.ceil((double) ((float) count / 26.0F));
        }
        while (this.getEntryCountStart() > this.visibleEntries.size()) {
            this.spread--;
        }
        if (this.spread == 0) {
            this.addEntryButtons(141, 38, 0, 11);
            this.addSubcategoryButtons();
        } else {
            int start = this.getEntryCountStart();
            this.addEntryButtons(15, 18, start, 13);
            this.addEntryButtons(141, 18, start + 13, 13);
        }
    }

    int getEntryCountStart() {
        if (this.spread == 0) {
            return 0;
        } else {
            int start = 11;
            return start + 26 * (this.spread - 1);
        }
    }

    void addEntryButtons(int x, int y, int start, int count) {
        for (int i = 0; i < count && i + start < this.visibleEntries.size(); i++) {
            Button button = new GuiButtonEntry(this, this.bookLeft + x, this.bookTop + y + i * 11, (BookEntry) this.visibleEntries.get(start + i), this::handleButtonEntry);
            this.m_142416_(button);
            this.entryButtons.add(button);
        }
    }

    public String getSearchQuery() {
        return this.searchField.getValue();
    }
}
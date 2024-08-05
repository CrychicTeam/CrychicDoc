package vazkii.patchouli.client.book.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;
import vazkii.patchouli.client.base.PersistentData;
import vazkii.patchouli.client.book.BookCategory;
import vazkii.patchouli.client.book.BookEntry;
import vazkii.patchouli.client.book.gui.button.GuiButtonBook;
import vazkii.patchouli.client.book.gui.button.GuiButtonBookResize;
import vazkii.patchouli.client.book.gui.button.GuiButtonCategory;
import vazkii.patchouli.client.book.gui.button.GuiButtonEntry;
import vazkii.patchouli.client.gui.GuiAdvancementsExt;
import vazkii.patchouli.common.book.Book;

public class GuiBookLanding extends GuiBook {

    @Nullable
    BookTextRenderer text;

    int loadedCategories = 0;

    final List<Button> pamphletEntryButtons = new ArrayList();

    List<BookEntry> entriesInPamphlet;

    public GuiBookLanding(Book book) {
        super(book, Component.translatable(book.name));
    }

    @Override
    public void init() {
        super.init();
        this.text = new BookTextRenderer(this, Component.translatable(this.book.landingText), 15, 43);
        boolean disableBar = !this.book.showProgress || !this.book.advancementsEnabled();
        int x = this.bookLeft + (disableBar ? 25 : 20);
        int y = this.bookTop + 180 - (disableBar ? 25 : 62);
        int dist = 15;
        int pos = 0;
        if (this.maxScale > 2) {
            this.m_142416_(new GuiButtonBookResize(this, x + pos++ * dist, y, this::handleButtonResize));
        }
        this.m_142416_(new GuiButtonBook(this, x + pos++ * dist, y, 330, 31, 11, 11, this::handleButtonHistory, Component.translatable("patchouli.gui.lexicon.button.history")));
        if (this.book.advancementsTab != null) {
            this.m_142416_(new GuiButtonBook(this, x + pos++ * dist, y, 330, 20, 11, 11, this::handleButtonAdvancements, Component.translatable("patchouli.gui.lexicon.button.advancements")));
        }
        if (Minecraft.getInstance().player.m_7500_()) {
            this.m_142416_(new GuiButtonBook(this, x + pos++ * dist, y, 308, 9, 11, 11, this::handleButtonEdit, Component.translatable("patchouli.gui.lexicon.button.editor"), Component.translatable("patchouli.gui.lexicon.button.editor.info").withStyle(ChatFormatting.GRAY)));
        }
        if (this.book.getContents().pamphletCategory == null) {
            int i = 0;
            List<BookCategory> categories = new ArrayList(this.book.getContents().categories.values());
            Collections.sort(categories);
            for (BookCategory category : categories) {
                if (category.getParentCategory() == null && !category.shouldHide()) {
                    this.addCategoryButton(i, category);
                    i++;
                }
            }
            this.addCategoryButton(i, null);
            this.loadedCategories = i + 1;
        } else {
            this.entriesInPamphlet = new ArrayList(this.book.getContents().entries.values());
            this.entriesInPamphlet.removeIf(BookEntry::shouldHide);
            Collections.sort(this.entriesInPamphlet);
            this.buildEntryButtons();
            this.loadedCategories = 0;
        }
    }

    private void addCategoryButton(int i, BookCategory category) {
        int x = 151 + i % 4 * 24;
        int y = 43 + i / 4 * 24;
        if (category == null) {
            this.m_142416_(new GuiButtonCategory(this, x, y, this.book.getIcon(), Component.translatable("patchouli.gui.lexicon.index"), this::handleButtonIndex));
        } else {
            this.m_142416_(new GuiButtonCategory(this, x, y, category, this::handleButtonCategory));
        }
    }

    @Override
    void drawForegroundElements(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        if (this.text != null) {
            this.text.render(graphics, mouseX, mouseY);
        }
        int topSeparator = 30;
        int bottomSeparator = topSeparator + 25 + 24 * ((this.loadedCategories - 1) / 4 + 1);
        this.drawHeader(graphics);
        if (this.book.getContents().pamphletCategory == null) {
            this.drawCenteredStringNoShadow(graphics, I18n.get("patchouli.gui.lexicon.categories"), 199, 18, this.book.headerColor);
            drawSeparator(graphics, this.book, 141, topSeparator);
            if (this.loadedCategories <= 16) {
                drawSeparator(graphics, this.book, 141, bottomSeparator);
            }
        }
        if (this.book.getContents().isErrored()) {
            int x = 199;
            int y = bottomSeparator + 12;
            this.drawCenteredStringNoShadow(graphics, I18n.get("patchouli.gui.lexicon.loading_error"), x, y, 16711680);
            this.drawCenteredStringNoShadow(graphics, I18n.get("patchouli.gui.lexicon.loading_error_hover"), x, y + 10, 7829367);
            x -= 58;
            y -= 4;
            if (this.isMouseInRelativeRange((double) mouseX, (double) mouseY, x, y, 116, 20)) {
                this.makeErrorTooltip();
            }
        }
        this.drawProgressBar(graphics, this.book, mouseX, mouseY, e -> true);
    }

    @Override
    void onPageChanged() {
        this.buildEntryButtons();
    }

    private void buildEntryButtons() {
        this.removeDrawablesIn(this.pamphletEntryButtons);
        this.pamphletEntryButtons.clear();
        this.maxSpreads = 1;
        this.addEntryButtons(141, 18, 0, 11);
    }

    private void addEntryButtons(int x, int y, int start, int count) {
        if (!this.book.getContents().isErrored()) {
            for (int i = 0; i < count && i + start < this.entriesInPamphlet.size(); i++) {
                Button button = new GuiButtonEntry(this, this.bookLeft + x, this.bookTop + y + i * 11, (BookEntry) this.entriesInPamphlet.get(start + i), this::handleButtonPamphletEntry);
                this.m_142416_(button);
                this.pamphletEntryButtons.add(button);
            }
        }
    }

    private void drawHeader(GuiGraphics graphics) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        drawFromTexture(graphics, this.book, -8, 12, 0, 180, 140, 31);
        int color = this.book.nameplateColor;
        graphics.drawString(this.f_96547_, this.book.getBookItem().getHoverName(), 13, 16, color, false);
        Component toDraw = this.book.getSubtitle().withStyle(this.book.getFontStyle());
        graphics.drawString(this.f_96547_, toDraw, 24, 24, color, false);
    }

    private void makeErrorTooltip() {
        Throwable e = this.book.getContents().getException();
        List<Component> lines;
        for (lines = new ArrayList(); e != null; e = e.getCause()) {
            String msg = e.getMessage();
            if (msg != null && !msg.isEmpty()) {
                lines.add(Component.literal(e.getMessage()));
            }
        }
        if (!lines.isEmpty()) {
            lines.add(Component.translatable("patchouli.gui.lexicon.loading_error_log").withStyle(ChatFormatting.GREEN));
            this.setTooltip(lines);
        }
    }

    @Override
    public boolean mouseClickedScaled(double mouseX, double mouseY, int mouseButton) {
        return this.text != null && this.text.click(mouseX, mouseY, mouseButton) || super.mouseClickedScaled(mouseX, mouseY, mouseButton);
    }

    public void handleButtonIndex(Button button) {
        this.displayLexiconGui(new GuiBookIndex(this.book), true);
    }

    public void handleButtonCategory(Button button) {
        this.displayLexiconGui(new GuiBookCategory(this.book, ((GuiButtonCategory) button).getCategory()), true);
    }

    private void handleButtonHistory(Button button) {
        this.displayLexiconGui(new GuiBookHistory(this.book), true);
    }

    private void handleButtonAdvancements(Button button) {
        this.f_96541_.setScreen(new GuiAdvancementsExt(this.f_96541_.player.connection.getAdvancements(), this, this.book.advancementsTab));
    }

    private void handleButtonEdit(Button button) {
        if (m_96638_()) {
            long time = System.currentTimeMillis();
            this.book.reloadContents(this.f_96541_.level, true);
            this.book.reloadLocks(false);
            this.displayLexiconGui(new GuiBookLanding(this.book), false);
            this.f_96541_.player.displayClientMessage(Component.translatable("patchouli.gui.lexicon.reloaded", System.currentTimeMillis() - time), false);
        } else {
            this.displayLexiconGui(new GuiBookWriter(this.book), true);
        }
    }

    public void handleButtonResize(Button button) {
        if (PersistentData.data.bookGuiScale >= this.maxScale) {
            PersistentData.data.bookGuiScale = 0;
        } else {
            PersistentData.data.bookGuiScale = Math.max(2, PersistentData.data.bookGuiScale + 1);
        }
        PersistentData.save();
        this.displayLexiconGui(this, false);
    }

    public void handleButtonPamphletEntry(Button button) {
        GuiBookEntry.displayOrBookmark(this, ((GuiButtonEntry) button).getEntry());
    }
}
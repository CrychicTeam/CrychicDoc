package vazkii.patchouli.client.book.gui;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vazkii.patchouli.api.IComponentRenderContext;
import vazkii.patchouli.client.base.PersistentData;
import vazkii.patchouli.client.book.BookEntry;
import vazkii.patchouli.client.book.BookPage;
import vazkii.patchouli.common.book.Book;

public class GuiBookEntry extends GuiBook implements IComponentRenderContext {

    protected final BookEntry entry;

    @Nullable
    private BookPage leftPage;

    @Nullable
    private BookPage rightPage;

    public GuiBookEntry(Book book, BookEntry entry) {
        this(book, entry, 0);
    }

    public GuiBookEntry(Book book, BookEntry entry, int spread) {
        super(book, entry.getName());
        this.entry = entry;
        this.spread = spread;
    }

    @Override
    public void init() {
        super.init();
        this.maxSpreads = (int) Math.ceil((double) ((float) this.entry.getPages().size() / 2.0F));
        this.setupPages();
    }

    @Override
    public void onFirstOpened() {
        super.onFirstOpened();
        boolean dirty = false;
        ResourceLocation key = this.entry.getId();
        PersistentData.BookData data = PersistentData.data.getBookData(this.book);
        if (!data.viewedEntries.contains(key)) {
            data.viewedEntries.add(key);
            dirty = true;
            this.entry.markReadStateDirty();
        }
        int index = data.history.indexOf(key);
        if (index != 0) {
            if (index > 0) {
                data.history.remove(key);
            }
            data.history.add(0, key);
            while (data.history.size() > 13) {
                data.history.remove(13);
            }
            dirty = true;
        }
        if (dirty) {
            PersistentData.save();
        }
    }

    @Override
    void drawForegroundElements(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        this.drawPage(graphics, this.leftPage, mouseX, mouseY, partialTicks);
        this.drawPage(graphics, this.rightPage, mouseX, mouseY, partialTicks);
        if (this.rightPage == null) {
            drawPageFiller(graphics, this.entry.getBook());
        }
    }

    @Override
    public boolean mouseClickedScaled(double mouseX, double mouseY, int mouseButton) {
        return this.clickPage(this.leftPage, mouseX, mouseY, mouseButton) || this.clickPage(this.rightPage, mouseX, mouseY, mouseButton) || super.mouseClickedScaled(mouseX, mouseY, mouseButton);
    }

    void drawPage(GuiGraphics graphics, @Nullable BookPage page, int mouseX, int mouseY, float pticks) {
        if (page != null) {
            graphics.pose().pushPose();
            graphics.pose().translate((float) page.left, (float) page.top, 0.0F);
            page.render(graphics, mouseX - page.left, mouseY - page.top, pticks);
            graphics.pose().popPose();
        }
    }

    private boolean clickPage(@Nullable BookPage page, double mouseX, double mouseY, int mouseButton) {
        return page != null ? page.mouseClicked(mouseX - (double) page.left, mouseY - (double) page.top, mouseButton) : false;
    }

    @Override
    void onPageChanged() {
        this.setupPages();
        this.needsBookmarkUpdate = true;
    }

    private void setupPages() {
        if (this.leftPage != null) {
            this.leftPage.onHidden(this);
        }
        if (this.rightPage != null) {
            this.rightPage.onHidden(this);
        }
        List<BookPage> pages = this.entry.getPages();
        int leftNum = this.spread * 2;
        int rightNum = this.spread * 2 + 1;
        this.leftPage = leftNum < pages.size() ? (BookPage) pages.get(leftNum) : null;
        this.rightPage = rightNum < pages.size() ? (BookPage) pages.get(rightNum) : null;
        if (this.leftPage != null) {
            this.leftPage.onDisplayed(this, 15, 18);
        }
        if (this.rightPage != null) {
            this.rightPage.onDisplayed(this, 141, 18);
        }
    }

    public BookEntry getEntry() {
        return this.entry;
    }

    public boolean equals(Object obj) {
        return obj == this || obj instanceof GuiBookEntry && ((GuiBookEntry) obj).entry == this.entry && ((GuiBookEntry) obj).spread == this.spread;
    }

    public int hashCode() {
        return Objects.hashCode(this.entry) * 31 + Objects.hashCode(this.spread);
    }

    @Override
    public boolean canBeOpened() {
        return !this.entry.isLocked() && !this.equals(Minecraft.getInstance().screen);
    }

    @Override
    protected boolean shouldAddAddBookmarkButton() {
        return !this.isBookmarkedAlready();
    }

    boolean isBookmarkedAlready() {
        if (this.entry != null && this.entry.getId() != null) {
            String entryKey = this.entry.getId().toString();
            PersistentData.BookData data = PersistentData.data.getBookData(this.book);
            for (PersistentData.Bookmark bookmark : data.bookmarks) {
                if (bookmark.entry.equals(entryKey) && bookmark.spread == this.spread) {
                    return true;
                }
            }
            return false;
        } else {
            return false;
        }
    }

    @Override
    public void bookmarkThis() {
        ResourceLocation entryKey = this.entry.getId();
        PersistentData.BookData data = PersistentData.data.getBookData(this.book);
        data.bookmarks.add(new PersistentData.Bookmark(entryKey, this.spread));
        PersistentData.save();
        this.needsBookmarkUpdate = true;
    }

    public static void displayOrBookmark(GuiBook currGui, BookEntry entry) {
        Book book = currGui.book;
        GuiBookEntry gui = new GuiBookEntry(currGui.book, entry);
        if (Screen.hasShiftDown()) {
            PersistentData.BookData data = PersistentData.data.getBookData(book);
            if (gui.isBookmarkedAlready()) {
                String key = entry.getId().toString();
                data.bookmarks.removeIf(bm -> bm.entry.equals(key) && bm.spread == 0);
                PersistentData.save();
                currGui.needsBookmarkUpdate = true;
                return;
            }
            if (data.bookmarks.size() < 10) {
                gui.bookmarkThis();
                currGui.needsBookmarkUpdate = true;
                return;
            }
        }
        book.getContents().openLexiconGui(gui, true);
    }

    @Override
    public Screen getGui() {
        return this;
    }

    @Override
    public Style getFont() {
        return this.book.getFontStyle();
    }

    @Override
    public void renderItemStack(GuiGraphics graphics, int x, int y, int mouseX, int mouseY, ItemStack stack) {
        if (!stack.isEmpty()) {
            graphics.renderItem(stack, x, y);
            graphics.renderItemDecorations(this.f_96547_, stack, x, y);
            if (this.isMouseInRelativeRange((double) mouseX, (double) mouseY, x, y, 16, 16)) {
                this.setTooltipStack(stack);
            }
        }
    }

    @Override
    public void renderIngredient(GuiGraphics graphics, int x, int y, int mouseX, int mouseY, Ingredient ingr) {
        ItemStack[] stacks = ingr.getItems();
        if (stacks.length > 0) {
            this.renderItemStack(graphics, x, y, mouseX, mouseY, stacks[this.ticksInBook / 20 % stacks.length]);
        }
    }

    @Override
    public void setHoverTooltip(List<String> tooltip) {
        this.setTooltip((List<Component>) tooltip.stream().map(Component::m_237113_).collect(Collectors.toList()));
    }

    @Override
    public void setHoverTooltipComponents(@NotNull List<Component> tooltip) {
        this.setTooltip(tooltip);
    }

    @Override
    public boolean isAreaHovered(int mouseX, int mouseY, int x, int y, int w, int h) {
        return this.isMouseInRelativeRange((double) mouseX, (double) mouseY, x, y, w, h);
    }

    @Override
    public boolean navigateToEntry(ResourceLocation entry, int page, boolean push) {
        BookEntry bookEntry = (BookEntry) this.book.getContents().entries.get(entry);
        if (bookEntry != null && !bookEntry.isLocked()) {
            this.displayLexiconGui(new GuiBookEntry(this.book, bookEntry, page), push);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void registerButton(Button button, int pageNum, Runnable onClick) {
        this.addWidget(button, pageNum);
    }

    @Override
    public void addWidget(AbstractWidget widget, int pageNum) {
        widget.setX(widget.getX() + this.bookLeft + (pageNum % 2 == 0 ? 15 : 141));
        widget.setY(widget.getY() + this.bookTop);
        this.m_142416_(widget);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (Minecraft.getInstance().options.keyInventory.matches(keyCode, scanCode)) {
            this.m_7379_();
            return true;
        } else {
            return super.keyPressed(keyCode, scanCode, modifiers);
        }
    }

    @Override
    protected boolean shouldAddMarkReadButton() {
        return false;
    }

    @Override
    public ResourceLocation getBookTexture() {
        return this.book.bookTexture;
    }

    @Override
    public ResourceLocation getCraftingTexture() {
        return this.book.craftingTexture;
    }

    @Override
    public int getTextColor() {
        return this.book.textColor;
    }

    @Override
    public int getHeaderColor() {
        return this.book.headerColor;
    }

    @Override
    public int getTicksInBook() {
        return this.ticksInBook;
    }
}
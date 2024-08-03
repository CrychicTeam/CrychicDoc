package net.minecraft.client.gui.screens.inventory;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.client.GameNarrator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.WrittenBookItem;

public class BookViewScreen extends Screen {

    public static final int PAGE_INDICATOR_TEXT_Y_OFFSET = 16;

    public static final int PAGE_TEXT_X_OFFSET = 36;

    public static final int PAGE_TEXT_Y_OFFSET = 30;

    public static final BookViewScreen.BookAccess EMPTY_ACCESS = new BookViewScreen.BookAccess() {

        @Override
        public int getPageCount() {
            return 0;
        }

        @Override
        public FormattedText getPageRaw(int p_98306_) {
            return FormattedText.EMPTY;
        }
    };

    public static final ResourceLocation BOOK_LOCATION = new ResourceLocation("textures/gui/book.png");

    protected static final int TEXT_WIDTH = 114;

    protected static final int TEXT_HEIGHT = 128;

    protected static final int IMAGE_WIDTH = 192;

    protected static final int IMAGE_HEIGHT = 192;

    private BookViewScreen.BookAccess bookAccess;

    private int currentPage;

    private List<FormattedCharSequence> cachedPageComponents = Collections.emptyList();

    private int cachedPage = -1;

    private Component pageMsg = CommonComponents.EMPTY;

    private PageButton forwardButton;

    private PageButton backButton;

    private final boolean playTurnSound;

    public BookViewScreen(BookViewScreen.BookAccess bookViewScreenBookAccess0) {
        this(bookViewScreenBookAccess0, true);
    }

    public BookViewScreen() {
        this(EMPTY_ACCESS, false);
    }

    private BookViewScreen(BookViewScreen.BookAccess bookViewScreenBookAccess0, boolean boolean1) {
        super(GameNarrator.NO_TITLE);
        this.bookAccess = bookViewScreenBookAccess0;
        this.playTurnSound = boolean1;
    }

    public void setBookAccess(BookViewScreen.BookAccess bookViewScreenBookAccess0) {
        this.bookAccess = bookViewScreenBookAccess0;
        this.currentPage = Mth.clamp(this.currentPage, 0, bookViewScreenBookAccess0.getPageCount());
        this.updateButtonVisibility();
        this.cachedPage = -1;
    }

    public boolean setPage(int int0) {
        int $$1 = Mth.clamp(int0, 0, this.bookAccess.getPageCount() - 1);
        if ($$1 != this.currentPage) {
            this.currentPage = $$1;
            this.updateButtonVisibility();
            this.cachedPage = -1;
            return true;
        } else {
            return false;
        }
    }

    protected boolean forcePage(int int0) {
        return this.setPage(int0);
    }

    @Override
    protected void init() {
        this.createMenuControls();
        this.createPageControlButtons();
    }

    protected void createMenuControls() {
        this.m_142416_(Button.builder(CommonComponents.GUI_DONE, p_289629_ -> this.m_7379_()).bounds(this.f_96543_ / 2 - 100, 196, 200, 20).build());
    }

    protected void createPageControlButtons() {
        int $$0 = (this.f_96543_ - 192) / 2;
        int $$1 = 2;
        this.forwardButton = (PageButton) this.m_142416_(new PageButton($$0 + 116, 159, true, p_98297_ -> this.pageForward(), this.playTurnSound));
        this.backButton = (PageButton) this.m_142416_(new PageButton($$0 + 43, 159, false, p_98287_ -> this.pageBack(), this.playTurnSound));
        this.updateButtonVisibility();
    }

    private int getNumPages() {
        return this.bookAccess.getPageCount();
    }

    protected void pageBack() {
        if (this.currentPage > 0) {
            this.currentPage--;
        }
        this.updateButtonVisibility();
    }

    protected void pageForward() {
        if (this.currentPage < this.getNumPages() - 1) {
            this.currentPage++;
        }
        this.updateButtonVisibility();
    }

    private void updateButtonVisibility() {
        this.forwardButton.f_93624_ = this.currentPage < this.getNumPages() - 1;
        this.backButton.f_93624_ = this.currentPage > 0;
    }

    @Override
    public boolean keyPressed(int int0, int int1, int int2) {
        if (super.keyPressed(int0, int1, int2)) {
            return true;
        } else {
            switch(int0) {
                case 266:
                    this.backButton.m_5691_();
                    return true;
                case 267:
                    this.forwardButton.m_5691_();
                    return true;
                default:
                    return false;
            }
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        this.m_280273_(guiGraphics0);
        int $$4 = (this.f_96543_ - 192) / 2;
        int $$5 = 2;
        guiGraphics0.blit(BOOK_LOCATION, $$4, 2, 0, 0, 192, 192);
        if (this.cachedPage != this.currentPage) {
            FormattedText $$6 = this.bookAccess.getPage(this.currentPage);
            this.cachedPageComponents = this.f_96547_.split($$6, 114);
            this.pageMsg = Component.translatable("book.pageIndicator", this.currentPage + 1, Math.max(this.getNumPages(), 1));
        }
        this.cachedPage = this.currentPage;
        int $$7 = this.f_96547_.width(this.pageMsg);
        guiGraphics0.drawString(this.f_96547_, this.pageMsg, $$4 - $$7 + 192 - 44, 18, 0, false);
        int $$8 = Math.min(128 / 9, this.cachedPageComponents.size());
        for (int $$9 = 0; $$9 < $$8; $$9++) {
            FormattedCharSequence $$10 = (FormattedCharSequence) this.cachedPageComponents.get($$9);
            guiGraphics0.drawString(this.f_96547_, $$10, $$4 + 36, 32 + $$9 * 9, 0, false);
        }
        Style $$11 = this.getClickedComponentStyleAt((double) int1, (double) int2);
        if ($$11 != null) {
            guiGraphics0.renderComponentHoverEffect(this.f_96547_, $$11, int1, int2);
        }
        super.render(guiGraphics0, int1, int2, float3);
    }

    @Override
    public boolean mouseClicked(double double0, double double1, int int2) {
        if (int2 == 0) {
            Style $$3 = this.getClickedComponentStyleAt(double0, double1);
            if ($$3 != null && this.handleComponentClicked($$3)) {
                return true;
            }
        }
        return super.m_6375_(double0, double1, int2);
    }

    @Override
    public boolean handleComponentClicked(Style style0) {
        ClickEvent $$1 = style0.getClickEvent();
        if ($$1 == null) {
            return false;
        } else if ($$1.getAction() == ClickEvent.Action.CHANGE_PAGE) {
            String $$2 = $$1.getValue();
            try {
                int $$3 = Integer.parseInt($$2) - 1;
                return this.forcePage($$3);
            } catch (Exception var5) {
                return false;
            }
        } else {
            boolean $$4 = super.handleComponentClicked(style0);
            if ($$4 && $$1.getAction() == ClickEvent.Action.RUN_COMMAND) {
                this.closeScreen();
            }
            return $$4;
        }
    }

    protected void closeScreen() {
        this.f_96541_.setScreen(null);
    }

    @Nullable
    public Style getClickedComponentStyleAt(double double0, double double1) {
        if (this.cachedPageComponents.isEmpty()) {
            return null;
        } else {
            int $$2 = Mth.floor(double0 - (double) ((this.f_96543_ - 192) / 2) - 36.0);
            int $$3 = Mth.floor(double1 - 2.0 - 30.0);
            if ($$2 >= 0 && $$3 >= 0) {
                int $$4 = Math.min(128 / 9, this.cachedPageComponents.size());
                if ($$2 <= 114 && $$3 < 9 * $$4 + $$4) {
                    int $$5 = $$3 / 9;
                    if ($$5 >= 0 && $$5 < this.cachedPageComponents.size()) {
                        FormattedCharSequence $$6 = (FormattedCharSequence) this.cachedPageComponents.get($$5);
                        return this.f_96541_.font.getSplitter().componentStyleAtWidth($$6, $$2);
                    } else {
                        return null;
                    }
                } else {
                    return null;
                }
            } else {
                return null;
            }
        }
    }

    static List<String> loadPages(CompoundTag compoundTag0) {
        Builder<String> $$1 = ImmutableList.builder();
        loadPages(compoundTag0, $$1::add);
        return $$1.build();
    }

    public static void loadPages(CompoundTag compoundTag0, Consumer<String> consumerString1) {
        ListTag $$2 = compoundTag0.getList("pages", 8).copy();
        IntFunction<String> $$4;
        if (Minecraft.getInstance().isTextFilteringEnabled() && compoundTag0.contains("filtered_pages", 10)) {
            CompoundTag $$3 = compoundTag0.getCompound("filtered_pages");
            $$4 = p_169702_ -> {
                String $$3x = String.valueOf(p_169702_);
                return $$3.contains($$3x) ? $$3.getString($$3x) : $$2.getString(p_169702_);
            };
        } else {
            $$4 = $$2::m_128778_;
        }
        for (int $$6 = 0; $$6 < $$2.size(); $$6++) {
            consumerString1.accept((String) $$4.apply($$6));
        }
    }

    public interface BookAccess {

        int getPageCount();

        FormattedText getPageRaw(int var1);

        default FormattedText getPage(int int0) {
            return int0 >= 0 && int0 < this.getPageCount() ? this.getPageRaw(int0) : FormattedText.EMPTY;
        }

        static BookViewScreen.BookAccess fromItem(ItemStack itemStack0) {
            if (itemStack0.is(Items.WRITTEN_BOOK)) {
                return new BookViewScreen.WrittenBookAccess(itemStack0);
            } else {
                return (BookViewScreen.BookAccess) (itemStack0.is(Items.WRITABLE_BOOK) ? new BookViewScreen.WritableBookAccess(itemStack0) : BookViewScreen.EMPTY_ACCESS);
            }
        }
    }

    public static class WritableBookAccess implements BookViewScreen.BookAccess {

        private final List<String> pages;

        public WritableBookAccess(ItemStack itemStack0) {
            this.pages = readPages(itemStack0);
        }

        private static List<String> readPages(ItemStack itemStack0) {
            CompoundTag $$1 = itemStack0.getTag();
            return (List<String>) ($$1 != null ? BookViewScreen.loadPages($$1) : ImmutableList.of());
        }

        @Override
        public int getPageCount() {
            return this.pages.size();
        }

        @Override
        public FormattedText getPageRaw(int int0) {
            return FormattedText.of((String) this.pages.get(int0));
        }
    }

    public static class WrittenBookAccess implements BookViewScreen.BookAccess {

        private final List<String> pages;

        public WrittenBookAccess(ItemStack itemStack0) {
            this.pages = readPages(itemStack0);
        }

        private static List<String> readPages(ItemStack itemStack0) {
            CompoundTag $$1 = itemStack0.getTag();
            return (List<String>) ($$1 != null && WrittenBookItem.makeSureTagIsValid($$1) ? BookViewScreen.loadPages($$1) : ImmutableList.of(Component.Serializer.toJson(Component.translatable("book.invalid.tag").withStyle(ChatFormatting.DARK_RED))));
        }

        @Override
        public int getPageCount() {
            return this.pages.size();
        }

        @Override
        public FormattedText getPageRaw(int int0) {
            String $$1 = (String) this.pages.get(int0);
            try {
                FormattedText $$2 = Component.Serializer.fromJson($$1);
                if ($$2 != null) {
                    return $$2;
                }
            } catch (Exception var4) {
            }
            return FormattedText.of($$1);
        }
    }
}
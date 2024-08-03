package vazkii.patchouli.client.book.page;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import vazkii.patchouli.client.base.ClientAdvancements;
import vazkii.patchouli.client.base.PersistentData;
import vazkii.patchouli.client.book.BookContentsBuilder;
import vazkii.patchouli.client.book.BookEntry;
import vazkii.patchouli.client.book.gui.GuiBook;
import vazkii.patchouli.client.book.gui.GuiBookEntry;
import vazkii.patchouli.client.book.page.abstr.PageWithText;
import vazkii.patchouli.common.book.Book;

public class PageQuest extends PageWithText {

    ResourceLocation trigger;

    String title;

    transient boolean isManual;

    @Override
    public int getTextHeight() {
        return 22;
    }

    @Override
    public void build(Level level, BookEntry entry, BookContentsBuilder builder, int pageNum) {
        super.build(level, entry, builder, pageNum);
        this.isManual = this.trigger == null;
    }

    public boolean isCompleted(Book book) {
        return this.isManual ? PersistentData.data.getBookData(book).completedManualQuests.contains(this.entry.getId()) : this.trigger != null && ClientAdvancements.hasDone(this.trigger.toString());
    }

    @Override
    public void onDisplayed(GuiBookEntry parent, int left, int top) {
        super.onDisplayed(parent, left, top);
        if (this.isManual) {
            Button button = Button.builder(Component.empty(), this::questButtonClicked).pos(8, 121).size(100, 20).build();
            this.addButton(button);
            this.updateButtonText(button);
        }
    }

    private void updateButtonText(Button button) {
        boolean completed = this.isCompleted(this.parent.book);
        Component s = Component.translatable(completed ? "patchouli.gui.lexicon.mark_incomplete" : "patchouli.gui.lexicon.mark_complete");
        button.m_93666_(s);
    }

    protected void questButtonClicked(Button button) {
        ResourceLocation res = this.entry.getId();
        PersistentData.BookData data = PersistentData.data.getBookData(this.parent.book);
        if (data.completedManualQuests.contains(res)) {
            data.completedManualQuests.remove(res);
        } else {
            data.completedManualQuests.add(res);
        }
        PersistentData.save();
        this.updateButtonText(button);
        this.entry.markReadStateDirty();
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float pticks) {
        super.render(graphics, mouseX, mouseY, pticks);
        this.parent.drawCenteredStringNoShadow(graphics, this.title != null && !this.title.isEmpty() ? this.i18n(this.title) : I18n.get("patchouli.gui.lexicon.objective"), 58, 0, this.book.headerColor);
        GuiBook.drawSeparator(graphics, this.book, 0, 12);
        if (!this.isManual) {
            GuiBook.drawSeparator(graphics, this.book, 0, 131);
            boolean completed = this.isCompleted(this.parent.book);
            String s = I18n.get(completed ? "patchouli.gui.lexicon.complete" : "patchouli.gui.lexicon.incomplete");
            int color = completed ? '謚' : this.book.headerColor;
            this.parent.drawCenteredStringNoShadow(graphics, s, 58, 139, color);
        }
    }
}
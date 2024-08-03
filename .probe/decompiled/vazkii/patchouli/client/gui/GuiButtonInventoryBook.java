package vazkii.patchouli.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import vazkii.patchouli.client.book.BookContents;
import vazkii.patchouli.client.book.EntryDisplayState;
import vazkii.patchouli.client.book.gui.GuiBook;
import vazkii.patchouli.common.book.Book;

public class GuiButtonInventoryBook extends Button {

    private final Book book;

    public GuiButtonInventoryBook(Book book, int x, int y) {
        super(x, y, 20, 20, Component.empty(), b -> {
            BookContents contents = book.getContents();
            contents.openLexiconGui(contents.getCurrentGui(), false);
        }, f_252438_);
        this.book = book;
    }

    @Override
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float pticks) {
        graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        boolean hovered = mouseX >= this.m_252754_() && mouseY >= this.m_252907_() && mouseX < this.m_252754_() + this.f_93618_ && mouseY < this.m_252907_() + this.f_93619_;
        graphics.blit(new ResourceLocation("patchouli", "textures/gui/inventory_button.png"), this.m_252754_(), this.m_252907_(), (float) (hovered ? 20 : 0), 0.0F, this.f_93618_, this.f_93619_, 64, 64);
        ItemStack stack = this.book.getBookItem();
        graphics.renderItem(stack, this.m_252754_() + 2, this.m_252907_() + 2);
        graphics.renderItemDecorations(Minecraft.getInstance().font, stack, this.m_252754_() + 2, this.m_252907_() + 2);
        EntryDisplayState readState = this.book.getContents().getReadState();
        if (readState.hasIcon && readState.showInInventory) {
            GuiBook.drawMarking(graphics, this.book, this.m_252754_(), this.m_252907_(), 0, readState);
        }
    }

    public Book getBook() {
        return this.book;
    }
}
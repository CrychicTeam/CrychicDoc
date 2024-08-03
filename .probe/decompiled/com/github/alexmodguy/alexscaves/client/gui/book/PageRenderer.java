package com.github.alexmodguy.alexscaves.client.gui.book;

import com.github.alexmodguy.alexscaves.client.gui.book.widget.BookWidget;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;

public class PageRenderer {

    public boolean enteringNewPageFlag;

    public boolean leavingNewPageFlag;

    private int relativePageNumber;

    private int entryPageNumber;

    private BookEntry entry;

    public PageRenderer(int relativePageNumber) {
        this.relativePageNumber = relativePageNumber;
    }

    public void setEntryPageNumber(int entryPageNumber) {
        this.entryPageNumber = entryPageNumber;
    }

    public int getDisplayPageNumber() {
        int i = this.relativePageNumber;
        if (this.enteringNewPageFlag) {
            i -= 2;
        }
        if (this.leavingNewPageFlag) {
            i += 2;
        }
        return 1 + i + this.entryPageNumber * 2;
    }

    public void setEntry(BookEntry entry) {
        this.entry = entry;
    }

    protected void renderPage(CaveBookScreen screen, PoseStack poseStack, int mouseX, int mouseY, float partialTicks, boolean onFlippingPage) {
        int pgNumber = this.getDisplayPageNumber();
        MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
        if (this.entry != null) {
            if (pgNumber == 1 && !this.entry.getTranslatableTitle().isEmpty()) {
                Component title = Component.translatable(this.entry.getTranslatableTitle());
                poseStack.pushPose();
                int titleLength = Math.max(screen.getMinecraft().font.width(title), 1);
                float titleScale = Math.min(135.0F / (float) titleLength, 2.5F);
                poseStack.translate(65.0F, 7.0F - 5.0F * titleScale, 0.0F);
                poseStack.scale(titleScale, titleScale, 1.0F);
                poseStack.translate((float) (-titleLength) / 2.0F, 0.0F, 0.0F);
                screen.getMinecraft().font.drawInBatch8xOutline(title.getVisualOrderText(), 0.0F, 0.0F, 16771007, 11179903, poseStack.last().pose(), bufferSource, 15728880);
                poseStack.popPose();
            }
            if (!this.entry.getEntryText().isEmpty()) {
                int startReadingAt = (pgNumber - 1) * 15;
                this.printLinesFromEntry(screen.getMinecraft().font, poseStack, bufferSource, this.entry, startReadingAt);
            }
            int numberWidth = screen.getMinecraft().font.width(String.valueOf(pgNumber));
            poseStack.pushPose();
            poseStack.scale(0.75F, 0.75F, 0.75F);
            screen.getMinecraft().font.drawInBatch(String.valueOf(pgNumber), 86.0F - (float) numberWidth * 0.5F, 210.0F, 8546881, false, poseStack.last().pose(), bufferSource, Font.DisplayMode.NORMAL, 0, 15728880);
            poseStack.popPose();
            for (BookWidget widget : this.entry.getWidgets()) {
                if (widget.getDisplayPage() == pgNumber) {
                    widget.render(poseStack, bufferSource, partialTicks, onFlippingPage);
                }
            }
        }
    }

    private void printLinesFromEntry(Font font, PoseStack poseStack, MultiBufferSource.BufferSource bufferSource, BookEntry bookEntry, int startReadingAt) {
        if (startReadingAt >= 0) {
            for (int i = startReadingAt; i < startReadingAt + 15; i++) {
                if (bookEntry.getEntryText().size() > i) {
                    String printLine = (String) bookEntry.getEntryText().get(i);
                    font.drawInBatch(printLine, 0.0F, (float) ((i - startReadingAt) * 10), 8546881, false, poseStack.last().pose(), bufferSource, Font.DisplayMode.NORMAL, 0, 15728880);
                    for (BookLink bookLink : bookEntry.getEntryLinks()) {
                        if (bookLink.getLineNumber() == i) {
                            int fontWidth = font.width(printLine.substring(0, bookLink.getCharacterStartsAt()));
                            Component component = Component.literal(bookLink.getDisplayText()).withStyle(ChatFormatting.UNDERLINE);
                            font.drawInBatch(component, (float) fontWidth, (float) ((i - startReadingAt) * 10), bookLink.isEnabled() ? (bookLink.isHovered() ? '铿' : 1118481) : 13879723, false, poseStack.last().pose(), bufferSource, Font.DisplayMode.NORMAL, 0, 15728880);
                        }
                    }
                }
            }
        }
    }
}
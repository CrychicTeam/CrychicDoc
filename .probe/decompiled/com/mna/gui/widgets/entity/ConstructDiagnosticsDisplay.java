package com.mna.gui.widgets.entity;

import com.mna.entities.constructs.animated.Construct;
import com.mna.entities.constructs.animated.ConstructDiagnostics;
import com.mna.gui.GuiTextures;
import java.util.Iterator;
import java.util.LinkedList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FastColor;

public class ConstructDiagnosticsDisplay extends AbstractWidget {

    private Construct construct;

    private LinkedList<ConstructDiagnostics.LogEntry> lastDiagnostics;

    private Font font;

    public ConstructDiagnosticsDisplay(int pX, int pY, Construct construct) {
        super(pX, pY, 142, 231, Component.literal(""));
        this.construct = construct;
        Minecraft mc = Minecraft.getInstance();
        this.font = mc.font;
    }

    public boolean isValid() {
        return this.construct != null;
    }

    @Override
    protected void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        if (this.construct != null) {
            if (this.construct.m_6084_()) {
                this.lastDiagnostics = (LinkedList<ConstructDiagnostics.LogEntry>) this.construct.getDiagnostics().getMessages().clone();
            } else {
                ConstructDiagnostics.LogEntry iLoveYou = new ConstructDiagnostics.LogEntry(Component.translatable("mna.constructs.feedback.death").getString(), null);
                ConstructDiagnostics.LogEntry lastLogLine = (ConstructDiagnostics.LogEntry) this.lastDiagnostics.peekLast();
                if (lastLogLine == null || !lastLogLine.equals(iLoveYou)) {
                    this.lastDiagnostics.add(iLoveYou);
                }
            }
            pGuiGraphics.blit(GuiTextures.Entities.CONSTRUCT_DIAGNOSTICS, this.m_252754_(), this.m_252907_(), 0, 0, 143, 207);
            int zebraCount = 0;
            int textXOffset = 16;
            int xPos = this.m_252754_();
            int yPos = this.m_252907_() + 4;
            Iterator<ConstructDiagnostics.LogEntry> diagnosticsIterator = this.lastDiagnostics.descendingIterator();
            label60: while (diagnosticsIterator.hasNext()) {
                ConstructDiagnostics.LogEntry s = (ConstructDiagnostics.LogEntry) diagnosticsIterator.next();
                if (s.icon != null) {
                    pGuiGraphics.blit(s.icon, this.m_252754_() + 2, yPos, 0.0F, 0.0F, 16, 16, 16, 16);
                }
                Iterator taskXStep = this.font.getSplitter().splitLines(Component.literal(s.message), 135 - textXOffset, Style.EMPTY).iterator();
                while (true) {
                    if (taskXStep.hasNext()) {
                        FormattedText props = (FormattedText) taskXStep.next();
                        pGuiGraphics.drawString(this.font, props.getString(), this.m_252754_() + textXOffset + 4, yPos, zebraCount % 2 == 0 ? FastColor.ARGB32.color(255, 255, 255, 255) : FastColor.ARGB32.color(255, 100, 100, 100));
                        yPos += 9;
                        if (yPos <= this.m_252907_() + 195) {
                            continue;
                        }
                    }
                    zebraCount++;
                    yPos += 3;
                    if (yPos > this.m_252907_() + 195) {
                        break label60;
                    }
                    break;
                }
            }
            int statusIconOffset = 14;
            int taskXStep = 24;
            xPos = this.m_252754_();
            yPos = this.m_252907_() - 24;
            for (Iterator<ConstructDiagnostics.TaskHistoryEntry> taskHistoryIterator = this.construct.getDiagnostics().getTaskHistory().descendingIterator(); taskHistoryIterator.hasNext(); xPos += taskXStep) {
                ConstructDiagnostics.TaskHistoryEntry e = (ConstructDiagnostics.TaskHistoryEntry) taskHistoryIterator.next();
                pGuiGraphics.blit(GuiTextures.Entities.CONSTRUCT_DIAGNOSTICS, xPos, yPos, 0, 217, 20, 20);
                if (e.getIcon() != null) {
                    pGuiGraphics.blit(e.getIcon(), xPos + 2, yPos + 2, 0.0F, 0.0F, 16, 16, 16, 16);
                }
                switch(e.getStatus()) {
                    case SUCCESS:
                        pGuiGraphics.blit(GuiTextures.Entities.CONSTRUCT_DIAGNOSTICS, xPos + statusIconOffset, yPos + statusIconOffset, 0, 237, 8, 8);
                        break;
                    case FAILURE:
                        pGuiGraphics.blit(GuiTextures.Entities.CONSTRUCT_DIAGNOSTICS, xPos + statusIconOffset, yPos + statusIconOffset, 8, 237, 8, 8);
                        break;
                    case RUNNING:
                        pGuiGraphics.blit(GuiTextures.Entities.CONSTRUCT_DIAGNOSTICS, xPos + statusIconOffset, yPos + statusIconOffset, 16, 237, 8, 8);
                }
            }
        }
    }

    public void setConstruct(Construct construct) {
        this.construct = construct;
    }

    public Construct getConstruct() {
        return this.construct;
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput) {
    }
}
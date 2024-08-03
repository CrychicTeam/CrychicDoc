package de.keksuccino.fancymenu.customization.element.editor;

import de.keksuccino.fancymenu.customization.element.AbstractElement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EditorElementBorderDisplay implements Renderable {

    public final AbstractEditorElement editorElement;

    public Font font;

    public final EditorElementBorderDisplay.DisplayPosition defaultPosition;

    public final List<EditorElementBorderDisplay.DisplayPosition> alternativePositions;

    public EditorElementBorderDisplay.DisplayPosition currentPosition;

    protected final Map<String, Supplier<Component>> lines;

    public boolean textShadow;

    protected List<Component> renderLines;

    protected int width;

    protected int height;

    public EditorElementBorderDisplay(@NotNull AbstractEditorElement editorElement, @NotNull EditorElementBorderDisplay.DisplayPosition defaultPosition, @Nullable EditorElementBorderDisplay.DisplayPosition... alternativePositions) {
        this.font = Minecraft.getInstance().font;
        this.alternativePositions = new ArrayList();
        this.lines = new LinkedHashMap();
        this.textShadow = true;
        this.renderLines = new ArrayList();
        this.width = 0;
        this.height = 0;
        this.defaultPosition = defaultPosition;
        this.currentPosition = defaultPosition;
        this.editorElement = editorElement;
        if (alternativePositions != null) {
            this.alternativePositions.addAll(Arrays.asList(alternativePositions));
        }
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partial) {
        this.updateDisplay();
        this.renderDisplayLines(graphics);
    }

    protected void renderDisplayLines(GuiGraphics graphics) {
        int x = this.editorElement.getX();
        int y = this.editorElement.getY() - this.getHeight() - 2;
        boolean leftAligned = true;
        if (this.currentPosition == EditorElementBorderDisplay.DisplayPosition.TOP_RIGHT) {
            x = this.editorElement.getX() + this.editorElement.getWidth() - this.getWidth();
            leftAligned = false;
        }
        if (this.currentPosition == EditorElementBorderDisplay.DisplayPosition.RIGHT_TOP) {
            x = this.editorElement.getX() + this.editorElement.getWidth() + 2;
            y = this.editorElement.getY();
        }
        if (this.currentPosition == EditorElementBorderDisplay.DisplayPosition.RIGHT_BOTTOM) {
            x = this.editorElement.getX() + this.editorElement.getWidth() + 2;
            y = this.editorElement.getY() + this.editorElement.getHeight() - this.getHeight();
        }
        if (this.currentPosition == EditorElementBorderDisplay.DisplayPosition.BOTTOM_RIGHT) {
            x = this.editorElement.getX() + this.editorElement.getWidth() - this.getWidth();
            y = this.editorElement.getY() + this.editorElement.getHeight() + 2;
            leftAligned = false;
        }
        if (this.currentPosition == EditorElementBorderDisplay.DisplayPosition.BOTTOM_LEFT) {
            y = this.editorElement.getY() + this.editorElement.getHeight() + 2;
        }
        if (this.currentPosition == EditorElementBorderDisplay.DisplayPosition.LEFT_BOTTOM) {
            x = this.editorElement.getX() - this.getWidth() - 2;
            y = this.editorElement.getY() + this.editorElement.getHeight() - this.getHeight();
            leftAligned = false;
        }
        if (this.currentPosition == EditorElementBorderDisplay.DisplayPosition.LEFT_TOP) {
            x = this.editorElement.getX() - this.getWidth() - 2;
            y = this.editorElement.getY();
            leftAligned = false;
        }
        float scale = this.getScale();
        int lineY = y;
        graphics.pose().pushPose();
        graphics.pose().scale(scale, scale, scale);
        for (Component c : this.renderLines) {
            int lineX = leftAligned ? x : x + (this.getWidth() - (int) ((float) this.font.width(c) * scale));
            graphics.drawString(this.font, c, (int) ((float) lineX / scale), (int) ((float) lineY / scale), -1, this.textShadow);
            lineY = (int) ((float) lineY + (float) (9 + 2) * scale);
        }
        graphics.pose().popPose();
    }

    public void addLine(String identifier, Supplier<Component> lineSupplier) {
        this.lines.put(identifier, lineSupplier);
    }

    public void removeLine(String identifier) {
        this.lines.remove(identifier);
    }

    public void clearLines() {
        this.lines.clear();
    }

    public boolean hasLine(String identifier) {
        return this.lines.containsKey(identifier);
    }

    protected void updateDisplay() {
        this.width = 0;
        this.height = 0;
        this.renderLines.clear();
        for (Supplier<Component> s : this.lines.values()) {
            Component c = (Component) s.get();
            if (c != null) {
                int w = this.font.width(c);
                if (w > this.width) {
                    this.width = w;
                }
                this.height += 9 + 2;
                this.renderLines.add(c);
            }
        }
        this.height = this.height > 0 ? this.height - 2 : 0;
        this.currentPosition = this.findPosition();
    }

    protected float getScale() {
        return !Minecraft.getInstance().isEnforceUnicode() ? 0.5F : 1.0F;
    }

    public int getWidth() {
        return (int) ((float) this.width * this.getScale());
    }

    public int getHeight() {
        return (int) ((float) this.height * this.getScale());
    }

    @NotNull
    protected EditorElementBorderDisplay.DisplayPosition findPosition() {
        List<EditorElementBorderDisplay.DisplayPosition> allowedPositions = new ArrayList(this.alternativePositions);
        allowedPositions.add(0, this.defaultPosition);
        List<EditorElementBorderDisplay.DisplayPosition> possiblePositions = this.getPossiblePositions();
        for (EditorElementBorderDisplay.DisplayPosition p : allowedPositions) {
            if (possiblePositions.contains(p)) {
                return p;
            }
        }
        return this.defaultPosition;
    }

    protected List<EditorElementBorderDisplay.DisplayPosition> getPossiblePositions() {
        List<EditorElementBorderDisplay.DisplayPosition> positions = new ArrayList();
        int screenW = AbstractElement.getScreenWidth();
        int screenH = AbstractElement.getScreenHeight();
        int eleX = this.editorElement.getX();
        int eleY = this.editorElement.getY();
        int eleW = this.editorElement.getWidth();
        int eleH = this.editorElement.getHeight();
        if (eleX >= this.getWidth()) {
            positions.add(EditorElementBorderDisplay.DisplayPosition.LEFT_TOP);
            positions.add(EditorElementBorderDisplay.DisplayPosition.LEFT_BOTTOM);
        }
        if (eleY >= this.getHeight()) {
            positions.add(EditorElementBorderDisplay.DisplayPosition.TOP_LEFT);
            positions.add(EditorElementBorderDisplay.DisplayPosition.TOP_RIGHT);
        }
        if (screenW - (eleX + eleW) >= this.getWidth()) {
            positions.add(EditorElementBorderDisplay.DisplayPosition.RIGHT_TOP);
            positions.add(EditorElementBorderDisplay.DisplayPosition.RIGHT_BOTTOM);
        }
        if (screenH - (eleY + eleH) >= this.getHeight()) {
            positions.add(EditorElementBorderDisplay.DisplayPosition.BOTTOM_LEFT);
            positions.add(EditorElementBorderDisplay.DisplayPosition.BOTTOM_RIGHT);
        }
        return positions;
    }

    public static enum DisplayPosition {

        TOP_LEFT,
        TOP_RIGHT,
        RIGHT_TOP,
        RIGHT_BOTTOM,
        BOTTOM_LEFT,
        BOTTOM_RIGHT,
        LEFT_TOP,
        LEFT_BOTTOM
    }
}
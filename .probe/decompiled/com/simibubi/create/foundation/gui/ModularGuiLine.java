package com.simibubi.create.foundation.gui;

import com.simibubi.create.foundation.gui.widget.ScrollInput;
import com.simibubi.create.foundation.gui.widget.TooltipArea;
import com.simibubi.create.foundation.utility.Couple;
import com.simibubi.create.foundation.utility.Pair;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.nbt.CompoundTag;

public class ModularGuiLine {

    List<Pair<AbstractWidget, String>> widgets = new ArrayList();

    List<Couple<Integer>> customBoxes = new ArrayList();

    boolean speechBubble = false;

    public void renderWidgetBG(int guiLeft, GuiGraphics graphics) {
        boolean first = true;
        if (this.customBoxes.isEmpty()) {
            for (Pair<AbstractWidget, String> pair : this.widgets) {
                if (!pair.getSecond().equals("Dummy")) {
                    AbstractWidget aw = pair.getFirst();
                    int x = aw.getX();
                    int width = aw.getWidth();
                    if (aw instanceof EditBox) {
                        x -= 5;
                        width += 9;
                    }
                    this.box(graphics, x, width, first & this.speechBubble);
                    first = false;
                }
            }
        } else {
            for (Couple<Integer> couple : this.customBoxes) {
                int x = couple.getFirst() + guiLeft;
                int width = couple.getSecond();
                this.box(graphics, x, width, first & this.speechBubble);
                first = false;
            }
        }
    }

    private void box(GuiGraphics graphics, int x, int width, boolean b) {
        UIRenderHelper.drawStretched(graphics, x, 0, width, 18, 0, AllGuiTextures.DATA_AREA);
        if (b) {
            AllGuiTextures.DATA_AREA_SPEECH.render(graphics, x - 3, 0);
        } else {
            AllGuiTextures.DATA_AREA_START.render(graphics, x, 0);
        }
        AllGuiTextures.DATA_AREA_END.render(graphics, x + width - 2, 0);
    }

    public void saveValues(CompoundTag data) {
        for (Pair<AbstractWidget, String> pair : this.widgets) {
            AbstractWidget w = pair.getFirst();
            String key = pair.getSecond();
            if (w instanceof EditBox eb) {
                data.putString(key, eb.getValue());
            }
            if (w instanceof ScrollInput si) {
                data.putInt(key, si.getState());
            }
        }
    }

    public <T extends GuiEventListener & Renderable & NarratableEntry> void loadValues(CompoundTag data, Consumer<T> addRenderable, Consumer<T> addRenderableOnly) {
        for (Pair<AbstractWidget, String> pair : this.widgets) {
            AbstractWidget w = pair.getFirst();
            String key = pair.getSecond();
            if (w instanceof EditBox eb) {
                eb.setValue(data.getString(key));
            }
            if (w instanceof ScrollInput si) {
                si.setState(data.getInt(key));
            }
            if (w instanceof TooltipArea) {
                addRenderableOnly.accept(w);
            } else {
                addRenderable.accept(w);
            }
        }
    }

    public void forEach(Consumer<GuiEventListener> callback) {
        this.widgets.forEach(p -> callback.accept((GuiEventListener) p.getFirst()));
    }

    public void clear() {
        this.widgets.clear();
        this.customBoxes.clear();
    }

    public void add(Pair<AbstractWidget, String> pair) {
        this.widgets.add(pair);
    }
}
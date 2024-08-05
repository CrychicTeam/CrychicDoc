package com.mojang.realmsclient.gui;

import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.realms.RealmsObjectSelectionList;

public abstract class RowButton {

    public final int width;

    public final int height;

    public final int xOffset;

    public final int yOffset;

    public RowButton(int int0, int int1, int int2, int int3) {
        this.width = int0;
        this.height = int1;
        this.xOffset = int2;
        this.yOffset = int3;
    }

    public void drawForRowAt(GuiGraphics guiGraphics0, int int1, int int2, int int3, int int4) {
        int $$5 = int1 + this.xOffset;
        int $$6 = int2 + this.yOffset;
        boolean $$7 = int3 >= $$5 && int3 <= $$5 + this.width && int4 >= $$6 && int4 <= $$6 + this.height;
        this.draw(guiGraphics0, $$5, $$6, $$7);
    }

    protected abstract void draw(GuiGraphics var1, int var2, int var3, boolean var4);

    public int getRight() {
        return this.xOffset + this.width;
    }

    public int getBottom() {
        return this.yOffset + this.height;
    }

    public abstract void onClick(int var1);

    public static void drawButtonsInRow(GuiGraphics guiGraphics0, List<RowButton> listRowButton1, RealmsObjectSelectionList<?> realmsObjectSelectionList2, int int3, int int4, int int5, int int6) {
        for (RowButton $$7 : listRowButton1) {
            if (realmsObjectSelectionList2.getRowWidth() > $$7.getRight()) {
                $$7.drawForRowAt(guiGraphics0, int3, int4, int5, int6);
            }
        }
    }

    public static void rowButtonMouseClicked(RealmsObjectSelectionList<?> realmsObjectSelectionList0, ObjectSelectionList.Entry<?> objectSelectionListEntry1, List<RowButton> listRowButton2, int int3, double double4, double double5) {
        if (int3 == 0) {
            int $$6 = realmsObjectSelectionList0.m_6702_().indexOf(objectSelectionListEntry1);
            if ($$6 > -1) {
                realmsObjectSelectionList0.selectItem($$6);
                int $$7 = realmsObjectSelectionList0.getRowLeft();
                int $$8 = realmsObjectSelectionList0.getRowTop($$6);
                int $$9 = (int) (double4 - (double) $$7);
                int $$10 = (int) (double5 - (double) $$8);
                for (RowButton $$11 : listRowButton2) {
                    if ($$9 >= $$11.xOffset && $$9 <= $$11.getRight() && $$10 >= $$11.yOffset && $$10 <= $$11.getBottom()) {
                        $$11.onClick($$6);
                    }
                }
            }
        }
    }
}
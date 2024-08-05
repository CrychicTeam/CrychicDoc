package se.mickelus.tetra.gui;

import javax.annotation.ParametersAreNonnullByDefault;
import se.mickelus.tetra.items.modular.impl.dynamic.ArchetypeSlotDefinition;

@ParametersAreNonnullByDefault
public class GuiModuleOffsets {

    private final int[] offsetX;

    private final int[] offsetY;

    private final boolean[] alignment;

    public GuiModuleOffsets(int... offsets) {
        this.offsetX = new int[offsets.length / 2];
        this.offsetY = new int[offsets.length / 2];
        this.alignment = new boolean[offsets.length / 2];
        for (int i = 0; i < offsets.length / 2; i++) {
            this.offsetX[i] = offsets[i * 2];
            this.offsetY[i] = offsets[i * 2 + 1];
            this.alignment[i] = this.offsetX[i] > 0;
        }
    }

    public GuiModuleOffsets(ArchetypeSlotDefinition[] slots) {
        this.offsetX = new int[slots.length];
        this.offsetY = new int[slots.length];
        this.alignment = new boolean[slots.length];
        for (int i = 0; i < slots.length; i++) {
            this.offsetX[i] = slots[i].x();
            this.offsetY[i] = slots[i].y();
            this.alignment[i] = this.offsetX[i] > 0;
        }
    }

    public int size() {
        return this.offsetX.length;
    }

    public int getX(int index) {
        return this.offsetX[index];
    }

    public int getY(int index) {
        return this.offsetY[index];
    }

    public boolean getAlignment(int index) {
        return this.alignment[index];
    }
}
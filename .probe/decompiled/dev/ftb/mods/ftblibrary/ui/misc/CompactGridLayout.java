package dev.ftb.mods.ftblibrary.ui.misc;

import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftblibrary.ui.Widget;
import dev.ftb.mods.ftblibrary.ui.WidgetLayout;

public class CompactGridLayout implements WidgetLayout {

    private static final int[][] LAYOUTS = new int[][] { { 1 }, { 2 }, { 3 }, { 4 }, { 3, 2 }, { 3, 3 }, { 4, 3 }, { 4, 4 }, { 3, 3, 3 }, { 3, 4, 3 }, { 4, 3, 4 }, { 4, 4, 4 }, { 4, 3, 3, 3 }, { 3, 4, 4, 3 }, { 4, 4, 4, 3 }, { 4, 4, 4, 4 } };

    private final int size;

    public CompactGridLayout(int s) {
        this.size = s;
    }

    @Override
    public int align(Panel panel) {
        int nWidgets = panel.getWidgets().size();
        if (nWidgets == 0) {
            return 0;
        } else if (nWidgets > LAYOUTS.length) {
            for (int i = 0; i < nWidgets; i++) {
                ((Widget) panel.getWidgets().get(i)).setPosAndSize(i % 4 * this.size, i / 4 * this.size, this.size, this.size);
            }
            return nWidgets / 4 * this.size;
        } else {
            int[] layout = LAYOUTS[nWidgets - 1];
            int max = 0;
            for (int v : layout) {
                max = Math.max(max, v);
            }
            int off = 0;
            for (int l = 0; l < layout.length; l++) {
                int o = layout[l] % 2 == max % 2 ? 0 : this.size / 2;
                for (int i = 0; i < layout[l]; i++) {
                    ((Widget) panel.getWidgets().get(off + i)).setPosAndSize(o + i * this.size, l * this.size, this.size, this.size);
                }
                off += layout[l];
            }
            return layout.length * this.size;
        }
    }
}
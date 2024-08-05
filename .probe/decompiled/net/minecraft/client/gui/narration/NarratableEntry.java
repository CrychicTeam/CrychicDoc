package net.minecraft.client.gui.narration;

import net.minecraft.client.gui.components.TabOrderedElement;

public interface NarratableEntry extends TabOrderedElement, NarrationSupplier {

    NarratableEntry.NarrationPriority narrationPriority();

    default boolean isActive() {
        return true;
    }

    public static enum NarrationPriority {

        NONE, HOVERED, FOCUSED;

        public boolean isTerminal() {
            return this == FOCUSED;
        }
    }
}
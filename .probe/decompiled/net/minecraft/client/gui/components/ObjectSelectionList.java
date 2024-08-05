package net.minecraft.client.gui.components;

import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ComponentPath;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.narration.NarrationSupplier;
import net.minecraft.client.gui.navigation.FocusNavigationEvent;
import net.minecraft.network.chat.Component;

public abstract class ObjectSelectionList<E extends ObjectSelectionList.Entry<E>> extends AbstractSelectionList<E> {

    private static final Component USAGE_NARRATION = Component.translatable("narration.selection.usage");

    public ObjectSelectionList(Minecraft minecraft0, int int1, int int2, int int3, int int4, int int5) {
        super(minecraft0, int1, int2, int3, int4, int5);
    }

    @Nullable
    @Override
    public ComponentPath nextFocusPath(FocusNavigationEvent focusNavigationEvent0) {
        if (this.m_5773_() == 0) {
            return null;
        } else if (this.m_93696_() && focusNavigationEvent0 instanceof FocusNavigationEvent.ArrowNavigation $$1) {
            E $$2 = (E) this.m_264254_($$1.direction());
            return $$2 != null ? ComponentPath.path(this, ComponentPath.leaf($$2)) : null;
        } else if (!this.m_93696_()) {
            E $$3 = (E) this.m_93511_();
            if ($$3 == null) {
                $$3 = (E) this.m_264254_(focusNavigationEvent0.getVerticalDirectionForInitialFocus());
            }
            return $$3 == null ? null : ComponentPath.path(this, ComponentPath.leaf($$3));
        } else {
            return null;
        }
    }

    @Override
    public void updateNarration(NarrationElementOutput narrationElementOutput0) {
        E $$1 = (E) this.m_168795_();
        if ($$1 != null) {
            this.m_168790_(narrationElementOutput0.nest(), $$1);
            $$1.updateNarration(narrationElementOutput0);
        } else {
            E $$2 = (E) this.m_93511_();
            if ($$2 != null) {
                this.m_168790_(narrationElementOutput0.nest(), $$2);
                $$2.updateNarration(narrationElementOutput0);
            }
        }
        if (this.m_93696_()) {
            narrationElementOutput0.add(NarratedElementType.USAGE, USAGE_NARRATION);
        }
    }

    public abstract static class Entry<E extends ObjectSelectionList.Entry<E>> extends AbstractSelectionList.Entry<E> implements NarrationSupplier {

        public abstract Component getNarration();

        @Override
        public void updateNarration(NarrationElementOutput narrationElementOutput0) {
            narrationElementOutput0.add(NarratedElementType.TITLE, this.getNarration());
        }
    }
}
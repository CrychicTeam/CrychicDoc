package net.minecraft.client.gui.components.events;

import javax.annotation.Nullable;

public abstract class AbstractContainerEventHandler implements ContainerEventHandler {

    @Nullable
    private GuiEventListener focused;

    private boolean isDragging;

    @Override
    public final boolean isDragging() {
        return this.isDragging;
    }

    @Override
    public final void setDragging(boolean boolean0) {
        this.isDragging = boolean0;
    }

    @Nullable
    @Override
    public GuiEventListener getFocused() {
        return this.focused;
    }

    @Override
    public void setFocused(@Nullable GuiEventListener guiEventListener0) {
        if (this.focused != null) {
            this.focused.setFocused(false);
        }
        if (guiEventListener0 != null) {
            guiEventListener0.setFocused(true);
        }
        this.focused = guiEventListener0;
    }
}
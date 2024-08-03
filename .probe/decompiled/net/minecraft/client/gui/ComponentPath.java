package net.minecraft.client.gui;

import javax.annotation.Nullable;
import net.minecraft.client.gui.components.events.ContainerEventHandler;
import net.minecraft.client.gui.components.events.GuiEventListener;

public interface ComponentPath {

    static ComponentPath leaf(GuiEventListener guiEventListener0) {
        return new ComponentPath.Leaf(guiEventListener0);
    }

    @Nullable
    static ComponentPath path(ContainerEventHandler containerEventHandler0, @Nullable ComponentPath componentPath1) {
        return componentPath1 == null ? null : new ComponentPath.Path(containerEventHandler0, componentPath1);
    }

    static ComponentPath path(GuiEventListener guiEventListener0, ContainerEventHandler... containerEventHandler1) {
        ComponentPath $$2 = leaf(guiEventListener0);
        for (ContainerEventHandler $$3 : containerEventHandler1) {
            $$2 = path($$3, $$2);
        }
        return $$2;
    }

    GuiEventListener component();

    void applyFocus(boolean var1);

    public static record Leaf(GuiEventListener f_263802_) implements ComponentPath {

        private final GuiEventListener component;

        public Leaf(GuiEventListener f_263802_) {
            this.component = f_263802_;
        }

        @Override
        public void applyFocus(boolean p_265248_) {
            this.component.setFocused(p_265248_);
        }
    }

    public static record Path(ContainerEventHandler f_263715_, ComponentPath f_263808_) implements ComponentPath {

        private final ContainerEventHandler component;

        private final ComponentPath childPath;

        public Path(ContainerEventHandler f_263715_, ComponentPath f_263808_) {
            this.component = f_263715_;
            this.childPath = f_263808_;
        }

        @Override
        public void applyFocus(boolean p_265230_) {
            if (!p_265230_) {
                this.component.setFocused(null);
            } else {
                this.component.setFocused(this.childPath.component());
            }
            this.childPath.applyFocus(p_265230_);
        }
    }
}
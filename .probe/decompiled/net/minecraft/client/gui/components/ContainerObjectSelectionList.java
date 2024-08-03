package net.minecraft.client.gui.components;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ComponentPath;
import net.minecraft.client.gui.components.events.ContainerEventHandler;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.navigation.FocusNavigationEvent;
import net.minecraft.client.gui.navigation.ScreenAxis;
import net.minecraft.client.gui.navigation.ScreenDirection;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

public abstract class ContainerObjectSelectionList<E extends ContainerObjectSelectionList.Entry<E>> extends AbstractSelectionList<E> {

    public ContainerObjectSelectionList(Minecraft minecraft0, int int1, int int2, int int3, int int4, int int5) {
        super(minecraft0, int1, int2, int3, int4, int5);
    }

    @Nullable
    @Override
    public ComponentPath nextFocusPath(FocusNavigationEvent focusNavigationEvent0) {
        if (this.m_5773_() == 0) {
            return null;
        } else if (!(focusNavigationEvent0 instanceof FocusNavigationEvent.ArrowNavigation $$1)) {
            return super.m_264064_(focusNavigationEvent0);
        } else {
            E $$2 = (E) this.m_7222_();
            if ($$1.direction().getAxis() == ScreenAxis.HORIZONTAL && $$2 != null) {
                return ComponentPath.path(this, $$2.nextFocusPath(focusNavigationEvent0));
            } else {
                int $$3 = -1;
                ScreenDirection $$4 = $$1.direction();
                if ($$2 != null) {
                    $$3 = $$2.m_6702_().indexOf($$2.getFocused());
                }
                if ($$3 == -1) {
                    switch($$4) {
                        case LEFT:
                            $$3 = Integer.MAX_VALUE;
                            $$4 = ScreenDirection.DOWN;
                            break;
                        case RIGHT:
                            $$3 = 0;
                            $$4 = ScreenDirection.DOWN;
                            break;
                        default:
                            $$3 = 0;
                    }
                }
                E $$5 = $$2;
                ComponentPath $$6;
                do {
                    $$5 = (E) this.m_264238_($$4, p_265784_ -> !p_265784_.m_6702_().isEmpty(), $$5);
                    if ($$5 == null) {
                        return null;
                    }
                    $$6 = $$5.focusPathAtIndex($$1, $$3);
                } while ($$6 == null);
                return ComponentPath.path(this, $$6);
            }
        }
    }

    @Override
    public void setFocused(@Nullable GuiEventListener guiEventListener0) {
        super.setFocused(guiEventListener0);
        if (guiEventListener0 == null) {
            this.m_6987_(null);
        }
    }

    @Override
    public NarratableEntry.NarrationPriority narrationPriority() {
        return this.m_93696_() ? NarratableEntry.NarrationPriority.FOCUSED : super.narrationPriority();
    }

    @Override
    protected boolean isSelectedItem(int int0) {
        return false;
    }

    @Override
    public void updateNarration(NarrationElementOutput narrationElementOutput0) {
        E $$1 = (E) this.m_168795_();
        if ($$1 != null) {
            $$1.updateNarration(narrationElementOutput0.nest());
            this.m_168790_(narrationElementOutput0, $$1);
        } else {
            E $$2 = (E) this.m_7222_();
            if ($$2 != null) {
                $$2.updateNarration(narrationElementOutput0.nest());
                this.m_168790_(narrationElementOutput0, $$2);
            }
        }
        narrationElementOutput0.add(NarratedElementType.USAGE, Component.translatable("narration.component_list.usage"));
    }

    public abstract static class Entry<E extends ContainerObjectSelectionList.Entry<E>> extends AbstractSelectionList.Entry<E> implements ContainerEventHandler {

        @Nullable
        private GuiEventListener focused;

        @Nullable
        private NarratableEntry lastNarratable;

        private boolean dragging;

        @Override
        public boolean isDragging() {
            return this.dragging;
        }

        @Override
        public void setDragging(boolean boolean0) {
            this.dragging = boolean0;
        }

        @Override
        public boolean mouseClicked(double double0, double double1, int int2) {
            return ContainerEventHandler.super.mouseClicked(double0, double1, int2);
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

        @Nullable
        @Override
        public GuiEventListener getFocused() {
            return this.focused;
        }

        @Nullable
        public ComponentPath focusPathAtIndex(FocusNavigationEvent focusNavigationEvent0, int int1) {
            if (this.m_6702_().isEmpty()) {
                return null;
            } else {
                ComponentPath $$2 = ((GuiEventListener) this.m_6702_().get(Math.min(int1, this.m_6702_().size() - 1))).nextFocusPath(focusNavigationEvent0);
                return ComponentPath.path(this, $$2);
            }
        }

        @Nullable
        @Override
        public ComponentPath nextFocusPath(FocusNavigationEvent focusNavigationEvent0) {
            if (focusNavigationEvent0 instanceof FocusNavigationEvent.ArrowNavigation $$1) {
                int $$2 = switch($$1.direction()) {
                    case LEFT ->
                        -1;
                    case RIGHT ->
                        1;
                    case UP, DOWN ->
                        0;
                };
                if ($$2 == 0) {
                    return null;
                }
                int $$3 = Mth.clamp($$2 + this.m_6702_().indexOf(this.getFocused()), 0, this.m_6702_().size() - 1);
                for (int $$4 = $$3; $$4 >= 0 && $$4 < this.m_6702_().size(); $$4 += $$2) {
                    GuiEventListener $$5 = (GuiEventListener) this.m_6702_().get($$4);
                    ComponentPath $$6 = $$5.nextFocusPath(focusNavigationEvent0);
                    if ($$6 != null) {
                        return ComponentPath.path(this, $$6);
                    }
                }
            }
            return ContainerEventHandler.super.nextFocusPath(focusNavigationEvent0);
        }

        public abstract List<? extends NarratableEntry> narratables();

        void updateNarration(NarrationElementOutput narrationElementOutput0) {
            List<? extends NarratableEntry> $$1 = this.narratables();
            Screen.NarratableSearchResult $$2 = Screen.findNarratableWidget($$1, this.lastNarratable);
            if ($$2 != null) {
                if ($$2.priority.isTerminal()) {
                    this.lastNarratable = $$2.entry;
                }
                if ($$1.size() > 1) {
                    narrationElementOutput0.add(NarratedElementType.POSITION, Component.translatable("narrator.position.object_list", $$2.index + 1, $$1.size()));
                    if ($$2.priority == NarratableEntry.NarrationPriority.FOCUSED) {
                        narrationElementOutput0.add(NarratedElementType.USAGE, Component.translatable("narration.component_list.usage"));
                    }
                }
                $$2.entry.m_142291_(narrationElementOutput0.nest());
            }
        }
    }
}
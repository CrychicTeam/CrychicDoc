package me.shedaniel.clothconfig2.gui.widget;

import java.util.List;
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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

@OnlyIn(Dist.CLIENT)
public abstract class DynamicElementListWidget<E extends DynamicElementListWidget.ElementEntry<E>> extends DynamicSmoothScrollingEntryListWidget<E> {

    private static final Component USAGE_NARRATION = Component.translatable("narration.selection.usage");

    public DynamicElementListWidget(Minecraft client, int width, int height, int top, int bottom, ResourceLocation backgroundLocation) {
        super(client, width, height, top, bottom, backgroundLocation);
    }

    @Nullable
    @Override
    public ComponentPath nextFocusPath(FocusNavigationEvent focusNavigationEvent) {
        if (this.getItemCount() == 0) {
            return null;
        } else if (!(focusNavigationEvent instanceof FocusNavigationEvent.ArrowNavigation arrowNavigation)) {
            return super.m_264064_(focusNavigationEvent);
        } else {
            DynamicElementListWidget.ElementEntry entry = this.getFocused();
            if (arrowNavigation.direction().getAxis() == ScreenAxis.HORIZONTAL && entry != null) {
                return ComponentPath.path(this, entry.nextFocusPath(focusNavigationEvent));
            } else {
                int i = -1;
                ScreenDirection screenDirection = arrowNavigation.direction();
                if (entry != null) {
                    i = entry.m_6702_().indexOf(entry.getFocused());
                }
                if (i == -1) {
                    switch(screenDirection) {
                        case LEFT:
                            i = Integer.MAX_VALUE;
                            screenDirection = ScreenDirection.DOWN;
                            break;
                        case RIGHT:
                            i = 0;
                            screenDirection = ScreenDirection.DOWN;
                            break;
                        default:
                            i = 0;
                    }
                }
                E entry2 = (E) entry;
                ComponentPath componentPath;
                do {
                    entry2 = this.nextEntry(screenDirection, entryx -> !entryx.m_6702_().isEmpty(), entry2);
                    if (entry2 == null) {
                        return null;
                    }
                    componentPath = entry2.focusPathAtIndex(arrowNavigation, i);
                } while (componentPath == null);
                return ComponentPath.path(this, componentPath);
            }
        }
    }

    @Override
    public void updateNarration(NarrationElementOutput narrationElementOutput) {
        E entry = this.hoveredItem;
        if (entry != null) {
            entry.updateNarration(narrationElementOutput.nest());
            this.narrateListElementPosition(narrationElementOutput, entry);
        } else {
            E entry2 = this.getFocused();
            if (entry2 != null) {
                entry2.updateNarration(narrationElementOutput.nest());
                this.narrateListElementPosition(narrationElementOutput, entry2);
            }
        }
        narrationElementOutput.add(NarratedElementType.USAGE, Component.translatable("narration.component_list.usage"));
    }

    @Override
    public void setFocused(@Nullable GuiEventListener guiEventListener) {
        super.m_7522_(guiEventListener);
        if (guiEventListener == null) {
            this.selectItem(null);
        }
    }

    @Override
    public NarratableEntry.NarrationPriority narrationPriority() {
        return this.m_93696_() ? NarratableEntry.NarrationPriority.FOCUSED : super.m_142684_();
    }

    @Override
    protected boolean isSelected(int i) {
        return false;
    }

    @OnlyIn(Dist.CLIENT)
    public abstract static class ElementEntry<E extends DynamicElementListWidget.ElementEntry<E>> extends DynamicEntryListWidget.Entry<E> implements ContainerEventHandler, NarratableEntry {

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
        public void setDragging(boolean bl) {
            this.dragging = bl;
        }

        @Nullable
        @Override
        public GuiEventListener getFocused() {
            return this.focused;
        }

        @Override
        public void setFocused(@Nullable GuiEventListener guiEventListener) {
            if (this.focused != null) {
                this.focused.setFocused(false);
            }
            if (guiEventListener != null) {
                guiEventListener.setFocused(true);
            }
            this.focused = guiEventListener;
        }

        @Nullable
        public ComponentPath focusPathAtIndex(FocusNavigationEvent focusNavigationEvent, int i) {
            if (this.m_6702_().isEmpty()) {
                return null;
            } else {
                ComponentPath componentPath = ((GuiEventListener) this.m_6702_().get(Math.min(i, this.m_6702_().size() - 1))).nextFocusPath(focusNavigationEvent);
                return ComponentPath.path(this, componentPath);
            }
        }

        @Nullable
        @Override
        public ComponentPath nextFocusPath(FocusNavigationEvent focusNavigationEvent) {
            if (focusNavigationEvent instanceof FocusNavigationEvent.ArrowNavigation arrowNavigation) {
                int var10000 = switch(arrowNavigation.direction()) {
                    case LEFT ->
                        -1;
                    case RIGHT ->
                        1;
                    case UP, DOWN ->
                        0;
                };
                if (var10000 == 0) {
                    return null;
                }
                int j = Mth.clamp(var10000 + this.m_6702_().indexOf(this.getFocused()), 0, this.m_6702_().size() - 1);
                for (int k = j; k >= 0 && k < this.m_6702_().size(); k += var10000) {
                    GuiEventListener guiEventListener = (GuiEventListener) this.m_6702_().get(k);
                    ComponentPath componentPath = guiEventListener.nextFocusPath(focusNavigationEvent);
                    if (componentPath != null) {
                        return ComponentPath.path(this, componentPath);
                    }
                }
            }
            return ContainerEventHandler.super.nextFocusPath(focusNavigationEvent);
        }

        @Override
        public abstract List<? extends NarratableEntry> narratables();

        @Override
        public void updateNarration(NarrationElementOutput narrationElementOutput) {
            List<? extends NarratableEntry> list = this.narratables();
            Screen.NarratableSearchResult narratableSearchResult = Screen.findNarratableWidget(list, this.lastNarratable);
            if (narratableSearchResult != null) {
                if (narratableSearchResult.priority.isTerminal()) {
                    this.lastNarratable = narratableSearchResult.entry;
                }
                if (list.size() > 1) {
                    narrationElementOutput.add(NarratedElementType.POSITION, Component.translatable("narrator.position.object_list", narratableSearchResult.index + 1, list.size()));
                    if (narratableSearchResult.priority == NarratableEntry.NarrationPriority.FOCUSED) {
                        narrationElementOutput.add(NarratedElementType.USAGE, Component.translatable("narration.component_list.usage"));
                    }
                }
                narratableSearchResult.entry.m_142291_(narrationElementOutput.nest());
            }
        }

        @Override
        public boolean isActive() {
            return false;
        }

        @Override
        public NarratableEntry.NarrationPriority narrationPriority() {
            return this.m_93696_() ? NarratableEntry.NarrationPriority.FOCUSED : NarratableEntry.NarrationPriority.NONE;
        }

        @Override
        public boolean mouseClicked(double d, double e, int i) {
            return !this.isEnabled() ? false : ContainerEventHandler.super.mouseClicked(d, e, i);
        }

        @Override
        public boolean mouseReleased(double d, double e, int i) {
            return !this.isEnabled() ? false : ContainerEventHandler.super.mouseReleased(d, e, i);
        }

        @Override
        public boolean mouseDragged(double d, double e, int i, double f, double g) {
            return !this.isEnabled() ? false : ContainerEventHandler.super.mouseDragged(d, e, i, f, g);
        }

        @Override
        public boolean mouseScrolled(double d, double e, double f) {
            return !this.isEnabled() ? false : ContainerEventHandler.super.mouseScrolled(d, e, f);
        }

        @Override
        public boolean keyPressed(int i, int j, int k) {
            return !this.isEnabled() ? false : ContainerEventHandler.super.keyPressed(i, j, k);
        }

        @Override
        public boolean keyReleased(int i, int j, int k) {
            return !this.isEnabled() ? false : ContainerEventHandler.super.keyReleased(i, j, k);
        }

        @Override
        public boolean charTyped(char c, int i) {
            return !this.isEnabled() ? false : ContainerEventHandler.super.charTyped(c, i);
        }
    }
}
package net.minecraft.client.gui.components.events;

import com.mojang.datafixers.util.Pair;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.client.gui.ComponentPath;
import net.minecraft.client.gui.navigation.FocusNavigationEvent;
import net.minecraft.client.gui.navigation.ScreenAxis;
import net.minecraft.client.gui.navigation.ScreenDirection;
import net.minecraft.client.gui.navigation.ScreenPosition;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import org.joml.Vector2i;

public interface ContainerEventHandler extends GuiEventListener {

    List<? extends GuiEventListener> children();

    default Optional<GuiEventListener> getChildAt(double double0, double double1) {
        for (GuiEventListener $$2 : this.children()) {
            if ($$2.isMouseOver(double0, double1)) {
                return Optional.of($$2);
            }
        }
        return Optional.empty();
    }

    @Override
    default boolean mouseClicked(double double0, double double1, int int2) {
        for (GuiEventListener $$3 : this.children()) {
            if ($$3.mouseClicked(double0, double1, int2)) {
                this.setFocused($$3);
                if (int2 == 0) {
                    this.setDragging(true);
                }
                return true;
            }
        }
        return false;
    }

    @Override
    default boolean mouseReleased(double double0, double double1, int int2) {
        this.setDragging(false);
        return this.getChildAt(double0, double1).filter(p_94708_ -> p_94708_.mouseReleased(double0, double1, int2)).isPresent();
    }

    @Override
    default boolean mouseDragged(double double0, double double1, int int2, double double3, double double4) {
        return this.getFocused() != null && this.isDragging() && int2 == 0 ? this.getFocused().mouseDragged(double0, double1, int2, double3, double4) : false;
    }

    boolean isDragging();

    void setDragging(boolean var1);

    @Override
    default boolean mouseScrolled(double double0, double double1, double double2) {
        return this.getChildAt(double0, double1).filter(p_94693_ -> p_94693_.mouseScrolled(double0, double1, double2)).isPresent();
    }

    @Override
    default boolean keyPressed(int int0, int int1, int int2) {
        return this.getFocused() != null && this.getFocused().keyPressed(int0, int1, int2);
    }

    @Override
    default boolean keyReleased(int int0, int int1, int int2) {
        return this.getFocused() != null && this.getFocused().keyReleased(int0, int1, int2);
    }

    @Override
    default boolean charTyped(char char0, int int1) {
        return this.getFocused() != null && this.getFocused().charTyped(char0, int1);
    }

    @Nullable
    GuiEventListener getFocused();

    void setFocused(@Nullable GuiEventListener var1);

    @Override
    default void setFocused(boolean boolean0) {
    }

    @Override
    default boolean isFocused() {
        return this.getFocused() != null;
    }

    @Nullable
    @Override
    default ComponentPath getCurrentFocusPath() {
        GuiEventListener $$0 = this.getFocused();
        return $$0 != null ? ComponentPath.path(this, $$0.getCurrentFocusPath()) : null;
    }

    default void magicalSpecialHackyFocus(@Nullable GuiEventListener guiEventListener0) {
        this.setFocused(guiEventListener0);
    }

    @Nullable
    @Override
    default ComponentPath nextFocusPath(FocusNavigationEvent focusNavigationEvent0) {
        GuiEventListener $$1 = this.getFocused();
        if ($$1 != null) {
            ComponentPath $$2 = $$1.nextFocusPath(focusNavigationEvent0);
            if ($$2 != null) {
                return ComponentPath.path(this, $$2);
            }
        }
        if (focusNavigationEvent0 instanceof FocusNavigationEvent.TabNavigation $$3) {
            return this.handleTabNavigation($$3);
        } else {
            return focusNavigationEvent0 instanceof FocusNavigationEvent.ArrowNavigation $$4 ? this.handleArrowNavigation($$4) : null;
        }
    }

    @Nullable
    private ComponentPath handleTabNavigation(FocusNavigationEvent.TabNavigation focusNavigationEventTabNavigation0) {
        boolean $$1 = focusNavigationEventTabNavigation0.forward();
        GuiEventListener $$2 = this.getFocused();
        List<? extends GuiEventListener> $$3 = new ArrayList(this.children());
        Collections.sort($$3, Comparator.comparingInt(p_289623_ -> p_289623_.m_267579_()));
        int $$4 = $$3.indexOf($$2);
        int $$5;
        if ($$2 != null && $$4 >= 0) {
            $$5 = $$4 + ($$1 ? 1 : 0);
        } else if ($$1) {
            $$5 = 0;
        } else {
            $$5 = $$3.size();
        }
        ListIterator<? extends GuiEventListener> $$8 = $$3.listIterator($$5);
        BooleanSupplier $$9 = $$1 ? $$8::hasNext : $$8::hasPrevious;
        Supplier<? extends GuiEventListener> $$10 = $$1 ? $$8::next : $$8::previous;
        while ($$9.getAsBoolean()) {
            GuiEventListener $$11 = (GuiEventListener) $$10.get();
            ComponentPath $$12 = $$11.nextFocusPath(focusNavigationEventTabNavigation0);
            if ($$12 != null) {
                return ComponentPath.path(this, $$12);
            }
        }
        return null;
    }

    @Nullable
    private ComponentPath handleArrowNavigation(FocusNavigationEvent.ArrowNavigation focusNavigationEventArrowNavigation0) {
        GuiEventListener $$1 = this.getFocused();
        if ($$1 == null) {
            ScreenDirection $$2 = focusNavigationEventArrowNavigation0.direction();
            ScreenRectangle $$3 = this.m_264198_().getBorder($$2.getOpposite());
            return ComponentPath.path(this, this.nextFocusPathInDirection($$3, $$2, null, focusNavigationEventArrowNavigation0));
        } else {
            ScreenRectangle $$4 = $$1.getRectangle();
            return ComponentPath.path(this, this.nextFocusPathInDirection($$4, focusNavigationEventArrowNavigation0.direction(), $$1, focusNavigationEventArrowNavigation0));
        }
    }

    @Nullable
    private ComponentPath nextFocusPathInDirection(ScreenRectangle screenRectangle0, ScreenDirection screenDirection1, @Nullable GuiEventListener guiEventListener2, FocusNavigationEvent focusNavigationEvent3) {
        ScreenAxis $$4 = screenDirection1.getAxis();
        ScreenAxis $$5 = $$4.orthogonal();
        ScreenDirection $$6 = $$5.getPositive();
        int $$7 = screenRectangle0.getBoundInDirection(screenDirection1.getOpposite());
        List<GuiEventListener> $$8 = new ArrayList();
        for (GuiEventListener $$9 : this.children()) {
            if ($$9 != guiEventListener2) {
                ScreenRectangle $$10 = $$9.getRectangle();
                if ($$10.overlapsInAxis(screenRectangle0, $$5)) {
                    int $$11 = $$10.getBoundInDirection(screenDirection1.getOpposite());
                    if (screenDirection1.isAfter($$11, $$7)) {
                        $$8.add($$9);
                    } else if ($$11 == $$7 && screenDirection1.isAfter($$10.getBoundInDirection(screenDirection1), screenRectangle0.getBoundInDirection(screenDirection1))) {
                        $$8.add($$9);
                    }
                }
            }
        }
        Comparator<GuiEventListener> $$12 = Comparator.comparing(p_264674_ -> p_264674_.getRectangle().getBoundInDirection(screenDirection1.getOpposite()), screenDirection1.coordinateValueComparator());
        Comparator<GuiEventListener> $$13 = Comparator.comparing(p_264676_ -> p_264676_.getRectangle().getBoundInDirection($$6.getOpposite()), $$6.coordinateValueComparator());
        $$8.sort($$12.thenComparing($$13));
        for (GuiEventListener $$14 : $$8) {
            ComponentPath $$15 = $$14.nextFocusPath(focusNavigationEvent3);
            if ($$15 != null) {
                return $$15;
            }
        }
        return this.nextFocusPathVaguelyInDirection(screenRectangle0, screenDirection1, guiEventListener2, focusNavigationEvent3);
    }

    @Nullable
    private ComponentPath nextFocusPathVaguelyInDirection(ScreenRectangle screenRectangle0, ScreenDirection screenDirection1, @Nullable GuiEventListener guiEventListener2, FocusNavigationEvent focusNavigationEvent3) {
        ScreenAxis $$4 = screenDirection1.getAxis();
        ScreenAxis $$5 = $$4.orthogonal();
        List<Pair<GuiEventListener, Long>> $$6 = new ArrayList();
        ScreenPosition $$7 = ScreenPosition.of($$4, screenRectangle0.getBoundInDirection(screenDirection1), screenRectangle0.getCenterInAxis($$5));
        for (GuiEventListener $$8 : this.children()) {
            if ($$8 != guiEventListener2) {
                ScreenRectangle $$9 = $$8.getRectangle();
                ScreenPosition $$10 = ScreenPosition.of($$4, $$9.getBoundInDirection(screenDirection1.getOpposite()), $$9.getCenterInAxis($$5));
                if (screenDirection1.isAfter($$10.getCoordinate($$4), $$7.getCoordinate($$4))) {
                    long $$11 = Vector2i.distanceSquared($$7.x(), $$7.y(), $$10.x(), $$10.y());
                    $$6.add(Pair.of($$8, $$11));
                }
            }
        }
        $$6.sort(Comparator.comparingDouble(Pair::getSecond));
        for (Pair<GuiEventListener, Long> $$12 : $$6) {
            ComponentPath $$13 = ((GuiEventListener) $$12.getFirst()).nextFocusPath(focusNavigationEvent3);
            if ($$13 != null) {
                return $$13;
            }
        }
        return null;
    }
}
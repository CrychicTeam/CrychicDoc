package com.simibubi.create.foundation.gui;

import com.simibubi.create.foundation.ponder.ui.NavigatableSimiScreen;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;

public class ScreenOpener {

    private static final Deque<Screen> backStack = new ArrayDeque();

    private static Screen backSteppedFrom = null;

    public static void open(Screen screen) {
        open(Minecraft.getInstance().screen, screen);
    }

    public static void open(@Nullable Screen current, Screen toOpen) {
        backSteppedFrom = null;
        if (current != null) {
            if (backStack.size() >= 15) {
                backStack.pollLast();
            }
            backStack.push(current);
        } else {
            backStack.clear();
        }
        openScreen(toOpen);
    }

    public static void openPreviousScreen(Screen current, Optional<NavigatableSimiScreen> screenWithContext) {
        if (!backStack.isEmpty()) {
            backSteppedFrom = current;
            Screen previousScreen = (Screen) backStack.pop();
            if (previousScreen instanceof NavigatableSimiScreen previousAbstractSimiScreen) {
                screenWithContext.ifPresent(s -> s.shareContextWith(previousAbstractSimiScreen));
                previousAbstractSimiScreen.transition.startWithValue(-0.1).chase(-1.0, 0.4F, LerpedFloat.Chaser.EXP);
            }
            openScreen(previousScreen);
        }
    }

    public static void transitionTo(NavigatableSimiScreen screen) {
        if (!tryBackTracking(screen)) {
            screen.transition.startWithValue(0.1).chase(1.0, 0.4F, LerpedFloat.Chaser.EXP);
            open(screen);
        }
    }

    private static boolean tryBackTracking(NavigatableSimiScreen screen) {
        List<Screen> screenHistory = getScreenHistory();
        if (screenHistory.isEmpty()) {
            return false;
        } else {
            Screen previouslyRenderedScreen = (Screen) screenHistory.get(0);
            if (!(previouslyRenderedScreen instanceof NavigatableSimiScreen)) {
                return false;
            } else if (!screen.isEquivalentTo((NavigatableSimiScreen) previouslyRenderedScreen)) {
                return false;
            } else {
                openPreviousScreen(Minecraft.getInstance().screen, Optional.of(screen));
                return true;
            }
        }
    }

    public static void clearStack() {
        backStack.clear();
    }

    public static List<Screen> getScreenHistory() {
        return new ArrayList(backStack);
    }

    @Nullable
    public static Screen getPreviouslyRenderedScreen() {
        return backSteppedFrom != null ? backSteppedFrom : (Screen) backStack.peek();
    }

    private static void openScreen(Screen screen) {
        Minecraft.getInstance().m_6937_(() -> {
            Minecraft.getInstance().setScreen(screen);
            Screen previouslyRenderedScreen = getPreviouslyRenderedScreen();
            if (previouslyRenderedScreen != null && screen instanceof NavigatableSimiScreen) {
                previouslyRenderedScreen.init(Minecraft.getInstance(), screen.width, screen.height);
            }
        });
    }
}
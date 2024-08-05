package icyllis.modernui.transition;

import icyllis.modernui.view.View;
import icyllis.modernui.view.ViewGroup;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class Scene {

    private final ViewGroup mSceneRoot;

    private View mLayout;

    private Runnable mEnterAction;

    private Runnable mExitAction;

    public Scene(@Nonnull ViewGroup sceneRoot) {
        this.mSceneRoot = sceneRoot;
    }

    public Scene(@Nonnull ViewGroup sceneRoot, @Nonnull View layout) {
        this.mSceneRoot = sceneRoot;
        this.mLayout = layout;
    }

    @Nonnull
    public ViewGroup getSceneRoot() {
        return this.mSceneRoot;
    }

    public void exit() {
        if (getCurrentScene(this.mSceneRoot) == this && this.mExitAction != null) {
            this.mExitAction.run();
        }
    }

    public void enter() {
        if (this.mLayout != null) {
            this.getSceneRoot().removeAllViews();
            this.mSceneRoot.addView(this.mLayout);
        }
        if (this.mEnterAction != null) {
            this.mEnterAction.run();
        }
        setCurrentScene(this.mSceneRoot, this);
    }

    static void setCurrentScene(@Nonnull ViewGroup sceneRoot, @Nullable Scene scene) {
        sceneRoot.setTag(67239937, scene);
    }

    @Nullable
    public static Scene getCurrentScene(@Nonnull ViewGroup sceneRoot) {
        return (Scene) sceneRoot.getTag(67239937);
    }

    public void setEnterAction(@Nullable Runnable action) {
        this.mEnterAction = action;
    }

    public void setExitAction(@Nullable Runnable action) {
        this.mExitAction = action;
    }
}
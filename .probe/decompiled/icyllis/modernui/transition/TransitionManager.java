package icyllis.modernui.transition;

import icyllis.modernui.util.ArrayMap;
import icyllis.modernui.view.View;
import icyllis.modernui.view.ViewGroup;
import icyllis.modernui.view.ViewTreeObserver;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TransitionManager {

    private static final Transition sDefaultTransition = new AutoTransition();

    private final ArrayMap<Scene, Transition> mSceneTransitions = new ArrayMap<>();

    private final ArrayMap<Scene, ArrayMap<Scene, Transition>> mScenePairTransitions = new ArrayMap<>();

    private static final ThreadLocal<WeakReference<ArrayMap<ViewGroup, ArrayList<Transition>>>> sRunningTransitions = new ThreadLocal();

    private static final ArrayList<ViewGroup> sPendingTransitions = new ArrayList();

    public void setTransition(@Nonnull Scene scene, @Nullable Transition transition) {
        this.mSceneTransitions.put(scene, transition);
    }

    public void setTransition(@Nonnull Scene fromScene, @Nonnull Scene toScene, @Nullable Transition transition) {
        ArrayMap<Scene, Transition> sceneTransitionMap = this.mScenePairTransitions.get(toScene);
        if (sceneTransitionMap == null) {
            sceneTransitionMap = new ArrayMap<>();
            this.mScenePairTransitions.put(toScene, sceneTransitionMap);
        }
        sceneTransitionMap.put(fromScene, transition);
    }

    private Transition getTransition(@Nonnull Scene scene) {
        ViewGroup sceneRoot = scene.getSceneRoot();
        Scene currScene = Scene.getCurrentScene(sceneRoot);
        if (currScene != null) {
            ArrayMap<Scene, Transition> sceneTransitionMap = this.mScenePairTransitions.get(scene);
            if (sceneTransitionMap != null) {
                Transition transition = sceneTransitionMap.get(currScene);
                if (transition != null) {
                    return transition;
                }
            }
        }
        Transition transition = this.mSceneTransitions.get(scene);
        return transition != null ? transition : sDefaultTransition;
    }

    private static void changeScene(@Nonnull Scene scene, @Nullable Transition transition) {
        ViewGroup sceneRoot = scene.getSceneRoot();
        if (!sPendingTransitions.contains(sceneRoot)) {
            Scene oldScene = Scene.getCurrentScene(sceneRoot);
            if (transition == null) {
                if (oldScene != null) {
                    oldScene.exit();
                }
                scene.enter();
            } else {
                sPendingTransitions.add(sceneRoot);
                Transition transitionClone = transition.clone();
                sceneChangeSetup(sceneRoot, transitionClone);
                scene.enter();
                sceneChangeRunTransition(sceneRoot, transitionClone);
            }
        }
    }

    @Nonnull
    static ArrayMap<ViewGroup, ArrayList<Transition>> getRunningTransitions() {
        WeakReference<ArrayMap<ViewGroup, ArrayList<Transition>>> runningTransitions = (WeakReference<ArrayMap<ViewGroup, ArrayList<Transition>>>) sRunningTransitions.get();
        if (runningTransitions != null) {
            ArrayMap<ViewGroup, ArrayList<Transition>> transitions = (ArrayMap<ViewGroup, ArrayList<Transition>>) runningTransitions.get();
            if (transitions != null) {
                return transitions;
            }
        }
        ArrayMap<ViewGroup, ArrayList<Transition>> transitions = new ArrayMap<>();
        runningTransitions = new WeakReference(transitions);
        sRunningTransitions.set(runningTransitions);
        return transitions;
    }

    private static void sceneChangeRunTransition(ViewGroup sceneRoot, Transition transition) {
        if (transition != null && sceneRoot != null) {
            TransitionManager.MultiListener listener = new TransitionManager.MultiListener(transition, sceneRoot);
            sceneRoot.addOnAttachStateChangeListener(listener);
            sceneRoot.getViewTreeObserver().addOnPreDrawListener(listener);
        }
    }

    private static void sceneChangeSetup(ViewGroup sceneRoot, Transition transition) {
        ArrayList<Transition> runningTransitions = getRunningTransitions().get(sceneRoot);
        if (runningTransitions != null && runningTransitions.size() > 0) {
            for (Transition runningTransition : runningTransitions) {
                runningTransition.pause(sceneRoot);
            }
        }
        if (transition != null) {
            transition.captureValues(sceneRoot, true);
        }
        Scene previousScene = Scene.getCurrentScene(sceneRoot);
        if (previousScene != null) {
            previousScene.exit();
        }
    }

    public void transitionTo(@Nonnull Scene scene) {
        changeScene(scene, this.getTransition(scene));
    }

    public static void go(@Nonnull Scene scene) {
        changeScene(scene, sDefaultTransition);
    }

    public static void go(@Nonnull Scene scene, @Nullable Transition transition) {
        changeScene(scene, transition);
    }

    public static void beginDelayedTransition(@Nonnull ViewGroup sceneRoot) {
        beginDelayedTransition(sceneRoot, null);
    }

    public static void beginDelayedTransition(@Nonnull ViewGroup sceneRoot, @Nullable Transition transition) {
        if (!sPendingTransitions.contains(sceneRoot) && sceneRoot.isLaidOut()) {
            sPendingTransitions.add(sceneRoot);
            if (transition == null) {
                transition = sDefaultTransition;
            }
            Transition transitionClone = transition.clone();
            sceneChangeSetup(sceneRoot, transitionClone);
            Scene.setCurrentScene(sceneRoot, null);
            sceneChangeRunTransition(sceneRoot, transitionClone);
        }
    }

    public static void endTransitions(ViewGroup sceneRoot) {
        sPendingTransitions.remove(sceneRoot);
        ArrayList<Transition> runningTransitions = getRunningTransitions().get(sceneRoot);
        if (runningTransitions != null && !runningTransitions.isEmpty()) {
            ArrayList<Transition> copy = new ArrayList(runningTransitions);
            for (int i = copy.size() - 1; i >= 0; i--) {
                Transition transition = (Transition) copy.get(i);
                transition.forceToEnd(sceneRoot);
            }
        }
    }

    private static class MultiListener implements ViewTreeObserver.OnPreDrawListener, View.OnAttachStateChangeListener {

        Transition mTransition;

        ViewGroup mSceneRoot;

        MultiListener(Transition transition, ViewGroup sceneRoot) {
            this.mTransition = transition;
            this.mSceneRoot = sceneRoot;
        }

        private void removeListeners() {
            this.mSceneRoot.getViewTreeObserver().removeOnPreDrawListener(this);
            this.mSceneRoot.removeOnAttachStateChangeListener(this);
        }

        @Override
        public void onViewAttachedToWindow(View v) {
        }

        @Override
        public void onViewDetachedFromWindow(View v) {
            this.removeListeners();
            TransitionManager.sPendingTransitions.remove(this.mSceneRoot);
            ArrayList<Transition> runningTransitions = TransitionManager.getRunningTransitions().get(this.mSceneRoot);
            if (runningTransitions != null && runningTransitions.size() > 0) {
                for (Transition runningTransition : runningTransitions) {
                    runningTransition.resume(this.mSceneRoot);
                }
            }
            this.mTransition.clearValues(true);
        }

        @Override
        public boolean onPreDraw() {
            this.removeListeners();
            if (!TransitionManager.sPendingTransitions.remove(this.mSceneRoot)) {
                return true;
            } else {
                final ArrayMap<ViewGroup, ArrayList<Transition>> runningTransitions = TransitionManager.getRunningTransitions();
                ArrayList<Transition> currentTransitions = runningTransitions.get(this.mSceneRoot);
                ArrayList<Transition> previousRunningTransitions = null;
                if (currentTransitions == null) {
                    currentTransitions = new ArrayList();
                    runningTransitions.put(this.mSceneRoot, currentTransitions);
                } else if (currentTransitions.size() > 0) {
                    previousRunningTransitions = new ArrayList(currentTransitions);
                }
                currentTransitions.add(this.mTransition);
                this.mTransition.addListener(new TransitionListener() {

                    @Override
                    public void onTransitionEnd(@Nonnull Transition transition) {
                        ArrayList<Transition> currentTransitions = runningTransitions.get(MultiListener.this.mSceneRoot);
                        currentTransitions.remove(transition);
                        transition.removeListener(this);
                    }
                });
                this.mTransition.captureValues(this.mSceneRoot, false);
                if (previousRunningTransitions != null) {
                    for (Transition runningTransition : previousRunningTransitions) {
                        runningTransition.resume(this.mSceneRoot);
                    }
                }
                this.mTransition.playTransition(this.mSceneRoot);
                return true;
            }
        }
    }
}
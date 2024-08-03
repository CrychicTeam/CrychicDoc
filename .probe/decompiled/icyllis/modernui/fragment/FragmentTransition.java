package icyllis.modernui.fragment;

import icyllis.modernui.core.CancellationSignal;
import icyllis.modernui.graphics.Rect;
import icyllis.modernui.transition.Transition;
import icyllis.modernui.transition.TransitionListener;
import icyllis.modernui.transition.TransitionSet;
import icyllis.modernui.util.ArrayMap;
import icyllis.modernui.view.OneShotPreDrawListener;
import icyllis.modernui.view.View;
import icyllis.modernui.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

final class FragmentTransition {

    private FragmentTransition() {
    }

    @Nullable
    static String findKeyForValue(@Nonnull ArrayMap<String, String> map, @Nonnull String value) {
        int numElements = map.size();
        for (int i = 0; i < numElements; i++) {
            if (value.equals(map.valueAt(i))) {
                return map.keyAt(i);
            }
        }
        return null;
    }

    static void retainValues(@Nonnull ArrayMap<String, String> nameOverrides, @Nonnull ArrayMap<String, View> namedViews) {
        for (int i = nameOverrides.size() - 1; i >= 0; i--) {
            String targetName = nameOverrides.valueAt(i);
            if (!namedViews.containsKey(targetName)) {
                nameOverrides.removeAt(i);
            }
        }
    }

    static void callSharedElementStartEnd(Fragment inFragment, Fragment outFragment, boolean isPop, ArrayMap<String, View> sharedElements, boolean isStart) {
        SharedElementCallback sharedElementCallback = isPop ? outFragment.getEnterTransitionCallback() : inFragment.getEnterTransitionCallback();
        if (sharedElementCallback != null) {
            ArrayList<View> views = new ArrayList();
            ArrayList<String> names = new ArrayList();
            int count = sharedElements == null ? 0 : sharedElements.size();
            for (int i = 0; i < count; i++) {
                names.add(sharedElements.keyAt(i));
                views.add(sharedElements.valueAt(i));
            }
            if (isStart) {
                sharedElementCallback.onSharedElementStart(names, views, null);
            } else {
                sharedElementCallback.onSharedElementEnd(names, views, null);
            }
        }
    }

    static void setViewVisibility(ArrayList<View> views, int visibility) {
        if (views != null) {
            for (int i = views.size() - 1; i >= 0; i--) {
                View view = (View) views.get(i);
                view.setVisibility(visibility);
            }
        }
    }

    @Nonnull
    static ArrayList<String> prepareSetNameOverridesReordered(@Nonnull ArrayList<View> sharedElementsIn) {
        ArrayList<String> names = new ArrayList();
        for (View view : sharedElementsIn) {
            names.add(view.getTransitionName());
            view.setTransitionName(null);
        }
        return names;
    }

    static void setNameOverridesReordered(@Nonnull View sceneRoot, @Nonnull ArrayList<View> sharedElementsOut, @Nonnull ArrayList<View> sharedElementsIn, @Nonnull ArrayList<String> inNames, @Nonnull Map<String, String> nameOverrides) {
        int numSharedElements = sharedElementsIn.size();
        ArrayList<String> outNames = new ArrayList();
        for (int i = 0; i < numSharedElements; i++) {
            View view = (View) sharedElementsOut.get(i);
            String name = view.getTransitionName();
            outNames.add(name);
            if (name != null) {
                view.setTransitionName(null);
                String inName = (String) nameOverrides.get(name);
                for (int j = 0; j < numSharedElements; j++) {
                    if (inName.equals(inNames.get(j))) {
                        ((View) sharedElementsIn.get(j)).setTransitionName(name);
                        break;
                    }
                }
            }
        }
        OneShotPreDrawListener.add(sceneRoot, () -> {
            for (int ix = 0; ix < numSharedElements; ix++) {
                ((View) sharedElementsIn.get(ix)).setTransitionName((String) inNames.get(ix));
                ((View) sharedElementsOut.get(ix)).setTransitionName((String) outNames.get(ix));
            }
        });
    }

    static void setSharedElementTargets(@Nonnull TransitionSet transition, @Nonnull View nonExistentView, @Nonnull ArrayList<View> sharedViews) {
        List<View> views = transition.getTargets();
        views.clear();
        for (View view : sharedViews) {
            bfsAddViewChildren(views, view);
        }
        views.add(nonExistentView);
        sharedViews.add(nonExistentView);
        addTargets(transition, sharedViews);
    }

    static void setEpicenter(@Nonnull Transition transition, View view) {
        if (view != null) {
            Rect epicenter = new Rect();
            view.getBoundsOnScreen(epicenter);
            transition.setEpicenterCallback(t -> epicenter);
        }
    }

    static void setEpicenter(Transition transition, Rect epicenter) {
        if (transition != null) {
            transition.setEpicenterCallback(t -> epicenter != null && !epicenter.isEmpty() ? epicenter : null);
        }
    }

    static void addTargets(@Nullable Transition transition, @Nonnull ArrayList<View> views) {
        if (transition != null) {
            if (transition instanceof TransitionSet set) {
                int numTransitions = set.getTransitionCount();
                for (int i = 0; i < numTransitions; i++) {
                    Transition child = set.getTransitionAt(i);
                    addTargets(child, views);
                }
            } else if (!hasSimpleTarget(transition)) {
                List<View> targets = transition.getTargets();
                if (isNullOrEmpty(targets)) {
                    for (View view : views) {
                        transition.addTarget(view);
                    }
                }
            }
        }
    }

    @Nonnull
    static TransitionSet mergeTransitionsTogether(@Nullable Transition transition1, @Nullable Transition transition2, @Nullable Transition transition3) {
        TransitionSet transitionSet = new TransitionSet();
        if (transition1 != null) {
            transitionSet.addTransition(transition1);
        }
        if (transition2 != null) {
            transitionSet.addTransition(transition2);
        }
        if (transition3 != null) {
            transitionSet.addTransition(transition3);
        }
        return transitionSet;
    }

    static void scheduleHideFragmentView(@Nonnull Transition exitTransition, View fragmentView, ArrayList<View> exitingViews) {
        exitTransition.addListener(new TransitionListener() {

            @Override
            public void onTransitionStart(@Nonnull Transition transition) {
                transition.removeListener(this);
                transition.addListener(this);
            }

            @Override
            public void onTransitionEnd(@Nonnull Transition transition) {
                transition.removeListener(this);
                fragmentView.setVisibility(8);
                for (View exitingView : exitingViews) {
                    exitingView.setVisibility(0);
                }
            }
        });
    }

    static Transition mergeTransitionsInSequence(Transition exitTransition, Transition enterTransition, Transition sharedElementTransition) {
        Transition staggered = null;
        if (exitTransition != null && enterTransition != null) {
            staggered = new TransitionSet().addTransition(exitTransition).addTransition(enterTransition).setOrdering(1);
        } else if (exitTransition != null) {
            staggered = exitTransition;
        } else if (enterTransition != null) {
            staggered = enterTransition;
        }
        if (sharedElementTransition != null) {
            TransitionSet together = new TransitionSet();
            if (staggered != null) {
                together.addTransition(staggered);
            }
            together.addTransition(sharedElementTransition);
            return together;
        } else {
            return staggered;
        }
    }

    static void scheduleRemoveTargets(@Nonnull Transition overallTransition, @Nullable Transition enterTransition, ArrayList<View> enteringViews, @Nullable Transition exitTransition, ArrayList<View> exitingViews, @Nullable Transition sharedElementTransition, ArrayList<View> sharedElementsIn) {
        overallTransition.addListener(new TransitionListener() {

            @Override
            public void onTransitionStart(@Nonnull Transition transition) {
                if (enterTransition != null) {
                    FragmentTransition.replaceTargets(enterTransition, enteringViews, null);
                }
                if (exitTransition != null) {
                    FragmentTransition.replaceTargets(exitTransition, exitingViews, null);
                }
                if (sharedElementTransition != null) {
                    FragmentTransition.replaceTargets(sharedElementTransition, sharedElementsIn, null);
                }
            }

            @Override
            public void onTransitionEnd(@Nonnull Transition transition) {
                transition.removeListener(this);
            }
        });
    }

    static void setListenerForTransitionEnd(@Nonnull Transition transition, @Nonnull CancellationSignal signal, @Nonnull Runnable transitionCompleteRunnable) {
        signal.setOnCancelListener(transition::cancel);
        transition.addListener(new TransitionListener() {

            @Override
            public void onTransitionEnd(@Nonnull Transition transition) {
                transitionCompleteRunnable.run();
            }
        });
    }

    static void swapSharedElementTargets(TransitionSet sharedElementTransition, ArrayList<View> sharedElementsOut, ArrayList<View> sharedElementsIn) {
        if (sharedElementTransition != null) {
            sharedElementTransition.getTargets().clear();
            sharedElementTransition.getTargets().addAll(sharedElementsIn);
            replaceTargets(sharedElementTransition, sharedElementsOut, sharedElementsIn);
        }
    }

    static void replaceTargets(Transition transition, ArrayList<View> oldTargets, ArrayList<View> newTargets) {
        if (transition instanceof TransitionSet set) {
            int numTransitions = set.getTransitionCount();
            for (int i = 0; i < numTransitions; i++) {
                Transition child = set.getTransitionAt(i);
                replaceTargets(child, oldTargets, newTargets);
            }
        } else if (!hasSimpleTarget(transition)) {
            List<View> targets = transition.getTargets();
            if (targets.size() == oldTargets.size() && targets.containsAll(oldTargets)) {
                int targetCount = newTargets == null ? 0 : newTargets.size();
                for (int i = 0; i < targetCount; i++) {
                    transition.addTarget((View) newTargets.get(i));
                }
                for (int i = oldTargets.size() - 1; i >= 0; i--) {
                    transition.removeTarget((View) oldTargets.get(i));
                }
            }
        }
    }

    static void bfsAddViewChildren(@Nonnull List<View> views, @Nonnull View startView) {
        int startIndex = views.size();
        if (!containedBeforeIndex(views, startView, startIndex)) {
            if (startView.getTransitionName() != null) {
                views.add(startView);
            }
            for (int index = startIndex; index < views.size(); index++) {
                View view = (View) views.get(index);
                if (view instanceof ViewGroup) {
                    ViewGroup viewGroup = (ViewGroup) view;
                    int childCount = viewGroup.getChildCount();
                    for (int childIndex = 0; childIndex < childCount; childIndex++) {
                        View child = viewGroup.getChildAt(childIndex);
                        if (!containedBeforeIndex(views, child, startIndex) && child.getTransitionName() != null) {
                            views.add(child);
                        }
                    }
                }
            }
        }
    }

    private static boolean containedBeforeIndex(List<View> views, View view, int maxIndex) {
        for (int i = 0; i < maxIndex; i++) {
            if (views.get(i) == view) {
                return true;
            }
        }
        return false;
    }

    private static boolean hasSimpleTarget(@Nonnull Transition transition) {
        return !isNullOrEmpty(transition.getTargetIds()) || !isNullOrEmpty(transition.getTargetNames()) || !isNullOrEmpty(transition.getTargetTypes());
    }

    private static boolean isNullOrEmpty(List<?> list) {
        return list == null || list.isEmpty();
    }
}
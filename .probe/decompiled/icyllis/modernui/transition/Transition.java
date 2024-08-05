package icyllis.modernui.transition;

import icyllis.modernui.animation.Animator;
import icyllis.modernui.animation.AnimatorListener;
import icyllis.modernui.animation.TimeInterpolator;
import icyllis.modernui.graphics.Rect;
import icyllis.modernui.util.ArrayMap;
import icyllis.modernui.util.LongSparseArray;
import icyllis.modernui.util.SparseArray;
import icyllis.modernui.view.View;
import icyllis.modernui.view.ViewGroup;
import icyllis.modernui.widget.ListView;
import it.unimi.dsi.fastutil.ints.Int2LongArrayMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntListIterator;
import it.unimi.dsi.fastutil.ints.Int2LongMap.Entry;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class Transition implements Cloneable {

    public static final int MATCH_INSTANCE = 1;

    private static final int MATCH_FIRST = 1;

    public static final int MATCH_NAME = 2;

    public static final int MATCH_ID = 3;

    public static final int MATCH_ITEM_ID = 4;

    private static final int MATCH_LAST = 4;

    private static final int[] DEFAULT_MATCH_ORDER = new int[] { 2, 1, 3, 4 };

    private long mStartDelay = -1L;

    long mDuration = -1L;

    private TimeInterpolator mInterpolator;

    IntArrayList mTargetIds = new IntArrayList();

    ArrayList<View> mTargets = new ArrayList();

    private ArrayList<String> mTargetNames;

    private ArrayList<Class<?>> mTargetTypes;

    private IntArrayList mTargetIdExcludes;

    private ArrayList<View> mTargetExcludes;

    private ArrayList<Class<?>> mTargetTypeExcludes;

    private ArrayList<String> mTargetNameExcludes;

    private IntArrayList mTargetIdChildExcludes;

    private ArrayList<View> mTargetChildExcludes;

    private ArrayList<Class<?>> mTargetTypeChildExcludes;

    private Transition.TransitionValuesMaps mStartValues = new Transition.TransitionValuesMaps();

    private Transition.TransitionValuesMaps mEndValues = new Transition.TransitionValuesMaps();

    TransitionSet mParent;

    private int[] mMatchOrder = DEFAULT_MATCH_ORDER;

    private ArrayList<TransitionValues> mStartValuesList;

    private ArrayList<TransitionValues> mEndValuesList;

    private static final ThreadLocal<ArrayMap<Animator, Transition.AnimationInfo>> sRunningAnimators = ThreadLocal.withInitial(ArrayMap::new);

    boolean mCanRemoveViews = false;

    private final ArrayList<Animator> mCurrentAnimators = new ArrayList();

    private int mNumInstances = 0;

    private boolean mPaused = false;

    private boolean mEnded = false;

    private CopyOnWriteArrayList<TransitionListener> mListeners;

    private ArrayList<Animator> mAnimators = new ArrayList();

    TransitionPropagation mPropagation;

    private Transition.EpicenterCallback mEpicenterCallback;

    @Nonnull
    public Transition setDuration(long duration) {
        this.mDuration = duration;
        return this;
    }

    public long getDuration() {
        return this.mDuration;
    }

    @Nonnull
    public Transition setStartDelay(long startDelay) {
        this.mStartDelay = startDelay;
        return this;
    }

    public long getStartDelay() {
        return this.mStartDelay;
    }

    @Nonnull
    public Transition setInterpolator(@Nullable TimeInterpolator interpolator) {
        this.mInterpolator = interpolator;
        return this;
    }

    @Nullable
    public TimeInterpolator getInterpolator() {
        return this.mInterpolator;
    }

    @Nullable
    public String[] getTransitionProperties() {
        return null;
    }

    @Nullable
    public Animator createAnimator(@Nonnull ViewGroup sceneRoot, @Nullable TransitionValues startValues, @Nullable TransitionValues endValues) {
        return null;
    }

    public void setMatchOrder(int... matches) {
        if (matches != null && matches.length != 0) {
            for (int i = 0; i < matches.length; i++) {
                int match = matches[i];
                if (!isValidMatch(match)) {
                    throw new IllegalArgumentException("matches contains invalid value");
                }
                if (alreadyContains(matches, i)) {
                    throw new IllegalArgumentException("matches contains a duplicate value");
                }
            }
            this.mMatchOrder = (int[]) matches.clone();
        } else {
            this.mMatchOrder = DEFAULT_MATCH_ORDER;
        }
    }

    private static boolean isValidMatch(int match) {
        return match >= 1 && match <= 4;
    }

    private static boolean alreadyContains(@Nonnull int[] array, int searchIndex) {
        int value = array[searchIndex];
        for (int i = 0; i < searchIndex; i++) {
            if (array[i] == value) {
                return true;
            }
        }
        return false;
    }

    private void matchInstances(@Nonnull ArrayMap<View, TransitionValues> unmatchedStart, @Nonnull ArrayMap<View, TransitionValues> unmatchedEnd) {
        for (int i = unmatchedStart.size() - 1; i >= 0; i--) {
            View view = unmatchedStart.keyAt(i);
            if (view != null && this.isValidTarget(view)) {
                TransitionValues end = unmatchedEnd.remove(view);
                if (end != null && this.isValidTarget(end.view)) {
                    TransitionValues start = unmatchedStart.removeAt(i);
                    this.mStartValuesList.add(start);
                    this.mEndValuesList.add(end);
                }
            }
        }
    }

    private void matchItemIds(@Nonnull ArrayMap<View, TransitionValues> unmatchedStart, @Nonnull ArrayMap<View, TransitionValues> unmatchedEnd, @Nonnull LongSparseArray<View> startItemIds, @Nonnull LongSparseArray<View> endItemIds) {
        int numStartIds = startItemIds.size();
        for (int i = 0; i < numStartIds; i++) {
            View startView = startItemIds.valueAt(i);
            if (startView != null && this.isValidTarget(startView)) {
                View endView = endItemIds.get(startItemIds.keyAt(i));
                if (endView != null && this.isValidTarget(endView)) {
                    TransitionValues startValues = unmatchedStart.get(startView);
                    TransitionValues endValues = unmatchedEnd.get(endView);
                    if (startValues != null && endValues != null) {
                        this.mStartValuesList.add(startValues);
                        this.mEndValuesList.add(endValues);
                        unmatchedStart.remove(startView);
                        unmatchedEnd.remove(endView);
                    }
                }
            }
        }
    }

    private void matchIds(@Nonnull ArrayMap<View, TransitionValues> unmatchedStart, @Nonnull ArrayMap<View, TransitionValues> unmatchedEnd, @Nonnull SparseArray<View> startIds, @Nonnull SparseArray<View> endIds) {
        int numStartIds = startIds.size();
        for (int i = 0; i < numStartIds; i++) {
            View startView = startIds.valueAt(i);
            if (startView != null && this.isValidTarget(startView)) {
                View endView = endIds.get(startIds.keyAt(i));
                if (endView != null && this.isValidTarget(endView)) {
                    TransitionValues startValues = unmatchedStart.get(startView);
                    TransitionValues endValues = unmatchedEnd.get(endView);
                    if (startValues != null && endValues != null) {
                        this.mStartValuesList.add(startValues);
                        this.mEndValuesList.add(endValues);
                        unmatchedStart.remove(startView);
                        unmatchedEnd.remove(endView);
                    }
                }
            }
        }
    }

    private void matchNames(@Nonnull ArrayMap<View, TransitionValues> unmatchedStart, @Nonnull ArrayMap<View, TransitionValues> unmatchedEnd, @Nonnull ArrayMap<String, View> startNames, @Nonnull ArrayMap<String, View> endNames) {
        int numStartNames = startNames.size();
        for (int i = 0; i < numStartNames; i++) {
            View startView = startNames.valueAt(i);
            if (startView != null && this.isValidTarget(startView)) {
                View endView = endNames.get(startNames.keyAt(i));
                if (endView != null && this.isValidTarget(endView)) {
                    TransitionValues startValues = unmatchedStart.get(startView);
                    TransitionValues endValues = unmatchedEnd.get(endView);
                    if (startValues != null && endValues != null) {
                        this.mStartValuesList.add(startValues);
                        this.mEndValuesList.add(endValues);
                        unmatchedStart.remove(startView);
                        unmatchedEnd.remove(endView);
                    }
                }
            }
        }
    }

    private void addUnmatched(@Nonnull ArrayMap<View, TransitionValues> unmatchedStart, @Nonnull ArrayMap<View, TransitionValues> unmatchedEnd) {
        for (int i = 0; i < unmatchedStart.size(); i++) {
            TransitionValues start = unmatchedStart.valueAt(i);
            if (this.isValidTarget(start.view)) {
                this.mStartValuesList.add(start);
                this.mEndValuesList.add(null);
            }
        }
        for (int ix = 0; ix < unmatchedEnd.size(); ix++) {
            TransitionValues end = unmatchedEnd.valueAt(ix);
            if (this.isValidTarget(end.view)) {
                this.mEndValuesList.add(end);
                this.mStartValuesList.add(null);
            }
        }
    }

    private void matchStartAndEnd(@Nonnull Transition.TransitionValuesMaps startValues, @Nonnull Transition.TransitionValuesMaps endValues) {
        ArrayMap<View, TransitionValues> unmatchedStart = new ArrayMap<>(startValues.mViewValues);
        ArrayMap<View, TransitionValues> unmatchedEnd = new ArrayMap<>(endValues.mViewValues);
        for (int j : this.mMatchOrder) {
            switch(j) {
                case 1:
                    this.matchInstances(unmatchedStart, unmatchedEnd);
                    break;
                case 2:
                    this.matchNames(unmatchedStart, unmatchedEnd, startValues.mNameValues, endValues.mNameValues);
                    break;
                case 3:
                    this.matchIds(unmatchedStart, unmatchedEnd, startValues.mIdValues, endValues.mIdValues);
                    break;
                case 4:
                    this.matchItemIds(unmatchedStart, unmatchedEnd, startValues.mItemIdValues, endValues.mItemIdValues);
            }
        }
        this.addUnmatched(unmatchedStart, unmatchedEnd);
    }

    protected void createAnimators(@Nonnull ViewGroup sceneRoot, @Nonnull Transition.TransitionValuesMaps startValues, @Nonnull Transition.TransitionValuesMaps endValues, @Nonnull ArrayList<TransitionValues> startValuesList, @Nonnull ArrayList<TransitionValues> endValuesList) {
        ArrayMap<Animator, Transition.AnimationInfo> runningAnimators = (ArrayMap<Animator, Transition.AnimationInfo>) sRunningAnimators.get();
        long minStartDelay = Long.MAX_VALUE;
        Int2LongArrayMap startDelays = new Int2LongArrayMap();
        int startValuesListCount = startValuesList.size();
        for (int i = 0; i < startValuesListCount; i++) {
            TransitionValues start = (TransitionValues) startValuesList.get(i);
            TransitionValues end = (TransitionValues) endValuesList.get(i);
            if (start != null && !start.mTargetedTransitions.contains(this)) {
                start = null;
            }
            if (end != null && !end.mTargetedTransitions.contains(this)) {
                end = null;
            }
            if (start != null || end != null) {
                boolean isChanged = start == null || end == null || this.isTransitionRequired(start, end);
                if (isChanged) {
                    Animator animator = this.createAnimator(sceneRoot, start, end);
                    if (animator != null) {
                        TransitionValues infoValues = null;
                        View view;
                        if (end != null) {
                            view = end.view;
                            String[] properties = this.getTransitionProperties();
                            if (properties != null && properties.length > 0) {
                                infoValues = new TransitionValues(view);
                                TransitionValues newValues = endValues.mViewValues.get(view);
                                if (newValues != null) {
                                    for (String property : properties) {
                                        infoValues.values.put(property, newValues.values.get(property));
                                    }
                                }
                                int numExistingAnimators = runningAnimators.size();
                                for (int j = 0; j < numExistingAnimators; j++) {
                                    Animator anim = runningAnimators.keyAt(j);
                                    Transition.AnimationInfo info = runningAnimators.get(anim);
                                    if (info.values != null && info.view == view && info.name.equals(this.getName()) && info.values.equals(infoValues)) {
                                        animator = null;
                                        break;
                                    }
                                }
                            }
                        } else {
                            view = start.view;
                        }
                        if (animator != null) {
                            if (this.mPropagation != null) {
                                long delay = this.mPropagation.getStartDelay(sceneRoot, this, start, end);
                                startDelays.put(this.mAnimators.size(), (long) ((int) delay));
                                minStartDelay = Math.min(delay, minStartDelay);
                            }
                            Transition.AnimationInfo info = new Transition.AnimationInfo(view, this.getName(), this, infoValues);
                            runningAnimators.put(animator, info);
                            this.mAnimators.add(animator);
                        }
                    }
                }
            }
        }
        if (startDelays.size() > 0) {
            ObjectIterator<Entry> it = startDelays.int2LongEntrySet().fastIterator();
            while (it.hasNext()) {
                Entry e = (Entry) it.next();
                int index = e.getIntKey();
                Animator animator = (Animator) this.mAnimators.get(index);
                long delay = e.getLongValue() - minStartDelay + animator.getStartDelay();
                animator.setStartDelay(delay);
            }
        }
    }

    boolean isValidTarget(@Nonnull View target) {
        int targetId = target.getId();
        if (this.mTargetIdExcludes != null && this.mTargetIdExcludes.contains(targetId)) {
            return false;
        } else if (this.mTargetExcludes != null && this.mTargetExcludes.contains(target)) {
            return false;
        } else {
            if (this.mTargetTypeExcludes != null) {
                for (Class<?> type : this.mTargetTypeExcludes) {
                    if (type.isInstance(target)) {
                        return false;
                    }
                }
            }
            if (this.mTargetNameExcludes != null && target.getTransitionName() != null && this.mTargetNameExcludes.contains(target.getTransitionName())) {
                return false;
            } else if (this.mTargetIds.size() != 0 || this.mTargets.size() != 0 || this.mTargetTypes != null && !this.mTargetTypes.isEmpty() || this.mTargetNames != null && !this.mTargetNames.isEmpty()) {
                if (this.mTargetIds.contains(targetId) || this.mTargets.contains(target)) {
                    return true;
                } else if (this.mTargetNames != null && this.mTargetNames.contains(target.getTransitionName())) {
                    return true;
                } else {
                    if (this.mTargetTypes != null) {
                        for (Class<?> targetType : this.mTargetTypes) {
                            if (targetType.isInstance(target)) {
                                return true;
                            }
                        }
                    }
                    return false;
                }
            } else {
                return true;
            }
        }
    }

    protected void runAnimators() {
        this.start();
        ArrayMap<Animator, Transition.AnimationInfo> runningAnimators = (ArrayMap<Animator, Transition.AnimationInfo>) sRunningAnimators.get();
        for (Animator anim : this.mAnimators) {
            if (runningAnimators.containsKey(anim)) {
                this.start();
                this.runAnimator(anim, runningAnimators);
            }
        }
        this.mAnimators.clear();
        this.end();
    }

    private void runAnimator(@Nullable Animator animator, ArrayMap<Animator, Transition.AnimationInfo> runningAnimators) {
        if (animator != null) {
            animator.addListener(new AnimatorListener() {

                @Override
                public void onAnimationStart(@Nonnull Animator animation) {
                    Transition.this.mCurrentAnimators.add(animation);
                }

                @Override
                public void onAnimationEnd(@Nonnull Animator animation) {
                    runningAnimators.remove(animation);
                    Transition.this.mCurrentAnimators.remove(animation);
                }
            });
            this.animate(animator);
        }
    }

    public abstract void captureStartValues(@Nonnull TransitionValues var1);

    public abstract void captureEndValues(@Nonnull TransitionValues var1);

    @Nonnull
    public Transition addTarget(@Nonnull View target) {
        this.mTargets.add(target);
        return this;
    }

    @Nonnull
    public Transition addTarget(int targetId) {
        if (targetId > 0) {
            this.mTargetIds.add(targetId);
        }
        return this;
    }

    @Nonnull
    public Transition addTarget(@Nonnull String targetName) {
        if (this.mTargetNames == null) {
            this.mTargetNames = new ArrayList();
        }
        this.mTargetNames.add(targetName);
        return this;
    }

    @Nonnull
    public Transition addTarget(@Nonnull Class<?> targetType) {
        if (this.mTargetTypes == null) {
            this.mTargetTypes = new ArrayList();
        }
        this.mTargetTypes.add(targetType);
        return this;
    }

    @Nonnull
    public Transition removeTarget(@Nonnull View target) {
        this.mTargets.remove(target);
        return this;
    }

    @Nonnull
    public Transition removeTarget(int targetId) {
        if (targetId > 0) {
            this.mTargetIds.rem(targetId);
        }
        return this;
    }

    @Nonnull
    public Transition removeTarget(@Nonnull String targetName) {
        if (this.mTargetNames != null) {
            this.mTargetNames.remove(targetName);
        }
        return this;
    }

    @Nonnull
    public Transition removeTarget(@Nonnull Class<?> target) {
        if (this.mTargetTypes != null) {
            this.mTargetTypes.remove(target);
        }
        return this;
    }

    @Nullable
    private static <T> ArrayList<T> excludeObject(@Nullable ArrayList<T> list, T target, boolean exclude) {
        if (target != null) {
            if (exclude) {
                list = Transition.ArrayListManager.add(list, target);
            } else {
                list = Transition.ArrayListManager.remove(list, target);
            }
        }
        return list;
    }

    @Nonnull
    public Transition excludeTarget(@Nonnull View target, boolean exclude) {
        this.mTargetExcludes = this.excludeView(this.mTargetExcludes, target, exclude);
        return this;
    }

    @Nonnull
    public Transition excludeTarget(int targetId, boolean exclude) {
        this.mTargetIdExcludes = this.excludeId(this.mTargetIdExcludes, targetId, exclude);
        return this;
    }

    @Nonnull
    public Transition excludeTarget(@Nonnull String targetName, boolean exclude) {
        this.mTargetNameExcludes = excludeObject(this.mTargetNameExcludes, targetName, exclude);
        return this;
    }

    @Nonnull
    public Transition excludeChildren(@Nonnull View target, boolean exclude) {
        this.mTargetChildExcludes = this.excludeView(this.mTargetChildExcludes, target, exclude);
        return this;
    }

    @Nonnull
    public Transition excludeChildren(int targetId, boolean exclude) {
        this.mTargetIdChildExcludes = this.excludeId(this.mTargetIdChildExcludes, targetId, exclude);
        return this;
    }

    @Nullable
    private IntArrayList excludeId(@Nullable IntArrayList list, int targetId, boolean exclude) {
        if (targetId > 0) {
            if (exclude) {
                list = Transition.ArrayListManager.add(list, targetId);
            } else {
                list = Transition.ArrayListManager.remove(list, targetId);
            }
        }
        return list;
    }

    @Nullable
    private ArrayList<View> excludeView(@Nullable ArrayList<View> list, View target, boolean exclude) {
        if (target != null) {
            if (exclude) {
                list = Transition.ArrayListManager.add(list, target);
            } else {
                list = Transition.ArrayListManager.remove(list, target);
            }
        }
        return list;
    }

    @Nonnull
    public Transition excludeTarget(@Nonnull Class<?> type, boolean exclude) {
        this.mTargetTypeExcludes = this.excludeType(this.mTargetTypeExcludes, type, exclude);
        return this;
    }

    @Nonnull
    public Transition excludeChildren(@Nonnull Class<?> type, boolean exclude) {
        this.mTargetTypeChildExcludes = this.excludeType(this.mTargetTypeChildExcludes, type, exclude);
        return this;
    }

    @Nullable
    private ArrayList<Class<?>> excludeType(@Nullable ArrayList<Class<?>> list, Class<?> type, boolean exclude) {
        if (type != null) {
            if (exclude) {
                list = Transition.ArrayListManager.add(list, type);
            } else {
                list = Transition.ArrayListManager.remove(list, type);
            }
        }
        return list;
    }

    @Nonnull
    public IntList getTargetIds() {
        return this.mTargetIds;
    }

    @Nonnull
    public List<View> getTargets() {
        return this.mTargets;
    }

    @Nullable
    public List<String> getTargetNames() {
        return this.mTargetNames;
    }

    @Nullable
    public List<Class<?>> getTargetTypes() {
        return this.mTargetTypes;
    }

    void captureValues(ViewGroup sceneRoot, boolean start) {
        this.clearValues(start);
        if ((this.mTargetIds.size() > 0 || this.mTargets.size() > 0) && (this.mTargetNames == null || this.mTargetNames.isEmpty()) && (this.mTargetTypes == null || this.mTargetTypes.isEmpty())) {
            IntListIterator var3 = this.mTargetIds.iterator();
            while (var3.hasNext()) {
                int id = (Integer) var3.next();
                View view = sceneRoot.findViewById(id);
                if (view != null) {
                    TransitionValues values = new TransitionValues(view);
                    if (start) {
                        this.captureStartValues(values);
                    } else {
                        this.captureEndValues(values);
                    }
                    values.mTargetedTransitions.add(this);
                    this.capturePropagationValues(values);
                    if (start) {
                        addViewValues(this.mStartValues, view, values);
                    } else {
                        addViewValues(this.mEndValues, view, values);
                    }
                }
            }
            for (View view : this.mTargets) {
                TransitionValues valuesx = new TransitionValues(view);
                if (start) {
                    this.captureStartValues(valuesx);
                } else {
                    this.captureEndValues(valuesx);
                }
                valuesx.mTargetedTransitions.add(this);
                this.capturePropagationValues(valuesx);
                if (start) {
                    addViewValues(this.mStartValues, view, valuesx);
                } else {
                    addViewValues(this.mEndValues, view, valuesx);
                }
            }
        } else {
            this.captureHierarchy(sceneRoot, start);
        }
    }

    private static void addViewValues(@Nonnull Transition.TransitionValuesMaps transitionValuesMaps, @Nonnull View view, TransitionValues transitionValues) {
        transitionValuesMaps.mViewValues.put(view, transitionValues);
        int id = view.getId();
        if (id >= 0) {
            if (transitionValuesMaps.mIdValues.indexOfKey(id) >= 0) {
                transitionValuesMaps.mIdValues.put(id, null);
            } else {
                transitionValuesMaps.mIdValues.put(id, view);
            }
        }
        String name = view.getTransitionName();
        if (name != null) {
            if (transitionValuesMaps.mNameValues.containsKey(name)) {
                transitionValuesMaps.mNameValues.put(name, null);
            } else {
                transitionValuesMaps.mNameValues.put(name, view);
            }
        }
        if (view.getParent() instanceof ListView listview && listview.getAdapter().hasStableIds()) {
            int position = listview.getPositionForView(view);
            long itemId = listview.getItemIdAtPosition(position);
            if (transitionValuesMaps.mItemIdValues.indexOfKey(itemId) >= 0) {
                View alreadyMatched = transitionValuesMaps.mItemIdValues.get(itemId);
                if (alreadyMatched != null) {
                    alreadyMatched.setHasTransientState(false);
                    transitionValuesMaps.mItemIdValues.put(itemId, null);
                }
            } else {
                view.setHasTransientState(true);
                transitionValuesMaps.mItemIdValues.put(itemId, view);
            }
        }
    }

    void clearValues(boolean start) {
        if (start) {
            this.mStartValues.mViewValues.clear();
            this.mStartValues.mIdValues.clear();
            this.mStartValues.mItemIdValues.clear();
            this.mStartValues.mNameValues.clear();
            this.mStartValuesList = null;
        } else {
            this.mEndValues.mViewValues.clear();
            this.mEndValues.mIdValues.clear();
            this.mEndValues.mItemIdValues.clear();
            this.mEndValues.mNameValues.clear();
            this.mEndValuesList = null;
        }
    }

    private void captureHierarchy(View view, boolean start) {
        if (view != null) {
            int id = view.getId();
            if (this.mTargetIdExcludes == null || !this.mTargetIdExcludes.contains(id)) {
                if (this.mTargetExcludes == null || !this.mTargetExcludes.contains(view)) {
                    if (this.mTargetTypeExcludes != null) {
                        for (Class<?> type : this.mTargetTypeExcludes) {
                            if (type.isInstance(view)) {
                                return;
                            }
                        }
                    }
                    if (view.getParent() instanceof ViewGroup) {
                        TransitionValues values = new TransitionValues(view);
                        if (start) {
                            this.captureStartValues(values);
                        } else {
                            this.captureEndValues(values);
                        }
                        values.mTargetedTransitions.add(this);
                        this.capturePropagationValues(values);
                        if (start) {
                            addViewValues(this.mStartValues, view, values);
                        } else {
                            addViewValues(this.mEndValues, view, values);
                        }
                    }
                    if (view instanceof ViewGroup parent) {
                        if (this.mTargetIdChildExcludes != null && this.mTargetIdChildExcludes.contains(id)) {
                            return;
                        }
                        if (this.mTargetChildExcludes != null && this.mTargetChildExcludes.contains(view)) {
                            return;
                        }
                        if (this.mTargetTypeChildExcludes != null) {
                            for (Class<?> targetTypeChildExclude : this.mTargetTypeChildExcludes) {
                                if (targetTypeChildExclude.isInstance(view)) {
                                    return;
                                }
                            }
                        }
                        for (int i = 0; i < parent.getChildCount(); i++) {
                            this.captureHierarchy(parent.getChildAt(i), start);
                        }
                    }
                }
            }
        }
    }

    @Nullable
    public TransitionValues getTransitionValues(@Nonnull View view, boolean start) {
        if (this.mParent != null) {
            return this.mParent.getTransitionValues(view, start);
        } else {
            Transition.TransitionValuesMaps valuesMaps = start ? this.mStartValues : this.mEndValues;
            return valuesMaps.mViewValues.get(view);
        }
    }

    @Nullable
    TransitionValues getMatchedTransitionValues(@Nonnull View view, boolean viewInStart) {
        if (this.mParent != null) {
            return this.mParent.getMatchedTransitionValues(view, viewInStart);
        } else {
            ArrayList<TransitionValues> lookIn = viewInStart ? this.mStartValuesList : this.mEndValuesList;
            if (lookIn == null) {
                return null;
            } else {
                int count = lookIn.size();
                int index = -1;
                for (int i = 0; i < count; i++) {
                    TransitionValues values = (TransitionValues) lookIn.get(i);
                    if (values == null) {
                        return null;
                    }
                    if (values.view == view) {
                        index = i;
                        break;
                    }
                }
                TransitionValues valuesx = null;
                if (index >= 0) {
                    ArrayList<TransitionValues> matchIn = viewInStart ? this.mEndValuesList : this.mStartValuesList;
                    valuesx = (TransitionValues) matchIn.get(index);
                }
                return valuesx;
            }
        }
    }

    public void pause(View sceneRoot) {
        if (!this.mEnded) {
            int numAnimators = this.mCurrentAnimators.size();
            for (int i = numAnimators - 1; i >= 0; i--) {
                Animator animator = (Animator) this.mCurrentAnimators.get(i);
                animator.pause();
            }
            if (this.mListeners != null && this.mListeners.size() > 0) {
                for (TransitionListener listener : this.mListeners) {
                    listener.onTransitionPause(this);
                }
            }
            this.mPaused = true;
        }
    }

    protected void resume(@Nonnull View sceneRoot) {
        if (this.mPaused) {
            if (!this.mEnded) {
                for (int i = this.mCurrentAnimators.size() - 1; i >= 0; i--) {
                    ((Animator) this.mCurrentAnimators.get(i)).resume();
                }
                if (this.mListeners != null && this.mListeners.size() > 0) {
                    for (TransitionListener listener : this.mListeners) {
                        listener.onTransitionResume(this);
                    }
                }
            }
            this.mPaused = false;
        }
    }

    void playTransition(@Nonnull ViewGroup sceneRoot) {
        this.mStartValuesList = new ArrayList();
        this.mEndValuesList = new ArrayList();
        this.matchStartAndEnd(this.mStartValues, this.mEndValues);
        ArrayMap<Animator, Transition.AnimationInfo> runningAnimators = (ArrayMap<Animator, Transition.AnimationInfo>) sRunningAnimators.get();
        for (int i = runningAnimators.size() - 1; i >= 0; i--) {
            Animator anim = runningAnimators.keyAt(i);
            if (anim != null) {
                Transition.AnimationInfo oldInfo = runningAnimators.get(anim);
                if (oldInfo != null && oldInfo.view != null) {
                    TransitionValues oldValues = oldInfo.values;
                    View oldView = oldInfo.view;
                    TransitionValues startValues = this.getTransitionValues(oldView, true);
                    TransitionValues endValues = this.getMatchedTransitionValues(oldView, true);
                    if (startValues == null && endValues == null) {
                        endValues = this.mEndValues.mViewValues.get(oldView);
                    }
                    boolean cancel = (startValues != null || endValues != null) && oldInfo.transition.isTransitionRequired(oldValues, endValues);
                    if (cancel) {
                        if (!anim.isRunning() && !anim.isStarted()) {
                            runningAnimators.remove(anim);
                        } else {
                            anim.cancel();
                        }
                    }
                }
            }
        }
        this.createAnimators(sceneRoot, this.mStartValues, this.mEndValues, this.mStartValuesList, this.mEndValuesList);
        this.runAnimators();
    }

    public boolean isTransitionRequired(@Nullable TransitionValues startValues, @Nullable TransitionValues endValues) {
        boolean valuesChanged = false;
        if (startValues != null && endValues != null) {
            String[] properties = this.getTransitionProperties();
            if (properties != null) {
                for (String property : properties) {
                    if (isValueChanged(startValues, endValues, property)) {
                        valuesChanged = true;
                        break;
                    }
                }
            } else {
                for (String key : startValues.values.keySet()) {
                    if (isValueChanged(startValues, endValues, key)) {
                        valuesChanged = true;
                        break;
                    }
                }
            }
        }
        return valuesChanged;
    }

    private static boolean isValueChanged(@Nonnull TransitionValues oldValues, @Nonnull TransitionValues newValues, @Nonnull String key) {
        if (oldValues.values.containsKey(key) != newValues.values.containsKey(key)) {
            return false;
        } else {
            Object oldValue = oldValues.values.get(key);
            Object newValue = newValues.values.get(key);
            boolean changed;
            if (oldValue == null && newValue == null) {
                changed = false;
            } else if (oldValue != null && newValue != null) {
                changed = !oldValue.equals(newValue);
            } else {
                changed = true;
            }
            return changed;
        }
    }

    protected void animate(@Nullable Animator animator) {
        if (animator == null) {
            this.end();
        } else {
            if (this.getDuration() >= 0L) {
                animator.setDuration(this.getDuration());
            }
            if (this.getStartDelay() >= 0L) {
                animator.setStartDelay(this.getStartDelay() + animator.getStartDelay());
            }
            if (this.getInterpolator() != null) {
                animator.setInterpolator(this.getInterpolator());
            }
            animator.addListener(new AnimatorListener() {

                @Override
                public void onAnimationEnd(@Nonnull Animator animation) {
                    Transition.this.end();
                    animation.removeListener(this);
                }
            });
            animator.start();
        }
    }

    protected void start() {
        if (this.mNumInstances == 0) {
            if (this.mListeners != null && this.mListeners.size() > 0) {
                for (TransitionListener listener : this.mListeners) {
                    listener.onTransitionStart(this);
                }
            }
            this.mEnded = false;
        }
        this.mNumInstances++;
    }

    protected void end() {
        this.mNumInstances--;
        if (this.mNumInstances == 0) {
            if (this.mListeners != null && this.mListeners.size() > 0) {
                for (TransitionListener listener : this.mListeners) {
                    listener.onTransitionEnd(this);
                }
            }
            for (int i = 0; i < this.mStartValues.mItemIdValues.size(); i++) {
                View view = this.mStartValues.mItemIdValues.valueAt(i);
                if (view != null) {
                    view.setHasTransientState(false);
                }
            }
            for (int ix = 0; ix < this.mEndValues.mItemIdValues.size(); ix++) {
                View view = this.mEndValues.mItemIdValues.valueAt(ix);
                if (view != null) {
                    view.setHasTransientState(false);
                }
            }
            this.mEnded = true;
        }
    }

    void forceToEnd(@Nonnull ViewGroup sceneRoot) {
        ArrayMap<Animator, Transition.AnimationInfo> runningAnimators = (ArrayMap<Animator, Transition.AnimationInfo>) sRunningAnimators.get();
        int size = runningAnimators.size();
        if (size != 0) {
            ArrayMap<Animator, Transition.AnimationInfo> animators = new ArrayMap<>(runningAnimators);
            runningAnimators.clear();
            for (int i = size - 1; i >= 0; i--) {
                Transition.AnimationInfo info = animators.valueAt(i);
                if (info.view != null) {
                    animators.keyAt(i).end();
                }
            }
        }
    }

    public void cancel() {
        for (int i = this.mCurrentAnimators.size() - 1; i >= 0; i--) {
            ((Animator) this.mCurrentAnimators.get(i)).cancel();
        }
        if (this.mListeners != null && this.mListeners.size() > 0) {
            for (TransitionListener listener : this.mListeners) {
                listener.onTransitionCancel(this);
            }
        }
    }

    @Nonnull
    public Transition addListener(@Nonnull TransitionListener listener) {
        if (this.mListeners == null) {
            this.mListeners = new CopyOnWriteArrayList();
        }
        this.mListeners.addIfAbsent(listener);
        return this;
    }

    @Nonnull
    public Transition removeListener(@Nonnull TransitionListener listener) {
        if (this.mListeners == null) {
            return this;
        } else {
            this.mListeners.remove(listener);
            if (this.mListeners.isEmpty()) {
                this.mListeners = null;
            }
            return this;
        }
    }

    public void setEpicenterCallback(@Nullable Transition.EpicenterCallback epicenterCallback) {
        this.mEpicenterCallback = epicenterCallback;
    }

    @Nullable
    public Transition.EpicenterCallback getEpicenterCallback() {
        return this.mEpicenterCallback;
    }

    @Nullable
    public Rect getEpicenter() {
        return this.mEpicenterCallback == null ? null : this.mEpicenterCallback.onGetEpicenter(this);
    }

    public void setPropagation(@Nullable TransitionPropagation transitionPropagation) {
        this.mPropagation = transitionPropagation;
    }

    @Nullable
    public TransitionPropagation getPropagation() {
        return this.mPropagation;
    }

    void capturePropagationValues(@Nonnull TransitionValues transitionValues) {
        if (this.mPropagation != null && !transitionValues.values.isEmpty()) {
            String[] propertyNames = this.mPropagation.getPropagationProperties();
            if (propertyNames == null) {
                return;
            }
            boolean containsAll = true;
            for (String propertyName : propertyNames) {
                if (!transitionValues.values.containsKey(propertyName)) {
                    containsAll = false;
                    break;
                }
            }
            if (!containsAll) {
                this.mPropagation.captureValues(transitionValues);
            }
        }
    }

    void setCanRemoveViews(boolean canRemoveViews) {
        this.mCanRemoveViews = canRemoveViews;
    }

    @Nonnull
    public String getName() {
        return this.getClass().getName();
    }

    public String toString() {
        return this.toString("");
    }

    String toString(String indent) {
        StringBuilder sb = new StringBuilder(indent + this.getClass().getSimpleName() + "@" + Integer.toHexString(this.hashCode()) + ": ");
        if (this.mDuration != -1L) {
            sb.append("duration(").append(this.mDuration).append(") ");
        }
        if (this.mStartDelay != -1L) {
            sb.append("delay(").append(this.mStartDelay).append(") ");
        }
        if (this.mInterpolator != null) {
            sb.append("interpolator(").append(this.mInterpolator).append(") ");
        }
        if (this.mTargetIds.size() > 0 || this.mTargets.size() > 0) {
            sb.append("targets(");
            if (this.mTargetIds.size() > 0) {
                for (int i = 0; i < this.mTargetIds.size(); i++) {
                    if (i > 0) {
                        sb.append(", ");
                    }
                    sb.append(this.mTargetIds.getInt(i));
                }
            }
            if (this.mTargets.size() > 0) {
                for (int i = 0; i < this.mTargets.size(); i++) {
                    if (i > 0) {
                        sb.append(", ");
                    }
                    sb.append(this.mTargets.get(i));
                }
            }
            sb.append(")");
        }
        return sb.toString();
    }

    public Transition clone() {
        try {
            Transition clone = (Transition) super.clone();
            clone.mAnimators = new ArrayList();
            clone.mStartValues = new Transition.TransitionValuesMaps();
            clone.mEndValues = new Transition.TransitionValuesMaps();
            clone.mStartValuesList = null;
            clone.mEndValuesList = null;
            return clone;
        } catch (CloneNotSupportedException var2) {
            throw new InternalError(var2);
        }
    }

    private static record AnimationInfo(View view, String name, Transition transition, TransitionValues values) {
    }

    private static class ArrayListManager {

        @Nonnull
        static <T> ArrayList<T> add(@Nullable ArrayList<T> list, T item) {
            if (list == null) {
                list = new ArrayList();
            }
            if (!list.contains(item)) {
                list.add(item);
            }
            return list;
        }

        @Nonnull
        static IntArrayList add(@Nullable IntArrayList list, int item) {
            if (list == null) {
                list = new IntArrayList();
            }
            if (!list.contains(item)) {
                list.add(item);
            }
            return list;
        }

        @Nullable
        static <T> ArrayList<T> remove(@Nullable ArrayList<T> list, T item) {
            if (list != null) {
                list.remove(item);
                if (list.isEmpty()) {
                    list = null;
                }
            }
            return list;
        }

        @Nullable
        static IntArrayList remove(@Nullable IntArrayList list, int item) {
            if (list != null) {
                list.rem(item);
                if (list.isEmpty()) {
                    list = null;
                }
            }
            return list;
        }
    }

    @FunctionalInterface
    public interface EpicenterCallback {

        @Nullable
        Rect onGetEpicenter(@Nonnull Transition var1);
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface MatchOrder {
    }

    static final class TransitionValuesMaps {

        final ArrayMap<View, TransitionValues> mViewValues = new ArrayMap<>();

        final SparseArray<View> mIdValues = new SparseArray<>();

        final LongSparseArray<View> mItemIdValues = new LongSparseArray<>();

        final ArrayMap<String, View> mNameValues = new ArrayMap<>();
    }
}
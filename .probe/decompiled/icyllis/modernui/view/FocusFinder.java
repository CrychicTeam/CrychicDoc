package icyllis.modernui.view;

import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.annotation.Nullable;
import icyllis.modernui.graphics.Rect;
import icyllis.modernui.util.ArrayMap;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class FocusFinder {

    private static final ThreadLocal<FocusFinder> TLS = ThreadLocal.withInitial(FocusFinder::new);

    final Rect mFocusedRect = new Rect();

    final Rect mOtherRect = new Rect();

    final Rect mBestCandidateRect = new Rect();

    private final FocusFinder.UserSpecifiedFocusComparator mUserSpecifiedFocusComparator = new FocusFinder.UserSpecifiedFocusComparator((r, v) -> isValidId(v.getNextFocusForwardId()) ? v.findUserSetNextFocus(r, 2) : null);

    private final FocusFinder.UserSpecifiedFocusComparator mUserSpecifiedClusterComparator = new FocusFinder.UserSpecifiedFocusComparator((r, v) -> isValidId(v.getNextClusterForwardId()) ? v.findUserSetNextKeyboardNavigationCluster(r, 2) : null);

    private final FocusFinder.FocusSorter mFocusSorter = new FocusFinder.FocusSorter();

    private final ArrayList<View> mTempList = new ArrayList();

    public static FocusFinder getInstance() {
        return (FocusFinder) TLS.get();
    }

    private FocusFinder() {
    }

    public final View findNextFocus(@NonNull ViewGroup root, @Nullable View focused, int direction) {
        return this.findNextFocus(root, focused, null, direction);
    }

    public View findNextFocusFromRect(@NonNull ViewGroup root, @NonNull Rect focusedRect, int direction) {
        this.mFocusedRect.set(focusedRect);
        return this.findNextFocus(root, null, this.mFocusedRect, direction);
    }

    private View findNextFocus(@NonNull ViewGroup root, @Nullable View focused, @Nullable Rect focusedRect, int direction) {
        View next = null;
        ViewGroup effectiveRoot = this.getEffectiveRoot(root, focused);
        if (focused != null) {
            next = this.findNextUserSpecifiedFocus(effectiveRoot, focused, direction);
        }
        if (next != null) {
            return next;
        } else {
            ArrayList<View> focusables = this.mTempList;
            try {
                focusables.clear();
                effectiveRoot.addFocusables(focusables, direction);
                if (!focusables.isEmpty()) {
                    next = this.findNextFocus(effectiveRoot, focused, focusedRect, direction, focusables);
                }
            } finally {
                focusables.clear();
            }
            return next;
        }
    }

    private ViewGroup getEffectiveRoot(@NonNull ViewGroup root, @Nullable View focused) {
        if (focused != null && focused != root) {
            ViewGroup effective = null;
            ViewParent nextParent = focused.getParent();
            while (nextParent != root) {
                assert nextParent != null;
                ViewGroup vg = (ViewGroup) nextParent;
                if (vg.getTouchscreenBlocksFocus() && vg.isKeyboardNavigationCluster()) {
                    effective = vg;
                }
                nextParent = nextParent.getParent();
                if (!(nextParent instanceof ViewGroup)) {
                    return root;
                }
            }
            return effective != null ? effective : root;
        } else {
            return root;
        }
    }

    public View findNextKeyboardNavigationCluster(@NonNull View root, @Nullable View currentCluster, int direction) {
        View next = null;
        if (currentCluster != null) {
            next = this.findNextUserSpecifiedKeyboardNavigationCluster(root, currentCluster, direction);
            if (next != null) {
                return next;
            }
        }
        ArrayList<View> clusters = this.mTempList;
        try {
            clusters.clear();
            root.addKeyboardNavigationClusters(clusters, direction);
            if (!clusters.isEmpty()) {
                next = this.findNextKeyboardNavigationCluster(root, currentCluster, clusters, direction);
            }
        } finally {
            clusters.clear();
        }
        return next;
    }

    @Nullable
    private View findNextUserSpecifiedKeyboardNavigationCluster(View root, @NonNull View currentCluster, int direction) {
        View userSetNextCluster = currentCluster.findUserSetNextKeyboardNavigationCluster(root, direction);
        return userSetNextCluster != null && userSetNextCluster.hasFocusable() ? userSetNextCluster : null;
    }

    @Nullable
    private View findNextUserSpecifiedFocus(ViewGroup root, @NonNull View focused, int direction) {
        View userSetNextFocus = focused.findUserSetNextFocus(root, direction);
        View cycleCheck = userSetNextFocus;
        boolean cycleStep = true;
        while (userSetNextFocus != null) {
            if (userSetNextFocus.isFocusable() && userSetNextFocus.getVisibility() == 0 && (!userSetNextFocus.isInTouchMode() || userSetNextFocus.isFocusableInTouchMode())) {
                return userSetNextFocus;
            }
            userSetNextFocus = userSetNextFocus.findUserSetNextFocus(root, direction);
            cycleStep = !cycleStep;
            if (cycleStep) {
                cycleCheck = cycleCheck.findUserSetNextFocus(root, direction);
                if (cycleCheck != userSetNextFocus) {
                    continue;
                }
                break;
            }
        }
        return null;
    }

    private View findNextFocus(ViewGroup root, View focused, Rect focusedRect, int direction, ArrayList<View> focusables) {
        if (focused != null) {
            if (focusedRect == null) {
                focusedRect = this.mFocusedRect;
            }
            focused.getFocusedRect(focusedRect);
            root.offsetDescendantRectToMyCoords(focused, focusedRect);
        } else if (focusedRect == null) {
            focusedRect = this.mFocusedRect;
            switch(direction) {
                case 1:
                    if (root.isLayoutRtl()) {
                        this.setFocusTopLeft(root, focusedRect);
                    } else {
                        this.setFocusBottomRight(root, focusedRect);
                    }
                    break;
                case 2:
                    if (root.isLayoutRtl()) {
                        this.setFocusBottomRight(root, focusedRect);
                    } else {
                        this.setFocusTopLeft(root, focusedRect);
                    }
                    break;
                case 17:
                case 33:
                    this.setFocusBottomRight(root, focusedRect);
                    break;
                case 66:
                case 130:
                    this.setFocusTopLeft(root, focusedRect);
            }
        }
        return switch(direction) {
            case 1, 2 ->
                this.findNextFocusInRelativeDirection(focusables, root, focused, direction);
            case 17, 33, 66, 130 ->
                this.findNextFocusInAbsoluteDirection(focusables, root, focused, focusedRect, direction);
            default ->
                throw new IllegalArgumentException("Unknown direction: " + direction);
        };
    }

    private View findNextKeyboardNavigationCluster(View root, View currentCluster, List<View> clusters, int direction) {
        try {
            this.mUserSpecifiedClusterComparator.setFocusables(clusters, root);
            clusters.sort(this.mUserSpecifiedClusterComparator);
        } finally {
            this.mUserSpecifiedClusterComparator.recycle();
        }
        int count = clusters.size();
        return switch(direction) {
            case 1, 17, 33 ->
                getPreviousKeyboardNavigationCluster(root, currentCluster, clusters, count);
            case 2, 66, 130 ->
                getNextKeyboardNavigationCluster(root, currentCluster, clusters, count);
            default ->
                throw new IllegalArgumentException("Unknown direction: " + direction);
        };
    }

    @Nullable
    private View findNextFocusInRelativeDirection(ArrayList<View> focusables, ViewGroup root, View focused, int direction) {
        try {
            this.mUserSpecifiedFocusComparator.setFocusables(focusables, root);
            focusables.sort(this.mUserSpecifiedFocusComparator);
        } finally {
            this.mUserSpecifiedFocusComparator.recycle();
        }
        int count = focusables.size();
        if (count < 2) {
            return null;
        } else {
            return switch(direction) {
                case 1 ->
                    getPreviousFocusable(focused, focusables, count);
                case 2 ->
                    getNextFocusable(focused, focusables, count);
                default ->
                    (View) focusables.get(count - 1);
            };
        }
    }

    private void setFocusBottomRight(@NonNull ViewGroup root, @NonNull Rect focusedRect) {
        int rootBottom = root.getScrollY() + root.getHeight();
        int rootRight = root.getScrollX() + root.getWidth();
        focusedRect.set(rootRight, rootBottom, rootRight, rootBottom);
    }

    private void setFocusTopLeft(@NonNull ViewGroup root, @NonNull Rect focusedRect) {
        int rootTop = root.getScrollY();
        int rootLeft = root.getScrollX();
        focusedRect.set(rootLeft, rootTop, rootLeft, rootTop);
    }

    View findNextFocusInAbsoluteDirection(ArrayList<View> focusables, ViewGroup root, View focused, Rect focusedRect, int direction) {
        this.mBestCandidateRect.set(focusedRect);
        switch(direction) {
            case 17:
                this.mBestCandidateRect.offset(focusedRect.width() + 1, 0);
                break;
            case 33:
                this.mBestCandidateRect.offset(0, focusedRect.height() + 1);
                break;
            case 66:
                this.mBestCandidateRect.offset(-(focusedRect.width() + 1), 0);
                break;
            case 130:
                this.mBestCandidateRect.offset(0, -(focusedRect.height() + 1));
        }
        View closest = null;
        for (View focusable : focusables) {
            if (focusable != focused && focusable != root) {
                focusable.getFocusedRect(this.mOtherRect);
                root.offsetDescendantRectToMyCoords(focusable, this.mOtherRect);
                if (this.isBetterCandidate(direction, focusedRect, this.mOtherRect, this.mBestCandidateRect)) {
                    this.mBestCandidateRect.set(this.mOtherRect);
                    closest = focusable;
                }
            }
        }
        return closest;
    }

    private static View getNextFocusable(View focused, ArrayList<View> focusables, int count) {
        if (count < 2) {
            return null;
        } else {
            if (focused != null) {
                int position = focusables.lastIndexOf(focused);
                if (position >= 0 && position + 1 < count) {
                    return (View) focusables.get(position + 1);
                }
            }
            return (View) focusables.get(0);
        }
    }

    private static View getPreviousFocusable(View focused, ArrayList<View> focusables, int count) {
        if (count < 2) {
            return null;
        } else {
            if (focused != null) {
                int position = focusables.indexOf(focused);
                if (position > 0) {
                    return (View) focusables.get(position - 1);
                }
            }
            return (View) focusables.get(count - 1);
        }
    }

    private static View getNextKeyboardNavigationCluster(View root, View currentCluster, List<View> clusters, int count) {
        if (currentCluster == null) {
            return (View) clusters.get(0);
        } else {
            int position = clusters.lastIndexOf(currentCluster);
            return position >= 0 && position + 1 < count ? (View) clusters.get(position + 1) : root;
        }
    }

    private static View getPreviousKeyboardNavigationCluster(View root, View currentCluster, List<View> clusters, int count) {
        if (currentCluster == null) {
            return (View) clusters.get(count - 1);
        } else {
            int position = clusters.indexOf(currentCluster);
            return position > 0 ? (View) clusters.get(position - 1) : root;
        }
    }

    boolean isBetterCandidate(int direction, Rect source, Rect rect1, Rect rect2) {
        if (!this.isCandidate(source, rect1, direction)) {
            return false;
        } else if (!this.isCandidate(source, rect2, direction)) {
            return true;
        } else if (this.beamBeats(direction, source, rect1, rect2)) {
            return true;
        } else {
            return this.beamBeats(direction, source, rect2, rect1) ? false : this.getWeightedDistanceFor((long) majorAxisDistance(direction, source, rect1), (long) minorAxisDistance(direction, source, rect1)) < this.getWeightedDistanceFor((long) majorAxisDistance(direction, source, rect2), (long) minorAxisDistance(direction, source, rect2));
        }
    }

    boolean beamBeats(int direction, Rect source, Rect rect1, Rect rect2) {
        boolean rect1InSrcBeam = this.beamsOverlap(direction, source, rect1);
        boolean rect2InSrcBeam = this.beamsOverlap(direction, source, rect2);
        if (rect2InSrcBeam || !rect1InSrcBeam) {
            return false;
        } else if (!this.isToDirectionOf(direction, source, rect2)) {
            return true;
        } else {
            return direction != 17 && direction != 66 ? majorAxisDistance(direction, source, rect1) < majorAxisDistanceToFarEdge(direction, source, rect2) : true;
        }
    }

    long getWeightedDistanceFor(long majorAxisDistance, long minorAxisDistance) {
        return 13L * majorAxisDistance * majorAxisDistance + minorAxisDistance * minorAxisDistance;
    }

    boolean isCandidate(Rect srcRect, Rect destRect, int direction) {
        switch(direction) {
            case 17:
                return (srcRect.right > destRect.right || srcRect.left >= destRect.right) && srcRect.left > destRect.left;
            case 33:
                return (srcRect.bottom > destRect.bottom || srcRect.top >= destRect.bottom) && srcRect.top > destRect.top;
            case 66:
                return (srcRect.left < destRect.left || srcRect.right <= destRect.left) && srcRect.right < destRect.right;
            case 130:
                return (srcRect.top < destRect.top || srcRect.bottom <= destRect.top) && srcRect.bottom < destRect.bottom;
            default:
                throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
        }
    }

    boolean beamsOverlap(int direction, Rect rect1, Rect rect2) {
        switch(direction) {
            case 17:
            case 66:
                return rect2.bottom > rect1.top && rect2.top < rect1.bottom;
            case 33:
            case 130:
                return rect2.right > rect1.left && rect2.left < rect1.right;
            default:
                throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
        }
    }

    boolean isToDirectionOf(int direction, Rect src, Rect dest) {
        switch(direction) {
            case 17:
                return src.left >= dest.right;
            case 33:
                return src.top >= dest.bottom;
            case 66:
                return src.right <= dest.left;
            case 130:
                return src.bottom <= dest.top;
            default:
                throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
        }
    }

    static int majorAxisDistance(int direction, Rect source, Rect dest) {
        return Math.max(0, majorAxisDistanceRaw(direction, source, dest));
    }

    static int majorAxisDistanceRaw(int direction, Rect source, Rect dest) {
        switch(direction) {
            case 17:
                return source.left - dest.right;
            case 33:
                return source.top - dest.bottom;
            case 66:
                return dest.left - source.right;
            case 130:
                return dest.top - source.bottom;
            default:
                throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
        }
    }

    static int majorAxisDistanceToFarEdge(int direction, Rect source, Rect dest) {
        return Math.max(1, majorAxisDistanceToFarEdgeRaw(direction, source, dest));
    }

    static int majorAxisDistanceToFarEdgeRaw(int direction, Rect source, Rect dest) {
        switch(direction) {
            case 17:
                return source.left - dest.left;
            case 33:
                return source.top - dest.top;
            case 66:
                return dest.right - source.right;
            case 130:
                return dest.bottom - source.bottom;
            default:
                throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
        }
    }

    static int minorAxisDistance(int direction, Rect source, Rect dest) {
        switch(direction) {
            case 17:
            case 66:
                return Math.abs(source.top + source.height() / 2 - (dest.top + dest.height() / 2));
            case 33:
            case 130:
                return Math.abs(source.left + source.width() / 2 - (dest.left + dest.width() / 2));
            default:
                throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
        }
    }

    public View findNearestTouchable(@NonNull ViewGroup root, int x, int y, int direction, int[] deltas) {
        ArrayList<View> touchables = root.getTouchables();
        int minDistance = Integer.MAX_VALUE;
        View closest = null;
        int edgeSlop = ViewConfiguration.get(root.getContext()).getScaledEdgeSlop();
        Rect closestBounds = new Rect();
        Rect touchableBounds = this.mOtherRect;
        for (View touchable : touchables) {
            touchable.getDrawingRect(touchableBounds);
            root.offsetRectBetweenParentAndChild(touchable, touchableBounds, true, true);
            if (this.isTouchCandidate(x, y, touchableBounds, direction)) {
                int distance = switch(direction) {
                    case 17 ->
                        x - touchableBounds.right + 1;
                    case 33 ->
                        y - touchableBounds.bottom + 1;
                    case 66 ->
                        touchableBounds.left;
                    case 130 ->
                        touchableBounds.top;
                    default ->
                        Integer.MAX_VALUE;
                };
                if (distance < edgeSlop && (closest == null || closestBounds.contains(touchableBounds) || !touchableBounds.contains(closestBounds) && distance < minDistance)) {
                    minDistance = distance;
                    closest = touchable;
                    closestBounds.set(touchableBounds);
                    switch(direction) {
                        case 17:
                            deltas[0] = -distance;
                            break;
                        case 33:
                            deltas[1] = -distance;
                            break;
                        case 66:
                            deltas[0] = distance;
                            break;
                        case 130:
                            deltas[1] = distance;
                    }
                }
            }
        }
        return closest;
    }

    private boolean isTouchCandidate(int x, int y, Rect destRect, int direction) {
        switch(direction) {
            case 17:
                return destRect.left <= x && destRect.top <= y && y <= destRect.bottom;
            case 33:
                return destRect.top <= y && destRect.left <= x && x <= destRect.right;
            case 66:
                return destRect.left >= x && destRect.top <= y && y <= destRect.bottom;
            case 130:
                return destRect.top >= y && destRect.left <= x && x <= destRect.right;
            default:
                throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
        }
    }

    private static boolean isValidId(int id) {
        return id != 0 && id != -1;
    }

    public static void sort(View[] views, int start, int end, ViewGroup root, boolean isRtl) {
        getInstance().mFocusSorter.sort(views, start, end, root, isRtl);
    }

    static final class FocusSorter {

        private final ArrayList<Rect> mRectPool = new ArrayList();

        private int mLastPoolRect;

        private int mRtlMult;

        private HashMap<View, Rect> mRectByView = null;

        private final Comparator<View> mTopsComparator = (first, second) -> {
            if (first == second) {
                return 0;
            } else {
                Rect firstRect = (Rect) this.mRectByView.get(first);
                Rect secondRect = (Rect) this.mRectByView.get(second);
                int result = firstRect.top - secondRect.top;
                return result == 0 ? firstRect.bottom - secondRect.bottom : result;
            }
        };

        private final Comparator<View> mSidesComparator = (first, second) -> {
            if (first == second) {
                return 0;
            } else {
                Rect firstRect = (Rect) this.mRectByView.get(first);
                Rect secondRect = (Rect) this.mRectByView.get(second);
                int result = firstRect.left - secondRect.left;
                return result == 0 ? firstRect.right - secondRect.right : this.mRtlMult * result;
            }
        };

        public void sort(View[] views, int start, int end, ViewGroup root, boolean isRtl) {
            int count = end - start;
            if (count >= 2) {
                if (this.mRectByView == null) {
                    this.mRectByView = new HashMap();
                }
                this.mRtlMult = isRtl ? -1 : 1;
                for (int i = this.mRectPool.size(); i < count; i++) {
                    this.mRectPool.add(new Rect());
                }
                for (int i = start; i < end; i++) {
                    Rect next = (Rect) this.mRectPool.get(this.mLastPoolRect++);
                    views[i].getDrawingRect(next);
                    root.offsetDescendantRectToMyCoords(views[i], next);
                    this.mRectByView.put(views[i], next);
                }
                Arrays.sort(views, start, count, this.mTopsComparator);
                int sweepBottom = ((Rect) this.mRectByView.get(views[start])).bottom;
                int rowStart = start;
                int sweepIdx;
                for (sweepIdx = start + 1; sweepIdx < end; sweepIdx++) {
                    Rect currRect = (Rect) this.mRectByView.get(views[sweepIdx]);
                    if (currRect.top >= sweepBottom) {
                        if (sweepIdx - rowStart > 1) {
                            Arrays.sort(views, rowStart, sweepIdx, this.mSidesComparator);
                        }
                        sweepBottom = currRect.bottom;
                        rowStart = sweepIdx;
                    } else {
                        sweepBottom = Math.max(sweepBottom, currRect.bottom);
                    }
                }
                if (sweepIdx - rowStart > 1) {
                    Arrays.sort(views, rowStart, sweepIdx, this.mSidesComparator);
                }
                this.mLastPoolRect = 0;
                this.mRectByView.clear();
            }
        }
    }

    private static final class UserSpecifiedFocusComparator implements Comparator<View> {

        private final ArrayMap<View, View> mNextFoci = new ArrayMap<>();

        private final ObjectArraySet<View> mIsConnectedTo = new ObjectArraySet();

        private final ArrayMap<View, View> mHeadsOfChains = new ArrayMap<>();

        private final ArrayMap<View, Integer> mOriginalOrdinal = new ArrayMap<>();

        private final FocusFinder.UserSpecifiedFocusComparator.NextFocusGetter mNextFocusGetter;

        private View mRoot;

        UserSpecifiedFocusComparator(FocusFinder.UserSpecifiedFocusComparator.NextFocusGetter nextFocusGetter) {
            this.mNextFocusGetter = nextFocusGetter;
        }

        public void recycle() {
            this.mRoot = null;
            this.mHeadsOfChains.clear();
            this.mIsConnectedTo.clear();
            this.mOriginalOrdinal.clear();
            this.mNextFoci.clear();
        }

        public void setFocusables(@NonNull List<View> focusables, View root) {
            this.mRoot = root;
            for (int i = 0; i < focusables.size(); i++) {
                this.mOriginalOrdinal.put((View) focusables.get(i), i);
            }
            for (int i = focusables.size() - 1; i >= 0; i--) {
                View view = (View) focusables.get(i);
                View next = this.mNextFocusGetter.get(this.mRoot, view);
                if (next != null && this.mOriginalOrdinal.containsKey(next)) {
                    this.mNextFoci.put(view, next);
                    this.mIsConnectedTo.add(next);
                }
            }
            for (int ix = focusables.size() - 1; ix >= 0; ix--) {
                View view = (View) focusables.get(ix);
                View next = this.mNextFoci.get(view);
                if (next != null && !this.mIsConnectedTo.contains(view)) {
                    this.setHeadOfChain(view);
                }
            }
        }

        private void setHeadOfChain(View head) {
            View view = head;
            while (view != null) {
                View otherHead = this.mHeadsOfChains.get(view);
                if (otherHead != null) {
                    if (otherHead == head) {
                        return;
                    }
                    view = head;
                    head = otherHead;
                }
                this.mHeadsOfChains.put(view, head);
                view = this.mNextFoci.get(view);
            }
        }

        public int compare(View first, View second) {
            if (first == second) {
                return 0;
            } else {
                View firstHead = this.mHeadsOfChains.get(first);
                View secondHead = this.mHeadsOfChains.get(second);
                if (firstHead != secondHead || firstHead == null) {
                    boolean involvesChain = false;
                    if (firstHead != null) {
                        first = firstHead;
                        involvesChain = true;
                    }
                    if (secondHead != null) {
                        second = secondHead;
                        involvesChain = true;
                    }
                    if (involvesChain) {
                        return this.mOriginalOrdinal.get(first) < this.mOriginalOrdinal.get(second) ? -1 : 1;
                    } else {
                        return 0;
                    }
                } else if (first == firstHead) {
                    return -1;
                } else if (second == firstHead) {
                    return 1;
                } else {
                    return this.mNextFoci.get(first) != null ? -1 : 1;
                }
            }
        }

        public interface NextFocusGetter {

            View get(View var1, View var2);
        }
    }
}
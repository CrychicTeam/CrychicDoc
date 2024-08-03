package icyllis.modernui.transition;

import icyllis.modernui.animation.Animator;
import icyllis.modernui.animation.AnimatorListener;
import icyllis.modernui.view.View;
import icyllis.modernui.view.ViewGroup;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class Visibility extends Transition {

    static final String PROPNAME_VISIBILITY = "modernui:visibility:visibility";

    private static final String PROPNAME_PARENT = "modernui:visibility:parent";

    private static final String PROPNAME_SCREEN_LOCATION = "modernui:visibility:screenLocation";

    public static final int MODE_IN = 1;

    public static final int MODE_OUT = 2;

    private static final String[] sTransitionProperties = new String[] { "modernui:visibility:visibility", "modernui:visibility:parent" };

    private int mMode = 3;

    public void setMode(int mode) {
        if ((mode & -4) != 0) {
            throw new IllegalArgumentException("Only MODE_IN and MODE_OUT flags are allowed");
        } else {
            this.mMode = mode;
        }
    }

    public int getMode() {
        return this.mMode;
    }

    @Nullable
    @Override
    public String[] getTransitionProperties() {
        return sTransitionProperties;
    }

    private void captureValues(@Nonnull TransitionValues transitionValues) {
        int visibility = transitionValues.view.getVisibility();
        transitionValues.values.put("modernui:visibility:visibility", visibility);
        transitionValues.values.put("modernui:visibility:parent", transitionValues.view.getParent());
        int[] loc = new int[2];
        transitionValues.view.getLocationInWindow(loc);
        transitionValues.values.put("modernui:visibility:screenLocation", loc);
    }

    @Override
    public void captureStartValues(@Nonnull TransitionValues transitionValues) {
        this.captureValues(transitionValues);
    }

    @Override
    public void captureEndValues(@Nonnull TransitionValues transitionValues) {
        this.captureValues(transitionValues);
    }

    public boolean isVisible(TransitionValues values) {
        if (values == null) {
            return false;
        } else {
            int visibility = (Integer) values.values.get("modernui:visibility:visibility");
            View parent = (View) values.values.get("modernui:visibility:parent");
            return visibility == 0 && parent != null;
        }
    }

    private Visibility.VisibilityInfo getVisibilityChangeInfo(TransitionValues startValues, TransitionValues endValues) {
        Visibility.VisibilityInfo visInfo = new Visibility.VisibilityInfo();
        visInfo.mVisibilityChange = false;
        visInfo.mFadeIn = false;
        if (startValues != null && startValues.values.containsKey("modernui:visibility:visibility")) {
            visInfo.mStartVisibility = (Integer) startValues.values.get("modernui:visibility:visibility");
            visInfo.mStartParent = (ViewGroup) startValues.values.get("modernui:visibility:parent");
        } else {
            visInfo.mStartVisibility = -1;
            visInfo.mStartParent = null;
        }
        if (endValues != null && endValues.values.containsKey("modernui:visibility:visibility")) {
            visInfo.mEndVisibility = (Integer) endValues.values.get("modernui:visibility:visibility");
            visInfo.mEndParent = (ViewGroup) endValues.values.get("modernui:visibility:parent");
        } else {
            visInfo.mEndVisibility = -1;
            visInfo.mEndParent = null;
        }
        if (startValues != null && endValues != null) {
            if (visInfo.mStartVisibility == visInfo.mEndVisibility && visInfo.mStartParent == visInfo.mEndParent) {
                return visInfo;
            }
            if (visInfo.mStartVisibility != visInfo.mEndVisibility) {
                if (visInfo.mStartVisibility == 0) {
                    visInfo.mFadeIn = false;
                    visInfo.mVisibilityChange = true;
                } else if (visInfo.mEndVisibility == 0) {
                    visInfo.mFadeIn = true;
                    visInfo.mVisibilityChange = true;
                }
            } else if (visInfo.mEndParent == null) {
                visInfo.mFadeIn = false;
                visInfo.mVisibilityChange = true;
            } else if (visInfo.mStartParent == null) {
                visInfo.mFadeIn = true;
                visInfo.mVisibilityChange = true;
            }
        } else if (startValues == null && visInfo.mEndVisibility == 0) {
            visInfo.mFadeIn = true;
            visInfo.mVisibilityChange = true;
        } else if (endValues == null && visInfo.mStartVisibility == 0) {
            visInfo.mFadeIn = false;
            visInfo.mVisibilityChange = true;
        }
        return visInfo;
    }

    @Nullable
    @Override
    public Animator createAnimator(@Nonnull ViewGroup sceneRoot, @Nullable TransitionValues startValues, @Nullable TransitionValues endValues) {
        Visibility.VisibilityInfo visInfo = this.getVisibilityChangeInfo(startValues, endValues);
        if (visInfo.mVisibilityChange && (visInfo.mStartParent != null || visInfo.mEndParent != null)) {
            return visInfo.mFadeIn ? this.onAppear(sceneRoot, startValues, visInfo.mStartVisibility, endValues, visInfo.mEndVisibility) : this.onDisappear(sceneRoot, startValues, visInfo.mStartVisibility, endValues, visInfo.mEndVisibility);
        } else {
            return null;
        }
    }

    @Nullable
    public Animator onAppear(ViewGroup sceneRoot, TransitionValues startValues, int startVisibility, TransitionValues endValues, int endVisibility) {
        if ((this.mMode & 1) == 1 && endValues != null) {
            if (startValues == null && endValues.view.getParent() instanceof View endParent) {
                TransitionValues startParentValues = this.getMatchedTransitionValues(endParent, false);
                TransitionValues endParentValues = this.getTransitionValues(endParent, false);
                Visibility.VisibilityInfo parentVisibilityInfo = this.getVisibilityChangeInfo(startParentValues, endParentValues);
                if (parentVisibilityInfo.mVisibilityChange) {
                    return null;
                }
            }
            return this.onAppear(sceneRoot, endValues.view, startValues, endValues);
        } else {
            return null;
        }
    }

    @Nullable
    public Animator onAppear(ViewGroup sceneRoot, View view, TransitionValues startValues, TransitionValues endValues) {
        return null;
    }

    @Nullable
    public Animator onDisappear(ViewGroup sceneRoot, TransitionValues startValues, int startVisibility, TransitionValues endValues, int endVisibility) {
        if ((this.mMode & 2) != 2) {
            return null;
        } else if (startValues == null) {
            return null;
        } else {
            final View startView = startValues.view;
            View endView = endValues != null ? endValues.view : null;
            View overlayView = null;
            View viewToKeep = null;
            boolean reusingOverlayView = false;
            View savedOverlayView = (View) startView.getTag(67239938);
            if (savedOverlayView != null) {
                overlayView = savedOverlayView;
                reusingOverlayView = true;
            } else {
                boolean needOverlayForStartView = false;
                if (endView != null && endView.getParent() != null) {
                    if (endVisibility == 4) {
                        viewToKeep = endView;
                    } else if (startView == endView) {
                        viewToKeep = endView;
                    } else {
                        needOverlayForStartView = true;
                    }
                } else if (endView != null) {
                    overlayView = endView;
                } else {
                    needOverlayForStartView = true;
                }
                if (needOverlayForStartView) {
                    if (startView.getParent() == null) {
                        overlayView = startView;
                    } else if (startView.getParent() instanceof View startParent) {
                        TransitionValues startParentValues = this.getTransitionValues(startParent, true);
                        TransitionValues endParentValues = this.getMatchedTransitionValues(startParent, true);
                        Visibility.VisibilityInfo parentVisibilityInfo = this.getVisibilityChangeInfo(startParentValues, endParentValues);
                        if (!parentVisibilityInfo.mVisibilityChange) {
                        }
                        overlayView = startView;
                    }
                }
            }
            if (overlayView != null) {
                if (!reusingOverlayView) {
                    int[] screenLoc = (int[]) startValues.values.get("modernui:visibility:screenLocation");
                    int screenX = screenLoc[0];
                    int screenY = screenLoc[1];
                    int[] loc = new int[2];
                    sceneRoot.getLocationInWindow(loc);
                    overlayView.offsetLeftAndRight(screenX - loc[0] - overlayView.getLeft());
                    overlayView.offsetTopAndBottom(screenY - loc[1] - overlayView.getTop());
                    sceneRoot.startViewTransition(overlayView);
                }
                Animator animator = this.onDisappear(sceneRoot, overlayView, startValues, endValues);
                if (!reusingOverlayView) {
                    if (animator == null) {
                        sceneRoot.endViewTransition(overlayView);
                    } else {
                        startView.setTag(67239938, overlayView);
                        final View finalOverlayView = overlayView;
                        final ViewGroup overlayHost = sceneRoot;
                        this.addListener(new TransitionListener() {

                            @Override
                            public void onTransitionPause(@Nonnull Transition transition) {
                                overlayHost.endViewTransition(finalOverlayView);
                            }

                            @Override
                            public void onTransitionResume(@Nonnull Transition transition) {
                                if (finalOverlayView.getParent() == null) {
                                    overlayHost.startViewTransition(finalOverlayView);
                                } else {
                                    Visibility.this.cancel();
                                }
                            }

                            @Override
                            public void onTransitionEnd(@Nonnull Transition transition) {
                                startView.setTag(67239938, null);
                                overlayHost.endViewTransition(finalOverlayView);
                                transition.removeListener(this);
                            }
                        });
                    }
                }
                return animator;
            } else if (viewToKeep != null) {
                int originalVisibility = viewToKeep.getVisibility();
                viewToKeep.setTransitionVisibility(0);
                Animator animator = this.onDisappear(sceneRoot, viewToKeep, startValues, endValues);
                if (animator != null) {
                    Visibility.DisappearListener disappearListener = new Visibility.DisappearListener(viewToKeep, endVisibility, true);
                    animator.addListener(disappearListener);
                    this.addListener(disappearListener);
                } else {
                    viewToKeep.setTransitionVisibility(originalVisibility);
                }
                return animator;
            } else {
                return null;
            }
        }
    }

    @Nullable
    public Animator onDisappear(ViewGroup sceneRoot, View view, TransitionValues startValues, TransitionValues endValues) {
        return null;
    }

    @Override
    public boolean isTransitionRequired(@Nullable TransitionValues startValues, @Nullable TransitionValues newValues) {
        if (startValues == null && newValues == null) {
            return false;
        } else if (startValues != null && newValues != null && newValues.values.containsKey("modernui:visibility:visibility") != startValues.values.containsKey("modernui:visibility:visibility")) {
            return false;
        } else {
            Visibility.VisibilityInfo changeInfo = this.getVisibilityChangeInfo(startValues, newValues);
            return changeInfo.mVisibilityChange && (changeInfo.mStartVisibility == 0 || changeInfo.mEndVisibility == 0);
        }
    }

    private static class DisappearListener implements AnimatorListener, TransitionListener {

        private final View mView;

        private final int mFinalVisibility;

        private final ViewGroup mParent;

        private final boolean mSuppressLayout;

        private boolean mLayoutSuppressed;

        boolean mCanceled = false;

        DisappearListener(@Nonnull View view, int finalVisibility, boolean suppressLayout) {
            this.mView = view;
            this.mFinalVisibility = finalVisibility;
            this.mParent = (ViewGroup) view.getParent();
            this.mSuppressLayout = suppressLayout;
            this.suppressLayout(true);
        }

        @Override
        public void onAnimationPause(@Nonnull Animator animation) {
            if (!this.mCanceled) {
                this.mView.setTransitionVisibility(this.mFinalVisibility);
            }
        }

        @Override
        public void onAnimationResume(@Nonnull Animator animation) {
            if (!this.mCanceled) {
                this.mView.setTransitionVisibility(0);
            }
        }

        @Override
        public void onAnimationCancel(@Nonnull Animator animation) {
            this.mCanceled = true;
        }

        @Override
        public void onAnimationRepeat(@Nonnull Animator animation) {
        }

        @Override
        public void onAnimationStart(@Nonnull Animator animation) {
        }

        @Override
        public void onAnimationEnd(@Nonnull Animator animation) {
            this.hideViewWhenNotCanceled();
        }

        @Override
        public void onTransitionStart(@Nonnull Transition transition) {
        }

        @Override
        public void onTransitionEnd(@Nonnull Transition transition) {
            this.hideViewWhenNotCanceled();
            transition.removeListener(this);
        }

        @Override
        public void onTransitionCancel(@Nonnull Transition transition) {
        }

        @Override
        public void onTransitionPause(@Nonnull Transition transition) {
            this.suppressLayout(false);
        }

        @Override
        public void onTransitionResume(@Nonnull Transition transition) {
            this.suppressLayout(true);
        }

        private void hideViewWhenNotCanceled() {
            if (!this.mCanceled) {
                this.mView.setTransitionVisibility(this.mFinalVisibility);
                if (this.mParent != null) {
                    this.mParent.invalidate();
                }
            }
            this.suppressLayout(false);
        }

        private void suppressLayout(boolean suppress) {
            if (this.mSuppressLayout && this.mLayoutSuppressed != suppress && this.mParent != null) {
                this.mLayoutSuppressed = suppress;
                this.mParent.suppressLayout(suppress);
            }
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface Mode {
    }

    private static class VisibilityInfo {

        boolean mVisibilityChange;

        boolean mFadeIn;

        int mStartVisibility;

        int mEndVisibility;

        ViewGroup mStartParent;

        ViewGroup mEndParent;
    }
}
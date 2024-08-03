package icyllis.modernui.fragment;

import icyllis.modernui.util.DataSet;
import icyllis.modernui.view.View;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface FragmentLifecycleCallbacks {

    default void onFragmentPreAttached(@Nonnull FragmentManager fm, @Nonnull Fragment f) {
    }

    default void onFragmentAttached(@Nonnull FragmentManager fm, @Nonnull Fragment f) {
    }

    default void onFragmentPreCreated(@Nonnull FragmentManager fm, @Nonnull Fragment f, @Nullable DataSet savedInstanceState) {
    }

    default void onFragmentCreated(@Nonnull FragmentManager fm, @Nonnull Fragment f, @Nullable DataSet savedInstanceState) {
    }

    default void onFragmentViewCreated(@Nonnull FragmentManager fm, @Nonnull Fragment f, @Nonnull View v, @Nullable DataSet savedInstanceState) {
    }

    default void onFragmentStarted(@Nonnull FragmentManager fm, @Nonnull Fragment f) {
    }

    default void onFragmentResumed(@Nonnull FragmentManager fm, @Nonnull Fragment f) {
    }

    default void onFragmentPaused(@Nonnull FragmentManager fm, @Nonnull Fragment f) {
    }

    default void onFragmentStopped(@Nonnull FragmentManager fm, @Nonnull Fragment f) {
    }

    default void onFragmentSaveInstanceState(@Nonnull FragmentManager fm, @Nonnull Fragment f, @Nonnull DataSet outState) {
    }

    default void onFragmentViewDestroyed(@Nonnull FragmentManager fm, @Nonnull Fragment f) {
    }

    default void onFragmentDestroyed(@Nonnull FragmentManager fm, @Nonnull Fragment f) {
    }

    default void onFragmentDetached(@Nonnull FragmentManager fm, @Nonnull Fragment f) {
    }
}
package icyllis.modernui.util;

public interface DataSetObserver {

    default void onChanged() {
    }

    default void onInvalidated() {
    }
}
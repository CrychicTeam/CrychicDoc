package icyllis.arc3d.core;

import javax.annotation.Nonnull;

public sealed interface Rect2ic permits Rect2i {

    boolean isEmpty();

    boolean isSorted();

    int x();

    int y();

    int left();

    int top();

    int right();

    int bottom();

    int width();

    int height();

    void store(@Nonnull Rect2i var1);

    void store(@Nonnull Rect2f var1);

    boolean intersects(Rect2ic var1);

    boolean contains(int var1, int var2);

    boolean contains(float var1, float var2);

    boolean contains(Rect2ic var1);

    boolean contains(Rect2fc var1);
}
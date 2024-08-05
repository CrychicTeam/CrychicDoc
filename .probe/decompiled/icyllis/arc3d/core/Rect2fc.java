package icyllis.arc3d.core;

import javax.annotation.Nonnull;

public sealed interface Rect2fc permits Rect2f {

    boolean isEmpty();

    boolean isSorted();

    boolean isFinite();

    float x();

    float y();

    float left();

    float top();

    float right();

    float bottom();

    float width();

    float height();

    float centerX();

    float centerY();

    void store(@Nonnull Rect2f var1);

    boolean intersects(@Nonnull Rect2fc var1);

    boolean intersects(@Nonnull Rect2ic var1);

    boolean contains(float var1, float var2);

    boolean contains(@Nonnull Rect2fc var1);

    boolean contains(@Nonnull Rect2ic var1);

    void round(@Nonnull Rect2i var1);

    void roundIn(@Nonnull Rect2i var1);

    void roundOut(@Nonnull Rect2i var1);

    void round(@Nonnull Rect2f var1);

    void roundIn(@Nonnull Rect2f var1);

    void roundOut(@Nonnull Rect2f var1);
}
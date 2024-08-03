package icyllis.modernui.text.style;

import icyllis.modernui.text.TextPaint;
import icyllis.modernui.view.View;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nonnull;
import org.jetbrains.annotations.ApiStatus.Internal;

public abstract class ClickableSpan extends CharacterStyle implements UpdateAppearance {

    private static final AtomicInteger sIdCounter = new AtomicInteger();

    private final int mId = sIdCounter.getAndIncrement();

    public abstract void onClick(@Nonnull View var1);

    @Override
    public void updateDrawState(@Nonnull TextPaint ds) {
        ds.setUnderline(true);
        ds.setColor(ds.linkColor);
    }

    @Internal
    public final int getId() {
        return this.mId;
    }
}
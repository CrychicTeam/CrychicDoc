package icyllis.modernui.markdown;

import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.text.Spanned;
import icyllis.modernui.widget.TextView;

@FunctionalInterface
public interface TextSetter {

    void setText(@NonNull TextView var1, @NonNull Spanned var2, @NonNull TextView.BufferType var3, @NonNull Runnable var4);
}
package icyllis.modernui.text;

import icyllis.modernui.util.Parcelable;
import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
public interface ParcelableSpan extends Parcelable {

    int getSpanTypeId();
}
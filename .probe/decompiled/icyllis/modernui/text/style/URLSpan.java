package icyllis.modernui.text.style;

import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.core.Core;
import icyllis.modernui.text.ParcelableSpan;
import icyllis.modernui.util.Parcel;
import icyllis.modernui.view.View;

public class URLSpan extends ClickableSpan implements ParcelableSpan {

    private final String mURL;

    public URLSpan(String url) {
        this.mURL = url;
    }

    public URLSpan(@NonNull Parcel src) {
        this.mURL = src.readString();
    }

    @Override
    public int getSpanTypeId() {
        return 11;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(this.mURL);
    }

    public String getURL() {
        return this.mURL;
    }

    @Override
    public void onClick(@NonNull View widget) {
        Core.openURI(this.mURL);
    }
}
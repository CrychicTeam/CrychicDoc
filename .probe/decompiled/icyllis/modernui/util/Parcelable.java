package icyllis.modernui.util;

import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.annotation.Nullable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.jetbrains.annotations.ApiStatus.Internal;

public interface Parcelable {

    void writeToParcel(@NonNull Parcel var1, int var2);

    @FunctionalInterface
    public interface ClassLoaderCreator<T> extends Parcelable.Creator<T> {

        @Override
        default T createFromParcel(@NonNull Parcel source) {
            return this.createFromParcel(source, null);
        }

        T createFromParcel(@NonNull Parcel var1, @Nullable ClassLoader var2);
    }

    @FunctionalInterface
    public interface Creator<T> {

        T createFromParcel(@NonNull Parcel var1);
    }

    @Retention(RetentionPolicy.SOURCE)
    @Internal
    public @interface WriteFlags {
    }
}
package icyllis.modernui.fragment;

import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.annotation.Nullable;
import icyllis.modernui.core.Context;
import icyllis.modernui.core.Handler;
import icyllis.modernui.view.View;
import java.io.FileDescriptor;
import java.io.PrintWriter;

public abstract class FragmentHostCallback<E> implements FragmentContainer {

    @NonNull
    final Context mContext;

    @NonNull
    final Handler mHandler;

    @NonNull
    final FragmentManager mFragmentManager = new FragmentManager();

    public FragmentHostCallback(@NonNull Context context, @NonNull Handler handler) {
        this.mContext = context;
        this.mHandler = handler;
    }

    public void onDump(@NonNull String prefix, @Nullable FileDescriptor fd, @NonNull PrintWriter writer, @Nullable String... args) {
    }

    @Nullable
    public abstract E onGetHost();

    @Nullable
    @Override
    public View onFindViewById(int id) {
        return null;
    }

    @Override
    public boolean onHasView() {
        return true;
    }
}
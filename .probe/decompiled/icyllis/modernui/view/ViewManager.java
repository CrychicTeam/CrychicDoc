package icyllis.modernui.view;

import icyllis.modernui.annotation.NonNull;

public interface ViewManager {

    void addView(@NonNull View var1, @NonNull ViewGroup.LayoutParams var2);

    void updateViewLayout(@NonNull View var1, @NonNull ViewGroup.LayoutParams var2);

    void removeView(@NonNull View var1);
}
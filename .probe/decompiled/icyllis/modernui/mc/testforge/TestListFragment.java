package icyllis.modernui.mc.testforge;

import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.fragment.Fragment;
import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.Paint;
import icyllis.modernui.graphics.drawable.Drawable;
import icyllis.modernui.util.DataSet;
import icyllis.modernui.view.LayoutInflater;
import icyllis.modernui.view.View;
import icyllis.modernui.view.ViewGroup;
import icyllis.modernui.widget.ArrayAdapter;
import icyllis.modernui.widget.FrameLayout;
import icyllis.modernui.widget.ListAdapter;
import icyllis.modernui.widget.ListView;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TestListFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable DataSet savedInstanceState) {
        ListView listView = new ListView(this.getContext());
        listView.setAdapter((ListAdapter) (new ArrayAdapter<>(this.getContext(), new String[] { "Apple", "Banana", "Cherry", "Grapes", "Mango", "Orange", "Strawberry", "Watermelon" })));
        listView.setDivider(new Drawable() {

            @Override
            public void draw(@Nonnull Canvas canvas) {
                Paint paint = Paint.obtain();
                paint.setRGBA(192, 192, 192, 128);
                canvas.drawRect(this.getBounds(), paint);
                paint.recycle();
            }

            @Override
            public int getIntrinsicHeight() {
                return 2;
            }
        });
        listView.setLayoutParams(new FrameLayout.LayoutParams(listView.dp(400.0F), listView.dp(300.0F), 17));
        return listView;
    }
}
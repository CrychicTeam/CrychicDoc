package icyllis.modernui.mc.testforge;

import icyllis.modernui.core.Context;
import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.view.View;
import icyllis.modernui.widget.RelativeLayout;
import javax.annotation.Nonnull;

public class TestRelativeLayout extends RelativeLayout {

    public TestRelativeLayout(Context context) {
        super(context);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(40, 20);
        lp.addRule(13);
        TestRelativeLayout.CView view = new TestRelativeLayout.CView(context);
        view.setId(1);
        view.setLayoutParams(lp);
        view.text = "First One!";
        this.addView(view);
        lp = new RelativeLayout.LayoutParams(60, 20);
        lp.addRule(2, 1);
        lp.addRule(14);
        view = new TestRelativeLayout.CView(context);
        view.setId(2);
        view.setLayoutParams(lp);
        view.text = "Second On The Top";
        this.addView(view);
        lp = new RelativeLayout.LayoutParams(-1, 40);
        lp.addRule(0, 1);
        lp.addRule(15);
        view = new TestRelativeLayout.CView(context);
        view.setId(3);
        view.setLayoutParams(lp);
        view.text = "3rd";
        this.addView(view);
    }

    private static class CView extends View {

        public String text;

        public CView(Context context) {
            super(context);
        }

        @Override
        protected void onDraw(@Nonnull Canvas canvas) {
        }
    }
}
package icyllis.modernui.mc.forge;

import icyllis.modernui.animation.Animator;
import icyllis.modernui.animation.Keyframe;
import icyllis.modernui.animation.ObjectAnimator;
import icyllis.modernui.animation.PropertyValuesHolder;
import icyllis.modernui.animation.TimeInterpolator;
import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.fragment.Fragment;
import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.GLSurfaceCanvas;
import icyllis.modernui.graphics.MathUtil;
import icyllis.modernui.graphics.Paint;
import icyllis.modernui.graphics.Rect;
import icyllis.modernui.graphics.drawable.Drawable;
import icyllis.modernui.graphics.drawable.StateListDrawable;
import icyllis.modernui.mc.ui.RectangleDrawable;
import icyllis.modernui.text.InputFilter;
import icyllis.modernui.text.method.DigitsInputFilter;
import icyllis.modernui.util.DataSet;
import icyllis.modernui.util.StateSet;
import icyllis.modernui.view.LayoutInflater;
import icyllis.modernui.view.View;
import icyllis.modernui.view.ViewGroup;
import icyllis.modernui.widget.EditText;
import icyllis.modernui.widget.FrameLayout;
import icyllis.modernui.widget.LinearLayout;
import icyllis.modernui.widget.RelativeLayout;
import icyllis.modernui.widget.ScrollView;
import icyllis.modernui.widget.SwitchButton;
import icyllis.modernui.widget.TextView;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.client.resources.language.I18n;
import net.minecraftforge.common.ForgeConfigSpec;

@Deprecated
public class CenterFragment extends Fragment implements icyllis.modernui.mc.ScreenCallback {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable DataSet savedInstanceState) {
        LinearLayout base = new LinearLayout(this.getContext());
        base.setId(16908290);
        base.setOrientation(1);
        base.setBackground(new CenterFragment.Background(base));
        int dp6 = base.dp(6.0F);
        TextView title = new TextView(this.getContext());
        title.setId(16908310);
        title.setText(I18n.get("modernui.center.title"));
        title.setTextSize(22.0F);
        title.setTextStyle(1);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-2, -2);
        params.gravity = 17;
        params.setMargins(0, base.dp(12.0F), 0, base.dp(12.0F));
        base.addView(title, params);
        LinearLayout content = new LinearLayout(this.getContext());
        content.setClipChildren(true);
        content.setOrientation(0);
        boolean xor1 = Math.random() < 0.5;
        boolean xor2 = Math.random() < 0.5;
        ScrollView left = new ScrollView(this.getContext());
        left.addView(this.createLeftPanel(), -1, -2);
        LinearLayout.LayoutParams paramsx = new LinearLayout.LayoutParams(0, -1, 1.0F);
        paramsx.setMargins(dp6, dp6, dp6, dp6);
        content.addView(left, paramsx);
        final ObjectAnimator animator = ObjectAnimator.ofFloat(left, xor2 ? View.ROTATION_Y : View.ROTATION_X, !xor1 && xor2 ? -80.0F : 80.0F, 0.0F);
        animator.setInterpolator(TimeInterpolator.DECELERATE);
        left.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {

            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                animator.start();
                v.removeOnLayoutChangeListener(this);
            }
        });
        left.setEdgeEffectColor(-3300456);
        left = new ScrollView(this.getContext());
        left.addView(this.createRightPanel(), -1, -2);
        paramsx = new LinearLayout.LayoutParams(0, -1, 1.0F);
        paramsx.setMargins(dp6, dp6, dp6, dp6);
        content.addView(left, paramsx);
        animator = ObjectAnimator.ofFloat(left, xor2 ? View.ROTATION_X : View.ROTATION_Y, xor1 && !xor2 ? -80.0F : 80.0F, 0.0F);
        animator.setInterpolator(TimeInterpolator.DECELERATE);
        left.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {

            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                animator.start();
                v.removeOnLayoutChangeListener(this);
            }
        });
        left.setEdgeEffectColor(-3300456);
        LinearLayout.LayoutParams paramsxx = new LinearLayout.LayoutParams(-1, -1);
        base.addView(content, paramsxx);
        FrameLayout.LayoutParams paramsxxx = new FrameLayout.LayoutParams(base.dp(720.0F), base.dp(450.0F));
        paramsxxx.gravity = 17;
        base.setLayoutParams(paramsxxx);
        return base;
    }

    @Nonnull
    private LinearLayout createCategory(String titleKey) {
        LinearLayout layout = new LinearLayout(this.getContext());
        layout.setOrientation(1);
        int dp6 = layout.dp(6.0F);
        int dp12 = layout.dp(12.0F);
        TextView title = new TextView(this.getContext());
        title.setId(16908310);
        title.setText(I18n.get(titleKey));
        title.setTextSize(16.0F);
        title.setTextColor(-3300456);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-2, -2);
        params.gravity = 8388611;
        params.setMargins(dp6, dp6, dp6, dp6);
        layout.addView(title, params);
        LinearLayout.LayoutParams paramsx = new LinearLayout.LayoutParams(-1, -2);
        paramsx.gravity = 17;
        paramsx.setMargins(dp12, dp12, dp12, layout.dp(18.0F));
        layout.setLayoutParams(paramsx);
        return layout;
    }

    @Nonnull
    private LinearLayout createInputOption(String titleKey) {
        LinearLayout layout = new LinearLayout(this.getContext());
        layout.setOrientation(0);
        layout.setHorizontalGravity(8388611);
        int dp3 = layout.dp(3.0F);
        int dp6 = layout.dp(6.0F);
        TextView title = new TextView(this.getContext());
        title.setText(I18n.get(titleKey));
        title.setTextAlignment(5);
        title.setTextSize(14.0F);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-2, -2, 1.0F);
        params.gravity = 8388627;
        layout.addView(title, params);
        EditText input = new EditText(this.getContext());
        input.setId(16908297);
        input.setTextAlignment(5);
        input.setTextSize(14.0F);
        input.setPadding(dp3, 0, dp3, 0);
        StateListDrawable background = new StateListDrawable();
        background.addState(StateSet.get(64), new RectangleDrawable());
        background.setEnterFadeDuration(300);
        background.setExitFadeDuration(300);
        input.setBackground(background);
        LinearLayout.LayoutParams paramsx = new LinearLayout.LayoutParams(-2, -2);
        paramsx.gravity = 16;
        layout.addView(input, paramsx);
        LinearLayout.LayoutParams paramsxx = new LinearLayout.LayoutParams(-1, -2);
        paramsxx.gravity = 17;
        paramsxx.setMargins(dp6, 0, dp6, 0);
        layout.setLayoutParams(paramsxx);
        return layout;
    }

    @Nonnull
    private LinearLayout createButtonOption(String titleKey) {
        LinearLayout layout = new LinearLayout(this.getContext());
        layout.setOrientation(0);
        layout.setHorizontalGravity(8388611);
        int dp3 = layout.dp(3.0F);
        int dp6 = layout.dp(6.0F);
        TextView title = new TextView(this.getContext());
        title.setText(I18n.get(titleKey));
        title.setTextAlignment(5);
        title.setTextSize(14.0F);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-2, -2, 1.0F);
        params.gravity = 16;
        layout.addView(title, params);
        SwitchButton button = new SwitchButton(this.getContext());
        button.setId(16908313);
        button.setCheckedColor(-3300456);
        params = new LinearLayout.LayoutParams(layout.dp(36.0F), layout.dp(16.0F));
        params.gravity = 16;
        params.setMargins(0, dp3, 0, dp3);
        layout.addView(button, params);
        LinearLayout.LayoutParams paramsx = new LinearLayout.LayoutParams(-1, -2);
        paramsx.gravity = 17;
        paramsx.setMargins(dp6, 0, dp6, 0);
        layout.setLayoutParams(paramsx);
        return layout;
    }

    @Nonnull
    private View createLeftPanel() {
        LinearLayout panel = new LinearLayout(this.getContext());
        panel.setOrientation(1);
        LinearLayout category = this.createCategory("modernui.center.category.screen");
        panel.addView(category);
        category = this.createCategory("modernui.center.category.extension");
        panel.addView(category);
        category = this.createCategory("modernui.center.category.text");
        LinearLayout option = this.createButtonOption("modernui.center.text.distanceField");
        SwitchButton button = option.requireViewById(16908313);
        button.setChecked(true);
        category.addView(option);
        option = this.createButtonOption("modernui.center.text.superSampling");
        button = option.requireViewById(16908313);
        button.setChecked(true);
        category.addView(option);
        option = this.createButtonOption("modernui.center.text.textShaping");
        option.<SwitchButton>requireViewById(16908313).setChecked(true);
        category.addView(option);
        option = this.createButtonOption("modernui.center.text.fixSurrogate");
        option.<SwitchButton>requireViewById(16908313).setChecked(true);
        category.addView(option);
        option = this.createButtonOption("modernui.center.text.fastDigitRepl");
        option.<SwitchButton>requireViewById(16908313).setChecked(true);
        category.addView(option);
        option = this.createButtonOption("modernui.center.text.fastStreamingAlgo");
        option.<SwitchButton>requireViewById(16908313).setChecked(true);
        category.addView(option);
        option = this.createButtonOption("modernui.center.text.graphemeAlgo");
        option.<SwitchButton>requireViewById(16908313).setChecked(true);
        category.addView(option);
        option = this.createButtonOption("modernui.center.text.lineBreakingAlgo");
        option.<SwitchButton>requireViewById(16908313).setChecked(true);
        category.addView(option);
        option = this.createButtonOption("modernui.center.text.substringAlgo");
        option.<SwitchButton>requireViewById(16908313).setChecked(true);
        category.addView(option);
        panel.addView(category);
        panel.setDividerDrawable(new CenterFragment.Divider(panel));
        panel.setDividerPadding(panel.dp(8.0F));
        panel.setShowDividers(7);
        return panel;
    }

    @Nonnull
    private View createRightPanel() {
        LinearLayout panel = new LinearLayout(this.getContext());
        panel.setOrientation(1);
        int dp6 = panel.dp(6.0F);
        LinearLayout category = this.createCategory("modernui.center.category.system");
        panel.addView(category);
        category = this.createCategory("modernui.center.category.font");
        panel.addView(category);
        RelativeLayout group = new RelativeLayout(this.getContext());
        TextView title = new TextView(this.getContext());
        title.setId(18);
        title.setText("View");
        title.setTextSize(16.0F);
        title.setTextColor(-3300456);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(-2, -2);
        params.addRule(10);
        params.addRule(20);
        params.setMargins(dp6, dp6, dp6, dp6);
        group.addView(title, params);
        this.addSystemSetting(20, "Scrollbar size", group, 1024, Config.CLIENT.mScrollbarSize);
        this.addSystemSetting(22, "Touch slop", group, 1024, Config.CLIENT.mTouchSlop);
        this.addSystemSetting(24, "Min scrollbar touch target", group, 1024, Config.CLIENT.mMinScrollbarTouchTarget);
        this.addSystemSetting(26, "Minimum fling velocity", group, 32767, Config.CLIENT.mMinimumFlingVelocity);
        this.addSystemSetting(28, "Maximum fling velocity", group, 32767, Config.CLIENT.mMaximumFlingVelocity);
        this.addSystemSetting(30, "Overscroll distance", group, 1024, Config.CLIENT.mOverscrollDistance);
        this.addSystemSetting(32, "Overfling distance", group, 1024, Config.CLIENT.mOverflingDistance);
        title = new TextView(this.getContext());
        title.setId(34);
        title.setText("Vertical scroll factor");
        title.setTextSize(14.0F);
        params = new RelativeLayout.LayoutParams(-2, -2);
        params.addRule(3, 32);
        params.addRule(18, 32);
        group.addView(title, params);
        EditText input = new EditText(this.getContext());
        input.setText(Config.CLIENT.mVerticalScrollFactor.get().toString());
        input.setTextAlignment(5);
        input.setTextSize(14.0F);
        input.setFilters(new InputFilter[] { DigitsInputFilter.getInstance(input.getTextLocale(), false, true), new InputFilter.LengthFilter(6) });
        input.setPadding(panel.dp(4.0F), 0, panel.dp(4.0F), 0);
        input.setOnFocusChangeListener((__, hasFocus) -> {
            if (!hasFocus) {
                double radius = Double.parseDouble(input.getText().toString());
                radius = Math.max(Math.min(radius, 1024.0), 0.0);
                input.setText(Double.toString(radius));
                if (radius != Config.CLIENT.mVerticalScrollFactor.get()) {
                    Config.CLIENT.mVerticalScrollFactor.set(Double.valueOf(radius));
                    Config.CLIENT.saveAndReloadAsync();
                }
            }
        });
        StateListDrawable drawable = new StateListDrawable();
        drawable.addState(StateSet.get(64), new RectangleDrawable());
        drawable.setEnterFadeDuration(300);
        drawable.setExitFadeDuration(300);
        input.setBackground(drawable);
        params = new RelativeLayout.LayoutParams(-2, -2);
        params.addRule(4, 34);
        params.addRule(21);
        params.setMargins(dp6, dp6, dp6, dp6);
        group.addView(input, params);
        title = new TextView(this.getContext());
        title.setId(36);
        title.setText("Horizontal scroll factor");
        title.setTextSize(14.0F);
        params = new RelativeLayout.LayoutParams(-2, -2);
        params.addRule(3, 34);
        params.addRule(18, 34);
        group.addView(title, params);
        input = new EditText(this.getContext());
        input.setText(Config.CLIENT.mHorizontalScrollFactor.get().toString());
        input.setTextAlignment(5);
        input.setTextSize(14.0F);
        input.setFilters(new InputFilter[] { DigitsInputFilter.getInstance(input.getTextLocale(), false, true), new InputFilter.LengthFilter(6) });
        input.setPadding(panel.dp(4.0F), 0, panel.dp(4.0F), 0);
        input.setOnFocusChangeListener((__, hasFocus) -> {
            if (!hasFocus) {
                double radius = Double.parseDouble(input.getText().toString());
                radius = Math.max(Math.min(radius, 1024.0), 0.0);
                input.setText(Double.toString(radius));
                if (radius != Config.CLIENT.mHorizontalScrollFactor.get()) {
                    Config.CLIENT.mHorizontalScrollFactor.set(Double.valueOf(radius));
                    Config.CLIENT.saveAndReloadAsync();
                }
            }
        });
        drawable = new StateListDrawable();
        drawable.addState(StateSet.get(64), new RectangleDrawable());
        drawable.setEnterFadeDuration(300);
        drawable.setExitFadeDuration(300);
        input.setBackground(drawable);
        params = new RelativeLayout.LayoutParams(-2, -2);
        params.addRule(4, 36);
        params.addRule(21);
        params.setMargins(dp6, dp6, dp6, dp6);
        group.addView(input, params);
        LinearLayout.LayoutParams paramsx = new LinearLayout.LayoutParams(-1, -2);
        paramsx.gravity = 17;
        paramsx.setMargins(panel.dp(12.0F), panel.dp(12.0F), panel.dp(12.0F), panel.dp(18.0F));
        panel.addView(group, paramsx);
        panel.setDividerDrawable(new CenterFragment.Divider(panel));
        panel.setDividerPadding(panel.dp(8.0F));
        panel.setShowDividers(7);
        return panel;
    }

    private void addSystemSetting(int id, String title, @Nonnull ViewGroup container, int max, @Nonnull ForgeConfigSpec.IntValue config) {
        TextView view = new TextView(this.getContext());
        view.setId(id);
        view.setText(title);
        view.setTextSize(14.0F);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(-2, -2);
        params.addRule(3, id - 2);
        params.addRule(18, id - 2);
        container.addView(view, params);
        EditText input = new EditText(this.getContext());
        input.setText(config.get().toString());
        input.setTextAlignment(5);
        input.setTextSize(14.0F);
        input.setFilters(new InputFilter[] { DigitsInputFilter.getInstance(input.getTextLocale()), new InputFilter.LengthFilter(5) });
        input.setPadding(view.dp(4.0F), 0, view.dp(4.0F), 0);
        input.setOnFocusChangeListener((__, hasFocus) -> {
            if (!hasFocus) {
                int val = Integer.parseInt(input.getText().toString());
                val = MathUtil.clamp(val, 0, max);
                input.setText(Integer.toString(val));
                if (val != config.get()) {
                    config.set(Integer.valueOf(val));
                    Config.CLIENT.saveAndReloadAsync();
                }
            }
        });
        StateListDrawable drawable = new StateListDrawable();
        drawable.addState(StateSet.get(64), new RectangleDrawable());
        drawable.setEnterFadeDuration(300);
        drawable.setExitFadeDuration(300);
        input.setBackground(drawable);
        params = new RelativeLayout.LayoutParams(-2, -2);
        params.addRule(4, id);
        params.addRule(21);
        params.setMargins(view.dp(6.0F), view.dp(6.0F), view.dp(6.0F), view.dp(6.0F));
        container.addView(input, params);
    }

    @Nullable
    @Override
    public Animator onCreateAnimator(int transit, boolean enter, int nextAnim) {
        if (enter && transit == 4097) {
            Keyframe kfStart = Keyframe.ofFloat(0.0F, 0.75F);
            Keyframe kfEnd = Keyframe.ofFloat(1.0F, 1.0F);
            kfEnd.setInterpolator(TimeInterpolator.OVERSHOOT);
            PropertyValuesHolder scaleX = PropertyValuesHolder.ofKeyframe(View.SCALE_X, kfStart, kfEnd);
            PropertyValuesHolder scaleY = PropertyValuesHolder.ofKeyframe(View.SCALE_Y, kfStart.copy(), kfEnd.copy());
            kfStart = Keyframe.ofFloat(0.0F, 0.0F);
            kfEnd = Keyframe.ofFloat(1.0F, 1.0F);
            kfEnd.setInterpolator(TimeInterpolator.DECELERATE_CUBIC);
            PropertyValuesHolder alpha = PropertyValuesHolder.ofKeyframe(View.ALPHA, kfStart, kfEnd);
            Animator animator = ObjectAnimator.ofPropertyValuesHolder(null, scaleX, scaleY, alpha);
            animator.setDuration(400L);
            animator.setInterpolator(null);
            return animator;
        } else {
            return super.onCreateAnimator(transit, enter, nextAnim);
        }
    }

    private static class Background extends Drawable {

        private final float mRadius;

        private final float mStrokeWidth;

        private Background(View view) {
            this.mRadius = (float) view.dp(8.0F);
            this.mStrokeWidth = (float) view.dp(4.0F);
        }

        @Override
        public void draw(@Nonnull Canvas canvas) {
            Paint paint = Paint.obtain();
            Rect bounds = this.getBounds();
            paint.setStyle(0);
            paint.setColor(-1071044052);
            float inner = this.mStrokeWidth * 0.5F;
            canvas.drawRoundRect((float) bounds.left + inner, (float) bounds.top + inner, (float) bounds.right - inner, (float) bounds.bottom - inner, this.mRadius, paint);
            ((GLSurfaceCanvas) canvas).drawGlowWave((float) bounds.left + inner * 1.5F, (float) bounds.top + inner * 1.5F, (float) bounds.right - inner, (float) bounds.bottom - inner);
            paint.setStyle(1);
            paint.setColor(-3300456);
            paint.setStrokeWidth(this.mStrokeWidth);
            canvas.drawRoundRect((float) bounds.left + inner, (float) bounds.top + inner, (float) bounds.right - inner, (float) bounds.bottom - inner, this.mRadius, paint);
            paint.recycle();
            this.invalidateSelf();
        }

        @Override
        public boolean getPadding(@Nonnull Rect padding) {
            int inner = (int) Math.ceil((double) (this.mStrokeWidth * 0.5F));
            padding.set(inner, inner, inner, inner);
            return true;
        }
    }

    private static class Divider extends Drawable {

        private final int mSize;

        public Divider(View view) {
            this.mSize = view.dp(2.0F);
        }

        @Override
        public void draw(@Nonnull Canvas canvas) {
            Paint paint = Paint.obtain();
            paint.setColor(-1067425696);
            canvas.drawRect(this.getBounds(), paint);
            paint.recycle();
        }

        @Override
        public int getIntrinsicHeight() {
            return this.mSize;
        }
    }
}
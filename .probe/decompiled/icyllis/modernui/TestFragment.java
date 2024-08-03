package icyllis.modernui;

import icyllis.modernui.animation.AnimationUtils;
import icyllis.modernui.animation.Animator;
import icyllis.modernui.animation.LayoutTransition;
import icyllis.modernui.animation.ObjectAnimator;
import icyllis.modernui.animation.PropertyValuesHolder;
import icyllis.modernui.animation.TimeInterpolator;
import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.annotation.Nullable;
import icyllis.modernui.audio.AudioManager;
import icyllis.modernui.audio.FFT;
import icyllis.modernui.audio.Track;
import icyllis.modernui.core.Context;
import icyllis.modernui.core.Core;
import icyllis.modernui.fragment.Fragment;
import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.MathUtil;
import icyllis.modernui.graphics.Paint;
import icyllis.modernui.graphics.Rect;
import icyllis.modernui.graphics.drawable.Drawable;
import icyllis.modernui.graphics.font.GlyphManager;
import icyllis.modernui.graphics.text.FontFamily;
import icyllis.modernui.material.MaterialCheckBox;
import icyllis.modernui.material.MaterialRadioButton;
import icyllis.modernui.text.PrecomputedText;
import icyllis.modernui.text.Spannable;
import icyllis.modernui.text.SpannableString;
import icyllis.modernui.text.TextPaint;
import icyllis.modernui.text.TextUtils;
import icyllis.modernui.text.style.ForegroundColorSpan;
import icyllis.modernui.text.style.RelativeSizeSpan;
import icyllis.modernui.text.style.StrikethroughSpan;
import icyllis.modernui.text.style.StyleSpan;
import icyllis.modernui.text.style.SuperscriptSpan;
import icyllis.modernui.text.style.URLSpan;
import icyllis.modernui.text.style.UnderlineSpan;
import icyllis.modernui.util.DataSet;
import icyllis.modernui.util.FloatProperty;
import icyllis.modernui.view.KeyEvent;
import icyllis.modernui.view.LayoutInflater;
import icyllis.modernui.view.MenuItem;
import icyllis.modernui.view.SubMenu;
import icyllis.modernui.view.View;
import icyllis.modernui.view.ViewGroup;
import icyllis.modernui.widget.ArrayAdapter;
import icyllis.modernui.widget.Button;
import icyllis.modernui.widget.CheckBox;
import icyllis.modernui.widget.EditText;
import icyllis.modernui.widget.FrameLayout;
import icyllis.modernui.widget.LinearLayout;
import icyllis.modernui.widget.PopupWindow;
import icyllis.modernui.widget.RadioButton;
import icyllis.modernui.widget.RadioGroup;
import icyllis.modernui.widget.ScrollView;
import icyllis.modernui.widget.SeekBar;
import icyllis.modernui.widget.Spinner;
import icyllis.modernui.widget.SpinnerAdapter;
import icyllis.modernui.widget.SwitchButton;
import icyllis.modernui.widget.TextView;
import icyllis.modernui.widget.Toast;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import javax.annotation.Nonnull;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;

public class TestFragment extends Fragment {

    public static void main(String[] args) {
        System.setProperty("java.awt.headless", "true");
        Configurator.setRootLevel(Level.DEBUG);
        try (ModernUI app = new ModernUI()) {
            app.run(new TestFragment());
        }
        AudioManager.getInstance().close();
        System.gc();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.getParentFragmentManager().beginTransaction().setPrimaryNavigationFragment(this).commit();
    }

    @Override
    public void onCreate(@Nullable DataSet savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getChildFragmentManager().beginTransaction().replace(660, new TestFragment.FragmentA(), null).commit();
        AudioManager.getInstance().initialize();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable DataSet savedInstanceState) {
        ScrollView base = new ScrollView(this.getContext());
        base.setId(660);
        base.setBackground(new Drawable() {

            long lastTime = AnimationUtils.currentAnimationTimeMillis();

            @Override
            public void draw(@NonNull Canvas canvas) {
                Paint paint = Paint.obtain();
                Rect b = this.getBounds();
                paint.setRGBA(8, 8, 8, 80);
                canvas.drawRoundRect((float) b.left, (float) b.top, (float) b.right, (float) b.bottom, 8.0F, paint);
                paint.recycle();
            }
        });
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(base.dp(640.0F), base.dp(360.0F));
        params.gravity = 17;
        base.setLayoutParams(params);
        container.setClipChildren(true);
        return base;
    }

    public static class FragmentA extends Fragment {

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable DataSet savedInstanceState) {
            LinearLayout content = new TestFragment.TestLinearLayout(this.getContext());
            content.setLayoutParams(new FrameLayout.LayoutParams(-1, -2));
            content.setOnKeyListener((v, keyCode, event) -> {
                if (keyCode == 69 && event.getAction() == 1) {
                    Core.postOnRenderThread(() -> GlyphManager.getInstance().debug());
                    return true;
                } else {
                    return false;
                }
            });
            ModernUI.LOGGER.info("{} onCreateView(), id={}", this.getClass().getSimpleName(), this.getId());
            return content;
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            ModernUI.LOGGER.info("{} onDestroy()", this.getClass().getSimpleName());
        }
    }

    public static class FragmentB extends Fragment {

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable DataSet savedInstanceState) {
            TextView tv = new TextView(this.getContext());
            tv.setText("My name is Van, I'm an arist, a performance artist.");
            return tv;
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            ModernUI.LOGGER.info("FragmentB onDestroy()");
        }
    }

    public static class SpectrumGraph {

        private final boolean mCircular;

        private final float[] mAmplitudes = new float[60];

        private final FFT mFFT;

        private final int mHeight;

        private boolean mUpdated;

        public SpectrumGraph(Track track, View view, boolean circular, int height) {
            this.mFFT = FFT.create(1024, track.getSampleRate());
            this.mFFT.setLogAverages(250, 14);
            this.mFFT.setWindowFunc(3);
            track.setAnalyzer(this.mFFT, f -> {
                this.updateAmplitudes();
                view.postInvalidate();
            });
            this.mCircular = circular;
            this.mHeight = height;
        }

        public void updateAmplitudes() {
            int len = Math.min(this.mFFT.getAverageSize() - 5, this.mAmplitudes.length);
            long time = Core.timeMillis();
            int iOff;
            if (this.mCircular) {
                iOff = (int) (time / 200L);
            } else {
                iOff = 0;
            }
            synchronized (this.mAmplitudes) {
                for (int i = 0; i < len; i++) {
                    float va = this.mFFT.getAverage((i + iOff) % len + 5) / (float) this.mFFT.getBandSize();
                    this.mAmplitudes[i] = Math.max(this.mAmplitudes[i], va);
                }
                this.mUpdated = true;
            }
        }

        public boolean update(long delta) {
            int len = Math.min(this.mFFT.getAverageSize() - 5, this.mAmplitudes.length);
            synchronized (this.mAmplitudes) {
                for (int i = 0; i < len; i++) {
                    this.mAmplitudes[i] = Math.max(0.0F, this.mAmplitudes[i] - (float) delta * 2.5E-5F * 198.0F * (this.mAmplitudes[i] + 0.03F));
                }
                boolean updated = this.mUpdated;
                this.mUpdated = false;
                if (!updated) {
                    for (int i = 0; i < len; i++) {
                        if (this.mAmplitudes[i] > 0.0F) {
                            return true;
                        }
                    }
                }
                return updated;
            }
        }

        public void draw(@Nonnull Canvas canvas, float cx, float cy) {
            Paint paint = Paint.obtain();
            if (this.mCircular) {
                long time = Core.timeMillis();
                float b = 1.5F + MathUtil.sin((float) time / 600.0F) / 2.0F;
                paint.setRGBA(160, 155, 230, (int) (64.0F * b));
                paint.setStrokeWidth(200.0F);
                paint.setSmoothWidth(200.0F);
                paint.setStyle(1);
                canvas.drawCircle(cx, cy, 130.0F, paint);
                paint.reset();
                for (int i = 0; i < this.mAmplitudes.length; i++) {
                    float f = Math.abs((float) ((i + (int) (time / 100L)) % this.mAmplitudes.length) - (float) (this.mAmplitudes.length - 1) / 2.0F) / (float) (this.mAmplitudes.length - 1) * b;
                    paint.setRGBA(100 + (int) (f * 120.0F), 220 - (int) (f * 130.0F), 240 - (int) (f * 20.0F), 255);
                    canvas.rotate(-360.0F / (float) this.mAmplitudes.length, cx, cy);
                    canvas.drawRect(cx - 6.0F, cy - 120.0F - this.mAmplitudes[i] * (float) this.mHeight, cx + 6.0F, cy - 120.0F, paint);
                }
            } else {
                for (int i = 0; i < this.mAmplitudes.length; i++) {
                    paint.setRGBA(100 + i * 2, 220 - i * 2, 240 - i * 4, 255);
                    canvas.drawRect(cx - 479.0F + (float) (i * 16), cy - this.mAmplitudes[i] * (float) this.mHeight, cx - 465.0F + (float) (i * 16), cy, paint);
                }
            }
            paint.recycle();
        }
    }

    public static class TestLinearLayout extends LinearLayout {

        private float c = 10.0F;

        private float f = 0.0F;

        private static final FloatBuffer sLinePoints = FloatBuffer.allocate(16);

        private static final IntBuffer sLineColors = IntBuffer.allocate(sLinePoints.capacity() / 2);

        private static final FloatBuffer sTrianglePoints = FloatBuffer.allocate(12);

        private static final IntBuffer sTriangleColors = IntBuffer.allocate(sTrianglePoints.capacity() / 2);

        private final Animator mRoundRectLenAnim;

        private float circleAcc1;

        private float circleAcc2;

        private float circleAcc3;

        private float circleAcc4;

        private float iconRadius = 40.0F;

        private float arcStart = 0.0F;

        private float arcEnd = 0.0F;

        private float mRoundRectLen = 0.0F;

        private float roundRectAlpha = 0.0F;

        private float mSmoothRadius = 0.0F;

        private boolean b;

        private int ticks;

        private final TextView mTextView;

        PopupWindow mPopupWindow = new PopupWindow();

        ObjectAnimator mGoodAnim;

        private static final FloatProperty<TestFragment.TestLinearLayout> sRoundRectLengthProp = new FloatProperty<TestFragment.TestLinearLayout>("roundRectLen") {

            public void setValue(@Nonnull TestFragment.TestLinearLayout object, float value) {
                object.mRoundRectLen = value;
            }

            public Float get(@Nonnull TestFragment.TestLinearLayout object) {
                return object.mRoundRectLen;
            }
        };

        private static final FloatProperty<TestFragment.TestLinearLayout> sSmoothRadiusProp = new FloatProperty<TestFragment.TestLinearLayout>("smoothRadius") {

            public void setValue(@Nonnull TestFragment.TestLinearLayout object, float value) {
                object.mSmoothRadius = value;
            }

            public Float get(@Nonnull TestFragment.TestLinearLayout object) {
                return object.mSmoothRadius;
            }
        };

        public TestLinearLayout(Context context) {
            super(context);
            this.setOrientation(1);
            this.setGravity(17);
            this.setDividerDrawable(new Drawable() {

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
            this.setShowDividers(6);
            this.setPadding(this.dp(12.0F), this.dp(12.0F), this.dp(12.0F), this.dp(12.0F));
            this.setDividerPadding(this.dp(8.0F));
            this.setClickable(true);
            this.setFocusable(true);
            this.setFocusableInTouchMode(true);
            String text = "\t\tعندما يريد العالم أن \u202aيتكلّم \u202c ، فهو يتحدّث بلغة يونيكود. تسجّل الآن لحضور المؤتمر الدولي العاشر ليونيكود (Unicode Conference)\n";
            int firstPara = text.length();
            text = text + "\t\t红 日（迫真）\n";
            int secondsPara = text.length();
            text = text + "\t\tMy name is Van, I'm 30 years old, and I'm from Japan. I'm an artist, I'm a performance artist. I'm hired for people to fulfill their fantasies, their deep dark fantasies.\n\t\t你看这个彬彬 才喝几罐 就醉了...真的太逊力；哦，听你那么说 你很勇哦；开玩笑，我超勇的好不好 我超会喝的啦\n";
            text = text + "Oops, your ";
            int emojiSt = text.length();
            text = text + "\ud83d\udc34 died\n";
            text = text + "\t\tহ্যালো مرحبا \ud808\udd99\ud808\udd99";
            TextView tv = new TextView(this.getContext());
            tv.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
            tv.setLineBreakWordStyle(3);
            Spannable spannable = new SpannableString(text);
            spannable.setSpan(new ForegroundColorSpan(-616012), text.length() - 54, text.length(), 33);
            spannable.setSpan(new RelativeSizeSpan(1.15F), text.length() - 99, text.length() - 30, 33);
            spannable.setSpan(new StyleSpan(1), text.length() - 50, text.length() - 40, 33);
            spannable.setSpan(new URLSpan("https://www.bilibili.com/video/BV1HA41147a4"), firstPara, secondsPara - 1, 33);
            spannable.setSpan(new ForegroundColorSpan(-11566659), firstPara, secondsPara - 1, 33);
            spannable.setSpan(new SuperscriptSpan(), firstPara + 4, firstPara + 5, 33);
            spannable.setSpan(new UnderlineSpan(), text.length() / 2, text.length() / 4 * 3, 33);
            spannable.setSpan(new StrikethroughSpan(), text.length() / 4 * 3, text.length(), 34);
            CompletableFuture.runAsync(() -> {
                long startNanos = System.nanoTime();
                PrecomputedText precomputed = PrecomputedText.create(spannable, tv.getTextMetricsParams());
                long usedNanos = System.nanoTime() - startNanos;
                ModernUI.LOGGER.info("Precomputed text in {} microseconds", usedNanos / 1000L);
                tv.post(() -> tv.setText(precomputed, TextView.BufferType.SPANNABLE));
            });
            tv.setLinksClickable(true);
            tv.setTextIsSelectable(true);
            tv.setTextAlignment(1);
            this.mTextView = tv;
            PropertyValuesHolder pvh1 = PropertyValuesHolder.ofFloat(ROTATION, 0.0F, 2880.0F);
            PropertyValuesHolder pvh2 = PropertyValuesHolder.ofFloat(ROTATION_Y, 0.0F, 720.0F);
            PropertyValuesHolder pvh3 = PropertyValuesHolder.ofFloat(ROTATION_X, 0.0F, 1440.0F);
            ObjectAnimator anim = ObjectAnimator.ofPropertyValuesHolder(this, pvh1, pvh2, pvh3);
            anim.setDuration(12000L);
            anim.setInterpolator(TimeInterpolator.ACCELERATE_DECELERATE);
            this.mGoodAnim = anim;
            for (int i = 0; i < 12; i++) {
                View v;
                LinearLayout.LayoutParams p;
                if (i == 1) {
                    Button button = new Button(this.getContext());
                    button.setText("Play A Music!");
                    button.setTextColor(-14113805);
                    button.setTextStyle(1);
                    button.setOnClickListener(__ -> {
                        if (this.mGoodAnim != null) {
                            this.mGoodAnim.start();
                        }
                    });
                    v = button;
                    p = new LinearLayout.LayoutParams(-2, -2);
                } else if (i == 4) {
                    SwitchButton switchButton = new SwitchButton(this.getContext());
                    v = switchButton;
                    switchButton.setOnCheckedChangeListener((buttonx, checked) -> {
                        if (checked) {
                            buttonx.post(() -> this.addView(this.mTextView, 2));
                        } else {
                            buttonx.post(() -> this.removeView(this.mTextView));
                        }
                    });
                    p = new LinearLayout.LayoutParams(this.dp(50.0F), this.dp(18.0F));
                } else {
                    if (i == 2) {
                        continue;
                    }
                    if (i == 3) {
                        EditText textField = new EditText(this.getContext());
                        v = textField;
                        p = new LinearLayout.LayoutParams(-1, -2);
                        textField.setHint("Your Name");
                        textField.setTextSize(16.0F);
                        textField.setTextAlignment(5);
                        textField.setPadding(this.dp(12.0F), 0, this.dp(12.0F), 0);
                    } else if (i == 10) {
                        LinearLayout layout = new LinearLayout(this.getContext());
                        layout.setOrientation(0);
                        layout.setHorizontalGravity(8388611);
                        int dp3 = this.dp(3.0F);
                        int dp6 = this.dp(6.0F);
                        TextView title = new TextView(this.getContext());
                        title.setText("Title");
                        title.setTextSize(14.0F);
                        title.setTextAlignment(5);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-2, -2, 1.0F);
                        params.gravity = 8388611;
                        layout.addView(title, params);
                        EditText input = new EditText(this.getContext());
                        input.setId(16908297);
                        input.setTextAlignment(6);
                        input.setTextSize(14.0F);
                        input.setPadding(dp3, 0, dp3, 0);
                        input.setText("Value");
                        params = new LinearLayout.LayoutParams(-2, -2);
                        params.gravity = 16;
                        layout.addView(input, params);
                        LinearLayout.LayoutParams paramsx = new LinearLayout.LayoutParams(-1, -2);
                        paramsx.gravity = 17;
                        paramsx.setMargins(dp6, 0, dp6, 0);
                        v = layout;
                        p = paramsx;
                    } else if (i == 5) {
                        RadioGroup group = new RadioGroup(this.getContext());
                        v = group;
                        for (int j = 0; j < 3; j++) {
                            RadioButton button = new MaterialRadioButton(this.getContext());
                            button.setText("Item " + j);
                            button.setTextSize(16.0F);
                            button.setId(9 + j);
                            group.addView(button);
                        }
                        group.setOnCheckedChangeListener((__, checkedId) -> Toast.makeText(context, "You checked " + checkedId, 0).show());
                        p = new LinearLayout.LayoutParams(-2, -2);
                    } else if (i == 6) {
                        CheckBox checkBox = new MaterialCheckBox(this.getContext());
                        v = checkBox;
                        checkBox.setText("Checkbox 0");
                        checkBox.setTextSize(16.0F);
                        checkBox.setGravity(8388613);
                        checkBox.setTooltipText("Hello, this is a tooltip.");
                        p = new LinearLayout.LayoutParams(-2, -2);
                    } else if (i == 7) {
                        Spinner spinner = new Spinner(this.getContext());
                        v = spinner;
                        ArrayList<String> list = new ArrayList(FontFamily.getSystemFontMap().keySet());
                        list.sort(null);
                        spinner.setAdapter((SpinnerAdapter) (new ArrayAdapter(this.getContext(), list)));
                        p = new LinearLayout.LayoutParams(-2, -2);
                        spinner.setMinimumWidth(this.dp(240.0F));
                    } else if (i == 11) {
                        SeekBar seekbar = new SeekBar(this.getContext());
                        seekbar.setMax(20);
                        v = seekbar;
                        p = new LinearLayout.LayoutParams(this.dp(200.0F), -2);
                    } else {
                        v = new TestFragment.TestLinearLayout.CView(this.getContext(), i);
                        p = new LinearLayout.LayoutParams(this.dp(200.0F), this.dp(50.0F));
                    }
                }
                if (i == 8) {
                    v.setOnCreateContextMenuListener((menu, v1, menuInfo) -> {
                        menu.setQwertyMode(true);
                        menu.setGroupDividerEnabled(true);
                        MenuItem item = menu.add(2, 0, 0, "Align start");
                        item.setAlphabeticShortcut('s', KeyEvent.META_CTRL_ON);
                        item.setChecked(true);
                        item = menu.add(2, 0, 0, "Align center");
                        item.setAlphabeticShortcut('d', KeyEvent.META_CTRL_ON);
                        item = menu.add(2, 0, 0, "Align end");
                        item.setAlphabeticShortcut('f', KeyEvent.META_CTRL_ON);
                        menu.setGroupCheckable(2, true, true);
                        SubMenu subMenu = menu.addSubMenu("New");
                        subMenu.add("Document");
                        subMenu.add("Image");
                        menu.add(1, 0, 0, "Delete");
                    });
                }
                v.setClickable(true);
                p.gravity = 17;
                this.addView(v, p);
            }
            this.addView(new TestFragment.TestLinearLayout.DView(this.getContext(), TimeInterpolator.DECELERATE), new LinearLayout.LayoutParams(this.dp(120.0F), this.dp(40.0F)));
            anim = ObjectAnimator.ofFloat(this, sRoundRectLengthProp, 0.0F, 80.0F);
            anim.setDuration(400L);
            anim.setInterpolator(TimeInterpolator.OVERSHOOT);
            anim.addUpdateListener(a -> this.invalidate());
            this.mRoundRectLenAnim = anim;
            this.setLayoutTransition(new LayoutTransition());
        }

        @Override
        protected void onDraw(@Nonnull Canvas canvas) {
            super.onDraw(canvas);
            Paint paint = Paint.obtain();
            paint.setColor(-3300456);
            paint.setStyle(0);
            canvas.drawRoundRect(6.0F, 90.0F, 46.0F, 104.0F, 7.0F, paint);
            paint.setStyle(1);
            paint.setStrokeWidth(4.0F);
            canvas.save();
            canvas.rotate(-45.0F);
            canvas.drawRoundRect(6.0F, 110.0F, 86.0F, 124.0F, 6.0F, paint);
            paint.setStyle(0);
            canvas.drawRect(6.0F, 126.0F, 86.0F, 156.0F, paint);
            canvas.restore();
            canvas.drawLine(560.0F, 20.0F, 600.0F, 100.0F, 10.0F, paint);
            canvas.drawLineListMesh(sLinePoints, sLineColors, paint);
            canvas.drawTriangleListMesh(sTrianglePoints, sTriangleColors, paint);
            paint.setStyle(1);
            canvas.drawPie(100.0F, 200.0F, 50.0F, 60.0F, 120.0F, paint);
            float s1 = (float) Math.sin((double) AnimationUtils.currentAnimationTimeMillis() / 300.0);
            canvas.drawPie(350.0F, 94.0F, 55.0F, 180.0F + 20.0F * s1, 100.0F + 50.0F * s1 * s1, paint);
            paint.setStrokeWidth(10.0F);
            canvas.drawRect(200.0F, 300.0F, 500.0F, 400.0F, paint);
            paint.setStrokeCap(8);
            canvas.drawRect(200.0F, 450.0F, 500.0F, 550.0F, paint);
            paint.setStrokeWidth(40.0F);
            paint.setSmoothWidth(40.0F);
            canvas.drawArc(80.0F, 400.0F, 50.0F, 60.0F, 240.0F, paint);
            canvas.drawBezier(80.0F, 400.0F, 180.0F, 420.0F, 80.0F, 600.0F, paint);
            paint.setStyle(0);
            canvas.drawCircle(80.0F, 700.0F, 60.0F, paint);
            paint.setSmoothWidth(0.0F);
            paint.setStyle(0);
            paint.setAlpha((int) (this.roundRectAlpha * 192.0F));
            canvas.drawRoundRect(20.0F, 480.0F, 20.0F + this.mRoundRectLen * 1.6F, 480.0F + this.mRoundRectLen, 10.0F, paint);
            paint.setAlpha(255);
            paint.recycle();
        }

        protected boolean onMousePressed(double mouseX, double mouseY, int mouseButton) {
            return true;
        }

        protected boolean onMouseReleased(double mouseX, double mouseY, int mouseButton) {
            this.f = 1.0F;
            return true;
        }

        public void tick() {
        }

        static {
            sLinePoints.put(100.0F).put(100.0F).put(110.0F).put(200.0F).put(120.0F).put(100.0F).put(130.0F).put(300.0F).put(140.0F).put(100.0F).put(150.0F).put(400.0F).put(160.0F).put(100.0F).put(170.0F).put(500.0F).flip();
            sLineColors.put(-1426128896).put(-65281).put(-1442840321).put(-16711936).put(-1442775041).put(-16711936).put(-1426063616).put(-1).flip();
            sTrianglePoints.put(420.0F).put(20.0F).put(420.0F).put(100.0F).put(490.0F).put(60.0F).put(300.0F).put(130.0F).put(250.0F).put(180.0F).put(350.0F).put(180.0F).flip();
            sTriangleColors.put(-1426128896).put(-65281).put(-1442840321).put(-1442775041).put(-16711936).put(-1426063616).flip();
        }

        private static class CView extends View {

            private final String mIndex;

            TextPaint mTextPaint = new TextPaint();

            public CView(Context context, int index) {
                super(context);
                this.mIndex = Integer.toString(index);
            }

            @Override
            protected void onDraw(@Nonnull Canvas canvas) {
                if (this.isHovered()) {
                    Paint paint = Paint.obtain();
                    paint.setARGB(128, 140, 200, 240);
                    canvas.drawRoundRect(0.0F, 1.0F, (float) this.getWidth(), (float) (this.getHeight() - 2), 4.0F, paint);
                    TextUtils.drawTextRun(canvas, this.mIndex, 0, this.mIndex.length(), 0, this.mIndex.length(), 20.0F, (float) (this.getHeight() >> 1), false, this.mTextPaint);
                    paint.recycle();
                }
            }

            @Override
            public void onHoverChanged(boolean hovered) {
                super.onHoverChanged(hovered);
                this.invalidate();
            }
        }

        private static class DView extends View {

            private float offsetY;

            private final TextPaint mTextPaint = new TextPaint();

            private int mTicks;

            private final ObjectAnimator mAnimator;

            public DView(Context context, TimeInterpolator interpolator) {
                super(context);
                this.mTextPaint.setTextSize(10.0F);
                PropertyValuesHolder pvh1 = PropertyValuesHolder.ofFloat(ROTATION, 0.0F, 360.0F);
                PropertyValuesHolder pvh2 = PropertyValuesHolder.ofFloat(SCALE_X, 1.0F, 0.2F);
                PropertyValuesHolder pvh3 = PropertyValuesHolder.ofFloat(SCALE_Y, 1.0F, 0.2F);
                PropertyValuesHolder pvh4 = PropertyValuesHolder.ofFloat(TRANSLATION_X, 0.0F, 60.0F);
                PropertyValuesHolder pvh5 = PropertyValuesHolder.ofFloat(TRANSLATION_Y, 0.0F, -180.0F);
                PropertyValuesHolder pvh6 = PropertyValuesHolder.ofFloat(ALPHA, 1.0F, 0.0F);
                this.mAnimator = ObjectAnimator.ofPropertyValuesHolder(this, pvh1, pvh2, pvh3, pvh4, pvh5, pvh6);
                this.mAnimator.setRepeatCount(1);
                this.mAnimator.setRepeatMode(2);
                this.mAnimator.setDuration(3000L);
                this.setClickable(true);
            }

            @Override
            protected void onDraw(@Nonnull Canvas canvas) {
                Paint paint = Paint.obtain();
                paint.setARGB(128, 140, 200, 240);
                canvas.drawRoundRect(0.0F, 1.0F, (float) this.getWidth(), (float) (this.getHeight() - 2), 4.0F, paint);
                TextUtils.drawTextRun(canvas, "18:52", 0, 5, 0, 5, (float) this.getWidth() / 2.0F, this.offsetY + 24.0F, false, this.mTextPaint);
                paint.recycle();
            }

            @Override
            public boolean performClick() {
                this.mAnimator.start();
                return super.performClick();
            }
        }
    }

    private static class TestView extends View {

        public TestView(Context context) {
            super(context);
        }

        @Override
        protected void onDraw(@NonNull Canvas canvas) {
        }
    }
}
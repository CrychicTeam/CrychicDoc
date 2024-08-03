package icyllis.modernui.mc.testforge;

import com.ibm.icu.text.DateFormat;
import com.ibm.icu.text.SimpleDateFormat;
import icyllis.modernui.animation.Animator;
import icyllis.modernui.animation.AnimatorListener;
import icyllis.modernui.animation.LayoutTransition;
import icyllis.modernui.animation.ObjectAnimator;
import icyllis.modernui.animation.TimeInterpolator;
import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.core.Context;
import icyllis.modernui.fragment.Fragment;
import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.Image;
import icyllis.modernui.graphics.Paint;
import icyllis.modernui.graphics.Rect;
import icyllis.modernui.graphics.drawable.Drawable;
import icyllis.modernui.mc.ContainerDrawHelper;
import icyllis.modernui.text.SpannableString;
import icyllis.modernui.text.TextPaint;
import icyllis.modernui.text.style.ForegroundColorSpan;
import icyllis.modernui.util.DataSet;
import icyllis.modernui.util.FloatProperty;
import icyllis.modernui.util.IntProperty;
import icyllis.modernui.view.LayoutInflater;
import icyllis.modernui.view.View;
import icyllis.modernui.view.ViewGroup;
import icyllis.modernui.widget.EditText;
import icyllis.modernui.widget.FrameLayout;
import icyllis.modernui.widget.LinearLayout;
import icyllis.modernui.widget.Toast;
import java.util.Date;
import java.util.Locale;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class TestPauseFragment extends Fragment {

    public static final int NETWORK_COLOR = -14066038;

    private Image mButtonIcon;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable DataSet savedInstanceState) {
        LinearLayout content = new LinearLayout(this.getContext());
        content.setOrientation(1);
        LinearLayout navigation = new LinearLayout(this.getContext());
        navigation.setOrientation(0);
        navigation.setHorizontalGravity(1);
        navigation.setLayoutTransition(new LayoutTransition());
        if (this.mButtonIcon == null) {
            this.mButtonIcon = Image.create("modernui", "gui/gui_icon.png");
        }
        for (int i = 0; i < 8; i++) {
            TestPauseFragment.NavigationButton button = new TestPauseFragment.NavigationButton(this.getContext(), this.mButtonIcon, i * 32);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(navigation.dp(32.0F), navigation.dp(32.0F));
            button.setClickable(true);
            params.setMarginsRelative(i == 7 ? 26 : 2, 2, 2, 6);
            if (i != 0 && i != 7) {
                int index = i;
                content.postDelayed(() -> navigation.addView(button, index, params), (long) (i * 50));
            } else {
                navigation.addView(button, params);
            }
            if (i == 2) {
                button.setOnClickListener(__ -> {
                    DateFormat dateFormat = SimpleDateFormat.getDateInstance(0, Locale.CHINA);
                    Toast.makeText(__.getContext(), "Hello, Toast! " + dateFormat.format(new Date()), 1).show();
                });
            }
            if (i == 3) {
                button.setOnClickListener(__ -> {
                    String s = "Your request was rejected by the server.";
                    SpannableString text = new SpannableString(s);
                    text.setSpan(new ForegroundColorSpan(-3205867), 0, s.length(), 33);
                    Toast.makeText(__.getContext(), text, 0).show();
                });
            }
        }
        content.addView(navigation, new LinearLayout.LayoutParams(-1, -2));
        LinearLayout tab = new LinearLayout(this.getContext());
        tab.setOrientation(1);
        tab.setLayoutTransition(new LayoutTransition());
        tab.setBackground(new TestPauseFragment.TabBackground(tab));
        for (int i = 0; i < 3; i++) {
            EditText v = new EditText(this.getContext());
            v.setText(switch(i) {
                case 0 ->
                    "Flux Point";
                case 1 ->
                    "0";
                default ->
                    "800000";
            });
            v.setHint(switch(i) {
                case 0 ->
                    "Flux Point";
                case 1 ->
                    "Priority";
                default ->
                    "Transfer Limit";
            });
            v.setSingleLine();
            v.setBackground(new TestPauseFragment.TextFieldBackground(v));
            v.setTextSize(16.0F);
            v.setCompoundDrawablesRelativeWithIntrinsicBounds(new TestPauseFragment.TextFieldStart(v, this.mButtonIcon, ((i + 1) % 3 + 1) * 64), null, null, null);
            v.setTextAlignment(5);
            LinearLayout.LayoutParams paramsx = new LinearLayout.LayoutParams(-1, -2);
            paramsx.setMargins(navigation.dp(20.0F), navigation.dp(i == 0 ? 50.0F : 2.0F), navigation.dp(20.0F), navigation.dp(2.0F));
            content.postDelayed(() -> tab.addView(v, params), (long) ((i + 1) * 100));
        }
        TestPauseFragment.ConnectorView v = new TestPauseFragment.ConnectorView(this.getContext(), this.mButtonIcon);
        LinearLayout.LayoutParams paramsx = new LinearLayout.LayoutParams(-1, -1);
        paramsx.setMargins(navigation.dp(8.0F), navigation.dp(2.0F), navigation.dp(8.0F), navigation.dp(8.0F));
        content.postDelayed(() -> tab.addView(v, params), 400L);
        int tabSize = navigation.dp(340.0F);
        content.addView(tab, new LinearLayout.LayoutParams(tabSize, tabSize));
        content.setLayoutParams(new FrameLayout.LayoutParams(-2, -2, 17));
        return content;
    }

    private static class ConnectorView extends View {

        private final Image mImage;

        private final int mSize;

        private float mRodLength;

        private final Paint mBoxPaint = new Paint();

        private final ObjectAnimator mRodAnimator;

        private final ObjectAnimator mBoxAnimator;

        private final ItemStack mItem = Items.DIAMOND_BLOCK.getDefaultInstance();

        public ConnectorView(Context context, Image image) {
            super(context);
            this.mImage = image;
            this.mSize = this.dp(32.0F);
            this.mRodAnimator = ObjectAnimator.ofFloat(this, new FloatProperty<TestPauseFragment.ConnectorView>("rodLength") {

                public void setValue(@Nonnull TestPauseFragment.ConnectorView object, float value) {
                    object.mRodLength = value;
                    ConnectorView.this.invalidate();
                }

                public Float get(@Nonnull TestPauseFragment.ConnectorView object) {
                    return object.mRodLength;
                }
            }, 0.0F, (float) this.dp(32.0F));
            this.mRodAnimator.setInterpolator(TimeInterpolator.DECELERATE);
            this.mRodAnimator.setDuration(400L);
            this.mRodAnimator.addListener(new AnimatorListener() {

                @Override
                public void onAnimationEnd(@Nonnull Animator animation) {
                    ConnectorView.this.mBoxAnimator.start();
                }
            });
            this.mBoxAnimator = ObjectAnimator.ofInt(this.mBoxPaint, new IntProperty<Paint>("boxAlpha") {

                public void setValue(@Nonnull Paint object, int value) {
                    object.setAlpha(value);
                    ConnectorView.this.invalidate();
                }

                public Integer get(@Nonnull Paint object) {
                    return object.getColor() >>> 24;
                }
            }, 0, 128);
            this.mRodAnimator.setInterpolator(TimeInterpolator.LINEAR);
            this.mBoxAnimator.setDuration(400L);
            this.mBoxPaint.setRGBA(64, 64, 64, 0);
        }

        @Override
        protected void onAttachedToWindow() {
            super.onAttachedToWindow();
            this.mRodAnimator.start();
        }

        @Override
        protected void onDraw(@Nonnull Canvas canvas) {
            Paint paint = Paint.obtain();
            paint.setColor(-14066038);
            paint.setAlpha(192);
            float centerX = (float) this.getWidth() / 2.0F;
            float centerY = (float) this.getHeight() / 2.0F;
            int boxAlpha = this.mBoxPaint.getColor() >>> 24;
            float px1l = centerX - 0.234375F * (float) this.mSize;
            float py1 = centerY + 0.125F * (float) this.mSize;
            canvas.save();
            canvas.rotate(22.5F, px1l, py1);
            canvas.drawLine(px1l, py1, px1l - this.mRodLength * 2.0F, py1, (float) this.mSize / 8.0F, paint);
            canvas.restore();
            if (boxAlpha > 0) {
                canvas.drawRect(px1l - (float) this.mSize * 2.9F, py1 - (float) this.mSize * 1.1F, px1l - (float) this.mSize * 1.9F, py1 - (float) this.mSize * 0.1F, this.mBoxPaint);
            }
            float px1r = centerX + 0.234375F * (float) this.mSize;
            canvas.save();
            canvas.rotate(-22.5F, px1r, py1);
            canvas.drawLine(px1r, py1, px1r + this.mRodLength * 2.0F, py1, (float) this.mSize / 8.0F, paint);
            canvas.restore();
            if (boxAlpha > 0) {
                canvas.drawRect(px1r + (float) this.mSize * 1.9F, py1 - (float) this.mSize * 1.1F, px1r + (float) this.mSize * 2.9F, py1 - (float) this.mSize * 0.1F, this.mBoxPaint);
            }
            float py2 = centerY + 0.296875F * (float) this.mSize;
            canvas.drawLine(centerX, py2, centerX, py2 + this.mRodLength, (float) this.mSize / 8.0F, paint);
            if (boxAlpha > 0) {
                canvas.drawRect(centerX - (float) this.mSize * 0.5F, py2 + (float) this.mSize * 1.1F, centerX + (float) this.mSize * 0.5F, py2 + (float) this.mSize * 2.1F, this.mBoxPaint);
            }
            float offset = (float) this.mSize / 2.0F;
            canvas.drawImage(this.mImage, 0.0F, 192.0F, 64.0F, 256.0F, centerX - offset, centerY - offset, centerX + offset, centerY + offset, null);
            canvas.save();
            canvas.rotate(-22.5F, px1l, py1);
            canvas.drawLine(px1l, py1, px1l - this.mRodLength * 2.0F, py1, (float) this.mSize / 8.0F, paint);
            canvas.restore();
            if (boxAlpha > 0) {
                canvas.drawRect(px1l - (float) this.mSize * 2.9F, py1 + (float) this.mSize * 0.1F, px1l - (float) this.mSize * 1.9F, py1 + (float) this.mSize * 1.1F, this.mBoxPaint);
            }
            canvas.save();
            canvas.rotate(22.5F, px1r, py1);
            canvas.drawLine(px1r, py1, px1r + this.mRodLength * 2.0F, py1, (float) this.mSize / 8.0F, paint);
            canvas.restore();
            if (boxAlpha > 0) {
                canvas.drawRect(px1r + (float) this.mSize * 1.9F, py1 + (float) this.mSize * 0.1F, px1r + (float) this.mSize * 2.9F, py1 + (float) this.mSize * 1.1F, this.mBoxPaint);
            }
            py2 = centerY - 0.296875F * (float) this.mSize;
            canvas.drawLine(centerX, py2, centerX, py2 - this.mRodLength, (float) this.mSize / 8.0F, paint);
            if (boxAlpha > 0) {
                canvas.drawRect(centerX - (float) this.mSize * 0.5F, py2 - (float) this.mSize * 2.1F, centerX + (float) this.mSize * 0.5F, py2 - (float) this.mSize * 1.1F, this.mBoxPaint);
                ContainerDrawHelper.drawItem(canvas, this.mItem, centerX, py2 - (float) this.mSize * 1.6F, 0.0F, (float) this.mSize, 0);
            }
            paint.recycle();
        }
    }

    private static class NavigationButton extends View {

        private final Image mImage;

        private final int mSrcLeft;

        public NavigationButton(Context context, Image image, int srcLeft) {
            super(context);
            this.mImage = image;
            this.mSrcLeft = srcLeft;
        }

        @Override
        protected void onDraw(@Nonnull Canvas canvas) {
            Paint paint = Paint.obtain();
            if (!this.isHovered()) {
                paint.setRGBA(192, 192, 192, 255);
            }
            canvas.drawImage(this.mImage, (float) this.mSrcLeft, 352.0F, (float) (this.mSrcLeft + 32), 384.0F, 0.0F, 0.0F, (float) this.getWidth(), (float) this.getHeight(), paint);
            paint.recycle();
        }

        @Override
        public void onHoverChanged(boolean hovered) {
            super.onHoverChanged(hovered);
            this.invalidate();
        }
    }

    private static class TabBackground extends Drawable {

        private final float mRadius;

        private final TextPaint mTextPaint;

        public TabBackground(View view) {
            this.mRadius = (float) view.dp(16.0F);
            this.mTextPaint = new TextPaint();
            this.mTextPaint.setFontSize(view.sp(16.0F));
        }

        @Override
        public void draw(@Nonnull Canvas canvas) {
            Rect b = this.getBounds();
            float stroke = this.mRadius * 0.25F;
            float start = stroke * 0.5F;
            Paint paint = Paint.obtain();
            paint.setRGBA(0, 0, 0, 180);
            canvas.drawRoundRect((float) b.left + start, (float) b.top + start, (float) b.right - start, (float) b.bottom - start, this.mRadius, paint);
            paint.setStyle(1);
            paint.setStrokeWidth(stroke);
            paint.setColor(-14066038);
            canvas.drawRoundRect((float) b.left + start, (float) b.top + start, (float) b.right - start, (float) b.bottom - start, this.mRadius, paint);
            paint.recycle();
        }
    }

    private static class TextFieldBackground extends Drawable {

        private final float mRadius;

        public TextFieldBackground(View view) {
            this.mRadius = (float) view.dp(3.0F);
        }

        @Override
        public void draw(@Nonnull Canvas canvas) {
            Rect b = this.getBounds();
            float start = this.mRadius * 0.5F;
            Paint paint = Paint.obtain();
            paint.setStyle(1);
            paint.setStrokeWidth(this.mRadius);
            paint.setColor(-14066038);
            canvas.drawRoundRect((float) b.left + start, (float) b.top + start, (float) b.right - start, (float) b.bottom - start, this.mRadius, paint);
            paint.recycle();
        }

        @Override
        public boolean getPadding(@Nonnull Rect padding) {
            int h = Math.round(this.mRadius);
            int v = Math.round(this.mRadius * 0.5F);
            padding.set(h, v, h, v);
            return true;
        }
    }

    private static class TextFieldStart extends Drawable {

        private final Image mImage;

        private final int mSrcLeft;

        private final int mSize;

        public TextFieldStart(View view, Image image, int srcLeft) {
            this.mImage = image;
            this.mSrcLeft = srcLeft;
            this.mSize = view.dp(24.0F);
        }

        @Override
        public void draw(@Nonnull Canvas canvas) {
            Rect b = this.getBounds();
            canvas.drawImage(this.mImage, (float) this.mSrcLeft, 192.0F, (float) (this.mSrcLeft + 64), 256.0F, (float) b.left, (float) b.top, (float) b.right, (float) b.bottom, null);
        }

        @Override
        public int getIntrinsicWidth() {
            return this.mSize;
        }

        @Override
        public int getIntrinsicHeight() {
            return this.mSize;
        }

        @Override
        public boolean getPadding(@Nonnull Rect padding) {
            int h = Math.round((float) this.mSize / 4.0F);
            int v = Math.round((float) this.mSize / 6.0F);
            padding.set(h, v, h, v);
            return true;
        }
    }
}
package icyllis.modernui.mc;

import icyllis.modernui.animation.LayoutTransition;
import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.annotation.Nullable;
import icyllis.modernui.core.Context;
import icyllis.modernui.fragment.Fragment;
import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.GLSurfaceCanvas;
import icyllis.modernui.graphics.Paint;
import icyllis.modernui.graphics.Rect;
import icyllis.modernui.graphics.drawable.Drawable;
import icyllis.modernui.markdown.Markdown;
import icyllis.modernui.markdown.core.CorePlugin;
import icyllis.modernui.mc.ui.ThemeControl;
import icyllis.modernui.text.Editable;
import icyllis.modernui.text.method.LinkMovementMethod;
import icyllis.modernui.util.DataSet;
import icyllis.modernui.view.LayoutInflater;
import icyllis.modernui.view.View;
import icyllis.modernui.view.ViewGroup;
import icyllis.modernui.widget.Button;
import icyllis.modernui.widget.FrameLayout;
import icyllis.modernui.widget.TextView;
import javax.annotation.Nonnull;

public class DashboardFragment extends Fragment {

    public static final String CREDIT_TEXT = "Modern UI 3.10.1\nby\nBloCamLimb\n(Icyllis Milica)\nCiallo～(∠・ω< )⌒☆";

    private ViewGroup mLayout;

    private TextView mSideBox;

    private TextView mInfoBox;

    final Runnable mUpdateText = this::updateText;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable DataSet savedInstanceState) {
        if (this.mLayout != null) {
            return this.mLayout;
        } else {
            FrameLayout layout = new FrameLayout(this.requireContext());
            TextView tv;
            if (this.mSideBox == null) {
                tv = new Button(this.getContext());
                tv.setText("Still Alive");
                tv.setTextSize(16.0F);
                tv.setTextColor(-2314702);
                tv.setTextAlignment(4);
                tv.setOnClickListener(this::play);
                ThemeControl.addBackground(tv);
                this.mSideBox = tv;
            } else {
                tv = this.mSideBox;
            }
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(-2, -2);
            params.setMarginEnd(tv.dp(120.0F));
            params.gravity = 8388629;
            layout.addView(tv, params);
            if (this.mInfoBox == null) {
                tv = new TextView(this.getContext());
                tv.setTextSize(16.0F);
                tv.setTextAlignment(2);
                tv.setMovementMethod(LinkMovementMethod.getInstance());
                Markdown.builder(this.requireContext()).usePlugin(CorePlugin.create()).setBufferType(TextView.BufferType.SPANNABLE).build().setMarkdown(tv, "What's New in Modern UI 3.10.1\n----\n* Brand-New Tooltip Style\n* Better Font Management\n* Better Mod Compatibility\n* Rendering Optimization\n* New Shader Compiler\n* New UI Components & Text Styles\n* Unicode 15.1 Emoji List\n* [Full Changelog…](https://github.com/BloCamLimb/ModernUI/blob/master/changelogs.md)\n* [Full Changelog…](https://github.com/BloCamLimb/ModernUI-MC/blob/master/changelogs.md)\n\n> Author: BloCamLimb  \n  Source Code: [Modern UI](https://github.com/BloCamLimb/ModernUI)  \n  Source Code: [Modern UI (MC)](https://github.com/BloCamLimb/ModernUI-MC)");
                this.mInfoBox = tv;
            } else {
                tv = this.mInfoBox;
            }
            params = new FrameLayout.LayoutParams(-2, -2);
            params.setMarginStart(tv.dp(120.0F));
            params.gravity = 8388627;
            layout.addView(tv, params);
            tv = new TextView(this.getContext());
            tv.setTextSize(12.0F);
            tv.setText("Copyright © 2019-2024 BloCamLimb. All rights reserved.");
            params = new FrameLayout.LayoutParams(-2, -2);
            params.gravity = 81;
            layout.addView(tv, params);
            layout.setLayoutTransition(new LayoutTransition());
            return this.mLayout = layout;
        }
    }

    private void play(View button) {
        TextView tv = (TextView) button;
        tv.setText("", TextView.BufferType.EDITABLE);
        tv.setClickable(false);
        if (this.mInfoBox != null) {
            this.mInfoBox.setVisibility(8);
        }
        StillAlive.getInstance().start();
        this.mLayout.postDelayed(() -> {
            if (this.mLayout.isAttachedToWindow()) {
                View view = new View(this.getContext());
                view.setBackground(new DashboardFragment.Background(view));
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(view.dp(480.0F), view.dp(270.0F));
                params.setMarginStart(view.dp(60.0F));
                params.setMarginEnd(view.dp(30.0F));
                params.gravity = 8388627;
                this.mLayout.addView(view, params);
            }
        }, 18000L);
        this.mLayout.postDelayed(this.mUpdateText, 2000L);
    }

    private void updateText() {
        if (this.mSideBox.isAttachedToWindow()) {
            Editable editable = this.mSideBox.getEditableText();
            if (editable.length() < "Modern UI 3.10.1\nby\nBloCamLimb\n(Icyllis Milica)\nCiallo～(∠・ω< )⌒☆".length()) {
                editable.append("Modern UI 3.10.1\nby\nBloCamLimb\n(Icyllis Milica)\nCiallo～(∠・ω< )⌒☆".charAt(editable.length()));
                if (editable.length() < "Modern UI 3.10.1\nby\nBloCamLimb\n(Icyllis Milica)\nCiallo～(∠・ω< )⌒☆".length()) {
                    this.mSideBox.postDelayed(this.mUpdateText, 250L);
                }
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        StillAlive.getInstance().stop();
    }

    private static class Background extends Drawable {

        private final float mStrokeWidth;

        private Background(View view) {
            this.mStrokeWidth = (float) view.dp(2.0F);
        }

        @Override
        public void draw(@Nonnull Canvas canvas) {
            if (canvas instanceof GLSurfaceCanvas) {
                Rect bounds = this.getBounds();
                float inner = this.mStrokeWidth * 0.5F;
                ((GLSurfaceCanvas) canvas).drawGlowWave((float) bounds.left + inner * 1.5F, (float) bounds.top + inner * 1.5F, (float) bounds.right - inner, (float) bounds.bottom - inner);
                Paint paint = Paint.obtain();
                paint.setStyle(1);
                paint.setColor(-3300456);
                paint.setStrokeWidth(this.mStrokeWidth);
                canvas.drawRoundRect((float) bounds.left + inner, (float) bounds.top + inner, (float) bounds.right - inner, (float) bounds.bottom - inner, this.mStrokeWidth * 2.0F, paint);
                paint.recycle();
                this.invalidateSelf();
            }
        }

        @Override
        public boolean getPadding(@Nonnull Rect padding) {
            int inner = (int) Math.ceil((double) (this.mStrokeWidth * 0.5F));
            padding.set(inner, inner, inner, inner);
            return true;
        }
    }
}
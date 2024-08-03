package icyllis.modernui.mc;

import icyllis.modernui.animation.AnimationUtils;
import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.annotation.Nullable;
import icyllis.modernui.audio.FFT;
import icyllis.modernui.core.Context;
import icyllis.modernui.core.Core;
import icyllis.modernui.fragment.Fragment;
import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.GLSurfaceCanvas;
import icyllis.modernui.graphics.MathUtil;
import icyllis.modernui.graphics.Paint;
import icyllis.modernui.graphics.Rect;
import icyllis.modernui.graphics.drawable.Drawable;
import icyllis.modernui.util.DataSet;
import icyllis.modernui.view.LayoutInflater;
import icyllis.modernui.view.View;
import icyllis.modernui.view.ViewGroup;
import icyllis.modernui.widget.Button;
import icyllis.modernui.widget.LinearLayout;
import icyllis.modernui.widget.SeekBar;
import icyllis.modernui.widget.TextView;
import icyllis.modernui.widget.Toast;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.Random;

public class MusicFragment extends Fragment {

    private MusicPlayer mMusicPlayer;

    private MusicFragment.SpectrumDrawable mSpectrumDrawable;

    private Button mTitleButton;

    private Button mPlayButton;

    private MusicFragment.SeekLayout mSeekLayout;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mMusicPlayer = MusicPlayer.getInstance();
        this.mMusicPlayer.setOnTrackLoadCallback(track -> {
            if (track != null) {
                this.mMusicPlayer.setAnalyzerCallback(fft -> {
                    fft.setLogAverages(250, 14);
                    fft.setWindowFunc(0);
                }, this.mSpectrumDrawable::updateAmplitudes);
                track.play();
                this.mPlayButton.setText("⏸");
            } else {
                this.mPlayButton.setText("⏵");
                Toast.makeText(this.requireContext(), "Failed to open Ogg Vorbis file", 0).show();
            }
            String trackName = this.mMusicPlayer.getTrackName();
            this.mTitleButton.setText((CharSequence) Objects.requireNonNullElse(trackName, "Play A Music!"));
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.mMusicPlayer.setAnalyzerCallback(null, null);
        if (!this.mMusicPlayer.isPlaying()) {
            this.mMusicPlayer.clearTrack();
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable DataSet savedInstanceState) {
        LinearLayout content = new LinearLayout(this.requireContext());
        content.setOrientation(1);
        this.mSpectrumDrawable = new MusicFragment.SpectrumDrawable(content.dp(14.0F), content.dp(2.0F), content.dp(640.0F));
        content.setBackground(this.mSpectrumDrawable);
        content.setGravity(17);
        this.mMusicPlayer.setAnalyzerCallback(null, this.mSpectrumDrawable::updateAmplitudes);
        Button button = new Button(this.requireContext());
        button.setOnClickListener(v -> {
            String path = MusicPlayer.openDialogGet();
            if (path != null) {
                this.mMusicPlayer.replaceTrack(Path.of(path));
            }
        });
        String trackName = this.mMusicPlayer.getTrackName();
        button.setText((CharSequence) Objects.requireNonNullElse(trackName, "Play A Music!"));
        button.setTextSize(16.0F);
        button.setTextColor(-14113805);
        button.setPadding(0, content.dp(4.0F), 0, content.dp(4.0F));
        button.setMinWidth(button.dp(200.0F));
        this.mTitleButton = button;
        content.addView(button, -2, -2);
        button = new Button(this.requireContext());
        button.setOnClickListener(v -> {
            Button btn = (Button) v;
            if (this.mMusicPlayer.isPlaying()) {
                this.mMusicPlayer.pause();
                btn.setText("⏵");
            } else {
                this.mMusicPlayer.play();
                btn.setText("⏸");
            }
        });
        if (this.mMusicPlayer.isPlaying()) {
            button.setText("⏸");
        } else {
            button.setText("⏵");
        }
        button.setTextSize(24.0F);
        button.setMinWidth(button.dp(200.0F));
        this.mPlayButton = button;
        content.addView(button, -2, -2);
        MusicFragment.SeekLayout seekLayout = new MusicFragment.SeekLayout(this.requireContext(), 12.0F);
        this.mSeekLayout = seekLayout;
        content.addView(seekLayout);
        seekLayout.post(this::updateProgress);
        seekLayout.mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            boolean mPlaying;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    float fraction = (float) progress / 10000.0F;
                    float length = MusicFragment.this.mMusicPlayer.getTrackLength();
                    MusicFragment.this.mSeekLayout.mMinText.setText(MusicFragment.this.formatTime((int) (fraction * length)));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                this.mPlaying = MusicFragment.this.mMusicPlayer.isPlaying();
                if (this.mPlaying) {
                    MusicFragment.this.mMusicPlayer.pause();
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                MusicFragment.this.mMusicPlayer.seek((float) seekBar.getProgress() / 10000.0F);
                if (this.mPlaying) {
                    MusicFragment.this.mMusicPlayer.play();
                }
            }
        });
        MusicFragment.SeekLayout volumeBar = new MusicFragment.SeekLayout(this.requireContext(), 18.0F);
        content.addView(volumeBar);
        volumeBar.mMinText.setText("\ud83d\udd07");
        volumeBar.mMaxText.setText("\ud83d\udd0a");
        volumeBar.mSeekBar.setProgress(Math.round(this.mMusicPlayer.getGain() * 10000.0F));
        volumeBar.mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    MusicFragment.this.mMusicPlayer.setGain((float) progress / 10000.0F);
                }
            }
        });
        return content;
    }

    private void updateProgress() {
        if (this.mMusicPlayer.isPlaying()) {
            float time = this.mMusicPlayer.getTrackTime();
            float length = this.mMusicPlayer.getTrackLength();
            this.mSeekLayout.mMinText.setText(this.formatTime((int) time));
            this.mSeekLayout.mSeekBar.setProgress((int) (time / length * 10000.0F));
            this.mSeekLayout.mMaxText.setText(this.formatTime((int) length));
        }
        this.mSeekLayout.postDelayed(this::updateProgress, 200L);
    }

    private String formatTime(int seconds) {
        int minutes = seconds / 60;
        seconds -= minutes * 60;
        int hours = minutes / 60;
        minutes -= hours * 60;
        return String.format("%d:%02d:%02d", hours, minutes, seconds);
    }

    public static class SeekLayout extends LinearLayout {

        TextView mMinText;

        SeekBar mSeekBar;

        TextView mMaxText;

        public SeekLayout(Context context, float textSize) {
            super(context);
            this.setOrientation(0);
            this.mMinText = new TextView(context);
            this.mMinText.setTextSize(textSize);
            this.mMinText.setMinWidth(this.dp(60.0F));
            this.mMinText.setTextAlignment(5);
            this.addView(this.mMinText);
            this.mSeekBar = new SeekBar(context);
            this.mSeekBar.setMax(10000);
            this.mSeekBar.setClickable(true);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2);
            lp.weight = 1.0F;
            this.mSeekBar.setLayoutParams(lp);
            this.addView(this.mSeekBar);
            this.mMaxText = new TextView(context);
            this.mMaxText.setTextSize(textSize);
            this.mMaxText.setMinWidth(this.dp(60.0F));
            this.mMaxText.setTextAlignment(6);
            this.addView(this.mMaxText);
            this.setGravity(17);
        }
    }

    public static class SpectrumDrawable extends Drawable {

        private static final Random RANDOM = new Random();

        private static final int AMPLITUDE_LENGTH = 60;

        private final float[] mAmplitudes = new float[60];

        private volatile int mActualAmplitudeLength;

        private long mLastAnimationTime;

        private final Runnable mAnimationRunnable = this::invalidateSelf;

        private final int mBandWidth;

        private final int mBandGap;

        private final int mBandHeight;

        private float mLastBassAmplitude;

        private static final int MAX_PARTICLES = 60;

        private final List<MusicFragment.SpectrumDrawable.Particle> mParticleList = new ArrayList(60);

        public SpectrumDrawable(int bandWidth, int bandGap, int bandHeight) {
            this.mBandWidth = bandWidth;
            this.mBandGap = bandGap;
            this.mBandHeight = bandHeight;
        }

        public void updateAmplitudes(FFT fft) {
            float[] amplitudes = this.mAmplitudes;
            int len = Math.min(fft.getAverageSize() - 5, 60);
            for (int i = 0; i < len; i++) {
                float value = fft.getAverage(i % len + 5) / (float) fft.getBandSize();
                amplitudes[i] = Math.max(amplitudes[i], value);
            }
            this.mActualAmplitudeLength = len;
            long now = Core.timeMillis();
            this.scheduleSelf(this.mAnimationRunnable, now);
        }

        private void computeParticles(float multiplier, long deltaMillis) {
            float delta = (float) deltaMillis / 1000.0F;
            ListIterator<MusicFragment.SpectrumDrawable.Particle> it = this.mParticleList.listIterator();
            while (it.hasNext()) {
                MusicFragment.SpectrumDrawable.Particle p = (MusicFragment.SpectrumDrawable.Particle) it.next();
                float newX = p.x + p.xVel * delta;
                float newY = p.y + p.yVel * delta;
                if ((double) newY < -0.1) {
                    it.remove();
                } else {
                    if (newX < 0.0F || newX > 1.0F) {
                        float d = Math.max(0.0F - newX, newX - 1.0F);
                        if (newX < 0.0F) {
                            newX = d;
                        } else {
                            newX = 1.0F - d;
                        }
                        p.xVel = -p.xVel * 0.8F;
                    }
                    p.x = newX;
                    p.y = newY;
                    float velSq = p.xVel * p.xVel + p.yVel * p.yVel;
                    float vel = (float) Math.sqrt((double) velSq);
                    float xVelSign = Math.signum(p.xVel);
                    float yVelSign = Math.signum(p.yVel);
                    if (vel > 1.0E-5F) {
                        p.xVel = Math.max(0.0F, Math.abs(p.xVel) - Math.abs(p.xVel) / vel * velSq * 0.003F) * xVelSign;
                    }
                    float y1 = 0.0F;
                    if (vel > 1.0E-5F) {
                        y1 = Math.max(0.0F, Math.abs(p.yVel) - Math.abs(p.yVel) / vel * velSq * 0.003F) * yVelSign;
                    }
                    float y2 = p.yVel - 0.5F * delta;
                    p.yVel = Math.min(y1, y2);
                }
            }
            if (this.mLastBassAmplitude >= 0.024F && multiplier >= 1.25F || this.mLastBassAmplitude >= 0.016F && multiplier >= 2.5F || this.mLastBassAmplitude >= 0.008F && multiplier >= 3.75F) {
                int count = 6;
                int var16;
                if (this.mLastBassAmplitude >= 0.024F) {
                    var16 = (int) Math.min(multiplier * 3.0F, (float) count);
                } else if (this.mLastBassAmplitude >= 0.016F) {
                    var16 = (int) Math.min(multiplier * 2.0F, (float) count);
                } else {
                    var16 = (int) Math.min(multiplier, (float) count);
                }
                int var17 = Math.min(var16, 60 - this.mParticleList.size());
                while (var17-- != 0) {
                    boolean leftSide = RANDOM.nextBoolean();
                    float x = leftSide ? 0.0F : 1.0F;
                    float y = RANDOM.nextFloat() * 0.6F + 0.25F;
                    float xVel = RANDOM.nextFloat() * 0.2F + 0.1F;
                    if (!leftSide) {
                        xVel = -xVel;
                    }
                    float velx = Math.min(0.08F * multiplier, 0.3F);
                    float yVel = (float) Math.sqrt((double) (velx - xVel * xVel));
                    this.mParticleList.add(new MusicFragment.SpectrumDrawable.Particle(x, y, xVel, yVel));
                }
            }
        }

        @Override
        public void draw(@NonNull Canvas canvas) {
            Rect b = this.getBounds();
            int contentCenter = (this.mBandWidth * 60 + this.mBandGap * 59) / 2;
            float x = (float) (b.centerX() - contentCenter);
            float bottom = (float) (b.bottom - this.mBandWidth);
            Paint paint = Paint.obtain();
            long time = AnimationUtils.currentAnimationTimeMillis();
            long delta = time - this.mLastAnimationTime;
            this.mLastAnimationTime = time;
            float[] amplitudes = this.mAmplitudes;
            int len = this.mActualAmplitudeLength;
            boolean invalidate = false;
            for (int i = 0; i < len; i++) {
                if (amplitudes[i] > 0.0F) {
                    invalidate = true;
                    break;
                }
            }
            for (int ix = 0; ix < len; ix++) {
                amplitudes[ix] = Math.max(0.0F, amplitudes[ix] - (float) delta * 2.5E-5F * 180.0F * (amplitudes[ix] + 0.03F));
            }
            int bassLen = len / 5;
            float bassAmplitude = 0.0F;
            for (int ix = 0; ix < bassLen; ix++) {
                bassAmplitude += amplitudes[ix];
            }
            bassAmplitude /= (float) bassLen;
            float multiplier = bassAmplitude / this.mLastBassAmplitude;
            this.computeParticles(multiplier, delta);
            this.mLastBassAmplitude = bassAmplitude;
            if (!this.mParticleList.isEmpty()) {
                invalidate = true;
            }
            if (canvas instanceof GLSurfaceCanvas && invalidate) {
                ((GLSurfaceCanvas) canvas).drawGlowWave((float) b.left, (float) b.top, (float) b.right, (float) b.bottom);
            }
            float alphaMult = 1.5F + MathUtil.sin((float) time / 600.0F) / 2.0F;
            paint.setRGBA(160, 155, 230, (int) (64.0F * alphaMult));
            float radius = (float) this.mBandHeight * 0.05F;
            paint.setSmoothWidth(radius * 2.2F);
            for (MusicFragment.SpectrumDrawable.Particle p : this.mParticleList) {
                canvas.drawCircle((float) b.x() + p.x * (float) b.width(), (float) b.y() + (1.0F - p.y) * (float) b.height(), radius, paint);
            }
            paint.setSmoothWidth(0.0F);
            for (int ix = 0; ix < 60; ix++) {
                paint.setRGBA(100 + ix * 2, 220 - ix * 2, 240 - ix * 4, 255);
                canvas.drawRect(x, bottom - amplitudes[ix] * (float) this.mBandHeight, x + (float) this.mBandWidth, bottom, paint);
                x += (float) (this.mBandWidth + this.mBandGap);
            }
            paint.recycle();
            if (invalidate) {
                this.invalidateSelf();
            }
        }

        @Override
        public int getIntrinsicWidth() {
            return this.mBandWidth * 60 + this.mBandGap * 59;
        }

        @Override
        public int getIntrinsicHeight() {
            return this.mBandHeight;
        }

        @Override
        public boolean getPadding(@NonNull Rect padding) {
            int pad = this.mBandWidth;
            padding.set(pad, pad, pad, pad);
            return true;
        }

        private static class Particle {

            float x;

            float y;

            float xVel;

            float yVel;

            Particle(float x, float y, float xVel, float yVel) {
                this.x = x;
                this.y = y;
                this.xVel = xVel;
                this.yVel = yVel;
            }
        }
    }
}
package icyllis.modernui.widget;

import icyllis.modernui.core.Context;
import icyllis.modernui.graphics.drawable.Drawable;
import icyllis.modernui.graphics.drawable.LayerDrawable;
import icyllis.modernui.graphics.drawable.ScaleDrawable;
import icyllis.modernui.graphics.drawable.ShapeDrawable;
import icyllis.modernui.util.ColorStateList;
import icyllis.modernui.util.StateSet;

public class SeekBar extends AbsSeekBar {

    private SeekBar.OnSeekBarChangeListener mOnSeekBarChangeListener;

    public SeekBar(Context context) {
        super(context);
        this.setFocusable(true);
        if (this.getClass() == SeekBar.class) {
            ShapeDrawable shape = new ShapeDrawable();
            shape.setShape(3);
            shape.setStroke(this.dp(2.0F), -3300456);
            shape.setSize(-1, this.dp(2.0F));
            shape.setCornerRadius(1.0F);
            Drawable track = new ScaleDrawable(shape, 3, 1.0F, -1.0F);
            ShapeDrawable shapex = new ShapeDrawable();
            shapex.setShape(3);
            shapex.setStroke(this.dp(2.0F), -12632257);
            shapex.setSize(-1, this.dp(2.0F));
            shapex.setCornerRadius(1.0F);
            LayerDrawable progress = new LayerDrawable(shapex, track);
            progress.setId(0, 16908303);
            progress.setId(1, 16908301);
            this.setProgressDrawable(progress);
            track = new ShapeDrawable();
            track.setShape(1);
            track.setSize(this.dp(12.0F), this.dp(12.0F));
            track.setColor(-3300456);
            int[][] stateSet = new int[][] { StateSet.get(16), StateSet.get(64), StateSet.WILD_CARD };
            int[] colors = new int[] { -2132943395, -2132943395, -8355712 };
            track.setStroke(this.dp(1.5F), new ColorStateList(stateSet, colors));
            track.setUseLevelForShape(false);
            this.setThumb(track);
            this.setPadding(this.dp(16.0F), 0, this.dp(16.0F), 0);
        }
    }

    @Override
    void onProgressRefresh(float scale, boolean fromUser, int progress) {
        super.onProgressRefresh(scale, fromUser, progress);
        if (this.mOnSeekBarChangeListener != null) {
            this.mOnSeekBarChangeListener.onProgressChanged(this, progress, fromUser);
        }
    }

    public void setOnSeekBarChangeListener(SeekBar.OnSeekBarChangeListener l) {
        this.mOnSeekBarChangeListener = l;
    }

    @Override
    void onStartTrackingTouch() {
        super.onStartTrackingTouch();
        if (this.mOnSeekBarChangeListener != null) {
            this.mOnSeekBarChangeListener.onStartTrackingTouch(this);
        }
    }

    @Override
    void onStopTrackingTouch() {
        super.onStopTrackingTouch();
        if (this.mOnSeekBarChangeListener != null) {
            this.mOnSeekBarChangeListener.onStopTrackingTouch(this);
        }
    }

    public interface OnSeekBarChangeListener {

        default void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        }

        default void onStartTrackingTouch(SeekBar seekBar) {
        }

        default void onStopTrackingTouch(SeekBar seekBar) {
        }
    }
}
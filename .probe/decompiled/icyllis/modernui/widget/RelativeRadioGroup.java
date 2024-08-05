package icyllis.modernui.widget;

import icyllis.modernui.core.Context;
import icyllis.modernui.view.View;
import icyllis.modernui.view.ViewGroup;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class RelativeRadioGroup extends RelativeLayout {

    private int mCheckedId = -1;

    private final Checkable.OnCheckedChangeListener mChildOnCheckedChangeListener = new RelativeRadioGroup.CheckedStateTracker();

    private boolean mProtectFromCheckedChange = false;

    @Nullable
    private RelativeRadioGroup.OnCheckedChangeListener mOnCheckedChangeListener;

    public RelativeRadioGroup(Context context) {
        super(context);
    }

    @Override
    protected void onViewAdded(View child) {
        super.onViewAdded(child);
        if (child instanceof RadioButton button) {
            if (button.getId() == -1) {
                button.setId(generateViewId());
            }
            if (button.isChecked()) {
                this.setCheckedStateForView(this.mCheckedId, false);
                this.setCheckedId(button.getId());
            }
            button.setOnCheckedChangeListener(this.mChildOnCheckedChangeListener);
        }
    }

    @Override
    protected void onViewRemoved(View child) {
        super.onViewRemoved(child);
        if (child instanceof RadioButton) {
            ((RadioButton) child).setOnCheckedChangeListener(null);
        }
    }

    public void check(int id) {
        if (id == -1 || id != this.mCheckedId) {
            this.setCheckedStateForView(this.mCheckedId, false);
            this.setCheckedStateForView(id, true);
            this.setCheckedId(id);
        }
    }

    private void setCheckedId(int id) {
        this.mCheckedId = id;
        if (this.mOnCheckedChangeListener != null) {
            this.mOnCheckedChangeListener.onCheckedChanged(this, this.mCheckedId);
        }
    }

    private void setCheckedStateForView(int viewId, boolean checked) {
        if (viewId != -1) {
            View checkedView = this.findViewById(viewId);
            if (checkedView instanceof RadioButton) {
                this.mProtectFromCheckedChange = true;
                ((RadioButton) checkedView).setChecked(checked);
                this.mProtectFromCheckedChange = false;
            }
        }
    }

    public final int getCheckedId() {
        return this.mCheckedId;
    }

    public final void clearCheck() {
        this.check(-1);
    }

    public void setOnCheckedChangeListener(@Nullable RelativeRadioGroup.OnCheckedChangeListener listener) {
        this.mOnCheckedChangeListener = listener;
    }

    @Nonnull
    @Override
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new RelativeLayout.LayoutParams(-2, -2);
    }

    private class CheckedStateTracker implements Checkable.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(View buttonView, boolean isChecked) {
            if (!RelativeRadioGroup.this.mProtectFromCheckedChange) {
                RelativeRadioGroup.this.setCheckedStateForView(RelativeRadioGroup.this.mCheckedId, false);
                RelativeRadioGroup.this.setCheckedId(buttonView.getId());
            }
        }
    }

    @FunctionalInterface
    public interface OnCheckedChangeListener {

        void onCheckedChanged(RelativeRadioGroup var1, int var2);
    }
}
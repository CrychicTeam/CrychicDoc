package icyllis.modernui.widget;

import icyllis.modernui.core.Context;
import icyllis.modernui.text.Editable;
import icyllis.modernui.text.Selection;
import icyllis.modernui.text.TextUtils;
import icyllis.modernui.text.method.ArrowKeyMovementMethod;
import javax.annotation.Nonnull;

public class EditText extends TextView {

    public EditText(Context context) {
        super(context);
        this.setText("", TextView.BufferType.EDITABLE);
        this.setMovementMethod(ArrowKeyMovementMethod.getInstance());
        this.setFocusableInTouchMode(true);
        this.setGravity(16);
    }

    @Nonnull
    public Editable getText() {
        CharSequence text = super.getText();
        if (text instanceof Editable) {
            return (Editable) super.getText();
        } else {
            super.setText(text, TextView.BufferType.EDITABLE);
            return (Editable) super.getText();
        }
    }

    @Override
    public void setText(@Nonnull CharSequence text, @Nonnull TextView.BufferType type) {
        super.setText(text, TextView.BufferType.EDITABLE);
    }

    public void setSelection(int start, int stop) {
        Selection.setSelection(this.getText(), start, stop);
    }

    public void setSelection(int index) {
        Selection.setSelection(this.getText(), index);
    }

    public void selectAll() {
        Selection.selectAll(this.getText());
    }

    public void extendSelection(int index) {
        Selection.extendSelection(this.getText(), index);
    }

    @Override
    public void setEllipsize(TextUtils.TruncateAt ellipsis) {
        if (ellipsis == TextUtils.TruncateAt.MARQUEE) {
            throw new IllegalArgumentException("EditText cannot use the ellipsize mode TextUtils.TruncateAt.MARQUEE");
        } else {
            super.setEllipsize(ellipsis);
        }
    }
}
package icyllis.modernui.mc.ui;

import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.core.Context;
import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.Color;
import icyllis.modernui.graphics.Paint;
import icyllis.modernui.graphics.Rect;
import icyllis.modernui.text.InputFilter;
import icyllis.modernui.view.View;
import icyllis.modernui.widget.EditText;
import icyllis.modernui.widget.RelativeLayout;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class FourColorPicker extends RelativeLayout {

    private EditText mULColorField;

    private EditText mURColorField;

    private EditText mLRColorField;

    private EditText mLLColorField;

    private int mULColor = -1;

    private int mURColor = -1;

    private int mLRColor = -1;

    private int mLLColor = -1;

    private final Rect mPreviewBox = new Rect();

    private final int mBorderRadius;

    private float mThicknessFactor = 0.44444445F;

    private final View.OnFocusChangeListener mOnFieldFocusChange;

    public FourColorPicker(Context context, Supplier<List<? extends String>> getter, Consumer<List<? extends String>> setter, Runnable saveFn) {
        super(context);
        this.mBorderRadius = this.dp(6.0F);
        this.mOnFieldFocusChange = (v, hasFocus) -> {
            EditText input = (EditText) v;
            if (!hasFocus) {
                try {
                    String string = input.getText().toString();
                    int color = -1;
                    int idx = -1;
                    try {
                        color = Color.parseColor(string);
                        if (input == this.mULColorField) {
                            if (this.mULColor != color) {
                                this.mULColor = color;
                                idx = 0;
                            }
                        } else if (input == this.mURColorField) {
                            if (this.mURColor != color) {
                                this.mURColor = color;
                                idx = 1;
                            }
                        } else if (input == this.mLRColorField) {
                            if (this.mLRColor != color) {
                                this.mLRColor = color;
                                idx = 2;
                            }
                        } else if (input == this.mLLColorField && this.mLLColor != color) {
                            this.mLLColor = color;
                            idx = 3;
                        }
                    } catch (IllegalArgumentException var11) {
                        var11.printStackTrace();
                    }
                    if (idx != -1) {
                        this.invalidate();
                        ArrayList<String> newList = new ArrayList((Collection) getter.get());
                        if (newList.isEmpty()) {
                            newList.add("#FFFFFFFF");
                        }
                        while (newList.size() < 4) {
                            newList.add((String) newList.get(newList.size() - 1));
                        }
                        newList.set(idx, string);
                        setter.accept(newList);
                        saveFn.run();
                    }
                    input.setTextColor(0xFF000000 | color);
                } catch (Exception var12) {
                    input.setTextColor(-65536);
                }
            }
        };
        List<? extends String> colors = (List<? extends String>) getter.get();
        int dp4 = this.dp(4.0F);
        this.mULColorField = this.createField(0, colors);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(-2, -2);
        params.addRule(9);
        params.addRule(10);
        params.setMargins(dp4, dp4, dp4, dp4);
        this.mULColorField.setId(601);
        this.addView(this.mULColorField, params);
        this.mURColorField = this.createField(1, colors);
        params = new RelativeLayout.LayoutParams(-2, -2);
        params.addRule(11);
        params.addRule(10);
        params.setMargins(dp4, dp4, dp4, dp4);
        this.mURColorField.setId(602);
        this.addView(this.mURColorField, params);
        this.mLRColorField = this.createField(2, colors);
        params = new RelativeLayout.LayoutParams(-2, -2);
        params.addRule(3, 602);
        params.addRule(11);
        params.setMargins(dp4, dp4, dp4, dp4);
        this.addView(this.mLRColorField, params);
        this.mLLColorField = this.createField(3, colors);
        params = new RelativeLayout.LayoutParams(-2, -2);
        params.addRule(3, 601);
        params.addRule(9);
        params.setMargins(dp4, dp4, dp4, dp4);
        this.addView(this.mLLColorField, params);
        this.mOnFieldFocusChange.onFocusChange(this.mULColorField, false);
        this.mOnFieldFocusChange.onFocusChange(this.mURColorField, false);
        this.mOnFieldFocusChange.onFocusChange(this.mLRColorField, false);
        this.mOnFieldFocusChange.onFocusChange(this.mLLColorField, false);
    }

    @NonNull
    private EditText createField(int idx, List<? extends String> colors) {
        EditText field = new EditText(this.getContext());
        field.setSingleLine();
        field.setText((CharSequence) (colors.isEmpty() ? "#FFFFFFFF" : (CharSequence) colors.get(Math.min(idx, colors.size() - 1))));
        field.setFilters(new InputFilter[] { new InputFilter.LengthFilter(10) });
        field.setTextSize(16.0F);
        field.setOnFocusChangeListener(this.mOnFieldFocusChange);
        return field;
    }

    public void setColors(String[] colors) {
        this.mULColorField.setText(colors[0]);
        this.mURColorField.setText(colors[1]);
        this.mLRColorField.setText(colors[2]);
        this.mLLColorField.setText(colors[3]);
        this.mOnFieldFocusChange.onFocusChange(this.mULColorField, false);
        this.mOnFieldFocusChange.onFocusChange(this.mURColorField, false);
        this.mOnFieldFocusChange.onFocusChange(this.mLRColorField, false);
        this.mOnFieldFocusChange.onFocusChange(this.mLLColorField, false);
    }

    public void setThicknessFactor(float thicknessFactor) {
        if (this.mThicknessFactor != thicknessFactor) {
            this.mThicknessFactor = thicknessFactor;
            this.invalidate();
        }
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = Paint.obtain();
        paint.setStyle(1);
        paint.setStrokeWidth((float) this.mBorderRadius * this.mThicknessFactor);
        canvas.drawRoundRectGradient((float) this.mPreviewBox.left, (float) this.mPreviewBox.top, (float) this.mPreviewBox.right, (float) this.mPreviewBox.bottom, this.mULColor, this.mURColor, this.mLRColor, this.mLLColor, (float) this.mBorderRadius, paint);
        paint.recycle();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        this.mPreviewBox.set(Math.max(this.mULColorField.getRight(), this.mLLColorField.getRight()), this.getPaddingTop(), Math.min(this.mURColorField.getLeft(), this.mLRColorField.getLeft()), this.getHeight() - this.getPaddingBottom());
        int inset = (int) ((float) this.mBorderRadius * 1.33F + 0.5F);
        this.mPreviewBox.inset(inset, inset);
        this.invalidate();
    }
}
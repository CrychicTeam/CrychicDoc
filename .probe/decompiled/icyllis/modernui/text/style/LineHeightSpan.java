package icyllis.modernui.text.style;

import icyllis.modernui.graphics.text.FontMetricsInt;
import icyllis.modernui.text.TextPaint;

public interface LineHeightSpan extends ParagraphStyle, WrapTogetherSpan {

    void chooseHeight(CharSequence var1, int var2, int var3, int var4, int var5, FontMetricsInt var6, TextPaint var7);
}
package icyllis.modernui.text.method;

import icyllis.modernui.text.Spannable;
import icyllis.modernui.view.KeyEvent;
import icyllis.modernui.view.MotionEvent;
import icyllis.modernui.widget.TextView;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface MovementMethod {

    void initialize(TextView var1, Spannable var2);

    boolean onKeyDown(TextView var1, Spannable var2, int var3, KeyEvent var4);

    boolean onKeyUp(TextView var1, Spannable var2, int var3, KeyEvent var4);

    void onTakeFocus(TextView var1, Spannable var2, int var3);

    boolean onTouchEvent(TextView var1, Spannable var2, MotionEvent var3);

    boolean onGenericMotionEvent(TextView var1, Spannable var2, MotionEvent var3);

    boolean canSelectArbitrarily();
}
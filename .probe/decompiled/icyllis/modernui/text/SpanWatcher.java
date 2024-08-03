package icyllis.modernui.text;

public interface SpanWatcher extends NoCopySpan {

    void onSpanAdded(Spannable var1, Object var2, int var3, int var4);

    void onSpanRemoved(Spannable var1, Object var2, int var3, int var4);

    void onSpanChanged(Spannable var1, Object var2, int var3, int var4, int var5, int var6);
}
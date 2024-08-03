package icyllis.modernui.text;

public interface TextDirectionHeuristic {

    boolean isRtl(char[] var1, int var2, int var3);

    boolean isRtl(CharSequence var1, int var2, int var3);
}
package icyllis.modernui.text.method;

public class SingleLineTransformationMethod extends ReplacementTransformationMethod {

    private static final SingleLineTransformationMethod sInstance = new SingleLineTransformationMethod();

    private static final char[] ORIGINAL = new char[] { '\n', '\r' };

    private static final char[] REPLACEMENT = new char[] { ' ', '\ufeff' };

    private SingleLineTransformationMethod() {
    }

    public static SingleLineTransformationMethod getInstance() {
        return sInstance;
    }

    @Override
    protected char[] getOriginal() {
        return ORIGINAL;
    }

    @Override
    protected char[] getReplacement() {
        return REPLACEMENT;
    }
}
package icyllis.modernui.mc.testforge.trash;

@Deprecated
public enum TextAlign {

    LEFT(0.0F), CENTER(0.5F), RIGHT(1.0F);

    public final float offsetFactor;

    private TextAlign(float offsetFactor) {
        this.offsetFactor = offsetFactor;
    }
}
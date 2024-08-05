package snownee.jade.overlay;

public enum IconUI {

    HEART(52, 0, 9, 9, 16, 0, 9, 9),
    HALF_HEART(61, 0, 9, 9, 16, 0, 9, 9),
    EMPTY_HEART(16, 0, 9, 9),
    ARMOR(34, 9, 9, 9),
    HALF_ARMOR(25, 9, 9, 9),
    EMPTY_ARMOR(16, 9, 9, 9),
    EXPERIENCE_BUBBLE(25, 18, 9, 9);

    public final int u;

    public final int v;

    public final int su;

    public final int sv;

    public final int bu;

    public final int bv;

    public final int bsu;

    public final int bsv;

    private IconUI(int u, int v, int su, int sv) {
        this(u, v, su, sv, -1, -1, -1, -1);
    }

    private IconUI(int u, int v, int su, int sv, int bu, int bv, int bsu, int bsv) {
        this.u = u;
        this.v = v;
        this.su = su;
        this.sv = sv;
        this.bu = bu;
        this.bv = bv;
        this.bsu = bsu;
        this.bsv = bsv;
    }
}
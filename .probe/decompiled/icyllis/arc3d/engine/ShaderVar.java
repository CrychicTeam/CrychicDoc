package icyllis.arc3d.engine;

import icyllis.arc3d.core.SLDataType;

public class ShaderVar {

    public static final byte kNone_TypeModifier = 0;

    public static final byte kOut_TypeModifier = 1;

    public static final byte kIn_TypeModifier = 2;

    public static final byte kInOut_TypeModifier = 3;

    public static final byte kUniform_TypeModifier = 4;

    public static final int kNonArray = 0;

    private byte mType;

    private byte mTypeModifier;

    private final int mCount;

    private String mName;

    private String mLayoutQualifier;

    private String mExtraModifiers;

    public ShaderVar() {
        this("", (byte) 0, (byte) 0, 0, "", "");
    }

    public ShaderVar(String name, byte type) {
        this(name, type, (byte) 0, 0, "", "");
    }

    public ShaderVar(String name, byte type, int arrayCount) {
        this(name, type, (byte) 0, arrayCount, "", "");
    }

    public ShaderVar(String name, byte type, byte typeModifier) {
        this(name, type, typeModifier, 0, "", "");
    }

    public ShaderVar(String name, byte type, byte typeModifier, int arrayCount) {
        this(name, type, typeModifier, arrayCount, "", "");
    }

    public ShaderVar(String name, byte type, byte typeModifier, int arrayCount, String layoutQualifier, String extraModifier) {
        assert name != null;
        assert SLDataType.checkSLType(type);
        assert typeModifier >= 0 && typeModifier <= 4;
        assert arrayCount == 0 || arrayCount > 0;
        assert layoutQualifier != null && extraModifier != null;
        this.mType = type;
        this.mTypeModifier = typeModifier;
        this.mCount = arrayCount;
        this.mName = name;
        this.mLayoutQualifier = layoutQualifier;
        this.mExtraModifiers = extraModifier;
    }

    public void set(String name, byte type) {
        assert type != 0;
        this.mType = type;
        this.mName = name;
    }

    public boolean isArray() {
        return this.mCount != 0;
    }

    public int getArrayCount() {
        return this.mCount;
    }

    public String getName() {
        return this.mName;
    }

    public byte getType() {
        return this.mType;
    }

    public byte getTypeModifier() {
        return this.mTypeModifier;
    }

    public void setTypeModifier(byte typeModifier) {
        assert typeModifier >= 0 && typeModifier <= 4;
        this.mTypeModifier = typeModifier;
    }

    public void addLayoutQualifier(String layoutQualifier) {
        assert layoutQualifier != null && !layoutQualifier.isEmpty();
        if (this.mLayoutQualifier.isEmpty()) {
            this.mLayoutQualifier = layoutQualifier;
        } else {
            this.mLayoutQualifier = this.mLayoutQualifier + ", " + layoutQualifier;
        }
    }

    public void addModifier(String modifier) {
        assert modifier != null && !modifier.isEmpty();
        if (this.mExtraModifiers.isEmpty()) {
            this.mExtraModifiers = modifier;
        } else {
            this.mExtraModifiers = this.mExtraModifiers + " " + modifier;
        }
    }

    public void appendDecl(StringBuilder out) {
        if (!this.mLayoutQualifier.isEmpty()) {
            out.append("layout(");
            out.append(this.mLayoutQualifier);
            out.append(") ");
        }
        if (!this.mExtraModifiers.isEmpty()) {
            out.append(this.mExtraModifiers);
            out.append(" ");
        }
        if (this.mTypeModifier != 0) {
            out.append(switch(this.mTypeModifier) {
                case 1 ->
                    "out ";
                case 2 ->
                    "in ";
                case 3 ->
                    "inout ";
                case 4 ->
                    "uniform ";
                default ->
                    throw new IllegalStateException();
            });
        }
        byte type = this.getType();
        if (this.isArray()) {
            assert this.getArrayCount() > 0;
            out.append(SLDataType.typeString(type));
            out.append(" ");
            out.append(this.getName());
            out.append("[");
            out.append(this.getArrayCount());
            out.append("]");
        } else {
            out.append(SLDataType.typeString(type));
            out.append(" ");
            out.append(this.getName());
        }
    }
}
package icyllis.arc3d.compiler.tree;

import icyllis.arc3d.compiler.Context;
import java.util.Objects;
import java.util.StringJoiner;
import javax.annotation.Nonnull;

public final class Modifiers extends Node {

    public static final int kSmooth_Flag = 1;

    public static final int kFlat_Flag = 2;

    public static final int kNoPerspective_Flag = 4;

    public static final int kConst_Flag = 8;

    public static final int kUniform_Flag = 16;

    public static final int kIn_Flag = 32;

    public static final int kOut_Flag = 64;

    public static final int kCoherent_Flag = 128;

    public static final int kVolatile_Flag = 256;

    public static final int kRestrict_Flag = 512;

    public static final int kReadOnly_Flag = 1024;

    public static final int kWriteOnly_Flag = 2048;

    public static final int kBuffer_Flag = 4096;

    public static final int kWorkgroup_Flag = 8192;

    public static final int kSubroutine_Flag = 16384;

    public static final int kPure_Flag = 32768;

    public static final int kInline_Flag = 65536;

    public static final int kNoInline_Flag = 131072;

    public static final int kCount_Flag = 18;

    public static final int kInterpolation_Flags = 7;

    public static final int kMemory_Flags = 3968;

    public static final int kStorage_Flags = 12400;

    private Layout mLayout = null;

    private int mFlags = 0;

    public static String describeFlag(int flag) {
        assert Integer.bitCount(flag) == 1;
        return switch(Integer.numberOfTrailingZeros(flag)) {
            case 0 ->
                "smooth";
            case 1 ->
                "flat";
            case 2 ->
                "noperspective";
            case 3 ->
                "const";
            case 4 ->
                "uniform";
            case 5 ->
                "in";
            case 6 ->
                "out";
            case 7 ->
                "coherent";
            case 8 ->
                "volatile";
            case 9 ->
                "restrict";
            case 10 ->
                "readonly";
            case 11 ->
                "writeonly";
            case 12 ->
                "buffer";
            case 13 ->
                "workgroup";
            case 14 ->
                "subroutine";
            case 15 ->
                "__pure";
            case 16 ->
                "inline";
            case 17 ->
                "noinline";
            default ->
                "";
        };
    }

    public Modifiers(int position) {
        super(position);
    }

    public Layout layout() {
        return this.mLayout;
    }

    public int layoutFlags() {
        return this.mLayout != null ? this.mLayout.layoutFlags() : 0;
    }

    public void setLayoutFlag(@Nonnull Context context, int mask, String name, int pos) {
        Layout layout = this.mLayout;
        if (layout == null) {
            layout = this.mLayout = new Layout();
        }
        layout.setLayoutFlag(context, mask, name, pos);
    }

    public void clearLayoutFlag(int mask) {
        if (this.mLayout != null) {
            this.mLayout.clearLayoutFlag(mask);
        }
    }

    public boolean checkLayoutFlags(@Nonnull Context context, int permittedLayoutFlags) {
        return this.mLayout != null ? this.mLayout.checkLayoutFlags(context, this.mPosition, permittedLayoutFlags) : true;
    }

    public int flags() {
        return this.mFlags;
    }

    public void setFlag(@Nonnull Context context, int mask, int pos) {
        if ((this.mFlags & mask) != 0) {
            context.error(pos, "qualifier '" + describeFlags(this.mFlags & mask) + "' appears more than once");
        }
        this.mFlags |= mask;
    }

    public void clearFlag(int mask) {
        this.mFlags &= ~mask;
    }

    public boolean checkFlags(@Nonnull Context context, int permittedFlags) {
        boolean success = true;
        for (int i = 0; i < 18; i++) {
            int flag = 1 << i;
            if ((this.mFlags & flag) != 0 && (permittedFlags & flag) == 0) {
                context.error(this.mPosition, "qualifier '" + describeFlag(flag) + "' is not permitted here");
                success = false;
            }
        }
        if (Integer.bitCount(this.mFlags & 7) > 1) {
            context.error(this.mPosition, "at most one interpolation qualifier can be used");
            success = false;
        }
        return success;
    }

    public boolean isConst() {
        return (this.mFlags & 8) != 0;
    }

    public int layoutOffset() {
        return this.mLayout != null ? this.mLayout.mOffset : -1;
    }

    public int layoutBuiltin() {
        return this.mLayout != null ? this.mLayout.mBuiltin : -1;
    }

    @Override
    public boolean accept(@Nonnull TreeVisitor visitor) {
        return false;
    }

    public int hashCode() {
        int result = Objects.hashCode(this.mLayout);
        return 31 * result + this.mFlags;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            Modifiers modifiers = (Modifiers) o;
            return this.mFlags == modifiers.mFlags && Objects.equals(this.mLayout, modifiers.mLayout);
        } else {
            return false;
        }
    }

    @Nonnull
    @Override
    public String toString() {
        return this.mLayout == null ? describeFlags(this.mFlags, true) : this.mLayout + describeFlags(this.mFlags, true);
    }

    public static String describeFlags(int flags) {
        return describeFlags(flags, false);
    }

    public static String describeFlags(int flags, boolean padded) {
        if (flags == 0) {
            return "";
        } else {
            StringJoiner joiner = new StringJoiner(" ", "", padded ? " " : "");
            if ((flags & 16384) != 0) {
                joiner.add("subroutine");
            }
            if ((flags & 32768) != 0) {
                joiner.add("__pure");
            }
            if ((flags & 65536) != 0) {
                joiner.add("inline");
            }
            if ((flags & 131072) != 0) {
                joiner.add("noinline");
            }
            if ((flags & 1) != 0) {
                joiner.add("smooth");
            }
            if ((flags & 2) != 0) {
                joiner.add("flat");
            }
            if ((flags & 4) != 0) {
                joiner.add("noperspective");
            }
            if ((flags & 8) != 0) {
                joiner.add("const");
            }
            if ((flags & 16) != 0) {
                joiner.add("uniform");
            }
            if ((flags & 32) != 0 && (flags & 64) != 0) {
                joiner.add("inout");
            } else if ((flags & 32) != 0) {
                joiner.add("in");
            } else if ((flags & 64) != 0) {
                joiner.add("out");
            }
            if ((flags & 128) != 0) {
                joiner.add("coherent");
            }
            if ((flags & 256) != 0) {
                joiner.add("volatile");
            }
            if ((flags & 512) != 0) {
                joiner.add("restrict");
            }
            if ((flags & 1024) != 0) {
                joiner.add("readonly");
            }
            if ((flags & 2048) != 0) {
                joiner.add("writeonly");
            }
            if ((flags & 4096) != 0) {
                joiner.add("buffer");
            }
            if ((flags & 8192) != 0) {
                joiner.add("workgroup");
            }
            return joiner.toString();
        }
    }
}
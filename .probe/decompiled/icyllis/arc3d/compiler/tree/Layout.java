package icyllis.arc3d.compiler.tree;

import icyllis.arc3d.compiler.Context;
import java.util.StringJoiner;
import javax.annotation.Nonnull;

public final class Layout {

    public static final int kOriginUpperLeft_LayoutFlag = 1;

    public static final int kPixelCenterInteger_LayoutFlag = 2;

    public static final int kEarlyFragmentTests_LayoutFlag = 4;

    public static final int kBlendSupportAllEquations_LayoutFlag = 8;

    public static final int kPushConstant_LayoutFlag = 16;

    public static final int kLocation_LayoutFlag = 32;

    public static final int kComponent_LayoutFlag = 64;

    public static final int kIndex_LayoutFlag = 128;

    public static final int kBinding_LayoutFlag = 256;

    public static final int kOffset_LayoutFlag = 512;

    public static final int kSet_LayoutFlag = 1024;

    public static final int kInputAttachmentIndex_LayoutFlag = 2048;

    public static final int kBuiltin_LayoutFlag = 4096;

    public static final int kCount_LayoutFlag = 13;

    private int mLayoutFlags = 0;

    public int mLocation = -1;

    public int mComponent = -1;

    public int mIndex = -1;

    public int mBinding = -1;

    public int mOffset = -1;

    public int mSet = -1;

    public int mInputAttachmentIndex = -1;

    public int mBuiltin = -1;

    public static String describeLayoutFlag(int flag) {
        assert Integer.bitCount(flag) == 1;
        return switch(Integer.numberOfTrailingZeros(flag)) {
            case 0 ->
                "origin_upper_left";
            case 1 ->
                "pixel_center_integer";
            case 2 ->
                "early_fragment_tests";
            case 3 ->
                "blend_support_all_equations";
            case 4 ->
                "push_constant";
            case 5 ->
                "location";
            case 6 ->
                "component";
            case 7 ->
                "index";
            case 8 ->
                "binding";
            case 9 ->
                "offset";
            case 10 ->
                "set";
            case 11 ->
                "input_attachment_index";
            case 12 ->
                "builtin";
            default ->
                "";
        };
    }

    public int layoutFlags() {
        return this.mLayoutFlags;
    }

    public void setLayoutFlag(@Nonnull Context context, int mask, String name, int pos) {
        if ((this.mLayoutFlags & mask) != 0) {
            context.error(pos, "layout qualifier '" + name + "' appears more than once");
        }
        this.mLayoutFlags |= mask;
    }

    public void clearLayoutFlag(int mask) {
        this.mLayoutFlags &= ~mask;
    }

    public boolean checkLayoutFlags(@Nonnull Context context, int pos, int permittedLayoutFlags) {
        boolean success = true;
        for (int i = 0; i < 13; i++) {
            int flag = 1 << i;
            if ((this.mLayoutFlags & flag) != 0 && (permittedLayoutFlags & flag) == 0) {
                context.error(pos, "layout qualifier '" + describeLayoutFlag(flag) + "' is not permitted here");
                success = false;
            }
        }
        return success;
    }

    public int hashCode() {
        int result = this.mLayoutFlags;
        result = 31 * result + this.mLocation;
        result = 31 * result + this.mComponent;
        result = 31 * result + this.mIndex;
        result = 31 * result + this.mBinding;
        result = 31 * result + this.mOffset;
        result = 31 * result + this.mSet;
        result = 31 * result + this.mInputAttachmentIndex;
        return 31 * result + this.mBuiltin;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            Layout layout = (Layout) o;
            if (this.mLayoutFlags != layout.mLayoutFlags) {
                return false;
            } else if (this.mLocation != layout.mLocation) {
                return false;
            } else if (this.mComponent != layout.mComponent) {
                return false;
            } else if (this.mIndex != layout.mIndex) {
                return false;
            } else if (this.mBinding != layout.mBinding) {
                return false;
            } else if (this.mOffset != layout.mOffset) {
                return false;
            } else if (this.mSet != layout.mSet) {
                return false;
            } else {
                return this.mInputAttachmentIndex != layout.mInputAttachmentIndex ? false : this.mBuiltin == layout.mBuiltin;
            }
        } else {
            return false;
        }
    }

    @Nonnull
    public String toString() {
        StringJoiner joiner = new StringJoiner(", ", "layout(", ") ");
        if (this.mLocation >= 0) {
            joiner.add("location = " + this.mLocation);
        }
        if (this.mComponent >= 0) {
            joiner.add("component = " + this.mComponent);
        }
        if (this.mIndex >= 0) {
            joiner.add("index = " + this.mIndex);
        }
        if (this.mBinding >= 0) {
            joiner.add("binding = " + this.mBinding);
        }
        if (this.mOffset >= 0) {
            joiner.add("offset = " + this.mOffset);
        }
        if (this.mSet >= 0) {
            joiner.add("set = " + this.mSet);
        }
        if (this.mInputAttachmentIndex >= 0) {
            joiner.add("input_attachment_index = " + this.mInputAttachmentIndex);
        }
        if (this.mBuiltin >= 0) {
            joiner.add("builtin = " + this.mBuiltin);
        }
        if ((this.mLayoutFlags & 1) != 0) {
            joiner.add("origin_upper_left");
        }
        if ((this.mLayoutFlags & 2) != 0) {
            joiner.add("pixel_center_integer");
        }
        if ((this.mLayoutFlags & 4) != 0) {
            joiner.add("early_fragment_tests");
        }
        if ((this.mLayoutFlags & 8) != 0) {
            joiner.add("blend_support_all_equations");
        }
        if ((this.mLayoutFlags & 16) != 0) {
            joiner.add("push_constant");
        }
        return joiner.toString();
    }
}
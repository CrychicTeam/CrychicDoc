package icyllis.arc3d.compiler;

import icyllis.arc3d.compiler.tree.Type;
import icyllis.arc3d.core.MathUtil;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public enum MemoryLayout {

    Std140, Extended, Std430, Scalar;

    public int alignment(@Nonnull Type type) {
        return this.alignment(type, null);
    }

    public int alignment(@Nonnull Type type, @Nullable int[] out) {
        return switch(type.getTypeKind()) {
            case 0 ->
                {
                    int align = this.alignment(type.getElementType(), out);
                    if (this == Std140 || this == Extended) {
                        align = Math.max(align, 16);
                        assert (align & 15) == 0;
                    }
                    if (out != null) {
                        out[2] = MathUtil.alignTo(out[0], align);
                        out[0] = type.isUnsizedArray() ? out[2] : out[2] * type.getArraySize();
                    }
                    yield align;
                }
            default ->
                throw new UnsupportedOperationException();
            case 2 ->
                {
                    int align = this.alignment(type.getElementType(), out);
                    if (this == Std140) {
                        align = Math.max(align, 16);
                        assert (align & 15) == 0;
                    }
                    if (out != null) {
                        out[1] = MathUtil.alignTo(out[0], align);
                        out[0] = out[1] * type.getCols();
                    }
                    yield align;
                }
            case 5 ->
                {
                }
            case 6 ->
                {
                    int align = 0;
                    int size = 0;
                    for (Type.Field field : type.getFields()) {
                        int memberAlign = this.alignment(field.type(), out);
                        align = Math.max(align, memberAlign);
                        if (out != null) {
                            size = MathUtil.alignTo(size, memberAlign);
                            size += out[0];
                        }
                    }
                    if (this == Std140 || this == Extended) {
                        align = Math.max(align, 16);
                        assert (align & 15) == 0;
                    }
                    if (out != null) {
                        if (this != Scalar) {
                            size = MathUtil.alignTo(size, align);
                        }
                        out[0] = size;
                        out[1] = out[2] = 0;
                    }
                    yield align;
                }
            case 7 ->
                {
                }
        };
    }

    public int stride(@Nonnull Type type) {
        switch(type.getTypeKind()) {
            case 0:
            case 2:
                int size = this.size(type.getElementType());
                return MathUtil.alignTo(size, this.alignment(type));
            default:
                throw new UnsupportedOperationException();
        }
    }

    public int size(@Nonnull Type type) {
        return switch(type.getTypeKind()) {
            case 0 ->
                {
                    int stride = this.stride(type);
                    ???;
                }
            default ->
                throw new UnsupportedOperationException();
            case 2 ->
                {
                    int stride = this.stride(type);
                    ???;
                }
            case 5 ->
                {
                }
            case 6 ->
                {
                    int size = 0;
                    for (Type.Field field : type.getFields()) {
                        int memberAlign = this.alignment(field.type());
                        size = MathUtil.alignTo(size, memberAlign);
                        size += this.size(field.type());
                    }
                    if (this != Scalar) {
                        int align = this.alignment(type);
                        size = MathUtil.alignTo(size, align);
                    }
                    yield size;
                }
            case 7 ->
                {
                    int size = this.size(type.getElementType());
                    ???;
                }
        };
    }

    public boolean isSupported(@Nonnull Type type) {
        return switch(type.getTypeKind()) {
            case 0, 2, 7 ->
                this.isSupported(type.getElementType());
            default ->
                false;
            case 5 ->
                !type.isBoolean();
            case 6 ->
                {
                    Type.Field[] var2 = type.getFields();
                    int var3 = var2.length;
                    int var4 = 0;
                    while (true) {
                        if (var4 >= var3) {
                        }
                        Type.Field field = var2[var4];
                        if (!this.isSupported(field.type())) {
                        }
                        var4++;
                    }
                }
        };
    }
}
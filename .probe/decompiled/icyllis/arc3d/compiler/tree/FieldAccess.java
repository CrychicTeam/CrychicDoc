package icyllis.arc3d.compiler.tree;

import icyllis.arc3d.compiler.Context;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class FieldAccess extends Expression {

    private final Expression mBase;

    private final int mFieldIndex;

    private final boolean mAnonymousBlock;

    private FieldAccess(int position, Expression base, int fieldIndex, boolean anonymousBlock) {
        super(position, base.getType().getFields()[fieldIndex].type());
        this.mBase = base;
        this.mFieldIndex = fieldIndex;
        this.mAnonymousBlock = anonymousBlock;
    }

    @Nullable
    public static Expression convert(@Nonnull Context context, int position, @Nonnull Expression base, int namePosition, @Nonnull String name) {
        Type baseType = base.getType();
        if (!baseType.isVector() && !baseType.isScalar()) {
            if (baseType.isStruct()) {
                Type.Field[] fields = baseType.getFields();
                for (int i = 0; i < fields.length; i++) {
                    if (fields[i].name().equals(name)) {
                        return make(position, base, i, false);
                    }
                }
            }
            context.error(position, "type '" + baseType.getName() + "' does not have a member named '" + name + "'");
            return null;
        } else {
            return Swizzle.convert(context, position, base, namePosition, name);
        }
    }

    @Nonnull
    public static Expression make(int position, Expression base, int fieldIndex, boolean anonymousBlock) {
        Type baseType = base.getType();
        if (!baseType.isStruct()) {
            throw new AssertionError();
        } else {
            Objects.checkIndex(fieldIndex, baseType.getFields().length);
            return new FieldAccess(position, base, fieldIndex, anonymousBlock);
        }
    }

    @Override
    public Node.ExpressionKind getKind() {
        return Node.ExpressionKind.FIELD_ACCESS;
    }

    @Override
    public boolean accept(@Nonnull TreeVisitor visitor) {
        return visitor.visitFieldAccess(this) ? true : this.mBase.accept(visitor);
    }

    public Expression getBase() {
        return this.mBase;
    }

    public int getFieldIndex() {
        return this.mFieldIndex;
    }

    public boolean isAnonymousBlock() {
        return this.mAnonymousBlock;
    }

    @Nonnull
    @Override
    public Expression clone(int position) {
        return new FieldAccess(position, this.mBase.clone(), this.mFieldIndex, this.mAnonymousBlock);
    }

    @Nonnull
    @Override
    public String toString(int parentPrecedence) {
        String s = this.mBase.toString(2);
        if (!s.isEmpty()) {
            s = s + ".";
        }
        return s + this.mBase.getType().getFields()[this.mFieldIndex].name();
    }
}
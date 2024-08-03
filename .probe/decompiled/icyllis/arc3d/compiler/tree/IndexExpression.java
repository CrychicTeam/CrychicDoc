package icyllis.arc3d.compiler.tree;

import icyllis.arc3d.compiler.ConstantFolder;
import icyllis.arc3d.compiler.Context;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class IndexExpression extends Expression {

    private Expression mBase;

    private Expression mIndex;

    private IndexExpression(int position, Expression base, Expression index) {
        super(position, base.getType().getElementType());
        this.mBase = base;
        this.mIndex = index;
    }

    private IndexExpression(int position, Type type, Expression base, Expression index) {
        super(position, type);
        this.mBase = base;
        this.mIndex = index;
    }

    private static boolean indexOutOfBounds(@Nonnull Context context, int pos, long index, @Nonnull Expression base) {
        Type baseType = base.getType();
        if (index >= 0L) {
            if (baseType.isArray()) {
                if (baseType.isUnsizedArray()) {
                    return false;
                }
                if (index < (long) baseType.getArraySize()) {
                    return false;
                }
            } else if (baseType.isMatrix()) {
                if (index < (long) baseType.getCols()) {
                    return false;
                }
            } else {
                assert baseType.isVector();
                if (index < (long) baseType.getRows()) {
                    return false;
                }
            }
        }
        context.error(pos, "index " + index + " out of range for '" + baseType + "'");
        return true;
    }

    @Nullable
    public static Expression convert(@Nonnull Context context, int pos, @Nonnull Expression base, @Nullable Expression index) {
        if (base instanceof TypeReference) {
            Type baseType = ((TypeReference) base).getValue();
            int arraySize;
            if (index != null) {
                arraySize = baseType.convertArraySize(context, pos, index);
            } else {
                arraySize = -1;
            }
            return arraySize == 0 ? null : TypeReference.make(context, pos, context.getSymbolTable().getArrayType(baseType, arraySize));
        } else if (index == null) {
            context.error(pos, "missing index in '[]'");
            return null;
        } else {
            Type baseType = base.getType();
            if (!baseType.isArray() && !baseType.isMatrix() && !baseType.isVector()) {
                context.error(base.mPosition, "expected array, matrix or vector, but found '" + baseType + "'");
                return null;
            } else {
                if (!index.getType().isInteger()) {
                    index = context.getTypes().mInt.coerceExpression(context, index);
                    if (index == null) {
                        return null;
                    }
                }
                base = ConstantFolder.getConstantValueForVariable(base);
                index = ConstantFolder.getConstantValueForVariable(index);
                if (index.isIntLiteral()) {
                    long indexValue = ((Literal) index).getIntegerValue();
                    if (indexOutOfBounds(context, index.mPosition, indexValue, base)) {
                        return null;
                    }
                }
                return make(context, pos, base, index);
            }
        }
    }

    public static Expression make(@Nonnull Context context, int pos, @Nonnull Expression base, @Nonnull Expression index) {
        return new IndexExpression(pos, base, index);
    }

    public Expression getBase() {
        return this.mBase;
    }

    public void setBase(Expression base) {
        this.mBase = base;
    }

    public Expression getIndex() {
        return this.mIndex;
    }

    public void setIndex(Expression index) {
        this.mIndex = index;
    }

    @Override
    public Node.ExpressionKind getKind() {
        return Node.ExpressionKind.INDEX;
    }

    @Override
    public boolean accept(@Nonnull TreeVisitor visitor) {
        return visitor.visitIndex(this) ? true : this.mBase.accept(visitor) || this.mIndex.accept(visitor);
    }

    @Nonnull
    @Override
    public Expression clone(int position) {
        return new IndexExpression(position, this.getType(), this.mBase.clone(), this.mIndex.clone());
    }

    @Nonnull
    @Override
    public String toString(int parentPrecedence) {
        return this.mBase.toString(2) + "[" + this.mIndex.toString(17) + "]";
    }
}
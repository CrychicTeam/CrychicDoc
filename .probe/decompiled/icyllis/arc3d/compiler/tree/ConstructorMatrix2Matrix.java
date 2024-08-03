package icyllis.arc3d.compiler.tree;

import java.util.OptionalDouble;
import javax.annotation.Nonnull;

public final class ConstructorMatrix2Matrix extends ConstructorCall {

    private ConstructorMatrix2Matrix(int position, Type type, Expression... arguments) {
        super(position, type, arguments);
        assert arguments.length == 1;
    }

    @Nonnull
    public static Expression make(int position, @Nonnull Type type, @Nonnull Expression arg) {
        assert type.isMatrix();
        assert arg.getType().getComponentType().matches(type.getComponentType());
        return (Expression) (type.getRows() == arg.getType().getRows() && type.getCols() == arg.getType().getCols() ? arg : new ConstructorMatrix2Matrix(position, type, arg));
    }

    @Override
    public Node.ExpressionKind getKind() {
        return Node.ExpressionKind.CONSTRUCTOR_MATRIX_TO_MATRIX;
    }

    @Override
    public OptionalDouble getConstantValue(int i) {
        int rows = this.getType().getRows();
        int row = i % rows;
        int col = i / rows;
        assert col >= 0;
        assert row >= 0;
        assert col < this.getType().getCols();
        assert row < this.getType().getRows();
        Type argType = this.getArguments()[0].getType();
        if (col < argType.getCols() && row < argType.getRows()) {
            i = row + col * argType.getRows();
            return this.getArguments()[0].getConstantValue(i);
        } else {
            return OptionalDouble.of(col == row ? 1.0 : 0.0);
        }
    }

    @Nonnull
    @Override
    public Expression clone(int position) {
        return new ConstructorMatrix2Matrix(position, this.getType(), this.cloneArguments());
    }
}
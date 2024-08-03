package icyllis.arc3d.compiler.tree;

import icyllis.arc3d.compiler.ConstantFolder;
import java.util.OptionalDouble;
import javax.annotation.Nonnull;

public final class ConstructorScalar2Matrix extends ConstructorCall {

    private ConstructorScalar2Matrix(int position, Type type, Expression... arguments) {
        super(position, type, arguments);
        assert arguments.length == 1;
    }

    @Nonnull
    public static Expression make(int position, @Nonnull Type type, @Nonnull Expression arg) {
        assert type.isMatrix();
        assert arg.getType().isScalar();
        assert arg.getType().matches(type.getComponentType());
        arg = ConstantFolder.makeConstantValueForVariable(position, arg);
        return new ConstructorScalar2Matrix(position, type, arg);
    }

    @Override
    public Node.ExpressionKind getKind() {
        return Node.ExpressionKind.CONSTRUCTOR_SCALAR_TO_MATRIX;
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
        return col == row ? this.getArguments()[0].getConstantValue(0) : OptionalDouble.of(0.0);
    }

    @Nonnull
    @Override
    public Expression clone(int position) {
        return new ConstructorScalar2Matrix(position, this.getType(), this.cloneArguments());
    }
}
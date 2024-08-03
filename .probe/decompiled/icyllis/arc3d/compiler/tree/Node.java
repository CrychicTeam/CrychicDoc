package icyllis.arc3d.compiler.tree;

import icyllis.arc3d.compiler.Position;
import javax.annotation.Nonnull;

public abstract class Node {

    public int mPosition;

    protected Node(int position) {
        this.mPosition = position;
    }

    public final int getStartOffset() {
        assert this.mPosition != -1;
        return Position.getStartOffset(this.mPosition);
    }

    public final int getEndOffset() {
        assert this.mPosition != -1;
        return Position.getEndOffset(this.mPosition);
    }

    public abstract boolean accept(@Nonnull TreeVisitor var1);

    @Nonnull
    public abstract String toString();

    public static enum ElementKind {

        EXTENSION(TopLevelElement.class),
        FUNCTION_DEFINITION(FunctionDefinition.class),
        FUNCTION_PROTOTYPE(FunctionPrototype.class),
        GLOBAL_VARIABLE(GlobalVariableDecl.class),
        INTERFACE_BLOCK(TopLevelElement.class),
        MODIFIERS(TopLevelElement.class),
        STRUCT_DEFINITION(TopLevelElement.class);

        private final Class<? extends TopLevelElement> mType;

        private ElementKind(Class<? extends TopLevelElement> type) {
            this.mType = type;
        }

        public Class<? extends TopLevelElement> getType() {
            return this.mType;
        }
    }

    public static enum ExpressionKind {

        BINARY(BinaryExpression.class),
        CONDITIONAL(ConditionalExpression.class),
        CONSTRUCTOR_ARRAY(ConstructorArray.class),
        CONSTRUCTOR_ARRAY_CAST(ConstructorArrayCast.class),
        CONSTRUCTOR_COMPOUND(ConstructorCompound.class),
        CONSTRUCTOR_COMPOUND_CAST(ConstructorCompoundCast.class),
        CONSTRUCTOR_MATRIX_TO_MATRIX(ConstructorMatrix2Matrix.class),
        CONSTRUCTOR_SCALAR_CAST(ConstructorScalarCast.class),
        CONSTRUCTOR_SCALAR_TO_MATRIX(ConstructorScalar2Matrix.class),
        CONSTRUCTOR_SCALAR_TO_VECTOR(ConstructorScalar2Vector.class),
        CONSTRUCTOR_STRUCT(ConstructorStruct.class),
        FIELD_ACCESS(FieldAccess.class),
        FUNCTION_CALL(FunctionCall.class),
        FUNCTION_REFERENCE(FunctionReference.class),
        INDEX(IndexExpression.class),
        LITERAL(Literal.class),
        POISON(Poison.class),
        POSTFIX(PostfixExpression.class),
        PREFIX(PrefixExpression.class),
        SWIZZLE(Swizzle.class),
        TYPE_REFERENCE(TypeReference.class),
        VARIABLE_REFERENCE(VariableReference.class);

        private final Class<? extends Expression> mType;

        private ExpressionKind(Class<? extends Expression> type) {
            this.mType = type;
        }

        public Class<? extends Expression> getType() {
            return this.mType;
        }
    }

    public static enum StatementKind {

        BLOCK(BlockStatement.class),
        BREAK(BreakStatement.class),
        CONTINUE(ContinueStatement.class),
        DISCARD(DiscardStatement.class),
        DO_LOOP(Statement.class),
        EMPTY(EmptyStatement.class),
        EXPRESSION(ExpressionStatement.class),
        FOR_LOOP(ForLoop.class),
        IF(IfStatement.class),
        RETURN(ReturnStatement.class),
        SWITCH(Statement.class),
        SWITCH_CASE(Statement.class),
        VARIABLE_DECL(VariableDecl.class);

        private final Class<? extends Statement> mType;

        private StatementKind(Class<? extends Statement> type) {
            this.mType = type;
        }

        public Class<? extends Statement> getType() {
            return this.mType;
        }
    }

    public static enum SymbolKind {

        ANONYMOUS_FIELD(AnonymousField.class), FUNCTION_DECL(FunctionDecl.class), TYPE(Type.class), VARIABLE(Variable.class);

        private final Class<? extends Symbol> mType;

        private SymbolKind(Class<? extends Symbol> type) {
            this.mType = type;
        }

        public Class<? extends Symbol> getType() {
            return this.mType;
        }
    }
}
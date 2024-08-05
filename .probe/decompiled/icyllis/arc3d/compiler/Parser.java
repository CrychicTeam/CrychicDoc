package icyllis.arc3d.compiler;

import icyllis.arc3d.compiler.parser.Lexer;
import icyllis.arc3d.compiler.parser.Token;
import icyllis.arc3d.compiler.tree.BinaryExpression;
import icyllis.arc3d.compiler.tree.BlockStatement;
import icyllis.arc3d.compiler.tree.BreakStatement;
import icyllis.arc3d.compiler.tree.ConditionalExpression;
import icyllis.arc3d.compiler.tree.ContinueStatement;
import icyllis.arc3d.compiler.tree.DiscardStatement;
import icyllis.arc3d.compiler.tree.EmptyStatement;
import icyllis.arc3d.compiler.tree.Expression;
import icyllis.arc3d.compiler.tree.ExpressionStatement;
import icyllis.arc3d.compiler.tree.FieldAccess;
import icyllis.arc3d.compiler.tree.ForLoop;
import icyllis.arc3d.compiler.tree.FunctionCall;
import icyllis.arc3d.compiler.tree.FunctionDecl;
import icyllis.arc3d.compiler.tree.FunctionDefinition;
import icyllis.arc3d.compiler.tree.FunctionPrototype;
import icyllis.arc3d.compiler.tree.GlobalVariableDecl;
import icyllis.arc3d.compiler.tree.IfStatement;
import icyllis.arc3d.compiler.tree.IndexExpression;
import icyllis.arc3d.compiler.tree.InterfaceBlock;
import icyllis.arc3d.compiler.tree.Layout;
import icyllis.arc3d.compiler.tree.Literal;
import icyllis.arc3d.compiler.tree.Modifiers;
import icyllis.arc3d.compiler.tree.Poison;
import icyllis.arc3d.compiler.tree.PostfixExpression;
import icyllis.arc3d.compiler.tree.PrefixExpression;
import icyllis.arc3d.compiler.tree.ReturnStatement;
import icyllis.arc3d.compiler.tree.Statement;
import icyllis.arc3d.compiler.tree.Symbol;
import icyllis.arc3d.compiler.tree.TopLevelElement;
import icyllis.arc3d.compiler.tree.TranslationUnit;
import icyllis.arc3d.compiler.tree.Type;
import icyllis.arc3d.compiler.tree.Variable;
import icyllis.arc3d.compiler.tree.VariableDecl;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongStack;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class Parser {

    private final ShaderCompiler mCompiler;

    private final ShaderKind mKind;

    private final CompileOptions mOptions;

    private final char[] mSource;

    private final Lexer mLexer;

    private final LongStack mPushback = new LongArrayList();

    private ArrayList<TopLevelElement> mUniqueElements;

    public Parser(ShaderCompiler compiler, ShaderKind kind, CompileOptions options, char[] source) {
        this.mCompiler = (ShaderCompiler) Objects.requireNonNull(compiler);
        this.mOptions = (CompileOptions) Objects.requireNonNull(options);
        this.mKind = (ShaderKind) Objects.requireNonNull(kind);
        this.mSource = source;
        this.mLexer = new Lexer(source);
    }

    @Nullable
    public TranslationUnit parse(ModuleUnit parent) {
        Objects.requireNonNull(parent);
        this.mUniqueElements = new ArrayList();
        this.GlobalDeclarations();
        Context context = this.mCompiler.getContext();
        TranslationUnit result;
        if (context.getErrorHandler().errorCount() == 0) {
            result = new TranslationUnit(this.rangeFromOffset(0), this.mSource, this.mKind, this.mOptions, context.isBuiltin(), context.isModule(), context.getTypes(), context.getSymbolTable(), this.mUniqueElements);
        } else {
            result = null;
        }
        this.mUniqueElements = null;
        return result;
    }

    @Nullable
    public ModuleUnit parseModule(ModuleUnit parent) {
        Objects.requireNonNull(parent);
        this.mUniqueElements = new ArrayList();
        this.GlobalDeclarations();
        Context context = this.mCompiler.getContext();
        ModuleUnit result;
        if (context.getErrorHandler().errorCount() == 0) {
            result = new ModuleUnit();
            result.mParent = parent;
            result.mSymbols = context.getSymbolTable();
            result.mElements = this.mUniqueElements;
        } else {
            result = null;
        }
        this.mUniqueElements = null;
        return result;
    }

    private long nextRawToken() {
        long token;
        if (!this.mPushback.isEmpty()) {
            token = this.mPushback.popLong();
        } else {
            token = this.mLexer.next();
            if (Token.kind(token) == 38) {
                this.error(token, "'" + this.text(token) + "' is a reserved keyword");
                return Token.replace(token, 39);
            }
        }
        return token;
    }

    private static boolean isWhitespace(int kind) {
        return switch(kind) {
            case 85, 86, 87 ->
                true;
            default ->
                false;
        };
    }

    private long nextToken() {
        long token;
        do {
            token = this.nextRawToken();
        } while (isWhitespace(Token.kind(token)));
        return token;
    }

    private void pushback(long token) {
        this.mPushback.push(token);
    }

    private long peek() {
        if (this.mPushback.isEmpty()) {
            long token = this.nextToken();
            this.mPushback.push(token);
            return token;
        } else {
            return this.mPushback.topLong();
        }
    }

    private boolean peek(int kind) {
        return Token.kind(this.peek()) == kind;
    }

    @Nonnull
    private String text(long token) {
        int offset = Token.offset(token);
        int length = Token.length(token);
        return new String(this.mSource, offset, length);
    }

    private int position(long token) {
        int offset = Token.offset(token);
        return Position.range(offset, offset + Token.length(token));
    }

    private void error(long token, String msg) {
        int offset = Token.offset(token);
        this.error(offset, offset + Token.length(token), msg);
    }

    private void error(int start, int end, String msg) {
        this.mCompiler.getContext().error(start, end, msg);
    }

    private void warning(long token, String msg) {
        int offset = Token.offset(token);
        this.warning(offset, offset + Token.length(token), msg);
    }

    private void warning(int start, int end, String msg) {
        this.mCompiler.getContext().warning(start, end, msg);
    }

    private int rangeFromOffset(int startOffset) {
        int endOffset = this.mPushback.isEmpty() ? this.mLexer.offset() : Token.offset(this.mPushback.topLong());
        return Position.range(startOffset, endOffset);
    }

    private int rangeFrom(int startPos) {
        return this.rangeFromOffset(Position.getStartOffset(startPos));
    }

    private int rangeFrom(long startToken) {
        return this.rangeFromOffset(Token.offset(startToken));
    }

    private boolean checkNext(int kind) {
        long next = this.peek();
        if (Token.kind(next) == kind) {
            this.nextToken();
            return true;
        } else {
            return false;
        }
    }

    private long checkIdentifier() {
        long next = this.peek();
        return Token.kind(next) == 39 && !this.mCompiler.getContext().getSymbolTable().isBuiltinType(this.text(next)) ? this.nextToken() : -1L;
    }

    private long expect(int kind, String expected) {
        long next = this.nextToken();
        if (Token.kind(next) != kind) {
            String msg = "expected " + expected + ", but found '" + this.text(next) + "'";
            this.error(next, msg);
            throw new IllegalStateException(msg);
        } else {
            return next;
        }
    }

    private long expectIdentifier() {
        long token = this.expect(39, "an identifier");
        if (this.mCompiler.getContext().getSymbolTable().isBuiltinType(this.text(token))) {
            String msg = "expected an identifier, but found type '" + this.text(token) + "'";
            this.error(token, msg);
            throw new IllegalStateException(msg);
        } else {
            return token;
        }
    }

    private boolean expectNewline() {
        long token = this.nextRawToken();
        if (Token.kind(token) == 85) {
            String tokenText = this.text(token);
            if (tokenText.indexOf(13) != -1 || tokenText.indexOf(10) != -1) {
                return true;
            }
        }
        this.pushback(token);
        return false;
    }

    private void GlobalDeclarations() {
        if (this.mSource.length > 8388606) {
            this.mCompiler.getContext().error(-1, "source code is too long, " + this.mSource.length + " > 8,388,606 chars");
        } else {
            while (true) {
                switch(Token.kind(this.peek())) {
                    case 0:
                        return;
                    case 88:
                        this.error(this.peek(), "invalid token");
                        return;
                    default:
                        try {
                            if (!this.GlobalDeclaration()) {
                                return;
                            }
                        } catch (IllegalStateException var2) {
                            return;
                        }
                }
            }
        }
    }

    private boolean GlobalDeclaration() {
        long start = this.peek();
        if (Token.kind(start) == 84) {
            this.nextToken();
            return true;
        } else {
            Modifiers modifiers = this.Modifiers();
            long peek = this.peek();
            if (Token.kind(peek) == 39 && !this.mCompiler.getContext().getSymbolTable().isType(this.text(peek))) {
                return this.InterfaceBlock(modifiers);
            } else if (Token.kind(peek) == 84) {
                this.nextToken();
                return true;
            } else if (Token.kind(peek) == 34) {
                this.StructDeclaration();
                return true;
            } else {
                Type type = this.TypeSpecifier(modifiers);
                if (type == null) {
                    return false;
                } else {
                    long name = this.expectIdentifier();
                    if (this.checkNext(40)) {
                        return this.FunctionDeclarationRest(this.position(start), modifiers, type, name);
                    } else {
                        this.GlobalVarDeclarationRest(this.position(start), modifiers, type, name);
                        return true;
                    }
                }
            }
        }
    }

    private boolean InterfaceBlock(@Nonnull Modifiers modifiers) {
        long name = this.expectIdentifier();
        String blockName = this.text(name);
        int pos = this.rangeFrom(modifiers.mPosition);
        if (!this.peek(42)) {
            this.error(name, "no type named '" + blockName + "'");
            return false;
        } else {
            this.nextToken();
            Context context = this.mCompiler.getContext();
            List<Type.Field> fields = new ArrayList();
            do {
                int startPos = this.position(this.peek());
                Modifiers fieldModifiers = this.Modifiers();
                Type baseType = this.TypeSpecifier(fieldModifiers);
                if (baseType == null) {
                    return false;
                }
                do {
                    long fieldName = this.expectIdentifier();
                    Type fieldType = this.ArraySpecifier(startPos, baseType);
                    if (fieldType == null) {
                        return false;
                    }
                    if (this.checkNext(48)) {
                        Expression init = this.AssignmentExpression();
                        if (init == null) {
                            return false;
                        }
                        context.error(init.mPosition, "initializers are not permitted in interface blocks");
                    }
                    fields.add(new Type.Field(this.rangeFrom(startPos), fieldModifiers, fieldType, this.text(fieldName)));
                } while (this.checkNext(47));
                this.expect(84, "';' to complete member declaration");
            } while (!this.checkNext(43));
            Type type = Type.makeStructType(context, pos, blockName, fields, true);
            context.getSymbolTable().insert(context, type);
            long instanceName = this.checkIdentifier();
            String instanceNameText = "";
            if (instanceName != -1L) {
                instanceNameText = this.text(instanceName);
                type = this.ArraySpecifier(pos, type);
                if (type == null) {
                    return false;
                }
            }
            this.expect(84, "';' to complete interface block");
            InterfaceBlock block = InterfaceBlock.convert(context, pos, modifiers, type, instanceNameText);
            if (block != null) {
                this.mUniqueElements.add(block);
                return true;
            } else {
                return false;
            }
        }
    }

    private boolean FunctionDeclarationRest(int start, Modifiers modifiers, Type returnType, long name) {
        List<Variable> parameters = new ArrayList();
        if (!this.peek(41)) {
            if (this.peek(39) && "void".equals(this.text(this.peek()))) {
                this.nextToken();
            } else {
                do {
                    Variable parameter = this.Parameter();
                    if (parameter == null) {
                        return false;
                    }
                    parameters.add(parameter);
                } while (this.checkNext(47));
            }
        }
        this.expect(41, "')' to complete parameter list");
        FunctionDecl decl = FunctionDecl.convert(this.mCompiler.getContext(), this.rangeFrom(start), modifiers, this.text(name), parameters, returnType);
        Context context = this.mCompiler.getContext();
        if (this.peek(84)) {
            this.nextToken();
            if (decl == null) {
                return false;
            } else {
                this.mUniqueElements.add(new FunctionPrototype(decl.mPosition, decl, context.isBuiltin()));
                return true;
            }
        } else {
            this.mCompiler.getContext().enterScope();
            boolean var14;
            try {
                if (decl != null) {
                    for (Variable param : decl.getParameters()) {
                        context.getSymbolTable().insert(this.mCompiler.getContext(), param);
                    }
                }
                long blockStart = this.peek();
                BlockStatement block = this.ScopedBlock();
                if (decl == null) {
                    return false;
                }
                int pos = this.rangeFrom(blockStart);
                FunctionDefinition function = FunctionDefinition.convert(this.mCompiler.getContext(), pos, decl, false, block);
                if (function != null) {
                    decl.setDefinition(function);
                    this.mUniqueElements.add(function);
                    return true;
                }
                var14 = false;
            } finally {
                this.mCompiler.getContext().leaveScope();
            }
            return var14;
        }
    }

    @Nullable
    private Variable Parameter() {
        int pos = this.position(this.peek());
        Modifiers modifiers = this.Modifiers();
        Type type = this.TypeSpecifier(modifiers);
        if (type == null) {
            return null;
        } else {
            long name = this.checkIdentifier();
            String nameText = "";
            if (name != -1L) {
                nameText = this.text(name);
                type = this.ArraySpecifier(pos, type);
                if (type == null) {
                    return null;
                }
            }
            return Variable.convert(this.mCompiler.getContext(), this.rangeFrom(pos), modifiers, type, nameText, (byte) 2);
        }
    }

    private BlockStatement ScopedBlock() {
        long start = this.expect(42, "'{'");
        List<Statement> statements = new ArrayList();
        while (!this.checkNext(43)) {
            Statement statement = this.Statement();
            if (statement != null) {
                statements.add(statement);
            }
        }
        int pos = this.rangeFrom(start);
        return BlockStatement.makeBlock(pos, statements);
    }

    private void GlobalVarDeclarationRest(int pos, Modifiers modifiers, Type baseType, long name) {
        boolean first = true;
        do {
            if (first) {
                first = false;
            } else {
                name = this.expectIdentifier();
            }
            Type type = this.ArraySpecifier(pos, baseType);
            if (type == null) {
                return;
            }
            Expression init = null;
            if (this.checkNext(48)) {
                init = this.AssignmentExpression();
                if (init == null) {
                    return;
                }
            }
            VariableDecl variableDecl = VariableDecl.convert(this.mCompiler.getContext(), this.rangeFrom(pos), modifiers, type, this.text(name), (byte) 1, init);
            if (variableDecl != null) {
                this.mUniqueElements.add(new GlobalVariableDecl(variableDecl));
            }
        } while (this.checkNext(47));
        this.expect(84, "';' to complete global variable declaration");
    }

    @Nullable
    private Expression operatorRight(Expression left, Operator op, @Nonnull Function<Parser, Expression> rightFn) {
        this.nextToken();
        Expression right = (Expression) rightFn.apply(this);
        if (right == null) {
            return null;
        } else {
            int pos = Position.range(left.getStartOffset(), right.getEndOffset());
            Expression result = BinaryExpression.convert(this.mCompiler.getContext(), pos, left, op, right);
            return result != null ? result : Poison.make(this.mCompiler.getContext(), pos);
        }
    }

    @Nullable
    private Expression Expression() {
        Expression result = this.AssignmentExpression();
        if (result == null) {
            return null;
        } else {
            while (this.peek(47)) {
                if ((result = this.operatorRight(result, Operator.COMMA, Parser::AssignmentExpression)) == null) {
                    return null;
                }
            }
            return result;
        }
    }

    @Nullable
    private Expression AssignmentExpression() {
        Expression result = this.ConditionalExpression();
        if (result == null) {
            return null;
        } else {
            Operator op;
            do {
                op = switch(Token.kind(this.peek())) {
                    case 48 ->
                        Operator.ASSIGN;
                    default ->
                        null;
                    case 74 ->
                        Operator.ADD_ASSIGN;
                    case 75 ->
                        Operator.SUB_ASSIGN;
                    case 76 ->
                        Operator.MUL_ASSIGN;
                    case 77 ->
                        Operator.DIV_ASSIGN;
                    case 78 ->
                        Operator.MOD_ASSIGN;
                    case 79 ->
                        Operator.SHL_ASSIGN;
                    case 80 ->
                        Operator.SHR_ASSIGN;
                    case 81 ->
                        Operator.AND_ASSIGN;
                    case 82 ->
                        Operator.OR_ASSIGN;
                    case 83 ->
                        Operator.XOR_ASSIGN;
                };
                if (op == null) {
                    return result;
                }
            } while ((result = this.operatorRight(result, op, Parser::AssignmentExpression)) != null);
            return null;
        }
    }

    @Nonnull
    private Expression expressionOrPoison(int pos, @Nullable Expression expr) {
        if (expr == null) {
            expr = Poison.make(this.mCompiler.getContext(), pos);
        }
        return expr;
    }

    @Nullable
    private Expression ConditionalExpression() {
        Expression base = this.LogicalOrExpression();
        if (base == null) {
            return null;
        } else if (!this.peek(53)) {
            return base;
        } else {
            this.nextToken();
            Expression whenTrue = this.Expression();
            if (whenTrue == null) {
                return null;
            } else {
                this.expect(54, "':'");
                Expression whenFalse = this.AssignmentExpression();
                if (whenFalse == null) {
                    return null;
                } else {
                    int pos = Position.range(base.getStartOffset(), whenFalse.getEndOffset());
                    return this.expressionOrPoison(pos, ConditionalExpression.convert(this.mCompiler.getContext(), pos, base, whenTrue, whenFalse));
                }
            }
        }
    }

    @Nullable
    private Expression LogicalOrExpression() {
        Expression result = this.LogicalXorExpression();
        if (result == null) {
            return null;
        } else {
            while (this.peek(69)) {
                if ((result = this.operatorRight(result, Operator.LOGICAL_OR, Parser::LogicalXorExpression)) == null) {
                    return null;
                }
            }
            return result;
        }
    }

    @Nullable
    private Expression LogicalXorExpression() {
        Expression result = this.LogicalAndExpression();
        if (result == null) {
            return null;
        } else {
            while (this.peek(70)) {
                if ((result = this.operatorRight(result, Operator.LOGICAL_XOR, Parser::LogicalAndExpression)) == null) {
                    return null;
                }
            }
            return result;
        }
    }

    @Nullable
    private Expression LogicalAndExpression() {
        Expression result = this.BitwiseOrExpression();
        if (result == null) {
            return null;
        } else {
            while (this.peek(68)) {
                if ((result = this.operatorRight(result, Operator.LOGICAL_AND, Parser::BitwiseOrExpression)) == null) {
                    return null;
                }
            }
            return result;
        }
    }

    @Nullable
    private Expression BitwiseOrExpression() {
        Expression result = this.BitwiseXorExpression();
        if (result == null) {
            return null;
        } else {
            while (this.peek(72)) {
                if ((result = this.operatorRight(result, Operator.BITWISE_OR, Parser::BitwiseXorExpression)) == null) {
                    return null;
                }
            }
            return result;
        }
    }

    @Nullable
    private Expression BitwiseXorExpression() {
        Expression result = this.BitwiseAndExpression();
        if (result == null) {
            return null;
        } else {
            while (this.peek(73)) {
                if ((result = this.operatorRight(result, Operator.BITWISE_XOR, Parser::BitwiseAndExpression)) == null) {
                    return null;
                }
            }
            return result;
        }
    }

    @Nullable
    private Expression BitwiseAndExpression() {
        Expression result = this.EqualityExpression();
        if (result == null) {
            return null;
        } else {
            while (this.peek(71)) {
                if ((result = this.operatorRight(result, Operator.BITWISE_AND, Parser::EqualityExpression)) == null) {
                    return null;
                }
            }
            return result;
        }
    }

    @Nullable
    private Expression EqualityExpression() {
        Expression result = this.RelationalExpression();
        if (result == null) {
            return null;
        } else {
            Operator op;
            do {
                op = switch(Token.kind(this.peek())) {
                    case 55 ->
                        Operator.EQ;
                    case 58 ->
                        Operator.NE;
                    default ->
                        null;
                };
                if (op == null) {
                    return result;
                }
            } while ((result = this.operatorRight(result, op, Parser::RelationalExpression)) != null);
            return null;
        }
    }

    @Nullable
    private Expression RelationalExpression() {
        Expression result = this.ShiftExpression();
        if (result == null) {
            return null;
        } else {
            Operator op;
            do {
                op = switch(Token.kind(this.peek())) {
                    case 49 ->
                        Operator.LT;
                    case 50 ->
                        Operator.GT;
                    default ->
                        null;
                    case 56 ->
                        Operator.LE;
                    case 57 ->
                        Operator.GE;
                };
                if (op == null) {
                    return result;
                }
            } while ((result = this.operatorRight(result, op, Parser::ShiftExpression)) != null);
            return null;
        }
    }

    @Nullable
    private Expression ShiftExpression() {
        Expression result = this.AdditiveExpression();
        if (result == null) {
            return null;
        } else {
            Operator op;
            do {
                op = switch(Token.kind(this.peek())) {
                    case 66 ->
                        Operator.SHL;
                    case 67 ->
                        Operator.SHR;
                    default ->
                        null;
                };
                if (op == null) {
                    return result;
                }
            } while ((result = this.operatorRight(result, op, Parser::AdditiveExpression)) != null);
            return null;
        }
    }

    @Nullable
    private Expression AdditiveExpression() {
        Expression result = this.MultiplicativeExpression();
        if (result == null) {
            return null;
        } else {
            Operator op;
            do {
                op = switch(Token.kind(this.peek())) {
                    case 61 ->
                        Operator.ADD;
                    case 62 ->
                        Operator.SUB;
                    default ->
                        null;
                };
                if (op == null) {
                    return result;
                }
            } while ((result = this.operatorRight(result, op, Parser::MultiplicativeExpression)) != null);
            return null;
        }
    }

    @Nullable
    private Expression MultiplicativeExpression() {
        Expression result = this.UnaryExpression();
        if (result == null) {
            return null;
        } else {
            Operator op;
            do {
                op = switch(Token.kind(this.peek())) {
                    case 63 ->
                        Operator.MUL;
                    case 64 ->
                        Operator.DIV;
                    case 65 ->
                        Operator.MOD;
                    default ->
                        null;
                };
                if (op == null) {
                    return result;
                }
            } while ((result = this.operatorRight(result, op, Parser::UnaryExpression)) != null);
            return null;
        }
    }

    @Nullable
    private Expression UnaryExpression() {
        long prefix = this.peek();
        Operator op = switch(Token.kind(prefix)) {
            case 51 ->
                Operator.LOGICAL_NOT;
            case 52 ->
                Operator.BITWISE_NOT;
            default ->
                null;
            case 59 ->
                Operator.INC;
            case 60 ->
                Operator.DEC;
            case 61 ->
                Operator.ADD;
            case 62 ->
                Operator.SUB;
        };
        if (op != null) {
            this.nextToken();
            Expression base = this.UnaryExpression();
            if (base == null) {
                return null;
            } else {
                int pos = Position.range(Token.offset(prefix), base.getEndOffset());
                return this.expressionOrPoison(pos, PrefixExpression.convert(this.mCompiler.getContext(), pos, op, base));
            }
        } else {
            return this.PostfixExpression();
        }
    }

    @Nullable
    private Expression PostfixExpression() {
        Expression result = this.PrimaryExpression();
        if (result == null) {
            return null;
        } else {
            while (true) {
                long t = this.peek();
                switch(Token.kind(t)) {
                    case 40:
                        {
                            this.nextToken();
                            List<Expression> args = new ArrayList();
                            if (!this.peek(41)) {
                                if (this.peek(39) && "void".equals(this.text(this.peek()))) {
                                    this.nextToken();
                                } else {
                                    do {
                                        Expression expr = this.AssignmentExpression();
                                        if (expr == null) {
                                            return null;
                                        }
                                        args.add(expr);
                                    } while (this.checkNext(47));
                                }
                            }
                            this.expect(41, "')' to complete invocation");
                            int pos = this.rangeFrom(result.mPosition);
                            result = this.expressionOrPoison(pos, FunctionCall.convert(this.mCompiler.getContext(), pos, result, args));
                            break;
                        }
                    case 44:
                        {
                            this.nextToken();
                            Expression index = null;
                            if (!this.peek(45)) {
                                index = this.Expression();
                                if (index == null) {
                                    return null;
                                }
                            }
                            this.expect(45, "']' to complete array specifier");
                            int pos = this.rangeFrom(result.mPosition);
                            result = this.expressionOrPoison(pos, IndexExpression.convert(this.mCompiler.getContext(), pos, result, index));
                            break;
                        }
                    case 46:
                        {
                            this.nextToken();
                            long name = this.expect(39, "identifier");
                            String text = this.text(name);
                            int pos = this.rangeFrom(result.mPosition);
                            int namePos = this.rangeFrom(name);
                            result = this.expressionOrPoison(pos, FieldAccess.convert(this.mCompiler.getContext(), pos, result, namePos, text));
                            break;
                        }
                    case 59:
                    case 60:
                        {
                            this.nextToken();
                            Operator op = Token.kind(t) == 59 ? Operator.INC : Operator.DEC;
                            int pos = this.rangeFrom(result.mPosition);
                            result = this.expressionOrPoison(pos, PostfixExpression.convert(this.mCompiler.getContext(), pos, result, op));
                            break;
                        }
                    default:
                        return result;
                }
            }
        }
    }

    @Nullable
    private Expression PrimaryExpression() {
        long t = this.peek();
        ???;
    }

    @Nullable
    private Literal IntLiteral() {
        long token = this.expect(1, "integer literal");
        String s = this.text(token);
        if (s.endsWith("u") || s.endsWith("U")) {
            s = s.substring(0, s.length() - 1);
        }
        try {
            long value = Long.decode(s);
            if (value <= 4294967295L) {
                return Literal.makeInteger(this.mCompiler.getContext(), this.position(token), value);
            } else {
                this.error(token, "integer value is too large: " + s);
                return null;
            }
        } catch (NumberFormatException var6) {
            this.error(token, "invalid integer value: " + var6.getMessage());
            return null;
        }
    }

    @Nullable
    private Literal FloatLiteral() {
        long token = this.expect(2, "float literal");
        String s = this.text(token);
        try {
            float value = Float.parseFloat(s);
            if (Float.isFinite(value)) {
                return Literal.makeFloat(this.mCompiler.getContext(), this.position(token), value);
            } else {
                this.error(token, "floating-point value is too large: " + s);
                return null;
            }
        } catch (NumberFormatException var5) {
            this.error(token, "invalid floating-point value: " + var5.getMessage());
            return null;
        }
    }

    @Nullable
    private Literal BooleanLiteral() {
        long token = this.nextToken();
        return switch(Token.kind(token)) {
            case 3 ->
                Literal.makeBoolean(this.mCompiler.getContext(), this.position(token), true);
            case 4 ->
                Literal.makeBoolean(this.mCompiler.getContext(), this.position(token), false);
            default ->
                {
                }
        };
    }

    @Nonnull
    private Modifiers Modifiers() {
        long start = this.peek();
        Modifiers modifiers = new Modifiers(-1);
        if (this.checkNext(33)) {
            this.Layout(modifiers);
        }
        while (true) {
            int mask = switch(Token.kind(this.peek())) {
                case 17 ->
                    32;
                case 18 ->
                    64;
                case 19 ->
                    96;
                case 20 ->
                    8;
                case 21 ->
                    16;
                case 22 ->
                    4096;
                case 23 ->
                    8192;
                case 24 ->
                    1;
                case 25 ->
                    2;
                case 26 ->
                    4;
                case 27 ->
                    128;
                case 28 ->
                    256;
                case 29 ->
                    512;
                case 30 ->
                    1024;
                case 31 ->
                    2048;
                case 32 ->
                    16384;
                default ->
                    0;
                case 35 ->
                    65536;
                case 36 ->
                    131072;
                case 37 ->
                    32768;
            };
            if (mask == 0) {
                modifiers.mPosition = this.rangeFrom(start);
                return modifiers;
            }
            long token = this.nextToken();
            modifiers.setFlag(this.mCompiler.getContext(), mask, this.position(token));
        }
    }

    private void Layout(Modifiers modifiers) {
        this.expect(40, "'('");
        do {
            long name = this.expect(39, "identifier");
            String text = this.text(name);
            int pos = this.position(name);
            int mask = switch(text) {
                case "origin_upper_left" ->
                    1;
                case "pixel_center_integer" ->
                    2;
                case "early_fragment_tests" ->
                    4;
                case "blend_support_all_equations" ->
                    8;
                case "push_constant" ->
                    16;
                case "location" ->
                    32;
                case "component" ->
                    64;
                case "index" ->
                    128;
                case "binding" ->
                    256;
                case "offset" ->
                    512;
                case "set" ->
                    1024;
                case "input_attachment_index" ->
                    2048;
                case "builtin" ->
                    4096;
                default ->
                    0;
            };
            if (mask != 0) {
                modifiers.setLayoutFlag(this.mCompiler.getContext(), mask, text, pos);
                Layout layout = modifiers.layout();
                switch(mask) {
                    case 32:
                        layout.mLocation = this.LayoutInt();
                        break;
                    case 64:
                        layout.mComponent = this.LayoutInt();
                        break;
                    case 128:
                        layout.mIndex = this.LayoutInt();
                        break;
                    case 256:
                        layout.mBinding = this.LayoutInt();
                        break;
                    case 512:
                        layout.mOffset = this.LayoutInt();
                        break;
                    case 1024:
                        layout.mSet = this.LayoutInt();
                        break;
                    case 2048:
                        layout.mInputAttachmentIndex = this.LayoutInt();
                        break;
                    case 4096:
                        layout.mBuiltin = this.LayoutBuiltin();
                }
            } else {
                int builtin = findBuiltinValue(text);
                if (builtin != -1) {
                    modifiers.setLayoutFlag(this.mCompiler.getContext(), 4096, text, pos);
                    modifiers.layout().mBuiltin = builtin;
                } else {
                    this.warning(name, "unrecognized layout qualifier '" + text + "'");
                    if (this.checkNext(48)) {
                        this.nextToken();
                    }
                }
            }
        } while (this.checkNext(47));
        this.expect(41, "')'");
    }

    private int LayoutInt() {
        this.expect(48, "'='");
        long token = this.expect(1, "integer literal");
        return this.LayoutIntValue(token);
    }

    private int LayoutBuiltin() {
        this.expect(48, "'='");
        if (this.peek(1)) {
            long token = this.nextToken();
            return this.LayoutIntValue(token);
        } else {
            long name = this.expectIdentifier();
            String text = this.text(name);
            int builtin = findBuiltinValue(text);
            if (builtin == -1) {
                this.error(name, "unrecognized built-in name '" + text + "'");
            }
            return builtin;
        }
    }

    private int LayoutIntValue(long token) {
        String s = this.text(token);
        if (s.endsWith("u") || s.endsWith("U")) {
            s = s.substring(0, s.length() - 1);
        }
        try {
            long value = Long.decode(s);
            if (value <= 2147483647L) {
                return (int) value;
            } else {
                this.error(token, "integer value is too large: " + s);
                return -1;
            }
        } catch (NumberFormatException var6) {
            this.error(token, "invalid integer value: " + var6.getMessage());
            return -1;
        }
    }

    private static int findBuiltinValue(@Nonnull String text) {
        return switch(text) {
            case "position" ->
                0;
            case "vertex_index" ->
                42;
            case "instance_index" ->
                43;
            case "frag_coord" ->
                15;
            case "front_facing" ->
                17;
            case "sample_mask" ->
                20;
            case "frag_depth" ->
                22;
            case "num_workgroups" ->
                24;
            case "workgroup_id" ->
                26;
            case "local_invocation_id" ->
                27;
            case "global_invocation_id" ->
                28;
            case "local_invocation_index" ->
                29;
            default ->
                -1;
        };
    }

    @Nullable
    private Type TypeSpecifier(Modifiers modifiers) {
        long start = this.expect(39, "a type name");
        String name = this.text(start);
        Symbol symbol = this.mCompiler.getContext().getSymbolTable().find(name);
        if (symbol == null) {
            this.error(start, "no type named '" + name + "'");
            return this.mCompiler.getContext().getTypes().mPoison;
        } else if (symbol instanceof Type result) {
            if (result.isInterfaceBlock()) {
                this.error(start, "expected a type, found interface block '" + name + "'");
                return this.mCompiler.getContext().getTypes().mInvalid;
            } else {
                return this.ArraySpecifier(this.position(start), result);
            }
        } else {
            this.error(start, "symbol '" + name + "' is not a type");
            return this.mCompiler.getContext().getTypes().mPoison;
        }
    }

    @Nullable
    private Type ArraySpecifier(int startPos, Type type) {
        Context context = this.mCompiler.getContext();
        while (this.peek(44)) {
            this.nextToken();
            Expression size = null;
            if (!this.peek(45)) {
                size = this.Expression();
                if (size == null) {
                    return null;
                }
            }
            this.expect(45, "']' to complete array specifier");
            int pos = this.rangeFrom(startPos);
            int arraySize;
            if (size != null) {
                arraySize = type.convertArraySize(this.mCompiler.getContext(), pos, size);
            } else if (!type.isUsableInArray(this.mCompiler.getContext(), pos)) {
                arraySize = 0;
            } else {
                arraySize = -1;
            }
            if (arraySize == 0) {
                type = context.getTypes().mPoison;
            } else {
                type = context.getSymbolTable().getArrayType(type, arraySize);
            }
        }
        return type;
    }

    @Nullable
    private Statement VarDeclarationRest(int pos, Modifiers modifiers, Type baseType) {
        long name = this.expectIdentifier();
        Type type = this.ArraySpecifier(pos, baseType);
        if (type == null) {
            return null;
        } else {
            Expression init = null;
            if (this.checkNext(48)) {
                init = this.AssignmentExpression();
                if (init == null) {
                    return null;
                }
            }
            Statement result = VariableDecl.convert(this.mCompiler.getContext(), this.rangeFrom(name), modifiers, type, this.text(name), (byte) 0, init);
            while (this.checkNext(47)) {
                name = this.expectIdentifier();
                type = this.ArraySpecifier(pos, baseType);
                if (type == null) {
                    break;
                }
                init = null;
                if (this.checkNext(48)) {
                    init = this.AssignmentExpression();
                    if (init == null) {
                        break;
                    }
                }
                Statement next = VariableDecl.convert(this.mCompiler.getContext(), this.rangeFrom(name), modifiers, type, this.text(name), (byte) 0, init);
                result = BlockStatement.makeCompound(result, next);
            }
            this.expect(84, "';' to complete local variable declaration");
            pos = this.rangeFrom(pos);
            return this.statementOrEmpty(pos, result);
        }
    }

    @Nullable
    private Statement VarDeclarationOrExpressionStatement() {
        long peek = this.peek();
        if (Token.kind(peek) == 20) {
            int pos = this.position(peek);
            Modifiers modifiers = this.Modifiers();
            Type type = this.TypeSpecifier(modifiers);
            return type == null ? null : this.VarDeclarationRest(pos, modifiers, type);
        } else if (this.mCompiler.getContext().getSymbolTable().isType(this.text(peek))) {
            int pos = this.position(peek);
            Modifiers modifiers = new Modifiers(pos);
            Type type = this.TypeSpecifier(modifiers);
            return type == null ? null : this.VarDeclarationRest(pos, modifiers, type);
        } else {
            return this.ExpressionStatement();
        }
    }

    @Nullable
    private Statement ExpressionStatement() {
        Expression expr = this.Expression();
        if (expr == null) {
            return null;
        } else {
            this.expect(84, "';' to complete expression statement");
            int pos = expr.mPosition;
            return this.statementOrEmpty(pos, ExpressionStatement.convert(this.mCompiler.getContext(), expr));
        }
    }

    private Type StructDeclaration() {
        long start = this.peek();
        this.expect(34, "'struct'");
        long typeName = this.expectIdentifier();
        this.expect(42, "'{'");
        return null;
    }

    @Nonnull
    private Statement statementOrEmpty(int pos, @Nullable Statement stmt) {
        if (stmt == null) {
            stmt = new EmptyStatement(pos);
        }
        return stmt;
    }

    @Nullable
    private Statement Statement() {
        ???;
    }

    @Nullable
    private Statement IfStatement() {
        long start = this.expect(10, "'if'");
        this.expect(40, "'('");
        Expression test = this.Expression();
        if (test == null) {
            return null;
        } else {
            this.expect(41, "')'");
            Statement whenTrue = this.Statement();
            if (whenTrue == null) {
                return null;
            } else {
                Statement whenFalse = null;
                if (this.checkNext(11)) {
                    whenFalse = this.Statement();
                    if (whenFalse == null) {
                        return null;
                    }
                }
                int pos = this.rangeFrom(start);
                return this.statementOrEmpty(pos, IfStatement.convert(this.mCompiler.getContext(), pos, test, whenTrue, whenFalse));
            }
        }
    }

    private Statement SwitchStatement() {
        return null;
    }

    @Nullable
    private Statement ForStatement() {
        long start = this.expect(8, "'for'");
        this.expect(40, "'('");
        this.mCompiler.getContext().enterScope();
        try {
            Statement init = null;
            if (this.peek(84)) {
                this.nextToken();
            } else {
                init = this.VarDeclarationOrExpressionStatement();
                if (init == null) {
                    return null;
                }
            }
            Expression cond = null;
            if (!this.peek(84)) {
                cond = this.Expression();
                if (cond == null) {
                    return null;
                }
            }
            this.expect(84, "';' to complete condition statement");
            Expression step = null;
            if (!this.peek(84)) {
                step = this.Expression();
                if (step == null) {
                    return null;
                }
            }
            this.expect(41, "')' to complete 'for' statement");
            Statement statement = this.Statement();
            if (statement != null) {
                int pos = this.rangeFrom(start);
                return this.statementOrEmpty(pos, ForLoop.convert(this.mCompiler.getContext(), pos, init, cond, step, statement));
            } else {
                return null;
            }
        } finally {
            this.mCompiler.getContext().leaveScope();
        }
    }
}
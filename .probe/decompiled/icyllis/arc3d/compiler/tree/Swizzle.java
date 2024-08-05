package icyllis.arc3d.compiler.tree;

import icyllis.arc3d.compiler.ConstantFolder;
import icyllis.arc3d.compiler.Context;
import icyllis.arc3d.compiler.Position;
import icyllis.arc3d.compiler.analysis.Analysis;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class Swizzle extends Expression {

    public static final byte X = 0;

    public static final byte Y = 1;

    public static final byte Z = 2;

    public static final byte W = 3;

    public static final byte R = 4;

    public static final byte G = 5;

    public static final byte B = 6;

    public static final byte A = 7;

    public static final byte S = 8;

    public static final byte T = 9;

    public static final byte P = 10;

    public static final byte Q = 11;

    public static final byte ZERO = 16;

    public static final byte ONE = 17;

    private final Expression mBase;

    private final byte[] mComponents;

    private Swizzle(int position, Type type, Expression base, byte[] components) {
        super(position, type);
        assert components.length >= 1 && components.length <= 4;
        this.mBase = base;
        this.mComponents = components;
    }

    private static boolean validateNameSet(byte[] components) {
        int set = -1;
        for (byte component : components) {
            int newSet;
            switch(component) {
                case 0:
                case 1:
                case 2:
                case 3:
                    newSet = 0;
                    break;
                case 4:
                case 5:
                case 6:
                case 7:
                    newSet = 1;
                    break;
                case 8:
                case 9:
                case 10:
                case 11:
                    newSet = 2;
                    break;
                case 12:
                case 13:
                case 14:
                case 15:
                default:
                    return false;
                case 16:
                case 17:
                    continue;
            }
            if (set == -1) {
                set = newSet;
            } else if (set != newSet) {
                return false;
            }
        }
        return true;
    }

    @Nullable
    private static Expression optimizeSwizzle(@Nonnull Context context, int pos, @Nonnull ConstructorCompound base, byte[] components, int numComponents) {
        Expression[] baseArguments = base.getArguments();
        Type exprType = base.getType();
        Type componentType = exprType.getComponentType();
        int numConstructorArgs = exprType.getRows();
        short[] argMap = new short[4];
        int writeIdx = 0;
        for (int argIdx = 0; argIdx < baseArguments.length; argIdx++) {
            Expression arg = baseArguments[argIdx];
            Type argType = arg.getType();
            if (!argType.isScalar() && !argType.isVector()) {
                return null;
            }
            int argComps = argType.getComponents();
            for (int componentIdx = 0; componentIdx < argComps; componentIdx++) {
                argMap[writeIdx] = (short) (argIdx | componentIdx << 8);
                writeIdx++;
            }
        }
        assert writeIdx == numConstructorArgs;
        byte[] exprUsed = new byte[4];
        for (byte c : components) {
            exprUsed[(byte) argMap[c]]++;
        }
        for (int index = 0; index < numConstructorArgs; index++) {
            byte constructorArgIndex = (byte) argMap[index];
            Expression baseArg = baseArguments[constructorArgIndex];
            if (exprUsed[constructorArgIndex] > 1 && !Analysis.isTrivialExpression(baseArg)) {
                return null;
            }
            if (exprUsed[constructorArgIndex] != 1 && Analysis.hasSideEffects(baseArg)) {
                return null;
            }
        }
        class ReorderedArgument {

            final byte mArgIndex;

            final byte[] mComponents = new byte[4];

            byte mNumComponents = 0;

            ReorderedArgument(byte argIndex) {
                this.mArgIndex = argIndex;
            }
        }
        List<ReorderedArgument> reorderedArgs = new ArrayList(4);
        for (byte c : components) {
            short argument = argMap[c];
            byte argumentIndex = (byte) argument;
            byte argumentComponent = (byte) (argument >> 8);
            Expression baseArgx = baseArguments[argumentIndex];
            if (baseArgx.getType().isScalar()) {
                assert argumentComponent == 0;
                reorderedArgs.add(new ReorderedArgument(argumentIndex));
            } else {
                assert baseArgx.getType().isVector();
                assert argumentComponent < baseArgx.getType().getRows();
                if (!reorderedArgs.isEmpty() && ((ReorderedArgument) reorderedArgs.get(reorderedArgs.size() - 1)).mArgIndex == argumentIndex) {
                    ReorderedArgument last = (ReorderedArgument) reorderedArgs.get(reorderedArgs.size() - 1);
                    assert last.mNumComponents != 0;
                    last.mComponents[last.mNumComponents++] = argumentComponent;
                } else {
                    ReorderedArgument toAdd = new ReorderedArgument(argumentIndex);
                    toAdd.mComponents[toAdd.mNumComponents++] = argumentComponent;
                    reorderedArgs.add(toAdd);
                }
            }
        }
        Expression[] newArgs = new Expression[numComponents];
        for (int i = 0; i < reorderedArgs.size(); i++) {
            ReorderedArgument reorderedArg = (ReorderedArgument) reorderedArgs.get(i);
            Expression newArg = baseArguments[reorderedArg.mArgIndex].clone();
            if (reorderedArg.mNumComponents == 0) {
                newArgs[i] = newArg;
            } else {
                newArgs[i] = make(context, pos, newArg, reorderedArg.mComponents, reorderedArg.mNumComponents);
            }
        }
        return ConstructorCompound.make(context, pos, componentType.toVector(context, numComponents), newArgs);
    }

    @Nullable
    public static Expression convert(@Nonnull Context context, int position, @Nonnull Expression base, int maskPosition, @Nonnull String maskString) {
        if (maskString.length() > 4) {
            context.error(maskPosition, "too many components in swizzle mask");
            return null;
        } else {
            byte[] inComponents = new byte[maskString.length()];
            for (int i = 0; i < maskString.length(); i++) {
                char field = maskString.charAt(i);
                byte c;
                switch(field) {
                    case '0':
                        c = 16;
                        break;
                    case '1':
                        c = 17;
                        break;
                    case 'a':
                        c = 7;
                        break;
                    case 'b':
                        c = 6;
                        break;
                    case 'g':
                        c = 5;
                        break;
                    case 'p':
                        c = 10;
                        break;
                    case 'q':
                        c = 11;
                        break;
                    case 'r':
                        c = 4;
                        break;
                    case 's':
                        c = 8;
                        break;
                    case 't':
                        c = 9;
                        break;
                    case 'w':
                        c = 3;
                        break;
                    case 'x':
                        c = 0;
                        break;
                    case 'y':
                        c = 1;
                        break;
                    case 'z':
                        c = 2;
                        break;
                    default:
                        int offset = Position.getStartOffset(maskPosition) + i;
                        context.error(Position.range(offset, offset + 1), String.format("invalid swizzle component '%c'", field));
                        return null;
                }
                inComponents[i] = c;
            }
            if (!validateNameSet(inComponents)) {
                context.error(maskPosition, "swizzle components '" + maskString + "' do not come from the same name set");
                return null;
            } else {
                Type baseType = base.getType();
                if (!baseType.isVector() && !baseType.isScalar()) {
                    context.error(position, "cannot swizzle value of type '" + baseType + "'");
                    return null;
                } else {
                    byte[] maskComponents = new byte[inComponents.length];
                    byte numComponents = 0;
                    boolean foundXYZW = false;
                    int i = 0;
                    label98: while (true) {
                        if (i >= inComponents.length) {
                            if (!foundXYZW) {
                                context.error(maskPosition, "swizzle must refer to base expression");
                                return null;
                            }
                            base = baseType.coerceExpression(context, base);
                            if (base == null) {
                                return null;
                            }
                            Expression expr = make(context, position, base, maskComponents, numComponents);
                            if (numComponents == inComponents.length) {
                                return expr;
                            }
                            List<Expression> constructorArgs = new ArrayList(3);
                            constructorArgs.add(expr);
                            Type scalarType = baseType.getComponentType();
                            byte maskFieldIdx = 0;
                            byte constantFieldIdx = numComponents;
                            byte constantZeroIdx = -1;
                            byte constantOneIdx = -1;
                            numComponents = 0;
                            for (byte component : inComponents) {
                                switch(component) {
                                    case 16:
                                        if (constantZeroIdx == -1) {
                                            constructorArgs.add(Literal.make(position, 0.0, scalarType));
                                            constantZeroIdx = constantFieldIdx++;
                                        }
                                        maskComponents[numComponents++] = constantZeroIdx;
                                        break;
                                    case 17:
                                        if (constantOneIdx == -1) {
                                            constructorArgs.add(Literal.make(position, 1.0, scalarType));
                                            constantOneIdx = constantFieldIdx++;
                                        }
                                        maskComponents[numComponents++] = constantOneIdx;
                                        break;
                                    default:
                                        maskComponents[numComponents++] = maskFieldIdx++;
                                }
                            }
                            Expression var28 = ConstructorCompound.make(context, position, scalarType.toVector(context, constantFieldIdx), (Expression[]) constructorArgs.toArray(new Expression[0]));
                            return make(context, position, var28, maskComponents, numComponents);
                        }
                        byte c = inComponents[i];
                        switch(c) {
                            case 0:
                            case 4:
                            case 8:
                                foundXYZW = true;
                                maskComponents[numComponents++] = 0;
                                break;
                            case 1:
                            case 5:
                            case 9:
                                foundXYZW = true;
                                if (baseType.getRows() >= 2) {
                                    maskComponents[numComponents++] = 1;
                                    break;
                                }
                            case 2:
                            case 6:
                            case 10:
                                foundXYZW = true;
                                if (baseType.getRows() >= 3) {
                                    maskComponents[numComponents++] = 2;
                                    break;
                                }
                            case 3:
                            case 7:
                            case 11:
                                foundXYZW = true;
                                if (baseType.getRows() >= 4) {
                                    maskComponents[numComponents++] = 3;
                                    break;
                                }
                            case 12:
                            case 13:
                            case 14:
                            case 15:
                            default:
                                break label98;
                            case 16:
                            case 17:
                        }
                        i++;
                    }
                    int offset = Position.getStartOffset(maskPosition) + i;
                    context.error(Position.range(offset, offset + 1), String.format("swizzle component '%c' is out of range for type '%s'", maskString.charAt(i), baseType));
                    return null;
                }
            }
        }
    }

    @Nonnull
    public static Expression make(@Nonnull Context context, int position, @Nonnull Expression base, byte[] components, int numComponents) {
        Type baseType = base.getType();
        assert baseType.isVector() || baseType.isScalar();
        for (int i = 0; i < numComponents; i++) {
            byte component = components[i];
            assert component == 0 || component == 1 || component == 2 || component == 3;
        }
        assert numComponents >= 1 && numComponents <= 4;
        if (baseType.isScalar()) {
            return ConstructorScalar2Vector.make(position, baseType.toVector(context, numComponents), base);
        } else {
            if (numComponents == baseType.getRows()) {
                boolean identity = true;
                for (int ix = 0; ix < numComponents; ix++) {
                    if (components[ix] != ix) {
                        identity = false;
                        break;
                    }
                }
                if (identity) {
                    base.mPosition = position;
                    return base;
                }
            }
            if (!(base instanceof Swizzle b)) {
                Expression value = ConstantFolder.getConstantValueForVariable(base);
                if (value instanceof ConstructorScalar2Vector ctor) {
                    Type ctorType = ctor.getComponentType().toVector(context, numComponents);
                    return ConstructorScalar2Vector.make(position, ctorType, ctor.getArguments()[0].clone());
                } else if (value instanceof ConstructorCompoundCast ctor) {
                    Type ctorType = ctor.getComponentType().toVector(context, numComponents);
                    Expression swizzled = make(context, position, ctor.getArguments()[0].clone(), components, numComponents);
                    Objects.requireNonNull(swizzled);
                    return ctorType.getRows() > 1 ? ConstructorCompoundCast.make(position, ctorType, swizzled) : ConstructorScalarCast.make(context, position, ctorType, swizzled);
                } else {
                    if (value.getKind() == Node.ExpressionKind.CONSTRUCTOR_COMPOUND) {
                        ConstructorCompound ctor = (ConstructorCompound) value;
                        Expression replacement = optimizeSwizzle(context, position, ctor, components, numComponents);
                        if (replacement != null) {
                            return replacement;
                        }
                    }
                    return new Swizzle(position, baseType.getComponentType().toVector(context, numComponents), base, Arrays.copyOf(components, numComponents));
                }
            } else {
                byte[] combined = new byte[numComponents];
                for (int ix = 0; ix < numComponents; ix++) {
                    byte c = components[ix];
                    combined[ix] = b.getComponents()[c];
                }
                return make(context, position, b.getBase(), combined, numComponents);
            }
        }
    }

    @Override
    public Node.ExpressionKind getKind() {
        return Node.ExpressionKind.SWIZZLE;
    }

    @Override
    public boolean accept(@Nonnull TreeVisitor visitor) {
        return visitor.visitSwizzle(this) ? true : this.mBase != null && this.mBase.accept(visitor);
    }

    public Expression getBase() {
        return this.mBase;
    }

    public byte[] getComponents() {
        return this.mComponents;
    }

    @Nonnull
    @Override
    public Expression clone(int position) {
        return new Swizzle(position, this.getType(), this.mBase.clone(), this.mComponents);
    }

    @Nonnull
    @Override
    public String toString(int parentPrecedence) {
        StringBuilder result = new StringBuilder(this.mBase.toString(2));
        result.append('.');
        for (byte component : this.mComponents) {
            result.append(switch(component) {
                case 0 ->
                    'x';
                case 1 ->
                    'y';
                case 2 ->
                    'z';
                case 3 ->
                    'w';
                default ->
                    throw new IllegalStateException();
            });
        }
        return result.toString();
    }
}
package icyllis.arc3d.compiler.tree;

import icyllis.arc3d.compiler.BuiltinTypes;
import icyllis.arc3d.compiler.ConstantFolder;
import icyllis.arc3d.compiler.Context;
import java.util.List;
import java.util.OptionalLong;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class Type extends Symbol {

    public static final int kUnsizedArray = -1;

    public static final int kMaxNestingDepth = 8;

    public static final byte kArray_TypeKind = 0;

    public static final byte kGeneric_TypeKind = 1;

    public static final byte kMatrix_TypeKind = 2;

    public static final byte kOther_TypeKind = 3;

    public static final byte kSampler_TypeKind = 4;

    public static final byte kScalar_TypeKind = 5;

    public static final byte kStruct_TypeKind = 6;

    public static final byte kVector_TypeKind = 7;

    public static final byte kVoid_TypeKind = 8;

    public static final byte kFloat_ScalarKind = 0;

    public static final byte kSigned_ScalarKind = 1;

    public static final byte kUnsigned_ScalarKind = 2;

    public static final byte kBoolean_ScalarKind = 3;

    public static final byte kNonScalar_ScalarKind = 4;

    private final String mDesc;

    private final byte mTypeKind;

    Type(String name, String desc, byte kind) {
        this(name, desc, kind, -1);
    }

    Type(String name, String desc, byte kind, int position) {
        super(position, name);
        this.mDesc = desc;
        this.mTypeKind = kind;
    }

    @Nonnull
    public static Type makeAliasType(String name, Type type) {
        assert type == type.resolve();
        return new Type.AliasType(name, type);
    }

    @Nonnull
    public static Type makeGenericType(String name, Type... types) {
        for (Type type : types) {
            assert type == type.resolve();
        }
        return new Type.GenericType(name, types);
    }

    @Nonnull
    public static Type makeScalarType(String name, String desc, byte kind, int rank, int width) {
        return makeScalarType(name, desc, kind, rank, width, width);
    }

    @Nonnull
    public static Type makeScalarType(String name, String desc, byte kind, int rank, int minWidth, int width) {
        assert minWidth == width || width == 32 && minWidth < width;
        return new Type.ScalarType(name, desc, kind, rank, minWidth, width);
    }

    @Nonnull
    public static Type makeVectorType(String name, String desc, Type componentType, int rows) {
        assert componentType == componentType.resolve();
        return new Type.VectorType(name, desc, componentType, rows);
    }

    @Nonnull
    public static Type makeMatrixType(String name, String desc, Type columnType, int cols) {
        assert columnType == columnType.resolve();
        return new Type.MatrixType(name, desc, columnType, cols);
    }

    @Nonnull
    public static Type makeSamplerType(String name, String abbr, Type component, int dimensions, boolean isShadow, boolean isArrayed, boolean isMultiSampled, boolean isSampled, boolean isSampler) {
        assert component == component.resolve();
        return new Type.SamplerType(name, abbr, component, dimensions, isShadow, isArrayed, isMultiSampled, isSampled, isSampler);
    }

    @Nonnull
    public static Type makeImageType(String name, String abbr, Type component, int dimensions, boolean isArrayed, boolean isMultiSampled) {
        assert component.isScalar();
        return makeSamplerType(name, abbr, component, dimensions, false, isArrayed, isMultiSampled, false, false);
    }

    @Nonnull
    public static Type makeTextureType(String name, String abbr, Type component, int dimensions, boolean isArrayed, boolean isMultiSampled) {
        assert component.isScalar();
        return makeSamplerType(name, abbr, component, dimensions, false, isArrayed, isMultiSampled, true, false);
    }

    @Nonnull
    public static Type makeSeparateType(String name, String abbr, Type component, boolean isShadow) {
        assert component.isVoid();
        return makeSamplerType(name, abbr, component, -1, isShadow, false, false, false, true);
    }

    @Nonnull
    public static Type makeCombinedType(String name, String abbr, Type component, int dimensions, boolean isShadow, boolean isArrayed, boolean isMultiSampled) {
        assert component.isScalar();
        return makeSamplerType(name, abbr, component, dimensions, isShadow, isArrayed, isMultiSampled, true, true);
    }

    @Nonnull
    public static Type makeSpecialType(String name, String abbr, byte kind) {
        return new Type(name, abbr, kind);
    }

    @Nonnull
    public static Type makeArrayType(String name, Type type, int size) {
        assert !type.isArray();
        return new Type.ArrayType(name, type, size);
    }

    @Nonnull
    public static Type makeStructType(@Nonnull Context context, int position, @Nonnull String name, @Nonnull List<Type.Field> fields, boolean interfaceBlock) {
        String structOrBlock = interfaceBlock ? "block" : "struct";
        if (fields.isEmpty()) {
            context.error(position, structOrBlock + " '" + name + "' must contain at least one field");
        }
        for (Type.Field field : fields) {
            if (field.type().isVoid()) {
                context.error(field.position(), "type 'void' is not permitted in a " + structOrBlock);
            }
        }
        int nestingDepth = 0;
        for (Type.Field fieldx : fields) {
            nestingDepth = Math.max(nestingDepth, fieldx.type().getNestingDepth());
        }
        if (nestingDepth >= 8) {
            context.error(position, structOrBlock + " '" + name + "' is too deeply nested");
        }
        return new Type.StructType(position, name, (Type.Field[]) fields.toArray(new Type.Field[0]), nestingDepth + 1, interfaceBlock);
    }

    @Nonnull
    @Override
    public Node.SymbolKind getKind() {
        return Node.SymbolKind.TYPE;
    }

    @Nonnull
    @Override
    public final Type getType() {
        return this;
    }

    @Nonnull
    public Type resolve() {
        return this;
    }

    @Nonnull
    public Type getElementType() {
        return this;
    }

    @Nonnull
    public Type getComponentType() {
        return this;
    }

    public final boolean matches(@Nonnull Type other) {
        return this.resolve().getName().equals(other.resolve().getName());
    }

    @Nonnull
    public final String getDesc() {
        return this.mDesc;
    }

    public final byte getTypeKind() {
        return this.mTypeKind;
    }

    public byte getScalarKind() {
        return 4;
    }

    @Nonnull
    @Override
    public final String toString() {
        return this.getName();
    }

    public final boolean isInBuiltinTypes() {
        return !this.isArray() && !this.isStruct();
    }

    public final boolean isBoolean() {
        return this.getScalarKind() == 3;
    }

    public final boolean isNumeric() {
        return switch(this.getScalarKind()) {
            case 0, 1, 2 ->
                true;
            default ->
                false;
        };
    }

    public final boolean isFloat() {
        return this.getScalarKind() == 0;
    }

    public final boolean isSigned() {
        return this.getScalarKind() == 1;
    }

    public final boolean isUnsigned() {
        return this.getScalarKind() == 2;
    }

    public final boolean isInteger() {
        return switch(this.getScalarKind()) {
            case 1, 2 ->
                true;
            default ->
                false;
        };
    }

    public final boolean isFloatOrCompound() {
        return (this.isScalar() || this.isVector() || this.isMatrix()) && this.getComponentType().isFloat();
    }

    public final boolean isSignedOrCompound() {
        return (this.isScalar() || this.isVector()) && this.getComponentType().isSigned();
    }

    public final boolean isUnsignedOrCompound() {
        return (this.isScalar() || this.isVector()) && this.getComponentType().isUnsigned();
    }

    public final boolean isBooleanOrCompound() {
        return (this.isScalar() || this.isVector()) && this.getComponentType().isBoolean();
    }

    public final boolean isOpaque() {
        return this.mTypeKind == 4;
    }

    public final boolean isGeneric() {
        return this.mTypeKind == 1;
    }

    public int getRank() {
        throw new UnsupportedOperationException("non-scalar");
    }

    public final boolean canCoerceTo(Type other, boolean allowNarrowing) {
        return Type.CoercionCost.accept(this.getCoercionCost(other), allowNarrowing);
    }

    public final long getCoercionCost(Type other) {
        if (this.matches(other)) {
            return Type.CoercionCost.free();
        } else {
            if (this.getTypeKind() == other.getTypeKind()) {
                if (this.isVector()) {
                    if (this.getRows() != other.getRows()) {
                        return Type.CoercionCost.saturate();
                    }
                    return this.getComponentType().getCoercionCost(other.getComponentType());
                }
                if (this.isMatrix()) {
                    if (this.getRows() == other.getRows() && this.getCols() == other.getCols()) {
                        return this.getComponentType().getCoercionCost(other.getComponentType());
                    }
                    return Type.CoercionCost.saturate();
                }
                if (this.isArray()) {
                    if (this.getArraySize() != other.getArraySize()) {
                        return Type.CoercionCost.saturate();
                    }
                    return this.getElementType().getCoercionCost(other.getElementType());
                }
            }
            if (this.isNumeric() && other.isNumeric()) {
                if (this.getScalarKind() != other.getScalarKind()) {
                    return Type.CoercionCost.saturate();
                }
                if (other.getRank() >= this.getRank()) {
                    return Type.CoercionCost.widening(other.getRank() - this.getRank());
                }
                if (this.getWidth() == other.getWidth()) {
                    return Type.CoercionCost.narrowing(this.getRank() - other.getRank());
                }
            }
            if (this.isGeneric()) {
                Type[] types = this.getCoercibleTypes();
                for (int i = 0; i < types.length; i++) {
                    if (types[i].matches(other)) {
                        return Type.CoercionCost.widening(i + 1);
                    }
                }
            }
            return Type.CoercionCost.saturate();
        }
    }

    @Nonnull
    public Type[] getCoercibleTypes() {
        throw new AssertionError();
    }

    @Nullable
    public final Expression coerceExpression(@Nonnull Context context, @Nullable Expression expr) {
        if (expr != null && !expr.isIncomplete(context)) {
            if (expr.getType().matches(this)) {
                return expr;
            } else {
                int pos = expr.mPosition;
                if (!Type.CoercionCost.accept(expr.getCoercionCost(this), false)) {
                    context.error(pos, "expected '" + this.getName() + "', but found '" + expr.getType().getName() + "'");
                    return null;
                } else if (this.isScalar()) {
                    return ConstructorScalarCast.make(context, pos, this, expr);
                } else if (this.isVector() || this.isMatrix()) {
                    return ConstructorCompoundCast.make(pos, this, expr);
                } else if (this.isArray()) {
                    return ConstructorArrayCast.make(context, pos, this, expr);
                } else {
                    context.error(pos, "cannot construct '" + this.getName() + "'");
                    return null;
                }
            }
        } else {
            return null;
        }
    }

    public double getMinValue() {
        throw new UnsupportedOperationException();
    }

    public double getMaxValue() {
        throw new UnsupportedOperationException();
    }

    public int getCols() {
        throw new AssertionError();
    }

    public int getRows() {
        throw new AssertionError();
    }

    public int getComponents() {
        return this.getCols() * this.getRows();
    }

    public int getArraySize() {
        throw new UnsupportedOperationException("non-array");
    }

    public int getDimensions() {
        throw new AssertionError();
    }

    @Nonnull
    public Type.Field[] getFields() {
        throw new AssertionError();
    }

    public boolean isShadow() {
        throw new AssertionError();
    }

    public boolean isArrayed() {
        throw new AssertionError();
    }

    public final boolean isVoid() {
        return this.mTypeKind == 8;
    }

    public boolean isScalar() {
        return false;
    }

    public boolean isVector() {
        return false;
    }

    public boolean isMatrix() {
        return false;
    }

    public boolean isArray() {
        return false;
    }

    public boolean isUnsizedArray() {
        return false;
    }

    public boolean isStruct() {
        return false;
    }

    public boolean isInterfaceBlock() {
        return false;
    }

    public boolean isMultiSampled() {
        throw new AssertionError();
    }

    public boolean isSampled() {
        throw new AssertionError();
    }

    public boolean isCombinedSampler() {
        throw new AssertionError();
    }

    public boolean isSeparateSampler() {
        throw new AssertionError();
    }

    public boolean isStorageImage() {
        throw new AssertionError();
    }

    public int getMinWidth() {
        return 0;
    }

    public int getWidth() {
        return 0;
    }

    public boolean isRelaxedPrecision() {
        Type componentType = this.getComponentType();
        return componentType.getWidth() == 32 && componentType.getMinWidth() < 32;
    }

    public final Type toVector(@Nonnull Context context, int rows) {
        return this.toCompound(context, 1, rows);
    }

    public final Type toCompound(@Nonnull Context context, int cols, int rows) {
        if (!this.isScalar()) {
            throw new IllegalStateException("non-scalar");
        } else if (cols == 1 && rows == 1) {
            return this;
        } else {
            BuiltinTypes types = context.getTypes();
            if (this.matches(types.mFloat)) {
                return switch(cols) {
                    case 1 ->
                        {
                            switch(rows) {
                                case 2:
                                    ???;
                                case 3:
                                    ???;
                                case 4:
                                    ???;
                                default:
                                    throw new AssertionError(rows);
                            }
                        }
                    case 2 ->
                        {
                            switch(rows) {
                                case 2:
                                    ???;
                                case 3:
                                    ???;
                                case 4:
                                    ???;
                                default:
                                    throw new AssertionError(rows);
                            }
                        }
                    case 3 ->
                        {
                            switch(rows) {
                                case 2:
                                    ???;
                                case 3:
                                    ???;
                                case 4:
                                    ???;
                                default:
                                    throw new AssertionError(rows);
                            }
                        }
                    case 4 ->
                        {
                            switch(rows) {
                                case 2:
                                    ???;
                                case 3:
                                    ???;
                                case 4:
                                    ???;
                                default:
                                    throw new AssertionError(rows);
                            }
                        }
                    default ->
                        throw new AssertionError(cols);
                };
            } else if (this.matches(types.mHalf)) {
                return switch(cols) {
                    case 1 ->
                        {
                            switch(rows) {
                                case 2:
                                    ???;
                                case 3:
                                    ???;
                                case 4:
                                    ???;
                                default:
                                    throw new AssertionError(rows);
                            }
                        }
                    case 2 ->
                        {
                            switch(rows) {
                                case 2:
                                    ???;
                                case 3:
                                    ???;
                                case 4:
                                    ???;
                                default:
                                    throw new AssertionError(rows);
                            }
                        }
                    case 3 ->
                        {
                            switch(rows) {
                                case 2:
                                    ???;
                                case 3:
                                    ???;
                                case 4:
                                    ???;
                                default:
                                    throw new AssertionError(rows);
                            }
                        }
                    case 4 ->
                        {
                            switch(rows) {
                                case 2:
                                    ???;
                                case 3:
                                    ???;
                                case 4:
                                    ???;
                                default:
                                    throw new AssertionError(rows);
                            }
                        }
                    default ->
                        throw new AssertionError(cols);
                };
            } else {
                if (this.matches(types.mInt)) {
                    if (cols == 1) {
                        return switch(rows) {
                            case 2 ->
                                types.mInt2;
                            case 3 ->
                                types.mInt3;
                            case 4 ->
                                types.mInt4;
                            default ->
                                throw new AssertionError(rows);
                        };
                    }
                } else if (this.matches(types.mShort)) {
                    if (cols == 1) {
                        return switch(rows) {
                            case 2 ->
                                types.mShort2;
                            case 3 ->
                                types.mShort3;
                            case 4 ->
                                types.mShort4;
                            default ->
                                throw new AssertionError(rows);
                        };
                    }
                } else if (this.matches(types.mUInt)) {
                    if (cols == 1) {
                        return switch(rows) {
                            case 2 ->
                                types.mUInt2;
                            case 3 ->
                                types.mUInt3;
                            case 4 ->
                                types.mUInt4;
                            default ->
                                throw new AssertionError(rows);
                        };
                    }
                } else if (this.matches(types.mUShort)) {
                    if (cols == 1) {
                        return switch(rows) {
                            case 2 ->
                                types.mUShort2;
                            case 3 ->
                                types.mUShort3;
                            case 4 ->
                                types.mUShort4;
                            default ->
                                throw new AssertionError(rows);
                        };
                    }
                } else if (this.matches(types.mBool) && cols == 1) {
                    return switch(rows) {
                        case 2 ->
                            types.mBool2;
                        case 3 ->
                            types.mBool3;
                        case 4 ->
                            types.mBool4;
                        default ->
                            throw new AssertionError(rows);
                    };
                }
                throw new IllegalStateException("type mismatch");
            }
        }
    }

    @Nonnull
    public String getArrayName(int size) {
        if (size == -1) {
            return this.getName() + "[]";
        } else {
            assert size > 0;
            return this.getName() + "[" + size + "]";
        }
    }

    public boolean checkLiteralOutOfRange(Context context, int pos, double value) {
        assert this.isScalar();
        if (!this.isNumeric()) {
            return false;
        } else if (value >= this.getMinValue() && value <= this.getMaxValue()) {
            return false;
        } else {
            context.error(pos, String.format("value is out of range for type '%s': %.0f", this, value));
            return true;
        }
    }

    public boolean isUsableInArray(@Nonnull Context context, int position) {
        if (this.isArray()) {
            context.error(position, "multi-dimensional arrays are not allowed");
            return false;
        } else if (this.isVoid()) {
            context.error(position, "type 'void' may not be used in an array");
            return false;
        } else if (this.isOpaque()) {
            context.error(position, "opaque type '" + this.getName() + "' may not be used in an array");
            return false;
        } else {
            return true;
        }
    }

    public int convertArraySize(@Nonnull Context context, int position, Expression size) {
        size = context.getTypes().mInt.coerceExpression(context, size);
        if (size == null) {
            return 0;
        } else {
            OptionalLong value = ConstantFolder.getConstantInt(size);
            if (value.isEmpty()) {
                context.error(size.mPosition, "array size must be a constant integer expression");
                return 0;
            } else {
                return this.convertArraySize(context, position, size.mPosition, value.getAsLong());
            }
        }
    }

    public int convertArraySize(@Nonnull Context context, int position, int sizePosition, long size) {
        if (!this.isUsableInArray(context, position)) {
            return 0;
        } else if (size <= 0L) {
            context.error(sizePosition, "array size must be positive");
            return 0;
        } else if (size > 2147483647L) {
            context.error(sizePosition, "array size is too large");
            return 0;
        } else {
            return (int) size;
        }
    }

    public int getNestingDepth() {
        return 0;
    }

    public static final class AliasType extends Type {

        private final Type mUnderlyingType;

        AliasType(String name, Type type) {
            super(name, type.getDesc(), type.getTypeKind());
            this.mUnderlyingType = type;
        }

        @Nonnull
        @Override
        public Type resolve() {
            return this.mUnderlyingType;
        }

        @Nonnull
        @Override
        public Type getElementType() {
            return this.mUnderlyingType.getElementType();
        }

        @Nonnull
        @Override
        public Type getComponentType() {
            return this.mUnderlyingType.getComponentType();
        }

        @Override
        public byte getScalarKind() {
            return this.mUnderlyingType.getScalarKind();
        }

        @Override
        public int getRank() {
            return this.mUnderlyingType.getRank();
        }

        @Override
        public int getCols() {
            return this.mUnderlyingType.getCols();
        }

        @Override
        public int getRows() {
            return this.mUnderlyingType.getRows();
        }

        @Override
        public int getComponents() {
            return this.mUnderlyingType.getComponents();
        }

        @Override
        public int getArraySize() {
            return this.mUnderlyingType.getArraySize();
        }

        @Override
        public double getMinValue() {
            return this.mUnderlyingType.getMinValue();
        }

        @Override
        public double getMaxValue() {
            return this.mUnderlyingType.getMaxValue();
        }

        @Override
        public int getMinWidth() {
            return this.mUnderlyingType.getMinWidth();
        }

        @Override
        public int getWidth() {
            return this.mUnderlyingType.getWidth();
        }

        @Override
        public int getDimensions() {
            return this.mUnderlyingType.getDimensions();
        }

        @Override
        public boolean isShadow() {
            return this.mUnderlyingType.isShadow();
        }

        @Override
        public boolean isArrayed() {
            return this.mUnderlyingType.isArrayed();
        }

        @Override
        public boolean isScalar() {
            return this.mUnderlyingType.isScalar();
        }

        @Override
        public boolean isVector() {
            return this.mUnderlyingType.isVector();
        }

        @Override
        public boolean isMatrix() {
            return this.mUnderlyingType.isMatrix();
        }

        @Override
        public boolean isArray() {
            return this.mUnderlyingType.isArray();
        }

        @Override
        public boolean isUnsizedArray() {
            return this.mUnderlyingType.isUnsizedArray();
        }

        @Override
        public boolean isStruct() {
            return this.mUnderlyingType.isStruct();
        }

        @Override
        public boolean isInterfaceBlock() {
            return this.mUnderlyingType.isInterfaceBlock();
        }

        @Override
        public boolean isMultiSampled() {
            return this.mUnderlyingType.isMultiSampled();
        }

        @Override
        public boolean isSampled() {
            return this.mUnderlyingType.isSampled();
        }

        @Override
        public boolean isCombinedSampler() {
            return this.mUnderlyingType.isCombinedSampler();
        }

        @Override
        public boolean isSeparateSampler() {
            return this.mUnderlyingType.isSeparateSampler();
        }

        @Override
        public boolean isStorageImage() {
            return this.mUnderlyingType.isStorageImage();
        }

        @Nonnull
        @Override
        public Type[] getCoercibleTypes() {
            return this.mUnderlyingType.getCoercibleTypes();
        }

        @Nonnull
        @Override
        public Type.Field[] getFields() {
            return this.mUnderlyingType.getFields();
        }
    }

    public static final class ArrayType extends Type {

        private final Type mElementType;

        private final int mArraySize;

        ArrayType(String name, Type type, int size) {
            super(name, type.getDesc() + (size != -1 ? "_" + size : "_x"), (byte) 0);
            assert name.equals(type.getArrayName(size));
            this.mElementType = type;
            this.mArraySize = size;
        }

        @Override
        public boolean isArray() {
            return true;
        }

        @Override
        public boolean isUnsizedArray() {
            return this.mArraySize == -1;
        }

        @Override
        public int getArraySize() {
            return this.mArraySize;
        }

        @Override
        public int getComponents() {
            return this.mArraySize == -1 ? 0 : this.mElementType.getComponents() * this.mArraySize;
        }

        @Nonnull
        @Override
        public Type getElementType() {
            return this.mElementType;
        }

        @Nonnull
        @Override
        public Type getComponentType() {
            return this.mElementType.getComponentType();
        }
    }

    public static final class CoercionCost {

        private static final long SATURATE = -9223372034707292160L;

        public static long free() {
            return 0L;
        }

        public static long widening(int cost) {
            assert cost >= 0;
            return (long) cost;
        }

        public static long narrowing(int cost) {
            assert cost >= 0;
            return (long) cost << 32;
        }

        public static long saturate() {
            return -9223372034707292160L;
        }

        public static boolean accept(long cost, boolean allowNarrowing) {
            return (cost & -9223372034707292160L) == 0L && (allowNarrowing || cost >> 32 == 0L);
        }

        public static long plus(long lhs, long rhs) {
            if ((lhs & -9223372034707292160L | rhs & -9223372034707292160L) != 0L) {
                return -9223372034707292160L;
            } else {
                long widening = (long) ((int) lhs + (int) rhs);
                long narrowing = (lhs >> 32) + (rhs >> 32);
                return widening | narrowing << 32;
            }
        }

        public static int compare(long lhs, long rhs) {
            int res = Boolean.compare((lhs & -9223372034707292160L) != 0L, (rhs & -9223372034707292160L) != 0L);
            if (res != 0) {
                return res;
            } else {
                res = Integer.compare((int) (lhs >> 32), (int) (rhs >> 32));
                return res != 0 ? res : Integer.compare((int) lhs, (int) rhs);
            }
        }
    }

    public static record Field(int position, Modifiers modifiers, Type type, String name) {

        @Nonnull
        public String toString() {
            return this.modifiers.toString() + this.type.getName() + " " + this.name + ";";
        }
    }

    public static final class GenericType extends Type {

        private final Type[] mCoercibleTypes;

        GenericType(String name, Type[] types) {
            super(name, "G", (byte) 1);
            this.mCoercibleTypes = types;
        }

        @Nonnull
        @Override
        public Type[] getCoercibleTypes() {
            return this.mCoercibleTypes;
        }
    }

    public static final class ImageType extends Type {

        ImageType(String name, String desc, byte kind) {
            super(name, desc, kind);
        }
    }

    public static final class MatrixType extends Type {

        private final Type.VectorType mColumnType;

        private final byte mCols;

        MatrixType(String name, String abbr, Type columnType, int cols) {
            super(name, abbr, (byte) 2);
            assert columnType.isVector();
            assert cols >= 2 && cols <= 4;
            int rows = columnType.getRows();
            Type componentType = columnType.getComponentType();
            assert abbr.equals(componentType.getDesc() + cols + rows);
            assert name.equals(componentType.getName() + cols + "x" + rows);
            this.mColumnType = (Type.VectorType) columnType;
            this.mCols = (byte) cols;
        }

        @Override
        public boolean isMatrix() {
            return true;
        }

        @Nonnull
        @Override
        public Type getElementType() {
            return this.mColumnType;
        }

        @Nonnull
        public Type.ScalarType getComponentType() {
            return this.mColumnType.getComponentType();
        }

        @Override
        public int getCols() {
            return this.mCols;
        }

        @Override
        public int getRows() {
            return this.mColumnType.getRows();
        }
    }

    public static final class SamplerType extends Type {

        private final Type mComponentType;

        private final int mDimensions;

        private final boolean mIsShadow;

        private final boolean mIsArrayed;

        private final boolean mIsMultiSampled;

        private final boolean mIsSampled;

        private final boolean mIsSampler;

        SamplerType(String name, String desc, Type type, int dimensions, boolean isShadow, boolean isArrayed, boolean isMultiSampled, boolean isSampled, boolean isSampler) {
            super(name, desc, (byte) 4);
            this.mComponentType = type;
            this.mDimensions = dimensions;
            this.mIsArrayed = isArrayed;
            this.mIsMultiSampled = isMultiSampled;
            this.mIsSampled = isSampled;
            this.mIsSampler = isSampler;
            this.mIsShadow = isShadow;
        }

        @Nonnull
        @Override
        public Type getComponentType() {
            return this.mComponentType;
        }

        @Override
        public int getDimensions() {
            return this.mDimensions;
        }

        @Override
        public boolean isShadow() {
            return this.mIsShadow;
        }

        @Override
        public boolean isArrayed() {
            return this.mIsArrayed;
        }

        @Override
        public boolean isMultiSampled() {
            return this.mIsMultiSampled;
        }

        @Override
        public boolean isSampled() {
            return this.mIsSampled;
        }

        @Override
        public boolean isCombinedSampler() {
            return this.mIsSampled && this.mIsSampler;
        }

        @Override
        public boolean isSeparateSampler() {
            return !this.mIsSampled && this.mIsSampler;
        }

        @Override
        public boolean isStorageImage() {
            return !this.mIsSampled && !this.mIsSampler && this.mDimensions != 6;
        }
    }

    public static final class ScalarType extends Type {

        private final byte mScalarKind;

        private final byte mRank;

        private final byte mMinWidth;

        private final byte mWidth;

        ScalarType(String name, String desc, byte kind, int rank, int minWidth, int width) {
            super(name, desc, (byte) 5);
            this.mScalarKind = kind;
            this.mRank = (byte) rank;
            this.mMinWidth = (byte) minWidth;
            this.mWidth = (byte) width;
        }

        @Override
        public boolean isScalar() {
            return true;
        }

        @Override
        public byte getScalarKind() {
            return this.mScalarKind;
        }

        @Override
        public int getRank() {
            return this.mRank;
        }

        @Override
        public int getMinWidth() {
            return this.mMinWidth;
        }

        @Override
        public int getWidth() {
            return this.mWidth;
        }

        @Override
        public int getCols() {
            return 1;
        }

        @Override
        public int getRows() {
            return 1;
        }

        @Override
        public int getComponents() {
            return 1;
        }

        @Override
        public double getMinValue() {
            return switch(this.mScalarKind) {
                case 1 ->
                    this.mMinWidth == 32 ? -2.1474836E9F : -32768.0;
                case 2 ->
                    0.0;
                default ->
                    this.mWidth == 64 ? -Double.MAX_VALUE : -Float.MAX_VALUE;
            };
        }

        @Override
        public double getMaxValue() {
            return switch(this.mScalarKind) {
                case 1 ->
                    this.mMinWidth == 32 ? 2.147483647E9 : 32767.0;
                case 2 ->
                    this.mMinWidth == 32 ? 4.294967295E9 : 65535.0;
                default ->
                    this.mWidth == 64 ? Double.MAX_VALUE : Float.MAX_VALUE;
            };
        }
    }

    public static final class StructType extends Type {

        private final Type.Field[] mFields;

        private final int mNestingDepth;

        private final boolean mInterfaceBlock;

        private final int mComponents;

        StructType(int position, String name, Type.Field[] fields, int nestingDepth, boolean interfaceBlock) {
            super(name, name, (byte) 6, position);
            this.mFields = fields;
            this.mNestingDepth = nestingDepth;
            this.mInterfaceBlock = interfaceBlock;
            int components = 0;
            for (Type.Field field : this.mFields) {
                components += field.type().getComponents();
            }
            this.mComponents = components;
        }

        @Override
        public boolean isStruct() {
            return true;
        }

        @Override
        public boolean isInterfaceBlock() {
            return this.mInterfaceBlock;
        }

        @Nonnull
        @Override
        public Type.Field[] getFields() {
            return this.mFields;
        }

        @Override
        public int getComponents() {
            return this.mComponents;
        }

        @Override
        public int getNestingDepth() {
            return this.mNestingDepth;
        }
    }

    public static final class VectorType extends Type {

        private final Type.ScalarType mComponentType;

        private final byte mRows;

        VectorType(String name, String abbr, Type componentType, int rows) {
            super(name, abbr, (byte) 7);
            assert rows >= 2 && rows <= 4;
            assert abbr.equals(componentType.getDesc() + rows);
            assert name.equals(componentType.getName() + rows);
            this.mComponentType = (Type.ScalarType) componentType;
            this.mRows = (byte) rows;
        }

        @Override
        public boolean isVector() {
            return true;
        }

        @Nonnull
        @Override
        public Type getElementType() {
            return this.mComponentType;
        }

        @Nonnull
        public Type.ScalarType getComponentType() {
            return this.mComponentType;
        }

        @Override
        public int getCols() {
            return 1;
        }

        @Override
        public int getRows() {
            return this.mRows;
        }
    }
}
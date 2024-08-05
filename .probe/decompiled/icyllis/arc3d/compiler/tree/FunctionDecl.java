package icyllis.arc3d.compiler.tree;

import icyllis.arc3d.compiler.Context;
import icyllis.arc3d.compiler.IntrinsicList;
import java.util.List;
import java.util.StringJoiner;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class FunctionDecl extends Symbol {

    private final Modifiers mModifiers;

    private final List<Variable> mParameters;

    private final Type mReturnType;

    private boolean mBuiltin;

    private boolean mEntryPoint;

    private int mIntrinsicKind;

    private FunctionDecl mNextOverload;

    private FunctionDefinition mDefinition;

    public FunctionDecl(int position, Modifiers modifiers, String name, List<Variable> parameters, Type returnType, boolean builtin, boolean entryPoint, int intrinsicKind) {
        super(position, name);
        this.mModifiers = modifiers;
        this.mParameters = parameters;
        this.mReturnType = returnType;
        this.mBuiltin = builtin;
        this.mEntryPoint = entryPoint;
        this.mIntrinsicKind = intrinsicKind;
    }

    private static boolean checkModifiers(@Nonnull Context context, @Nonnull Modifiers modifiers) {
        boolean success = modifiers.checkLayoutFlags(context, 0);
        int permittedFlags = 196608 | (context.isModule() ? '耀' : 0);
        success &= modifiers.checkFlags(context, permittedFlags);
        if ((modifiers.flags() & 196608) == 196608) {
            context.error(modifiers.mPosition, "functions cannot be both 'inline' and 'noinline'");
            return false;
        } else {
            return success;
        }
    }

    private static boolean checkReturnType(@Nonnull Context context, int pos, @Nonnull Type returnType) {
        if (returnType.isOpaque()) {
            context.error(pos, "functions may not return opaque type '" + returnType.getName() + "'");
            return false;
        } else if (returnType.isGeneric()) {
            context.error(pos, "functions may not return generic type '" + returnType.getName() + "'");
            return false;
        } else if (returnType.isUnsizedArray()) {
            context.error(pos, "functions may not return unsized array type '" + returnType.getName() + "'");
            return false;
        } else {
            return true;
        }
    }

    private static boolean checkParameters(@Nonnull Context context, @Nonnull List<Variable> parameters, @Nonnull Modifiers modifiers) {
        boolean success = true;
        for (Variable param : parameters) {
            Type type = param.getType();
            int permittedFlags = 40;
            int permittedLayoutFlags = 0;
            if (!type.isOpaque()) {
                permittedFlags |= 64;
            } else if (type.isStorageImage()) {
                permittedFlags |= 3968;
            }
            success &= param.getModifiers().checkFlags(context, permittedFlags);
            success &= param.getModifiers().checkLayoutFlags(context, permittedLayoutFlags);
            if ((modifiers.flags() & 32768) != 0 && (param.getModifiers().flags() & 64) != 0) {
                context.error(param.getModifiers().mPosition, "pure functions cannot have out parameters");
                success = false;
            }
        }
        return success;
    }

    private static boolean checkEntryPointSignature(@Nonnull Context context, int pos, @Nonnull Type returnType, @Nonnull List<Variable> parameters) {
        switch(context.getKind()) {
            case VERTEX:
            case FRAGMENT:
            case COMPUTE:
                if (!returnType.matches(context.getTypes().mVoid)) {
                    context.error(pos, "entry point must return 'void'");
                    return false;
                } else if (!parameters.isEmpty()) {
                    context.error(pos, "entry point must have zero parameters");
                    return false;
                }
            default:
                return true;
        }
    }

    private static int findGenericIndex(@Nonnull Type concreteType, @Nonnull Type genericType, boolean allowNarrowing) {
        Type[] types = genericType.getCoercibleTypes();
        for (int index = 0; index < types.length; index++) {
            if (concreteType.canCoerceTo(types[index], allowNarrowing)) {
                return index;
            }
        }
        return -1;
    }

    private static boolean typeMatches(@Nonnull Type concreteType, @Nonnull Type maybeGenericType) {
        return maybeGenericType.isGeneric() ? findGenericIndex(concreteType, maybeGenericType, false) != -1 : concreteType.matches(maybeGenericType);
    }

    private static boolean parametersMatch(@Nonnull List<Variable> params, @Nonnull List<Variable> otherParams) {
        if (params.size() != otherParams.size()) {
            return false;
        } else {
            int genericIndex = -1;
            for (int i = 0; i < params.size(); i++) {
                Type paramType = ((Variable) params.get(i)).getType();
                Type otherParamType = ((Variable) otherParams.get(i)).getType();
                if (otherParamType.isGeneric()) {
                    int genericIndexForThisParam = findGenericIndex(paramType, otherParamType, false);
                    if (genericIndexForThisParam == -1) {
                        return false;
                    }
                    if (genericIndex != -1 && genericIndex != genericIndexForThisParam) {
                        return false;
                    }
                    genericIndex = genericIndexForThisParam;
                }
            }
            for (int ix = 0; ix < params.size(); ix++) {
                Type paramType = ((Variable) params.get(ix)).getType();
                Type otherParamType = ((Variable) otherParams.get(ix)).getType();
                if (otherParamType.isGeneric()) {
                    otherParamType = otherParamType.getCoercibleTypes()[genericIndex];
                }
                if (!paramType.matches(otherParamType)) {
                    return false;
                }
            }
            return true;
        }
    }

    @Nullable
    public static FunctionDecl convert(@Nonnull Context context, int pos, @Nonnull Modifiers modifiers, @Nonnull String name, @Nonnull List<Variable> parameters, @Nonnull Type returnType) {
        int intrinsicKind = context.isBuiltin() ? IntrinsicList.findIntrinsicKind(name) : -1;
        boolean isEntryPoint = name.equals(context.getOptions().mEntryPointName);
        if (!checkModifiers(context, modifiers)) {
            return null;
        } else if (!checkReturnType(context, pos, returnType)) {
            return null;
        } else if (!checkParameters(context, parameters, modifiers)) {
            return null;
        } else if (isEntryPoint && !checkEntryPointSignature(context, pos, returnType, parameters)) {
            return null;
        } else {
            Symbol entry = context.getSymbolTable().find(name);
            if (entry != null) {
                if (!(entry instanceof FunctionDecl chain)) {
                    context.error(pos, "symbol '" + name + "' was already defined");
                    return null;
                }
                FunctionDecl existingDecl = null;
                FunctionDecl other = chain;
                while (true) {
                    if (other != null) {
                        if (!parametersMatch(parameters, other.getParameters())) {
                            other = other.getNextOverload();
                            continue;
                        }
                        if (!typeMatches(returnType, other.getReturnType())) {
                            FunctionDecl invalidDecl = new FunctionDecl(pos, modifiers, name, parameters, returnType, context.isBuiltin(), isEntryPoint, intrinsicKind);
                            context.error(pos, "functions '" + invalidDecl + "' and '" + other + "' differ only in return type");
                            return null;
                        }
                        for (int i = 0; i < parameters.size(); i++) {
                            if (!((Variable) parameters.get(i)).getModifiers().equals(((Variable) other.getParameters().get(i)).getModifiers())) {
                                context.error(((Variable) parameters.get(i)).mPosition, "modifiers on parameter " + (i + 1) + " differ between declaration and definition");
                                return null;
                            }
                        }
                        if (other.getDefinition() != null || other.isIntrinsic() || !modifiers.equals(other.getModifiers())) {
                            FunctionDecl invalidDecl = new FunctionDecl(pos, modifiers, name, parameters, returnType, context.isBuiltin(), isEntryPoint, intrinsicKind);
                            context.error(pos, "redefinition of '" + invalidDecl + "'");
                            return null;
                        }
                        existingDecl = other;
                    }
                    if (existingDecl == null && chain.isEntryPoint()) {
                        context.error(pos, "redefinition of entry point");
                        return null;
                    }
                    if (existingDecl != null) {
                        return existingDecl;
                    }
                    break;
                }
            }
            return context.getSymbolTable().insert(context, new FunctionDecl(pos, modifiers, name, parameters, returnType, context.isBuiltin(), isEntryPoint, intrinsicKind));
        }
    }

    @Nonnull
    @Override
    public Node.SymbolKind getKind() {
        return Node.SymbolKind.FUNCTION_DECL;
    }

    @Nonnull
    @Override
    public Type getType() {
        throw new AssertionError();
    }

    @Nonnull
    public Type getReturnType() {
        return this.mReturnType;
    }

    public boolean isIntrinsic() {
        return this.mIntrinsicKind != -1;
    }

    public FunctionDefinition getDefinition() {
        return this.mDefinition;
    }

    public void setDefinition(FunctionDefinition definition) {
        this.mDefinition = definition;
    }

    public List<Variable> getParameters() {
        return this.mParameters;
    }

    @Nonnull
    public String getMangledName() {
        if (!this.isIntrinsic() && !this.isEntryPoint()) {
            StringBuilder mangledName = new StringBuilder(this.getName());
            for (Variable p : this.mParameters) {
                mangledName.append('_').append(p.getType().getDesc());
            }
            return mangledName.toString();
        } else {
            return this.getName();
        }
    }

    @Nullable
    public FunctionDecl getNextOverload() {
        return this.mNextOverload;
    }

    public void setNextOverload(FunctionDecl overload) {
        assert overload == null || overload.getName().equals(this.getName());
        this.mNextOverload = overload;
    }

    public Modifiers getModifiers() {
        return this.mModifiers;
    }

    public boolean isBuiltin() {
        return this.mBuiltin;
    }

    public boolean isEntryPoint() {
        return this.mEntryPoint;
    }

    @Nullable
    public Type resolveParameterTypes(@Nonnull List<Expression> arguments, List<Type> outParameterTypes) {
        List<Variable> parameters = this.mParameters;
        assert parameters.size() == arguments.size();
        int genericIndex = -1;
        for (int i = 0; i < arguments.size(); i++) {
            Type parameterType = ((Variable) parameters.get(i)).getType();
            if (!parameterType.isGeneric()) {
                outParameterTypes.add(parameterType);
            } else {
                if (genericIndex == -1) {
                    genericIndex = findGenericIndex(((Expression) arguments.get(i)).getType(), parameterType, true);
                    if (genericIndex == -1) {
                        return null;
                    }
                }
                outParameterTypes.add(parameterType.getCoercibleTypes()[genericIndex]);
            }
        }
        Type returnType = this.mReturnType;
        if (!returnType.isGeneric()) {
            return returnType;
        } else {
            return genericIndex == -1 ? null : returnType.getCoercibleTypes()[genericIndex];
        }
    }

    @Nonnull
    @Override
    public String toString() {
        String header = this.mModifiers.toString() + this.mReturnType.getName() + " " + this.getName() + "(";
        StringJoiner joiner = new StringJoiner(", ");
        for (Variable p : this.mParameters) {
            joiner.add(p.toString());
        }
        return header + joiner + ")";
    }
}
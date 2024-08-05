package moe.wolfgirl.probejs.lang.transpiler;

import dev.latvian.mods.kubejs.script.ScriptManager;
import dev.latvian.mods.kubejs.typings.Generics;
import dev.latvian.mods.kubejs.typings.desc.ArrayDescJS;
import dev.latvian.mods.kubejs.typings.desc.DescriptionContext;
import dev.latvian.mods.kubejs.typings.desc.FixedArrayDescJS;
import dev.latvian.mods.kubejs.typings.desc.GenericDescJS;
import dev.latvian.mods.kubejs.typings.desc.ObjectDescJS;
import dev.latvian.mods.kubejs.typings.desc.OrDescJS;
import dev.latvian.mods.kubejs.typings.desc.PrimitiveDescJS;
import dev.latvian.mods.kubejs.typings.desc.TypeDescJS;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import moe.wolfgirl.probejs.lang.java.clazz.ClassPath;
import moe.wolfgirl.probejs.lang.java.type.TypeDescriptor;
import moe.wolfgirl.probejs.lang.java.type.impl.ArrayType;
import moe.wolfgirl.probejs.lang.java.type.impl.ClassType;
import moe.wolfgirl.probejs.lang.java.type.impl.ParamType;
import moe.wolfgirl.probejs.lang.java.type.impl.VariableType;
import moe.wolfgirl.probejs.lang.java.type.impl.WildcardType;
import moe.wolfgirl.probejs.lang.typescript.code.member.ParamDecl;
import moe.wolfgirl.probejs.lang.typescript.code.type.BaseType;
import moe.wolfgirl.probejs.lang.typescript.code.type.TSArrayType;
import moe.wolfgirl.probejs.lang.typescript.code.type.TSClassType;
import moe.wolfgirl.probejs.lang.typescript.code.type.TSParamType;
import moe.wolfgirl.probejs.lang.typescript.code.type.TSVariableType;
import moe.wolfgirl.probejs.lang.typescript.code.type.Types;
import moe.wolfgirl.probejs.lang.typescript.code.type.js.JSArrayType;
import moe.wolfgirl.probejs.lang.typescript.code.type.js.JSJoinedType;
import moe.wolfgirl.probejs.lang.typescript.code.type.js.JSLambdaType;
import moe.wolfgirl.probejs.lang.typescript.code.type.js.JSObjectType;
import moe.wolfgirl.probejs.lang.typescript.code.type.js.JSTypeOfType;

public class TypeConverter {

    public static final String PROBEJS_PREFIX = "$$probejs$$";

    public static final DescriptionContext PROBEJS = new DescriptionContext() {

        @Override
        public String typeName(Class<?> type) {
            return "$$probejs$$" + type.getName();
        }
    };

    public final Map<ClassPath, BaseType> predefinedTypes = new HashMap();

    public final ScriptManager scriptManager;

    public TypeConverter(ScriptManager manager) {
        this.scriptManager = manager;
    }

    public void addType(Class<?> clazz, BaseType type) {
        this.predefinedTypes.put(new ClassPath(clazz), type);
    }

    public BaseType convertType(TypeDescriptor descriptor) {
        if (descriptor instanceof ClassType classType) {
            return (BaseType) this.predefinedTypes.getOrDefault(classType.classPath, new TSClassType(classType.classPath));
        } else if (descriptor instanceof ArrayType arrayType) {
            return new TSArrayType(this.convertType(arrayType.component));
        } else if (descriptor instanceof ParamType paramType) {
            Generics generics = paramType.getAnnotation(Generics.class);
            if (generics != null) {
                BaseType baseType = new TSClassType(new ClassPath(generics.base()));
                List<BaseType> params = Arrays.stream(generics.value()).map(c -> new TSClassType(new ClassPath(c))).toList();
                return new TSParamType(baseType, params);
            } else {
                BaseType base = this.convertType(paramType.base);
                if (base == Types.ANY) {
                    return Types.ANY;
                } else {
                    List<BaseType> params = paramType.params.stream().map(this::convertType).toList();
                    return new TSParamType(base, params);
                }
            }
        } else if (descriptor instanceof VariableType variableType) {
            List<TypeDescriptor> desc = variableType.descriptors;
            switch(desc.size()) {
                case 0:
                    return new TSVariableType(variableType.symbol, null);
                case 1:
                    return new TSVariableType(variableType.symbol, this.convertType((TypeDescriptor) desc.get(0)));
                default:
                    List<BaseType> converted = desc.stream().map(this::convertType).toList();
                    return new TSVariableType(variableType.symbol, new JSJoinedType.Intersection(converted));
            }
        } else if (descriptor instanceof WildcardType wildcardType) {
            return (BaseType) wildcardType.stream().findAny().map(this::convertType).orElse(Types.ANY);
        } else {
            throw new RuntimeException("Unknown subclass of TypeDescriptor.");
        }
    }

    public BaseType convertType(BaseType baseType) {
        if (baseType instanceof JSArrayType jsArrayType) {
            return new JSArrayType(jsArrayType.components.stream().map(this::convertType).toList());
        } else if (baseType instanceof JSJoinedType.Union union) {
            return new JSJoinedType.Union(union.types.stream().map(this::convertType).toList());
        } else if (baseType instanceof JSJoinedType.Intersection intersection) {
            return new JSJoinedType.Intersection(intersection.types.stream().map(this::convertType).toList());
        } else if (!(baseType instanceof JSObjectType jsObjectType)) {
            if (baseType instanceof TSArrayType arrayType) {
                return new TSArrayType(this.convertType(arrayType.component));
            } else if (baseType instanceof TSClassType classType) {
                return (BaseType) this.predefinedTypes.getOrDefault(classType.classPath, classType);
            } else if (baseType instanceof TSParamType paramType) {
                return new TSParamType(this.convertType(paramType.baseType), paramType.params.stream().map(this::convertType).toList());
            } else if (baseType instanceof JSTypeOfType typeOfType) {
                return new JSTypeOfType(this.convertType(typeOfType.inner));
            } else if (baseType instanceof TSVariableType variableType) {
                return variableType.extendsType == null ? variableType : new TSVariableType(variableType.symbol, this.convertType(variableType.extendsType));
            } else {
                return (BaseType) (baseType instanceof JSLambdaType lambdaType ? new JSLambdaType(lambdaType.params.stream().map(t -> new ParamDecl(t.name, this.convertType(t.type), t.varArg, t.optional)).toList(), this.convertType(lambdaType.returnType)) : baseType);
            }
        } else {
            Map<String, BaseType> members = new HashMap();
            for (Entry<String, BaseType> entry : jsObjectType.members.entrySet()) {
                String key = (String) entry.getKey();
                BaseType member = (BaseType) entry.getValue();
                members.put(key, this.convertType(member));
            }
            return new JSObjectType(members);
        }
    }

    public BaseType convertType(TypeDescJS typeDesc) {
        if (typeDesc instanceof ArrayDescJS arrayDesc) {
            return this.convertType(arrayDesc.type()).asArray();
        } else if (typeDesc instanceof FixedArrayDescJS fixedArrayDesc) {
            return new JSArrayType(Arrays.stream(fixedArrayDesc.types()).map(this::convertType).toList());
        } else if (typeDesc instanceof GenericDescJS genericDesc) {
            if (!(genericDesc.type() instanceof PrimitiveDescJS primitiveDesc) || !primitiveDesc.type().equals("Map")) {
                return new TSParamType(this.convertType(genericDesc.type()), Arrays.stream(genericDesc.types()).map(this::convertType).toList());
            }
            if (genericDesc.types().length != 2) {
                return Types.ANY;
            } else {
                BaseType valueType = this.convertType(genericDesc.types()[1]);
                return Types.custom((decl, formatType) -> "{[k: string]: %s}".formatted(valueType.line(decl, formatType)), (ClassPath[]) valueType.getUsedClassPaths().toArray(new ClassPath[0]));
            }
        } else if (typeDesc instanceof ObjectDescJS objectDesc) {
            Map<String, BaseType> members = new HashMap();
            for (ObjectDescJS.Entry type : objectDesc.types()) {
                String name = type.key();
                if (type.optional()) {
                    name = name + "?";
                }
                members.put(name, this.convertType(type.value()));
            }
            return new JSObjectType(members);
        } else if (typeDesc instanceof OrDescJS orDesc) {
            return new JSJoinedType.Union(Arrays.stream(orDesc.types()).map(this::convertType).toList());
        } else if (typeDesc instanceof PrimitiveDescJS primitiveDesc) {
            String content = primitiveDesc.type();
            if (content.startsWith("$$probejs$$")) {
                content = content.substring("$$probejs$$".length());
                String[] parts = content.split("\\.");
                parts[parts.length - 1] = "$" + parts[parts.length - 1];
                return new TSClassType(new ClassPath(Arrays.stream(parts).toList()));
            } else {
                return Types.primitive(content);
            }
        } else {
            throw new RuntimeException("Unknown TypeDescJS");
        }
    }
}
package ca.fxco.memoryleakfix.mixinextras.sugar.impl.ref;

import ca.fxco.memoryleakfix.mixinextras.lib.apache.commons.StringUtils;
import ca.fxco.memoryleakfix.mixinextras.service.MixinExtrasService;
import ca.fxco.memoryleakfix.mixinextras.sugar.ref.LocalBooleanRef;
import ca.fxco.memoryleakfix.mixinextras.sugar.ref.LocalByteRef;
import ca.fxco.memoryleakfix.mixinextras.sugar.ref.LocalCharRef;
import ca.fxco.memoryleakfix.mixinextras.sugar.ref.LocalDoubleRef;
import ca.fxco.memoryleakfix.mixinextras.sugar.ref.LocalFloatRef;
import ca.fxco.memoryleakfix.mixinextras.sugar.ref.LocalIntRef;
import ca.fxco.memoryleakfix.mixinextras.sugar.ref.LocalLongRef;
import ca.fxco.memoryleakfix.mixinextras.sugar.ref.LocalRef;
import ca.fxco.memoryleakfix.mixinextras.sugar.ref.LocalShortRef;
import ca.fxco.memoryleakfix.mixinextras.utils.ASMUtils;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.TypeInsnNode;

public class LocalRefUtils {

    public static Class<?> getInterfaceFor(Type type) {
        if (!ASMUtils.isPrimitive(type)) {
            return LocalRef.class;
        } else {
            switch(type.getDescriptor().charAt(0)) {
                case 'B':
                    return LocalByteRef.class;
                case 'C':
                    return LocalCharRef.class;
                case 'D':
                    return LocalDoubleRef.class;
                case 'E':
                case 'G':
                case 'H':
                case 'K':
                case 'L':
                case 'M':
                case 'N':
                case 'O':
                case 'P':
                case 'Q':
                case 'R':
                case 'T':
                case 'U':
                case 'V':
                case 'W':
                case 'X':
                case 'Y':
                default:
                    throw new IllegalStateException("Illegal descriptor " + type.getDescriptor());
                case 'F':
                    return LocalFloatRef.class;
                case 'I':
                    return LocalIntRef.class;
                case 'J':
                    return LocalLongRef.class;
                case 'S':
                    return LocalShortRef.class;
                case 'Z':
                    return LocalBooleanRef.class;
            }
        }
    }

    public static Type getTargetType(Type type, Type generic) {
        if (type.getSort() == 10 && MixinExtrasService.getInstance().isClassOwned(type.getClassName())) {
            String var2 = StringUtils.substringAfterLast(type.getInternalName(), "/");
            switch(var2) {
                case "LocalBooleanRef":
                    return Type.BOOLEAN_TYPE;
                case "LocalByteRef":
                    return Type.BYTE_TYPE;
                case "LocalCharRef":
                    return Type.CHAR_TYPE;
                case "LocalDoubleRef":
                    return Type.DOUBLE_TYPE;
                case "LocalFloatRef":
                    return Type.FLOAT_TYPE;
                case "LocalIntRef":
                    return Type.INT_TYPE;
                case "LocalLongRef":
                    return Type.LONG_TYPE;
                case "LocalShortRef":
                    return Type.SHORT_TYPE;
                case "LocalRef":
                    if (generic == null) {
                        throw new IllegalStateException("LocalRef must have a concrete type argument!");
                    }
                    return generic;
                default:
                    return type;
            }
        } else {
            return type;
        }
    }

    public static void generateNew(InsnList insns, Type innerType) {
        String refImpl = LocalRefClassGenerator.getForType(innerType);
        insns.add(new TypeInsnNode(187, refImpl));
        insns.add(new InsnNode(89));
        insns.add(new MethodInsnNode(183, refImpl, "<init>", "()V", false));
    }

    public static void generateInitialization(InsnList insns, Type innerType) {
        String refImpl = LocalRefClassGenerator.getForType(innerType);
        insns.add(new MethodInsnNode(182, refImpl, "init", Type.getMethodDescriptor(Type.VOID_TYPE, new Type[] { getErasedType(innerType) }), false));
    }

    public static void generateDisposal(InsnList insns, Type innerType) {
        String refImpl = LocalRefClassGenerator.getForType(innerType);
        insns.add(new MethodInsnNode(182, refImpl, "dispose", Type.getMethodDescriptor(getErasedType(innerType), new Type[0]), false));
        if (!ASMUtils.isPrimitive(innerType)) {
            insns.add(new TypeInsnNode(192, innerType.getInternalName()));
        }
    }

    public static void generateUnwrapping(InsnList insns, Type innerType, Runnable load) {
        String refInterface = Type.getInternalName(getInterfaceFor(innerType));
        load.run();
        insns.add(new MethodInsnNode(185, refInterface, "get", Type.getMethodDescriptor(getErasedType(innerType), new Type[0]), true));
        if (!ASMUtils.isPrimitive(innerType)) {
            insns.add(new TypeInsnNode(192, innerType.getInternalName()));
        }
    }

    private static Type getErasedType(Type actual) {
        return ASMUtils.isPrimitive(actual) ? actual : Type.getType(Object.class);
    }
}
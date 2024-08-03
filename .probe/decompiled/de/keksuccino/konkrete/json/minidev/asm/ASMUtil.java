package de.keksuccino.konkrete.json.minidev.asm;

import java.lang.reflect.Field;
import java.util.HashMap;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

public class ASMUtil {

    public static void autoBoxing(MethodVisitor mv, Class<?> clz) {
        autoBoxing(mv, Type.getType(clz));
    }

    public static Accessor[] getAccessors(Class<?> type, FieldFilter filter) {
        Class<?> nextClass = type;
        HashMap<String, Accessor> map = new HashMap();
        if (filter == null) {
            filter = BasicFiledFilter.SINGLETON;
        }
        while (nextClass != Object.class) {
            Field[] declaredFields = nextClass.getDeclaredFields();
            for (Field field : declaredFields) {
                String fn = field.getName();
                if (!map.containsKey(fn)) {
                    Accessor acc = new Accessor(nextClass, field, filter);
                    if (acc.isUsable()) {
                        map.put(fn, acc);
                    }
                }
            }
            nextClass = nextClass.getSuperclass();
        }
        return (Accessor[]) map.values().toArray(new Accessor[map.size()]);
    }

    protected static void autoBoxing(MethodVisitor mv, Type fieldType) {
        switch(fieldType.getSort()) {
            case 1:
                mv.visitMethodInsn(184, "java/lang/Boolean", "valueOf", "(Z)Ljava/lang/Boolean;", false);
                break;
            case 2:
                mv.visitMethodInsn(184, "java/lang/Character", "valueOf", "(C)Ljava/lang/Character;", false);
                break;
            case 3:
                mv.visitMethodInsn(184, "java/lang/Byte", "valueOf", "(B)Ljava/lang/Byte;", false);
                break;
            case 4:
                mv.visitMethodInsn(184, "java/lang/Short", "valueOf", "(S)Ljava/lang/Short;", false);
                break;
            case 5:
                mv.visitMethodInsn(184, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false);
                break;
            case 6:
                mv.visitMethodInsn(184, "java/lang/Float", "valueOf", "(F)Ljava/lang/Float;", false);
                break;
            case 7:
                mv.visitMethodInsn(184, "java/lang/Long", "valueOf", "(J)Ljava/lang/Long;", false);
                break;
            case 8:
                mv.visitMethodInsn(184, "java/lang/Double", "valueOf", "(D)Ljava/lang/Double;", false);
        }
    }

    protected static void autoUnBoxing1(MethodVisitor mv, Type fieldType) {
        switch(fieldType.getSort()) {
            case 1:
                mv.visitTypeInsn(192, "java/lang/Boolean");
                mv.visitMethodInsn(182, "java/lang/Boolean", "booleanValue", "()Z", false);
                break;
            case 2:
                mv.visitTypeInsn(192, "java/lang/Character");
                mv.visitMethodInsn(182, "java/lang/Character", "charValue", "()C", false);
                break;
            case 3:
                mv.visitTypeInsn(192, "java/lang/Byte");
                mv.visitMethodInsn(182, "java/lang/Byte", "byteValue", "()B", false);
                break;
            case 4:
                mv.visitTypeInsn(192, "java/lang/Short");
                mv.visitMethodInsn(182, "java/lang/Short", "shortValue", "()S", false);
                break;
            case 5:
                mv.visitTypeInsn(192, "java/lang/Integer");
                mv.visitMethodInsn(182, "java/lang/Integer", "intValue", "()I", false);
                break;
            case 6:
                mv.visitTypeInsn(192, "java/lang/Float");
                mv.visitMethodInsn(182, "java/lang/Float", "floatValue", "()F", false);
                break;
            case 7:
                mv.visitTypeInsn(192, "java/lang/Long");
                mv.visitMethodInsn(182, "java/lang/Long", "longValue", "()J", false);
                break;
            case 8:
                mv.visitTypeInsn(192, "java/lang/Double");
                mv.visitMethodInsn(182, "java/lang/Double", "doubleValue", "()D", false);
                break;
            case 9:
                mv.visitTypeInsn(192, fieldType.getInternalName());
                break;
            default:
                mv.visitTypeInsn(192, fieldType.getInternalName());
        }
    }

    protected static void autoUnBoxing2(MethodVisitor mv, Type fieldType) {
        switch(fieldType.getSort()) {
            case 1:
                mv.visitTypeInsn(192, "java/lang/Boolean");
                mv.visitMethodInsn(182, "java/lang/Boolean", "booleanValue", "()Z", false);
                break;
            case 2:
                mv.visitTypeInsn(192, "java/lang/Character");
                mv.visitMethodInsn(182, "java/lang/Character", "charValue", "()C", false);
                break;
            case 3:
                mv.visitTypeInsn(192, "java/lang/Number");
                mv.visitMethodInsn(182, "java/lang/Number", "byteValue", "()B", false);
                break;
            case 4:
                mv.visitTypeInsn(192, "java/lang/Number");
                mv.visitMethodInsn(182, "java/lang/Number", "shortValue", "()S", false);
                break;
            case 5:
                mv.visitTypeInsn(192, "java/lang/Number");
                mv.visitMethodInsn(182, "java/lang/Number", "intValue", "()I", false);
                break;
            case 6:
                mv.visitTypeInsn(192, "java/lang/Number");
                mv.visitMethodInsn(182, "java/lang/Number", "floatValue", "()F", false);
                break;
            case 7:
                mv.visitTypeInsn(192, "java/lang/Number");
                mv.visitMethodInsn(182, "java/lang/Number", "longValue", "()J", false);
                break;
            case 8:
                mv.visitTypeInsn(192, "java/lang/Number");
                mv.visitMethodInsn(182, "java/lang/Number", "doubleValue", "()D", false);
                break;
            case 9:
                mv.visitTypeInsn(192, fieldType.getInternalName());
                break;
            default:
                mv.visitTypeInsn(192, fieldType.getInternalName());
        }
    }

    public static Label[] newLabels(int cnt) {
        Label[] r = new Label[cnt];
        for (int i = 0; i < cnt; i++) {
            r[i] = new Label();
        }
        return r;
    }

    public static String getSetterName(String key) {
        int len = key.length();
        char[] b = new char[len + 3];
        b[0] = 's';
        b[1] = 'e';
        b[2] = 't';
        char c = key.charAt(0);
        if (c >= 'a' && c <= 'z') {
            c = (char) (c - ' ');
        }
        b[3] = c;
        for (int i = 1; i < len; i++) {
            b[i + 3] = key.charAt(i);
        }
        return new String(b);
    }

    public static String getGetterName(String key) {
        int len = key.length();
        char[] b = new char[len + 3];
        b[0] = 'g';
        b[1] = 'e';
        b[2] = 't';
        char c = key.charAt(0);
        if (c >= 'a' && c <= 'z') {
            c = (char) (c - ' ');
        }
        b[3] = c;
        for (int i = 1; i < len; i++) {
            b[i + 3] = key.charAt(i);
        }
        return new String(b);
    }

    public static String getIsName(String key) {
        int len = key.length();
        char[] b = new char[len + 2];
        b[0] = 'i';
        b[1] = 's';
        char c = key.charAt(0);
        if (c >= 'a' && c <= 'z') {
            c = (char) (c - ' ');
        }
        b[2] = c;
        for (int i = 1; i < len; i++) {
            b[i + 2] = key.charAt(i);
        }
        return new String(b);
    }
}
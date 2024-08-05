package moe.wolfgirl.probejs.docs;

import java.util.Collection;
import java.util.List;
import moe.wolfgirl.probejs.lang.java.clazz.ClassPath;
import moe.wolfgirl.probejs.lang.transpiler.TypeConverter;
import moe.wolfgirl.probejs.lang.typescript.Declaration;
import moe.wolfgirl.probejs.lang.typescript.ScriptDump;
import moe.wolfgirl.probejs.lang.typescript.code.Code;
import moe.wolfgirl.probejs.lang.typescript.code.type.Types;
import moe.wolfgirl.probejs.lang.typescript.code.type.js.JSPrimitiveType;
import moe.wolfgirl.probejs.plugin.ProbeJSPlugin;

public class Primitives extends ProbeJSPlugin {

    public static final JSPrimitiveType LONG = Types.primitive("long");

    public static final JSPrimitiveType INTEGER = Types.primitive("integer");

    public static final JSPrimitiveType SHORT = Types.primitive("short");

    public static final JSPrimitiveType BYTE = Types.primitive("byte");

    public static final JSPrimitiveType DOUBLE = Types.primitive("double");

    public static final JSPrimitiveType FLOAT = Types.primitive("float");

    public static final JSPrimitiveType CHARACTER = Types.primitive("character");

    public static final JSPrimitiveType CHAR_SEQUENCE = Types.primitive("charseq");

    @Override
    public void addPredefinedTypes(TypeConverter converter) {
        converter.addType(Object.class, Types.ANY);
        converter.addType(String.class, Types.STRING);
        converter.addType(CharSequence.class, CHAR_SEQUENCE);
        converter.addType(Character.class, CHARACTER);
        converter.addType(char.class, CHARACTER);
        converter.addType(Void.class, Types.VOID);
        converter.addType(void.class, Types.VOID);
        converter.addType(Long.class, LONG);
        converter.addType(long.class, LONG);
        converter.addType(Integer.class, INTEGER);
        converter.addType(int.class, INTEGER);
        converter.addType(Short.class, SHORT);
        converter.addType(short.class, SHORT);
        converter.addType(Byte.class, BYTE);
        converter.addType(byte.class, BYTE);
        converter.addType(Number.class, Types.NUMBER);
        converter.addType(Double.class, DOUBLE);
        converter.addType(double.class, DOUBLE);
        converter.addType(Float.class, FLOAT);
        converter.addType(float.class, FLOAT);
        converter.addType(Boolean.class, Types.BOOLEAN);
        converter.addType(boolean.class, Types.BOOLEAN);
    }

    @Override
    public void addGlobals(ScriptDump scriptDump) {
        scriptDump.addGlobal("primitives", Primitives.JavaPrimitive.of("long", "Number"), Primitives.JavaPrimitive.of("integer", "Number"), Primitives.JavaPrimitive.of("short", "Number"), Primitives.JavaPrimitive.of("byte", "Number"), Primitives.JavaPrimitive.of("double", "Number"), Primitives.JavaPrimitive.of("float", "Number"), Primitives.JavaPrimitive.of("character", "String"), Primitives.JavaPrimitive.of("charseq", "String"));
    }

    static class JavaPrimitive extends Code {

        private final String javaPrimitive;

        private final String jsInterface;

        JavaPrimitive(String javaPrimitive, String jsInterface) {
            this.javaPrimitive = javaPrimitive;
            this.jsInterface = jsInterface;
        }

        @Override
        public Collection<ClassPath> getUsedClassPaths() {
            return List.of();
        }

        @Override
        public List<String> format(Declaration declaration) {
            return List.of("interface %s extends %s {}".formatted(this.javaPrimitive, this.jsInterface));
        }

        static Primitives.JavaPrimitive of(String javaPrimitive, String jsInterface) {
            return new Primitives.JavaPrimitive(javaPrimitive, jsInterface);
        }
    }
}
package de.keksuccino.konkrete.json.minidev.asm;

import java.lang.reflect.Method;
import java.util.HashMap;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

public class BeansAccessBuilder {

    private static String METHOD_ACCESS_NAME = Type.getInternalName(BeansAccess.class);

    final Class<?> type;

    final Accessor[] accs;

    final DynamicClassLoader loader;

    final String className;

    final String accessClassName;

    final String accessClassNameInternal;

    final String classNameInternal;

    final HashMap<Class<?>, Method> convMtds = new HashMap();

    Class<? extends Exception> exceptionClass = NoSuchFieldException.class;

    public BeansAccessBuilder(Class<?> type, Accessor[] accs, DynamicClassLoader loader) {
        this.type = type;
        this.accs = accs;
        this.loader = loader;
        this.className = type.getName();
        if (this.className.startsWith("java.")) {
            this.accessClassName = "net.minidev.asm." + this.className + "AccAccess";
        } else {
            this.accessClassName = this.className.concat("AccAccess");
        }
        this.accessClassNameInternal = this.accessClassName.replace('.', '/');
        this.classNameInternal = this.className.replace('.', '/');
    }

    public void addConversion(Iterable<Class<?>> conv) {
        if (conv != null) {
            for (Class<?> c : conv) {
                this.addConversion(c);
            }
        }
    }

    public void addConversion(Class<?> conv) {
        if (conv != null) {
            for (Method mtd : conv.getMethods()) {
                if ((mtd.getModifiers() & 8) != 0) {
                    Class<?>[] param = mtd.getParameterTypes();
                    if (param.length == 1 && param[0].equals(Object.class)) {
                        Class<?> rType = mtd.getReturnType();
                        if (!rType.equals(void.class)) {
                            this.convMtds.put(rType, mtd);
                        }
                    }
                }
            }
        }
    }

    public Class<?> bulid() {
        ClassWriter cw = new ClassWriter(1);
        boolean USE_HASH = this.accs.length > 10;
        int HASH_LIMIT = 14;
        String signature = "Lnet/minidev/asm/BeansAccess<L" + this.classNameInternal + ";>;";
        cw.visit(50, 33, this.accessClassNameInternal, signature, METHOD_ACCESS_NAME, null);
        MethodVisitor mv = cw.visitMethod(1, "<init>", "()V", null, null);
        mv.visitCode();
        mv.visitVarInsn(25, 0);
        mv.visitMethodInsn(183, METHOD_ACCESS_NAME, "<init>", "()V", false);
        mv.visitInsn(177);
        mv.visitMaxs(1, 1);
        mv.visitEnd();
        mv = cw.visitMethod(1, "set", "(Ljava/lang/Object;ILjava/lang/Object;)V", null, null);
        mv.visitCode();
        if (this.accs.length != 0) {
            if (this.accs.length > HASH_LIMIT) {
                mv.visitVarInsn(21, 2);
                Label[] labels = ASMUtil.newLabels(this.accs.length);
                Label defaultLabel = new Label();
                mv.visitTableSwitchInsn(0, labels.length - 1, defaultLabel, labels);
                int i = 0;
                for (Accessor acc : this.accs) {
                    mv.visitLabel(labels[i++]);
                    if (!acc.isWritable()) {
                        mv.visitInsn(177);
                    } else {
                        this.internalSetFiled(mv, acc);
                    }
                }
                mv.visitLabel(defaultLabel);
            } else {
                Label[] labels = ASMUtil.newLabels(this.accs.length);
                int i = 0;
                for (Accessor accx : this.accs) {
                    this.ifNotEqJmp(mv, 2, i, labels[i]);
                    this.internalSetFiled(mv, accx);
                    mv.visitLabel(labels[i]);
                    mv.visitFrame(3, 0, null, 0, null);
                    i++;
                }
            }
        }
        if (this.exceptionClass != null) {
            this.throwExIntParam(mv, this.exceptionClass);
        } else {
            mv.visitInsn(177);
        }
        mv.visitMaxs(0, 0);
        mv.visitEnd();
        mv = cw.visitMethod(1, "get", "(Ljava/lang/Object;I)Ljava/lang/Object;", null, null);
        mv.visitCode();
        if (this.accs.length == 0) {
            mv.visitFrame(3, 0, null, 0, null);
        } else if (this.accs.length > HASH_LIMIT) {
            mv.visitVarInsn(21, 2);
            Label[] labels = ASMUtil.newLabels(this.accs.length);
            Label defaultLabel = new Label();
            mv.visitTableSwitchInsn(0, labels.length - 1, defaultLabel, labels);
            int i = 0;
            for (Accessor accx : this.accs) {
                mv.visitLabel(labels[i++]);
                mv.visitFrame(3, 0, null, 0, null);
                if (!accx.isReadable()) {
                    mv.visitInsn(1);
                    mv.visitInsn(176);
                } else {
                    mv.visitVarInsn(25, 1);
                    mv.visitTypeInsn(192, this.classNameInternal);
                    Type fieldType = Type.getType(accx.getType());
                    if (!accx.isPublic() && accx.getter != null) {
                        String sig = Type.getMethodDescriptor(accx.getter);
                        mv.visitMethodInsn(182, this.classNameInternal, accx.getter.getName(), sig, false);
                    } else {
                        mv.visitFieldInsn(180, this.classNameInternal, accx.getName(), fieldType.getDescriptor());
                    }
                    ASMUtil.autoBoxing(mv, fieldType);
                    mv.visitInsn(176);
                }
            }
            mv.visitLabel(defaultLabel);
            mv.visitFrame(3, 0, null, 0, null);
        } else {
            Label[] labels = ASMUtil.newLabels(this.accs.length);
            int i = 0;
            for (Accessor accxx : this.accs) {
                this.ifNotEqJmp(mv, 2, i, labels[i]);
                mv.visitVarInsn(25, 1);
                mv.visitTypeInsn(192, this.classNameInternal);
                Type fieldType = Type.getType(accxx.getType());
                if (!accxx.isPublic() && accxx.getter != null) {
                    if (accxx.getter == null) {
                        throw new RuntimeException("no Getter for field " + accxx.getName() + " in class " + this.className);
                    }
                    String sig = Type.getMethodDescriptor(accxx.getter);
                    mv.visitMethodInsn(182, this.classNameInternal, accxx.getter.getName(), sig, false);
                } else {
                    mv.visitFieldInsn(180, this.classNameInternal, accxx.getName(), fieldType.getDescriptor());
                }
                ASMUtil.autoBoxing(mv, fieldType);
                mv.visitInsn(176);
                mv.visitLabel(labels[i]);
                mv.visitFrame(3, 0, null, 0, null);
                i++;
            }
        }
        if (this.exceptionClass != null) {
            this.throwExIntParam(mv, this.exceptionClass);
        } else {
            mv.visitInsn(1);
            mv.visitInsn(176);
        }
        mv.visitMaxs(0, 0);
        mv.visitEnd();
        if (!USE_HASH) {
            mv = cw.visitMethod(1, "set", "(Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Object;)V", null, null);
            mv.visitCode();
            Label[] labels = ASMUtil.newLabels(this.accs.length);
            int i = 0;
            for (Accessor accxx : this.accs) {
                mv.visitVarInsn(25, 2);
                mv.visitLdcInsn(accxx.fieldName);
                mv.visitMethodInsn(182, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false);
                mv.visitJumpInsn(153, labels[i]);
                this.internalSetFiled(mv, accxx);
                mv.visitLabel(labels[i]);
                mv.visitFrame(3, 0, null, 0, null);
                i++;
            }
            if (this.exceptionClass != null) {
                this.throwExStrParam(mv, this.exceptionClass);
            } else {
                mv.visitInsn(177);
            }
            mv.visitMaxs(0, 0);
            mv.visitEnd();
        }
        if (!USE_HASH) {
            mv = cw.visitMethod(1, "get", "(Ljava/lang/Object;Ljava/lang/String;)Ljava/lang/Object;", null, null);
            mv.visitCode();
            Label[] labels = ASMUtil.newLabels(this.accs.length);
            int i = 0;
            for (Accessor accxx : this.accs) {
                mv.visitVarInsn(25, 2);
                mv.visitLdcInsn(accxx.fieldName);
                mv.visitMethodInsn(182, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false);
                mv.visitJumpInsn(153, labels[i]);
                mv.visitVarInsn(25, 1);
                mv.visitTypeInsn(192, this.classNameInternal);
                Type fieldType = Type.getType(accxx.getType());
                if (!accxx.isPublic() && accxx.getter != null) {
                    String sig = Type.getMethodDescriptor(accxx.getter);
                    mv.visitMethodInsn(182, this.classNameInternal, accxx.getter.getName(), sig, false);
                } else {
                    mv.visitFieldInsn(180, this.classNameInternal, accxx.getName(), fieldType.getDescriptor());
                }
                ASMUtil.autoBoxing(mv, fieldType);
                mv.visitInsn(176);
                mv.visitLabel(labels[i]);
                mv.visitFrame(3, 0, null, 0, null);
                i++;
            }
            if (this.exceptionClass != null) {
                this.throwExStrParam(mv, this.exceptionClass);
            } else {
                mv.visitInsn(1);
                mv.visitInsn(176);
            }
            mv.visitMaxs(0, 0);
            mv.visitEnd();
        }
        mv = cw.visitMethod(1, "newInstance", "()Ljava/lang/Object;", null, null);
        mv.visitCode();
        mv.visitTypeInsn(187, this.classNameInternal);
        mv.visitInsn(89);
        mv.visitMethodInsn(183, this.classNameInternal, "<init>", "()V", false);
        mv.visitInsn(176);
        mv.visitMaxs(2, 1);
        mv.visitEnd();
        cw.visitEnd();
        byte[] data = cw.toByteArray();
        return this.loader.defineClass(this.accessClassName, data);
    }

    private void dumpDebug(byte[] data, String destFile) {
    }

    private void internalSetFiled(MethodVisitor mv, Accessor acc) {
        mv.visitVarInsn(25, 1);
        mv.visitTypeInsn(192, this.classNameInternal);
        mv.visitVarInsn(25, 3);
        Type fieldType = Type.getType(acc.getType());
        Class<?> type = acc.getType();
        String destClsName = Type.getInternalName(type);
        Method conMtd = (Method) this.convMtds.get(type);
        if (conMtd != null) {
            String clsSig = Type.getInternalName(conMtd.getDeclaringClass());
            String mtdName = conMtd.getName();
            String mtdSig = Type.getMethodDescriptor(conMtd);
            mv.visitMethodInsn(184, clsSig, mtdName, mtdSig, false);
        } else if (acc.isEnum()) {
            Label isNull = new Label();
            mv.visitJumpInsn(198, isNull);
            mv.visitVarInsn(25, 3);
            mv.visitMethodInsn(182, "java/lang/Object", "toString", "()Ljava/lang/String;", false);
            mv.visitMethodInsn(184, destClsName, "valueOf", "(Ljava/lang/String;)L" + destClsName + ";", false);
            mv.visitVarInsn(58, 3);
            mv.visitLabel(isNull);
            mv.visitFrame(3, 0, null, 0, null);
            mv.visitVarInsn(25, 1);
            mv.visitTypeInsn(192, this.classNameInternal);
            mv.visitVarInsn(25, 3);
            mv.visitTypeInsn(192, destClsName);
        } else if (type.equals(String.class)) {
            Label isNull = new Label();
            mv.visitJumpInsn(198, isNull);
            mv.visitVarInsn(25, 3);
            mv.visitMethodInsn(182, "java/lang/Object", "toString", "()Ljava/lang/String;", false);
            mv.visitVarInsn(58, 3);
            mv.visitLabel(isNull);
            mv.visitFrame(3, 0, null, 0, null);
            mv.visitVarInsn(25, 1);
            mv.visitTypeInsn(192, this.classNameInternal);
            mv.visitVarInsn(25, 3);
            mv.visitTypeInsn(192, destClsName);
        } else {
            mv.visitTypeInsn(192, destClsName);
        }
        if (!acc.isPublic() && acc.setter != null) {
            String sig = Type.getMethodDescriptor(acc.setter);
            mv.visitMethodInsn(182, this.classNameInternal, acc.setter.getName(), sig, false);
        } else {
            mv.visitFieldInsn(181, this.classNameInternal, acc.getName(), fieldType.getDescriptor());
        }
        mv.visitInsn(177);
    }

    private void throwExIntParam(MethodVisitor mv, Class<?> exCls) {
        String exSig = Type.getInternalName(exCls);
        mv.visitTypeInsn(187, exSig);
        mv.visitInsn(89);
        mv.visitLdcInsn("mapping " + this.className + " failed to map field:");
        mv.visitVarInsn(21, 2);
        mv.visitMethodInsn(184, "java/lang/Integer", "toString", "(I)Ljava/lang/String;", false);
        mv.visitMethodInsn(182, "java/lang/String", "concat", "(Ljava/lang/String;)Ljava/lang/String;", false);
        mv.visitMethodInsn(183, exSig, "<init>", "(Ljava/lang/String;)V", false);
        mv.visitInsn(191);
    }

    private void throwExStrParam(MethodVisitor mv, Class<?> exCls) {
        String exSig = Type.getInternalName(exCls);
        mv.visitTypeInsn(187, exSig);
        mv.visitInsn(89);
        mv.visitLdcInsn("mapping " + this.className + " failed to map field:");
        mv.visitVarInsn(25, 2);
        mv.visitMethodInsn(182, "java/lang/String", "concat", "(Ljava/lang/String;)Ljava/lang/String;", false);
        mv.visitMethodInsn(183, exSig, "<init>", "(Ljava/lang/String;)V", false);
        mv.visitInsn(191);
    }

    private void ifNotEqJmp(MethodVisitor mv, int param, int value, Label label) {
        mv.visitVarInsn(21, param);
        if (value == 0) {
            mv.visitJumpInsn(154, label);
        } else if (value == 1) {
            mv.visitInsn(4);
            mv.visitJumpInsn(160, label);
        } else if (value == 2) {
            mv.visitInsn(5);
            mv.visitJumpInsn(160, label);
        } else if (value == 3) {
            mv.visitInsn(6);
            mv.visitJumpInsn(160, label);
        } else if (value == 4) {
            mv.visitInsn(7);
            mv.visitJumpInsn(160, label);
        } else if (value == 5) {
            mv.visitInsn(8);
            mv.visitJumpInsn(160, label);
        } else {
            if (value < 6) {
                throw new RuntimeException("non supported negative values");
            }
            mv.visitIntInsn(16, value);
            mv.visitJumpInsn(160, label);
        }
    }
}
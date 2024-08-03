package org.embeddedt.embeddium.render.world;

import com.google.common.base.Suppliers;
import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.function.Supplier;
import me.jellysquid.mods.sodium.client.world.WorldSlice;
import net.minecraft.world.level.BlockAndTintGetter;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.spongepowered.asm.mixin.MixinEnvironment;

public class WorldSliceLocalGenerator {

    private static final Class<?> WORLD_SLICE_LOCAL_CLASS = createWrapperClass();

    private static final MethodHandle WORLD_SLICE_LOCAL_CONSTRUCTOR;

    private static final String WORLD_SLICE_LOCAL_CLASS_NAME = "org/embeddedt/embeddium/render/world/WorldSliceLocal";

    private static final String WORLD_SLICE_LOCAL_CLASS_DESC = "Lorg/embeddedt/embeddium/render/world/WorldSliceLocal;";

    private static final Supplier<WorldSliceLocalGenerator.Definer> DEFINE_CLASS = Suppliers.memoize(() -> {
        try {
            Method makePrivateLookup = MethodHandles.class.getMethod("privateLookupIn", Class.class, Lookup.class);
            Object privateLookup = makePrivateLookup.invoke(null, WorldSliceLocalGenerator.class, MethodHandles.lookup());
            Method defineClass = Lookup.class.getMethod("defineClass", byte[].class);
            return (bytes, name) -> (Class<?>) defineClass.invoke(privateLookup, bytes);
        } catch (Exception var4) {
            try {
                Method defineClassx = ClassLoader.class.getDeclaredMethod("defineClass", String.class, byte[].class, int.class, int.class);
                defineClassx.setAccessible(true);
                ClassLoader loader = WorldSliceLocalGenerator.class.getClassLoader();
                return (bytes, name) -> (Class<?>) defineClass.invoke(loader, name, bytes, 0, bytes.length);
            } catch (NoSuchMethodException var3) {
                throw new RuntimeException(var3);
            }
        }
    });

    private static final boolean VERIFY = false;

    public static BlockAndTintGetter generate(WorldSlice originalSlice) {
        try {
            return (BlockAndTintGetter) WORLD_SLICE_LOCAL_CONSTRUCTOR.invokeExact(originalSlice);
        } catch (Throwable var2) {
            throw new RuntimeException("Exception creating WorldSlice wrapper", var2);
        }
    }

    private static byte[] createWrapperClassBytecode() {
        String worldSliceDesc = Type.getDescriptor(WorldSlice.class);
        ClassWriter classWriter = new ClassWriter(0);
        ClassVisitor classVisitor = classWriter;
        Class<?>[] interfaces = WorldSlice.class.getInterfaces();
        classWriter.visit(MixinEnvironment.getCompatibilityLevel().getClassVersion(), 33, "org/embeddedt/embeddium/render/world/WorldSliceLocal", null, "java/lang/Object", (String[]) Arrays.stream(interfaces).map(Type::getInternalName).toArray(String[]::new));
        FieldVisitor fieldVisitor = classWriter.visitField(18, "view", worldSliceDesc, null, null);
        fieldVisitor.visitEnd();
        classWriter.visitSource(null, null);
        MethodVisitor methodVisitor = classWriter.visitMethod(1, "<init>", "(" + worldSliceDesc + ")V", null, null);
        methodVisitor.visitCode();
        Label label0 = new Label();
        methodVisitor.visitLabel(label0);
        methodVisitor.visitVarInsn(25, 0);
        methodVisitor.visitMethodInsn(183, "java/lang/Object", "<init>", "()V", false);
        methodVisitor.visitVarInsn(25, 0);
        methodVisitor.visitVarInsn(25, 1);
        methodVisitor.visitFieldInsn(181, "org/embeddedt/embeddium/render/world/WorldSliceLocal", "view", worldSliceDesc);
        methodVisitor.visitInsn(177);
        Label label3 = new Label();
        methodVisitor.visitLabel(label3);
        methodVisitor.visitLocalVariable("this", "Lorg/embeddedt/embeddium/render/world/WorldSliceLocal;", null, label0, label3, 0);
        methodVisitor.visitLocalVariable("view", Type.getDescriptor(WorldSlice.class), null, label0, label3, 1);
        methodVisitor.visitMaxs(2, 2);
        methodVisitor.visitEnd();
        for (Method method : WorldSlice.class.getMethods()) {
            if (Modifier.isPublic(method.getModifiers()) && !Modifier.isStatic(method.getModifiers()) && !method.getDeclaringClass().isAssignableFrom(Object.class)) {
                int maxStack = 0;
                String methodDescription = Type.getMethodDescriptor(method);
                methodVisitor = classVisitor.visitMethod(1, method.getName(), methodDescription, null, null);
                methodVisitor.visitCode();
                methodVisitor.visitVarInsn(25, 0);
                methodVisitor.visitFieldInsn(180, "org/embeddedt/embeddium/render/world/WorldSliceLocal", "view", worldSliceDesc);
                maxStack++;
                int maxLocals = 1;
                for (Type t : Type.getArgumentTypes(method)) {
                    int size = t.getSize();
                    methodVisitor.visitVarInsn(t.getOpcode(21), maxLocals);
                    maxLocals += size;
                    maxStack += size;
                }
                boolean itf = method.getDeclaringClass().isInterface();
                methodVisitor.visitMethodInsn(itf ? 185 : 182, Type.getInternalName(method.getDeclaringClass()), method.getName(), methodDescription, itf);
                Type returnType = Type.getReturnType(methodDescription);
                methodVisitor.visitInsn(returnType.getOpcode(172));
                methodVisitor.visitMaxs(maxStack, maxLocals);
                methodVisitor.visitEnd();
            }
        }
        classVisitor.visitEnd();
        return classWriter.toByteArray();
    }

    private static Class<?> createWrapperClass() {
        byte[] bytes = createWrapperClassBytecode();
        try {
            return ((WorldSliceLocalGenerator.Definer) DEFINE_CLASS.get()).define(bytes, "org/embeddedt/embeddium/render/world/WorldSliceLocal".replace('/', '.'));
        } catch (Exception var2) {
            throw new RuntimeException("Error defining WorldSlice wrapper", var2);
        }
    }

    public static void testClassGeneration() {
        try {
            Files.write(new File("/tmp/WorldSliceLocal.class").toPath(), createWrapperClassBytecode(), new OpenOption[] { StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING });
        } catch (IOException var1) {
            var1.printStackTrace();
        }
    }

    static {
        try {
            WORLD_SLICE_LOCAL_CONSTRUCTOR = MethodHandles.publicLookup().findConstructor(WORLD_SLICE_LOCAL_CLASS, MethodType.methodType(void.class, WorldSlice.class)).asType(MethodType.methodType(BlockAndTintGetter.class, WorldSlice.class));
        } catch (IllegalAccessException | NoSuchMethodException var1) {
            throw new RuntimeException(var1);
        }
    }

    private interface Definer {

        Class<?> define(byte[] var1, String var2) throws Exception;
    }
}
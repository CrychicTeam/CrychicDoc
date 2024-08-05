package me.jellysquid.mods.sodium.client.render.vertex.serializers.generated;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.util.ArrayList;
import java.util.List;
import net.caffeinemc.mods.sodium.api.vertex.attributes.CommonVertexAttribute;
import net.caffeinemc.mods.sodium.api.vertex.format.VertexFormatDescription;
import net.caffeinemc.mods.sodium.api.vertex.serializer.VertexSerializer;
import org.lwjgl.system.MemoryUtil;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public class VertexSerializerFactory {

    private static final Lookup LOOKUP = MethodHandles.lookup();

    public static VertexSerializerFactory.Bytecode generate(VertexFormatDescription srcFormat, VertexFormatDescription dstFormat, String identifier) {
        List<VertexSerializerFactory.MemoryTransfer> memoryCopies = createMemoryTransferList(srcFormat, dstFormat);
        String name = "me/jellysquid/mods/sodium/client/render/vertex/serializers/generated/VertexSerializer$Impl$" + identifier;
        ClassWriter classWriter = new ClassWriter(0);
        classWriter.visit(61, 17, name, null, Type.getInternalName(Object.class), new String[] { Type.getInternalName(VertexSerializer.class) });
        int localThis = 0;
        MethodVisitor methodVisitor = classWriter.visitMethod(1, "<init>", "()V", null, null);
        methodVisitor.visitCode();
        Label labelInit = new Label();
        methodVisitor.visitLabel(labelInit);
        methodVisitor.visitVarInsn(25, 0);
        methodVisitor.visitMethodInsn(183, "java/lang/Object", "<init>", "()V", false);
        Label labelExit = new Label();
        methodVisitor.visitLabel(labelExit);
        methodVisitor.visitInsn(177);
        Label labelEnd = new Label();
        methodVisitor.visitLabel(labelEnd);
        methodVisitor.visitLocalVariable("this", "L" + name + ";", null, labelInit, labelEnd, 0);
        methodVisitor.visitMaxs(2, 1);
        methodVisitor.visitEnd();
        localThis = 0;
        int localSrcPointer = 1;
        int localDstPointer = 3;
        int localVertexCount = 5;
        int localVertexIndex = 6;
        MethodVisitor methodVisitorx = classWriter.visitMethod(1, "serialize", "(JJI)V", null, null);
        methodVisitorx.visitCode();
        Label labelLoopInit = new Label();
        methodVisitorx.visitLabel(labelLoopInit);
        methodVisitorx.visitInsn(3);
        methodVisitorx.visitVarInsn(54, 6);
        Label labelLoopConditionSetup = new Label();
        methodVisitorx.visitLabel(labelLoopConditionSetup);
        methodVisitorx.visitFrame(1, 1, new Object[] { Opcodes.INTEGER }, 0, null);
        methodVisitorx.visitVarInsn(21, 6);
        methodVisitorx.visitVarInsn(21, 5);
        Label labelLoopConditionComparison = new Label();
        methodVisitorx.visitJumpInsn(162, labelLoopConditionComparison);
        for (VertexSerializerFactory.MemoryTransfer op : memoryCopies) {
            int i = 0;
            while (i < op.length()) {
                int remaining = op.length() - i;
                Label labelMemoryTransfer = new Label();
                methodVisitorx.visitLabel(labelMemoryTransfer);
                methodVisitorx.visitVarInsn(22, 3);
                methodVisitorx.visitLdcInsn((long) (op.dst() + i));
                methodVisitorx.visitInsn(97);
                methodVisitorx.visitVarInsn(22, 1);
                methodVisitorx.visitLdcInsn((long) (op.src() + i));
                methodVisitorx.visitInsn(97);
                if (remaining >= 8) {
                    methodVisitorx.visitMethodInsn(184, Type.getInternalName(MemoryUtil.class), "memGetLong", "(J)J", false);
                    methodVisitorx.visitMethodInsn(184, Type.getInternalName(MemoryUtil.class), "memPutLong", "(JJ)V", false);
                    i += 8;
                } else if (remaining >= 4) {
                    methodVisitorx.visitMethodInsn(184, Type.getInternalName(MemoryUtil.class), "memGetInt", "(J)I", false);
                    methodVisitorx.visitMethodInsn(184, Type.getInternalName(MemoryUtil.class), "memPutInt", "(JI)V", false);
                    i += 4;
                } else if (remaining >= 2) {
                    methodVisitorx.visitMethodInsn(184, Type.getInternalName(MemoryUtil.class), "memGetShort", "(J)S", false);
                    methodVisitorx.visitMethodInsn(184, Type.getInternalName(MemoryUtil.class), "memPutShort", "(JS)V", false);
                    i += 2;
                } else {
                    methodVisitorx.visitMethodInsn(184, Type.getInternalName(MemoryUtil.class), "memGetByte", "(J)B", false);
                    methodVisitorx.visitMethodInsn(184, Type.getInternalName(MemoryUtil.class), "memPutByte", "(JB)V", false);
                    i++;
                }
            }
        }
        Label labelIncrementSourcePointer = new Label();
        methodVisitorx.visitLabel(labelIncrementSourcePointer);
        methodVisitorx.visitVarInsn(22, 1);
        methodVisitorx.visitLdcInsn((long) srcFormat.stride());
        methodVisitorx.visitInsn(97);
        methodVisitorx.visitVarInsn(55, 1);
        Label labelIncrementDestinationPointer = new Label();
        methodVisitorx.visitLabel(labelIncrementDestinationPointer);
        methodVisitorx.visitVarInsn(22, 3);
        methodVisitorx.visitLdcInsn((long) dstFormat.stride());
        methodVisitorx.visitInsn(97);
        methodVisitorx.visitVarInsn(55, 3);
        Label labelRestartLoop = new Label();
        methodVisitorx.visitLabel(labelRestartLoop);
        methodVisitorx.visitIincInsn(6, 1);
        methodVisitorx.visitJumpInsn(167, labelLoopConditionSetup);
        methodVisitorx.visitLabel(labelLoopConditionComparison);
        methodVisitorx.visitFrame(2, 1, null, 0, null);
        methodVisitorx.visitInsn(177);
        Label labelExitx = new Label();
        methodVisitorx.visitLabel(labelExitx);
        methodVisitorx.visitLocalVariable("this", "L" + name + ";", null, labelLoopInit, labelExitx, 0);
        methodVisitorx.visitLocalVariable("src", "J", null, labelLoopInit, labelExitx, 1);
        methodVisitorx.visitLocalVariable("dst", "J", null, labelLoopInit, labelExitx, 3);
        methodVisitorx.visitLocalVariable("vertexCount", "I", null, labelLoopInit, labelExitx, 5);
        methodVisitorx.visitLocalVariable("vertexIndex", "I", null, labelLoopConditionSetup, labelLoopConditionComparison, 6);
        methodVisitorx.visitMaxs(6, 7);
        methodVisitorx.visitEnd();
        classWriter.visitEnd();
        return new VertexSerializerFactory.Bytecode(classWriter.toByteArray());
    }

    private static List<VertexSerializerFactory.MemoryTransfer> createMemoryTransferList(VertexFormatDescription srcVertexFormat, VertexFormatDescription dstVertexFormat) {
        ArrayList<VertexSerializerFactory.MemoryTransfer> ops = new ArrayList();
        for (CommonVertexAttribute elementType : CommonVertexAttribute.values()) {
            if (dstVertexFormat.containsElement(elementType)) {
                if (!srcVertexFormat.containsElement(elementType)) {
                    throw new RuntimeException("Source format is missing element %s as required by destination format".formatted(elementType));
                }
                int srcOffset = srcVertexFormat.getElementOffset(elementType);
                int dstOffset = dstVertexFormat.getElementOffset(elementType);
                ops.add(new VertexSerializerFactory.MemoryTransfer(srcOffset, dstOffset, elementType.getByteLength()));
            }
        }
        return mergeAdjacentMemoryTransfers(ops);
    }

    private static List<VertexSerializerFactory.MemoryTransfer> mergeAdjacentMemoryTransfers(ArrayList<VertexSerializerFactory.MemoryTransfer> src) {
        ArrayList<VertexSerializerFactory.MemoryTransfer> dst = new ArrayList(src.size());
        int srcOffset = 0;
        int dstOffset = 0;
        int length = 0;
        for (VertexSerializerFactory.MemoryTransfer op : src) {
            if (srcOffset + length == op.src() && dstOffset + length == op.dst()) {
                length += op.length();
            } else {
                if (length > 0) {
                    dst.add(new VertexSerializerFactory.MemoryTransfer(srcOffset, dstOffset, length));
                }
                srcOffset = op.src();
                dstOffset = op.dst();
                length = op.length();
            }
        }
        if (length > 0) {
            dst.add(new VertexSerializerFactory.MemoryTransfer(srcOffset, dstOffset, length));
        }
        return dst;
    }

    public static Class<?> define(VertexSerializerFactory.Bytecode bytecode) {
        try {
            return LOOKUP.defineClass(bytecode.data);
        } catch (IllegalAccessException var2) {
            throw new RuntimeException("Failed to access generated class", var2);
        }
    }

    public static final class Bytecode {

        private final byte[] data;

        private Bytecode(byte[] data) {
            this.data = data;
        }

        public byte[] copy() {
            return (byte[]) this.data.clone();
        }
    }

    public static record MemoryTransfer(int src, int dst, int length) {
    }
}
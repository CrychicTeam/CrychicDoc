package me.lucko.spark.lib.asm;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ClassReader {

    public static final int SKIP_CODE = 1;

    public static final int SKIP_DEBUG = 2;

    public static final int SKIP_FRAMES = 4;

    public static final int EXPAND_FRAMES = 8;

    static final int EXPAND_ASM_INSNS = 256;

    private static final int INPUT_STREAM_DATA_CHUNK_SIZE = 4096;

    @Deprecated
    public final byte[] b;

    public final int header;

    final byte[] classFileBuffer;

    private final int[] cpInfoOffsets;

    private final String[] constantUtf8Values;

    private final ConstantDynamic[] constantDynamicValues;

    private final int[] bootstrapMethodOffsets;

    private final int maxStringLength;

    public ClassReader(byte[] classFile) {
        this(classFile, 0, classFile.length);
    }

    public ClassReader(byte[] classFileBuffer, int classFileOffset, int classFileLength) {
        this(classFileBuffer, classFileOffset, true);
    }

    ClassReader(byte[] classFileBuffer, int classFileOffset, boolean checkClassVersion) {
        this.classFileBuffer = classFileBuffer;
        this.b = classFileBuffer;
        if (checkClassVersion && this.readShort(classFileOffset + 6) > 61) {
            throw new IllegalArgumentException("Unsupported class file major version " + this.readShort(classFileOffset + 6));
        } else {
            int constantPoolCount = this.readUnsignedShort(classFileOffset + 8);
            this.cpInfoOffsets = new int[constantPoolCount];
            this.constantUtf8Values = new String[constantPoolCount];
            int currentCpInfoIndex = 1;
            int currentCpInfoOffset = classFileOffset + 10;
            int currentMaxStringLength = 0;
            boolean hasBootstrapMethods = false;
            boolean hasConstantDynamic = false;
            while (currentCpInfoIndex < constantPoolCount) {
                this.cpInfoOffsets[currentCpInfoIndex++] = currentCpInfoOffset + 1;
                int cpInfoSize;
                switch(classFileBuffer[currentCpInfoOffset]) {
                    case 1:
                        cpInfoSize = 3 + this.readUnsignedShort(currentCpInfoOffset + 1);
                        if (cpInfoSize > currentMaxStringLength) {
                            currentMaxStringLength = cpInfoSize;
                        }
                        break;
                    case 2:
                    case 13:
                    case 14:
                    default:
                        throw new IllegalArgumentException();
                    case 3:
                    case 4:
                    case 9:
                    case 10:
                    case 11:
                    case 12:
                        cpInfoSize = 5;
                        break;
                    case 5:
                    case 6:
                        cpInfoSize = 9;
                        currentCpInfoIndex++;
                        break;
                    case 7:
                    case 8:
                    case 16:
                    case 19:
                    case 20:
                        cpInfoSize = 3;
                        break;
                    case 15:
                        cpInfoSize = 4;
                        break;
                    case 17:
                        cpInfoSize = 5;
                        hasBootstrapMethods = true;
                        hasConstantDynamic = true;
                        break;
                    case 18:
                        cpInfoSize = 5;
                        hasBootstrapMethods = true;
                }
                currentCpInfoOffset += cpInfoSize;
            }
            this.maxStringLength = currentMaxStringLength;
            this.header = currentCpInfoOffset;
            this.constantDynamicValues = hasConstantDynamic ? new ConstantDynamic[constantPoolCount] : null;
            this.bootstrapMethodOffsets = hasBootstrapMethods ? this.readBootstrapMethodsAttribute(currentMaxStringLength) : null;
        }
    }

    public ClassReader(InputStream inputStream) throws IOException {
        this(readStream(inputStream, false));
    }

    public ClassReader(String className) throws IOException {
        this(readStream(ClassLoader.getSystemResourceAsStream(className.replace('.', '/') + ".class"), true));
    }

    private static byte[] readStream(InputStream inputStream, boolean close) throws IOException {
        if (inputStream == null) {
            throw new IOException("Class not found");
        } else {
            byte[] var5;
            try {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                try {
                    byte[] data = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(data, 0, data.length)) != -1) {
                        outputStream.write(data, 0, bytesRead);
                    }
                    outputStream.flush();
                    var5 = outputStream.toByteArray();
                } catch (Throwable var11) {
                    try {
                        outputStream.close();
                    } catch (Throwable var10) {
                    }
                    throw var11;
                }
                outputStream.close();
            } finally {
                if (close) {
                    inputStream.close();
                }
            }
            return var5;
        }
    }

    public int getAccess() {
        return this.readUnsignedShort(this.header);
    }

    public String getClassName() {
        return this.readClass(this.header + 2, new char[this.maxStringLength]);
    }

    public String getSuperName() {
        return this.readClass(this.header + 4, new char[this.maxStringLength]);
    }

    public String[] getInterfaces() {
        int currentOffset = this.header + 6;
        int interfacesCount = this.readUnsignedShort(currentOffset);
        String[] interfaces = new String[interfacesCount];
        if (interfacesCount > 0) {
            char[] charBuffer = new char[this.maxStringLength];
            for (int i = 0; i < interfacesCount; i++) {
                currentOffset += 2;
                interfaces[i] = this.readClass(currentOffset, charBuffer);
            }
        }
        return interfaces;
    }

    public void accept(ClassVisitor classVisitor, int parsingOptions) {
        this.accept(classVisitor, new Attribute[0], parsingOptions);
    }

    public void accept(ClassVisitor classVisitor, Attribute[] attributePrototypes, int parsingOptions) {
        Context context = new Context();
        context.attributePrototypes = attributePrototypes;
        context.parsingOptions = parsingOptions;
        context.charBuffer = new char[this.maxStringLength];
        char[] charBuffer = context.charBuffer;
        int currentOffset = this.header;
        int accessFlags = this.readUnsignedShort(currentOffset);
        String thisClass = this.readClass(currentOffset + 2, charBuffer);
        String superClass = this.readClass(currentOffset + 4, charBuffer);
        String[] interfaces = new String[this.readUnsignedShort(currentOffset + 6)];
        currentOffset += 8;
        for (int i = 0; i < interfaces.length; i++) {
            interfaces[i] = this.readClass(currentOffset, charBuffer);
            currentOffset += 2;
        }
        int innerClassesOffset = 0;
        int enclosingMethodOffset = 0;
        String signature = null;
        String sourceFile = null;
        String sourceDebugExtension = null;
        int runtimeVisibleAnnotationsOffset = 0;
        int runtimeInvisibleAnnotationsOffset = 0;
        int runtimeVisibleTypeAnnotationsOffset = 0;
        int runtimeInvisibleTypeAnnotationsOffset = 0;
        int moduleOffset = 0;
        int modulePackagesOffset = 0;
        String moduleMainClass = null;
        String nestHostClass = null;
        int nestMembersOffset = 0;
        int permittedSubclassesOffset = 0;
        int recordOffset = 0;
        Attribute attributes = null;
        int currentAttributeOffset = this.getFirstAttributeOffset();
        for (int i = this.readUnsignedShort(currentAttributeOffset - 2); i > 0; i--) {
            String attributeName = this.readUTF8(currentAttributeOffset, charBuffer);
            int attributeLength = this.readInt(currentAttributeOffset + 2);
            currentAttributeOffset += 6;
            if ("SourceFile".equals(attributeName)) {
                sourceFile = this.readUTF8(currentAttributeOffset, charBuffer);
            } else if ("InnerClasses".equals(attributeName)) {
                innerClassesOffset = currentAttributeOffset;
            } else if ("EnclosingMethod".equals(attributeName)) {
                enclosingMethodOffset = currentAttributeOffset;
            } else if ("NestHost".equals(attributeName)) {
                nestHostClass = this.readClass(currentAttributeOffset, charBuffer);
            } else if ("NestMembers".equals(attributeName)) {
                nestMembersOffset = currentAttributeOffset;
            } else if ("PermittedSubclasses".equals(attributeName)) {
                permittedSubclassesOffset = currentAttributeOffset;
            } else if ("Signature".equals(attributeName)) {
                signature = this.readUTF8(currentAttributeOffset, charBuffer);
            } else if ("RuntimeVisibleAnnotations".equals(attributeName)) {
                runtimeVisibleAnnotationsOffset = currentAttributeOffset;
            } else if ("RuntimeVisibleTypeAnnotations".equals(attributeName)) {
                runtimeVisibleTypeAnnotationsOffset = currentAttributeOffset;
            } else if ("Deprecated".equals(attributeName)) {
                accessFlags |= 131072;
            } else if ("Synthetic".equals(attributeName)) {
                accessFlags |= 4096;
            } else if ("SourceDebugExtension".equals(attributeName)) {
                if (attributeLength > this.classFileBuffer.length - currentAttributeOffset) {
                    throw new IllegalArgumentException();
                }
                sourceDebugExtension = this.readUtf(currentAttributeOffset, attributeLength, new char[attributeLength]);
            } else if ("RuntimeInvisibleAnnotations".equals(attributeName)) {
                runtimeInvisibleAnnotationsOffset = currentAttributeOffset;
            } else if ("RuntimeInvisibleTypeAnnotations".equals(attributeName)) {
                runtimeInvisibleTypeAnnotationsOffset = currentAttributeOffset;
            } else if ("Record".equals(attributeName)) {
                recordOffset = currentAttributeOffset;
                accessFlags |= 65536;
            } else if ("Module".equals(attributeName)) {
                moduleOffset = currentAttributeOffset;
            } else if ("ModuleMainClass".equals(attributeName)) {
                moduleMainClass = this.readClass(currentAttributeOffset, charBuffer);
            } else if ("ModulePackages".equals(attributeName)) {
                modulePackagesOffset = currentAttributeOffset;
            } else if (!"BootstrapMethods".equals(attributeName)) {
                Attribute attribute = this.readAttribute(attributePrototypes, attributeName, currentAttributeOffset, attributeLength, charBuffer, -1, null);
                attribute.nextAttribute = attributes;
                attributes = attribute;
            }
            currentAttributeOffset += attributeLength;
        }
        classVisitor.visit(this.readInt(this.cpInfoOffsets[1] - 7), accessFlags, thisClass, signature, superClass, interfaces);
        if ((parsingOptions & 2) == 0 && (sourceFile != null || sourceDebugExtension != null)) {
            classVisitor.visitSource(sourceFile, sourceDebugExtension);
        }
        if (moduleOffset != 0) {
            this.readModuleAttributes(classVisitor, context, moduleOffset, modulePackagesOffset, moduleMainClass);
        }
        if (nestHostClass != null) {
            classVisitor.visitNestHost(nestHostClass);
        }
        if (enclosingMethodOffset != 0) {
            String className = this.readClass(enclosingMethodOffset, charBuffer);
            int methodIndex = this.readUnsignedShort(enclosingMethodOffset + 2);
            String name = methodIndex == 0 ? null : this.readUTF8(this.cpInfoOffsets[methodIndex], charBuffer);
            String type = methodIndex == 0 ? null : this.readUTF8(this.cpInfoOffsets[methodIndex] + 2, charBuffer);
            classVisitor.visitOuterClass(className, name, type);
        }
        if (runtimeVisibleAnnotationsOffset != 0) {
            int numAnnotations = this.readUnsignedShort(runtimeVisibleAnnotationsOffset);
            int currentAnnotationOffset = runtimeVisibleAnnotationsOffset + 2;
            while (numAnnotations-- > 0) {
                String annotationDescriptor = this.readUTF8(currentAnnotationOffset, charBuffer);
                currentAnnotationOffset += 2;
                currentAnnotationOffset = this.readElementValues(classVisitor.visitAnnotation(annotationDescriptor, true), currentAnnotationOffset, true, charBuffer);
            }
        }
        if (runtimeInvisibleAnnotationsOffset != 0) {
            int numAnnotations = this.readUnsignedShort(runtimeInvisibleAnnotationsOffset);
            int currentAnnotationOffset = runtimeInvisibleAnnotationsOffset + 2;
            while (numAnnotations-- > 0) {
                String annotationDescriptor = this.readUTF8(currentAnnotationOffset, charBuffer);
                currentAnnotationOffset += 2;
                currentAnnotationOffset = this.readElementValues(classVisitor.visitAnnotation(annotationDescriptor, false), currentAnnotationOffset, true, charBuffer);
            }
        }
        if (runtimeVisibleTypeAnnotationsOffset != 0) {
            int numAnnotations = this.readUnsignedShort(runtimeVisibleTypeAnnotationsOffset);
            int currentAnnotationOffset = runtimeVisibleTypeAnnotationsOffset + 2;
            while (numAnnotations-- > 0) {
                currentAnnotationOffset = this.readTypeAnnotationTarget(context, currentAnnotationOffset);
                String annotationDescriptor = this.readUTF8(currentAnnotationOffset, charBuffer);
                currentAnnotationOffset += 2;
                currentAnnotationOffset = this.readElementValues(classVisitor.visitTypeAnnotation(context.currentTypeAnnotationTarget, context.currentTypeAnnotationTargetPath, annotationDescriptor, true), currentAnnotationOffset, true, charBuffer);
            }
        }
        if (runtimeInvisibleTypeAnnotationsOffset != 0) {
            int numAnnotations = this.readUnsignedShort(runtimeInvisibleTypeAnnotationsOffset);
            int currentAnnotationOffset = runtimeInvisibleTypeAnnotationsOffset + 2;
            while (numAnnotations-- > 0) {
                currentAnnotationOffset = this.readTypeAnnotationTarget(context, currentAnnotationOffset);
                String annotationDescriptor = this.readUTF8(currentAnnotationOffset, charBuffer);
                currentAnnotationOffset += 2;
                currentAnnotationOffset = this.readElementValues(classVisitor.visitTypeAnnotation(context.currentTypeAnnotationTarget, context.currentTypeAnnotationTargetPath, annotationDescriptor, false), currentAnnotationOffset, true, charBuffer);
            }
        }
        while (attributes != null) {
            Attribute nextAttribute = attributes.nextAttribute;
            attributes.nextAttribute = null;
            classVisitor.visitAttribute(attributes);
            attributes = nextAttribute;
        }
        if (nestMembersOffset != 0) {
            int numberOfNestMembers = this.readUnsignedShort(nestMembersOffset);
            for (int currentNestMemberOffset = nestMembersOffset + 2; numberOfNestMembers-- > 0; currentNestMemberOffset += 2) {
                classVisitor.visitNestMember(this.readClass(currentNestMemberOffset, charBuffer));
            }
        }
        if (permittedSubclassesOffset != 0) {
            int numberOfPermittedSubclasses = this.readUnsignedShort(permittedSubclassesOffset);
            for (int currentPermittedSubclassesOffset = permittedSubclassesOffset + 2; numberOfPermittedSubclasses-- > 0; currentPermittedSubclassesOffset += 2) {
                classVisitor.visitPermittedSubclass(this.readClass(currentPermittedSubclassesOffset, charBuffer));
            }
        }
        if (innerClassesOffset != 0) {
            int numberOfClasses = this.readUnsignedShort(innerClassesOffset);
            for (int currentClassesOffset = innerClassesOffset + 2; numberOfClasses-- > 0; currentClassesOffset += 8) {
                classVisitor.visitInnerClass(this.readClass(currentClassesOffset, charBuffer), this.readClass(currentClassesOffset + 2, charBuffer), this.readUTF8(currentClassesOffset + 4, charBuffer), this.readUnsignedShort(currentClassesOffset + 6));
            }
        }
        if (recordOffset != 0) {
            int recordComponentsCount = this.readUnsignedShort(recordOffset);
            recordOffset += 2;
            while (recordComponentsCount-- > 0) {
                recordOffset = this.readRecordComponent(classVisitor, context, recordOffset);
            }
        }
        int fieldsCount = this.readUnsignedShort(currentOffset);
        currentOffset += 2;
        while (fieldsCount-- > 0) {
            currentOffset = this.readField(classVisitor, context, currentOffset);
        }
        int methodsCount = this.readUnsignedShort(currentOffset);
        currentOffset += 2;
        while (methodsCount-- > 0) {
            currentOffset = this.readMethod(classVisitor, context, currentOffset);
        }
        classVisitor.visitEnd();
    }

    private void readModuleAttributes(ClassVisitor classVisitor, Context context, int moduleOffset, int modulePackagesOffset, String moduleMainClass) {
        char[] buffer = context.charBuffer;
        String moduleName = this.readModule(moduleOffset, buffer);
        int moduleFlags = this.readUnsignedShort(moduleOffset + 2);
        String moduleVersion = this.readUTF8(moduleOffset + 4, buffer);
        int currentOffset = moduleOffset + 6;
        ModuleVisitor moduleVisitor = classVisitor.visitModule(moduleName, moduleFlags, moduleVersion);
        if (moduleVisitor != null) {
            if (moduleMainClass != null) {
                moduleVisitor.visitMainClass(moduleMainClass);
            }
            if (modulePackagesOffset != 0) {
                int packageCount = this.readUnsignedShort(modulePackagesOffset);
                for (int currentPackageOffset = modulePackagesOffset + 2; packageCount-- > 0; currentPackageOffset += 2) {
                    moduleVisitor.visitPackage(this.readPackage(currentPackageOffset, buffer));
                }
            }
            int requiresCount = this.readUnsignedShort(currentOffset);
            currentOffset += 2;
            while (requiresCount-- > 0) {
                String requires = this.readModule(currentOffset, buffer);
                int requiresFlags = this.readUnsignedShort(currentOffset + 2);
                String requiresVersion = this.readUTF8(currentOffset + 4, buffer);
                currentOffset += 6;
                moduleVisitor.visitRequire(requires, requiresFlags, requiresVersion);
            }
            int exportsCount = this.readUnsignedShort(currentOffset);
            currentOffset += 2;
            while (exportsCount-- > 0) {
                String exports = this.readPackage(currentOffset, buffer);
                int exportsFlags = this.readUnsignedShort(currentOffset + 2);
                int exportsToCount = this.readUnsignedShort(currentOffset + 4);
                currentOffset += 6;
                String[] exportsTo = null;
                if (exportsToCount != 0) {
                    exportsTo = new String[exportsToCount];
                    for (int i = 0; i < exportsToCount; i++) {
                        exportsTo[i] = this.readModule(currentOffset, buffer);
                        currentOffset += 2;
                    }
                }
                moduleVisitor.visitExport(exports, exportsFlags, exportsTo);
            }
            int opensCount = this.readUnsignedShort(currentOffset);
            currentOffset += 2;
            while (opensCount-- > 0) {
                String opens = this.readPackage(currentOffset, buffer);
                int opensFlags = this.readUnsignedShort(currentOffset + 2);
                int opensToCount = this.readUnsignedShort(currentOffset + 4);
                currentOffset += 6;
                String[] opensTo = null;
                if (opensToCount != 0) {
                    opensTo = new String[opensToCount];
                    for (int i = 0; i < opensToCount; i++) {
                        opensTo[i] = this.readModule(currentOffset, buffer);
                        currentOffset += 2;
                    }
                }
                moduleVisitor.visitOpen(opens, opensFlags, opensTo);
            }
            int usesCount = this.readUnsignedShort(currentOffset);
            for (currentOffset += 2; usesCount-- > 0; currentOffset += 2) {
                moduleVisitor.visitUse(this.readClass(currentOffset, buffer));
            }
            int providesCount = this.readUnsignedShort(currentOffset);
            currentOffset += 2;
            while (providesCount-- > 0) {
                String provides = this.readClass(currentOffset, buffer);
                int providesWithCount = this.readUnsignedShort(currentOffset + 2);
                currentOffset += 4;
                String[] providesWith = new String[providesWithCount];
                for (int i = 0; i < providesWithCount; i++) {
                    providesWith[i] = this.readClass(currentOffset, buffer);
                    currentOffset += 2;
                }
                moduleVisitor.visitProvide(provides, providesWith);
            }
            moduleVisitor.visitEnd();
        }
    }

    private int readRecordComponent(ClassVisitor classVisitor, Context context, int recordComponentOffset) {
        char[] charBuffer = context.charBuffer;
        String name = this.readUTF8(recordComponentOffset, charBuffer);
        String descriptor = this.readUTF8(recordComponentOffset + 2, charBuffer);
        int currentOffset = recordComponentOffset + 4;
        String signature = null;
        int runtimeVisibleAnnotationsOffset = 0;
        int runtimeInvisibleAnnotationsOffset = 0;
        int runtimeVisibleTypeAnnotationsOffset = 0;
        int runtimeInvisibleTypeAnnotationsOffset = 0;
        Attribute attributes = null;
        int attributesCount = this.readUnsignedShort(currentOffset);
        currentOffset += 2;
        while (attributesCount-- > 0) {
            String attributeName = this.readUTF8(currentOffset, charBuffer);
            int attributeLength = this.readInt(currentOffset + 2);
            currentOffset += 6;
            if ("Signature".equals(attributeName)) {
                signature = this.readUTF8(currentOffset, charBuffer);
            } else if ("RuntimeVisibleAnnotations".equals(attributeName)) {
                runtimeVisibleAnnotationsOffset = currentOffset;
            } else if ("RuntimeVisibleTypeAnnotations".equals(attributeName)) {
                runtimeVisibleTypeAnnotationsOffset = currentOffset;
            } else if ("RuntimeInvisibleAnnotations".equals(attributeName)) {
                runtimeInvisibleAnnotationsOffset = currentOffset;
            } else if ("RuntimeInvisibleTypeAnnotations".equals(attributeName)) {
                runtimeInvisibleTypeAnnotationsOffset = currentOffset;
            } else {
                Attribute attribute = this.readAttribute(context.attributePrototypes, attributeName, currentOffset, attributeLength, charBuffer, -1, null);
                attribute.nextAttribute = attributes;
                attributes = attribute;
            }
            currentOffset += attributeLength;
        }
        RecordComponentVisitor recordComponentVisitor = classVisitor.visitRecordComponent(name, descriptor, signature);
        if (recordComponentVisitor == null) {
            return currentOffset;
        } else {
            if (runtimeVisibleAnnotationsOffset != 0) {
                int numAnnotations = this.readUnsignedShort(runtimeVisibleAnnotationsOffset);
                int currentAnnotationOffset = runtimeVisibleAnnotationsOffset + 2;
                while (numAnnotations-- > 0) {
                    String annotationDescriptor = this.readUTF8(currentAnnotationOffset, charBuffer);
                    currentAnnotationOffset += 2;
                    currentAnnotationOffset = this.readElementValues(recordComponentVisitor.visitAnnotation(annotationDescriptor, true), currentAnnotationOffset, true, charBuffer);
                }
            }
            if (runtimeInvisibleAnnotationsOffset != 0) {
                int numAnnotations = this.readUnsignedShort(runtimeInvisibleAnnotationsOffset);
                int currentAnnotationOffset = runtimeInvisibleAnnotationsOffset + 2;
                while (numAnnotations-- > 0) {
                    String annotationDescriptor = this.readUTF8(currentAnnotationOffset, charBuffer);
                    currentAnnotationOffset += 2;
                    currentAnnotationOffset = this.readElementValues(recordComponentVisitor.visitAnnotation(annotationDescriptor, false), currentAnnotationOffset, true, charBuffer);
                }
            }
            if (runtimeVisibleTypeAnnotationsOffset != 0) {
                int numAnnotations = this.readUnsignedShort(runtimeVisibleTypeAnnotationsOffset);
                int currentAnnotationOffset = runtimeVisibleTypeAnnotationsOffset + 2;
                while (numAnnotations-- > 0) {
                    currentAnnotationOffset = this.readTypeAnnotationTarget(context, currentAnnotationOffset);
                    String annotationDescriptor = this.readUTF8(currentAnnotationOffset, charBuffer);
                    currentAnnotationOffset += 2;
                    currentAnnotationOffset = this.readElementValues(recordComponentVisitor.visitTypeAnnotation(context.currentTypeAnnotationTarget, context.currentTypeAnnotationTargetPath, annotationDescriptor, true), currentAnnotationOffset, true, charBuffer);
                }
            }
            if (runtimeInvisibleTypeAnnotationsOffset != 0) {
                int numAnnotations = this.readUnsignedShort(runtimeInvisibleTypeAnnotationsOffset);
                int currentAnnotationOffset = runtimeInvisibleTypeAnnotationsOffset + 2;
                while (numAnnotations-- > 0) {
                    currentAnnotationOffset = this.readTypeAnnotationTarget(context, currentAnnotationOffset);
                    String annotationDescriptor = this.readUTF8(currentAnnotationOffset, charBuffer);
                    currentAnnotationOffset += 2;
                    currentAnnotationOffset = this.readElementValues(recordComponentVisitor.visitTypeAnnotation(context.currentTypeAnnotationTarget, context.currentTypeAnnotationTargetPath, annotationDescriptor, false), currentAnnotationOffset, true, charBuffer);
                }
            }
            while (attributes != null) {
                Attribute nextAttribute = attributes.nextAttribute;
                attributes.nextAttribute = null;
                recordComponentVisitor.visitAttribute(attributes);
                attributes = nextAttribute;
            }
            recordComponentVisitor.visitEnd();
            return currentOffset;
        }
    }

    private int readField(ClassVisitor classVisitor, Context context, int fieldInfoOffset) {
        char[] charBuffer = context.charBuffer;
        int accessFlags = this.readUnsignedShort(fieldInfoOffset);
        String name = this.readUTF8(fieldInfoOffset + 2, charBuffer);
        String descriptor = this.readUTF8(fieldInfoOffset + 4, charBuffer);
        int currentOffset = fieldInfoOffset + 6;
        Object constantValue = null;
        String signature = null;
        int runtimeVisibleAnnotationsOffset = 0;
        int runtimeInvisibleAnnotationsOffset = 0;
        int runtimeVisibleTypeAnnotationsOffset = 0;
        int runtimeInvisibleTypeAnnotationsOffset = 0;
        Attribute attributes = null;
        int attributesCount = this.readUnsignedShort(currentOffset);
        currentOffset += 2;
        while (attributesCount-- > 0) {
            String attributeName = this.readUTF8(currentOffset, charBuffer);
            int attributeLength = this.readInt(currentOffset + 2);
            currentOffset += 6;
            if ("ConstantValue".equals(attributeName)) {
                int constantvalueIndex = this.readUnsignedShort(currentOffset);
                constantValue = constantvalueIndex == 0 ? null : this.readConst(constantvalueIndex, charBuffer);
            } else if ("Signature".equals(attributeName)) {
                signature = this.readUTF8(currentOffset, charBuffer);
            } else if ("Deprecated".equals(attributeName)) {
                accessFlags |= 131072;
            } else if ("Synthetic".equals(attributeName)) {
                accessFlags |= 4096;
            } else if ("RuntimeVisibleAnnotations".equals(attributeName)) {
                runtimeVisibleAnnotationsOffset = currentOffset;
            } else if ("RuntimeVisibleTypeAnnotations".equals(attributeName)) {
                runtimeVisibleTypeAnnotationsOffset = currentOffset;
            } else if ("RuntimeInvisibleAnnotations".equals(attributeName)) {
                runtimeInvisibleAnnotationsOffset = currentOffset;
            } else if ("RuntimeInvisibleTypeAnnotations".equals(attributeName)) {
                runtimeInvisibleTypeAnnotationsOffset = currentOffset;
            } else {
                Attribute attribute = this.readAttribute(context.attributePrototypes, attributeName, currentOffset, attributeLength, charBuffer, -1, null);
                attribute.nextAttribute = attributes;
                attributes = attribute;
            }
            currentOffset += attributeLength;
        }
        FieldVisitor fieldVisitor = classVisitor.visitField(accessFlags, name, descriptor, signature, constantValue);
        if (fieldVisitor == null) {
            return currentOffset;
        } else {
            if (runtimeVisibleAnnotationsOffset != 0) {
                int numAnnotations = this.readUnsignedShort(runtimeVisibleAnnotationsOffset);
                int currentAnnotationOffset = runtimeVisibleAnnotationsOffset + 2;
                while (numAnnotations-- > 0) {
                    String annotationDescriptor = this.readUTF8(currentAnnotationOffset, charBuffer);
                    currentAnnotationOffset += 2;
                    currentAnnotationOffset = this.readElementValues(fieldVisitor.visitAnnotation(annotationDescriptor, true), currentAnnotationOffset, true, charBuffer);
                }
            }
            if (runtimeInvisibleAnnotationsOffset != 0) {
                int numAnnotations = this.readUnsignedShort(runtimeInvisibleAnnotationsOffset);
                int currentAnnotationOffset = runtimeInvisibleAnnotationsOffset + 2;
                while (numAnnotations-- > 0) {
                    String annotationDescriptor = this.readUTF8(currentAnnotationOffset, charBuffer);
                    currentAnnotationOffset += 2;
                    currentAnnotationOffset = this.readElementValues(fieldVisitor.visitAnnotation(annotationDescriptor, false), currentAnnotationOffset, true, charBuffer);
                }
            }
            if (runtimeVisibleTypeAnnotationsOffset != 0) {
                int numAnnotations = this.readUnsignedShort(runtimeVisibleTypeAnnotationsOffset);
                int currentAnnotationOffset = runtimeVisibleTypeAnnotationsOffset + 2;
                while (numAnnotations-- > 0) {
                    currentAnnotationOffset = this.readTypeAnnotationTarget(context, currentAnnotationOffset);
                    String annotationDescriptor = this.readUTF8(currentAnnotationOffset, charBuffer);
                    currentAnnotationOffset += 2;
                    currentAnnotationOffset = this.readElementValues(fieldVisitor.visitTypeAnnotation(context.currentTypeAnnotationTarget, context.currentTypeAnnotationTargetPath, annotationDescriptor, true), currentAnnotationOffset, true, charBuffer);
                }
            }
            if (runtimeInvisibleTypeAnnotationsOffset != 0) {
                int numAnnotations = this.readUnsignedShort(runtimeInvisibleTypeAnnotationsOffset);
                int currentAnnotationOffset = runtimeInvisibleTypeAnnotationsOffset + 2;
                while (numAnnotations-- > 0) {
                    currentAnnotationOffset = this.readTypeAnnotationTarget(context, currentAnnotationOffset);
                    String annotationDescriptor = this.readUTF8(currentAnnotationOffset, charBuffer);
                    currentAnnotationOffset += 2;
                    currentAnnotationOffset = this.readElementValues(fieldVisitor.visitTypeAnnotation(context.currentTypeAnnotationTarget, context.currentTypeAnnotationTargetPath, annotationDescriptor, false), currentAnnotationOffset, true, charBuffer);
                }
            }
            while (attributes != null) {
                Attribute nextAttribute = attributes.nextAttribute;
                attributes.nextAttribute = null;
                fieldVisitor.visitAttribute(attributes);
                attributes = nextAttribute;
            }
            fieldVisitor.visitEnd();
            return currentOffset;
        }
    }

    private int readMethod(ClassVisitor classVisitor, Context context, int methodInfoOffset) {
        char[] charBuffer = context.charBuffer;
        context.currentMethodAccessFlags = this.readUnsignedShort(methodInfoOffset);
        context.currentMethodName = this.readUTF8(methodInfoOffset + 2, charBuffer);
        context.currentMethodDescriptor = this.readUTF8(methodInfoOffset + 4, charBuffer);
        int currentOffset = methodInfoOffset + 6;
        int codeOffset = 0;
        int exceptionsOffset = 0;
        String[] exceptions = null;
        boolean synthetic = false;
        int signatureIndex = 0;
        int runtimeVisibleAnnotationsOffset = 0;
        int runtimeInvisibleAnnotationsOffset = 0;
        int runtimeVisibleParameterAnnotationsOffset = 0;
        int runtimeInvisibleParameterAnnotationsOffset = 0;
        int runtimeVisibleTypeAnnotationsOffset = 0;
        int runtimeInvisibleTypeAnnotationsOffset = 0;
        int annotationDefaultOffset = 0;
        int methodParametersOffset = 0;
        Attribute attributes = null;
        int attributesCount = this.readUnsignedShort(currentOffset);
        currentOffset += 2;
        while (attributesCount-- > 0) {
            String attributeName = this.readUTF8(currentOffset, charBuffer);
            int attributeLength = this.readInt(currentOffset + 2);
            currentOffset += 6;
            if ("Code".equals(attributeName)) {
                if ((context.parsingOptions & 1) == 0) {
                    codeOffset = currentOffset;
                }
            } else if ("Exceptions".equals(attributeName)) {
                exceptionsOffset = currentOffset;
                exceptions = new String[this.readUnsignedShort(currentOffset)];
                int currentExceptionOffset = currentOffset + 2;
                for (int i = 0; i < exceptions.length; i++) {
                    exceptions[i] = this.readClass(currentExceptionOffset, charBuffer);
                    currentExceptionOffset += 2;
                }
            } else if ("Signature".equals(attributeName)) {
                signatureIndex = this.readUnsignedShort(currentOffset);
            } else if ("Deprecated".equals(attributeName)) {
                context.currentMethodAccessFlags |= 131072;
            } else if ("RuntimeVisibleAnnotations".equals(attributeName)) {
                runtimeVisibleAnnotationsOffset = currentOffset;
            } else if ("RuntimeVisibleTypeAnnotations".equals(attributeName)) {
                runtimeVisibleTypeAnnotationsOffset = currentOffset;
            } else if ("AnnotationDefault".equals(attributeName)) {
                annotationDefaultOffset = currentOffset;
            } else if ("Synthetic".equals(attributeName)) {
                synthetic = true;
                context.currentMethodAccessFlags |= 4096;
            } else if ("RuntimeInvisibleAnnotations".equals(attributeName)) {
                runtimeInvisibleAnnotationsOffset = currentOffset;
            } else if ("RuntimeInvisibleTypeAnnotations".equals(attributeName)) {
                runtimeInvisibleTypeAnnotationsOffset = currentOffset;
            } else if ("RuntimeVisibleParameterAnnotations".equals(attributeName)) {
                runtimeVisibleParameterAnnotationsOffset = currentOffset;
            } else if ("RuntimeInvisibleParameterAnnotations".equals(attributeName)) {
                runtimeInvisibleParameterAnnotationsOffset = currentOffset;
            } else if ("MethodParameters".equals(attributeName)) {
                methodParametersOffset = currentOffset;
            } else {
                Attribute attribute = this.readAttribute(context.attributePrototypes, attributeName, currentOffset, attributeLength, charBuffer, -1, null);
                attribute.nextAttribute = attributes;
                attributes = attribute;
            }
            currentOffset += attributeLength;
        }
        MethodVisitor methodVisitor = classVisitor.visitMethod(context.currentMethodAccessFlags, context.currentMethodName, context.currentMethodDescriptor, signatureIndex == 0 ? null : this.readUtf(signatureIndex, charBuffer), exceptions);
        if (methodVisitor == null) {
            return currentOffset;
        } else {
            if (methodVisitor instanceof MethodWriter) {
                MethodWriter methodWriter = (MethodWriter) methodVisitor;
                if (methodWriter.canCopyMethodAttributes(this, synthetic, (context.currentMethodAccessFlags & 131072) != 0, this.readUnsignedShort(methodInfoOffset + 4), signatureIndex, exceptionsOffset)) {
                    methodWriter.setMethodAttributesSource(methodInfoOffset, currentOffset - methodInfoOffset);
                    return currentOffset;
                }
            }
            if (methodParametersOffset != 0 && (context.parsingOptions & 2) == 0) {
                int parametersCount = this.readByte(methodParametersOffset);
                for (int currentParameterOffset = methodParametersOffset + 1; parametersCount-- > 0; currentParameterOffset += 4) {
                    methodVisitor.visitParameter(this.readUTF8(currentParameterOffset, charBuffer), this.readUnsignedShort(currentParameterOffset + 2));
                }
            }
            if (annotationDefaultOffset != 0) {
                AnnotationVisitor annotationVisitor = methodVisitor.visitAnnotationDefault();
                this.readElementValue(annotationVisitor, annotationDefaultOffset, null, charBuffer);
                if (annotationVisitor != null) {
                    annotationVisitor.visitEnd();
                }
            }
            if (runtimeVisibleAnnotationsOffset != 0) {
                int numAnnotations = this.readUnsignedShort(runtimeVisibleAnnotationsOffset);
                int currentAnnotationOffset = runtimeVisibleAnnotationsOffset + 2;
                while (numAnnotations-- > 0) {
                    String annotationDescriptor = this.readUTF8(currentAnnotationOffset, charBuffer);
                    currentAnnotationOffset += 2;
                    currentAnnotationOffset = this.readElementValues(methodVisitor.visitAnnotation(annotationDescriptor, true), currentAnnotationOffset, true, charBuffer);
                }
            }
            if (runtimeInvisibleAnnotationsOffset != 0) {
                int numAnnotations = this.readUnsignedShort(runtimeInvisibleAnnotationsOffset);
                int currentAnnotationOffset = runtimeInvisibleAnnotationsOffset + 2;
                while (numAnnotations-- > 0) {
                    String annotationDescriptor = this.readUTF8(currentAnnotationOffset, charBuffer);
                    currentAnnotationOffset += 2;
                    currentAnnotationOffset = this.readElementValues(methodVisitor.visitAnnotation(annotationDescriptor, false), currentAnnotationOffset, true, charBuffer);
                }
            }
            if (runtimeVisibleTypeAnnotationsOffset != 0) {
                int numAnnotations = this.readUnsignedShort(runtimeVisibleTypeAnnotationsOffset);
                int currentAnnotationOffset = runtimeVisibleTypeAnnotationsOffset + 2;
                while (numAnnotations-- > 0) {
                    currentAnnotationOffset = this.readTypeAnnotationTarget(context, currentAnnotationOffset);
                    String annotationDescriptor = this.readUTF8(currentAnnotationOffset, charBuffer);
                    currentAnnotationOffset += 2;
                    currentAnnotationOffset = this.readElementValues(methodVisitor.visitTypeAnnotation(context.currentTypeAnnotationTarget, context.currentTypeAnnotationTargetPath, annotationDescriptor, true), currentAnnotationOffset, true, charBuffer);
                }
            }
            if (runtimeInvisibleTypeAnnotationsOffset != 0) {
                int numAnnotations = this.readUnsignedShort(runtimeInvisibleTypeAnnotationsOffset);
                int currentAnnotationOffset = runtimeInvisibleTypeAnnotationsOffset + 2;
                while (numAnnotations-- > 0) {
                    currentAnnotationOffset = this.readTypeAnnotationTarget(context, currentAnnotationOffset);
                    String annotationDescriptor = this.readUTF8(currentAnnotationOffset, charBuffer);
                    currentAnnotationOffset += 2;
                    currentAnnotationOffset = this.readElementValues(methodVisitor.visitTypeAnnotation(context.currentTypeAnnotationTarget, context.currentTypeAnnotationTargetPath, annotationDescriptor, false), currentAnnotationOffset, true, charBuffer);
                }
            }
            if (runtimeVisibleParameterAnnotationsOffset != 0) {
                this.readParameterAnnotations(methodVisitor, context, runtimeVisibleParameterAnnotationsOffset, true);
            }
            if (runtimeInvisibleParameterAnnotationsOffset != 0) {
                this.readParameterAnnotations(methodVisitor, context, runtimeInvisibleParameterAnnotationsOffset, false);
            }
            while (attributes != null) {
                Attribute nextAttribute = attributes.nextAttribute;
                attributes.nextAttribute = null;
                methodVisitor.visitAttribute(attributes);
                attributes = nextAttribute;
            }
            if (codeOffset != 0) {
                methodVisitor.visitCode();
                this.readCode(methodVisitor, context, codeOffset);
            }
            methodVisitor.visitEnd();
            return currentOffset;
        }
    }

    private void readCode(MethodVisitor methodVisitor, Context context, int codeOffset) {
        byte[] classBuffer = this.classFileBuffer;
        char[] charBuffer = context.charBuffer;
        int maxStack = this.readUnsignedShort(codeOffset);
        int maxLocals = this.readUnsignedShort(codeOffset + 2);
        int codeLength = this.readInt(codeOffset + 4);
        int currentOffset = codeOffset + 8;
        if (codeLength > this.classFileBuffer.length - currentOffset) {
            throw new IllegalArgumentException();
        } else {
            int bytecodeStartOffset = currentOffset;
            int bytecodeEndOffset = currentOffset + codeLength;
            Label[] labels = context.currentMethodLabels = new Label[codeLength + 1];
            while (currentOffset < bytecodeEndOffset) {
                int bytecodeOffset = currentOffset - bytecodeStartOffset;
                int opcode = classBuffer[currentOffset] & 255;
                switch(opcode) {
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                    case 7:
                    case 8:
                    case 9:
                    case 10:
                    case 11:
                    case 12:
                    case 13:
                    case 14:
                    case 15:
                    case 26:
                    case 27:
                    case 28:
                    case 29:
                    case 30:
                    case 31:
                    case 32:
                    case 33:
                    case 34:
                    case 35:
                    case 36:
                    case 37:
                    case 38:
                    case 39:
                    case 40:
                    case 41:
                    case 42:
                    case 43:
                    case 44:
                    case 45:
                    case 46:
                    case 47:
                    case 48:
                    case 49:
                    case 50:
                    case 51:
                    case 52:
                    case 53:
                    case 59:
                    case 60:
                    case 61:
                    case 62:
                    case 63:
                    case 64:
                    case 65:
                    case 66:
                    case 67:
                    case 68:
                    case 69:
                    case 70:
                    case 71:
                    case 72:
                    case 73:
                    case 74:
                    case 75:
                    case 76:
                    case 77:
                    case 78:
                    case 79:
                    case 80:
                    case 81:
                    case 82:
                    case 83:
                    case 84:
                    case 85:
                    case 86:
                    case 87:
                    case 88:
                    case 89:
                    case 90:
                    case 91:
                    case 92:
                    case 93:
                    case 94:
                    case 95:
                    case 96:
                    case 97:
                    case 98:
                    case 99:
                    case 100:
                    case 101:
                    case 102:
                    case 103:
                    case 104:
                    case 105:
                    case 106:
                    case 107:
                    case 108:
                    case 109:
                    case 110:
                    case 111:
                    case 112:
                    case 113:
                    case 114:
                    case 115:
                    case 116:
                    case 117:
                    case 118:
                    case 119:
                    case 120:
                    case 121:
                    case 122:
                    case 123:
                    case 124:
                    case 125:
                    case 126:
                    case 127:
                    case 128:
                    case 129:
                    case 130:
                    case 131:
                    case 133:
                    case 134:
                    case 135:
                    case 136:
                    case 137:
                    case 138:
                    case 139:
                    case 140:
                    case 141:
                    case 142:
                    case 143:
                    case 144:
                    case 145:
                    case 146:
                    case 147:
                    case 148:
                    case 149:
                    case 150:
                    case 151:
                    case 152:
                    case 172:
                    case 173:
                    case 174:
                    case 175:
                    case 176:
                    case 177:
                    case 190:
                    case 191:
                    case 194:
                    case 195:
                        currentOffset++;
                        break;
                    case 16:
                    case 18:
                    case 21:
                    case 22:
                    case 23:
                    case 24:
                    case 25:
                    case 54:
                    case 55:
                    case 56:
                    case 57:
                    case 58:
                    case 169:
                    case 188:
                        currentOffset += 2;
                        break;
                    case 17:
                    case 19:
                    case 20:
                    case 132:
                    case 178:
                    case 179:
                    case 180:
                    case 181:
                    case 182:
                    case 183:
                    case 184:
                    case 187:
                    case 189:
                    case 192:
                    case 193:
                        currentOffset += 3;
                        break;
                    case 153:
                    case 154:
                    case 155:
                    case 156:
                    case 157:
                    case 158:
                    case 159:
                    case 160:
                    case 161:
                    case 162:
                    case 163:
                    case 164:
                    case 165:
                    case 166:
                    case 167:
                    case 168:
                    case 198:
                    case 199:
                        this.createLabel(bytecodeOffset + this.readShort(currentOffset + 1), labels);
                        currentOffset += 3;
                        break;
                    case 170:
                        currentOffset += 4 - (bytecodeOffset & 3);
                        this.createLabel(bytecodeOffset + this.readInt(currentOffset), labels);
                        int numTableEntries = this.readInt(currentOffset + 8) - this.readInt(currentOffset + 4) + 1;
                        for (currentOffset += 12; numTableEntries-- > 0; currentOffset += 4) {
                            this.createLabel(bytecodeOffset + this.readInt(currentOffset), labels);
                        }
                        break;
                    case 171:
                        currentOffset += 4 - (bytecodeOffset & 3);
                        this.createLabel(bytecodeOffset + this.readInt(currentOffset), labels);
                        int numSwitchCases = this.readInt(currentOffset + 4);
                        for (currentOffset += 8; numSwitchCases-- > 0; currentOffset += 8) {
                            this.createLabel(bytecodeOffset + this.readInt(currentOffset + 4), labels);
                        }
                        break;
                    case 185:
                    case 186:
                        currentOffset += 5;
                        break;
                    case 196:
                        switch(classBuffer[currentOffset + 1] & 0xFF) {
                            case 21:
                            case 22:
                            case 23:
                            case 24:
                            case 25:
                            case 54:
                            case 55:
                            case 56:
                            case 57:
                            case 58:
                            case 169:
                                currentOffset += 4;
                                continue;
                            case 132:
                                currentOffset += 6;
                                continue;
                            default:
                                throw new IllegalArgumentException();
                        }
                    case 197:
                        currentOffset += 4;
                        break;
                    case 200:
                    case 201:
                    case 220:
                        this.createLabel(bytecodeOffset + this.readInt(currentOffset + 1), labels);
                        currentOffset += 5;
                        break;
                    case 202:
                    case 203:
                    case 204:
                    case 205:
                    case 206:
                    case 207:
                    case 208:
                    case 209:
                    case 210:
                    case 211:
                    case 212:
                    case 213:
                    case 214:
                    case 215:
                    case 216:
                    case 217:
                    case 218:
                    case 219:
                        this.createLabel(bytecodeOffset + this.readUnsignedShort(currentOffset + 1), labels);
                        currentOffset += 3;
                        break;
                    default:
                        throw new IllegalArgumentException();
                }
            }
            int exceptionTableLength = this.readUnsignedShort(currentOffset);
            currentOffset += 2;
            while (exceptionTableLength-- > 0) {
                Label start = this.createLabel(this.readUnsignedShort(currentOffset), labels);
                Label end = this.createLabel(this.readUnsignedShort(currentOffset + 2), labels);
                Label handler = this.createLabel(this.readUnsignedShort(currentOffset + 4), labels);
                String catchType = this.readUTF8(this.cpInfoOffsets[this.readUnsignedShort(currentOffset + 6)], charBuffer);
                currentOffset += 8;
                methodVisitor.visitTryCatchBlock(start, end, handler, catchType);
            }
            int stackMapFrameOffset = 0;
            int stackMapTableEndOffset = 0;
            boolean compressedFrames = true;
            int localVariableTableOffset = 0;
            int localVariableTypeTableOffset = 0;
            int[] visibleTypeAnnotationOffsets = null;
            int[] invisibleTypeAnnotationOffsets = null;
            Attribute attributes = null;
            int attributesCount = this.readUnsignedShort(currentOffset);
            currentOffset += 2;
            while (attributesCount-- > 0) {
                String attributeName = this.readUTF8(currentOffset, charBuffer);
                int attributeLength = this.readInt(currentOffset + 2);
                currentOffset += 6;
                if ("LocalVariableTable".equals(attributeName)) {
                    if ((context.parsingOptions & 2) == 0) {
                        localVariableTableOffset = currentOffset;
                        int localVariableTableLength = this.readUnsignedShort(currentOffset);
                        for (int var67 = currentOffset + 2; localVariableTableLength-- > 0; var67 += 10) {
                            int startPc = this.readUnsignedShort(var67);
                            this.createDebugLabel(startPc, labels);
                            int length = this.readUnsignedShort(var67 + 2);
                            this.createDebugLabel(startPc + length, labels);
                        }
                    }
                } else if ("LocalVariableTypeTable".equals(attributeName)) {
                    localVariableTypeTableOffset = currentOffset;
                } else if ("LineNumberTable".equals(attributeName)) {
                    if ((context.parsingOptions & 2) == 0) {
                        int lineNumberTableLength = this.readUnsignedShort(currentOffset);
                        int var66 = currentOffset + 2;
                        while (lineNumberTableLength-- > 0) {
                            int startPc = this.readUnsignedShort(var66);
                            int lineNumber = this.readUnsignedShort(var66 + 2);
                            var66 += 4;
                            this.createDebugLabel(startPc, labels);
                            labels[startPc].addLineNumber(lineNumber);
                        }
                    }
                } else if ("RuntimeVisibleTypeAnnotations".equals(attributeName)) {
                    visibleTypeAnnotationOffsets = this.readTypeAnnotations(methodVisitor, context, currentOffset, true);
                } else if ("RuntimeInvisibleTypeAnnotations".equals(attributeName)) {
                    invisibleTypeAnnotationOffsets = this.readTypeAnnotations(methodVisitor, context, currentOffset, false);
                } else if ("StackMapTable".equals(attributeName)) {
                    if ((context.parsingOptions & 4) == 0) {
                        stackMapFrameOffset = currentOffset + 2;
                        stackMapTableEndOffset = currentOffset + attributeLength;
                    }
                } else if ("StackMap".equals(attributeName)) {
                    if ((context.parsingOptions & 4) == 0) {
                        stackMapFrameOffset = currentOffset + 2;
                        stackMapTableEndOffset = currentOffset + attributeLength;
                        compressedFrames = false;
                    }
                } else {
                    Attribute attribute = this.readAttribute(context.attributePrototypes, attributeName, currentOffset, attributeLength, charBuffer, codeOffset, labels);
                    attribute.nextAttribute = attributes;
                    attributes = attribute;
                }
                currentOffset += attributeLength;
            }
            boolean expandFrames = (context.parsingOptions & 8) != 0;
            if (stackMapFrameOffset != 0) {
                context.currentFrameOffset = -1;
                context.currentFrameType = 0;
                context.currentFrameLocalCount = 0;
                context.currentFrameLocalCountDelta = 0;
                context.currentFrameLocalTypes = new Object[maxLocals];
                context.currentFrameStackCount = 0;
                context.currentFrameStackTypes = new Object[maxStack];
                if (expandFrames) {
                    this.computeImplicitFrame(context);
                }
                for (int offset = stackMapFrameOffset; offset < stackMapTableEndOffset - 2; offset++) {
                    if (classBuffer[offset] == 8) {
                        int potentialBytecodeOffset = this.readUnsignedShort(offset + 1);
                        if (potentialBytecodeOffset >= 0 && potentialBytecodeOffset < codeLength && (classBuffer[bytecodeStartOffset + potentialBytecodeOffset] & 255) == 187) {
                            this.createLabel(potentialBytecodeOffset, labels);
                        }
                    }
                }
            }
            if (expandFrames && (context.parsingOptions & 256) != 0) {
                methodVisitor.visitFrame(-1, maxLocals, null, 0, null);
            }
            int currentVisibleTypeAnnotationIndex = 0;
            int currentVisibleTypeAnnotationBytecodeOffset = this.getTypeAnnotationBytecodeOffset(visibleTypeAnnotationOffsets, 0);
            int currentInvisibleTypeAnnotationIndex = 0;
            int currentInvisibleTypeAnnotationBytecodeOffset = this.getTypeAnnotationBytecodeOffset(invisibleTypeAnnotationOffsets, 0);
            boolean insertFrame = false;
            int wideJumpOpcodeDelta = (context.parsingOptions & 256) == 0 ? 33 : 0;
            currentOffset = bytecodeStartOffset;
            while (currentOffset < bytecodeEndOffset) {
                int currentBytecodeOffset = currentOffset - bytecodeStartOffset;
                Label currentLabel = labels[currentBytecodeOffset];
                if (currentLabel != null) {
                    currentLabel.accept(methodVisitor, (context.parsingOptions & 2) == 0);
                }
                while (stackMapFrameOffset != 0 && (context.currentFrameOffset == currentBytecodeOffset || context.currentFrameOffset == -1)) {
                    if (context.currentFrameOffset != -1) {
                        if (compressedFrames && !expandFrames) {
                            methodVisitor.visitFrame(context.currentFrameType, context.currentFrameLocalCountDelta, context.currentFrameLocalTypes, context.currentFrameStackCount, context.currentFrameStackTypes);
                        } else {
                            methodVisitor.visitFrame(-1, context.currentFrameLocalCount, context.currentFrameLocalTypes, context.currentFrameStackCount, context.currentFrameStackTypes);
                        }
                        insertFrame = false;
                    }
                    if (stackMapFrameOffset < stackMapTableEndOffset) {
                        stackMapFrameOffset = this.readStackMapFrame(stackMapFrameOffset, compressedFrames, expandFrames, context);
                    } else {
                        stackMapFrameOffset = 0;
                    }
                }
                if (insertFrame) {
                    if ((context.parsingOptions & 8) != 0) {
                        methodVisitor.visitFrame(256, 0, null, 0, null);
                    }
                    insertFrame = false;
                }
                int opcode = classBuffer[currentOffset] & 255;
                switch(opcode) {
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                    case 7:
                    case 8:
                    case 9:
                    case 10:
                    case 11:
                    case 12:
                    case 13:
                    case 14:
                    case 15:
                    case 46:
                    case 47:
                    case 48:
                    case 49:
                    case 50:
                    case 51:
                    case 52:
                    case 53:
                    case 79:
                    case 80:
                    case 81:
                    case 82:
                    case 83:
                    case 84:
                    case 85:
                    case 86:
                    case 87:
                    case 88:
                    case 89:
                    case 90:
                    case 91:
                    case 92:
                    case 93:
                    case 94:
                    case 95:
                    case 96:
                    case 97:
                    case 98:
                    case 99:
                    case 100:
                    case 101:
                    case 102:
                    case 103:
                    case 104:
                    case 105:
                    case 106:
                    case 107:
                    case 108:
                    case 109:
                    case 110:
                    case 111:
                    case 112:
                    case 113:
                    case 114:
                    case 115:
                    case 116:
                    case 117:
                    case 118:
                    case 119:
                    case 120:
                    case 121:
                    case 122:
                    case 123:
                    case 124:
                    case 125:
                    case 126:
                    case 127:
                    case 128:
                    case 129:
                    case 130:
                    case 131:
                    case 133:
                    case 134:
                    case 135:
                    case 136:
                    case 137:
                    case 138:
                    case 139:
                    case 140:
                    case 141:
                    case 142:
                    case 143:
                    case 144:
                    case 145:
                    case 146:
                    case 147:
                    case 148:
                    case 149:
                    case 150:
                    case 151:
                    case 152:
                    case 172:
                    case 173:
                    case 174:
                    case 175:
                    case 176:
                    case 177:
                    case 190:
                    case 191:
                    case 194:
                    case 195:
                        methodVisitor.visitInsn(opcode);
                        currentOffset++;
                        break;
                    case 16:
                    case 188:
                        methodVisitor.visitIntInsn(opcode, classBuffer[currentOffset + 1]);
                        currentOffset += 2;
                        break;
                    case 17:
                        methodVisitor.visitIntInsn(opcode, this.readShort(currentOffset + 1));
                        currentOffset += 3;
                        break;
                    case 18:
                        methodVisitor.visitLdcInsn(this.readConst(classBuffer[currentOffset + 1] & 255, charBuffer));
                        currentOffset += 2;
                        break;
                    case 19:
                    case 20:
                        methodVisitor.visitLdcInsn(this.readConst(this.readUnsignedShort(currentOffset + 1), charBuffer));
                        currentOffset += 3;
                        break;
                    case 21:
                    case 22:
                    case 23:
                    case 24:
                    case 25:
                    case 54:
                    case 55:
                    case 56:
                    case 57:
                    case 58:
                    case 169:
                        methodVisitor.visitVarInsn(opcode, classBuffer[currentOffset + 1] & 255);
                        currentOffset += 2;
                        break;
                    case 26:
                    case 27:
                    case 28:
                    case 29:
                    case 30:
                    case 31:
                    case 32:
                    case 33:
                    case 34:
                    case 35:
                    case 36:
                    case 37:
                    case 38:
                    case 39:
                    case 40:
                    case 41:
                    case 42:
                    case 43:
                    case 44:
                    case 45:
                        opcode -= 26;
                        methodVisitor.visitVarInsn(21 + (opcode >> 2), opcode & 3);
                        currentOffset++;
                        break;
                    case 59:
                    case 60:
                    case 61:
                    case 62:
                    case 63:
                    case 64:
                    case 65:
                    case 66:
                    case 67:
                    case 68:
                    case 69:
                    case 70:
                    case 71:
                    case 72:
                    case 73:
                    case 74:
                    case 75:
                    case 76:
                    case 77:
                    case 78:
                        opcode -= 59;
                        methodVisitor.visitVarInsn(54 + (opcode >> 2), opcode & 3);
                        currentOffset++;
                        break;
                    case 132:
                        methodVisitor.visitIincInsn(classBuffer[currentOffset + 1] & 255, classBuffer[currentOffset + 2]);
                        currentOffset += 3;
                        break;
                    case 153:
                    case 154:
                    case 155:
                    case 156:
                    case 157:
                    case 158:
                    case 159:
                    case 160:
                    case 161:
                    case 162:
                    case 163:
                    case 164:
                    case 165:
                    case 166:
                    case 167:
                    case 168:
                    case 198:
                    case 199:
                        methodVisitor.visitJumpInsn(opcode, labels[currentBytecodeOffset + this.readShort(currentOffset + 1)]);
                        currentOffset += 3;
                        break;
                    case 170:
                        currentOffset += 4 - (currentBytecodeOffset & 3);
                        Label defaultLabel = labels[currentBytecodeOffset + this.readInt(currentOffset)];
                        int low = this.readInt(currentOffset + 4);
                        int high = this.readInt(currentOffset + 8);
                        currentOffset += 12;
                        Label[] table = new Label[high - low + 1];
                        for (int i = 0; i < table.length; i++) {
                            table[i] = labels[currentBytecodeOffset + this.readInt(currentOffset)];
                            currentOffset += 4;
                        }
                        methodVisitor.visitTableSwitchInsn(low, high, defaultLabel, table);
                        break;
                    case 171:
                        currentOffset += 4 - (currentBytecodeOffset & 3);
                        Label defaultLabel = labels[currentBytecodeOffset + this.readInt(currentOffset)];
                        int numPairs = this.readInt(currentOffset + 4);
                        currentOffset += 8;
                        int[] keys = new int[numPairs];
                        Label[] values = new Label[numPairs];
                        for (int i = 0; i < numPairs; i++) {
                            keys[i] = this.readInt(currentOffset);
                            values[i] = labels[currentBytecodeOffset + this.readInt(currentOffset + 4)];
                            currentOffset += 8;
                        }
                        methodVisitor.visitLookupSwitchInsn(defaultLabel, keys, values);
                        break;
                    case 178:
                    case 179:
                    case 180:
                    case 181:
                    case 182:
                    case 183:
                    case 184:
                    case 185:
                        int cpInfoOffset = this.cpInfoOffsets[this.readUnsignedShort(currentOffset + 1)];
                        int nameAndTypeCpInfoOffset = this.cpInfoOffsets[this.readUnsignedShort(cpInfoOffset + 2)];
                        String owner = this.readClass(cpInfoOffset, charBuffer);
                        String name = this.readUTF8(nameAndTypeCpInfoOffset, charBuffer);
                        String descriptor = this.readUTF8(nameAndTypeCpInfoOffset + 2, charBuffer);
                        if (opcode < 182) {
                            methodVisitor.visitFieldInsn(opcode, owner, name, descriptor);
                        } else {
                            boolean isInterface = classBuffer[cpInfoOffset - 1] == 11;
                            methodVisitor.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
                        }
                        if (opcode == 185) {
                            currentOffset += 5;
                        } else {
                            currentOffset += 3;
                        }
                        break;
                    case 186:
                        int cpInfoOffsetx = this.cpInfoOffsets[this.readUnsignedShort(currentOffset + 1)];
                        int nameAndTypeCpInfoOffsetx = this.cpInfoOffsets[this.readUnsignedShort(cpInfoOffsetx + 2)];
                        String namex = this.readUTF8(nameAndTypeCpInfoOffsetx, charBuffer);
                        String descriptorx = this.readUTF8(nameAndTypeCpInfoOffsetx + 2, charBuffer);
                        int bootstrapMethodOffset = this.bootstrapMethodOffsets[this.readUnsignedShort(cpInfoOffsetx)];
                        Handle handle = (Handle) this.readConst(this.readUnsignedShort(bootstrapMethodOffset), charBuffer);
                        Object[] bootstrapMethodArguments = new Object[this.readUnsignedShort(bootstrapMethodOffset + 2)];
                        bootstrapMethodOffset += 4;
                        for (int i = 0; i < bootstrapMethodArguments.length; i++) {
                            bootstrapMethodArguments[i] = this.readConst(this.readUnsignedShort(bootstrapMethodOffset), charBuffer);
                            bootstrapMethodOffset += 2;
                        }
                        methodVisitor.visitInvokeDynamicInsn(namex, descriptorx, handle, bootstrapMethodArguments);
                        currentOffset += 5;
                        break;
                    case 187:
                    case 189:
                    case 192:
                    case 193:
                        methodVisitor.visitTypeInsn(opcode, this.readClass(currentOffset + 1, charBuffer));
                        currentOffset += 3;
                        break;
                    case 196:
                        opcode = classBuffer[currentOffset + 1] & 255;
                        if (opcode == 132) {
                            methodVisitor.visitIincInsn(this.readUnsignedShort(currentOffset + 2), this.readShort(currentOffset + 4));
                            currentOffset += 6;
                        } else {
                            methodVisitor.visitVarInsn(opcode, this.readUnsignedShort(currentOffset + 2));
                            currentOffset += 4;
                        }
                        break;
                    case 197:
                        methodVisitor.visitMultiANewArrayInsn(this.readClass(currentOffset + 1, charBuffer), classBuffer[currentOffset + 3] & 255);
                        currentOffset += 4;
                        break;
                    case 200:
                    case 201:
                        methodVisitor.visitJumpInsn(opcode - wideJumpOpcodeDelta, labels[currentBytecodeOffset + this.readInt(currentOffset + 1)]);
                        currentOffset += 5;
                        break;
                    case 202:
                    case 203:
                    case 204:
                    case 205:
                    case 206:
                    case 207:
                    case 208:
                    case 209:
                    case 210:
                    case 211:
                    case 212:
                    case 213:
                    case 214:
                    case 215:
                    case 216:
                    case 217:
                    case 218:
                    case 219:
                        opcode = opcode < 218 ? opcode - 49 : opcode - 20;
                        Label target = labels[currentBytecodeOffset + this.readUnsignedShort(currentOffset + 1)];
                        if (opcode != 167 && opcode != 168) {
                            opcode = opcode < 167 ? (opcode + 1 ^ 1) - 1 : opcode ^ 1;
                            Label endif = this.createLabel(currentBytecodeOffset + 3, labels);
                            methodVisitor.visitJumpInsn(opcode, endif);
                            methodVisitor.visitJumpInsn(200, target);
                            insertFrame = true;
                        } else {
                            methodVisitor.visitJumpInsn(opcode + 33, target);
                        }
                        currentOffset += 3;
                        break;
                    case 220:
                        methodVisitor.visitJumpInsn(200, labels[currentBytecodeOffset + this.readInt(currentOffset + 1)]);
                        insertFrame = true;
                        currentOffset += 5;
                        break;
                    default:
                        throw new AssertionError();
                }
                for (; visibleTypeAnnotationOffsets != null && currentVisibleTypeAnnotationIndex < visibleTypeAnnotationOffsets.length && currentVisibleTypeAnnotationBytecodeOffset <= currentBytecodeOffset; currentVisibleTypeAnnotationBytecodeOffset = this.getTypeAnnotationBytecodeOffset(visibleTypeAnnotationOffsets, ++currentVisibleTypeAnnotationIndex)) {
                    if (currentVisibleTypeAnnotationBytecodeOffset == currentBytecodeOffset) {
                        int currentAnnotationOffset = this.readTypeAnnotationTarget(context, visibleTypeAnnotationOffsets[currentVisibleTypeAnnotationIndex]);
                        String annotationDescriptor = this.readUTF8(currentAnnotationOffset, charBuffer);
                        currentAnnotationOffset += 2;
                        this.readElementValues(methodVisitor.visitInsnAnnotation(context.currentTypeAnnotationTarget, context.currentTypeAnnotationTargetPath, annotationDescriptor, true), currentAnnotationOffset, true, charBuffer);
                    }
                }
                for (; invisibleTypeAnnotationOffsets != null && currentInvisibleTypeAnnotationIndex < invisibleTypeAnnotationOffsets.length && currentInvisibleTypeAnnotationBytecodeOffset <= currentBytecodeOffset; currentInvisibleTypeAnnotationBytecodeOffset = this.getTypeAnnotationBytecodeOffset(invisibleTypeAnnotationOffsets, ++currentInvisibleTypeAnnotationIndex)) {
                    if (currentInvisibleTypeAnnotationBytecodeOffset == currentBytecodeOffset) {
                        int currentAnnotationOffset = this.readTypeAnnotationTarget(context, invisibleTypeAnnotationOffsets[currentInvisibleTypeAnnotationIndex]);
                        String annotationDescriptor = this.readUTF8(currentAnnotationOffset, charBuffer);
                        currentAnnotationOffset += 2;
                        this.readElementValues(methodVisitor.visitInsnAnnotation(context.currentTypeAnnotationTarget, context.currentTypeAnnotationTargetPath, annotationDescriptor, false), currentAnnotationOffset, true, charBuffer);
                    }
                }
            }
            if (labels[codeLength] != null) {
                methodVisitor.visitLabel(labels[codeLength]);
            }
            if (localVariableTableOffset != 0 && (context.parsingOptions & 2) == 0) {
                int[] typeTable = null;
                if (localVariableTypeTableOffset != 0) {
                    typeTable = new int[this.readUnsignedShort(localVariableTypeTableOffset) * 3];
                    currentOffset = localVariableTypeTableOffset + 2;
                    for (int typeTableIndex = typeTable.length; typeTableIndex > 0; currentOffset += 10) {
                        typeTableIndex--;
                        typeTable[typeTableIndex] = currentOffset + 6;
                        typeTableIndex--;
                        typeTable[typeTableIndex] = this.readUnsignedShort(currentOffset + 8);
                        typeTableIndex--;
                        typeTable[typeTableIndex] = this.readUnsignedShort(currentOffset);
                    }
                }
                int localVariableTableLength = this.readUnsignedShort(localVariableTableOffset);
                currentOffset = localVariableTableOffset + 2;
                while (localVariableTableLength-- > 0) {
                    int startPc = this.readUnsignedShort(currentOffset);
                    int length = this.readUnsignedShort(currentOffset + 2);
                    String namex = this.readUTF8(currentOffset + 4, charBuffer);
                    String descriptorx = this.readUTF8(currentOffset + 6, charBuffer);
                    int index = this.readUnsignedShort(currentOffset + 8);
                    currentOffset += 10;
                    String signature = null;
                    if (typeTable != null) {
                        for (int i = 0; i < typeTable.length; i += 3) {
                            if (typeTable[i] == startPc && typeTable[i + 1] == index) {
                                signature = this.readUTF8(typeTable[i + 2], charBuffer);
                                break;
                            }
                        }
                    }
                    methodVisitor.visitLocalVariable(namex, descriptorx, signature, labels[startPc], labels[startPc + length], index);
                }
            }
            if (visibleTypeAnnotationOffsets != null) {
                for (int typeAnnotationOffset : visibleTypeAnnotationOffsets) {
                    int targetType = this.readByte(typeAnnotationOffset);
                    if (targetType == 64 || targetType == 65) {
                        currentOffset = this.readTypeAnnotationTarget(context, typeAnnotationOffset);
                        String annotationDescriptor = this.readUTF8(currentOffset, charBuffer);
                        currentOffset += 2;
                        this.readElementValues(methodVisitor.visitLocalVariableAnnotation(context.currentTypeAnnotationTarget, context.currentTypeAnnotationTargetPath, context.currentLocalVariableAnnotationRangeStarts, context.currentLocalVariableAnnotationRangeEnds, context.currentLocalVariableAnnotationRangeIndices, annotationDescriptor, true), currentOffset, true, charBuffer);
                    }
                }
            }
            if (invisibleTypeAnnotationOffsets != null) {
                for (int typeAnnotationOffsetx : invisibleTypeAnnotationOffsets) {
                    int targetType = this.readByte(typeAnnotationOffsetx);
                    if (targetType == 64 || targetType == 65) {
                        currentOffset = this.readTypeAnnotationTarget(context, typeAnnotationOffsetx);
                        String annotationDescriptor = this.readUTF8(currentOffset, charBuffer);
                        currentOffset += 2;
                        this.readElementValues(methodVisitor.visitLocalVariableAnnotation(context.currentTypeAnnotationTarget, context.currentTypeAnnotationTargetPath, context.currentLocalVariableAnnotationRangeStarts, context.currentLocalVariableAnnotationRangeEnds, context.currentLocalVariableAnnotationRangeIndices, annotationDescriptor, false), currentOffset, true, charBuffer);
                    }
                }
            }
            while (attributes != null) {
                Attribute nextAttribute = attributes.nextAttribute;
                attributes.nextAttribute = null;
                methodVisitor.visitAttribute(attributes);
                attributes = nextAttribute;
            }
            methodVisitor.visitMaxs(maxStack, maxLocals);
        }
    }

    protected Label readLabel(int bytecodeOffset, Label[] labels) {
        if (labels[bytecodeOffset] == null) {
            labels[bytecodeOffset] = new Label();
        }
        return labels[bytecodeOffset];
    }

    private Label createLabel(int bytecodeOffset, Label[] labels) {
        Label label = this.readLabel(bytecodeOffset, labels);
        label.flags = (short) (label.flags & -2);
        return label;
    }

    private void createDebugLabel(int bytecodeOffset, Label[] labels) {
        if (labels[bytecodeOffset] == null) {
            Label var10000 = this.readLabel(bytecodeOffset, labels);
            var10000.flags = (short) (var10000.flags | 1);
        }
    }

    private int[] readTypeAnnotations(MethodVisitor methodVisitor, Context context, int runtimeTypeAnnotationsOffset, boolean visible) {
        char[] charBuffer = context.charBuffer;
        int[] typeAnnotationsOffsets = new int[this.readUnsignedShort(runtimeTypeAnnotationsOffset)];
        int currentOffset = runtimeTypeAnnotationsOffset + 2;
        for (int i = 0; i < typeAnnotationsOffsets.length; i++) {
            typeAnnotationsOffsets[i] = currentOffset;
            int targetType = this.readInt(currentOffset);
            switch(targetType >>> 24) {
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
                case 11:
                case 12:
                case 13:
                case 14:
                case 15:
                case 19:
                case 20:
                case 21:
                case 22:
                case 24:
                case 25:
                case 26:
                case 27:
                case 28:
                case 29:
                case 30:
                case 31:
                case 32:
                case 33:
                case 34:
                case 35:
                case 36:
                case 37:
                case 38:
                case 39:
                case 40:
                case 41:
                case 42:
                case 43:
                case 44:
                case 45:
                case 46:
                case 47:
                case 48:
                case 49:
                case 50:
                case 51:
                case 52:
                case 53:
                case 54:
                case 55:
                case 56:
                case 57:
                case 58:
                case 59:
                case 60:
                case 61:
                case 62:
                case 63:
                default:
                    throw new IllegalArgumentException();
                case 16:
                case 17:
                case 18:
                case 23:
                case 66:
                case 67:
                case 68:
                case 69:
                case 70:
                    currentOffset += 3;
                    break;
                case 64:
                case 65:
                    int tableLength = this.readUnsignedShort(currentOffset + 1);
                    currentOffset += 3;
                    while (tableLength-- > 0) {
                        int startPc = this.readUnsignedShort(currentOffset);
                        int length = this.readUnsignedShort(currentOffset + 2);
                        currentOffset += 6;
                        this.createLabel(startPc, context.currentMethodLabels);
                        this.createLabel(startPc + length, context.currentMethodLabels);
                    }
                    break;
                case 71:
                case 72:
                case 73:
                case 74:
                case 75:
                    currentOffset += 4;
            }
            int pathLength = this.readByte(currentOffset);
            if (targetType >>> 24 == 66) {
                TypePath path = pathLength == 0 ? null : new TypePath(this.classFileBuffer, currentOffset);
                currentOffset += 1 + 2 * pathLength;
                String annotationDescriptor = this.readUTF8(currentOffset, charBuffer);
                currentOffset += 2;
                currentOffset = this.readElementValues(methodVisitor.visitTryCatchAnnotation(targetType & -256, path, annotationDescriptor, visible), currentOffset, true, charBuffer);
            } else {
                currentOffset += 3 + 2 * pathLength;
                currentOffset = this.readElementValues(null, currentOffset, true, charBuffer);
            }
        }
        return typeAnnotationsOffsets;
    }

    private int getTypeAnnotationBytecodeOffset(int[] typeAnnotationOffsets, int typeAnnotationIndex) {
        return typeAnnotationOffsets != null && typeAnnotationIndex < typeAnnotationOffsets.length && this.readByte(typeAnnotationOffsets[typeAnnotationIndex]) >= 67 ? this.readUnsignedShort(typeAnnotationOffsets[typeAnnotationIndex] + 1) : -1;
    }

    private int readTypeAnnotationTarget(Context context, int typeAnnotationOffset) {
        int targetType = this.readInt(typeAnnotationOffset);
        int currentOffset;
        switch(targetType >>> 24) {
            case 0:
            case 1:
            case 22:
                targetType &= -65536;
                currentOffset = typeAnnotationOffset + 2;
                break;
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 24:
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
            case 30:
            case 31:
            case 32:
            case 33:
            case 34:
            case 35:
            case 36:
            case 37:
            case 38:
            case 39:
            case 40:
            case 41:
            case 42:
            case 43:
            case 44:
            case 45:
            case 46:
            case 47:
            case 48:
            case 49:
            case 50:
            case 51:
            case 52:
            case 53:
            case 54:
            case 55:
            case 56:
            case 57:
            case 58:
            case 59:
            case 60:
            case 61:
            case 62:
            case 63:
            default:
                throw new IllegalArgumentException();
            case 16:
            case 17:
            case 18:
            case 23:
            case 66:
                targetType &= -256;
                currentOffset = typeAnnotationOffset + 3;
                break;
            case 19:
            case 20:
            case 21:
                targetType &= -16777216;
                currentOffset = typeAnnotationOffset + 1;
                break;
            case 64:
            case 65:
                targetType &= -16777216;
                int tableLength = this.readUnsignedShort(typeAnnotationOffset + 1);
                currentOffset = typeAnnotationOffset + 3;
                context.currentLocalVariableAnnotationRangeStarts = new Label[tableLength];
                context.currentLocalVariableAnnotationRangeEnds = new Label[tableLength];
                context.currentLocalVariableAnnotationRangeIndices = new int[tableLength];
                for (int i = 0; i < tableLength; i++) {
                    int startPc = this.readUnsignedShort(currentOffset);
                    int length = this.readUnsignedShort(currentOffset + 2);
                    int index = this.readUnsignedShort(currentOffset + 4);
                    currentOffset += 6;
                    context.currentLocalVariableAnnotationRangeStarts[i] = this.createLabel(startPc, context.currentMethodLabels);
                    context.currentLocalVariableAnnotationRangeEnds[i] = this.createLabel(startPc + length, context.currentMethodLabels);
                    context.currentLocalVariableAnnotationRangeIndices[i] = index;
                }
                break;
            case 67:
            case 68:
            case 69:
            case 70:
                targetType &= -16777216;
                currentOffset = typeAnnotationOffset + 3;
                break;
            case 71:
            case 72:
            case 73:
            case 74:
            case 75:
                targetType &= -16776961;
                currentOffset = typeAnnotationOffset + 4;
        }
        context.currentTypeAnnotationTarget = targetType;
        int pathLength = this.readByte(currentOffset);
        context.currentTypeAnnotationTargetPath = pathLength == 0 ? null : new TypePath(this.classFileBuffer, currentOffset);
        return currentOffset + 1 + 2 * pathLength;
    }

    private void readParameterAnnotations(MethodVisitor methodVisitor, Context context, int runtimeParameterAnnotationsOffset, boolean visible) {
        int currentOffset = runtimeParameterAnnotationsOffset + 1;
        int numParameters = this.classFileBuffer[runtimeParameterAnnotationsOffset] & 255;
        methodVisitor.visitAnnotableParameterCount(numParameters, visible);
        char[] charBuffer = context.charBuffer;
        for (int i = 0; i < numParameters; i++) {
            int numAnnotations = this.readUnsignedShort(currentOffset);
            currentOffset += 2;
            while (numAnnotations-- > 0) {
                String annotationDescriptor = this.readUTF8(currentOffset, charBuffer);
                currentOffset += 2;
                currentOffset = this.readElementValues(methodVisitor.visitParameterAnnotation(i, annotationDescriptor, visible), currentOffset, true, charBuffer);
            }
        }
    }

    private int readElementValues(AnnotationVisitor annotationVisitor, int annotationOffset, boolean named, char[] charBuffer) {
        int numElementValuePairs = this.readUnsignedShort(annotationOffset);
        int currentOffset = annotationOffset + 2;
        if (named) {
            while (numElementValuePairs-- > 0) {
                String elementName = this.readUTF8(currentOffset, charBuffer);
                currentOffset = this.readElementValue(annotationVisitor, currentOffset + 2, elementName, charBuffer);
            }
        } else {
            while (numElementValuePairs-- > 0) {
                currentOffset = this.readElementValue(annotationVisitor, currentOffset, null, charBuffer);
            }
        }
        if (annotationVisitor != null) {
            annotationVisitor.visitEnd();
        }
        return currentOffset;
    }

    private int readElementValue(AnnotationVisitor annotationVisitor, int elementValueOffset, String elementName, char[] charBuffer) {
        if (annotationVisitor == null) {
            switch(this.classFileBuffer[elementValueOffset] & 0xFF) {
                case 64:
                    return this.readElementValues(null, elementValueOffset + 3, true, charBuffer);
                case 91:
                    return this.readElementValues(null, elementValueOffset + 1, false, charBuffer);
                case 101:
                    return elementValueOffset + 5;
                default:
                    return elementValueOffset + 3;
            }
        } else {
            int currentOffset = elementValueOffset + 1;
            switch(this.classFileBuffer[elementValueOffset] & 0xFF) {
                case 64:
                    currentOffset = this.readElementValues(annotationVisitor.visitAnnotation(elementName, this.readUTF8(currentOffset, charBuffer)), currentOffset + 2, true, charBuffer);
                    break;
                case 65:
                case 69:
                case 71:
                case 72:
                case 75:
                case 76:
                case 77:
                case 78:
                case 79:
                case 80:
                case 81:
                case 82:
                case 84:
                case 85:
                case 86:
                case 87:
                case 88:
                case 89:
                case 92:
                case 93:
                case 94:
                case 95:
                case 96:
                case 97:
                case 98:
                case 100:
                case 102:
                case 103:
                case 104:
                case 105:
                case 106:
                case 107:
                case 108:
                case 109:
                case 110:
                case 111:
                case 112:
                case 113:
                case 114:
                default:
                    throw new IllegalArgumentException();
                case 66:
                    annotationVisitor.visit(elementName, (byte) this.readInt(this.cpInfoOffsets[this.readUnsignedShort(currentOffset)]));
                    currentOffset += 2;
                    break;
                case 67:
                    annotationVisitor.visit(elementName, (char) this.readInt(this.cpInfoOffsets[this.readUnsignedShort(currentOffset)]));
                    currentOffset += 2;
                    break;
                case 68:
                case 70:
                case 73:
                case 74:
                    annotationVisitor.visit(elementName, this.readConst(this.readUnsignedShort(currentOffset), charBuffer));
                    currentOffset += 2;
                    break;
                case 83:
                    annotationVisitor.visit(elementName, (short) this.readInt(this.cpInfoOffsets[this.readUnsignedShort(currentOffset)]));
                    currentOffset += 2;
                    break;
                case 90:
                    annotationVisitor.visit(elementName, this.readInt(this.cpInfoOffsets[this.readUnsignedShort(currentOffset)]) == 0 ? Boolean.FALSE : Boolean.TRUE);
                    currentOffset += 2;
                    break;
                case 91:
                    int numValues = this.readUnsignedShort(currentOffset);
                    currentOffset += 2;
                    if (numValues == 0) {
                        return this.readElementValues(annotationVisitor.visitArray(elementName), currentOffset - 2, false, charBuffer);
                    }
                    switch(this.classFileBuffer[currentOffset] & 0xFF) {
                        case 66:
                            byte[] byteValues = new byte[numValues];
                            for (int i = 0; i < numValues; i++) {
                                byteValues[i] = (byte) this.readInt(this.cpInfoOffsets[this.readUnsignedShort(currentOffset + 1)]);
                                currentOffset += 3;
                            }
                            annotationVisitor.visit(elementName, byteValues);
                            return currentOffset;
                        case 67:
                            char[] charValues = new char[numValues];
                            for (int i = 0; i < numValues; i++) {
                                charValues[i] = (char) this.readInt(this.cpInfoOffsets[this.readUnsignedShort(currentOffset + 1)]);
                                currentOffset += 3;
                            }
                            annotationVisitor.visit(elementName, charValues);
                            return currentOffset;
                        case 68:
                            double[] doubleValues = new double[numValues];
                            for (int i = 0; i < numValues; i++) {
                                doubleValues[i] = Double.longBitsToDouble(this.readLong(this.cpInfoOffsets[this.readUnsignedShort(currentOffset + 1)]));
                                currentOffset += 3;
                            }
                            annotationVisitor.visit(elementName, doubleValues);
                            return currentOffset;
                        case 69:
                        case 71:
                        case 72:
                        case 75:
                        case 76:
                        case 77:
                        case 78:
                        case 79:
                        case 80:
                        case 81:
                        case 82:
                        case 84:
                        case 85:
                        case 86:
                        case 87:
                        case 88:
                        case 89:
                        default:
                            currentOffset = this.readElementValues(annotationVisitor.visitArray(elementName), currentOffset - 2, false, charBuffer);
                            return currentOffset;
                        case 70:
                            float[] floatValues = new float[numValues];
                            for (int i = 0; i < numValues; i++) {
                                floatValues[i] = Float.intBitsToFloat(this.readInt(this.cpInfoOffsets[this.readUnsignedShort(currentOffset + 1)]));
                                currentOffset += 3;
                            }
                            annotationVisitor.visit(elementName, floatValues);
                            return currentOffset;
                        case 73:
                            int[] intValues = new int[numValues];
                            for (int i = 0; i < numValues; i++) {
                                intValues[i] = this.readInt(this.cpInfoOffsets[this.readUnsignedShort(currentOffset + 1)]);
                                currentOffset += 3;
                            }
                            annotationVisitor.visit(elementName, intValues);
                            return currentOffset;
                        case 74:
                            long[] longValues = new long[numValues];
                            for (int i = 0; i < numValues; i++) {
                                longValues[i] = this.readLong(this.cpInfoOffsets[this.readUnsignedShort(currentOffset + 1)]);
                                currentOffset += 3;
                            }
                            annotationVisitor.visit(elementName, longValues);
                            return currentOffset;
                        case 83:
                            short[] shortValues = new short[numValues];
                            for (int i = 0; i < numValues; i++) {
                                shortValues[i] = (short) this.readInt(this.cpInfoOffsets[this.readUnsignedShort(currentOffset + 1)]);
                                currentOffset += 3;
                            }
                            annotationVisitor.visit(elementName, shortValues);
                            return currentOffset;
                        case 90:
                            boolean[] booleanValues = new boolean[numValues];
                            for (int i = 0; i < numValues; i++) {
                                booleanValues[i] = this.readInt(this.cpInfoOffsets[this.readUnsignedShort(currentOffset + 1)]) != 0;
                                currentOffset += 3;
                            }
                            annotationVisitor.visit(elementName, booleanValues);
                            return currentOffset;
                    }
                case 99:
                    annotationVisitor.visit(elementName, Type.getType(this.readUTF8(currentOffset, charBuffer)));
                    currentOffset += 2;
                    break;
                case 101:
                    annotationVisitor.visitEnum(elementName, this.readUTF8(currentOffset, charBuffer), this.readUTF8(currentOffset + 2, charBuffer));
                    currentOffset += 4;
                    break;
                case 115:
                    annotationVisitor.visit(elementName, this.readUTF8(currentOffset, charBuffer));
                    currentOffset += 2;
            }
            return currentOffset;
        }
    }

    private void computeImplicitFrame(Context context) {
        String methodDescriptor = context.currentMethodDescriptor;
        Object[] locals = context.currentFrameLocalTypes;
        int numLocal = 0;
        if ((context.currentMethodAccessFlags & 8) == 0) {
            if ("<init>".equals(context.currentMethodName)) {
                locals[numLocal++] = Opcodes.UNINITIALIZED_THIS;
            } else {
                locals[numLocal++] = this.readClass(this.header + 2, context.charBuffer);
            }
        }
        int currentMethodDescritorOffset = 1;
        while (true) {
            int currentArgumentDescriptorStartOffset = currentMethodDescritorOffset;
            switch(methodDescriptor.charAt(currentMethodDescritorOffset++)) {
                case 'B':
                case 'C':
                case 'I':
                case 'S':
                case 'Z':
                    locals[numLocal++] = Opcodes.INTEGER;
                    break;
                case 'D':
                    locals[numLocal++] = Opcodes.DOUBLE;
                    break;
                case 'E':
                case 'G':
                case 'H':
                case 'K':
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
                    context.currentFrameLocalCount = numLocal;
                    return;
                case 'F':
                    locals[numLocal++] = Opcodes.FLOAT;
                    break;
                case 'J':
                    locals[numLocal++] = Opcodes.LONG;
                    break;
                case 'L':
                    while (methodDescriptor.charAt(currentMethodDescritorOffset) != ';') {
                        currentMethodDescritorOffset++;
                    }
                    locals[numLocal++] = methodDescriptor.substring(currentArgumentDescriptorStartOffset + 1, currentMethodDescritorOffset++);
                    break;
                case '[':
                    while (methodDescriptor.charAt(currentMethodDescritorOffset) == '[') {
                        currentMethodDescritorOffset++;
                    }
                    if (methodDescriptor.charAt(currentMethodDescritorOffset) == 'L') {
                        currentMethodDescritorOffset++;
                        while (methodDescriptor.charAt(currentMethodDescritorOffset) != ';') {
                            currentMethodDescritorOffset++;
                        }
                    }
                    locals[numLocal++] = methodDescriptor.substring(currentArgumentDescriptorStartOffset, ++currentMethodDescritorOffset);
            }
        }
    }

    private int readStackMapFrame(int stackMapFrameOffset, boolean compressed, boolean expand, Context context) {
        int currentOffset = stackMapFrameOffset;
        char[] charBuffer = context.charBuffer;
        Label[] labels = context.currentMethodLabels;
        int frameType;
        if (compressed) {
            currentOffset = stackMapFrameOffset + 1;
            frameType = this.classFileBuffer[stackMapFrameOffset] & 255;
        } else {
            frameType = 255;
            context.currentFrameOffset = -1;
        }
        context.currentFrameLocalCountDelta = 0;
        int offsetDelta;
        if (frameType < 64) {
            offsetDelta = frameType;
            context.currentFrameType = 3;
            context.currentFrameStackCount = 0;
        } else if (frameType < 128) {
            offsetDelta = frameType - 64;
            currentOffset = this.readVerificationTypeInfo(currentOffset, context.currentFrameStackTypes, 0, charBuffer, labels);
            context.currentFrameType = 4;
            context.currentFrameStackCount = 1;
        } else {
            if (frameType < 247) {
                throw new IllegalArgumentException();
            }
            offsetDelta = this.readUnsignedShort(currentOffset);
            currentOffset += 2;
            if (frameType == 247) {
                currentOffset = this.readVerificationTypeInfo(currentOffset, context.currentFrameStackTypes, 0, charBuffer, labels);
                context.currentFrameType = 4;
                context.currentFrameStackCount = 1;
            } else if (frameType >= 248 && frameType < 251) {
                context.currentFrameType = 2;
                context.currentFrameLocalCountDelta = 251 - frameType;
                context.currentFrameLocalCount = context.currentFrameLocalCount - context.currentFrameLocalCountDelta;
                context.currentFrameStackCount = 0;
            } else if (frameType == 251) {
                context.currentFrameType = 3;
                context.currentFrameStackCount = 0;
            } else if (frameType < 255) {
                int local = expand ? context.currentFrameLocalCount : 0;
                for (int k = frameType - 251; k > 0; k--) {
                    currentOffset = this.readVerificationTypeInfo(currentOffset, context.currentFrameLocalTypes, local++, charBuffer, labels);
                }
                context.currentFrameType = 1;
                context.currentFrameLocalCountDelta = frameType - 251;
                context.currentFrameLocalCount = context.currentFrameLocalCount + context.currentFrameLocalCountDelta;
                context.currentFrameStackCount = 0;
            } else {
                int numberOfLocals = this.readUnsignedShort(currentOffset);
                currentOffset += 2;
                context.currentFrameType = 0;
                context.currentFrameLocalCountDelta = numberOfLocals;
                context.currentFrameLocalCount = numberOfLocals;
                for (int local = 0; local < numberOfLocals; local++) {
                    currentOffset = this.readVerificationTypeInfo(currentOffset, context.currentFrameLocalTypes, local, charBuffer, labels);
                }
                int numberOfStackItems = this.readUnsignedShort(currentOffset);
                currentOffset += 2;
                context.currentFrameStackCount = numberOfStackItems;
                for (int stack = 0; stack < numberOfStackItems; stack++) {
                    currentOffset = this.readVerificationTypeInfo(currentOffset, context.currentFrameStackTypes, stack, charBuffer, labels);
                }
            }
        }
        context.currentFrameOffset += offsetDelta + 1;
        this.createLabel(context.currentFrameOffset, labels);
        return currentOffset;
    }

    private int readVerificationTypeInfo(int verificationTypeInfoOffset, Object[] frame, int index, char[] charBuffer, Label[] labels) {
        int currentOffset = verificationTypeInfoOffset + 1;
        int tag = this.classFileBuffer[verificationTypeInfoOffset] & 255;
        switch(tag) {
            case 0:
                frame[index] = Opcodes.TOP;
                break;
            case 1:
                frame[index] = Opcodes.INTEGER;
                break;
            case 2:
                frame[index] = Opcodes.FLOAT;
                break;
            case 3:
                frame[index] = Opcodes.DOUBLE;
                break;
            case 4:
                frame[index] = Opcodes.LONG;
                break;
            case 5:
                frame[index] = Opcodes.NULL;
                break;
            case 6:
                frame[index] = Opcodes.UNINITIALIZED_THIS;
                break;
            case 7:
                frame[index] = this.readClass(currentOffset, charBuffer);
                currentOffset += 2;
                break;
            case 8:
                frame[index] = this.createLabel(this.readUnsignedShort(currentOffset), labels);
                currentOffset += 2;
                break;
            default:
                throw new IllegalArgumentException();
        }
        return currentOffset;
    }

    final int getFirstAttributeOffset() {
        int currentOffset = this.header + 8 + this.readUnsignedShort(this.header + 6) * 2;
        int fieldsCount = this.readUnsignedShort(currentOffset);
        currentOffset += 2;
        while (fieldsCount-- > 0) {
            int attributesCount = this.readUnsignedShort(currentOffset + 6);
            currentOffset += 8;
            while (attributesCount-- > 0) {
                currentOffset += 6 + this.readInt(currentOffset + 2);
            }
        }
        int methodsCount = this.readUnsignedShort(currentOffset);
        currentOffset += 2;
        while (methodsCount-- > 0) {
            int attributesCount = this.readUnsignedShort(currentOffset + 6);
            currentOffset += 8;
            while (attributesCount-- > 0) {
                currentOffset += 6 + this.readInt(currentOffset + 2);
            }
        }
        return currentOffset + 2;
    }

    private int[] readBootstrapMethodsAttribute(int maxStringLength) {
        char[] charBuffer = new char[maxStringLength];
        int currentAttributeOffset = this.getFirstAttributeOffset();
        for (int i = this.readUnsignedShort(currentAttributeOffset - 2); i > 0; i--) {
            String attributeName = this.readUTF8(currentAttributeOffset, charBuffer);
            int attributeLength = this.readInt(currentAttributeOffset + 2);
            currentAttributeOffset += 6;
            if ("BootstrapMethods".equals(attributeName)) {
                int[] result = new int[this.readUnsignedShort(currentAttributeOffset)];
                int currentBootstrapMethodOffset = currentAttributeOffset + 2;
                for (int j = 0; j < result.length; j++) {
                    result[j] = currentBootstrapMethodOffset;
                    currentBootstrapMethodOffset += 4 + this.readUnsignedShort(currentBootstrapMethodOffset + 2) * 2;
                }
                return result;
            }
            currentAttributeOffset += attributeLength;
        }
        throw new IllegalArgumentException();
    }

    private Attribute readAttribute(Attribute[] attributePrototypes, String type, int offset, int length, char[] charBuffer, int codeAttributeOffset, Label[] labels) {
        for (Attribute attributePrototype : attributePrototypes) {
            if (attributePrototype.type.equals(type)) {
                return attributePrototype.read(this, offset, length, charBuffer, codeAttributeOffset, labels);
            }
        }
        return new Attribute(type).read(this, offset, length, null, -1, null);
    }

    public int getItemCount() {
        return this.cpInfoOffsets.length;
    }

    public int getItem(int constantPoolEntryIndex) {
        return this.cpInfoOffsets[constantPoolEntryIndex];
    }

    public int getMaxStringLength() {
        return this.maxStringLength;
    }

    public int readByte(int offset) {
        return this.classFileBuffer[offset] & 0xFF;
    }

    public int readUnsignedShort(int offset) {
        byte[] classBuffer = this.classFileBuffer;
        return (classBuffer[offset] & 0xFF) << 8 | classBuffer[offset + 1] & 0xFF;
    }

    public short readShort(int offset) {
        byte[] classBuffer = this.classFileBuffer;
        return (short) ((classBuffer[offset] & 255) << 8 | classBuffer[offset + 1] & 255);
    }

    public int readInt(int offset) {
        byte[] classBuffer = this.classFileBuffer;
        return (classBuffer[offset] & 0xFF) << 24 | (classBuffer[offset + 1] & 0xFF) << 16 | (classBuffer[offset + 2] & 0xFF) << 8 | classBuffer[offset + 3] & 0xFF;
    }

    public long readLong(int offset) {
        long l1 = (long) this.readInt(offset);
        long l0 = (long) this.readInt(offset + 4) & 4294967295L;
        return l1 << 32 | l0;
    }

    public String readUTF8(int offset, char[] charBuffer) {
        int constantPoolEntryIndex = this.readUnsignedShort(offset);
        return offset != 0 && constantPoolEntryIndex != 0 ? this.readUtf(constantPoolEntryIndex, charBuffer) : null;
    }

    final String readUtf(int constantPoolEntryIndex, char[] charBuffer) {
        String value = this.constantUtf8Values[constantPoolEntryIndex];
        if (value != null) {
            return value;
        } else {
            int cpInfoOffset = this.cpInfoOffsets[constantPoolEntryIndex];
            return this.constantUtf8Values[constantPoolEntryIndex] = this.readUtf(cpInfoOffset + 2, this.readUnsignedShort(cpInfoOffset), charBuffer);
        }
    }

    private String readUtf(int utfOffset, int utfLength, char[] charBuffer) {
        int currentOffset = utfOffset;
        int endOffset = utfOffset + utfLength;
        int strLength = 0;
        byte[] classBuffer = this.classFileBuffer;
        while (currentOffset < endOffset) {
            int currentByte = classBuffer[currentOffset++];
            if ((currentByte & 128) == 0) {
                charBuffer[strLength++] = (char) (currentByte & 127);
            } else if ((currentByte & 224) == 192) {
                charBuffer[strLength++] = (char) (((currentByte & 31) << 6) + (classBuffer[currentOffset++] & 63));
            } else {
                charBuffer[strLength++] = (char) (((currentByte & 15) << 12) + ((classBuffer[currentOffset++] & 63) << 6) + (classBuffer[currentOffset++] & 63));
            }
        }
        return new String(charBuffer, 0, strLength);
    }

    private String readStringish(int offset, char[] charBuffer) {
        return this.readUTF8(this.cpInfoOffsets[this.readUnsignedShort(offset)], charBuffer);
    }

    public String readClass(int offset, char[] charBuffer) {
        return this.readStringish(offset, charBuffer);
    }

    public String readModule(int offset, char[] charBuffer) {
        return this.readStringish(offset, charBuffer);
    }

    public String readPackage(int offset, char[] charBuffer) {
        return this.readStringish(offset, charBuffer);
    }

    private ConstantDynamic readConstantDynamic(int constantPoolEntryIndex, char[] charBuffer) {
        ConstantDynamic constantDynamic = this.constantDynamicValues[constantPoolEntryIndex];
        if (constantDynamic != null) {
            return constantDynamic;
        } else {
            int cpInfoOffset = this.cpInfoOffsets[constantPoolEntryIndex];
            int nameAndTypeCpInfoOffset = this.cpInfoOffsets[this.readUnsignedShort(cpInfoOffset + 2)];
            String name = this.readUTF8(nameAndTypeCpInfoOffset, charBuffer);
            String descriptor = this.readUTF8(nameAndTypeCpInfoOffset + 2, charBuffer);
            int bootstrapMethodOffset = this.bootstrapMethodOffsets[this.readUnsignedShort(cpInfoOffset)];
            Handle handle = (Handle) this.readConst(this.readUnsignedShort(bootstrapMethodOffset), charBuffer);
            Object[] bootstrapMethodArguments = new Object[this.readUnsignedShort(bootstrapMethodOffset + 2)];
            bootstrapMethodOffset += 4;
            for (int i = 0; i < bootstrapMethodArguments.length; i++) {
                bootstrapMethodArguments[i] = this.readConst(this.readUnsignedShort(bootstrapMethodOffset), charBuffer);
                bootstrapMethodOffset += 2;
            }
            return this.constantDynamicValues[constantPoolEntryIndex] = new ConstantDynamic(name, descriptor, handle, bootstrapMethodArguments);
        }
    }

    public Object readConst(int constantPoolEntryIndex, char[] charBuffer) {
        int cpInfoOffset = this.cpInfoOffsets[constantPoolEntryIndex];
        switch(this.classFileBuffer[cpInfoOffset - 1]) {
            case 3:
                return this.readInt(cpInfoOffset);
            case 4:
                return Float.intBitsToFloat(this.readInt(cpInfoOffset));
            case 5:
                return this.readLong(cpInfoOffset);
            case 6:
                return Double.longBitsToDouble(this.readLong(cpInfoOffset));
            case 7:
                return Type.getObjectType(this.readUTF8(cpInfoOffset, charBuffer));
            case 8:
                return this.readUTF8(cpInfoOffset, charBuffer);
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            default:
                throw new IllegalArgumentException();
            case 15:
                int referenceKind = this.readByte(cpInfoOffset);
                int referenceCpInfoOffset = this.cpInfoOffsets[this.readUnsignedShort(cpInfoOffset + 1)];
                int nameAndTypeCpInfoOffset = this.cpInfoOffsets[this.readUnsignedShort(referenceCpInfoOffset + 2)];
                String owner = this.readClass(referenceCpInfoOffset, charBuffer);
                String name = this.readUTF8(nameAndTypeCpInfoOffset, charBuffer);
                String descriptor = this.readUTF8(nameAndTypeCpInfoOffset + 2, charBuffer);
                boolean isInterface = this.classFileBuffer[referenceCpInfoOffset - 1] == 11;
                return new Handle(referenceKind, owner, name, descriptor, isInterface);
            case 16:
                return Type.getMethodType(this.readUTF8(cpInfoOffset, charBuffer));
            case 17:
                return this.readConstantDynamic(constantPoolEntryIndex, charBuffer);
        }
    }
}
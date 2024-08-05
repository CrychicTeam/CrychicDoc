package me.lucko.spark.lib.asm;

final class AnnotationWriter extends AnnotationVisitor {

    private final SymbolTable symbolTable;

    private final boolean useNamedValues;

    private final ByteVector annotation;

    private final int numElementValuePairsOffset;

    private int numElementValuePairs;

    private final AnnotationWriter previousAnnotation;

    private AnnotationWriter nextAnnotation;

    AnnotationWriter(SymbolTable symbolTable, boolean useNamedValues, ByteVector annotation, AnnotationWriter previousAnnotation) {
        super(589824);
        this.symbolTable = symbolTable;
        this.useNamedValues = useNamedValues;
        this.annotation = annotation;
        this.numElementValuePairsOffset = annotation.length == 0 ? -1 : annotation.length - 2;
        this.previousAnnotation = previousAnnotation;
        if (previousAnnotation != null) {
            previousAnnotation.nextAnnotation = this;
        }
    }

    static AnnotationWriter create(SymbolTable symbolTable, String descriptor, AnnotationWriter previousAnnotation) {
        ByteVector annotation = new ByteVector();
        annotation.putShort(symbolTable.addConstantUtf8(descriptor)).putShort(0);
        return new AnnotationWriter(symbolTable, true, annotation, previousAnnotation);
    }

    static AnnotationWriter create(SymbolTable symbolTable, int typeRef, TypePath typePath, String descriptor, AnnotationWriter previousAnnotation) {
        ByteVector typeAnnotation = new ByteVector();
        TypeReference.putTarget(typeRef, typeAnnotation);
        TypePath.put(typePath, typeAnnotation);
        typeAnnotation.putShort(symbolTable.addConstantUtf8(descriptor)).putShort(0);
        return new AnnotationWriter(symbolTable, true, typeAnnotation, previousAnnotation);
    }

    @Override
    public void visit(String name, Object value) {
        this.numElementValuePairs++;
        if (this.useNamedValues) {
            this.annotation.putShort(this.symbolTable.addConstantUtf8(name));
        }
        if (value instanceof String) {
            this.annotation.put12(115, this.symbolTable.addConstantUtf8((String) value));
        } else if (value instanceof Byte) {
            this.annotation.put12(66, this.symbolTable.addConstantInteger((Byte) value).index);
        } else if (value instanceof Boolean) {
            int booleanValue = (Boolean) value ? 1 : 0;
            this.annotation.put12(90, this.symbolTable.addConstantInteger(booleanValue).index);
        } else if (value instanceof Character) {
            this.annotation.put12(67, this.symbolTable.addConstantInteger((Character) value).index);
        } else if (value instanceof Short) {
            this.annotation.put12(83, this.symbolTable.addConstantInteger((Short) value).index);
        } else if (value instanceof Type) {
            this.annotation.put12(99, this.symbolTable.addConstantUtf8(((Type) value).getDescriptor()));
        } else if (value instanceof byte[]) {
            byte[] byteArray = (byte[]) value;
            this.annotation.put12(91, byteArray.length);
            for (byte byteValue : byteArray) {
                this.annotation.put12(66, this.symbolTable.addConstantInteger(byteValue).index);
            }
        } else if (value instanceof boolean[]) {
            boolean[] booleanArray = (boolean[]) value;
            this.annotation.put12(91, booleanArray.length);
            for (boolean booleanValue : booleanArray) {
                this.annotation.put12(90, this.symbolTable.addConstantInteger(booleanValue ? 1 : 0).index);
            }
        } else if (value instanceof short[]) {
            short[] shortArray = (short[]) value;
            this.annotation.put12(91, shortArray.length);
            for (short shortValue : shortArray) {
                this.annotation.put12(83, this.symbolTable.addConstantInteger(shortValue).index);
            }
        } else if (value instanceof char[]) {
            char[] charArray = (char[]) value;
            this.annotation.put12(91, charArray.length);
            for (char charValue : charArray) {
                this.annotation.put12(67, this.symbolTable.addConstantInteger(charValue).index);
            }
        } else if (value instanceof int[]) {
            int[] intArray = (int[]) value;
            this.annotation.put12(91, intArray.length);
            for (int intValue : intArray) {
                this.annotation.put12(73, this.symbolTable.addConstantInteger(intValue).index);
            }
        } else if (value instanceof long[]) {
            long[] longArray = (long[]) value;
            this.annotation.put12(91, longArray.length);
            for (long longValue : longArray) {
                this.annotation.put12(74, this.symbolTable.addConstantLong(longValue).index);
            }
        } else if (value instanceof float[]) {
            float[] floatArray = (float[]) value;
            this.annotation.put12(91, floatArray.length);
            for (float floatValue : floatArray) {
                this.annotation.put12(70, this.symbolTable.addConstantFloat(floatValue).index);
            }
        } else if (value instanceof double[]) {
            double[] doubleArray = (double[]) value;
            this.annotation.put12(91, doubleArray.length);
            for (double doubleValue : doubleArray) {
                this.annotation.put12(68, this.symbolTable.addConstantDouble(doubleValue).index);
            }
        } else {
            Symbol symbol = this.symbolTable.addConstant(value);
            this.annotation.put12(".s.IFJDCS".charAt(symbol.tag), symbol.index);
        }
    }

    @Override
    public void visitEnum(String name, String descriptor, String value) {
        this.numElementValuePairs++;
        if (this.useNamedValues) {
            this.annotation.putShort(this.symbolTable.addConstantUtf8(name));
        }
        this.annotation.put12(101, this.symbolTable.addConstantUtf8(descriptor)).putShort(this.symbolTable.addConstantUtf8(value));
    }

    @Override
    public AnnotationVisitor visitAnnotation(String name, String descriptor) {
        this.numElementValuePairs++;
        if (this.useNamedValues) {
            this.annotation.putShort(this.symbolTable.addConstantUtf8(name));
        }
        this.annotation.put12(64, this.symbolTable.addConstantUtf8(descriptor)).putShort(0);
        return new AnnotationWriter(this.symbolTable, true, this.annotation, null);
    }

    @Override
    public AnnotationVisitor visitArray(String name) {
        this.numElementValuePairs++;
        if (this.useNamedValues) {
            this.annotation.putShort(this.symbolTable.addConstantUtf8(name));
        }
        this.annotation.put12(91, 0);
        return new AnnotationWriter(this.symbolTable, false, this.annotation, null);
    }

    @Override
    public void visitEnd() {
        if (this.numElementValuePairsOffset != -1) {
            byte[] data = this.annotation.data;
            data[this.numElementValuePairsOffset] = (byte) (this.numElementValuePairs >>> 8);
            data[this.numElementValuePairsOffset + 1] = (byte) this.numElementValuePairs;
        }
    }

    int computeAnnotationsSize(String attributeName) {
        if (attributeName != null) {
            this.symbolTable.addConstantUtf8(attributeName);
        }
        int attributeSize = 8;
        for (AnnotationWriter annotationWriter = this; annotationWriter != null; annotationWriter = annotationWriter.previousAnnotation) {
            attributeSize += annotationWriter.annotation.length;
        }
        return attributeSize;
    }

    static int computeAnnotationsSize(AnnotationWriter lastRuntimeVisibleAnnotation, AnnotationWriter lastRuntimeInvisibleAnnotation, AnnotationWriter lastRuntimeVisibleTypeAnnotation, AnnotationWriter lastRuntimeInvisibleTypeAnnotation) {
        int size = 0;
        if (lastRuntimeVisibleAnnotation != null) {
            size += lastRuntimeVisibleAnnotation.computeAnnotationsSize("RuntimeVisibleAnnotations");
        }
        if (lastRuntimeInvisibleAnnotation != null) {
            size += lastRuntimeInvisibleAnnotation.computeAnnotationsSize("RuntimeInvisibleAnnotations");
        }
        if (lastRuntimeVisibleTypeAnnotation != null) {
            size += lastRuntimeVisibleTypeAnnotation.computeAnnotationsSize("RuntimeVisibleTypeAnnotations");
        }
        if (lastRuntimeInvisibleTypeAnnotation != null) {
            size += lastRuntimeInvisibleTypeAnnotation.computeAnnotationsSize("RuntimeInvisibleTypeAnnotations");
        }
        return size;
    }

    void putAnnotations(int attributeNameIndex, ByteVector output) {
        int attributeLength = 2;
        int numAnnotations = 0;
        AnnotationWriter annotationWriter = this;
        AnnotationWriter firstAnnotation;
        for (firstAnnotation = null; annotationWriter != null; annotationWriter = annotationWriter.previousAnnotation) {
            annotationWriter.visitEnd();
            attributeLength += annotationWriter.annotation.length;
            numAnnotations++;
            firstAnnotation = annotationWriter;
        }
        output.putShort(attributeNameIndex);
        output.putInt(attributeLength);
        output.putShort(numAnnotations);
        for (AnnotationWriter var7 = firstAnnotation; var7 != null; var7 = var7.nextAnnotation) {
            output.putByteArray(var7.annotation.data, 0, var7.annotation.length);
        }
    }

    static void putAnnotations(SymbolTable symbolTable, AnnotationWriter lastRuntimeVisibleAnnotation, AnnotationWriter lastRuntimeInvisibleAnnotation, AnnotationWriter lastRuntimeVisibleTypeAnnotation, AnnotationWriter lastRuntimeInvisibleTypeAnnotation, ByteVector output) {
        if (lastRuntimeVisibleAnnotation != null) {
            lastRuntimeVisibleAnnotation.putAnnotations(symbolTable.addConstantUtf8("RuntimeVisibleAnnotations"), output);
        }
        if (lastRuntimeInvisibleAnnotation != null) {
            lastRuntimeInvisibleAnnotation.putAnnotations(symbolTable.addConstantUtf8("RuntimeInvisibleAnnotations"), output);
        }
        if (lastRuntimeVisibleTypeAnnotation != null) {
            lastRuntimeVisibleTypeAnnotation.putAnnotations(symbolTable.addConstantUtf8("RuntimeVisibleTypeAnnotations"), output);
        }
        if (lastRuntimeInvisibleTypeAnnotation != null) {
            lastRuntimeInvisibleTypeAnnotation.putAnnotations(symbolTable.addConstantUtf8("RuntimeInvisibleTypeAnnotations"), output);
        }
    }

    static int computeParameterAnnotationsSize(String attributeName, AnnotationWriter[] annotationWriters, int annotableParameterCount) {
        int attributeSize = 7 + 2 * annotableParameterCount;
        for (int i = 0; i < annotableParameterCount; i++) {
            AnnotationWriter annotationWriter = annotationWriters[i];
            attributeSize += annotationWriter == null ? 0 : annotationWriter.computeAnnotationsSize(attributeName) - 8;
        }
        return attributeSize;
    }

    static void putParameterAnnotations(int attributeNameIndex, AnnotationWriter[] annotationWriters, int annotableParameterCount, ByteVector output) {
        int attributeLength = 1 + 2 * annotableParameterCount;
        for (int i = 0; i < annotableParameterCount; i++) {
            AnnotationWriter annotationWriter = annotationWriters[i];
            attributeLength += annotationWriter == null ? 0 : annotationWriter.computeAnnotationsSize(null) - 8;
        }
        output.putShort(attributeNameIndex);
        output.putInt(attributeLength);
        output.putByte(annotableParameterCount);
        for (int i = 0; i < annotableParameterCount; i++) {
            AnnotationWriter annotationWriter = annotationWriters[i];
            AnnotationWriter firstAnnotation = null;
            int numAnnotations;
            for (numAnnotations = 0; annotationWriter != null; annotationWriter = annotationWriter.previousAnnotation) {
                annotationWriter.visitEnd();
                numAnnotations++;
                firstAnnotation = annotationWriter;
            }
            output.putShort(numAnnotations);
            for (AnnotationWriter var11 = firstAnnotation; var11 != null; var11 = var11.nextAnnotation) {
                output.putByteArray(var11.annotation.data, 0, var11.annotation.length);
            }
        }
    }
}
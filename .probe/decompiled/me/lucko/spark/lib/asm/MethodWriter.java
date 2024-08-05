package me.lucko.spark.lib.asm;

final class MethodWriter extends MethodVisitor {

    static final int COMPUTE_NOTHING = 0;

    static final int COMPUTE_MAX_STACK_AND_LOCAL = 1;

    static final int COMPUTE_MAX_STACK_AND_LOCAL_FROM_FRAMES = 2;

    static final int COMPUTE_INSERTED_FRAMES = 3;

    static final int COMPUTE_ALL_FRAMES = 4;

    private static final int NA = 0;

    private static final int[] STACK_SIZE_DELTA = new int[] { 0, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 1, 1, 1, 2, 2, 1, 1, 1, 0, 0, 1, 2, 1, 2, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, 0, -1, 0, -1, -1, -1, -1, -1, -2, -1, -2, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -3, -4, -3, -4, -3, -3, -3, -3, -1, -2, 1, 1, 1, 2, 2, 2, 0, -1, -2, -1, -2, -1, -2, -1, -2, -1, -2, -1, -2, -1, -2, -1, -2, -1, -2, -1, -2, 0, 0, 0, 0, -1, -1, -1, -1, -1, -1, -1, -2, -1, -2, -1, -2, 0, 1, 0, 1, -1, -1, 0, 0, 1, 1, -1, 0, -1, 0, 0, 0, -3, -1, -1, -3, -3, -1, -1, -1, -1, -1, -1, -2, -2, -2, -2, -2, -2, -2, -2, 0, 1, 0, -1, -1, -1, -2, -1, -2, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, -1, -1, 0, 0, -1, -1, 0, 0 };

    private final SymbolTable symbolTable;

    private final int accessFlags;

    private final int nameIndex;

    private final String name;

    private final int descriptorIndex;

    private final String descriptor;

    private int maxStack;

    private int maxLocals;

    private final ByteVector code = new ByteVector();

    private Handler firstHandler;

    private Handler lastHandler;

    private int lineNumberTableLength;

    private ByteVector lineNumberTable;

    private int localVariableTableLength;

    private ByteVector localVariableTable;

    private int localVariableTypeTableLength;

    private ByteVector localVariableTypeTable;

    private int stackMapTableNumberOfEntries;

    private ByteVector stackMapTableEntries;

    private AnnotationWriter lastCodeRuntimeVisibleTypeAnnotation;

    private AnnotationWriter lastCodeRuntimeInvisibleTypeAnnotation;

    private Attribute firstCodeAttribute;

    private final int numberOfExceptions;

    private final int[] exceptionIndexTable;

    private final int signatureIndex;

    private AnnotationWriter lastRuntimeVisibleAnnotation;

    private AnnotationWriter lastRuntimeInvisibleAnnotation;

    private int visibleAnnotableParameterCount;

    private AnnotationWriter[] lastRuntimeVisibleParameterAnnotations;

    private int invisibleAnnotableParameterCount;

    private AnnotationWriter[] lastRuntimeInvisibleParameterAnnotations;

    private AnnotationWriter lastRuntimeVisibleTypeAnnotation;

    private AnnotationWriter lastRuntimeInvisibleTypeAnnotation;

    private ByteVector defaultValue;

    private int parametersCount;

    private ByteVector parameters;

    private Attribute firstAttribute;

    private final int compute;

    private Label firstBasicBlock;

    private Label lastBasicBlock;

    private Label currentBasicBlock;

    private int relativeStackSize;

    private int maxRelativeStackSize;

    private int currentLocals;

    private int previousFrameOffset;

    private int[] previousFrame;

    private int[] currentFrame;

    private boolean hasSubroutines;

    private boolean hasAsmInstructions;

    private int lastBytecodeOffset;

    private int sourceOffset;

    private int sourceLength;

    MethodWriter(SymbolTable symbolTable, int access, String name, String descriptor, String signature, String[] exceptions, int compute) {
        super(589824);
        this.symbolTable = symbolTable;
        this.accessFlags = "<init>".equals(name) ? access | 262144 : access;
        this.nameIndex = symbolTable.addConstantUtf8(name);
        this.name = name;
        this.descriptorIndex = symbolTable.addConstantUtf8(descriptor);
        this.descriptor = descriptor;
        this.signatureIndex = signature == null ? 0 : symbolTable.addConstantUtf8(signature);
        if (exceptions != null && exceptions.length > 0) {
            this.numberOfExceptions = exceptions.length;
            this.exceptionIndexTable = new int[this.numberOfExceptions];
            for (int i = 0; i < this.numberOfExceptions; i++) {
                this.exceptionIndexTable[i] = symbolTable.addConstantClass(exceptions[i]).index;
            }
        } else {
            this.numberOfExceptions = 0;
            this.exceptionIndexTable = null;
        }
        this.compute = compute;
        if (compute != 0) {
            int argumentsSize = Type.getArgumentsAndReturnSizes(descriptor) >> 2;
            if ((access & 8) != 0) {
                argumentsSize--;
            }
            this.maxLocals = argumentsSize;
            this.currentLocals = argumentsSize;
            this.firstBasicBlock = new Label();
            this.visitLabel(this.firstBasicBlock);
        }
    }

    boolean hasFrames() {
        return this.stackMapTableNumberOfEntries > 0;
    }

    boolean hasAsmInstructions() {
        return this.hasAsmInstructions;
    }

    @Override
    public void visitParameter(String name, int access) {
        if (this.parameters == null) {
            this.parameters = new ByteVector();
        }
        this.parametersCount++;
        this.parameters.putShort(name == null ? 0 : this.symbolTable.addConstantUtf8(name)).putShort(access);
    }

    @Override
    public AnnotationVisitor visitAnnotationDefault() {
        this.defaultValue = new ByteVector();
        return new AnnotationWriter(this.symbolTable, false, this.defaultValue, null);
    }

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        return visible ? (this.lastRuntimeVisibleAnnotation = AnnotationWriter.create(this.symbolTable, descriptor, this.lastRuntimeVisibleAnnotation)) : (this.lastRuntimeInvisibleAnnotation = AnnotationWriter.create(this.symbolTable, descriptor, this.lastRuntimeInvisibleAnnotation));
    }

    @Override
    public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        return visible ? (this.lastRuntimeVisibleTypeAnnotation = AnnotationWriter.create(this.symbolTable, typeRef, typePath, descriptor, this.lastRuntimeVisibleTypeAnnotation)) : (this.lastRuntimeInvisibleTypeAnnotation = AnnotationWriter.create(this.symbolTable, typeRef, typePath, descriptor, this.lastRuntimeInvisibleTypeAnnotation));
    }

    @Override
    public void visitAnnotableParameterCount(int parameterCount, boolean visible) {
        if (visible) {
            this.visibleAnnotableParameterCount = parameterCount;
        } else {
            this.invisibleAnnotableParameterCount = parameterCount;
        }
    }

    @Override
    public AnnotationVisitor visitParameterAnnotation(int parameter, String annotationDescriptor, boolean visible) {
        if (visible) {
            if (this.lastRuntimeVisibleParameterAnnotations == null) {
                this.lastRuntimeVisibleParameterAnnotations = new AnnotationWriter[Type.getArgumentTypes(this.descriptor).length];
            }
            return this.lastRuntimeVisibleParameterAnnotations[parameter] = AnnotationWriter.create(this.symbolTable, annotationDescriptor, this.lastRuntimeVisibleParameterAnnotations[parameter]);
        } else {
            if (this.lastRuntimeInvisibleParameterAnnotations == null) {
                this.lastRuntimeInvisibleParameterAnnotations = new AnnotationWriter[Type.getArgumentTypes(this.descriptor).length];
            }
            return this.lastRuntimeInvisibleParameterAnnotations[parameter] = AnnotationWriter.create(this.symbolTable, annotationDescriptor, this.lastRuntimeInvisibleParameterAnnotations[parameter]);
        }
    }

    @Override
    public void visitAttribute(Attribute attribute) {
        if (attribute.isCodeAttribute()) {
            attribute.nextAttribute = this.firstCodeAttribute;
            this.firstCodeAttribute = attribute;
        } else {
            attribute.nextAttribute = this.firstAttribute;
            this.firstAttribute = attribute;
        }
    }

    @Override
    public void visitCode() {
    }

    @Override
    public void visitFrame(int type, int numLocal, Object[] local, int numStack, Object[] stack) {
        if (this.compute != 4) {
            if (this.compute == 3) {
                if (this.currentBasicBlock.frame == null) {
                    this.currentBasicBlock.frame = new CurrentFrame(this.currentBasicBlock);
                    this.currentBasicBlock.frame.setInputFrameFromDescriptor(this.symbolTable, this.accessFlags, this.descriptor, numLocal);
                    this.currentBasicBlock.frame.accept(this);
                } else {
                    if (type == -1) {
                        this.currentBasicBlock.frame.setInputFrameFromApiFormat(this.symbolTable, numLocal, local, numStack, stack);
                    }
                    this.currentBasicBlock.frame.accept(this);
                }
            } else if (type == -1) {
                if (this.previousFrame == null) {
                    int argumentsSize = Type.getArgumentsAndReturnSizes(this.descriptor) >> 2;
                    Frame implicitFirstFrame = new Frame(new Label());
                    implicitFirstFrame.setInputFrameFromDescriptor(this.symbolTable, this.accessFlags, this.descriptor, argumentsSize);
                    implicitFirstFrame.accept(this);
                }
                this.currentLocals = numLocal;
                int frameIndex = this.visitFrameStart(this.code.length, numLocal, numStack);
                for (int i = 0; i < numLocal; i++) {
                    this.currentFrame[frameIndex++] = Frame.getAbstractTypeFromApiFormat(this.symbolTable, local[i]);
                }
                for (int i = 0; i < numStack; i++) {
                    this.currentFrame[frameIndex++] = Frame.getAbstractTypeFromApiFormat(this.symbolTable, stack[i]);
                }
                this.visitFrameEnd();
            } else {
                if (this.symbolTable.getMajorVersion() < 50) {
                    throw new IllegalArgumentException("Class versions V1_5 or less must use F_NEW frames.");
                }
                int offsetDelta;
                if (this.stackMapTableEntries == null) {
                    this.stackMapTableEntries = new ByteVector();
                    offsetDelta = this.code.length;
                } else {
                    offsetDelta = this.code.length - this.previousFrameOffset - 1;
                    if (offsetDelta < 0) {
                        if (type == 3) {
                            return;
                        }
                        throw new IllegalStateException();
                    }
                }
                switch(type) {
                    case 0:
                        this.currentLocals = numLocal;
                        this.stackMapTableEntries.putByte(255).putShort(offsetDelta).putShort(numLocal);
                        for (int i = 0; i < numLocal; i++) {
                            this.putFrameType(local[i]);
                        }
                        this.stackMapTableEntries.putShort(numStack);
                        for (int i = 0; i < numStack; i++) {
                            this.putFrameType(stack[i]);
                        }
                        break;
                    case 1:
                        this.currentLocals += numLocal;
                        this.stackMapTableEntries.putByte(251 + numLocal).putShort(offsetDelta);
                        for (int i = 0; i < numLocal; i++) {
                            this.putFrameType(local[i]);
                        }
                        break;
                    case 2:
                        this.currentLocals -= numLocal;
                        this.stackMapTableEntries.putByte(251 - numLocal).putShort(offsetDelta);
                        break;
                    case 3:
                        if (offsetDelta < 64) {
                            this.stackMapTableEntries.putByte(offsetDelta);
                        } else {
                            this.stackMapTableEntries.putByte(251).putShort(offsetDelta);
                        }
                        break;
                    case 4:
                        if (offsetDelta < 64) {
                            this.stackMapTableEntries.putByte(64 + offsetDelta);
                        } else {
                            this.stackMapTableEntries.putByte(247).putShort(offsetDelta);
                        }
                        this.putFrameType(stack[0]);
                        break;
                    default:
                        throw new IllegalArgumentException();
                }
                this.previousFrameOffset = this.code.length;
                this.stackMapTableNumberOfEntries++;
            }
            if (this.compute == 2) {
                this.relativeStackSize = numStack;
                for (int i = 0; i < numStack; i++) {
                    if (stack[i] == Opcodes.LONG || stack[i] == Opcodes.DOUBLE) {
                        this.relativeStackSize++;
                    }
                }
                if (this.relativeStackSize > this.maxRelativeStackSize) {
                    this.maxRelativeStackSize = this.relativeStackSize;
                }
            }
            this.maxStack = Math.max(this.maxStack, numStack);
            this.maxLocals = Math.max(this.maxLocals, this.currentLocals);
        }
    }

    @Override
    public void visitInsn(int opcode) {
        this.lastBytecodeOffset = this.code.length;
        this.code.putByte(opcode);
        if (this.currentBasicBlock != null) {
            if (this.compute != 4 && this.compute != 3) {
                int size = this.relativeStackSize + STACK_SIZE_DELTA[opcode];
                if (size > this.maxRelativeStackSize) {
                    this.maxRelativeStackSize = size;
                }
                this.relativeStackSize = size;
            } else {
                this.currentBasicBlock.frame.execute(opcode, 0, null, null);
            }
            if (opcode >= 172 && opcode <= 177 || opcode == 191) {
                this.endCurrentBasicBlockWithNoSuccessor();
            }
        }
    }

    @Override
    public void visitIntInsn(int opcode, int operand) {
        this.lastBytecodeOffset = this.code.length;
        if (opcode == 17) {
            this.code.put12(opcode, operand);
        } else {
            this.code.put11(opcode, operand);
        }
        if (this.currentBasicBlock != null) {
            if (this.compute == 4 || this.compute == 3) {
                this.currentBasicBlock.frame.execute(opcode, operand, null, null);
            } else if (opcode != 188) {
                int size = this.relativeStackSize + 1;
                if (size > this.maxRelativeStackSize) {
                    this.maxRelativeStackSize = size;
                }
                this.relativeStackSize = size;
            }
        }
    }

    @Override
    public void visitVarInsn(int opcode, int var) {
        this.lastBytecodeOffset = this.code.length;
        if (var < 4 && opcode != 169) {
            int optimizedOpcode;
            if (opcode < 54) {
                optimizedOpcode = 26 + (opcode - 21 << 2) + var;
            } else {
                optimizedOpcode = 59 + (opcode - 54 << 2) + var;
            }
            this.code.putByte(optimizedOpcode);
        } else if (var >= 256) {
            this.code.putByte(196).put12(opcode, var);
        } else {
            this.code.put11(opcode, var);
        }
        if (this.currentBasicBlock != null) {
            if (this.compute == 4 || this.compute == 3) {
                this.currentBasicBlock.frame.execute(opcode, var, null, null);
            } else if (opcode == 169) {
                this.currentBasicBlock.flags = (short) (this.currentBasicBlock.flags | 64);
                this.currentBasicBlock.outputStackSize = (short) this.relativeStackSize;
                this.endCurrentBasicBlockWithNoSuccessor();
            } else {
                int size = this.relativeStackSize + STACK_SIZE_DELTA[opcode];
                if (size > this.maxRelativeStackSize) {
                    this.maxRelativeStackSize = size;
                }
                this.relativeStackSize = size;
            }
        }
        if (this.compute != 0) {
            int currentMaxLocals;
            if (opcode != 22 && opcode != 24 && opcode != 55 && opcode != 57) {
                currentMaxLocals = var + 1;
            } else {
                currentMaxLocals = var + 2;
            }
            if (currentMaxLocals > this.maxLocals) {
                this.maxLocals = currentMaxLocals;
            }
        }
        if (opcode >= 54 && this.compute == 4 && this.firstHandler != null) {
            this.visitLabel(new Label());
        }
    }

    @Override
    public void visitTypeInsn(int opcode, String type) {
        this.lastBytecodeOffset = this.code.length;
        Symbol typeSymbol = this.symbolTable.addConstantClass(type);
        this.code.put12(opcode, typeSymbol.index);
        if (this.currentBasicBlock != null) {
            if (this.compute == 4 || this.compute == 3) {
                this.currentBasicBlock.frame.execute(opcode, this.lastBytecodeOffset, typeSymbol, this.symbolTable);
            } else if (opcode == 187) {
                int size = this.relativeStackSize + 1;
                if (size > this.maxRelativeStackSize) {
                    this.maxRelativeStackSize = size;
                }
                this.relativeStackSize = size;
            }
        }
    }

    @Override
    public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
        this.lastBytecodeOffset = this.code.length;
        Symbol fieldrefSymbol = this.symbolTable.addConstantFieldref(owner, name, descriptor);
        this.code.put12(opcode, fieldrefSymbol.index);
        if (this.currentBasicBlock != null) {
            if (this.compute != 4 && this.compute != 3) {
                int size;
                label72: {
                    char firstDescChar = descriptor.charAt(0);
                    switch(opcode) {
                        case 178:
                            size = this.relativeStackSize + (firstDescChar != 'D' && firstDescChar != 'J' ? 1 : 2);
                            break label72;
                        case 179:
                            size = this.relativeStackSize + (firstDescChar != 'D' && firstDescChar != 'J' ? -1 : -2);
                            break label72;
                        case 180:
                            size = this.relativeStackSize + (firstDescChar != 'D' && firstDescChar != 'J' ? 0 : 1);
                            break label72;
                        case 181:
                    }
                    size = this.relativeStackSize + (firstDescChar != 'D' && firstDescChar != 'J' ? -2 : -3);
                }
                if (size > this.maxRelativeStackSize) {
                    this.maxRelativeStackSize = size;
                }
                this.relativeStackSize = size;
            } else {
                this.currentBasicBlock.frame.execute(opcode, 0, fieldrefSymbol, this.symbolTable);
            }
        }
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
        this.lastBytecodeOffset = this.code.length;
        Symbol methodrefSymbol = this.symbolTable.addConstantMethodref(owner, name, descriptor, isInterface);
        if (opcode == 185) {
            this.code.put12(185, methodrefSymbol.index).put11(methodrefSymbol.getArgumentsAndReturnSizes() >> 2, 0);
        } else {
            this.code.put12(opcode, methodrefSymbol.index);
        }
        if (this.currentBasicBlock != null) {
            if (this.compute != 4 && this.compute != 3) {
                int argumentsAndReturnSize = methodrefSymbol.getArgumentsAndReturnSizes();
                int stackSizeDelta = (argumentsAndReturnSize & 3) - (argumentsAndReturnSize >> 2);
                int size;
                if (opcode == 184) {
                    size = this.relativeStackSize + stackSizeDelta + 1;
                } else {
                    size = this.relativeStackSize + stackSizeDelta;
                }
                if (size > this.maxRelativeStackSize) {
                    this.maxRelativeStackSize = size;
                }
                this.relativeStackSize = size;
            } else {
                this.currentBasicBlock.frame.execute(opcode, 0, methodrefSymbol, this.symbolTable);
            }
        }
    }

    @Override
    public void visitInvokeDynamicInsn(String name, String descriptor, Handle bootstrapMethodHandle, Object... bootstrapMethodArguments) {
        this.lastBytecodeOffset = this.code.length;
        Symbol invokeDynamicSymbol = this.symbolTable.addConstantInvokeDynamic(name, descriptor, bootstrapMethodHandle, bootstrapMethodArguments);
        this.code.put12(186, invokeDynamicSymbol.index);
        this.code.putShort(0);
        if (this.currentBasicBlock != null) {
            if (this.compute != 4 && this.compute != 3) {
                int argumentsAndReturnSize = invokeDynamicSymbol.getArgumentsAndReturnSizes();
                int stackSizeDelta = (argumentsAndReturnSize & 3) - (argumentsAndReturnSize >> 2) + 1;
                int size = this.relativeStackSize + stackSizeDelta;
                if (size > this.maxRelativeStackSize) {
                    this.maxRelativeStackSize = size;
                }
                this.relativeStackSize = size;
            } else {
                this.currentBasicBlock.frame.execute(186, 0, invokeDynamicSymbol, this.symbolTable);
            }
        }
    }

    @Override
    public void visitJumpInsn(int opcode, Label label) {
        this.lastBytecodeOffset = this.code.length;
        int baseOpcode = opcode >= 200 ? opcode - 33 : opcode;
        boolean nextInsnIsJumpTarget = false;
        if ((label.flags & 4) != 0 && label.bytecodeOffset - this.code.length < -32768) {
            if (baseOpcode == 167) {
                this.code.putByte(200);
            } else if (baseOpcode == 168) {
                this.code.putByte(201);
            } else {
                this.code.putByte(baseOpcode >= 198 ? baseOpcode ^ 1 : (baseOpcode + 1 ^ 1) - 1);
                this.code.putShort(8);
                this.code.putByte(220);
                this.hasAsmInstructions = true;
                nextInsnIsJumpTarget = true;
            }
            label.put(this.code, this.code.length - 1, true);
        } else if (baseOpcode != opcode) {
            this.code.putByte(opcode);
            label.put(this.code, this.code.length - 1, true);
        } else {
            this.code.putByte(baseOpcode);
            label.put(this.code, this.code.length - 1, false);
        }
        if (this.currentBasicBlock != null) {
            Label nextBasicBlock = null;
            if (this.compute == 4) {
                this.currentBasicBlock.frame.execute(baseOpcode, 0, null, null);
                Label var10000 = label.getCanonicalInstance();
                var10000.flags = (short) (var10000.flags | 2);
                this.addSuccessorToCurrentBasicBlock(0, label);
                if (baseOpcode != 167) {
                    nextBasicBlock = new Label();
                }
            } else if (this.compute == 3) {
                this.currentBasicBlock.frame.execute(baseOpcode, 0, null, null);
            } else if (this.compute == 2) {
                this.relativeStackSize = this.relativeStackSize + STACK_SIZE_DELTA[baseOpcode];
            } else if (baseOpcode == 168) {
                if ((label.flags & 32) == 0) {
                    label.flags = (short) (label.flags | 32);
                    this.hasSubroutines = true;
                }
                this.currentBasicBlock.flags = (short) (this.currentBasicBlock.flags | 16);
                this.addSuccessorToCurrentBasicBlock(this.relativeStackSize + 1, label);
                nextBasicBlock = new Label();
            } else {
                this.relativeStackSize = this.relativeStackSize + STACK_SIZE_DELTA[baseOpcode];
                this.addSuccessorToCurrentBasicBlock(this.relativeStackSize, label);
            }
            if (nextBasicBlock != null) {
                if (nextInsnIsJumpTarget) {
                    nextBasicBlock.flags = (short) (nextBasicBlock.flags | 2);
                }
                this.visitLabel(nextBasicBlock);
            }
            if (baseOpcode == 167) {
                this.endCurrentBasicBlockWithNoSuccessor();
            }
        }
    }

    @Override
    public void visitLabel(Label label) {
        this.hasAsmInstructions = this.hasAsmInstructions | label.resolve(this.code.data, this.code.length);
        if ((label.flags & 1) == 0) {
            if (this.compute == 4) {
                if (this.currentBasicBlock != null) {
                    if (label.bytecodeOffset == this.currentBasicBlock.bytecodeOffset) {
                        this.currentBasicBlock.flags = (short) (this.currentBasicBlock.flags | label.flags & 2);
                        label.frame = this.currentBasicBlock.frame;
                        return;
                    }
                    this.addSuccessorToCurrentBasicBlock(0, label);
                }
                if (this.lastBasicBlock != null) {
                    if (label.bytecodeOffset == this.lastBasicBlock.bytecodeOffset) {
                        this.lastBasicBlock.flags = (short) (this.lastBasicBlock.flags | label.flags & 2);
                        label.frame = this.lastBasicBlock.frame;
                        this.currentBasicBlock = this.lastBasicBlock;
                        return;
                    }
                    this.lastBasicBlock.nextBasicBlock = label;
                }
                this.lastBasicBlock = label;
                this.currentBasicBlock = label;
                label.frame = new Frame(label);
            } else if (this.compute == 3) {
                if (this.currentBasicBlock == null) {
                    this.currentBasicBlock = label;
                } else {
                    this.currentBasicBlock.frame.owner = label;
                }
            } else if (this.compute == 1) {
                if (this.currentBasicBlock != null) {
                    this.currentBasicBlock.outputStackMax = (short) this.maxRelativeStackSize;
                    this.addSuccessorToCurrentBasicBlock(this.relativeStackSize, label);
                }
                this.currentBasicBlock = label;
                this.relativeStackSize = 0;
                this.maxRelativeStackSize = 0;
                if (this.lastBasicBlock != null) {
                    this.lastBasicBlock.nextBasicBlock = label;
                }
                this.lastBasicBlock = label;
            } else if (this.compute == 2 && this.currentBasicBlock == null) {
                this.currentBasicBlock = label;
            }
        }
    }

    @Override
    public void visitLdcInsn(Object value) {
        this.lastBytecodeOffset = this.code.length;
        Symbol constantSymbol = this.symbolTable.addConstant(value);
        int constantIndex = constantSymbol.index;
        char firstDescriptorChar;
        boolean isLongOrDouble = constantSymbol.tag == 5 || constantSymbol.tag == 6 || constantSymbol.tag == 17 && ((firstDescriptorChar = constantSymbol.value.charAt(0)) == 'J' || firstDescriptorChar == 'D');
        if (isLongOrDouble) {
            this.code.put12(20, constantIndex);
        } else if (constantIndex >= 256) {
            this.code.put12(19, constantIndex);
        } else {
            this.code.put11(18, constantIndex);
        }
        if (this.currentBasicBlock != null) {
            if (this.compute != 4 && this.compute != 3) {
                int size = this.relativeStackSize + (isLongOrDouble ? 2 : 1);
                if (size > this.maxRelativeStackSize) {
                    this.maxRelativeStackSize = size;
                }
                this.relativeStackSize = size;
            } else {
                this.currentBasicBlock.frame.execute(18, 0, constantSymbol, this.symbolTable);
            }
        }
    }

    @Override
    public void visitIincInsn(int var, int increment) {
        this.lastBytecodeOffset = this.code.length;
        if (var <= 255 && increment <= 127 && increment >= -128) {
            this.code.putByte(132).put11(var, increment);
        } else {
            this.code.putByte(196).put12(132, var).putShort(increment);
        }
        if (this.currentBasicBlock != null && (this.compute == 4 || this.compute == 3)) {
            this.currentBasicBlock.frame.execute(132, var, null, null);
        }
        if (this.compute != 0) {
            int currentMaxLocals = var + 1;
            if (currentMaxLocals > this.maxLocals) {
                this.maxLocals = currentMaxLocals;
            }
        }
    }

    @Override
    public void visitTableSwitchInsn(int min, int max, Label dflt, Label... labels) {
        this.lastBytecodeOffset = this.code.length;
        this.code.putByte(170).putByteArray(null, 0, (4 - this.code.length % 4) % 4);
        dflt.put(this.code, this.lastBytecodeOffset, true);
        this.code.putInt(min).putInt(max);
        for (Label label : labels) {
            label.put(this.code, this.lastBytecodeOffset, true);
        }
        this.visitSwitchInsn(dflt, labels);
    }

    @Override
    public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
        this.lastBytecodeOffset = this.code.length;
        this.code.putByte(171).putByteArray(null, 0, (4 - this.code.length % 4) % 4);
        dflt.put(this.code, this.lastBytecodeOffset, true);
        this.code.putInt(labels.length);
        for (int i = 0; i < labels.length; i++) {
            this.code.putInt(keys[i]);
            labels[i].put(this.code, this.lastBytecodeOffset, true);
        }
        this.visitSwitchInsn(dflt, labels);
    }

    private void visitSwitchInsn(Label dflt, Label[] labels) {
        if (this.currentBasicBlock != null) {
            if (this.compute == 4) {
                this.currentBasicBlock.frame.execute(171, 0, null, null);
                this.addSuccessorToCurrentBasicBlock(0, dflt);
                Label var10000 = dflt.getCanonicalInstance();
                var10000.flags = (short) (var10000.flags | 2);
                for (Label label : labels) {
                    this.addSuccessorToCurrentBasicBlock(0, label);
                    var10000 = label.getCanonicalInstance();
                    var10000.flags = (short) (var10000.flags | 2);
                }
            } else if (this.compute == 1) {
                this.relativeStackSize--;
                this.addSuccessorToCurrentBasicBlock(this.relativeStackSize, dflt);
                for (Label label : labels) {
                    this.addSuccessorToCurrentBasicBlock(this.relativeStackSize, label);
                }
            }
            this.endCurrentBasicBlockWithNoSuccessor();
        }
    }

    @Override
    public void visitMultiANewArrayInsn(String descriptor, int numDimensions) {
        this.lastBytecodeOffset = this.code.length;
        Symbol descSymbol = this.symbolTable.addConstantClass(descriptor);
        this.code.put12(197, descSymbol.index).putByte(numDimensions);
        if (this.currentBasicBlock != null) {
            if (this.compute != 4 && this.compute != 3) {
                this.relativeStackSize += 1 - numDimensions;
            } else {
                this.currentBasicBlock.frame.execute(197, numDimensions, descSymbol, this.symbolTable);
            }
        }
    }

    @Override
    public AnnotationVisitor visitInsnAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        return visible ? (this.lastCodeRuntimeVisibleTypeAnnotation = AnnotationWriter.create(this.symbolTable, typeRef & -16776961 | this.lastBytecodeOffset << 8, typePath, descriptor, this.lastCodeRuntimeVisibleTypeAnnotation)) : (this.lastCodeRuntimeInvisibleTypeAnnotation = AnnotationWriter.create(this.symbolTable, typeRef & -16776961 | this.lastBytecodeOffset << 8, typePath, descriptor, this.lastCodeRuntimeInvisibleTypeAnnotation));
    }

    @Override
    public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
        Handler newHandler = new Handler(start, end, handler, type != null ? this.symbolTable.addConstantClass(type).index : 0, type);
        if (this.firstHandler == null) {
            this.firstHandler = newHandler;
        } else {
            this.lastHandler.nextHandler = newHandler;
        }
        this.lastHandler = newHandler;
    }

    @Override
    public AnnotationVisitor visitTryCatchAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        return visible ? (this.lastCodeRuntimeVisibleTypeAnnotation = AnnotationWriter.create(this.symbolTable, typeRef, typePath, descriptor, this.lastCodeRuntimeVisibleTypeAnnotation)) : (this.lastCodeRuntimeInvisibleTypeAnnotation = AnnotationWriter.create(this.symbolTable, typeRef, typePath, descriptor, this.lastCodeRuntimeInvisibleTypeAnnotation));
    }

    @Override
    public void visitLocalVariable(String name, String descriptor, String signature, Label start, Label end, int index) {
        if (signature != null) {
            if (this.localVariableTypeTable == null) {
                this.localVariableTypeTable = new ByteVector();
            }
            this.localVariableTypeTableLength++;
            this.localVariableTypeTable.putShort(start.bytecodeOffset).putShort(end.bytecodeOffset - start.bytecodeOffset).putShort(this.symbolTable.addConstantUtf8(name)).putShort(this.symbolTable.addConstantUtf8(signature)).putShort(index);
        }
        if (this.localVariableTable == null) {
            this.localVariableTable = new ByteVector();
        }
        this.localVariableTableLength++;
        this.localVariableTable.putShort(start.bytecodeOffset).putShort(end.bytecodeOffset - start.bytecodeOffset).putShort(this.symbolTable.addConstantUtf8(name)).putShort(this.symbolTable.addConstantUtf8(descriptor)).putShort(index);
        if (this.compute != 0) {
            char firstDescChar = descriptor.charAt(0);
            int currentMaxLocals = index + (firstDescChar != 'J' && firstDescChar != 'D' ? 1 : 2);
            if (currentMaxLocals > this.maxLocals) {
                this.maxLocals = currentMaxLocals;
            }
        }
    }

    @Override
    public AnnotationVisitor visitLocalVariableAnnotation(int typeRef, TypePath typePath, Label[] start, Label[] end, int[] index, String descriptor, boolean visible) {
        ByteVector typeAnnotation = new ByteVector();
        typeAnnotation.putByte(typeRef >>> 24).putShort(start.length);
        for (int i = 0; i < start.length; i++) {
            typeAnnotation.putShort(start[i].bytecodeOffset).putShort(end[i].bytecodeOffset - start[i].bytecodeOffset).putShort(index[i]);
        }
        TypePath.put(typePath, typeAnnotation);
        typeAnnotation.putShort(this.symbolTable.addConstantUtf8(descriptor)).putShort(0);
        return visible ? (this.lastCodeRuntimeVisibleTypeAnnotation = new AnnotationWriter(this.symbolTable, true, typeAnnotation, this.lastCodeRuntimeVisibleTypeAnnotation)) : (this.lastCodeRuntimeInvisibleTypeAnnotation = new AnnotationWriter(this.symbolTable, true, typeAnnotation, this.lastCodeRuntimeInvisibleTypeAnnotation));
    }

    @Override
    public void visitLineNumber(int line, Label start) {
        if (this.lineNumberTable == null) {
            this.lineNumberTable = new ByteVector();
        }
        this.lineNumberTableLength++;
        this.lineNumberTable.putShort(start.bytecodeOffset);
        this.lineNumberTable.putShort(line);
    }

    @Override
    public void visitMaxs(int maxStack, int maxLocals) {
        if (this.compute == 4) {
            this.computeAllFrames();
        } else if (this.compute == 1) {
            this.computeMaxStackAndLocal();
        } else if (this.compute == 2) {
            this.maxStack = this.maxRelativeStackSize;
        } else {
            this.maxStack = maxStack;
            this.maxLocals = maxLocals;
        }
    }

    private void computeAllFrames() {
        for (Handler handler = this.firstHandler; handler != null; handler = handler.nextHandler) {
            String catchTypeDescriptor = handler.catchTypeDescriptor == null ? "java/lang/Throwable" : handler.catchTypeDescriptor;
            int catchType = Frame.getAbstractTypeFromInternalName(this.symbolTable, catchTypeDescriptor);
            Label handlerBlock = handler.handlerPc.getCanonicalInstance();
            handlerBlock.flags = (short) (handlerBlock.flags | 2);
            Label handlerRangeBlock = handler.startPc.getCanonicalInstance();
            for (Label handlerRangeEnd = handler.endPc.getCanonicalInstance(); handlerRangeBlock != handlerRangeEnd; handlerRangeBlock = handlerRangeBlock.nextBasicBlock) {
                handlerRangeBlock.outgoingEdges = new Edge(catchType, handlerBlock, handlerRangeBlock.outgoingEdges);
            }
        }
        Frame firstFrame = this.firstBasicBlock.frame;
        firstFrame.setInputFrameFromDescriptor(this.symbolTable, this.accessFlags, this.descriptor, this.maxLocals);
        firstFrame.accept(this);
        Label listOfBlocksToProcess = this.firstBasicBlock;
        listOfBlocksToProcess.nextListElement = Label.EMPTY_LIST;
        int maxStackSize = 0;
        while (listOfBlocksToProcess != Label.EMPTY_LIST) {
            Label basicBlock = listOfBlocksToProcess;
            listOfBlocksToProcess = listOfBlocksToProcess.nextListElement;
            basicBlock.nextListElement = null;
            basicBlock.flags = (short) (basicBlock.flags | 8);
            int maxBlockStackSize = basicBlock.frame.getInputStackSize() + basicBlock.outputStackMax;
            if (maxBlockStackSize > maxStackSize) {
                maxStackSize = maxBlockStackSize;
            }
            for (Edge outgoingEdge = basicBlock.outgoingEdges; outgoingEdge != null; outgoingEdge = outgoingEdge.nextEdge) {
                Label successorBlock = outgoingEdge.successor.getCanonicalInstance();
                boolean successorBlockChanged = basicBlock.frame.merge(this.symbolTable, successorBlock.frame, outgoingEdge.info);
                if (successorBlockChanged && successorBlock.nextListElement == null) {
                    successorBlock.nextListElement = listOfBlocksToProcess;
                    listOfBlocksToProcess = successorBlock;
                }
            }
        }
        for (Label basicBlock = this.firstBasicBlock; basicBlock != null; basicBlock = basicBlock.nextBasicBlock) {
            if ((basicBlock.flags & 10) == 10) {
                basicBlock.frame.accept(this);
            }
            if ((basicBlock.flags & 8) == 0) {
                Label nextBasicBlock = basicBlock.nextBasicBlock;
                int startOffset = basicBlock.bytecodeOffset;
                int endOffset = (nextBasicBlock == null ? this.code.length : nextBasicBlock.bytecodeOffset) - 1;
                if (endOffset >= startOffset) {
                    for (int i = startOffset; i < endOffset; i++) {
                        this.code.data[i] = 0;
                    }
                    this.code.data[endOffset] = -65;
                    int frameIndex = this.visitFrameStart(startOffset, 0, 1);
                    this.currentFrame[frameIndex] = Frame.getAbstractTypeFromInternalName(this.symbolTable, "java/lang/Throwable");
                    this.visitFrameEnd();
                    this.firstHandler = Handler.removeRange(this.firstHandler, basicBlock, nextBasicBlock);
                    maxStackSize = Math.max(maxStackSize, 1);
                }
            }
        }
        this.maxStack = maxStackSize;
    }

    private void computeMaxStackAndLocal() {
        for (Handler handler = this.firstHandler; handler != null; handler = handler.nextHandler) {
            Label handlerBlock = handler.handlerPc;
            Label handlerRangeBlock = handler.startPc;
            for (Label handlerRangeEnd = handler.endPc; handlerRangeBlock != handlerRangeEnd; handlerRangeBlock = handlerRangeBlock.nextBasicBlock) {
                if ((handlerRangeBlock.flags & 16) == 0) {
                    handlerRangeBlock.outgoingEdges = new Edge(Integer.MAX_VALUE, handlerBlock, handlerRangeBlock.outgoingEdges);
                } else {
                    handlerRangeBlock.outgoingEdges.nextEdge.nextEdge = new Edge(Integer.MAX_VALUE, handlerBlock, handlerRangeBlock.outgoingEdges.nextEdge.nextEdge);
                }
            }
        }
        if (this.hasSubroutines) {
            short numSubroutines = 1;
            this.firstBasicBlock.markSubroutine(numSubroutines);
            for (short currentSubroutine = 1; currentSubroutine <= numSubroutines; currentSubroutine++) {
                for (Label basicBlock = this.firstBasicBlock; basicBlock != null; basicBlock = basicBlock.nextBasicBlock) {
                    if ((basicBlock.flags & 16) != 0 && basicBlock.subroutineId == currentSubroutine) {
                        Label jsrTarget = basicBlock.outgoingEdges.nextEdge.successor;
                        if (jsrTarget.subroutineId == 0) {
                            jsrTarget.markSubroutine(++numSubroutines);
                        }
                    }
                }
            }
            for (Label basicBlockx = this.firstBasicBlock; basicBlockx != null; basicBlockx = basicBlockx.nextBasicBlock) {
                if ((basicBlockx.flags & 16) != 0) {
                    Label subroutine = basicBlockx.outgoingEdges.nextEdge.successor;
                    subroutine.addSubroutineRetSuccessors(basicBlockx);
                }
            }
        }
        Label listOfBlocksToProcess = this.firstBasicBlock;
        listOfBlocksToProcess.nextListElement = Label.EMPTY_LIST;
        int maxStackSize = this.maxStack;
        while (listOfBlocksToProcess != Label.EMPTY_LIST) {
            Label basicBlockxx = listOfBlocksToProcess;
            listOfBlocksToProcess = listOfBlocksToProcess.nextListElement;
            int inputStackTop = basicBlockxx.inputStackSize;
            int maxBlockStackSize = inputStackTop + basicBlockxx.outputStackMax;
            if (maxBlockStackSize > maxStackSize) {
                maxStackSize = maxBlockStackSize;
            }
            Edge outgoingEdge = basicBlockxx.outgoingEdges;
            if ((basicBlockxx.flags & 16) != 0) {
                outgoingEdge = outgoingEdge.nextEdge;
            }
            for (; outgoingEdge != null; outgoingEdge = outgoingEdge.nextEdge) {
                Label successorBlock = outgoingEdge.successor;
                if (successorBlock.nextListElement == null) {
                    successorBlock.inputStackSize = (short) (outgoingEdge.info == Integer.MAX_VALUE ? 1 : inputStackTop + outgoingEdge.info);
                    successorBlock.nextListElement = listOfBlocksToProcess;
                    listOfBlocksToProcess = successorBlock;
                }
            }
        }
        this.maxStack = maxStackSize;
    }

    @Override
    public void visitEnd() {
    }

    private void addSuccessorToCurrentBasicBlock(int info, Label successor) {
        this.currentBasicBlock.outgoingEdges = new Edge(info, successor, this.currentBasicBlock.outgoingEdges);
    }

    private void endCurrentBasicBlockWithNoSuccessor() {
        if (this.compute == 4) {
            Label nextBasicBlock = new Label();
            nextBasicBlock.frame = new Frame(nextBasicBlock);
            nextBasicBlock.resolve(this.code.data, this.code.length);
            this.lastBasicBlock.nextBasicBlock = nextBasicBlock;
            this.lastBasicBlock = nextBasicBlock;
            this.currentBasicBlock = null;
        } else if (this.compute == 1) {
            this.currentBasicBlock.outputStackMax = (short) this.maxRelativeStackSize;
            this.currentBasicBlock = null;
        }
    }

    int visitFrameStart(int offset, int numLocal, int numStack) {
        int frameLength = 3 + numLocal + numStack;
        if (this.currentFrame == null || this.currentFrame.length < frameLength) {
            this.currentFrame = new int[frameLength];
        }
        this.currentFrame[0] = offset;
        this.currentFrame[1] = numLocal;
        this.currentFrame[2] = numStack;
        return 3;
    }

    void visitAbstractType(int frameIndex, int abstractType) {
        this.currentFrame[frameIndex] = abstractType;
    }

    void visitFrameEnd() {
        if (this.previousFrame != null) {
            if (this.stackMapTableEntries == null) {
                this.stackMapTableEntries = new ByteVector();
            }
            this.putFrame();
            this.stackMapTableNumberOfEntries++;
        }
        this.previousFrame = this.currentFrame;
        this.currentFrame = null;
    }

    private void putFrame() {
        int numLocal = this.currentFrame[1];
        int numStack = this.currentFrame[2];
        if (this.symbolTable.getMajorVersion() < 50) {
            this.stackMapTableEntries.putShort(this.currentFrame[0]).putShort(numLocal);
            this.putAbstractTypes(3, 3 + numLocal);
            this.stackMapTableEntries.putShort(numStack);
            this.putAbstractTypes(3 + numLocal, 3 + numLocal + numStack);
        } else {
            int offsetDelta = this.stackMapTableNumberOfEntries == 0 ? this.currentFrame[0] : this.currentFrame[0] - this.previousFrame[0] - 1;
            int previousNumlocal = this.previousFrame[1];
            int numLocalDelta = numLocal - previousNumlocal;
            int type = 255;
            if (numStack == 0) {
                switch(numLocalDelta) {
                    case -3:
                    case -2:
                    case -1:
                        type = 248;
                        break;
                    case 0:
                        type = offsetDelta < 64 ? 0 : 251;
                        break;
                    case 1:
                    case 2:
                    case 3:
                        type = 252;
                }
            } else if (numLocalDelta == 0 && numStack == 1) {
                type = offsetDelta < 63 ? 64 : 247;
            }
            if (type != 255) {
                int frameIndex = 3;
                for (int i = 0; i < previousNumlocal && i < numLocal; i++) {
                    if (this.currentFrame[frameIndex] != this.previousFrame[frameIndex]) {
                        type = 255;
                        break;
                    }
                    frameIndex++;
                }
            }
            switch(type) {
                case 0:
                    this.stackMapTableEntries.putByte(offsetDelta);
                    break;
                case 64:
                    this.stackMapTableEntries.putByte(64 + offsetDelta);
                    this.putAbstractTypes(3 + numLocal, 4 + numLocal);
                    break;
                case 247:
                    this.stackMapTableEntries.putByte(247).putShort(offsetDelta);
                    this.putAbstractTypes(3 + numLocal, 4 + numLocal);
                    break;
                case 248:
                    this.stackMapTableEntries.putByte(251 + numLocalDelta).putShort(offsetDelta);
                    break;
                case 251:
                    this.stackMapTableEntries.putByte(251).putShort(offsetDelta);
                    break;
                case 252:
                    this.stackMapTableEntries.putByte(251 + numLocalDelta).putShort(offsetDelta);
                    this.putAbstractTypes(3 + previousNumlocal, 3 + numLocal);
                    break;
                case 255:
                default:
                    this.stackMapTableEntries.putByte(255).putShort(offsetDelta).putShort(numLocal);
                    this.putAbstractTypes(3, 3 + numLocal);
                    this.stackMapTableEntries.putShort(numStack);
                    this.putAbstractTypes(3 + numLocal, 3 + numLocal + numStack);
            }
        }
    }

    private void putAbstractTypes(int start, int end) {
        for (int i = start; i < end; i++) {
            Frame.putAbstractType(this.symbolTable, this.currentFrame[i], this.stackMapTableEntries);
        }
    }

    private void putFrameType(Object type) {
        if (type instanceof Integer) {
            this.stackMapTableEntries.putByte((Integer) type);
        } else if (type instanceof String) {
            this.stackMapTableEntries.putByte(7).putShort(this.symbolTable.addConstantClass((String) type).index);
        } else {
            this.stackMapTableEntries.putByte(8).putShort(((Label) type).bytecodeOffset);
        }
    }

    boolean canCopyMethodAttributes(ClassReader source, boolean hasSyntheticAttribute, boolean hasDeprecatedAttribute, int descriptorIndex, int signatureIndex, int exceptionsOffset) {
        if (source == this.symbolTable.getSource() && descriptorIndex == this.descriptorIndex && signatureIndex == this.signatureIndex && hasDeprecatedAttribute == ((this.accessFlags & 131072) != 0)) {
            boolean needSyntheticAttribute = this.symbolTable.getMajorVersion() < 49 && (this.accessFlags & 4096) != 0;
            if (hasSyntheticAttribute != needSyntheticAttribute) {
                return false;
            } else {
                if (exceptionsOffset == 0) {
                    if (this.numberOfExceptions != 0) {
                        return false;
                    }
                } else if (source.readUnsignedShort(exceptionsOffset) == this.numberOfExceptions) {
                    int currentExceptionOffset = exceptionsOffset + 2;
                    for (int i = 0; i < this.numberOfExceptions; i++) {
                        if (source.readUnsignedShort(currentExceptionOffset) != this.exceptionIndexTable[i]) {
                            return false;
                        }
                        currentExceptionOffset += 2;
                    }
                }
                return true;
            }
        } else {
            return false;
        }
    }

    void setMethodAttributesSource(int methodInfoOffset, int methodInfoLength) {
        this.sourceOffset = methodInfoOffset + 6;
        this.sourceLength = methodInfoLength - 6;
    }

    int computeMethodInfoSize() {
        if (this.sourceOffset != 0) {
            return 6 + this.sourceLength;
        } else {
            int size = 8;
            if (this.code.length > 0) {
                if (this.code.length > 65535) {
                    throw new MethodTooLargeException(this.symbolTable.getClassName(), this.name, this.descriptor, this.code.length);
                }
                this.symbolTable.addConstantUtf8("Code");
                size += 16 + this.code.length + Handler.getExceptionTableSize(this.firstHandler);
                if (this.stackMapTableEntries != null) {
                    boolean useStackMapTable = this.symbolTable.getMajorVersion() >= 50;
                    this.symbolTable.addConstantUtf8(useStackMapTable ? "StackMapTable" : "StackMap");
                    size += 8 + this.stackMapTableEntries.length;
                }
                if (this.lineNumberTable != null) {
                    this.symbolTable.addConstantUtf8("LineNumberTable");
                    size += 8 + this.lineNumberTable.length;
                }
                if (this.localVariableTable != null) {
                    this.symbolTable.addConstantUtf8("LocalVariableTable");
                    size += 8 + this.localVariableTable.length;
                }
                if (this.localVariableTypeTable != null) {
                    this.symbolTable.addConstantUtf8("LocalVariableTypeTable");
                    size += 8 + this.localVariableTypeTable.length;
                }
                if (this.lastCodeRuntimeVisibleTypeAnnotation != null) {
                    size += this.lastCodeRuntimeVisibleTypeAnnotation.computeAnnotationsSize("RuntimeVisibleTypeAnnotations");
                }
                if (this.lastCodeRuntimeInvisibleTypeAnnotation != null) {
                    size += this.lastCodeRuntimeInvisibleTypeAnnotation.computeAnnotationsSize("RuntimeInvisibleTypeAnnotations");
                }
                if (this.firstCodeAttribute != null) {
                    size += this.firstCodeAttribute.computeAttributesSize(this.symbolTable, this.code.data, this.code.length, this.maxStack, this.maxLocals);
                }
            }
            if (this.numberOfExceptions > 0) {
                this.symbolTable.addConstantUtf8("Exceptions");
                size += 8 + 2 * this.numberOfExceptions;
            }
            size += Attribute.computeAttributesSize(this.symbolTable, this.accessFlags, this.signatureIndex);
            size += AnnotationWriter.computeAnnotationsSize(this.lastRuntimeVisibleAnnotation, this.lastRuntimeInvisibleAnnotation, this.lastRuntimeVisibleTypeAnnotation, this.lastRuntimeInvisibleTypeAnnotation);
            if (this.lastRuntimeVisibleParameterAnnotations != null) {
                size += AnnotationWriter.computeParameterAnnotationsSize("RuntimeVisibleParameterAnnotations", this.lastRuntimeVisibleParameterAnnotations, this.visibleAnnotableParameterCount == 0 ? this.lastRuntimeVisibleParameterAnnotations.length : this.visibleAnnotableParameterCount);
            }
            if (this.lastRuntimeInvisibleParameterAnnotations != null) {
                size += AnnotationWriter.computeParameterAnnotationsSize("RuntimeInvisibleParameterAnnotations", this.lastRuntimeInvisibleParameterAnnotations, this.invisibleAnnotableParameterCount == 0 ? this.lastRuntimeInvisibleParameterAnnotations.length : this.invisibleAnnotableParameterCount);
            }
            if (this.defaultValue != null) {
                this.symbolTable.addConstantUtf8("AnnotationDefault");
                size += 6 + this.defaultValue.length;
            }
            if (this.parameters != null) {
                this.symbolTable.addConstantUtf8("MethodParameters");
                size += 7 + this.parameters.length;
            }
            if (this.firstAttribute != null) {
                size += this.firstAttribute.computeAttributesSize(this.symbolTable);
            }
            return size;
        }
    }

    void putMethodInfo(ByteVector output) {
        boolean useSyntheticAttribute = this.symbolTable.getMajorVersion() < 49;
        int mask = useSyntheticAttribute ? 4096 : 0;
        output.putShort(this.accessFlags & ~mask).putShort(this.nameIndex).putShort(this.descriptorIndex);
        if (this.sourceOffset != 0) {
            output.putByteArray(this.symbolTable.getSource().classFileBuffer, this.sourceOffset, this.sourceLength);
        } else {
            int attributeCount = 0;
            if (this.code.length > 0) {
                attributeCount++;
            }
            if (this.numberOfExceptions > 0) {
                attributeCount++;
            }
            if ((this.accessFlags & 4096) != 0 && useSyntheticAttribute) {
                attributeCount++;
            }
            if (this.signatureIndex != 0) {
                attributeCount++;
            }
            if ((this.accessFlags & 131072) != 0) {
                attributeCount++;
            }
            if (this.lastRuntimeVisibleAnnotation != null) {
                attributeCount++;
            }
            if (this.lastRuntimeInvisibleAnnotation != null) {
                attributeCount++;
            }
            if (this.lastRuntimeVisibleParameterAnnotations != null) {
                attributeCount++;
            }
            if (this.lastRuntimeInvisibleParameterAnnotations != null) {
                attributeCount++;
            }
            if (this.lastRuntimeVisibleTypeAnnotation != null) {
                attributeCount++;
            }
            if (this.lastRuntimeInvisibleTypeAnnotation != null) {
                attributeCount++;
            }
            if (this.defaultValue != null) {
                attributeCount++;
            }
            if (this.parameters != null) {
                attributeCount++;
            }
            if (this.firstAttribute != null) {
                attributeCount += this.firstAttribute.getAttributeCount();
            }
            output.putShort(attributeCount);
            if (this.code.length > 0) {
                int size = 10 + this.code.length + Handler.getExceptionTableSize(this.firstHandler);
                int codeAttributeCount = 0;
                if (this.stackMapTableEntries != null) {
                    size += 8 + this.stackMapTableEntries.length;
                    codeAttributeCount++;
                }
                if (this.lineNumberTable != null) {
                    size += 8 + this.lineNumberTable.length;
                    codeAttributeCount++;
                }
                if (this.localVariableTable != null) {
                    size += 8 + this.localVariableTable.length;
                    codeAttributeCount++;
                }
                if (this.localVariableTypeTable != null) {
                    size += 8 + this.localVariableTypeTable.length;
                    codeAttributeCount++;
                }
                if (this.lastCodeRuntimeVisibleTypeAnnotation != null) {
                    size += this.lastCodeRuntimeVisibleTypeAnnotation.computeAnnotationsSize("RuntimeVisibleTypeAnnotations");
                    codeAttributeCount++;
                }
                if (this.lastCodeRuntimeInvisibleTypeAnnotation != null) {
                    size += this.lastCodeRuntimeInvisibleTypeAnnotation.computeAnnotationsSize("RuntimeInvisibleTypeAnnotations");
                    codeAttributeCount++;
                }
                if (this.firstCodeAttribute != null) {
                    size += this.firstCodeAttribute.computeAttributesSize(this.symbolTable, this.code.data, this.code.length, this.maxStack, this.maxLocals);
                    codeAttributeCount += this.firstCodeAttribute.getAttributeCount();
                }
                output.putShort(this.symbolTable.addConstantUtf8("Code")).putInt(size).putShort(this.maxStack).putShort(this.maxLocals).putInt(this.code.length).putByteArray(this.code.data, 0, this.code.length);
                Handler.putExceptionTable(this.firstHandler, output);
                output.putShort(codeAttributeCount);
                if (this.stackMapTableEntries != null) {
                    boolean useStackMapTable = this.symbolTable.getMajorVersion() >= 50;
                    output.putShort(this.symbolTable.addConstantUtf8(useStackMapTable ? "StackMapTable" : "StackMap")).putInt(2 + this.stackMapTableEntries.length).putShort(this.stackMapTableNumberOfEntries).putByteArray(this.stackMapTableEntries.data, 0, this.stackMapTableEntries.length);
                }
                if (this.lineNumberTable != null) {
                    output.putShort(this.symbolTable.addConstantUtf8("LineNumberTable")).putInt(2 + this.lineNumberTable.length).putShort(this.lineNumberTableLength).putByteArray(this.lineNumberTable.data, 0, this.lineNumberTable.length);
                }
                if (this.localVariableTable != null) {
                    output.putShort(this.symbolTable.addConstantUtf8("LocalVariableTable")).putInt(2 + this.localVariableTable.length).putShort(this.localVariableTableLength).putByteArray(this.localVariableTable.data, 0, this.localVariableTable.length);
                }
                if (this.localVariableTypeTable != null) {
                    output.putShort(this.symbolTable.addConstantUtf8("LocalVariableTypeTable")).putInt(2 + this.localVariableTypeTable.length).putShort(this.localVariableTypeTableLength).putByteArray(this.localVariableTypeTable.data, 0, this.localVariableTypeTable.length);
                }
                if (this.lastCodeRuntimeVisibleTypeAnnotation != null) {
                    this.lastCodeRuntimeVisibleTypeAnnotation.putAnnotations(this.symbolTable.addConstantUtf8("RuntimeVisibleTypeAnnotations"), output);
                }
                if (this.lastCodeRuntimeInvisibleTypeAnnotation != null) {
                    this.lastCodeRuntimeInvisibleTypeAnnotation.putAnnotations(this.symbolTable.addConstantUtf8("RuntimeInvisibleTypeAnnotations"), output);
                }
                if (this.firstCodeAttribute != null) {
                    this.firstCodeAttribute.putAttributes(this.symbolTable, this.code.data, this.code.length, this.maxStack, this.maxLocals, output);
                }
            }
            if (this.numberOfExceptions > 0) {
                output.putShort(this.symbolTable.addConstantUtf8("Exceptions")).putInt(2 + 2 * this.numberOfExceptions).putShort(this.numberOfExceptions);
                for (int exceptionIndex : this.exceptionIndexTable) {
                    output.putShort(exceptionIndex);
                }
            }
            Attribute.putAttributes(this.symbolTable, this.accessFlags, this.signatureIndex, output);
            AnnotationWriter.putAnnotations(this.symbolTable, this.lastRuntimeVisibleAnnotation, this.lastRuntimeInvisibleAnnotation, this.lastRuntimeVisibleTypeAnnotation, this.lastRuntimeInvisibleTypeAnnotation, output);
            if (this.lastRuntimeVisibleParameterAnnotations != null) {
                AnnotationWriter.putParameterAnnotations(this.symbolTable.addConstantUtf8("RuntimeVisibleParameterAnnotations"), this.lastRuntimeVisibleParameterAnnotations, this.visibleAnnotableParameterCount == 0 ? this.lastRuntimeVisibleParameterAnnotations.length : this.visibleAnnotableParameterCount, output);
            }
            if (this.lastRuntimeInvisibleParameterAnnotations != null) {
                AnnotationWriter.putParameterAnnotations(this.symbolTable.addConstantUtf8("RuntimeInvisibleParameterAnnotations"), this.lastRuntimeInvisibleParameterAnnotations, this.invisibleAnnotableParameterCount == 0 ? this.lastRuntimeInvisibleParameterAnnotations.length : this.invisibleAnnotableParameterCount, output);
            }
            if (this.defaultValue != null) {
                output.putShort(this.symbolTable.addConstantUtf8("AnnotationDefault")).putInt(this.defaultValue.length).putByteArray(this.defaultValue.data, 0, this.defaultValue.length);
            }
            if (this.parameters != null) {
                output.putShort(this.symbolTable.addConstantUtf8("MethodParameters")).putInt(1 + this.parameters.length).putByte(this.parametersCount).putByteArray(this.parameters.data, 0, this.parameters.length);
            }
            if (this.firstAttribute != null) {
                this.firstAttribute.putAttributes(this.symbolTable, output);
            }
        }
    }

    final void collectAttributePrototypes(Attribute.Set attributePrototypes) {
        attributePrototypes.addAttributes(this.firstAttribute);
        attributePrototypes.addAttributes(this.firstCodeAttribute);
    }
}
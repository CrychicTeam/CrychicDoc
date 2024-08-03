package icyllis.arc3d.compiler.spirv;

import icyllis.arc3d.compiler.CodeGenerator;
import icyllis.arc3d.compiler.ConstantFolder;
import icyllis.arc3d.compiler.MemoryLayout;
import icyllis.arc3d.compiler.Operator;
import icyllis.arc3d.compiler.SPIRVVersion;
import icyllis.arc3d.compiler.ShaderCaps;
import icyllis.arc3d.compiler.ShaderCompiler;
import icyllis.arc3d.compiler.ShaderKind;
import icyllis.arc3d.compiler.TargetApi;
import icyllis.arc3d.compiler.analysis.Analysis;
import icyllis.arc3d.compiler.tree.BinaryExpression;
import icyllis.arc3d.compiler.tree.BlockStatement;
import icyllis.arc3d.compiler.tree.Expression;
import icyllis.arc3d.compiler.tree.ExpressionStatement;
import icyllis.arc3d.compiler.tree.FieldAccess;
import icyllis.arc3d.compiler.tree.FunctionDecl;
import icyllis.arc3d.compiler.tree.FunctionDefinition;
import icyllis.arc3d.compiler.tree.GlobalVariableDecl;
import icyllis.arc3d.compiler.tree.IndexExpression;
import icyllis.arc3d.compiler.tree.InterfaceBlock;
import icyllis.arc3d.compiler.tree.Layout;
import icyllis.arc3d.compiler.tree.Modifiers;
import icyllis.arc3d.compiler.tree.Statement;
import icyllis.arc3d.compiler.tree.Swizzle;
import icyllis.arc3d.compiler.tree.TopLevelElement;
import icyllis.arc3d.compiler.tree.TranslationUnit;
import icyllis.arc3d.compiler.tree.Type;
import icyllis.arc3d.compiler.tree.Variable;
import icyllis.arc3d.compiler.tree.VariableDecl;
import icyllis.arc3d.compiler.tree.VariableReference;
import icyllis.arc3d.core.MathUtil;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntListIterator;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.Object2IntMap.Entry;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class SPIRVCodeGenerator extends CodeGenerator {

    public static final int GENERATOR_MAGIC_NUMBER = 0;

    public static final int NONE_ID = -1;

    public final TargetApi mOutputTarget;

    public final SPIRVVersion mOutputVersion;

    private final WordBuffer mNameBuffer = new WordBuffer();

    private final WordBuffer mConstantBuffer = new WordBuffer();

    private final WordBuffer mDecorationBuffer = new WordBuffer();

    private final WordBuffer mFunctionBuffer = new WordBuffer();

    private final WordBuffer mGlobalInitBuffer = new WordBuffer();

    private final WordBuffer mVariableBuffer = new WordBuffer();

    private final WordBuffer mBodyBuffer = new WordBuffer();

    private final HashMap<Type, int[]> mStructTable = new HashMap();

    private final Object2IntOpenHashMap<FunctionDecl> mFunctionTable = new Object2IntOpenHashMap();

    private final Object2IntOpenHashMap<Variable> mVariableTable = new Object2IntOpenHashMap();

    private final InstructionBuilder[] mInstBuilderPool = new InstructionBuilder[10];

    private int mInstBuilderPoolSize = 0;

    private final Object2IntOpenHashMap<Instruction> mOpCache = new Object2IntOpenHashMap();

    private final Int2ObjectOpenHashMap<Instruction> mSpvIdCache = new Int2ObjectOpenHashMap();

    final Int2IntOpenHashMap mStoreCache = new Int2IntOpenHashMap();

    private final IntArrayList mReachableOps = new IntArrayList();

    private final IntArrayList mStoreOps = new IntArrayList();

    private int mCurrentBlock = 0;

    private static final int kBranchIsAbove = 0;

    private static final int kBranchIsBelow = 1;

    private static final int kBranchesOnBothSides = 2;

    private final IntArrayList mAccessChain = new IntArrayList();

    private final IntOpenHashSet mCapabilities = new IntOpenHashSet(16, 0.5F);

    private final IntArrayList mInterfaceVariables = new IntArrayList();

    private FunctionDecl mEntryPointFunction;

    private int mIdCount = 1;

    private int mGLSLExtendedInstructions;

    private boolean mEmitNames;

    public SPIRVCodeGenerator(@Nonnull ShaderCompiler compiler, @Nonnull TranslationUnit translationUnit, @Nonnull ShaderCaps shaderCaps) {
        super(compiler, translationUnit);
        this.mOutputTarget = (TargetApi) Objects.requireNonNullElse(shaderCaps.mTargetApi, TargetApi.VULKAN_1_0);
        this.mOutputVersion = (SPIRVVersion) Objects.requireNonNullElse(shaderCaps.mSPIRVVersion, SPIRVVersion.SPIRV_1_0);
    }

    @Nullable
    @Override
    public ByteBuffer generateCode() {
        assert this.getContext().getErrorHandler().errorCount() == 0;
        if (this.mOutputTarget.isOpenGLES()) {
            this.getContext().error(-1, "OpenGL ES is not a valid client API for SPIR-V");
            return null;
        } else {
            ShaderKind kind = this.mTranslationUnit.getKind();
            if (!kind.isVertex() && !kind.isFragment() && !kind.isCompute()) {
                this.getContext().error(-1, "shader kind " + kind + " is not executable in SPIR-V");
                return null;
            } else {
                this.mEmitNames = !this.getContext().getOptions().mMinifyNames;
                this.buildInstructions(this.mTranslationUnit);
                if (this.getContext().getErrorHandler().errorCount() != 0) {
                    return null;
                } else {
                    String entryPointName = this.mEntryPointFunction.getName();
                    int estimatedSize = 20;
                    estimatedSize += this.mCapabilities.size() * 2 * 4;
                    estimatedSize += 24;
                    estimatedSize += 12;
                    int entryPointWordCount = 3 + (entryPointName.length() + 4) / 4 + this.mInterfaceVariables.size();
                    estimatedSize += entryPointWordCount * 4;
                    estimatedSize += this.mNameBuffer.size() * 4;
                    estimatedSize += this.mDecorationBuffer.size() * 4;
                    estimatedSize += this.mConstantBuffer.size() * 4;
                    estimatedSize += this.mFunctionBuffer.size() * 4;
                    estimatedSize += 40;
                    BufferWriter writer = new BufferWriter(estimatedSize);
                    writer.writeWord(119734787);
                    writer.writeWord(this.mOutputVersion.mVersionNumber);
                    writer.writeWord(0);
                    writer.writeWord(this.mIdCount);
                    writer.writeWord(0);
                    IntIterator it = this.mCapabilities.iterator();
                    while (it.hasNext()) {
                        this.writeInstruction(17, it.nextInt(), writer);
                    }
                    this.writeInstruction(11, this.mGLSLExtendedInstructions, "GLSL.std.450", writer);
                    this.writeInstruction(14, 0, 1, writer);
                    this.writeOpcode(15, entryPointWordCount, writer);
                    if (kind.isVertex()) {
                        writer.writeWord(0);
                    } else if (kind.isFragment()) {
                        writer.writeWord(4);
                    } else if (kind.isCompute()) {
                        writer.writeWord(5);
                    }
                    int entryPoint = this.mFunctionTable.getInt(this.mEntryPointFunction);
                    writer.writeWord(entryPoint);
                    writer.writeString8(this.getContext(), entryPointName);
                    IntListIterator var7 = this.mInterfaceVariables.iterator();
                    while (var7.hasNext()) {
                        int id = (Integer) var7.next();
                        writer.writeWord(id);
                    }
                    if (kind.isFragment()) {
                        this.writeInstruction(16, entryPoint, this.mOutputTarget.isOpenGL() ? 8 : 7, writer);
                    }
                    writer.writeWords(this.mNameBuffer.elements(), this.mNameBuffer.size());
                    writer.writeWords(this.mDecorationBuffer.elements(), this.mDecorationBuffer.size());
                    writer.writeWords(this.mConstantBuffer.elements(), this.mConstantBuffer.size());
                    writer.writeWords(this.mFunctionBuffer.elements(), this.mFunctionBuffer.size());
                    return this.getContext().getErrorHandler().errorCount() != 0 ? null : writer.detach();
                }
            }
        }
    }

    private int getStorageClass(@Nonnull Variable variable) {
        Type type = variable.getType();
        if (type.isArray()) {
            type = type.getElementType();
        }
        if (type.isOpaque()) {
            return 0;
        } else {
            Modifiers modifiers = variable.getModifiers();
            if (variable.getStorage() == 1) {
                if ((modifiers.flags() & 32) != 0) {
                    return 1;
                }
                if ((modifiers.flags() & 64) != 0) {
                    return 3;
                }
            }
            if ((modifiers.flags() & 4096) != 0 && this.mOutputVersion.isAtLeast(SPIRVVersion.SPIRV_1_3)) {
                return 12;
            } else if ((modifiers.flags() & 4112) != 0) {
                if ((modifiers.layoutFlags() & 16) != 0) {
                    return 9;
                } else if (type.isInterfaceBlock()) {
                    return 2;
                } else {
                    if (!this.mOutputTarget.isOpenGL()) {
                        this.getContext().error(variable.mPosition, "uniform variables at global scope are not allowed");
                    }
                    return 0;
                }
            } else if ((modifiers.flags() & 8192) != 0) {
                return 4;
            } else {
                return variable.getStorage() == 1 ? 6 : 7;
            }
        }
    }

    private int getStorageClass(@Nonnull Expression expr) {
        switch(expr.getKind()) {
            case VARIABLE_REFERENCE:
                return this.getStorageClass(((VariableReference) expr).getVariable());
            case INDEX:
                return this.getStorageClass(((IndexExpression) expr).getBase());
            case FIELD_ACCESS:
                return this.getStorageClass(((FieldAccess) expr).getBase());
            default:
                return 7;
        }
    }

    int getUniqueId(@Nonnull Type type) {
        return this.getUniqueId(type.isRelaxedPrecision());
    }

    private int getUniqueId(boolean relaxedPrecision) {
        int id = this.getUniqueId();
        if (relaxedPrecision) {
            this.writeInstruction(71, id, 0, this.mDecorationBuffer);
        }
        return id;
    }

    private int getUniqueId() {
        return this.mIdCount++;
    }

    private InstructionBuilder getInstBuilder(int opcode) {
        return this.mInstBuilderPoolSize == 0 ? new InstructionBuilder(opcode) : this.mInstBuilderPool[--this.mInstBuilderPoolSize].reset(opcode);
    }

    int writeType(@Nonnull Type type) {
        return this.writeType(type, null, null);
    }

    private int writeType(@Nonnull Type type, @Nullable Modifiers modifiers, @Nullable MemoryLayout memoryLayout) {
        return switch(type.getTypeKind()) {
            case 0 ->
                {
                    int stride;
                    if (memoryLayout != null) {
                        if (!memoryLayout.isSupported(type)) {
                        }
                        stride = memoryLayout.stride(type);
                        assert stride > 0;
                    } else {
                        stride = 0;
                    }
                    int elementTypeId = this.writeType(type.getElementType(), modifiers, memoryLayout);
                    int resultId;
                    if (type.isUnsizedArray()) {
                        resultId = this.writeInstructionWithCache(this.getInstBuilder(29).addKeyedResult(stride).addWord(elementTypeId), this.mConstantBuffer);
                    } else {
                        int arraySize = type.getArraySize();
                        if (arraySize <= 0) {
                        }
                        int arraySizeId = this.writeScalarConstant((double) arraySize, this.getContext().getTypes().mInt);
                        resultId = this.writeInstructionWithCache(this.getInstBuilder(28).addKeyedResult(stride).addWord(elementTypeId).addWord(arraySizeId), this.mConstantBuffer);
                    }
                    if (stride > 0) {
                        this.writeInstructionWithCache(this.getInstBuilder(71).addWord(resultId).addWord(6).addWord(stride), this.mDecorationBuffer);
                    }
                    yield resultId;
                }
            default ->
                {
                }
            case 2 ->
                {
                }
            case 5 ->
                {
                    if (type.isBoolean()) {
                    } else if (type.isInteger()) {
                    } else {
                    }
                }
            case 6 ->
                this.writeStruct(type, memoryLayout);
            case 7 ->
                {
                }
            case 8 ->
                this.writeInstructionWithCache(this.getInstBuilder(19).addResult(), this.mConstantBuffer);
        };
    }

    private int writeStruct(@Nonnull Type type, @Nullable MemoryLayout memoryLayout) {
        assert type.isStruct();
        int[] cached = (int[]) this.mStructTable.computeIfAbsent(type, __ -> new int[MemoryLayout.Scalar.ordinal() + 2]);
        int slot = memoryLayout == null ? 0 : memoryLayout.ordinal() + 1;
        int resultId = cached[slot];
        if (resultId != 0) {
            return resultId;
        } else {
            if (slot == 0) {
                for (int i = 1; i < cached.length; i++) {
                    resultId = cached[i];
                    if (resultId != 0) {
                        return resultId;
                    }
                }
            } else {
                resultId = cached[0];
                if (resultId != 0) {
                    cached[0] = 0;
                }
            }
            Type.Field[] fields = type.getFields();
            boolean unique;
            if (resultId == 0) {
                unique = true;
                InstructionBuilder builder = this.getInstBuilder(30).addUniqueResult();
                for (Type.Field f : fields) {
                    int typeId = this.writeType(f.type(), f.modifiers(), memoryLayout);
                    builder.addWord(typeId);
                }
                resultId = this.writeInstructionWithCache(builder, this.mConstantBuffer);
                if (this.mEmitNames) {
                    this.writeInstruction(5, resultId, type.getName(), this.mNameBuffer);
                }
            } else {
                unique = false;
            }
            cached[slot] = resultId;
            int offset = 0;
            int[] size = new int[3];
            for (int ix = 0; ix < fields.length; ix++) {
                Type.Field field = fields[ix];
                if (unique) {
                    if (this.mEmitNames) {
                        this.writeInstruction(6, resultId, ix, field.name(), this.mNameBuffer);
                    }
                    if (field.type().isRelaxedPrecision()) {
                        this.writeInstruction(72, resultId, ix, 0, this.mDecorationBuffer);
                    }
                    this.writeFieldModifiers(field.modifiers(), resultId, ix);
                }
                if (memoryLayout != null) {
                    if (!memoryLayout.isSupported(field.type())) {
                        this.getContext().error(field.position(), "type '" + field.type() + "' is not permitted here");
                        return resultId;
                    }
                    int alignment = memoryLayout.alignment(field.type(), size);
                    int fieldOffset = field.modifiers().layoutOffset();
                    if (fieldOffset >= 0) {
                        if (fieldOffset < offset) {
                            this.getContext().error(field.position(), "offset of field '" + field.name() + "' must be at least " + offset);
                        }
                        if ((fieldOffset & alignment - 1) != 0) {
                            this.getContext().error(field.position(), "offset of field '" + field.name() + "' must round up to " + alignment);
                        }
                        offset = fieldOffset;
                    } else {
                        offset = MathUtil.alignTo(offset, alignment);
                    }
                    if (field.modifiers().layoutBuiltin() >= 0) {
                        this.getContext().error(field.position(), "builtin field '" + field.name() + "' cannot be explicitly laid out.");
                    } else {
                        this.writeInstruction(72, resultId, ix, 35, offset, this.mDecorationBuffer);
                    }
                    int matrixStride = size[1];
                    if (matrixStride > 0) {
                        assert field.type().isMatrix() || field.type().isArray() && field.type().getElementType().isMatrix();
                        this.writeInstruction(72, resultId, ix, 5, this.mDecorationBuffer);
                        this.writeInstruction(72, resultId, ix, 7, matrixStride, this.mDecorationBuffer);
                    }
                    offset += size[0];
                }
            }
            return resultId;
        }
    }

    private int writeFunction(@Nonnull FunctionDefinition func, Writer writer) {
        int numReachableOps = this.mReachableOps.size();
        int numStoreOps = this.mStoreOps.size();
        this.mVariableBuffer.clear();
        int result = this.writeFunctionDecl(func.getFunctionDecl(), writer);
        this.mCurrentBlock = 0;
        this.writeLabel(this.getUniqueId(), writer);
        this.mBodyBuffer.clear();
        this.writeBlock(func.getBody(), this.mBodyBuffer);
        writer.writeWords(this.mVariableBuffer.elements(), this.mVariableBuffer.size());
        if (func.getFunctionDecl().isEntryPoint()) {
            writer.writeWords(this.mGlobalInitBuffer.elements(), this.mGlobalInitBuffer.size());
        }
        writer.writeWords(this.mBodyBuffer.elements(), this.mBodyBuffer.size());
        if (this.mCurrentBlock != 0) {
            if (func.getFunctionDecl().getReturnType().isVoid()) {
                this.writeInstruction(253, writer);
            } else {
                this.writeInstruction(255, writer);
            }
        }
        this.writeInstruction(56, writer);
        this.pruneConditionalOps(numReachableOps, numStoreOps);
        return result;
    }

    private int writeFunctionDecl(@Nonnull FunctionDecl decl, Writer writer) {
        int result = this.mFunctionTable.getInt(decl);
        int returnTypeId = this.writeType(decl.getReturnType());
        int functionTypeId = this.writeFunctionType(decl);
        this.writeInstruction(54, returnTypeId, result, 0, functionTypeId, writer);
        if (this.mEmitNames) {
            String mangledName = decl.getMangledName();
            if (mangledName.length() <= 5200) {
                this.writeInstruction(5, result, mangledName, this.mNameBuffer);
            }
        }
        for (Variable parameter : decl.getParameters()) {
            int id = this.getUniqueId();
            this.mVariableTable.put(parameter, id);
            int type = this.writeFunctionParameterType(parameter.getType(), parameter.getModifiers());
            this.writeInstruction(55, type, id, writer);
        }
        return result;
    }

    private int writeFunctionType(@Nonnull FunctionDecl function) {
        int returnTypeId = this.writeType(function.getReturnType());
        InstructionBuilder builder = this.getInstBuilder(33).addResult().addWord(returnTypeId);
        for (Variable parameter : function.getParameters()) {
            int typeId = this.writeFunctionParameterType(parameter.getType(), parameter.getModifiers());
            builder.addWord(typeId);
        }
        return this.writeInstructionWithCache(builder, this.mConstantBuffer);
    }

    private int writeFunctionParameterType(@Nonnull Type paramType, @Nullable Modifiers paramMods) {
        int storageClass;
        if (paramType.isOpaque()) {
            storageClass = 0;
        } else {
            storageClass = 7;
        }
        return this.writePointerType(paramType, paramMods, null, storageClass);
    }

    private int writePointerType(@Nonnull Type type, int storageClass) {
        return this.writePointerType(type, null, null, storageClass);
    }

    private int writePointerType(@Nonnull Type type, @Nullable Modifiers modifiers, @Nullable MemoryLayout memoryLayout, int storageClass) {
        int typeId = this.writeType(type, modifiers, memoryLayout);
        return this.writeInstructionWithCache(this.getInstBuilder(32).addResult().addWord(storageClass).addWord(typeId), this.mConstantBuffer);
    }

    private int writeOpConstantTrue(@Nonnull Type type) {
        assert type.isBoolean();
        int typeId = this.writeType(type);
        return this.writeInstructionWithCache(this.getInstBuilder(41).addWord(typeId).addResult(), this.mConstantBuffer);
    }

    private int writeOpConstantFalse(@Nonnull Type type) {
        assert type.isBoolean();
        int typeId = this.writeType(type);
        return this.writeInstructionWithCache(this.getInstBuilder(42).addWord(typeId).addResult(), this.mConstantBuffer);
    }

    private int writeOpConstant(@Nonnull Type type, int valueBits) {
        assert type.isInteger() || type.isFloat();
        assert type.getWidth() == 32;
        int typeId = this.writeType(type);
        return this.writeInstructionWithCache(this.getInstBuilder(43).addWord(typeId).addResult().addWord(valueBits), this.mConstantBuffer);
    }

    private int writeScalarConstant(double value, Type type) {
        int valueBits;
        switch(type.getScalarKind()) {
            case 0:
                valueBits = Float.floatToRawIntBits((float) value);
                break;
            case 1:
                valueBits = (int) value;
                break;
            case 2:
                valueBits = (int) ((long) value);
                break;
            case 3:
                return value != 0.0 ? this.writeOpConstantTrue(type) : this.writeOpConstantFalse(type);
            default:
                throw new AssertionError();
        }
        assert type.getWidth() == 32;
        return this.writeOpConstant(type, valueBits);
    }

    private void writeModifiers(@Nonnull Modifiers modifiers, int targetId) {
        Layout layout = modifiers.layout();
        boolean hasLocation = false;
        boolean hasBinding = false;
        int descriptorSet = -1;
        if (layout != null) {
            boolean isPushConstant = (layout.layoutFlags() & 16) != 0;
            if (layout.mLocation >= 0) {
                this.writeInstruction(71, targetId, 30, layout.mLocation, this.mDecorationBuffer);
                hasLocation = true;
            }
            if (layout.mComponent >= 0) {
                this.writeInstruction(71, targetId, 31, layout.mComponent, this.mDecorationBuffer);
            }
            if (layout.mIndex >= 0) {
                this.writeInstruction(71, targetId, 32, layout.mIndex, this.mDecorationBuffer);
            }
            if (layout.mBinding >= 0) {
                if (isPushConstant) {
                    this.getContext().error(modifiers.mPosition, "cannot combine 'binding' with 'push_constants'");
                } else {
                    this.writeInstruction(71, targetId, 33, layout.mBinding, this.mDecorationBuffer);
                }
                hasBinding = true;
            }
            if (layout.mSet >= 0) {
                if (isPushConstant) {
                    this.getContext().error(modifiers.mPosition, "cannot combine 'set' with 'push_constants'");
                } else {
                    this.writeInstruction(71, targetId, 34, layout.mSet, this.mDecorationBuffer);
                }
                descriptorSet = layout.mSet;
            }
            if (layout.mInputAttachmentIndex >= 0) {
                this.writeInstruction(71, targetId, 43, layout.mInputAttachmentIndex, this.mDecorationBuffer);
                this.mCapabilities.add(40);
            }
            if (layout.mBuiltin >= 0) {
                this.writeInstruction(71, targetId, 11, layout.mBuiltin, this.mDecorationBuffer);
            }
        }
        if ((modifiers.flags() & 4112) != 0) {
            if (!hasBinding) {
                this.getContext().error(modifiers.mPosition, "'binding' is missing");
            }
            if (descriptorSet < 0) {
                if (!this.mOutputTarget.isOpenGL()) {
                    this.getContext().error(modifiers.mPosition, "'set' is missing");
                }
            } else if (this.mOutputTarget.isOpenGL() && descriptorSet != 0) {
                this.getContext().error(modifiers.mPosition, "'set' must be 0");
            }
        }
        if ((modifiers.flags() & 1) == 0) {
            if ((modifiers.flags() & 4) != 0) {
                this.writeInstruction(71, targetId, 13, this.mDecorationBuffer);
            } else if ((modifiers.flags() & 2) != 0) {
                this.writeInstruction(71, targetId, 14, this.mDecorationBuffer);
            }
        }
        if ((modifiers.flags() & 256) != 0) {
            this.writeInstruction(71, targetId, 21, this.mDecorationBuffer);
        }
        if ((modifiers.flags() & 384) != 0) {
            this.writeInstruction(71, targetId, 23, this.mDecorationBuffer);
        }
        if ((modifiers.flags() & 512) != 0) {
            this.writeInstruction(71, targetId, 19, this.mDecorationBuffer);
        }
        if ((modifiers.flags() & 1024) != 0) {
            this.writeInstruction(71, targetId, 24, this.mDecorationBuffer);
        }
        if ((modifiers.flags() & 2048) != 0) {
            this.writeInstruction(71, targetId, 25, this.mDecorationBuffer);
        }
    }

    private void writeFieldModifiers(@Nonnull Modifiers modifiers, int targetId, int member) {
        Layout layout = modifiers.layout();
        if (layout != null) {
            assert layout.mIndex == -1;
            assert layout.mBinding == -1;
            assert layout.mSet == -1;
            assert layout.mInputAttachmentIndex == -1;
            if (layout.mLocation >= 0) {
                this.writeInstruction(72, targetId, member, 30, layout.mLocation, this.mDecorationBuffer);
            }
            if (layout.mComponent >= 0) {
                this.writeInstruction(72, targetId, member, 31, layout.mComponent, this.mDecorationBuffer);
            }
            if (layout.mBuiltin >= 0) {
                this.writeInstruction(72, targetId, member, 11, layout.mBuiltin, this.mDecorationBuffer);
            }
        }
        if ((modifiers.flags() & 1) == 0) {
            if ((modifiers.flags() & 4) != 0) {
                this.writeInstruction(72, targetId, member, 13, this.mDecorationBuffer);
            } else if ((modifiers.flags() & 2) != 0) {
                this.writeInstruction(72, targetId, member, 14, this.mDecorationBuffer);
            }
        }
        if ((modifiers.flags() & 256) != 0) {
            this.writeInstruction(72, targetId, member, 21, this.mDecorationBuffer);
        }
        if ((modifiers.flags() & 384) != 0) {
            this.writeInstruction(72, targetId, member, 23, this.mDecorationBuffer);
        }
        if ((modifiers.flags() & 512) != 0) {
            this.writeInstruction(72, targetId, member, 19, this.mDecorationBuffer);
        }
        if ((modifiers.flags() & 1024) != 0) {
            this.writeInstruction(72, targetId, member, 24, this.mDecorationBuffer);
        }
        if ((modifiers.flags() & 2048) != 0) {
            this.writeInstruction(72, targetId, member, 25, this.mDecorationBuffer);
        }
    }

    private int writeInterfaceBlock(@Nonnull InterfaceBlock block) {
        int resultId = this.getUniqueId();
        Variable variable = block.getVariable();
        Modifiers modifiers = variable.getModifiers();
        MemoryLayout memoryLayout;
        if ((modifiers.flags() & 4096) != 0) {
            memoryLayout = MemoryLayout.Std430;
        } else if ((modifiers.flags() & 16) != 0) {
            memoryLayout = (modifiers.layoutFlags() & 16) != 0 ? MemoryLayout.Std430 : MemoryLayout.Extended;
        } else {
            memoryLayout = null;
        }
        Type type = variable.getType();
        assert type.isInterfaceBlock();
        if (memoryLayout != null && !memoryLayout.isSupported(type)) {
            this.getContext().error(type.mPosition, "type '" + type + "' is not permitted here");
            return resultId;
        } else {
            int typeId = this.writeStruct(type, memoryLayout);
            if (modifiers.layoutBuiltin() == -1) {
                boolean legacyBufferBlock = (modifiers.flags() & 4096) != 0 && this.mOutputVersion.isBefore(SPIRVVersion.SPIRV_1_3);
                this.writeInstruction(71, typeId, legacyBufferBlock ? 3 : 2, this.mDecorationBuffer);
            }
            this.writeModifiers(modifiers, resultId);
            int ptrTypeId = this.getUniqueId();
            int storageClass = this.getStorageClass(variable);
            this.writeInstruction(32, ptrTypeId, storageClass, typeId, this.mConstantBuffer);
            this.writeInstruction(59, ptrTypeId, resultId, storageClass, this.mConstantBuffer);
            if (this.mEmitNames) {
                this.writeInstruction(5, resultId, variable.getName(), this.mNameBuffer);
            }
            this.mVariableTable.put(variable, resultId);
            return resultId;
        }
    }

    private static boolean isCompileTimeConstant(VariableDecl variableDecl) {
        return variableDecl.getVariable().getModifiers().isConst() && (variableDecl.getVariable().getType().isScalar() || variableDecl.getVariable().getType().isVector()) && (ConstantFolder.getConstantValueOrNullForVariable(variableDecl.getInit()) != null || Analysis.isCompileTimeConstant(variableDecl.getInit()));
    }

    private boolean writeGlobalVariableDecl(VariableDecl variableDecl) {
        if (isCompileTimeConstant(variableDecl)) {
            return true;
        } else {
            int storageClass = this.getStorageClass(variableDecl.getVariable());
            int id = this.writeGlobalVariable(storageClass, variableDecl.getVariable());
            if (variableDecl.getInit() != null) {
                int init = this.writeExpression(variableDecl.getInit(), this.mGlobalInitBuffer);
                this.writeOpStore(storageClass, id, init, this.mGlobalInitBuffer);
            }
            return true;
        }
    }

    private int writeGlobalVariable(int storageClass, Variable variable) {
        Type type = variable.getType();
        int resultId = this.getUniqueId(type);
        Modifiers modifiers = variable.getModifiers();
        this.writeModifiers(modifiers, resultId);
        int ptrTypeId = this.writePointerType(type, modifiers, null, storageClass);
        this.writeInstruction(59, ptrTypeId, resultId, storageClass, this.mConstantBuffer);
        if (this.mEmitNames) {
            this.writeInstruction(5, resultId, variable.getName(), this.mNameBuffer);
        }
        this.mVariableTable.put(variable, resultId);
        return resultId;
    }

    private int writeExpression(@Nonnull Expression expr, Writer writer) {
        return switch(expr.getKind()) {
            case VARIABLE_REFERENCE ->
                this.writeVariableReference((VariableReference) expr, writer);
            default ->
                0;
            case FIELD_ACCESS ->
                this.writeLValue(expr, writer).load(this, writer);
            case BINARY ->
                this.writeBinaryExpression((BinaryExpression) expr, writer);
        };
    }

    private int writeBinaryExpression(@Nonnull BinaryExpression expr, Writer writer) {
        Expression left = expr.getLeft();
        Expression right = expr.getRight();
        Operator op = expr.getOperator();
        switch(op) {
            case ASSIGN:
                int rvalue = this.writeExpression(right, writer);
                this.writeLValue(left, writer).store(this, rvalue, writer);
                return rvalue;
            case LOGICAL_AND:
                if (!this.getContext().getOptions().mNoShortCircuit) {
                    return this.writeLogicalAndSC(left, right, writer);
                }
                break;
            case LOGICAL_OR:
                if (!this.getContext().getOptions().mNoShortCircuit) {
                    return this.writeLogicalOrSC(left, right, writer);
                }
        }
        LValue lvalue;
        int lhs;
        if (op.isAssignment()) {
            lvalue = this.writeLValue(left, writer);
            lhs = lvalue.load(this, writer);
        } else {
            lvalue = null;
            lhs = this.writeExpression(left, writer);
        }
        int rhs = this.writeExpression(right, writer);
        int result = this.writeBinaryOp(left.getType(), lhs, op.removeAssignment(), right.getType(), rhs, expr.getType(), writer);
        if (lvalue != null) {
            lvalue.store(this, result, writer);
        }
        return result;
    }

    private int writeLogicalAndSC(@Nonnull Expression left, @Nonnull Expression right, Writer writer) {
        int falseConstant = this.writeScalarConstant(0.0, this.getContext().getTypes().mBool);
        int lhs = this.writeExpression(left, writer);
        int numReachableOps = this.mReachableOps.size();
        int numStoreOps = this.mStoreOps.size();
        int rhsLabel = this.getUniqueId();
        int end = this.getUniqueId();
        int lhsBlock = this.mCurrentBlock;
        this.writeInstruction(247, end, 0, writer);
        this.writeInstruction(250, lhs, rhsLabel, end, writer);
        this.writeLabel(rhsLabel, writer);
        int rhs = this.writeExpression(right, writer);
        int rhsBlock = this.mCurrentBlock;
        this.writeInstruction(249, end, writer);
        this.writeLabel(end, 0, numReachableOps, numStoreOps, writer);
        int result = this.getUniqueId();
        int typeId = this.writeType(this.getContext().getTypes().mBool);
        this.writeInstruction(245, typeId, result, falseConstant, lhsBlock, rhs, rhsBlock, writer);
        return result;
    }

    private int writeLogicalOrSC(@Nonnull Expression left, @Nonnull Expression right, Writer writer) {
        int trueConstant = this.writeScalarConstant(1.0, this.getContext().getTypes().mBool);
        int lhs = this.writeExpression(left, writer);
        int numReachableOps = this.mReachableOps.size();
        int numStoreOps = this.mStoreOps.size();
        int rhsLabel = this.getUniqueId();
        int end = this.getUniqueId();
        int lhsBlock = this.mCurrentBlock;
        this.writeInstruction(247, end, 0, writer);
        this.writeInstruction(250, lhs, end, rhsLabel, writer);
        this.writeLabel(rhsLabel, writer);
        int rhs = this.writeExpression(right, writer);
        int rhsBlock = this.mCurrentBlock;
        this.writeInstruction(249, end, writer);
        this.writeLabel(end, 0, numReachableOps, numStoreOps, writer);
        int result = this.getUniqueId();
        int typeId = this.writeType(this.getContext().getTypes().mBool);
        this.writeInstruction(245, typeId, result, trueConstant, lhsBlock, rhs, rhsBlock, writer);
        return result;
    }

    private int writeBinaryOp(Type leftType, int lhs, Operator op, Type rightType, int rhs, Type resultType, Writer writer) {
        return 0;
    }

    private int writeBinaryOp(Type resultType, Type operandType, int lhs, int rhs, int _float, int _signed, int _unsigned, int _boolean, Writer writer) {
        int resultId = this.getUniqueId(resultType);
        int op = select_by_component_type(operandType, _float, _signed, _unsigned, _boolean);
        if (op == 1) {
            this.getContext().error(operandType.mPosition, "unsupported operand for binary expression: " + operandType);
            return -1;
        } else {
            int typeId = this.writeType(resultType);
            this.writeInstruction(op, typeId, resultId, lhs, rhs, writer);
            return resultId;
        }
    }

    private int writeVariableReference(VariableReference ref, Writer writer) {
        Expression constExpr = ConstantFolder.getConstantValueOrNullForVariable(ref);
        return constExpr != null ? this.writeExpression(constExpr, writer) : this.writeLValue(ref, writer).load(this, writer);
    }

    private void pruneConditionalOps(int numReachableOps, int numStoreOps) {
        while (this.mReachableOps.size() > numReachableOps) {
            int id = this.mReachableOps.popInt();
            Instruction inst = (Instruction) this.mSpvIdCache.remove(id);
            if (inst == null) {
                throw new AssertionError("reachable-op list contains unrecognized SpvId");
            }
            this.mOpCache.removeInt(inst);
        }
        while (this.mStoreOps.size() > numStoreOps) {
            int id = this.mStoreOps.popInt();
            this.mStoreCache.remove(id);
        }
    }

    private void writeStatement(@Nonnull Statement stmt, Writer writer) {
        switch(stmt.getKind()) {
            case EXPRESSION:
                this.writeExpression(((ExpressionStatement) stmt).getExpression(), writer);
                break;
            case DISCARD:
                if (this.mOutputVersion.isAtLeast(SPIRVVersion.SPIRV_1_6)) {
                    this.writeInstruction(4416, writer);
                } else {
                    this.writeInstruction(252, writer);
                }
        }
    }

    private static int select_by_component_type(Type type, int _float, int _signed, int _unsigned, int _boolean) {
        if (type.isFloatOrCompound()) {
            return _float;
        } else if (type.isSignedOrCompound()) {
            return _signed;
        } else if (type.isUnsignedOrCompound()) {
            return _unsigned;
        } else if (type.isBooleanOrCompound()) {
            return _boolean;
        } else {
            throw new AssertionError();
        }
    }

    private void writeBlock(BlockStatement blockStmt, Writer writer) {
        for (Statement stmt : blockStmt.getStatements()) {
            this.writeStatement(stmt, writer);
        }
    }

    private void writeLabel(int label, Writer writer) {
        assert this.mCurrentBlock == 0;
        this.mCurrentBlock = label;
        this.writeInstruction(248, label, writer);
    }

    private void writeLabel(int label, int type, int numReachableOps, int numStoreOps, Writer writer) {
        switch(type) {
            case 1:
            case 2:
                this.mStoreCache.clear();
            case 0:
                this.pruneConditionalOps(numReachableOps, numStoreOps);
            default:
                this.writeLabel(label, writer);
        }
    }

    private void writeAccessChain(@Nonnull Expression expr, Writer writer, IntList chain) {
        switch(expr.getKind()) {
            case INDEX:
                {
                    IndexExpression indexExpr = (IndexExpression) expr;
                    if (indexExpr.getBase() instanceof Swizzle) {
                        this.getContext().error(indexExpr.mPosition, "indexing on swizzle is not allowed");
                    }
                    this.writeAccessChain(indexExpr.getBase(), writer, chain);
                    int id = this.writeExpression(indexExpr.getIndex(), writer);
                    chain.add(id);
                    break;
                }
            case FIELD_ACCESS:
                {
                    FieldAccess fieldAccess = (FieldAccess) expr;
                    this.writeAccessChain(fieldAccess.getBase(), writer, chain);
                    int id = this.writeScalarConstant((double) fieldAccess.getFieldIndex(), this.getContext().getTypes().mInt);
                    chain.add(id);
                    break;
                }
            default:
                {
                    int id = this.writeLValue(expr, writer).getPointer();
                    assert id != -1;
                    chain.add(id);
                }
        }
    }

    @Nonnull
    private LValue writeLValue(@Nonnull Expression expr, Writer writer) {
        Type type = expr.getType();
        boolean relaxedPrecision = type.isRelaxedPrecision();
        switch(expr.getKind()) {
            case VARIABLE_REFERENCE:
                {
                    Variable variable = ((VariableReference) expr).getVariable();
                    int entry = this.mVariableTable.getInt(variable);
                    assert entry != 0;
                    int typeId = this.writeType(type, variable.getModifiers(), null);
                    return new PointerLValue(entry, true, typeId, relaxedPrecision, this.getStorageClass(expr));
                }
            case INDEX:
            case FIELD_ACCESS:
                {
                    IntArrayList chain = this.mAccessChain;
                    chain.clear();
                    this.writeAccessChain(expr, writer, chain);
                    int member = this.getUniqueId();
                    int storageClass = this.getStorageClass(expr);
                    this.writeOpcode(65, 3 + chain.size(), writer);
                    writer.writeWord(this.writePointerType(type, storageClass));
                    writer.writeWord(member);
                    writer.writeWords(chain.elements(), chain.size());
                    int typeId = this.writeType(type, null, null);
                    return new PointerLValue(member, false, typeId, relaxedPrecision, storageClass);
                }
            case BINARY:
            default:
                assert false;
                throw new UnsupportedOperationException();
            case SWIZZLE:
                Swizzle swizzle = (Swizzle) expr;
                LValue lvalue = this.writeLValue(swizzle.getBase(), writer);
                if (lvalue.applySwizzle(swizzle.getComponents(), type)) {
                    return lvalue;
                } else {
                    int base = lvalue.getPointer();
                    if (base == -1) {
                        this.getContext().error(swizzle.mPosition, "unable to retrieve lvalue from swizzle");
                    }
                    int storageClassx = this.getStorageClass(swizzle.getBase());
                    if (swizzle.getComponents().length == 1) {
                        int memberx = this.getUniqueId();
                        int typeIdx = this.writePointerType(type, storageClassx);
                        int indexId = this.writeScalarConstant((double) swizzle.getComponents()[0], this.getContext().getTypes().mInt);
                        this.writeInstruction(65, typeIdx, memberx, base, indexId, writer);
                        return new PointerLValue(memberx, false, this.writeType(type), relaxedPrecision, storageClassx);
                    } else {
                        return new SwizzleLValue(base, swizzle.getComponents(), swizzle.getBase().getType(), type, storageClassx);
                    }
                }
        }
    }

    private void buildInstructions(@Nonnull TranslationUnit translationUnit) {
        this.mGLSLExtendedInstructions = this.getUniqueId();
        this.mCapabilities.add(1);
        for (TopLevelElement e : translationUnit) {
            if (e instanceof FunctionDefinition) {
                FunctionDefinition funcDef = (FunctionDefinition) e;
                FunctionDecl function = funcDef.getFunctionDecl();
                this.mFunctionTable.put(function, this.getUniqueId());
                if (function.isEntryPoint()) {
                    this.mEntryPointFunction = function;
                }
            }
        }
        if (this.mEntryPointFunction == null) {
            this.getContext().error(-1, "translation unit does not contain an entry point");
        } else {
            for (TopLevelElement ex : translationUnit) {
                if (ex instanceof InterfaceBlock block) {
                    this.writeInterfaceBlock(block);
                }
            }
            for (TopLevelElement exx : translationUnit) {
                if (exx instanceof GlobalVariableDecl globalVariableDecl) {
                    VariableDecl variableDecl = globalVariableDecl.getVariableDecl();
                    if (!this.writeGlobalVariableDecl(variableDecl)) {
                        return;
                    }
                }
            }
            for (TopLevelElement exxx : translationUnit) {
                if (exxx instanceof FunctionDefinition functionDef) {
                    this.writeFunction(functionDef, this.mFunctionBuffer);
                }
            }
            ObjectIterator var9 = this.mVariableTable.object2IntEntrySet().iterator();
            while (var9.hasNext()) {
                Entry<Variable> exxxx = (Entry<Variable>) var9.next();
                Variable variable = (Variable) exxxx.getKey();
                if (variable.getStorage() == 1 && variable.getModifiers().layoutBuiltin() == -1 && (this.mOutputVersion.isAtLeast(SPIRVVersion.SPIRV_1_4) || (variable.getModifiers().flags() & 96) != 0)) {
                    this.mInterfaceVariables.add(exxxx.getIntValue());
                }
            }
        }
    }

    int writeOpLoad(int type, boolean relaxedPrecision, int pointer, Writer writer) {
        int cachedOp = this.mStoreCache.get(pointer);
        if (cachedOp != 0) {
            return cachedOp;
        } else {
            int resultId = this.getUniqueId(relaxedPrecision);
            this.writeInstruction(61, type, resultId, pointer, writer);
            return resultId;
        }
    }

    void writeOpStore(int storageClass, int pointer, int rvalue, Writer writer) {
        this.writeInstruction(62, pointer, rvalue, writer);
        if (storageClass == 7) {
            this.mStoreCache.put(pointer, rvalue);
            this.mStoreOps.add(pointer);
        }
    }

    void writeOpcode(int opcode, int count, Writer writer) {
        if ((count & -65536) != 0) {
            this.getContext().error(-1, "too many words");
        }
        writer.writeWord(count << 16 | opcode);
    }

    void writeInstruction(int opcode, Writer writer) {
        this.writeOpcode(opcode, 1, writer);
    }

    void writeInstruction(int opcode, int word1, Writer writer) {
        this.writeOpcode(opcode, 2, writer);
        writer.writeWord(word1);
    }

    void writeInstruction(int opcode, int word1, int word2, Writer writer) {
        this.writeOpcode(opcode, 3, writer);
        writer.writeWord(word1);
        writer.writeWord(word2);
    }

    private void writeInstruction(int opcode, int word1, String string, Writer writer) {
        this.writeOpcode(opcode, 2 + (string.length() + 4 >> 2), writer);
        writer.writeWord(word1);
        writer.writeString8(this.getContext(), string);
    }

    void writeInstruction(int opcode, int word1, int word2, int word3, Writer writer) {
        this.writeOpcode(opcode, 4, writer);
        writer.writeWord(word1);
        writer.writeWord(word2);
        writer.writeWord(word3);
    }

    private void writeInstruction(int opcode, int word1, int word2, String string, Writer writer) {
        this.writeOpcode(opcode, 3 + (string.length() + 4 >> 2), writer);
        writer.writeWord(word1);
        writer.writeWord(word2);
        writer.writeString8(this.getContext(), string);
    }

    void writeInstruction(int opcode, int word1, int word2, int word3, int word4, Writer writer) {
        this.writeOpcode(opcode, 5, writer);
        writer.writeWord(word1);
        writer.writeWord(word2);
        writer.writeWord(word3);
        writer.writeWord(word4);
    }

    void writeInstruction(int opcode, int word1, int word2, int word3, int word4, int word5, Writer writer) {
        this.writeOpcode(opcode, 6, writer);
        writer.writeWord(word1);
        writer.writeWord(word2);
        writer.writeWord(word3);
        writer.writeWord(word4);
        writer.writeWord(word5);
    }

    void writeInstruction(int opcode, int word1, int word2, int word3, int word4, int word5, int word6, Writer writer) {
        this.writeOpcode(opcode, 7, writer);
        writer.writeWord(word1);
        writer.writeWord(word2);
        writer.writeWord(word3);
        writer.writeWord(word4);
        writer.writeWord(word5);
        writer.writeWord(word6);
    }

    void writeInstruction(int opcode, int word1, int word2, int word3, int word4, int word5, int word6, int word7, Writer writer) {
        this.writeOpcode(opcode, 8, writer);
        writer.writeWord(word1);
        writer.writeWord(word2);
        writer.writeWord(word3);
        writer.writeWord(word4);
        writer.writeWord(word5);
        writer.writeWord(word6);
        writer.writeWord(word7);
    }

    void writeInstruction(int opcode, int word1, int word2, int word3, int word4, int word5, int word6, int word7, int word8, Writer writer) {
        this.writeOpcode(opcode, 9, writer);
        writer.writeWord(word1);
        writer.writeWord(word2);
        writer.writeWord(word3);
        writer.writeWord(word4);
        writer.writeWord(word5);
        writer.writeWord(word6);
        writer.writeWord(word7);
        writer.writeWord(word8);
    }

    private int writeInstructionWithCache(@Nonnull InstructionBuilder key, @Nonnull Writer writer) {
        assert key.mOpcode != 61;
        assert key.mOpcode != 62;
        int cachedId = this.mOpCache.getInt(key);
        if (cachedId != 0) {
            this.releaseInstBuilder(key);
            return cachedId;
        } else {
            Instruction instruction = key.copy();
            int resultId = -1;
            boolean relaxedPrecision = false;
            switch(key.mResultKind) {
                case 1:
                    this.mOpCache.put(instruction, resultId);
                    break;
                case 3:
                    relaxedPrecision = true;
                case 2:
                case 5:
                    resultId = this.getUniqueId(relaxedPrecision);
                    this.mOpCache.put(instruction, resultId);
                    this.mSpvIdCache.put(resultId, instruction);
                    break;
                case 4:
                    resultId = this.getUniqueId();
                    this.mSpvIdCache.put(resultId, instruction);
                    break;
                default:
                    throw new AssertionError();
            }
            int[] values = key.mValues.elements();
            int[] kinds = key.mKinds.elements();
            int s = key.mValues.size();
            this.writeOpcode(key.mOpcode, s + 1, writer);
            for (int i = 0; i < s; i++) {
                if (Instruction.isResult(kinds[i])) {
                    assert resultId != -1;
                    writer.writeWord(resultId);
                } else {
                    writer.writeWord(values[i]);
                }
            }
            this.releaseInstBuilder(key);
            return resultId;
        }
    }

    private void releaseInstBuilder(InstructionBuilder key) {
        if (this.mInstBuilderPoolSize != this.mInstBuilderPool.length) {
            this.mInstBuilderPool[this.mInstBuilderPoolSize++] = key;
        }
    }
}
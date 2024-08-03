package icyllis.arc3d.compiler.tree;

import icyllis.arc3d.compiler.Context;
import icyllis.arc3d.compiler.ShaderKind;
import java.lang.ref.WeakReference;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class InterfaceBlock extends TopLevelElement {

    private final WeakReference<Variable> mVariable;

    public InterfaceBlock(int position, @Nonnull Variable variable) {
        super(position);
        this.mVariable = new WeakReference(variable);
        variable.setInterfaceBlock(this);
    }

    private static boolean checkBlock(@Nonnull Context context, int pos, @Nonnull Modifiers modifiers, @Nonnull Type blockType, int blockStorage) {
        boolean success = true;
        if (blockType.isUnsizedArray()) {
            context.error(pos, "interface blocks may not have unsized array type");
            success = false;
        }
        int permittedFlags = 12400;
        int permittedLayoutFlags = 0;
        if ((blockStorage & 96) != 0) {
            permittedLayoutFlags |= 32;
        }
        if ((blockStorage & 4112) != 0) {
            permittedLayoutFlags |= 1296;
        }
        if (blockStorage == 4096) {
            permittedFlags |= 3968;
        }
        if ((modifiers.layoutFlags() & 16) != 0 && (modifiers.layoutFlags() & 1280) != 0) {
            context.error(pos, "'push_constant' cannot be used with 'binding' or 'set'");
            success = false;
        }
        success &= modifiers.checkFlags(context, permittedFlags);
        return success & modifiers.checkLayoutFlags(context, permittedLayoutFlags);
    }

    private static boolean checkFields(@Nonnull Context context, @Nonnull Type.Field[] fields, int blockStorage) {
        boolean success = true;
        for (int i = 0; i < fields.length; i++) {
            Type.Field field = fields[i];
            Modifiers fieldModifiers = field.modifiers();
            int permittedFlags = 12400;
            int permittedLayoutFlags = 0;
            if ((fieldModifiers.flags() & 12400) != 0 && (fieldModifiers.flags() & 12400) != blockStorage) {
                context.error(field.modifiers().mPosition, "storage qualifier of a member must be storage qualifier '" + Modifiers.describeFlag(blockStorage) + "' of the block");
                success = false;
            }
            if ((blockStorage & 96) != 0) {
                permittedFlags |= 7;
                permittedLayoutFlags |= 4192;
            }
            if ((blockStorage & 4112) != 0) {
                permittedLayoutFlags |= 512;
            }
            if (blockStorage == 4096) {
                permittedFlags |= 3968;
            }
            success &= fieldModifiers.checkFlags(context, permittedFlags);
            success &= fieldModifiers.checkLayoutFlags(context, permittedLayoutFlags);
            if (field.type().isOpaque()) {
                context.error(field.position(), "opaque type '" + field.type().getName() + "' is not permitted in an interface block");
                success = false;
            }
            if (field.type().isUnsizedArray()) {
                if (blockStorage == 4096) {
                    if (i != fields.length - 1) {
                        context.error(field.position(), "runtime sized array must be the last member of a shader storage block");
                        success = false;
                    }
                } else {
                    context.error(field.position(), "runtime sized array is only permitted in shader storage blocks");
                    success = false;
                }
            }
        }
        return success;
    }

    @Nullable
    public static InterfaceBlock convert(@Nonnull Context context, int pos, @Nonnull Modifiers modifiers, @Nonnull Type blockType, @Nonnull String instanceName) {
        if (!blockType.getElementType().isInterfaceBlock()) {
            context.error(pos, "type '" + blockType + "' is not an interface block");
            return null;
        } else {
            boolean success = true;
            ShaderKind kind = context.getKind();
            if (!kind.isFragment() && !kind.isVertex() && !kind.isCompute()) {
                context.error(pos, "interface blocks are not allowed in this shader kind");
                success = false;
            }
            if ((modifiers.flags() & 8192) != 0) {
                context.error(pos, "workgroup qualifier is not allowed on an interface block");
                return null;
            } else {
                int blockStorage = modifiers.flags() & 12400;
                if (Integer.bitCount(blockStorage) != 1) {
                    context.error(pos, "an interface block must start with one of in, out, uniform, or buffer qualifier");
                    return null;
                } else {
                    if (kind.isVertex() && blockStorage == 32) {
                        context.error(pos, "an input block is not allowed in vertex shader kind");
                        success = false;
                    }
                    if (kind.isFragment() && blockStorage == 64) {
                        context.error(pos, "an output block is not allowed in fragment shader kind");
                        success = false;
                    }
                    success &= checkBlock(context, pos, modifiers, blockType, blockStorage);
                    success &= checkFields(context, blockType.getElementType().getFields(), blockStorage);
                    if (!success) {
                        return null;
                    } else {
                        Variable variable = Variable.convert(context, pos, modifiers, blockType, instanceName, (byte) 1);
                        return make(context, pos, variable);
                    }
                }
            }
        }
    }

    @Nonnull
    public static InterfaceBlock make(@Nonnull Context context, int pos, @Nonnull Variable variable) {
        assert variable.getType().getElementType().isInterfaceBlock();
        if (variable.getName().isEmpty()) {
            Type.Field[] fields = variable.getType().getFields();
            for (int i = 0; i < fields.length; i++) {
                context.getSymbolTable().insert(context, new AnonymousField(fields[i].position(), variable, i));
            }
        } else {
            context.getSymbolTable().insert(context, variable);
        }
        return new InterfaceBlock(pos, variable);
    }

    @Nonnull
    public Variable getVariable() {
        return (Variable) Objects.requireNonNull((Variable) this.mVariable.get(), "your symbol table is gone");
    }

    @Nonnull
    public String getBlockName() {
        return this.getVariable().getType().getElementType().getName();
    }

    @Nonnull
    public String getInstanceName() {
        return this.getVariable().getName();
    }

    @Override
    public Node.ElementKind getKind() {
        return Node.ElementKind.INTERFACE_BLOCK;
    }

    @Override
    public boolean accept(@Nonnull TreeVisitor visitor) {
        return visitor.visitInterfaceBlock(this);
    }

    @Nonnull
    @Override
    public String toString() {
        Variable variable = this.getVariable();
        StringBuilder result = new StringBuilder(variable.getModifiers().toString() + this.getBlockName() + " {\n");
        Type type = variable.getType();
        for (Type.Field field : type.getElementType().getFields()) {
            result.append(field.toString()).append("\n");
        }
        result.append("}");
        if (!this.getInstanceName().isEmpty()) {
            result.append(" ").append(this.getInstanceName());
            if (type.isArray()) {
                result.append("[").append(type.getArraySize()).append("]");
            }
        }
        result.append(";");
        return result.toString();
    }
}
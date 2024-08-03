package icyllis.arc3d.compiler.tree;

import icyllis.arc3d.compiler.Context;
import java.lang.ref.WeakReference;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class Variable extends Symbol {

    public static final byte kLocal_Storage = 0;

    public static final byte kGlobal_Storage = 1;

    public static final byte kParameter_Storage = 2;

    private final Modifiers mModifiers;

    private final Type mType;

    private final byte mStorage;

    private final boolean mBuiltin;

    private Node mDecl;

    private WeakReference<InterfaceBlock> mInterfaceBlock;

    public Variable(int position, Modifiers modifiers, Type type, String name, byte storage, boolean builtin) {
        super(position, name);
        this.mModifiers = modifiers;
        this.mType = type;
        this.mStorage = storage;
        this.mBuiltin = builtin;
    }

    @Nonnull
    public static Variable convert(@Nonnull Context context, int pos, @Nonnull Modifiers modifiers, @Nonnull Type type, @Nonnull String name, byte storage) {
        if (context.getKind().isCompute() && (modifiers.layoutFlags() & 4096) == 0 && storage == 1) {
            if ((modifiers.flags() & 32) != 0) {
                context.error(pos, "pipeline inputs not permitted in compute shaders");
            } else if ((modifiers.flags() & 64) != 0) {
                context.error(pos, "pipeline outputs not permitted in compute shaders");
            }
        }
        if (storage == 2 && (modifiers.flags() & 96) == 32) {
            modifiers.clearFlag(96);
        }
        return make(pos, modifiers, type, name, storage, context.isBuiltin());
    }

    @Nonnull
    public static Variable make(int pos, @Nonnull Modifiers modifiers, @Nonnull Type type, @Nonnull String name, byte storage, boolean builtin) {
        return new Variable(pos, modifiers, type, name, storage, builtin);
    }

    @Nonnull
    @Override
    public Node.SymbolKind getKind() {
        return Node.SymbolKind.VARIABLE;
    }

    @Nonnull
    @Override
    public Type getType() {
        return this.mType;
    }

    public Modifiers getModifiers() {
        return this.mModifiers;
    }

    public boolean isBuiltin() {
        return this.mBuiltin;
    }

    public byte getStorage() {
        return this.mStorage;
    }

    @Nullable
    public Expression initialValue() {
        VariableDecl decl = this.getVariableDecl();
        return decl != null ? decl.getInit() : null;
    }

    @Nullable
    public VariableDecl getVariableDecl() {
        if (this.mDecl instanceof VariableDecl) {
            return (VariableDecl) this.mDecl;
        } else {
            return this.mDecl instanceof GlobalVariableDecl ? ((GlobalVariableDecl) this.mDecl).getVariableDecl() : null;
        }
    }

    @Nullable
    public GlobalVariableDecl getGlobalVariableDecl() {
        return this.mDecl instanceof GlobalVariableDecl ? (GlobalVariableDecl) this.mDecl : null;
    }

    public void setVariableDecl(VariableDecl decl) {
        if (this.mDecl != null && decl.getVariable() != this) {
            throw new AssertionError();
        } else {
            if (this.mDecl == null) {
                this.mDecl = decl;
            }
        }
    }

    public void setGlobalVariableDecl(GlobalVariableDecl globalDecl) {
        if (this.mDecl != null && globalDecl.getVariableDecl().getVariable() != this) {
            throw new AssertionError();
        } else {
            this.mDecl = globalDecl;
        }
    }

    @Nullable
    public InterfaceBlock getInterfaceBlock() {
        return this.mInterfaceBlock != null ? (InterfaceBlock) this.mInterfaceBlock.get() : null;
    }

    public void setInterfaceBlock(InterfaceBlock interfaceBlock) {
        this.mInterfaceBlock = new WeakReference(interfaceBlock);
    }

    @Nonnull
    @Override
    public String toString() {
        return this.mModifiers.toString() + this.mType.getName() + " " + this.getName();
    }
}
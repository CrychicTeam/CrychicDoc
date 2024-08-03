package icyllis.arc3d.compiler.spirv;

import icyllis.arc3d.compiler.tree.Type;

class PointerLValue implements LValue {

    private final int mPointer;

    private final boolean mIsMemoryObject;

    private final int mType;

    private final boolean mRelaxedPrecision;

    private final int mStorageClass;

    PointerLValue(int pointer, boolean isMemoryObject, int type, boolean relaxedPrecision, int storageClass) {
        this.mPointer = pointer;
        this.mIsMemoryObject = isMemoryObject;
        this.mType = type;
        this.mRelaxedPrecision = relaxedPrecision;
        this.mStorageClass = storageClass;
    }

    @Override
    public int getPointer() {
        return this.mPointer;
    }

    @Override
    public int load(SPIRVCodeGenerator gen, Writer writer) {
        return gen.writeOpLoad(this.mType, this.mRelaxedPrecision, this.mPointer, writer);
    }

    @Override
    public void store(SPIRVCodeGenerator gen, int rvalue, Writer writer) {
        if (!this.mIsMemoryObject) {
            gen.mStoreCache.clear();
        }
        gen.writeOpStore(this.mStorageClass, this.mPointer, rvalue, writer);
    }

    @Override
    public boolean applySwizzle(byte[] components, Type newType) {
        return false;
    }
}
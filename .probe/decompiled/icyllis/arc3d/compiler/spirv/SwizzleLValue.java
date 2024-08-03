package icyllis.arc3d.compiler.spirv;

import icyllis.arc3d.compiler.tree.Type;

class SwizzleLValue implements LValue {

    private final int mBasePointer;

    private byte[] mComponents;

    private final Type mBaseType;

    private Type mResultType;

    private final int mStorageClass;

    SwizzleLValue(int basePointer, byte[] components, Type baseType, Type resultType, int storageClass) {
        this.mBasePointer = basePointer;
        this.mComponents = components;
        this.mBaseType = baseType;
        this.mResultType = resultType;
        this.mStorageClass = storageClass;
    }

    @Override
    public int getPointer() {
        return -1;
    }

    @Override
    public int load(SPIRVCodeGenerator gen, Writer writer) {
        int base = gen.getUniqueId(this.mBaseType);
        int baseType = gen.writeType(this.mBaseType);
        gen.writeInstruction(61, baseType, base, this.mBasePointer, writer);
        int result = gen.getUniqueId(this.mBaseType);
        gen.writeOpcode(79, 5 + this.mComponents.length, writer);
        int resultType = gen.writeType(this.mResultType);
        writer.writeWord(resultType);
        writer.writeWord(result);
        writer.writeWord(base);
        writer.writeWord(base);
        for (int component : this.mComponents) {
            writer.writeWord(component);
        }
        return result;
    }

    @Override
    public void store(SPIRVCodeGenerator gen, int rvalue, Writer writer) {
        int base = gen.getUniqueId(this.mBaseType);
        int baseType = gen.writeType(this.mBaseType);
        gen.writeInstruction(61, baseType, base, this.mBasePointer, writer);
        int shuffle = gen.getUniqueId(this.mBaseType);
        gen.writeOpcode(79, 5 + this.mBaseType.getRows(), writer);
        writer.writeWord(baseType);
        writer.writeWord(shuffle);
        writer.writeWord(base);
        writer.writeWord(rvalue);
        for (int i = 0; i < this.mBaseType.getRows(); i++) {
            int offset = i;
            for (int j = 0; j < this.mComponents.length; j++) {
                if (this.mComponents[j] == i) {
                    offset = j + this.mBaseType.getRows();
                    break;
                }
            }
            writer.writeWord(offset);
        }
        gen.writeOpStore(this.mStorageClass, this.mBasePointer, shuffle, writer);
    }

    @Override
    public boolean applySwizzle(byte[] components, Type newType) {
        byte[] newSwizzle = new byte[components.length];
        for (int i = 0; i < components.length; i++) {
            byte component = components[i];
            if (component < 0 || component >= this.mComponents.length) {
                assert false;
                return false;
            }
            newSwizzle[i] = this.mComponents[component];
        }
        this.mComponents = newSwizzle;
        this.mResultType = newType;
        return true;
    }
}
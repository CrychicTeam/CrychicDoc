package icyllis.arc3d.compiler.spirv;

import icyllis.arc3d.compiler.tree.Type;

interface LValue {

    int getPointer();

    int load(SPIRVCodeGenerator var1, Writer var2);

    void store(SPIRVCodeGenerator var1, int var2, Writer var3);

    boolean applySwizzle(byte[] var1, Type var2);
}
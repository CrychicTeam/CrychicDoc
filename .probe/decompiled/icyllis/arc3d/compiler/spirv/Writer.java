package icyllis.arc3d.compiler.spirv;

import icyllis.arc3d.compiler.Context;

interface Writer {

    void writeWord(int var1);

    void writeWords(int[] var1, int var2);

    void writeString8(Context var1, String var2);
}
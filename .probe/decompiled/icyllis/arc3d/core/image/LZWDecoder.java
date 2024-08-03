package icyllis.arc3d.core.image;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

public final class LZWDecoder {

    private static final ThreadLocal<LZWDecoder> TLS = ThreadLocal.withInitial(LZWDecoder::new);

    private static final int MAX_TABLE_SIZE = 4096;

    private ByteBuffer mData;

    private int mInitCodeSize;

    private int mClearCode;

    private int mEndOfInfo;

    private int mCodeSize;

    private int mCodeMask;

    private int mTableIndex;

    private int mPrevCode;

    private int mBlockPos;

    private int mBlockLength;

    private final byte[] mBlock = new byte[255];

    private int mInData;

    private int mInBits;

    private final int[] mPrefix = new int[4096];

    private final byte[] mSuffix = new byte[4096];

    private final byte[] mInitial = new byte[4096];

    private final int[] mLength = new int[4096];

    private final byte[] mString = new byte[4096];

    public static LZWDecoder getInstance() {
        return (LZWDecoder) TLS.get();
    }

    public byte[] setData(ByteBuffer data, int initCodeSize) {
        this.mData = data;
        this.mBlockPos = 0;
        this.mBlockLength = 0;
        this.mInData = 0;
        this.mInBits = 0;
        this.mInitCodeSize = initCodeSize;
        this.mClearCode = 1 << this.mInitCodeSize;
        this.mEndOfInfo = this.mClearCode + 1;
        this.initTable();
        return this.mString;
    }

    public int readString() {
        int code = this.getNextCode();
        if (code == this.mEndOfInfo) {
            return -1;
        } else {
            if (code == this.mClearCode) {
                this.initTable();
                code = this.getNextCode();
                if (code == this.mEndOfInfo) {
                    return -1;
                }
            } else {
                int newSuffixIndex;
                if (code < this.mTableIndex) {
                    newSuffixIndex = code;
                } else {
                    newSuffixIndex = this.mPrevCode;
                    if (code != this.mTableIndex) {
                        return -1;
                    }
                }
                if (this.mTableIndex < 4096) {
                    int tableIndex = this.mTableIndex;
                    int prevCode = this.mPrevCode;
                    this.mPrefix[tableIndex] = prevCode;
                    this.mSuffix[tableIndex] = this.mInitial[newSuffixIndex];
                    this.mInitial[tableIndex] = this.mInitial[prevCode];
                    this.mLength[tableIndex] = this.mLength[prevCode] + 1;
                    this.mTableIndex++;
                    if (this.mTableIndex == 1 << this.mCodeSize && this.mTableIndex < 4096) {
                        this.mCodeSize++;
                        this.mCodeMask = (1 << this.mCodeSize) - 1;
                    }
                }
            }
            int c = code;
            int len = this.mLength[code];
            for (int i = len - 1; i >= 0; i--) {
                this.mString[i] = this.mSuffix[c];
                c = this.mPrefix[c];
            }
            this.mPrevCode = code;
            return len;
        }
    }

    private void initTable() {
        int size = 1 << this.mInitCodeSize;
        for (int i = 0; i < size; i++) {
            this.mPrefix[i] = -1;
            this.mSuffix[i] = (byte) i;
            this.mInitial[i] = (byte) i;
            this.mLength[i] = 1;
        }
        for (int i = size; i < 4096; i++) {
            this.mPrefix[i] = -1;
            this.mSuffix[i] = 0;
            this.mInitial[i] = 0;
            this.mLength[i] = 1;
        }
        this.mCodeSize = this.mInitCodeSize + 1;
        this.mCodeMask = (1 << this.mCodeSize) - 1;
        this.mTableIndex = size + 2;
        this.mPrevCode = 0;
    }

    private int getNextCode() {
        while (this.mInBits < this.mCodeSize) {
            if (this.mBlockPos == this.mBlockLength) {
                this.mBlockPos = 0;
                try {
                    if ((this.mBlockLength = this.mData.get() & 255) <= 0) {
                        return this.mEndOfInfo;
                    }
                    this.mData.get(this.mBlock, 0, this.mBlockLength);
                } catch (BufferUnderflowException var2) {
                    return this.mEndOfInfo;
                }
            }
            this.mInData = this.mInData | (this.mBlock[this.mBlockPos++] & 255) << this.mInBits;
            this.mInBits += 8;
        }
        int code = this.mInData & this.mCodeMask;
        this.mInBits = this.mInBits - this.mCodeSize;
        this.mInData = this.mInData >>> this.mCodeSize;
        return code;
    }
}
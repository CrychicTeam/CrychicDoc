package org.embeddedt.embeddium.taint.scanning;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class ClassConstantPoolParser {

    private static final int UTF8 = 1;

    private static final int INT = 3;

    private static final int FLOAT = 4;

    private static final int LONG = 5;

    private static final int DOUBLE = 6;

    private static final int FIELD = 9;

    private static final int METH = 10;

    private static final int IMETH = 11;

    private static final int NAME_TYPE = 12;

    private static final int HANDLE = 15;

    private static final int CONDY = 17;

    private static final int INDY = 18;

    private final ArrayList<byte[]> BYTES_TO_SEARCH;

    public ClassConstantPoolParser(String... strings) {
        this.BYTES_TO_SEARCH = new ArrayList(strings.length);
        for (int i = 0; i < strings.length; i++) {
            this.BYTES_TO_SEARCH.add(i, strings[i].getBytes(StandardCharsets.UTF_8));
        }
    }

    public void addString(String string) {
        this.BYTES_TO_SEARCH.add(string.getBytes(StandardCharsets.UTF_8));
    }

    public boolean find(byte[] basicClass) {
        return this.find(basicClass, false);
    }

    public boolean find(byte[] basicClass, boolean prefixes) {
        if (basicClass != null && basicClass.length != 0) {
            int n = readUnsignedShort(8, basicClass);
            int index = 10;
            for (int i = 1; i < n; i++) {
                int size;
                switch(basicClass[index]) {
                    case 1:
                        int strLen = readUnsignedShort(index + 1, basicClass);
                        size = 3 + strLen;
                        for (byte[] bytes : this.BYTES_TO_SEARCH) {
                            if (prefixes ? strLen >= bytes.length : strLen == bytes.length) {
                                boolean found = true;
                                for (int j = index + 3; j < index + 3 + bytes.length; j++) {
                                    if (basicClass[j] != bytes[j - (index + 3)]) {
                                        found = false;
                                        break;
                                    }
                                }
                                if (found) {
                                    return true;
                                }
                            }
                        }
                        break;
                    case 2:
                    case 7:
                    case 8:
                    case 13:
                    case 14:
                    case 16:
                    default:
                        size = 3;
                        break;
                    case 3:
                    case 4:
                    case 9:
                    case 10:
                    case 11:
                    case 12:
                    case 17:
                    case 18:
                        size = 5;
                        break;
                    case 5:
                    case 6:
                        size = 9;
                        i++;
                        break;
                    case 15:
                        size = 4;
                }
                index += size;
            }
            return false;
        } else {
            return false;
        }
    }

    private static short readShort(int index, byte[] basicClass) {
        return (short) ((basicClass[index] & 255) << 8 | basicClass[index + 1] & 255);
    }

    private static int readUnsignedShort(int index, byte[] basicClass) {
        return (basicClass[index] & 0xFF) << 8 | basicClass[index + 1] & 0xFF;
    }
}
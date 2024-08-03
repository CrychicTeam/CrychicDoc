package io.redspace.ironsspellbooks.util;

import io.redspace.ironsspellbooks.datafix.ParallelMatcher;
import java.io.DataInputStream;
import java.io.IOException;

public class ByteHelper {

    public static int indexOf(byte[] array, int length, byte[] target) {
        if (array != null && target != null && target.length != 0 && length != 0) {
            label29: for (int i = 0; i < length - target.length + 1; i++) {
                for (int j = 0; j < target.length; j++) {
                    if (array[i + j] != target[j]) {
                        continue label29;
                    }
                }
                return i;
            }
            return -1;
        } else {
            return -1;
        }
    }

    public static int indexOf(DataInputStream dataInputStream, byte[] target) throws IOException {
        if (dataInputStream != null && target != null && target.length != 0) {
            int index = -1;
            label32: while (true) {
                int data = dataInputStream.read();
                index++;
                int addlReadCount = 0;
                for (int j = 0; j < target.length; j++) {
                    if (data != target[j]) {
                        index += addlReadCount;
                        if (data == -1) {
                            break label32;
                        }
                        continue label32;
                    }
                    if (j < target.length - 1) {
                        data = dataInputStream.read();
                        addlReadCount++;
                        if (data == -1) {
                            return -1;
                        }
                    }
                }
                return index;
            }
            return -1;
        } else {
            return -1;
        }
    }

    public static int indexOf(DataInputStream dataInputStream, ParallelMatcher parallelMatcher) throws IOException {
        if (dataInputStream != null && parallelMatcher != null) {
            int index = -1;
            int data;
            do {
                data = dataInputStream.read();
                index++;
                if (parallelMatcher.pushValue(data)) {
                    return index;
                }
            } while (data != -1);
            return -1;
        } else {
            return -1;
        }
    }
}
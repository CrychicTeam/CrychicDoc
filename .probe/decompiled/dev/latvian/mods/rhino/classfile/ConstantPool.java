package dev.latvian.mods.rhino.classfile;

import dev.latvian.mods.rhino.ObjToIntMap;
import dev.latvian.mods.rhino.UintMap;

final class ConstantPool {

    static final byte CONSTANT_Class = 7;

    static final byte CONSTANT_Fieldref = 9;

    static final byte CONSTANT_Methodref = 10;

    static final byte CONSTANT_InterfaceMethodref = 11;

    static final byte CONSTANT_String = 8;

    static final byte CONSTANT_Integer = 3;

    static final byte CONSTANT_Float = 4;

    static final byte CONSTANT_Long = 5;

    static final byte CONSTANT_Double = 6;

    static final byte CONSTANT_NameAndType = 12;

    static final byte CONSTANT_Utf8 = 1;

    static final byte CONSTANT_MethodType = 16;

    static final byte CONSTANT_MethodHandle = 15;

    static final byte CONSTANT_InvokeDynamic = 18;

    private static final int ConstantPoolSize = 256;

    private static final int MAX_UTF_ENCODING_SIZE = 65535;

    private final ClassFileWriter cfw;

    private final UintMap itsStringConstHash = new UintMap();

    private final ObjToIntMap itsUtf8Hash = new ObjToIntMap();

    private final ObjToIntMap itsFieldRefHash = new ObjToIntMap();

    private final ObjToIntMap itsMethodRefHash = new ObjToIntMap();

    private final ObjToIntMap itsClassHash = new ObjToIntMap();

    private final ObjToIntMap itsConstantHash = new ObjToIntMap();

    private final UintMap itsConstantData = new UintMap();

    private final UintMap itsPoolTypes = new UintMap();

    private int itsTop;

    private int itsTopIndex;

    private byte[] itsPool;

    ConstantPool(ClassFileWriter cfw) {
        this.cfw = cfw;
        this.itsTopIndex = 1;
        this.itsPool = new byte[256];
        this.itsTop = 0;
    }

    int write(byte[] data, int offset) {
        offset = ClassFileWriter.putInt16((short) this.itsTopIndex, data, offset);
        System.arraycopy(this.itsPool, 0, data, offset, this.itsTop);
        return offset + this.itsTop;
    }

    int getWriteSize() {
        return 2 + this.itsTop;
    }

    int addConstant(int k) {
        this.ensure(5);
        this.itsPool[this.itsTop++] = 3;
        this.itsTop = ClassFileWriter.putInt32(k, this.itsPool, this.itsTop);
        this.itsPoolTypes.put(this.itsTopIndex, 3);
        return (short) (this.itsTopIndex++);
    }

    int addConstant(long k) {
        this.ensure(9);
        this.itsPool[this.itsTop++] = 5;
        this.itsTop = ClassFileWriter.putInt64(k, this.itsPool, this.itsTop);
        int index = this.itsTopIndex;
        this.itsTopIndex += 2;
        this.itsPoolTypes.put(index, 5);
        return index;
    }

    int addConstant(float k) {
        this.ensure(5);
        this.itsPool[this.itsTop++] = 4;
        int bits = Float.floatToIntBits(k);
        this.itsTop = ClassFileWriter.putInt32(bits, this.itsPool, this.itsTop);
        this.itsPoolTypes.put(this.itsTopIndex, 4);
        return this.itsTopIndex++;
    }

    int addConstant(double k) {
        this.ensure(9);
        this.itsPool[this.itsTop++] = 6;
        long bits = Double.doubleToLongBits(k);
        this.itsTop = ClassFileWriter.putInt64(bits, this.itsPool, this.itsTop);
        int index = this.itsTopIndex;
        this.itsTopIndex += 2;
        this.itsPoolTypes.put(index, 6);
        return index;
    }

    int addConstant(String k) {
        int utf8Index = '\uffff' & this.addUtf8(k);
        int theIndex = this.itsStringConstHash.getInt(utf8Index, -1);
        if (theIndex == -1) {
            theIndex = this.itsTopIndex++;
            this.ensure(3);
            this.itsPool[this.itsTop++] = 8;
            this.itsTop = ClassFileWriter.putInt16(utf8Index, this.itsPool, this.itsTop);
            this.itsStringConstHash.put(utf8Index, theIndex);
        }
        this.itsPoolTypes.put(theIndex, 8);
        return theIndex;
    }

    int addConstant(Object value) {
        if (value instanceof Integer || value instanceof Byte || value instanceof Short) {
            return this.addConstant(((Number) value).intValue());
        } else if (value instanceof Character) {
            return this.addConstant(((Character) value).charValue());
        } else if (value instanceof Boolean) {
            return this.addConstant((Boolean) value ? 1 : 0);
        } else if (value instanceof Float) {
            return this.addConstant(((Float) value).floatValue());
        } else if (value instanceof Long) {
            return this.addConstant(((Long) value).longValue());
        } else if (value instanceof Double) {
            return this.addConstant(((Double) value).doubleValue());
        } else if (value instanceof String) {
            return this.addConstant((String) value);
        } else if (value instanceof ClassFileWriter.MHandle) {
            return this.addMethodHandle((ClassFileWriter.MHandle) value);
        } else {
            throw new IllegalArgumentException("value " + value);
        }
    }

    boolean isUnderUtfEncodingLimit(String s) {
        int strLen = s.length();
        if (strLen * 3 <= 65535) {
            return true;
        } else {
            return strLen > 65535 ? false : strLen == this.getUtfEncodingLimit(s, 0, strLen);
        }
    }

    int getUtfEncodingLimit(String s, int start, int end) {
        if ((end - start) * 3 <= 65535) {
            return end;
        } else {
            int limit = 65535;
            for (int i = start; i != end; i++) {
                int c = s.charAt(i);
                if (0 != c && c <= 127) {
                    limit--;
                } else if (c < 2047) {
                    limit -= 2;
                } else {
                    limit -= 3;
                }
                if (limit < 0) {
                    return i;
                }
            }
            return end;
        }
    }

    short addUtf8(String k) {
        int theIndex = this.itsUtf8Hash.get(k, -1);
        if (theIndex == -1) {
            int strLen = k.length();
            boolean tooBigString;
            if (strLen > 65535) {
                tooBigString = true;
            } else {
                tooBigString = false;
                this.ensure(3 + strLen * 3);
                int top = this.itsTop;
                this.itsPool[top++] = 1;
                top += 2;
                char[] chars = this.cfw.getCharBuffer(strLen);
                k.getChars(0, strLen, chars, 0);
                for (int i = 0; i != strLen; i++) {
                    int c = chars[i];
                    if (c != 0 && c <= 127) {
                        this.itsPool[top++] = (byte) c;
                    } else if (c > 2047) {
                        this.itsPool[top++] = (byte) (224 | c >> 12);
                        this.itsPool[top++] = (byte) (128 | c >> 6 & 63);
                        this.itsPool[top++] = (byte) (128 | c & 63);
                    } else {
                        this.itsPool[top++] = (byte) (192 | c >> 6);
                        this.itsPool[top++] = (byte) (128 | c & 63);
                    }
                }
                int utfLen = top - (this.itsTop + 1 + 2);
                if (utfLen > 65535) {
                    tooBigString = true;
                } else {
                    this.itsPool[this.itsTop + 1] = (byte) (utfLen >>> 8);
                    this.itsPool[this.itsTop + 2] = (byte) utfLen;
                    this.itsTop = top;
                    theIndex = this.itsTopIndex++;
                    this.itsUtf8Hash.put(k, theIndex);
                }
            }
            if (tooBigString) {
                throw new IllegalArgumentException("Too big string");
            }
        }
        this.setConstantData(theIndex, k);
        this.itsPoolTypes.put(theIndex, 1);
        return (short) theIndex;
    }

    private short addNameAndType(String name, String type) {
        short nameIndex = this.addUtf8(name);
        short typeIndex = this.addUtf8(type);
        this.ensure(5);
        this.itsPool[this.itsTop++] = 12;
        this.itsTop = ClassFileWriter.putInt16(nameIndex, this.itsPool, this.itsTop);
        this.itsTop = ClassFileWriter.putInt16(typeIndex, this.itsPool, this.itsTop);
        this.itsPoolTypes.put(this.itsTopIndex, 12);
        return (short) (this.itsTopIndex++);
    }

    short addClass(String className) {
        int theIndex = this.itsClassHash.get(className, -1);
        if (theIndex == -1) {
            String slashed = className;
            if (className.indexOf(46) > 0) {
                slashed = ClassFileWriter.getSlashedForm(className);
                theIndex = this.itsClassHash.get(slashed, -1);
                if (theIndex != -1) {
                    this.itsClassHash.put(className, theIndex);
                }
            }
            if (theIndex == -1) {
                int utf8Index = this.addUtf8(slashed);
                this.ensure(3);
                this.itsPool[this.itsTop++] = 7;
                this.itsTop = ClassFileWriter.putInt16(utf8Index, this.itsPool, this.itsTop);
                theIndex = this.itsTopIndex++;
                this.itsClassHash.put(slashed, theIndex);
                if (!className.equals(slashed)) {
                    this.itsClassHash.put(className, theIndex);
                }
            }
        }
        this.setConstantData(theIndex, className);
        this.itsPoolTypes.put(theIndex, 7);
        return (short) theIndex;
    }

    short addFieldRef(String className, String fieldName, String fieldType) {
        FieldOrMethodRef ref = new FieldOrMethodRef(className, fieldName, fieldType);
        int theIndex = this.itsFieldRefHash.get(ref, -1);
        if (theIndex == -1) {
            short ntIndex = this.addNameAndType(fieldName, fieldType);
            short classIndex = this.addClass(className);
            this.ensure(5);
            this.itsPool[this.itsTop++] = 9;
            this.itsTop = ClassFileWriter.putInt16(classIndex, this.itsPool, this.itsTop);
            this.itsTop = ClassFileWriter.putInt16(ntIndex, this.itsPool, this.itsTop);
            theIndex = this.itsTopIndex++;
            this.itsFieldRefHash.put(ref, theIndex);
        }
        this.setConstantData(theIndex, ref);
        this.itsPoolTypes.put(theIndex, 9);
        return (short) theIndex;
    }

    short addMethodRef(String className, String methodName, String methodType) {
        FieldOrMethodRef ref = new FieldOrMethodRef(className, methodName, methodType);
        int theIndex = this.itsMethodRefHash.get(ref, -1);
        if (theIndex == -1) {
            short ntIndex = this.addNameAndType(methodName, methodType);
            short classIndex = this.addClass(className);
            this.ensure(5);
            this.itsPool[this.itsTop++] = 10;
            this.itsTop = ClassFileWriter.putInt16(classIndex, this.itsPool, this.itsTop);
            this.itsTop = ClassFileWriter.putInt16(ntIndex, this.itsPool, this.itsTop);
            theIndex = this.itsTopIndex++;
            this.itsMethodRefHash.put(ref, theIndex);
        }
        this.setConstantData(theIndex, ref);
        this.itsPoolTypes.put(theIndex, 10);
        return (short) theIndex;
    }

    short addInterfaceMethodRef(String className, String methodName, String methodType) {
        short ntIndex = this.addNameAndType(methodName, methodType);
        short classIndex = this.addClass(className);
        this.ensure(5);
        this.itsPool[this.itsTop++] = 11;
        this.itsTop = ClassFileWriter.putInt16(classIndex, this.itsPool, this.itsTop);
        this.itsTop = ClassFileWriter.putInt16(ntIndex, this.itsPool, this.itsTop);
        FieldOrMethodRef r = new FieldOrMethodRef(className, methodName, methodType);
        this.setConstantData(this.itsTopIndex, r);
        this.itsPoolTypes.put(this.itsTopIndex, 11);
        return (short) (this.itsTopIndex++);
    }

    short addInvokeDynamic(String methodName, String methodType, int bootstrapIndex) {
        ConstantEntry entry = new ConstantEntry(18, bootstrapIndex, methodName, methodType);
        int theIndex = this.itsConstantHash.get(entry, -1);
        if (theIndex == -1) {
            short nameTypeIndex = this.addNameAndType(methodName, methodType);
            this.ensure(5);
            this.itsPool[this.itsTop++] = 18;
            this.itsTop = ClassFileWriter.putInt16(bootstrapIndex, this.itsPool, this.itsTop);
            this.itsTop = ClassFileWriter.putInt16(nameTypeIndex, this.itsPool, this.itsTop);
            theIndex = this.itsTopIndex++;
            this.itsConstantHash.put(entry, theIndex);
            this.setConstantData(theIndex, methodType);
            this.itsPoolTypes.put(theIndex, 18);
        }
        return (short) theIndex;
    }

    short addMethodHandle(ClassFileWriter.MHandle mh) {
        int theIndex = this.itsConstantHash.get(mh, -1);
        if (theIndex == -1) {
            short ref;
            if (mh.tag <= 4) {
                ref = this.addFieldRef(mh.owner, mh.name, mh.desc);
            } else if (mh.tag == 9) {
                ref = this.addInterfaceMethodRef(mh.owner, mh.name, mh.desc);
            } else {
                ref = this.addMethodRef(mh.owner, mh.name, mh.desc);
            }
            this.ensure(4);
            this.itsPool[this.itsTop++] = 15;
            this.itsPool[this.itsTop++] = mh.tag;
            this.itsTop = ClassFileWriter.putInt16(ref, this.itsPool, this.itsTop);
            theIndex = this.itsTopIndex++;
            this.itsConstantHash.put(mh, theIndex);
            this.itsPoolTypes.put(theIndex, 15);
        }
        return (short) theIndex;
    }

    Object getConstantData(int index) {
        return this.itsConstantData.getObject(index);
    }

    void setConstantData(int index, Object data) {
        this.itsConstantData.put(index, data);
    }

    byte getConstantType(int index) {
        return (byte) this.itsPoolTypes.getInt(index, 0);
    }

    private void ensure(int howMuch) {
        if (this.itsTop + howMuch > this.itsPool.length) {
            int newCapacity = this.itsPool.length * 2;
            if (this.itsTop + howMuch > newCapacity) {
                newCapacity = this.itsTop + howMuch;
            }
            byte[] tmp = new byte[newCapacity];
            System.arraycopy(this.itsPool, 0, tmp, 0, this.itsTop);
            this.itsPool = tmp;
        }
    }
}
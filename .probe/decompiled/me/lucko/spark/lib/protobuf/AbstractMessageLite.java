package me.lucko.spark.lib.protobuf;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class AbstractMessageLite<MessageType extends AbstractMessageLite<MessageType, BuilderType>, BuilderType extends AbstractMessageLite.Builder<MessageType, BuilderType>> implements MessageLite {

    protected int memoizedHashCode = 0;

    @Override
    public ByteString toByteString() {
        try {
            ByteString.CodedBuilder out = ByteString.newCodedBuilder(this.getSerializedSize());
            this.writeTo(out.getCodedOutput());
            return out.build();
        } catch (IOException var2) {
            throw new RuntimeException(this.getSerializingExceptionMessage("ByteString"), var2);
        }
    }

    @Override
    public byte[] toByteArray() {
        try {
            byte[] result = new byte[this.getSerializedSize()];
            CodedOutputStream output = CodedOutputStream.newInstance(result);
            this.writeTo(output);
            output.checkNoSpaceLeft();
            return result;
        } catch (IOException var3) {
            throw new RuntimeException(this.getSerializingExceptionMessage("byte array"), var3);
        }
    }

    @Override
    public void writeTo(OutputStream output) throws IOException {
        int bufferSize = CodedOutputStream.computePreferredBufferSize(this.getSerializedSize());
        CodedOutputStream codedOutput = CodedOutputStream.newInstance(output, bufferSize);
        this.writeTo(codedOutput);
        codedOutput.flush();
    }

    @Override
    public void writeDelimitedTo(OutputStream output) throws IOException {
        int serialized = this.getSerializedSize();
        int bufferSize = CodedOutputStream.computePreferredBufferSize(CodedOutputStream.computeUInt32SizeNoTag(serialized) + serialized);
        CodedOutputStream codedOutput = CodedOutputStream.newInstance(output, bufferSize);
        codedOutput.writeUInt32NoTag(serialized);
        this.writeTo(codedOutput);
        codedOutput.flush();
    }

    int getMemoizedSerializedSize() {
        throw new UnsupportedOperationException();
    }

    void setMemoizedSerializedSize(int size) {
        throw new UnsupportedOperationException();
    }

    int getSerializedSize(Schema schema) {
        int memoizedSerializedSize = this.getMemoizedSerializedSize();
        if (memoizedSerializedSize == -1) {
            memoizedSerializedSize = schema.getSerializedSize(this);
            this.setMemoizedSerializedSize(memoizedSerializedSize);
        }
        return memoizedSerializedSize;
    }

    UninitializedMessageException newUninitializedMessageException() {
        return new UninitializedMessageException(this);
    }

    private String getSerializingExceptionMessage(String target) {
        return "Serializing " + this.getClass().getName() + " to a " + target + " threw an IOException (should never happen).";
    }

    protected static void checkByteStringIsUtf8(ByteString byteString) throws IllegalArgumentException {
        if (!byteString.isValidUtf8()) {
            throw new IllegalArgumentException("Byte string is not UTF-8.");
        }
    }

    @Deprecated
    protected static <T> void addAll(Iterable<T> values, Collection<? super T> list) {
        AbstractMessageLite.Builder.addAll(values, (List<? super T>) list);
    }

    protected static <T> void addAll(Iterable<T> values, List<? super T> list) {
        AbstractMessageLite.Builder.addAll(values, list);
    }

    public abstract static class Builder<MessageType extends AbstractMessageLite<MessageType, BuilderType>, BuilderType extends AbstractMessageLite.Builder<MessageType, BuilderType>> implements MessageLite.Builder {

        public abstract BuilderType clone();

        public BuilderType mergeFrom(CodedInputStream input) throws IOException {
            return this.mergeFrom(input, ExtensionRegistryLite.getEmptyRegistry());
        }

        public abstract BuilderType mergeFrom(CodedInputStream var1, ExtensionRegistryLite var2) throws IOException;

        public BuilderType mergeFrom(ByteString data) throws InvalidProtocolBufferException {
            try {
                CodedInputStream input = data.newCodedInput();
                this.mergeFrom(input);
                input.checkLastTagWas(0);
                return (BuilderType) this;
            } catch (InvalidProtocolBufferException var3) {
                throw var3;
            } catch (IOException var4) {
                throw new RuntimeException(this.getReadingExceptionMessage("ByteString"), var4);
            }
        }

        public BuilderType mergeFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            try {
                CodedInputStream input = data.newCodedInput();
                this.mergeFrom(input, extensionRegistry);
                input.checkLastTagWas(0);
                return (BuilderType) this;
            } catch (InvalidProtocolBufferException var4) {
                throw var4;
            } catch (IOException var5) {
                throw new RuntimeException(this.getReadingExceptionMessage("ByteString"), var5);
            }
        }

        public BuilderType mergeFrom(byte[] data) throws InvalidProtocolBufferException {
            return this.mergeFrom(data, 0, data.length);
        }

        public BuilderType mergeFrom(byte[] data, int off, int len) throws InvalidProtocolBufferException {
            try {
                CodedInputStream input = CodedInputStream.newInstance(data, off, len);
                this.mergeFrom(input);
                input.checkLastTagWas(0);
                return (BuilderType) this;
            } catch (InvalidProtocolBufferException var5) {
                throw var5;
            } catch (IOException var6) {
                throw new RuntimeException(this.getReadingExceptionMessage("byte array"), var6);
            }
        }

        public BuilderType mergeFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return this.mergeFrom(data, 0, data.length, extensionRegistry);
        }

        public BuilderType mergeFrom(byte[] data, int off, int len, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            try {
                CodedInputStream input = CodedInputStream.newInstance(data, off, len);
                this.mergeFrom(input, extensionRegistry);
                input.checkLastTagWas(0);
                return (BuilderType) this;
            } catch (InvalidProtocolBufferException var6) {
                throw var6;
            } catch (IOException var7) {
                throw new RuntimeException(this.getReadingExceptionMessage("byte array"), var7);
            }
        }

        public BuilderType mergeFrom(InputStream input) throws IOException {
            CodedInputStream codedInput = CodedInputStream.newInstance(input);
            this.mergeFrom(codedInput);
            codedInput.checkLastTagWas(0);
            return (BuilderType) this;
        }

        public BuilderType mergeFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            CodedInputStream codedInput = CodedInputStream.newInstance(input);
            this.mergeFrom(codedInput, extensionRegistry);
            codedInput.checkLastTagWas(0);
            return (BuilderType) this;
        }

        @Override
        public boolean mergeDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            int firstByte = input.read();
            if (firstByte == -1) {
                return false;
            } else {
                int size = CodedInputStream.readRawVarint32(firstByte, input);
                InputStream limitedInput = new AbstractMessageLite.Builder.LimitedInputStream(input, size);
                this.mergeFrom(limitedInput, extensionRegistry);
                return true;
            }
        }

        @Override
        public boolean mergeDelimitedFrom(InputStream input) throws IOException {
            return this.mergeDelimitedFrom(input, ExtensionRegistryLite.getEmptyRegistry());
        }

        public BuilderType mergeFrom(MessageLite other) {
            if (!this.getDefaultInstanceForType().getClass().isInstance(other)) {
                throw new IllegalArgumentException("mergeFrom(MessageLite) can only merge messages of the same type.");
            } else {
                return this.internalMergeFrom((MessageType) other);
            }
        }

        protected abstract BuilderType internalMergeFrom(MessageType var1);

        private String getReadingExceptionMessage(String target) {
            return "Reading " + this.getClass().getName() + " from a " + target + " threw an IOException (should never happen).";
        }

        private static <T> void addAllCheckingNulls(Iterable<T> values, List<? super T> list) {
            if (list instanceof ArrayList && values instanceof Collection) {
                ((ArrayList) list).ensureCapacity(list.size() + ((Collection) values).size());
            }
            int begin = list.size();
            for (T value : values) {
                if (value == null) {
                    String message = "Element at index " + (list.size() - begin) + " is null.";
                    for (int i = list.size() - 1; i >= begin; i--) {
                        list.remove(i);
                    }
                    throw new NullPointerException(message);
                }
                list.add(value);
            }
        }

        protected static UninitializedMessageException newUninitializedMessageException(MessageLite message) {
            return new UninitializedMessageException(message);
        }

        @Deprecated
        protected static <T> void addAll(Iterable<T> values, Collection<? super T> list) {
            addAll(values, (List<? super T>) list);
        }

        protected static <T> void addAll(Iterable<T> values, List<? super T> list) {
            Internal.checkNotNull(values);
            if (values instanceof LazyStringList) {
                List<?> lazyValues = ((LazyStringList) values).getUnderlyingElements();
                LazyStringList lazyList = (LazyStringList) list;
                int begin = list.size();
                for (Object value : lazyValues) {
                    if (value == null) {
                        String message = "Element at index " + (lazyList.size() - begin) + " is null.";
                        for (int i = lazyList.size() - 1; i >= begin; i--) {
                            lazyList.remove(i);
                        }
                        throw new NullPointerException(message);
                    }
                    if (value instanceof ByteString) {
                        lazyList.add((ByteString) value);
                    } else {
                        lazyList.add((String) value);
                    }
                }
            } else if (values instanceof PrimitiveNonBoxingCollection) {
                list.addAll((Collection) values);
            } else {
                addAllCheckingNulls(values, list);
            }
        }

        static final class LimitedInputStream extends FilterInputStream {

            private int limit;

            LimitedInputStream(InputStream in, int limit) {
                super(in);
                this.limit = limit;
            }

            public int available() throws IOException {
                return Math.min(super.available(), this.limit);
            }

            public int read() throws IOException {
                if (this.limit <= 0) {
                    return -1;
                } else {
                    int result = super.read();
                    if (result >= 0) {
                        this.limit--;
                    }
                    return result;
                }
            }

            public int read(byte[] b, int off, int len) throws IOException {
                if (this.limit <= 0) {
                    return -1;
                } else {
                    len = Math.min(len, this.limit);
                    int result = super.read(b, off, len);
                    if (result >= 0) {
                        this.limit -= result;
                    }
                    return result;
                }
            }

            public long skip(long n) throws IOException {
                int result = (int) super.skip(Math.min(n, (long) this.limit));
                if (result >= 0) {
                    this.limit -= result;
                }
                return (long) result;
            }
        }
    }

    protected interface InternalOneOfEnum {

        int getNumber();
    }
}
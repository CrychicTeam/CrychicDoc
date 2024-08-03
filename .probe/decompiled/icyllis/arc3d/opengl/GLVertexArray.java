package icyllis.arc3d.opengl;

import icyllis.arc3d.core.SharedPtr;
import icyllis.arc3d.engine.GeometryProcessor;
import icyllis.arc3d.engine.GpuResource;
import icyllis.arc3d.engine.ManagedResource;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class GLVertexArray extends ManagedResource {

    private static final int INVALID_BINDING = -1;

    private int mVertexArray;

    private final int mVertexBinding;

    private final int mInstanceBinding;

    private final int mVertexStride;

    private final int mInstanceStride;

    private final int mNumVertexLocations;

    private final int mNumInstanceLocations;

    private final boolean mDSAElementBuffer;

    private final int[] mAttributes;

    private GpuResource.UniqueID mIndexBuffer;

    private GpuResource.UniqueID mVertexBuffer;

    private GpuResource.UniqueID mInstanceBuffer;

    private long mVertexOffset;

    private long mInstanceOffset;

    private GLVertexArray(GLDevice device, int vertexArray, int vertexBinding, int instanceBinding, int vertexStride, int instanceStride, int numVertexLocations, int numInstanceLocations, int[] attributes) {
        super(device);
        assert vertexArray != 0;
        assert vertexBinding == -1 || vertexStride > 0;
        assert instanceBinding == -1 || instanceStride > 0;
        this.mVertexArray = vertexArray;
        this.mVertexBinding = vertexBinding;
        this.mInstanceBinding = instanceBinding;
        this.mVertexStride = vertexStride;
        this.mInstanceStride = instanceStride;
        this.mNumVertexLocations = numVertexLocations;
        this.mNumInstanceLocations = numInstanceLocations;
        this.mAttributes = attributes;
        this.mDSAElementBuffer = attributes == null && !device.getCaps().dsaElementBufferBroken();
    }

    @Nullable
    @SharedPtr
    public static GLVertexArray make(@Nonnull GLDevice device, @Nonnull GeometryProcessor geomProc) {
        boolean dsa = device.getCaps().hasDSASupport();
        int vertexArray;
        if (dsa) {
            vertexArray = GLCore.glCreateVertexArrays();
        } else {
            vertexArray = GLCore.glGenVertexArrays();
        }
        if (vertexArray == 0) {
            return null;
        } else {
            int oldVertexArray = 0;
            if (!dsa) {
                oldVertexArray = GLCore.glGetInteger(34229);
                GLCore.glBindVertexArray(vertexArray);
            }
            int index = 0;
            int bindingIndex = 0;
            int vertexBinding = -1;
            int instanceBinding = -1;
            int numVertexLocations = 0;
            int numInstanceLocations = 0;
            if (geomProc.hasVertexAttributes()) {
                if (dsa) {
                    int prevIndex = index;
                    index = set_vertex_format_4(geomProc.vertexAttributes(), vertexArray, index, bindingIndex, 0);
                    numVertexLocations = index - prevIndex;
                    vertexBinding = bindingIndex++;
                } else {
                    numVertexLocations = geomProc.numVertexLocations();
                    index += numVertexLocations;
                }
            }
            if (geomProc.hasInstanceAttributes()) {
                if (dsa) {
                    int prevIndex = index;
                    index = set_vertex_format_4(geomProc.instanceAttributes(), vertexArray, index, bindingIndex, 1);
                    numInstanceLocations = index - prevIndex;
                    instanceBinding = bindingIndex;
                } else {
                    numInstanceLocations = geomProc.numInstanceLocations();
                    index += numInstanceLocations;
                }
            }
            assert index == numVertexLocations + numInstanceLocations;
            if (index > device.getCaps().maxVertexAttributes()) {
                GLCore.glDeleteVertexArrays(vertexArray);
                if (!dsa) {
                    GLCore.glBindVertexArray(oldVertexArray);
                }
                return null;
            } else {
                int[] attributes = null;
                if (!dsa) {
                    attributes = new int[index];
                    index = 0;
                    if (numVertexLocations > 0) {
                        index = set_vertex_format_3(geomProc.vertexAttributes(), index, 0, attributes);
                    }
                    if (numInstanceLocations > 0) {
                        index = set_vertex_format_3(geomProc.instanceAttributes(), index, 1, attributes);
                    }
                    assert index == numVertexLocations + numInstanceLocations;
                    GLCore.glBindVertexArray(oldVertexArray);
                }
                if (device.getCaps().hasDebugSupport()) {
                    String label = geomProc.name();
                    if (!label.isEmpty()) {
                        label = label.substring(0, Math.min(label.length(), device.getCaps().maxLabelLength()));
                        GLCore.glObjectLabel(32884, vertexArray, label);
                    }
                }
                return new GLVertexArray(device, vertexArray, vertexBinding, instanceBinding, geomProc.vertexStride(), geomProc.instanceStride(), numVertexLocations, numInstanceLocations, attributes);
            }
        }
    }

    private static int set_vertex_format_3(@Nonnull Iterable<GeometryProcessor.Attribute> attribs, int index, int divisor, int[] attributes) {
        for (GeometryProcessor.Attribute attrib : attribs) {
            int locations = attrib.locations();
            for (int offset = attrib.offset(); locations-- != 0; offset += attrib.size()) {
                GLCore.glEnableVertexAttribArray(index);
                GLCore.glVertexAttribDivisor(index, divisor);
                assert offset >= 0 && offset <= 16777215;
                attributes[index] = offset & 16777215 | (attrib.srcType() & 255) << 24;
                index++;
            }
        }
        return index;
    }

    private static void set_attrib_format_3(int type, int index, int stride, long offset) {
        switch(type) {
            case 0:
                GLCore.glVertexAttribPointer(index, 1, 5126, false, stride, offset);
                break;
            case 1:
                GLCore.glVertexAttribPointer(index, 2, 5126, false, stride, offset);
                break;
            case 2:
                GLCore.glVertexAttribPointer(index, 3, 5126, false, stride, offset);
                break;
            case 3:
                GLCore.glVertexAttribPointer(index, 4, 5126, false, stride, offset);
                break;
            case 4:
                GLCore.glVertexAttribPointer(index, 1, 5131, false, stride, offset);
                break;
            case 5:
                GLCore.glVertexAttribPointer(index, 2, 5131, false, stride, offset);
                break;
            case 6:
                GLCore.glVertexAttribPointer(index, 4, 5131, false, stride, offset);
                break;
            case 7:
                GLCore.glVertexAttribIPointer(index, 2, 5124, stride, offset);
                break;
            case 8:
                GLCore.glVertexAttribIPointer(index, 3, 5124, stride, offset);
                break;
            case 9:
                GLCore.glVertexAttribIPointer(index, 4, 5124, stride, offset);
                break;
            case 10:
                GLCore.glVertexAttribIPointer(index, 1, 5120, stride, offset);
                break;
            case 11:
                GLCore.glVertexAttribIPointer(index, 2, 5120, stride, offset);
                break;
            case 12:
                GLCore.glVertexAttribIPointer(index, 4, 5120, stride, offset);
                break;
            case 13:
                GLCore.glVertexAttribIPointer(index, 1, 5121, stride, offset);
                break;
            case 14:
                GLCore.glVertexAttribIPointer(index, 2, 5121, stride, offset);
                break;
            case 15:
                GLCore.glVertexAttribIPointer(index, 4, 5121, stride, offset);
                break;
            case 16:
                GLCore.glVertexAttribPointer(index, 1, 5121, true, stride, offset);
                break;
            case 17:
                GLCore.glVertexAttribPointer(index, 4, 5121, true, stride, offset);
                break;
            case 18:
                GLCore.glVertexAttribIPointer(index, 2, 5122, stride, offset);
                break;
            case 19:
                GLCore.glVertexAttribIPointer(index, 4, 5122, stride, offset);
                break;
            case 20:
                GLCore.glVertexAttribIPointer(index, 2, 5123, stride, offset);
                break;
            case 21:
                GLCore.glVertexAttribPointer(index, 2, 5123, true, stride, offset);
                break;
            case 22:
                GLCore.glVertexAttribIPointer(index, 1, 5124, stride, offset);
                break;
            case 23:
                GLCore.glVertexAttribIPointer(index, 1, 5125, stride, offset);
                break;
            case 24:
                GLCore.glVertexAttribPointer(index, 1, 5123, true, stride, offset);
                break;
            case 25:
                GLCore.glVertexAttribPointer(index, 4, 5123, true, stride, offset);
                break;
            default:
                throw new AssertionError(type);
        }
    }

    private static int set_vertex_format_4(@Nonnull Iterable<GeometryProcessor.Attribute> attribs, int array, int index, int binding, int divisor) {
        for (GeometryProcessor.Attribute attrib : attribs) {
            int locations = attrib.locations();
            for (int offset = attrib.offset(); locations-- != 0; offset += attrib.size()) {
                GLCore.glEnableVertexArrayAttrib(array, index);
                GLCore.glVertexArrayAttribBinding(array, index, binding);
                set_attrib_format_4(attrib.srcType(), array, index, offset);
                index++;
            }
        }
        GLCore.glVertexArrayBindingDivisor(array, binding, divisor);
        return index;
    }

    private static void set_attrib_format_4(int type, int array, int index, int offset) {
        switch(type) {
            case 0:
                GLCore.glVertexArrayAttribFormat(array, index, 1, 5126, false, offset);
                break;
            case 1:
                GLCore.glVertexArrayAttribFormat(array, index, 2, 5126, false, offset);
                break;
            case 2:
                GLCore.glVertexArrayAttribFormat(array, index, 3, 5126, false, offset);
                break;
            case 3:
                GLCore.glVertexArrayAttribFormat(array, index, 4, 5126, false, offset);
                break;
            case 4:
                GLCore.glVertexArrayAttribFormat(array, index, 1, 5131, false, offset);
                break;
            case 5:
                GLCore.glVertexArrayAttribFormat(array, index, 2, 5131, false, offset);
                break;
            case 6:
                GLCore.glVertexArrayAttribFormat(array, index, 4, 5131, false, offset);
                break;
            case 7:
                GLCore.glVertexArrayAttribIFormat(array, index, 2, 5124, offset);
                break;
            case 8:
                GLCore.glVertexArrayAttribIFormat(array, index, 3, 5124, offset);
                break;
            case 9:
                GLCore.glVertexArrayAttribIFormat(array, index, 4, 5124, offset);
                break;
            case 10:
                GLCore.glVertexArrayAttribIFormat(array, index, 1, 5120, offset);
                break;
            case 11:
                GLCore.glVertexArrayAttribIFormat(array, index, 2, 5120, offset);
                break;
            case 12:
                GLCore.glVertexArrayAttribIFormat(array, index, 4, 5120, offset);
                break;
            case 13:
                GLCore.glVertexArrayAttribIFormat(array, index, 1, 5121, offset);
                break;
            case 14:
                GLCore.glVertexArrayAttribIFormat(array, index, 2, 5121, offset);
                break;
            case 15:
                GLCore.glVertexArrayAttribIFormat(array, index, 4, 5121, offset);
                break;
            case 16:
                GLCore.glVertexArrayAttribFormat(array, index, 1, 5121, true, offset);
                break;
            case 17:
                GLCore.glVertexArrayAttribFormat(array, index, 4, 5121, true, offset);
                break;
            case 18:
                GLCore.glVertexArrayAttribIFormat(array, index, 2, 5122, offset);
                break;
            case 19:
                GLCore.glVertexArrayAttribIFormat(array, index, 4, 5122, offset);
                break;
            case 20:
                GLCore.glVertexArrayAttribIFormat(array, index, 2, 5123, offset);
                break;
            case 21:
                GLCore.glVertexArrayAttribFormat(array, index, 2, 5123, true, offset);
                break;
            case 22:
                GLCore.glVertexArrayAttribIFormat(array, index, 1, 5124, offset);
                break;
            case 23:
                GLCore.glVertexArrayAttribIFormat(array, index, 1, 5125, offset);
                break;
            case 24:
                GLCore.glVertexArrayAttribFormat(array, index, 1, 5123, true, offset);
                break;
            case 25:
                GLCore.glVertexArrayAttribFormat(array, index, 4, 5123, true, offset);
                break;
            default:
                throw new AssertionError(type);
        }
    }

    @Override
    protected void deallocate() {
        if (this.mVertexArray != 0) {
            GLCore.glDeleteVertexArrays(this.mVertexArray);
        }
        this.discard();
    }

    public void discard() {
        this.mVertexArray = 0;
    }

    public int getHandle() {
        return this.mVertexArray;
    }

    public int getVertexStride() {
        return this.mVertexStride;
    }

    public int getInstanceStride() {
        return this.mInstanceStride;
    }

    public void bindIndexBuffer(@Nonnull GLBuffer buffer) {
        if (this.mVertexArray != 0) {
            if (this.mIndexBuffer != buffer.getUniqueID()) {
                if (this.mDSAElementBuffer) {
                    GLCore.glVertexArrayElementBuffer(this.mVertexArray, buffer.getHandle());
                } else {
                    this.getDevice().bindIndexBufferInPipe(buffer);
                }
                this.mIndexBuffer = buffer.getUniqueID();
            }
        }
    }

    public void bindVertexBuffer(@Nonnull GLBuffer buffer, long offset) {
        if (this.mVertexArray != 0) {
            assert this.mVertexStride > 0;
            if (this.mVertexBuffer != buffer.getUniqueID() || this.mVertexOffset != offset) {
                if (this.mVertexBinding != -1) {
                    GLCore.glVertexArrayVertexBuffer(this.mVertexArray, this.mVertexBinding, buffer.getHandle(), offset, this.mVertexStride);
                } else if (this.mAttributes != null) {
                    int target = this.getDevice().bindBuffer(buffer);
                    assert target == 34962;
                    for (int index = 0; index < this.mNumVertexLocations; index++) {
                        int info = this.mAttributes[index];
                        set_attrib_format_3(info >> 24, index, this.mVertexStride, offset + (long) (info & 16777215));
                    }
                } else {
                    assert false;
                }
                this.mVertexBuffer = buffer.getUniqueID();
                this.mVertexOffset = offset;
            }
        }
    }

    public void bindInstanceBuffer(@Nonnull GLBuffer buffer, long offset) {
        if (this.mVertexArray != 0) {
            assert this.mInstanceStride > 0;
            if (this.mInstanceBuffer != buffer.getUniqueID() || this.mInstanceOffset != offset) {
                if (this.mInstanceBinding != -1) {
                    GLCore.glVertexArrayVertexBuffer(this.mVertexArray, this.mInstanceBinding, buffer.getHandle(), offset, this.mInstanceStride);
                } else if (this.mAttributes != null) {
                    int target = this.getDevice().bindBuffer(buffer);
                    assert target == 34962;
                    for (int index = this.mNumVertexLocations; index < this.mNumVertexLocations + this.mNumInstanceLocations; index++) {
                        int info = this.mAttributes[index];
                        set_attrib_format_3(info >> 24, index, this.mInstanceStride, offset + (long) (info & 16777215));
                    }
                } else {
                    assert false;
                }
                this.mInstanceBuffer = buffer.getUniqueID();
                this.mInstanceOffset = offset;
            }
        }
    }

    protected GLDevice getDevice() {
        return (GLDevice) super.getDevice();
    }
}
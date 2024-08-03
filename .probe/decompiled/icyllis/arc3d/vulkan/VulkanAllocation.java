package icyllis.arc3d.vulkan;

public class VulkanAllocation {

    public static final int VISIBLE_FLAG = 1;

    public static final int COHERENT_FLAG = 2;

    public static final int LAZILY_ALLOCATED_FLAG = 4;

    public long mMemory = 0L;

    public long mOffset = 0L;

    public long mSize = 0L;

    public int mMemoryFlags = 0;

    public long mAllocation = 0L;

    public void set(VulkanAllocation alloc) {
        this.mMemory = alloc.mMemory;
        this.mOffset = alloc.mOffset;
        this.mSize = alloc.mSize;
        this.mMemoryFlags = alloc.mMemoryFlags;
        this.mAllocation = alloc.mAllocation;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            VulkanAllocation vkAlloc = (VulkanAllocation) o;
            if (this.mMemory != vkAlloc.mMemory) {
                return false;
            } else if (this.mOffset != vkAlloc.mOffset) {
                return false;
            } else {
                return this.mSize != vkAlloc.mSize ? false : this.mMemoryFlags == vkAlloc.mMemoryFlags;
            }
        } else {
            return false;
        }
    }

    public int hashCode() {
        int result = (int) (this.mMemory ^ this.mMemory >>> 32);
        result = 31 * result + (int) (this.mOffset ^ this.mOffset >>> 32);
        result = 31 * result + (int) (this.mSize ^ this.mSize >>> 32);
        return 31 * result + this.mMemoryFlags;
    }
}
package icyllis.arc3d.engine;

public interface Mesh {

    default int getVertexSize() {
        return 0;
    }

    default int getVertexCount() {
        return 0;
    }

    default void setVertexBuffer(GpuBuffer buffer, int baseVertex, int actualVertexCount) {
        throw new IllegalStateException();
    }

    default int getInstanceSize() {
        return 0;
    }

    default int getInstanceCount() {
        return 0;
    }

    default void setInstanceBuffer(GpuBuffer buffer, int baseInstance, int actualInstanceCount) {
        throw new IllegalStateException();
    }

    default int getIndexCount() {
        return 0;
    }

    default void setIndexBuffer(GpuBuffer buffer, int baseIndex, int actualIndexCount) {
        throw new IllegalStateException();
    }
}
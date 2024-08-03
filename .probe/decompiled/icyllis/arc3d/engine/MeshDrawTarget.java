package icyllis.arc3d.engine;

import java.nio.ByteBuffer;
import javax.annotation.Nullable;

public interface MeshDrawTarget {

    long makeVertexSpace(Mesh var1);

    long makeInstanceSpace(Mesh var1);

    long makeIndexSpace(Mesh var1);

    @Nullable
    ByteBuffer makeVertexWriter(Mesh var1);

    @Nullable
    ByteBuffer makeInstanceWriter(Mesh var1);

    @Nullable
    ByteBuffer makeIndexWriter(Mesh var1);
}
package icyllis.arc3d.core;

import javax.annotation.Nonnull;

public interface MatrixProvider {

    @Nonnull
    Matrix4 getLocalToDevice();
}
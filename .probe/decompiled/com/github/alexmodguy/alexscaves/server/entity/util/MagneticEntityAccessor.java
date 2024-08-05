package com.github.alexmodguy.alexscaves.server.entity.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

public interface MagneticEntityAccessor {

    float getMagneticDeltaX();

    float getMagneticDeltaY();

    float getMagneticDeltaZ();

    Direction getMagneticAttachmentFace();

    Direction getPrevMagneticAttachmentFace();

    float getAttachmentProgress(float var1);

    void setMagneticDeltaX(float var1);

    void setMagneticDeltaY(float var1);

    void setMagneticDeltaZ(float var1);

    void setMagneticAttachmentFace(Direction var1);

    void postMagnetJump();

    boolean canChangeDirection();

    void stepOnMagnetBlock(BlockPos var1);
}
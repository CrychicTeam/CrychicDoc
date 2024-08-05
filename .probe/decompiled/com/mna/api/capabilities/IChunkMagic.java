package com.mna.api.capabilities;

import java.util.List;
import net.minecraft.core.BlockPos;

public interface IChunkMagic {

    void addResidualMagic(float var1);

    void removeResidualMagic(float var1);

    float getResidualMagic();

    void setResidualMagic(float var1);

    void pushKnownEldrinSupplier(BlockPos var1);

    void popKnownEldrinSupplier(BlockPos var1);

    List<Long> getKnownEldrinSuppliers();
}
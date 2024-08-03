package com.mna.capabilities.chunkdata;

import com.mna.api.capabilities.IChunkMagic;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.core.BlockPos;

public class ChunkMagic implements IChunkMagic {

    private float residualMagic = 0.0F;

    private HashSet<Long> knownSuppliers = new HashSet();

    @Override
    public void addResidualMagic(float amount) {
        this.residualMagic += amount;
    }

    @Override
    public void removeResidualMagic(float amount) {
        this.residualMagic -= amount;
        if (this.residualMagic < 0.0F) {
            this.residualMagic = 0.0F;
        }
    }

    @Override
    public float getResidualMagic() {
        return this.residualMagic;
    }

    @Override
    public void setResidualMagic(float amount) {
        this.residualMagic = amount;
    }

    @Override
    public void pushKnownEldrinSupplier(BlockPos pos) {
        this.knownSuppliers.add(pos.asLong());
    }

    @Override
    public void popKnownEldrinSupplier(BlockPos pos) {
        this.knownSuppliers.remove(pos.asLong());
    }

    @Override
    public List<Long> getKnownEldrinSuppliers() {
        return (List<Long>) this.knownSuppliers.stream().collect(Collectors.toList());
    }
}
package com.mna.capabilities.chunkdata;

import com.mna.ManaAndArtifice;
import com.mna.api.capabilities.IChunkMagic;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.LongTag;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class ChunkMagicProvider implements ICapabilitySerializable<Tag> {

    private static final String KEY_RESIDUAL_MAGIC = "residualMagic";

    private static final String KEY_ELDRIN_POSITIONS = "knownEldrinPositions";

    public static final Capability<IChunkMagic> MAGIC = CapabilityManager.get(new CapabilityToken<IChunkMagic>() {
    });

    private final LazyOptional<IChunkMagic> holder = LazyOptional.of(ChunkMagic::new);

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return MAGIC.orEmpty(cap, this.holder);
    }

    @Override
    public Tag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        IChunkMagic resolved = (IChunkMagic) this.holder.resolve().get();
        nbt.putFloat("residualMagic", resolved.getResidualMagic());
        ListTag positions = new ListTag();
        resolved.getKnownEldrinSuppliers().forEach(pos -> positions.add(LongTag.valueOf(pos)));
        nbt.put("knownEldrinPositions", positions);
        return nbt;
    }

    @Override
    public void deserializeNBT(Tag nbt) {
        if (nbt instanceof CompoundTag) {
            IChunkMagic resolved = (IChunkMagic) this.holder.resolve().get();
            CompoundTag cnbt = (CompoundTag) nbt;
            if (cnbt.contains("residualMagic")) {
                resolved.setResidualMagic(((CompoundTag) nbt).getFloat("residualMagic"));
            }
            if (cnbt.contains("knownEldrinPositions", 9)) {
                ListTag positions = cnbt.getList("knownEldrinPositions", 4);
                positions.forEach(pos -> resolved.pushKnownEldrinSupplier(BlockPos.of(((LongTag) pos).getAsLong())));
            }
        } else {
            ManaAndArtifice.LOGGER.error("Chunk capability data NBT passed back not an instance of CompoundNBT - save data was NOT loaded!");
        }
    }
}
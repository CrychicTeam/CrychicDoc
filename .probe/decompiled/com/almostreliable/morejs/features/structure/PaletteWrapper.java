package com.almostreliable.morejs.features.structure;

import com.google.common.base.Preconditions;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

public class PaletteWrapper {

    private final StructureTemplate.Palette palette;

    private final Vec3i borderSize;

    private final Map<BlockPos, StructureTemplate.StructureBlockInfo> cache = new HashMap();

    public PaletteWrapper(StructureTemplate.Palette palette, Vec3i borderSize) {
        this.palette = palette;
        this.borderSize = borderSize;
    }

    public void clear() {
        this.palette.blocks().clear();
        this.cache.clear();
    }

    public void add(BlockPos pos, BlockState state) {
        this.add(pos, state, null);
    }

    public void add(BlockPos pos, BlockState state, @Nullable CompoundTag tag) {
        Preconditions.checkNotNull(pos, "Invalid position");
        Preconditions.checkNotNull(state, "Invalid state");
        Preconditions.checkArgument(0 <= pos.m_123341_() && pos.m_123341_() < this.borderSize.getX(), "Invalid position, x must be between 0 and " + this.borderSize.getX());
        Preconditions.checkArgument(0 <= pos.m_123341_() && pos.m_123342_() < this.borderSize.getY(), "Invalid position, y must be between 0 and " + this.borderSize.getY());
        Preconditions.checkArgument(0 <= pos.m_123341_() && pos.m_123343_() < this.borderSize.getZ(), "Invalid position, z must be between 0 and " + this.borderSize.getZ());
        if (this.get(pos) instanceof StructureBlockInfoModification mod) {
            mod.setVanillaBlockState(state);
            mod.setNbt(tag);
        } else {
            StructureTemplate.StructureBlockInfo newInfo = new StructureTemplate.StructureBlockInfo(pos, state, tag);
            this.palette.blocks().add(newInfo);
            this.cache.put(pos, newInfo);
        }
    }

    public void forEach(Consumer<StructureTemplate.StructureBlockInfo> consumer) {
        this.palette.blocks().forEach(consumer);
    }

    public void removeIf(Predicate<StructureTemplate.StructureBlockInfo> predicate) {
        this.palette.blocks().removeIf(block -> {
            if (predicate.test(block)) {
                this.cache.remove(block.pos());
                return true;
            } else {
                return false;
            }
        });
    }

    @Nullable
    public StructureTemplate.StructureBlockInfo get(BlockPos pos) {
        if (this.cache.isEmpty()) {
            this.forEach(info -> this.cache.put(info.pos(), info));
        }
        return (StructureTemplate.StructureBlockInfo) this.cache.get(pos);
    }
}
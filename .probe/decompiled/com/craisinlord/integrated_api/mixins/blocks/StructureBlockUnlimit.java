package com.craisinlord.integrated_api.mixins.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.StructureBlockEntity;
import net.minecraft.world.level.block.state.properties.StructureMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(value = { StructureBlockEntity.class }, priority = 999)
public abstract class StructureBlockUnlimit {

    @ModifyConstant(method = { "load" }, constant = { @Constant(intValue = 48) }, require = 0)
    public int readNbtUpper(int value) {
        return 512;
    }

    @ModifyConstant(method = { "load" }, constant = { @Constant(intValue = -48) }, require = 0)
    public int readNbtLower(int value) {
        return -512;
    }

    @Overwrite
    private Stream<BlockPos> getRelatedCorners(BlockPos min, BlockPos max) {
        StructureBlockEntity blockEntity = (StructureBlockEntity) this;
        Level level = blockEntity.m_58904_();
        if (level == null) {
            return Stream.empty();
        } else {
            BlockPos middle = blockEntity.m_58899_();
            List<BlockPos> blocks = new ArrayList(2);
            int maxSearch = this.detectSize(-1) + 1;
            BlockPos.findClosestMatch(middle, maxSearch, Math.min(maxSearch, level.m_141928_() + 1), pos -> {
                if (level.getBlockEntity(pos) instanceof StructureBlockEntity block && block.getMode() == StructureMode.CORNER && Objects.equals(blockEntity.getStructureName(), block.getStructureName())) {
                    blocks.add(block.m_58899_());
                }
                return blocks.size() == 2;
            });
            return blocks.stream();
        }
    }

    @ModifyConstant(method = { "detectSize" }, constant = { @Constant(intValue = 80) }, require = 0)
    public int detectSize(int value) {
        return 512;
    }
}
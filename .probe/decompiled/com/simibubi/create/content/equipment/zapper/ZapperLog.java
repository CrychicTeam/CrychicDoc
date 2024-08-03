package com.simibubi.create.content.equipment.zapper;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

public class ZapperLog {

    private Level activeWorld;

    private List<List<StructureTemplate.StructureBlockInfo>> log = new LinkedList();

    public void record(Level world, List<BlockPos> positions) {
        if (world != this.activeWorld) {
            this.log.clear();
        }
        this.activeWorld = world;
        List<StructureTemplate.StructureBlockInfo> blocks = (List<StructureTemplate.StructureBlockInfo>) positions.stream().map(pos -> {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            return new StructureTemplate.StructureBlockInfo(pos, world.getBlockState(pos), blockEntity == null ? null : blockEntity.saveWithFullMetadata());
        }).collect(Collectors.toList());
        this.log.add(0, blocks);
    }

    public void undo() {
    }

    public void redo() {
    }
}
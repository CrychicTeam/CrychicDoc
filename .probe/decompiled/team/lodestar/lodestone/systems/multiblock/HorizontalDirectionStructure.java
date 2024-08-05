package team.lodestar.lodestone.systems.multiblock;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class HorizontalDirectionStructure extends MultiBlockStructure {

    public HorizontalDirectionStructure(ArrayList<MultiBlockStructure.StructurePiece> structurePieces) {
        super(structurePieces);
    }

    @Override
    public void place(BlockPlaceContext context) {
        this.structurePieces.forEach(s -> s.place(context.getClickedPos(), context.m_43725_(), (BlockState) s.state.m_61124_(BlockStateProperties.HORIZONTAL_FACING, context.m_8125_().getOpposite())));
    }

    public static HorizontalDirectionStructure of(MultiBlockStructure.StructurePiece... pieces) {
        return new HorizontalDirectionStructure(new ArrayList(List.of(pieces)));
    }
}
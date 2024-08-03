package se.mickelus.tetra.blocks.scroll;

import java.util.EnumMap;
import java.util.Map;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.registries.ObjectHolder;
import se.mickelus.mutil.util.RotationHelper;

@ParametersAreNonnullByDefault
public class WallScrollBlock extends ScrollBlock {

    public static final String identifier = "scroll_wall";

    @ObjectHolder(registryName = "block", value = "tetra:scroll_wall")
    public static ScrollBlock instance;

    private final Map<Direction, VoxelShape> shapes;

    private final VoxelShape baseShape = Shapes.or(Block.box(1.0, 14.0, 0.0, 15.0, 16.0, 2.0), Block.box(1.0, 1.0, 0.0, 15.0, 14.0, 0.1));

    public WallScrollBlock() {
        super(ScrollBlock.Arrangement.wall);
        this.shapes = new EnumMap(Direction.class);
        for (int i = 0; i < 4; i++) {
            Direction direction = Direction.from2DDataValue(i);
            this.shapes.put(direction, RotationHelper.rotateDirection(this.baseShape, direction));
        }
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return (VoxelShape) this.shapes.get(state.m_61143_(BlockStateProperties.HORIZONTAL_FACING));
    }
}
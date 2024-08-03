package se.mickelus.tetra.blocks.forged.extractor;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.registries.RegistryObject;
import se.mickelus.mutil.network.PacketHandler;
import se.mickelus.mutil.util.TileEntityOptional;
import se.mickelus.tetra.blocks.TetraWaterloggedBlock;
import se.mickelus.tetra.blocks.forged.ForgedBlockCommon;

@ParametersAreNonnullByDefault
public class CoreExtractorPistonBlock extends TetraWaterloggedBlock implements EntityBlock {

    public static final String identifier = "extractor_piston";

    public static final BooleanProperty hackProp = BooleanProperty.create("hack");

    public static final VoxelShape boundingBox = m_49796_(5.0, 0.0, 5.0, 11.0, 16.0, 11.0);

    public static RegistryObject<CoreExtractorPistonBlock> instance;

    public CoreExtractorPistonBlock() {
        super(ForgedBlockCommon.propertiesNotSolid);
    }

    @Override
    public void commonInit(PacketHandler packetHandler) {
        packetHandler.registerPacket(CoreExtractorPistonUpdatePacket.class, CoreExtractorPistonUpdatePacket::new);
    }

    @Override
    public void animateTick(BlockState stateIn, Level worldIn, BlockPos pos, RandomSource rand) {
        TileEntityOptional.from(worldIn, pos, CoreExtractorPistonBlockEntity.class).ifPresent(te -> {
            if (te.isActive()) {
                float random = rand.nextFloat();
                if (random < 0.6F) {
                    worldIn.addParticle(ParticleTypes.SMOKE, (double) pos.m_123341_() + 0.4 + rand.nextGaussian() * 0.2, (double) pos.m_123342_() + rand.nextGaussian(), (double) pos.m_123343_() + 0.4 + rand.nextGaussian() * 0.2, 0.0, 0.0, 0.0);
                }
            }
        });
    }

    @Override
    public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor world, BlockPos currentPos, BlockPos facingPos) {
        if (Direction.DOWN.equals(facing) && !CoreExtractorBaseBlock.instance.get().equals(facingState.m_60734_())) {
            return state.m_61143_(BlockStateProperties.WATERLOGGED) ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState();
        } else {
            return super.updateShape(state, facing, facingState, world, currentPos, facingPos);
        }
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return boundingBox;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(hackProp);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CoreExtractorPistonBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> entityType) {
        return getTicker(entityType, CoreExtractorPistonBlockEntity.type.get(), (lvl, pos, blockState, tile) -> tile.tick(lvl, pos, blockState));
    }
}
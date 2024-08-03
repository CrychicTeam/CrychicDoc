package fr.lucreeper74.createmetallurgy.content.glassed_foundry_lid;

import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;
import fr.lucreeper74.createmetallurgy.content.foundry_mixer.FoundryMixerBlockEntity;
import fr.lucreeper74.createmetallurgy.registries.CMBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class GlassedFoundryLidBlock extends Block implements IBE<GlassedFoundryLidBlockEntity>, IWrenchable {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public static final BooleanProperty UNDER_FOUNDRY_MIXER = BooleanProperty.create("under_foundry_mixer");

    public static final BooleanProperty OPEN = BlockStateProperties.OPEN;

    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public GlassedFoundryLidBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.m_49959_((BlockState) super.defaultBlockState().m_61124_(UNDER_FOUNDRY_MIXER, false));
        this.m_49959_((BlockState) super.defaultBlockState().m_61124_(OPEN, false));
        this.m_49959_((BlockState) super.defaultBlockState().m_61124_(POWERED, false));
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return Shapes.or(Block.box(1.0, 0.0, 1.0, 15.0, 14.0, 15.0), Block.box(3.0, 13.0, 3.0, 13.0, 15.0, 13.0));
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState blockState0, boolean boolean1) {
        super.m_6807_(state, level, pos, blockState0, boolean1);
        if (level.getBlockEntity(pos.above()) instanceof FoundryMixerBlockEntity) {
            level.setBlock(pos, (BlockState) state.m_61124_(UNDER_FOUNDRY_MIXER, true), 1);
        } else {
            level.setBlock(pos, (BlockState) state.m_61124_(UNDER_FOUNDRY_MIXER, false), 1);
        }
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos blockPos0, boolean boolean1) {
        boolean flag = level.m_276867_(pos);
        if (level.getBlockEntity(pos.above()) instanceof FoundryMixerBlockEntity) {
            state = (BlockState) state.m_61124_(UNDER_FOUNDRY_MIXER, true);
        } else {
            state = (BlockState) state.m_61124_(UNDER_FOUNDRY_MIXER, false);
        }
        if (flag != (Boolean) state.m_61143_(POWERED)) {
            if (flag != (Boolean) state.m_61143_(OPEN)) {
                level.m_5898_(null, flag ? 1037 : 1036, pos, 0);
            }
            level.setBlock(pos, (BlockState) ((BlockState) state.m_61124_(POWERED, flag)).m_61124_(OPEN, flag), 2);
        } else {
            level.setBlock(pos, state, 2);
        }
    }

    @Override
    public InteractionResult use(@NotNull BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        boolean currentState = (Boolean) state.m_61143_(OPEN);
        if (!level.isClientSide() && hand == InteractionHand.MAIN_HAND) {
            level.setBlock(pos, (BlockState) state.m_61124_(OPEN, !currentState), 3);
            level.m_5898_(null, currentState ? 1037 : 1036, pos, 0);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(UNDER_FOUNDRY_MIXER);
        builder.add(FACING);
        builder.add(OPEN);
        builder.add(POWERED);
        super.createBlockStateDefinition(builder);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return pContext.m_43723_().m_6144_() ? (BlockState) this.m_49966_().m_61124_(FACING, pContext.m_8125_().getOpposite()) : (BlockState) this.m_49966_().m_61124_(FACING, pContext.m_8125_());
    }

    @Override
    public Class<GlassedFoundryLidBlockEntity> getBlockEntityClass() {
        return GlassedFoundryLidBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends GlassedFoundryLidBlockEntity> getBlockEntityType() {
        return (BlockEntityType<? extends GlassedFoundryLidBlockEntity>) CMBlockEntityTypes.GLASSED_ALLOYER_TOP.get();
    }
}
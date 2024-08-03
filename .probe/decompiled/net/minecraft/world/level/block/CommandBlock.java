package net.minecraft.world.level.block;

import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringUtil;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BaseCommandBlock;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.CommandBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.slf4j.Logger;

public class CommandBlock extends BaseEntityBlock implements GameMasterBlock {

    private static final Logger LOGGER = LogUtils.getLogger();

    public static final DirectionProperty FACING = DirectionalBlock.FACING;

    public static final BooleanProperty CONDITIONAL = BlockStateProperties.CONDITIONAL;

    private final boolean automatic;

    public CommandBlock(BlockBehaviour.Properties blockBehaviourProperties0, boolean boolean1) {
        super(blockBehaviourProperties0);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(FACING, Direction.NORTH)).m_61124_(CONDITIONAL, false));
        this.automatic = boolean1;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos0, BlockState blockState1) {
        CommandBlockEntity $$2 = new CommandBlockEntity(blockPos0, blockState1);
        $$2.setAutomatic(this.automatic);
        return $$2;
    }

    @Override
    public void neighborChanged(BlockState blockState0, Level level1, BlockPos blockPos2, Block block3, BlockPos blockPos4, boolean boolean5) {
        if (!level1.isClientSide) {
            if (level1.getBlockEntity(blockPos2) instanceof CommandBlockEntity $$7) {
                boolean $$8 = level1.m_276867_(blockPos2);
                boolean $$9 = $$7.isPowered();
                $$7.setPowered($$8);
                if (!$$9 && !$$7.isAutomatic() && $$7.getMode() != CommandBlockEntity.Mode.SEQUENCE) {
                    if ($$8) {
                        $$7.markConditionMet();
                        level1.m_186460_(blockPos2, this, 1);
                    }
                }
            }
        }
    }

    @Override
    public void tick(BlockState blockState0, ServerLevel serverLevel1, BlockPos blockPos2, RandomSource randomSource3) {
        if (serverLevel1.m_7702_(blockPos2) instanceof CommandBlockEntity $$5) {
            BaseCommandBlock $$6 = $$5.getCommandBlock();
            boolean $$7 = !StringUtil.isNullOrEmpty($$6.getCommand());
            CommandBlockEntity.Mode $$8 = $$5.getMode();
            boolean $$9 = $$5.wasConditionMet();
            if ($$8 == CommandBlockEntity.Mode.AUTO) {
                $$5.markConditionMet();
                if ($$9) {
                    this.execute(blockState0, serverLevel1, blockPos2, $$6, $$7);
                } else if ($$5.isConditional()) {
                    $$6.setSuccessCount(0);
                }
                if ($$5.isPowered() || $$5.isAutomatic()) {
                    serverLevel1.m_186460_(blockPos2, this, 1);
                }
            } else if ($$8 == CommandBlockEntity.Mode.REDSTONE) {
                if ($$9) {
                    this.execute(blockState0, serverLevel1, blockPos2, $$6, $$7);
                } else if ($$5.isConditional()) {
                    $$6.setSuccessCount(0);
                }
            }
            serverLevel1.m_46717_(blockPos2, this);
        }
    }

    private void execute(BlockState blockState0, Level level1, BlockPos blockPos2, BaseCommandBlock baseCommandBlock3, boolean boolean4) {
        if (boolean4) {
            baseCommandBlock3.performCommand(level1);
        } else {
            baseCommandBlock3.setSuccessCount(0);
        }
        executeChain(level1, blockPos2, (Direction) blockState0.m_61143_(FACING));
    }

    @Override
    public InteractionResult use(BlockState blockState0, Level level1, BlockPos blockPos2, Player player3, InteractionHand interactionHand4, BlockHitResult blockHitResult5) {
        BlockEntity $$6 = level1.getBlockEntity(blockPos2);
        if ($$6 instanceof CommandBlockEntity && player3.canUseGameMasterBlocks()) {
            player3.openCommandBlock((CommandBlockEntity) $$6);
            return InteractionResult.sidedSuccess(level1.isClientSide);
        } else {
            return InteractionResult.PASS;
        }
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState blockState0) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState blockState0, Level level1, BlockPos blockPos2) {
        BlockEntity $$3 = level1.getBlockEntity(blockPos2);
        return $$3 instanceof CommandBlockEntity ? ((CommandBlockEntity) $$3).getCommandBlock().getSuccessCount() : 0;
    }

    @Override
    public void setPlacedBy(Level level0, BlockPos blockPos1, BlockState blockState2, LivingEntity livingEntity3, ItemStack itemStack4) {
        if (level0.getBlockEntity(blockPos1) instanceof CommandBlockEntity $$6) {
            BaseCommandBlock $$7 = $$6.getCommandBlock();
            if (itemStack4.hasCustomHoverName()) {
                $$7.setName(itemStack4.getHoverName());
            }
            if (!level0.isClientSide) {
                if (BlockItem.getBlockEntityData(itemStack4) == null) {
                    $$7.setTrackOutput(level0.getGameRules().getBoolean(GameRules.RULE_SENDCOMMANDFEEDBACK));
                    $$6.setAutomatic(this.automatic);
                }
                if ($$6.getMode() == CommandBlockEntity.Mode.SEQUENCE) {
                    boolean $$8 = level0.m_276867_(blockPos1);
                    $$6.setPowered($$8);
                }
            }
        }
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState0) {
        return RenderShape.MODEL;
    }

    @Override
    public BlockState rotate(BlockState blockState0, Rotation rotation1) {
        return (BlockState) blockState0.m_61124_(FACING, rotation1.rotate((Direction) blockState0.m_61143_(FACING)));
    }

    @Override
    public BlockState mirror(BlockState blockState0, Mirror mirror1) {
        return blockState0.m_60717_(mirror1.getRotation((Direction) blockState0.m_61143_(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinitionBuilderBlockBlockState0) {
        stateDefinitionBuilderBlockBlockState0.add(FACING, CONDITIONAL);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext0) {
        return (BlockState) this.m_49966_().m_61124_(FACING, blockPlaceContext0.getNearestLookingDirection().getOpposite());
    }

    private static void executeChain(Level level0, BlockPos blockPos1, Direction direction2) {
        BlockPos.MutableBlockPos $$3 = blockPos1.mutable();
        GameRules $$4 = level0.getGameRules();
        int $$5 = $$4.getInt(GameRules.RULE_MAX_COMMAND_CHAIN_LENGTH);
        while ($$5-- > 0) {
            $$3.move(direction2);
            BlockState $$6 = level0.getBlockState($$3);
            Block $$7 = $$6.m_60734_();
            if (!$$6.m_60713_(Blocks.CHAIN_COMMAND_BLOCK) || !(level0.getBlockEntity($$3) instanceof CommandBlockEntity $$9) || $$9.getMode() != CommandBlockEntity.Mode.SEQUENCE) {
                break;
            }
            if ($$9.isPowered() || $$9.isAutomatic()) {
                BaseCommandBlock $$10 = $$9.getCommandBlock();
                if ($$9.markConditionMet()) {
                    if (!$$10.performCommand(level0)) {
                        break;
                    }
                    level0.updateNeighbourForOutputSignal($$3, $$7);
                } else if ($$9.isConditional()) {
                    $$10.setSuccessCount(0);
                }
            }
            direction2 = (Direction) $$6.m_61143_(FACING);
        }
        if ($$5 <= 0) {
            int $$11 = Math.max($$4.getInt(GameRules.RULE_MAX_COMMAND_CHAIN_LENGTH), 0);
            LOGGER.warn("Command Block chain tried to execute more than {} steps!", $$11);
        }
    }
}
package dev.ftb.mods.ftbquests.block;

import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.injectables.annotations.ExpectPlatform.Transformed;
import dev.ftb.mods.ftbquests.block.entity.ITaskScreen;
import dev.ftb.mods.ftbquests.block.entity.TaskScreenAuxBlockEntity;
import dev.ftb.mods.ftbquests.block.entity.TaskScreenBlockEntity;
import dev.ftb.mods.ftbquests.block.forge.TaskScreenBlockImpl;
import dev.ftb.mods.ftbquests.client.FTBQuestsClient;
import dev.ftb.mods.ftbquests.item.ScreenBlockItem;
import dev.ftb.mods.ftbquests.net.TaskScreenConfigRequest;
import dev.ftb.mods.ftbquests.quest.BaseQuestFile;
import dev.ftb.mods.ftbquests.quest.ServerQuestFile;
import dev.ftb.mods.ftbquests.quest.task.Task;
import dev.ftb.mods.ftbteams.api.FTBTeamsAPI;
import java.util.List;
import java.util.Objects;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class TaskScreenBlock extends BaseEntityBlock {

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    private final int size;

    protected TaskScreenBlock(int size) {
        super(BlockBehaviour.Properties.of().mapColor(DyeColor.BLACK).strength(0.3F));
        this.size = size;
        this.m_49959_((BlockState) ((BlockState) this.m_49965_().any()).m_61124_(FACING, Direction.NORTH));
    }

    public int getSize() {
        return this.size;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.m_7926_(builder);
        builder.add(FACING);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return blockEntityProvider().create(blockPos, blockState);
    }

    @ExpectPlatform
    @Transformed
    public static BlockEntityType.BlockEntitySupplier<TaskScreenBlockEntity> blockEntityProvider() {
        return TaskScreenBlockImpl.blockEntityProvider();
    }

    @ExpectPlatform
    @Transformed
    public static BlockEntityType.BlockEntitySupplier<TaskScreenAuxBlockEntity> blockEntityAuxProvider() {
        return TaskScreenBlockImpl.blockEntityAuxProvider();
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        return !this.validatePlaceable(blockPlaceContext) ? null : (BlockState) super.m_5573_(blockPlaceContext).m_61124_(FACING, blockPlaceContext.m_8125_().getOpposite());
    }

    private boolean validatePlaceable(BlockPlaceContext ctx) {
        int screenSize = ScreenBlockItem.getSize(ctx.m_43722_());
        if (screenSize == 1) {
            return true;
        } else {
            Direction facing = ctx.m_8125_();
            return BlockPos.betweenClosedStream(getMultiblockBounds(ctx.getClickedPos(), this.getSize(), facing)).allMatch(pos -> ctx.m_43725_().getBlockState(pos).m_60629_(ctx));
        }
    }

    @Override
    public void setPlacedBy(Level level, BlockPos blockPos, BlockState blockState, @Nullable LivingEntity livingEntity, ItemStack itemStack) {
        super.m_6402_(level, blockPos, blockState, livingEntity, itemStack);
        if (level.getBlockEntity(blockPos) instanceof TaskScreenBlockEntity coreScreen) {
            if (livingEntity instanceof ServerPlayer sp) {
                coreScreen.setTeamId(ServerQuestFile.INSTANCE.getOrCreateTeamData(sp).getTeamId());
            }
            Direction facing = (Direction) blockState.m_61143_(FACING);
            BlockState auxState = (BlockState) ((Block) FTBQuestsBlocks.AUX_SCREEN.get()).defaultBlockState().m_61124_(FACING, facing);
            BlockPos.betweenClosedStream(getMultiblockBounds(blockPos, this.getSize(), facing)).filter(pos -> !pos.equals(blockPos)).forEach(auxPos -> {
                level.setBlockAndUpdate(auxPos, auxState);
                if (level.getBlockEntity(auxPos) instanceof TaskScreenAuxBlockEntity auxScreen) {
                    auxScreen.setCoreScreen(coreScreen);
                }
            });
        }
    }

    @Override
    public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState newState, boolean isMoving) {
        if (blockState.m_60734_() != newState.m_60734_() && level.getBlockEntity(blockPos) instanceof ITaskScreen taskScreen) {
            taskScreen.getCoreScreen().ifPresent(coreScreen -> {
                coreScreen.removeAllAuxScreens();
                if (coreScreen != taskScreen) {
                    level.m_46953_(coreScreen.m_58899_(), true, null);
                }
            });
            super.m_6810_(blockState, level, blockPos, newState, isMoving);
        }
    }

    @Override
    public float getDestroyProgress(BlockState blockState, Player player, BlockGetter blockGetter, BlockPos blockPos) {
        if (player.m_9236_().getBlockEntity(blockPos) instanceof ITaskScreen taskScreen && taskScreen.isIndestructible()) {
            return 0.0F;
        }
        return super.m_5880_(blockState, player, blockGetter, blockPos);
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (player instanceof ServerPlayer sp && level.getBlockEntity(blockPos) instanceof ITaskScreen taskScreen) {
            if (!hasPermissionToEdit(sp, taskScreen)) {
                sp.displayClientMessage(Component.translatable("block.ftbquests.screen.no_permission").withStyle(ChatFormatting.RED), true);
                return InteractionResult.FAIL;
            }
            taskScreen.getCoreScreen().ifPresent(coreScreen -> new TaskScreenConfigRequest(coreScreen.m_58899_()).sendTo(sp));
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable BlockGetter blockGetter, List<Component> list, TooltipFlag tooltipFlag) {
        super.m_5871_(itemStack, blockGetter, list, tooltipFlag);
        if (itemStack.getTag() != null && itemStack.getTag().contains("BlockEntityTag", 10)) {
            CompoundTag subTag = (CompoundTag) Objects.requireNonNull(itemStack.getTagElement("BlockEntityTag"));
            BaseQuestFile questFile = FTBQuestsClient.getClientQuestFile();
            if (questFile != null) {
                Task task = questFile.getTask(subTag.getLong("TaskID"));
                if (task != null) {
                    list.add(Component.translatable("ftbquests.chapter").append(": ").append(task.getQuest().getChapter().getTitle().copy().withStyle(ChatFormatting.YELLOW)));
                    list.add(Component.translatable("ftbquests.quest").append(": ").append(task.getQuest().getMutableTitle().withStyle(ChatFormatting.YELLOW)));
                    list.add(Component.translatable("ftbquests.task").append(": ").append(task.getMutableTitle().withStyle(ChatFormatting.YELLOW)));
                }
            }
        }
    }

    public static boolean hasPermissionToEdit(ServerPlayer player, ITaskScreen screen) {
        return player.m_20148_().equals(screen.getTeamId()) ? true : (Boolean) FTBTeamsAPI.api().getManager().getTeamByID(screen.getTeamId()).map(team -> team.getRankForPlayer(player.m_20148_()).isMemberOrBetter()).orElse(false);
    }

    public static AABB getMultiblockBounds(BlockPos corePos, int size, Direction facing) {
        if (size == 1) {
            return new AABB(corePos, corePos);
        } else {
            int size2 = size / 2;
            facing = facing.getCounterClockWise();
            return new AABB((double) (corePos.m_123341_() - size2 * facing.getStepX()), (double) corePos.m_123342_(), (double) (corePos.m_123343_() - size2 * facing.getStepZ()), (double) (corePos.m_123341_() + size2 * facing.getStepX()), (double) (corePos.m_123342_() + size - 1), (double) (corePos.m_123343_() + size2 * facing.getStepZ()));
        }
    }

    public static class Aux extends TaskScreenBlock {

        protected Aux() {
            super(0);
        }

        @Nullable
        @Override
        public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
            return blockEntityAuxProvider().create(blockPos, blockState);
        }
    }
}
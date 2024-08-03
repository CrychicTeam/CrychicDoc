package net.blay09.mods.waystones.block;

import java.util.Objects;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.waystones.api.IWaystone;
import net.blay09.mods.waystones.block.entity.WaystoneBlockEntity;
import net.blay09.mods.waystones.block.entity.WaystoneBlockEntityBase;
import net.blay09.mods.waystones.config.WaystonesConfig;
import net.blay09.mods.waystones.core.PlayerWaystoneManager;
import net.blay09.mods.waystones.core.WaystoneSyncManager;
import net.blay09.mods.waystones.tag.ModItemTags;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class WaystoneBlock extends WaystoneBlockBase {

    private static final VoxelShape LOWER_SHAPE = Shapes.or(m_49796_(0.0, 0.0, 0.0, 16.0, 3.0, 16.0), m_49796_(1.0, 3.0, 1.0, 15.0, 7.0, 15.0), m_49796_(2.0, 7.0, 2.0, 14.0, 9.0, 14.0), m_49796_(3.0, 9.0, 3.0, 13.0, 16.0, 13.0)).optimize();

    private static final VoxelShape UPPER_SHAPE = Shapes.or(m_49796_(3.0, 0.0, 3.0, 13.0, 8.0, 13.0), m_49796_(2.0, 8.0, 2.0, 14.0, 10.0, 14.0), m_49796_(1.0, 10.0, 1.0, 15.0, 12.0, 15.0), m_49796_(3.0, 12.0, 3.0, 13.0, 14.0, 13.0), m_49796_(4.0, 14.0, 4.0, 12.0, 16.0, 12.0)).optimize();

    public WaystoneBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(HALF, DoubleBlockHalf.LOWER)).m_61124_(WATERLOGGED, false));
    }

    @Override
    protected boolean canSilkTouch() {
        return true;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext context) {
        return state.m_61143_(HALF) == DoubleBlockHalf.UPPER ? UPPER_SHAPE : LOWER_SHAPE;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new WaystoneBlockEntity(pos, state);
    }

    @Override
    protected InteractionResult handleActivation(Level world, BlockPos pos, Player player, WaystoneBlockEntityBase tileEntity, IWaystone waystone) {
        if (player.m_21205_().is(ModItemTags.BOUND_SCROLLS)) {
            return InteractionResult.PASS;
        } else {
            boolean isActivated = PlayerWaystoneManager.isWaystoneActivated(player, waystone);
            if (isActivated) {
                if (!world.isClientSide) {
                    if (WaystonesConfig.getActive().restrictions.allowWaystoneToWaystoneTeleport) {
                        Balm.getNetworking().openGui(player, tileEntity.getMenuProvider());
                    } else {
                        player.displayClientMessage(Component.translatable("chat.waystones.waystone_to_waystone_disabled"), true);
                    }
                }
            } else {
                PlayerWaystoneManager.activateWaystone(player, waystone);
                if (!world.isClientSide) {
                    MutableComponent nameComponent = Component.literal(waystone.getName());
                    nameComponent.withStyle(ChatFormatting.WHITE);
                    MutableComponent chatComponent = Component.translatable("chat.waystones.waystone_activated", nameComponent);
                    chatComponent.withStyle(ChatFormatting.YELLOW);
                    player.m_213846_(chatComponent);
                    WaystoneSyncManager.sendActivatedWaystones(player);
                    world.playSound(null, pos, SoundEvents.PLAYER_LEVELUP, SoundSource.BLOCKS, 0.2F, 1.0F);
                }
                this.notifyObserversOfAction(world, pos);
                if (world.isClientSide) {
                    for (int i = 0; i < 32; i++) {
                        world.addParticle(ParticleTypes.ENCHANT, (double) pos.m_123341_() + 0.5 + (world.random.nextDouble() - 0.5) * 2.0, (double) (pos.m_123342_() + 3), (double) pos.m_123343_() + 0.5 + (world.random.nextDouble() - 0.5) * 2.0, 0.0, -5.0, 0.0);
                        world.addParticle(ParticleTypes.ENCHANT, (double) pos.m_123341_() + 0.5 + (world.random.nextDouble() - 0.5) * 2.0, (double) (pos.m_123342_() + 4), (double) pos.m_123343_() + 0.5 + (world.random.nextDouble() - 0.5) * 2.0, 0.0, -5.0, 0.0);
                    }
                }
            }
            return InteractionResult.SUCCESS;
        }
    }

    @Override
    public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
        if (random.nextFloat() < 0.75F) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            Player player = Minecraft.getInstance().player;
            if (blockEntity instanceof WaystoneBlockEntity && PlayerWaystoneManager.isWaystoneActivated((Player) Objects.requireNonNull(player), ((WaystoneBlockEntity) blockEntity).getWaystone())) {
                world.addParticle(ParticleTypes.PORTAL, (double) pos.m_123341_() + 0.5 + (random.nextDouble() - 0.5) * 1.5, (double) pos.m_123342_() + 0.5, (double) pos.m_123343_() + 0.5 + (random.nextDouble() - 0.5) * 1.5, 0.0, 0.0, 0.0);
                world.addParticle(ParticleTypes.ENCHANT, (double) pos.m_123341_() + 0.5 + (random.nextDouble() - 0.5) * 1.5, (double) pos.m_123342_() + 0.5, (double) pos.m_123343_() + 0.5 + (random.nextDouble() - 0.5) * 1.5, 0.0, 0.0, 0.0);
            }
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(HALF);
    }

    @Override
    public boolean isPathfindable(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, PathComputationType pathComputationType) {
        return false;
    }
}
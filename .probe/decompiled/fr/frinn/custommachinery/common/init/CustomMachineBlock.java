package fr.frinn.custommachinery.common.init;

import com.communi.suggestu.saecularia.caudices.core.block.IBlockWithWorldlyProperties;
import fr.frinn.custommachinery.CustomMachinery;
import fr.frinn.custommachinery.PlatformHelper;
import fr.frinn.custommachinery.api.component.IMachineComponentManager;
import fr.frinn.custommachinery.api.component.MachineComponentType;
import fr.frinn.custommachinery.common.component.LightMachineComponent;
import fr.frinn.custommachinery.common.component.RedstoneMachineComponent;
import fr.frinn.custommachinery.common.component.handler.FluidComponentHandler;
import fr.frinn.custommachinery.common.machine.CustomMachine;
import fr.frinn.custommachinery.common.network.SRefreshCustomMachineTilePacket;
import fr.frinn.custommachinery.common.util.MachineBlockState;
import fr.frinn.custommachinery.common.util.Utils;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.SignalGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class CustomMachineBlock extends Block implements EntityBlock, IBlockWithWorldlyProperties {

    private static final BlockBehaviour.StateArgumentPredicate<EntityType<?>> spawnPredicate = (state, level, pos, type) -> state.m_60783_(level, pos, Direction.UP) && state.m_60734_() instanceof CustomMachineBlock machineBlock && machineBlock.getLightEmission(state, level, pos) < 14;

    public final String renderType;

    public static BlockBehaviour.Properties makeProperties(boolean occlusion) {
        return occlusion ? BlockBehaviour.Properties.copy(Blocks.STONE).requiresCorrectToolForDrops().strength(3.5F).dynamicShape().isValidSpawn(spawnPredicate) : BlockBehaviour.Properties.copy(Blocks.STONE).requiresCorrectToolForDrops().strength(3.5F).noOcclusion().dynamicShape().isValidSpawn(spawnPredicate);
    }

    public CustomMachineBlock(String renderType, boolean occlusion) {
        super(makeProperties(occlusion));
        this.renderType = renderType;
    }

    public CustomMachineBlock() {
        this("translucent", false);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.getBlockEntity(pos) instanceof CustomMachineTile machine) {
            if (player.m_21120_(hand).is((Item) Registration.CONFIGURATION_CARD_ITEM.get())) {
                return ConfigurationCardItem.pasteConfiguration(level, player, machine, player.m_21120_(hand));
            } else if ((Boolean) machine.getComponentManager().getComponentHandler((MachineComponentType) Registration.FLUID_MACHINE_COMPONENT.get()).map(h -> (FluidComponentHandler) h).map(fluidHandler -> fluidHandler.getCommonFluidHandler().interactWithFluidHandler(player, hand)).orElse(false)) {
                return InteractionResult.SUCCESS;
            } else {
                if (!level.isClientSide() && player instanceof ServerPlayer serverPlayer && !machine.getGuiElements().isEmpty()) {
                    CustomMachineContainer.open(serverPlayer, machine);
                    return InteractionResult.SUCCESS;
                }
                return InteractionResult.SUCCESS;
            }
        } else {
            return super.m_6227_(state, level, pos, player, hand, hit);
        }
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        CustomMachineItem.getMachine(stack).ifPresent(machine -> {
            if (level.getBlockEntity(pos) instanceof CustomMachineTile machineTile) {
                machineTile.setId(machine.getId());
                if (placer != null) {
                    machineTile.setOwner(placer);
                }
                if (!level.isClientSide() && level.getServer() != null && placer != null && placer.getItemInHand(InteractionHand.OFF_HAND) == stack) {
                    level.getServer().m_6937_(new TickTask(1, () -> new SRefreshCustomMachineTilePacket(pos, machine.getId()).sendToChunkListeners(level.getChunkAt(pos))));
                }
            }
        });
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        ResourceLocation id = (ResourceLocation) CustomMachinery.CUSTOM_BLOCK_MACHINES.inverse().get(this);
        if (id != null && level.getBlockEntity(pos) instanceof CustomMachineTile machineTile) {
            machineTile.setId(id);
        }
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack tool) {
        if (blockEntity instanceof CustomMachineTile machine && PlatformHelper.hasCorrectToolsForDrops(player, (BlockState) MachineBlockState.CACHE.getUnchecked(machine.getAppearance()))) {
            super.playerDestroy(level, player, pos, state, blockEntity, tool);
        }
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        super.playerWillDestroy(level, pos, state, player);
        if (player.getAbilities().instabuild && level instanceof ServerLevel serverLevel && level.getBlockEntity(pos) instanceof CustomMachineTile machine) {
            ((List) machine.getComponentManager().getComponentHandler((MachineComponentType) Registration.ITEM_MACHINE_COMPONENT.get()).map(handler -> handler.getComponents().stream().filter(component -> component.getVariant().shouldDrop(machine.getComponentManager())).map(component -> component.getItemStack().copy()).filter(stack -> !stack.isEmpty()).toList()).orElse(Collections.emptyList())).forEach(stack -> Block.popResource(serverLevel, pos, stack));
        }
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        List<ItemStack> drops = super.m_49635_(state, builder);
        if (builder.getParameter(LootContextParams.BLOCK_ENTITY) instanceof CustomMachineTile machine) {
            machine.getComponentManager().getComponentHandler((MachineComponentType) Registration.ITEM_MACHINE_COMPONENT.get()).ifPresent(handler -> handler.getComponents().stream().filter(component -> component.getVariant().shouldDrop(machine.getComponentManager())).map(component -> component.getItemStack().copy()).filter(stack -> stack != ItemStack.EMPTY).forEach(drops::add));
            drops.add(CustomMachineItem.makeMachineItem(machine.getId()));
        }
        return drops;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(BlockStateProperties.HORIZONTAL_FACING);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return (BlockState) this.m_49966_().m_61124_(BlockStateProperties.HORIZONTAL_FACING, context.m_8125_().getOpposite());
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return PlatformHelper.createMachineTile(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide() ? Utils.createTickerHelper(type, (BlockEntityType) Registration.CUSTOM_MACHINE_TILE.get(), CustomMachineTile::clientTick) : Utils.createTickerHelper(type, (BlockEntityType) Registration.CUSTOM_MACHINE_TILE.get(), CustomMachineTile::serverTick);
    }

    @Override
    public boolean isSignalSource(BlockState state) {
        return true;
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        BlockEntity tile = level.getBlockEntity(pos);
        return tile instanceof CustomMachineTile ? (Integer) ((CustomMachineTile) tile).getComponentManager().getComponent((MachineComponentType) Registration.REDSTONE_MACHINE_COMPONENT.get()).map(RedstoneMachineComponent::getComparatorInput).orElse(0) : 0;
    }

    @Override
    public int getDirectSignal(BlockState state, BlockGetter level, BlockPos pos, Direction side) {
        BlockEntity tile = level.getBlockEntity(pos);
        return tile instanceof CustomMachineTile ? (Integer) ((CustomMachineTile) tile).getComponentManager().getComponent((MachineComponentType) Registration.REDSTONE_MACHINE_COMPONENT.get()).map(RedstoneMachineComponent::getPowerOutput).orElse(0) : 0;
    }

    @Override
    public int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction side) {
        BlockEntity tile = level.getBlockEntity(pos);
        return tile instanceof CustomMachineTile ? (Integer) ((CustomMachineTile) tile).getComponentManager().getComponent((MachineComponentType) Registration.REDSTONE_MACHINE_COMPONENT.get()).map(RedstoneMachineComponent::getPowerOutput).orElse(0) : 0;
    }

    @Override
    public float getDestroyProgress(BlockState state, Player player, BlockGetter level, BlockPos pos) {
        return (Float) Optional.ofNullable(level.getBlockEntity(pos)).filter(tile -> tile instanceof CustomMachineTile).map(tile -> (CustomMachineTile) tile).map(CustomMachineTile::getAppearance).map(appearance -> Utils.getMachineBreakSpeed(appearance, level, pos, player)).orElseGet(() -> {
            float f = state.m_60800_(level, pos);
            if (f == -1.0F) {
                return 0.0F;
            } else {
                int i = PlatformHelper.hasCorrectToolsForDrops(player, state) ? 30 : 100;
                return player.getDestroySpeed(state) / f / (float) i;
            }
        });
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return (VoxelShape) Optional.ofNullable(level.getBlockEntity(pos)).filter(tile -> tile instanceof CustomMachineTile).map(tile -> (VoxelShape) ((CustomMachineTile) tile).getAppearance().getCollisionShape().apply((Direction) state.m_61143_(BlockStateProperties.HORIZONTAL_FACING))).orElse(super.m_5939_(state, level, pos, context));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return (VoxelShape) Optional.ofNullable(level.getBlockEntity(pos)).filter(tile -> tile instanceof CustomMachineTile).map(tile -> ((CustomMachineTile) tile).getAppearance().getShape().apply((Direction) state.m_61143_(BlockStateProperties.HORIZONTAL_FACING))).orElse(super.m_5940_(state, level, pos, context));
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        if (level.getBlockEntity(pos) instanceof CustomMachineTile customMachineTile) {
            CustomMachine machine = customMachineTile.getMachine();
            return CustomMachineItem.makeMachineItem(machine.getId());
        } else {
            return super.getCloneItemStack(level, pos, state);
        }
    }

    public float getExplosionResistance(BlockState state, BlockGetter level, BlockPos pos, Explosion explosion) {
        return (Float) Optional.ofNullable(level.getBlockEntity(pos)).filter(tile -> tile instanceof CustomMachineTile).map(tile -> ((CustomMachineTile) tile).getAppearance().getResistance()).orElse(super.getExplosionResistance());
    }

    public SoundType getSoundType(BlockState state, LevelReader level, BlockPos pos, @Nullable Entity entity) {
        return (SoundType) Optional.ofNullable(level.m_7702_(pos)).filter(blockEntity -> blockEntity instanceof CustomMachineTile).map(tile -> ((CustomMachineTile) tile).getAppearance().getInteractionSound()).orElse(super.getSoundType(state));
    }

    public float getFriction(BlockState state, LevelReader level, BlockPos pos, @Nullable Entity entity) {
        return this.m_49958_();
    }

    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
        BlockEntity tile = level.getBlockEntity(pos);
        if (tile instanceof CustomMachineTile) {
            IMachineComponentManager manager = ((CustomMachineTile) tile).getComponentManager();
            return (Integer) manager.getComponent((MachineComponentType) Registration.LIGHT_MACHINE_COMPONENT.get()).map(LightMachineComponent::getMachineLight).orElse(0);
        } else {
            return 0;
        }
    }

    public boolean shouldDisplayFluidOverlay(BlockState state, BlockAndTintGetter level, BlockPos pos, FluidState fluidState) {
        return true;
    }

    public float[] getBeaconColorMultiplier(BlockState blockState, LevelReader levelReader, BlockPos blockPos, BlockPos blockPos1) {
        return new float[0];
    }

    public boolean canHarvestBlock(BlockState state, BlockGetter level, BlockPos pos, Player player) {
        return (Boolean) Optional.ofNullable(level.getBlockEntity(pos)).filter(blockEntity -> blockEntity instanceof CustomMachineTile).map(blockEntity -> (CustomMachineTile) blockEntity).map(machine -> PlatformHelper.hasCorrectToolsForDrops(player, (BlockState) MachineBlockState.CACHE.getUnchecked(machine.getAppearance()))).orElse(player.hasCorrectToolForDrops(state));
    }

    public ItemStack getCloneItemStack(BlockState state, HitResult hitResult, BlockGetter level, BlockPos pos, Player player) {
        if (level.getBlockEntity(pos) instanceof CustomMachineTile customMachineTile) {
            CustomMachine machine = customMachineTile.getMachine();
            return CustomMachineItem.makeMachineItem(machine.getId());
        } else {
            return super.getCloneItemStack(level, pos, state);
        }
    }

    public BlockState rotate(BlockState state, LevelAccessor level, BlockPos pos, Rotation rotation) {
        return (BlockState) state.m_61124_(BlockStateProperties.HORIZONTAL_FACING, rotation.rotate((Direction) state.m_61143_(BlockStateProperties.HORIZONTAL_FACING)));
    }

    public boolean shouldCheckWeakPower(BlockState blockState, SignalGetter levelReader, BlockPos blockPos, Direction direction) {
        return true;
    }

    public DyeColor getColor() {
        return DyeColor.BLACK;
    }
}
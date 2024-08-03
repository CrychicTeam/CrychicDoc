package net.mehvahdjukaar.supplementaries.common.block.blocks;

import com.google.common.base.Suppliers;
import com.mojang.authlib.GameProfile;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;
import net.mehvahdjukaar.moonlight.api.block.IPistonMotionReact;
import net.mehvahdjukaar.moonlight.api.block.ISoftFluidConsumer;
import net.mehvahdjukaar.moonlight.api.block.IWashable;
import net.mehvahdjukaar.moonlight.api.block.WaterBlock;
import net.mehvahdjukaar.moonlight.api.fluids.BuiltInSoftFluids;
import net.mehvahdjukaar.moonlight.api.fluids.SoftFluidStack;
import net.mehvahdjukaar.moonlight.api.util.FakePlayerManager;
import net.mehvahdjukaar.supplementaries.common.block.ModBlockProperties;
import net.mehvahdjukaar.supplementaries.common.block.tiles.BambooSpikesBlockTile;
import net.mehvahdjukaar.supplementaries.configs.CommonConfigs;
import net.mehvahdjukaar.supplementaries.integration.CompatHandler;
import net.mehvahdjukaar.supplementaries.integration.QuarkCompat;
import net.mehvahdjukaar.supplementaries.reg.ModDamageSources;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.LingeringPotionItem;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.piston.PistonMovingBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class BambooSpikesBlock extends WaterBlock implements ISoftFluidConsumer, EntityBlock, IWashable, IPistonMotionReact {

    protected static final VoxelShape SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 13.0, 16.0);

    protected static final VoxelShape SHAPE_UP = Block.box(0.0, 0.0, 0.0, 16.0, 1.0, 16.0);

    protected static final VoxelShape SHAPE_DOWN = Block.box(0.0, 15.0, 0.0, 16.0, 16.0, 16.0);

    protected static final VoxelShape SHAPE_NORTH = Block.box(0.0, 0.0, 15.0, 16.0, 16.0, 16.0);

    protected static final VoxelShape SHAPE_SOUTH = Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 1.0);

    protected static final VoxelShape SHAPE_WEST = Block.box(15.0, 0.0, 0.0, 16.0, 16.0, 16.0);

    protected static final VoxelShape SHAPE_EAST = Block.box(0.0, 0.0, 0.0, 1.0, 16.0, 16.0);

    public static final DirectionProperty FACING = DirectionalBlock.FACING;

    public static final BooleanProperty TIPPED = ModBlockProperties.TIPPED;

    private static final GameProfile SPIKE_PLAYER = new GameProfile(UUID.randomUUID(), "Spike Fake Player");

    private static final Supplier<Boolean> TIPPED_ENABLED = Suppliers.memoize(CommonConfigs.Functional.TIPPED_SPIKES_ENABLED::get);

    public BambooSpikesBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(FACING, Direction.NORTH)).m_61124_(WATERLOGGED, false)).m_61124_(TIPPED, false));
    }

    public static DamageSource getDamageSource(Level level) {
        if ((Boolean) CommonConfigs.Functional.BAMBOO_SPIKES_DROP_LOOT.get()) {
            ServerPlayer fakePlayer = (ServerPlayer) FakePlayerManager.get(SPIKE_PLAYER, level);
            fakePlayer.getAdvancements().stopListening();
            fakePlayer.setGameMode(GameType.SPECTATOR);
            return ModDamageSources.spikePlayer(fakePlayer);
        } else {
            return ModDamageSources.spike();
        }
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return (BlockState) state.m_61124_(FACING, rot.rotate((Direction) state.m_61143_(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.m_60717_(mirrorIn.getRotation((Direction) state.m_61143_(FACING)));
    }

    @Override
    public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.m_6402_(worldIn, pos, state, placer, stack);
        if (worldIn.getBlockEntity(pos) instanceof BambooSpikesBlockTile tile) {
            CompoundTag com = stack.getTag();
            if (com != null) {
                Potion p = PotionUtils.getPotion(stack);
                if (p != Potions.EMPTY && com.contains("Damage")) {
                    tile.potion = p;
                    tile.setMissingCharges(com.getInt("Damage"));
                }
            }
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        CompoundTag com = context.m_43722_().getTag();
        int charges = com != null ? context.m_43722_().getMaxDamage() - com.getInt("Damage") : 0;
        boolean flag = context.m_43725_().getFluidState(context.getClickedPos()).getType() == Fluids.WATER;
        return (BlockState) ((BlockState) ((BlockState) this.m_49966_().m_61124_(FACING, context.m_43719_())).m_61124_(WATERLOGGED, flag)).m_61124_(TIPPED, charges != 0 && PotionUtils.getPotion(com) != Potions.EMPTY);
    }

    public ItemStack getSpikeItem(BlockEntity te) {
        return te instanceof BambooSpikesBlockTile tile ? tile.getSpikeItem() : new ItemStack((ItemLike) ModRegistry.BAMBOO_SPIKES_ITEM.get());
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        List<ItemStack> list = new ArrayList();
        list.add(this.getSpikeItem(builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY)));
        return list;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return switch((Direction) state.m_61143_(FACING)) {
            case DOWN ->
                SHAPE_DOWN;
            case UP ->
                SHAPE_UP;
            case EAST ->
                SHAPE_EAST;
            case WEST ->
                SHAPE_WEST;
            case NORTH ->
                SHAPE_NORTH;
            case SOUTH ->
                SHAPE_SOUTH;
        };
    }

    @Override
    public VoxelShape getInteractionShape(BlockState state, BlockGetter worldIn, BlockPos pos) {
        return Shapes.block();
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entityIn) {
        if (entityIn instanceof Player player && player.isCreative()) {
            return;
        }
        if (entityIn instanceof LivingEntity le && entityIn.isAlive()) {
            boolean up = state.m_61143_(FACING) == Direction.UP;
            double vy = up ? 0.45 : 0.95;
            float fall = entityIn.fallDistance;
            entityIn.makeStuckInBlock(state, new Vec3(0.95, vy, 0.95));
            entityIn.fallDistance = fall;
            if (!level.isClientSide) {
                if (up && entityIn instanceof Player && entityIn.isShiftKeyDown()) {
                    return;
                }
                float damage = entityIn.getY() > (double) pos.m_123342_() + 0.0625 ? 3.0F : 1.5F;
                entityIn.hurt(getDamageSource(level), damage);
                if ((Boolean) state.m_61143_(TIPPED) && level.getBlockEntity(pos) instanceof BambooSpikesBlockTile te && te.interactWithEntity(le, level)) {
                    level.setBlock(pos, (BlockState) state.m_61124_(TIPPED, false), 3);
                }
            }
        }
    }

    public BlockPathTypes getBlockPathType(BlockState state, BlockGetter level, BlockPos pos, @Nullable Mob mob) {
        return BlockPathTypes.DAMAGE_OTHER;
    }

    @Nullable
    public BlockPathTypes getAdjacentBlockPathType(BlockState state, BlockGetter level, BlockPos pos, @Nullable Mob mob, BlockPathTypes originalType) {
        return BlockPathTypes.DAMAGE_OTHER;
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if ((Boolean) TIPPED_ENABLED.get() && !(Boolean) state.m_61143_(TIPPED)) {
            ItemStack stack = player.m_21120_(handIn);
            if (stack.getItem() instanceof LingeringPotionItem) {
                if (tryAddingPotion(state, worldIn, pos, PotionUtils.getPotion(stack)) && !player.isCreative()) {
                    player.m_21008_(handIn, ItemUtils.createFilledResult(stack.copy(), player, new ItemStack(Items.GLASS_BOTTLE), false));
                }
                return InteractionResult.sidedSuccess(worldIn.isClientSide);
            } else {
                return InteractionResult.PASS;
            }
        } else {
            return InteractionResult.PASS;
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, WATERLOGGED, TIPPED);
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        return this.getSpikeItem(level.getBlockEntity(pos));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return pState.m_61143_(TIPPED) ? new BambooSpikesBlockTile(pPos, pState) : null;
    }

    @Override
    public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
        if (0.01 > (double) random.nextFloat() && (Boolean) state.m_61143_(TIPPED) && world.getBlockEntity(pos) instanceof BambooSpikesBlockTile tile) {
            tile.makeParticle(world);
        }
    }

    @Override
    public boolean tryAcceptingFluid(Level world, BlockState state, BlockPos pos, SoftFluidStack fluid) {
        if ((Boolean) TIPPED_ENABLED.get() && !(Boolean) state.m_61143_(TIPPED)) {
            return fluid.is(BuiltInSoftFluids.POTION.get()) && fluid.hasTag() && fluid.getTag().getString("PotionType").equals("Lingering") ? tryAddingPotion(state, world, pos, PotionUtils.getPotion(fluid.getTag())) : false;
        } else {
            return false;
        }
    }

    public static boolean tryAddingPotion(BlockState state, LevelAccessor world, BlockPos pos, Potion potion) {
        world.m_7731_(pos, (BlockState) state.m_61124_(TIPPED, true), 0);
        BlockEntity te = world.m_7702_(pos);
        if (te instanceof BambooSpikesBlockTile tile && tile.tryApplyPotion(potion)) {
            world.playSound(null, pos, SoundEvents.HONEY_BLOCK_FALL, SoundSource.BLOCKS, 0.5F, 1.5F);
            world.m_7731_(pos, (BlockState) state.m_61124_(TIPPED, true), 3);
            return true;
        }
        if (te != null) {
            te.setRemoved();
        }
        world.m_7731_(pos, (BlockState) state.m_61124_(TIPPED, false), 0);
        return false;
    }

    @Override
    public boolean tryWash(Level level, BlockPos pos, BlockState state) {
        if ((Boolean) state.m_61143_(TIPPED)) {
            if (!level.isClientSide) {
                BlockEntity te = level.getBlockEntity(pos);
                if (te != null) {
                    te.setRemoved();
                }
                level.setBlock(pos, (BlockState) state.m_61124_(TIPPED, false), 3);
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean ticksWhileMoved() {
        return true;
    }

    @Override
    public void moveTick(BlockState movedState, Level level, BlockPos pos, AABB aabb, PistonMovingBlockEntity tile) {
        boolean sameDir = ((Direction) movedState.m_61143_(FACING)).equals(tile.getDirection());
        if (CompatHandler.QUARK) {
            QuarkCompat.tickPiston(level, pos, movedState, aabb, sameDir, tile);
        }
    }
}
package se.mickelus.tetra.blocks.forged.hammer;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.registries.ObjectHolder;
import se.mickelus.mutil.util.CastOptional;
import se.mickelus.mutil.util.TileEntityOptional;
import se.mickelus.tetra.TetraToolActions;
import se.mickelus.tetra.blocks.IToolProviderBlock;
import se.mickelus.tetra.blocks.TetraWaterloggedBlock;
import se.mickelus.tetra.blocks.forged.ForgedBlockCommon;
import se.mickelus.tetra.blocks.salvage.BlockInteraction;
import se.mickelus.tetra.blocks.salvage.IInteractiveBlock;
import se.mickelus.tetra.blocks.salvage.TileBlockInteraction;

@ParametersAreNonnullByDefault
public class HammerHeadBlock extends TetraWaterloggedBlock implements IInteractiveBlock, IToolProviderBlock, EntityBlock {

    public static final String identifier = "hammer_head";

    public static final VoxelShape shape = m_49796_(2.0, 14.0, 2.0, 14.0, 16.0, 14.0);

    public static final VoxelShape jamShape = m_49796_(2.0, 4.0, 2.0, 14.0, 16.0, 14.0);

    static final BlockInteraction[] interactions = new BlockInteraction[] { new TileBlockInteraction(TetraToolActions.hammer, 4, Direction.EAST, 1.0F, 11.0F, 7.0F, 11.0F, HammerHeadBlockEntity.class, HammerHeadBlockEntity::isJammed, (world, pos, blockState, player, hand, hitFace) -> unjam(world, pos, player)) };

    @ObjectHolder(registryName = "block", value = "tetra:hammer_head")
    public static HammerHeadBlock instance;

    public HammerHeadBlock() {
        super(ForgedBlockCommon.propertiesNotSolid);
    }

    private static boolean unjam(Level world, BlockPos pos, Player playerEntity) {
        TileEntityOptional.from(world, pos, HammerHeadBlockEntity.class).ifPresent(tile -> tile.setJammed(false));
        world.playSound(playerEntity, pos, SoundEvents.ZOMBIE_ATTACK_IRON_DOOR, SoundSource.PLAYERS, 1.0F, 0.5F);
        return true;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter world, List<Component> tooltip, TooltipFlag advanced) {
        tooltip.add(ForgedBlockCommon.locationTooltip);
    }

    private boolean isJammed(BlockGetter world, BlockPos pos) {
        return (Boolean) TileEntityOptional.from(world, pos, HammerHeadBlockEntity.class).map(HammerHeadBlockEntity::isJammed).orElse(false);
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        return BlockInteraction.attemptInteraction(world, state, pos, player, hand, hit);
    }

    private boolean isFunctional(Level world, BlockPos pos) {
        BlockPos basePos = pos.above();
        boolean functionalBase = (Boolean) CastOptional.cast(world.getBlockState(basePos).m_60734_(), HammerBaseBlock.class).map(base -> base.isFunctional(world, basePos)).orElse(false);
        return functionalBase && !this.isJammed(world, pos);
    }

    @Override
    public boolean canProvideTools(Level world, BlockPos pos, BlockPos targetPos) {
        return pos.equals(targetPos.above());
    }

    @Override
    public Collection<ToolAction> getTools(Level world, BlockPos pos, BlockState blockState) {
        return this.isFunctional(world, pos) ? Collections.singletonList(TetraToolActions.hammer) : Collections.emptyList();
    }

    @Override
    public int getToolLevel(Level world, BlockPos pos, BlockState blockState, ToolAction toolAction) {
        if (TetraToolActions.hammer.equals(toolAction) && this.isFunctional(world, pos)) {
            BlockPos basePos = pos.above();
            HammerBaseBlock baseBlock = (HammerBaseBlock) world.getBlockState(basePos).m_60734_();
            return baseBlock.getHammerLevel(world, basePos);
        } else {
            return -1;
        }
    }

    @Override
    public ItemStack onCraftConsumeTool(Level world, BlockPos pos, BlockState blockState, ItemStack targetStack, String slot, boolean isReplacing, Player player, ToolAction requiredTool, int requiredLevel, boolean consumeResources) {
        BlockPos basePos = pos.above();
        BlockState baseState = world.getBlockState(basePos);
        ItemStack upgradedStack = (ItemStack) CastOptional.cast(baseState.m_60734_(), HammerBaseBlock.class).map(base -> base.applyCraftEffects(world, basePos, baseState, targetStack, slot, isReplacing, player, requiredTool, requiredLevel, consumeResources)).orElse(targetStack);
        if (consumeResources) {
            TileEntityOptional.from(world, pos, HammerHeadBlockEntity.class).ifPresent(HammerHeadBlockEntity::activate);
            world.playSound(player, pos, SoundEvents.ANVIL_LAND, SoundSource.PLAYERS, 0.2F, (float) (0.5 + Math.random() * 0.2));
        }
        return upgradedStack;
    }

    @Override
    public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource rand) {
        if (this.isJammed(world, pos) && rand.nextBoolean()) {
            boolean flipped = rand.nextBoolean();
            float x = (float) pos.m_123341_() + (flipped ? (rand.nextBoolean() ? 0.1F : 0.9F) : rand.nextFloat());
            float z = (float) pos.m_123343_() + (!flipped ? (rand.nextBoolean() ? 0.1F : 0.9F) : rand.nextFloat());
            world.addParticle(new BlockParticleOption(ParticleTypes.FALLING_DUST, state), (double) x, (double) (pos.m_123342_() + 1), (double) z, 0.0, 0.0, 0.0);
        }
    }

    @Override
    public ItemStack onActionConsumeTool(Level world, BlockPos pos, BlockState blockState, ItemStack targetStack, Player player, ToolAction requiredTool, int requiredLevel, boolean consumeResources) {
        BlockPos basePos = pos.above();
        BlockState baseState = world.getBlockState(basePos);
        ItemStack upgradedStack = (ItemStack) CastOptional.cast(baseState.m_60734_(), HammerBaseBlock.class).map(base -> base.applyActionEffects(world, basePos, baseState, targetStack, player, requiredTool, requiredLevel, consumeResources)).orElse(targetStack);
        if (consumeResources) {
            TileEntityOptional.from(world, pos, HammerHeadBlockEntity.class).ifPresent(HammerHeadBlockEntity::activate);
            world.playSound(player, pos, SoundEvents.ANVIL_LAND, SoundSource.BLOCKS, 0.2F, (float) (0.5 + Math.random() * 0.2));
        }
        return upgradedStack;
    }

    @Override
    public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor world, BlockPos currentPos, BlockPos facingPos) {
        if (Direction.UP.equals(facing) && !HammerBaseBlock.instance.equals(facingState.m_60734_())) {
            return state.m_61143_(BlockStateProperties.WATERLOGGED) ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState();
        } else {
            return super.updateShape(state, facing, facingState, world, currentPos, facingPos);
        }
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter world, BlockPos pos, CollisionContext context) {
        if (context == CollisionContext.empty()) {
            return jamShape;
        } else {
            return this.isJammed(world, pos) ? jamShape : shape;
        }
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public BlockInteraction[] getPotentialInteractions(Level world, BlockPos pos, BlockState blockState, Direction face, Collection<ToolAction> tools) {
        return this.isJammed(world, pos) && face.getAxis().getPlane() == Direction.Plane.HORIZONTAL ? interactions : new BlockInteraction[0];
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos0, BlockState blockState1) {
        return new HammerHeadBlockEntity(blockPos0, blockState1);
    }
}
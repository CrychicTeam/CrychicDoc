package se.mickelus.tetra.blocks.multischematic;

import java.util.Collection;
import java.util.stream.Stream;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.predicate.BlockStatePredicate;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import se.mickelus.mutil.util.RotationHelper;
import se.mickelus.tetra.ServerScheduler;
import se.mickelus.tetra.TetraToolActions;
import se.mickelus.tetra.blocks.salvage.BlockInteraction;
import se.mickelus.tetra.blocks.salvage.IInteractiveBlock;
import se.mickelus.tetra.effect.EffectHelper;

public class MultiblockSchematicBlock extends HorizontalDirectionalBlock implements IInteractiveBlock {

    public static final DirectionProperty facingProp = BlockStateProperties.HORIZONTAL_FACING;

    public final int x;

    public final int y;

    public final int height;

    public final int width;

    public final RegistryObject<RuinedMultiblockSchematicBlock> ruinedRef;

    protected String schematic;

    protected ResourceLocation pryTable;

    protected BlockInteraction[] pryAction = new BlockInteraction[] { new BlockInteraction(TetraToolActions.pry, 1, Direction.EAST, 6.0F, 10.0F, 7.0F, 10.0F, BlockStatePredicate.ANY, this::pryBlock) };

    public MultiblockSchematicBlock(BlockBehaviour.Properties properties, String schematic, RegistryObject<RuinedMultiblockSchematicBlock> ruinedRef, @Nullable ResourceLocation pryTable, int x, int y, int height, int width) {
        super(properties);
        this.schematic = schematic;
        this.ruinedRef = ruinedRef;
        this.pryTable = pryTable;
        this.x = x;
        this.y = y;
        this.height = height;
        this.width = width;
        this.m_49959_((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(facingProp, Direction.EAST));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(facingProp);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return (BlockState) super.m_5573_(context).m_61124_(facingProp, context.m_8125_().getOpposite());
    }

    protected Stream<MultiblockSchematicBlock.Part> getSchematicParts(BlockState blockState, LevelAccessor level, BlockPos blockPos) {
        Direction dir = (Direction) blockState.m_61143_(facingProp);
        AABB baseBox = new AABB(0.0, 0.0, 0.0, (double) (this.width - 1), (double) (this.height - 1), 0.0);
        return BlockPos.betweenClosedStream(baseBox).map(pos -> {
            BlockPos worldPos = RotationHelper.rotateDirection(pos.offset(-this.x, -this.y, 0), dir).offset(blockPos);
            return new MultiblockSchematicBlock.Part(pos.immutable(), worldPos, level.m_8055_(worldPos));
        });
    }

    @Override
    public void onPlace(BlockState blockState, Level level, BlockPos blockPos, BlockState oldBlock, boolean boolean0) {
        super.m_6807_(blockState, level, blockPos, oldBlock, boolean0);
        this.notifyPrimary(level, blockPos, blockState);
    }

    @Override
    public void destroy(LevelAccessor level, BlockPos blockPos, BlockState blockState) {
        super.m_6786_(level, blockPos, blockState);
        if (!level.m_5776_()) {
            this.notifyPrimary(level, blockPos, blockState);
        }
    }

    @Override
    public void setPlacedBy(Level level, BlockPos blockPos, BlockState blockState, @Nullable LivingEntity entity, ItemStack itemStack) {
        super.m_6402_(level, blockPos, blockState, entity, itemStack);
        if (level.isClientSide()) {
            this.spawnParticles(blockState, (ClientLevel) level, blockPos);
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        return this.pryTable != null ? BlockInteraction.attemptInteraction(world, state, pos, player, hand, hit) : super.m_6227_(state, world, pos, player, hand, hit);
    }

    @Override
    public BlockInteraction[] getPotentialInteractions(Level world, BlockPos pos, BlockState blockState, Direction face, Collection<ToolAction> tools) {
        return this.pryTable != null && face.getOpposite().equals(blockState.m_61143_(facingProp)) ? this.pryAction : new BlockInteraction[0];
    }

    protected boolean pryBlock(Level world, BlockPos pos, BlockState blockState, Player player, InteractionHand hand, Direction facing) {
        boolean didBreak = EffectHelper.breakBlock(world, player, player.m_21120_(hand), pos, blockState, false, false);
        if (didBreak && world instanceof ServerLevel) {
            BlockInteraction.getLoot(this.pryTable, player, hand, (ServerLevel) world, blockState).forEach(lootStack -> m_49840_(world, pos, lootStack));
        }
        return true;
    }

    protected void notifyPrimary(LevelAccessor level, BlockPos blockPos, BlockState blockState) {
        this.getSchematicParts(blockState, level, blockPos).filter(part -> part.blockState.m_60734_() instanceof PrimaryMultiblockSchematicBlock).forEach(part -> ((PrimaryMultiblockSchematicBlock) part.blockState.m_60734_()).updateComplete(part.blockState(), level, part.worldPos(), blockPos));
    }

    protected void spawnParticles(BlockState blockState, ClientLevel level, BlockPos blockPos) {
        Vec3 face = Vec3.atLowerCornerOf(((Direction) blockState.m_61143_(facingProp)).getNormal());
        Vec3 dir = Vec3.atLowerCornerOf(((Direction) blockState.m_61143_(facingProp)).getClockWise().getNormal());
        this.getSchematicParts(blockState, level, blockPos).forEach(part -> ServerScheduler.schedule(blockPos.m_123333_(part.worldPos) * 2, () -> this.spawnParticleBlock(level, blockState, part.basePos(), part.blockState(), part.worldPos(), face, dir)));
    }

    protected void spawnParticleBlock(ClientLevel level, BlockState originState, BlockPos basePos, BlockState blockState, BlockPos pos, Vec3 face, Vec3 dir) {
        Vec3 facePos;
        DustParticleOptions particle;
        label14: {
            facePos = Vec3.atCenterOf(pos).add(face.scale(0.52));
            if (blockState.m_60734_() instanceof MultiblockSchematicBlock block && block.x == basePos.m_123341_() && block.y == basePos.m_123342_()) {
                particle = new DustParticleOptions(new Vector3f(0.1F, 0.9F, 0.5F), 1.0F);
                break label14;
            }
            particle = new DustParticleOptions(new Vector3f(0.9F, 0.3F, 0.3F), 1.0F);
        }
        this.spawnParticle(level, particle, facePos);
    }

    protected void spawnParticle(ClientLevel level, DustParticleOptions particle, Vec3 pos) {
        level.addParticle(particle, pos.x, pos.y, pos.z, 0.0, 0.0, 0.0);
    }

    public static class Builder {

        public static final String format = "%s_%d_%d";

        public static final String ruinedFormat = "%s_ruined_%d_%d";

        public static final String pryTablePrefix = "actions/forged_schematic/";

        private String identifier;

        private int height;

        private int width;

        private BlockBehaviour.Properties properties;

        private BlockBehaviour.Properties ruinedProperties;

        public Builder(String identifier, int width, int height, BlockBehaviour.Properties properties) {
            this.identifier = identifier;
            this.width = width;
            this.height = height;
            this.properties = properties;
            this.ruinedProperties = properties;
        }

        public MultiblockSchematicBlock.Builder withRuinedProperties(BlockBehaviour.Properties properties) {
            this.ruinedProperties = properties;
            return this;
        }

        public void build(DeferredRegister<Block> blocks, DeferredRegister<Item> items) {
            if (FMLEnvironment.dist.isClient()) {
                MultiblockSchematicScrollHandler.setupSchematic(this.identifier, this.width * this.height);
            }
            for (int i = 0; i < this.width; i++) {
                for (int j = 0; j < this.height; j++) {
                    int x = i;
                    int y = j;
                    String ruinedId = String.format("%s_ruined_%d_%d", this.identifier, x, y);
                    ResourceLocation brokenPryTable = new ResourceLocation("tetra", "actions/forged_schematic/" + ruinedId);
                    RegistryObject<RuinedMultiblockSchematicBlock> ruinedRef = blocks.register(ruinedId, () -> new RuinedMultiblockSchematicBlock(this.ruinedProperties, brokenPryTable));
                    String id = String.format("%s_%d_%d", this.identifier, x, y);
                    ResourceLocation pryTable = new ResourceLocation("tetra", "actions/forged_schematic/" + id);
                    RegistryObject<MultiblockSchematicBlock> ref = x == this.width / 2 && y == this.height / 2 ? blocks.register(id, () -> new PrimaryMultiblockSchematicBlock(this.properties, this.identifier, ruinedRef, pryTable, x, y, this.height, this.width)) : blocks.register(id, () -> new MultiblockSchematicBlock(this.properties, this.identifier, ruinedRef, pryTable, x, y, this.height, this.width));
                    items.register(id, () -> {
                        StackedMultiblockSchematicItem item = new StackedMultiblockSchematicItem(ref.get(), ruinedRef.get());
                        if (FMLEnvironment.dist.isClient()) {
                            MultiblockSchematicScrollHandler.addSchematic(this.identifier, y * this.width + x, item);
                        }
                        return item;
                    });
                    items.register(ruinedId, () -> new RuinedMultiblockSchematicItem(ruinedRef.get(), ref.get()));
                }
            }
        }
    }

    static record Part(BlockPos basePos, BlockPos worldPos, BlockState blockState) {
    }
}
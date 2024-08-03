package se.mickelus.tetra.blocks.multischematic;

import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.RegistryObject;
import org.joml.Vector3f;
import se.mickelus.mutil.util.RotationHelper;
import se.mickelus.tetra.ServerScheduler;
import se.mickelus.tetra.advancements.BlockUseCriterion;
import se.mickelus.tetra.blocks.ISchematicProviderBlock;

public class PrimaryMultiblockSchematicBlock extends MultiblockSchematicBlock implements ISchematicProviderBlock {

    public static final BooleanProperty complete = BooleanProperty.create("complete");

    protected final ResourceLocation[] schematics;

    public PrimaryMultiblockSchematicBlock(BlockBehaviour.Properties properties, String schematic, RegistryObject<RuinedMultiblockSchematicBlock> ruinedRef, ResourceLocation pryTable, int x, int y, int height, int width) {
        super(properties, schematic, ruinedRef, pryTable, x, y, height, width);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(facingProp, Direction.EAST)).m_61124_(complete, false));
        this.schematics = new ResourceLocation[] { new ResourceLocation("tetra", schematic) };
    }

    @Override
    public boolean canUnlockSchematics(Level world, BlockPos pos, BlockPos targetPos) {
        return (Boolean) world.getBlockState(pos).m_61143_(complete);
    }

    @Override
    public ResourceLocation[] getSchematics(Level world, BlockPos pos, BlockState blockState) {
        return this.schematics;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(complete);
    }

    public void updateComplete(BlockState blockState, LevelAccessor level, BlockPos worldPos, BlockPos placePos) {
        boolean isComplete = this.getSchematicParts(blockState, level, worldPos).filter(part -> part.blockState().m_60734_() instanceof MultiblockSchematicBlock).filter(part -> this.isCorrectPart(part.basePos(), (MultiblockSchematicBlock) part.blockState().m_60734_())).count() == (long) (this.width * this.height);
        level.m_7731_(worldPos, (BlockState) blockState.m_61124_(complete, isComplete), 3);
        if (isComplete) {
            level.m_45976_(ServerPlayer.class, new AABB(worldPos).inflate(10.0, 5.0, 10.0)).forEach(player -> BlockUseCriterion.trigger(player, blockState, ItemStack.EMPTY, ImmutableMap.builder().put("complete_schematic", "true").put("schematic", this.schematic).build()));
            this.spawnCompleteParticle(blockState, (ServerLevel) level, worldPos, placePos);
        }
    }

    protected void spawnCompleteParticle(BlockState blockState, ServerLevel level, BlockPos worldPos, BlockPos placePos) {
        Direction facing = (Direction) blockState.m_61143_(facingProp);
        Vec3 face = Vec3.atLowerCornerOf(facing.getNormal());
        Vec3 dir = Vec3.atLowerCornerOf(facing.getCounterClockWise().getNormal());
        Vec3 placeVec = Vec3.atCenterOf(placePos);
        Vec3 origin = Vec3.atBottomCenterOf(RotationHelper.rotateDirection(new BlockPos(-this.x, -this.y, 0), facing).offset(worldPos)).add(face.scale(0.52)).add(dir.scale(-0.5));
        for (float x = 0.0F; x < (float) this.width; x = (float) ((double) x + 0.333333333)) {
            Vec3 pPos = origin.add(dir.scale((double) x)).add(0.0, Math.sin((double) (System.currentTimeMillis() + 5L) + (double) this.y * 2.3) * 0.1, 0.0);
            this.spawnParticle(level, pPos, this.getDelay(pPos, placeVec));
        }
        for (float x = 0.0F; x < (float) this.width; x = (float) ((double) x + 0.333333333)) {
            Vec3 pPos = origin.add(dir.scale((double) x)).add(0.0, (double) this.height + Math.sin((double) (System.currentTimeMillis() + 2L) + (double) this.y * 2.56) * 0.1, 0.0);
            this.spawnParticle(level, pPos, this.getDelay(pPos, placeVec));
        }
        for (float y = 0.0F; y < (float) this.height; y = (float) ((double) y + 0.333333333)) {
            Vec3 pPos = origin.add(0.0, (double) y, 0.0).add(dir.scale(Math.sin((double) (System.currentTimeMillis() + 2L) + (double) y * 2.56) * 0.1));
            this.spawnParticle(level, pPos, this.getDelay(pPos, placeVec));
        }
        for (float y = 0.0F; y < (float) this.height; y = (float) ((double) y + 0.333333333)) {
            Vec3 pPos = origin.add(0.0, (double) y, 0.0).add(dir.scale((double) this.width + Math.sin((double) (System.currentTimeMillis() + 5L) + (double) y * 2.3) * 0.1));
            this.spawnParticle(level, pPos, this.getDelay(pPos, placeVec));
        }
    }

    private void spawnParticle(ServerLevel level, Vec3 pos, int delay) {
        ServerScheduler.schedule(delay, () -> level.sendParticles(new DustParticleOptions(new Vector3f(0.1F, 0.9F, 0.5F), 1.0F), pos.x, pos.y, pos.z, 1, 0.0, 0.0, 0.0, 1.0));
    }

    private int getDelay(Vec3 a, Vec3 b) {
        return 2 * (this.width + this.height) + Math.max((int) (3.0 * ((double) (this.width + this.height) - (Math.abs(a.x - b.x) + Math.abs(a.y - b.y) + Math.abs(a.z - b.z)))), 0);
    }

    protected boolean isCorrectPart(BlockPos basePos, MultiblockSchematicBlock block) {
        return this.schematic.equals(block.schematic) && block.x == basePos.m_123341_() && block.y == basePos.m_123342_();
    }

    @Override
    public void onPlace(BlockState blockState, Level level, BlockPos blockPos, BlockState oldBlock, boolean boolean0) {
        if (!this.equals(oldBlock.m_60734_())) {
            this.updateComplete(blockState, level, blockPos, blockPos);
        }
    }
}
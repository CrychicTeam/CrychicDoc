package snownee.lychee.dripstone_dripping;

import com.google.common.base.Preconditions;
import com.google.gson.JsonObject;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import net.minecraft.advancements.critereon.BlockPredicate;
import net.minecraft.advancements.critereon.LocationPredicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LocationCheck;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import snownee.lychee.LycheeLootContextParamSets;
import snownee.lychee.LycheeLootContextParams;
import snownee.lychee.RecipeSerializers;
import snownee.lychee.RecipeTypes;
import snownee.lychee.core.Job;
import snownee.lychee.core.LycheeContext;
import snownee.lychee.core.contextual.Location;
import snownee.lychee.core.contextual.Not;
import snownee.lychee.core.def.BlockPredicateHelper;
import snownee.lychee.core.post.Break;
import snownee.lychee.core.post.Delay;
import snownee.lychee.core.recipe.BlockKeyRecipe;
import snownee.lychee.core.recipe.ChanceRecipe;
import snownee.lychee.core.recipe.LycheeRecipe;
import snownee.lychee.core.recipe.type.LycheeRecipeType;
import snownee.lychee.mixin.PointedDripstoneBlockAccess;
import snownee.lychee.util.Pair;

public class DripstoneRecipe extends LycheeRecipe<DripstoneContext> implements BlockKeyRecipe<DripstoneRecipe>, ChanceRecipe {

    private float chance = 1.0F;

    protected BlockPredicate sourceBlock;

    protected BlockPredicate targetBlock;

    public DripstoneRecipe(ResourceLocation id) {
        super(id);
    }

    public boolean matches(DripstoneContext ctx, Level level) {
        return !BlockPredicateHelper.fastMatch(this.targetBlock, ctx) ? false : BlockPredicateHelper.fastMatch(this.sourceBlock, ctx.source, () -> level.getBlockEntity(ctx.getParam(LycheeLootContextParams.BLOCK_POS)));
    }

    @Override
    public float getChance() {
        return this.chance;
    }

    @Override
    public void setChance(float chance) {
        this.chance = chance;
    }

    @Override
    public LycheeRecipe.Serializer<?> getSerializer() {
        return RecipeSerializers.DRIPSTONE_DRIPPING;
    }

    @Override
    public LycheeRecipeType<?, ?> getType() {
        return RecipeTypes.DRIPSTONE_DRIPPING;
    }

    public int compareTo(DripstoneRecipe that) {
        int i = Integer.compare(this.m_5598_() ? 1 : 0, that.m_5598_() ? 1 : 0);
        if (i != 0) {
            return i;
        } else {
            i = Integer.compare(this.targetBlock == BlockPredicate.ANY ? 1 : 0, that.targetBlock == BlockPredicate.ANY ? 1 : 0);
            return i != 0 ? i : this.m_6423_().compareTo(that.m_6423_());
        }
    }

    @Override
    public BlockPredicate getBlock() {
        return this.targetBlock;
    }

    public BlockPredicate getSourceBlock() {
        return this.sourceBlock;
    }

    @Override
    public List<BlockPredicate> getBlockInputs() {
        return List.of(this.sourceBlock, this.targetBlock);
    }

    @Override
    public void applyPostActions(LycheeContext ctx, int times) {
        if (!ctx.getLevel().isClientSide) {
            ctx.enqueueActions(this.getPostActions(), times, true);
        }
    }

    public static boolean safeTick(BlockState state, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource) {
        if (!PointedDripstoneBlockAccess.callIsStalactiteStartPos(state, serverLevel, blockPos)) {
            return false;
        } else {
            float f = randomSource.nextFloat();
            return f > 0.17578125F && f > 0.05859375F ? false : on(state, serverLevel, blockPos);
        }
    }

    public static boolean on(BlockState blockState, ServerLevel level, BlockPos blockPos) {
        if (RecipeTypes.DRIPSTONE_DRIPPING.isEmpty()) {
            return false;
        } else {
            BlockPos tipPos = PointedDripstoneBlockAccess.callFindTip(blockState, level, blockPos, 11, false);
            if (tipPos == null) {
                return false;
            } else {
                BlockPos targetPos = findTargetBelowStalactiteTip(level, tipPos);
                if (targetPos == null) {
                    return false;
                } else {
                    BlockState sourceBlock = getBlockAboveStalactite(level, blockPos, blockState);
                    if (sourceBlock == null) {
                        return false;
                    } else {
                        BlockState targetBlock = level.m_8055_(targetPos);
                        Pair<DripstoneContext, DripstoneRecipe> result = RecipeTypes.DRIPSTONE_DRIPPING.process(level, targetBlock, () -> {
                            DripstoneContext.Builder builderx = new DripstoneContext.Builder(level, sourceBlock);
                            builderx.withParameter(LootContextParams.BLOCK_STATE, targetBlock);
                            Vec3 origin = new Vec3((double) targetPos.m_123341_() + 0.5, (double) targetPos.m_123342_() + 0.99, (double) targetPos.m_123343_() + 0.5);
                            builderx.withParameter(LootContextParams.ORIGIN, origin);
                            builderx.withParameter(LycheeLootContextParams.BLOCK_POS, targetPos);
                            return builderx.create(LycheeLootContextParamSets.BLOCK_ONLY);
                        });
                        if (result == null) {
                            return false;
                        } else {
                            DripstoneContext ctx = result.getFirst();
                            DripstoneRecipe recipe = result.getSecond();
                            level.m_46796_(1504, tipPos, 0);
                            int i = tipPos.m_123342_() - targetPos.m_123342_();
                            int j = 50 + i;
                            Break breakAction = new Break();
                            LocationPredicate.Builder builder = new LocationPredicate.Builder().setBlock(recipe.targetBlock);
                            LocationCheck check = (LocationCheck) LocationCheck.checkLocation(builder).build();
                            breakAction.withCondition(new Not(new Location(check)));
                            ctx.runtime.jobs.push(new Job(breakAction, 1));
                            ctx.runtime.jobs.push(new Job(new Delay((float) j / 20.0F), 1));
                            ctx.runtime.run(recipe, ctx);
                            return true;
                        }
                    }
                }
            }
        }
    }

    @Nullable
    private static BlockPos findTargetBelowStalactiteTip(Level level, BlockPos blockPos2) {
        Predicate<BlockState> predicate = blockState -> !blockState.m_60795_() && RecipeTypes.DRIPSTONE_DRIPPING.has(blockState);
        BiPredicate<BlockPos, BlockState> biPredicate = (blockPos, blockState) -> PointedDripstoneBlockAccess.callCanDripThrough(level, blockPos, blockState);
        return (BlockPos) PointedDripstoneBlockAccess.callFindBlockVertical(level, blockPos2, Direction.DOWN.getAxisDirection(), biPredicate, predicate, 11).orElse(null);
    }

    public static BlockState getBlockAboveStalactite(Level level, BlockPos blockPos2, BlockState blockState) {
        return (BlockState) PointedDripstoneBlockAccess.callFindRootBlock(level, blockPos2, blockState, 11).map(blockPos -> level.getBlockState(blockPos.above())).orElse(null);
    }

    public static class Serializer extends LycheeRecipe.Serializer<DripstoneRecipe> {

        public Serializer() {
            super(DripstoneRecipe::new);
        }

        public void fromJson(DripstoneRecipe pRecipe, JsonObject pSerializedRecipe) {
            pRecipe.sourceBlock = BlockPredicateHelper.fromJson(pSerializedRecipe.get("source_block"));
            pRecipe.targetBlock = BlockPredicateHelper.fromJson(pSerializedRecipe.get("target_block"));
            Preconditions.checkArgument(pRecipe.sourceBlock != BlockPredicate.ANY, "source_block can't be wildcard");
            Preconditions.checkArgument(pRecipe.targetBlock != BlockPredicate.ANY, "target_block can't be wildcard");
        }

        public void fromNetwork(DripstoneRecipe pRecipe, FriendlyByteBuf pBuffer) {
            pRecipe.sourceBlock = BlockPredicateHelper.fromNetwork(pBuffer);
            pRecipe.targetBlock = BlockPredicateHelper.fromNetwork(pBuffer);
        }

        public void toNetwork0(FriendlyByteBuf pBuffer, DripstoneRecipe pRecipe) {
            BlockPredicateHelper.toNetwork(pRecipe.sourceBlock, pBuffer);
            BlockPredicateHelper.toNetwork(pRecipe.targetBlock, pBuffer);
        }
    }
}
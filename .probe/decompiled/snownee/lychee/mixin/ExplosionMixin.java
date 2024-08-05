package snownee.lychee.mixin;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import snownee.lychee.RecipeTypes;
import snownee.lychee.block_exploding.BlockExplodingContext;
import snownee.lychee.block_exploding.BlockExplodingRecipe;

@Mixin(value = { Explosion.class }, priority = 700)
public abstract class ExplosionMixin {

    @Unique
    private static final ThreadLocal<snownee.lychee.util.Pair<BlockExplodingContext.Builder, List<ItemStack>>> lychee$CONTEXT = ThreadLocal.withInitial(() -> snownee.lychee.util.Pair.of(null, null));

    @Final
    @Shadow
    private float radius;

    @Final
    @Shadow
    @Nullable
    private Entity source;

    @Final
    @Shadow
    private Level level;

    @Shadow
    @Final
    private Explosion.BlockInteraction blockInteraction;

    @Shadow
    private static void addBlockDrops(ObjectArrayList<Pair<ItemStack, BlockPos>> objectArrayList, ItemStack itemStack, BlockPos blockPos) {
        throw new AssertionError();
    }

    @Inject(method = { "lambda$finalizeExplosion$0", "m_46071_" }, at = { @At("HEAD") }, cancellable = true)
    private static void lychee_deferAddingDrops(ObjectArrayList<Pair<ItemStack, BlockPos>> objectArrayList, BlockPos blockPos, ItemStack itemStack, CallbackInfo ci) {
        snownee.lychee.util.Pair<BlockExplodingContext.Builder, List<ItemStack>> pair = (snownee.lychee.util.Pair<BlockExplodingContext.Builder, List<ItemStack>>) lychee$CONTEXT.get();
        if (pair.getSecond() != null) {
            pair.getSecond().add(itemStack);
            ci.cancel();
        }
    }

    @Inject(method = { "finalizeExplosion" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;getDrops(Lnet/minecraft/world/level/storage/loot/LootParams$Builder;)Ljava/util/List;") }, locals = LocalCapture.CAPTURE_FAILHARD)
    private void initDeferring(boolean bl, CallbackInfo ci, boolean bl2, ObjectArrayList<Pair<ItemStack, BlockPos>> objectArrayList, boolean bl3, ObjectListIterator<Pair<ItemStack, BlockPos>> var5, BlockPos blockPos, BlockState state, Block block, BlockPos blockPos2, Level level, ServerLevel serverLevel, BlockEntity blockEntity) {
        snownee.lychee.util.Pair<BlockExplodingContext.Builder, List<ItemStack>> pair = (snownee.lychee.util.Pair<BlockExplodingContext.Builder, List<ItemStack>>) lychee$CONTEXT.get();
        if (RecipeTypes.BLOCK_EXPLODING.isEmpty() || !RecipeTypes.BLOCK_EXPLODING.has(state)) {
            pair.setSecond(null);
        } else if (pair.getFirst() == null) {
            pair.setSecond(new ObjectArrayList());
        } else {
            pair.getSecond().clear();
        }
    }

    @Inject(method = { "finalizeExplosion" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;onBlockExploded(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/Explosion;)V", remap = false) }, locals = LocalCapture.CAPTURE_FAILHARD)
    private void lychee_removeBlockPre(boolean bl, CallbackInfo ci, boolean bl2, ObjectArrayList<Pair<ItemStack, BlockPos>> objectArrayList, boolean bl3, ObjectListIterator<Pair<ItemStack, BlockPos>> var5, BlockPos blockPos, BlockState state, Block block) {
        if (!this.level.isClientSide && !RecipeTypes.BLOCK_EXPLODING.isEmpty() && RecipeTypes.BLOCK_EXPLODING.has(state)) {
            BlockExplodingContext.Builder builder = new BlockExplodingContext.Builder(this.level);
            builder.withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(blockPos));
            builder.withParameter(LootContextParams.BLOCK_STATE, state);
            BlockEntity blockEntity = state.m_155947_() ? this.level.getBlockEntity(blockPos) : null;
            builder.withOptionalParameter(LootContextParams.BLOCK_ENTITY, blockEntity);
            builder.withOptionalParameter(LootContextParams.THIS_ENTITY, this.source);
            if (this.blockInteraction == Explosion.BlockInteraction.DESTROY_WITH_DECAY) {
                builder.withParameter(LootContextParams.EXPLOSION_RADIUS, Float.valueOf(this.radius));
            }
            ((snownee.lychee.util.Pair) lychee$CONTEXT.get()).setFirst(builder);
        }
    }

    @Inject(method = { "finalizeExplosion" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;onBlockExploded(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/Explosion;)V", remap = false, shift = Shift.AFTER) }, locals = LocalCapture.CAPTURE_FAILHARD)
    private void lychee_removeBlockPost(boolean bl, CallbackInfo ci, boolean bl2, ObjectArrayList<Pair<ItemStack, BlockPos>> objectArrayList, boolean bl3, ObjectListIterator<Pair<ItemStack, BlockPos>> var5, BlockPos blockPos, BlockState state, Block block) {
        if (!this.level.isClientSide) {
            snownee.lychee.util.Pair<BlockExplodingContext.Builder, List<ItemStack>> pair = (snownee.lychee.util.Pair<BlockExplodingContext.Builder, List<ItemStack>>) lychee$CONTEXT.get();
            BlockExplodingContext.Builder ctxBuilder = pair.getFirst();
            if (ctxBuilder != null) {
                pair.setFirst(null);
                snownee.lychee.util.Pair<BlockExplodingContext, BlockExplodingRecipe> result = RecipeTypes.BLOCK_EXPLODING.process(this.level, state, () -> ctxBuilder.create(RecipeTypes.BLOCK_EXPLODING.contextParamSet));
                if (result == null) {
                    for (ItemStack stack : pair.getSecond()) {
                        addBlockDrops(objectArrayList, stack, blockPos);
                    }
                } else {
                    BlockExplodingContext ctx = result.getFirst();
                    if (ctx.runtime.doDefault && pair.getSecond() != null) {
                        for (ItemStack stack : pair.getSecond()) {
                            addBlockDrops(objectArrayList, stack, blockPos);
                        }
                    }
                    for (ItemStack stack : ctx.itemHolders.tempList) {
                        addBlockDrops(objectArrayList, stack, blockPos);
                    }
                }
            }
        }
    }
}
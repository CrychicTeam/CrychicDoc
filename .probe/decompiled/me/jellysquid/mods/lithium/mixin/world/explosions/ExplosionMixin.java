package me.jellysquid.mods.lithium.mixin.world.explosions;

import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import me.jellysquid.mods.lithium.common.util.Pos;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ Explosion.class })
public abstract class ExplosionMixin {

    @Shadow
    @Final
    private float radius;

    @Shadow
    @Final
    private double x;

    @Shadow
    @Final
    private double y;

    @Shadow
    @Final
    private double z;

    @Shadow
    @Final
    private Level level;

    @Shadow
    @Final
    private ExplosionDamageCalculator damageCalculator;

    @Shadow
    @Final
    private boolean fire;

    private final BlockPos.MutableBlockPos cachedPos = new BlockPos.MutableBlockPos();

    private int prevChunkX = Integer.MIN_VALUE;

    private int prevChunkZ = Integer.MIN_VALUE;

    private ChunkAccess prevChunk;

    private boolean explodeAirBlocks;

    private int minY;

    private int maxY;

    @Inject(method = { "<init>(Lnet/minecraft/world/World;Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/damage/DamageSource;Lnet/minecraft/world/explosion/ExplosionBehavior;DDDFZLnet/minecraft/world/explosion/Explosion$DestructionType;)V" }, at = { @At("TAIL") })
    private void init(Level world, Entity entity, DamageSource damageSource, ExplosionDamageCalculator explosionBehavior, double d, double e, double f, float g, boolean bl, Explosion.BlockInteraction destructionType, CallbackInfo ci) {
        this.minY = this.level.m_141937_();
        this.maxY = this.level.m_151558_();
        boolean explodeAir = this.fire;
        if (!explodeAir && this.level.dimension() == Level.END && this.level.dimensionTypeRegistration().is(BuiltinDimensionTypes.END)) {
            float overestimatedExplosionRange = (float) (8 + (int) (6.0F * this.radius));
            int endPortalX = 0;
            int endPortalZ = 0;
            if ((double) overestimatedExplosionRange > Math.abs(this.x - (double) endPortalX) && (double) overestimatedExplosionRange > Math.abs(this.z - (double) endPortalZ)) {
                explodeAir = true;
            }
        }
        this.explodeAirBlocks = explodeAir;
    }

    @Redirect(method = { "collectBlocksAndDamageEntities()V" }, at = @At(value = "INVOKE", target = "Lcom/google/common/collect/Sets;newHashSet()Ljava/util/HashSet;", remap = false))
    public HashSet<BlockPos> skipNewHashSet() {
        return null;
    }

    @ModifyConstant(method = { "collectBlocksAndDamageEntities()V" }, constant = { @Constant(intValue = 16, ordinal = 1) })
    public int skipLoop(int prevValue) {
        return 0;
    }

    @Redirect(method = { "collectBlocksAndDamageEntities()V" }, at = @At(value = "INVOKE", target = "Lit/unimi/dsi/fastutil/objects/ObjectArrayList;addAll(Ljava/util/Collection;)Z", remap = false))
    public boolean collectBlocks(ObjectArrayList<BlockPos> affectedBlocks, Collection<BlockPos> collection) {
        LongOpenHashSet touched = new LongOpenHashSet(0);
        RandomSource random = this.level.random;
        for (int rayX = 0; rayX < 16; rayX++) {
            boolean xPlane = rayX == 0 || rayX == 15;
            double vecX = (double) ((float) rayX / 15.0F * 2.0F - 1.0F);
            for (int rayY = 0; rayY < 16; rayY++) {
                boolean yPlane = rayY == 0 || rayY == 15;
                double vecY = (double) ((float) rayY / 15.0F * 2.0F - 1.0F);
                for (int rayZ = 0; rayZ < 16; rayZ++) {
                    boolean zPlane = rayZ == 0 || rayZ == 15;
                    if (xPlane || yPlane || zPlane) {
                        double vecZ = (double) ((float) rayZ / 15.0F * 2.0F - 1.0F);
                        this.performRayCast(random, vecX, vecY, vecZ, touched);
                    }
                }
            }
        }
        LongIterator it = touched.iterator();
        boolean added = false;
        while (it.hasNext()) {
            added |= affectedBlocks.add(BlockPos.of(it.nextLong()));
        }
        return added;
    }

    private void performRayCast(RandomSource random, double vecX, double vecY, double vecZ, LongOpenHashSet touched) {
        double dist = Math.sqrt(vecX * vecX + vecY * vecY + vecZ * vecZ);
        double normX = vecX / dist * 0.3;
        double normY = vecY / dist * 0.3;
        double normZ = vecZ / dist * 0.3;
        float strength = this.radius * (0.7F + random.nextFloat() * 0.6F);
        double stepX = this.x;
        double stepY = this.y;
        double stepZ = this.z;
        int prevX = Integer.MIN_VALUE;
        int prevY = Integer.MIN_VALUE;
        int prevZ = Integer.MIN_VALUE;
        float prevResistance = 0.0F;
        int boundMinY = this.minY;
        for (int boundMaxY = this.maxY; strength > 0.0F; stepZ += normZ) {
            int blockX = Mth.floor(stepX);
            int blockY = Mth.floor(stepY);
            int blockZ = Mth.floor(stepZ);
            float resistance;
            if (prevX == blockX && prevY == blockY && prevZ == blockZ) {
                resistance = prevResistance;
            } else {
                if (blockY < boundMinY || blockY >= boundMaxY || blockX < -30000000 || blockZ < -30000000 || blockX >= 30000000 || blockZ >= 30000000) {
                    return;
                }
                resistance = this.traverseBlock(strength, blockX, blockY, blockZ, touched);
                prevX = blockX;
                prevY = blockY;
                prevZ = blockZ;
                prevResistance = resistance;
            }
            strength -= resistance;
            strength -= 0.22500001F;
            stepX += normX;
            stepY += normY;
        }
    }

    private float traverseBlock(float strength, int blockX, int blockY, int blockZ, LongOpenHashSet touched) {
        BlockPos pos = this.cachedPos.set(blockX, blockY, blockZ);
        if (this.level.m_151562_(blockY)) {
            Optional<Float> blastResistance = this.damageCalculator.getBlockExplosionResistance((Explosion) this, this.level, pos, Blocks.AIR.defaultBlockState(), Fluids.EMPTY.defaultFluidState());
            return blastResistance.isPresent() ? ((Float) blastResistance.get() + 0.3F) * 0.3F : 0.0F;
        } else {
            int chunkX = Pos.ChunkCoord.fromBlockCoord(blockX);
            int chunkZ = Pos.ChunkCoord.fromBlockCoord(blockZ);
            if (this.prevChunkX != chunkX || this.prevChunkZ != chunkZ) {
                this.prevChunk = this.level.getChunk(chunkX, chunkZ);
                this.prevChunkX = chunkX;
                this.prevChunkZ = chunkZ;
            }
            BlockState blockState;
            float totalResistance;
            Optional<Float> blastResistance;
            label42: {
                ChunkAccess chunk = this.prevChunk;
                blockState = Blocks.AIR.defaultBlockState();
                totalResistance = 0.0F;
                if (chunk != null) {
                    LevelChunkSection section = chunk.getSections()[Pos.SectionYIndex.fromBlockCoord(chunk, blockY)];
                    if (section != null && !section.hasOnlyAir()) {
                        blockState = section.getBlockState(blockX & 15, blockY & 15, blockZ & 15);
                        if (blockState.m_60734_() != Blocks.AIR) {
                            FluidState fluidState = blockState.m_60819_();
                            blastResistance = this.damageCalculator.getBlockExplosionResistance((Explosion) this, this.level, pos, blockState, fluidState);
                            break label42;
                        }
                    }
                }
                blastResistance = this.damageCalculator.getBlockExplosionResistance((Explosion) this, this.level, pos, Blocks.AIR.defaultBlockState(), Fluids.EMPTY.defaultFluidState());
            }
            if (blastResistance.isPresent()) {
                totalResistance = ((Float) blastResistance.get() + 0.3F) * 0.3F;
            }
            float reducedStrength = strength - totalResistance;
            if (reducedStrength > 0.0F && (this.explodeAirBlocks || !blockState.m_60795_()) && this.damageCalculator.shouldBlockExplode((Explosion) this, this.level, pos, blockState, reducedStrength)) {
                touched.add(pos.asLong());
            }
            return totalResistance;
        }
    }
}
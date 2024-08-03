package net.minecraft.world.level;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.ProtectionEnchantment;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class Explosion {

    private static final ExplosionDamageCalculator EXPLOSION_DAMAGE_CALCULATOR = new ExplosionDamageCalculator();

    private static final int MAX_DROPS_PER_COMBINED_STACK = 16;

    private final boolean fire;

    private final Explosion.BlockInteraction blockInteraction;

    private final RandomSource random = RandomSource.create();

    private final Level level;

    private final double x;

    private final double y;

    private final double z;

    @Nullable
    private final Entity source;

    private final float radius;

    private final DamageSource damageSource;

    private final ExplosionDamageCalculator damageCalculator;

    private final ObjectArrayList<BlockPos> toBlow = new ObjectArrayList();

    private final Map<Player, Vec3> hitPlayers = Maps.newHashMap();

    public Explosion(Level level0, @Nullable Entity entity1, double double2, double double3, double double4, float float5, List<BlockPos> listBlockPos6) {
        this(level0, entity1, double2, double3, double4, float5, false, Explosion.BlockInteraction.DESTROY_WITH_DECAY, listBlockPos6);
    }

    public Explosion(Level level0, @Nullable Entity entity1, double double2, double double3, double double4, float float5, boolean boolean6, Explosion.BlockInteraction explosionBlockInteraction7, List<BlockPos> listBlockPos8) {
        this(level0, entity1, double2, double3, double4, float5, boolean6, explosionBlockInteraction7);
        this.toBlow.addAll(listBlockPos8);
    }

    public Explosion(Level level0, @Nullable Entity entity1, double double2, double double3, double double4, float float5, boolean boolean6, Explosion.BlockInteraction explosionBlockInteraction7) {
        this(level0, entity1, null, null, double2, double3, double4, float5, boolean6, explosionBlockInteraction7);
    }

    public Explosion(Level level0, @Nullable Entity entity1, @Nullable DamageSource damageSource2, @Nullable ExplosionDamageCalculator explosionDamageCalculator3, double double4, double double5, double double6, float float7, boolean boolean8, Explosion.BlockInteraction explosionBlockInteraction9) {
        this.level = level0;
        this.source = entity1;
        this.radius = float7;
        this.x = double4;
        this.y = double5;
        this.z = double6;
        this.fire = boolean8;
        this.blockInteraction = explosionBlockInteraction9;
        this.damageSource = damageSource2 == null ? level0.damageSources().explosion(this) : damageSource2;
        this.damageCalculator = explosionDamageCalculator3 == null ? this.makeDamageCalculator(entity1) : explosionDamageCalculator3;
    }

    private ExplosionDamageCalculator makeDamageCalculator(@Nullable Entity entity0) {
        return (ExplosionDamageCalculator) (entity0 == null ? EXPLOSION_DAMAGE_CALCULATOR : new EntityBasedExplosionDamageCalculator(entity0));
    }

    public static float getSeenPercent(Vec3 vec0, Entity entity1) {
        AABB $$2 = entity1.getBoundingBox();
        double $$3 = 1.0 / (($$2.maxX - $$2.minX) * 2.0 + 1.0);
        double $$4 = 1.0 / (($$2.maxY - $$2.minY) * 2.0 + 1.0);
        double $$5 = 1.0 / (($$2.maxZ - $$2.minZ) * 2.0 + 1.0);
        double $$6 = (1.0 - Math.floor(1.0 / $$3) * $$3) / 2.0;
        double $$7 = (1.0 - Math.floor(1.0 / $$5) * $$5) / 2.0;
        if (!($$3 < 0.0) && !($$4 < 0.0) && !($$5 < 0.0)) {
            int $$8 = 0;
            int $$9 = 0;
            for (double $$10 = 0.0; $$10 <= 1.0; $$10 += $$3) {
                for (double $$11 = 0.0; $$11 <= 1.0; $$11 += $$4) {
                    for (double $$12 = 0.0; $$12 <= 1.0; $$12 += $$5) {
                        double $$13 = Mth.lerp($$10, $$2.minX, $$2.maxX);
                        double $$14 = Mth.lerp($$11, $$2.minY, $$2.maxY);
                        double $$15 = Mth.lerp($$12, $$2.minZ, $$2.maxZ);
                        Vec3 $$16 = new Vec3($$13 + $$6, $$14, $$15 + $$7);
                        if (entity1.level().m_45547_(new ClipContext($$16, vec0, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entity1)).getType() == HitResult.Type.MISS) {
                            $$8++;
                        }
                        $$9++;
                    }
                }
            }
            return (float) $$8 / (float) $$9;
        } else {
            return 0.0F;
        }
    }

    public void explode() {
        this.level.m_220400_(this.source, GameEvent.EXPLODE, new Vec3(this.x, this.y, this.z));
        Set<BlockPos> $$0 = Sets.newHashSet();
        int $$1 = 16;
        for (int $$2 = 0; $$2 < 16; $$2++) {
            for (int $$3 = 0; $$3 < 16; $$3++) {
                for (int $$4 = 0; $$4 < 16; $$4++) {
                    if ($$2 == 0 || $$2 == 15 || $$3 == 0 || $$3 == 15 || $$4 == 0 || $$4 == 15) {
                        double $$5 = (double) ((float) $$2 / 15.0F * 2.0F - 1.0F);
                        double $$6 = (double) ((float) $$3 / 15.0F * 2.0F - 1.0F);
                        double $$7 = (double) ((float) $$4 / 15.0F * 2.0F - 1.0F);
                        double $$8 = Math.sqrt($$5 * $$5 + $$6 * $$6 + $$7 * $$7);
                        $$5 /= $$8;
                        $$6 /= $$8;
                        $$7 /= $$8;
                        float $$9 = this.radius * (0.7F + this.level.random.nextFloat() * 0.6F);
                        double $$10 = this.x;
                        double $$11 = this.y;
                        double $$12 = this.z;
                        for (float $$13 = 0.3F; $$9 > 0.0F; $$9 -= 0.22500001F) {
                            BlockPos $$14 = BlockPos.containing($$10, $$11, $$12);
                            BlockState $$15 = this.level.getBlockState($$14);
                            FluidState $$16 = this.level.getFluidState($$14);
                            if (!this.level.isInWorldBounds($$14)) {
                                break;
                            }
                            Optional<Float> $$17 = this.damageCalculator.getBlockExplosionResistance(this, this.level, $$14, $$15, $$16);
                            if ($$17.isPresent()) {
                                $$9 -= ($$17.get() + 0.3F) * 0.3F;
                            }
                            if ($$9 > 0.0F && this.damageCalculator.shouldBlockExplode(this, this.level, $$14, $$15, $$9)) {
                                $$0.add($$14);
                            }
                            $$10 += $$5 * 0.3F;
                            $$11 += $$6 * 0.3F;
                            $$12 += $$7 * 0.3F;
                        }
                    }
                }
            }
        }
        this.toBlow.addAll($$0);
        float $$18 = this.radius * 2.0F;
        int $$19 = Mth.floor(this.x - (double) $$18 - 1.0);
        int $$20 = Mth.floor(this.x + (double) $$18 + 1.0);
        int $$21 = Mth.floor(this.y - (double) $$18 - 1.0);
        int $$22 = Mth.floor(this.y + (double) $$18 + 1.0);
        int $$23 = Mth.floor(this.z - (double) $$18 - 1.0);
        int $$24 = Mth.floor(this.z + (double) $$18 + 1.0);
        List<Entity> $$25 = this.level.m_45933_(this.source, new AABB((double) $$19, (double) $$21, (double) $$23, (double) $$20, (double) $$22, (double) $$24));
        Vec3 $$26 = new Vec3(this.x, this.y, this.z);
        for (int $$27 = 0; $$27 < $$25.size(); $$27++) {
            Entity $$28 = (Entity) $$25.get($$27);
            if (!$$28.ignoreExplosion()) {
                double $$29 = Math.sqrt($$28.distanceToSqr($$26)) / (double) $$18;
                if ($$29 <= 1.0) {
                    double $$30 = $$28.getX() - this.x;
                    double $$31 = ($$28 instanceof PrimedTnt ? $$28.getY() : $$28.getEyeY()) - this.y;
                    double $$32 = $$28.getZ() - this.z;
                    double $$33 = Math.sqrt($$30 * $$30 + $$31 * $$31 + $$32 * $$32);
                    if ($$33 != 0.0) {
                        $$30 /= $$33;
                        $$31 /= $$33;
                        $$32 /= $$33;
                        double $$34 = (double) getSeenPercent($$26, $$28);
                        double $$35 = (1.0 - $$29) * $$34;
                        $$28.hurt(this.getDamageSource(), (float) ((int) (($$35 * $$35 + $$35) / 2.0 * 7.0 * (double) $$18 + 1.0)));
                        double $$37;
                        if ($$28 instanceof LivingEntity $$36) {
                            $$37 = ProtectionEnchantment.getExplosionKnockbackAfterDampener($$36, $$35);
                        } else {
                            $$37 = $$35;
                        }
                        $$30 *= $$37;
                        $$31 *= $$37;
                        $$32 *= $$37;
                        Vec3 $$39 = new Vec3($$30, $$31, $$32);
                        $$28.setDeltaMovement($$28.getDeltaMovement().add($$39));
                        if ($$28 instanceof Player) {
                            Player $$40 = (Player) $$28;
                            if (!$$40.isSpectator() && (!$$40.isCreative() || !$$40.getAbilities().flying)) {
                                this.hitPlayers.put($$40, $$39);
                            }
                        }
                    }
                }
            }
        }
    }

    public void finalizeExplosion(boolean boolean0) {
        if (this.level.isClientSide) {
            this.level.playLocalSound(this.x, this.y, this.z, SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 4.0F, (1.0F + (this.level.random.nextFloat() - this.level.random.nextFloat()) * 0.2F) * 0.7F, false);
        }
        boolean $$1 = this.interactsWithBlocks();
        if (boolean0) {
            if (!(this.radius < 2.0F) && $$1) {
                this.level.addParticle(ParticleTypes.EXPLOSION_EMITTER, this.x, this.y, this.z, 1.0, 0.0, 0.0);
            } else {
                this.level.addParticle(ParticleTypes.EXPLOSION, this.x, this.y, this.z, 1.0, 0.0, 0.0);
            }
        }
        if ($$1) {
            ObjectArrayList<Pair<ItemStack, BlockPos>> $$2 = new ObjectArrayList();
            boolean $$3 = this.getIndirectSourceEntity() instanceof Player;
            Util.shuffle(this.toBlow, this.level.random);
            ObjectListIterator var5 = this.toBlow.iterator();
            while (var5.hasNext()) {
                BlockPos $$4 = (BlockPos) var5.next();
                BlockState $$5 = this.level.getBlockState($$4);
                Block $$6 = $$5.m_60734_();
                if (!$$5.m_60795_()) {
                    BlockPos $$7 = $$4.immutable();
                    this.level.getProfiler().push("explosion_blocks");
                    if ($$6.dropFromExplosion(this)) {
                        Level $$9 = this.level;
                        if ($$9 instanceof ServerLevel) {
                            ServerLevel $$8 = (ServerLevel) $$9;
                            BlockEntity $$9x = $$5.m_155947_() ? this.level.getBlockEntity($$4) : null;
                            LootParams.Builder $$10 = new LootParams.Builder($$8).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf($$4)).withParameter(LootContextParams.TOOL, ItemStack.EMPTY).withOptionalParameter(LootContextParams.BLOCK_ENTITY, $$9x).withOptionalParameter(LootContextParams.THIS_ENTITY, this.source);
                            if (this.blockInteraction == Explosion.BlockInteraction.DESTROY_WITH_DECAY) {
                                $$10.withParameter(LootContextParams.EXPLOSION_RADIUS, this.radius);
                            }
                            $$5.m_222967_($$8, $$4, ItemStack.EMPTY, $$3);
                            $$5.m_287290_($$10).forEach(p_46074_ -> addBlockDrops($$2, p_46074_, $$7));
                        }
                    }
                    this.level.setBlock($$4, Blocks.AIR.defaultBlockState(), 3);
                    $$6.wasExploded(this.level, $$4, this);
                    this.level.getProfiler().pop();
                }
            }
            var5 = $$2.iterator();
            while (var5.hasNext()) {
                Pair<ItemStack, BlockPos> $$11 = (Pair<ItemStack, BlockPos>) var5.next();
                Block.popResource(this.level, (BlockPos) $$11.getSecond(), (ItemStack) $$11.getFirst());
            }
        }
        if (this.fire) {
            ObjectListIterator var13 = this.toBlow.iterator();
            while (var13.hasNext()) {
                BlockPos $$12 = (BlockPos) var13.next();
                if (this.random.nextInt(3) == 0 && this.level.getBlockState($$12).m_60795_() && this.level.getBlockState($$12.below()).m_60804_(this.level, $$12.below())) {
                    this.level.setBlockAndUpdate($$12, BaseFireBlock.getState(this.level, $$12));
                }
            }
        }
    }

    public boolean interactsWithBlocks() {
        return this.blockInteraction != Explosion.BlockInteraction.KEEP;
    }

    private static void addBlockDrops(ObjectArrayList<Pair<ItemStack, BlockPos>> objectArrayListPairItemStackBlockPos0, ItemStack itemStack1, BlockPos blockPos2) {
        int $$3 = objectArrayListPairItemStackBlockPos0.size();
        for (int $$4 = 0; $$4 < $$3; $$4++) {
            Pair<ItemStack, BlockPos> $$5 = (Pair<ItemStack, BlockPos>) objectArrayListPairItemStackBlockPos0.get($$4);
            ItemStack $$6 = (ItemStack) $$5.getFirst();
            if (ItemEntity.areMergable($$6, itemStack1)) {
                ItemStack $$7 = ItemEntity.merge($$6, itemStack1, 16);
                objectArrayListPairItemStackBlockPos0.set($$4, Pair.of($$7, (BlockPos) $$5.getSecond()));
                if (itemStack1.isEmpty()) {
                    return;
                }
            }
        }
        objectArrayListPairItemStackBlockPos0.add(Pair.of(itemStack1, blockPos2));
    }

    public DamageSource getDamageSource() {
        return this.damageSource;
    }

    public Map<Player, Vec3> getHitPlayers() {
        return this.hitPlayers;
    }

    @Nullable
    public LivingEntity getIndirectSourceEntity() {
        if (this.source == null) {
            return null;
        } else if (this.source instanceof PrimedTnt $$0) {
            return $$0.getOwner();
        } else {
            Entity $$3 = this.source;
            if ($$3 instanceof LivingEntity) {
                return (LivingEntity) $$3;
            } else {
                if (this.source instanceof Projectile $$2) {
                    $$3 = $$2.getOwner();
                    if ($$3 instanceof LivingEntity) {
                        return (LivingEntity) $$3;
                    }
                }
                return null;
            }
        }
    }

    @Nullable
    public Entity getDirectSourceEntity() {
        return this.source;
    }

    public void clearToBlow() {
        this.toBlow.clear();
    }

    public List<BlockPos> getToBlow() {
        return this.toBlow;
    }

    public static enum BlockInteraction {

        KEEP, DESTROY, DESTROY_WITH_DECAY
    }
}
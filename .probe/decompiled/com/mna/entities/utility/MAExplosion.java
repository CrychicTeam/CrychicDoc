package com.mna.entities.utility;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.ClientboundExplodePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.ProtectionEnchantment;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.EntityBasedExplosionDamageCalculator;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;

public class MAExplosion extends Explosion {

    private static final ExplosionDamageCalculator EXPLOSION_DAMAGE_CALCULATOR = new ExplosionDamageCalculator();

    private final boolean fire;

    private Explosion.BlockInteraction blockInteraction;

    private final Random random = new Random();

    private final Level level;

    private final double x;

    private final double y;

    private final double z;

    private final float damage;

    @Nullable
    private final Entity source;

    private final float radius;

    private final DamageSource damageSource;

    private final ExplosionDamageCalculator damageCalculator;

    private final ObjectArrayList<BlockPos> toBlow = new ObjectArrayList();

    private final Map<Player, Vec3> hitPlayers = Maps.newHashMap();

    private final Vec3 position;

    public static MAExplosion make(@Nullable LivingEntity caster, ServerLevel world, double x, double y, double z, float radius, float damage, boolean doFire, Explosion.BlockInteraction mode, DamageSource source) {
        MAExplosion explosion = new MAExplosion(world, caster, source, new ExplosionDamageCalculator(), x, y, z, radius, doFire, mode, damage);
        if (ForgeEventFactory.onExplosionStart(world, explosion)) {
            explosion.blockInteraction = Explosion.BlockInteraction.KEEP;
        }
        explosion.explode();
        explosion.finalizeExplosion(false);
        if (mode == Explosion.BlockInteraction.KEEP) {
            explosion.clearToBlow();
        }
        for (ServerPlayer serverplayerentity : world.players()) {
            if (serverplayerentity.m_20275_(x, y, x) < 4096.0) {
                serverplayerentity.connection.send(new ClientboundExplodePacket(x, y, z, radius, explosion.getToBlow(), (Vec3) explosion.getHitPlayers().get(serverplayerentity)));
            }
        }
        world.m_6263_(null, x, y, z, SoundEvents.GENERIC_EXPLODE, SoundSource.NEUTRAL, (float) (0.9 + Math.random() * 0.2), (float) (0.9 + Math.random() * 0.2));
        return explosion;
    }

    public MAExplosion(Level level, @Nullable Entity source, DamageSource damagesource, @Nullable ExplosionDamageCalculator damageCalculator, double x, double y, double z, float radius, boolean doFire, Explosion.BlockInteraction interactionMode, float damage) {
        super(level, source, damagesource, damageCalculator, x, y, z, radius, doFire, interactionMode);
        this.damage = damage;
        this.level = level;
        this.source = source;
        this.radius = radius;
        this.x = x;
        this.y = y;
        this.z = z;
        this.fire = doFire;
        this.blockInteraction = interactionMode;
        this.damageSource = damagesource;
        this.damageCalculator = damageCalculator == null ? this.makeDamageCalculator(source) : damageCalculator;
        this.position = new Vec3(this.x, this.y, this.z);
    }

    private ExplosionDamageCalculator makeDamageCalculator(@Nullable Entity entity) {
        return (ExplosionDamageCalculator) (entity == null ? EXPLOSION_DAMAGE_CALCULATOR : new EntityBasedExplosionDamageCalculator(entity));
    }

    public static float getSeenPercent(Vec3 vector, Entity entity) {
        AABB axisalignedbb = entity.getBoundingBox();
        double d0 = 1.0 / ((axisalignedbb.maxX - axisalignedbb.minX) * 2.0 + 1.0);
        double d1 = 1.0 / ((axisalignedbb.maxY - axisalignedbb.minY) * 2.0 + 1.0);
        double d2 = 1.0 / ((axisalignedbb.maxZ - axisalignedbb.minZ) * 2.0 + 1.0);
        double d3 = (1.0 - Math.floor(1.0 / d0) * d0) / 2.0;
        double d4 = (1.0 - Math.floor(1.0 / d2) * d2) / 2.0;
        if (!(d0 < 0.0) && !(d1 < 0.0) && !(d2 < 0.0)) {
            int i = 0;
            int j = 0;
            for (float f = 0.0F; f <= 1.0F; f = (float) ((double) f + d0)) {
                for (float f1 = 0.0F; f1 <= 1.0F; f1 = (float) ((double) f1 + d1)) {
                    for (float f2 = 0.0F; f2 <= 1.0F; f2 = (float) ((double) f2 + d2)) {
                        double d5 = Mth.lerp((double) f, axisalignedbb.minX, axisalignedbb.maxX);
                        double d6 = Mth.lerp((double) f1, axisalignedbb.minY, axisalignedbb.maxY);
                        double d7 = Mth.lerp((double) f2, axisalignedbb.minZ, axisalignedbb.maxZ);
                        Vec3 vector3d = new Vec3(d5 + d3, d6, d7 + d4);
                        if (entity.level().m_45547_(new ClipContext(vector3d, vector, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entity)).getType() == HitResult.Type.MISS) {
                            i++;
                        }
                        j++;
                    }
                }
            }
            return (float) i / (float) j;
        } else {
            return 0.0F;
        }
    }

    @Override
    public void explode() {
        Set<BlockPos> set = Sets.newHashSet();
        for (int j = 0; j < 16; j++) {
            for (int k = 0; k < 16; k++) {
                for (int l = 0; l < 16; l++) {
                    if (j == 0 || j == 15 || k == 0 || k == 15 || l == 0 || l == 15) {
                        double d0 = (double) ((float) j / 15.0F * 2.0F - 1.0F);
                        double d1 = (double) ((float) k / 15.0F * 2.0F - 1.0F);
                        double d2 = (double) ((float) l / 15.0F * 2.0F - 1.0F);
                        double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
                        d0 /= d3;
                        d1 /= d3;
                        d2 /= d3;
                        float f = this.radius * (0.7F + this.level.random.nextFloat() * 0.6F);
                        double d4 = this.x;
                        double d6 = this.y;
                        for (double d8 = this.z; f > 0.0F; f -= 0.22500001F) {
                            BlockPos blockpos = BlockPos.containing(d4, d6, d8);
                            BlockState blockstate = this.level.getBlockState(blockpos);
                            FluidState fluidstate = this.level.getFluidState(blockpos);
                            Optional<Float> optional = this.damageCalculator.getBlockExplosionResistance(this, this.level, blockpos, blockstate, fluidstate);
                            if (optional.isPresent()) {
                                f -= (optional.get() + 0.3F) * 0.3F;
                            }
                            if (f > 0.0F && this.damageCalculator.shouldBlockExplode(this, this.level, blockpos, blockstate, f)) {
                                set.add(blockpos);
                            }
                            d4 += d0 * 0.3F;
                            d6 += d1 * 0.3F;
                            d8 += d2 * 0.3F;
                        }
                    }
                }
            }
        }
        this.toBlow.addAll(set);
        float f2 = this.radius * 2.0F;
        int k1 = Mth.floor(this.x - (double) f2 - 1.0);
        int l1 = Mth.floor(this.x + (double) f2 + 1.0);
        int i2 = Mth.floor(this.y - (double) f2 - 1.0);
        int i1 = Mth.floor(this.y + (double) f2 + 1.0);
        int j2 = Mth.floor(this.z - (double) f2 - 1.0);
        int j1 = Mth.floor(this.z + (double) f2 + 1.0);
        List<Entity> list = this.level.m_45933_(this.source, new AABB((double) k1, (double) i2, (double) j2, (double) l1, (double) i1, (double) j1));
        ForgeEventFactory.onExplosionDetonate(this.level, this, list, (double) f2);
        Vec3 vector3d = new Vec3(this.x, this.y, this.z);
        for (int k2 = 0; k2 < list.size(); k2++) {
            Entity entity = (Entity) list.get(k2);
            if (!entity.ignoreExplosion()) {
                double d12 = (double) (Mth.sqrt((float) entity.distanceToSqr(vector3d)) / f2);
                if (d12 <= 1.0) {
                    double d5 = entity.getX() - this.x;
                    double d7 = (entity instanceof PrimedTnt ? entity.getY() : entity.getEyeY()) - this.y;
                    double d9 = entity.getZ() - this.z;
                    double d13 = (double) Mth.sqrt((float) (d5 * d5 + d7 * d7 + d9 * d9));
                    if (d13 != 0.0) {
                        d5 /= d13;
                        d7 /= d13;
                        d9 /= d13;
                        double d14 = (double) getSeenPercent(vector3d, entity);
                        double d10 = (1.0 - d12) * d14;
                        entity.hurt(this.getDamageSource(), this.damage);
                        double d11 = d10;
                        if (entity instanceof LivingEntity) {
                            d11 = ProtectionEnchantment.getExplosionKnockbackAfterDampener((LivingEntity) entity, d10);
                        }
                        entity.setDeltaMovement(entity.getDeltaMovement().add(d5 * d11, d7 * d11, d9 * d11));
                        if (entity instanceof Player) {
                            Player playerentity = (Player) entity;
                            if (!playerentity.isSpectator() && (!playerentity.isCreative() || !playerentity.getAbilities().flying)) {
                                this.hitPlayers.put(playerentity, new Vec3(d5 * d10, d7 * d10, d9 * d10));
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void finalizeExplosion(boolean p_77279_1_) {
        if (this.level.isClientSide()) {
            this.level.playLocalSound(this.x, this.y, this.z, SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 4.0F, (1.0F + (this.level.random.nextFloat() - this.level.random.nextFloat()) * 0.2F) * 0.7F, false);
        }
        boolean flag = this.blockInteraction != Explosion.BlockInteraction.KEEP;
        if (p_77279_1_) {
            if (!(this.radius < 2.0F) && flag) {
                this.level.addParticle(ParticleTypes.EXPLOSION_EMITTER, this.x, this.y, this.z, 1.0, 0.0, 0.0);
            } else {
                this.level.addParticle(ParticleTypes.EXPLOSION, this.x, this.y, this.z, 1.0, 0.0, 0.0);
            }
        }
        if (flag) {
            ObjectArrayList<Pair<ItemStack, BlockPos>> objectarraylist = new ObjectArrayList();
            boolean flag1 = this.m_252906_() instanceof Player;
            Util.shuffle(this.toBlow, this.level.random);
            ObjectListIterator var5 = this.toBlow.iterator();
            while (var5.hasNext()) {
                BlockPos blockpos = (BlockPos) var5.next();
                BlockState blockstate = this.level.getBlockState(blockpos);
                if (!blockstate.m_60795_()) {
                    BlockPos blockpos1 = blockpos.immutable();
                    this.level.getProfiler().push("explosion_blocks");
                    if (blockstate.canDropFromExplosion(this.level, blockpos, this)) {
                        Level $$9 = this.level;
                        if ($$9 instanceof ServerLevel) {
                            ServerLevel serverlevel = (ServerLevel) $$9;
                            BlockEntity blockentity = blockstate.m_155947_() ? this.level.getBlockEntity(blockpos) : null;
                            LootParams.Builder lootparams$builder = new LootParams.Builder(serverlevel).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(blockpos)).withParameter(LootContextParams.TOOL, ItemStack.EMPTY).withOptionalParameter(LootContextParams.BLOCK_ENTITY, blockentity).withOptionalParameter(LootContextParams.THIS_ENTITY, this.source);
                            if (this.blockInteraction == Explosion.BlockInteraction.DESTROY_WITH_DECAY) {
                                lootparams$builder.withParameter(LootContextParams.EXPLOSION_RADIUS, this.radius);
                            }
                            blockstate.m_222967_(serverlevel, blockpos, ItemStack.EMPTY, flag1);
                            blockstate.m_287290_(lootparams$builder).forEach(p_46074_ -> addBlockDrops(objectarraylist, p_46074_, blockpos1));
                        }
                    }
                    blockstate.onBlockExploded(this.level, blockpos, this);
                    this.level.getProfiler().pop();
                }
            }
            var5 = objectarraylist.iterator();
            while (var5.hasNext()) {
                Pair<ItemStack, BlockPos> pair = (Pair<ItemStack, BlockPos>) var5.next();
                Block.popResource(this.level, (BlockPos) pair.getSecond(), (ItemStack) pair.getFirst());
            }
        }
        if (this.fire) {
            ObjectListIterator var13 = this.toBlow.iterator();
            while (var13.hasNext()) {
                BlockPos blockpos2 = (BlockPos) var13.next();
                if (this.random.nextInt(3) == 0 && this.level.getBlockState(blockpos2).m_60795_() && this.level.getBlockState(blockpos2.below()).m_60804_(this.level, blockpos2.below())) {
                    this.level.setBlockAndUpdate(blockpos2, BaseFireBlock.getState(this.level, blockpos2));
                }
            }
        }
    }

    private static void addBlockDrops(ObjectArrayList<Pair<ItemStack, BlockPos>> p_229976_0_, ItemStack p_229976_1_, BlockPos p_229976_2_) {
        int i = p_229976_0_.size();
        for (int j = 0; j < i; j++) {
            Pair<ItemStack, BlockPos> pair = (Pair<ItemStack, BlockPos>) p_229976_0_.get(j);
            ItemStack itemstack = (ItemStack) pair.getFirst();
            if (ItemEntity.areMergable(itemstack, p_229976_1_)) {
                ItemStack itemstack1 = ItemEntity.merge(itemstack, p_229976_1_, 16);
                p_229976_0_.set(j, Pair.of(itemstack1, (BlockPos) pair.getSecond()));
                if (p_229976_1_.isEmpty()) {
                    return;
                }
            }
        }
        p_229976_0_.add(Pair.of(p_229976_1_, p_229976_2_));
    }

    @Override
    public DamageSource getDamageSource() {
        return this.damageSource;
    }

    @Override
    public Map<Player, Vec3> getHitPlayers() {
        return this.hitPlayers;
    }

    @Override
    public Entity getDirectSourceEntity() {
        if (this.source == null) {
            return null;
        } else if (this.source instanceof PrimedTnt) {
            return ((PrimedTnt) this.source).getOwner();
        } else if (this.source instanceof LivingEntity) {
            return this.source;
        } else {
            if (this.source instanceof Projectile) {
                Entity entity = ((Projectile) this.source).getOwner();
                if (entity instanceof LivingEntity) {
                    return entity;
                }
            }
            return null;
        }
    }

    @Override
    public void clearToBlow() {
        this.toBlow.clear();
    }

    @Override
    public List<BlockPos> getToBlow() {
        return this.toBlow;
    }

    public Vec3 getPosition() {
        return this.position;
    }

    @Nullable
    public Entity getExploder() {
        return this.source;
    }

    public static enum Mode {

        NONE, BREAK, DESTROY
    }
}
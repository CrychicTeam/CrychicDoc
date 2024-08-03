package com.github.alexmodguy.alexscaves.server.entity.util;

import com.github.alexmodguy.alexscaves.client.particle.ACParticleRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
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
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.EntityBasedExplosionDamageCalculator;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class MineExplosion {

    private static final ExplosionDamageCalculator EXPLOSION_DAMAGE_CALCULATOR = new ExplosionDamageCalculator();

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

    private final Vec3 position;

    private boolean underwaterSound;

    public MineExplosion(Level level, @Nullable Entity entity, double x, double y, double z, float radius, boolean underwaterSound, Explosion.BlockInteraction blockInteraction) {
        this.level = level;
        this.source = entity;
        this.radius = radius;
        this.x = x;
        this.y = y;
        this.z = z;
        this.blockInteraction = blockInteraction;
        this.damageSource = level.damageSources().explosion(this.getDirectSourceEntity(), this.getIndirectSourceEntity());
        this.damageCalculator = this.makeDamageCalculator(entity);
        this.position = new Vec3(this.x, this.y, this.z);
    }

    private ExplosionDamageCalculator makeDamageCalculator(@Nullable Entity entity0) {
        return (ExplosionDamageCalculator) (entity0 == null ? EXPLOSION_DAMAGE_CALCULATOR : new EntityBasedExplosionDamageCalculator(entity0));
    }

    public static float getSeenPercent(Vec3 vec0, Entity entity1) {
        AABB aabb = entity1.getBoundingBox();
        double d0 = 1.0 / ((aabb.maxX - aabb.minX) * 2.0 + 1.0);
        double d1 = 1.0 / ((aabb.maxY - aabb.minY) * 2.0 + 1.0);
        double d2 = 1.0 / ((aabb.maxZ - aabb.minZ) * 2.0 + 1.0);
        double d3 = (1.0 - Math.floor(1.0 / d0) * d0) / 2.0;
        double d4 = (1.0 - Math.floor(1.0 / d2) * d2) / 2.0;
        if (!(d0 < 0.0) && !(d1 < 0.0) && !(d2 < 0.0)) {
            int i = 0;
            int j = 0;
            for (double d5 = 0.0; d5 <= 1.0; d5 += d0) {
                for (double d6 = 0.0; d6 <= 1.0; d6 += d1) {
                    for (double d7 = 0.0; d7 <= 1.0; d7 += d2) {
                        double d8 = Mth.lerp(d5, aabb.minX, aabb.maxX);
                        double d9 = Mth.lerp(d6, aabb.minY, aabb.maxY);
                        double d10 = Mth.lerp(d7, aabb.minZ, aabb.maxZ);
                        Vec3 vec3 = new Vec3(d8 + d3, d9, d10 + d4);
                        if (entity1.level().m_45547_(new ClipContext(vec3, vec0, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entity1)).getType() == HitResult.Type.MISS) {
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

    public void explode() {
        this.level.m_220400_(this.source, GameEvent.EXPLODE, new Vec3(this.x, this.y, this.z));
        Set<BlockPos> set = Sets.newHashSet();
        int i = 16;
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
                        double d8 = this.z;
                        float f1 = 0.3F;
                        while (f > 0.0F) {
                            BlockPos blockpos = BlockPos.containing(d4, d6, d8);
                            BlockState blockstate = this.level.getBlockState(blockpos);
                            if (!this.level.isInWorldBounds(blockpos) || blockstate.m_204336_(ACTagRegistry.UNMOVEABLE)) {
                                break;
                            }
                            f -= blockstate.m_60819_().isEmpty() ? (blockstate.m_60734_().getExplosionResistance() + 0.3F) * 0.3F : 0.0F;
                            set.add(blockpos);
                            d4 += d0 * 0.3F;
                            d6 += d1 * 0.3F;
                            d8 += d2 * 0.3F;
                            f -= 0.22500001F;
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
        Vec3 vec3 = new Vec3(this.x, this.y, this.z);
        for (int k2 = 0; k2 < list.size(); k2++) {
            Entity entity = (Entity) list.get(k2);
            if (!entity.ignoreExplosion()) {
                double d12 = Math.sqrt(entity.distanceToSqr(vec3)) / (double) f2;
                if (d12 <= 1.0) {
                    double d5 = entity.getX() - this.x;
                    double d7 = (entity instanceof PrimedTnt ? entity.getY() : entity.getEyeY()) - this.y;
                    double d9 = entity.getZ() - this.z;
                    double d13 = Math.sqrt(d5 * d5 + d7 * d7 + d9 * d9);
                    if (d13 != 0.0) {
                        d5 /= d13;
                        d7 /= d13;
                        d9 /= d13;
                        double d14 = (double) getSeenPercent(vec3, entity);
                        double d10 = (1.0 - d12) * d14;
                        entity.hurt(this.getDamageSource(), (float) ((int) ((d10 * d10 + d10) / 2.0 * 7.0 * (double) f2 + 1.0)));
                        double d11;
                        if (entity instanceof LivingEntity livingentity) {
                            d11 = ProtectionEnchantment.getExplosionKnockbackAfterDampener(livingentity, d10);
                        } else {
                            d11 = d10;
                        }
                        d5 *= d11;
                        d7 *= d11;
                        d9 *= d11;
                        Vec3 vec31 = new Vec3(d5, d7, d9);
                        entity.setDeltaMovement(entity.getDeltaMovement().add(vec31));
                        if (entity instanceof Player) {
                            Player player = (Player) entity;
                            if (!player.isSpectator() && (!player.isCreative() || !player.getAbilities().flying)) {
                                this.hitPlayers.put(player, vec31);
                            }
                        }
                    }
                }
            }
        }
    }

    public void finalizeExplosion(boolean particles) {
        this.level.playSound(null, this.x, this.y, this.z, this.underwaterSound ? ACSoundRegistry.MINE_GUARDIAN_EXPLODE.get() : ACSoundRegistry.MINE_GUARDIAN_LAND_EXPLODE.get(), SoundSource.BLOCKS, 4.0F, (1.0F + (this.level.random.nextFloat() - this.level.random.nextFloat()) * 0.2F) * 0.7F);
        boolean flag = this.interactsWithBlocks();
        if (particles && !this.level.isClientSide && this.level instanceof ServerLevel serverLevel) {
            for (int i = 0; (float) i < 5.0F + this.radius * 10.0F; i++) {
                float particleX = this.random.nextFloat() * this.radius * 2.0F - this.radius;
                float particleY = this.random.nextFloat() * this.radius * 2.0F - this.radius;
                float particleZ = this.random.nextFloat() * this.radius * 2.0F - this.radius;
                serverLevel.sendParticles(ACParticleRegistry.MINE_EXPLOSION.get(), this.x + (double) particleX, this.y + (double) particleY, this.z + (double) particleZ, 0, 0.0, 0.0, 0.0, 1.0);
            }
        }
        if (flag) {
            ObjectArrayList<Pair<ItemStack, BlockPos>> objectarraylist = new ObjectArrayList();
            boolean flag1 = this.getIndirectSourceEntity() instanceof Player;
            Util.shuffle(this.toBlow, this.level.random);
            ObjectListIterator var16 = this.toBlow.iterator();
            while (var16.hasNext()) {
                BlockPos blockpos = (BlockPos) var16.next();
                BlockState blockstate = this.level.getBlockState(blockpos);
                if (!blockstate.m_60795_()) {
                    BlockPos blockpos1 = blockpos.immutable();
                    this.level.getProfiler().push("explosion_blocks");
                    Level level1 = this.level;
                    if (level1 instanceof ServerLevel) {
                        ServerLevel serverlevel = (ServerLevel) level1;
                        BlockEntity blockentity = blockstate.m_155947_() ? this.level.getBlockEntity(blockpos) : null;
                        LootParams.Builder lootcontext$builder = new LootParams.Builder(serverlevel).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(blockpos)).withParameter(LootContextParams.TOOL, ItemStack.EMPTY).withOptionalParameter(LootContextParams.BLOCK_ENTITY, blockentity).withOptionalParameter(LootContextParams.THIS_ENTITY, this.source);
                        if (this.blockInteraction == Explosion.BlockInteraction.DESTROY_WITH_DECAY) {
                            lootcontext$builder.withParameter(LootContextParams.EXPLOSION_RADIUS, this.radius);
                        }
                        blockstate.m_222967_(serverlevel, blockpos, ItemStack.EMPTY, flag1);
                        blockstate.m_287290_(lootcontext$builder).forEach(p_46074_ -> addBlockDrops(objectarraylist, p_46074_, blockpos1));
                    }
                    if (blockstate.m_60819_().isEmpty()) {
                        this.level.setBlock(blockpos, Blocks.AIR.defaultBlockState(), 3);
                    }
                    this.level.getProfiler().pop();
                }
            }
            var16 = objectarraylist.iterator();
            while (var16.hasNext()) {
                Pair<ItemStack, BlockPos> pair = (Pair<ItemStack, BlockPos>) var16.next();
                Block.popResource(this.level, (BlockPos) pair.getSecond(), (ItemStack) pair.getFirst());
            }
        }
    }

    public boolean interactsWithBlocks() {
        return this.blockInteraction != Explosion.BlockInteraction.KEEP;
    }

    private static void addBlockDrops(ObjectArrayList<Pair<ItemStack, BlockPos>> objectArrayListPairItemStackBlockPos0, ItemStack itemStack1, BlockPos blockPos2) {
        int i = objectArrayListPairItemStackBlockPos0.size();
        for (int j = 0; j < i; j++) {
            Pair<ItemStack, BlockPos> pair = (Pair<ItemStack, BlockPos>) objectArrayListPairItemStackBlockPos0.get(j);
            ItemStack itemstack = (ItemStack) pair.getFirst();
            if (ItemEntity.areMergable(itemstack, itemStack1)) {
                ItemStack itemstack1 = ItemEntity.merge(itemstack, itemStack1, 16);
                objectArrayListPairItemStackBlockPos0.set(j, Pair.of(itemstack1, (BlockPos) pair.getSecond()));
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
        } else if (this.source instanceof PrimedTnt primedtnt) {
            return primedtnt.getOwner();
        } else {
            Entity var3 = this.source;
            if (var3 instanceof LivingEntity) {
                return (LivingEntity) var3;
            } else {
                if (this.source instanceof Projectile projectile) {
                    var3 = projectile.getOwner();
                    if (var3 instanceof LivingEntity) {
                        return (LivingEntity) var3;
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

    public Vec3 getPosition() {
        return this.position;
    }

    @Nullable
    public Entity getExploder() {
        return this.source;
    }
}
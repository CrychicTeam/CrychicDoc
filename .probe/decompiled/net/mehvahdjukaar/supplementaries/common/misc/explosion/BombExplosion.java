package net.mehvahdjukaar.supplementaries.common.misc.explosion;

import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.Map.Entry;
import net.mehvahdjukaar.moonlight.api.platform.ForgeHelper;
import net.mehvahdjukaar.supplementaries.common.entities.BombEntity;
import net.mehvahdjukaar.supplementaries.common.network.ClientBoundSendKnockbackPacket;
import net.mehvahdjukaar.supplementaries.common.network.ModNetwork;
import net.mehvahdjukaar.supplementaries.integration.CompatHandler;
import net.mehvahdjukaar.supplementaries.integration.FlanCompat;
import net.mehvahdjukaar.supplementaries.reg.ModDamageSources;
import net.mehvahdjukaar.supplementaries.reg.ModSounds;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.ProtectionEnchantment;
import net.minecraft.world.level.EntityBasedExplosionDamageCalculator;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class BombExplosion extends Explosion {

    private final BombEntity.BombType bombType;

    private final ExplosionDamageCalculator damageCalculator;

    private static final ExplosionDamageCalculator EXPLOSION_DAMAGE_CALCULATOR = new ExplosionDamageCalculator();

    public BombExplosion(Level world, @Nullable Entity entity, @Nullable ExplosionDamageCalculator context, double x, double y, double z, float radius, BombEntity.BombType bombType, Explosion.BlockInteraction interaction) {
        super(world, entity, null, context, x, y, z, radius, false, interaction);
        this.bombType = bombType;
        this.damageCalculator = context == null ? this.bombMakeDamageCalculator(entity) : context;
        this.f_46018_ = ModDamageSources.bombExplosion(this.m_253049_(), this.m_252906_());
    }

    private ExplosionDamageCalculator bombMakeDamageCalculator(@Nullable Entity entity) {
        return (ExplosionDamageCalculator) (entity == null ? EXPLOSION_DAMAGE_CALCULATOR : new EntityBasedExplosionDamageCalculator(entity));
    }

    public ObjectArrayList<BlockPos> getToBlow() {
        return (ObjectArrayList<BlockPos>) super.getToBlow();
    }

    public void doFinalizeExplosion() {
        this.f_46012_.playSound(null, this.f_46013_, this.f_46014_, this.f_46015_, (SoundEvent) ModSounds.BOMB_EXPLOSION.get(), SoundSource.NEUTRAL, this.bombType.volume(), 1.2F + (this.f_46012_.random.nextFloat() - this.f_46012_.random.nextFloat()) * 0.2F);
        ObjectArrayList<Pair<ItemStack, BlockPos>> drops = new ObjectArrayList();
        Util.shuffle(this.getToBlow(), this.f_46012_.random);
        ObjectListIterator var2 = this.getToBlow().iterator();
        while (var2.hasNext()) {
            BlockPos blockpos = (BlockPos) var2.next();
            BlockState blockstate = this.f_46012_.getBlockState(blockpos);
            if (!blockstate.m_60795_()) {
                BlockPos immutable = blockpos.immutable();
                this.f_46012_.getProfiler().push("explosion_blocks");
                if (ForgeHelper.canDropFromExplosion(blockstate, this.f_46012_, blockpos, this)) {
                    Level blockEntity = this.f_46012_;
                    if (blockEntity instanceof ServerLevel) {
                        ServerLevel serverLevel = (ServerLevel) blockEntity;
                        BlockEntity blockEntityx = blockstate.m_155947_() ? this.f_46012_.getBlockEntity(blockpos) : null;
                        LootParams.Builder builder = new LootParams.Builder(serverLevel).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(blockpos)).withParameter(LootContextParams.TOOL, ItemStack.EMPTY).withOptionalParameter(LootContextParams.BLOCK_ENTITY, blockEntityx).withOptionalParameter(LootContextParams.THIS_ENTITY, this.f_46016_);
                        if (this.f_46010_ == Explosion.BlockInteraction.DESTROY) {
                            builder.withParameter(LootContextParams.EXPLOSION_RADIUS, this.f_46017_);
                        }
                        blockstate.m_287290_(builder).forEach(stack -> m_46067_(drops, stack, immutable));
                    }
                }
                ForgeHelper.onBlockExploded(blockstate, this.f_46012_, blockpos, this);
                this.f_46012_.getProfiler().pop();
            }
        }
        var2 = drops.iterator();
        while (var2.hasNext()) {
            Pair<ItemStack, BlockPos> pair = (Pair<ItemStack, BlockPos>) var2.next();
            Block.popResource(this.f_46012_, (BlockPos) pair.getSecond(), (ItemStack) pair.getFirst());
        }
    }

    @Override
    public void explode() {
        Set<BlockPos> set;
        Player var10000;
        label154: {
            this.f_46012_.m_142346_(this.f_46016_, GameEvent.EXPLODE, BlockPos.containing(this.f_46013_, this.f_46014_, this.f_46015_));
            set = Sets.newHashSet();
            if (this.f_46016_ instanceof Projectile pr && pr.getOwner() instanceof Player pl) {
                var10000 = pl;
                break label154;
            }
            var10000 = null;
        }
        Player owner = var10000;
        if (this.f_46010_ != Explosion.BlockInteraction.KEEP) {
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
                            float f = this.f_46017_ * (0.7F + this.f_46012_.random.nextFloat() * 0.6F);
                            double d4 = this.f_46013_;
                            double d6 = this.f_46014_;
                            double d8 = this.f_46015_;
                            for (float f1 = 0.3F; f > 0.0F; f -= 0.22500001F) {
                                BlockPos blockpos = BlockPos.containing(d4, d6, d8);
                                BlockState blockstate = this.f_46012_.getBlockState(blockpos);
                                FluidState fluidstate = this.f_46012_.getFluidState(blockpos);
                                Optional<Float> optional = this.damageCalculator.getBlockExplosionResistance(this, this.f_46012_, blockpos, blockstate, fluidstate);
                                if (optional.isPresent()) {
                                    f -= (optional.get() + 0.3F) * 0.3F;
                                }
                                if (f > 0.0F && this.damageCalculator.shouldBlockExplode(this, this.f_46012_, blockpos, blockstate, f) && (owner == null || !CompatHandler.FLAN || FlanCompat.canBreak(owner, blockpos))) {
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
        }
        this.getToBlow().addAll(set);
        float diameter = this.f_46017_ * 2.0F;
        int k1 = Mth.floor(this.f_46013_ - (double) diameter - 1.0);
        int l1 = Mth.floor(this.f_46013_ + (double) diameter + 1.0);
        int i2 = Mth.floor(this.f_46014_ - (double) diameter - 1.0);
        int i1 = Mth.floor(this.f_46014_ + (double) diameter + 1.0);
        int j2 = Mth.floor(this.f_46015_ - (double) diameter - 1.0);
        int j1 = Mth.floor(this.f_46015_ + (double) diameter + 1.0);
        List<Entity> list = this.f_46012_.m_45933_(this.m_253049_(), new AABB((double) k1, (double) i2, (double) j2, (double) l1, (double) i1, (double) j1));
        ForgeHelper.onExplosionDetonate(this.f_46012_, this, list, (double) diameter);
        Vec3 vector3d = new Vec3(this.f_46013_, this.f_46014_, this.f_46015_);
        for (Entity entity : list) {
            if (!entity.ignoreExplosion() && (owner == null || !CompatHandler.FLAN || FlanCompat.canAttack(owner, entity))) {
                double distSq = entity.distanceToSqr(vector3d);
                double normalizedDist = (double) (Mth.sqrt((float) distSq) / diameter);
                if (normalizedDist <= 1.0) {
                    double dx = entity.getX() - this.f_46013_;
                    double dy = (entity instanceof PrimedTnt ? entity.getY() : entity.getEyeY()) - this.f_46014_;
                    double dz = entity.getZ() - this.f_46015_;
                    double distFromCenterSqr = (double) Mth.sqrt((float) (dx * dx + dy * dy + dz * dz));
                    if (distFromCenterSqr != 0.0) {
                        dx /= distFromCenterSqr;
                        dy /= distFromCenterSqr;
                        dz /= distFromCenterSqr;
                        double d14 = (double) m_46064_(vector3d, entity);
                        double d10 = (1.0 - normalizedDist) * d14;
                        entity.hurt(this.m_46077_(), (float) ((int) ((d10 * d10 + d10) / 2.0 * 7.0 * (double) diameter + 1.0)));
                        double d11 = d10;
                        boolean isPlayer = entity instanceof Player;
                        Player playerEntity = null;
                        if (isPlayer) {
                            playerEntity = (Player) entity;
                            if (!playerEntity.isSpectator() && (!playerEntity.isCreative() || !playerEntity.getAbilities().flying)) {
                                this.m_46078_().put(playerEntity, new Vec3(dx * d10, dy * d10, dz * d10));
                            }
                        }
                        if (entity instanceof LivingEntity) {
                            LivingEntity livingEntity = (LivingEntity) entity;
                            if (!isPlayer || !playerEntity.isSpectator() && !playerEntity.isCreative()) {
                                this.bombType.applyStatusEffects(livingEntity, distSq);
                            }
                            if (entity instanceof Creeper creeper) {
                                creeper.ignite();
                            }
                            d11 = ProtectionEnchantment.getExplosionKnockbackAfterDampener((LivingEntity) entity, d10);
                        }
                        entity.setDeltaMovement(entity.getDeltaMovement().add(dx * d11, dy * d11, dz * d11));
                    }
                }
            }
        }
        if (!this.f_46012_.isClientSide) {
            for (Entry<Player, Vec3> e : this.m_46078_().entrySet()) {
                ModNetwork.CHANNEL.sendToClientPlayer((ServerPlayer) e.getKey(), new ClientBoundSendKnockbackPacket((Vec3) e.getValue(), ((Player) e.getKey()).m_19879_()));
            }
        }
        this.bombType.afterExploded(this, this.f_46012_);
    }
}
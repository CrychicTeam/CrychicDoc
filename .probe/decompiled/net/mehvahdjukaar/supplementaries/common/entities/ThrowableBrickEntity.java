package net.mehvahdjukaar.supplementaries.common.entities;

import com.mojang.authlib.GameProfile;
import java.util.UUID;
import net.mehvahdjukaar.moonlight.api.entity.ImprovedProjectileEntity;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.mehvahdjukaar.moonlight.api.util.FakePlayerManager;
import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.mehvahdjukaar.supplementaries.integration.CompatHandler;
import net.mehvahdjukaar.supplementaries.integration.FlanCompat;
import net.mehvahdjukaar.supplementaries.reg.ModEntities;
import net.mehvahdjukaar.supplementaries.reg.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class ThrowableBrickEntity extends ImprovedProjectileEntity {

    private static final GameProfile BRICK_PLAYER = new GameProfile(UUID.randomUUID(), "Throwable Brick Fake Player");

    public ThrowableBrickEntity(EntityType<? extends ThrowableBrickEntity> type, Level world) {
        super(type, world);
    }

    public ThrowableBrickEntity(Level worldIn, LivingEntity throwerIn) {
        super((EntityType<? extends ThrowableItemProjectile>) ModEntities.THROWABLE_BRICK.get(), throwerIn, worldIn);
    }

    public ThrowableBrickEntity(Level worldIn, double x, double y, double z) {
        super((EntityType<? extends ThrowableItemProjectile>) ModEntities.THROWABLE_BRICK.get(), x, y, z, worldIn);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return PlatHelper.getEntitySpawnPacket(this);
    }

    @Override
    protected Item getDefaultItem() {
        return Items.BRICK;
    }

    private ParticleOptions makeParticle() {
        ItemStack itemstack = this.m_37454_();
        return itemstack.isEmpty() ? new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(this.getDefaultItem())) : new ItemParticleOption(ParticleTypes.ITEM, itemstack);
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 3) {
            ParticleOptions particle = this.makeParticle();
            for (int i = 0; i < 8; i++) {
                this.m_9236_().addParticle(particle, this.m_20185_(), this.m_20186_(), this.m_20189_(), 0.0, 0.0, 0.0);
            }
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult rayTraceResult) {
        super.m_8060_(rayTraceResult);
        Level level = this.m_9236_();
        if (!level.isClientSide) {
            Entity entity = this.m_19749_();
            BlockPos pos = rayTraceResult.getBlockPos();
            if (entity instanceof Player player) {
                if (CompatHandler.FLAN && !FlanCompat.canBreak(player, pos)) {
                    return;
                }
                if (!Utils.mayBuild(player, pos) && !this.m_7846_().hasAdventureModeBreakTagForBlock(level.registryAccess().registryOrThrow(Registries.BLOCK), new BlockInWorld(level, pos, false))) {
                    return;
                }
            }
            if (!(entity instanceof Mob) || level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING) || PlatHelper.isMobGriefingOn(level, this)) {
                Player p = FakePlayerManager.get(BRICK_PLAYER, level);
                p.m_21008_(InteractionHand.MAIN_HAND, Items.IRON_PICKAXE.getDefaultInstance());
                if (level.getBlockState(pos).m_204336_(ModTags.BRICK_BREAKABLE_POTS)) {
                    level.m_46953_(pos, true, p);
                } else {
                    this.breakGlass(pos, 6, p);
                }
            }
        }
    }

    private void breakGlass(BlockPos pos, int chance, Player p) {
        int c = chance - 1 - this.f_19796_.nextInt(4);
        BlockState state = this.m_9236_().getBlockState(pos);
        if (!(state.m_60734_().getExplosionResistance() > 3.0F)) {
            if (c >= 0 && state.m_204336_(ModTags.BRICK_BREAKABLE_GLASS)) {
                this.m_9236_().m_46953_(pos, true, p);
                this.breakGlass(pos.above(), c, p);
                this.breakGlass(pos.below(), c, p);
                this.breakGlass(pos.east(), c, p);
                this.breakGlass(pos.west(), c, p);
                this.breakGlass(pos.north(), c, p);
                this.breakGlass(pos.south(), c, p);
            }
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        super.m_5790_(entityHitResult);
        Entity entity = entityHitResult.getEntity();
        int i = 1;
        entity.hurt(this.m_9236_().damageSources().thrown(this, this.m_19749_()), (float) i);
    }

    @Override
    protected void onHit(HitResult result) {
        super.m_6532_(result);
        if (!this.m_9236_().isClientSide) {
            Vec3 v = result.getLocation();
            this.m_9236_().playSound(null, v.x, v.y, v.z, SoundEvents.NETHER_BRICKS_BREAK, SoundSource.NEUTRAL, 0.75F, 1.0F);
            this.m_9236_().broadcastEntityEvent(this, (byte) 3);
            this.m_142687_(Entity.RemovalReason.DISCARDED);
        }
    }

    @Override
    protected void updateRotation() {
    }
}
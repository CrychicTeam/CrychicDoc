package com.github.alexmodguy.alexscaves.server.entity.item;

import com.github.alexmodguy.alexscaves.server.entity.ACEntityRegistry;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;

public class BurrowingArrowEntity extends AbstractArrow {

    private static final EntityDataAccessor<Integer> DUG_BLOCK_COUNT = SynchedEntityData.defineId(BurrowingArrowEntity.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Boolean> DIGGING = SynchedEntityData.defineId(BurrowingArrowEntity.class, EntityDataSerializers.BOOLEAN);

    private float prevDiggingProgress;

    private float diggingProgress;

    private int miningTime = 0;

    private int lastMineBlockBreakProgress = -1;

    private int soundTime = 0;

    private BlockPos hitPos;

    public BurrowingArrowEntity(EntityType entityType, Level level) {
        super(entityType, level);
        this.m_36781_(3.5);
        this.m_36767_((byte) (this.m_36796_() + 1));
    }

    public BurrowingArrowEntity(Level level, LivingEntity shooter) {
        super(ACEntityRegistry.BURROWING_ARROW.get(), shooter, level);
        this.m_36781_(3.5);
        this.m_36767_((byte) (this.m_36796_() + 1));
    }

    public BurrowingArrowEntity(Level level, double x, double y, double z) {
        super(ACEntityRegistry.BURROWING_ARROW.get(), x, y, z, level);
        this.m_36781_(3.5);
    }

    public BurrowingArrowEntity(PlayMessages.SpawnEntity spawnEntity, Level level) {
        this(ACEntityRegistry.BURROWING_ARROW.get(), level);
        this.m_20011_(this.m_142242_());
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.f_19804_.define(DUG_BLOCK_COUNT, 0);
        this.f_19804_.define(DIGGING, false);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void tick() {
        super.tick();
        this.prevDiggingProgress = this.diggingProgress;
        if (this.isDigging() && this.diggingProgress < 5.0F) {
            this.diggingProgress++;
        }
        if (!this.isDigging() && this.diggingProgress > 0.0F) {
            this.diggingProgress--;
        }
        if (this.f_36703_ && this.hitPos != null && this.canMine(this.hitPos)) {
            this.setDigging(true);
            BlockState state = this.m_9236_().getBlockState(this.hitPos);
            int hardness = (int) (Math.max(state.m_60800_(this.m_9236_(), this.hitPos), 0.2F) * 15.0F);
            int i = (int) ((float) this.miningTime / (float) hardness * 10.0F);
            if (i != this.lastMineBlockBreakProgress) {
                this.m_9236_().destroyBlockProgress(this.m_19879_(), this.hitPos, i);
                this.lastMineBlockBreakProgress = i;
            }
            if (this.miningTime % 8 == 0) {
                this.m_216990_(state.m_60827_().getHitSound());
            }
            Vec3 centerOf = this.hitPos.getCenter().subtract(this.m_20182_());
            if (this.miningTime++ > hardness) {
                this.m_9236_().m_46961_(this.hitPos, true);
                this.setDugBlockCount(this.getDugBlockCount() + 1);
                this.miningTime = 0;
                this.lastMineBlockBreakProgress = -1;
                this.m_20256_(centerOf.normalize().scale(0.3F));
            } else {
                this.m_20256_(centerOf.scale(0.2F));
            }
        } else {
            if (this.hitPos != null) {
                this.m_9236_().destroyBlockProgress(this.m_19879_(), this.hitPos, -1);
                this.hitPos = null;
            }
            this.setDigging(false);
        }
        if (this.isDigging() && this.soundTime-- <= 0) {
            this.soundTime = 38;
            this.m_216990_(ACSoundRegistry.CORRODENT_TEETH.get());
        }
    }

    @Override
    protected void tickDespawn() {
        if (!this.isDigging()) {
            super.tickDespawn();
        }
    }

    @Override
    public void startFalling() {
        this.f_36703_ = false;
    }

    @Override
    public void remove(Entity.RemovalReason removalReason) {
        if (this.hitPos != null) {
            this.m_9236_().destroyBlockProgress(this.m_19879_(), this.hitPos, -1);
            this.hitPos = null;
        }
        super.m_142687_(removalReason);
    }

    private boolean canMine(BlockPos hitPos) {
        Entity owner = this.m_19749_();
        if (owner != null && !(owner instanceof Player)) {
            return false;
        } else {
            BlockState state = this.m_9236_().getBlockState(hitPos);
            return !state.m_204336_(ACTagRegistry.UNMOVEABLE) && state.m_60819_().isEmpty() && state.m_60800_(this.m_9236_(), hitPos) != -1.0F && this.getDugBlockCount() < 5;
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult blockHitResult) {
        super.onHitBlock(blockHitResult);
        this.hitPos = blockHitResult.getBlockPos();
    }

    @Override
    protected ItemStack getPickupItem() {
        return new ItemStack(ACItemRegistry.BURROWING_ARROW.get());
    }

    private int getDugBlockCount() {
        return this.f_19804_.get(DUG_BLOCK_COUNT);
    }

    private void setDugBlockCount(int count) {
        this.f_19804_.set(DUG_BLOCK_COUNT, count);
    }

    private boolean isDigging() {
        return this.f_19804_.get(DIGGING);
    }

    private void setDigging(boolean digging) {
        this.f_19804_.set(DIGGING, digging);
    }

    public float getDiggingAmount(float partialTicks) {
        return (this.prevDiggingProgress + (this.diggingProgress - this.prevDiggingProgress) * partialTicks) * 0.2F;
    }
}
package com.github.alexmodguy.alexscaves.server.entity.living;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.github.alexmodguy.alexscaves.server.entity.util.MagnetronJoint;
import com.github.alexmodguy.alexscaves.server.message.MultipartEntityMessage;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.PartEntity;

public class MagnetronPartEntity extends PartEntity<MagnetronEntity> {

    private final MagnetronJoint joint;

    private BlockPos startPosition;

    private BlockState blockState;

    private EntityDimensions size;

    public boolean left;

    public float scale = 1.0F;

    private final BlockState STONE = Blocks.STONE.defaultBlockState();

    public MagnetronPartEntity(MagnetronEntity parent, MagnetronJoint joint, boolean left) {
        super(parent);
        this.f_19850_ = true;
        this.size = EntityDimensions.fixed(0.9F, 0.9F);
        this.joint = joint;
        this.left = left;
        this.m_6210_();
    }

    @Override
    public EntityDimensions getDimensions(Pose pose) {
        return this.size;
    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    public MagnetronJoint getJoint() {
        return this.joint;
    }

    public boolean isLeft() {
        return this.left;
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        MagnetronEntity parent = this.getParent();
        if (parent == null) {
            return InteractionResult.PASS;
        } else {
            this.m_216990_(SoundEvents.ITEM_BREAK);
            if (player.m_9236_().isClientSide) {
                AlexsCaves.sendMSGToServer(new MultipartEntityMessage(parent.m_19879_(), player.m_19879_(), 0, 0.0));
            }
            return parent.m_6096_(player, hand);
        }
    }

    @Override
    public boolean save(CompoundTag tag) {
        return false;
    }

    @Override
    public boolean canBeCollidedWith() {
        MagnetronEntity parent = this.getParent();
        return parent != null && parent.m_5829_();
    }

    @Override
    public boolean isPickable() {
        MagnetronEntity parent = this.getParent();
        return parent != null && parent.m_6087_();
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        MagnetronEntity parent = this.getParent();
        if (!this.m_6673_(source) && parent != null) {
            Entity player = source.getEntity();
            if (player != null && player.level().isClientSide) {
                AlexsCaves.sendMSGToServer(new MultipartEntityMessage(parent.m_19879_(), player.getId(), 1, (double) amount));
            }
        }
        return false;
    }

    @Override
    public boolean is(Entity entityIn) {
        return this == entityIn || this.getParent() == entityIn;
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
    }

    @Override
    public boolean shouldBeSaved() {
        return false;
    }

    public void positionMultipart(MagnetronEntity entity) {
        Vec3 targetPos = entity.m_20182_().add(this.joint.getTargetPosition(entity, this.left));
        Vec3 start = this.startPosition == null ? entity.m_20182_() : Vec3.atCenterOf(this.startPosition);
        Vec3 addToStart = targetPos.subtract(start);
        this.m_146884_(start.add(addToStart.scale((double) entity.getFormProgress(1.0F))));
    }

    public void setStartsAt(BlockPos pos) {
        this.startPosition = pos;
    }

    public BlockPos getStartPosition() {
        return this.startPosition;
    }

    public BlockState getBlockState() {
        return this.blockState;
    }

    public BlockState getVisualBlockState() {
        MagnetronEntity parent = this.getParent();
        return this.blockState == null && parent != null && parent.m_6084_() ? this.STONE : this.blockState;
    }

    public void setBlockState(BlockState state) {
        this.blockState = state;
    }

    public double getLowPoint() {
        return this.m_20191_().minY;
    }
}
package net.mehvahdjukaar.supplementaries.common.entities;

import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.mehvahdjukaar.supplementaries.common.block.ModBlockProperties;
import net.mehvahdjukaar.supplementaries.common.block.blocks.AbstractRopeKnotBlock;
import net.mehvahdjukaar.supplementaries.common.misc.RopeHelper;
import net.mehvahdjukaar.supplementaries.common.utils.ItemsUtil;
import net.mehvahdjukaar.supplementaries.configs.CommonConfigs;
import net.mehvahdjukaar.supplementaries.integration.CompatHandler;
import net.mehvahdjukaar.supplementaries.integration.FlanCompat;
import net.mehvahdjukaar.supplementaries.reg.ModEntities;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
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
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public class RopeArrowEntity extends AbstractArrow {

    private int charges = 0;

    private BlockPos prevPlacedPos = null;

    public RopeArrowEntity(EntityType<? extends RopeArrowEntity> type, Level world) {
        super(type, world);
    }

    public RopeArrowEntity(Level worldIn, LivingEntity throwerIn, int charges) {
        super((EntityType<? extends AbstractArrow>) ModEntities.ROPE_ARROW.get(), throwerIn, worldIn);
        this.charges = charges;
    }

    public RopeArrowEntity(Level worldIn, double x, double y, double z, int charges) {
        super((EntityType<? extends AbstractArrow>) ModEntities.ROPE_ARROW.get(), x, y, z, worldIn);
        this.charges = charges;
    }

    public RopeArrowEntity(Level worldIn, double x, double y, double z) {
        super((EntityType<? extends AbstractArrow>) ModEntities.ROPE_ARROW.get(), x, y, z, worldIn);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return PlatHelper.getEntitySpawnPacket(this);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Ropes", this.charges);
        if (this.prevPlacedPos != null) {
            compound.put("PrevPlacedPos", NbtUtils.writeBlockPos(this.prevPlacedPos));
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.charges = compound.getInt("Ropes");
        if (compound.contains("PrevPlacedPos")) {
            this.prevPlacedPos = NbtUtils.readBlockPos(compound.getCompound("PrevPlacedPos"));
        }
    }

    @Override
    protected ItemStack getPickupItem() {
        if (this.charges != 0) {
            ItemStack stack = new ItemStack((ItemLike) ModRegistry.ROPE_ARROW_ITEM.get());
            stack.setDamageValue(stack.getMaxDamage() - this.charges);
            return stack;
        } else {
            return new ItemStack(Items.ARROW);
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult rayTraceResult) {
        super.onHitBlock(rayTraceResult);
        Block ropeBlock = CommonConfigs.getSelectedRope();
        if (ropeBlock != null) {
            if (this.charges > 0) {
                Level level = this.m_9236_();
                if (!level.isClientSide) {
                    this.prevPlacedPos = null;
                    Entity entity = this.m_19749_();
                    Player player = null;
                    if (!(entity instanceof Mob) || PlatHelper.isMobGriefingOn(level, this)) {
                        BlockPos hitPos = rayTraceResult.getBlockPos();
                        if (entity instanceof Player pl) {
                            player = pl;
                            if (CompatHandler.FLAN && !FlanCompat.canPlace(pl, hitPos)) {
                                return;
                            }
                        }
                        BlockState hitState = level.getBlockState(hitPos);
                        Block hitBlock = hitState.m_60734_();
                        if (ropeBlock == ModRegistry.ROPE.get()) {
                            ModBlockProperties.PostType knotType = ModBlockProperties.PostType.get(hitState);
                            if (knotType != null) {
                                BlockState knotState = AbstractRopeKnotBlock.convertToRopeKnot(knotType, hitState, level, hitPos);
                                if (knotState != null) {
                                    if (((Direction.Axis) knotState.m_61143_(AbstractRopeKnotBlock.AXIS)).isVertical()) {
                                        this.prevPlacedPos = hitPos.relative(rayTraceResult.getDirection()).above();
                                    } else {
                                        this.prevPlacedPos = hitPos;
                                    }
                                    this.removeCharge();
                                    return;
                                }
                            }
                        }
                        if (hitBlock == ropeBlock && RopeHelper.addRopeDown(hitPos, level, player, InteractionHand.MAIN_HAND, ropeBlock)) {
                            this.prevPlacedPos = hitPos;
                            this.removeCharge();
                            return;
                        }
                        hitPos = hitPos.relative(rayTraceResult.getDirection());
                        hitBlock = level.getBlockState(hitPos).m_60734_();
                        if (hitBlock == ropeBlock && RopeHelper.addRopeDown(hitPos, level, player, InteractionHand.MAIN_HAND, ropeBlock)) {
                            this.prevPlacedPos = hitPos;
                            this.removeCharge();
                            return;
                        }
                        ItemStack ropes = new ItemStack(ropeBlock);
                        BlockPlaceContext context = new BlockPlaceContext(level, player, InteractionHand.MAIN_HAND, ropes, rayTraceResult);
                        if (context.canPlace()) {
                            BlockState state = ItemsUtil.getPlacementState(context, ropeBlock);
                            if (state != null) {
                                level.setBlock(context.getClickedPos(), state, 11);
                                this.prevPlacedPos = context.getClickedPos();
                                this.removeCharge();
                            }
                        }
                    }
                }
            }
        }
    }

    private void removeCharge() {
        this.charges = Math.max(0, this.charges - 1);
        this.m_9236_().playSound(null, this.prevPlacedPos, SoundEvents.LEASH_KNOT_PLACE, SoundSource.BLOCKS, 0.2F, 1.7F);
    }

    private void continueUnwindingRope() {
        Block ropeBlock = CommonConfigs.getSelectedRope();
        if (ropeBlock != null) {
            Player player = null;
            if (this.m_19749_() instanceof Player player1 && player1.mayBuild()) {
                player = player1;
            }
            BlockPos hitPos = this.prevPlacedPos;
            if (RopeHelper.addRopeDown(hitPos.below(), this.m_9236_(), player, InteractionHand.MAIN_HAND, ropeBlock)) {
                this.prevPlacedPos = hitPos.below();
                this.removeCharge();
            } else {
                this.prevPlacedPos = null;
            }
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.m_9236_().isClientSide && this.charges != 0 && this.prevPlacedPos != null) {
            this.continueUnwindingRope();
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        Entity entity = pResult.getEntity();
        int k = entity.getRemainingFireTicks();
        if (this.m_6060_() && entity.getType() != EntityType.ENDERMAN) {
            entity.setSecondsOnFire(5);
        }
        entity.setRemainingFireTicks(k);
        this.m_20256_(this.m_20184_().scale(-0.1));
        this.m_146922_(this.m_146908_() + 180.0F);
        this.f_19859_ += 180.0F;
        if (!this.m_9236_().isClientSide && this.m_20184_().lengthSqr() < 1.0E-7) {
            if (this.f_36705_ == AbstractArrow.Pickup.ALLOWED) {
                this.m_5552_(this.getPickupItem(), 0.1F);
            }
            this.m_146870_();
        }
    }

    @Override
    public void playerTouch(Player entityIn) {
        if (!this.m_9236_().isClientSide && entityIn.getInventory().add(this.getPickupItem())) {
            entityIn.m_7938_(this, 1);
            this.m_142687_(Entity.RemovalReason.DISCARDED);
        }
    }
}
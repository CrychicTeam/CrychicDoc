package com.github.alexmodguy.alexscaves.server.entity.item;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.github.alexmodguy.alexscaves.server.entity.util.MovingBlockData;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.network.NetworkHooks;

public abstract class AbstractMovingBlockEntity extends Entity {

    private static final EntityDataAccessor<CompoundTag> BLOCK_DATA_TAG = SynchedEntityData.defineId(AbstractMovingBlockEntity.class, EntityDataSerializers.COMPOUND_TAG);

    private List<MovingBlockData> data;

    private VoxelShape shape = null;

    private int placementCooldown = 40;

    private static boolean destroyErrorMessage;

    public AbstractMovingBlockEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> entityDataAccessor) {
        super.onSyncedDataUpdated(entityDataAccessor);
        if (BLOCK_DATA_TAG.equals(entityDataAccessor)) {
            this.data = this.buildDataFromTrackerTag();
            this.shape = this.getShape();
            this.m_20011_(this.makeBoundingBox());
        }
    }

    @Override
    protected void defineSynchedData() {
        this.f_19804_.define(BLOCK_DATA_TAG, new CompoundTag());
    }

    @Override
    public void tick() {
        super.tick();
        if (this.movesEntities() && this.m_20184_().length() > 0.0) {
            this.moveEntitiesOnTop();
        }
        this.m_6478_(MoverType.SELF, this.m_20184_());
        if (!this.m_9236_().isClientSide && this.canBePlaced()) {
            if (this.placementCooldown > 0) {
                this.placementCooldown--;
            } else {
                boolean clearance = true;
                BlockPos pos = BlockPos.containing(this.m_20185_(), this.m_20186_(), this.m_20189_());
                for (MovingBlockData dataBlock : this.getData()) {
                    BlockPos set = pos.offset(dataBlock.getOffset());
                    BlockState at = this.m_9236_().getBlockState(set);
                    if (!at.m_60795_()) {
                        if (at.m_247087_()) {
                            this.m_9236_().m_46961_(set, true);
                        } else {
                            clearance = false;
                        }
                    }
                }
                if (clearance) {
                    for (MovingBlockData dataBlockx : this.getData()) {
                        BlockPos set = pos.offset(dataBlockx.getOffset());
                        this.m_9236_().setBlockAndUpdate(set, dataBlockx.getState());
                        if (dataBlockx.blockData != null && dataBlockx.getState().m_155947_()) {
                            BlockEntity blockentity = this.m_9236_().getBlockEntity(set);
                            if (blockentity != null) {
                                CompoundTag compoundtag = blockentity.saveWithoutMetadata();
                                for (String s : dataBlockx.blockData.getAllKeys()) {
                                    compoundtag.put(s, dataBlockx.blockData.get(s).copy());
                                }
                                try {
                                    blockentity.load(compoundtag);
                                } catch (Exception var10) {
                                }
                                blockentity.setChanged();
                            }
                        }
                    }
                    this.m_142687_(Entity.RemovalReason.KILLED);
                } else {
                    this.placementCooldown = 5 + this.f_19796_.nextInt(10);
                }
            }
        }
        this.m_20256_(this.m_20184_().scale(0.98));
    }

    public boolean canBePlaced() {
        return true;
    }

    public abstract boolean movesEntities();

    public void moveEntitiesOnTop() {
        for (Entity entity : this.m_9236_().getEntities(this, this.m_20191_().inflate(0.0, 0.01F, 0.0), EntitySelector.NO_SPECTATORS.and(entityx -> !entityx.isPassengerOfSameVehicle(this)))) {
            if (!entity.noPhysics && !(entity instanceof MovingMetalBlockEntity)) {
                double gravity = entity.isNoGravity() ? 0.0 : 0.08;
                if (entity instanceof LivingEntity living) {
                    AttributeInstance attribute = living.getAttribute(ForgeMod.ENTITY_GRAVITY.get());
                    gravity = attribute.getValue();
                }
                float f2 = 1.0F;
                entity.move(MoverType.SHULKER, new Vec3((double) (f2 * (float) this.m_20184_().x), (double) (f2 * (float) this.m_20184_().y), (double) (f2 * (float) this.m_20184_().z)));
                if (this.m_20184_().y >= 0.0) {
                    entity.setDeltaMovement(entity.getDeltaMovement().add(0.0, gravity, 0.0));
                }
            }
        }
    }

    protected void createBlockDropAt(BlockPos crushPos, BlockState state, CompoundTag blockData) {
        if (this.m_9236_() instanceof ServerLevel serverLevel) {
            LootParams.Builder lootparams$builder = new LootParams.Builder(serverLevel).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(crushPos)).withParameter(LootContextParams.TOOL, ItemStack.EMPTY);
            try {
                for (ItemStack drop : state.m_287290_(lootparams$builder)) {
                    Block.popResource(serverLevel, crushPos, drop);
                }
                state.m_222967_(serverLevel, crushPos, ItemStack.EMPTY, true);
            } catch (Exception var9) {
                if (!destroyErrorMessage) {
                    destroyErrorMessage = true;
                    AlexsCaves.LOGGER.warn("Stopped crash when trying to destroy fake block entity for {}", state.m_60734_());
                }
            }
        }
    }

    @Override
    protected Entity.MovementEmission getMovementEmission() {
        return Entity.MovementEmission.NONE;
    }

    @Override
    public boolean isAttackable() {
        return false;
    }

    @Override
    public boolean displayFireAnimation() {
        return false;
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        if (compound.contains("BlockDataContainer", 10)) {
            this.setAllBlockData(compound.getCompound("BlockDataContainer"));
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        if (this.getAllBlockData() != null) {
            compound.put("BlockDataContainer", this.getAllBlockData());
        }
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    private List<MovingBlockData> buildDataFromTrackerTag() {
        List<MovingBlockData> list = new ArrayList();
        CompoundTag data = this.getAllBlockData();
        if (data.contains("BlockData")) {
            ListTag listTag = data.getList("BlockData", 10);
            for (int i = 0; i < listTag.size(); i++) {
                CompoundTag innerTag = listTag.getCompound(i);
                list.add(new MovingBlockData(this.m_9236_(), innerTag));
            }
        }
        return list;
    }

    public void setPlacementCooldown(int cooldown) {
        this.placementCooldown = cooldown;
    }

    public CompoundTag getAllBlockData() {
        return this.f_19804_.get(BLOCK_DATA_TAG);
    }

    public void setAllBlockData(CompoundTag tag) {
        this.f_19804_.set(BLOCK_DATA_TAG, tag);
    }

    public List<MovingBlockData> getData() {
        if (this.data == null) {
            this.data = this.buildDataFromTrackerTag();
        }
        return this.data;
    }

    public VoxelShape getShape() {
        Vec3 leftMostCorner = new Vec3(this.m_20185_() - 0.5, this.m_20186_() - 0.5, this.m_20189_() - 0.5);
        if (this.data != null && !this.data.isEmpty()) {
            VoxelShape building = Shapes.create(leftMostCorner.x, leftMostCorner.y, leftMostCorner.z, leftMostCorner.x + 1.0, leftMostCorner.y + 1.0, leftMostCorner.z + 1.0);
            for (MovingBlockData data : this.getData()) {
                building = Shapes.join(building, data.getShape().move(leftMostCorner.x + (double) data.getOffset().m_123341_(), leftMostCorner.y + (double) data.getOffset().m_123342_(), leftMostCorner.z + (double) data.getOffset().m_123343_()), BooleanOp.OR);
            }
            return building;
        } else {
            return Shapes.create(leftMostCorner.x, leftMostCorner.y, leftMostCorner.z, leftMostCorner.x + 1.0, leftMostCorner.y + 1.0, leftMostCorner.z + 1.0);
        }
    }

    @Override
    protected AABB makeBoundingBox() {
        List<AABB> aabbs = this.getShape().toAabbs();
        AABB minMax = new AABB(this.m_20185_() - 0.5, this.m_20186_() - 0.5, this.m_20189_() - 0.5, this.m_20185_() + 0.5, this.m_20186_() + 0.5, this.m_20189_() + 0.5);
        for (AABB aabb : aabbs) {
            minMax = minMax.minmax(aabb);
        }
        return minMax;
    }

    @Override
    public Vec3 getLightProbePosition(float f) {
        return this.m_20318_(f);
    }

    public static CompoundTag createTagFromData(List<MovingBlockData> blocks) {
        CompoundTag tag = new CompoundTag();
        ListTag listTag = new ListTag();
        for (MovingBlockData data : blocks) {
            listTag.add(data.toTag());
        }
        tag.put("BlockData", listTag);
        return tag;
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }
}
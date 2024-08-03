package dev.latvian.mods.kubejs.block.entity;

import dev.latvian.mods.kubejs.core.InventoryKJS;
import dev.latvian.mods.kubejs.level.BlockContainerJS;
import dev.latvian.mods.rhino.util.HideFromJS;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class BlockEntityJS extends BlockEntity {

    public final BlockEntityInfo info;

    protected BlockContainerJS block;

    public final int x;

    public final int y;

    public final int z;

    public int tick;

    public int cycle;

    public CompoundTag data;

    public final BlockEntityAttachment[] attachments;

    public InventoryKJS inventory;

    public UUID placerId;

    public BlockEntityJS(BlockPos blockPos, BlockState blockState, BlockEntityInfo entityInfo) {
        super(entityInfo.entityType, blockPos, blockState);
        this.info = entityInfo;
        this.x = blockPos.m_123341_();
        this.y = blockPos.m_123342_();
        this.z = blockPos.m_123343_();
        this.tick = 0;
        this.cycle = 0;
        this.data = this.info.initialData.copy();
        if (entityInfo.attachments != null) {
            this.attachments = new BlockEntityAttachment[entityInfo.attachments.size()];
            for (int i = 0; i < this.attachments.length; i++) {
                this.attachments[i] = ((BlockEntityAttachmentHolder) entityInfo.attachments.get(i)).factory().create(this);
                if (this.inventory == null && this.attachments[i] instanceof InventoryKJS inv) {
                    this.inventory = inv;
                }
            }
        } else {
            this.attachments = BlockEntityAttachment.EMPTY_ARRAY;
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("data", this.data);
        if (this.tick > 0) {
            tag.putInt("tick", this.tick);
        }
        if (this.cycle > 0) {
            tag.putInt("cycle", this.cycle);
        }
        if (this.placerId != null) {
            tag.putUUID("placer", this.placerId);
        }
        if (this.attachments.length > 0) {
            ListTag list = new ListTag();
            for (BlockEntityAttachment att : this.attachments) {
                list.add(att.writeAttachment());
            }
            tag.put("attachments", list);
        }
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.data = tag.getCompound("data");
        this.tick = tag.getInt("tick");
        this.cycle = tag.getInt("cycle");
        this.placerId = tag.contains("placer") ? tag.getUUID("placer") : null;
        if (this.attachments.length > 0) {
            ListTag list = tag.getList("attachments", 10);
            if (this.attachments.length == list.size()) {
                for (int i = 0; i < this.attachments.length; i++) {
                    this.attachments[i].readAttachment(list.getCompound(i));
                }
            } else {
                for (BlockEntityAttachment att : this.attachments) {
                    att.readAttachment(new CompoundTag());
                }
            }
        }
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        if (this.info.sync && !this.data.isEmpty()) {
            tag.put("data", this.data);
        }
        if (this.tick > 0) {
            tag.putInt("tick", this.tick);
        }
        if (this.cycle > 0) {
            tag.putInt("cycle", this.cycle);
        }
        return tag;
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public void save() {
        if (this.f_58857_ != null) {
            this.f_58857_.blockEntityChanged(this.f_58858_);
        }
    }

    public void sync() {
        if (this.f_58857_ != null) {
            this.save();
            this.f_58857_.sendBlockUpdated(this.f_58858_, this.m_58900_(), this.m_58900_(), 11);
        }
    }

    public void sendEvent(int eventId, int data) {
        this.f_58857_.blockEvent(this.f_58858_, this.m_58900_().m_60734_(), eventId, data);
    }

    @Override
    public boolean triggerEvent(int eventId, int data) {
        if (this.info.eventHandlers != null) {
            BlockEntityEventCallback e = (BlockEntityEventCallback) this.info.eventHandlers.get(eventId);
            if (e != null) {
                e.accept(this, data);
                return true;
            }
        }
        return false;
    }

    @HideFromJS
    public void postTick(boolean c) {
        this.tick++;
        if (c) {
            this.cycle++;
        }
    }

    public BlockContainerJS getBlock() {
        if (this.block == null) {
            this.block = new BlockContainerJS(this.f_58857_, this.f_58858_);
            this.block.cachedEntity = this;
        }
        return this.block;
    }
}
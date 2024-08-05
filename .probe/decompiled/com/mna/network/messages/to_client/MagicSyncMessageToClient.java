package com.mna.network.messages.to_client;

import com.mna.ManaAndArtifice;
import com.mna.api.affinity.Affinity;
import com.mna.api.capabilities.IPlayerMagic;
import com.mna.api.capabilities.resource.SyncStatus;
import com.mna.network.messages.BaseMessage;
import javax.annotation.Nullable;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class MagicSyncMessageToClient extends BaseMessage {

    private int _level;

    private int _xp;

    private float[] _affinities;

    private NonNullList<ItemStack> _grimoireInventory;

    private NonNullList<ItemStack> _roteInventory;

    private CompoundTag _cantripData;

    private CompoundTag castingResourceData;

    private ResourceLocation castingResourceType;

    private boolean _syncGrimoire;

    private boolean _syncRote;

    private boolean _syncCastingResource;

    private boolean _syncCantrips;

    private boolean _teleporting;

    private int _teleport_ticks_elapsed;

    private int _teleport_ticks_total;

    private MagicSyncMessageToClient() {
        this.messageIsValid = false;
        this._grimoireInventory = NonNullList.create();
        this._roteInventory = NonNullList.create();
    }

    public MagicSyncMessageToClient(int level, int xp, float[] affinities) {
        this();
        this._level = level;
        this._xp = xp;
        this._affinities = affinities;
        this.messageIsValid = true;
    }

    @Nullable
    public CompoundTag getCastingResourceData() {
        return this.castingResourceData;
    }

    public int getLevel() {
        return this._level;
    }

    public int getXP() {
        return this._xp;
    }

    public boolean getIsTeleporting() {
        return this._teleporting;
    }

    public int getTeleportElapsed() {
        return this._teleport_ticks_elapsed;
    }

    public int getTeleportTotal() {
        return this._teleport_ticks_total;
    }

    public float[] getAffinities() {
        return this._affinities;
    }

    public boolean syncGrimoire() {
        return this._syncGrimoire;
    }

    public boolean syncRote() {
        return this._syncRote;
    }

    public boolean syncCastingResource() {
        return this._syncCastingResource;
    }

    public boolean syncCantrips() {
        return this._syncCantrips;
    }

    @Nullable
    public NonNullList<ItemStack> getGrimoireInventory() {
        return this._grimoireInventory;
    }

    @Nullable
    public NonNullList<ItemStack> getRoteInventory() {
        return this._roteInventory;
    }

    @Nullable
    public CompoundTag getCantripData() {
        return this._cantripData;
    }

    public static MagicSyncMessageToClient decode(FriendlyByteBuf buf) {
        MagicSyncMessageToClient msg = new MagicSyncMessageToClient();
        try {
            msg._level = buf.readInt();
            msg._xp = buf.readInt();
            int affC = buf.readInt();
            msg._affinities = new float[affC];
            for (int i = 0; i < affC; i++) {
                msg._affinities[i] = buf.readFloat();
            }
            msg._syncCastingResource = buf.readBoolean();
            if (msg._syncCastingResource) {
                msg.castingResourceType = buf.readResourceLocation();
                msg.castingResourceData = buf.readNbt();
            }
            msg._syncGrimoire = buf.readBoolean();
            if (msg._syncGrimoire) {
                int count = buf.readInt();
                for (int i = 0; i < count; i++) {
                    msg._grimoireInventory.add(buf.readItem());
                }
            }
            msg._syncRote = buf.readBoolean();
            if (msg._syncRote) {
                int count = buf.readInt();
                for (int i = 0; i < count; i++) {
                    msg._roteInventory.add(buf.readItem());
                }
            }
            msg._syncCantrips = buf.readBoolean();
            if (msg._syncCantrips) {
                msg._cantripData = buf.readNbt();
            }
            msg._teleporting = buf.readBoolean();
            msg._teleport_ticks_elapsed = buf.readInt();
            msg._teleport_ticks_total = buf.readInt();
        } catch (IndexOutOfBoundsException | IllegalArgumentException var5) {
            ManaAndArtifice.LOGGER.error("Exception while reading MagicSyncMessageToClient: " + var5);
            return msg;
        }
        msg.messageIsValid = true;
        return msg;
    }

    public static void encode(MagicSyncMessageToClient msg, FriendlyByteBuf buf) {
        buf.writeInt(msg._level);
        buf.writeInt(msg._xp);
        buf.writeInt(msg._affinities.length);
        for (int i = 0; i < msg._affinities.length; i++) {
            buf.writeFloat(msg._affinities[i]);
        }
        buf.writeBoolean(msg.syncCastingResource());
        if (msg.syncCastingResource()) {
            buf.writeResourceLocation(msg.getCastingResourceType());
            buf.writeNbt(msg.castingResourceData);
        }
        buf.writeBoolean(msg.syncGrimoire());
        if (msg.syncGrimoire()) {
            buf.writeInt(msg._grimoireInventory.size());
            for (int i = 0; i < msg._grimoireInventory.size(); i++) {
                buf.writeItem(msg._grimoireInventory.get(i));
            }
        }
        buf.writeBoolean(msg.syncRote());
        if (msg.syncRote()) {
            buf.writeInt(msg._roteInventory.size());
            for (int i = 0; i < msg._roteInventory.size(); i++) {
                buf.writeItem(msg._roteInventory.get(i));
            }
        }
        buf.writeBoolean(msg.syncCantrips());
        if (msg.syncCantrips()) {
            buf.writeNbt(msg._cantripData);
        }
        buf.writeBoolean(msg._teleporting);
        buf.writeInt(msg._teleport_ticks_elapsed);
        buf.writeInt(msg._teleport_ticks_total);
    }

    public static MagicSyncMessageToClient fromCapability(IPlayerMagic capability) {
        float[] f = new float[Affinity.values().length];
        if (capability == null) {
            return new MagicSyncMessageToClient(0, 0, f);
        } else {
            int count = 0;
            for (Affinity aff : Affinity.values()) {
                f[count++] = capability.getAffinityDepth(aff);
            }
            MagicSyncMessageToClient msg = new MagicSyncMessageToClient(capability.getMagicLevel(), capability.getMagicXP(), f);
            if (capability.getCastingResource().getSyncStatus() != SyncStatus.NOT_NEEDED) {
                msg._syncCastingResource = true;
                msg.castingResourceType = capability.getCastingResource().getRegistryName();
                msg.castingResourceData = new CompoundTag();
                capability.getCastingResource().writeNBT(msg.castingResourceData);
            }
            if (capability.shouldSyncGrimoire()) {
                msg._syncGrimoire = true;
                for (int i = 0; i < capability.getGrimoireInventory().m_6643_(); i++) {
                    msg._grimoireInventory.add(capability.getGrimoireInventory().m_8020_(i));
                }
            }
            if (capability.shouldSyncRote()) {
                msg._syncRote = true;
                for (int i = 0; i < capability.getRoteInventory().m_6643_(); i++) {
                    msg._roteInventory.add(capability.getRoteInventory().m_8020_(i));
                }
            }
            if (capability.getCantripData().needsSync()) {
                msg._syncCantrips = true;
                msg._cantripData = capability.getCantripData().writeToNBT(false);
            }
            msg._teleporting = capability.getIsTeleporting();
            msg._teleport_ticks_elapsed = capability.getTeleportElapsedTicks();
            msg._teleport_ticks_total = capability.getTeleportTotalTicks();
            return msg;
        }
    }

    public ResourceLocation getCastingResourceType() {
        return this.castingResourceType;
    }
}
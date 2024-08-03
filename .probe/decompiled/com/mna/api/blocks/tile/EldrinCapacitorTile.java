package com.mna.api.blocks.tile;

import com.mna.api.ManaAndArtificeMod;
import com.mna.api.affinity.Affinity;
import com.mna.api.capabilities.IWellspringNodeRegistry;
import com.mna.api.events.EldrinPowerTransferredEvent;
import com.mna.api.faction.IFaction;
import com.mna.api.gui.EldrinCapacitorPermissionsContainer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;

public class EldrinCapacitorTile extends BlockEntity implements IEldrinCapacitorTile, Consumer<FriendlyByteBuf> {

    protected List<Affinity> suppliedAffinities;

    protected HashMap<Affinity, Float> power;

    protected float capacity;

    protected List<EldrinCapacitorPermissionsContainer> openContainers;

    protected boolean isPublic;

    protected boolean shareWithTeam;

    protected boolean shareWithFaction;

    protected UUID placedByPlayerID;

    protected String placedByPlayerName;

    protected IFaction placedByFaction;

    protected String placedByTeamName;

    protected boolean redstoneDisablesSupply = true;

    public EldrinCapacitorTile(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState, float capacity, Affinity... suppliedAffinities) {
        super(pType, pPos, pBlockState);
        this.suppliedAffinities = Arrays.asList(suppliedAffinities);
        this.power = new HashMap();
        this.capacity = capacity;
        this.openContainers = new ArrayList();
        this.suppliedAffinities.forEach(a -> this.power.put(a, 0.0F));
    }

    public static void Tick(Level level, BlockPos pos, BlockState state, EldrinCapacitorTile tile) {
        tile.ambientCharge();
    }

    protected void ambientCharge() {
        if (this.getPlacedBy() == null) {
            this.f_58857_.m_46961_(this.m_58899_(), true);
        } else {
            this.f_58857_.getCapability(ManaAndArtificeMod.getWorldMagicCapability()).ifPresent(worldMagic -> {
                IWellspringNodeRegistry wellsprings = worldMagic.getWellspringRegistry();
                this.suppliedAffinities.forEach(affinity -> {
                    float current = (Float) this.power.get(affinity);
                    if (current < this.capacity) {
                        float amount = wellsprings.consumePower(this.placedByPlayerID, this.f_58857_, affinity, this.getChargeRate());
                        this.power.put(affinity, current + amount);
                    }
                });
            });
        }
    }

    protected Vec3 getVFXOriginOffset() {
        return Vec3.atCenterOf(this.m_58899_());
    }

    @Override
    public boolean isPublic() {
        return this.isPublic;
    }

    @Override
    public boolean shareWithTeam() {
        return this.shareWithTeam;
    }

    @Override
    public boolean shareWithFaction() {
        return this.shareWithFaction;
    }

    @Override
    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    @Override
    public void setTeamShare(boolean teamShare) {
        this.shareWithTeam = teamShare;
    }

    @Override
    public void setFactionShare(boolean factionShare) {
        this.shareWithFaction = factionShare;
    }

    @Override
    public List<Affinity> getAffinities() {
        return this.suppliedAffinities;
    }

    @Override
    public float getCapacity(Affinity affinity) {
        return !this.supplies(affinity) ? 0.0F : this.capacity;
    }

    @Override
    public float getCharge(Affinity affinity) {
        return !this.supplies(affinity) ? 0.0F : (Float) this.power.get(affinity);
    }

    @Override
    public void setCharge(Affinity affinity, float amount) {
        if (this.supplies(affinity)) {
            this.power.put(affinity, amount);
        }
    }

    @Override
    public float charge(Affinity affinity, float amount) {
        if (!this.supplies(affinity)) {
            return 0.0F;
        } else {
            float current = (Float) this.power.get(affinity);
            if (current + amount > this.capacity) {
                amount = this.capacity - current;
            }
            this.power.put(affinity, current + amount);
            return amount;
        }
    }

    @Override
    public float consume(Affinity affinity, float amount) {
        if (!this.supplies(affinity)) {
            return 0.0F;
        } else {
            float current = (Float) this.power.get(affinity);
            if (current < amount) {
                amount = current;
            }
            this.power.put(affinity, current - amount);
            return amount;
        }
    }

    @Override
    public float supply(Player requester, Vec3 destPos, Affinity affinity, float amount) {
        if (this.getPlacedBy() == null) {
            return 0.0F;
        } else if (this.redstoneDisablesSupply && this.m_58904_().m_277086_(this.m_58899_()) > 0) {
            return 0.0F;
        } else {
            float actualAmount = IEldrinCapacitorTile.super.supply(requester, destPos, affinity, amount, true);
            EldrinPowerTransferredEvent event = new EldrinPowerTransferredEvent(affinity, actualAmount, this.getVFXOriginOffset(), destPos);
            return MinecraftForge.EVENT_BUS.post(event) ? 0.0F : IEldrinCapacitorTile.super.supply(requester, destPos, affinity, amount, false);
        }
    }

    @Override
    public void setPlacedBy(Player player) {
        this.placedByPlayerID = player.getGameProfile().getId();
        this.placedByPlayerName = player.getGameProfile().getName();
        if (player.m_5647_() != null) {
            this.placedByTeamName = player.m_5647_().getName();
        }
        player.getCapability(ManaAndArtificeMod.getProgressionCapability()).ifPresent(p -> this.placedByFaction = p.getAlliedFaction());
    }

    @Override
    public UUID getPlacedBy() {
        return this.placedByPlayerID;
    }

    @Override
    public String getPlacedByTeam() {
        if (this.placedByTeamName == null) {
            Player player = this.f_58857_.m_46003_(this.placedByPlayerID);
            if (player != null && player.m_5647_() != null) {
                this.placedByTeamName = player.m_5647_().getName();
            }
        }
        return this.placedByTeamName;
    }

    @Override
    public String getPlacedByPlayerName() {
        if (this.placedByPlayerName == null) {
            Player player = this.f_58857_.m_46003_(this.placedByPlayerID);
            if (player != null && player.m_5647_() != null) {
                this.placedByPlayerName = player.getGameProfile().getName();
            }
        }
        return this.placedByPlayerName;
    }

    @Override
    public IFaction getPlacedByFaction() {
        if (this.placedByFaction == null) {
            Player player = this.f_58857_.m_46003_(this.placedByPlayerID);
            if (player != null) {
                player.getCapability(ManaAndArtificeMod.getProgressionCapability()).ifPresent(c -> this.placedByFaction = c.getAlliedFaction());
            }
        }
        return this.placedByFaction;
    }

    public void addContainer(EldrinCapacitorPermissionsContainer container) {
        this.openContainers.add(container);
    }

    public void removeContainer(EldrinCapacitorPermissionsContainer container) {
        this.openContainers.remove(container);
    }

    public void accept(FriendlyByteBuf data) {
        data.writeBlockPos(this.m_58899_());
        if (this.getPlacedBy() != null) {
            data.writeBoolean(true);
            data.writeUUID(this.getPlacedBy());
        } else {
            data.writeBoolean(false);
        }
        if (this.placedByPlayerName != null) {
            data.writeBoolean(true);
            data.writeUtf(this.placedByPlayerName);
        } else {
            data.writeBoolean(false);
        }
        if (this.getPlacedByTeam() != null) {
            data.writeBoolean(true);
            data.writeUtf(this.getPlacedByTeam());
        } else {
            data.writeBoolean(false);
        }
        if (this.getPlacedByFaction() != null) {
            data.writeBoolean(true);
            data.writeRegistryId(ManaAndArtificeMod.getFactionsRegistry(), this.getPlacedByFaction());
        } else {
            data.writeBoolean(false);
        }
        data.writeInt(this.power.size());
        for (Entry<Affinity, Float> entry : this.power.entrySet()) {
            data.writeInt(((Affinity) entry.getKey()).ordinal());
            data.writeFloat((Float) entry.getValue());
        }
    }

    public EldrinCapacitorTile readFrom(FriendlyByteBuf data) {
        if (data.readBoolean()) {
            this.placedByPlayerID = data.readUUID();
        }
        if (data.readBoolean()) {
            this.placedByPlayerName = data.readUtf();
        }
        if (data.readBoolean()) {
            this.placedByTeamName = data.readUtf();
        }
        if (data.readBoolean()) {
            this.placedByFaction = (IFaction) data.readRegistryIdSafe(IFaction.class);
        }
        this.power.clear();
        int count = data.readInt();
        for (int i = 0; i < count; i++) {
            Affinity aff = Affinity.values()[data.readInt() % Affinity.values().length];
            this.power.put(aff, data.readFloat());
        }
        return this;
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.putBoolean("isPublic", this.isPublic);
        pTag.putBoolean("teamShare", this.shareWithTeam);
        pTag.putBoolean("factionShare", this.shareWithFaction);
        if (this.placedByPlayerID != null) {
            pTag.putString("player", this.placedByPlayerID.toString());
        }
        if (this.placedByPlayerName != null) {
            pTag.putString("playerName", this.placedByPlayerName);
        }
        if (this.placedByTeamName != null) {
            pTag.putString("team", this.placedByTeamName);
        }
        if (this.placedByFaction != null) {
            pTag.putString("faction", ManaAndArtificeMod.getFactionsRegistry().getKey(this.placedByFaction).toString());
        }
        this.suppliedAffinities.forEach(affinity -> pTag.putFloat(affinity.name(), (Float) this.power.get(affinity)));
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        if (pTag.contains("isPublic")) {
            this.isPublic = pTag.getBoolean("isPublic");
        }
        if (pTag.contains("teamShare")) {
            this.shareWithTeam = pTag.getBoolean("teamShare");
        }
        if (pTag.contains("factionShare")) {
            this.shareWithFaction = pTag.getBoolean("factionShare");
        }
        if (pTag.contains("player")) {
            this.placedByPlayerID = UUID.fromString(pTag.getString("player"));
        }
        if (pTag.contains("playerName")) {
            this.placedByPlayerName = pTag.getString("playerName");
        }
        if (pTag.contains("team")) {
            this.placedByTeamName = pTag.getString("team");
        }
        if (pTag.contains("faction")) {
            ResourceLocation faction = new ResourceLocation(pTag.getString("faction"));
            this.placedByFaction = ManaAndArtificeMod.getFactionsRegistry().getValue(faction);
        }
        this.power.clear();
        for (Affinity affinity : Affinity.values()) {
            if (pTag.contains(affinity.name())) {
                this.power.put(affinity, pTag.getFloat(affinity.name()));
            }
        }
        this.suppliedAffinities = new ArrayList(this.power.keySet());
    }
}
package com.mna.api.capabilities;

import com.mna.api.affinity.Affinity;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class WellspringNode {

    public static final float MIN_STRENGTH = 5.0F;

    public static final float MAX_STRENGTH = 25.0F;

    private final float strength;

    private Affinity affinity;

    private UUID claimedBy;

    private ResourceLocation claimedByFaction;

    private int yLevel = -1;

    public WellspringNode(Affinity affinity, float strength) {
        this.affinity = affinity;
        this.strength = Mth.clamp(strength, 5.0F, 25.0F);
        this.claimedByFaction = null;
    }

    public void writeToNBT(CompoundTag nbt) {
        nbt.putInt("affinity", this.affinity.ordinal());
        nbt.putFloat("strength", this.strength);
        if (this.claimedBy != null) {
            nbt.putString("claimedBy", this.claimedBy.toString());
        }
        if (this.claimedByFaction != null) {
            nbt.putString("claimedByFaction", this.claimedByFaction.toString());
        }
        if (this.yLevel > -1) {
            nbt.putInt("y", this.yLevel);
        }
    }

    public void setClaimedBy(UUID playerUUID, ResourceLocation faction, int y) {
        this.claimedBy = playerUUID;
        this.claimedByFaction = faction;
        this.yLevel = y;
    }

    public boolean setAffinity(Affinity affinity) {
        if (this.affinity == Affinity.UNKNOWN) {
            this.affinity = affinity;
            return true;
        } else {
            return false;
        }
    }

    public void clearClaimedBy() {
        this.claimedBy = null;
        this.claimedByFaction = null;
        this.yLevel = -1;
    }

    public boolean isClaimedBy(UUID playerUUID) {
        return playerUUID != null && this.claimedBy != null ? this.claimedBy.equals(playerUUID) : false;
    }

    public boolean isClaimedBy(ResourceLocation faction) {
        return this.claimedByFaction == null ? false : this.claimedByFaction == faction;
    }

    public boolean isClaimed() {
        return this.claimedBy != null && this.claimedByFaction != null;
    }

    public boolean hasForcedYLevel() {
        return this.yLevel > -1;
    }

    public Affinity getAffinity() {
        return this.affinity;
    }

    public float getStrength() {
        return this.strength;
    }

    @Nullable
    public UUID getClaimedBy() {
        return this.claimedBy;
    }

    public ResourceLocation getClaimedByFaction() {
        return this.claimedByFaction;
    }

    public int getYLevel() {
        return this.yLevel;
    }

    @Nullable
    public static WellspringNode fromNBT(CompoundTag nbt) {
        if (nbt.contains("affinity") && nbt.contains("strength")) {
            WellspringNode lln = new WellspringNode(Affinity.values()[nbt.getInt("affinity")], nbt.getFloat("strength"));
            if (nbt.contains("claimedBy") && nbt.contains("claimedByFaction") && nbt.contains("y")) {
                lln.setClaimedBy(UUID.fromString(nbt.getString("claimedBy")), new ResourceLocation(nbt.getString("claimedByFaction")), nbt.getInt("y"));
            }
            return lln;
        } else {
            return null;
        }
    }
}
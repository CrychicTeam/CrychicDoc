package com.mna.api.blocks.tile;

import com.mna.api.ManaAndArtificeMod;
import com.mna.api.affinity.Affinity;
import com.mna.api.capabilities.IPlayerProgression;
import com.mna.api.faction.IFaction;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.phys.Vec3;

public interface IEldrinCapacitorTile extends ContainerData {

    int DATA_SHARE_TEAM = 0;

    int DATA_SHARE_FACTION = 1;

    int DATA_SHARE_PUBLIC = 2;

    int DATA_AFFINITY_AMOUNT_ARCANE = 3;

    int DATA_AFFINITY_AMOUNT_ENDER = 4;

    int DATA_AFFINITY_AMOUNT_FIRE = 5;

    int DATA_AFFINITY_AMOUNT_WATER = 6;

    int DATA_AFFINITY_AMOUNT_EARTH = 7;

    int DATA_AFFINITY_AMOUNT_WIND = 8;

    int DATA_CHARGE_RATE = 9;

    int DATA_CHARGE_RADIUS = 10;

    List<Affinity> getAffinities();

    default float getChargeRate() {
        return 1.0F;
    }

    default float getRateLimit() {
        return -1.0F;
    }

    default float getChargeRadius() {
        return 16.0F;
    }

    float getCapacity(Affinity var1);

    float getCharge(Affinity var1);

    void setCharge(Affinity var1, float var2);

    float charge(Affinity var1, float var2);

    float consume(Affinity var1, float var2);

    default boolean supplies(Affinity affinity) {
        return this.getAffinities().contains(affinity);
    }

    default boolean canSupply(Affinity affinity, Player crafter) {
        if (affinity != Affinity.UNKNOWN && !this.supplies(affinity)) {
            return false;
        } else if (crafter != null && this.getPlacedBy() != null) {
            if (crafter.getGameProfile().getId().equals(this.getPlacedBy())) {
                return true;
            } else if (this.shareWithTeam() && this.getPlacedByTeam() != null && crafter.m_5647_() != null) {
                return crafter.m_5647_().getName() == this.getPlacedByTeam();
            } else {
                if (this.shareWithFaction()) {
                    IPlayerProgression progression = (IPlayerProgression) crafter.getCapability(ManaAndArtificeMod.getProgressionCapability()).orElse(null);
                    if (progression != null) {
                        IFaction crafterFaction = progression.getAlliedFaction();
                        IFaction placedByFaction = this.getPlacedByFaction();
                        if (crafterFaction != null && placedByFaction != null) {
                            return crafterFaction == placedByFaction;
                        }
                    }
                }
                return this.isPublic();
            }
        } else {
            return false;
        }
    }

    boolean isPublic();

    boolean shareWithTeam();

    boolean shareWithFaction();

    void setPublic(boolean var1);

    void setTeamShare(boolean var1);

    void setFactionShare(boolean var1);

    UUID getPlacedBy();

    String getPlacedByPlayerName();

    @Nullable
    String getPlacedByTeam();

    @Nullable
    IFaction getPlacedByFaction();

    void setPlacedBy(Player var1);

    default float supply(Player requester, Vec3 destPos, Affinity affinity, float amount) {
        return this.supply(requester, destPos, affinity, amount, false);
    }

    default float supply(Player requester, Vec3 destPos, Affinity affinity, float amount, boolean simulate) {
        if (!this.canSupply(affinity, requester)) {
            return 0.0F;
        } else {
            float rateLimit = this.getRateLimit();
            if (rateLimit > 0.0F && amount > rateLimit) {
                amount = rateLimit;
            }
            if (simulate) {
                float current = this.getCharge(affinity);
                return current < amount ? current : amount;
            } else {
                return this.consume(affinity, amount);
            }
        }
    }

    @Override
    default int getCount() {
        return 11;
    }

    @Override
    default void set(int pIndex, int pValue) {
        switch(pIndex) {
            case 0:
                this.setTeamShare(pValue == 1);
                break;
            case 1:
                this.setFactionShare(pValue == 1);
                break;
            case 2:
                this.setPublic(pValue == 1);
                break;
            case 3:
                this.setCharge(Affinity.ARCANE, (float) pValue / 100.0F);
                break;
            case 4:
                this.setCharge(Affinity.ENDER, (float) pValue / 100.0F);
                break;
            case 5:
                this.setCharge(Affinity.FIRE, (float) pValue / 100.0F);
                break;
            case 6:
                this.setCharge(Affinity.WATER, (float) pValue / 100.0F);
                break;
            case 7:
                this.setCharge(Affinity.EARTH, (float) pValue / 100.0F);
                break;
            case 8:
                this.setCharge(Affinity.WIND, (float) pValue / 100.0F);
        }
    }

    @Override
    default int get(int pIndex) {
        switch(pIndex) {
            case 0:
                return this.shareWithTeam() ? 1 : 0;
            case 1:
                return this.shareWithFaction() ? 1 : 0;
            case 2:
                return this.isPublic() ? 1 : 0;
            case 3:
                return (int) (this.getCharge(Affinity.ARCANE) * 100.0F);
            case 4:
                return (int) (this.getCharge(Affinity.ENDER) * 100.0F);
            case 5:
                return (int) (this.getCharge(Affinity.FIRE) * 100.0F);
            case 6:
                return (int) (this.getCharge(Affinity.WATER) * 100.0F);
            case 7:
                return (int) (this.getCharge(Affinity.EARTH) * 100.0F);
            case 8:
                return (int) (this.getCharge(Affinity.WIND) * 100.0F);
            case 9:
                return (int) (this.getChargeRate() * 100.0F);
            case 10:
                return (int) (this.getChargeRadius() * 100.0F);
            default:
                return 0;
        }
    }
}
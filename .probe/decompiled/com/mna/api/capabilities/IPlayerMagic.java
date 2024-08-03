package com.mna.api.capabilities;

import com.mna.api.affinity.Affinity;
import com.mna.api.capabilities.resource.ICastingResource;
import com.mna.api.items.inventory.SpellInventory;
import com.mna.api.spells.base.ISpellDefinition;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public interface IPlayerMagic {

    int FLAGS_CASTING_RESOURCE = 1;

    int FLAGS_GRIMOIRE = 2;

    int FLAGS_ROTE = 4;

    void setModifierPressed(boolean var1);

    boolean isModifierPressed();

    boolean isMagicUnlocked();

    void unlockMagic();

    void copyFrom(IPlayerMagic var1);

    int getMagicLevel();

    void setMagicLevel(Player var1, int var2);

    void magicLevelUp(Player var1, IPlayerProgression var2);

    int getMagicXP();

    int getXPForLevel(int var1);

    void setMagicXP(int var1);

    void addMagicXP(int var1, Player var2, IPlayerProgression var3);

    float getAffinityDepth(Affinity var1);

    Map<Affinity, Float> getSortedAffinityDepths();

    void setAffinityDepth(Affinity var1, float var2);

    void shiftAffinity(Player var1, Affinity var2, float var3);

    ICastingResource getCastingResource();

    void setCastingResourceType(ResourceLocation var1);

    int getParticleColorOverride();

    void setParticleColorOverride(int var1);

    boolean canRegenerate(Player var1);

    int getTeleportSalt();

    void setTeleportSalt(int var1);

    void resetTeleportSalt();

    void tick(Player var1);

    boolean needsSync();

    void clearSyncFlags();

    void forceSync();

    void forceSync(int var1);

    boolean shouldSyncGrimoire();

    void setSyncGrimoire();

    boolean shouldSyncRote();

    void setSyncRote();

    int getPortalCooldown();

    void setPortalCooldown(int var1);

    boolean getIsTeleporting();

    void delayedTeleportTo(int var1, Vec3 var2, Vec2 var3, ServerLevel var4);

    void cancelTeleport();

    int getTeleportElapsedTicks();

    int getTeleportTotalTicks();

    void updateClientsideTeleportData(boolean var1, int var2, int var3);

    void clearRememberedPoints();

    void addRememberedPoint(Vector3f var1, Vector3f var2);

    Vector3f[] getRememberedPoints();

    Vector3f getAverageLook();

    Vector3f[] getRememberedLooks();

    SimpleContainer getRiftInventory();

    SpellInventory getGrimoireInventory();

    SpellInventory getRoteInventory();

    @Deprecated(forRemoval = true)
    default int getAirCastLimit(Player player) {
        return this.getAirCastLimit(player, null);
    }

    int getAirCastLimit(Player var1, @Nullable ISpellDefinition var2);

    int getAirCasts();

    @Deprecated(forRemoval = true)
    default void incrementAirCasts(Player player) {
        this.incrementAirCasts(player, null);
    }

    void incrementAirCasts(Player var1, @Nullable ISpellDefinition var2);

    void setAirCasts(int var1);

    int getAirJumps();

    void incrementAirJumps(Player var1);

    void setAirJumps(int var1);

    ChronoAnchorData getChronoAnchorData();

    void setNeedsChronoExhaustion();

    Vec3 getLiftPosition();

    void setLiftPosition(Vec3 var1);

    boolean didAllowFlying();

    void setDidAllowFlying(boolean var1);

    float getFocusDistance();

    void setFocusDistance(float var1);

    void offsetFocusDistance(float var1, float var2);

    int getEmberCooldown();

    void setEmberCooldown(int var1);

    boolean getHadWizardSight();

    void setHadWizardSight(boolean var1);

    IPlayerCantrips getCantripData();

    int bankArmorRepair(int var1, float var2);

    HashMap<Integer, Float> getBankedArmorRepair();

    void setBankedArmorRepair(HashMap<Integer, Float> var1);

    void validate();
}
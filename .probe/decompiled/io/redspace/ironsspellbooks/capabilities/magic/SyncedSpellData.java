package io.redspace.ironsspellbooks.capabilities.magic;

import io.redspace.ironsspellbooks.api.entity.IMagicEntity;
import io.redspace.ironsspellbooks.api.magic.LearnedSpellData;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.gui.overlays.SpellSelection;
import io.redspace.ironsspellbooks.network.ClientboundSyncEntityData;
import io.redspace.ironsspellbooks.network.ClientboundSyncPlayerData;
import io.redspace.ironsspellbooks.player.SpinAttackType;
import io.redspace.ironsspellbooks.setup.Messages;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

public class SyncedSpellData {

    public static final long ANGEL_WINGS = 1L;

    public static final long EVASION = 2L;

    public static final long HEARTSTOP = 4L;

    public static final long ABYSSAL_SHROUD = 8L;

    public static final long ASCENSION = 16L;

    public static final long TRUE_INVIS = 32L;

    public static final long CHARGED = 64L;

    public static final long PLANAR_SIGHT = 128L;

    public static final long HEAL_TARGET = 1L;

    private final int serverPlayerId;

    @Nullable
    private LivingEntity livingEntity = null;

    private boolean isCasting;

    private String castingSpellId;

    private int castingSpellLevel;

    private long syncedEffectFlags;

    private long localEffectFlags;

    private float heartStopAccumulatedDamage;

    private int evasionHitsRemaining;

    private SpinAttackType spinAttackType;

    private LearnedSpellData learnedSpellData;

    private SpellSelection spellSelection;

    private String castingEquipmentSlot;

    public static final EntityDataSerializer<SyncedSpellData> SYNCED_SPELL_DATA = new EntityDataSerializer.ForValueType<SyncedSpellData>() {

        public void write(FriendlyByteBuf buffer, SyncedSpellData data) {
            buffer.writeInt(data.serverPlayerId);
            buffer.writeBoolean(data.isCasting);
            buffer.writeUtf(data.castingSpellId);
            buffer.writeInt(data.castingSpellLevel);
            buffer.writeLong(data.syncedEffectFlags);
            buffer.writeFloat(data.heartStopAccumulatedDamage);
            buffer.writeInt(data.evasionHitsRemaining);
            buffer.writeEnum(data.spinAttackType);
            buffer.writeUtf(data.castingEquipmentSlot);
            data.learnedSpellData.writeToBuffer(buffer);
            data.spellSelection.writeToBuffer(buffer);
        }

        public SyncedSpellData read(FriendlyByteBuf buffer) {
            SyncedSpellData data = new SyncedSpellData(buffer.readInt());
            data.isCasting = buffer.readBoolean();
            data.castingSpellId = buffer.readUtf();
            data.castingSpellLevel = buffer.readInt();
            data.syncedEffectFlags = buffer.readLong();
            data.heartStopAccumulatedDamage = buffer.readFloat();
            data.evasionHitsRemaining = buffer.readInt();
            data.spinAttackType = buffer.readEnum(SpinAttackType.class);
            data.castingEquipmentSlot = buffer.readUtf();
            data.learnedSpellData.readFromBuffer(buffer);
            data.spellSelection.readFromBuffer(buffer);
            return data;
        }
    };

    public SyncedSpellData(int serverPlayerId) {
        this.serverPlayerId = serverPlayerId;
        this.isCasting = false;
        this.castingSpellId = "";
        this.castingEquipmentSlot = "";
        this.castingSpellLevel = 0;
        this.syncedEffectFlags = 0L;
        this.localEffectFlags = 0L;
        this.heartStopAccumulatedDamage = 0.0F;
        this.evasionHitsRemaining = 0;
        this.spinAttackType = SpinAttackType.RIPTIDE;
        this.learnedSpellData = new LearnedSpellData();
        this.spellSelection = new SpellSelection();
    }

    public SyncedSpellData(LivingEntity livingEntity) {
        this(livingEntity == null ? -1 : livingEntity.m_19879_());
        this.livingEntity = livingEntity;
    }

    public void saveNBTData(CompoundTag compound) {
        compound.putBoolean("isCasting", this.isCasting);
        compound.putString("castingSpellId", this.castingSpellId);
        compound.putString("castingEquipmentSlot", this.castingEquipmentSlot);
        compound.putInt("castingSpellLevel", this.castingSpellLevel);
        compound.putLong("effectFlags", this.syncedEffectFlags);
        compound.putFloat("heartStopAccumulatedDamage", this.heartStopAccumulatedDamage);
        compound.putFloat("evasionHitsRemaining", (float) this.evasionHitsRemaining);
        this.learnedSpellData.saveToNBT(compound);
        compound.put("spellSelection", this.spellSelection.serializeNBT());
    }

    public void loadNBTData(CompoundTag compound) {
        this.isCasting = compound.getBoolean("isCasting");
        this.castingSpellId = compound.getString("castingSpellId");
        this.castingEquipmentSlot = compound.getString("castingEquipmentSlot");
        this.castingSpellLevel = compound.getInt("castingSpellLevel");
        this.syncedEffectFlags = compound.getLong("effectFlags");
        this.heartStopAccumulatedDamage = compound.getFloat("heartStopAccumulatedDamage");
        this.evasionHitsRemaining = compound.getInt("evasionHitsRemaining");
        this.learnedSpellData.loadFromNBT(compound);
        this.spellSelection.deserializeNBT(compound.getCompound("spellSelection"));
    }

    public int getServerPlayerId() {
        return this.serverPlayerId;
    }

    public boolean hasEffect(long effectFlags) {
        return (this.syncedEffectFlags & effectFlags) == effectFlags;
    }

    public String getCastingEquipmentSlot() {
        return this.castingEquipmentSlot;
    }

    public boolean hasLocalEffect(long effectFlags) {
        return (this.localEffectFlags & effectFlags) == effectFlags;
    }

    public void addLocalEffect(long effectFlags) {
        this.localEffectFlags |= effectFlags;
    }

    public void removeLocalEffect(long effectFlags) {
        this.localEffectFlags &= ~effectFlags;
    }

    public float getHeartstopAccumulatedDamage() {
        return this.heartStopAccumulatedDamage;
    }

    public boolean hasDodgeEffect() {
        return this.hasEffect(2L) || this.hasEffect(8L);
    }

    public void setHeartstopAccumulatedDamage(float damage) {
        this.heartStopAccumulatedDamage = damage;
        this.doSync();
    }

    public SpellSelection getSpellSelection() {
        return this.spellSelection;
    }

    public void setSpellSelection(SpellSelection spellSelection) {
        this.spellSelection = spellSelection;
        this.doSync();
    }

    public void learnSpell(AbstractSpell spell) {
        this.learnedSpellData.learnedSpells.add(spell.getSpellResource());
        this.doSync();
    }

    public void forgetAllSpells() {
        this.learnedSpellData.learnedSpells.clear();
        this.doSync();
    }

    public boolean isSpellLearned(AbstractSpell spell) {
        return !spell.needsLearning() || this.learnedSpellData.learnedSpells.contains(spell.getSpellResource());
    }

    public SpinAttackType getSpinAttackType() {
        return this.spinAttackType;
    }

    public void setSpinAttackType(SpinAttackType spinAttackType) {
        this.spinAttackType = spinAttackType;
        this.doSync();
    }

    public int getEvasionHitsRemaining() {
        return this.evasionHitsRemaining;
    }

    public void subtractEvasionHit() {
        this.evasionHitsRemaining--;
        this.doSync();
    }

    public void setEvasionHitsRemaining(int hitsRemaining) {
        this.evasionHitsRemaining = hitsRemaining;
        this.doSync();
    }

    public void addHeartstopDamage(float damage) {
        this.heartStopAccumulatedDamage += damage;
        this.doSync();
    }

    public void addEffects(long effectFlags) {
        this.syncedEffectFlags |= effectFlags;
        this.doSync();
    }

    public void removeEffects(long effectFlags) {
        this.syncedEffectFlags &= ~effectFlags;
        this.doSync();
    }

    public void doSync() {
        if (this.livingEntity instanceof ServerPlayer serverPlayer) {
            Messages.sendToPlayer(new ClientboundSyncPlayerData(this), serverPlayer);
            Messages.sendToPlayersTrackingEntity(new ClientboundSyncPlayerData(this), serverPlayer);
        } else if (this.livingEntity instanceof IMagicEntity abstractSpellCastingMob) {
            Messages.sendToPlayersTrackingEntity(new ClientboundSyncEntityData(this, abstractSpellCastingMob), this.livingEntity);
        }
    }

    public void syncToPlayer(ServerPlayer serverPlayer) {
        Messages.sendToPlayer(new ClientboundSyncPlayerData(this), serverPlayer);
    }

    public void setIsCasting(boolean isCasting, String castingSpellId, int castingSpellLevel, String castingEquipmentSlot) {
        this.isCasting = isCasting;
        this.castingSpellId = castingSpellId;
        this.castingSpellLevel = castingSpellLevel;
        this.castingEquipmentSlot = castingEquipmentSlot;
        this.doSync();
    }

    public boolean isCasting() {
        return this.isCasting;
    }

    public String getCastingSpellId() {
        return this.castingSpellId;
    }

    public int getCastingSpellLevel() {
        return this.castingSpellLevel;
    }

    protected SyncedSpellData clone() {
        return new SyncedSpellData(this.livingEntity);
    }

    public String toString() {
        return String.format("isCasting:%s, spellID:%s, spellLevel:%d, effectFlags:%d", this.isCasting, this.castingSpellId, this.castingSpellLevel, this.syncedEffectFlags);
    }

    public SyncedSpellData getPersistentData() {
        SyncedSpellData persistentData = new SyncedSpellData(this.livingEntity);
        persistentData.learnedSpellData.learnedSpells.addAll(this.learnedSpellData.learnedSpells);
        return persistentData;
    }
}
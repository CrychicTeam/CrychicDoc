package io.redspace.ironsspellbooks.api.magic;

import io.redspace.ironsspellbooks.api.entity.IMagicEntity;
import io.redspace.ironsspellbooks.api.events.ChangeManaEvent;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.ICastData;
import io.redspace.ironsspellbooks.api.spells.SpellData;
import io.redspace.ironsspellbooks.capabilities.magic.PlayerCooldowns;
import io.redspace.ironsspellbooks.capabilities.magic.PlayerMagicProvider;
import io.redspace.ironsspellbooks.capabilities.magic.PlayerRecasts;
import io.redspace.ironsspellbooks.capabilities.magic.SyncedSpellData;
import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.Nullable;

public class MagicData {

    private boolean isMob = false;

    private ServerPlayer serverPlayer = null;

    public static final String MANA = "mana";

    public static final String COOLDOWNS = "cooldowns";

    public static final String RECASTS = "recasts";

    private float mana;

    private SyncedSpellData syncedSpellData;

    private int castingSpellLevel = 0;

    private int castDuration = 0;

    private int castDurationRemaining = 0;

    private CastSource castSource;

    private CastType castType;

    @Nullable
    private ICastData additionalCastData;

    private int poisonedTimestamp;

    private ItemStack castingItemStack = ItemStack.EMPTY;

    private final PlayerCooldowns playerCooldowns = new PlayerCooldowns();

    private PlayerRecasts playerRecasts = new PlayerRecasts();

    public MagicData(boolean isMob) {
        this.isMob = isMob;
    }

    public MagicData() {
        this(false);
    }

    public MagicData(ServerPlayer serverPlayer) {
        this(false);
        this.serverPlayer = serverPlayer;
        this.playerRecasts = new PlayerRecasts(serverPlayer);
    }

    public void setServerPlayer(ServerPlayer serverPlayer) {
        if (this.serverPlayer == null && serverPlayer != null) {
            this.serverPlayer = serverPlayer;
            this.playerRecasts = new PlayerRecasts(serverPlayer);
        }
    }

    public float getMana() {
        return this.mana;
    }

    public void setMana(float mana) {
        ChangeManaEvent e = new ChangeManaEvent(this.serverPlayer, this, this.mana, mana);
        if (this.serverPlayer == null || !MinecraftForge.EVENT_BUS.post(e)) {
            this.mana = e.getNewMana();
        }
        if (this.serverPlayer != null) {
            float maxMana = (float) this.serverPlayer.m_21133_(AttributeRegistry.MAX_MANA.get());
            if (this.mana > maxMana) {
                this.mana = maxMana;
            }
        }
    }

    public void addMana(float mana) {
        this.setMana(this.mana + mana);
    }

    public SyncedSpellData getSyncedData() {
        if (this.syncedSpellData == null) {
            this.syncedSpellData = new SyncedSpellData(this.serverPlayer);
        }
        return this.syncedSpellData;
    }

    public void setSyncedData(SyncedSpellData syncedSpellData) {
        this.syncedSpellData = syncedSpellData;
    }

    public void resetCastingState() {
        this.castingSpellLevel = 0;
        this.castDuration = 0;
        this.castDurationRemaining = 0;
        this.castSource = CastSource.NONE;
        this.castType = CastType.NONE;
        this.getSyncedData().setIsCasting(false, "", 0, this.getCastingEquipmentSlot());
        this.resetAdditionalCastData();
        if (this.serverPlayer != null) {
            this.serverPlayer.m_5810_();
        } else if (!this.isMob) {
            Minecraft.getInstance().player.stopUsingItem();
        }
    }

    public void initiateCast(AbstractSpell spell, int spellLevel, int castDuration, CastSource castSource, String castingEquipmentSlot) {
        this.castingSpellLevel = spellLevel;
        this.castDuration = castDuration;
        this.castDurationRemaining = castDuration;
        this.castSource = castSource;
        this.castType = spell.getCastType();
        this.syncedSpellData.setIsCasting(true, spell.getSpellId(), spellLevel, castingEquipmentSlot);
    }

    public ICastData getAdditionalCastData() {
        return this.additionalCastData;
    }

    public void setAdditionalCastData(ICastData newCastData) {
        this.additionalCastData = newCastData;
    }

    public void resetAdditionalCastData() {
        if (this.additionalCastData != null) {
            this.additionalCastData.reset();
            this.additionalCastData = null;
        }
    }

    public boolean isCasting() {
        return this.getSyncedData().isCasting();
    }

    public String getCastingEquipmentSlot() {
        return this.getSyncedData().getCastingEquipmentSlot();
    }

    public String getCastingSpellId() {
        return this.getSyncedData().getCastingSpellId();
    }

    public SpellData getCastingSpell() {
        return new SpellData(SpellRegistry.getSpell(this.getSyncedData().getCastingSpellId()), this.castingSpellLevel);
    }

    public int getCastingSpellLevel() {
        return this.castingSpellLevel;
    }

    public CastSource getCastSource() {
        return this.castSource == null ? CastSource.NONE : this.castSource;
    }

    public CastType getCastType() {
        return this.castType;
    }

    public float getCastCompletionPercent() {
        return this.castDuration == 0 ? 1.0F : 1.0F - (float) this.castDurationRemaining / (float) this.castDuration;
    }

    public int getCastDurationRemaining() {
        return this.castDurationRemaining;
    }

    public int getCastDuration() {
        return this.castDuration;
    }

    public void handleCastDuration() {
        this.castDurationRemaining--;
        if (this.castDurationRemaining <= 0) {
            this.castDurationRemaining = 0;
        }
    }

    public void setPlayerCastingItem(ItemStack itemStack) {
        this.castingItemStack = itemStack;
    }

    public ItemStack getPlayerCastingItem() {
        return this.castingItemStack;
    }

    public void markPoisoned() {
        if (this.serverPlayer != null) {
            this.poisonedTimestamp = this.serverPlayer.f_19797_;
        }
    }

    public boolean popMarkedPoison() {
        if (this.serverPlayer != null) {
            boolean poisoned = this.serverPlayer.f_19797_ - this.poisonedTimestamp <= 1;
            this.poisonedTimestamp = 0;
            return poisoned;
        } else {
            return false;
        }
    }

    public PlayerCooldowns getPlayerCooldowns() {
        return this.playerCooldowns;
    }

    public PlayerRecasts getPlayerRecasts() {
        return this.playerRecasts;
    }

    @OnlyIn(Dist.CLIENT)
    public void setPlayerRecasts(PlayerRecasts playerRecasts) {
        this.playerRecasts = playerRecasts;
    }

    public static MagicData getPlayerMagicData(LivingEntity livingEntity) {
        if (livingEntity instanceof IMagicEntity magicEntity) {
            return magicEntity.getMagicData();
        } else if (livingEntity instanceof ServerPlayer serverPlayer) {
            LazyOptional<MagicData> capContainer = serverPlayer.getCapability(PlayerMagicProvider.PLAYER_MAGIC);
            if (capContainer.isPresent()) {
                Optional<MagicData> opt = capContainer.resolve();
                if (opt.isEmpty()) {
                    return new MagicData(serverPlayer);
                } else {
                    MagicData pmd = (MagicData) opt.get();
                    pmd.setServerPlayer(serverPlayer);
                    return pmd;
                }
            } else {
                return new MagicData(serverPlayer);
            }
        } else {
            return new MagicData(true);
        }
    }

    public void saveNBTData(CompoundTag compound) {
        compound.putInt("mana", (int) this.mana);
        if (this.playerCooldowns.hasCooldownsActive()) {
            compound.put("cooldowns", this.playerCooldowns.saveNBTData());
        }
        if (this.playerRecasts.hasRecastsActive()) {
            compound.put("recasts", this.playerRecasts.saveNBTData());
        }
        this.getSyncedData().saveNBTData(compound);
    }

    public void loadNBTData(CompoundTag compound) {
        this.mana = (float) compound.getInt("mana");
        ListTag listTag = (ListTag) compound.get("cooldowns");
        if (listTag != null && !listTag.isEmpty()) {
            this.playerCooldowns.loadNBTData(listTag);
        }
        listTag = (ListTag) compound.get("recasts");
        if (listTag != null && !listTag.isEmpty()) {
            this.playerRecasts.loadNBTData(listTag);
        }
        this.getSyncedData().loadNBTData(compound);
    }

    public String toString() {
        return String.format("isCasting:%s, spellID:%s], spellLevel:%s, duration:%s, durationRemaining:%s, source:%s, type:%s", this.getSyncedData().isCasting(), this.getSyncedData().getCastingSpellId(), this.castingSpellLevel, this.castDuration, this.castDurationRemaining, this.castSource, this.castType);
    }
}
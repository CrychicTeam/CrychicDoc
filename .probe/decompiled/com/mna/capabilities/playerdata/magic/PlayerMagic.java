package com.mna.capabilities.playerdata.magic;

import com.mna.ManaAndArtifice;
import com.mna.advancements.CustomAdvancementTriggers;
import com.mna.api.affinity.Affinity;
import com.mna.api.capabilities.ChronoAnchorData;
import com.mna.api.capabilities.IPlayerCantrips;
import com.mna.api.capabilities.IPlayerMagic;
import com.mna.api.capabilities.IPlayerProgression;
import com.mna.api.capabilities.resource.ICastingResource;
import com.mna.api.capabilities.resource.SyncStatus;
import com.mna.api.events.AffinityChangedEvent;
import com.mna.api.events.AirCastLimitEvent;
import com.mna.api.events.MagicXPGainedEvent;
import com.mna.api.items.inventory.SpellInventory;
import com.mna.api.sound.SFX;
import com.mna.api.spells.base.ISpellDefinition;
import com.mna.capabilities.playerdata.magic.resources.CastingResourceRegistry;
import com.mna.capabilities.playerdata.magic.resources.Mana;
import com.mna.capabilities.playerdata.progression.PlayerProgressionProvider;
import com.mna.effects.EffectInit;
import com.mna.events.EventDispatcher;
import com.mna.items.ItemInit;
import com.mna.tools.TeleportHelper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import org.apache.commons.lang3.mutable.MutableInt;
import org.joml.Vector3f;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;

public class PlayerMagic implements IPlayerMagic {

    public static final float MIN_FOCUS_DIST = 0.5F;

    private static final float EXHAUSTION_PER_TICK = 3.3333333E-4F;

    private static int MAGIC_LEVELS_PER_MILESTONE = 15;

    private static final int SYNC_INTERVAL = 10;

    public static final int TELEPORT_TIME = 100;

    private ICastingResource castingResource;

    private int tick_count = 10;

    private int teleport_salt;

    private int portal_cooldown = 0;

    private int teleport_count = 0;

    private boolean is_teleporting = false;

    private int teleport_duration = 0;

    private Vec3 teleport_destination = null;

    private ServerLevel teleport_dimension = null;

    private Vec2 teleport_rotation = null;

    private int magicLevel = 0;

    private boolean didAllowFlight = false;

    private int magicXP = 0;

    private int air_casts = 0;

    private int air_jumps = 0;

    private float focus_distance = 4.0F;

    private int ember_cooldown = 0;

    private HashMap<Integer, Float> armor_repair_accumulation;

    private ArrayList<Vector3f> rememberedPoints;

    private ArrayList<Vector3f> rememberedLooks;

    private HashMap<Affinity, Float> affinityDepths;

    private SimpleContainer riftInventory;

    private SpellInventory grimoireInventory;

    private SpellInventory roteInventory;

    private ChronoAnchorData chronoData;

    private PlayerCantrips cantripData;

    private Vec3 liftPosition;

    private boolean syncGrimoire = true;

    private boolean syncRote = true;

    private boolean needsChronoExhaustion = false;

    private boolean hadWizardSight = false;

    private int globalParticleColorOverride = -1;

    private boolean modifierPressed = false;

    public PlayerMagic() {
        this.castingResource = new Mana();
        this.rememberedPoints = new ArrayList();
        this.rememberedLooks = new ArrayList();
        this.chronoData = new ChronoAnchorData();
        this.affinityDepths = new HashMap();
        for (Affinity aff : Affinity.values()) {
            this.affinityDepths.put(aff, 0.0F);
        }
        this.armor_repair_accumulation = new HashMap();
        this.riftInventory = new SimpleContainer(54);
        this.grimoireInventory = new SpellInventory(16);
        this.cantripData = new PlayerCantrips();
        this.roteInventory = new SpellInventory(16);
        for (int i = 0; i < this.roteInventory.m_6643_(); i++) {
            this.roteInventory.m_6836_(i, new ItemStack(ItemInit.SPELL.get()));
        }
    }

    @Override
    public boolean isMagicUnlocked() {
        return this.getMagicLevel() > 0;
    }

    @Override
    public void unlockMagic() {
        if (this.getMagicLevel() == 0) {
            this.setMagicLevel(null, 1);
        }
    }

    @Override
    public void copyFrom(IPlayerMagic other) {
        this.setCastingResourceType(other.getCastingResource().getRegistryName());
        this.getCastingResource().copyFrom(other.getCastingResource());
        this.setTeleportSalt(other.getTeleportSalt());
        this.setMagicXP(other.getMagicXP());
        this.setMagicLevel(null, other.getMagicLevel());
        this.setPortalCooldown(0);
        this.setAirCasts(0);
        for (int i = 0; i < other.getGrimoireInventory().m_6643_(); i++) {
            this.grimoireInventory.m_6836_(i, other.getGrimoireInventory().m_8020_(i));
        }
        for (int i = 0; i < other.getRiftInventory().getContainerSize(); i++) {
            this.riftInventory.setItem(i, other.getRiftInventory().getItem(i));
        }
        for (int i = 0; i < other.getRoteInventory().m_6643_(); i++) {
            this.roteInventory.m_6836_(i, other.getRoteInventory().m_8020_(i));
        }
        for (Affinity aff : Affinity.values()) {
            this.setAffinityDepth(aff, other.getAffinityDepth(aff));
        }
        this.cantripData = new PlayerCantrips();
        this.cantripData.readFromNBT(((PlayerCantrips) other.getCantripData()).writeToNBT(false));
        this.getCastingResource().copyFrom(this.castingResource);
        this.forceSync();
    }

    @Override
    public void tick(Player attachedPlayer) {
        if (this.needsChronoExhaustion) {
            try {
                attachedPlayer.m_7292_(new MobEffectInstance(EffectInit.CHRONO_EXHAUSTION.get(), 6000));
            } catch (Exception var7) {
                ManaAndArtifice.LOGGER.error("Failed to apply chrono exhaustion to " + attachedPlayer != null ? attachedPlayer.getName().getString() : "unknown player");
                ManaAndArtifice.LOGGER.catching(var7);
            } finally {
                this.needsChronoExhaustion = false;
            }
        }
        if (this.is_teleporting) {
            this.teleport_count++;
            if (this.teleport_count >= this.teleport_duration && attachedPlayer instanceof ServerPlayer spe) {
                this.is_teleporting = false;
                this.forceSync();
                TeleportHelper.performDelayedTeleport(spe, this.teleport_destination, this.teleport_rotation, this.teleport_dimension, this);
            }
        } else if (this.teleport_count > 0) {
            this.teleport_count--;
        }
        if (this.portal_cooldown > 0) {
            this.portal_cooldown--;
        }
        if (this.ember_cooldown > 0) {
            this.ember_cooldown--;
        }
        if (attachedPlayer.m_20096_()) {
            this.setAirCasts(0);
            this.setAirJumps(0);
        }
        if (this.getCastingResource().getAmount() != this.getCastingResource().getMaxAmount()) {
            if (attachedPlayer.isCreative()) {
                this.getCastingResource().setAmount(this.getCastingResource().getMaxAmount());
            } else if (this.canRegenerate(attachedPlayer)) {
                attachedPlayer.getFoodData().addExhaustion(3.3333333E-4F);
                float regen_pct_per_tick = 1.0F / (float) this.getCastingResource().getRegenerationRate(attachedPlayer);
                MobEffectInstance shieldEffect = attachedPlayer.m_21124_(MobEffects.DAMAGE_RESISTANCE);
                if (shieldEffect != null) {
                    regen_pct_per_tick *= 1.0F - 0.05F * (float) (shieldEffect.getAmplifier() + 1);
                }
                float restored_mana = this.getCastingResource().getMaxAmount() * regen_pct_per_tick;
                if (this.getCastingResource().hungerAffectsRegenRate() && !(attachedPlayer.getFoodData().getSaturationLevel() > 0.0F)) {
                    this.getCastingResource().restore(restored_mana);
                } else {
                    this.getCastingResource().restore(restored_mana * 3.0F);
                }
            }
            this.tick_count++;
            if (this.getCastingResource().getAmount() == this.getCastingResource().getMaxAmount()) {
                this.forceSync();
            }
        }
    }

    @Override
    public boolean needsSync() {
        return this.tick_count >= 10 || this.getCastingResource().getSyncStatus() == SyncStatus.IMMEDIATE;
    }

    @Override
    public boolean shouldSyncGrimoire() {
        return this.syncGrimoire;
    }

    @Override
    public void setSyncGrimoire() {
        this.syncGrimoire = true;
    }

    @Override
    public void setSyncRote() {
        this.syncRote = true;
    }

    @Override
    public boolean shouldSyncRote() {
        return this.syncRote;
    }

    @Override
    public void clearSyncFlags() {
        this.tick_count = 0;
        this.syncGrimoire = false;
        this.syncRote = false;
        this.getCastingResource().clearSyncStatus();
        this.getCantripData().clearSync();
    }

    @Override
    public int getTeleportSalt() {
        return this.teleport_salt;
    }

    @Override
    public void resetTeleportSalt() {
        this.teleport_salt = (int) (Math.random() * 2.147483647E9);
    }

    @Override
    public void setTeleportSalt(int salt) {
        this.teleport_salt = salt;
    }

    @Override
    public void clearRememberedPoints() {
        this.rememberedPoints.clear();
        this.rememberedLooks.clear();
    }

    @Override
    public void addRememberedPoint(Vector3f point, Vector3f look) {
        this.rememberedPoints.add(point);
        this.rememberedLooks.add(look);
    }

    @Override
    public Vector3f[] getRememberedPoints() {
        Vector3f[] points = new Vector3f[this.rememberedPoints.size()];
        return (Vector3f[]) this.rememberedPoints.toArray(points);
    }

    @Override
    public int getPortalCooldown() {
        return this.portal_cooldown;
    }

    @Override
    public void setPortalCooldown(int cooldown) {
        this.portal_cooldown = cooldown;
    }

    @Override
    public boolean getIsTeleporting() {
        return this.is_teleporting;
    }

    @Override
    public void delayedTeleportTo(int ticks, Vec3 destination, Vec2 rotation, ServerLevel level) {
        this.is_teleporting = true;
        this.teleport_duration = ticks;
        this.teleport_destination = destination;
        this.teleport_rotation = rotation;
        this.teleport_dimension = level;
        this.forceSync();
    }

    @Override
    public void cancelTeleport() {
        this.is_teleporting = false;
        this.teleport_duration = 0;
        this.teleport_destination = null;
        this.teleport_rotation = null;
        this.teleport_dimension = null;
        this.forceSync();
    }

    @Override
    public void updateClientsideTeleportData(boolean teleporting, int elapsed, int total) {
        this.is_teleporting = teleporting;
        this.teleport_count = elapsed;
        this.teleport_duration = total;
    }

    @Override
    public int getTeleportElapsedTicks() {
        return this.teleport_count;
    }

    @Override
    public int getTeleportTotalTicks() {
        return this.teleport_duration;
    }

    @Override
    public Vector3f getAverageLook() {
        if (this.rememberedLooks.size() == 0) {
            return new Vector3f(0.0F, 0.0F, 0.0F);
        } else {
            float aX = 0.0F;
            float aY = 0.0F;
            float aZ = 0.0F;
            for (int i = 0; i < this.rememberedLooks.size(); i++) {
                aX += ((Vector3f) this.rememberedLooks.get(i)).x();
                aY += ((Vector3f) this.rememberedLooks.get(i)).y();
                aZ += ((Vector3f) this.rememberedLooks.get(i)).z();
            }
            aX /= (float) this.rememberedLooks.size();
            aY /= (float) this.rememberedLooks.size();
            aZ /= (float) this.rememberedLooks.size();
            return new Vector3f(aX, aY, aZ);
        }
    }

    @Override
    public Vector3f[] getRememberedLooks() {
        Vector3f[] points = new Vector3f[this.rememberedLooks.size()];
        return (Vector3f[]) this.rememberedLooks.toArray(points);
    }

    @Override
    public SimpleContainer getRiftInventory() {
        return this.riftInventory;
    }

    @Override
    public SpellInventory getGrimoireInventory() {
        return this.grimoireInventory;
    }

    @Override
    public int getMagicLevel() {
        return this.magicLevel;
    }

    @Override
    public void setMagicLevel(Player player, int level) {
        this.magicLevel = level;
        this.getCastingResource().setMaxAmountByLevel(level);
        if (player != null && player instanceof ServerPlayer) {
            CustomAdvancementTriggers.MAGIC_LEVEL.trigger((ServerPlayer) player, this.magicLevel);
        }
        this.forceSync();
    }

    private boolean canAdvanceToNextMagicLevel(IPlayerProgression progression) {
        return this.magicLevel < progression.getTier() * MAGIC_LEVELS_PER_MILESTONE;
    }

    @Override
    public int getXPForLevel(int level) {
        int baseline = 150;
        int amountPerLevel = (int) Math.pow((double) (level - 1), 2.1F);
        return baseline + amountPerLevel;
    }

    private int getXPForNextLevel() {
        return this.getXPForLevel(this.magicLevel + 1);
    }

    @Override
    public int getMagicXP() {
        return this.magicXP;
    }

    @Override
    public void setMagicXP(int amount) {
        this.magicXP = amount;
    }

    @Override
    public void addMagicXP(int amount, Player player, IPlayerProgression progression) {
        if (this.canAdvanceToNextMagicLevel(progression)) {
            MagicXPGainedEvent evt = new MagicXPGainedEvent(player, amount);
            if (!MinecraftForge.EVENT_BUS.post(evt)) {
                amount = evt.getAmount();
                this.magicXP = this.magicXP + Math.max(amount, 1);
                if (this.magicXP > this.getXPForNextLevel()) {
                    this.magicLevelUp(player, progression);
                }
            }
        }
    }

    @Override
    public float getAffinityDepth(Affinity affinity) {
        return (Float) this.affinityDepths.get(affinity);
    }

    @Override
    public void setAffinityDepth(Affinity affinity, float depth) {
        this.affinityDepths.put(affinity, depth);
        this.sortAffinities();
    }

    @Override
    public void shiftAffinity(Player player, Affinity affinity, float amount) {
        if (affinity != Affinity.UNKNOWN) {
            AffinityChangedEvent evt = new AffinityChangedEvent(player, affinity, (Float) this.affinityDepths.get(affinity), amount);
            if (player == null || !MinecraftForge.EVENT_BUS.post(evt)) {
                amount = evt.getShift();
                affinity = affinity.getShiftAffinity();
                float halfshift = amount / 2.0F;
                for (Affinity aff : Affinity.values()) {
                    if (aff == affinity) {
                        float result = (Float) this.affinityDepths.get(aff) + amount;
                        this.affinityDepths.put(aff, Math.min(result, 100.0F));
                    } else {
                        this.affinityDepths.put(aff, Math.max((Float) this.affinityDepths.get(aff) - halfshift, 0.0F));
                    }
                }
                this.sortAffinities();
            }
        }
    }

    @Override
    public void magicLevelUp(Player player, IPlayerProgression progression) {
        if (this.canAdvanceToNextMagicLevel(progression)) {
            if (EventDispatcher.DispatchPlayerLevelUp(player, this.magicLevel + 1)) {
                this.magicLevel++;
                this.getCastingResource().setMaxAmountByLevel(this.magicLevel);
                this.getCastingResource().setAmount(this.getCastingResource().getMaxAmount());
                this.magicXP = 0;
                if (!player.m_9236_().isClientSide()) {
                    player.m_9236_().playSound(null, player.m_20185_(), player.m_20186_(), player.m_20189_(), SFX.Event.Player.MAGIC_LEVEL_UP, SoundSource.PLAYERS, 1.0F, 1.0F);
                    if (player instanceof ServerPlayer) {
                        CustomAdvancementTriggers.MAGIC_LEVEL.trigger((ServerPlayer) player, this.magicLevel);
                    }
                }
                this.forceSync();
            }
        }
    }

    @Override
    public int getAirCasts() {
        return this.air_casts;
    }

    @Override
    public int getAirCastLimit(Player player, ISpellDefinition spell) {
        MutableInt airCasts = new MutableInt(2);
        if (player != null && (ItemInit.AIR_CAST_RING.get().isEquippedAndHasMana(player, 1.0F, false) || ItemInit.BLINK_PRECISION_RING.get().isEquippedAndHasMana(player, 1.0F, false))) {
            player.getCapability(PlayerProgressionProvider.PROGRESSION).ifPresent(p -> airCasts.add(1 + p.getTier()));
        }
        AirCastLimitEvent event = new AirCastLimitEvent(player, spell, this.getAirCasts(), airCasts.intValue());
        MinecraftForge.EVENT_BUS.post(event);
        return event.getLimit();
    }

    @Override
    public void setAirCasts(int casts) {
        this.air_casts = casts;
    }

    @Override
    public void incrementAirCasts(Player player, ISpellDefinition spell) {
        this.air_casts++;
        if (this.air_casts > 2 && player != null && this.air_casts < this.getAirCastLimit(player, spell)) {
            Optional<SlotResult> t = CuriosApi.getCuriosHelper().findFirstCurio(player, ItemInit.AIR_CAST_RING.get());
            if (t.isPresent()) {
                ItemInit.AIR_CAST_RING.get().consumeMana(((SlotResult) t.get()).stack(), 1.0F, player);
            }
        }
    }

    @Override
    public int getAirJumps() {
        return this.air_jumps;
    }

    @Override
    public void incrementAirJumps(Player source) {
        this.air_jumps++;
    }

    @Override
    public void setAirJumps(int jumps) {
        this.air_jumps = jumps;
    }

    @Override
    public ChronoAnchorData getChronoAnchorData() {
        return this.chronoData;
    }

    @Override
    public Vec3 getLiftPosition() {
        return this.liftPosition;
    }

    @Override
    public void setLiftPosition(Vec3 pos) {
        this.liftPosition = pos;
    }

    @Override
    public boolean didAllowFlying() {
        return this.didAllowFlight;
    }

    @Override
    public void setDidAllowFlying(boolean flight) {
        this.didAllowFlight = flight;
    }

    private void sortAffinities() {
        List<Entry<Affinity, Float>> list = new LinkedList(this.affinityDepths.entrySet());
        Collections.sort(list, new Comparator<Entry<Affinity, Float>>() {

            public int compare(Entry<Affinity, Float> o1, Entry<Affinity, Float> o2) {
                return ((Float) o2.getValue()).compareTo((Float) o1.getValue());
            }
        });
        HashMap<Affinity, Float> temp = new LinkedHashMap();
        for (Entry<Affinity, Float> aa : list) {
            temp.put((Affinity) aa.getKey(), (Float) aa.getValue());
        }
        this.affinityDepths = temp;
    }

    @Override
    public Map<Affinity, Float> getSortedAffinityDepths() {
        return (Map<Affinity, Float>) this.affinityDepths.clone();
    }

    @Override
    public void forceSync() {
        this.forceSync(0);
    }

    @Override
    public void forceSync(int forceFlags) {
        if ((forceFlags & 1) == 1) {
            this.getCastingResource().setNeedsSync();
        }
        if ((forceFlags & 2) == 2) {
            this.syncGrimoire = true;
        }
        if ((forceFlags & 4) == 4) {
            this.syncRote = true;
        }
        this.tick_count = 10;
    }

    @Override
    public boolean canRegenerate(Player player) {
        return this.getCastingResource().getRegenerationRate(player) > 0;
    }

    @Override
    public void setNeedsChronoExhaustion() {
        this.needsChronoExhaustion = true;
    }

    @Override
    public SpellInventory getRoteInventory() {
        return this.roteInventory;
    }

    @Override
    public boolean getHadWizardSight() {
        return this.hadWizardSight;
    }

    @Override
    public void setHadWizardSight(boolean had) {
        this.hadWizardSight = had;
    }

    @Override
    public IPlayerCantrips getCantripData() {
        return this.cantripData;
    }

    @Override
    public int bankArmorRepair(int slot, float amount) {
        if (!this.armor_repair_accumulation.containsKey(slot)) {
            this.armor_repair_accumulation.put(slot, 0.0F);
        }
        float newAmt = (Float) this.armor_repair_accumulation.get(slot) + amount;
        int returnAmt = (int) Math.floor((double) newAmt);
        newAmt -= (float) returnAmt;
        this.armor_repair_accumulation.put(slot, newAmt);
        return returnAmt;
    }

    @Override
    public HashMap<Integer, Float> getBankedArmorRepair() {
        return this.armor_repair_accumulation;
    }

    @Override
    public void setBankedArmorRepair(HashMap<Integer, Float> data) {
        this.armor_repair_accumulation = data;
    }

    @Override
    public void setModifierPressed(boolean pressed) {
        this.modifierPressed = pressed;
    }

    @Override
    public boolean isModifierPressed() {
        return this.modifierPressed;
    }

    @Override
    public ICastingResource getCastingResource() {
        return this.castingResource;
    }

    @Override
    public void setCastingResourceType(ResourceLocation rLoc) {
        if (rLoc != null && rLoc.getPath() != "" && !this.getCastingResource().getRegistryName().equals(rLoc)) {
            Class<? extends ICastingResource> clazz = CastingResourceRegistry.Instance.getRegisteredClass(rLoc);
            try {
                this.castingResource = (ICastingResource) clazz.getConstructor().newInstance();
                this.castingResource.setMaxAmountByLevel(this.getMagicLevel());
                this.castingResource.setAmount(this.castingResource.getMaxAmount());
            } catch (Exception var4) {
                ManaAndArtifice.LOGGER.error("Failed to set casting resource type from identifier " + rLoc.toString());
                ManaAndArtifice.LOGGER.error(var4);
            }
        }
    }

    @Override
    public void validate() {
        SpellInventory spellInv = this.getRoteInventory();
        for (int i = 0; i < spellInv.getActiveSpells().length; i++) {
            ItemStack stack = spellInv.getActiveSpells()[i];
            if (stack.getItem() != ItemInit.SPELL.get()) {
                ItemStack replacement = new ItemStack(ItemInit.SPELL.get(), 1, stack.getTag());
                spellInv.m_6836_(i, replacement);
            }
        }
    }

    @Override
    public int getParticleColorOverride() {
        return this.globalParticleColorOverride;
    }

    @Override
    public void setParticleColorOverride(int color) {
        this.globalParticleColorOverride = color;
    }

    @Override
    public float getFocusDistance() {
        return this.focus_distance;
    }

    @Override
    public void setFocusDistance(float distance) {
        this.focus_distance = distance;
    }

    @Override
    public void offsetFocusDistance(float amount, float maximum) {
        this.focus_distance = Mth.clamp(this.focus_distance + amount, 0.5F, maximum);
    }

    @Override
    public int getEmberCooldown() {
        return this.ember_cooldown;
    }

    @Override
    public void setEmberCooldown(int cooldown) {
        this.ember_cooldown = cooldown;
    }
}
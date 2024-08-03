package com.mna.entities.manaweaving;

import com.mna.advancements.CustomAdvancementTriggers;
import com.mna.api.blocks.IManaweaveNotifiable;
import com.mna.api.cantrips.ICantrip;
import com.mna.api.capabilities.IPlayerMagic;
import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.api.recipes.IManaweavePattern;
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import com.mna.entities.EntityInit;
import com.mna.events.EventDispatcher;
import com.mna.items.ItemInit;
import com.mna.items.manaweaving.ItemManaweaveBottle;
import com.mna.network.ServerMessageDispatcher;
import com.mna.recipes.manaweaving.ManaweavingPattern;
import com.mna.recipes.manaweaving.ManaweavingPatternHelper;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;

public class Manaweave extends Entity {

    private static final int MAX_AGE = 200;

    private static final EntityDataAccessor<CompoundTag> PATTERNS = SynchedEntityData.defineId(Manaweave.class, EntityDataSerializers.COMPOUND_TAG);

    private static final EntityDataAccessor<Integer> CASTER_ID = SynchedEntityData.defineId(Manaweave.class, EntityDataSerializers.INT);

    private static final String NBT_ENTITY_AGE = "entity_age";

    private static final String NBT_PATTERN = "patterns";

    private static final String NBT_CASTER_ID = "caster_id";

    private static final String MANUALLY_DRAWN = "manually_drawn";

    private static final String MANA_REFUNDED = "mana_refunded";

    private static final String NBT_HAND = "hand";

    int age = 0;

    boolean isMerging = false;

    private ArrayList<IManaweavePattern> cachedPatterns;

    private InteractionHand hand;

    private boolean manuallyDrawn = false;

    private float manaRefunded = 0.0F;

    public Manaweave(EntityType<? extends Entity> entityTypeIn, Level worldIn) {
        super(entityTypeIn, worldIn);
    }

    public Manaweave(Level world) {
        this(EntityInit.MANAWEAVE_ENTITY.get(), world);
    }

    @Override
    public void tick() {
        if ((this.getPatterns() == null || this.getPatterns().size() == 0) && !this.m_9236_().isClientSide()) {
            this.m_142687_(Entity.RemovalReason.DISCARDED);
        }
        if (this.age == 0 && !this.m_9236_().isClientSide() && !this.notifyNearbyBlocksAltars()) {
            this.mergeWithNearby(this.hand);
        }
        this.age++;
        if (this.age >= 200 && !this.m_9236_().isClientSide()) {
            this.m_142687_(Entity.RemovalReason.DISCARDED);
        }
    }

    public void onRemovedFromWorld() {
        if (this.m_9236_().isClientSide()) {
            for (int i = 0; i < 50; i++) {
                this.m_9236_().addParticle(new MAParticleType(ParticleInit.BLUE_SPARKLE_VELOCITY.get()), this.m_20185_(), this.m_20186_(), this.m_20189_(), -0.25 + Math.random() * 0.5, -0.25 + Math.random() * 0.5, -0.25 + Math.random() * 0.5);
            }
        }
        super.onRemovedFromWorld();
    }

    public void onAddedToWorld() {
        super.onAddedToWorld();
        if (!this.m_9236_().isClientSide()) {
            LivingEntity caster = this.getCaster();
            if (caster != null && caster instanceof ServerPlayer player && this.getPatterns().size() == 1) {
                CustomAdvancementTriggers.DRAW_MANAWEAVE.trigger(player, ((IManaweavePattern) this.getPatterns().get(0)).getRegistryId());
            }
        }
    }

    public boolean isMerging() {
        return this.isMerging;
    }

    public void setMerging(boolean merging) {
        this.isMerging = merging;
    }

    public float getManaReturn(Player player) {
        return this.manuallyDrawn ? this.manaRefunded : 0.0F;
    }

    @Override
    public boolean isAttackable() {
        return true;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        return false;
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    public void setPattern(ResourceLocation pattern) {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt("count", 0);
        nbt.putString("index_0", pattern.toString());
        this.f_19804_.set(PATTERNS, nbt);
        this.cachedPatterns = null;
    }

    public void addPattern(ResourceLocation pattern) {
        CompoundTag nbt = this.f_19804_.get(PATTERNS).copy();
        int count = nbt.getInt("count") + 1;
        nbt.putInt("count", count);
        nbt.putString("index_" + count, pattern.toString());
        this.f_19804_.set(PATTERNS, nbt);
        this.cachedPatterns = null;
    }

    @Nullable
    public LivingEntity getCaster() {
        int id = this.f_19804_.get(CASTER_ID);
        if (id > -1) {
            Entity e = this.m_9236_().getEntity(id);
            if (e instanceof LivingEntity) {
                return (LivingEntity) e;
            }
        }
        return null;
    }

    public void setCaster(LivingEntity caster, InteractionHand hand) {
        if (caster != null) {
            this.f_19804_.set(CASTER_ID, caster.m_19879_());
            this.hand = hand;
        }
    }

    public void setManuallyDrawn() {
        this.manuallyDrawn = true;
    }

    public void setManaRefunded(int drawTicks) {
        this.manaRefunded = (float) drawTicks * 1.0F;
    }

    public void mergeWith(Manaweave other) {
        ArrayList<IManaweavePattern> otherPatterns = other.getPatterns();
        for (int i = otherPatterns.size() - 1; i >= 0; i--) {
            this.addPattern(((IManaweavePattern) otherPatterns.get(i)).getRegistryId());
        }
        this.manuallyDrawn = this.manuallyDrawn | other.manuallyDrawn;
        this.manaRefunded = this.manaRefunded + other.manaRefunded;
        other.m_142687_(Entity.RemovalReason.DISCARDED);
    }

    private void mergeWithNearby(InteractionHand hand) {
        if (this.getCaster() != null) {
            for (Entity e : this.m_9236_().getEntities(this, this.m_20191_().inflate(2.0), ex -> ex instanceof Manaweave && ((Manaweave) ex).getCaster() == this.getCaster())) {
                this.mergeWith((Manaweave) e);
            }
            if (this.getPatterns().size() > 1) {
                this.checkMergeRecipe(hand);
            }
        }
    }

    private void checkMergeRecipe(InteractionHand hand) {
        LivingEntity caster = this.getCaster();
        if (caster != null && caster instanceof Player player) {
            IPlayerMagic magic = (IPlayerMagic) player.getCapability(PlayerMagicProvider.MAGIC).orElse(null);
            if (magic != null) {
                if (!(caster.m_20280_(this) > 25.0)) {
                    ICantrip cantrip = magic.getCantripData().matchAndCastCantrip(player, hand, (List<Recipe<?>>) this.getPatterns().stream().map(r -> (Recipe) r).collect(Collectors.toList()));
                    if (cantrip != null) {
                        EventDispatcher.DispatchCantripCast(cantrip, player);
                        if (player instanceof ServerPlayer serverPlayer) {
                            ServerMessageDispatcher.sendCantripTimerMessage(cantrip.getId().toString(), cantrip.getDelay(), serverPlayer);
                            CustomAdvancementTriggers.CANTRIP_CAST.trigger(serverPlayer, cantrip.getId(), cantrip.getTier());
                        }
                        if (cantrip.getSound() != null) {
                            this.m_9236_().playSound(null, this.m_20183_(), cantrip.getSound(), SoundSource.PLAYERS, 0.5F, 1.0F);
                        }
                        this.m_142687_(Entity.RemovalReason.DISCARDED);
                    }
                }
            }
        }
    }

    @Nullable
    public ArrayList<IManaweavePattern> getPatterns() {
        if (this.cachedPatterns == null) {
            CompoundTag nbt = this.f_19804_.get(PATTERNS);
            if (nbt.contains("count")) {
                this.cachedPatterns = new ArrayList();
                int count = nbt.getInt("count");
                for (int i = 0; i <= count; i++) {
                    String key = "index_" + i;
                    if (nbt.contains(key)) {
                        String rLoc = nbt.getString(key);
                        ManaweavingPattern mwp = ManaweavingPatternHelper.GetManaweavingRecipe(this.m_9236_(), new ResourceLocation(rLoc));
                        if (mwp != null) {
                            this.cachedPatterns.add(0, mwp);
                        }
                    }
                }
            }
        }
        return this.cachedPatterns;
    }

    @Override
    public InteractionResult interactAt(Player player, Vec3 vec, InteractionHand hand) {
        if (player.m_21120_(hand).getItem() == Items.GLASS_BOTTLE && this.getPatterns().size() == 1) {
            if (!this.m_9236_().isClientSide()) {
                ItemStack captureStack = new ItemStack(ItemInit.MANAWEAVE_BOTTLE.get());
                ItemManaweaveBottle.setPattern((IManaweavePattern) this.getPatterns().get(0), captureStack);
                player.m_21120_(hand).shrink(1);
                if (!player.addItem(captureStack)) {
                    player.m_19983_(captureStack);
                }
                this.m_142687_(Entity.RemovalReason.DISCARDED);
            }
            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.FAIL;
        }
    }

    @Override
    protected void defineSynchedData() {
        this.f_19804_.define(PATTERNS, new CompoundTag());
        this.f_19804_.define(CASTER_ID, -1);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        if (compound.contains("entity_age")) {
            this.age = compound.getInt("entity_age");
        }
        if (compound.contains("patterns")) {
            this.f_19804_.set(PATTERNS, (CompoundTag) compound.get("patterns"));
        }
        if (compound.contains("caster_id")) {
            this.f_19804_.set(CASTER_ID, compound.getInt("caster_id"));
        }
        if (compound.contains("hand")) {
            this.hand = InteractionHand.values()[compound.getInt("hand")];
        }
        if (compound.contains("manually_drawn")) {
            this.manuallyDrawn = compound.getBoolean("manually_drawn");
        }
        if (compound.contains("mana_refunded")) {
            this.manaRefunded = compound.getFloat("mana_refunded");
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        compound.putInt("entity_age", this.age);
        compound.put("patterns", this.f_19804_.get(PATTERNS));
        compound.putInt("caster_id", this.f_19804_.get(CASTER_ID));
        compound.putInt("hand", this.hand != null ? this.hand.ordinal() : 0);
        compound.putBoolean("manually_drawn", this.manuallyDrawn);
        compound.putFloat("mana_refunded", this.manaRefunded);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public int getAge() {
        return this.age;
    }

    private boolean notifyNearbyBlocksAltars() {
        ArrayList<IManaweavePattern> p = this.getPatterns();
        if (p != null && p.size() == 1) {
            LivingEntity caster = this.getCaster();
            BlockPos me = this.m_20183_();
            int radius = 3;
            for (int i = -radius; i <= radius; i++) {
                for (int j = -radius; j <= radius; j++) {
                    for (int k = -radius; k <= radius; k++) {
                        BlockPos test = me.offset(i, j, k);
                        BlockState state = this.m_9236_().getBlockState(test);
                        if (state.m_60734_() instanceof IManaweaveNotifiable notifiable && notifiable.notify(this.m_9236_(), test, state, p, caster)) {
                            this.m_142687_(Entity.RemovalReason.DISCARDED);
                            return true;
                        }
                        BlockEntity be = this.m_9236_().getBlockEntity(test);
                        if (be != null && be instanceof IManaweaveNotifiable notifiable && notifiable.notify(this.m_9236_(), test, state, p, caster)) {
                            this.m_142687_(Entity.RemovalReason.DISCARDED);
                            return true;
                        }
                    }
                }
            }
            return false;
        } else {
            return false;
        }
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        if (key == PATTERNS) {
            this.cachedPatterns = null;
        }
        super.onSyncedDataUpdated(key);
    }

    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return new ArrayList();
    }

    @Override
    public void setItemSlot(EquipmentSlot slotIn, ItemStack stack) {
    }
}
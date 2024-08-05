package com.mna.blocks.tileentities.wizard_lab;

import com.mna.ManaAndArtifice;
import com.mna.api.affinity.Affinity;
import com.mna.api.blocks.tile.TileEntityWithInventory;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public abstract class WizardLabTile extends TileEntityWithInventory implements GeoBlockEntity {

    private boolean active;

    private int stateFlags;

    private UUID crafterID;

    private Player crafterReference;

    private boolean tickContinuing;

    private int activeTicks;

    private boolean syncToggle;

    private final AnimatableInstanceCache animCache = GeckoLibUtil.createInstanceCache(this);

    public WizardLabTile(BlockEntityType<?> type, BlockPos pos, BlockState state, int inventorySize) {
        super(type, pos, state, inventorySize);
    }

    public static <T extends WizardLabTile> void Tick(Level level, BlockPos pos, BlockState state, T tile) {
        tile.tick();
    }

    protected void tick() {
        if (this.active) {
            if (!this.m_58904_().isClientSide()) {
                if (!this.canContinue()) {
                    this.setInactive();
                    return;
                }
                if (this.canActiveTick()) {
                    if (!this.tickContinuing) {
                        this.tickContinuing = true;
                        this.syncAndSave();
                    }
                    this.tickActiveLogic();
                    this.activeTicks++;
                    if (this.getPctComplete() >= 1.0F) {
                        this.onComplete();
                        if (this.getPctComplete() >= 1.0F) {
                            this.setInactive();
                        }
                    }
                } else if (this.tickContinuing) {
                    this.tickContinuing = false;
                    this.syncAndSave();
                }
                if (this.m_58904_().getGameTime() % 20L == 0L) {
                    this.syncAndSave();
                }
            } else if (this.tickContinuing) {
                this.activeTicks++;
                if (this.getPctComplete() >= 1.0F) {
                    this.onComplete();
                    if (this.getPctComplete() >= 1.0F) {
                        this.setInactive();
                    }
                }
            }
        }
    }

    protected boolean canActiveTick() {
        return true;
    }

    protected abstract boolean canContinue();

    public abstract float getPctComplete();

    protected void onComplete() {
    }

    public HashMap<Affinity, WizardLabTile.PowerStatus> powerRequirementStatus() {
        return new HashMap();
    }

    protected void onCraftStart(Player crafter) {
    }

    public int getXPCost(Player crafter) {
        return 0;
    }

    @Nullable
    public SoundEvent getAmbientSound() {
        return null;
    }

    protected final int getActiveTicks() {
        return this.activeTicks;
    }

    @Nullable
    public final Player getCrafter() {
        if (this.crafterReference == null && this.crafterID != null) {
            this.crafterReference = this.f_58857_.m_46003_(this.crafterID);
        }
        return this.crafterReference;
    }

    @Nullable
    public final UUID getCrafterID() {
        return this.crafterID;
    }

    protected WizardLabTile.PowerStatus fromPowerCode(float requested, float obtained) {
        if (obtained < 0.0F) {
            return WizardLabTile.PowerStatus.NO_CONDUIT;
        } else if (obtained == 0.0F) {
            return WizardLabTile.PowerStatus.NO_POWER;
        } else {
            return obtained < requested ? WizardLabTile.PowerStatus.PARTIAL : WizardLabTile.PowerStatus.SUPPLIED;
        }
    }

    protected void tickActiveLogic() {
    }

    protected boolean needsPower() {
        return true;
    }

    protected abstract boolean canActivate(Player var1);

    public boolean canStart(Player player) {
        if (this.isActive()) {
            return false;
        } else {
            return player.totalExperience < this.getXPCost(player) && !player.isCreative() ? false : this.canActivate(player);
        }
    }

    public final boolean isActive() {
        return this.active;
    }

    public final boolean setActive(Player crafter) {
        if (crafter.getGameProfile() == null || crafter.getGameProfile().getId() == null) {
            return false;
        } else if (!this.canActivate(crafter)) {
            return false;
        } else {
            if (!this.m_58904_().isClientSide()) {
                this.active = true;
                this.tickContinuing = true;
                this.activeTicks = 0;
                this.crafterReference = crafter;
                this.crafterID = crafter.getGameProfile().getId();
                crafter.giveExperiencePoints(-this.getXPCost(crafter));
                this.onCraftStart(crafter);
                this.syncAndSave();
            }
            return true;
        }
    }

    protected boolean shouldLoopingSoundPlay(String ID) {
        return this.isActive() && this.f_58857_.getBlockEntity(this.f_58858_) == this;
    }

    protected final boolean reactivate() {
        if (this.crafterID == null) {
            return false;
        } else {
            this.active = true;
            this.activeTicks = 0;
            this.onCraftStart(null);
            this.syncAndSave();
            return true;
        }
    }

    public final void setInactive() {
        this.onDeactivated();
        this.active = false;
        this.activeTicks = 0;
        this.tickContinuing = false;
        this.crafterID = null;
        this.syncAndSave();
    }

    protected void onDeactivated() {
    }

    public final int getStateFlags() {
        return this.stateFlags;
    }

    public final void setStateFlag(int mask) {
        this.stateFlags |= mask;
    }

    public final void clearStateFlag(int mask) {
        this.stateFlags &= ~mask;
    }

    public final boolean isFlagSet(int mask) {
        return (this.stateFlags & mask) != 0;
    }

    public final void syncAndSave() {
        if (!this.m_58904_().isClientSide()) {
            this.m_6596_();
            this.m_58904_().sendBlockUpdated(this.m_58899_(), this.m_58900_(), this.m_58900_(), 3);
        }
    }

    protected abstract List<Integer> getSyncedInventorySlots();

    protected final ListTag saveSyncedInvData() {
        ListTag inventorySync = new ListTag();
        for (Integer i : this.getSyncedInventorySlots()) {
            CompoundTag sub = new CompoundTag();
            this.m_8020_(i).save(sub);
            inventorySync.add(sub);
        }
        return inventorySync;
    }

    protected final void loadSyncedInvData(ListTag inventorySync) {
        List<Integer> slots = this.getSyncedInventorySlots();
        if (inventorySync.size() != slots.size()) {
            ManaAndArtifice.LOGGER.error("Failed to parse synced inventory data for tile " + this.m_58903_().toString() + " at " + this.m_58899_().m_123344_());
        } else {
            for (int i = 0; i < slots.size(); i++) {
                int slot = (Integer) slots.get(i);
                CompoundTag item = inventorySync.getCompound(i);
                this.setItem(slot, ItemStack.of(item));
            }
        }
    }

    protected CompoundTag getMeta() {
        return new CompoundTag();
    }

    protected void loadMeta(CompoundTag tag) {
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag base = super.m_5995_();
        base.put("invSync", this.saveSyncedInvData());
        base.putBoolean("toggle", this.syncToggle);
        base.putBoolean("active", this.active);
        base.putBoolean("ticking", this.tickContinuing);
        base.putInt("activeTicks", this.activeTicks);
        base.putInt("flags", this.stateFlags);
        base.put("meta", this.getMeta());
        return base;
    }

    public void handleUpdateTag(CompoundTag tag) {
        super.handleUpdateTag(tag);
        boolean wasActive = this.active;
        this.loadSyncedInvData(tag.getList("invSync", 10));
        this.active = tag.getBoolean("active");
        this.tickContinuing = tag.getBoolean("ticking");
        this.stateFlags = tag.getInt("flags");
        this.activeTicks = tag.getInt("activeTicks");
        this.loadMeta(tag.getCompound("meta"));
        if (this.active && !wasActive && this.getAmbientSound() != null && this.shouldLoopingSoundPlay("active_loop")) {
            ManaAndArtifice.instance.proxy.playLoopingSound(this.getAmbientSound(), "active loop", this::shouldLoopingSoundPlay, 1.0F);
        }
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        CompoundTag tag = pkt.getTag();
        boolean wasActive = this.active;
        this.loadSyncedInvData(tag.getList("invSync", 10));
        this.active = tag.getBoolean("active");
        this.tickContinuing = tag.getBoolean("ticking");
        this.stateFlags = tag.getInt("flags");
        this.activeTicks = tag.getInt("activeTicks");
        this.loadMeta(tag.getCompound("meta"));
        if (this.active && !wasActive && this.getAmbientSound() != null && this.shouldLoopingSoundPlay("active_loop")) {
            ManaAndArtifice.instance.proxy.playLoopingSound(this.getAmbientSound(), "active loop", this::shouldLoopingSoundPlay, 1.0F, this.f_58858_);
        }
    }

    protected void writePowerConsumeStatus(WizardLabTile.PowerStatus value, CompoundTag tag) {
        this.writePowerConsumeStatus(value, tag, "powerStatus");
    }

    protected void writePowerConsumeStatus(WizardLabTile.PowerStatus value, CompoundTag tag, String key) {
        tag.putInt(key, value.ordinal());
    }

    protected WizardLabTile.PowerStatus readPowerConsumeStatus(CompoundTag tag) {
        return this.readPowerConsumeStatus(tag, "powerStatus");
    }

    protected WizardLabTile.PowerStatus readPowerConsumeStatus(CompoundTag tag, String key) {
        return tag.contains(key, 3) ? WizardLabTile.PowerStatus.values()[tag.getInt(key)] : WizardLabTile.PowerStatus.NOT_REQUESTING;
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        super.setItem(index, stack);
        if (this.getSyncedInventorySlots().contains(index)) {
            this.syncAndSave();
        }
    }

    @Override
    public ItemStack removeItem(int index, int count) {
        ItemStack output = super.removeItem(index, count);
        if (this.getSyncedInventorySlots().contains(index)) {
            this.syncAndSave();
        }
        return output;
    }

    @Override
    public void clearContent() {
        super.clearContent();
        this.syncAndSave();
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        compound.putInt("flags", this.stateFlags);
        compound.putBoolean("active", this.active);
        compound.putBoolean("ticking", this.tickContinuing);
        compound.putInt("activeTicks", this.activeTicks);
        compound.put("meta", this.getMeta());
        if (this.crafterID != null) {
            compound.putString("crafter", this.crafterID.toString());
        }
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        this.stateFlags = compound.getInt("flags");
        this.active = compound.getBoolean("active");
        if (compound.contains("ticking")) {
            this.tickContinuing = compound.getBoolean("ticking");
        }
        if (compound.contains("activeTicks")) {
            this.activeTicks = compound.getInt("activeTicks");
        }
        if (compound.contains("meta")) {
            this.loadMeta(compound.getCompound("meta"));
        }
        if (compound.contains("crafter")) {
            try {
                this.crafterID = UUID.fromString(compound.getString("crafter"));
            } catch (Exception var3) {
            }
        }
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.animCache;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar registrar) {
        registrar.add(new AnimationController<>(this, state -> this.handleAnimState(state)));
    }

    protected PlayState handleAnimState(AnimationState<? extends WizardLabTile> state) {
        return PlayState.STOP;
    }

    public boolean clickMenuButton(Player player, int button) {
        if (button == 0 && this.canActivate(player)) {
            this.setActive(player);
            return true;
        } else {
            return false;
        }
    }

    public static enum PowerStatus {

        SUPPLIED(true), PARTIAL(true), NO_POWER(false), NO_CONDUIT(false), NOT_REQUESTING(false);

        boolean allow_machine_operation = false;

        private PowerStatus(boolean allow_machine_operation) {
            this.allow_machine_operation = allow_machine_operation;
        }

        public boolean allowMachineOperation() {
            return this.allow_machine_operation;
        }
    }
}
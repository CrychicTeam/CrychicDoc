package net.minecraft.world.entity.animal.horse;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

public abstract class AbstractChestedHorse extends AbstractHorse {

    private static final EntityDataAccessor<Boolean> DATA_ID_CHEST = SynchedEntityData.defineId(AbstractChestedHorse.class, EntityDataSerializers.BOOLEAN);

    public static final int INV_CHEST_COUNT = 15;

    protected AbstractChestedHorse(EntityType<? extends AbstractChestedHorse> entityTypeExtendsAbstractChestedHorse0, Level level1) {
        super(entityTypeExtendsAbstractChestedHorse0, level1);
        this.f_30523_ = false;
    }

    @Override
    protected void randomizeAttributes(RandomSource randomSource0) {
        this.m_21051_(Attributes.MAX_HEALTH).setBaseValue((double) m_271722_(randomSource0::m_188503_));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.f_19804_.define(DATA_ID_CHEST, false);
    }

    public static AttributeSupplier.Builder createBaseChestedHorseAttributes() {
        return m_30627_().add(Attributes.MOVEMENT_SPEED, 0.175F).add(Attributes.JUMP_STRENGTH, 0.5);
    }

    public boolean hasChest() {
        return this.f_19804_.get(DATA_ID_CHEST);
    }

    public void setChest(boolean boolean0) {
        this.f_19804_.set(DATA_ID_CHEST, boolean0);
    }

    @Override
    protected int getInventorySize() {
        return this.hasChest() ? 17 : super.getInventorySize();
    }

    @Override
    public double getPassengersRidingOffset() {
        return super.m_6048_() - 0.25;
    }

    @Override
    protected void dropEquipment() {
        super.dropEquipment();
        if (this.hasChest()) {
            if (!this.m_9236_().isClientSide) {
                this.m_19998_(Blocks.CHEST);
            }
            this.setChest(false);
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag0) {
        super.addAdditionalSaveData(compoundTag0);
        compoundTag0.putBoolean("ChestedHorse", this.hasChest());
        if (this.hasChest()) {
            ListTag $$1 = new ListTag();
            for (int $$2 = 2; $$2 < this.f_30520_.getContainerSize(); $$2++) {
                ItemStack $$3 = this.f_30520_.getItem($$2);
                if (!$$3.isEmpty()) {
                    CompoundTag $$4 = new CompoundTag();
                    $$4.putByte("Slot", (byte) $$2);
                    $$3.save($$4);
                    $$1.add($$4);
                }
            }
            compoundTag0.put("Items", $$1);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag0) {
        super.readAdditionalSaveData(compoundTag0);
        this.setChest(compoundTag0.getBoolean("ChestedHorse"));
        this.m_30625_();
        if (this.hasChest()) {
            ListTag $$1 = compoundTag0.getList("Items", 10);
            for (int $$2 = 0; $$2 < $$1.size(); $$2++) {
                CompoundTag $$3 = $$1.getCompound($$2);
                int $$4 = $$3.getByte("Slot") & 255;
                if ($$4 >= 2 && $$4 < this.f_30520_.getContainerSize()) {
                    this.f_30520_.setItem($$4, ItemStack.of($$3));
                }
            }
        }
        this.m_7493_();
    }

    @Override
    public SlotAccess getSlot(int int0) {
        return int0 == 499 ? new SlotAccess() {

            @Override
            public ItemStack get() {
                return AbstractChestedHorse.this.hasChest() ? new ItemStack(Items.CHEST) : ItemStack.EMPTY;
            }

            @Override
            public boolean set(ItemStack p_149485_) {
                if (p_149485_.isEmpty()) {
                    if (AbstractChestedHorse.this.hasChest()) {
                        AbstractChestedHorse.this.setChest(false);
                        AbstractChestedHorse.this.m_30625_();
                    }
                    return true;
                } else if (p_149485_.is(Items.CHEST)) {
                    if (!AbstractChestedHorse.this.hasChest()) {
                        AbstractChestedHorse.this.setChest(true);
                        AbstractChestedHorse.this.m_30625_();
                    }
                    return true;
                } else {
                    return false;
                }
            }
        } : super.getSlot(int0);
    }

    @Override
    public InteractionResult mobInteract(Player player0, InteractionHand interactionHand1) {
        boolean $$2 = !this.m_6162_() && this.m_30614_() && player0.isSecondaryUseActive();
        if (!this.m_20160_() && !$$2) {
            ItemStack $$3 = player0.m_21120_(interactionHand1);
            if (!$$3.isEmpty()) {
                if (this.m_6898_($$3)) {
                    return this.m_30580_(player0, $$3);
                }
                if (!this.m_30614_()) {
                    this.m_7564_();
                    return InteractionResult.sidedSuccess(this.m_9236_().isClientSide);
                }
                if (!this.hasChest() && $$3.is(Items.CHEST)) {
                    this.equipChest(player0, $$3);
                    return InteractionResult.sidedSuccess(this.m_9236_().isClientSide);
                }
            }
            return super.mobInteract(player0, interactionHand1);
        } else {
            return super.mobInteract(player0, interactionHand1);
        }
    }

    private void equipChest(Player player0, ItemStack itemStack1) {
        this.setChest(true);
        this.playChestEquipsSound();
        if (!player0.getAbilities().instabuild) {
            itemStack1.shrink(1);
        }
        this.m_30625_();
    }

    protected void playChestEquipsSound() {
        this.m_5496_(SoundEvents.DONKEY_CHEST, 1.0F, (this.f_19796_.nextFloat() - this.f_19796_.nextFloat()) * 0.2F + 1.0F);
    }

    public int getInventoryColumns() {
        return 5;
    }
}
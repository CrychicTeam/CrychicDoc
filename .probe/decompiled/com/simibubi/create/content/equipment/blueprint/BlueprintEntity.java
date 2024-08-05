package com.simibubi.create.content.equipment.blueprint;

import com.simibubi.create.AllEntityTypes;
import com.simibubi.create.AllItems;
import com.simibubi.create.Create;
import com.simibubi.create.content.logistics.filter.FilterItemStack;
import com.simibubi.create.content.schematics.requirement.ISpecialEntityItemRequirement;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import com.simibubi.create.foundation.networking.ISyncPersistentData;
import com.simibubi.create.foundation.utility.Couple;
import com.simibubi.create.foundation.utility.IInteractionChecker;
import com.simibubi.create.foundation.utility.VecHelper;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.TransientCraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DiodeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.network.NetworkHooks;
import org.apache.commons.lang3.Validate;

public class BlueprintEntity extends HangingEntity implements IEntityAdditionalSpawnData, ISpecialEntityItemRequirement, ISyncPersistentData, IInteractionChecker {

    protected int size;

    protected Direction verticalOrientation;

    private Map<Integer, BlueprintEntity.BlueprintSection> sectionCache = new HashMap();

    public BlueprintEntity(EntityType<?> p_i50221_1_, Level p_i50221_2_) {
        super((EntityType<? extends HangingEntity>) p_i50221_1_, p_i50221_2_);
        this.size = 1;
    }

    public BlueprintEntity(Level world, BlockPos pos, Direction facing, Direction verticalOrientation) {
        super((EntityType<? extends HangingEntity>) AllEntityTypes.CRAFTING_BLUEPRINT.get(), world, pos);
        for (int size = 3; size > 0; size--) {
            this.size = size;
            this.updateFacingWithBoundingBox(facing, verticalOrientation);
            if (this.survives()) {
                break;
            }
        }
    }

    public static EntityType.Builder<?> build(EntityType.Builder<?> builder) {
        return builder;
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag p_213281_1_) {
        p_213281_1_.putByte("Facing", (byte) this.f_31699_.get3DDataValue());
        p_213281_1_.putByte("Orientation", (byte) this.verticalOrientation.get3DDataValue());
        p_213281_1_.putInt("Size", this.size);
        super.addAdditionalSaveData(p_213281_1_);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag p_70037_1_) {
        if (p_70037_1_.contains("Facing", 99)) {
            this.f_31699_ = Direction.from3DDataValue(p_70037_1_.getByte("Facing"));
            this.verticalOrientation = Direction.from3DDataValue(p_70037_1_.getByte("Orientation"));
            this.size = p_70037_1_.getInt("Size");
        } else {
            this.f_31699_ = Direction.SOUTH;
            this.verticalOrientation = Direction.DOWN;
            this.size = 1;
        }
        super.readAdditionalSaveData(p_70037_1_);
        this.updateFacingWithBoundingBox(this.f_31699_, this.verticalOrientation);
    }

    protected void updateFacingWithBoundingBox(Direction facing, Direction verticalOrientation) {
        Validate.notNull(facing);
        this.f_31699_ = facing;
        this.verticalOrientation = verticalOrientation;
        if (facing.getAxis().isHorizontal()) {
            this.m_146926_(0.0F);
            this.m_146922_((float) (this.f_31699_.get2DDataValue() * 90));
        } else {
            this.m_146926_((float) (-90 * facing.getAxisDirection().getStep()));
            this.m_146922_(verticalOrientation.getAxis().isHorizontal() ? 180.0F + verticalOrientation.toYRot() : 0.0F);
        }
        this.f_19860_ = this.m_146909_();
        this.f_19859_ = this.m_146908_();
        this.recalculateBoundingBox();
    }

    @Override
    protected float getEyeHeight(Pose p_213316_1_, EntityDimensions p_213316_2_) {
        return 0.0F;
    }

    @Override
    protected void recalculateBoundingBox() {
        if (this.f_31699_ != null) {
            if (this.verticalOrientation != null) {
                Vec3 pos = Vec3.atLowerCornerOf(this.m_31748_()).add(0.5, 0.5, 0.5).subtract(Vec3.atLowerCornerOf(this.f_31699_.getNormal()).scale(0.46875));
                double d1 = pos.x;
                double d2 = pos.y;
                double d3 = pos.z;
                this.m_20343_(d1, d2, d3);
                Direction.Axis axis = this.f_31699_.getAxis();
                if (this.size == 2) {
                    pos = pos.add(Vec3.atLowerCornerOf(axis.isHorizontal() ? this.f_31699_.getCounterClockWise().getNormal() : this.verticalOrientation.getClockWise().getNormal()).scale(0.5)).add(Vec3.atLowerCornerOf(axis.isHorizontal() ? Direction.UP.getNormal() : (this.f_31699_ == Direction.UP ? this.verticalOrientation.getNormal() : this.verticalOrientation.getOpposite().getNormal())).scale(0.5));
                }
                d1 = pos.x;
                d2 = pos.y;
                d3 = pos.z;
                double d4 = (double) this.getWidth();
                double d5 = (double) this.getHeight();
                double d6 = (double) this.getWidth();
                Direction.Axis direction$axis = this.f_31699_.getAxis();
                switch(direction$axis) {
                    case X:
                        d4 = 1.0;
                        break;
                    case Y:
                        d5 = 1.0;
                        break;
                    case Z:
                        d6 = 1.0;
                }
                d4 /= 32.0;
                d5 /= 32.0;
                d6 /= 32.0;
                this.m_20011_(new AABB(d1 - d4, d2 - d5, d3 - d6, d1 + d4, d2 + d5, d3 + d6));
            }
        }
    }

    @Override
    public boolean survives() {
        if (!this.m_9236_().m_45786_(this)) {
            return false;
        } else {
            int i = Math.max(1, this.getWidth() / 16);
            int j = Math.max(1, this.getHeight() / 16);
            BlockPos blockpos = this.f_31698_.relative(this.f_31699_.getOpposite());
            Direction upDirection = this.f_31699_.getAxis().isHorizontal() ? Direction.UP : (this.f_31699_ == Direction.UP ? this.verticalOrientation : this.verticalOrientation.getOpposite());
            Direction newDirection = this.f_31699_.getAxis().isVertical() ? this.verticalOrientation.getClockWise() : this.f_31699_.getCounterClockWise();
            BlockPos.MutableBlockPos blockpos$mutable = new BlockPos.MutableBlockPos();
            for (int k = 0; k < i; k++) {
                for (int l = 0; l < j; l++) {
                    int i1 = (i - 1) / -2;
                    int j1 = (j - 1) / -2;
                    blockpos$mutable.set(blockpos).move(newDirection, k + i1).move(upDirection, l + j1);
                    BlockState blockstate = this.m_9236_().getBlockState(blockpos$mutable);
                    if (!Block.canSupportCenter(this.m_9236_(), blockpos$mutable, this.f_31699_) && !blockstate.m_280296_() && !DiodeBlock.isDiode(blockstate)) {
                        return false;
                    }
                }
            }
            return this.m_9236_().getEntities(this, this.m_20191_(), f_31697_).isEmpty();
        }
    }

    @Override
    public int getWidth() {
        return 16 * this.size;
    }

    @Override
    public int getHeight() {
        return 16 * this.size;
    }

    @Override
    public boolean skipAttackInteraction(Entity source) {
        if (source instanceof Player && !this.m_9236_().isClientSide) {
            Player player = (Player) source;
            double attrib = player.m_21051_(ForgeMod.BLOCK_REACH.get()).getValue() + (double) (player.isCreative() ? 0.0F : -0.5F);
            Vec3 eyePos = source.getEyePosition(1.0F);
            Vec3 look = source.getViewVector(1.0F);
            Vec3 target = eyePos.add(look.scale(attrib));
            Optional<Vec3> rayTrace = this.m_20191_().clip(eyePos, target);
            if (!rayTrace.isPresent()) {
                return super.skipAttackInteraction(source);
            } else {
                Vec3 hitVec = (Vec3) rayTrace.get();
                BlueprintEntity.BlueprintSection sectionAt = this.getSectionAt(hitVec.subtract(this.m_20182_()));
                ItemStackHandler items = sectionAt.getItems();
                if (items.getStackInSlot(9).isEmpty()) {
                    return super.skipAttackInteraction(source);
                } else {
                    for (int i = 0; i < items.getSlots(); i++) {
                        items.setStackInSlot(i, ItemStack.EMPTY);
                    }
                    sectionAt.save(items);
                    return true;
                }
            }
        } else {
            return super.skipAttackInteraction(source);
        }
    }

    @Override
    public void dropItem(@Nullable Entity p_110128_1_) {
        if (this.m_9236_().getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
            this.m_5496_(SoundEvents.PAINTING_BREAK, 1.0F, 1.0F);
            if (p_110128_1_ instanceof Player playerentity && playerentity.getAbilities().instabuild) {
                return;
            }
            this.m_19983_(AllItems.CRAFTING_BLUEPRINT.asStack());
        }
    }

    public ItemStack getPickedResult(HitResult target) {
        return AllItems.CRAFTING_BLUEPRINT.asStack();
    }

    @Override
    public ItemRequirement getRequiredItems() {
        return new ItemRequirement(ItemRequirement.ItemUseType.CONSUME, (Item) AllItems.CRAFTING_BLUEPRINT.get());
    }

    @Override
    public void playPlacementSound() {
        this.m_5496_(SoundEvents.PAINTING_PLACE, 1.0F, 1.0F);
    }

    @Override
    public void moveTo(double p_70012_1_, double p_70012_3_, double p_70012_5_, float p_70012_7_, float p_70012_8_) {
        this.m_6034_(p_70012_1_, p_70012_3_, p_70012_5_);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void lerpTo(double p_180426_1_, double p_180426_3_, double p_180426_5_, float p_180426_7_, float p_180426_8_, int p_180426_9_, boolean p_180426_10_) {
        BlockPos blockpos = this.f_31698_.offset(BlockPos.containing(p_180426_1_ - this.m_20185_(), p_180426_3_ - this.m_20186_(), p_180426_5_ - this.m_20189_()));
        this.m_6034_((double) blockpos.m_123341_(), (double) blockpos.m_123342_(), (double) blockpos.m_123343_());
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {
        CompoundTag compound = new CompoundTag();
        this.addAdditionalSaveData(compound);
        buffer.writeNbt(compound);
        buffer.writeNbt(this.getPersistentData());
    }

    @Override
    public void readSpawnData(FriendlyByteBuf additionalData) {
        this.readAdditionalSaveData(additionalData.readNbt());
        this.getPersistentData().merge(additionalData.readNbt());
    }

    @Override
    public InteractionResult interactAt(Player player, Vec3 vec, InteractionHand hand) {
        if (player instanceof FakePlayer) {
            return InteractionResult.PASS;
        } else {
            boolean holdingWrench = AllItems.WRENCH.isIn(player.m_21120_(hand));
            BlueprintEntity.BlueprintSection section = this.getSectionAt(vec);
            ItemStackHandler items = section.getItems();
            if (!holdingWrench && !this.m_9236_().isClientSide && !items.getStackInSlot(9).isEmpty()) {
                IItemHandlerModifiable playerInv = new InvWrapper(player.getInventory());
                boolean firstPass = true;
                int amountCrafted = 0;
                ForgeHooks.setCraftingPlayer(player);
                Optional<CraftingRecipe> recipe = Optional.empty();
                do {
                    Map<Integer, ItemStack> stacksTaken = new HashMap();
                    Map<Integer, ItemStack> craftingGrid = new HashMap();
                    boolean success = true;
                    label76: for (int i = 0; i < 9; i++) {
                        FilterItemStack requestedItem = FilterItemStack.of(items.getStackInSlot(i));
                        if (requestedItem.isEmpty()) {
                            craftingGrid.put(i, ItemStack.EMPTY);
                        } else {
                            for (int slot = 0; slot < playerInv.getSlots(); slot++) {
                                if (requestedItem.test(this.m_9236_(), playerInv.getStackInSlot(slot))) {
                                    ItemStack currentItem = playerInv.extractItem(slot, 1, false);
                                    if (stacksTaken.containsKey(slot)) {
                                        ((ItemStack) stacksTaken.get(slot)).grow(1);
                                    } else {
                                        stacksTaken.put(slot, currentItem.copy());
                                    }
                                    craftingGrid.put(i, currentItem);
                                    continue label76;
                                }
                            }
                            success = false;
                            break;
                        }
                    }
                    if (success) {
                        CraftingContainer craftingInventory = new BlueprintEntity.BlueprintCraftingInventory(craftingGrid);
                        if (!recipe.isPresent()) {
                            recipe = this.m_9236_().getRecipeManager().getRecipeFor(RecipeType.CRAFTING, craftingInventory, this.m_9236_());
                        }
                        ItemStack result = (ItemStack) recipe.filter(r -> r.m_5818_(craftingInventory, this.m_9236_())).map(r -> r.m_5874_(craftingInventory, this.m_9236_().registryAccess())).orElse(ItemStack.EMPTY);
                        if (result.isEmpty()) {
                            success = false;
                        } else if (result.getCount() + amountCrafted > 64) {
                            success = false;
                        } else {
                            amountCrafted += result.getCount();
                            result.onCraftedBy(player.m_9236_(), player, 1);
                            ForgeEventFactory.firePlayerCraftingEvent(player, result, craftingInventory);
                            NonNullList<ItemStack> nonnulllist = this.m_9236_().getRecipeManager().getRemainingItemsFor(RecipeType.CRAFTING, craftingInventory, this.m_9236_());
                            if (firstPass) {
                                this.m_9236_().playSound(null, player.m_20183_(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.2F, 1.0F + Create.RANDOM.nextFloat());
                            }
                            player.getInventory().placeItemBackInInventory(result);
                            for (ItemStack itemStack : nonnulllist) {
                                player.getInventory().placeItemBackInInventory(itemStack);
                            }
                            firstPass = false;
                        }
                    }
                    if (!success) {
                        for (Entry<Integer, ItemStack> entry : stacksTaken.entrySet()) {
                            playerInv.insertItem((Integer) entry.getKey(), (ItemStack) entry.getValue(), false);
                        }
                        break;
                    }
                } while (player.m_6144_());
                ForgeHooks.setCraftingPlayer(null);
                return InteractionResult.SUCCESS;
            } else {
                int ix = section.index;
                if (!this.m_9236_().isClientSide && player instanceof ServerPlayer) {
                    NetworkHooks.openScreen((ServerPlayer) player, section, buf -> {
                        buf.writeVarInt(this.m_19879_());
                        buf.writeVarInt(i);
                    });
                }
                return InteractionResult.SUCCESS;
            }
        }
    }

    public BlueprintEntity.BlueprintSection getSectionAt(Vec3 vec) {
        int index = 0;
        if (this.size > 1) {
            vec = VecHelper.rotate(vec, (double) this.m_146908_(), Direction.Axis.Y);
            vec = VecHelper.rotate(vec, (double) (-this.m_146909_()), Direction.Axis.X);
            vec = vec.add(0.5, 0.5, 0.0);
            if (this.size == 3) {
                vec = vec.add(1.0, 1.0, 0.0);
            }
            int x = Mth.clamp(Mth.floor(vec.x), 0, this.size - 1);
            int y = Mth.clamp(Mth.floor(vec.y), 0, this.size - 1);
            index = x + y * this.size;
        }
        BlueprintEntity.BlueprintSection section = this.getSection(index);
        return section;
    }

    public CompoundTag getOrCreateRecipeCompound() {
        CompoundTag persistentData = this.getPersistentData();
        if (!persistentData.contains("Recipes")) {
            persistentData.put("Recipes", new CompoundTag());
        }
        return persistentData.getCompound("Recipes");
    }

    public BlueprintEntity.BlueprintSection getSection(int index) {
        return (BlueprintEntity.BlueprintSection) this.sectionCache.computeIfAbsent(index, i -> new BlueprintEntity.BlueprintSection(i));
    }

    @Override
    public void onPersistentDataUpdated() {
        this.sectionCache.clear();
    }

    @Override
    public boolean canPlayerUse(Player player) {
        AABB box = this.m_20191_();
        double dx = 0.0;
        if (box.minX > player.m_20185_()) {
            dx = box.minX - player.m_20185_();
        } else if (player.m_20185_() > box.maxX) {
            dx = player.m_20185_() - box.maxX;
        }
        double dy = 0.0;
        if (box.minY > player.m_20186_()) {
            dy = box.minY - player.m_20186_();
        } else if (player.m_20186_() > box.maxY) {
            dy = player.m_20186_() - box.maxY;
        }
        double dz = 0.0;
        if (box.minZ > player.m_20189_()) {
            dz = box.minZ - player.m_20189_();
        } else if (player.m_20189_() > box.maxZ) {
            dz = player.m_20189_() - box.maxZ;
        }
        return dx * dx + dy * dy + dz * dz <= 64.0;
    }

    static class BlueprintCraftingInventory extends TransientCraftingContainer {

        private static final AbstractContainerMenu dummyContainer = new AbstractContainerMenu(null, -1) {

            @Override
            public boolean stillValid(Player playerIn) {
                return false;
            }

            @Override
            public ItemStack quickMoveStack(Player p_38941_, int p_38942_) {
                return ItemStack.EMPTY;
            }
        };

        public BlueprintCraftingInventory(Map<Integer, ItemStack> items) {
            super(dummyContainer, 3, 3);
            for (int y = 0; y < 3; y++) {
                for (int x = 0; x < 3; x++) {
                    ItemStack stack = (ItemStack) items.get(y * 3 + x);
                    this.m_6836_(y * 3 + x, stack == null ? ItemStack.EMPTY : stack.copy());
                }
            }
        }
    }

    class BlueprintSection implements MenuProvider, IInteractionChecker {

        int index;

        Couple<ItemStack> cachedDisplayItems;

        public boolean inferredIcon = false;

        public BlueprintSection(int index) {
            this.index = index;
        }

        public Couple<ItemStack> getDisplayItems() {
            if (this.cachedDisplayItems != null) {
                return this.cachedDisplayItems;
            } else {
                ItemStackHandler items = this.getItems();
                return this.cachedDisplayItems = Couple.create(items.getStackInSlot(9), items.getStackInSlot(10));
            }
        }

        public ItemStackHandler getItems() {
            ItemStackHandler newInv = new ItemStackHandler(11);
            CompoundTag list = BlueprintEntity.this.getOrCreateRecipeCompound();
            CompoundTag invNBT = list.getCompound(this.index + "");
            this.inferredIcon = list.getBoolean("InferredIcon");
            if (!invNBT.isEmpty()) {
                newInv.deserializeNBT(invNBT);
            }
            return newInv;
        }

        public void save(ItemStackHandler inventory) {
            CompoundTag list = BlueprintEntity.this.getOrCreateRecipeCompound();
            list.put(this.index + "", inventory.serializeNBT());
            list.putBoolean("InferredIcon", this.inferredIcon);
            this.cachedDisplayItems = null;
            if (!BlueprintEntity.this.m_9236_().isClientSide) {
                BlueprintEntity.this.syncPersistentDataWithTracking(BlueprintEntity.this);
            }
        }

        public boolean isEntityAlive() {
            return BlueprintEntity.this.m_6084_();
        }

        public Level getBlueprintWorld() {
            return BlueprintEntity.this.m_9236_();
        }

        @Override
        public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
            return BlueprintMenu.create(id, inv, this);
        }

        @Override
        public Component getDisplayName() {
            return ((BlueprintItem) AllItems.CRAFTING_BLUEPRINT.get()).m_41466_();
        }

        @Override
        public boolean canPlayerUse(Player player) {
            return BlueprintEntity.this.canPlayerUse(player);
        }
    }
}
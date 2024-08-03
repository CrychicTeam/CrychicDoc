package net.blay09.mods.waystones.block.entity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.WeakHashMap;
import java.util.Map.Entry;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.container.ImplementedContainer;
import net.blay09.mods.balm.api.menu.BalmMenuProvider;
import net.blay09.mods.waystones.api.IMutableWaystone;
import net.blay09.mods.waystones.api.IWaystone;
import net.blay09.mods.waystones.api.WaystoneOrigin;
import net.blay09.mods.waystones.api.WaystonesAPI;
import net.blay09.mods.waystones.block.WarpPlateBlock;
import net.blay09.mods.waystones.config.WaystonesConfig;
import net.blay09.mods.waystones.core.PlayerWaystoneManager;
import net.blay09.mods.waystones.core.WarpMode;
import net.blay09.mods.waystones.core.Waystone;
import net.blay09.mods.waystones.core.WaystoneSyncManager;
import net.blay09.mods.waystones.core.WaystoneTypes;
import net.blay09.mods.waystones.menu.WarpPlateContainer;
import net.blay09.mods.waystones.recipe.ModRecipes;
import net.blay09.mods.waystones.recipe.WarpPlateRecipe;
import net.blay09.mods.waystones.tag.ModItemTags;
import net.blay09.mods.waystones.worldgen.namegen.NameGenerationMode;
import net.blay09.mods.waystones.worldgen.namegen.NameGenerator;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WarpPlateBlockEntity extends WaystoneBlockEntityBase implements ImplementedContainer {

    private static final Logger logger = LoggerFactory.getLogger(WarpPlateBlockEntity.class);

    private final WeakHashMap<Entity, Integer> ticksPassedPerEntity = new WeakHashMap();

    private final Random random = new Random();

    private final ContainerData dataAccess;

    private final NonNullList<ItemStack> items = NonNullList.withSize(5, ItemStack.EMPTY);

    private int attunementTicks;

    private boolean readyForAttunement;

    private boolean completedFirstAttunement;

    private int lastAttunementSlot;

    public WarpPlateBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.warpPlate.get(), blockPos, blockState);
        this.dataAccess = new ContainerData() {

            @Override
            public int get(int i) {
                return WarpPlateBlockEntity.this.attunementTicks;
            }

            @Override
            public void set(int i, int j) {
                WarpPlateBlockEntity.this.attunementTicks = j;
            }

            @Override
            public int getCount() {
                return 1;
            }
        };
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return this.items;
    }

    @Override
    public ItemStack removeItem(int slot, int count) {
        return !this.completedFirstAttunement ? ItemStack.EMPTY : ImplementedContainer.super.removeItem(slot, count);
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        return !this.completedFirstAttunement ? ItemStack.EMPTY : ImplementedContainer.super.removeItemNoUpdate(slot);
    }

    @Override
    public void initializeFromExisting(ServerLevelAccessor world, Waystone existingWaystone, ItemStack itemStack) {
        super.initializeFromExisting(world, existingWaystone, itemStack);
        CompoundTag tag = itemStack.getTag();
        this.completedFirstAttunement = tag != null && tag.getBoolean("CompletedFirstAttunement");
        if (!this.completedFirstAttunement) {
            this.initializeInventory(world);
        }
    }

    @Override
    public void initializeWaystone(ServerLevelAccessor world, @Nullable LivingEntity player, WaystoneOrigin origin) {
        super.initializeWaystone(world, player, origin);
        IWaystone waystone = this.getWaystone();
        if (waystone instanceof IMutableWaystone) {
            String name = NameGenerator.get(world.m_7654_()).getName(waystone, world.m_213780_(), NameGenerationMode.RANDOM_ONLY);
            ((IMutableWaystone) waystone).setName(name);
        }
        WaystoneSyncManager.sendWaystoneUpdateToAll(world.m_7654_(), waystone);
        this.initializeInventory(world);
    }

    private void initializeInventory(ServerLevelAccessor levelAccessor) {
        WarpPlateRecipe initializingRecipe = (WarpPlateRecipe) levelAccessor.getLevel().getRecipeManager().<Container, WarpPlateRecipe>getAllRecipesFor(ModRecipes.warpPlateRecipeType).stream().filter(recipe -> recipe.getId().getNamespace().equals("waystones") && recipe.getId().getPath().equals("attuned_shard")).findFirst().orElse(null);
        if (initializingRecipe == null) {
            logger.error("Failed to find Warp Plate recipe for initial attunement");
            this.completedFirstAttunement = true;
        } else {
            for (int i = 0; i < 5; i++) {
                Ingredient ingredient = initializingRecipe.getIngredients().get(i);
                ItemStack[] ingredientItems = ingredient.getItems();
                ItemStack ingredientItem = ingredientItems.length > 0 ? ingredientItems[0] : ItemStack.EMPTY;
                this.m_6836_(i, ingredientItem.copy());
            }
        }
    }

    @Override
    protected ResourceLocation getWaystoneType() {
        return WaystoneTypes.WARP_PLATE;
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        ContainerHelper.saveAllItems(tag, this.items);
        tag.putBoolean("ReadyForAttunement", this.readyForAttunement);
        tag.putBoolean("CompletedFirstAttunement", this.completedFirstAttunement);
        tag.putInt("LastAttunementSlot", this.lastAttunementSlot);
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        ContainerHelper.loadAllItems(compound, this.items);
        this.readyForAttunement = compound.getBoolean("ReadyForAttunement");
        this.completedFirstAttunement = compound.getBoolean("CompletedFirstAttunement");
        this.lastAttunementSlot = compound.getInt("LastAttunementSlot");
    }

    public BalmMenuProvider getMenuProvider() {
        return new BalmMenuProvider() {

            @Override
            public Component getDisplayName() {
                return Component.translatable("container.waystones.warp_plate");
            }

            @Override
            public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player player) {
                return new WarpPlateContainer(i, WarpPlateBlockEntity.this, WarpPlateBlockEntity.this.dataAccess, playerInventory);
            }

            @Override
            public void writeScreenOpeningData(ServerPlayer player, FriendlyByteBuf buf) {
                buf.writeBlockPos(WarpPlateBlockEntity.this.f_58858_);
            }
        };
    }

    @Override
    public MenuProvider getSettingsMenuProvider() {
        return null;
    }

    public void onEntityCollision(Entity entity) {
        Integer ticksPassed = (Integer) this.ticksPassedPerEntity.putIfAbsent(entity, 0);
        if (ticksPassed == null || ticksPassed != -1) {
            WarpPlateBlock.WarpPlateStatus status = (WarpPlateBlock.WarpPlateStatus) this.getTargetWaystone().filter(IWaystone::isValid).map(it -> WarpPlateBlock.WarpPlateStatus.ACTIVE).orElse(WarpPlateBlock.WarpPlateStatus.INVALID);
            this.f_58857_.setBlock(this.f_58858_, (BlockState) ((BlockState) this.m_58900_().m_61124_(WarpPlateBlock.ACTIVE, true)).m_61124_(WarpPlateBlock.STATUS, status), 3);
        }
    }

    private boolean isEntityOnWarpPlate(Entity entity) {
        return entity.getX() >= (double) this.f_58858_.m_123341_() && entity.getX() < (double) (this.f_58858_.m_123341_() + 1) && entity.getY() >= (double) this.f_58858_.m_123342_() && entity.getY() < (double) (this.f_58858_.m_123342_() + 1) && entity.getZ() >= (double) this.f_58858_.m_123343_() && entity.getZ() < (double) (this.f_58858_.m_123343_() + 1);
    }

    public void serverTick() {
        WarpPlateRecipe recipe = this.trySelectRecipe();
        if (recipe != null) {
            this.attunementTicks++;
            if (this.attunementTicks >= this.getMaxAttunementTicks()) {
                this.attunementTicks = 0;
                ItemStack attunedShard = recipe.assemble(this, RegistryAccess.EMPTY);
                WaystonesAPI.setBoundWaystone(attunedShard, this.getWaystone());
                ItemStack centerStack = this.m_8020_(0);
                if (centerStack.getCount() > 1) {
                    centerStack = centerStack.copyWithCount(centerStack.getCount() - 1);
                    if (!Minecraft.getInstance().player.m_150109_().add(centerStack)) {
                        Minecraft.getInstance().player.m_36176_(centerStack, false);
                    }
                }
                this.m_6836_(0, attunedShard);
                for (int i = 1; i <= 4; i++) {
                    this.m_8020_(i).shrink(1);
                }
                this.completedFirstAttunement = true;
            }
        } else {
            this.attunementTicks = 0;
        }
        if (this.m_58900_().m_61143_(WarpPlateBlock.STATUS) != WarpPlateBlock.WarpPlateStatus.IDLE) {
            AABB boundsAbove = new AABB((double) this.f_58858_.m_123341_(), (double) this.f_58858_.m_123342_(), (double) this.f_58858_.m_123343_(), (double) (this.f_58858_.m_123341_() + 1), (double) (this.f_58858_.m_123342_() + 1), (double) (this.f_58858_.m_123343_() + 1));
            List<Entity> entities = this.f_58857_.getEntities((Entity) null, boundsAbove, EntitySelector.ENTITY_STILL_ALIVE);
            if (entities.isEmpty()) {
                this.f_58857_.setBlock(this.f_58858_, (BlockState) ((BlockState) this.m_58900_().m_61124_(WarpPlateBlock.ACTIVE, false)).m_61124_(WarpPlateBlock.STATUS, WarpPlateBlock.WarpPlateStatus.IDLE), 3);
                this.ticksPassedPerEntity.clear();
            }
        }
        int useTime = this.getWarpPlateUseTime();
        Iterator<Entry<Entity, Integer>> iterator = this.ticksPassedPerEntity.entrySet().iterator();
        while (iterator.hasNext()) {
            Entry<Entity, Integer> entry = (Entry<Entity, Integer>) iterator.next();
            Entity entity = (Entity) entry.getKey();
            Integer ticksPassed = (Integer) entry.getValue();
            if (entity.isAlive() && this.isEntityOnWarpPlate(entity)) {
                if (ticksPassed > useTime) {
                    ItemStack targetAttunementStack = this.getTargetAttunementStack();
                    IWaystone targetWaystone = (IWaystone) WaystonesAPI.getBoundWaystone(targetAttunementStack).orElse(null);
                    if (targetWaystone != null && targetWaystone.isValid()) {
                        this.teleportToWarpPlate(entity, targetWaystone, targetAttunementStack);
                    }
                    if (entity instanceof Player) {
                        if (targetWaystone == null) {
                            MutableComponent chatComponent = Component.translatable("chat.waystones.warp_plate_has_no_target");
                            chatComponent.withStyle(ChatFormatting.DARK_RED);
                            ((Player) entity).displayClientMessage(chatComponent, true);
                        } else if (!targetWaystone.isValid()) {
                            MutableComponent chatComponent = Component.translatable("chat.waystones.warp_plate_has_invalid_target");
                            chatComponent.withStyle(ChatFormatting.DARK_RED);
                            ((Player) entity).displayClientMessage(chatComponent, true);
                        }
                    }
                    iterator.remove();
                } else if (ticksPassed != -1) {
                    entry.setValue(ticksPassed + 1);
                }
            } else {
                iterator.remove();
            }
        }
    }

    private int getWarpPlateUseTime() {
        float useTimeMultiplier = 1.0F;
        for (int i = 0; i < this.m_6643_(); i++) {
            ItemStack itemStack = this.m_8020_(i);
            if (itemStack.getItem() == Items.AMETHYST_SHARD) {
                useTimeMultiplier -= 0.016F * (float) itemStack.getCount();
            } else if (itemStack.getItem() == Items.SLIME_BALL) {
                useTimeMultiplier += 0.016F * (float) itemStack.getCount();
            }
        }
        int configuredUseTime = WaystonesConfig.getActive().cooldowns.warpPlateUseTime;
        return Mth.clamp((int) ((float) configuredUseTime * useTimeMultiplier), 1, configuredUseTime * 2);
    }

    private void teleportToWarpPlate(Entity entity, IWaystone targetWaystone, ItemStack targetAttunementStack) {
        WaystonesAPI.createDefaultTeleportContext(entity, targetWaystone, WarpMode.WARP_PLATE, this.getWaystone()).flatMap(ctx -> {
            ctx.setWarpItem(targetAttunementStack);
            ctx.setConsumesWarpItem(targetAttunementStack.is(ModItemTags.SINGLE_USE_WARP_SHARDS));
            return PlayerWaystoneManager.tryTeleport(ctx);
        }).ifRight(PlayerWaystoneManager.informRejectedTeleport(entity)).ifLeft(entities -> entities.forEach(this::applyWarpPlateEffects)).left();
    }

    private void applyWarpPlateEffects(Entity entity) {
        int fireSeconds = 0;
        int poisonSeconds = 0;
        int blindSeconds = 0;
        int featherFallSeconds = 0;
        int fireResistanceSeconds = 0;
        int witherSeconds = 0;
        int potency = 1;
        List<ItemStack> curativeItems = new ArrayList();
        for (int i = 0; i < this.m_6643_(); i++) {
            ItemStack itemStack = this.m_8020_(i);
            if (itemStack.getItem() == Items.BLAZE_POWDER) {
                fireSeconds += itemStack.getCount();
            } else if (itemStack.getItem() == Items.POISONOUS_POTATO) {
                poisonSeconds += itemStack.getCount();
            } else if (itemStack.getItem() == Items.INK_SAC) {
                blindSeconds += itemStack.getCount();
            } else if (itemStack.getItem() == Items.MILK_BUCKET || itemStack.getItem() == Items.HONEY_BLOCK) {
                curativeItems.add(itemStack);
            } else if (itemStack.getItem() == Items.DIAMOND) {
                potency = Math.min(4, potency + itemStack.getCount());
            } else if (itemStack.getItem() == Items.FEATHER) {
                featherFallSeconds = Math.min(8, featherFallSeconds + itemStack.getCount());
            } else if (itemStack.getItem() == Items.MAGMA_CREAM) {
                fireResistanceSeconds = Math.min(8, fireResistanceSeconds + itemStack.getCount());
            } else if (itemStack.getItem() == Items.WITHER_ROSE) {
                witherSeconds += itemStack.getCount();
            }
        }
        if (entity instanceof LivingEntity) {
            if (fireSeconds > 0) {
                entity.setSecondsOnFire(fireSeconds);
            }
            if (poisonSeconds > 0) {
                ((LivingEntity) entity).addEffect(new MobEffectInstance(MobEffects.POISON, poisonSeconds * 20, potency));
            }
            if (blindSeconds > 0) {
                ((LivingEntity) entity).addEffect(new MobEffectInstance(MobEffects.BLINDNESS, blindSeconds * 20, potency));
            }
            if (featherFallSeconds > 0) {
                ((LivingEntity) entity).addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, featherFallSeconds * 20, potency));
            }
            if (fireResistanceSeconds > 0) {
                ((LivingEntity) entity).addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, fireResistanceSeconds * 20, potency));
            }
            if (witherSeconds > 0) {
                ((LivingEntity) entity).addEffect(new MobEffectInstance(MobEffects.WITHER, witherSeconds * 20, potency));
            }
            for (ItemStack curativeItem : curativeItems) {
                Balm.getHooks().curePotionEffects((LivingEntity) entity, curativeItem);
            }
        }
    }

    @Nullable
    private WarpPlateRecipe trySelectRecipe() {
        if (this.readyForAttunement && this.f_58857_ != null) {
            return this.m_8020_(0).getCount() > 1 ? null : (WarpPlateRecipe) this.f_58857_.getRecipeManager().getRecipeFor(ModRecipes.warpPlateRecipeType, this, this.f_58857_).orElse(null);
        } else {
            return null;
        }
    }

    public ItemStack getTargetAttunementStack() {
        boolean shouldRoundRobin = false;
        boolean shouldPrioritizeSingleUseShards = false;
        List<ItemStack> attunedShards = new ArrayList();
        for (int i = 0; i < this.m_6643_(); i++) {
            ItemStack itemStack = this.m_8020_(i);
            if (itemStack.is(ModItemTags.WARP_SHARDS)) {
                IWaystone waystoneAttunedTo = (IWaystone) WaystonesAPI.getBoundWaystone(itemStack).orElse(null);
                if (waystoneAttunedTo != null && !waystoneAttunedTo.getWaystoneUid().equals(this.getWaystone().getWaystoneUid())) {
                    attunedShards.add(itemStack);
                }
            } else if (itemStack.getItem() == Items.QUARTZ) {
                shouldRoundRobin = true;
            } else if (itemStack.getItem() == Items.SPIDER_EYE) {
                shouldPrioritizeSingleUseShards = true;
            }
        }
        if (shouldPrioritizeSingleUseShards && attunedShards.stream().anyMatch(stack -> stack.is(ModItemTags.SINGLE_USE_WARP_SHARDS))) {
            attunedShards.removeIf(stack -> !stack.is(ModItemTags.SINGLE_USE_WARP_SHARDS));
        }
        if (!attunedShards.isEmpty()) {
            this.lastAttunementSlot = (this.lastAttunementSlot + 1) % attunedShards.size();
            return shouldRoundRobin ? (ItemStack) attunedShards.get(this.lastAttunementSlot) : (ItemStack) attunedShards.get(this.random.nextInt(attunedShards.size()));
        } else {
            return ItemStack.EMPTY;
        }
    }

    public Optional<IWaystone> getTargetWaystone() {
        return WaystonesAPI.getBoundWaystone(this.getTargetAttunementStack());
    }

    public int getMaxAttunementTicks() {
        return 30;
    }

    public void markReadyForAttunement() {
        this.readyForAttunement = true;
    }

    public void markEntityForCooldown(Entity entity) {
        this.ticksPassedPerEntity.put(entity, -1);
    }

    public boolean isCompletedFirstAttunement() {
        return this.completedFirstAttunement;
    }

    public ContainerData getContainerData() {
        return this.dataAccess;
    }

    @Override
    public boolean canPlaceItem(int index, ItemStack stack) {
        return index == 0 && !this.m_8020_(0).isEmpty() ? false : ImplementedContainer.super.m_7013_(index, stack);
    }
}
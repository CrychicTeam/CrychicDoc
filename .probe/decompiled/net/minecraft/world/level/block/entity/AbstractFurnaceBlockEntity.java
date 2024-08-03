package net.minecraft.world.level.block.entity;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.Object2IntMap.Entry;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.SharedConstants;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.RecipeHolder;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public abstract class AbstractFurnaceBlockEntity extends BaseContainerBlockEntity implements WorldlyContainer, RecipeHolder, StackedContentsCompatible {

    protected static final int SLOT_INPUT = 0;

    protected static final int SLOT_FUEL = 1;

    protected static final int SLOT_RESULT = 2;

    public static final int DATA_LIT_TIME = 0;

    private static final int[] SLOTS_FOR_UP = new int[] { 0 };

    private static final int[] SLOTS_FOR_DOWN = new int[] { 2, 1 };

    private static final int[] SLOTS_FOR_SIDES = new int[] { 1 };

    public static final int DATA_LIT_DURATION = 1;

    public static final int DATA_COOKING_PROGRESS = 2;

    public static final int DATA_COOKING_TOTAL_TIME = 3;

    public static final int NUM_DATA_VALUES = 4;

    public static final int BURN_TIME_STANDARD = 200;

    public static final int BURN_COOL_SPEED = 2;

    protected NonNullList<ItemStack> items = NonNullList.withSize(3, ItemStack.EMPTY);

    int litTime;

    int litDuration;

    int cookingProgress;

    int cookingTotalTime;

    protected final ContainerData dataAccess = new ContainerData() {

        @Override
        public int get(int p_58431_) {
            switch(p_58431_) {
                case 0:
                    return AbstractFurnaceBlockEntity.this.litTime;
                case 1:
                    return AbstractFurnaceBlockEntity.this.litDuration;
                case 2:
                    return AbstractFurnaceBlockEntity.this.cookingProgress;
                case 3:
                    return AbstractFurnaceBlockEntity.this.cookingTotalTime;
                default:
                    return 0;
            }
        }

        @Override
        public void set(int p_58433_, int p_58434_) {
            switch(p_58433_) {
                case 0:
                    AbstractFurnaceBlockEntity.this.litTime = p_58434_;
                    break;
                case 1:
                    AbstractFurnaceBlockEntity.this.litDuration = p_58434_;
                    break;
                case 2:
                    AbstractFurnaceBlockEntity.this.cookingProgress = p_58434_;
                    break;
                case 3:
                    AbstractFurnaceBlockEntity.this.cookingTotalTime = p_58434_;
            }
        }

        @Override
        public int getCount() {
            return 4;
        }
    };

    private final Object2IntOpenHashMap<ResourceLocation> recipesUsed = new Object2IntOpenHashMap();

    private final RecipeManager.CachedCheck<Container, ? extends AbstractCookingRecipe> quickCheck;

    protected AbstractFurnaceBlockEntity(BlockEntityType<?> blockEntityType0, BlockPos blockPos1, BlockState blockState2, RecipeType<? extends AbstractCookingRecipe> recipeTypeExtendsAbstractCookingRecipe3) {
        super(blockEntityType0, blockPos1, blockState2);
        this.quickCheck = RecipeManager.createCheck(recipeTypeExtendsAbstractCookingRecipe3);
    }

    public static Map<Item, Integer> getFuel() {
        Map<Item, Integer> $$0 = Maps.newLinkedHashMap();
        add($$0, Items.LAVA_BUCKET, 20000);
        add($$0, Blocks.COAL_BLOCK, 16000);
        add($$0, Items.BLAZE_ROD, 2400);
        add($$0, Items.COAL, 1600);
        add($$0, Items.CHARCOAL, 1600);
        add($$0, ItemTags.LOGS, 300);
        add($$0, ItemTags.BAMBOO_BLOCKS, 300);
        add($$0, ItemTags.PLANKS, 300);
        add($$0, Blocks.BAMBOO_MOSAIC, 300);
        add($$0, ItemTags.WOODEN_STAIRS, 300);
        add($$0, Blocks.BAMBOO_MOSAIC_STAIRS, 300);
        add($$0, ItemTags.WOODEN_SLABS, 150);
        add($$0, Blocks.BAMBOO_MOSAIC_SLAB, 150);
        add($$0, ItemTags.WOODEN_TRAPDOORS, 300);
        add($$0, ItemTags.WOODEN_PRESSURE_PLATES, 300);
        add($$0, ItemTags.WOODEN_FENCES, 300);
        add($$0, ItemTags.FENCE_GATES, 300);
        add($$0, Blocks.NOTE_BLOCK, 300);
        add($$0, Blocks.BOOKSHELF, 300);
        add($$0, Blocks.CHISELED_BOOKSHELF, 300);
        add($$0, Blocks.LECTERN, 300);
        add($$0, Blocks.JUKEBOX, 300);
        add($$0, Blocks.CHEST, 300);
        add($$0, Blocks.TRAPPED_CHEST, 300);
        add($$0, Blocks.CRAFTING_TABLE, 300);
        add($$0, Blocks.DAYLIGHT_DETECTOR, 300);
        add($$0, ItemTags.BANNERS, 300);
        add($$0, Items.BOW, 300);
        add($$0, Items.FISHING_ROD, 300);
        add($$0, Blocks.LADDER, 300);
        add($$0, ItemTags.SIGNS, 200);
        add($$0, ItemTags.HANGING_SIGNS, 800);
        add($$0, Items.WOODEN_SHOVEL, 200);
        add($$0, Items.WOODEN_SWORD, 200);
        add($$0, Items.WOODEN_HOE, 200);
        add($$0, Items.WOODEN_AXE, 200);
        add($$0, Items.WOODEN_PICKAXE, 200);
        add($$0, ItemTags.WOODEN_DOORS, 200);
        add($$0, ItemTags.BOATS, 1200);
        add($$0, ItemTags.WOOL, 100);
        add($$0, ItemTags.WOODEN_BUTTONS, 100);
        add($$0, Items.STICK, 100);
        add($$0, ItemTags.SAPLINGS, 100);
        add($$0, Items.BOWL, 100);
        add($$0, ItemTags.WOOL_CARPETS, 67);
        add($$0, Blocks.DRIED_KELP_BLOCK, 4001);
        add($$0, Items.CROSSBOW, 300);
        add($$0, Blocks.BAMBOO, 50);
        add($$0, Blocks.DEAD_BUSH, 100);
        add($$0, Blocks.SCAFFOLDING, 50);
        add($$0, Blocks.LOOM, 300);
        add($$0, Blocks.BARREL, 300);
        add($$0, Blocks.CARTOGRAPHY_TABLE, 300);
        add($$0, Blocks.FLETCHING_TABLE, 300);
        add($$0, Blocks.SMITHING_TABLE, 300);
        add($$0, Blocks.COMPOSTER, 300);
        add($$0, Blocks.AZALEA, 100);
        add($$0, Blocks.FLOWERING_AZALEA, 100);
        add($$0, Blocks.MANGROVE_ROOTS, 300);
        return $$0;
    }

    private static boolean isNeverAFurnaceFuel(Item item0) {
        return item0.builtInRegistryHolder().is(ItemTags.NON_FLAMMABLE_WOOD);
    }

    private static void add(Map<Item, Integer> mapItemInteger0, TagKey<Item> tagKeyItem1, int int2) {
        for (Holder<Item> $$3 : BuiltInRegistries.ITEM.m_206058_(tagKeyItem1)) {
            if (!isNeverAFurnaceFuel($$3.value())) {
                mapItemInteger0.put($$3.value(), int2);
            }
        }
    }

    private static void add(Map<Item, Integer> mapItemInteger0, ItemLike itemLike1, int int2) {
        Item $$3 = itemLike1.asItem();
        if (isNeverAFurnaceFuel($$3)) {
            if (SharedConstants.IS_RUNNING_IN_IDE) {
                throw (IllegalStateException) Util.pauseInIde(new IllegalStateException("A developer tried to explicitly make fire resistant item " + $$3.getName(null).getString() + " a furnace fuel. That will not work!"));
            }
        } else {
            mapItemInteger0.put($$3, int2);
        }
    }

    private boolean isLit() {
        return this.litTime > 0;
    }

    @Override
    public void load(CompoundTag compoundTag0) {
        super.load(compoundTag0);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(compoundTag0, this.items);
        this.litTime = compoundTag0.getShort("BurnTime");
        this.cookingProgress = compoundTag0.getShort("CookTime");
        this.cookingTotalTime = compoundTag0.getShort("CookTimeTotal");
        this.litDuration = this.getBurnDuration(this.items.get(1));
        CompoundTag $$1 = compoundTag0.getCompound("RecipesUsed");
        for (String $$2 : $$1.getAllKeys()) {
            this.recipesUsed.put(new ResourceLocation($$2), $$1.getInt($$2));
        }
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag0) {
        super.saveAdditional(compoundTag0);
        compoundTag0.putShort("BurnTime", (short) this.litTime);
        compoundTag0.putShort("CookTime", (short) this.cookingProgress);
        compoundTag0.putShort("CookTimeTotal", (short) this.cookingTotalTime);
        ContainerHelper.saveAllItems(compoundTag0, this.items);
        CompoundTag $$1 = new CompoundTag();
        this.recipesUsed.forEach((p_187449_, p_187450_) -> $$1.putInt(p_187449_.toString(), p_187450_));
        compoundTag0.put("RecipesUsed", $$1);
    }

    public static void serverTick(Level level0, BlockPos blockPos1, BlockState blockState2, AbstractFurnaceBlockEntity abstractFurnaceBlockEntity3) {
        boolean $$4 = abstractFurnaceBlockEntity3.isLit();
        boolean $$5 = false;
        if (abstractFurnaceBlockEntity3.isLit()) {
            abstractFurnaceBlockEntity3.litTime--;
        }
        ItemStack $$6 = abstractFurnaceBlockEntity3.items.get(1);
        boolean $$7 = !abstractFurnaceBlockEntity3.items.get(0).isEmpty();
        boolean $$8 = !$$6.isEmpty();
        if (abstractFurnaceBlockEntity3.isLit() || $$8 && $$7) {
            Recipe<?> $$9;
            if ($$7) {
                $$9 = (Recipe<?>) abstractFurnaceBlockEntity3.quickCheck.getRecipeFor(abstractFurnaceBlockEntity3, level0).orElse(null);
            } else {
                $$9 = null;
            }
            int $$11 = abstractFurnaceBlockEntity3.m_6893_();
            if (!abstractFurnaceBlockEntity3.isLit() && canBurn(level0.registryAccess(), $$9, abstractFurnaceBlockEntity3.items, $$11)) {
                abstractFurnaceBlockEntity3.litTime = abstractFurnaceBlockEntity3.getBurnDuration($$6);
                abstractFurnaceBlockEntity3.litDuration = abstractFurnaceBlockEntity3.litTime;
                if (abstractFurnaceBlockEntity3.isLit()) {
                    $$5 = true;
                    if ($$8) {
                        Item $$12 = $$6.getItem();
                        $$6.shrink(1);
                        if ($$6.isEmpty()) {
                            Item $$13 = $$12.getCraftingRemainingItem();
                            abstractFurnaceBlockEntity3.items.set(1, $$13 == null ? ItemStack.EMPTY : new ItemStack($$13));
                        }
                    }
                }
            }
            if (abstractFurnaceBlockEntity3.isLit() && canBurn(level0.registryAccess(), $$9, abstractFurnaceBlockEntity3.items, $$11)) {
                abstractFurnaceBlockEntity3.cookingProgress++;
                if (abstractFurnaceBlockEntity3.cookingProgress == abstractFurnaceBlockEntity3.cookingTotalTime) {
                    abstractFurnaceBlockEntity3.cookingProgress = 0;
                    abstractFurnaceBlockEntity3.cookingTotalTime = getTotalCookTime(level0, abstractFurnaceBlockEntity3);
                    if (burn(level0.registryAccess(), $$9, abstractFurnaceBlockEntity3.items, $$11)) {
                        abstractFurnaceBlockEntity3.setRecipeUsed($$9);
                    }
                    $$5 = true;
                }
            } else {
                abstractFurnaceBlockEntity3.cookingProgress = 0;
            }
        } else if (!abstractFurnaceBlockEntity3.isLit() && abstractFurnaceBlockEntity3.cookingProgress > 0) {
            abstractFurnaceBlockEntity3.cookingProgress = Mth.clamp(abstractFurnaceBlockEntity3.cookingProgress - 2, 0, abstractFurnaceBlockEntity3.cookingTotalTime);
        }
        if ($$4 != abstractFurnaceBlockEntity3.isLit()) {
            $$5 = true;
            blockState2 = (BlockState) blockState2.m_61124_(AbstractFurnaceBlock.LIT, abstractFurnaceBlockEntity3.isLit());
            level0.setBlock(blockPos1, blockState2, 3);
        }
        if ($$5) {
            m_155232_(level0, blockPos1, blockState2);
        }
    }

    private static boolean canBurn(RegistryAccess registryAccess0, @Nullable Recipe<?> recipe1, NonNullList<ItemStack> nonNullListItemStack2, int int3) {
        if (!nonNullListItemStack2.get(0).isEmpty() && recipe1 != null) {
            ItemStack $$4 = recipe1.getResultItem(registryAccess0);
            if ($$4.isEmpty()) {
                return false;
            } else {
                ItemStack $$5 = nonNullListItemStack2.get(2);
                if ($$5.isEmpty()) {
                    return true;
                } else if (!ItemStack.isSameItem($$5, $$4)) {
                    return false;
                } else {
                    return $$5.getCount() < int3 && $$5.getCount() < $$5.getMaxStackSize() ? true : $$5.getCount() < $$4.getMaxStackSize();
                }
            }
        } else {
            return false;
        }
    }

    private static boolean burn(RegistryAccess registryAccess0, @Nullable Recipe<?> recipe1, NonNullList<ItemStack> nonNullListItemStack2, int int3) {
        if (recipe1 != null && canBurn(registryAccess0, recipe1, nonNullListItemStack2, int3)) {
            ItemStack $$4 = nonNullListItemStack2.get(0);
            ItemStack $$5 = recipe1.getResultItem(registryAccess0);
            ItemStack $$6 = nonNullListItemStack2.get(2);
            if ($$6.isEmpty()) {
                nonNullListItemStack2.set(2, $$5.copy());
            } else if ($$6.is($$5.getItem())) {
                $$6.grow(1);
            }
            if ($$4.is(Blocks.WET_SPONGE.asItem()) && !nonNullListItemStack2.get(1).isEmpty() && nonNullListItemStack2.get(1).is(Items.BUCKET)) {
                nonNullListItemStack2.set(1, new ItemStack(Items.WATER_BUCKET));
            }
            $$4.shrink(1);
            return true;
        } else {
            return false;
        }
    }

    protected int getBurnDuration(ItemStack itemStack0) {
        if (itemStack0.isEmpty()) {
            return 0;
        } else {
            Item $$1 = itemStack0.getItem();
            return (Integer) getFuel().getOrDefault($$1, 0);
        }
    }

    private static int getTotalCookTime(Level level0, AbstractFurnaceBlockEntity abstractFurnaceBlockEntity1) {
        return (Integer) abstractFurnaceBlockEntity1.quickCheck.getRecipeFor(abstractFurnaceBlockEntity1, level0).map(AbstractCookingRecipe::m_43753_).orElse(200);
    }

    public static boolean isFuel(ItemStack itemStack0) {
        return getFuel().containsKey(itemStack0.getItem());
    }

    @Override
    public int[] getSlotsForFace(Direction direction0) {
        if (direction0 == Direction.DOWN) {
            return SLOTS_FOR_DOWN;
        } else {
            return direction0 == Direction.UP ? SLOTS_FOR_UP : SLOTS_FOR_SIDES;
        }
    }

    @Override
    public boolean canPlaceItemThroughFace(int int0, ItemStack itemStack1, @Nullable Direction direction2) {
        return this.canPlaceItem(int0, itemStack1);
    }

    @Override
    public boolean canTakeItemThroughFace(int int0, ItemStack itemStack1, Direction direction2) {
        return direction2 == Direction.DOWN && int0 == 1 ? itemStack1.is(Items.WATER_BUCKET) || itemStack1.is(Items.BUCKET) : true;
    }

    @Override
    public int getContainerSize() {
        return this.items.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack $$0 : this.items) {
            if (!$$0.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getItem(int int0) {
        return this.items.get(int0);
    }

    @Override
    public ItemStack removeItem(int int0, int int1) {
        return ContainerHelper.removeItem(this.items, int0, int1);
    }

    @Override
    public ItemStack removeItemNoUpdate(int int0) {
        return ContainerHelper.takeItem(this.items, int0);
    }

    @Override
    public void setItem(int int0, ItemStack itemStack1) {
        ItemStack $$2 = this.items.get(int0);
        boolean $$3 = !itemStack1.isEmpty() && ItemStack.isSameItemSameTags($$2, itemStack1);
        this.items.set(int0, itemStack1);
        if (itemStack1.getCount() > this.m_6893_()) {
            itemStack1.setCount(this.m_6893_());
        }
        if (int0 == 0 && !$$3) {
            this.cookingTotalTime = getTotalCookTime(this.f_58857_, this);
            this.cookingProgress = 0;
            this.m_6596_();
        }
    }

    @Override
    public boolean stillValid(Player player0) {
        return Container.stillValidBlockEntity(this, player0);
    }

    @Override
    public boolean canPlaceItem(int int0, ItemStack itemStack1) {
        if (int0 == 2) {
            return false;
        } else if (int0 != 1) {
            return true;
        } else {
            ItemStack $$2 = this.items.get(1);
            return isFuel(itemStack1) || itemStack1.is(Items.BUCKET) && !$$2.is(Items.BUCKET);
        }
    }

    @Override
    public void clearContent() {
        this.items.clear();
    }

    @Override
    public void setRecipeUsed(@Nullable Recipe<?> recipe0) {
        if (recipe0 != null) {
            ResourceLocation $$1 = recipe0.getId();
            this.recipesUsed.addTo($$1, 1);
        }
    }

    @Nullable
    @Override
    public Recipe<?> getRecipeUsed() {
        return null;
    }

    @Override
    public void awardUsedRecipes(Player player0, List<ItemStack> listItemStack1) {
    }

    public void awardUsedRecipesAndPopExperience(ServerPlayer serverPlayer0) {
        List<Recipe<?>> $$1 = this.getRecipesToAwardAndPopExperience(serverPlayer0.serverLevel(), serverPlayer0.m_20182_());
        serverPlayer0.awardRecipes($$1);
        for (Recipe<?> $$2 : $$1) {
            if ($$2 != null) {
                serverPlayer0.triggerRecipeCrafted($$2, this.items);
            }
        }
        this.recipesUsed.clear();
    }

    public List<Recipe<?>> getRecipesToAwardAndPopExperience(ServerLevel serverLevel0, Vec3 vec1) {
        List<Recipe<?>> $$2 = Lists.newArrayList();
        ObjectIterator var4 = this.recipesUsed.object2IntEntrySet().iterator();
        while (var4.hasNext()) {
            Entry<ResourceLocation> $$3 = (Entry<ResourceLocation>) var4.next();
            serverLevel0.getRecipeManager().byKey((ResourceLocation) $$3.getKey()).ifPresent(p_155023_ -> {
                $$2.add(p_155023_);
                createExperience(serverLevel0, vec1, $$3.getIntValue(), ((AbstractCookingRecipe) p_155023_).getExperience());
            });
        }
        return $$2;
    }

    private static void createExperience(ServerLevel serverLevel0, Vec3 vec1, int int2, float float3) {
        int $$4 = Mth.floor((float) int2 * float3);
        float $$5 = Mth.frac((float) int2 * float3);
        if ($$5 != 0.0F && Math.random() < (double) $$5) {
            $$4++;
        }
        ExperienceOrb.award(serverLevel0, vec1, $$4);
    }

    @Override
    public void fillStackedContents(StackedContents stackedContents0) {
        for (ItemStack $$1 : this.items) {
            stackedContents0.accountStack($$1);
        }
    }
}
package vectorwing.farmersdelight.common.block.entity;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.Object2IntMap.Entry;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.Nameable;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.RecipeHolder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import vectorwing.farmersdelight.common.block.CookingPotBlock;
import vectorwing.farmersdelight.common.block.entity.container.CookingPotMenu;
import vectorwing.farmersdelight.common.block.entity.inventory.CookingPotItemHandler;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;
import vectorwing.farmersdelight.common.mixin.accessor.RecipeManagerAccessor;
import vectorwing.farmersdelight.common.registry.ModBlockEntityTypes;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.registry.ModParticleTypes;
import vectorwing.farmersdelight.common.registry.ModRecipeTypes;
import vectorwing.farmersdelight.common.utility.ItemUtils;
import vectorwing.farmersdelight.common.utility.TextUtils;

public class CookingPotBlockEntity extends SyncedBlockEntity implements MenuProvider, HeatableBlockEntity, Nameable, RecipeHolder {

    public static final int MEAL_DISPLAY_SLOT = 6;

    public static final int CONTAINER_SLOT = 7;

    public static final int OUTPUT_SLOT = 8;

    public static final int INVENTORY_SIZE = 9;

    public static final Map<Item, Item> INGREDIENT_REMAINDER_OVERRIDES = Map.ofEntries(Map.entry(Items.POWDER_SNOW_BUCKET, Items.BUCKET), Map.entry(Items.AXOLOTL_BUCKET, Items.BUCKET), Map.entry(Items.COD_BUCKET, Items.BUCKET), Map.entry(Items.PUFFERFISH_BUCKET, Items.BUCKET), Map.entry(Items.SALMON_BUCKET, Items.BUCKET), Map.entry(Items.TROPICAL_FISH_BUCKET, Items.BUCKET), Map.entry(Items.SUSPICIOUS_STEW, Items.BOWL), Map.entry(Items.MUSHROOM_STEW, Items.BOWL), Map.entry(Items.RABBIT_STEW, Items.BOWL), Map.entry(Items.BEETROOT_SOUP, Items.BOWL), Map.entry(Items.POTION, Items.GLASS_BOTTLE), Map.entry(Items.SPLASH_POTION, Items.GLASS_BOTTLE), Map.entry(Items.LINGERING_POTION, Items.GLASS_BOTTLE), Map.entry(Items.EXPERIENCE_BOTTLE, Items.GLASS_BOTTLE));

    private final ItemStackHandler inventory = this.createHandler();

    private final LazyOptional<IItemHandler> inputHandler = LazyOptional.of(() -> new CookingPotItemHandler(this.inventory, Direction.UP));

    private final LazyOptional<IItemHandler> outputHandler = LazyOptional.of(() -> new CookingPotItemHandler(this.inventory, Direction.DOWN));

    private int cookTime;

    private int cookTimeTotal;

    private ItemStack mealContainerStack = ItemStack.EMPTY;

    private Component customName;

    protected final ContainerData cookingPotData = this.createIntArray();

    private final Object2IntOpenHashMap<ResourceLocation> usedRecipeTracker = new Object2IntOpenHashMap();

    private ResourceLocation lastRecipeID;

    private boolean checkNewRecipe = true;

    public CookingPotBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.COOKING_POT.get(), pos, state);
    }

    public static ItemStack getMealFromItem(ItemStack cookingPotStack) {
        if (!cookingPotStack.is(ModItems.COOKING_POT.get())) {
            return ItemStack.EMPTY;
        } else {
            CompoundTag compound = cookingPotStack.getTagElement("BlockEntityTag");
            if (compound != null) {
                CompoundTag inventoryTag = compound.getCompound("Inventory");
                if (inventoryTag.contains("Items", 9)) {
                    ItemStackHandler handler = new ItemStackHandler();
                    handler.deserializeNBT(inventoryTag);
                    return handler.getStackInSlot(6);
                }
            }
            return ItemStack.EMPTY;
        }
    }

    public static void takeServingFromItem(ItemStack cookingPotStack) {
        if (cookingPotStack.is(ModItems.COOKING_POT.get())) {
            CompoundTag compound = cookingPotStack.getTagElement("BlockEntityTag");
            if (compound != null) {
                CompoundTag inventoryTag = compound.getCompound("Inventory");
                if (inventoryTag.contains("Items", 9)) {
                    ItemStackHandler handler = new ItemStackHandler();
                    handler.deserializeNBT(inventoryTag);
                    ItemStack newMealStack = handler.getStackInSlot(6);
                    newMealStack.shrink(1);
                    compound.remove("Inventory");
                    compound.put("Inventory", handler.serializeNBT());
                }
            }
        }
    }

    public static ItemStack getContainerFromItem(ItemStack cookingPotStack) {
        if (!cookingPotStack.is(ModItems.COOKING_POT.get())) {
            return ItemStack.EMPTY;
        } else {
            CompoundTag compound = cookingPotStack.getTagElement("BlockEntityTag");
            return compound != null ? ItemStack.of(compound.getCompound("Container")) : ItemStack.EMPTY;
        }
    }

    @Override
    public void load(CompoundTag compound) {
        super.m_142466_(compound);
        this.inventory.deserializeNBT(compound.getCompound("Inventory"));
        this.cookTime = compound.getInt("CookTime");
        this.cookTimeTotal = compound.getInt("CookTimeTotal");
        this.mealContainerStack = ItemStack.of(compound.getCompound("Container"));
        if (compound.contains("CustomName", 8)) {
            this.customName = Component.Serializer.fromJson(compound.getString("CustomName"));
        }
        CompoundTag compoundRecipes = compound.getCompound("RecipesUsed");
        for (String key : compoundRecipes.getAllKeys()) {
            this.usedRecipeTracker.put(new ResourceLocation(key), compoundRecipes.getInt(key));
        }
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.m_183515_(compound);
        compound.putInt("CookTime", this.cookTime);
        compound.putInt("CookTimeTotal", this.cookTimeTotal);
        compound.put("Container", this.mealContainerStack.serializeNBT());
        if (this.customName != null) {
            compound.putString("CustomName", Component.Serializer.toJson(this.customName));
        }
        compound.put("Inventory", this.inventory.serializeNBT());
        CompoundTag compoundRecipes = new CompoundTag();
        this.usedRecipeTracker.forEach((recipeId, craftedAmount) -> compoundRecipes.putInt(recipeId.toString(), craftedAmount));
        compound.put("RecipesUsed", compoundRecipes);
    }

    private CompoundTag writeItems(CompoundTag compound) {
        super.m_183515_(compound);
        compound.put("Container", this.mealContainerStack.serializeNBT());
        compound.put("Inventory", this.inventory.serializeNBT());
        return compound;
    }

    public CompoundTag writeMeal(CompoundTag compound) {
        if (this.getMeal().isEmpty()) {
            return compound;
        } else {
            ItemStackHandler drops = new ItemStackHandler(9);
            for (int i = 0; i < 9; i++) {
                drops.setStackInSlot(i, i == 6 ? this.inventory.getStackInSlot(i) : ItemStack.EMPTY);
            }
            if (this.customName != null) {
                compound.putString("CustomName", Component.Serializer.toJson(this.customName));
            }
            compound.put("Container", this.mealContainerStack.serializeNBT());
            compound.put("Inventory", drops.serializeNBT());
            return compound;
        }
    }

    public static void cookingTick(Level level, BlockPos pos, BlockState state, CookingPotBlockEntity cookingPot) {
        boolean isHeated = cookingPot.isHeated(level, pos);
        boolean didInventoryChange = false;
        if (isHeated && cookingPot.hasInput()) {
            Optional<CookingPotRecipe> recipe = cookingPot.getMatchingRecipe(new RecipeWrapper(cookingPot.inventory));
            if (recipe.isPresent() && cookingPot.canCook((CookingPotRecipe) recipe.get())) {
                didInventoryChange = cookingPot.processCooking((CookingPotRecipe) recipe.get(), cookingPot);
            } else {
                cookingPot.cookTime = 0;
            }
        } else if (cookingPot.cookTime > 0) {
            cookingPot.cookTime = Mth.clamp(cookingPot.cookTime - 2, 0, cookingPot.cookTimeTotal);
        }
        ItemStack mealStack = cookingPot.getMeal();
        if (!mealStack.isEmpty()) {
            if (!cookingPot.doesMealHaveContainer(mealStack)) {
                cookingPot.moveMealToOutput();
                didInventoryChange = true;
            } else if (!cookingPot.inventory.getStackInSlot(7).isEmpty()) {
                cookingPot.useStoredContainersOnMeal();
                didInventoryChange = true;
            }
        }
        if (didInventoryChange) {
            cookingPot.inventoryChanged();
        }
    }

    public static void animationTick(Level level, BlockPos pos, BlockState state, CookingPotBlockEntity cookingPot) {
        if (cookingPot.isHeated(level, pos)) {
            RandomSource random = level.random;
            if (random.nextFloat() < 0.2F) {
                double x = (double) pos.m_123341_() + 0.5 + (random.nextDouble() * 0.6 - 0.3);
                double y = (double) pos.m_123342_() + 0.7;
                double z = (double) pos.m_123343_() + 0.5 + (random.nextDouble() * 0.6 - 0.3);
                level.addParticle(ParticleTypes.BUBBLE_POP, x, y, z, 0.0, 0.0, 0.0);
            }
            if (random.nextFloat() < 0.05F) {
                double x = (double) pos.m_123341_() + 0.5 + (random.nextDouble() * 0.4 - 0.2);
                double y = (double) pos.m_123342_() + 0.5;
                double z = (double) pos.m_123343_() + 0.5 + (random.nextDouble() * 0.4 - 0.2);
                double motionY = random.nextBoolean() ? 0.015 : 0.005;
                level.addParticle(ModParticleTypes.STEAM.get(), x, y, z, 0.0, motionY, 0.0);
            }
        }
    }

    private Optional<CookingPotRecipe> getMatchingRecipe(RecipeWrapper inventoryWrapper) {
        if (this.f_58857_ == null) {
            return Optional.empty();
        } else {
            if (this.lastRecipeID != null) {
                Recipe<RecipeWrapper> recipe = (Recipe<RecipeWrapper>) ((RecipeManagerAccessor) this.f_58857_.getRecipeManager()).<RecipeWrapper, CookingPotRecipe>getRecipeMap(ModRecipeTypes.COOKING.get()).get(this.lastRecipeID);
                if (recipe instanceof CookingPotRecipe) {
                    if (recipe.matches(inventoryWrapper, this.f_58857_)) {
                        return Optional.of(recipe);
                    }
                    if (ItemStack.isSameItem(recipe.getResultItem(this.f_58857_.registryAccess()), this.getMeal())) {
                        return Optional.empty();
                    }
                }
            }
            if (this.checkNewRecipe) {
                Optional<CookingPotRecipe> recipe = this.f_58857_.getRecipeManager().getRecipeFor(ModRecipeTypes.COOKING.get(), inventoryWrapper, this.f_58857_);
                if (recipe.isPresent()) {
                    ResourceLocation newRecipeID = ((CookingPotRecipe) recipe.get()).getId();
                    if (this.lastRecipeID != null && !this.lastRecipeID.equals(newRecipeID)) {
                        this.cookTime = 0;
                    }
                    this.lastRecipeID = newRecipeID;
                    return recipe;
                }
            }
            this.checkNewRecipe = false;
            return Optional.empty();
        }
    }

    public ItemStack getContainer() {
        ItemStack mealStack = this.getMeal();
        return !mealStack.isEmpty() && !this.mealContainerStack.isEmpty() ? this.mealContainerStack : mealStack.getCraftingRemainingItem();
    }

    private boolean hasInput() {
        for (int i = 0; i < 6; i++) {
            if (!this.inventory.getStackInSlot(i).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    protected boolean canCook(CookingPotRecipe recipe) {
        if (this.hasInput()) {
            ItemStack resultStack = recipe.getResultItem(this.f_58857_.registryAccess());
            if (resultStack.isEmpty()) {
                return false;
            } else {
                ItemStack storedMealStack = this.inventory.getStackInSlot(6);
                if (storedMealStack.isEmpty()) {
                    return true;
                } else if (!ItemStack.isSameItem(storedMealStack, resultStack)) {
                    return false;
                } else {
                    return storedMealStack.getCount() + resultStack.getCount() <= this.inventory.getSlotLimit(6) ? true : storedMealStack.getCount() + resultStack.getCount() <= resultStack.getMaxStackSize();
                }
            }
        } else {
            return false;
        }
    }

    private boolean processCooking(CookingPotRecipe recipe, CookingPotBlockEntity cookingPot) {
        if (this.f_58857_ == null) {
            return false;
        } else {
            this.cookTime++;
            this.cookTimeTotal = recipe.getCookTime();
            if (this.cookTime < this.cookTimeTotal) {
                return false;
            } else {
                this.cookTime = 0;
                this.mealContainerStack = recipe.getOutputContainer();
                ItemStack resultStack = recipe.getResultItem(this.f_58857_.registryAccess());
                ItemStack storedMealStack = this.inventory.getStackInSlot(6);
                if (storedMealStack.isEmpty()) {
                    this.inventory.setStackInSlot(6, resultStack.copy());
                } else if (ItemStack.isSameItem(storedMealStack, resultStack)) {
                    storedMealStack.grow(resultStack.getCount());
                }
                cookingPot.setRecipeUsed(recipe);
                for (int i = 0; i < 6; i++) {
                    ItemStack slotStack = this.inventory.getStackInSlot(i);
                    if (slotStack.hasCraftingRemainingItem()) {
                        this.ejectIngredientRemainder(slotStack.getCraftingRemainingItem());
                    } else if (INGREDIENT_REMAINDER_OVERRIDES.containsKey(slotStack.getItem())) {
                        this.ejectIngredientRemainder(((Item) INGREDIENT_REMAINDER_OVERRIDES.get(slotStack.getItem())).getDefaultInstance());
                    }
                    if (!slotStack.isEmpty()) {
                        slotStack.shrink(1);
                    }
                }
                return true;
            }
        }
    }

    protected void ejectIngredientRemainder(ItemStack remainderStack) {
        Direction direction = ((Direction) this.m_58900_().m_61143_(CookingPotBlock.FACING)).getCounterClockWise();
        double x = (double) this.f_58858_.m_123341_() + 0.5 + (double) direction.getStepX() * 0.25;
        double y = (double) this.f_58858_.m_123342_() + 0.7;
        double z = (double) this.f_58858_.m_123343_() + 0.5 + (double) direction.getStepZ() * 0.25;
        ItemUtils.spawnItemEntity(this.f_58857_, remainderStack, x, y, z, (double) ((float) direction.getStepX() * 0.08F), 0.25, (double) ((float) direction.getStepZ() * 0.08F));
    }

    @Override
    public void setRecipeUsed(@Nullable Recipe<?> recipe) {
        if (recipe != null) {
            ResourceLocation recipeID = recipe.getId();
            this.usedRecipeTracker.addTo(recipeID, 1);
        }
    }

    @Nullable
    @Override
    public Recipe<?> getRecipeUsed() {
        return null;
    }

    @Override
    public void awardUsedRecipes(Player player, List<ItemStack> items) {
        List<Recipe<?>> usedRecipes = this.getUsedRecipesAndPopExperience(player.m_9236_(), player.m_20182_());
        player.awardRecipes(usedRecipes);
        this.usedRecipeTracker.clear();
    }

    public List<Recipe<?>> getUsedRecipesAndPopExperience(Level level, Vec3 pos) {
        List<Recipe<?>> list = Lists.newArrayList();
        ObjectIterator var4 = this.usedRecipeTracker.object2IntEntrySet().iterator();
        while (var4.hasNext()) {
            Entry<ResourceLocation> entry = (Entry<ResourceLocation>) var4.next();
            level.getRecipeManager().byKey((ResourceLocation) entry.getKey()).ifPresent(recipe -> {
                list.add(recipe);
                splitAndSpawnExperience((ServerLevel) level, pos, entry.getIntValue(), ((CookingPotRecipe) recipe).getExperience());
            });
        }
        return list;
    }

    private static void splitAndSpawnExperience(ServerLevel level, Vec3 pos, int craftedAmount, float experience) {
        int expTotal = Mth.floor((float) craftedAmount * experience);
        float expFraction = Mth.frac((float) craftedAmount * experience);
        if (expFraction != 0.0F && Math.random() < (double) expFraction) {
            expTotal++;
        }
        ExperienceOrb.award(level, pos, expTotal);
    }

    public boolean isHeated() {
        return this.f_58857_ == null ? false : this.isHeated(this.f_58857_, this.f_58858_);
    }

    public ItemStackHandler getInventory() {
        return this.inventory;
    }

    public ItemStack getMeal() {
        return this.inventory.getStackInSlot(6);
    }

    public NonNullList<ItemStack> getDroppableInventory() {
        NonNullList<ItemStack> drops = NonNullList.create();
        for (int i = 0; i < 9; i++) {
            if (i != 6) {
                drops.add(this.inventory.getStackInSlot(i));
            }
        }
        return drops;
    }

    private void moveMealToOutput() {
        ItemStack mealStack = this.inventory.getStackInSlot(6);
        ItemStack outputStack = this.inventory.getStackInSlot(8);
        int mealCount = Math.min(mealStack.getCount(), mealStack.getMaxStackSize() - outputStack.getCount());
        if (outputStack.isEmpty()) {
            this.inventory.setStackInSlot(8, mealStack.split(mealCount));
        } else if (outputStack.getItem() == mealStack.getItem()) {
            mealStack.shrink(mealCount);
            outputStack.grow(mealCount);
        }
    }

    private void useStoredContainersOnMeal() {
        ItemStack mealStack = this.inventory.getStackInSlot(6);
        ItemStack containerInputStack = this.inventory.getStackInSlot(7);
        ItemStack outputStack = this.inventory.getStackInSlot(8);
        if (this.isContainerValid(containerInputStack) && outputStack.getCount() < outputStack.getMaxStackSize()) {
            int smallerStackCount = Math.min(mealStack.getCount(), containerInputStack.getCount());
            int mealCount = Math.min(smallerStackCount, mealStack.getMaxStackSize() - outputStack.getCount());
            if (outputStack.isEmpty()) {
                containerInputStack.shrink(mealCount);
                this.inventory.setStackInSlot(8, mealStack.split(mealCount));
            } else if (outputStack.getItem() == mealStack.getItem()) {
                mealStack.shrink(mealCount);
                containerInputStack.shrink(mealCount);
                outputStack.grow(mealCount);
            }
        }
    }

    public ItemStack useHeldItemOnMeal(ItemStack container) {
        if (this.isContainerValid(container) && !this.getMeal().isEmpty()) {
            container.shrink(1);
            return this.getMeal().split(1);
        } else {
            return ItemStack.EMPTY;
        }
    }

    private boolean doesMealHaveContainer(ItemStack meal) {
        return !this.mealContainerStack.isEmpty() || meal.hasCraftingRemainingItem();
    }

    public boolean isContainerValid(ItemStack containerItem) {
        if (containerItem.isEmpty()) {
            return false;
        } else {
            return !this.mealContainerStack.isEmpty() ? ItemStack.isSameItem(this.mealContainerStack, containerItem) : ItemStack.isSameItem(this.getMeal(), containerItem);
        }
    }

    @Override
    public Component getName() {
        return (Component) (this.customName != null ? this.customName : TextUtils.getTranslation("container.cooking_pot"));
    }

    @Override
    public Component getDisplayName() {
        return this.getName();
    }

    @Nullable
    @Override
    public Component getCustomName() {
        return this.customName;
    }

    public void setCustomName(Component name) {
        this.customName = name;
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory player, Player entity) {
        return new CookingPotMenu(id, player, this, this.cookingPotData);
    }

    @Nonnull
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
        if (cap.equals(ForgeCapabilities.ITEM_HANDLER)) {
            return side != null && !side.equals(Direction.UP) ? this.outputHandler.cast() : this.inputHandler.cast();
        } else {
            return super.getCapability(cap, side);
        }
    }

    @Override
    public void setRemoved() {
        super.m_7651_();
        this.inputHandler.invalidate();
        this.outputHandler.invalidate();
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.writeItems(new CompoundTag());
    }

    private ItemStackHandler createHandler() {
        return new ItemStackHandler(9) {

            @Override
            protected void onContentsChanged(int slot) {
                if (slot >= 0 && slot < 6) {
                    CookingPotBlockEntity.this.checkNewRecipe = true;
                }
                CookingPotBlockEntity.this.inventoryChanged();
            }
        };
    }

    private ContainerData createIntArray() {
        return new ContainerData() {

            @Override
            public int get(int index) {
                return switch(index) {
                    case 0 ->
                        CookingPotBlockEntity.this.cookTime;
                    case 1 ->
                        CookingPotBlockEntity.this.cookTimeTotal;
                    default ->
                        0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch(index) {
                    case 0:
                        CookingPotBlockEntity.this.cookTime = value;
                        break;
                    case 1:
                        CookingPotBlockEntity.this.cookTimeTotal = value;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }
}
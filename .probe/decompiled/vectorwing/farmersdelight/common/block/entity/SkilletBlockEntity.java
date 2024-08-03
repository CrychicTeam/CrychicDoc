package vectorwing.farmersdelight.common.block.entity;

import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import vectorwing.farmersdelight.common.block.SkilletBlock;
import vectorwing.farmersdelight.common.mixin.accessor.RecipeManagerAccessor;
import vectorwing.farmersdelight.common.registry.ModBlockEntityTypes;
import vectorwing.farmersdelight.common.registry.ModParticleTypes;
import vectorwing.farmersdelight.common.registry.ModSounds;
import vectorwing.farmersdelight.common.utility.ItemUtils;
import vectorwing.farmersdelight.common.utility.TextUtils;

public class SkilletBlockEntity extends SyncedBlockEntity implements HeatableBlockEntity {

    private final ItemStackHandler inventory = this.createHandler();

    private int cookingTime;

    private int cookingTimeTotal;

    private ResourceLocation lastRecipeID;

    private ItemStack skilletStack = ItemStack.EMPTY;

    private int fireAspectLevel;

    public SkilletBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.SKILLET.get(), pos, state);
    }

    public static void cookingTick(Level level, BlockPos pos, BlockState state, SkilletBlockEntity skillet) {
        boolean isHeated = skillet.isHeated(level, pos);
        if (isHeated) {
            ItemStack cookingStack = skillet.getStoredStack();
            if (cookingStack.isEmpty()) {
                skillet.cookingTime = 0;
            } else {
                skillet.cookAndOutputItems(cookingStack, level);
            }
        } else if (skillet.cookingTime > 0) {
            skillet.cookingTime = Mth.clamp(skillet.cookingTime - 2, 0, skillet.cookingTimeTotal);
        }
    }

    public static void animationTick(Level level, BlockPos pos, BlockState state, SkilletBlockEntity skillet) {
        if (skillet.isHeated(level, pos) && skillet.hasStoredStack()) {
            RandomSource random = level.random;
            if (random.nextFloat() < 0.2F) {
                double x = (double) pos.m_123341_() + 0.5 + (random.nextDouble() * 0.4 - 0.2);
                double y = (double) pos.m_123342_() + 0.1;
                double z = (double) pos.m_123343_() + 0.5 + (random.nextDouble() * 0.4 - 0.2);
                double motionY = random.nextBoolean() ? 0.015 : 0.005;
                level.addParticle(ModParticleTypes.STEAM.get(), x, y, z, 0.0, motionY, 0.0);
            }
            if (skillet.fireAspectLevel > 0 && random.nextFloat() < (float) skillet.fireAspectLevel * 0.05F) {
                double x = (double) pos.m_123341_() + 0.5 + (random.nextDouble() * 0.4 - 0.2);
                double y = (double) pos.m_123342_() + 0.1;
                double z = (double) pos.m_123343_() + 0.5 + (random.nextDouble() * 0.4 - 0.2);
                double motionX = (double) (level.random.nextFloat() - 0.5F);
                double motionY = (double) (level.random.nextFloat() * 0.5F + 0.2F);
                double motionZ = (double) (level.random.nextFloat() - 0.5F);
                level.addParticle(ParticleTypes.ENCHANTED_HIT, x, y, z, motionX, motionY, motionZ);
            }
        }
    }

    private void cookAndOutputItems(ItemStack cookingStack, Level level) {
        if (level != null) {
            this.cookingTime++;
            if (this.cookingTime >= this.cookingTimeTotal) {
                SimpleContainer wrapper = new SimpleContainer(cookingStack);
                Optional<CampfireCookingRecipe> recipe = this.getMatchingRecipe(wrapper);
                if (recipe.isPresent()) {
                    ItemStack resultStack = ((CampfireCookingRecipe) recipe.get()).m_5874_(wrapper, level.registryAccess());
                    Direction direction = ((Direction) this.m_58900_().m_61143_(SkilletBlock.FACING)).getClockWise();
                    ItemUtils.spawnItemEntity(level, resultStack.copy(), (double) this.f_58858_.m_123341_() + 0.5, (double) this.f_58858_.m_123342_() + 0.3, (double) this.f_58858_.m_123343_() + 0.5, (double) ((float) direction.getStepX() * 0.08F), 0.25, (double) ((float) direction.getStepZ() * 0.08F));
                    this.cookingTime = 0;
                    this.inventory.extractItem(0, 1, false);
                }
            }
        }
    }

    public boolean isCooking() {
        return this.isHeated() && this.hasStoredStack();
    }

    public boolean isHeated() {
        return this.f_58857_ != null ? this.isHeated(this.f_58857_, this.f_58858_) : false;
    }

    private Optional<CampfireCookingRecipe> getMatchingRecipe(Container recipeWrapper) {
        if (this.f_58857_ == null) {
            return Optional.empty();
        } else {
            if (this.lastRecipeID != null) {
                Recipe<Container> recipe = (Recipe<Container>) ((RecipeManagerAccessor) this.f_58857_.getRecipeManager()).<Container, CampfireCookingRecipe>getRecipeMap(RecipeType.CAMPFIRE_COOKING).get(this.lastRecipeID);
                if (recipe instanceof CampfireCookingRecipe && recipe.matches(recipeWrapper, this.f_58857_)) {
                    return Optional.of(recipe);
                }
            }
            Optional<CampfireCookingRecipe> recipe = this.f_58857_.getRecipeManager().getRecipeFor(RecipeType.CAMPFIRE_COOKING, recipeWrapper, this.f_58857_);
            if (recipe.isPresent()) {
                this.lastRecipeID = ((CampfireCookingRecipe) recipe.get()).m_6423_();
                return recipe;
            } else {
                return Optional.empty();
            }
        }
    }

    @Override
    public void load(CompoundTag compound) {
        super.m_142466_(compound);
        this.inventory.deserializeNBT(compound.getCompound("Inventory"));
        this.cookingTime = compound.getInt("CookTime");
        this.cookingTimeTotal = compound.getInt("CookTimeTotal");
        this.skilletStack = ItemStack.of(compound.getCompound("Skillet"));
        this.fireAspectLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FIRE_ASPECT, this.skilletStack);
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.m_183515_(compound);
        compound.put("Inventory", this.inventory.serializeNBT());
        compound.putInt("CookTime", this.cookingTime);
        compound.putInt("CookTimeTotal", this.cookingTimeTotal);
        compound.put("Skillet", this.skilletStack.save(new CompoundTag()));
    }

    public CompoundTag writeSkilletItem(CompoundTag compound) {
        compound.put("Skillet", this.skilletStack.save(new CompoundTag()));
        return compound;
    }

    public void setSkilletItem(ItemStack stack) {
        this.skilletStack = stack.copy();
        this.fireAspectLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FIRE_ASPECT, stack);
        this.inventoryChanged();
    }

    public ItemStack addItemToCook(ItemStack addedStack, @Nullable Player player) {
        Optional<CampfireCookingRecipe> recipe = this.getMatchingRecipe(new SimpleContainer(addedStack));
        if (recipe.isPresent()) {
            this.cookingTimeTotal = SkilletBlock.getSkilletCookingTime(((CampfireCookingRecipe) recipe.get()).m_43753_(), this.fireAspectLevel);
            boolean wasEmpty = this.getStoredStack().isEmpty();
            ItemStack remainderStack = this.inventory.insertItem(0, addedStack.copy(), false);
            if (!ItemStack.matches(remainderStack, addedStack)) {
                this.lastRecipeID = ((CampfireCookingRecipe) recipe.get()).m_6423_();
                this.cookingTime = 0;
                if (wasEmpty && this.f_58857_ != null && this.isHeated(this.f_58857_, this.f_58858_)) {
                    this.f_58857_.playSound(null, (double) ((float) this.f_58858_.m_123341_() + 0.5F), (double) ((float) this.f_58858_.m_123342_() + 0.5F), (double) ((float) this.f_58858_.m_123343_() + 0.5F), ModSounds.BLOCK_SKILLET_ADD_FOOD.get(), SoundSource.BLOCKS, 0.8F, 1.0F);
                }
                return remainderStack;
            }
        } else if (player != null) {
            player.displayClientMessage(TextUtils.getTranslation("block.skillet.invalid_item"), true);
        }
        return addedStack;
    }

    public ItemStack removeItem() {
        return this.inventory.extractItem(0, this.getStoredStack().getMaxStackSize(), false);
    }

    public IItemHandler getInventory() {
        return this.inventory;
    }

    public ItemStack getStoredStack() {
        return this.inventory.getStackInSlot(0);
    }

    public boolean hasStoredStack() {
        return !this.getStoredStack().isEmpty();
    }

    private ItemStackHandler createHandler() {
        return new ItemStackHandler() {

            @Override
            protected void onContentsChanged(int slot) {
                SkilletBlockEntity.this.inventoryChanged();
            }
        };
    }

    @Override
    public void setRemoved() {
        super.m_7651_();
    }
}
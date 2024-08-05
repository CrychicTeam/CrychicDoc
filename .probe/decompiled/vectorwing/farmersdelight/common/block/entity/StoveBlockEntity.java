package vectorwing.farmersdelight.common.block.entity;

import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.items.ItemStackHandler;
import vectorwing.farmersdelight.common.block.StoveBlock;
import vectorwing.farmersdelight.common.mixin.accessor.RecipeManagerAccessor;
import vectorwing.farmersdelight.common.registry.ModBlockEntityTypes;
import vectorwing.farmersdelight.common.utility.ItemUtils;

public class StoveBlockEntity extends SyncedBlockEntity {

    private static final VoxelShape GRILLING_AREA = Block.box(3.0, 0.0, 3.0, 13.0, 1.0, 13.0);

    private static final int INVENTORY_SLOT_COUNT = 6;

    private final ItemStackHandler inventory = this.createHandler();

    private final int[] cookingTimes = new int[6];

    private final int[] cookingTimesTotal = new int[6];

    private ResourceLocation[] lastRecipeIDs = new ResourceLocation[6];

    public StoveBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.STOVE.get(), pos, state);
    }

    @Override
    public void load(CompoundTag compound) {
        super.m_142466_(compound);
        if (compound.contains("Inventory")) {
            this.inventory.deserializeNBT(compound.getCompound("Inventory"));
        } else {
            this.inventory.deserializeNBT(compound);
        }
        if (compound.contains("CookingTimes", 11)) {
            int[] arrayCookingTimes = compound.getIntArray("CookingTimes");
            System.arraycopy(arrayCookingTimes, 0, this.cookingTimes, 0, Math.min(this.cookingTimesTotal.length, arrayCookingTimes.length));
        }
        if (compound.contains("CookingTotalTimes", 11)) {
            int[] arrayCookingTimesTotal = compound.getIntArray("CookingTotalTimes");
            System.arraycopy(arrayCookingTimesTotal, 0, this.cookingTimesTotal, 0, Math.min(this.cookingTimesTotal.length, arrayCookingTimesTotal.length));
        }
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        this.writeItems(compound);
        compound.putIntArray("CookingTimes", this.cookingTimes);
        compound.putIntArray("CookingTotalTimes", this.cookingTimesTotal);
    }

    private CompoundTag writeItems(CompoundTag compound) {
        super.m_183515_(compound);
        compound.put("Inventory", this.inventory.serializeNBT());
        return compound;
    }

    public static void cookingTick(Level level, BlockPos pos, BlockState state, StoveBlockEntity stove) {
        boolean isStoveLit = (Boolean) state.m_61143_(StoveBlock.LIT);
        if (stove.isStoveBlockedAbove()) {
            if (!ItemUtils.isInventoryEmpty(stove.inventory)) {
                ItemUtils.dropItems(level, pos, stove.inventory);
                stove.inventoryChanged();
            }
        } else if (isStoveLit) {
            stove.cookAndOutputItems();
        } else {
            for (int i = 0; i < stove.inventory.getSlots(); i++) {
                if (stove.cookingTimes[i] > 0) {
                    stove.cookingTimes[i] = Mth.clamp(stove.cookingTimes[i] - 2, 0, stove.cookingTimesTotal[i]);
                }
            }
        }
    }

    public static void animationTick(Level level, BlockPos pos, BlockState state, StoveBlockEntity stove) {
        for (int i = 0; i < stove.inventory.getSlots(); i++) {
            if (!stove.inventory.getStackInSlot(i).isEmpty() && level.random.nextFloat() < 0.2F) {
                Vec2 stoveItemVector = stove.getStoveItemOffset(i);
                Direction direction = (Direction) state.m_61143_(StoveBlock.FACING);
                int directionIndex = direction.get2DDataValue();
                Vec2 offset = directionIndex % 2 == 0 ? stoveItemVector : new Vec2(stoveItemVector.y, stoveItemVector.x);
                double x = (double) pos.m_123341_() + 0.5 - (double) ((float) direction.getStepX() * offset.x) + (double) ((float) direction.getClockWise().getStepX() * offset.x);
                double y = (double) pos.m_123342_() + 1.0;
                double z = (double) pos.m_123343_() + 0.5 - (double) ((float) direction.getStepZ() * offset.y) + (double) ((float) direction.getClockWise().getStepZ() * offset.y);
                for (int k = 0; k < 3; k++) {
                    level.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0, 5.0E-4, 0.0);
                }
            }
        }
    }

    private void cookAndOutputItems() {
        if (this.f_58857_ != null) {
            boolean didInventoryChange = false;
            for (int i = 0; i < this.inventory.getSlots(); i++) {
                ItemStack stoveStack = this.inventory.getStackInSlot(i);
                if (!stoveStack.isEmpty()) {
                    this.cookingTimes[i]++;
                    if (this.cookingTimes[i] >= this.cookingTimesTotal[i]) {
                        Container inventoryWrapper = new SimpleContainer(stoveStack);
                        Optional<CampfireCookingRecipe> recipe = this.getMatchingRecipe(inventoryWrapper, i);
                        if (recipe.isPresent()) {
                            ItemStack resultStack = ((CampfireCookingRecipe) recipe.get()).m_8043_(this.f_58857_.registryAccess());
                            if (!resultStack.isEmpty()) {
                                ItemUtils.spawnItemEntity(this.f_58857_, resultStack.copy(), (double) this.f_58858_.m_123341_() + 0.5, (double) this.f_58858_.m_123342_() + 1.0, (double) this.f_58858_.m_123343_() + 0.5, this.f_58857_.random.nextGaussian() * 0.01F, 0.1F, this.f_58857_.random.nextGaussian() * 0.01F);
                            }
                        }
                        this.inventory.setStackInSlot(i, ItemStack.EMPTY);
                        didInventoryChange = true;
                    }
                }
            }
            if (didInventoryChange) {
                this.inventoryChanged();
            }
        }
    }

    public int getNextEmptySlot() {
        for (int i = 0; i < this.inventory.getSlots(); i++) {
            ItemStack slotStack = this.inventory.getStackInSlot(i);
            if (slotStack.isEmpty()) {
                return i;
            }
        }
        return -1;
    }

    public boolean addItem(ItemStack itemStackIn, CampfireCookingRecipe recipe, int slot) {
        if (0 <= slot && slot < this.inventory.getSlots()) {
            ItemStack slotStack = this.inventory.getStackInSlot(slot);
            if (slotStack.isEmpty()) {
                this.cookingTimesTotal[slot] = recipe.m_43753_();
                this.cookingTimes[slot] = 0;
                this.inventory.setStackInSlot(slot, itemStackIn.split(1));
                this.lastRecipeIDs[slot] = recipe.m_6423_();
                this.inventoryChanged();
                return true;
            }
        }
        return false;
    }

    public Optional<CampfireCookingRecipe> getMatchingRecipe(Container recipeWrapper, int slot) {
        if (this.f_58857_ == null) {
            return Optional.empty();
        } else {
            if (this.lastRecipeIDs[slot] != null) {
                Recipe<Container> recipe = (Recipe<Container>) ((RecipeManagerAccessor) this.f_58857_.getRecipeManager()).<Container, CampfireCookingRecipe>getRecipeMap(RecipeType.CAMPFIRE_COOKING).get(this.lastRecipeIDs[slot]);
                if (recipe instanceof CampfireCookingRecipe && recipe.matches(recipeWrapper, this.f_58857_)) {
                    return Optional.of(recipe);
                }
            }
            return this.f_58857_.getRecipeManager().getRecipeFor(RecipeType.CAMPFIRE_COOKING, recipeWrapper, this.f_58857_);
        }
    }

    public ItemStackHandler getInventory() {
        return this.inventory;
    }

    public boolean isStoveBlockedAbove() {
        if (this.f_58857_ != null) {
            BlockState above = this.f_58857_.getBlockState(this.f_58858_.above());
            return Shapes.joinIsNotEmpty(GRILLING_AREA, above.m_60808_(this.f_58857_, this.f_58858_.above()), BooleanOp.AND);
        } else {
            return false;
        }
    }

    public Vec2 getStoveItemOffset(int index) {
        float X_OFFSET = 0.3F;
        float Y_OFFSET = 0.2F;
        Vec2[] OFFSETS = new Vec2[] { new Vec2(0.3F, 0.2F), new Vec2(0.0F, 0.2F), new Vec2(-0.3F, 0.2F), new Vec2(0.3F, -0.2F), new Vec2(0.0F, -0.2F), new Vec2(-0.3F, -0.2F) };
        return OFFSETS[index];
    }

    private void addParticles() {
        if (this.f_58857_ != null) {
            for (int i = 0; i < this.inventory.getSlots(); i++) {
                if (!this.inventory.getStackInSlot(i).isEmpty() && this.f_58857_.random.nextFloat() < 0.2F) {
                    Vec2 stoveItemVector = this.getStoveItemOffset(i);
                    Direction direction = (Direction) this.m_58900_().m_61143_(StoveBlock.FACING);
                    int directionIndex = direction.get2DDataValue();
                    Vec2 offset = directionIndex % 2 == 0 ? stoveItemVector : new Vec2(stoveItemVector.y, stoveItemVector.x);
                    double x = (double) this.f_58858_.m_123341_() + 0.5 - (double) ((float) direction.getStepX() * offset.x) + (double) ((float) direction.getClockWise().getStepX() * offset.x);
                    double y = (double) this.f_58858_.m_123342_() + 1.0;
                    double z = (double) this.f_58858_.m_123343_() + 0.5 - (double) ((float) direction.getStepZ() * offset.y) + (double) ((float) direction.getClockWise().getStepZ() * offset.y);
                    for (int k = 0; k < 3; k++) {
                        this.f_58857_.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0, 5.0E-4, 0.0);
                    }
                }
            }
        }
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.writeItems(new CompoundTag());
    }

    private ItemStackHandler createHandler() {
        return new ItemStackHandler(6) {

            @Override
            public int getSlotLimit(int slot) {
                return 1;
            }
        };
    }
}
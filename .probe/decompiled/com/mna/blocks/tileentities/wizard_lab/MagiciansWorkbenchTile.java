package com.mna.blocks.tileentities.wizard_lab;

import com.mna.api.blocks.tile.TileEntityWithInventory;
import com.mna.blocks.tileentities.init.TileEntityInit;
import com.mna.gui.containers.block.ContainerMagiciansWorkbench;
import com.mna.tools.InventoryUtilities;
import com.mna.tools.RecipeUtil;
import it.unimi.dsi.fastutil.ints.IntList;
import java.util.LinkedList;
import java.util.Optional;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.Connection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.items.wrapper.InvWrapper;

public class MagiciansWorkbenchTile extends TileEntityWithInventory implements MenuProvider, Consumer<FriendlyByteBuf> {

    public static final int MAX_RECIPES = 8;

    public ResultContainer firstResultInv = new ResultContainer();

    public ResultContainer secondResultInv = new ResultContainer();

    private final LinkedList<MagiciansWorkbenchTile.RememberedRecipe> rememberedRecipes = new LinkedList();

    public MagiciansWorkbenchTile(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state, 38);
    }

    public MagiciansWorkbenchTile(BlockPos pos, BlockState state) {
        this(TileEntityInit.MAGICIANS_WORKBENCH.get(), pos, state);
    }

    public MagiciansWorkbenchTile readFrom(FriendlyByteBuf data) {
        return this;
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player) {
        return new ContainerMagiciansWorkbench(id, playerInventory, this);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("mna:container.magicians_workbench");
    }

    public void accept(FriendlyByteBuf data) {
        data.writeBlockPos(this.m_58899_());
    }

    public void rememberRecipe(ItemStack output, ItemStack[] recipeItems) {
        if (!output.isEmpty()) {
            RecipeUtil.lookupCraftingRecipe(this.m_58904_(), recipeItems).ifPresent(resolvedRecipe -> {
                for (MagiciansWorkbenchTile.RememberedRecipe recipe : this.rememberedRecipes) {
                    if (recipe.recipeId.equals(resolvedRecipe.m_6423_())) {
                        return;
                    }
                }
                if (this.popRecipe()) {
                    this.rememberedRecipes.add(new MagiciansWorkbenchTile.RememberedRecipe(resolvedRecipe.m_6423_(), output));
                    this.m_58904_().sendBlockUpdated(this.m_58899_(), this.m_58900_(), this.m_58900_(), 3);
                }
            });
        }
    }

    private boolean popRecipe() {
        if (this.rememberedRecipes.size() < 8) {
            return true;
        } else {
            for (int index = 0; index < this.rememberedRecipes.size(); index++) {
                if (!((MagiciansWorkbenchTile.RememberedRecipe) this.rememberedRecipes.get(index)).isLocked) {
                    this.rememberedRecipes.remove(index);
                    return true;
                }
            }
            return false;
        }
    }

    public boolean toggleRecipeLock(int recipeIndex) {
        if (recipeIndex >= this.getRememberedRecipeItems().size()) {
            return false;
        } else {
            MagiciansWorkbenchTile.RememberedRecipe recipe = (MagiciansWorkbenchTile.RememberedRecipe) this.getRememberedRecipeItems().get(recipeIndex);
            if (recipe == null) {
                return false;
            } else {
                recipe.isLocked = !recipe.isLocked;
                this.m_58904_().sendBlockUpdated(this.m_58899_(), this.m_58900_(), this.m_58900_(), 3);
                return true;
            }
        }
    }

    public LinkedList<MagiciansWorkbenchTile.RememberedRecipe> getRememberedRecipeItems() {
        return this.rememberedRecipes;
    }

    private StackedContents fillStackedContents() {
        StackedContents contents = new StackedContents();
        for (int i = ContainerMagiciansWorkbench.INVENTORY_STORAGE_START; i <= ContainerMagiciansWorkbench.INVENTORY_STORAGE_END; i++) {
            contents.accountSimpleStack(this.m_8020_(i));
        }
        return contents;
    }

    public MagiciansWorkbenchTile.AutoCraftResult craftFromRemembered(ItemStack desiredItem) {
        Optional<MagiciansWorkbenchTile.RememberedRecipe> remembered = this.rememberedRecipes.stream().filter(rr -> ItemStack.isSameItemSameTags(rr.output, desiredItem)).findFirst();
        if (!remembered.isPresent()) {
            return MagiciansWorkbenchTile.AutoCraftResult.NO_RECIPE;
        } else {
            StackedContents contents = this.fillStackedContents();
            if (!contents.canCraft(((MagiciansWorkbenchTile.RememberedRecipe) remembered.get()).getRecipe(this.f_58857_), (IntList) null)) {
                return MagiciansWorkbenchTile.AutoCraftResult.MISSING_ITEMS;
            } else {
                InvWrapper myInvWrapper = new InvWrapper(this);
                for (Ingredient ingred : ((MagiciansWorkbenchTile.RememberedRecipe) remembered.get()).getRecipe(this.f_58857_).m_7527_()) {
                    if (!ingred.isEmpty()) {
                        for (int i = ContainerMagiciansWorkbench.INVENTORY_STORAGE_START; i <= ContainerMagiciansWorkbench.INVENTORY_STORAGE_END; i++) {
                            ItemStack existing = this.m_8020_(i);
                            if (!existing.isEmpty() && ingred.test(existing)) {
                                ItemStack resultStack = existing.getCraftingRemainingItem();
                                existing.shrink(1);
                                if (!InventoryUtilities.mergeIntoInventory(myInvWrapper, resultStack)) {
                                    Vec3 dropPos = Vec3.atCenterOf(this.m_58899_());
                                    ItemEntity drop = new ItemEntity(this.m_58904_(), dropPos.x, dropPos.y, dropPos.z, resultStack);
                                    drop.m_20334_(0.0, 0.25, 0.0);
                                    drop.setDefaultPickUpDelay();
                                    this.m_58904_().m_7967_(drop);
                                }
                                break;
                            }
                        }
                    }
                }
                return MagiciansWorkbenchTile.AutoCraftResult.SUCCESS;
            }
        }
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        ListTag recall = new ListTag();
        for (MagiciansWorkbenchTile.RememberedRecipe recipe : this.rememberedRecipes) {
            try {
                CompoundTag output = new CompoundTag();
                recipe.getOutput(this.f_58857_).save(output);
                output.putBoolean("isLocked", recipe.isLocked);
                output.putString("recipeId", recipe.recipeId.toString());
                recall.add(output);
            } catch (Throwable var6) {
            }
        }
        compound.put("rememberedRecipes", recall);
    }

    @Override
    public void load(CompoundTag compound) {
        ListTag recall = compound.getList("rememberedRecipes", 10);
        this.rememberedRecipes.clear();
        if (recall != null) {
            for (int i = 0; i < recall.size(); i++) {
                CompoundTag rememberedRecipe = (CompoundTag) recall.get(i);
                if (rememberedRecipe.contains("recipeId")) {
                    ResourceLocation recipeId = new ResourceLocation(rememberedRecipe.getString("recipeId"));
                    ItemStack output = ItemStack.of(rememberedRecipe);
                    if (!output.isEmpty()) {
                        MagiciansWorkbenchTile.RememberedRecipe rec = new MagiciansWorkbenchTile.RememberedRecipe(recipeId, output);
                        rec.isLocked = rememberedRecipe.getBoolean("isLocked");
                        this.rememberedRecipes.add(rec);
                    }
                }
            }
        }
        super.load(compound);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        this.saveAdditional(tag);
        return tag;
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public void handleUpdateTag(CompoundTag tag) {
        super.handleUpdateTag(tag);
        this.load(tag);
    }

    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        this.load(pkt.getTag());
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        return index < 18 || index > 37;
    }

    @Override
    public boolean canPlaceItem(int index, ItemStack stack) {
        return index < 18 || index > 37;
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack itemStackIn, Direction direction) {
        return index < 18 || index > 37;
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        int[] slots = new int[18];
        int i = 0;
        while (i < 18) {
            slots[i] = i++;
        }
        return slots;
    }

    public static enum AutoCraftResult {

        MISSING_ITEMS, NO_RECIPE, SUCCESS
    }

    public class RememberedRecipe {

        public final ResourceLocation recipeId;

        private ItemStack output;

        private Ingredient[] ingredients;

        private CraftingRecipe recipe;

        private boolean isLocked;

        private boolean hasResolved = false;

        public RememberedRecipe(ResourceLocation recipeId, ItemStack output) {
            this.recipeId = recipeId;
            this.isLocked = false;
        }

        public void lock() {
            this.isLocked = true;
        }

        public void unlock() {
            this.isLocked = false;
        }

        public boolean isLocked() {
            return this.isLocked;
        }

        public ItemStack getOutput(Level world) {
            if (!this.hasResolved) {
                this.resolveRecipe(world);
            }
            return this.output;
        }

        public Ingredient[] getComponents(Level world) {
            if (!this.hasResolved) {
                this.resolveRecipe(world);
            }
            return this.ingredients;
        }

        @Nullable
        public CraftingRecipe getRecipe(Level world) {
            if (!this.hasResolved) {
                this.resolveRecipe(world);
            }
            return this.recipe;
        }

        private void resolveRecipe(Level world) {
            world.getRecipeManager().byKey(this.recipeId).ifPresent(recipe -> {
                if (recipe instanceof CraftingRecipe) {
                    this.output = recipe.getResultItem(world.registryAccess());
                    this.ingredients = (Ingredient[]) recipe.getIngredients().toArray(new Ingredient[0]);
                    this.recipe = (CraftingRecipe) recipe;
                }
            });
            this.hasResolved = true;
        }
    }
}
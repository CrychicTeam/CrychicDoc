package com.mna.blocks.tileentities.wizard_lab;

import com.mna.advancements.CustomAdvancementTriggers;
import com.mna.api.config.GeneralConfigValues;
import com.mna.blocks.BlockInit;
import com.mna.blocks.tileentities.init.TileEntityInit;
import com.mna.events.EventDispatcher;
import com.mna.gui.containers.block.ContainerRunescribingTable;
import com.mna.items.ItemInit;
import com.mna.network.ClientMessageDispatcher;
import com.mna.network.messages.to_server.RunescribingTableMutexChangeMessage;
import com.mna.recipes.RecipeInit;
import com.mna.recipes.runeforging.RunescribingRecipe;
import com.mna.tools.ContainerTools;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class RunescribingTableTile extends WizardLabTile implements MenuProvider, Consumer<FriendlyByteBuf> {

    public static final int SLOT_HAMMER = 0;

    public static final int SLOT_CHISEL = 1;

    public static final int SLOT_PATTERN = 2;

    public static final int SLOT_GUIDE = 3;

    public static final int SLOT_CLAY = 4;

    public static final int SLOT_PATTERNS_START = 5;

    public static final int SLOT_PATTERNS_END = 29;

    public static final int INVENTORY_SIZE = 30;

    public RunescribingTableTile(BlockEntityType<?> tileEntityTypeIn, BlockPos pos, BlockState state) {
        super(tileEntityTypeIn, pos, state, 30);
    }

    public RunescribingTableTile(BlockPos pos, BlockState state) {
        this(TileEntityInit.RUNESCRIBING_TABLE.get(), pos, state);
    }

    @Override
    public boolean canActivate(Player player) {
        return false;
    }

    @Override
    public float getPctComplete() {
        return 1.0F;
    }

    @Override
    protected boolean canContinue() {
        return true;
    }

    @Override
    protected List<Integer> getSyncedInventorySlots() {
        return Arrays.asList(0, 1, 2, 3, 4);
    }

    public long getHMutex() {
        ItemStack patternStack = this.m_8020_(2);
        return patternStack.getItem() == ItemInit.RUNE_PATTERN.get() && patternStack.hasTag() ? patternStack.getOrCreateTag().getLong("hmutex") : 0L;
    }

    public long getVMutex() {
        ItemStack patternStack = this.m_8020_(2);
        return patternStack.getItem() == ItemInit.RUNE_PATTERN.get() && patternStack.hasTag() ? patternStack.getTag().getLong("vmutex") : 0L;
    }

    public boolean canUndo(Player player) {
        return !this.m_8020_(4).isEmpty() && player.totalExperience >= GeneralConfigValues.ExperiencePerRunescribeUndo;
    }

    public boolean hasRequiredItems(boolean toolsRequired) {
        ItemStack patternStack = this.m_8020_(2);
        ItemStack chiselStack = this.m_8020_(1);
        ItemStack hammerStack = this.m_8020_(0);
        return !toolsRequired ? patternStack.getItem() == ItemInit.RUNE_PATTERN.get() : patternStack.getItem() == ItemInit.RUNE_PATTERN.get() && chiselStack.getItem() == ItemInit.RUNESMITH_CHISEL.get() && (hammerStack.getItem() == ItemInit.RUNESMITH_HAMMER.get() || hammerStack.getItem() == ItemInit.RUNIC_MALUS.get());
    }

    private void damageHammerAndChisel(RandomSource source) {
        ItemStack chiselStack = this.m_8020_(1);
        ItemStack hammerStack = this.m_8020_(0);
        if (chiselStack.getItem() == ItemInit.RUNESMITH_CHISEL.get() && chiselStack.hurt(1, source, null)) {
            this.m_6836_(1, ItemStack.EMPTY);
        }
        if (hammerStack.getItem() == ItemInit.RUNESMITH_HAMMER.get() && hammerStack.hurt(1, source, null)) {
            this.m_6836_(0, ItemStack.EMPTY);
        }
    }

    @Nullable
    public RunescribingRecipe getRecipeFromGuideSlot() {
        ItemStack recipe = this.m_8020_(3);
        return recipe.isEmpty() ? null : ItemInit.RECIPE_SCRAP_RUNESCRIBING.get().getRecipe(recipe, this.f_58857_);
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player playerEntity) {
        return new ContainerRunescribingTable(id, playerInventory, this);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("mna:container.inscription_table");
    }

    public void accept(FriendlyByteBuf data) {
        data.writeBlockPos(this.m_58899_());
    }

    public RunescribingTableTile readFrom(FriendlyByteBuf data) {
        return this;
    }

    public boolean writeMutexChanges(long hMutex, long vMutex, Player player, int playerTier, boolean isRemove, RandomSource source) {
        boolean returnValue = false;
        if (this.m_58904_().isClientSide()) {
            ClientMessageDispatcher.sendRunescribingMutexChange(this, hMutex, vMutex, playerTier, isRemove);
            return returnValue;
        } else {
            if (isRemove) {
                if (!this.canUndo(player)) {
                    return false;
                }
                this.m_8020_(4).shrink(1);
                player.giveExperiencePoints(-GeneralConfigValues.ExperiencePerRunescribeUndo);
            }
            ItemStack patternStack = this.m_8020_(2);
            if (patternStack.getItem() == ItemInit.RUNE_PATTERN.get()) {
                ItemInit.RUNE_PATTERN.get().setHMutex(patternStack, hMutex);
                ItemInit.RUNE_PATTERN.get().setVMutex(patternStack, vMutex);
            }
            RunescribingRecipe recipe = (RunescribingRecipe) this.m_58904_().getRecipeManager().getRecipeFor(RecipeInit.RUNESCRIBING_TYPE.get(), ContainerTools.createTemporaryContainer(patternStack), this.f_58857_).orElse(null);
            if (recipe != null && (playerTier < 0 || recipe.getTier() <= playerTier) && EventDispatcher.DispatchRunescribeCraft(recipe, recipe.getResultItem(), player)) {
                this.m_6836_(2, recipe.getResultItem());
                if (player instanceof ServerPlayer) {
                    CustomAdvancementTriggers.RUNESCRIBE.trigger((ServerPlayer) player, recipe.getResultItem());
                }
                returnValue = true;
            }
            if (playerTier > 0) {
                this.damageHammerAndChisel(source);
            }
            return returnValue;
        }
    }

    private static RunescribingTableTile getAndVerify(Level world, BlockPos position) {
        if (world.isLoaded(position) && world.getBlockState(position).m_60734_() == BlockInit.RUNESCRIBING_TABLE.get()) {
            BlockEntity te = world.getBlockEntity(position);
            if (te != null && te instanceof RunescribingTableTile) {
                return (RunescribingTableTile) te;
            }
        }
        return null;
    }

    public static void handleMutexChangeMessage(ServerPlayer sendingPlayer, RunescribingTableMutexChangeMessage message) {
        RunescribingTableTile te = getAndVerify(sendingPlayer.m_9236_(), message.getPosition());
        if (te != null) {
            te.writeMutexChanges(message.getHMutex(), message.getVMutex(), sendingPlayer, message.getPlayerTier(), message.getIsRemove(), sendingPlayer.m_9236_().getRandom());
        }
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack itemStackIn, Direction direction) {
        if (itemStackIn.getCount() != 1) {
            return false;
        } else if (index == 0 && this.m_8020_(0).isEmpty() && itemStackIn.getItem() == ItemInit.RUNESMITH_HAMMER.get()) {
            return true;
        } else if (index == 1 && this.m_8020_(1).isEmpty() && itemStackIn.getItem() == ItemInit.RUNESMITH_CHISEL.get()) {
            return true;
        } else {
            return index == 4 && itemStackIn.getItem() == Items.CLAY_BALL ? true : index == 2 && this.m_8020_(2).isEmpty() && itemStackIn.getItem() == ItemInit.RUNE_PATTERN.get();
        }
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        if (!this.m_8020_(index).isEmpty() && ItemStack.matches(this.m_8020_(index), stack)) {
            if (direction != Direction.EAST && direction != Direction.WEST && direction != Direction.NORTH && direction != Direction.SOUTH) {
                if ((direction == Direction.DOWN || direction == Direction.UP) && index == 2) {
                    return true;
                }
            } else if (index == 0 || index == 1 || index == 4) {
                return true;
            }
            return false;
        } else {
            return false;
        }
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        switch(slot) {
            case 0:
                return stack.getItem() == ItemInit.RUNESMITH_HAMMER.get() || stack.getItem() == ItemInit.RUNIC_MALUS.get();
            case 1:
                return stack.getItem() == ItemInit.RUNESMITH_CHISEL.get();
            case 2:
                return stack.getItem() == ItemInit.RUNE_PATTERN.get();
            case 3:
                return stack.getItem() == ItemInit.RECIPE_SCRAP_RUNESCRIBING.get();
            case 4:
                return stack.getItem() == Items.CLAY_BALL;
            default:
                return slot >= 5 && slot <= 29 ? stack.getItem() == ItemInit.RECIPE_SCRAP_RUNESCRIBING.get() : false;
        }
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        if (side == Direction.EAST || side == Direction.WEST || side == Direction.NORTH || side == Direction.SOUTH) {
            return new int[] { 0, 1, 4 };
        } else {
            return side != Direction.DOWN && side != Direction.UP ? new int[0] : new int[] { 2 };
        }
    }

    @Override
    public int getMaxStackSize() {
        return 64;
    }
}
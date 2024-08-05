package noppes.npcs.containers;

import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.TransientCraftingContainer;
import net.minecraft.world.item.ItemStack;
import noppes.npcs.CustomBlocks;
import noppes.npcs.CustomContainer;
import noppes.npcs.controllers.RecipeController;
import noppes.npcs.controllers.data.RecipeCarpentry;

public class ContainerCarpentryBench extends AbstractContainerMenu {

    public CraftingContainer craftMatrix = new TransientCraftingContainer(this, 4, 4);

    public Container craftResult = new ResultContainer();

    private Player player;

    private BlockPos pos;

    public ContainerCarpentryBench(int id, Inventory par1PlayerInventory, BlockPos pos) {
        super(CustomContainer.container_carpentrybench, id);
        this.pos = pos;
        this.player = par1PlayerInventory.player;
        this.m_38897_(new SlotNpcCrafting(par1PlayerInventory.player, this.craftMatrix, this.craftResult, 0, 133, 41));
        for (int var6 = 0; var6 < 4; var6++) {
            for (int var7 = 0; var7 < 4; var7++) {
                this.m_38897_(new Slot(this.craftMatrix, var7 + var6 * 4, 17 + var7 * 18, 14 + var6 * 18));
            }
        }
        for (int var61 = 0; var61 < 3; var61++) {
            for (int var7 = 0; var7 < 9; var7++) {
                this.m_38897_(new Slot(par1PlayerInventory, var7 + var61 * 9 + 9, 8 + var7 * 18, 98 + var61 * 18));
            }
        }
        for (int var71 = 0; var71 < 9; var71++) {
            this.m_38897_(new Slot(par1PlayerInventory, var71, 8 + var71 * 18, 156));
        }
        this.slotsChanged(this.craftMatrix);
    }

    @Override
    public void slotsChanged(Container par1Container) {
        if (!this.player.m_9236_().isClientSide) {
            RecipeCarpentry recipe = RecipeController.instance.findMatchingRecipe(this.craftMatrix);
            ItemStack item = ItemStack.EMPTY;
            if (recipe != null && recipe.availability.isAvailable(this.player)) {
                item = recipe.m_5874_(this.craftMatrix, this.player.m_9236_().registryAccess());
            }
            this.craftResult.setItem(0, item);
            ServerPlayer plmp = (ServerPlayer) this.player;
            plmp.connection.send(new ClientboundContainerSetSlotPacket(this.f_38840_, this.m_182425_(), 0, item));
        }
    }

    @Override
    public void removed(Player par1Player) {
        super.removed(par1Player);
        if (!par1Player.m_9236_().isClientSide) {
            for (int var2 = 0; var2 < 16; var2++) {
                ItemStack var3 = this.craftMatrix.m_8016_(var2);
                if (var3 != null) {
                    par1Player.drop(var3, false);
                }
            }
        }
    }

    @Override
    public boolean stillValid(Player par1Player) {
        return par1Player.m_9236_().getBlockState(this.pos).m_60734_() == CustomBlocks.carpenty && par1Player.m_20275_((double) this.pos.m_123341_() + 0.5, (double) this.pos.m_123342_() + 0.5, (double) this.pos.m_123343_() + 0.5) <= 64.0;
    }

    @Override
    public ItemStack quickMoveStack(Player par1Player, int par1) {
        ItemStack var2x = ItemStack.EMPTY;
        Slot var3 = (Slot) this.f_38839_.get(par1);
        if (var3 != null && var3.hasItem()) {
            ItemStack var4 = var3.getItem();
            var2x = var4.copy();
            if (par1 == 0) {
                if (!this.m_38903_(var4, 17, 53, true)) {
                    return ItemStack.EMPTY;
                }
                var3.onQuickCraft(var4, var2x);
            } else if (par1 >= 17 && par1 < 44) {
                if (!this.m_38903_(var4, 44, 53, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (par1 >= 44 && par1 < 53) {
                if (!this.m_38903_(var4, 17, 44, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.m_38903_(var4, 17, 53, false)) {
                return ItemStack.EMPTY;
            }
            if (var4.getCount() == 0) {
                var3.set(ItemStack.EMPTY);
            } else {
                var3.setChanged();
            }
            if (var4.getCount() == var2x.getCount()) {
                return ItemStack.EMPTY;
            }
            var3.onTake(par1Player, var4);
        }
        return var2x;
    }

    @Override
    public boolean canTakeItemForPickAll(ItemStack stack, Slot slotIn) {
        return slotIn.container != this.craftResult && super.canTakeItemForPickAll(stack, slotIn);
    }
}
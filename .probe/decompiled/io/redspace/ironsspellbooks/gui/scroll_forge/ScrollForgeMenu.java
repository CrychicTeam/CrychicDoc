package io.redspace.ironsspellbooks.gui.scroll_forge;

import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import io.redspace.ironsspellbooks.block.scroll_forge.ScrollForgeTile;
import io.redspace.ironsspellbooks.item.InkItem;
import io.redspace.ironsspellbooks.registries.BlockRegistry;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import io.redspace.ironsspellbooks.registries.MenuRegistry;
import io.redspace.ironsspellbooks.util.ModTags;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ScrollForgeMenu extends AbstractContainerMenu {

    public final ScrollForgeTile blockEntity;

    private final Level level;

    private final Slot inkSlot;

    private final Slot blankScrollSlot;

    private final Slot focusSlot;

    private final Slot resultSlot;

    private AbstractSpell spellRecipeSelection = SpellRegistry.none();

    private static final int HOTBAR_SLOT_COUNT = 9;

    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;

    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;

    private static final int PLAYER_INVENTORY_SLOT_COUNT = 27;

    private static final int VANILLA_SLOT_COUNT = 36;

    private static final int VANILLA_FIRST_SLOT_INDEX = 0;

    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = 36;

    private static final int TE_INVENTORY_SLOT_COUNT = 4;

    public ScrollForgeMenu(int containerId, Inventory inv, FriendlyByteBuf extraData) {
        this(containerId, inv, inv.player.m_9236_().getBlockEntity(extraData.readBlockPos()));
    }

    public ScrollForgeMenu(int containerId, Inventory inv, BlockEntity entity) {
        super(MenuRegistry.SCROLL_FORGE_MENU.get(), containerId);
        m_38869_(inv, 4);
        this.blockEntity = (ScrollForgeTile) entity;
        this.level = inv.player.m_9236_();
        this.addPlayerInventory(inv);
        this.addPlayerHotbar(inv);
        IItemHandler itemHandler = (IItemHandler) this.blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).resolve().get();
        this.inkSlot = new SlotItemHandler(itemHandler, 0, 12, 17) {

            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.getItem() instanceof InkItem;
            }
        };
        this.blankScrollSlot = new SlotItemHandler(itemHandler, 1, 35, 17) {

            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.is(Items.PAPER);
            }
        };
        this.focusSlot = new SlotItemHandler(itemHandler, 2, 58, 17) {

            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.is(ModTags.SCHOOL_FOCUS);
            }
        };
        this.resultSlot = new SlotItemHandler(itemHandler, 3, 35, 47) {

            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }

            @Override
            public void onTake(Player player, ItemStack stack) {
                ScrollForgeMenu.this.inkSlot.remove(1);
                ScrollForgeMenu.this.blankScrollSlot.remove(1);
                ScrollForgeMenu.this.focusSlot.remove(1);
                ScrollForgeMenu.this.level.playSound(null, ScrollForgeMenu.this.blockEntity.m_58899_(), SoundEvents.UI_CARTOGRAPHY_TABLE_TAKE_RESULT, SoundSource.BLOCKS, 0.8F, 1.1F);
                super.m_142406_(player, stack);
            }
        };
        this.m_38897_(this.inkSlot);
        this.m_38897_(this.blankScrollSlot);
        this.m_38897_(this.focusSlot);
        this.m_38897_(this.resultSlot);
    }

    public void onSlotsChanged(int slot) {
        if (slot != 3) {
            this.setupResultSlot(this.spellRecipeSelection);
        }
    }

    private void setupResultSlot(AbstractSpell spell) {
        ItemStack scrollStack = this.blankScrollSlot.getItem();
        ItemStack inkStack = this.inkSlot.getItem();
        ItemStack focusStack = this.focusSlot.getItem();
        ItemStack resultStack = ItemStack.EMPTY;
        if (!scrollStack.isEmpty() && !inkStack.isEmpty() && !focusStack.isEmpty() && !spell.equals(SpellRegistry.none()) && spell.getSchoolType() == SchoolRegistry.getSchoolFromFocus(focusStack) && scrollStack.getItem().equals(Items.PAPER) && inkStack.getItem() instanceof InkItem inkItem) {
            resultStack = new ItemStack(ItemRegistry.SCROLL.get());
            resultStack.setCount(1);
            ISpellContainer.createScrollContainer(spell, spell.getMinLevelForRarity(inkItem.getRarity()), resultStack);
        }
        if (!ItemStack.matches(resultStack, this.resultSlot.getItem())) {
            if (resultStack.isEmpty()) {
                this.spellRecipeSelection = SpellRegistry.none();
            }
            this.resultSlot.set(resultStack);
        }
    }

    public void setRecipeSpell(AbstractSpell typeFromValue) {
        this.spellRecipeSelection = typeFromValue;
        this.setupResultSlot(typeFromValue);
    }

    public Slot getInkSlot() {
        return this.inkSlot;
    }

    public Slot getBlankScrollSlot() {
        return this.blankScrollSlot;
    }

    public Slot getFocusSlot() {
        return this.focusSlot;
    }

    public Slot getResultSlot() {
        return this.resultSlot;
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        Slot sourceSlot = (Slot) this.f_38839_.get(index);
        if (sourceSlot != null && sourceSlot.hasItem()) {
            ItemStack sourceStack = sourceSlot.getItem();
            ItemStack copyOfSourceStack = sourceStack.copy();
            if (index < 36) {
                if (!this.m_38903_(sourceStack, 36, 40, false)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (index >= 40) {
                    System.out.println("Invalid slotIndex:" + index);
                    return ItemStack.EMPTY;
                }
                if (!this.m_38903_(sourceStack, 0, 36, false)) {
                    return ItemStack.EMPTY;
                }
            }
            if (sourceStack.getCount() == 0) {
                sourceSlot.set(ItemStack.EMPTY);
            } else {
                sourceSlot.setChanged();
            }
            sourceSlot.onTake(playerIn, sourceStack);
            return copyOfSourceStack;
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public boolean canTakeItemForPickAll(ItemStack pStack, Slot pSlot) {
        return pSlot.container != this.resultSlot.container && super.canTakeItemForPickAll(pStack, pSlot);
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return m_38889_(ContainerLevelAccess.create(this.level, this.blockEntity.m_58899_()), pPlayer, BlockRegistry.SCROLL_FORGE_BLOCK.get());
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; i++) {
            for (int l = 0; l < 9; l++) {
                this.m_38897_(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18 + 21, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; i++) {
            this.m_38897_(new Slot(playerInventory, i, 8 + i * 18 + 21, 142));
        }
    }
}
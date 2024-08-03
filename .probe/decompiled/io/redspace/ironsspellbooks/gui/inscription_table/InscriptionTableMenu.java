package io.redspace.ironsspellbooks.gui.inscription_table;

import io.redspace.ironsspellbooks.api.events.InscribeSpellEvent;
import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import io.redspace.ironsspellbooks.api.spells.SpellData;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.item.Scroll;
import io.redspace.ironsspellbooks.item.SpellBook;
import io.redspace.ironsspellbooks.registries.BlockRegistry;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import io.redspace.ironsspellbooks.registries.MenuRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;

public class InscriptionTableMenu extends AbstractContainerMenu {

    private final Level level;

    private final Slot spellBookSlot;

    private final Slot scrollSlot;

    private final Slot resultSlot;

    private int selectedSpellIndex = -1;

    private boolean fromCurioSlot = false;

    protected final ResultContainer resultSlots = new ResultContainer();

    protected final Container inputSlots = new SimpleContainer(2) {

        @Override
        public void setChanged() {
            super.setChanged();
            InscriptionTableMenu.this.slotsChanged(this);
        }
    };

    protected final ContainerLevelAccess access;

    private static final int HOTBAR_SLOT_COUNT = 9;

    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;

    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;

    private static final int PLAYER_INVENTORY_SLOT_COUNT = 27;

    private static final int VANILLA_SLOT_COUNT = 36;

    private static final int VANILLA_FIRST_SLOT_INDEX = 0;

    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = 36;

    private static final int TE_INVENTORY_SLOT_COUNT = 3;

    public InscriptionTableMenu(int containerId, Inventory inv, FriendlyByteBuf extraData) {
        this(containerId, inv, ContainerLevelAccess.NULL);
    }

    public InscriptionTableMenu(int containerId, Inventory inv, ContainerLevelAccess access) {
        super(MenuRegistry.INSCRIPTION_TABLE_MENU.get(), containerId);
        this.access = access;
        m_38869_(inv, 3);
        this.level = inv.player.m_9236_();
        this.addPlayerInventory(inv);
        this.addPlayerHotbar(inv);
        this.spellBookSlot = new Slot(this.inputSlots, 0, 17, 21) {

            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.getItem() instanceof SpellBook;
            }

            @Override
            public void onTake(Player pPlayer, ItemStack pStack) {
                InscriptionTableMenu.this.setSelectedSpell(-1);
                super.onTake(pPlayer, pStack);
            }
        };
        this.scrollSlot = new Slot(this.inputSlots, 1, 17, 53) {

            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.is(ItemRegistry.SCROLL.get());
            }
        };
        this.resultSlot = new Slot(this.resultSlots, 2, 208, 136) {

            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }

            @Override
            public void onTake(Player player, ItemStack stack) {
                ItemStack spellBookStack = InscriptionTableMenu.this.spellBookSlot.getItem();
                ISpellContainer spellList = ISpellContainer.get(spellBookStack);
                spellList.removeSpellAtIndex(InscriptionTableMenu.this.selectedSpellIndex, spellBookStack);
                super.onTake(player, spellBookStack);
            }
        };
        this.m_38897_(this.spellBookSlot);
        this.m_38897_(this.scrollSlot);
        this.m_38897_(this.resultSlot);
        ItemStack spellbookStack = Utils.getPlayerSpellbookStack(inv.player);
        if (spellbookStack != null) {
            this.fromCurioSlot = true;
            this.spellBookSlot.set(spellbookStack);
        }
    }

    public Slot getSpellBookSlot() {
        return this.spellBookSlot;
    }

    public Slot getScrollSlot() {
        return this.scrollSlot;
    }

    public Slot getResultSlot() {
        return this.resultSlot;
    }

    @Override
    public void slotsChanged(Container pContainer) {
        super.slotsChanged(pContainer);
        this.setupResultSlot();
    }

    public void setSelectedSpell(int index) {
        this.selectedSpellIndex = index;
        this.setupResultSlot();
    }

    public void doInscription(int selectedIndex) {
        ItemStack spellBookItemStack = this.getSpellBookSlot().getItem();
        ItemStack scrollItemStack = this.getScrollSlot().getItem();
        if (spellBookItemStack.getItem() instanceof SpellBook && scrollItemStack.getItem() instanceof Scroll) {
            ISpellContainer bookContainer = ISpellContainer.get(spellBookItemStack);
            ISpellContainer scrollContainer = ISpellContainer.get(scrollItemStack);
            SpellData scrollSlot = scrollContainer.getSpellAtIndex(0);
            if (bookContainer.addSpellAtIndex(scrollSlot.getSpell(), scrollSlot.getLevel(), selectedIndex, false, spellBookItemStack)) {
                this.getScrollSlot().remove(1);
            }
        }
    }

    @Override
    public boolean clickMenuButton(Player pPlayer, int pId) {
        if (pId < 0) {
            ItemStack scrollStack = this.getScrollSlot().getItem();
            if (this.selectedSpellIndex >= 0 && scrollStack.getItem() instanceof Scroll scroll) {
                SpellData spellData = ISpellContainer.get(scrollStack).getSpellAtIndex(0);
                if (MinecraftForge.EVENT_BUS.post(new InscribeSpellEvent(pPlayer, spellData))) {
                    return false;
                }
                this.doInscription(this.selectedSpellIndex);
            }
        } else {
            this.setSelectedSpell(pId);
        }
        return true;
    }

    private void setupResultSlot() {
        ItemStack resultStack = ItemStack.EMPTY;
        ItemStack spellBookStack = this.spellBookSlot.getItem();
        if (spellBookStack.getItem() instanceof SpellBook) {
            ISpellContainer spellList = ISpellContainer.get(spellBookStack);
            if (this.selectedSpellIndex >= 0) {
                SpellData spellData = spellList.getSpellAtIndex(this.selectedSpellIndex);
                if (spellData != SpellData.EMPTY && spellData.canRemove()) {
                    resultStack = new ItemStack(ItemRegistry.SCROLL.get());
                    resultStack.setCount(1);
                    ISpellContainer.createScrollContainer(spellData.getSpell(), spellData.getLevel(), resultStack);
                }
            }
        }
        if (!ItemStack.matches(resultStack, this.resultSlot.getItem())) {
            this.resultSlot.set(resultStack);
        }
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        Slot sourceSlot = (Slot) this.f_38839_.get(index);
        if (sourceSlot != null && sourceSlot.hasItem()) {
            ItemStack sourceStack = sourceSlot.getItem();
            ItemStack copyOfSourceStack = sourceStack.copy();
            if (index < 36) {
                if (!this.m_38903_(sourceStack, 36, 39, false)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (index >= 39) {
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
    public boolean stillValid(Player pPlayer) {
        return this.access.evaluate((level, blockPos) -> !level.getBlockState(blockPos).m_60713_(BlockRegistry.INSCRIPTION_TABLE_BLOCK.get()) ? false : pPlayer.m_20275_((double) blockPos.m_123341_() + 0.5, (double) blockPos.m_123342_() + 0.5, (double) blockPos.m_123343_() + 0.5) <= 64.0, true);
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; i++) {
            for (int l = 0; l < 9; l++) {
                this.m_38897_(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; i++) {
            this.m_38897_(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }

    @Override
    public void removed(Player pPlayer) {
        if (this.fromCurioSlot) {
            Utils.setPlayerSpellbookStack(pPlayer, this.spellBookSlot.remove(1));
        }
        super.removed(pPlayer);
        this.access.execute((p_39796_, p_39797_) -> this.m_150411_(pPlayer, this.inputSlots));
    }
}
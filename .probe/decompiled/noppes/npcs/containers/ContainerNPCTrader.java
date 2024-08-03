package noppes.npcs.containers;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import noppes.npcs.CustomContainer;
import noppes.npcs.EventHooks;
import noppes.npcs.NoppesUtilPlayer;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.api.event.RoleEvent;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.roles.RoleTrader;

public class ContainerNPCTrader extends ContainerNpcInterface {

    public RoleTrader role;

    private EntityNPCInterface npc;

    public ContainerNPCTrader(int containerId, Inventory playerInventory, int entityId) {
        super(CustomContainer.container_trader, containerId, playerInventory);
        this.npc = (EntityNPCInterface) playerInventory.player.m_9236_().getEntity(entityId);
        this.role = (RoleTrader) this.npc.role;
        for (int i = 0; i < 18; i++) {
            int x = 53;
            x += i % 3 * 72;
            int y = 7;
            y += i / 3 * 21;
            this.m_38897_(new Slot(this.role.inventorySold, i, x, y));
        }
        for (int i1 = 0; i1 < 3; i1++) {
            for (int l1 = 0; l1 < 9; l1++) {
                this.m_38897_(new Slot(playerInventory, l1 + i1 * 9 + 9, 32 + l1 * 18, 140 + i1 * 18));
            }
        }
        for (int j1 = 0; j1 < 9; j1++) {
            this.m_38897_(new Slot(playerInventory, j1, 32 + j1 * 18, 198));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player par1Player, int i) {
        return ItemStack.EMPTY;
    }

    @Override
    public void clicked(int i, int j, ClickType par3, Player entityplayer) {
        if (par3 == ClickType.PICKUP) {
            if (i < 0 || i >= 18) {
                super.m_150399_(i, j, par3, entityplayer);
            } else if (j != 1) {
                Slot slot = (Slot) this.f_38839_.get(i);
                if (slot != null && slot.getItem() != null && !slot.getItem().isEmpty()) {
                    ItemStack item = slot.getItem();
                    if (this.canGivePlayer(item, entityplayer)) {
                        ItemStack currency = this.role.inventoryCurrency.getItem(i);
                        ItemStack currency2 = this.role.inventoryCurrency.getItem(i + 18);
                        if (!this.canBuy(currency, currency2, entityplayer)) {
                            RoleEvent.TradeFailedEvent event = new RoleEvent.TradeFailedEvent(entityplayer, this.npc.wrappedNPC, item, currency, currency2);
                            EventHooks.onNPCRole(this.npc, event);
                            this.m_142503_(event.receiving == null ? ItemStack.EMPTY : event.receiving.getMCItemStack());
                        } else {
                            RoleEvent.TraderEvent event = new RoleEvent.TraderEvent(entityplayer, this.npc.wrappedNPC, item, currency, currency2);
                            if (!EventHooks.onNPCRole(this.npc, event)) {
                                if (event.currency1 != null && !event.currency1.isEmpty()) {
                                    currency = event.currency1.getMCItemStack();
                                }
                                if (event.currency2 != null && !event.currency2.isEmpty()) {
                                    currency2 = event.currency2.getMCItemStack();
                                }
                                if (this.canBuy(currency, currency2, entityplayer)) {
                                    NoppesUtilPlayer.consumeItem(entityplayer, currency, this.role.ignoreDamage, this.role.ignoreNBT);
                                    NoppesUtilPlayer.consumeItem(entityplayer, currency2, this.role.ignoreDamage, this.role.ignoreNBT);
                                    ItemStack soldItem = ItemStack.EMPTY;
                                    if (event.sold != null && !event.sold.isEmpty()) {
                                        soldItem = event.sold.getMCItemStack();
                                        this.givePlayer(soldItem.copy(), entityplayer);
                                    }
                                    this.m_142503_(soldItem);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public boolean canBuy(ItemStack currency, ItemStack currency2, Player player) {
        if (NoppesUtilServer.IsItemStackNull(currency) && NoppesUtilServer.IsItemStackNull(currency2)) {
            return true;
        } else {
            if (NoppesUtilServer.IsItemStackNull(currency)) {
                currency = currency2;
                currency2 = ItemStack.EMPTY;
            }
            if (NoppesUtilPlayer.compareItems(currency, currency2, this.role.ignoreDamage, this.role.ignoreNBT)) {
                currency = currency.copy();
                currency.grow(currency2.getCount());
                currency2 = ItemStack.EMPTY;
            }
            return NoppesUtilServer.IsItemStackNull(currency2) ? NoppesUtilPlayer.compareItems(player, currency, this.role.ignoreDamage, this.role.ignoreNBT) : NoppesUtilPlayer.compareItems(player, currency, this.role.ignoreDamage, this.role.ignoreNBT) && NoppesUtilPlayer.compareItems(player, currency2, this.role.ignoreDamage, this.role.ignoreNBT);
        }
    }

    private boolean canGivePlayer(ItemStack item, Player entityplayer) {
        ItemStack itemstack3 = entityplayer.containerMenu.getCarried();
        if (NoppesUtilServer.IsItemStackNull(itemstack3)) {
            return true;
        } else {
            if (NoppesUtilPlayer.compareItems(itemstack3, item, false, false)) {
                int k1 = item.getCount();
                if (k1 > 0 && k1 + itemstack3.getCount() <= itemstack3.getMaxStackSize()) {
                    return true;
                }
            }
            return false;
        }
    }

    private void givePlayer(ItemStack item, Player entityplayer) {
        ItemStack itemstack3 = entityplayer.containerMenu.getCarried();
        if (NoppesUtilServer.IsItemStackNull(itemstack3)) {
            entityplayer.containerMenu.setCarried(item);
        } else if (NoppesUtilPlayer.compareItems(itemstack3, item, false, false)) {
            int k1 = item.getCount();
            if (k1 > 0 && k1 + itemstack3.getCount() <= itemstack3.getMaxStackSize()) {
                itemstack3.grow(k1);
            }
        }
    }
}
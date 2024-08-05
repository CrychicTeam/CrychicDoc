package dev.xkmc.l2hostility.content.menu.equipments;

import dev.xkmc.l2library.base.menu.base.BaseContainerMenu;
import dev.xkmc.l2library.base.menu.base.PredSlot;
import dev.xkmc.l2library.base.menu.base.SpriteManager;
import dev.xkmc.l2library.util.Proxy;
import javax.annotation.Nullable;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class EquipmentsMenu extends BaseContainerMenu<EquipmentsMenu> {

    public static EquipmentSlot[] SLOTS = new EquipmentSlot[] { EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND, EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET };

    public static final SpriteManager MANAGER = new SpriteManager("l2hostility", "equipments");

    @Nullable
    protected final Mob golem;

    public static EquipmentsMenu fromNetwork(MenuType<EquipmentsMenu> type, int wid, Inventory plInv, FriendlyByteBuf buf) {
        Entity entity = Proxy.getClientWorld().getEntity(buf.readInt());
        return new EquipmentsMenu(type, wid, plInv, entity instanceof Mob golem ? golem : null);
    }

    protected EquipmentsMenu(MenuType<?> type, int wid, Inventory plInv, @Nullable Mob golem) {
        super(type, wid, plInv, MANAGER, EquipmentsContainer::new, false);
        this.golem = golem;
        this.addSlot("hand", (i, e) -> this.isValid(SLOTS[i], e));
        this.addSlot("armor", (i, e) -> this.isValid(SLOTS[i + 2], e));
    }

    private boolean isValid(EquipmentSlot slot, ItemStack stack) {
        if (this.golem != null && this.stillValid(this.inventory.player)) {
            EquipmentSlot exp = LivingEntity.getEquipmentSlotForItem(stack);
            return exp == slot ? true : !exp.isArmor();
        } else {
            return false;
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return this.golem != null && !this.golem.m_213877_();
    }

    @Override
    public PredSlot getAsPredSlot(String name, int i, int j) {
        return super.getAsPredSlot(name, i, j);
    }

    @Override
    public ItemStack quickMoveStack(Player pl, int id) {
        if (this.golem != null) {
            ItemStack stack = ((Slot) this.f_38839_.get(id)).getItem();
            if (id >= 36) {
                this.m_38903_(stack, 0, 36, true);
            } else {
                for (int i = 0; i < 6; i++) {
                    if (SLOTS[i] == LivingEntity.getEquipmentSlotForItem(stack)) {
                        this.m_38903_(stack, 36 + i, 37 + i, false);
                    }
                }
            }
            this.container.setChanged();
        }
        return ItemStack.EMPTY;
    }
}
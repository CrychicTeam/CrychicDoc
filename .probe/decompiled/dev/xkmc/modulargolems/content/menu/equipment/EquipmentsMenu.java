package dev.xkmc.modulargolems.content.menu.equipment;

import dev.xkmc.l2library.base.menu.base.BaseContainerMenu;
import dev.xkmc.l2library.base.menu.base.PredSlot;
import dev.xkmc.l2library.base.menu.base.SpriteManager;
import dev.xkmc.l2library.util.Proxy;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.entity.humanoid.HumanoidGolemEntity;
import dev.xkmc.modulargolems.content.entity.metalgolem.MetalGolemEntity;
import dev.xkmc.modulargolems.content.item.equipments.MetalGolemArmorItem;
import dev.xkmc.modulargolems.content.item.equipments.MetalGolemBeaconItem;
import dev.xkmc.modulargolems.content.item.equipments.MetalGolemWeaponItem;
import dev.xkmc.modulargolems.content.item.golem.GolemHolder;
import dev.xkmc.modulargolems.events.event.GolemEquipEvent;
import javax.annotation.Nullable;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;

public class EquipmentsMenu extends BaseContainerMenu<EquipmentsMenu> {

    public static EquipmentSlot[] SLOTS = new EquipmentSlot[] { EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND, EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET };

    public static final SpriteManager MANAGER = new SpriteManager("modulargolems", "equipments");

    public static final SpriteManager EXTRA = new SpriteManager("modulargolems", "equipments_extra");

    public final AbstractGolemEntity<?, ?> golem;

    public static EquipmentsMenu fromNetwork(MenuType<EquipmentsMenu> type, int wid, Inventory plInv, FriendlyByteBuf buf) {
        assert Proxy.getClientWorld() != null;
        Entity entity = Proxy.getClientWorld().getEntity(buf.readInt());
        return new EquipmentsMenu(type, wid, plInv, entity instanceof AbstractGolemEntity<?, ?> golem ? golem : null);
    }

    protected EquipmentsMenu(MenuType<?> type, int wid, Inventory plInv, @Nullable AbstractGolemEntity<?, ?> golem) {
        super(type, wid, plInv, golem instanceof HumanoidGolemEntity ? EXTRA : MANAGER, EquipmentsContainer::new, false);
        this.golem = golem;
        this.addSlot("hand", (i, e) -> this.isValid(SLOTS[i], e));
        this.addSlot("armor", (i, e) -> this.isValid(SLOTS[i + 2], e));
        if (golem instanceof HumanoidGolemEntity) {
            this.addSlot("backup", e -> this.isValid(EquipmentSlot.MAINHAND, e));
            this.addSlot("arrow", ItemStack::m_41753_);
        }
    }

    private boolean isValid(EquipmentSlot slot, ItemStack stack) {
        return this.getSlotForItem(stack) == slot;
    }

    @Override
    public boolean stillValid(Player player) {
        if (this.golem == null) {
            return false;
        } else {
            this.golem.inventoryTick = 5;
            return this.golem.m_6084_() && !this.golem.m_213877_();
        }
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
                EquipmentSlot es = this.getSlotForItem(stack);
                for (int i = 0; i < 6; i++) {
                    if (SLOTS[i] == es) {
                        this.m_38903_(stack, 36 + i, 37 + i, false);
                    }
                }
            }
            this.container.setChanged();
        }
        return ItemStack.EMPTY;
    }

    @Nullable
    public EquipmentSlot getSlotForItem(ItemStack stack) {
        if (!this.stillValid(this.inventory.player) || this.golem == null) {
            return null;
        } else if (!stack.getItem().canFitInsideContainerItems()) {
            return null;
        } else if (stack.getItem() instanceof GolemHolder) {
            return null;
        } else if (this.golem instanceof HumanoidGolemEntity humanoidGolem) {
            GolemEquipEvent event = new GolemEquipEvent(humanoidGolem, stack);
            MinecraftForge.EVENT_BUS.post(event);
            return event.canEquip() ? event.getSlot() : null;
        } else {
            if (this.golem instanceof MetalGolemEntity) {
                if (stack.getItem() instanceof MetalGolemArmorItem mgai) {
                    return mgai.getSlot();
                }
                if (stack.getItem() instanceof MetalGolemBeaconItem) {
                    return EquipmentSlot.FEET;
                }
                if (stack.getItem() instanceof MetalGolemWeaponItem) {
                    return EquipmentSlot.MAINHAND;
                }
                if (stack.getItem() instanceof BannerItem) {
                    if (this.golem.m_6844_(EquipmentSlot.HEAD).isEmpty()) {
                        return EquipmentSlot.HEAD;
                    }
                    return EquipmentSlot.FEET;
                }
            }
            return null;
        }
    }
}
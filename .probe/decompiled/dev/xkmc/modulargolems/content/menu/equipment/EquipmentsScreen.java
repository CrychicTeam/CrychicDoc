package dev.xkmc.modulargolems.content.menu.equipment;

import dev.xkmc.l2library.base.menu.base.BaseContainerScreen;
import dev.xkmc.l2library.base.menu.base.MenuLayoutConfig;
import dev.xkmc.modulargolems.content.entity.humanoid.HumanoidGolemEntity;
import dev.xkmc.modulargolems.content.menu.registry.EquipmentGroup;
import dev.xkmc.modulargolems.content.menu.registry.GolemTabRegistry;
import dev.xkmc.modulargolems.content.menu.tabs.GolemTabManager;
import dev.xkmc.modulargolems.content.menu.tabs.ITabScreen;
import dev.xkmc.modulargolems.init.data.MGLangData;
import java.util.List;
import java.util.Optional;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class EquipmentsScreen extends BaseContainerScreen<EquipmentsMenu> implements ITabScreen {

    public EquipmentsScreen(EquipmentsMenu cont, Inventory plInv, Component title) {
        super(cont, plInv, title);
    }

    @Override
    protected void renderBg(GuiGraphics g, float pTick, int mx, int my) {
        MenuLayoutConfig.ScreenRenderer sr = ((EquipmentsMenu) this.f_97732_).sprite.get().getRenderer(this);
        sr.start(g);
        if (((EquipmentsMenu) this.f_97732_).getAsPredSlot("hand", 0, 1).m_7993_().isEmpty()) {
            sr.draw(g, "hand", "altas_shield", 0, 18);
        }
        if (((EquipmentsMenu) this.f_97732_).getAsPredSlot("armor", 0, 0).m_7993_().isEmpty()) {
            sr.draw(g, "armor", "altas_helmet", 0, 0);
        }
        if (((EquipmentsMenu) this.f_97732_).getAsPredSlot("armor", 0, 1).m_7993_().isEmpty()) {
            sr.draw(g, "armor", "altas_chestplate", 0, 18);
        }
        if (((EquipmentsMenu) this.f_97732_).getAsPredSlot("armor", 0, 2).m_7993_().isEmpty()) {
            sr.draw(g, "armor", "altas_leggings", 0, 36);
        }
        if (((EquipmentsMenu) this.f_97732_).getAsPredSlot("armor", 0, 3).m_7993_().isEmpty()) {
            sr.draw(g, "armor", "altas_boots", 0, 54);
        }
        if (((EquipmentsMenu) this.f_97732_).golem instanceof HumanoidGolemEntity && ((EquipmentsMenu) this.f_97732_).getAsPredSlot("arrow", 0, 0).m_7993_().isEmpty()) {
            sr.draw(g, "arrow", "slotbg_arrow", -1, -1);
        }
    }

    @Override
    protected void init() {
        super.m_7856_();
        new GolemTabManager<>(this, new EquipmentGroup(((EquipmentsMenu) this.f_97732_).golem)).init(x$0 -> {
            AbstractWidget var10000 = (AbstractWidget) this.m_142416_(x$0);
        }, GolemTabRegistry.EQUIPMENT);
    }

    @Override
    protected void renderTooltip(GuiGraphics g, int mx, int my) {
        super.m_280072_(g, mx, my);
        if (((EquipmentsMenu) this.f_97732_).golem instanceof HumanoidGolemEntity && ((EquipmentsMenu) this.f_97732_).m_142621_().isEmpty() && this.f_97734_ != null && !this.f_97734_.hasItem()) {
            List<Component> list = null;
            if (this.f_97734_.getContainerSlot() == 0) {
                list = List.of(MGLangData.SLOT_MAIN.get(), MGLangData.SLOT_MAIN_DESC.get());
            }
            if (this.f_97734_.getContainerSlot() == 1) {
                list = List.of(MGLangData.SLOT_OFF.get());
            }
            if (this.f_97734_.getContainerSlot() == 6) {
                list = List.of(MGLangData.SLOT_BACKUP.get(), MGLangData.SLOT_BACKUP_DESC.get(), MGLangData.SLOT_BACKUP_INFO.get());
            }
            if (this.f_97734_.getContainerSlot() == 7) {
                list = List.of(MGLangData.SLOT_ARROW.get(), MGLangData.SLOT_ARROW_DESC.get());
            }
            if (list != null) {
                g.renderTooltip(this.f_96547_, list, Optional.empty(), ItemStack.EMPTY, mx, my);
            }
        }
    }

    @Override
    public int screenWidth() {
        return this.f_96543_;
    }

    @Override
    public int screenHeight() {
        return this.f_96544_;
    }
}
package dev.xkmc.modulargolems.compat.curio;

import dev.xkmc.l2tabs.compat.BaseCuriosListScreen;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.menu.registry.EquipmentGroup;
import dev.xkmc.modulargolems.content.menu.tabs.GolemTabManager;
import dev.xkmc.modulargolems.content.menu.tabs.ITabScreen;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class GolemCuriosListScreen extends BaseCuriosListScreen<GolemCuriosListMenu> implements ITabScreen {

    public GolemCuriosListScreen(GolemCuriosListMenu cont, Inventory plInv, Component title) {
        super(cont, plInv, title);
    }

    public void init() {
        super.m_7856_();
        CurioCompatRegistry compat = CurioCompatRegistry.get();
        assert compat != null;
        new GolemTabManager<>(this, new EquipmentGroup((AbstractGolemEntity<?, ?>) ((GolemCuriosListMenu) this.f_97732_).curios.entity)).init(x$0 -> {
            AbstractWidget var10000 = (AbstractWidget) this.m_142416_(x$0);
        }, compat.tab);
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
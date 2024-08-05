package io.github.lightman314.lightmanscurrency.client.gui.screen.inventory;

import io.github.lightman314.lightmanscurrency.LCText;
import io.github.lightman314.lightmanscurrency.api.misc.client.rendering.EasyGuiGraphics;
import io.github.lightman314.lightmanscurrency.client.gui.easy.EasyMenuScreen;
import io.github.lightman314.lightmanscurrency.client.gui.widget.button.icon.IconButton;
import io.github.lightman314.lightmanscurrency.client.gui.widget.easy.EasyAddonHelper;
import io.github.lightman314.lightmanscurrency.client.gui.widget.easy.EasyButton;
import io.github.lightman314.lightmanscurrency.client.util.IconAndButtonUtil;
import io.github.lightman314.lightmanscurrency.client.util.ScreenArea;
import io.github.lightman314.lightmanscurrency.common.emergency_ejection.EjectionData;
import io.github.lightman314.lightmanscurrency.common.menus.EjectionRecoveryMenu;
import javax.annotation.Nonnull;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.common.util.NonNullSupplier;

public class EjectionRecoveryScreen extends EasyMenuScreen<EjectionRecoveryMenu> {

    public static final ResourceLocation GUI_TEXTURE = new ResourceLocation("textures/gui/container/generic_54.png");

    EasyButton buttonLeft;

    EasyButton buttonRight;

    public EjectionRecoveryScreen(EjectionRecoveryMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.resize(176, 222);
    }

    @Override
    protected void initialize(ScreenArea screenArea) {
        super.init();
        this.buttonLeft = this.addChild(new IconButton(screenArea.pos.offset(-20, 0), b -> this.changeSelection(-1), IconAndButtonUtil.ICON_LEFT).withAddons(EasyAddonHelper.activeCheck((NonNullSupplier<Boolean>) (() -> ((EjectionRecoveryMenu) this.f_97732_).getSelectedIndex() > 0))));
        this.buttonRight = this.addChild(new IconButton(screenArea.pos.offset(screenArea.width, 0), b -> this.changeSelection(1), IconAndButtonUtil.ICON_RIGHT).withAddons(EasyAddonHelper.activeCheck((NonNullSupplier<Boolean>) (() -> ((EjectionRecoveryMenu) this.f_97732_).getSelectedIndex() < ((EjectionRecoveryMenu) this.f_97732_).getValidEjectionData().size() - 1))));
    }

    @Override
    protected void renderBG(@Nonnull EasyGuiGraphics gui) {
        gui.renderNormalBackground(GUI_TEXTURE, this);
        gui.drawString(this.getTraderTitle(), this.f_97728_, this.f_97729_, 4210752);
        gui.drawString(this.f_169604_, this.f_97730_, this.f_97727_ - 94, 4210752);
    }

    private Component getTraderTitle() {
        EjectionData data = ((EjectionRecoveryMenu) this.f_97732_).getSelectedData();
        return data != null ? data.getTraderName() : LCText.GUI_EJECTION_NO_DATA.get();
    }

    private void changeSelection(int delta) {
        int newSelection = ((EjectionRecoveryMenu) this.f_97732_).getSelectedIndex() + delta;
        ((EjectionRecoveryMenu) this.f_97732_).changeSelection(newSelection);
    }
}
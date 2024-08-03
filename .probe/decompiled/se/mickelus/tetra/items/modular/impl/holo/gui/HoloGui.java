package se.mickelus.tetra.items.modular.impl.holo.gui;

import java.util.List;
import java.util.Optional;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.tetra.ConfigHandler;
import se.mickelus.tetra.data.DataManager;
import se.mickelus.tetra.gui.GuiSpinner;
import se.mickelus.tetra.items.modular.IModularItem;
import se.mickelus.tetra.items.modular.impl.holo.HoloPage;
import se.mickelus.tetra.items.modular.impl.holo.gui.craft.HoloCraftRootGui;
import se.mickelus.tetra.items.modular.impl.holo.gui.scan.HoloScanRootGui;
import se.mickelus.tetra.items.modular.impl.holo.gui.system.HoloSystemRootGui;
import se.mickelus.tetra.module.schematic.UpgradeSchematic;

@ParametersAreNonnullByDefault
@OnlyIn(Dist.CLIENT)
public class HoloGui extends Screen {

    private static final Logger logger = LogManager.getLogger();

    private static HoloGui instance = null;

    private static boolean hasListener = false;

    private final HoloHeaderGui header;

    private final HoloRootBaseGui[] pages;

    private final GuiElement defaultGui;

    private final GuiElement spinner;

    private HoloRootBaseGui currentPage;

    private Runnable closeCallback;

    public HoloGui() {
        super(Component.literal("tetra:holosphere"));
        this.f_96543_ = 320;
        this.f_96544_ = 240;
        this.defaultGui = new GuiElement(0, 0, this.f_96543_, this.f_96544_);
        this.header = new HoloHeaderGui(0, 0, this.f_96543_, this::changePage);
        this.defaultGui.addChild(this.header);
        this.pages = new HoloRootBaseGui[HoloPage.values().length];
        this.pages[0] = new HoloCraftRootGui(0, 18);
        this.defaultGui.addChild(this.pages[0]);
        this.pages[1] = new HoloScanRootGui(0, 18);
        this.pages[1].setVisible(false);
        this.defaultGui.addChild(this.pages[1]);
        this.pages[2] = new HoloSystemRootGui(0, 18);
        this.pages[2].setVisible(false);
        this.defaultGui.addChild(this.pages[2]);
        this.currentPage = this.pages[0];
        this.spinner = new GuiSpinner(-8, 6);
        this.spinner.setVisible(false);
        this.defaultGui.addChild(this.spinner);
        if (ConfigHandler.development.get() && !hasListener) {
            DataManager.instance.destabilizationData.onReload(() -> Minecraft.getInstance().m_18709_(HoloGui::onReload));
            hasListener = true;
        }
    }

    public static HoloGui getInstance() {
        if (instance == null) {
            instance = new HoloGui();
        }
        return instance;
    }

    private static void onReload() {
        if (instance != null && instance.getMinecraft().screen == instance) {
            logger.info("Refreshing holosphere gui data");
            instance.spinner.setVisible(false);
            if (instance.currentPage != null) {
                instance.currentPage.onReload();
            }
        }
    }

    public void openSchematic(IModularItem item, ItemStack itemStack, String slot, UpgradeSchematic schematic, Runnable closeCallback) {
        this.changePage(HoloPage.craft);
        ((HoloCraftRootGui) this.pages[0]).updateState(item, itemStack, slot, schematic);
        this.closeCallback = closeCallback;
    }

    @Override
    public void removed() {
        super.removed();
        if (this.closeCallback != null) {
            Runnable callback = this.closeCallback;
            this.closeCallback = null;
            callback.run();
        }
    }

    public void onShow() {
        this.header.onShow();
        this.currentPage.animateOpen();
    }

    private void changePage(HoloPage page) {
        this.header.changePage(page);
        for (int i = 0; i < this.pages.length; i++) {
            this.pages[i].setVisible(page.ordinal() == i);
        }
        this.currentPage = this.pages[page.ordinal()];
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        this.m_280273_(graphics);
        super.render(graphics, mouseX, mouseY, partialTicks);
        this.defaultGui.updateFocusState((this.f_96543_ - this.defaultGui.getWidth()) / 2, (this.f_96544_ - this.defaultGui.getHeight()) / 2, mouseX, mouseY);
        this.defaultGui.draw(graphics, (this.f_96543_ - this.defaultGui.getWidth()) / 2, (this.f_96544_ - this.defaultGui.getHeight()) / 2, this.f_96543_, this.f_96544_, mouseX, mouseY, 1.0F);
        this.renderHoveredToolTip(graphics, mouseX, mouseY);
    }

    protected void renderHoveredToolTip(GuiGraphics graphics, int mouseX, int mouseY) {
        List<Component> tooltipLines = this.defaultGui.getTooltipLines();
        if (tooltipLines != null) {
            graphics.renderTooltip(this.f_96547_, tooltipLines, Optional.empty(), mouseX, mouseY);
        }
    }

    @Override
    public boolean mouseClicked(double x, double y, int button) {
        return this.defaultGui.onMouseClick((int) x, (int) y, button) ? true : super.m_6375_(x, y, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double distance) {
        return this.currentPage.onMouseScroll(mouseX, mouseY, distance) ? true : super.m_6050_(mouseX, mouseY, distance);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return this.currentPage.onKeyPress(keyCode, scanCode, modifiers) ? true : super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        return this.currentPage.onKeyRelease(keyCode, scanCode, modifiers) ? true : super.m_7920_(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char typedChar, int keyCode) {
        if (this.currentPage.onCharType(typedChar, keyCode)) {
            return true;
        } else {
            if (ConfigHandler.development.get()) {
                switch(typedChar) {
                    case 'r':
                        instance = null;
                        Minecraft.getInstance().setScreen(null);
                        HoloGui gui = getInstance();
                        Minecraft.getInstance().setScreen(gui);
                        gui.onShow();
                        break;
                    case 't':
                        this.getMinecraft().player.connection.sendUnsignedCommand("reload");
                        this.spinner.setVisible(true);
                }
            }
            return false;
        }
    }
}
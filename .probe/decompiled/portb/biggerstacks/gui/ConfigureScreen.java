package portb.biggerstacks.gui;

import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.MultiLineLabel;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import portb.biggerstacks.Constants;
import portb.biggerstacks.net.ClientboundConfigureScreenOpenPacket;
import portb.biggerstacks.net.PacketHandler;
import portb.biggerstacks.net.ServerboundCreateConfigTemplatePacket;
import portb.configlib.template.TemplateOptions;

@OnlyIn(Dist.CLIENT)
public class ConfigureScreen extends Screen {

    private static final int WIDTH = 200;

    private static final int HEIGHT = 180;

    private final TemplateOptions previousOptions;

    private final boolean isAlreadyUsingCustomFile;

    private MultiLineLabel OVERWRITE_WARNING_LABEL;

    private EditBox potionsBox;

    private EditBox enchBooksBox;

    private EditBox normalItemsBox;

    private Button confirmButton;

    protected ConfigureScreen(ClientboundConfigureScreenOpenPacket options) {
        super(Component.literal("Configure stack size"));
        this.previousOptions = options;
        this.isAlreadyUsingCustomFile = options.isAlreadyUsingCustomFile();
    }

    public static void open(ClientboundConfigureScreenOpenPacket packet) {
        Minecraft.getInstance().setScreen(new ConfigureScreen(packet));
    }

    private static boolean isEditBoxInputValid(String inputString) {
        if (!inputString.matches("[0-9]+")) {
            return false;
        } else {
            try {
                int value = Integer.parseInt(inputString);
                return value > 0 && value <= 1073741823;
            } catch (NumberFormatException var2) {
                return false;
            }
        }
    }

    @Override
    protected void init() {
        int relX = (this.f_96543_ - 200) / 2;
        int relY = (this.f_96544_ - 180) / 2;
        int editBoxStartX = 120;
        int editBoxStartY = 30;
        this.OVERWRITE_WARNING_LABEL = MultiLineLabel.create(this.f_96547_, Component.translatable("biggerstacks.overwrite.warn").withStyle(Style.EMPTY.withColor(16755370)), 200);
        this.normalItemsBox = new EditBoxWithADifferentBorderColour(this.f_96547_, relX + editBoxStartX, relY + editBoxStartY, 60, 20, Component.translatable("biggerstacks.normalbox.alt"));
        this.potionsBox = new EditBoxWithADifferentBorderColour(this.f_96547_, relX + editBoxStartX, relY + editBoxStartY + 30, 60, 20, Component.translatable("biggerstacks.potsbox.alt"));
        this.enchBooksBox = new EditBoxWithADifferentBorderColour(this.f_96547_, relX + editBoxStartX, relY + editBoxStartY + 60, 60, 20, Component.translatable("biggerstacks.enchbox.alt"));
        this.enchBooksBox.setValue(String.valueOf(this.previousOptions.getEnchBookLimit()));
        this.potionsBox.setValue(String.valueOf(this.previousOptions.getPotionStackLimit()));
        this.normalItemsBox.setValue(String.valueOf(this.previousOptions.getNormalStackLimit()));
        this.enchBooksBox.setResponder(this.verifyInputBoxNumber(this.enchBooksBox));
        this.potionsBox.setResponder(this.verifyInputBoxNumber(this.potionsBox));
        this.normalItemsBox.setResponder(this.verifyInputBoxNumber(this.normalItemsBox));
        this.confirmButton = new Button.Builder(Component.translatable("biggerstacks.save"), this::onConfirmButtonClicked).bounds(relX + 60, relY + 180 - 30, 80, 20).build();
        this.m_142416_(this.normalItemsBox);
        this.m_142416_(this.potionsBox);
        this.m_142416_(this.enchBooksBox);
        this.m_142416_(this.confirmButton);
        super.init();
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(graphics);
        int relX = (this.f_96543_ - 200) / 2;
        int relY = (this.f_96544_ - 180) / 2;
        graphics.drawCenteredString(this.f_96547_, Component.translatable("biggerstacks.config.title"), this.f_96543_ / 2, relY + 10, 16777215);
        int centreOffset = (20 - 9) / 2;
        int labelStartX = 20;
        int labelStartY = 30;
        graphics.drawString(this.f_96547_, Component.translatable("biggerstacks.normalbox.label"), relX + labelStartX, centreOffset + relY + labelStartY, 16777215);
        graphics.drawString(this.f_96547_, Component.translatable("biggerstacks.potsbox.label"), relX + labelStartX, centreOffset + relY + labelStartY + 30, 16777215);
        graphics.drawString(this.f_96547_, Component.translatable("biggerstacks.enchbox.label"), relX + labelStartX, centreOffset + relY + labelStartY + 60, 16777215);
        if (this.isAlreadyUsingCustomFile) {
            this.OVERWRITE_WARNING_LABEL.renderCentered(graphics, this.f_96543_ / 2, relY + 125);
        }
        super.render(graphics, pMouseX, pMouseY, pPartialTick);
    }

    @Override
    public void renderBackground(GuiGraphics graphics) {
        int relX = (this.f_96543_ - 200) / 2;
        int relY = (this.f_96544_ - 180) / 2;
        graphics.blitWithBorder(Constants.CONFIG_GUI_BG, relX, relY, 0, 0, 200, 180, 256, 256, 4);
    }

    private void onConfirmButtonClicked(Button button) {
        if (this.isStateValid()) {
            this.submit();
        }
    }

    @Override
    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
        if (pKeyCode == 257 && this.isStateValid()) {
            this.submit();
            return true;
        } else {
            return super.keyPressed(pKeyCode, pScanCode, pModifiers);
        }
    }

    private boolean isStateValid() {
        return isEditBoxInputValid(this.normalItemsBox.getValue()) && isEditBoxInputValid(this.enchBooksBox.getValue()) && isEditBoxInputValid(this.potionsBox.getValue());
    }

    private void submit() {
        PacketHandler.INSTANCE.sendToServer(new ServerboundCreateConfigTemplatePacket(Integer.parseInt(this.normalItemsBox.getValue()), Integer.parseInt(this.potionsBox.getValue()), Integer.parseInt(this.enchBooksBox.getValue())));
        Minecraft.getInstance().setScreen(null);
    }

    @Override
    public boolean isPauseScreen() {
        return true;
    }

    private Consumer<String> verifyInputBoxNumber(EditBox editBox) {
        return inputString -> {
            if (isEditBoxInputValid(inputString)) {
                editBox.setTextColor(16777215);
                this.confirmButton.f_93623_ = true;
            } else {
                editBox.setTextColor(16711680);
                this.confirmButton.f_93623_ = false;
            }
        };
    }
}
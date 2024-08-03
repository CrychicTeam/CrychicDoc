package net.mehvahdjukaar.supplementaries.client.screens;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.mehvahdjukaar.supplementaries.client.screens.widgets.DrawableBlackBoardButton;
import net.mehvahdjukaar.supplementaries.client.screens.widgets.DyeBlackBoardButton;
import net.mehvahdjukaar.supplementaries.common.block.tiles.BlackboardBlockTile;
import net.mehvahdjukaar.supplementaries.common.network.ModNetwork;
import net.mehvahdjukaar.supplementaries.common.network.ServerBoundSetBlackboardPacket;
import net.mehvahdjukaar.supplementaries.common.utils.CircularList;
import net.mehvahdjukaar.supplementaries.configs.CommonConfigs;
import net.mehvahdjukaar.supplementaries.integration.CompatHandler;
import net.mehvahdjukaar.supplementaries.integration.ImmediatelyFastCompat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class BlackBoardScreen extends Screen {

    private static final MutableComponent CLEAR = Component.translatable("gui.supplementaries.blackboard.clear");

    private static final MutableComponent UNDO = Component.translatable("gui.supplementaries.blackboard.undo");

    private static final MutableComponent EDIT = Component.translatable("gui.supplementaries.blackboard.edit");

    private final BlackboardBlockTile tile;

    private final DrawableBlackBoardButton[][] buttons = new DrawableBlackBoardButton[16][16];

    private final Deque<List<BlackBoardScreen.Entry>> history = new CircularList<List<BlackBoardScreen.Entry>>(20);

    private List<BlackBoardScreen.Entry> currentHistoryStep = new ArrayList();

    private Button historyButton;

    private byte selectedColor = 1;

    private BlackBoardScreen(BlackboardBlockTile teBoard) {
        super(EDIT);
        this.tile = teBoard;
    }

    public static void open(BlackboardBlockTile sign) {
        Minecraft.getInstance().setScreen(new BlackBoardScreen(sign));
    }

    @Override
    public void tick() {
        if (!this.isValid()) {
            this.onClose();
        } else if (!(this.m_7222_() instanceof DrawableBlackBoardButton)) {
            this.m_7522_(null);
        }
    }

    private boolean isValid() {
        return this.f_96541_ != null && this.f_96541_.player != null && !this.tile.m_58901_() && !this.tile.playerIsTooFarAwayToEdit(this.tile.m_58904_(), this.tile.m_58899_(), this.f_96541_.player.m_20148_());
    }

    public byte getSelectedColor() {
        return this.selectedColor;
    }

    public void setSelectedColor(byte color) {
        this.selectedColor = color;
    }

    @Override
    public void onClose() {
        this.tile.setChanged();
        super.onClose();
    }

    @Override
    public void removed() {
        byte[][] pixels = new byte[16][16];
        for (int xx = 0; xx < 16; xx++) {
            for (int yy = 0; yy < 16; yy++) {
                pixels[xx][yy] = this.buttons[xx][yy].getColor();
            }
        }
        ModNetwork.CHANNEL.sendToServer(new ServerBoundSetBlackboardPacket(this.tile.m_58899_(), pixels));
    }

    public void updateBlackboard(int x, int y, byte newColor) {
        this.tile.setPixel(x, y, newColor);
    }

    public void addHistory(int x, int y, byte oldColor) {
        this.currentHistoryStep.add(new BlackBoardScreen.Entry(x, y, oldColor));
    }

    public void saveHistoryStep() {
        if (!this.currentHistoryStep.isEmpty()) {
            this.history.add(this.currentHistoryStep);
            this.currentHistoryStep = new ArrayList();
            this.historyButton.f_93623_ = true;
        }
    }

    public void onButtonDragged(double mx, double my, byte buttonValue) {
        for (int xx = 0; xx < 16; xx++) {
            for (int yy = 0; yy < 16; yy++) {
                DrawableBlackBoardButton b = this.buttons[xx][yy];
                if (b.m_5953_(mx, my) && b.getColor() != buttonValue) {
                    b.setColor(buttonValue);
                }
            }
        }
    }

    private void clearPressed(Button button) {
        for (int xx = 0; xx < 16; xx++) {
            for (int yy = 0; yy < 16; yy++) {
                this.buttons[xx][yy].setColor((byte) 0);
            }
        }
        this.saveHistoryStep();
    }

    private void undoPressed(Button button) {
        if (!this.history.isEmpty()) {
            for (BlackBoardScreen.Entry v : (List) this.history.pollLast()) {
                this.buttons[v.x()][v.y()].setColor(v.color());
            }
            this.currentHistoryStep.clear();
        }
        if (this.history.isEmpty()) {
            this.historyButton.f_93623_ = false;
        }
    }

    @Override
    protected void init() {
        for (int xx = 0; xx < 16; xx++) {
            for (int yy = 0; yy < 16; yy++) {
                byte pixel = this.tile.getPixel(xx, yy);
                DrawableBlackBoardButton widget = new DrawableBlackBoardButton(this, this.f_96543_ / 2, 65, xx, yy, pixel);
                this.buttons[xx][yy] = (DrawableBlackBoardButton) this.m_142416_(widget);
            }
        }
        int buttonW = 56;
        int sep = 4;
        this.m_142416_(Button.builder(CLEAR, this::clearPressed).bounds(this.f_96543_ / 2 - buttonW / 2 - buttonW + sep / 2, this.f_96544_ / 4 + 120, buttonW - sep, 20).build());
        this.m_142416_(Button.builder(CommonComponents.GUI_DONE, button -> this.onClose()).bounds(this.f_96543_ / 2 - buttonW / 2 + sep / 2, this.f_96544_ / 4 + 120, buttonW - sep, 20).build());
        this.historyButton = (Button) this.m_142416_(Button.builder(UNDO, this::undoPressed).bounds(this.f_96543_ / 2 + buttonW / 2 + sep / 2, this.f_96544_ / 4 + 120, buttonW - sep, 20).build());
        if ((Boolean) CommonConfigs.Building.BLACKBOARD_COLOR.get() || PlatHelper.isDev()) {
            for (byte b = 0; b < 16; b++) {
                int ox = b % 2;
                int oy = b / 2;
                this.m_142416_(new DyeBlackBoardButton(this, this.f_96543_ / 2 - 78 + ox * 10, 73 + oy * 10, b));
            }
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        this.m_280273_(graphics);
        graphics.drawCenteredString(this.f_96547_, this.f_96539_, this.f_96543_ / 2, 40, 16777215);
        if (CompatHandler.IMMEDIATELY_FAST) {
            ImmediatelyFastCompat.startBatching();
        }
        super.render(graphics, mouseX, mouseY, partialTicks);
        graphics.pose().pushPose();
        label30: for (int xx = 0; xx < 16; xx++) {
            for (int yy = 0; yy < 16; yy++) {
                DrawableBlackBoardButton button = this.buttons[xx][yy];
                if (button.isShouldDrawOverlay()) {
                    button.renderHoverOverlay(graphics);
                    break label30;
                }
            }
        }
        graphics.pose().popPose();
        if (CompatHandler.IMMEDIATELY_FAST) {
            ImmediatelyFastCompat.endBatching();
        }
    }

    private static record Entry(int x, int y, byte color) {
    }
}
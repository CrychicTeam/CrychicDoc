package net.mehvahdjukaar.supplementaries.client.screens;

import net.mehvahdjukaar.supplementaries.SuppPlatformStuff;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.MultiLineLabel;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import org.jetbrains.annotations.Nullable;

public class WelcomeMessageScreen extends Screen {

    private final Screen lastScreen;

    private final Component text;

    private final Component url;

    private final Runnable onTurnOff;

    private int ticksUntilEnable;

    private MultiLineLabel message = MultiLineLabel.EMPTY;

    private MultiLineLabel suggestions = MultiLineLabel.EMPTY;

    private Button exitButton;

    private Button disaleButton;

    private static final Component OF_TEXT = Component.translatable("gui.supplementaries.optifine.message");

    private static final Component OF_URL = Component.translatable("gui.supplementaries.optifine.suggestions").withStyle(Style.EMPTY.withColor(ChatFormatting.GOLD).applyFormat(ChatFormatting.UNDERLINE).withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://optifine.alternatives.lambdaurora.dev/")));

    private static final Component OF_TITLE = Component.translatable("gui.supplementaries.optifine.title").withStyle(ChatFormatting.RED).withStyle(ChatFormatting.BOLD);

    private static final Component AM_TEXT = Component.translatable("gui.supplementaries.amendments.message");

    private static final Component AM_URL = Component.translatable("gui.supplementaries.amendments.suggestions").withStyle(Style.EMPTY.withColor(ChatFormatting.GREEN).applyFormat(ChatFormatting.UNDERLINE).withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://legacy.curseforge.com/minecraft/mc-mods/amendments")));

    private static final Component AM_TITLE = Component.translatable("gui.supplementaries.amendments.title").withStyle(ChatFormatting.GOLD).withStyle(ChatFormatting.BOLD);

    public WelcomeMessageScreen(Screen screen, int ticksUntilEnable, Component title, Component text, Component url, Runnable onTurnOff) {
        super(title);
        this.lastScreen = screen;
        this.ticksUntilEnable = ticksUntilEnable;
        this.text = text;
        this.url = url;
        this.onTurnOff = onTurnOff;
    }

    @Override
    public Component getNarrationMessage() {
        return CommonComponents.joinForNarration(super.getNarrationMessage(), this.text);
    }

    @Override
    protected void init() {
        super.init();
        this.exitButton = (Button) this.m_142416_(Button.builder(CommonComponents.GUI_PROCEED, pressed -> Minecraft.getInstance().setScreen(this.lastScreen)).bounds(this.f_96543_ / 2 + 5, this.f_96544_ * 5 / 6, 150, 20).build());
        this.exitButton.f_93623_ = false;
        this.disaleButton = (Button) this.m_142416_(Button.builder(Component.translatable("gui.supplementaries.welcome_screen.turn_off"), pressed -> {
            Minecraft.getInstance().setScreen(this.lastScreen);
            this.onTurnOff.run();
        }).bounds(this.f_96543_ / 2 - 155, this.f_96544_ * 5 / 6, 150, 20).build());
        this.disaleButton.f_93623_ = false;
        this.message = MultiLineLabel.create(this.f_96547_, this.text, this.f_96543_ - 50);
        this.suggestions = MultiLineLabel.create(this.f_96547_, this.url, this.f_96543_ - 50);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        this.m_280273_(graphics);
        graphics.drawCenteredString(this.f_96547_, this.f_96539_, this.f_96543_ / 2, 30, 16777215);
        this.message.renderCentered(graphics, this.f_96543_ / 2, 55);
        this.suggestions.renderCentered(graphics, this.f_96543_ / 2, 180);
        super.render(graphics, mouseX, mouseY, partialTicks);
        this.exitButton.m_88315_(graphics, mouseX, mouseY, partialTicks);
    }

    @Override
    public void tick() {
        super.tick();
        if (--this.ticksUntilEnable <= 0) {
            this.exitButton.f_93623_ = true;
            this.disaleButton.f_93623_ = true;
        }
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return this.ticksUntilEnable <= 0;
    }

    @Override
    public void onClose() {
        Minecraft.getInstance().setScreen(this.lastScreen);
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        if (pMouseY > 180.0 && pMouseY < 190.0) {
            Style style = this.getClickedComponentStyleAt((int) pMouseX);
            if (style != null && style.getClickEvent() != null && style.getClickEvent().getAction() == ClickEvent.Action.OPEN_URL) {
                this.m_5561_(style);
                return false;
            }
        }
        return super.m_6375_(pMouseX, pMouseY, pButton);
    }

    @Nullable
    private Style getClickedComponentStyleAt(int xPos) {
        int wid = Minecraft.getInstance().font.width(this.url);
        int left = this.f_96543_ / 2 - wid / 2;
        int right = this.f_96543_ / 2 + wid / 2;
        return xPos >= left && xPos <= right ? Minecraft.getInstance().font.getSplitter().componentStyleAtWidth(this.url, xPos - left) : null;
    }

    public static WelcomeMessageScreen createOptifine(Screen screen) {
        return new WelcomeMessageScreen(screen, 200, OF_TITLE, OF_TEXT, OF_URL, () -> SuppPlatformStuff.disableOFWarn(true));
    }

    public static WelcomeMessageScreen createAmendments(Screen screen) {
        return new WelcomeMessageScreen(screen, 100, AM_TITLE, AM_TEXT, AM_URL, SuppPlatformStuff::disableAMWarn);
    }
}
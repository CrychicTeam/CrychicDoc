package com.mojang.realmsclient.gui.screens;

import com.mojang.logging.LogUtils;
import com.mojang.realmsclient.exception.RealmsDefaultUncaughtExceptionHandler;
import com.mojang.realmsclient.gui.ErrorCallback;
import com.mojang.realmsclient.util.task.LongRunningTask;
import java.time.Duration;
import javax.annotation.Nullable;
import net.minecraft.client.GameNarrator;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.realms.RealmsScreen;
import net.minecraft.realms.RepeatedNarrator;
import org.slf4j.Logger;

public class RealmsLongRunningMcoTaskScreen extends RealmsScreen implements ErrorCallback {

    private static final RepeatedNarrator REPEATED_NARRATOR = new RepeatedNarrator(Duration.ofSeconds(5L));

    private static final Logger LOGGER = LogUtils.getLogger();

    private final Screen lastScreen;

    private volatile Component title = CommonComponents.EMPTY;

    @Nullable
    private volatile Component errorMessage;

    private volatile boolean aborted;

    private int animTicks;

    private final LongRunningTask task;

    private final int buttonLength = 212;

    private Button cancelOrBackButton;

    public static final String[] SYMBOLS = new String[] { "▃ ▄ ▅ ▆ ▇ █ ▇ ▆ ▅ ▄ ▃", "_ ▃ ▄ ▅ ▆ ▇ █ ▇ ▆ ▅ ▄", "_ _ ▃ ▄ ▅ ▆ ▇ █ ▇ ▆ ▅", "_ _ _ ▃ ▄ ▅ ▆ ▇ █ ▇ ▆", "_ _ _ _ ▃ ▄ ▅ ▆ ▇ █ ▇", "_ _ _ _ _ ▃ ▄ ▅ ▆ ▇ █", "_ _ _ _ ▃ ▄ ▅ ▆ ▇ █ ▇", "_ _ _ ▃ ▄ ▅ ▆ ▇ █ ▇ ▆", "_ _ ▃ ▄ ▅ ▆ ▇ █ ▇ ▆ ▅", "_ ▃ ▄ ▅ ▆ ▇ █ ▇ ▆ ▅ ▄", "▃ ▄ ▅ ▆ ▇ █ ▇ ▆ ▅ ▄ ▃", "▄ ▅ ▆ ▇ █ ▇ ▆ ▅ ▄ ▃ _", "▅ ▆ ▇ █ ▇ ▆ ▅ ▄ ▃ _ _", "▆ ▇ █ ▇ ▆ ▅ ▄ ▃ _ _ _", "▇ █ ▇ ▆ ▅ ▄ ▃ _ _ _ _", "█ ▇ ▆ ▅ ▄ ▃ _ _ _ _ _", "▇ █ ▇ ▆ ▅ ▄ ▃ _ _ _ _", "▆ ▇ █ ▇ ▆ ▅ ▄ ▃ _ _ _", "▅ ▆ ▇ █ ▇ ▆ ▅ ▄ ▃ _ _", "▄ ▅ ▆ ▇ █ ▇ ▆ ▅ ▄ ▃ _" };

    public RealmsLongRunningMcoTaskScreen(Screen screen0, LongRunningTask longRunningTask1) {
        super(GameNarrator.NO_TITLE);
        this.lastScreen = screen0;
        this.task = longRunningTask1;
        longRunningTask1.setScreen(this);
        Thread $$2 = new Thread(longRunningTask1, "Realms-long-running-task");
        $$2.setUncaughtExceptionHandler(new RealmsDefaultUncaughtExceptionHandler(LOGGER));
        $$2.start();
    }

    @Override
    public void tick() {
        super.m_86600_();
        REPEATED_NARRATOR.narrate(this.f_96541_.getNarrator(), this.title);
        this.animTicks++;
        this.task.tick();
    }

    @Override
    public boolean keyPressed(int int0, int int1, int int2) {
        if (int0 == 256) {
            this.cancelOrBackButtonClicked();
            return true;
        } else {
            return super.m_7933_(int0, int1, int2);
        }
    }

    @Override
    public void init() {
        this.task.init();
        this.cancelOrBackButton = (Button) this.m_142416_(Button.builder(CommonComponents.GUI_CANCEL, p_88795_ -> this.cancelOrBackButtonClicked()).bounds(this.f_96543_ / 2 - 106, m_120774_(12), 212, 20).build());
    }

    private void cancelOrBackButtonClicked() {
        this.aborted = true;
        this.task.abortTask();
        this.f_96541_.setScreen(this.lastScreen);
    }

    @Override
    public void render(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        this.m_280273_(guiGraphics0);
        guiGraphics0.drawCenteredString(this.f_96547_, this.title, this.f_96543_ / 2, m_120774_(3), 16777215);
        Component $$4 = this.errorMessage;
        if ($$4 == null) {
            guiGraphics0.drawCenteredString(this.f_96547_, SYMBOLS[this.animTicks % SYMBOLS.length], this.f_96543_ / 2, m_120774_(8), 8421504);
        } else {
            guiGraphics0.drawCenteredString(this.f_96547_, $$4, this.f_96543_ / 2, m_120774_(8), 16711680);
        }
        super.m_88315_(guiGraphics0, int1, int2, float3);
    }

    @Override
    public void error(Component component0) {
        this.errorMessage = component0;
        this.f_96541_.getNarrator().sayNow(component0);
        this.f_96541_.execute(() -> {
            this.m_169411_(this.cancelOrBackButton);
            this.cancelOrBackButton = (Button) this.m_142416_(Button.builder(CommonComponents.GUI_BACK, p_88790_ -> this.cancelOrBackButtonClicked()).bounds(this.f_96543_ / 2 - 106, this.f_96544_ / 4 + 120 + 12, 200, 20).build());
        });
    }

    public void setTitle(Component component0) {
        this.title = component0;
    }

    public boolean aborted() {
        return this.aborted;
    }
}
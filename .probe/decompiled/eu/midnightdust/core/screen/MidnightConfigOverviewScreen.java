package eu.midnightdust.core.screen;

import eu.midnightdust.core.MidnightLibClient;
import eu.midnightdust.lib.config.MidnightConfig;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MidnightConfigOverviewScreen extends Screen {

    private final Screen parent;

    private MidnightConfigOverviewScreen.MidnightOverviewListWidget list;

    public MidnightConfigOverviewScreen(Screen parent) {
        super(Component.translatable("midnightlib.overview.title"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        this.m_142416_(Button.builder(CommonComponents.GUI_DONE, button -> ((Minecraft) Objects.requireNonNull(this.f_96541_)).setScreen(this.parent)).bounds(this.f_96543_ / 2 - 100, this.f_96544_ - 28, 200, 20).build());
        this.list = new MidnightConfigOverviewScreen.MidnightOverviewListWidget(this.f_96541_, this.f_96543_, this.f_96544_, 32, this.f_96544_ - 32, 25);
        if (this.f_96541_ != null && this.f_96541_.level != null) {
            this.list.m_93488_(false);
        }
        this.m_7787_(this.list);
        List<String> sortedMods = new ArrayList(MidnightConfig.configClass.keySet());
        Collections.sort(sortedMods);
        sortedMods.forEach(modid -> {
            if (!MidnightLibClient.hiddenMods.contains(modid)) {
                this.list.addButton(Button.builder(Component.translatable(modid + ".midnightconfig.title"), button -> ((Minecraft) Objects.requireNonNull(this.f_96541_)).setScreen(MidnightConfig.getScreen(this, modid))).bounds(this.f_96543_ / 2 - 125, this.f_96544_ - 28, 250, 20).build());
            }
        });
        super.init();
    }

    @Override
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
        this.m_280273_(context);
        this.list.m_88315_(context, mouseX, mouseY, delta);
        context.drawCenteredString(this.f_96547_, this.f_96539_, this.f_96543_ / 2, 15, 16777215);
        super.render(context, mouseX, mouseY, delta);
    }

    @OnlyIn(Dist.CLIENT)
    public static class MidnightOverviewListWidget extends ContainerObjectSelectionList<MidnightConfigOverviewScreen.OverviewButtonEntry> {

        Font textRenderer;

        public MidnightOverviewListWidget(Minecraft minecraftClient, int i, int j, int k, int l, int m) {
            super(minecraftClient, i, j, k, l, m);
            this.f_93394_ = false;
            this.textRenderer = minecraftClient.font;
        }

        @Override
        public int getScrollbarPosition() {
            return this.f_93388_ - 7;
        }

        public void addButton(AbstractWidget button) {
            this.m_7085_(MidnightConfigOverviewScreen.OverviewButtonEntry.create(button));
        }

        @Override
        public int getRowWidth() {
            return 400;
        }
    }

    public static class OverviewButtonEntry extends ContainerObjectSelectionList.Entry<MidnightConfigOverviewScreen.OverviewButtonEntry> {

        private final AbstractWidget button;

        private final List<AbstractWidget> buttonList = new ArrayList();

        private OverviewButtonEntry(AbstractWidget button) {
            this.button = button;
            this.buttonList.add(button);
        }

        public static MidnightConfigOverviewScreen.OverviewButtonEntry create(AbstractWidget button) {
            return new MidnightConfigOverviewScreen.OverviewButtonEntry(button);
        }

        @Override
        public void render(GuiGraphics context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            this.button.setY(y);
            this.button.render(context, mouseX, mouseY, tickDelta);
        }

        @Override
        public List<? extends GuiEventListener> children() {
            return this.buttonList;
        }

        @Override
        public List<? extends NarratableEntry> narratables() {
            return this.buttonList;
        }
    }
}
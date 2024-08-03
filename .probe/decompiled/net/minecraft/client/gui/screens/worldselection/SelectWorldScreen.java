package net.minecraft.client.gui.screens.worldselection;

import com.mojang.logging.LogUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.levelgen.WorldOptions;
import org.slf4j.Logger;

public class SelectWorldScreen extends Screen {

    private static final Logger LOGGER = LogUtils.getLogger();

    public static final WorldOptions TEST_OPTIONS = new WorldOptions((long) "test1".hashCode(), true, false);

    protected final Screen lastScreen;

    private Button deleteButton;

    private Button selectButton;

    private Button renameButton;

    private Button copyButton;

    protected EditBox searchBox;

    private WorldSelectionList list;

    public SelectWorldScreen(Screen screen0) {
        super(Component.translatable("selectWorld.title"));
        this.lastScreen = screen0;
    }

    @Override
    public void tick() {
        this.searchBox.tick();
    }

    @Override
    protected void init() {
        this.searchBox = new EditBox(this.f_96547_, this.f_96543_ / 2 - 100, 22, 200, 20, this.searchBox, Component.translatable("selectWorld.search"));
        this.searchBox.setResponder(p_232980_ -> this.list.updateFilter(p_232980_));
        this.list = new WorldSelectionList(this, this.f_96541_, this.f_96543_, this.f_96544_, 48, this.f_96544_ - 64, 36, this.searchBox.getValue(), this.list);
        this.m_7787_(this.searchBox);
        this.m_7787_(this.list);
        this.selectButton = (Button) this.m_142416_(Button.builder(Component.translatable("selectWorld.select"), p_232984_ -> this.list.getSelectedOpt().ifPresent(WorldSelectionList.WorldListEntry::m_101704_)).bounds(this.f_96543_ / 2 - 154, this.f_96544_ - 52, 150, 20).build());
        this.m_142416_(Button.builder(Component.translatable("selectWorld.create"), p_280918_ -> CreateWorldScreen.openFresh(this.f_96541_, this)).bounds(this.f_96543_ / 2 + 4, this.f_96544_ - 52, 150, 20).build());
        this.renameButton = (Button) this.m_142416_(Button.builder(Component.translatable("selectWorld.edit"), p_101378_ -> this.list.getSelectedOpt().ifPresent(WorldSelectionList.WorldListEntry::m_101739_)).bounds(this.f_96543_ / 2 - 154, this.f_96544_ - 28, 72, 20).build());
        this.deleteButton = (Button) this.m_142416_(Button.builder(Component.translatable("selectWorld.delete"), p_101376_ -> this.list.getSelectedOpt().ifPresent(WorldSelectionList.WorldListEntry::m_101738_)).bounds(this.f_96543_ / 2 - 76, this.f_96544_ - 28, 72, 20).build());
        this.copyButton = (Button) this.m_142416_(Button.builder(Component.translatable("selectWorld.recreate"), p_101373_ -> this.list.getSelectedOpt().ifPresent(WorldSelectionList.WorldListEntry::m_101743_)).bounds(this.f_96543_ / 2 + 4, this.f_96544_ - 28, 72, 20).build());
        this.m_142416_(Button.builder(CommonComponents.GUI_CANCEL, p_280917_ -> this.f_96541_.setScreen(this.lastScreen)).bounds(this.f_96543_ / 2 + 82, this.f_96544_ - 28, 72, 20).build());
        this.updateButtonStatus(false, false);
        this.m_264313_(this.searchBox);
    }

    @Override
    public boolean keyPressed(int int0, int int1, int int2) {
        return super.keyPressed(int0, int1, int2) ? true : this.searchBox.keyPressed(int0, int1, int2);
    }

    @Override
    public void onClose() {
        this.f_96541_.setScreen(this.lastScreen);
    }

    @Override
    public boolean charTyped(char char0, int int1) {
        return this.searchBox.charTyped(char0, int1);
    }

    @Override
    public void render(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        this.list.render(guiGraphics0, int1, int2, float3);
        this.searchBox.m_88315_(guiGraphics0, int1, int2, float3);
        guiGraphics0.drawCenteredString(this.f_96547_, this.f_96539_, this.f_96543_ / 2, 8, 16777215);
        super.render(guiGraphics0, int1, int2, float3);
    }

    public void updateButtonStatus(boolean boolean0, boolean boolean1) {
        this.selectButton.f_93623_ = boolean0;
        this.renameButton.f_93623_ = boolean0;
        this.copyButton.f_93623_ = boolean0;
        this.deleteButton.f_93623_ = boolean1;
    }

    @Override
    public void removed() {
        if (this.list != null) {
            this.list.m_6702_().forEach(WorldSelectionList.Entry::close);
        }
    }
}
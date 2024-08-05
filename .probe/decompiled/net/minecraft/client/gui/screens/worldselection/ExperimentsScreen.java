package net.minecraft.client.gui.screens.worldselection;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.Object2BooleanLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.MultiLineTextWidget;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.server.packs.repository.PackSource;

public class ExperimentsScreen extends Screen {

    private static final int MAIN_CONTENT_WIDTH = 310;

    private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this);

    private final Screen parent;

    private final PackRepository packRepository;

    private final Consumer<PackRepository> output;

    private final Object2BooleanMap<Pack> packs = new Object2BooleanLinkedOpenHashMap();

    protected ExperimentsScreen(Screen screen0, PackRepository packRepository1, Consumer<PackRepository> consumerPackRepository2) {
        super(Component.translatable("experiments_screen.title"));
        this.parent = screen0;
        this.packRepository = packRepository1;
        this.output = consumerPackRepository2;
        for (Pack $$3 : packRepository1.getAvailablePacks()) {
            if ($$3.getPackSource() == PackSource.FEATURE) {
                this.packs.put($$3, packRepository1.getSelectedPacks().contains($$3));
            }
        }
    }

    @Override
    protected void init() {
        this.layout.addToHeader(new StringWidget(Component.translatable("selectWorld.experiments"), this.f_96547_));
        GridLayout.RowHelper $$0 = this.layout.addToContents(new GridLayout()).createRowHelper(1);
        $$0.addChild(new MultiLineTextWidget(Component.translatable("selectWorld.experiments.info").withStyle(ChatFormatting.RED), this.f_96547_).setMaxWidth(310), $$0.newCellSettings().paddingBottom(15));
        SwitchGrid.Builder $$1 = SwitchGrid.builder(310).withInfoUnderneath(2, true).withRowSpacing(4);
        this.packs.forEach((p_270880_, p_270874_) -> $$1.addSwitch(getHumanReadableTitle(p_270880_), () -> this.packs.getBoolean(p_270880_), p_270491_ -> this.packs.put(p_270880_, p_270491_)).withInfo(p_270880_.getDescription()));
        $$1.build($$0::m_264139_);
        GridLayout.RowHelper $$2 = this.layout.addToFooter(new GridLayout().columnSpacing(10)).createRowHelper(2);
        $$2.addChild(Button.builder(CommonComponents.GUI_DONE, p_270336_ -> this.onDone()).build());
        $$2.addChild(Button.builder(CommonComponents.GUI_CANCEL, p_274702_ -> this.onClose()).build());
        this.layout.m_264134_(p_270313_ -> {
            AbstractWidget var10000 = (AbstractWidget) this.m_142416_(p_270313_);
        });
        this.repositionElements();
    }

    private static Component getHumanReadableTitle(Pack pack0) {
        String $$1 = "dataPack." + pack0.getId() + ".name";
        return (Component) (I18n.exists($$1) ? Component.translatable($$1) : pack0.getTitle());
    }

    @Override
    public void onClose() {
        this.f_96541_.setScreen(this.parent);
    }

    private void onDone() {
        List<Pack> $$0 = new ArrayList(this.packRepository.getSelectedPacks());
        List<Pack> $$1 = new ArrayList();
        this.packs.forEach((p_270540_, p_270780_) -> {
            $$0.remove(p_270540_);
            if (p_270780_) {
                $$1.add(p_270540_);
            }
        });
        $$0.addAll(Lists.reverse($$1));
        this.packRepository.setSelected($$0.stream().map(Pack::m_10446_).toList());
        this.output.accept(this.packRepository);
    }

    @Override
    protected void repositionElements() {
        this.layout.arrangeElements();
    }

    @Override
    public void render(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        this.m_280273_(guiGraphics0);
        guiGraphics0.setColor(0.125F, 0.125F, 0.125F, 1.0F);
        int $$4 = 32;
        guiGraphics0.blit(f_279548_, 0, this.layout.getHeaderHeight(), 0.0F, 0.0F, this.f_96543_, this.f_96544_ - this.layout.getHeaderHeight() - this.layout.getFooterHeight(), 32, 32);
        guiGraphics0.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        super.render(guiGraphics0, int1, int2, float3);
    }
}
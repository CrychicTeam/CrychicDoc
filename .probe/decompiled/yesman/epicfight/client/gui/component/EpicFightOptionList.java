package yesman.epicfight.client.gui.component;

import com.google.common.collect.ImmutableList;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class EpicFightOptionList extends ContainerObjectSelectionList<EpicFightOptionList.OptionEntry> {

    public EpicFightOptionList(Minecraft minecraft, int int0, int int1, int int2, int int3, int int4) {
        super(minecraft, int0, int1, int2, int3, int4);
        this.f_93394_ = false;
    }

    public int addBig(AbstractWidget button1) {
        return this.m_7085_(EpicFightOptionList.OptionEntry.big(this.f_93388_, button1));
    }

    public void addSmall(AbstractWidget button1, @Nullable AbstractWidget button2) {
        this.m_7085_(EpicFightOptionList.OptionEntry.small(this.f_93388_, button1, button2));
    }

    @Override
    public int getRowWidth() {
        return 400;
    }

    @Override
    protected int getScrollbarPosition() {
        return super.m_5756_() + 32;
    }

    @OnlyIn(Dist.CLIENT)
    protected static class OptionEntry extends ContainerObjectSelectionList.Entry<EpicFightOptionList.OptionEntry> {

        final List<AbstractWidget> children;

        private OptionEntry(List<AbstractWidget> listAbstractWidget0) {
            this.children = ImmutableList.copyOf(listAbstractWidget0);
        }

        public static EpicFightOptionList.OptionEntry big(int width, AbstractWidget widget) {
            return new EpicFightOptionList.OptionEntry(List.of(widget));
        }

        public static EpicFightOptionList.OptionEntry small(int width, AbstractWidget button1, @Nullable AbstractWidget button2) {
            return button2 == null ? new EpicFightOptionList.OptionEntry(List.of(button1)) : new EpicFightOptionList.OptionEntry(List.of(button1, button2));
        }

        @Override
        public void render(GuiGraphics guiGraphics, int x, int y, int int0, int int1, int int2, int mouseX, int mouseY, boolean boolean3, float partialTicks) {
            this.children.forEach(widget -> {
                widget.setY(y);
                widget.render(guiGraphics, mouseX, mouseY, partialTicks);
            });
        }

        @Override
        public List<? extends GuiEventListener> children() {
            return this.children;
        }

        @Override
        public List<? extends NarratableEntry> narratables() {
            return this.children;
        }
    }
}
package vazkii.patchouli.client.book.gui.button;

import java.util.Arrays;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import vazkii.patchouli.client.base.PersistentData;
import vazkii.patchouli.client.book.gui.GuiBook;

public class GuiButtonBookResize extends GuiButtonBook {

    public GuiButtonBookResize(GuiBook parent, int x, int y, Button.OnPress onPress) {
        super(parent, x, y, 330, 9, 11, 11, onPress, Component.translatable("patchouli.gui.lexicon.button.resize"));
    }

    @Override
    public List<Component> getTooltipLines() {
        int size = PersistentData.data.bookGuiScale;
        int lastSize = 5;
        MutableComponent sizeMsg;
        if (size <= 5) {
            sizeMsg = Component.translatable("patchouli.gui.lexicon.button.resize.size" + size);
        } else {
            sizeMsg = Component.translatable("patchouli.gui.lexicon.button.resize.verybig.message");
            for (int i = 0; i < size - 5; i++) {
                sizeMsg = Component.translatable("patchouli.gui.lexicon.button.resize.verybig.container", sizeMsg);
            }
        }
        return Arrays.asList((Component) this.tooltip.get(0), sizeMsg.withStyle(ChatFormatting.GRAY));
    }
}
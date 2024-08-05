package se.mickelus.tetra.blocks.multischematic;

import java.util.StringJoiner;
import java.util.stream.Stream;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import se.mickelus.mutil.gui.GuiAttachment;
import se.mickelus.mutil.gui.GuiRoot;
import se.mickelus.mutil.gui.GuiString;
import se.mickelus.mutil.gui.impl.GuiVerticalLayoutGroup;

@ParametersAreNonnullByDefault
public class MultiblockSchematicGui extends GuiRoot implements IGuiOverlay {

    private final GuiVerticalLayoutGroup element;

    private int selected = -1;

    public MultiblockSchematicGui(Minecraft mc) {
        super(mc);
        this.element = new GuiVerticalLayoutGroup(12, 0, 0, 1);
        this.element.setAttachmentPoint(GuiAttachment.middleLeft);
        this.element.setAttachmentAnchor(GuiAttachment.middleCenter);
        this.addChild(this.element);
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (TickEvent.Phase.END == event.phase && this.mc.player != null && this.mc.level != null && (this.mc.level.m_46467_() % 10L == 0L || this.selected != this.mc.player.m_150109_().selected)) {
            this.selected = this.mc.player.m_150109_().selected;
            this.element.clearChildren();
            ItemStack itemStack = (ItemStack) Stream.of(this.mc.player.m_21205_(), this.mc.player.m_21206_()).filter(stack -> !stack.isEmpty()).filter(stack -> stack.getItem() instanceof StackedMultiblockSchematicItem).findFirst().orElse(ItemStack.EMPTY);
            if (!itemStack.isEmpty()) {
                MultiblockSchematicBlock block = ((StackedMultiblockSchematicItem) itemStack.getItem()).schematicBlock;
                for (int y = block.height - 1; y >= 0; y--) {
                    StringJoiner part = new StringJoiner(" ");
                    for (int x = 0; x < block.width; x++) {
                        part.add(x == block.x && y == block.y ? ChatFormatting.WHITE + "◆" : ChatFormatting.GRAY + "◇");
                    }
                    this.element.addChild(new GuiString(0, 0, part.toString()));
                }
                this.element.forceLayout();
            }
        }
    }

    @Override
    public void render(ForgeGui gui, GuiGraphics graphics, float partialTick, int screenWidth, int screenHeight) {
        this.draw(graphics);
    }
}
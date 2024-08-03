package snownee.lychee.compat.rei;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.function.Supplier;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.level.block.state.BlockState;
import snownee.lychee.client.gui.GuiGameElement;
import snownee.lychee.client.gui.RenderElement;
import snownee.lychee.client.gui.ScreenElement;
import snownee.lychee.compat.JEIREI;

public class SideBlockIcon extends RenderElement {

    private final ScreenElement mainIcon;

    private final Supplier<BlockState> blockProvider;

    public SideBlockIcon(ScreenElement mainIcon, Supplier<BlockState> blockProvider) {
        this.mainIcon = mainIcon;
        this.blockProvider = blockProvider;
    }

    @Override
    public void render(GuiGraphics graphics) {
        PoseStack ms = graphics.pose();
        ms.pushPose();
        ms.translate(this.x, this.y, this.z);
        ms.scale(0.625F, 0.625F, 0.625F);
        this.mainIcon.render(graphics, 0, 0);
        ms.popPose();
        GuiGameElement.of((BlockState) this.blockProvider.get()).lighting(JEIREI.SIDE_ICON_LIGHTING).scale(7.0).rotateBlock(30.0, 202.5, 0.0).<RenderElement>at(this.x + 4.0F, this.y + 2.0F).render(graphics);
    }
}
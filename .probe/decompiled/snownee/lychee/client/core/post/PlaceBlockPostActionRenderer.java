package snownee.lychee.client.core.post;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import snownee.lychee.client.gui.GuiGameElement;
import snownee.lychee.core.def.BlockPredicateHelper;
import snownee.lychee.core.post.PlaceBlock;

public class PlaceBlockPostActionRenderer implements PostActionRenderer<PlaceBlock> {

    public void render(PlaceBlock action, GuiGraphics graphics, int x, int y) {
        BlockState state = BlockPredicateHelper.anyBlockState(action.block);
        if (state.m_60795_()) {
            GuiGameElement.of(Items.BARRIER).render(graphics, x, y);
        } else {
            GuiGameElement.of(state).rotateBlock(30.0, 225.0, 0.0).scale(10.0).render(graphics, x, y);
        }
    }
}
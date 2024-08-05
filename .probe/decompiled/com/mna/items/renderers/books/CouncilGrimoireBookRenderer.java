package com.mna.items.renderers.books;

import com.mna.api.tools.RLoc;
import com.mna.items.renderers.ItemSpellBookRenderer;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;

public class CouncilGrimoireBookRenderer extends ItemSpellBookRenderer {

    public CouncilGrimoireBookRenderer(BlockEntityRenderDispatcher berd, EntityModelSet ems) {
        super(berd, ems, RLoc.create("item/special/grimoire_council_open"), RLoc.create("item/special/grimoire_council_closed"), true);
    }
}
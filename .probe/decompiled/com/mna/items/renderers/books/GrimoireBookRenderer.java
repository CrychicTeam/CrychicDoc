package com.mna.items.renderers.books;

import com.mna.api.tools.RLoc;
import com.mna.items.renderers.ItemSpellBookRenderer;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;

public class GrimoireBookRenderer extends ItemSpellBookRenderer {

    public GrimoireBookRenderer(BlockEntityRenderDispatcher berd, EntityModelSet ems) {
        super(berd, ems, RLoc.create("item/special/grimoire_basic_open"), RLoc.create("item/special/grimoire_basic_closed"), false);
        this.addLayer(RLoc.create("item/special/grimoire_basic_open_2"), RLoc.create("item/special/grimoire_basic_closed_2"));
    }
}
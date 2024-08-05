package com.mna.items.renderers.books;

import com.mna.api.tools.RLoc;
import com.mna.items.renderers.ItemSpellBookRenderer;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;

public class UndeadGrimoireBookRenderer extends ItemSpellBookRenderer {

    public UndeadGrimoireBookRenderer(BlockEntityRenderDispatcher berd, EntityModelSet ems) {
        super(berd, ems, RLoc.create("item/special/grimoire_undead_open"), RLoc.create("item/special/grimoire_undead_closed"), true);
    }
}
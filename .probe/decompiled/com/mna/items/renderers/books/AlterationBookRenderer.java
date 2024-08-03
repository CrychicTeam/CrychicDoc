package com.mna.items.renderers.books;

import com.mna.api.tools.RLoc;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;

public class AlterationBookRenderer extends ItemBookRenderer {

    public AlterationBookRenderer(BlockEntityRenderDispatcher berd, EntityModelSet ems) {
        super(berd, ems, RLoc.create("item/special/alteration_book_open"), RLoc.create("item/special/alteration_book_closed"));
    }
}
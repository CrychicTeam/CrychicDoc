package com.mna.items.renderers.books;

import com.mna.api.tools.RLoc;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;

public class MarkBookRenderer extends ItemBookRenderer {

    public MarkBookRenderer(BlockEntityRenderDispatcher berd, EntityModelSet ems) {
        super(berd, ems, RLoc.create("item/special/mark_book_open"), RLoc.create("item/special/mark_book_closed"));
    }
}
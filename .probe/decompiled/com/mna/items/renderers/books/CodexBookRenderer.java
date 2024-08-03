package com.mna.items.renderers.books;

import com.mna.api.tools.RLoc;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;

public class CodexBookRenderer extends ItemBookRenderer {

    public CodexBookRenderer(BlockEntityRenderDispatcher berd, EntityModelSet ems) {
        super(berd, ems, RLoc.create("item/special/guide_book_open"), RLoc.create("item/special/guide_book_closed"));
    }
}
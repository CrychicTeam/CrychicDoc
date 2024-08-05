package com.mna.items.renderers.books;

import com.mna.api.tools.RLoc;
import com.mna.items.renderers.ItemSpellBookRenderer;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.resources.ResourceLocation;

public class SpellBookRenderer extends ItemSpellBookRenderer {

    public SpellBookRenderer(BlockEntityRenderDispatcher berd, EntityModelSet ems, ResourceLocation openModel, ResourceLocation closedModel, boolean renderBookInFirstPerson) {
        super(berd, ems, openModel, closedModel, renderBookInFirstPerson);
    }

    public SpellBookRenderer(BlockEntityRenderDispatcher berd, EntityModelSet ems) {
        super(berd, ems, RLoc.create("item/special/spell_book_open"), RLoc.create("item/special/spell_book_closed"), false);
        this.addLayer(RLoc.create("item/special/spell_book_open_2"), RLoc.create("item/special/spell_book_closed_2"));
    }
}
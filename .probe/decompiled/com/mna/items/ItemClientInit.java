package com.mna.items;

import com.mna.api.tools.RLoc;
import com.mna.blocks.tileentities.models.RunescribingTableModel;
import com.mna.items.renderers.AstroBladeRenderer;
import com.mna.items.renderers.ItemSpellRenderer;
import com.mna.items.renderers.fluid_jugs.FluidJugItemRenderer;
import com.mna.items.renderers.obj_gecko.EnderDiscRenderer;
import com.mna.items.renderers.obj_gecko.RunicMalusRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ItemClientInit {

    @SubscribeEvent
    public static void onRegisterSpecialModels(ModelEvent.RegisterAdditional event) {
        event.register(ItemSpellRenderer.location_spell);
        event.register(ItemSpellRenderer.location_bangle);
        event.register(RLoc.create("item/manaweave_bottle_texture"));
        event.register(RLoc.create("item/spell_book_texture"));
        event.register(RLoc.create("item/rote_book_texture"));
        event.register(RLoc.create("item/grimoire_texture"));
        event.register(RLoc.create("item/special/guide_book_open"));
        event.register(RLoc.create("item/special/guide_book_closed"));
        event.register(RLoc.create("item/special/alteration_book_open"));
        event.register(RLoc.create("item/special/alteration_book_closed"));
        event.register(RLoc.create("item/special/mark_book_open"));
        event.register(RLoc.create("item/special/mark_book_closed"));
        event.register(RLoc.create("item/special/flat_lands_book_open"));
        event.register(RLoc.create("item/special/flat_lands_book_closed"));
        event.register(RLoc.create("item/special/spell_book_open"));
        event.register(RLoc.create("item/special/spell_book_closed"));
        event.register(RLoc.create("item/special/spell_book_open_2"));
        event.register(RLoc.create("item/special/spell_book_closed_2"));
        event.register(RLoc.create("item/special/rote_book_open"));
        event.register(RLoc.create("item/special/rote_book_closed"));
        event.register(RLoc.create("item/special/rote_book_open_2"));
        event.register(RLoc.create("item/special/rote_book_closed_2"));
        event.register(RLoc.create("item/special/grimoire_basic_open"));
        event.register(RLoc.create("item/special/grimoire_basic_closed"));
        event.register(RLoc.create("item/special/grimoire_basic_open_2"));
        event.register(RLoc.create("item/special/grimoire_basic_closed_2"));
        event.register(RLoc.create("item/special/grimoire_council_open"));
        event.register(RLoc.create("item/special/grimoire_council_closed"));
        event.register(RLoc.create("item/special/grimoire_fey_open"));
        event.register(RLoc.create("item/special/grimoire_fey_closed"));
        event.register(RLoc.create("item/special/grimoire_undead_open"));
        event.register(RLoc.create("item/special/grimoire_undead_closed"));
        event.register(RLoc.create("item/special/grimoire_demon_open"));
        event.register(RLoc.create("item/special/grimoire_demon_closed"));
        event.register(RLoc.create("item/special/recipe_copy_book_open"));
        event.register(RLoc.create("item/special/recipe_copy_book_closed"));
        event.register(RLoc.create("item/special/recipe_copy_book_open_2"));
        event.register(RLoc.create("item/special/recipe_copy_book_closed_2"));
        event.register(EnderDiscRenderer.disk_model);
        event.register(RunicMalusRenderer.hammer_model);
        event.register(AstroBladeRenderer.blade_model);
        event.register(AstroBladeRenderer.handle_model);
        event.register(AstroBladeRenderer.blade_model_warden);
        event.register(AstroBladeRenderer.handle_model_warden);
        event.register(FluidJugItemRenderer.jug_artifact);
        event.register(FluidJugItemRenderer.jug_base);
        event.register(RunescribingTableModel.hammer);
        event.register(RunescribingTableModel.chisel);
        for (ResourceLocation spell : SpellIconList.ALL) {
            event.register(spell);
        }
    }

    @SubscribeEvent
    public static void registerItemColors(RegisterColorHandlersEvent.Item event) {
        event.register((stack, layer) -> layer > 0 ? -1 : ((DyeableLeatherItem) stack.getItem()).getColor(stack), ItemInit.MAGE_BOOTS.get(), ItemInit.MAGE_ROBES.get(), ItemInit.MAGE_LEGGINGS.get(), ItemInit.MAGE_HOOD.get(), ItemInit.PRACTITIONERS_POUCH.get(), ItemInit.SPELL_BOOK.get(), ItemInit.RECIPE_COPY_BOOK.get(), ItemInit.GRIMOIRE.get(), ItemInit.ROTE_BOOK.get(), ItemInit.BANGLE.get());
    }
}
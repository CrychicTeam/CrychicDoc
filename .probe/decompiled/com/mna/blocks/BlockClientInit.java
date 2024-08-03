package com.mna.blocks;

import com.mna.ManaAndArtifice;
import com.mna.api.blocks.interfaces.ICutoutBlock;
import com.mna.api.blocks.interfaces.ITranslucentBlock;
import com.mna.api.tools.RLoc;
import com.mna.blocks.tileentities.models.AffinityTinkerModel;
import com.mna.blocks.tileentities.models.ArcanaAltarModel;
import com.mna.blocks.tileentities.models.DisenchanterModel;
import com.mna.blocks.tileentities.models.EldrinFumeModel;
import com.mna.blocks.tileentities.models.RunescribingTableModel;
import com.mna.blocks.tileentities.models.SpecializationDeskModel;
import com.mna.blocks.tileentities.models.ThesisDeskModel;
import com.mna.blocks.tileentities.models.TranscriptionTableModel;
import com.mna.blocks.tileentities.renderers.ConstructWorkbenchRenderer;
import com.mna.blocks.tileentities.renderers.LodestarRenderer;
import com.mna.blocks.tileentities.renderers.ManaCrystalRenderer;
import com.mna.blocks.tileentities.renderers.ManaweaveCacheRenderer;
import com.mna.blocks.tileentities.renderers.RunicAnvilRenderer;
import com.mna.blocks.tileentities.renderers.ScrollShelfRenderer;
import com.mna.blocks.tileentities.renderers.SeerStoneRenderer;
import com.mna.blocks.tileentities.renderers.SpectralRenderer;
import com.mna.blocks.tileentities.renderers.wizard_lab.InscriptionTableRenderer;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.RegistryObject;

public class BlockClientInit {

    @SubscribeEvent
    public static void onClientSetupEvent(FMLClientSetupEvent event) {
        BlockInit.BLOCKS.getEntries().stream().map(RegistryObject::get).forEach(block -> {
            if (block instanceof ICutoutBlock || block instanceof FlowerPotBlock) {
                ItemBlockRenderTypes.setRenderLayer(block, RenderType.cutout());
            } else if (block instanceof ITranslucentBlock) {
                ItemBlockRenderTypes.setRenderLayer(block, RenderType.translucent());
            }
        });
        ItemBlockRenderTypes.setRenderLayer(BlockInit.SLIPSTREAM_GENERATOR.get(), RenderType.cutoutMipped());
        ManaAndArtifice.LOGGER.info("M&A -> Block Render Layers Set");
    }

    @SubscribeEvent
    public static void onRegisterSpecialModels(ModelEvent.RegisterAdditional event) {
        event.register(RLoc.create("block/pedestal"));
        event.register(RLoc.create("block/mana_resevoir/full"));
        event.register(RLoc.create("block/mana_crystal"));
        event.register(RLoc.create("block/manaweaving_altar_crystal_a"));
        event.register(RLoc.create("block/manaweaving_altar_crystal_b"));
        event.register(RLoc.create("block/manaweaving_altar_crystal_c"));
        event.register(RLoc.create("block/occulus_eye_shell_interior"));
        event.register(RLoc.create("block/occulus_eye_shell"));
        event.register(RLoc.create("block/eldrin/wellspring_pillar_cap"));
        event.register(RLoc.create("block/eldrin/supplier_lower_exterior"));
        event.register(RLoc.create("block/eldrin/supplier_lower_interior"));
        event.register(RLoc.create("block/eldrin/supplier_upper"));
        event.register(RLoc.create("block/eldrin/receiver_crystal"));
        event.register(ScrollShelfRenderer.scroll);
        event.register(ScrollShelfRenderer.bottle);
        event.register(ConstructWorkbenchRenderer.hook_arm);
        event.register(ConstructWorkbenchRenderer.hook_head);
        event.register(ConstructWorkbenchRenderer.hook_leg);
        event.register(ConstructWorkbenchRenderer.hook_torso);
        event.register(RunicAnvilRenderer.ring_small);
        event.register(RunicAnvilRenderer.ring_large);
        event.register(ManaCrystalRenderer.crystal);
        event.register(ManaCrystalRenderer.runes);
        event.register(InscriptionTableRenderer.ash);
        event.register(InscriptionTableRenderer.ink);
        event.register(InscriptionTableRenderer.vellum);
        event.register(RLoc.create("block/illusion_block"));
        event.register(SeerStoneRenderer.crystal);
        event.register(SeerStoneRenderer.crystal_band);
        event.register(LodestarRenderer.base);
        event.register(LodestarRenderer.delegation_station);
        event.register(LodestarRenderer.small_crystal);
        event.register(LodestarRenderer.big_crystal);
        event.register(LodestarRenderer.small_gear);
        event.register(LodestarRenderer.stabilizer);
        event.register(ManaweaveCacheRenderer.interior);
        event.register(ManaweaveCacheRenderer.exterior);
        event.register(SpectralRenderer.anvil_top);
        event.register(SpectralRenderer.floating_bot);
        event.register(SpectralRenderer.floating_mid);
        event.register(SpectralRenderer.floating_runes);
        event.register(SpectralRenderer.floating_top);
        event.register(SpectralRenderer.stonecutter_blade);
        event.register(SpectralRenderer.stonecutter_top);
        event.register(SpectralRenderer.workbench_top);
        event.register(ThesisDeskModel.ink);
        event.register(ThesisDeskModel.vellum);
        event.register(EldrinFumeModel.coal);
        event.register(SpecializationDeskModel.crystal_back);
        event.register(SpecializationDeskModel.crystal_left);
        event.register(SpecializationDeskModel.crystal_right);
        event.register(TranscriptionTableModel.ink);
        event.register(TranscriptionTableModel.lapis);
        event.register(RunescribingTableModel.water);
        event.register(RunescribingTableModel.recipe);
        event.register(DisenchanterModel.crystal);
        event.register(AffinityTinkerModel.scroll);
        event.register(ArcanaAltarModel.candle_short);
        event.register(ArcanaAltarModel.candle_long);
    }
}
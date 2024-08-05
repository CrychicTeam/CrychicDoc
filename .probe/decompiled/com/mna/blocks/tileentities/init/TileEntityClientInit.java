package com.mna.blocks.tileentities.init;

import com.mna.ManaAndArtifice;
import com.mna.blocks.tileentities.models.AffinityTinkerModel;
import com.mna.blocks.tileentities.models.EldrinFumeModel;
import com.mna.blocks.tileentities.models.ModelIceSpike;
import com.mna.blocks.tileentities.models.ModelImpaleSpike;
import com.mna.blocks.tileentities.models.RunescribingTableModel;
import com.mna.blocks.tileentities.models.SpecializationDeskModel;
import com.mna.blocks.tileentities.models.ThesisDeskModel;
import com.mna.blocks.tileentities.models.TranscriptionTableModel;
import com.mna.blocks.tileentities.renderers.ArcaneSentryRenderer;
import com.mna.blocks.tileentities.renderers.BookshelfRenderer;
import com.mna.blocks.tileentities.renderers.ChalkRuneRenderer;
import com.mna.blocks.tileentities.renderers.ConstructWorkbenchRenderer;
import com.mna.blocks.tileentities.renderers.EldrinAltarRenderer;
import com.mna.blocks.tileentities.renderers.EldrinConduitRenderer;
import com.mna.blocks.tileentities.renderers.FluidJugRenderer;
import com.mna.blocks.tileentities.renderers.IllusionBlockRenderer;
import com.mna.blocks.tileentities.renderers.LodestarRenderer;
import com.mna.blocks.tileentities.renderers.MageLightRenderer;
import com.mna.blocks.tileentities.renderers.ManaCrystalRenderer;
import com.mna.blocks.tileentities.renderers.ManaweaveCacheRenderer;
import com.mna.blocks.tileentities.renderers.ManaweaveProjectorRenderer;
import com.mna.blocks.tileentities.renderers.ManaweavingAltarRenderer;
import com.mna.blocks.tileentities.renderers.OcculusRenderer;
import com.mna.blocks.tileentities.renderers.PedestalRenderer;
import com.mna.blocks.tileentities.renderers.RuneforgeRenderer;
import com.mna.blocks.tileentities.renderers.RunicAnvilRenderer;
import com.mna.blocks.tileentities.renderers.ScrollShelfRenderer;
import com.mna.blocks.tileentities.renderers.SeerStoneRenderer;
import com.mna.blocks.tileentities.renderers.SimpleGeoBlockRenderer;
import com.mna.blocks.tileentities.renderers.SpectralRenderer;
import com.mna.blocks.tileentities.renderers.TransitoryTileRenderer;
import com.mna.blocks.tileentities.renderers.WellspringPillarRenderer;
import com.mna.blocks.tileentities.renderers.wizard_lab.ArcanaAltarRenderer;
import com.mna.blocks.tileentities.renderers.wizard_lab.DisenchanterRenderer;
import com.mna.blocks.tileentities.renderers.wizard_lab.InscriptionTableRenderer;
import com.mna.blocks.tileentities.renderers.wizard_lab.WizardLabRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class TileEntityClientInit {

    @SubscribeEvent
    public static void onClientSetupEvent(FMLClientSetupEvent event) {
        BlockEntityRenderers.register(TileEntityInit.CHALK_RUNE.get(), ChalkRuneRenderer::new);
        BlockEntityRenderers.register(TileEntityInit.PEDESTAL.get(), PedestalRenderer::new);
        BlockEntityRenderers.register(TileEntityInit.MANAWEAVING_ALTAR.get(), ManaweavingAltarRenderer::new);
        BlockEntityRenderers.register(TileEntityInit.RUNEFORGE.get(), RuneforgeRenderer::new);
        BlockEntityRenderers.register(TileEntityInit.RUNIC_ANVIL.get(), RunicAnvilRenderer::new);
        BlockEntityRenderers.register(TileEntityInit.MANAWEAVE_PROJECTOR.get(), ManaweaveProjectorRenderer::new);
        BlockEntityRenderers.register(TileEntityInit.OCCULUS.get(), OcculusRenderer::new);
        BlockEntityRenderers.register(TileEntityInit.ELEMENTAL_SENTRY.get(), ArcaneSentryRenderer::new);
        BlockEntityRenderers.register(TileEntityInit.CONSTRUCT_WORKBENCH.get(), ConstructWorkbenchRenderer::new);
        BlockEntityRenderers.register(TileEntityInit.LODESTAR.get(), LodestarRenderer::new);
        BlockEntityRenderers.register(TileEntityInit.INSCRIPTION_TABLE.get(), InscriptionTableRenderer::new);
        BlockEntityRenderers.register(TileEntityInit.MAGE_LIGHT.get(), MageLightRenderer::new);
        BlockEntityRenderers.register(TileEntityInit.TRANSITORY_TILE.get(), TransitoryTileRenderer::new);
        BlockEntityRenderers.register(TileEntityInit.WELLSPRING_PILLAR.get(), WellspringPillarRenderer::new);
        BlockEntityRenderers.register(TileEntityInit.ELDRIN_CONDUIT_TILE.get(), EldrinConduitRenderer::new);
        BlockEntityRenderers.register(TileEntityInit.ELDRIN_ALTAR_TILE.get(), EldrinAltarRenderer::new);
        BlockEntityRenderers.register(TileEntityInit.ILLUSION_BLOCK.get(), IllusionBlockRenderer::new);
        BlockEntityRenderers.register(TileEntityInit.SPECTRAL_TILE.get(), SpectralRenderer::new);
        BlockEntityRenderers.register(TileEntityInit.MANAWEAVE_CACHE.get(), ManaweaveCacheRenderer::new);
        BlockEntityRenderers.register(TileEntityInit.FLUID_JUG.get(), FluidJugRenderer::new);
        BlockEntityRenderers.register(TileEntityInit.MANA_CRYSTAL.get(), ManaCrystalRenderer::new);
        BlockEntityRenderers.register(TileEntityInit.SEER_STONE.get(), SeerStoneRenderer::new);
        BlockEntityRenderers.register(TileEntityInit.BOOKSHELF.get(), BookshelfRenderer::new);
        BlockEntityRenderers.register(TileEntityInit.SCROLLSHELF.get(), ScrollShelfRenderer::new);
        BlockEntityRenderers.register(TileEntityInit.ICE_SPIKE.get(), ctx -> new SimpleGeoBlockRenderer<>(ctx, new ModelIceSpike()));
        BlockEntityRenderers.register(TileEntityInit.IMPALE_SPIKE.get(), ctx -> new SimpleGeoBlockRenderer<>(ctx, new ModelImpaleSpike()));
        BlockEntityRenderers.register(TileEntityInit.THESIS_DESK.get(), ctx -> new WizardLabRenderer<>(ctx, new ThesisDeskModel()));
        BlockEntityRenderers.register(TileEntityInit.ELDRIN_FUME.get(), ctx -> new WizardLabRenderer<>(ctx, new EldrinFumeModel()));
        BlockEntityRenderers.register(TileEntityInit.SPELL_SPECIALIZAITON.get(), ctx -> new WizardLabRenderer<>(ctx, new SpecializationDeskModel()));
        BlockEntityRenderers.register(TileEntityInit.TRANSCRIPTION_TABLE.get(), ctx -> new WizardLabRenderer<>(ctx, new TranscriptionTableModel()));
        BlockEntityRenderers.register(TileEntityInit.RUNESCRIBING_TABLE.get(), ctx -> new WizardLabRenderer<>(ctx, new RunescribingTableModel()));
        BlockEntityRenderers.register(TileEntityInit.DISENCHANTER.get(), ctx -> new DisenchanterRenderer(ctx));
        BlockEntityRenderers.register(TileEntityInit.AFFINITY_TINKER.get(), ctx -> new WizardLabRenderer<>(ctx, new AffinityTinkerModel()));
        BlockEntityRenderers.register(TileEntityInit.ALTAR_OF_ARCANA.get(), ctx -> new ArcanaAltarRenderer(ctx));
        ManaAndArtifice.LOGGER.info("M&A -> Tile Entity Renderers Registered");
    }
}
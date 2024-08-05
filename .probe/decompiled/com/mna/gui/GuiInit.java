package com.mna.gui;

import com.mna.ManaAndArtifice;
import com.mna.api.gui.GuiEldrinCapacitorPermissions;
import com.mna.gui.block.GuiAffinityTinker;
import com.mna.gui.block.GuiArcanaAltar;
import com.mna.gui.block.GuiBookshelf;
import com.mna.gui.block.GuiDisenchanter;
import com.mna.gui.block.GuiEldrinFume;
import com.mna.gui.block.GuiInscriptionTable;
import com.mna.gui.block.GuiLodestarV2;
import com.mna.gui.block.GuiMagiciansWorkbench;
import com.mna.gui.block.GuiOcculus;
import com.mna.gui.block.GuiParticleEmission;
import com.mna.gui.block.GuiRunescribingTable;
import com.mna.gui.block.GuiScrollShelf;
import com.mna.gui.block.GuiSeerStone;
import com.mna.gui.block.GuiSpectralCraftingTable;
import com.mna.gui.block.GuiSpellSpecialization;
import com.mna.gui.block.GuiStudyDesk;
import com.mna.gui.block.GuiThesisDesk;
import com.mna.gui.block.GuiTranscriptionTable;
import com.mna.gui.containers.ContainerInit;
import com.mna.gui.entity.GuiRift;
import com.mna.gui.entity.GuiWanderingWizard;
import com.mna.gui.item.GuiAstroBlade;
import com.mna.gui.item.GuiCantrips;
import com.mna.gui.item.GuiEnderDisc;
import com.mna.gui.item.GuiFilterItem;
import com.mna.gui.item.GuiGrimoire;
import com.mna.gui.item.GuiGuideBook;
import com.mna.gui.item.GuiMarkBook;
import com.mna.gui.item.GuiPhylacteryStaff;
import com.mna.gui.item.GuiPractitionersPouch;
import com.mna.gui.item.GuiRoteBook;
import com.mna.gui.item.GuiRunicMalus;
import com.mna.gui.item.GuiSpellAdjust;
import com.mna.gui.item.GuiSpellBook;
import com.mna.gui.item.GuiSpellNaming;
import com.mna.gui.item.GuiSpellRecipe;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class GuiInit {

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void onClientSetupEvent(FMLClientSetupEvent event) {
        MenuScreens.register(ContainerInit.RITUAL_KIT.get(), GuiPractitionersPouch::new);
        MenuScreens.register(ContainerInit.SPELL_BOOK.get(), GuiSpellBook::new);
        MenuScreens.register(ContainerInit.GRIMOIRE.get(), GuiGrimoire::new);
        MenuScreens.register(ContainerInit.ROTE_BOOK.get(), GuiRoteBook::new);
        MenuScreens.register(ContainerInit.MARK_BOOK.get(), GuiMarkBook::new);
        MenuScreens.register(ContainerInit.RUNIC_MALUS.get(), GuiRunicMalus::new);
        MenuScreens.register(ContainerInit.ASTRO_BLADE.get(), GuiAstroBlade::new);
        MenuScreens.register(ContainerInit.PHYLACTERY_STAFF.get(), GuiPhylacteryStaff::new);
        MenuScreens.register(ContainerInit.FILTER_ITEM.get(), GuiFilterItem::new);
        MenuScreens.register(ContainerInit.GUIDE_BOOK.get(), GuiGuideBook::new);
        MenuScreens.register(ContainerInit.ENDER_DISC.get(), GuiEnderDisc::new);
        MenuScreens.register(ContainerInit.SPELL_RECIPE_LIST.get(), GuiSpellRecipe::new);
        MenuScreens.register(ContainerInit.CANTRIPS.get(), GuiCantrips::new);
        MenuScreens.register(ContainerInit.SPELL_CUSTOMIZATION.get(), GuiSpellNaming::new);
        MenuScreens.register(ContainerInit.SPELL_ADJUSTMENTS.get(), GuiSpellAdjust::new);
        MenuScreens.register(ContainerInit.RIFT.get(), GuiRift::new);
        MenuScreens.register(ContainerInit.WANDERING_WIZARD.get(), GuiWanderingWizard::new);
        MenuScreens.register(ContainerInit.SPECTRAL_CRAFTING_TABLE.get(), GuiSpectralCraftingTable::new);
        MenuScreens.register(ContainerInit.PARTICLE_EMITTER.get(), GuiParticleEmission::new);
        MenuScreens.register(ContainerInit.OCCULUS.get(), GuiOcculus::new);
        MenuScreens.register(ContainerInit.LODESTAR.get(), GuiLodestarV2::new);
        MenuScreens.register(ContainerInit.SEER_STONE.get(), GuiSeerStone::new);
        MenuScreens.register(ContainerInit.BOOKSHELF.get(), GuiBookshelf::new);
        MenuScreens.register(ContainerInit.SCROLLSHELF.get(), GuiScrollShelf::new);
        MenuScreens.register(ContainerInit.MAGICIANS_WORKBENCH.get(), GuiMagiciansWorkbench::new);
        MenuScreens.register(ContainerInit.INSCRIPTION_TABLE.get(), GuiInscriptionTable::new);
        MenuScreens.register(ContainerInit.RUNESCRIBING_TABLE.get(), GuiRunescribingTable::new);
        MenuScreens.register(ContainerInit.THESIS_DESK.get(), GuiThesisDesk::new);
        MenuScreens.register(ContainerInit.STUDY_DESK.get(), GuiStudyDesk::new);
        MenuScreens.register(ContainerInit.DISENCHANTER.get(), GuiDisenchanter::new);
        MenuScreens.register(ContainerInit.ELDRIN_FUME.get(), GuiEldrinFume::new);
        MenuScreens.register(ContainerInit.AFFINITY_TINKER.get(), GuiAffinityTinker::new);
        MenuScreens.register(ContainerInit.SPELL_SPECIALIZATION.get(), GuiSpellSpecialization::new);
        MenuScreens.register(ContainerInit.TRANSCRIPTION_TABLE.get(), GuiTranscriptionTable::new);
        MenuScreens.register(ContainerInit.ENSORCELLATION_STATION.get(), GuiArcanaAltar::new);
        MenuScreens.register(ContainerInit.ELDRIN_PERMISSIONS.get(), GuiEldrinCapacitorPermissions::new);
        HUDOverlayRenderer.instance = new HUDOverlayRenderer();
        ManaAndArtifice.LOGGER.info("M&A -> Gui Screens Registered");
    }
}
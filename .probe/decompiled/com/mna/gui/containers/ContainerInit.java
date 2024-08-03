package com.mna.gui.containers;

import com.mna.api.gui.EldrinCapacitorPermissionsContainer;
import com.mna.blocks.BlockInit;
import com.mna.gui.containers.block.ContainerAffinityTinker;
import com.mna.gui.containers.block.ContainerArcaneAltar;
import com.mna.gui.containers.block.ContainerBookshelf;
import com.mna.gui.containers.block.ContainerDisenchanter;
import com.mna.gui.containers.block.ContainerEldrinFume;
import com.mna.gui.containers.block.ContainerInscriptionTable;
import com.mna.gui.containers.block.ContainerLodestar;
import com.mna.gui.containers.block.ContainerMagiciansWorkbench;
import com.mna.gui.containers.block.ContainerOcculus;
import com.mna.gui.containers.block.ContainerRunescribingTable;
import com.mna.gui.containers.block.ContainerScrollShelf;
import com.mna.gui.containers.block.ContainerSeerStone;
import com.mna.gui.containers.block.ContainerSpectralAnvil;
import com.mna.gui.containers.block.ContainerSpectralCraftingTable;
import com.mna.gui.containers.block.ContainerSpectralStonecutter;
import com.mna.gui.containers.block.ContainerSpellSpecialization;
import com.mna.gui.containers.block.ContainerStudyDesk;
import com.mna.gui.containers.block.ContainerThesisDesk;
import com.mna.gui.containers.block.ContainerTranscriptionTable;
import com.mna.gui.containers.entity.ContainerRift;
import com.mna.gui.containers.entity.ContainerWanderingWizard;
import com.mna.gui.containers.item.ContainerAstroBlade;
import com.mna.gui.containers.item.ContainerCantrips;
import com.mna.gui.containers.item.ContainerEnderDisc;
import com.mna.gui.containers.item.ContainerFilterItem;
import com.mna.gui.containers.item.ContainerGrimoire;
import com.mna.gui.containers.item.ContainerGuideBook;
import com.mna.gui.containers.item.ContainerMarkBook;
import com.mna.gui.containers.item.ContainerPhylacteryStaff;
import com.mna.gui.containers.item.ContainerPractitionersPouch;
import com.mna.gui.containers.item.ContainerRoteBook;
import com.mna.gui.containers.item.ContainerRunicMalus;
import com.mna.gui.containers.item.ContainerSpellAdjustments;
import com.mna.gui.containers.item.ContainerSpellBook;
import com.mna.gui.containers.item.ContainerSpellName;
import com.mna.gui.containers.item.ContainerSpellRecipe;
import com.mna.gui.containers.particle.ParticleEmissionContainer;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@EventBusSubscriber(modid = "mna", bus = Bus.MOD)
public class ContainerInit {

    public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, "mna");

    public static RegistryObject<MenuType<ContainerInscriptionTable>> INSCRIPTION_TABLE = CONTAINERS.register(of(BlockInit.INSCRIPTION_TABLE), () -> IForgeMenuType.create(ContainerInscriptionTable::new));

    public static RegistryObject<MenuType<ContainerSeerStone>> SEER_STONE = CONTAINERS.register(of(BlockInit.SEER_STONE), () -> IForgeMenuType.create(ContainerSeerStone::new));

    public static RegistryObject<MenuType<ContainerRunescribingTable>> RUNESCRIBING_TABLE = CONTAINERS.register(of(BlockInit.RUNESCRIBING_TABLE), () -> IForgeMenuType.create(ContainerRunescribingTable::new));

    public static RegistryObject<MenuType<ContainerLodestar>> LODESTAR = CONTAINERS.register(of(BlockInit.LODESTAR), () -> IForgeMenuType.create(ContainerLodestar::new));

    public static RegistryObject<MenuType<ContainerSpectralCraftingTable>> SPECTRAL_CRAFTING_TABLE = CONTAINERS.register(of(BlockInit.SPECTRAL_CRAFTING_TABLE), () -> IForgeMenuType.create(ContainerSpectralCraftingTable::new));

    public static RegistryObject<MenuType<ContainerSpectralAnvil>> SPECTRAL_ANVIL = CONTAINERS.register(of(BlockInit.SPECTRAL_ANVIL), () -> IForgeMenuType.create(ContainerSpectralAnvil::new));

    public static RegistryObject<MenuType<ContainerSpectralStonecutter>> SPECTRAL_STONECUTTER = CONTAINERS.register(of(BlockInit.SPECTRAL_STONECUTTER), () -> IForgeMenuType.create(ContainerSpectralStonecutter::new));

    public static RegistryObject<MenuType<ContainerMagiciansWorkbench>> MAGICIANS_WORKBENCH = CONTAINERS.register(of(BlockInit.MAGICIANS_WORKBENCH), () -> IForgeMenuType.create(ContainerMagiciansWorkbench::new));

    public static RegistryObject<MenuType<ParticleEmissionContainer>> PARTICLE_EMITTER = CONTAINERS.register(of(BlockInit.PARTICLE_EMITTER), () -> IForgeMenuType.create(ParticleEmissionContainer::new));

    public static RegistryObject<MenuType<ContainerThesisDesk>> THESIS_DESK = CONTAINERS.register(of(BlockInit.THESIS_DESK), () -> IForgeMenuType.create(ContainerThesisDesk::new));

    public static RegistryObject<MenuType<ContainerStudyDesk>> STUDY_DESK = CONTAINERS.register(of(BlockInit.STUDY_DESK), () -> IForgeMenuType.create(ContainerStudyDesk::new));

    public static RegistryObject<MenuType<ContainerDisenchanter>> DISENCHANTER = CONTAINERS.register(of(BlockInit.DISENCHANTER), () -> IForgeMenuType.create(ContainerDisenchanter::new));

    public static RegistryObject<MenuType<ContainerEldrinFume>> ELDRIN_FUME = CONTAINERS.register(of(BlockInit.ELDRIN_FUME), () -> IForgeMenuType.create(ContainerEldrinFume::new));

    public static RegistryObject<MenuType<ContainerAffinityTinker>> AFFINITY_TINKER = CONTAINERS.register(of(BlockInit.AFFINITY_TINKER), () -> IForgeMenuType.create(ContainerAffinityTinker::new));

    public static RegistryObject<MenuType<ContainerSpellSpecialization>> SPELL_SPECIALIZATION = CONTAINERS.register(of(BlockInit.SPELL_SPECIALIZATION), () -> IForgeMenuType.create(ContainerSpellSpecialization::new));

    public static RegistryObject<MenuType<ContainerTranscriptionTable>> TRANSCRIPTION_TABLE = CONTAINERS.register(of(BlockInit.TRANSCRIPTION_TABLE), () -> IForgeMenuType.create(ContainerTranscriptionTable::new));

    public static RegistryObject<MenuType<ContainerArcaneAltar>> ENSORCELLATION_STATION = CONTAINERS.register(of(BlockInit.ALTAR_OF_ARCANA), () -> IForgeMenuType.create(ContainerArcaneAltar::new));

    public static RegistryObject<MenuType<ContainerBookshelf>> BOOKSHELF = CONTAINERS.register(of(BlockInit.BOOKSHELF), () -> IForgeMenuType.create(ContainerBookshelf::new));

    public static RegistryObject<MenuType<ContainerScrollShelf>> SCROLLSHELF = CONTAINERS.register(of(BlockInit.SCROLLSHELF), () -> IForgeMenuType.create(ContainerScrollShelf::new));

    public static RegistryObject<MenuType<EldrinCapacitorPermissionsContainer>> ELDRIN_PERMISSIONS = CONTAINERS.register("eldrin_capacitor_permissions", () -> IForgeMenuType.create(EldrinCapacitorPermissionsContainer::new));

    public static final RegistryObject<MenuType<ContainerPractitionersPouch>> RITUAL_KIT = CONTAINERS.register("ritual_kit", () -> IForgeMenuType.create(ContainerPractitionersPouch::new));

    public static final RegistryObject<MenuType<ContainerSpellBook>> SPELL_BOOK = CONTAINERS.register("spell_book", () -> IForgeMenuType.create(ContainerSpellBook::new));

    public static final RegistryObject<MenuType<ContainerGrimoire>> GRIMOIRE = CONTAINERS.register("grimoire", () -> IForgeMenuType.create(ContainerGrimoire::new));

    public static final RegistryObject<MenuType<ContainerRoteBook>> ROTE_BOOK = CONTAINERS.register("book_of_rote", () -> IForgeMenuType.create(ContainerRoteBook::new));

    public static final RegistryObject<MenuType<ContainerMarkBook>> MARK_BOOK = CONTAINERS.register("mark_book", () -> IForgeMenuType.create(ContainerMarkBook::new));

    public static final RegistryObject<MenuType<ContainerRunicMalus>> RUNIC_MALUS = CONTAINERS.register("runic_malus", () -> IForgeMenuType.create(ContainerRunicMalus::new));

    public static final RegistryObject<MenuType<ContainerAstroBlade>> ASTRO_BLADE = CONTAINERS.register("astro_blade", () -> IForgeMenuType.create(ContainerAstroBlade::new));

    public static final RegistryObject<MenuType<ContainerPhylacteryStaff>> PHYLACTERY_STAFF = CONTAINERS.register("phylactery_book", () -> IForgeMenuType.create(ContainerPhylacteryStaff::new));

    public static final RegistryObject<MenuType<ContainerFilterItem>> FILTER_ITEM = CONTAINERS.register("filter_item", () -> IForgeMenuType.create(ContainerFilterItem::new));

    public static final RegistryObject<MenuType<ContainerRift>> RIFT = CONTAINERS.register("rift", () -> IForgeMenuType.create(ContainerRift::new));

    public static final RegistryObject<MenuType<ContainerGuideBook>> GUIDE_BOOK = CONTAINERS.register("guide_book", () -> IForgeMenuType.create(ContainerGuideBook::new));

    public static final RegistryObject<MenuType<ContainerEnderDisc>> ENDER_DISC = CONTAINERS.register("ender_disc", () -> IForgeMenuType.create(ContainerEnderDisc::new));

    public static final RegistryObject<MenuType<ContainerSpellName>> SPELL_CUSTOMIZATION = CONTAINERS.register("spell_customization", () -> IForgeMenuType.create(ContainerSpellName::new));

    public static final RegistryObject<MenuType<ContainerOcculus>> OCCULUS = CONTAINERS.register("occulus", () -> IForgeMenuType.create(ContainerOcculus::new));

    public static final RegistryObject<MenuType<ContainerSpellRecipe>> SPELL_RECIPE_LIST = CONTAINERS.register("spell_recipe_list", () -> IForgeMenuType.create(ContainerSpellRecipe::new));

    public static final RegistryObject<MenuType<ContainerCantrips>> CANTRIPS = CONTAINERS.register("cantrips", () -> IForgeMenuType.create(ContainerCantrips::new));

    public static final RegistryObject<MenuType<ContainerSpellAdjustments>> SPELL_ADJUSTMENTS = CONTAINERS.register("spell_adjustment", () -> IForgeMenuType.create(ContainerSpellAdjustments::new));

    public static final RegistryObject<MenuType<ContainerWanderingWizard>> WANDERING_WIZARD = CONTAINERS.register("wandering_wizard", () -> IForgeMenuType.create(ContainerWanderingWizard::new));

    static <T extends Block> String of(RegistryObject<T> block) {
        return block.getId().getPath();
    }
}
package io.redspace.ironsspellbooks;

import com.mojang.logging.LogUtils;
import io.redspace.ironsspellbooks.api.magic.MagicHelper;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.compat.Curios;
import io.redspace.ironsspellbooks.config.ClientConfigs;
import io.redspace.ironsspellbooks.config.ServerConfigs;
import io.redspace.ironsspellbooks.gui.arcane_anvil.ArcaneAnvilScreen;
import io.redspace.ironsspellbooks.gui.inscription_table.InscriptionTableScreen;
import io.redspace.ironsspellbooks.gui.scroll_forge.ScrollForgeScreen;
import io.redspace.ironsspellbooks.registries.BlockRegistry;
import io.redspace.ironsspellbooks.registries.CommandArgumentRegistry;
import io.redspace.ironsspellbooks.registries.CreativeTabRegistry;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.registries.FeatureRegistry;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import io.redspace.ironsspellbooks.registries.LootRegistry;
import io.redspace.ironsspellbooks.registries.MenuRegistry;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import io.redspace.ironsspellbooks.registries.OverlayRegistry;
import io.redspace.ironsspellbooks.registries.ParticleRegistry;
import io.redspace.ironsspellbooks.registries.PotionRegistry;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.registries.StructureElementRegistry;
import io.redspace.ironsspellbooks.registries.StructureProcessorRegistry;
import io.redspace.ironsspellbooks.setup.ModSetup;
import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Collectors;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.resource.PathPackResources;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

@Mod("irons_spellbooks")
public class IronsSpellbooks {

    public static final String MODID = "irons_spellbooks";

    public static final Logger LOGGER = LogUtils.getLogger();

    public static MagicManager MAGIC_MANAGER;

    public static MinecraftServer MCS;

    public static ServerLevel OVERWORLD;

    public IronsSpellbooks() {
        ModSetup.setup();
        MAGIC_MANAGER = new MagicManager();
        MagicHelper.MAGIC_MANAGER = MAGIC_MANAGER;
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(ModSetup::init);
        modEventBus.addListener(OverlayRegistry::onRegisterOverlays);
        SchoolRegistry.register(modEventBus);
        SpellRegistry.register(modEventBus);
        ItemRegistry.register(modEventBus);
        AttributeRegistry.register(modEventBus);
        BlockRegistry.register(modEventBus);
        MenuRegistry.register(modEventBus);
        EntityRegistry.register(modEventBus);
        LootRegistry.register(modEventBus);
        MobEffectRegistry.register(modEventBus);
        ParticleRegistry.register(modEventBus);
        SoundRegistry.register(modEventBus);
        FeatureRegistry.register(modEventBus);
        PotionRegistry.register(modEventBus);
        CommandArgumentRegistry.register(modEventBus);
        StructureProcessorRegistry.register(modEventBus);
        StructureElementRegistry.register(modEventBus);
        CreativeTabRegistry.register(modEventBus);
        modEventBus.addListener(this::clientSetup);
        modEventBus.addListener(this::addPackFinders);
        ModLoadingContext.get().registerConfig(Type.CLIENT, ClientConfigs.SPEC, String.format("%s-client.toml", "irons_spellbooks"));
        ModLoadingContext.get().registerConfig(Type.SERVER, ServerConfigs.SPEC, String.format("%s-server.toml", "irons_spellbooks"));
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void clientSetup(FMLClientSetupEvent e) {
        MenuScreens.register(MenuRegistry.INSCRIPTION_TABLE_MENU.get(), InscriptionTableScreen::new);
        MenuScreens.register(MenuRegistry.SCROLL_FORGE_MENU.get(), ScrollForgeScreen::new);
        MenuScreens.register(MenuRegistry.ARCANE_ANVIL_MENU.get(), ArcaneAnvilScreen::new);
        ItemBlockRenderTypes.setRenderLayer(BlockRegistry.INSCRIPTION_TABLE_BLOCK.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BlockRegistry.ARMOR_PILE_BLOCK.get(), RenderType.translucent());
    }

    public void addPackFinders(AddPackFindersEvent event) {
        LOGGER.debug("addPackFinders");
        try {
            if (event.getPackType() == PackType.CLIENT_RESOURCES) {
                addBuiltinPack(event, "legacy_dead_king_resource_pack", Component.literal("Legacy Dead King"));
            }
        } catch (IOException var3) {
            LOGGER.error("Failed to load a builtin resource pack! If you are seeing this message, please report an issue to https://github.com/iron431/Irons-Spells-n-Spellbooks/issues");
        }
    }

    private static void addBuiltinPack(AddPackFindersEvent event, String filename, Component displayName) throws IOException {
        filename = "builtin_resource_packs/" + filename;
        String id = "builtin/" + filename;
        Path resourcePath = ModList.get().getModFileById("irons_spellbooks").getFile().findResource(new String[] { filename });
        Pack pack = Pack.readMetaAndCreate(id, displayName, false, path -> new PathPackResources(path, true, resourcePath), PackType.CLIENT_RESOURCES, Pack.Position.TOP, PackSource.BUILT_IN);
        event.addRepositorySource(packConsumer -> packConsumer.accept(pack));
    }

    private void enqueueIMC(InterModEnqueueEvent event) {
        Curios.registerCurioSlot(Curios.RING_SLOT, 2, false, null);
        Curios.registerCurioSlot(Curios.NECKLACE_SLOT, 1, false, null);
        Curios.registerCurioSlot(Curios.SPELLBOOK_SLOT, 1, false, new ResourceLocation("curios:slot/spellbook_slot"));
    }

    private void processIMC(InterModProcessEvent event) {
        LOGGER.info("Got IMC {}", event.getIMCStream().map(m -> m.messageSupplier().get()).collect(Collectors.toList()));
    }

    public static ResourceLocation id(@NotNull String path) {
        return new ResourceLocation("irons_spellbooks", path);
    }
}
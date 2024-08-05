package noobanidus.mods.lootr;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import noobanidus.mods.lootr.command.CommandLootr;
import noobanidus.mods.lootr.config.ConfigManager;
import noobanidus.mods.lootr.init.ModAdvancements;
import noobanidus.mods.lootr.init.ModBlockEntities;
import noobanidus.mods.lootr.init.ModBlocks;
import noobanidus.mods.lootr.init.ModEntities;
import noobanidus.mods.lootr.init.ModItems;
import noobanidus.mods.lootr.init.ModLoot;
import noobanidus.mods.lootr.init.ModTabs;

@Mod("lootr")
public class Lootr {

    public CommandLootr COMMAND_LOOTR;

    public CreativeModeTab TAB;

    public Lootr() {
        ModLoadingContext context = ModLoadingContext.get();
        context.registerConfig(Type.COMMON, ConfigManager.COMMON_CONFIG);
        context.registerConfig(Type.CLIENT, ConfigManager.CLIENT_CONFIG);
        ConfigManager.loadConfig(ConfigManager.COMMON_CONFIG, FMLPaths.CONFIGDIR.get().resolve("lootr-common.toml"));
        if (ConfigManager.MAXIMUM_AGE.get() == 6000) {
            ConfigManager.MAXIMUM_AGE.set(Integer.valueOf(18000));
            ConfigManager.COMMON_CONFIG.save();
        }
        ConfigManager.loadConfig(ConfigManager.CLIENT_CONFIG, FMLPaths.CONFIGDIR.get().resolve("lootr-client.toml"));
        MinecraftForge.EVENT_BUS.addListener(this::onCommands);
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModTabs.register(modBus);
        ModBlockEntities.register(modBus);
        ModBlocks.register(modBus);
        ModEntities.register(modBus);
        ModItems.register(modBus);
        ModLoot.register(modBus);
    }

    public void onCommands(RegisterCommandsEvent event) {
        this.COMMAND_LOOTR = new CommandLootr(event.getDispatcher());
        this.COMMAND_LOOTR.register();
    }

    static {
        ModAdvancements.load();
    }
}
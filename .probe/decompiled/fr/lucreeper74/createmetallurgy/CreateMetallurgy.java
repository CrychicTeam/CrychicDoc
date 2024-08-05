package fr.lucreeper74.createmetallurgy;

import com.mojang.logging.LogUtils;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.item.TooltipModifier;
import fr.lucreeper74.createmetallurgy.content.casting.CastingWithSpout;
import fr.lucreeper74.createmetallurgy.content.light_bulb.network.NetworkHandler;
import fr.lucreeper74.createmetallurgy.data.CMDatagen;
import fr.lucreeper74.createmetallurgy.registries.CMArmInteract;
import fr.lucreeper74.createmetallurgy.registries.CMBlockEntityTypes;
import fr.lucreeper74.createmetallurgy.registries.CMBlocks;
import fr.lucreeper74.createmetallurgy.registries.CMCreativeTabs;
import fr.lucreeper74.createmetallurgy.registries.CMFluids;
import fr.lucreeper74.createmetallurgy.registries.CMItems;
import fr.lucreeper74.createmetallurgy.registries.CMPartialModels;
import fr.lucreeper74.createmetallurgy.registries.CMRecipeTypes;
import fr.lucreeper74.createmetallurgy.registries.CMSpriteShifts;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod("createmetallurgy")
public class CreateMetallurgy {

    public static final String MOD_ID = "createmetallurgy";

    public static final CreateRegistrate REGISTRATE = CreateRegistrate.create("createmetallurgy");

    public static final Logger LOGGER = LogUtils.getLogger();

    public static final NetworkHandler NETWORK_HANDLER = new NetworkHandler();

    public CreateMetallurgy() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        REGISTRATE.registerEventListeners(eventBus);
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> CMPartialModels::init);
        CMCreativeTabs.register(eventBus);
        CMBlocks.register();
        CMItems.register();
        CMFluids.register();
        CMArmInteract.register();
        CMSpriteShifts.init();
        CMBlockEntityTypes.register();
        CMRecipeTypes.register(eventBus);
        CastingWithSpout.registerDefaults();
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> CreateMetallurgyClient.loadClient(eventBus));
        eventBus.addListener(EventPriority.LOWEST, CMDatagen::gatherData);
        eventBus.addListener(this::setup);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(FMLCommonSetupEvent event) {
    }

    public static ResourceLocation genRL(String path) {
        return new ResourceLocation("createmetallurgy", path);
    }

    static {
        REGISTRATE.setTooltipModifierFactory(item -> new ItemDescription.Modifier(item, TooltipHelper.Palette.STANDARD_CREATE).andThen(TooltipModifier.mapNull(KineticStats.create(item))));
    }
}
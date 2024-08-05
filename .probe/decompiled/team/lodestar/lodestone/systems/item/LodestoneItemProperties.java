package team.lodestar.lodestone.systems.item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class LodestoneItemProperties extends Item.Properties {

    public static final Map<ResourceKey<CreativeModeTab>, List<ResourceLocation>> TAB_SORTING = new HashMap();

    public final ResourceKey<CreativeModeTab> tab;

    public LodestoneItemProperties(RegistryObject<CreativeModeTab> registryObject) {
        this(registryObject.getKey());
    }

    public LodestoneItemProperties(ResourceKey<CreativeModeTab> tab) {
        this.tab = tab;
    }

    public static void populateItemGroups(BuildCreativeModeTabContentsEvent event) {
        ResourceKey<CreativeModeTab> tabKey = event.getTabKey();
        if (TAB_SORTING.containsKey(tabKey)) {
            ((List) TAB_SORTING.get(tabKey)).stream().map(ForgeRegistries.ITEMS::getValue).filter(Objects::nonNull).forEach(event::m_246326_);
        }
    }
}
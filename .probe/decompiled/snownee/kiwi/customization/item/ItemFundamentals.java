package snownee.kiwi.customization.item;

import com.google.common.base.Preconditions;
import java.util.Map;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import snownee.kiwi.customization.item.loader.ConfiguredItemTemplate;
import snownee.kiwi.customization.item.loader.ItemDefinitionProperties;
import snownee.kiwi.customization.item.loader.KItemDefinition;
import snownee.kiwi.customization.item.loader.KItemTemplate;
import snownee.kiwi.util.resource.OneTimeLoader;

public record ItemFundamentals(Map<ResourceLocation, KItemTemplate> templates, Map<ResourceLocation, KItemDefinition> items, ConfiguredItemTemplate blockItemTemplate, ItemDefinitionProperties defaultProperties) {

    public static ItemFundamentals reload(ResourceManager resourceManager, OneTimeLoader.Context context, boolean booting) {
        Map<ResourceLocation, KItemTemplate> templates = OneTimeLoader.load(resourceManager, "kiwi/template/item", KItemTemplate.codec(), context);
        if (booting) {
            templates.forEach((key, value) -> value.resolve(key));
        }
        Map<ResourceLocation, KItemDefinition> items = OneTimeLoader.load(resourceManager, "kiwi/item", KItemDefinition.codec(templates), context);
        KItemTemplate blockItemTemplate = (KItemTemplate) templates.get(new ResourceLocation("block"));
        Preconditions.checkNotNull(blockItemTemplate, "Default block item template not found");
        return new ItemFundamentals(templates, items, new ConfiguredItemTemplate(blockItemTemplate, ConfiguredItemTemplate.DEFAULT_JSON), ItemDefinitionProperties.empty());
    }

    public void addDefaultBlockItem(ResourceLocation id) {
        this.items.put(id, new KItemDefinition(this.blockItemTemplate, this.defaultProperties));
    }
}
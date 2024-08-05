package snownee.kiwi.customization.item.loader;

import com.google.common.base.Preconditions;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Map;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import snownee.kiwi.customization.item.KItemSettings;
import snownee.kiwi.util.codec.CustomizationCodecs;

public record KItemDefinition(ConfiguredItemTemplate template, ItemDefinitionProperties properties) {

    public KItemDefinition(ConfiguredItemTemplate template, ItemDefinitionProperties properties) {
        this.template = template;
        this.properties = (ItemDefinitionProperties) template.template().properties().map(properties::merge).orElse(properties);
    }

    public static Codec<KItemDefinition> codec(Map<ResourceLocation, KItemTemplate> templates) {
        KItemTemplate defaultTemplate = (KItemTemplate) templates.get(new ResourceLocation("item"));
        Preconditions.checkNotNull(defaultTemplate);
        ConfiguredItemTemplate defaultConfiguredTemplate = new ConfiguredItemTemplate(defaultTemplate);
        return RecordCodecBuilder.create(instance -> instance.group(CustomizationCodecs.strictOptionalField(ConfiguredItemTemplate.codec(templates), "template", defaultConfiguredTemplate).forGetter(KItemDefinition::template), ItemDefinitionProperties.mapCodec().forGetter(KItemDefinition::properties)).apply(instance, KItemDefinition::new));
    }

    public KItemSettings.Builder createSettings(ResourceLocation id) {
        KItemSettings.Builder builder = KItemSettings.builder();
        ItemDefinitionProperties.PartialVanillaProperties vanilla = this.properties.vanillaProperties();
        builder.configure($ -> {
            vanilla.maxStackSize().ifPresent($::m_41487_);
            vanilla.maxDamage().ifPresent($::m_41503_);
            vanilla.craftingRemainingItem().map(BuiltInRegistries.ITEM::m_6246_).ifPresent($::m_41495_);
            vanilla.food().ifPresent($::m_41489_);
            vanilla.rarity().ifPresent($::m_41497_);
        });
        return builder;
    }

    public Item createItem(ResourceLocation id) {
        KItemSettings.Builder builder = this.createSettings(id);
        return this.template.template().createItem(id, builder.get(), this.template.json());
    }
}
package dev.latvian.mods.kubejs.block;

import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;

public class SeedItemBuilder extends BlockItemBuilder {

    public SeedItemBuilder(ResourceLocation i) {
        super(i);
    }

    @Override
    public String getTranslationKeyGroup() {
        return "item";
    }

    @Override
    public Item createObject() {
        return new ItemNameBlockItem(this.blockBuilder.get(), this.createItemProperties());
    }

    @Override
    public void generateAssetJsons(AssetJsonGenerator generator) {
        if (this.modelJson != null) {
            generator.json(AssetJsonGenerator.asItemModelLocation(this.id), this.modelJson);
        } else {
            generator.itemModel(this.id, m -> {
                if (!this.parentModel.isEmpty()) {
                    m.parent(this.parentModel);
                } else {
                    m.parent("minecraft:item/generated");
                }
                if (this.textureJson.size() == 0) {
                    this.texture(this.newID("item/", "").toString());
                }
                m.textures(this.textureJson);
            });
        }
    }
}
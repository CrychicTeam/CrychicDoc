package dev.latvian.mods.kubejs.fluid;

import dev.architectury.core.item.ArchitecturyBucketItem;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import dev.latvian.mods.kubejs.item.ItemBuilder;
import net.minecraft.world.item.BucketItem;

public class FluidBucketItemBuilder extends ItemBuilder {

    public final FluidBuilder fluidBuilder;

    public FluidBucketItemBuilder(FluidBuilder b) {
        super(b.newID("", "_bucket"));
        this.fluidBuilder = b;
        this.maxStackSize(1);
    }

    public BucketItem createObject() {
        return new ArchitecturyBucketItem(this.fluidBuilder, this.createItemProperties());
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
                    m.parent("kubejs:item/generated_bucket");
                }
                if (this.textureJson.size() > 0) {
                    m.textures(this.textureJson);
                }
            });
        }
    }
}
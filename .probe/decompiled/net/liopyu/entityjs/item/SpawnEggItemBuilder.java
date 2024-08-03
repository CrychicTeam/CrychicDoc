package net.liopyu.entityjs.item;

import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import dev.latvian.mods.kubejs.item.ItemBuilder;
import dev.latvian.mods.kubejs.typings.Info;
import java.util.function.Supplier;
import net.liopyu.entityjs.builders.living.entityjs.MobBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;

public class SpawnEggItemBuilder extends ItemBuilder {

    public transient int backgroundColor = -1;

    public transient int highlightColor = -1;

    public final transient MobBuilder<?> parent;

    public SpawnEggItemBuilder(ResourceLocation i, MobBuilder<?> parent) {
        super(i);
        this.parent = parent;
    }

    @Info("Sets the background color of the egg item")
    public SpawnEggItemBuilder backgroundColor(int i) {
        this.backgroundColor = i;
        return this;
    }

    @Info("Sets the highlight color of the egg item")
    public SpawnEggItemBuilder highlightColor(int i) {
        this.highlightColor = i;
        return this;
    }

    public Item createObject() {
        return new ForgeSpawnEggItem((Supplier<? extends EntityType<? extends Mob>>) this.parent, this.backgroundColor, this.highlightColor, this.createItemProperties());
    }

    @Override
    public void generateAssetJsons(AssetJsonGenerator generator) {
        if (this.modelJson != null) {
            generator.json(AssetJsonGenerator.asItemModelLocation(this.id), this.modelJson);
        } else {
            generator.itemModel(this.id, m -> {
                if (!this.parentModel.isEmpty()) {
                    m.parent(this.parentModel);
                    if (this.textureJson.size() == 0) {
                        this.texture(this.newID("item/", "").toString());
                    }
                    m.textures(this.textureJson);
                } else {
                    m.parent("item/template_spawn_egg");
                    if (this.textureJson.size() != 0) {
                        m.textures(this.textureJson);
                    }
                }
            });
        }
    }
}
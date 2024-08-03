package net.liopyu.entityjs.builders.nonliving.vanilla;

import dev.latvian.mods.kubejs.registry.RegistryInfo;
import dev.latvian.mods.kubejs.typings.Generics;
import dev.latvian.mods.kubejs.typings.Info;
import java.util.function.Consumer;
import java.util.function.Function;
import net.liopyu.entityjs.builders.nonliving.BaseEntityBuilder;
import net.liopyu.entityjs.builders.nonliving.NonAnimatableEntityTypeBuilder;
import net.liopyu.entityjs.entities.nonliving.vanilla.EyeOfEnderEntityJS;
import net.liopyu.entityjs.item.EyeOfEnderItemBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.EyeOfEnder;

public class EyeOfEnderJSBuilder extends EyeOfEnderEntityBuilder<EyeOfEnderEntityJS> {

    public transient Function<EyeOfEnder, Object> getItem;

    public transient EyeOfEnderItemBuilder item;

    public transient boolean noItem;

    public EyeOfEnderJSBuilder(ResourceLocation i) {
        super(i);
        this.item = (EyeOfEnderItemBuilder) new EyeOfEnderItemBuilder(this.id, this).texture(i.getNamespace() + ":item/" + i.getPath());
    }

    @Override
    public EntityType<EyeOfEnderEntityJS> createObject() {
        return new NonAnimatableEntityTypeBuilder<EyeOfEnderEntityJS>(this).get();
    }

    @Info("Sets a function to determine the itemstack the entity drops when it\nturns back into an item\nDefaults to eye of ender.\nExample usage:\n```javascript\nbuilder.getItem(entity => {\n    // Use information about the entity provided by the context.\n    return Item.of('kubejs:eye_of_ender')// Some ItemStack\n});\n```\n")
    public EyeOfEnderJSBuilder getItem(Function<EyeOfEnder, Object> function) {
        this.getItem = function;
        return this;
    }

    @Info("Indicates that no item should be created for this entity type")
    public EyeOfEnderJSBuilder noItem() {
        this.noItem = true;
        return this;
    }

    @Info("Creates the item for this entity type")
    @Generics({ BaseEntityBuilder.class })
    public EyeOfEnderJSBuilder item(Consumer<EyeOfEnderItemBuilder> item) {
        this.item = new EyeOfEnderItemBuilder(this.id, this);
        item.accept(this.item);
        return this;
    }

    @Override
    public EntityType.EntityFactory<EyeOfEnderEntityJS> factory() {
        return (type, level) -> new EyeOfEnderEntityJS(this, type, level);
    }

    @Override
    public void createAdditionalObjects() {
        if (!this.noItem) {
            RegistryInfo.ITEM.addBuilder(this.item);
        }
    }
}
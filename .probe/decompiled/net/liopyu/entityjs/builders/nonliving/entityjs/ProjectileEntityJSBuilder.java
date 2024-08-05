package net.liopyu.entityjs.builders.nonliving.entityjs;

import dev.latvian.mods.kubejs.registry.RegistryInfo;
import dev.latvian.mods.kubejs.typings.Generics;
import dev.latvian.mods.kubejs.typings.Info;
import java.util.function.Consumer;
import net.liopyu.entityjs.builders.nonliving.BaseEntityBuilder;
import net.liopyu.entityjs.entities.nonliving.entityjs.ProjectileEntityJS;
import net.liopyu.entityjs.item.ProjectileItemBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class ProjectileEntityJSBuilder extends ProjectileEntityBuilder<ProjectileEntityJS> {

    public transient ProjectileItemBuilder item;

    public transient boolean noItem;

    public transient Level level;

    public ProjectileEntityJSBuilder(ResourceLocation i) {
        super(i);
        this.item = (ProjectileItemBuilder) new ProjectileItemBuilder(this.id, this).canThrow(true).texture(i.getNamespace() + ":item/" + i.getPath());
    }

    @Override
    public EntityType.EntityFactory<ProjectileEntityJS> factory() {
        return (type, level) -> new ProjectileEntityJS(this, type, level);
    }

    @Info("Indicates that no projectile item should be created for this entity type")
    public ProjectileEntityJSBuilder noItem() {
        this.noItem = true;
        return this;
    }

    @Info("Creates the arrow item for this entity type")
    @Generics({ BaseEntityBuilder.class })
    public ProjectileEntityJSBuilder item(Consumer<ProjectileItemBuilder> item) {
        this.item = new ProjectileItemBuilder(this.id, this);
        item.accept(this.item);
        return this;
    }

    @Override
    public void createAdditionalObjects() {
        if (!this.noItem) {
            RegistryInfo.ITEM.addBuilder(this.item);
        }
    }
}
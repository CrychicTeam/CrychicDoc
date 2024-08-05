package net.liopyu.entityjs.builders.nonliving.modded;

import dev.latvian.mods.kubejs.registry.RegistryInfo;
import dev.latvian.mods.kubejs.typings.Info;
import java.util.function.Consumer;
import net.liopyu.entityjs.builders.nonliving.BaseEntityBuilder;
import net.liopyu.entityjs.entities.nonliving.modded.CGMProjectileEntityJS;
import net.liopyu.entityjs.item.CGMProjectileItemBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.level.Level;

public class CGMProjectileEntityJSBuilder extends BaseEntityBuilder<CGMProjectileEntityJS> {

    public transient CGMProjectileItemBuilder item;

    public transient boolean noItem;

    public transient Consumer<CGMProjectileEntityJS.ShotContext> onHitEntity;

    public transient Consumer<CGMProjectileEntityJS.HitBlockContext> onHitBlock;

    public transient Consumer<Entity> onProjectileTick;

    public transient boolean explosionEnabled = false;

    public transient Level level;

    public CGMProjectileEntityJSBuilder(ResourceLocation i) {
        super(i);
        this.noItem = false;
        this.item = (CGMProjectileItemBuilder) new CGMProjectileItemBuilder(this.id, this).texture(i.getNamespace() + ":item/" + i.getPath());
    }

    @Info("Indicates that no projectile item should be created for this entity type")
    public CGMProjectileEntityJSBuilder noItem() {
        this.noItem = true;
        return this;
    }

    @Info("Creates the projectile item for this entity type")
    public CGMProjectileEntityJSBuilder item(Consumer<CGMProjectileItemBuilder> item) {
        this.item = new CGMProjectileItemBuilder(this.id, this);
        item.accept(this.item);
        return this;
    }

    @Override
    public void createAdditionalObjects() {
        if (!this.noItem) {
            RegistryInfo.ITEM.addBuilder(this.item);
        }
    }

    @Info("@param explosionEnabled A boolean deciding whether or not default explosion behavior is enabled.\nDefaults to false.\n\nExample usage:\n```javascript\nentityBuilder.explosionEnabled(true);\n```\n")
    public CGMProjectileEntityJSBuilder explosionEnabled(boolean b) {
        this.explosionEnabled = b;
        return this;
    }

    @Info("Sets a callback function to override the onProjectileTick param.\n\n@param onProjectileTick A Consumer accepting a {@link Entity} parameter, defining the behavior to be executed on each tick.\n\nExample usage:\n```javascript\nentityBuilder.onProjectileTick(entity => {\n    // This overrides the onProjectileTick method giving scriptors\n    // a chance to override the custom particle behavior ect.\n});\n```\n")
    public CGMProjectileEntityJSBuilder onProjectileTick(Consumer<Entity> consumer) {
        this.onProjectileTick = consumer;
        return this;
    }

    @Info("Sets a callback function to be executed when the projectile hits an entity.\n\n@param onHitEntity A Consumer accepting a {@link CGMProjectileEntityJS.ShotContext} parameter.\n\nExample usage:\n```javascript\nentityBuilder.onHitEntity(entity => {\n    // Custom logic to be executed when the projectile hits an entity\n});\n```\n")
    public CGMProjectileEntityJSBuilder onHitEntity(Consumer<CGMProjectileEntityJS.ShotContext> consumer) {
        this.onHitEntity = consumer;
        return this;
    }

    @Info("Sets a callback function to be executed when the projectile hits a block.\n\n@param onHitBlock A Consumer accepting a {@link CGMProjectileEntityJS.HitBlockContext} parameter.\n\nExample usage:\n```javascript\nentityBuilder.onHitBlock(entity => {\n    // Custom logic to be executed when the projectile hits a block\n});\n```\n")
    public CGMProjectileEntityJSBuilder onHitBlock(Consumer<CGMProjectileEntityJS.HitBlockContext> consumer) {
        this.onHitBlock = consumer;
        return this;
    }

    @Override
    public EntityType.EntityFactory<CGMProjectileEntityJS> factory() {
        return (type, level) -> new CGMProjectileEntityJS(this, type, level);
    }

    @Override
    public AttributeSupplier.Builder getAttributeBuilder() {
        return null;
    }
}
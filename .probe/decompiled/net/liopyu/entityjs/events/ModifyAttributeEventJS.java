package net.liopyu.entityjs.events;

import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.typings.Param;
import dev.latvian.mods.rhino.util.HideFromJS;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import net.liopyu.entityjs.util.EntityJSHelperClass;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.registries.ForgeRegistries;

public class ModifyAttributeEventJS extends EventJS {

    private final EntityAttributeModificationEvent event;

    public ModifyAttributeEventJS(EntityAttributeModificationEvent event) {
        this.event = event;
    }

    @Info(value = "Modifies the given entity type's attributes", params = { @Param(name = "entityType", value = "The entity type whose default attributes are to be modified"), @Param(name = "attributes", value = "A consumer for setting the default attributes and their values") })
    public void modify(EntityType<? extends LivingEntity> entityType, Consumer<ModifyAttributeEventJS.AttributeModificationHelper> attributes) {
        ModifyAttributeEventJS.AttributeModificationHelper helper = new ModifyAttributeEventJS.AttributeModificationHelper(entityType, this.event);
        attributes.accept(helper);
    }

    @Info("Returns a list of all entity types that can have their attributes modified by this event")
    public List<EntityType<? extends LivingEntity>> getAllTypes() {
        return this.event.getTypes();
    }

    @Info("Returns a list of all attributes the given entity type has by default")
    public List<Attribute> getAttributes(EntityType<? extends LivingEntity> entityType) {
        List<Attribute> present = new ArrayList();
        for (Attribute attribute : ForgeRegistries.ATTRIBUTES.getValues()) {
            if (this.event.has(entityType, attribute)) {
                present.add(attribute);
            }
        }
        return present;
    }

    public static record AttributeModificationHelper(@HideFromJS EntityType<? extends LivingEntity> type, @HideFromJS EntityAttributeModificationEvent event) {

        @Info("Adds the given attribute to the entity type, using its default value\n\nIt is safe to add an attribute that an entity type already has\n")
        public void add(Attribute attribute) {
            this.event.add(this.type, attribute);
        }

        @Info(value = "Adds the given attribute to the entity type, using the provided default value\n\nIt is safe to add an attribute that an entity type already has\n", params = { @Param(name = "attribute", value = "The attribute to add"), @Param(name = "defaultValue", value = "The default value of the attribute") })
        public void add(Object attribute, double defaultValue) {
            if (attribute instanceof String string) {
                ResourceLocation stringLocation = new ResourceLocation(string.toLowerCase());
                Attribute att = ForgeRegistries.ATTRIBUTES.getValue(stringLocation);
                if (att != null) {
                    this.event.add(this.type, att, defaultValue);
                } else {
                    EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Unable to add attribute, attribute " + attribute + " does not exist");
                }
            } else if (attribute instanceof Attribute att) {
                this.event.add(this.type, att, defaultValue);
            } else {
                EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Unable to add attribute, attribute: " + attribute + ". Must be of type Attribute or resource location. Example: \"minecraft:generic.max_health\"");
            }
        }
    }
}
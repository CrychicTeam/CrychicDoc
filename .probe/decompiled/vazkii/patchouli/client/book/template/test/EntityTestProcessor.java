package vazkii.patchouli.client.book.template.test;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.api.IVariableProvider;

public class EntityTestProcessor implements IComponentProcessor {

    private String entityName;

    @Override
    public void setup(Level level, IVariableProvider variables) {
        String entityType = variables.get("entity").unwrap().getAsString();
        if (entityType.contains("{")) {
            entityType = entityType.substring(0, entityType.indexOf("{"));
        }
        ResourceLocation key = new ResourceLocation(entityType);
        this.entityName = (String) BuiltInRegistries.ENTITY_TYPE.m_6612_(key).map(EntityType::m_20676_).map(Component::getString).orElse(null);
    }

    @Override
    public IVariable process(Level level, String key) {
        return key.equals("name") ? IVariable.wrap(this.entityName) : null;
    }
}
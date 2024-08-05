package top.theillusivec4.curios.api.type.data;

import com.google.gson.JsonObject;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.crafting.conditions.ICondition;

public interface IEntitiesData {

    IEntitiesData replace(boolean var1);

    IEntitiesData addPlayer();

    IEntitiesData addEntities(EntityType<?>... var1);

    IEntitiesData addSlots(String... var1);

    IEntitiesData addCondition(ICondition var1);

    JsonObject serialize();
}
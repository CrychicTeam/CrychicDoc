package top.theillusivec4.curios.api.type.data;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.common.crafting.conditions.ICondition;
import top.theillusivec4.curios.api.type.capability.ICurio;

public interface ISlotData {

    ISlotData replace(boolean var1);

    ISlotData order(int var1);

    ISlotData size(int var1);

    ISlotData operation(AttributeModifier.Operation var1);

    ISlotData useNativeGui(boolean var1);

    ISlotData addCosmetic(boolean var1);

    ISlotData renderToggle(boolean var1);

    ISlotData icon(ResourceLocation var1);

    ISlotData dropRule(ICurio.DropRule var1);

    ISlotData addCondition(ICondition var1);

    ISlotData addValidator(ResourceLocation var1);

    JsonObject serialize();
}
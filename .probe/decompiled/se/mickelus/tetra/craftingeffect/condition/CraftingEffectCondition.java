package se.mickelus.tetra.craftingeffect.condition;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ToolAction;
import se.mickelus.tetra.craftingeffect.CraftingEffectRegistry;
import se.mickelus.tetra.data.DataManager;
import se.mickelus.tetra.module.schematic.UpgradeSchematic;

public interface CraftingEffectCondition {

    CraftingEffectCondition any = (unlocks, upgradedStack, slot, isReplacing, player, materials, tools, schematic, world, pos, blockState) -> true;

    boolean test(ResourceLocation[] var1, ItemStack var2, String var3, boolean var4, Player var5, ItemStack[] var6, Map<ToolAction, Integer> var7, UpgradeSchematic var8, Level var9, BlockPos var10, BlockState var11);

    public static class Deserializer implements JsonDeserializer<CraftingEffectCondition> {

        public CraftingEffectCondition deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            String type = jsonObject.get("type").getAsString();
            Class<? extends CraftingEffectCondition> clazz = CraftingEffectRegistry.getConditionClass(type);
            if (clazz != null) {
                return (CraftingEffectCondition) DataManager.gson.fromJson(json, clazz);
            } else {
                throw new JsonParseException("Crafting effect condition type \"" + type + "\" is not valid");
            }
        }
    }
}
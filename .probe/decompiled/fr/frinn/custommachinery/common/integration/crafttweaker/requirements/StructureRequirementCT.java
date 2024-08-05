package fr.frinn.custommachinery.common.integration.crafttweaker.requirements;

import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.google.gson.JsonPrimitive;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.DataResult.PartialResult;
import fr.frinn.custommachinery.api.integration.crafttweaker.RecipeCTBuilder;
import fr.frinn.custommachinery.common.requirement.StructureRequirement;
import fr.frinn.custommachinery.common.util.PartialBlockState;
import fr.frinn.custommachinery.common.util.ingredient.IIngredient;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.openzen.zencode.java.ZenCodeType.Method;
import org.openzen.zencode.java.ZenCodeType.Name;

@ZenRegister
@Name("mods.custommachinery.requirement.Structure")
public interface StructureRequirementCT<T> extends RecipeCTBuilder<T> {

    @Method
    default T destroyStructure(String[][] pattern, Map<String, String> keys) {
        return this.requireStructure(pattern, keys, StructureRequirement.Action.DESTROY);
    }

    @Method
    default T breakStructure(String[][] pattern, Map<String, String> keys) {
        return this.requireStructure(pattern, keys, StructureRequirement.Action.BREAK);
    }

    @Method
    default T requireStructure(String[][] pattern, Map<String, String> keys) {
        return this.requireStructure(pattern, keys, StructureRequirement.Action.CHECK);
    }

    default T placeStructure(String[][] pattern, Map<String, String> keys, boolean drops) {
        return this.requireStructure(pattern, keys, drops ? StructureRequirement.Action.PLACE_BREAK : StructureRequirement.Action.PLACE_DESTROY);
    }

    @Method
    default T requireStructure(String[][] pattern, Map<String, String> keys, StructureRequirement.Action action) {
        List<List<String>> patternList = Arrays.stream(pattern).map(floors -> Arrays.stream(floors).toList()).toList();
        Map<Character, IIngredient<PartialBlockState>> keysMap = new HashMap();
        for (Entry<String, String> entry : keys.entrySet()) {
            if (((String) entry.getKey()).length() != 1) {
                return this.error("Invalid structure key: {}\nMust be a single character which is not 'm'", new Object[] { entry.getKey() });
            }
            char keyChar = ((String) entry.getKey()).charAt(0);
            DataResult<IIngredient<PartialBlockState>> result = IIngredient.BLOCK.read(JsonOps.INSTANCE, new JsonPrimitive((String) entry.getValue()));
            if (result.error().isPresent() || result.result().isEmpty()) {
                return this.error("Invalid structure block: {}\n{}", new Object[] { entry.getValue(), ((PartialResult) result.error().get()).message() });
            }
            keysMap.put(keyChar, (IIngredient) result.result().get());
        }
        try {
            return this.addRequirement(new StructureRequirement(patternList, keysMap, action));
        } catch (IllegalStateException var10) {
            return this.error("Error while creating structure requirement: {}\nPattern: {}\nKeys: {}", new Object[] { var10.getMessage(), pattern, keys });
        }
    }
}
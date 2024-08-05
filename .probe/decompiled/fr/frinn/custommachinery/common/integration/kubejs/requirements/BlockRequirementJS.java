package fr.frinn.custommachinery.common.integration.kubejs.requirements;

import com.google.gson.JsonPrimitive;
import com.mojang.serialization.JsonOps;
import dev.latvian.mods.kubejs.script.ScriptType;
import fr.frinn.custommachinery.api.integration.kubejs.RecipeJSBuilder;
import fr.frinn.custommachinery.api.requirement.RequirementIOMode;
import fr.frinn.custommachinery.common.requirement.BlockRequirement;
import fr.frinn.custommachinery.common.util.ComparatorMode;
import fr.frinn.custommachinery.common.util.PartialBlockState;
import fr.frinn.custommachinery.common.util.ingredient.IIngredient;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import net.minecraft.world.phys.AABB;

public interface BlockRequirementJS extends RecipeJSBuilder {

    default RecipeJSBuilder requireBlock(String[] filter, boolean whitelist, int startX, int startY, int startZ, int endX, int endY, int endZ) {
        return this.requireBlock(filter, whitelist, startX, startY, startZ, endX, endY, endZ, 1, ">=");
    }

    default RecipeJSBuilder requireBlock(String[] filter, boolean whitelist, int startX, int startY, int startZ, int endX, int endY, int endZ, int amount) {
        return this.requireBlock(filter, whitelist, startX, startY, startZ, endX, endY, endZ, amount, ">=");
    }

    default RecipeJSBuilder requireBlock(String[] filter, boolean whitelist, int startX, int startY, int startZ, int endX, int endY, int endZ, int amount, String comparator) {
        return this.blockRequirement(RequirementIOMode.INPUT, BlockRequirement.ACTION.CHECK, "", startX, startY, startZ, endX, endY, endZ, amount, comparator, filter, whitelist);
    }

    default RecipeJSBuilder placeBlockOnStart(String block, int startX, int startY, int startZ, int endX, int endY, int endZ) {
        return this.placeBlockOnStart(block, startX, startY, startZ, endX, endY, endZ, 1);
    }

    default RecipeJSBuilder placeBlockOnStart(String block, int startX, int startY, int startZ, int endX, int endY, int endZ, int amount) {
        return this.blockRequirement(RequirementIOMode.INPUT, BlockRequirement.ACTION.PLACE, block, startX, startY, startZ, endX, endY, endZ, amount, ComparatorMode.EQUALS.toString(), new String[0], true);
    }

    default RecipeJSBuilder placeBlockOnEnd(String block, int startX, int startY, int startZ, int endX, int endY, int endZ) {
        return this.placeBlockOnEnd(block, startX, startY, startZ, endX, endY, endZ, 1);
    }

    default RecipeJSBuilder placeBlockOnEnd(String block, int startX, int startY, int startZ, int endX, int endY, int endZ, int amount) {
        return this.blockRequirement(RequirementIOMode.OUTPUT, BlockRequirement.ACTION.PLACE, block, startX, startY, startZ, endX, endY, endZ, amount, ComparatorMode.EQUALS.toString(), new String[0], true);
    }

    default RecipeJSBuilder breakAndPlaceBlockOnStart(String block, int startX, int startY, int startZ, int endX, int endY, int endZ) {
        return this.breakAndPlaceBlockOnStart(block, startX, startY, startZ, endX, endY, endZ, 1);
    }

    default RecipeJSBuilder breakAndPlaceBlockOnStart(String block, int startX, int startY, int startZ, int endX, int endY, int endZ, int amount) {
        return this.breakAndPlaceBlockOnStart(block, startX, startY, startZ, endX, endY, endZ, amount, new String[0]);
    }

    default RecipeJSBuilder breakAndPlaceBlockOnStart(String block, int startX, int startY, int startZ, int endX, int endY, int endZ, int amount, String[] filter) {
        return this.breakAndPlaceBlockOnStart(block, startX, startY, startZ, endX, endY, endZ, amount, filter, false);
    }

    default RecipeJSBuilder breakAndPlaceBlockOnStart(String block, int startX, int startY, int startZ, int endX, int endY, int endZ, int amount, String[] filter, boolean whitelist) {
        return this.blockRequirement(RequirementIOMode.INPUT, BlockRequirement.ACTION.REPLACE_BREAK, block, startX, startY, startZ, endX, endY, endZ, amount, ComparatorMode.EQUALS.toString(), filter, whitelist);
    }

    default RecipeJSBuilder breakAndPlaceBlockOnEnd(String block, int startX, int startY, int startZ, int endX, int endY, int endZ) {
        return this.breakAndPlaceBlockOnEnd(block, startX, startY, startZ, endX, endY, endZ, 1);
    }

    default RecipeJSBuilder breakAndPlaceBlockOnEnd(String block, int startX, int startY, int startZ, int endX, int endY, int endZ, int amount) {
        return this.breakAndPlaceBlockOnEnd(block, startX, startY, startZ, endX, endY, endZ, amount, new String[0]);
    }

    default RecipeJSBuilder breakAndPlaceBlockOnEnd(String block, int startX, int startY, int startZ, int endX, int endY, int endZ, int amount, String[] filter) {
        return this.breakAndPlaceBlockOnEnd(block, startX, startY, startZ, endX, endY, endZ, amount, filter, false);
    }

    default RecipeJSBuilder breakAndPlaceBlockOnEnd(String block, int startX, int startY, int startZ, int endX, int endY, int endZ, int amount, String[] filter, boolean whitelist) {
        return this.blockRequirement(RequirementIOMode.OUTPUT, BlockRequirement.ACTION.REPLACE_BREAK, block, startX, startY, startZ, endX, endY, endZ, amount, ComparatorMode.EQUALS.toString(), filter, whitelist);
    }

    default RecipeJSBuilder destroyAndPlaceBlockOnStart(String block, int startX, int startY, int startZ, int endX, int endY, int endZ) {
        return this.destroyAndPlaceBlockOnStart(block, startX, startY, startZ, endX, endY, endZ, 1);
    }

    default RecipeJSBuilder destroyAndPlaceBlockOnStart(String block, int startX, int startY, int startZ, int endX, int endY, int endZ, int amount) {
        return this.destroyAndPlaceBlockOnStart(block, startX, startY, startZ, endX, endY, endZ, amount, new String[0]);
    }

    default RecipeJSBuilder destroyAndPlaceBlockOnStart(String block, int startX, int startY, int startZ, int endX, int endY, int endZ, int amount, String[] filter) {
        return this.destroyAndPlaceBlockOnStart(block, startX, startY, startZ, endX, endY, endZ, amount, filter, false);
    }

    default RecipeJSBuilder destroyAndPlaceBlockOnStart(String block, int startX, int startY, int startZ, int endX, int endY, int endZ, int amount, String[] filter, boolean whitelist) {
        return this.blockRequirement(RequirementIOMode.INPUT, BlockRequirement.ACTION.REPLACE_DESTROY, block, startX, startY, startZ, endX, endY, endZ, amount, ComparatorMode.EQUALS.toString(), filter, whitelist);
    }

    default RecipeJSBuilder destroyAndPlaceBlockOnEnd(String block, int startX, int startY, int startZ, int endX, int endY, int endZ) {
        return this.destroyAndPlaceBlockOnEnd(block, startX, startY, startZ, endX, endY, endZ, 1);
    }

    default RecipeJSBuilder destroyAndPlaceBlockOnEnd(String block, int startX, int startY, int startZ, int endX, int endY, int endZ, int amount) {
        return this.destroyAndPlaceBlockOnEnd(block, startX, startY, startZ, endX, endY, endZ, amount, new String[0]);
    }

    default RecipeJSBuilder destroyAndPlaceBlockOnEnd(String block, int startX, int startY, int startZ, int endX, int endY, int endZ, int amount, String[] filter) {
        return this.destroyAndPlaceBlockOnEnd(block, startX, startY, startZ, endX, endY, endZ, amount, filter, false);
    }

    default RecipeJSBuilder destroyAndPlaceBlockOnEnd(String block, int startX, int startY, int startZ, int endX, int endY, int endZ, int amount, String[] filter, boolean whitelist) {
        return this.blockRequirement(RequirementIOMode.OUTPUT, BlockRequirement.ACTION.REPLACE_DESTROY, block, startX, startY, startZ, endX, endY, endZ, amount, ComparatorMode.EQUALS.toString(), filter, whitelist);
    }

    default RecipeJSBuilder destroyBlockOnStart(String[] filter, boolean whitelist, int startX, int startY, int startZ, int endX, int endY, int endZ) {
        return this.destroyBlockOnStart(filter, whitelist, startX, startY, startZ, endX, endY, endZ, 1);
    }

    default RecipeJSBuilder destroyBlockOnStart(String[] filter, boolean whitelist, int startX, int startY, int startZ, int endX, int endY, int endZ, int amount) {
        return this.blockRequirement(RequirementIOMode.INPUT, BlockRequirement.ACTION.DESTROY, "", startX, startY, startZ, endX, endY, endZ, amount, ComparatorMode.EQUALS.toString(), filter, whitelist);
    }

    default RecipeJSBuilder destroyBlockOnEnd(String[] filter, boolean whitelist, int startX, int startY, int startZ, int endX, int endY, int endZ) {
        return this.destroyBlockOnEnd(filter, whitelist, startX, startY, startZ, endX, endY, endZ, 1);
    }

    default RecipeJSBuilder destroyBlockOnEnd(String[] filter, boolean whitelist, int startX, int startY, int startZ, int endX, int endY, int endZ, int amount) {
        return this.blockRequirement(RequirementIOMode.OUTPUT, BlockRequirement.ACTION.DESTROY, "", startX, startY, startZ, endX, endY, endZ, amount, ComparatorMode.EQUALS.toString(), filter, whitelist);
    }

    default RecipeJSBuilder breakBlockOnStart(String[] filter, boolean whitelist, int startX, int startY, int startZ, int endX, int endY, int endZ) {
        return this.breakBlockOnStart(filter, whitelist, startX, startY, startZ, endX, endY, endZ, 1);
    }

    default RecipeJSBuilder breakBlockOnStart(String[] filter, boolean whitelist, int startX, int startY, int startZ, int endX, int endY, int endZ, int amount) {
        return this.blockRequirement(RequirementIOMode.INPUT, BlockRequirement.ACTION.BREAK, "", startX, startY, startZ, endX, endY, endZ, amount, ComparatorMode.EQUALS.toString(), filter, whitelist);
    }

    default RecipeJSBuilder breakBlockOnEnd(String[] filter, boolean whitelist, int startX, int startY, int startZ, int endX, int endY, int endZ) {
        return this.breakBlockOnEnd(filter, whitelist, startX, startY, startZ, endX, endY, endZ, 1);
    }

    default RecipeJSBuilder breakBlockOnEnd(String[] filter, boolean whitelist, int startX, int startY, int startZ, int endX, int endY, int endZ, int amount) {
        return this.blockRequirement(RequirementIOMode.OUTPUT, BlockRequirement.ACTION.BREAK, "", startX, startY, startZ, endX, endY, endZ, amount, ComparatorMode.EQUALS.toString(), filter, whitelist);
    }

    default RecipeJSBuilder blockRequirement(RequirementIOMode mode, BlockRequirement.ACTION action, String block, int startX, int startY, int startZ, int endX, int endY, int endZ, int amount, String comparator, String[] stringFilter, boolean whitelist) {
        PartialBlockState state;
        if (block.isEmpty()) {
            state = PartialBlockState.AIR;
        } else {
            state = (PartialBlockState) PartialBlockState.CODEC.read(JsonOps.INSTANCE, new JsonPrimitive(block)).resultOrPartial(ScriptType.SERVER.console::warn).orElse(null);
        }
        if (state == null) {
            return this.error("Invalid block: {}", new Object[] { block });
        } else {
            AABB bb = new AABB((double) startX, (double) startY, (double) startZ, (double) endX, (double) endY, (double) endZ);
            List<IIngredient<PartialBlockState>> filter = Arrays.stream(stringFilter).map(s -> (IIngredient) IIngredient.BLOCK.read(JsonOps.INSTANCE, new JsonPrimitive(s)).resultOrPartial(ScriptType.SERVER.console::warn).orElse(null)).filter(Objects::nonNull).toList();
            try {
                return this.addRequirement(new BlockRequirement(mode, action, bb, amount, ComparatorMode.value(comparator), state, filter, whitelist));
            } catch (IllegalArgumentException var18) {
                return this.error("Invalid comparator: {}", new Object[] { comparator });
            }
        }
    }
}
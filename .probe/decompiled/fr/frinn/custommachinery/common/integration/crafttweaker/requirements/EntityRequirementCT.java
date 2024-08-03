package fr.frinn.custommachinery.common.integration.crafttweaker.requirements;

import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import fr.frinn.custommachinery.api.integration.crafttweaker.RecipeCTBuilder;
import fr.frinn.custommachinery.api.requirement.RequirementIOMode;
import fr.frinn.custommachinery.common.requirement.EntityRequirement;
import java.util.Arrays;
import net.minecraft.world.entity.EntityType;
import org.openzen.zencode.java.ZenCodeType.Method;
import org.openzen.zencode.java.ZenCodeType.Name;
import org.openzen.zencode.java.ZenCodeType.Optional;
import org.openzen.zencode.java.ZenCodeType.OptionalBoolean;

@ZenRegister
@Name("mods.custommachinery.requirement.Entity")
public interface EntityRequirementCT<T> extends RecipeCTBuilder<T> {

    @Method
    default T requireEntities(int amount, int radius, @Optional EntityType[] filter, @OptionalBoolean(true) boolean whitelist) {
        return this.addRequirement(new EntityRequirement(RequirementIOMode.INPUT, EntityRequirement.ACTION.CHECK_AMOUNT, amount, radius, Arrays.asList(filter), whitelist));
    }

    @Method
    default T requireEntitiesHealth(int amount, int radius, @Optional EntityType[] filter, @OptionalBoolean(true) boolean whitelist) {
        return this.addRequirement(new EntityRequirement(RequirementIOMode.INPUT, EntityRequirement.ACTION.CHECK_HEALTH, amount, radius, Arrays.asList(filter), whitelist));
    }

    @Method
    default T consumeEntityHealthOnStart(int amount, int radius, @Optional EntityType[] filter, @OptionalBoolean(true) boolean whitelist) {
        return this.addRequirement(new EntityRequirement(RequirementIOMode.INPUT, EntityRequirement.ACTION.CONSUME_HEALTH, amount, radius, Arrays.asList(filter), whitelist));
    }

    @Method
    default T consumeEntityHealthOnEnd(int amount, int radius, @Optional EntityType[] filter, @OptionalBoolean(true) boolean whitelist) {
        return this.addRequirement(new EntityRequirement(RequirementIOMode.OUTPUT, EntityRequirement.ACTION.CONSUME_HEALTH, amount, radius, Arrays.asList(filter), whitelist));
    }

    @Method
    default T killEntityOnStart(int amount, int radius, @Optional EntityType[] filter, @OptionalBoolean(true) boolean whitelist) {
        return this.addRequirement(new EntityRequirement(RequirementIOMode.INPUT, EntityRequirement.ACTION.KILL, amount, radius, Arrays.asList(filter), whitelist));
    }

    @Method
    default T killEntityOnEnd(int amount, int radius, @Optional EntityType[] filter, @OptionalBoolean(true) boolean whitelist) {
        return this.addRequirement(new EntityRequirement(RequirementIOMode.OUTPUT, EntityRequirement.ACTION.KILL, amount, radius, Arrays.asList(filter), whitelist));
    }
}
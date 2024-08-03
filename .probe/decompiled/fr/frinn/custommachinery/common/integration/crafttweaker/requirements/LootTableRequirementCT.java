package fr.frinn.custommachinery.common.integration.crafttweaker.requirements;

import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import fr.frinn.custommachinery.api.integration.crafttweaker.RecipeCTBuilder;
import fr.frinn.custommachinery.common.requirement.LootTableRequirement;
import fr.frinn.custommachinery.common.util.Utils;
import net.minecraft.resources.ResourceLocation;
import org.openzen.zencode.java.ZenCodeType.Method;
import org.openzen.zencode.java.ZenCodeType.Name;
import org.openzen.zencode.java.ZenCodeType.OptionalFloat;

@ZenRegister
@Name("mods.custommachinery.requirement.LootTable")
public interface LootTableRequirementCT<T> extends RecipeCTBuilder<T> {

    @Method
    default T lootTableOutput(String lootTable, @OptionalFloat float luck) {
        if (!Utils.isResourceNameValid(lootTable)) {
            return this.error("Invalid loot table id: {}", new Object[] { lootTable });
        } else {
            ResourceLocation tableLoc = new ResourceLocation(lootTable);
            return this.addRequirement(new LootTableRequirement(tableLoc, luck));
        }
    }
}
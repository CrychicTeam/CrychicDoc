package fr.frinn.custommachinery.common.integration.crafttweaker.requirements;

import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import fr.frinn.custommachinery.api.integration.crafttweaker.RecipeCTBuilder;
import fr.frinn.custommachinery.common.requirement.DimensionRequirement;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import net.minecraft.ResourceLocationException;
import net.minecraft.resources.ResourceLocation;
import org.openzen.zencode.java.ZenCodeType.Method;
import org.openzen.zencode.java.ZenCodeType.Name;

@ZenRegister
@Name("mods.custommachinery.requirement.Dimension")
public interface DimensionRequirementCT<T> extends RecipeCTBuilder<T> {

    @Method
    default T dimensionWhitelist(String[] dimensions) {
        try {
            List<ResourceLocation> dimensionsID = Arrays.stream(dimensions).map(ResourceLocation::new).toList();
            return this.addRequirement(new DimensionRequirement(dimensionsID, false));
        } catch (ResourceLocationException var3) {
            return this.error("Invalid dimension ID: {}", new Object[] { var3.getMessage() });
        }
    }

    @Method
    default T dimensionWhitelist(String dimension) {
        try {
            return this.addRequirement(new DimensionRequirement(Collections.singletonList(new ResourceLocation(dimension)), false));
        } catch (ResourceLocationException var3) {
            return this.error("Invalid dimension ID: {}", new Object[] { var3.getMessage() });
        }
    }

    @Method
    default T dimensionBlacklist(String[] dimensions) {
        try {
            List<ResourceLocation> dimensionsID = Arrays.stream(dimensions).map(ResourceLocation::new).toList();
            return this.addRequirement(new DimensionRequirement(dimensionsID, false));
        } catch (ResourceLocationException var3) {
            return this.error("Invalid dimension ID: {}", new Object[] { var3.getMessage() });
        }
    }

    @Method
    default T dimensionBlacklist(String dimension) {
        try {
            return this.addRequirement(new DimensionRequirement(Collections.singletonList(new ResourceLocation(dimension)), false));
        } catch (ResourceLocationException var3) {
            return this.error("Invalid dimension ID: {}", new Object[] { var3.getMessage() });
        }
    }
}
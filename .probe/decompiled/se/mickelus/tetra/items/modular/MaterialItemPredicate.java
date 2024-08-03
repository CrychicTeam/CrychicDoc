package se.mickelus.tetra.items.modular;

import com.google.gson.JsonObject;
import java.util.Comparator;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.world.item.ItemStack;
import se.mickelus.tetra.data.DataManager;
import se.mickelus.tetra.module.data.MaterialData;

@ParametersAreNonnullByDefault
public class MaterialItemPredicate extends ItemPredicate {

    private final String category;

    public MaterialItemPredicate(JsonObject jsonObject) {
        this.category = jsonObject.get("category").getAsString();
    }

    @Override
    public boolean matches(ItemStack itemStack) {
        MaterialData materialData = (MaterialData) DataManager.instance.materialData.getData().values().stream().sorted(Comparator.comparing(data -> data.material.isTagged())).filter(data -> data.material.isValid()).filter(data -> data.material.getPredicate().matches(itemStack)).findFirst().orElse(null);
        return materialData == null ? false : this.category == null || this.category.equals(materialData.category);
    }
}
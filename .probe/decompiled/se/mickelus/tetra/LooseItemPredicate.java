package se.mickelus.tetra;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.Optional;
import java.util.stream.StreamSupport;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

@ParametersAreNonnullByDefault
public class LooseItemPredicate extends ItemPredicate {

    private final String[] keys;

    public LooseItemPredicate(JsonObject jsonObject) {
        this.keys = (String[]) StreamSupport.stream(jsonObject.get("keys").getAsJsonArray().spliterator(), false).map(JsonElement::getAsString).toArray(String[]::new);
    }

    @Override
    public boolean matches(ItemStack itemStack) {
        String target = (String) Optional.of(itemStack.getItem()).map(ForgeRegistries.ITEMS::getKey).map(ResourceLocation::m_135815_).orElse(null);
        for (String key : this.keys) {
            if (key.equals(target)) {
                return true;
            }
        }
        return false;
    }
}
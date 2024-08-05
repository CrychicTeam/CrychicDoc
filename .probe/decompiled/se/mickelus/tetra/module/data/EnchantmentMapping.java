package se.mickelus.tetra.module.data;

import com.google.gson.JsonObject;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.ForgeRegistries;

@ParametersAreNonnullByDefault
public class EnchantmentMapping {

    public Enchantment enchantment;

    public String improvement;

    public boolean extract = true;

    public boolean apply = true;

    public float multiplier = 1.0F;

    public JsonObject toJson() {
        JsonObject result = new JsonObject();
        result.addProperty("enchantment", ForgeRegistries.ENCHANTMENTS.getKey(this.enchantment).getPath());
        result.addProperty("improvement", this.improvement);
        if (!this.extract) {
            result.addProperty("extract", this.extract);
        }
        if (!this.apply) {
            result.addProperty("apply", this.apply);
        }
        if (this.multiplier != 1.0F) {
            result.addProperty("multiplier", this.multiplier);
        }
        return result;
    }
}
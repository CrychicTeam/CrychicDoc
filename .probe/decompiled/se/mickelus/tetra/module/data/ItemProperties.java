package se.mickelus.tetra.module.data;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

@ParametersAreNonnullByDefault
public class ItemProperties {

    public int durability = 0;

    public float durabilityMultiplier = 1.0F;

    public int integrity = 0;

    public int integrityUsage = 0;

    public float integrityMultiplier = 1.0F;

    public Set<TagKey<Item>> tags;

    public Rarity rarity;

    public static ItemProperties merge(ItemProperties a, ItemProperties b) {
        if (a == null) {
            return b;
        } else if (b == null) {
            return a;
        } else {
            ItemProperties result = new ItemProperties();
            result.durability = a.durability + b.durability;
            result.durabilityMultiplier = a.durabilityMultiplier * b.durabilityMultiplier;
            if (a.integrity < 0) {
                result.integrityUsage = result.integrityUsage - a.integrity;
            } else {
                result.integrity = result.integrity + a.integrity;
            }
            if (b.integrity < 0) {
                result.integrityUsage = result.integrityUsage - b.integrity;
            } else {
                result.integrity = result.integrity + b.integrity;
            }
            result.integrityUsage = result.integrityUsage + a.integrityUsage + b.integrityUsage;
            if (a.tags == null) {
                result.tags = b.tags;
            } else if (b.tags == null) {
                result.tags = a.tags;
            } else {
                result.tags = (Set<TagKey<Item>>) Stream.concat(a.tags.stream(), b.tags.stream()).collect(Collectors.toSet());
            }
            if (a.rarity == null) {
                result.rarity = b.rarity;
            } else if (b.rarity == null) {
                result.rarity = a.rarity;
            } else {
                result.rarity = a.rarity.ordinal() > b.rarity.ordinal() ? a.rarity : b.rarity;
            }
            return result;
        }
    }

    public ItemProperties multiply(float factor) {
        ItemProperties result = new ItemProperties();
        result.durability = Math.round((float) this.durability * factor);
        if (this.durabilityMultiplier != 1.0F) {
            result.durabilityMultiplier = 1.0F + (this.durabilityMultiplier - 1.0F) * factor;
        }
        result.integrity = Math.round((float) this.integrity * factor);
        result.integrityUsage = Math.round((float) this.integrityUsage * factor);
        if (this.integrityMultiplier != 1.0F) {
            result.integrityMultiplier = 1.0F + (this.integrityMultiplier - 1.0F) * factor;
        }
        return result;
    }
}
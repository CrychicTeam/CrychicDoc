package dev.xkmc.l2complements.content.feature;

import java.util.function.Supplier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.tags.IReverseTag;
import top.theillusivec4.curios.api.CuriosApi;

public record CurioFeaturePredicate(Supplier<Item> item) implements FeaturePredicate {

    @Override
    public boolean test(LivingEntity e) {
        ((IReverseTag) ForgeRegistries.ITEMS.tags().getReverseTag((Item) this.item.get()).get()).getTagKeys();
        return CuriosApi.getCuriosInventory(e).resolve().flatMap(x -> x.findFirstCurio((Item) this.item.get())).isPresent();
    }
}
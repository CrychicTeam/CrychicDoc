package dev.xkmc.l2complements.compat;

import com.tterrag.registrate.providers.RegistrateItemTagsProvider;
import dev.xkmc.l2complements.init.data.TagGen;
import dev.xkmc.l2complements.init.materials.LCMats;
import net.minecraft.world.item.Items;
import net.minecraftforge.fml.ModList;
import twilightforest.data.tags.ItemTagGenerator;

public class TFCompat {

    public static void onItemTagGen(RegistrateItemTagsProvider pvd) {
        if (ModList.get().isLoaded("twilightforest")) {
            onItemTagGenImpl(pvd);
        }
    }

    public static void onItemTagGenImpl(RegistrateItemTagsProvider pvd) {
        pvd.addTag(ItemTagGenerator.BANNED_UNCRAFTING_INGREDIENTS).add(Items.ENCHANTED_BOOK).addTag(TagGen.SPECIAL_ITEM).add(LCMats.TOTEMIC_GOLD.getIngot());
        pvd.addTag(ItemTagGenerator.BANNED_UNCRAFTABLES).add(Items.ENCHANTED_BOOK);
    }
}
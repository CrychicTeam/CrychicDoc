package dev.latvian.mods.kubejs.create.custom;

import com.simibubi.create.AllItems;
import com.simibubi.create.Create;
import com.simibubi.create.content.equipment.sandPaper.SandPaperItem;
import com.simibubi.create.foundation.item.ItemDescription;
import dev.latvian.mods.kubejs.item.ItemBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class SandpaperItemBuilder extends ItemBuilder {

    public SandpaperItemBuilder(ResourceLocation i) {
        super(i);
        this.tag(Create.asResource("sandpaper"));
    }

    public Item createObject() {
        SandPaperItem item = new SandPaperItem(this.createItemProperties());
        ItemDescription.referKey(item, AllItems.SAND_PAPER);
        return item;
    }
}
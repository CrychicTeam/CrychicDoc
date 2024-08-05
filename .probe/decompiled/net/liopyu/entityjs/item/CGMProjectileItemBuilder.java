package net.liopyu.entityjs.item;

import com.mrcrayfish.guns.item.AmmoItem;
import dev.latvian.mods.kubejs.item.ItemBuilder;
import java.util.ArrayList;
import java.util.List;
import net.liopyu.entityjs.builders.nonliving.modded.CGMProjectileEntityJSBuilder;
import net.minecraft.resources.ResourceLocation;

public class CGMProjectileItemBuilder extends ItemBuilder {

    public static final List<CGMProjectileItemBuilder> thisList = new ArrayList();

    public final transient CGMProjectileEntityJSBuilder parent;

    public CGMProjectileItemBuilder(ResourceLocation i, CGMProjectileEntityJSBuilder parent) {
        super(i);
        thisList.add(this);
        this.parent = parent;
        this.texture = i.getNamespace() + ":item/" + i.getPath();
    }

    public AmmoItem createObject() {
        return new AmmoItem(this.createItemProperties());
    }
}
package net.minecraft.client.renderer;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ItemModelShaper {

    public final Int2ObjectMap<ModelResourceLocation> shapes = new Int2ObjectOpenHashMap(256);

    private final Int2ObjectMap<BakedModel> shapesCache = new Int2ObjectOpenHashMap(256);

    private final ModelManager modelManager;

    public ItemModelShaper(ModelManager modelManager0) {
        this.modelManager = modelManager0;
    }

    public BakedModel getItemModel(ItemStack itemStack0) {
        BakedModel $$1 = this.getItemModel(itemStack0.getItem());
        return $$1 == null ? this.modelManager.getMissingModel() : $$1;
    }

    @Nullable
    public BakedModel getItemModel(Item item0) {
        return (BakedModel) this.shapesCache.get(getIndex(item0));
    }

    private static int getIndex(Item item0) {
        return Item.getId(item0);
    }

    public void register(Item item0, ModelResourceLocation modelResourceLocation1) {
        this.shapes.put(getIndex(item0), modelResourceLocation1);
    }

    public ModelManager getModelManager() {
        return this.modelManager;
    }

    public void rebuildCache() {
        this.shapesCache.clear();
        ObjectIterator var1 = this.shapes.entrySet().iterator();
        while (var1.hasNext()) {
            Entry<Integer, ModelResourceLocation> $$0 = (Entry<Integer, ModelResourceLocation>) var1.next();
            this.shapesCache.put((Integer) $$0.getKey(), this.modelManager.getModel((ModelResourceLocation) $$0.getValue()));
        }
    }
}
package net.minecraftforge.client.model.generators;

import java.util.Objects;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

public abstract class ItemModelProvider extends ModelProvider<ItemModelBuilder> {

    public ItemModelProvider(PackOutput output, String modid, ExistingFileHelper existingFileHelper) {
        super(output, modid, "item", ItemModelBuilder::new, existingFileHelper);
    }

    public ItemModelBuilder basicItem(Item item) {
        return this.basicItem((ResourceLocation) Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(item)));
    }

    public ItemModelBuilder basicItem(ResourceLocation item) {
        return this.getBuilder(item.toString()).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", new ResourceLocation(item.getNamespace(), "item/" + item.getPath()));
    }

    @NotNull
    @Override
    public String getName() {
        return "Item Models: " + this.modid;
    }
}
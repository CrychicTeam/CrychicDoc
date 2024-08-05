package io.github.lightman314.lightmanscurrency.datagen.client.resourcepacks;

import io.github.lightman314.lightmanscurrency.datagen.client.builders.ItemPositionBuilder;
import io.github.lightman314.lightmanscurrency.datagen.client.generators.ItemPositionProvider;
import javax.annotation.Nonnull;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import org.joml.Vector3f;

public class LCCloserItemPositionProvider extends ItemPositionProvider {

    public LCCloserItemPositionProvider(@Nonnull PackOutput output) {
        super(output, "lightmanscurrency", "CloserItemsPack");
    }

    @Override
    protected void addEntries() {
        this.addData(new ResourceLocation("lightmanscurrency", "card_display"), ItemPositionBuilder.builder().withGlobalScale(0.4F).withGlobalRotationType("FACING_UP").withGlobalExtraCount(2).withGlobalExtraOffset(new Vector3f(1.0E-4F, 0.1F, 1.0E-4F)).withEntry(new Vector3f(0.3125F, 0.5625F, 0.28125F)).withEntry(new Vector3f(0.6875F, 0.5625F, 0.28125F)).withEntry(new Vector3f(0.3125F, 0.75F, 0.75F)).withEntry(new Vector3f(0.6875F, 0.75F, 0.75F)));
        this.addData(new ResourceLocation("lightmanscurrency", "freezer"), ItemPositionBuilder.builder().withGlobalScale(0.4F).withGlobalRotationType("FACING").withGlobalExtraCount(5).withGlobalExtraOffset(new Vector3f(1.0E-4F, 1.0E-4F, 0.1F)).withEntry(new Vector3f(0.3125F, 1.75F, 0.375F)).withEntry(new Vector3f(0.6875F, 1.75F, 0.375F)).withEntry(new Vector3f(0.3125F, 1.3125F, 0.375F)).withEntry(new Vector3f(0.6875F, 1.3125F, 0.375F)).withEntry(new Vector3f(0.3125F, 0.875F, 0.375F)).withEntry(new Vector3f(0.6875F, 0.875F, 0.375F)).withEntry(new Vector3f(0.3125F, 0.4375F, 0.375F)).withEntry(new Vector3f(0.6875F, 0.4375F, 0.375F)));
        this.addData(new ResourceLocation("lightmanscurrency", "shelf"), ItemPositionBuilder.builder().withGlobalScale(0.875F).withGlobalRotationType("FACING").withGlobalExtraCount(1).withGlobalExtraOffset(new Vector3f(1.0E-4F, 1.0E-4F, -0.1F)).withEntry(new Vector3f(0.5F, 0.5625F, 0.90625F)));
        this.addData(new ResourceLocation("lightmanscurrency", "shelf_2x2"), ItemPositionBuilder.builder().withGlobalScale(0.34375F).withGlobalRotationType("FACING").withGlobalExtraCount(1).withGlobalExtraOffset(new Vector3f(1.0E-4F, 1.0E-4F, -0.1F)).withEntry(new Vector3f(0.25F, 0.8125F, 0.90625F)).withEntry(new Vector3f(0.75F, 0.8125F, 0.90625F)).withEntry(new Vector3f(0.25F, 0.3125F, 0.90625F)).withEntry(new Vector3f(0.75F, 0.3125F, 0.90625F)));
        this.addData(new ResourceLocation("lightmanscurrency", "vending_machine"), ItemPositionBuilder.builder().withGlobalScale(0.3F).withGlobalRotationType("FACING").withGlobalExtraCount(5).withGlobalExtraOffset(new Vector3f(1.0E-4F, 1.0E-4F, 0.1F)).withEntry(new Vector3f(0.21875F, 1.6875F, 0.375F)).withEntry(new Vector3f(0.59375F, 1.6875F, 0.375F)).withEntry(new Vector3f(0.21875F, 1.25F, 0.375F)).withEntry(new Vector3f(0.59375F, 1.25F, 0.375F)).withEntry(new Vector3f(0.21875F, 0.8125F, 0.375F)).withEntry(new Vector3f(0.59375F, 0.8125F, 0.375F)));
        this.addData(new ResourceLocation("lightmanscurrency", "large_vending_machine"), ItemPositionBuilder.builder().withGlobalScale(0.3F).withGlobalRotationType("FACING").withGlobalExtraCount(5).withGlobalExtraOffset(new Vector3f(1.0E-4F, 1.0E-4F, 0.1F)).withEntry(new Vector3f(0.21875F, 1.6875F, 0.375F)).withEntry(new Vector3f(0.65625F, 1.6875F, 0.375F)).withEntry(new Vector3f(1.09375F, 1.6875F, 0.375F)).withEntry(new Vector3f(1.53125F, 1.6875F, 0.375F)).withEntry(new Vector3f(0.21875F, 1.25F, 0.375F)).withEntry(new Vector3f(0.65625F, 1.25F, 0.375F)).withEntry(new Vector3f(1.09375F, 1.25F, 0.375F)).withEntry(new Vector3f(1.53125F, 1.25F, 0.375F)).withEntry(new Vector3f(0.21875F, 0.8125F, 0.375F)).withEntry(new Vector3f(0.65625F, 0.8125F, 0.375F)).withEntry(new Vector3f(1.09375F, 0.8125F, 0.375F)).withEntry(new Vector3f(1.53125F, 0.8125F, 0.375F)));
    }
}
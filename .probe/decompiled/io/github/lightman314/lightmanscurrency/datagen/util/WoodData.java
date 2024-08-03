package io.github.lightman314.lightmanscurrency.datagen.util;

import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.RegistryObject;

public class WoodData {

    private final Supplier<? extends ItemLike> logBlock;

    public final ResourceLocation logSideTexture;

    public final ResourceLocation logTopTexture;

    private final Supplier<? extends ItemLike> plankBlock;

    public final ResourceLocation plankTexture;

    private final Supplier<? extends ItemLike> slabBlock;

    public final Item getLog() {
        return this.getFromSupplier(this.logBlock);
    }

    public final Item getPlank() {
        return this.getFromSupplier(this.plankBlock);
    }

    public final Item getSlab() {
        return this.getFromSupplier(this.slabBlock);
    }

    @Nullable
    private Item getFromSupplier(@Nullable Supplier<? extends ItemLike> source) {
        if (source != null) {
            ItemLike il = (ItemLike) source.get();
            if (il != null) {
                return il.asItem();
            }
        }
        return null;
    }

    private WoodData(Supplier<? extends ItemLike> logBlock, Supplier<? extends ItemLike> plankBlock, Supplier<? extends ItemLike> slabBlock, ResourceLocation logSideTexture, ResourceLocation logTopTexture, ResourceLocation plankTexture) {
        this.logBlock = logBlock;
        this.logSideTexture = logSideTexture;
        this.logTopTexture = logTopTexture;
        this.plankBlock = plankBlock;
        this.plankTexture = plankTexture;
        this.slabBlock = slabBlock;
    }

    public static WoodData of1(Supplier<RegistryObject<? extends ItemLike>> logBlock, Supplier<RegistryObject<? extends ItemLike>> plankBlock, Supplier<RegistryObject<? extends ItemLike>> slabBlock, ResourceLocation logSideTexture, ResourceLocation logTopTexture, ResourceLocation plankTexture) {
        return new WoodData(() -> (ItemLike) ((RegistryObject) logBlock.get()).get(), () -> (ItemLike) ((RegistryObject) plankBlock.get()).get(), () -> (ItemLike) ((RegistryObject) slabBlock.get()).get(), logSideTexture, logTopTexture, plankTexture);
    }

    public static WoodData of1(Supplier<RegistryObject<? extends ItemLike>> logBlock, Supplier<RegistryObject<? extends ItemLike>> plankBlock, Supplier<RegistryObject<? extends ItemLike>> slabBlock, String logSideTexture, String logTopTexture, String plankTexture) {
        return of1(logBlock, plankBlock, slabBlock, new ResourceLocation(logSideTexture), new ResourceLocation(logTopTexture), new ResourceLocation(plankTexture));
    }

    public static WoodData of2(Supplier<? extends ItemLike> logBlock, Supplier<? extends ItemLike> plankBlock, Supplier<? extends ItemLike> slabBlock, ResourceLocation logSideTexture, ResourceLocation logTopTexture, ResourceLocation plankTexture) {
        return new WoodData(logBlock, plankBlock, slabBlock, logSideTexture, logTopTexture, plankTexture);
    }

    public static WoodData of2(Supplier<? extends ItemLike> logBlock, Supplier<? extends ItemLike> plankBlock, Supplier<? extends ItemLike> slabBlock, String logSideTexture, String logTopTexture, String plankTexture) {
        return of2(logBlock, plankBlock, slabBlock, new ResourceLocation(logSideTexture), new ResourceLocation(logTopTexture), new ResourceLocation(plankTexture));
    }

    public static WoodData of(ItemLike logBlock, ItemLike plankBlock, ItemLike slabBlock, ResourceLocation logSideTexture, ResourceLocation logTopTexture, ResourceLocation plankTexture) {
        return new WoodData(() -> logBlock, () -> plankBlock, () -> slabBlock, logSideTexture, logTopTexture, plankTexture);
    }

    public static WoodData of(ItemLike logBlock, ItemLike plankBlock, ItemLike slabBlock, String logSideTexture, String logTopTexture, String plankTexture) {
        return new WoodData(() -> logBlock, () -> plankBlock, () -> slabBlock, new ResourceLocation(logSideTexture), new ResourceLocation(logTopTexture), new ResourceLocation(plankTexture));
    }
}
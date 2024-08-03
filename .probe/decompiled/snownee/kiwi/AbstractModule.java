package snownee.kiwi;

import com.google.common.collect.Maps;
import com.mojang.datafixers.types.Type;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.stream.Stream;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.data.event.GatherDataEvent;
import snownee.kiwi.block.entity.InheritanceBlockEntityType;
import snownee.kiwi.block.entity.TagBasedBlockEntityType;
import snownee.kiwi.loader.event.ClientInitEvent;
import snownee.kiwi.loader.event.InitEvent;
import snownee.kiwi.loader.event.PostInitEvent;
import snownee.kiwi.loader.event.ServerInitEvent;

public abstract class AbstractModule {

    protected final Map<Object, BiConsumer<ModuleInfo, ?>> decorators = Maps.newHashMap();

    public ResourceLocation uid;

    protected static <T> KiwiGO<T> go(Supplier<? extends T> factory) {
        return new KiwiGO<>((Supplier<T>) factory);
    }

    protected static <T> KiwiGO<T> go(Supplier<? extends T> factory, Supplier<Object> registry) {
        return new KiwiGO.RegistrySpecified<>((Supplier<T>) factory, registry);
    }

    protected static Item.Properties itemProp() {
        return new Item.Properties();
    }

    protected static BlockBehaviour.Properties blockProp() {
        return BlockBehaviour.Properties.of();
    }

    protected static BlockBehaviour.Properties blockProp(BlockBehaviour block) {
        return BlockBehaviour.Properties.copy(block);
    }

    public static <T extends BlockEntity> KiwiGO<BlockEntityType<T>> blockEntity(BlockEntityType.BlockEntitySupplier<? extends T> factory, Type<?> datafixer, Supplier<? extends Block>... blocks) {
        return go(() -> BlockEntityType.Builder.of(factory, (Block[]) Stream.of(blocks).map(Supplier::get).toArray(Block[]::new)).build(datafixer));
    }

    @Deprecated
    public static <T extends BlockEntity> KiwiGO<BlockEntityType<T>> blockEntity(BlockEntityType.BlockEntitySupplier<? extends T> factory, Type<?> datafixer, TagKey<Block> blockTag) {
        return go(() -> new TagBasedBlockEntityType<>(factory, blockTag, datafixer));
    }

    public static <T extends BlockEntity> KiwiGO<BlockEntityType<T>> blockEntity(BlockEntityType.BlockEntitySupplier<? extends T> factory, Type<?> datafixer, Class<? extends Block> blockClass) {
        return go(() -> new InheritanceBlockEntityType<>(factory, blockClass, datafixer));
    }

    public static CreativeModeTab.Builder itemCategory(String namespace, String path, Supplier<ItemStack> icon) {
        return new KiwiTabBuilder(new ResourceLocation(namespace, path)).m_257737_(icon);
    }

    public static TagKey<Item> itemTag(String namespace, String path) {
        return tag(Registries.ITEM, namespace, path);
    }

    public static TagKey<EntityType<?>> entityTag(String namespace, String path) {
        return tag(Registries.ENTITY_TYPE, namespace, path);
    }

    public static TagKey<Block> blockTag(String namespace, String path) {
        return tag(Registries.BLOCK, namespace, path);
    }

    public static TagKey<Fluid> fluidTag(String namespace, String path) {
        return tag(Registries.FLUID, namespace, path);
    }

    public static <T> TagKey<T> tag(ResourceKey<? extends Registry<T>> registryKey, String namespace, String path) {
        return TagKey.create(registryKey, new ResourceLocation(namespace, path));
    }

    protected void preInit() {
    }

    protected void init(InitEvent event) {
    }

    protected void clientInit(ClientInitEvent event) {
    }

    protected void serverInit(ServerInitEvent event) {
    }

    protected void postInit(PostInitEvent event) {
    }

    @Deprecated
    protected void gatherData(GatherDataEvent event) {
    }

    public ResourceLocation RL(String path) {
        return new ResourceLocation(this.uid.getNamespace(), path);
    }
}
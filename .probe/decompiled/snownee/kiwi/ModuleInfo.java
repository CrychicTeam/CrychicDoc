package snownee.kiwi;

import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.common.collect.MultimapBuilder.ListMultimapBuilder;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.data.loading.DatagenModLoader;
import net.minecraftforge.fml.javafmlmod.FMLModContainer;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.commons.lang3.mutable.MutableObject;
import snownee.kiwi.block.IKiwiBlock;
import snownee.kiwi.item.ItemCategoryFiller;
import snownee.kiwi.item.ModBlockItem;
import snownee.kiwi.loader.Platform;
import snownee.kiwi.loader.event.ClientInitEvent;
import snownee.kiwi.loader.event.InitEvent;
import snownee.kiwi.loader.event.PostInitEvent;
import snownee.kiwi.loader.event.ServerInitEvent;

public class ModuleInfo {

    public final AbstractModule module;

    public final ModContext context;

    public GroupSetting groupSetting;

    final ModuleInfo.RegistryHolder registries = new ModuleInfo.RegistryHolder();

    final Map<Block, Item.Properties> blockItemBuilders = Maps.newHashMap();

    final Set<Object> noCategories = Sets.newHashSet();

    final Set<Block> noItems = Sets.newHashSet();

    public ModuleInfo(ResourceLocation rl, AbstractModule module, ModContext context) {
        this.module = module;
        this.context = context;
        module.uid = rl;
        if (DatagenModLoader.isRunningDataGen() && context.modContainer instanceof FMLModContainer) {
            ((FMLModContainer) context.modContainer).getEventBus().addListener(module::gatherData);
        }
    }

    public void register(Object object, ResourceLocation name, Object registry, @Nullable Field field) {
        NamedEntry entry = new NamedEntry<>(name, object, registry, field);
        this.registries.put(entry);
        if (field != null) {
            KiwiModule.Category group = (KiwiModule.Category) field.getAnnotation(KiwiModule.Category.class);
            if (group != null) {
                entry.groupSetting = GroupSetting.of(group, this.groupSetting);
            }
        }
    }

    public void handleRegister(Object registry) {
        this.context.setActiveContainer();
        Collection<NamedEntry<?>> entries = this.registries.get(registry);
        BiConsumer<ModuleInfo, Object> decorator = (BiConsumer<ModuleInfo, Object>) this.module.decorators.getOrDefault(registry, (BiConsumer) (a, b) -> {
        });
        if (registry == ForgeRegistries.ITEMS) {
            this.handleRegister(BuiltInRegistries.CREATIVE_MODE_TAB);
            this.registries.get(ForgeRegistries.BLOCKS).forEach(e -> {
                if (!this.noItems.contains(e.entry)) {
                    Item.Properties builder = (Item.Properties) this.blockItemBuilders.get(e.entry);
                    if (builder == null) {
                        builder = new Item.Properties();
                    }
                    BlockItem item;
                    if (e.entry instanceof IKiwiBlock) {
                        item = ((IKiwiBlock) e.entry).createItem(builder);
                    } else {
                        item = new ModBlockItem((Block) e.entry, builder);
                    }
                    if (this.noCategories.contains(e.entry)) {
                        this.noCategories.add(item);
                    }
                    NamedEntry itemEntry = new NamedEntry<>(e.name, item, registry, null);
                    itemEntry.groupSetting = e.groupSetting;
                    entries.add(itemEntry);
                }
            });
            Set<GroupSetting> groupSettings = Sets.newLinkedHashSet();
            MutableObject<GroupSetting> prevSetting = new MutableObject();
            if (this.groupSetting != null) {
                prevSetting.setValue(this.groupSetting);
                groupSettings.add(this.groupSetting);
            }
            entries.forEach(e -> {
                Item item = (Item) e.entry;
                if (this.noCategories.contains(item)) {
                    prevSetting.setValue(this.groupSetting);
                } else {
                    if (!(item instanceof ItemCategoryFiller filler)) {
                        filler = (tab, flags, hasPermissions, items) -> items.add(new ItemStack(item));
                    }
                    if (e.groupSetting != null) {
                        e.groupSetting.apply(filler);
                        groupSettings.add(e.groupSetting);
                        prevSetting.setValue(e.groupSetting);
                    } else if (prevSetting.getValue() != null) {
                        ((GroupSetting) prevSetting.getValue()).apply(filler);
                    }
                }
            });
            groupSettings.forEach(GroupSetting::postApply);
        }
        entries.forEach(e -> {
            decorator.accept(this, e.entry);
            if (registry instanceof Registry) {
                Registry.register((Registry) registry, e.name, e.entry);
            } else {
                if (!(registry instanceof IForgeRegistry)) {
                    throw new RuntimeException("registry is invalid");
                }
                ((IForgeRegistry) registry).register(e.name, e.entry);
            }
        });
        if (registry == ForgeRegistries.BLOCKS && Platform.isPhysicalClient() && !Platform.isDataGen()) {
            RenderType solid = RenderType.solid();
            Map<Class<?>, RenderType> cache = Maps.newHashMap();
            entries.stream().forEach(e -> {
                Block block = (Block) e.entry;
                if (e.field != null) {
                    KiwiModule.RenderLayer layer = (KiwiModule.RenderLayer) e.field.getAnnotation(KiwiModule.RenderLayer.class);
                    if (layer != null) {
                        RenderType type = (RenderType) layer.value().value;
                        if (type != solid && type != null) {
                            ItemBlockRenderTypes.setRenderLayer(block, type);
                            return;
                        }
                    }
                }
                Class<?> klass = block.getClass();
                RenderType type = (RenderType) cache.computeIfAbsent(klass, k -> {
                    while (k != Block.class) {
                        KiwiModule.RenderLayer layer = (KiwiModule.RenderLayer) k.getDeclaredAnnotation(KiwiModule.RenderLayer.class);
                        if (layer != null) {
                            return (RenderType) layer.value().value;
                        }
                        k = k.getSuperclass();
                    }
                    return solid;
                });
                if (type != solid && type != null) {
                    ItemBlockRenderTypes.setRenderLayer(block, type);
                }
            });
        }
    }

    public void preInit() {
        this.context.setActiveContainer();
        this.module.preInit();
        KiwiModules.ALL_USED_REGISTRIES.addAll(this.registries.registries.keySet());
    }

    public void init(InitEvent event) {
        this.context.setActiveContainer();
        this.module.init(event);
    }

    public void clientInit(ClientInitEvent event) {
        this.context.setActiveContainer();
        this.module.clientInit(event);
    }

    public void serverInit(ServerInitEvent event) {
        this.context.setActiveContainer();
        this.module.serverInit(event);
    }

    public void postInit(PostInitEvent event) {
        this.context.setActiveContainer();
        this.module.postInit(event);
    }

    public <T> List<T> getRegistries(Object registry) {
        return (List<T>) this.registries.get(registry).stream().map($ -> $.entry).collect(Collectors.toList());
    }

    public <T> Stream<NamedEntry<T>> getRegistryEntries(Object registry) {
        return this.registries.get(registry).stream().map($ -> $);
    }

    public static final class RegistryHolder {

        final Multimap<Object, NamedEntry<?>> registries = ListMultimapBuilder.linkedHashKeys().linkedListValues().build();

        void put(NamedEntry<?> entry) {
            this.registries.put(entry.registry, entry);
        }

        Collection<NamedEntry<?>> get(Object registry) {
            return this.registries.get(registry);
        }
    }
}
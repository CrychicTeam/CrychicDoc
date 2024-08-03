package dev.xkmc.l2artifacts.init.registrate.entries;

import com.tterrag.registrate.builders.AbstractBuilder;
import com.tterrag.registrate.builders.BuilderCallback;
import com.tterrag.registrate.providers.RegistrateLangProvider;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import com.tterrag.registrate.util.nullness.NonnullType;
import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.content.core.ArtifactSet;
import dev.xkmc.l2artifacts.content.core.ArtifactSlot;
import dev.xkmc.l2artifacts.content.core.BaseArtifact;
import dev.xkmc.l2artifacts.init.L2Artifacts;
import dev.xkmc.l2artifacts.init.registrate.ArtifactTypeRegistry;
import dev.xkmc.l2artifacts.init.registrate.items.ArtifactItems;
import dev.xkmc.l2library.base.NamedEntry;
import dev.xkmc.l2serial.util.Wrappers;
import java.util.Objects;
import java.util.function.Consumer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.tags.ITagManager;
import org.jetbrains.annotations.NotNull;

public class SetBuilder<T extends ArtifactSet, I extends BaseArtifact, P> extends AbstractBuilder<ArtifactSet, T, P, SetBuilder<T, I, P>> {

    private final NonNullSupplier<T> sup;

    private final int min_rank;

    private final int max_rank;

    private RegistryEntry<ArtifactSlot>[] slots;

    private ItemEntry<BaseArtifact>[][] items;

    private Consumer<ArtifactSetConfig.SetBuilder> builder;

    public SetBuilder(ArtifactRegistrate owner, P parent, String name, BuilderCallback callback, NonNullSupplier<T> sup, int min_rank, int max_rank) {
        super(owner, parent, name, callback, ArtifactTypeRegistry.SET.key());
        this.sup = sup;
        this.min_rank = min_rank;
        this.max_rank = max_rank;
    }

    @SafeVarargs
    public final SetBuilder<T, I, P> setSlots(RegistryEntry<ArtifactSlot>... slots) {
        this.slots = slots;
        return this;
    }

    public SetBuilder<T, I, P> buildConfig(Consumer<ArtifactSetConfig.SetBuilder> builder) {
        this.builder = builder;
        return this;
    }

    public SetBuilder<T, I, P> regItems() {
        if (this.slots == null) {
            throw new IllegalStateException("call setSlots() first");
        } else {
            this.items = new ItemEntry[this.slots.length][this.max_rank - this.min_rank + 1];
            ITagManager<Item> manager = (ITagManager<Item>) Objects.requireNonNull(ForgeRegistries.ITEMS.tags());
            TagKey<Item> artifact = manager.createTagKey(new ResourceLocation("l2artifacts", "artifact"));
            for (int i = 0; i < this.slots.length; i++) {
                RegistryEntry<ArtifactSlot> slot = this.slots[i];
                String slot_name = slot.getId().getPath();
                TagKey<Item> curios_tag = manager.createTagKey(new ResourceLocation("curios", "artifact_" + slot_name));
                TagKey<Item> slot_tag = manager.createTagKey(new ResourceLocation("l2artifacts", slot_name));
                for (int r = this.min_rank; r <= this.max_rank; r++) {
                    TagKey<Item> rank_tag = manager.createTagKey(new ResourceLocation("l2artifacts", "rank_" + r));
                    String name = this.getName() + "_" + slot_name + "_" + r;
                    int rank = r;
                    this.items[i][r - this.min_rank] = L2Artifacts.REGISTRATE.item(name, p -> new BaseArtifact(p, this.asSupplier()::get, slot, rank)).model((ctx, pvd) -> ((ItemModelBuilder) pvd.getBuilder(name)).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", new ResourceLocation("l2artifacts", "item/rank/" + rank)).texture("layer1", new ResourceLocation("l2artifacts", "item/" + this.getName() + "/" + slot_name))).tag(new TagKey[] { curios_tag, slot_tag, rank_tag, artifact }).lang(RegistrateLangProvider.toEnglishName(this.getName() + "_" + slot_name) + ArtifactItems.RANK_NAME[r - 1]).register();
                }
            }
            return this;
        }
    }

    protected RegistryEntry<T> createEntryWrapper(RegistryObject<T> delegate) {
        if (this.slots == null) {
            throw new IllegalStateException("call setSlots() first");
        } else if (this.builder == null) {
            throw new IllegalStateException("call buildConfig() first");
        } else if (this.items == null) {
            throw new IllegalStateException("call regItems() first");
        } else {
            return new SetEntry<>((ArtifactRegistrate) Wrappers.cast(this.getOwner()), delegate, this.items, this.builder);
        }
    }

    @NonnullType
    @NotNull
    protected T createEntry() {
        return (T) this.sup.get();
    }

    public SetBuilder<T, I, P> lang(String name) {
        return (SetBuilder<T, I, P>) this.lang(NamedEntry::getDescriptionId, name);
    }

    public SetEntry<T> register() {
        return (SetEntry<T>) Wrappers.cast(super.register());
    }
}
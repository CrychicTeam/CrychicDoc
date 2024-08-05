package dev.xkmc.l2hostility.init.entries;

import com.tterrag.registrate.builders.AbstractBuilder;
import com.tterrag.registrate.builders.BuilderCallback;
import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import com.tterrag.registrate.util.nullness.NonnullType;
import dev.xkmc.l2hostility.content.config.TraitConfig;
import dev.xkmc.l2hostility.content.item.traits.TraitSymbol;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2hostility.init.data.LHTagGen;
import dev.xkmc.l2hostility.init.registrate.LHTraits;
import dev.xkmc.l2library.base.NamedEntry;
import dev.xkmc.l2serial.util.Wrappers;
import java.util.function.Function;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

public class TraitBuilder<T extends MobTrait, P> extends AbstractBuilder<MobTrait, T, P, TraitBuilder<T, P>> {

    private final NonNullSupplier<T> sup;

    public TraitBuilder(LHRegistrate owner, P parent, String name, BuilderCallback callback, NonNullSupplier<T> sup, Function<ResourceLocation, TraitConfig> config) {
        super(owner, parent, name, callback, LHTraits.TRAITS.key());
        this.sup = sup;
        ResourceLocation rl = new ResourceLocation(this.getOwner().getModid(), this.getName());
        TraitConfig entry = (TraitConfig) config.apply(rl);
        owner.addTraitConfig(e -> e.add(L2Hostility.TRAIT, rl, entry));
    }

    protected RegistryEntry<T> createEntryWrapper(RegistryObject<T> delegate) {
        return new TraitEntry<>((LHRegistrate) Wrappers.cast(this.getOwner()), delegate);
    }

    public TraitBuilder<T, P> lang(String name) {
        return (TraitBuilder<T, P>) this.lang(NamedEntry::getDescriptionId, name);
    }

    public <I extends TraitSymbol> ItemBuilder<I, TraitBuilder<T, P>> item(NonNullFunction<Item.Properties, I> sup) {
        return ((ItemBuilder) this.getOwner().item(this, this.getName(), sup).model((ctx, pvd) -> pvd.generated(ctx, new ResourceLocation[] { pvd.modLoc("item/bg"), pvd.modLoc("item/trait/" + ctx.getName()) })).setData(ProviderType.LANG, NonNullBiConsumer.noop())).tag(new TagKey[] { LHTagGen.TRAIT_ITEM });
    }

    @NonnullType
    @NotNull
    protected T createEntry() {
        return (T) this.sup.get();
    }

    public TraitBuilder<T, P> desc(String s) {
        this.getOwner().addRawLang("trait." + this.getOwner().getModid() + "." + this.getName() + ".desc", s);
        return this;
    }
}
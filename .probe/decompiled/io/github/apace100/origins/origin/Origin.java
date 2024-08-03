package io.github.apace100.origins.origin;

import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.origins.component.OriginComponent;
import io.github.edwinmindcraft.origins.api.OriginsAPI;
import io.github.edwinmindcraft.origins.api.capabilities.IOriginContainer;
import io.github.edwinmindcraft.origins.common.registry.OriginRegisters;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;
import net.minecraft.advancements.Advancement;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.Lazy;

@Deprecated
public class Origin {

    public static final Origin EMPTY = new Origin(OriginRegisters.EMPTY);

    private final Lazy<io.github.edwinmindcraft.origins.api.origin.Origin> wrapped;

    @Deprecated
    public static void init() {
    }

    @Deprecated
    public static Map<OriginLayer, Origin> get(Entity entity) {
        return (Map<OriginLayer, Origin>) (entity instanceof Player ? get((Player) entity) : new HashMap());
    }

    @Deprecated
    public static Map<OriginLayer, Origin> get(Player player) {
        return (Map<OriginLayer, Origin>) IOriginContainer.get(player).map(IOriginContainer::asLegacyComponent).map(OriginComponent::getOrigins).orElseGet(HashMap::new);
    }

    public Origin(Supplier<io.github.edwinmindcraft.origins.api.origin.Origin> wrapped) {
        this.wrapped = Lazy.of(wrapped);
    }

    public io.github.edwinmindcraft.origins.api.origin.Origin getWrapped() {
        return (io.github.edwinmindcraft.origins.api.origin.Origin) this.wrapped.get();
    }

    @Deprecated
    public boolean hasUpgrade() {
        return !this.getWrapped().getUpgrades().isEmpty();
    }

    @Deprecated
    public Optional<OriginUpgrade> getUpgrade(Advancement advancement) {
        return this.getWrapped().findUpgrade(advancement.getId()).map(OriginUpgrade::new);
    }

    @Deprecated
    public ResourceLocation getIdentifier() {
        return OriginsAPI.getOriginsRegistry().getKey(this.getWrapped());
    }

    @Deprecated
    public void removePowerType(PowerType<?> powerType) {
        throw new UnsupportedOperationException("Origins are immutable in forge.");
    }

    @Deprecated
    public boolean isSpecial() {
        return this.getWrapped().isSpecial();
    }

    @Deprecated
    public boolean isChoosable() {
        return this.getWrapped().isChoosable();
    }

    @Deprecated
    public Impact getImpact() {
        return this.getWrapped().getImpact();
    }

    @Deprecated
    public ItemStack getDisplayItem() {
        return this.getWrapped().getIcon();
    }

    @Deprecated
    public String getOrCreateNameTranslationKey() {
        if (this.getWrapped().getName() instanceof MutableComponent mc && mc.getContents() instanceof TranslatableContents tc) {
            return tc.getKey();
        }
        return "";
    }

    @Deprecated
    public Component getName() {
        return this.getWrapped().getName();
    }

    @Deprecated
    public String getOrCreateDescriptionTranslationKey() {
        if (this.getWrapped().getDescription() instanceof MutableComponent mc && mc.getContents() instanceof TranslatableContents tc) {
            return tc.getKey();
        }
        return "";
    }

    @Deprecated
    public Component getDescription() {
        return this.getWrapped().getDescription();
    }

    @Deprecated
    public int getOrder() {
        return this.getWrapped().getOrder();
    }

    public String toString() {
        return this.getWrapped().toString();
    }

    public int hashCode() {
        return this.getWrapped().hashCode();
    }

    public boolean equals(Object obj) {
        return obj instanceof Origin other ? Objects.equals(this.getWrapped(), other.getWrapped()) : false;
    }
}
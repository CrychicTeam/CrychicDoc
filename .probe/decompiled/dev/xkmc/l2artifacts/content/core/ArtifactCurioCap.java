package dev.xkmc.l2artifacts.content.core;

import com.google.common.collect.Multimap;
import dev.xkmc.l2artifacts.init.L2Artifacts;
import dev.xkmc.l2damagetracker.contents.curios.AttrTooltip;
import dev.xkmc.l2library.capability.conditionals.ConditionalData;
import dev.xkmc.l2library.capability.conditionals.TokenKey;
import dev.xkmc.l2serial.serialization.codec.TagCodec;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;

public class ArtifactCurioCap implements ICurio {

    private final ItemStack stack;

    private final LazyOptional<ArtifactStats> stats;

    public ArtifactCurioCap(ItemStack stack) {
        this.stack = stack;
        if (stack.getTag() != null && stack.getTag().contains("ArtifactData")) {
            this.stats = LazyOptional.of(() -> (ArtifactStats) Objects.requireNonNull((ArtifactStats) TagCodec.fromTag(stack.getTag().getCompound("ArtifactData"), ArtifactStats.class)));
        } else {
            this.stats = LazyOptional.empty();
        }
    }

    @Override
    public ItemStack getStack() {
        return this.stack;
    }

    public Optional<ArtifactStats> getStats() {
        return this.stats.resolve();
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid) {
        return this.getStats().isPresent() ? ((ArtifactStats) this.getStats().get()).buildAttributes(slotContext.identifier() + slotContext.index()) : ICurio.super.getAttributeModifiers(slotContext, uuid);
    }

    @Override
    public List<Component> getAttributesTooltip(List<Component> tooltips) {
        if (this.getStats().isPresent()) {
            List<Component> ans = AttrTooltip.modifyTooltip(tooltips, ((ArtifactStats) this.getStats().get()).buildAttributes(""), true);
            return (List<Component>) (ans.size() <= 2 ? new ArrayList() : ans);
        } else {
            return tooltips;
        }
    }

    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack) {
        if (this.stack.getItem() instanceof BaseArtifact base) {
            ((ArtifactSet) base.set.get()).update(slotContext);
        }
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack) {
        if (this.stack.getItem() instanceof BaseArtifact base) {
            ((ArtifactSet) base.set.get()).update(slotContext);
        }
    }

    @Override
    public void curioTick(SlotContext slotContext) {
        try {
            if (this.stack.getItem() instanceof BaseArtifact base) {
                ((ArtifactSet) base.set.get()).tick(slotContext);
            }
        } catch (Exception var5) {
            if (slotContext.entity() instanceof Player player) {
                ConditionalData.HOLDER.get(player).data.entrySet().removeIf(x -> ((TokenKey) x.getKey()).type().equals("l2artifacts"));
                L2Artifacts.LOGGER.error("Player " + player + " has invalid artifact data for " + this.stack.getItem() + ". This could be a bug.");
            }
        }
    }
}
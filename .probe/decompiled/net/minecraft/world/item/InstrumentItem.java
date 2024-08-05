package net.minecraft.world.item;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;

public class InstrumentItem extends Item {

    private static final String TAG_INSTRUMENT = "instrument";

    private final TagKey<Instrument> instruments;

    public InstrumentItem(Item.Properties itemProperties0, TagKey<Instrument> tagKeyInstrument1) {
        super(itemProperties0);
        this.instruments = tagKeyInstrument1;
    }

    @Override
    public void appendHoverText(ItemStack itemStack0, @Nullable Level level1, List<Component> listComponent2, TooltipFlag tooltipFlag3) {
        super.appendHoverText(itemStack0, level1, listComponent2, tooltipFlag3);
        Optional<ResourceKey<Instrument>> $$4 = this.getInstrument(itemStack0).flatMap(Holder::m_203543_);
        if ($$4.isPresent()) {
            MutableComponent $$5 = Component.translatable(Util.makeDescriptionId("instrument", ((ResourceKey) $$4.get()).location()));
            listComponent2.add($$5.withStyle(ChatFormatting.GRAY));
        }
    }

    public static ItemStack create(Item item0, Holder<Instrument> holderInstrument1) {
        ItemStack $$2 = new ItemStack(item0);
        setSoundVariantId($$2, holderInstrument1);
        return $$2;
    }

    public static void setRandom(ItemStack itemStack0, TagKey<Instrument> tagKeyInstrument1, RandomSource randomSource2) {
        Optional<Holder<Instrument>> $$3 = BuiltInRegistries.INSTRUMENT.getTag(tagKeyInstrument1).flatMap(p_220103_ -> p_220103_.m_213653_(randomSource2));
        $$3.ifPresent(p_248417_ -> setSoundVariantId(itemStack0, p_248417_));
    }

    private static void setSoundVariantId(ItemStack itemStack0, Holder<Instrument> holderInstrument1) {
        CompoundTag $$2 = itemStack0.getOrCreateTag();
        $$2.putString("instrument", ((ResourceKey) holderInstrument1.unwrapKey().orElseThrow(() -> new IllegalStateException("Invalid instrument"))).location().toString());
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level0, Player player1, InteractionHand interactionHand2) {
        ItemStack $$3 = player1.m_21120_(interactionHand2);
        Optional<? extends Holder<Instrument>> $$4 = this.getInstrument($$3);
        if ($$4.isPresent()) {
            Instrument $$5 = (Instrument) ((Holder) $$4.get()).value();
            player1.m_6672_(interactionHand2);
            play(level0, player1, $$5);
            player1.getCooldowns().addCooldown(this, $$5.useDuration());
            player1.awardStat(Stats.ITEM_USED.get(this));
            return InteractionResultHolder.consume($$3);
        } else {
            return InteractionResultHolder.fail($$3);
        }
    }

    @Override
    public int getUseDuration(ItemStack itemStack0) {
        Optional<? extends Holder<Instrument>> $$1 = this.getInstrument(itemStack0);
        return (Integer) $$1.map(p_248418_ -> ((Instrument) p_248418_.value()).useDuration()).orElse(0);
    }

    private Optional<? extends Holder<Instrument>> getInstrument(ItemStack itemStack0) {
        CompoundTag $$1 = itemStack0.getTag();
        if ($$1 != null && $$1.contains("instrument", 8)) {
            ResourceLocation $$2 = ResourceLocation.tryParse($$1.getString("instrument"));
            if ($$2 != null) {
                return BuiltInRegistries.INSTRUMENT.getHolder(ResourceKey.create(Registries.INSTRUMENT, $$2));
            }
        }
        Iterator<Holder<Instrument>> $$3 = BuiltInRegistries.INSTRUMENT.getTagOrEmpty(this.instruments).iterator();
        return $$3.hasNext() ? Optional.of((Holder) $$3.next()) : Optional.empty();
    }

    @Override
    public UseAnim getUseAnimation(ItemStack itemStack0) {
        return UseAnim.TOOT_HORN;
    }

    private static void play(Level level0, Player player1, Instrument instrument2) {
        SoundEvent $$3 = instrument2.soundEvent().value();
        float $$4 = instrument2.range() / 16.0F;
        level0.playSound(player1, player1, $$3, SoundSource.RECORDS, $$4, 1.0F);
        level0.m_214171_(GameEvent.INSTRUMENT_PLAY, player1.m_20182_(), GameEvent.Context.of(player1));
    }
}
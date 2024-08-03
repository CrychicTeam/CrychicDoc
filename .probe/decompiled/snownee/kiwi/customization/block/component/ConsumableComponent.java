package snownee.kiwi.customization.block.component;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import snownee.kiwi.customization.block.KBlockUtils;
import snownee.kiwi.customization.block.behavior.BlockBehaviorRegistry;
import snownee.kiwi.customization.block.loader.KBlockComponents;
import snownee.kiwi.util.codec.CustomizationCodecs;

public record ConsumableComponent(IntegerProperty property, Optional<FoodProperties> food, Optional<ResourceKey<ResourceLocation>> stat) implements KBlockComponent, LayeredComponent {

    public static final Codec<ConsumableComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(ExtraCodecs.intRange(0, 1).fieldOf("min").forGetter(ConsumableComponent::minValue), ExtraCodecs.POSITIVE_INT.fieldOf("max").forGetter(ConsumableComponent::maxValue), CustomizationCodecs.FOOD.optionalFieldOf("food").forGetter(ConsumableComponent::food), ResourceKey.codec(Registries.CUSTOM_STAT).optionalFieldOf("stat").forGetter(ConsumableComponent::stat)).apply(instance, ConsumableComponent::create));

    public static ConsumableComponent create(int min, int max, Optional<FoodProperties> food, Optional<ResourceKey<ResourceLocation>> stat) {
        return new ConsumableComponent(KBlockUtils.internProperty(IntegerProperty.create("uses", min, max)), food, stat);
    }

    @Override
    public KBlockComponent.Type<?> type() {
        return KBlockComponents.CONSUMABLE.getOrCreate();
    }

    @Override
    public void injectProperties(Block block, StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(this.property);
    }

    @Override
    public BlockState registerDefaultState(BlockState state) {
        return (BlockState) state.m_61124_(this.property, this.getDefaultLayer());
    }

    public int minValue() {
        return this.property.min;
    }

    public int maxValue() {
        return this.property.max;
    }

    @Override
    public boolean hasAnalogOutputSignal() {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState state) {
        return Math.min((Integer) state.m_61143_(this.property) - this.minValue() + 1, 15);
    }

    @Override
    public void addBehaviors(BlockBehaviorRegistry registry) {
        registry.addUseHandler((pState, pPlayer, pLevel, pHand, pHit) -> {
            int value = (Integer) pState.m_61143_(this.property);
            if (value == 0) {
                return InteractionResult.PASS;
            } else {
                this.stat.map(ResourceKey::m_135782_).ifPresent(pPlayer::m_36220_);
                BlockPos pos = pHit.getBlockPos();
                if (this.food.isPresent()) {
                    FoodProperties food = (FoodProperties) this.food.get();
                    if (!pPlayer.canEat(food.canAlwaysEat())) {
                        return InteractionResult.FAIL;
                    }
                    Item item = pState.m_60734_().asItem();
                    pLevel.playSound(pPlayer, pPlayer.m_20185_(), pPlayer.m_20186_(), pPlayer.m_20189_(), item.getEatingSound(), SoundSource.NEUTRAL, 1.0F, 1.0F + (pLevel.random.nextFloat() - pLevel.random.nextFloat()) * 0.4F);
                    if (!pLevel.isClientSide) {
                        pPlayer.getFoodData().eat(food.getNutrition(), food.getSaturationModifier());
                        for (Pair<MobEffectInstance, Float> pair : food.getEffects()) {
                            if (pair.getFirst() != null && pLevel.random.nextFloat() < (Float) pair.getSecond()) {
                                pPlayer.m_7292_(new MobEffectInstance((MobEffectInstance) pair.getFirst()));
                            }
                        }
                    }
                    pLevel.m_142346_(pPlayer, GameEvent.EAT, pos);
                }
                if (value == this.minValue()) {
                    pLevel.removeBlock(pos, false);
                } else {
                    pLevel.setBlockAndUpdate(pos, (BlockState) pState.m_61124_(this.property, value - 1));
                }
                return InteractionResult.sidedSuccess(pLevel.isClientSide);
            }
        });
    }

    @Override
    public IntegerProperty getLayerProperty() {
        return this.property;
    }

    @Override
    public int getDefaultLayer() {
        return this.maxValue();
    }
}
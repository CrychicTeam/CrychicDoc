package se.mickelus.tetra.effect.potion;

import java.util.function.Consumer;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientMobEffectExtensions;
import se.mickelus.mutil.effect.EffectTooltipRenderer;

@ParametersAreNonnullByDefault
public class SteeledPotionEffect extends MobEffect {

    public static final String identifier = "steeled";

    public static SteeledPotionEffect instance;

    public SteeledPotionEffect() {
        super(MobEffectCategory.BENEFICIAL, 8912896);
        this.m_19472_(Attributes.ARMOR, "62eba42f-3fe5-436c-812d-2f5ef72bc55f", 1.0, AttributeModifier.Operation.ADDITION);
        instance = this;
    }

    @OnlyIn(Dist.CLIENT)
    public void initializeClient(Consumer<IClientMobEffectExtensions> consumer) {
        super.initializeClient(consumer);
        consumer.accept(new EffectTooltipRenderer(effect -> I18n.get("effect.tetra.steeled.tooltip", effect.getAmplifier() + 1)));
    }
}
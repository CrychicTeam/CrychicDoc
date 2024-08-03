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
public class UnwaveringPotionEffect extends MobEffect {

    public static final String identifier = "unwavering";

    public static UnwaveringPotionEffect instance;

    public UnwaveringPotionEffect() {
        super(MobEffectCategory.BENEFICIAL, 8943360);
        this.m_19472_(Attributes.KNOCKBACK_RESISTANCE, "6531461a-9c46-4fb9-8c84-002f0b37def1", 1.0, AttributeModifier.Operation.ADDITION);
        instance = this;
    }

    @OnlyIn(Dist.CLIENT)
    public void initializeClient(Consumer<IClientMobEffectExtensions> consumer) {
        super.initializeClient(consumer);
        consumer.accept(new EffectTooltipRenderer(effect -> I18n.get("effect.tetra.unwavering.tooltip")));
    }
}
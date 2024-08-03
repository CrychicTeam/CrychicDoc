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
public class SmallStrengthPotionEffect extends MobEffect {

    public static final String identifier = "small_strength";

    public static SmallStrengthPotionEffect instance;

    public SmallStrengthPotionEffect() {
        super(MobEffectCategory.BENEFICIAL, 8912896);
        this.m_19472_(Attributes.ATTACK_DAMAGE, "fc8d272d-056c-43b4-9d18-f3d7f6cf3983", 1.0, AttributeModifier.Operation.ADDITION);
        instance = this;
    }

    @OnlyIn(Dist.CLIENT)
    public void initializeClient(Consumer<IClientMobEffectExtensions> consumer) {
        super.initializeClient(consumer);
        consumer.accept(new EffectTooltipRenderer(effect -> I18n.get("effect.tetra.small_strength.tooltip", effect.getAmplifier() + 1)));
    }
}
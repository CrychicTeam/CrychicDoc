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
public class SmallHealthPotionEffect extends MobEffect {

    public static final String identifier = "small_health";

    public static SmallHealthPotionEffect instance;

    public SmallHealthPotionEffect() {
        super(MobEffectCategory.BENEFICIAL, 12272708);
        this.m_19472_(Attributes.MAX_HEALTH, "c89b4203-0804-4607-b320-f6b8daf2d272", 1.0, AttributeModifier.Operation.ADDITION);
        instance = this;
    }

    @OnlyIn(Dist.CLIENT)
    public void initializeClient(Consumer<IClientMobEffectExtensions> consumer) {
        super.initializeClient(consumer);
        consumer.accept(new EffectTooltipRenderer(effect -> I18n.get("effect.tetra.small_health.tooltip", effect.getAmplifier() + 1)));
    }
}
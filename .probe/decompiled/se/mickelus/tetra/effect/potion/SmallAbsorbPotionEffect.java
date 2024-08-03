package se.mickelus.tetra.effect.potion;

import java.util.function.Consumer;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientMobEffectExtensions;
import se.mickelus.mutil.effect.EffectTooltipRenderer;

@ParametersAreNonnullByDefault
public class SmallAbsorbPotionEffect extends MobEffect {

    public static final String identifier = "small_absorb";

    public static SmallAbsorbPotionEffect instance;

    public SmallAbsorbPotionEffect() {
        super(MobEffectCategory.BENEFICIAL, 2445989);
        instance = this;
    }

    @Override
    public void removeAttributeModifiers(LivingEntity entity, AttributeMap attributeManager, int amplifier) {
        entity.setAbsorptionAmount(entity.getAbsorptionAmount() - (float) (amplifier + 1));
        super.removeAttributeModifiers(entity, attributeManager, amplifier);
    }

    @Override
    public void addAttributeModifiers(LivingEntity entity, AttributeMap attributeManager, int amplifier) {
        entity.setAbsorptionAmount(entity.getAbsorptionAmount() + (float) amplifier + 1.0F);
        super.addAttributeModifiers(entity, attributeManager, amplifier);
    }

    @OnlyIn(Dist.CLIENT)
    public void initializeClient(Consumer<IClientMobEffectExtensions> consumer) {
        super.initializeClient(consumer);
        consumer.accept(new EffectTooltipRenderer(effect -> I18n.get("effect.tetra.small_absorb.tooltip", effect.getAmplifier() + 1)));
    }
}
package se.mickelus.tetra.effect.potion;

import java.util.function.Consumer;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientMobEffectExtensions;
import org.joml.Vector3f;
import se.mickelus.mutil.effect.EffectTooltipRenderer;

@ParametersAreNonnullByDefault
public class SeveredPotionEffect extends MobEffect {

    public static final String identifier = "severed";

    public static SeveredPotionEffect instance;

    public SeveredPotionEffect() {
        super(MobEffectCategory.HARMFUL, 8912896);
        this.m_19472_(Attributes.MAX_HEALTH, "7e68e993-e133-41c0-aea3-703afc401831", -0.1, AttributeModifier.Operation.MULTIPLY_TOTAL);
        this.m_19472_(Attributes.ATTACK_DAMAGE, "3ca939c9-62fe-41a6-a722-22235066f808", -0.05, AttributeModifier.Operation.MULTIPLY_TOTAL);
        instance = this;
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (!entity.m_20193_().isClientSide) {
            RandomSource rand = entity.getRandom();
            ((ServerLevel) entity.m_9236_()).sendParticles(new DustParticleOptions(new Vector3f(0.5F, 0.0F, 0.0F), 0.5F), entity.m_20185_() + (double) entity.m_20205_() * (0.3 + rand.nextGaussian() * 0.4), entity.m_20186_() + (double) entity.m_20206_() * (0.2 + rand.nextGaussian() * 0.4), entity.m_20189_() + (double) entity.m_20205_() * (0.3 + rand.nextGaussian() * 0.4), 20, 0.0, 0.0, 0.0, 0.0);
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % 10 == 0;
    }

    @OnlyIn(Dist.CLIENT)
    public void initializeClient(Consumer<IClientMobEffectExtensions> consumer) {
        super.initializeClient(consumer);
        consumer.accept(new EffectTooltipRenderer(effect -> {
            int amp = effect.getAmplifier() + 1;
            return I18n.get("effect.tetra.severed.tooltip", String.format("%d", amp * 10), String.format("%d", amp * 5));
        }));
    }
}
package se.mickelus.tetra.effect.potion;

import java.util.function.Consumer;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientMobEffectExtensions;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import se.mickelus.tetra.effect.gui.EffectUnRenderer;

@ParametersAreNonnullByDefault
public class MiningSpeedPotionEffect extends MobEffect {

    public static final String identifier = "mining_speed";

    public static MiningSpeedPotionEffect instance;

    public MiningSpeedPotionEffect() {
        super(MobEffectCategory.BENEFICIAL, 15658734);
        instance = this;
    }

    @SubscribeEvent
    public static void onBreakSpeed(PlayerEvent.BreakSpeed event) {
        if (event.getEntity().m_21023_(instance)) {
            event.setNewSpeed(event.getNewSpeed() * (float) event.getEntity().m_21124_(instance).getAmplifier() / 10.0F);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void initializeClient(Consumer<IClientMobEffectExtensions> consumer) {
        super.initializeClient(consumer);
        consumer.accept(EffectUnRenderer.INSTANCE);
    }
}
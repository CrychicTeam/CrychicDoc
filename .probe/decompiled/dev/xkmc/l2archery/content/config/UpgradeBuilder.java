package dev.xkmc.l2archery.content.config;

import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.xkmc.l2archery.content.upgrade.Upgrade;
import net.minecraft.world.effect.MobEffect;

public class UpgradeBuilder extends BaseBuilder<UpgradeBuilder, Upgrade, Upgrade, BowArrowStatConfig.ConfigEffect> {

    UpgradeBuilder(BowArrowStatConfig config, RegistryEntry<Upgrade> up) {
        super(config, config.upgrade_effects, up);
    }

    public UpgradeBuilder putEffect(MobEffect type, int duration, int amplifier) {
        this.effects.put(type, new BowArrowStatConfig.ConfigEffect(duration, amplifier));
        return this;
    }
}
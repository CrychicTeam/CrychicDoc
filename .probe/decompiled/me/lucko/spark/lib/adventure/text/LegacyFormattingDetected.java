package me.lucko.spark.lib.adventure.text;

import me.lucko.spark.lib.adventure.util.Nag;

final class LegacyFormattingDetected extends Nag {

    private static final long serialVersionUID = -947793022628807411L;

    LegacyFormattingDetected(final Component component) {
        super("Legacy formatting codes have been detected in a component - this is unsupported behaviour. Please refer to the Adventure documentation (https://docs.adventure.kyori.net) for more information. Component: " + component);
    }
}
package dev.shadowsoffire.attributeslib;

import dev.shadowsoffire.placebo.config.Configuration;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.ResourceLocationException;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import repack.evalex.Expression;

public class ALConfig {

    public static final String[] DEFAULT_BLOCKED_ATTRIBUTES = new String[] { "forge:nametag_distance", "attributeslib:creative_flight", "attributeslib:elytra_flight", "attributeslib:ghost_health" };

    public static boolean enableAttributesGui = true;

    public static boolean enablePotionTooltips = true;

    public static Set<ResourceLocation> hiddenAttributes = new HashSet();

    private static Optional<Expression> protExpr;

    private static Optional<Expression> aValueExpr;

    private static Optional<Expression> armorExpr;

    public static void load() {
        Configuration cfg = new Configuration("attributeslib");
        enableAttributesGui = cfg.getBoolean("Enable Attributes GUI", "general", true, "If the Attributes GUI is available.");
        enablePotionTooltips = cfg.getBoolean("Enable Potion Tooltips", "general", true, "If description tooltips will be added to potion items.");
        String[] hidden = cfg.getStringList("Hidden Attributes", "general", DEFAULT_BLOCKED_ATTRIBUTES, "A list of attributes that will be hidden from the Attributes GUI.");
        hiddenAttributes.clear();
        for (String name : hidden) {
            try {
                hiddenAttributes.add(new ResourceLocation(name));
            } catch (ResourceLocationException var7) {
                AttributesLib.LOGGER.error("Ignoring invalid \"Hidden Attributes\" config entry " + name, var7);
            }
        }
        protExpr = readConfigExpression(cfg, "Protection Formula", "combat_rules", "1 - min(0.025 * protPoints, 0.85)", "The protection damage reduction formula.\nComputed after Prot Pierce and Prot Shred are applied.\nArguments:\n    'protPoints' - The number of protection points the user has after reductions.\nOutput:\n    The percentage of damage taken after protection has been applied, from 0 (no damage taken) to 1 (full damage taken).\nReference:\n    See https://github.com/ezylang/EvalEx#usage-examples for how to write expressions.\n", "protPoints");
        aValueExpr = readConfigExpression(cfg, "A-Value Formula", "combat_rules", "if(damage < 20, 10, 10 + (damage - 20) / 2)", "The a-value formula, which computes an intermediate used in the armor formula.\nArguments:\n    'damage' - The damage of the incoming attack.\nOutput:\n    The a-value, which will be supplied as an argument to the armor formula.\nReference:\n    See https://github.com/ezylang/EvalEx#usage-examples for how to write expressions.\n", "damage");
        armorExpr = readConfigExpression(cfg, "Armor Formula", "combat_rules", "a / (a + armor)", "The armor damage reduction formula.\nComputed after Armor Pierce and Armor Shred are applied.\nArguments:\n    'a' - The a-value computed by the a-value formula.\n    'damage' - The damage of the incoming attack.\n    'armor' - The armor value of the user after reductions.\nOutput:\n    The percentage of damage taken after armor has been applied, from 0 (no damage taken) to 1 (full damage taken).\nReference:\n    See https://github.com/ezylang/EvalEx#usage-examples for how to write expressions.\n", "a", "damage", "armor");
        if (cfg.hasChanged()) {
            cfg.save();
        }
    }

    public static Optional<Expression> getAValueExpr() {
        return aValueExpr;
    }

    public static Optional<Expression> getProtExpr() {
        return protExpr;
    }

    public static Optional<Expression> getArmorExpr() {
        return armorExpr;
    }

    public static ResourceManagerReloadListener makeReloader() {
        return resman -> load();
    }

    private static Optional<Expression> readConfigExpression(Configuration cfg, String key, String group, String defaultValue, String comment, String... args) {
        String exprStr = cfg.getString(key, group, defaultValue, comment);
        if (exprStr.equals(defaultValue)) {
            return Optional.empty();
        } else {
            try {
                Expression expr = new Expression(exprStr);
                for (String arg : args) {
                    expr.setVariable(arg, new BigDecimal(ThreadLocalRandom.current().nextInt(20)));
                }
                expr.eval();
                return Optional.of(expr);
            } catch (Exception var12) {
                AttributesLib.LOGGER.error("Ignoring invalid {} entry {} as the expression failed to evaluate.", key, exprStr);
                var12.printStackTrace();
                return Optional.empty();
            }
        }
    }
}
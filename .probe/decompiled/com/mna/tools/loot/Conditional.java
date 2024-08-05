package com.mna.tools.loot;

import java.util.LinkedHashMap;
import java.util.Map;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class Conditional {

    public static final Map<Conditional, Conditional> reverse = new LinkedHashMap();

    public static final Conditional magmaCream = new Conditional("jer.magmaCream.text", ChatFormatting.DARK_RED);

    public static final Conditional slimeBall = new Conditional("jer.slimeBall.text", ChatFormatting.GREEN);

    public static final Conditional rareDrop = new Conditional("jer.rareDrop.text", ChatFormatting.LIGHT_PURPLE);

    public static final Conditional silkTouch = new Conditional("jer.worldgen.silkTouch", ChatFormatting.DARK_AQUA);

    public static final Conditional equipmentDrop = new Conditional("jer.equipmentDrop.text", ChatFormatting.AQUA);

    public static final Conditional affectedByLooting = new Conditional("jer.affectedByLooting.text", ChatFormatting.AQUA);

    public static final Conditional affectedByFortune = new Conditional("jer.affectedByFortune.text", ChatFormatting.AQUA);

    public static final Conditional powered = new Conditional("jer.powered.text", ChatFormatting.AQUA);

    public static final Conditional burning = new Conditional("jer.burning.text", ChatFormatting.RED);

    public static final Conditional notBurning = new Conditional("jer.notBurning.text", burning);

    public static final Conditional wet = new Conditional("jer.wet.text", ChatFormatting.AQUA);

    public static final Conditional notWet = new Conditional("jer.notWet.text", wet);

    public static final Conditional hasPotion = new Conditional("jer.hasPotion.text", ChatFormatting.LIGHT_PURPLE);

    public static final Conditional hasNoPotion = new Conditional("jer.hasNoPotion.text", hasPotion);

    public static final Conditional beyond = new Conditional("jer.beyond.text", ChatFormatting.DARK_GREEN);

    public static final Conditional nearer = new Conditional("jer.nearer.text", beyond);

    public static final Conditional raining = new Conditional("jer.raining.text", ChatFormatting.GRAY);

    public static final Conditional dry = new Conditional("jer.dry.text", raining);

    public static final Conditional thundering = new Conditional("jer.thundering.text", ChatFormatting.DARK_GRAY);

    public static final Conditional notThundering = new Conditional("jer.notThundering.text", thundering);

    public static final Conditional moonPhase = new Conditional("jer.moonPhase.text");

    public static final Conditional notMoonPhase = new Conditional("jer.notMoonPhase.text", moonPhase);

    public static final Conditional pastTime = new Conditional("jer.pastTime.text", ChatFormatting.LIGHT_PURPLE);

    public static final Conditional beforeTime = new Conditional("jer.beforeTime.text", pastTime);

    public static final Conditional pastWorldTime = new Conditional("jer.pastWorldTime.text", ChatFormatting.DARK_PURPLE);

    public static final Conditional beforeWorldTime = new Conditional("jer.beforeWorldTime.text", pastWorldTime);

    public static final Conditional pastWorldDifficulty = new Conditional("jer.pastWorldDifficulty.text", ChatFormatting.GOLD);

    public static final Conditional beforeWorldDifficulty = new Conditional("jer.beforeWorldDifficulty.text", pastWorldDifficulty);

    public static final Conditional gameDifficulty = new Conditional("jer.gameDifficulty.text", ChatFormatting.GOLD);

    public static final Conditional notGameDifficulty = new Conditional("jer.notGameDifficulty.text", gameDifficulty);

    public static final Conditional inDimension = new Conditional("jer.inDimension.text", ChatFormatting.YELLOW);

    public static final Conditional notInDimension = new Conditional("jer.notInDimension.text", inDimension);

    public static final Conditional inBiome = new Conditional("jer.inBiome.text", ChatFormatting.GOLD);

    public static final Conditional notInBiome = new Conditional("jer.notInBiome.text", inBiome);

    public static final Conditional onBlock = new Conditional("jer.onBlock.text", ChatFormatting.RED);

    public static final Conditional notOnBlock = new Conditional("jer.notOnBlock.text", onBlock);

    public static final Conditional below = new Conditional("jer.below.text", ChatFormatting.DARK_GREEN);

    public static final Conditional above = new Conditional("jer.above.text", below);

    public static final Conditional playerOnline = new Conditional("jer.playerOnline.text", ChatFormatting.BOLD);

    public static final Conditional playerOffline = new Conditional("jer.playerOffline.text", playerOnline);

    public static final Conditional playerKill = new Conditional("jer.playerKill.text");

    public static final Conditional notPlayerKill = new Conditional("jer.notPlayerKill.text", playerKill);

    public static final Conditional aboveLooting = new Conditional("jer.aboveLooting.text", ChatFormatting.DARK_BLUE);

    public static final Conditional belowLooting = new Conditional("jer.belowLooting.text", aboveLooting);

    public static final Conditional killedBy = new Conditional("jer.killedBy.text", ChatFormatting.DARK_RED);

    public static final Conditional notKilledBy = new Conditional("jer.notKilledBy.text", killedBy);

    protected String text;

    protected String colour = "";

    public Conditional() {
    }

    public Conditional(String text, ChatFormatting... ChatFormattings) {
        this.text = text;
        for (ChatFormatting ChatFormatting : ChatFormattings) {
            this.colour = this.colour + ChatFormatting.toString();
        }
    }

    public Conditional(String text, Conditional opposite) {
        this(text);
        this.colour = opposite.colour;
        reverse.put(opposite, this);
        reverse.put(this, opposite);
    }

    public MutableComponent toStringTextComponent() {
        return Component.literal(this.toString());
    }

    public String toString() {
        return this.colour + I18n.get(this.text);
    }
}
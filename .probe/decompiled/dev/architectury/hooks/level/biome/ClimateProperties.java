package dev.architectury.hooks.level.biome;

import net.minecraft.world.level.biome.Biome;

public interface ClimateProperties {

    boolean hasPrecipitation();

    float getTemperature();

    Biome.TemperatureModifier getTemperatureModifier();

    float getDownfall();

    public interface Mutable extends ClimateProperties {

        ClimateProperties.Mutable setHasPrecipitation(boolean var1);

        ClimateProperties.Mutable setTemperature(float var1);

        ClimateProperties.Mutable setTemperatureModifier(Biome.TemperatureModifier var1);

        ClimateProperties.Mutable setDownfall(float var1);
    }
}
package me.srrapero720.embeddiumplus.mixins.impl.entitydistance;

import me.srrapero720.embeddiumplus.EmbeddiumPlus;
import me.srrapero720.embeddiumplus.EmbyConfig;
import me.srrapero720.embeddiumplus.EmbyTools;
import me.srrapero720.embeddiumplus.foundation.entitydistance.IWhitelistCheck;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin({ EntityType.class })
public abstract class EntityTypeMixin implements IWhitelistCheck {

    @Unique
    private static final Marker e$IT = MarkerManager.getMarker("EntityType");

    @Unique
    private boolean embPlus$checked = false;

    @Unique
    private boolean embPlus$whitelisted = false;

    @Shadow
    public abstract MobCategory getCategory();

    @Unique
    @Override
    public boolean embPlus$isWhitelisted() {
        if (this.embPlus$checked) {
            return this.embPlus$whitelisted;
        } else {
            ResourceLocation resource = this.embPlus$resourceLocation();
            if (resource == null) {
                EmbeddiumPlus.LOGGER.warn(e$IT, "key for '{}' is null, some mod decides to broke itself, not whitelisting", this.getClass().getName());
                return false;
            } else {
                this.embPlus$whitelisted = EmbyTools.isWhitelisted(resource, this.getCategory() == MobCategory.MONSTER ? EmbyConfig.monsterWhitelist : EmbyConfig.entityWhitelist);
                this.embPlus$checked = true;
                EmbeddiumPlus.LOGGER.debug(e$IT, "Whitelist checked for {}", resource.toString());
                return this.embPlus$whitelisted;
            }
        }
    }

    @Unique
    public ResourceLocation embPlus$resourceLocation() {
        return BuiltInRegistries.ENTITY_TYPE.getKey(this.embPlus$cast());
    }

    @Unique
    private EntityType<?> embPlus$cast() {
        return (EntityType<?>) this;
    }
}
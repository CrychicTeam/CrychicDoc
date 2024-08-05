package me.srrapero720.embeddiumplus.mixins.impl.entitydistance;

import me.srrapero720.embeddiumplus.EmbeddiumPlus;
import me.srrapero720.embeddiumplus.EmbyConfig;
import me.srrapero720.embeddiumplus.EmbyTools;
import me.srrapero720.embeddiumplus.foundation.entitydistance.IWhitelistCheck;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin({ BlockEntityType.class })
public abstract class TileEntityTypeMixin implements IWhitelistCheck {

    @Unique
    private static final Marker e$IT = MarkerManager.getMarker("BlockEntityType");

    @Unique
    private boolean embPlus$checked = false;

    @Unique
    private boolean embPlus$whitelisted = false;

    @Override
    public boolean embPlus$isWhitelisted() {
        if (this.embPlus$checked) {
            return this.embPlus$whitelisted;
        } else {
            ResourceLocation resource = BlockEntityType.getKey(this.embPlus$cast());
            if (resource == null) {
                EmbeddiumPlus.LOGGER.warn(e$IT, "key for '{}' is null, some mod decides to broke itself, not whitelisting", this.getClass().getName());
                return false;
            } else {
                this.embPlus$whitelisted = EmbyTools.isWhitelisted(resource, EmbyConfig.tileEntityWhitelist);
                this.embPlus$checked = true;
                EmbeddiumPlus.LOGGER.debug(e$IT, "Whitelist checked for {}", resource.toString());
                return this.embPlus$whitelisted;
            }
        }
    }

    @Unique
    private BlockEntityType<?> embPlus$cast() {
        return (BlockEntityType<?>) this;
    }
}
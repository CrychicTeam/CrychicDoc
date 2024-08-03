package journeymap.client.api.model;

import java.lang.ref.WeakReference;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.phys.Vec3;

public interface WrappedEntity {

    WeakReference<LivingEntity> getEntityLivingRef();

    String getEntityId();

    @Nullable
    ResourceLocation getEntityIconLocation();

    Boolean getHostile();

    Vec3 getPosition();

    BlockPos getChunkPos();

    double getHeading();

    @Nullable
    Component getCustomName();

    @Nullable
    Entity getOwner();

    @Nullable
    String getProfession();

    @Nullable
    String getPlayerName();

    @Nullable
    Biome getBiome();

    ResourceKey<Level> getDimension();

    @Nullable
    Boolean getUnderground();

    boolean isInvisible();

    boolean isSneaking();

    boolean isPassiveAnimal();

    boolean isNpc();

    int getColor();

    boolean isDisabled();

    @Nullable
    List<Component> getEntityToolTips();

    void setEntityToolTips(@Nullable List<Component> var1);

    void setEntityIconLocation(ResourceLocation var1);

    void setCustomName(Component var1);

    void setCustomName(String var1);

    void setColor(int var1);

    void setDisable(boolean var1);
}
package journeymap.client.model;

import com.google.common.cache.CacheLoader;
import com.google.common.collect.Lists;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import journeymap.client.JourneymapClient;
import journeymap.client.api.model.WrappedEntity;
import journeymap.client.properties.CoreProperties;
import journeymap.common.Journeymap;
import journeymap.common.helper.DimensionHelper;
import journeymap.common.log.LogFormatter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.npc.Npc;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.PlayerTeam;

public class EntityDTO implements WrappedEntity, Serializable {

    public final String entityId;

    public transient WeakReference<LivingEntity> entityLivingRef;

    public transient ResourceLocation entityIconLocation;

    public String iconLocation;

    public Boolean hostile;

    public double posX;

    public double posY;

    public double posZ;

    public int chunkCoordX;

    public int chunkCoordY;

    public int chunkCoordZ;

    private BlockPos chunkPos;

    public double heading;

    private Component customName;

    public String serializedCustomName;

    public String owner;

    public String profession;

    public String username;

    public String biome;

    public ResourceKey<Level> dimension;

    public Boolean underground;

    public boolean invisible;

    public boolean sneaking;

    public boolean passiveAnimal;

    public boolean npc;

    public int color;

    private List<Component> entityToolTips;

    private List<String> serializedTooltips;

    public boolean disabled = false;

    private EntityDTO(LivingEntity entity) {
        this.entityLivingRef = new WeakReference(entity);
        this.entityId = entity.m_20148_().toString();
    }

    public void update(LivingEntity entity, boolean hostile) {
        Minecraft mc = Minecraft.getInstance();
        Player currentPlayer = Minecraft.getInstance().player;
        this.dimension = DimensionHelper.getDimension(entity);
        this.posX = entity.m_20182_().x();
        this.posY = entity.m_20182_().y();
        this.posZ = entity.m_20182_().z();
        this.chunkCoordX = entity.m_146902_().x;
        this.chunkCoordY = entity.m_146904_() >> 4;
        this.chunkCoordZ = entity.m_146902_().z;
        this.chunkPos = new BlockPos(this.chunkCoordX, this.chunkCoordY, this.chunkCoordZ);
        this.heading = (double) Math.round(entity.m_146908_() % 360.0F);
        this.disabled = false;
        this.customName = null;
        this.serializedCustomName = null;
        this.entityToolTips = null;
        if (currentPlayer != null) {
            this.invisible = entity.m_20177_(currentPlayer);
        } else {
            this.invisible = false;
        }
        this.sneaking = entity.m_6144_();
        CoreProperties coreProperties = JourneymapClient.getInstance().getCoreProperties();
        ResourceLocation entityIcon = null;
        int playerColor = coreProperties.getColor(coreProperties.colorPlayer);
        PlayerTeam team = null;
        try {
            team = mc.level.getScoreboard().getPlayersTeam(entity.m_20149_());
        } catch (Throwable var16) {
        }
        if (entity instanceof Player) {
            this.username = StringUtil.stripColor(entity.m_7755_().getString());
            try {
                if (team != null) {
                    playerColor = team.getColor().getId();
                } else if (currentPlayer.equals(entity)) {
                    playerColor = coreProperties.getColor(coreProperties.colorSelf);
                } else {
                    playerColor = coreProperties.getColor(coreProperties.colorPlayer);
                }
            } catch (Throwable var15) {
            }
            entityIcon = DefaultPlayerSkin.getDefaultSkin();
            try {
                ClientPacketListener client = Minecraft.getInstance().getConnection();
                PlayerInfo info = client.getPlayerInfo(entity.m_20148_());
                if (info != null) {
                    entityIcon = info.getSkinLocation();
                }
            } catch (Throwable var14) {
                Journeymap.getLogger().error("Error looking up player skin: " + LogFormatter.toPartialString(var14));
            }
        } else {
            this.username = null;
            entityIcon = EntityHelper.getIconTextureLocation(entity);
        }
        if (entityIcon != null) {
            this.entityIconLocation = entityIcon;
            this.iconLocation = entityIcon.toString();
        }
        Entity ownerEntity = this.getTamedOwner(entity);
        if (ownerEntity != null) {
            this.owner = ownerEntity.getName().getString();
        }
        Component customName = null;
        boolean passive = false;
        if (entity.m_8077_() && entity.shouldShowName()) {
            customName = entity.m_7770_();
        }
        if (!hostile && currentPlayer != null) {
            LivingEntity attackTarget = entity.getKillCredit();
            if (attackTarget != null && attackTarget.m_20148_().equals(currentPlayer.m_20148_())) {
                hostile = true;
            }
        }
        if (EntityHelper.isPassive(entity)) {
            passive = true;
        }
        if (entity instanceof Villager villager) {
            this.profession = villager.getVillagerData().getProfession().name();
        } else if (entity instanceof Npc) {
            this.npc = true;
            this.profession = null;
            this.passiveAnimal = false;
        } else {
            this.profession = null;
            this.passiveAnimal = passive;
        }
        if (customName != null) {
            this.setCustomName(customName);
        }
        this.hostile = hostile;
        if (entity instanceof Player) {
            this.color = playerColor;
        } else if (team != null) {
            this.color = team.getColor().getId();
        } else {
            label100: if (this.owner == null) {
                if (entity instanceof AbstractHorse horse && horse.isTamed()) {
                    break label100;
                }
                if (this.profession == null && !this.npc) {
                    if (hostile) {
                        this.color = coreProperties.getColor(coreProperties.colorHostile);
                    } else {
                        this.color = coreProperties.getColor(coreProperties.colorPassive);
                    }
                    return;
                }
                this.color = coreProperties.getColor(coreProperties.colorVillager);
                return;
            }
            this.color = coreProperties.getColor(coreProperties.colorPet);
        }
    }

    private Entity getTamedOwner(LivingEntity livingEntity) {
        Player currentPlayer = Minecraft.getInstance().player;
        if (livingEntity instanceof TamableAnimal) {
            Entity ownerEntity = ((TamableAnimal) livingEntity).m_269323_();
            if (ownerEntity != null) {
                return ownerEntity;
            }
        } else if (livingEntity instanceof Horse) {
            UUID ownerUuid = ((Horse) livingEntity).m_21805_();
            if (currentPlayer != null && ownerUuid != null) {
                try {
                    String playerUuid = currentPlayer.m_20148_().toString();
                    if (playerUuid.equals(ownerUuid.toString())) {
                        return currentPlayer;
                    }
                } catch (Throwable var5) {
                    var5.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    public String getEntityId() {
        return this.entityId;
    }

    @Override
    public WeakReference<LivingEntity> getEntityLivingRef() {
        return this.entityLivingRef;
    }

    @Override
    public ResourceLocation getEntityIconLocation() {
        return this.entityIconLocation;
    }

    @Override
    public Boolean getHostile() {
        return this.hostile;
    }

    @Override
    public Vec3 getPosition() {
        return ((LivingEntity) this.entityLivingRef.get()).m_20182_();
    }

    @Override
    public BlockPos getChunkPos() {
        return this.chunkPos;
    }

    @Override
    public double getHeading() {
        return this.heading;
    }

    @Override
    public Component getCustomName() {
        return this.customName;
    }

    @Override
    public Entity getOwner() {
        return this.getTamedOwner((LivingEntity) this.entityLivingRef.get());
    }

    @Override
    public String getProfession() {
        return this.profession;
    }

    @Override
    public String getPlayerName() {
        return this.username;
    }

    @Override
    public Biome getBiome() {
        return (Biome) Minecraft.getInstance().level.m_204166_(((LivingEntity) this.entityLivingRef.get()).m_20183_()).value();
    }

    @Override
    public ResourceKey<Level> getDimension() {
        return this.dimension;
    }

    @Override
    public Boolean getUnderground() {
        return this.underground;
    }

    @Override
    public boolean isInvisible() {
        return this.invisible;
    }

    @Override
    public boolean isSneaking() {
        return this.sneaking;
    }

    @Override
    public boolean isPassiveAnimal() {
        return this.passiveAnimal;
    }

    @Override
    public boolean isNpc() {
        return this.npc;
    }

    @Override
    public int getColor() {
        return this.color;
    }

    @Override
    public boolean isDisabled() {
        return this.disabled;
    }

    @Override
    public void setCustomName(String customName) {
        this.setCustomName(Component.literal(customName));
    }

    @Override
    public void setCustomName(Component customName) {
        this.customName = customName;
        this.serializedCustomName = Component.Serializer.toJson(customName);
    }

    @Override
    public void setColor(int color) {
        this.color = color;
    }

    @Override
    public void setDisable(boolean disable) {
        this.disabled = disable;
    }

    @Override
    public void setEntityIconLocation(ResourceLocation entityIconLocation) {
        this.entityIconLocation = entityIconLocation;
        this.iconLocation = entityIconLocation.toString();
    }

    @Override
    public List<Component> getEntityToolTips() {
        return this.entityToolTips;
    }

    @Override
    public void setEntityToolTips(List<Component> entityToolTips) {
        this.entityToolTips = entityToolTips;
        this.serializedTooltips = (List<String>) entityToolTips.stream().map(Component.Serializer::m_130703_).collect(Collectors.toList());
    }

    public static class EntityDTOExclusionStrategy implements ExclusionStrategy {

        List<String> excludedFields = Lists.newArrayList(new String[] { "customName", "chunkPos", "entityToolTips", "dimension" });

        public boolean shouldSkipField(FieldAttributes f) {
            return f.getDeclaringClass().equals(EntityDTO.class) ? this.excludedFields.contains(f.getName()) : false;
        }

        public boolean shouldSkipClass(Class<?> clazz) {
            return false;
        }
    }

    public static class SimpleCacheLoader extends CacheLoader<LivingEntity, EntityDTO> {

        public EntityDTO load(LivingEntity entity) throws Exception {
            return new EntityDTO(entity);
        }
    }
}
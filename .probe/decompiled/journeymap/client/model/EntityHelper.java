package journeymap.client.model;

import com.google.common.collect.ImmutableSortedMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import journeymap.client.JourneymapClient;
import journeymap.client.api.event.forge.EntityRadarUpdateEvent;
import journeymap.client.data.DataCache;
import journeymap.client.event.dispatchers.CustomEventDispatcher;
import journeymap.client.log.JMLogger;
import journeymap.client.log.StatTimer;
import journeymap.client.mod.impl.Pixelmon;
import journeymap.client.render.RenderFacade;
import journeymap.common.Journeymap;
import journeymap.common.log.LogFormatter;
import journeymap.common.util.PlayerRadarManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.renderer.entity.CreeperRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.HorseRenderer;
import net.minecraft.client.renderer.entity.VillagerRenderer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.npc.Npc;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;

public class EntityHelper {

    public static EntityHelper.EntityDistanceComparator entityDistanceComparator = new EntityHelper.EntityDistanceComparator();

    public static EntityHelper.EntityDTODistanceComparator entityDTODistanceComparator = new EntityHelper.EntityDTODistanceComparator();

    public static EntityHelper.EntityMapComparator entityMapComparator = new EntityHelper.EntityMapComparator();

    private static final String[] HORSE_TEXTURES = new String[] { "textures/entity/horse/horse_white.png", "textures/entity/horse/horse_creamy.png", "textures/entity/horse/horse_chestnut.png", "textures/entity/horse/horse_brown.png", "textures/entity/horse/horse_black.png", "textures/entity/horse/horse_gray.png", "textures/entity/horse/horse_darkbrown.png" };

    public static List<EntityDTO> getEntitiesNearby(String timerName, int maxEntities, boolean hostile, Class... entityClasses) {
        StatTimer timer = StatTimer.get("EntityHelper." + timerName);
        timer.start();
        Minecraft mc = Minecraft.getInstance();
        List<EntityDTO> list = new ArrayList();
        List<Entity> allEntities = new ArrayList();
        mc.level.entitiesForRendering().forEach(allEntities::add);
        AABB bb = getBB(mc.player);
        try {
            for (Entity entity : allEntities) {
                if (entity instanceof LivingEntity && entity.isAlive() && bb.intersects(entity.getBoundingBox())) {
                    for (Class entityClass : entityClasses) {
                        if (entityClass.isAssignableFrom(entity.getClass()) && isSpecialCreature(entity, hostile)) {
                            LivingEntity entityLivingBase = (LivingEntity) entity;
                            EntityDTO dto = DataCache.INSTANCE.getEntityDTO(entityLivingBase);
                            dto.update(entityLivingBase, hostile);
                            if (CustomEventDispatcher.getInstance().entityRadarUpdateEvent(EntityRadarUpdateEvent.EntityType.MOB, dto)) {
                                list.add(dto);
                            }
                            break;
                        }
                    }
                }
            }
            if (list.size() > maxEntities) {
                int before = list.size();
                entityDTODistanceComparator.player = mc.player;
                Collections.sort(list, entityDTODistanceComparator);
                list = list.subList(0, maxEntities);
            }
        } catch (Throwable var17) {
            Journeymap.getLogger().warn("Failed to " + timerName + ": " + LogFormatter.toString(var17));
        }
        timer.stop();
        return list;
    }

    private static boolean isSpecialCreature(Entity entity, boolean hostile) {
        if (PathfinderMob.class.isAssignableFrom(entity.getClass())) {
            return hostile && entity.getType().getCategory().equals(MobCategory.MONSTER) ? true : !hostile && entity.getType().getCategory().isFriendly();
        } else {
            return true;
        }
    }

    public static List<EntityDTO> getMobsNearby() {
        return getEntitiesNearby("getMobsNearby", JourneymapClient.getInstance().getCoreProperties().maxMobsData.get(), true, Enemy.class, PathfinderMob.class);
    }

    public static List<EntityDTO> getVillagersNearby() {
        return getEntitiesNearby("getVillagersNearby", JourneymapClient.getInstance().getCoreProperties().maxVillagersData.get(), false, Villager.class, Npc.class);
    }

    public static List<EntityDTO> getAnimalsNearby() {
        return getEntitiesNearby("getAnimalsNearby", JourneymapClient.getInstance().getCoreProperties().maxAnimalsData.get(), false, Animal.class, AbstractGolem.class, WaterAnimal.class, PathfinderMob.class);
    }

    public static boolean isPassive(LivingEntity entityLiving) {
        if (entityLiving == null) {
            return false;
        } else if (entityLiving instanceof Enemy) {
            return false;
        } else {
            LivingEntity attackTarget = entityLiving.getKillCredit();
            return attackTarget == null || !(attackTarget instanceof Player) && !(attackTarget instanceof TamableAnimal);
        }
    }

    public static List<EntityDTO> getPlayersNearby() {
        StatTimer timer = StatTimer.get("EntityHelper.getPlayersNearby");
        timer.start();
        Minecraft mc = Minecraft.getInstance();
        List<Player> allPlayers = new ArrayList();
        if (JourneymapClient.getInstance().getStateHandler().isExpandedRadarEnabled()) {
            if (mc.getConnection().getOnlinePlayers() != null && mc.getConnection().getOnlinePlayers().size() > 1) {
                for (PlayerInfo onlinePlayer : mc.getConnection().getOnlinePlayers()) {
                    if (!onlinePlayer.getProfile().getId().equals(mc.player.m_20148_())) {
                        Player networkedPlayer = (Player) PlayerRadarManager.getInstance().getPlayers().get(onlinePlayer.getProfile().getId());
                        if (networkedPlayer != null && networkedPlayer.m_9236_().dimension() == Minecraft.getInstance().player.m_9236_().dimension()) {
                            allPlayers.add(networkedPlayer);
                        }
                    }
                }
            }
        } else {
            allPlayers.addAll(mc.level.players());
            allPlayers.remove(mc.player);
        }
        int max = JourneymapClient.getInstance().getCoreProperties().maxPlayersData.get();
        if (allPlayers.size() > max) {
            entityDistanceComparator.player = mc.player;
            Collections.sort(allPlayers, entityDistanceComparator);
            allPlayers = allPlayers.subList(0, max);
        }
        List<EntityDTO> playerDTOs = new ArrayList(allPlayers.size());
        for (Player player : allPlayers) {
            EntityDTO dto = DataCache.INSTANCE.getEntityDTO(player);
            dto.update(player, false);
            if (CustomEventDispatcher.getInstance().entityRadarUpdateEvent(EntityRadarUpdateEvent.EntityType.PLAYER, dto)) {
                playerDTOs.add(dto);
            }
        }
        timer.stop();
        return playerDTOs;
    }

    private static AABB getBB(Player player) {
        int lateralDistance = JourneymapClient.getInstance().getCoreProperties().radarLateralDistance.get();
        int verticalDistance = JourneymapClient.getInstance().getCoreProperties().radarVerticalDistance.get();
        return getBoundingBox(player, (double) lateralDistance, (double) verticalDistance);
    }

    public static AABB getBoundingBox(Player player, double lateralDistance, double verticalDistance) {
        return player.m_20191_().inflate(lateralDistance, verticalDistance, lateralDistance);
    }

    public static Map<String, EntityDTO> buildEntityIdMap(List<? extends EntityDTO> list, boolean sort) {
        if (list != null && !list.isEmpty()) {
            if (sort) {
                Collections.sort(list, new EntityHelper.EntityMapComparator());
            }
            LinkedHashMap<String, EntityDTO> idMap = new LinkedHashMap(list.size());
            for (EntityDTO entityMap : list) {
                idMap.put("id" + entityMap.entityId, entityMap);
            }
            return ImmutableSortedMap.copyOf(idMap);
        } else {
            return Collections.emptyMap();
        }
    }

    public static ResourceLocation getIconTextureLocation(Entity entity) {
        try {
            EntityRenderer entityRender = Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(entity);
            ResourceLocation original = null;
            if (entityRender instanceof CreeperRenderer) {
                Creeper creeper = (Creeper) entity;
                original = entityRender.getTextureLocation(creeper);
                if (creeper.isPowered()) {
                    String path = original.getPath().replace("creeper.png", "creeper_armor.png");
                    original = new ResourceLocation(original.getNamespace(), path);
                }
            } else if (entityRender instanceof VillagerRenderer) {
                Villager villager = (Villager) entity;
                original = entityRender.getTextureLocation(villager);
                String profession = BuiltInRegistries.VILLAGER_PROFESSION.getKey(villager.getVillagerData().getProfession()).getPath();
                if (profession.contains(":")) {
                    String namespace = profession.split(":")[0];
                    String path = profession.split(":")[1];
                    String poiJobPath = original.getPath().replace("villager.png", path + ".png");
                    original = new ResourceLocation(namespace, poiJobPath);
                } else if ("minecraft".equals(original.getNamespace()) && !"unemployed".equals(profession) && !"none".equals(profession)) {
                    String path = original.getPath().replace("villager.png", profession + ".png");
                    original = new ResourceLocation(original.getNamespace(), path);
                }
            } else if (entityRender instanceof HorseRenderer) {
                Horse horse = (Horse) entity;
                original = entityRender.getTextureLocation(horse);
            } else if (Pixelmon.loaded) {
                original = Pixelmon.INSTANCE.getPixelmonResource(entity);
                if (original != null) {
                    return original;
                }
                original = RenderFacade.getEntityTexture(entityRender, entity);
            } else {
                original = RenderFacade.getEntityTexture(entityRender, entity);
            }
            if (original == null) {
                JMLogger.logOnce("Can't get entityTexture for " + entity.getClass() + " via " + entityRender.getClass());
                return null;
            } else {
                ResourceLocation entityIconLoc;
                if (original.getNamespace().equals("cobblemon")) {
                    entityIconLoc = new ResourceLocation(original.getNamespace(), original.getPath().replace("pokemon", "entity_icon"));
                } else if (original.getPath().contains("/model/entity/")) {
                    entityIconLoc = new ResourceLocation(original.getNamespace(), original.getPath().replace("/model/entity/", "/entity_icon/"));
                } else if ((original.getPath().contains("/model/") || original.getPath().contains("/models/")) && !original.getPath().contains("/entity/")) {
                    entityIconLoc = new ResourceLocation(original.getNamespace(), original.getPath().replace("/model/", "/entity_icon/"));
                } else {
                    if (!original.getPath().contains("/entity/")) {
                        JMLogger.logOnce(original + " doesn't have /entity/ in path, so can't look for /entity_icon/");
                        return null;
                    }
                    entityIconLoc = new ResourceLocation(original.getNamespace(), original.getPath().replace("/entity/", "/entity_icon/"));
                }
                if (Minecraft.getInstance().getResourceManager().m_213713_(entityIconLoc).isEmpty()) {
                    JMLogger.logOnce("Can't get entityTexture for " + entityIconLoc);
                    return null;
                } else {
                    return entityIconLoc;
                }
            }
        } catch (Throwable var8) {
            JMLogger.throwLogOnce("Can't get entityTexture for " + entity.getName(), var8);
            return null;
        }
    }

    private static class EntityDTODistanceComparator implements Comparator<EntityDTO> {

        Player player;

        public int compare(EntityDTO o1, EntityDTO o2) {
            LivingEntity e1 = (LivingEntity) o1.entityLivingRef.get();
            LivingEntity e2 = (LivingEntity) o2.entityLivingRef.get();
            return e1 != null && e2 != null ? Double.compare(e1.m_20280_(this.player), e2.m_20280_(this.player)) : 0;
        }
    }

    private static class EntityDistanceComparator implements Comparator<Entity> {

        Player player;

        public int compare(Entity o1, Entity o2) {
            return Double.compare(o1.distanceToSqr(this.player), o2.distanceToSqr(this.player));
        }
    }

    private static class EntityMapComparator implements Comparator<EntityDTO> {

        public int compare(EntityDTO o1, EntityDTO o2) {
            Integer o1rank = 0;
            Integer o2rank = 0;
            if (o1.getCustomName() != null) {
                o1rank = o1rank + 1;
            } else if (o1.username != null) {
                o1rank = o1rank + 2;
            }
            if (o2.getCustomName() != null) {
                o2rank = o2rank + 1;
            } else if (o2.username != null) {
                o2rank = o2rank + 2;
            }
            return o1rank.compareTo(o2rank);
        }
    }
}
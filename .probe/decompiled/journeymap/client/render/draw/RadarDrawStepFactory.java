package journeymap.client.render.draw;

import com.google.common.base.Strings;
import java.util.ArrayList;
import java.util.List;
import journeymap.client.data.DataCache;
import journeymap.client.model.EntityDTO;
import journeymap.client.properties.InGameMapProperties;
import journeymap.client.render.map.GridRenderer;
import journeymap.client.texture.Texture;
import journeymap.client.ui.minimap.EntityDisplay;
import journeymap.common.Journeymap;
import journeymap.common.log.LogFormatter;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Player;

public class RadarDrawStepFactory {

    public List<DrawStep> prepareSteps(List<EntityDTO> entityDTOs, GridRenderer grid, InGameMapProperties mapProperties) {
        boolean showAnimals = mapProperties.showAnimals.get();
        boolean showPets = mapProperties.showPets.get();
        boolean showVillagers = mapProperties.showVillagers.get();
        EntityDisplay mobDisplay = mapProperties.mobDisplay.get();
        EntityDisplay playerDisplay = mapProperties.playerDisplay.get();
        boolean showMobHeading = mapProperties.showMobHeading.get();
        boolean showPlayerHeading = mapProperties.showPlayerHeading.get();
        boolean showEntityNames = mapProperties.showEntityNames.get();
        float mobDisplayDrawScale = mapProperties.mobDisplayScale.get();
        float playerDisplayDrawScale = mapProperties.playerDisplayScale.get();
        List<DrawStep> drawStepList = new ArrayList();
        try {
            for (EntityDTO dto : entityDTOs) {
                try {
                    Texture entityIcon = null;
                    Texture locatorImg = null;
                    LivingEntity entityLiving = (LivingEntity) dto.entityLivingRef.get();
                    boolean isPlayer = entityLiving instanceof Player;
                    if (entityLiving != null && grid.getPixel(dto.posX, dto.posZ) != null) {
                        boolean var10000;
                        label103: {
                            label83: if (dto.owner == null || Strings.isNullOrEmpty(dto.owner)) {
                                if (entityLiving instanceof AbstractHorse horse && horse.isTamed()) {
                                    break label83;
                                }
                                var10000 = false;
                                break label103;
                            }
                            var10000 = true;
                        }
                        boolean isPet = var10000;
                        if ((showPets || !isPet) && (showAnimals || !dto.passiveAnimal || isPlayer || isPet && showPets) && (showVillagers || dto.profession == null && !dto.npc)) {
                            DrawEntityStep drawStep = DataCache.INSTANCE.getDrawEntityStep(entityLiving);
                            if (isPlayer) {
                                locatorImg = EntityDisplay.getLocatorTexture(playerDisplay, showPlayerHeading);
                                Player entity = (Player) entityLiving;
                                entityIcon = EntityDisplay.getEntityTexture(playerDisplay, entityLiving.m_20148_(), entity.getGameProfile().getName());
                                drawStep.update(playerDisplay, locatorImg, entityIcon, dto.color, showPlayerHeading, showEntityNames, playerDisplayDrawScale);
                                drawStepList.add(drawStep);
                            } else {
                                locatorImg = EntityDisplay.getLocatorTexture(mobDisplay, showMobHeading);
                                entityIcon = EntityDisplay.getEntityTexture(mobDisplay, dto.entityIconLocation);
                                EntityDisplay actualDisplay = mobDisplay;
                                if (!mobDisplay.isDots() && entityIcon == null) {
                                    actualDisplay = mobDisplay.getDot();
                                    entityIcon = EntityDisplay.getEntityTexture(actualDisplay, dto.entityIconLocation);
                                }
                                drawStep.update(actualDisplay, locatorImg, entityIcon, dto.color, showMobHeading, showEntityNames, mobDisplayDrawScale);
                                drawStepList.add(drawStep);
                            }
                        }
                    }
                } catch (Exception var24) {
                    Journeymap.getLogger().error("Exception during prepareSteps: " + LogFormatter.toString(var24));
                }
            }
        } catch (Throwable var25) {
            Journeymap.getLogger().error("Throwable during prepareSteps: " + LogFormatter.toString(var25));
        }
        return drawStepList;
    }
}
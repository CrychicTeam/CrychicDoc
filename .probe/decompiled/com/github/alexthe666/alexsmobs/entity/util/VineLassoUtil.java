package com.github.alexthe666.alexsmobs.entity.util;

import com.github.alexthe666.citadel.Citadel;
import com.github.alexthe666.citadel.server.entity.CitadelEntityData;
import com.github.alexthe666.citadel.server.message.PropertiesMessage;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;

public class VineLassoUtil {

    private static final String LASSO_PACKET = "LassoSentPacketAlexsMobs";

    private static final String LASSO_REMOVED = "LassoRemovedAlexsMobs";

    private static final String LASSOED_TO_TAG = "LassoOwnerAlexsMobs";

    private static final String LASSOED_TO_ENTITY_ID_TAG = "LassoOwnerIDAlexsMobs";

    public static void lassoTo(@Nullable LivingEntity lassoer, LivingEntity lassoed) {
        CompoundTag lassoedTag = CitadelEntityData.getOrCreateCitadelTag(lassoed);
        if (lassoer == null) {
            lassoedTag.putUUID("LassoOwnerAlexsMobs", UUID.randomUUID());
            lassoedTag.putInt("LassoOwnerIDAlexsMobs", -1);
            lassoedTag.putBoolean("LassoRemovedAlexsMobs", true);
        } else if (!lassoedTag.contains("LassoOwnerIDAlexsMobs") || lassoedTag.getInt("LassoOwnerIDAlexsMobs") == -1) {
            lassoedTag.putUUID("LassoOwnerAlexsMobs", lassoer.m_20148_());
            lassoedTag.putInt("LassoOwnerIDAlexsMobs", lassoer.m_19879_());
            lassoedTag.putBoolean("LassoRemovedAlexsMobs", false);
        }
        lassoedTag.putBoolean("LassoSentPacketAlexsMobs", true);
        CitadelEntityData.setCitadelTag(lassoed, lassoedTag);
        if (!lassoed.m_9236_().isClientSide) {
            Citadel.sendMSGToAll(new PropertiesMessage("CitadelPatreonConfig", lassoedTag, lassoed.m_19879_()));
        }
    }

    public static boolean hasLassoData(LivingEntity lasso) {
        CompoundTag lassoedTag = CitadelEntityData.getOrCreateCitadelTag(lasso);
        return lassoedTag.contains("LassoOwnerIDAlexsMobs") && !lassoedTag.getBoolean("LassoRemovedAlexsMobs") && lassoedTag.getInt("LassoOwnerIDAlexsMobs") != -1;
    }

    public static Entity getLassoedTo(LivingEntity lassoed) {
        CompoundTag lassoedTag = CitadelEntityData.getOrCreateCitadelTag(lassoed);
        if (lassoedTag.getBoolean("LassoRemovedAlexsMobs")) {
            return null;
        } else {
            if (hasLassoData(lassoed)) {
                if (lassoed.m_9236_().isClientSide && lassoedTag.contains("LassoOwnerIDAlexsMobs")) {
                    int i = lassoedTag.getInt("LassoOwnerIDAlexsMobs");
                    if (i != -1) {
                        Entity found = lassoed.m_9236_().getEntity(i);
                        if (found != null) {
                            return found;
                        }
                        UUID uuid = lassoedTag.getUUID("LassoOwnerAlexsMobs");
                        if (uuid != null) {
                            return lassoed.m_9236_().m_46003_(uuid);
                        }
                    }
                } else if (lassoed.m_9236_() instanceof ServerLevel) {
                    UUID uuid = lassoedTag.getUUID("LassoOwnerAlexsMobs");
                    if (uuid != null) {
                        Entity foundx = ((ServerLevel) lassoed.m_9236_()).getEntity(uuid);
                        if (foundx != null) {
                            lassoedTag.putInt("LassoOwnerIDAlexsMobs", foundx.getId());
                            return foundx;
                        }
                    }
                }
            }
            return null;
        }
    }

    public static void tickLasso(LivingEntity lassoed) {
        CompoundTag tag = CitadelEntityData.getOrCreateCitadelTag(lassoed);
        if (!lassoed.m_9236_().isClientSide && (tag.contains("LassoSentPacketAlexsMobs") || tag.getBoolean("LassoRemovedAlexsMobs"))) {
            tag.putBoolean("LassoSentPacketAlexsMobs", false);
            CitadelEntityData.setCitadelTag(lassoed, tag);
            Citadel.sendMSGToAll(new PropertiesMessage("CitadelPatreonConfig", tag, lassoed.m_19879_()));
        }
        Entity lassoedOwner = getLassoedTo(lassoed);
        if (lassoedOwner != null) {
            double distance = (double) lassoed.m_20270_(lassoedOwner);
            if (lassoed instanceof Mob mob) {
                if (distance > 3.0) {
                    mob.getNavigation().moveTo(lassoedOwner, 1.0);
                } else {
                    mob.getNavigation().stop();
                }
            }
            if (distance > 10.0) {
                double d0 = (lassoedOwner.getX() - lassoed.m_20185_()) / distance;
                double d1 = (lassoedOwner.getY() - lassoed.m_20186_()) / distance;
                double d2 = (lassoedOwner.getZ() - lassoed.m_20189_()) / distance;
                double yd = Math.copySign(d1 * d1 * 0.4, d1);
                if (lassoed instanceof Player) {
                    yd = 0.0;
                }
                lassoed.m_20256_(lassoed.m_20184_().add(Math.copySign(d0 * d0 * 0.4, d0), yd, Math.copySign(d2 * d2 * 0.4, d2)));
            }
        }
    }
}
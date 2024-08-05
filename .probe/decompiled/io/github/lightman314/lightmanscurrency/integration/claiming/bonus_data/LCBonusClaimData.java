package io.github.lightman314.lightmanscurrency.integration.claiming.bonus_data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nonnull;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraftforge.server.ServerLifecycleHooks;

public class LCBonusClaimData extends SavedData {

    private final List<LCBonusClaimData.PlayerData> bonusClaimData = new ArrayList();

    private LCBonusClaimData() {
    }

    private LCBonusClaimData(@Nonnull CompoundTag tag) {
        ListTag dataList = tag.getList("BonusClaims", 10);
        for (int i = 0; i < dataList.size(); i++) {
            CompoundTag data = dataList.getCompound(i);
            LCBonusClaimData.PlayerData playerData = new LCBonusClaimData.PlayerData(data.getUUID("ID"));
            playerData.bonusClaims = data.getInt("Claims");
            playerData.bonusLoads = data.getInt("ChunkLoads");
            this.bonusClaimData.add(playerData);
        }
    }

    @Nonnull
    @Override
    public CompoundTag save(@Nonnull CompoundTag tag) {
        ListTag dataList = new ListTag();
        for (LCBonusClaimData.PlayerData pd : this.bonusClaimData) {
            if (pd.bonusClaims != 0) {
                CompoundTag data = new CompoundTag();
                data.putUUID("ID", pd.playerID);
                data.putInt("Claims", pd.bonusClaims);
                data.putInt("ChunkLoads", pd.bonusLoads);
                dataList.add(data);
            }
        }
        tag.put("BonusClaims", dataList);
        return tag;
    }

    public static int getBonusClaimsFor(Player player) {
        if (player == null) {
            return 0;
        } else {
            LCBonusClaimData data = get();
            if (data != null) {
                for (LCBonusClaimData.PlayerData pd : data.bonusClaimData) {
                    if (pd.matches(player)) {
                        return pd.bonusClaims;
                    }
                }
            }
            return 0;
        }
    }

    public static int getBonusChunkLoadsFor(Player player) {
        if (player == null) {
            return 0;
        } else {
            LCBonusClaimData data = get();
            if (data != null) {
                for (LCBonusClaimData.PlayerData pd : data.bonusClaimData) {
                    if (pd.matches(player)) {
                        return pd.bonusLoads;
                    }
                }
            }
            return 0;
        }
    }

    public static void addBonusClaimsFor(Player player, int amount) {
        if (player != null) {
            LCBonusClaimData data = get();
            if (data != null) {
                for (LCBonusClaimData.PlayerData pd : data.bonusClaimData) {
                    if (pd.matches(player)) {
                        pd.bonusClaims += amount;
                        data.m_77762_();
                        return;
                    }
                }
                LCBonusClaimData.PlayerData newData = new LCBonusClaimData.PlayerData(player.m_20148_());
                data.bonusClaimData.add(newData);
                newData.bonusClaims = amount;
                data.m_77762_();
            }
        }
    }

    public static void addBonusChunkLoadsFor(Player player, int amount) {
        if (player != null) {
            LCBonusClaimData data = get();
            if (data != null) {
                for (LCBonusClaimData.PlayerData pd : data.bonusClaimData) {
                    if (pd.matches(player)) {
                        pd.bonusLoads += amount;
                        data.m_77762_();
                        return;
                    }
                }
                LCBonusClaimData.PlayerData newData = new LCBonusClaimData.PlayerData(player.m_20148_());
                data.bonusClaimData.add(newData);
                newData.bonusLoads = amount;
                data.m_77762_();
            }
        }
    }

    private static LCBonusClaimData get() {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server != null) {
            ServerLevel level = server.getLevel(Level.OVERWORLD);
            if (level != null) {
                return level.getDataStorage().computeIfAbsent(LCBonusClaimData::new, LCBonusClaimData::new, "lightmanscurrency_cadmus_data");
            }
        }
        return null;
    }

    private static class PlayerData {

        final UUID playerID;

        int bonusClaims = 0;

        int bonusLoads = 0;

        PlayerData(@Nonnull UUID playerID) {
            this.playerID = playerID;
        }

        boolean matches(@Nonnull Player player) {
            return this.playerID.equals(player.m_20148_());
        }
    }
}
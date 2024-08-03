package noppes.npcs.blocks.tiles;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import noppes.npcs.CustomBlocks;
import noppes.npcs.controllers.data.PlayerData;
import noppes.npcs.controllers.data.PlayerQuestData;
import noppes.npcs.controllers.data.QuestData;
import noppes.npcs.quests.QuestLocation;

public class TileWaypoint extends TileNpcEntity {

    public String name = "";

    private int ticks = 10;

    private List<Player> recentlyChecked = new ArrayList();

    private List<Player> toCheck;

    public int range = 10;

    public TileWaypoint(BlockPos pos, BlockState state) {
        super(CustomBlocks.tile_waypoint, pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, TileWaypoint tile) {
        if (!level.isClientSide && !tile.name.isEmpty()) {
            tile.ticks--;
            if (tile.ticks <= 0) {
                tile.ticks = 10;
                tile.toCheck = tile.getPlayerList(tile.range, tile.range, tile.range);
                tile.toCheck.removeAll(tile.recentlyChecked);
                List<Player> listMax = tile.getPlayerList(tile.range + 10, tile.range + 10, tile.range + 10);
                tile.recentlyChecked.retainAll(listMax);
                tile.recentlyChecked.addAll(tile.toCheck);
                if (!tile.toCheck.isEmpty()) {
                    for (Player player : tile.toCheck) {
                        PlayerData pdata = PlayerData.get(player);
                        PlayerQuestData playerdata = pdata.questData;
                        for (QuestData data : playerdata.activeQuests.values()) {
                            if (data.quest.type == 3) {
                                QuestLocation quest = (QuestLocation) data.quest.questInterface;
                                if (quest.setFound(data, tile.name)) {
                                    player.m_213846_(Component.translatable(tile.name).append(" ").append(Component.translatable("quest.found")));
                                    playerdata.checkQuestCompletion(player, 3);
                                    pdata.updateClient = true;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private List<Player> getPlayerList(int x, int y, int z) {
        return this.f_58857_.m_45976_(Player.class, new AABB(this.f_58858_, this.f_58858_.offset(1, 1, 1)).inflate((double) x, (double) y, (double) z));
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        this.name = compound.getString("LocationName");
        this.range = compound.getInt("LocationRange");
        if (this.range < 2) {
            this.range = 2;
        }
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        if (!this.name.isEmpty()) {
            compound.putString("LocationName", this.name);
        }
        compound.putInt("LocationRange", this.range);
        super.saveAdditional(compound);
    }
}
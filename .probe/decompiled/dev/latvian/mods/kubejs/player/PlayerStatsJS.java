package dev.latvian.mods.kubejs.player;

import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.stats.StatsCounter;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class PlayerStatsJS {

    public final Player player;

    private final StatsCounter statFile;

    public PlayerStatsJS(Player p, StatsCounter s) {
        this.player = p;
        this.statFile = s;
    }

    public static Stat<?> statOf(Object o) {
        if (o instanceof Stat) {
            return (Stat<?>) o;
        } else if (o instanceof ResourceLocation rl) {
            return Stats.CUSTOM.get(rl);
        } else {
            return o instanceof CharSequence cs ? Stats.CUSTOM.get(new ResourceLocation(cs.toString())) : null;
        }
    }

    public int get(Stat<?> stat) {
        return this.statFile.getValue(stat);
    }

    @HideFromJS
    public int get(ResourceLocation rl) {
        return this.get(Stats.CUSTOM.get(rl));
    }

    public int getPlayTime() {
        return this.get(Stats.PLAY_TIME);
    }

    public int getTimeSinceDeath() {
        return this.get(Stats.TIME_SINCE_DEATH);
    }

    public int getTimeSinceRest() {
        return this.get(Stats.TIME_SINCE_REST);
    }

    public int getTimeCrouchTime() {
        return this.get(Stats.CROUCH_TIME);
    }

    public int getJumps() {
        return this.get(Stats.JUMP);
    }

    public int getWalkDistance() {
        return this.get(Stats.WALK_ONE_CM);
    }

    public int getSprintDistance() {
        return this.get(Stats.SPRINT_ONE_CM);
    }

    public int getSwimDistance() {
        return this.get(Stats.SWIM_ONE_CM);
    }

    public int getCrouchDistance() {
        return this.get(Stats.CROUCH_ONE_CM);
    }

    public int getDamageDealt() {
        return this.get(Stats.DAMAGE_DEALT);
    }

    public int getDamageDealt_absorbed() {
        return this.get(Stats.DAMAGE_DEALT_ABSORBED);
    }

    public int getDamageDealt_resisted() {
        return this.get(Stats.DAMAGE_DEALT_RESISTED);
    }

    public int getDamageTaken() {
        return this.get(Stats.DAMAGE_TAKEN);
    }

    public int getDamageBlocked_by_shield() {
        return this.get(Stats.DAMAGE_BLOCKED_BY_SHIELD);
    }

    public int getDamageAbsorbed() {
        return this.get(Stats.DAMAGE_ABSORBED);
    }

    public int getDamageResisted() {
        return this.get(Stats.DAMAGE_RESISTED);
    }

    public int getDeaths() {
        return this.get(Stats.DEATHS);
    }

    public int getMobKills() {
        return this.get(Stats.MOB_KILLS);
    }

    public int getAnimalsBred() {
        return this.get(Stats.ANIMALS_BRED);
    }

    public int getPlayerKills() {
        return this.get(Stats.PLAYER_KILLS);
    }

    public int getFishCaught() {
        return this.get(Stats.FISH_CAUGHT);
    }

    public void set(Stat<?> stat, int value) {
        this.statFile.setValue(this.player, stat, value);
    }

    public void add(Stat<?> stat, int value) {
        this.statFile.increment(this.player, stat, value);
    }

    public int getBlocksMined(Block block) {
        return this.statFile.getValue(Stats.BLOCK_MINED.get(block));
    }

    public int getItemsCrafted(Item item) {
        return this.statFile.getValue(Stats.ITEM_CRAFTED.get(item));
    }

    public int getItemsUsed(Item item) {
        return this.statFile.getValue(Stats.ITEM_USED.get(item));
    }

    public int getItemsBroken(Item item) {
        return this.statFile.getValue(Stats.ITEM_BROKEN.get(item));
    }

    public int getItemsPickedUp(Item item) {
        return this.statFile.getValue(Stats.ITEM_PICKED_UP.get(item));
    }

    public int getItemsDropped(Item item) {
        return this.statFile.getValue(Stats.ITEM_DROPPED.get(item));
    }

    public int getKilled(EntityType<?> entity) {
        return this.statFile.getValue(Stats.ENTITY_KILLED.get(entity));
    }

    public int getKilledBy(EntityType<?> entity) {
        return this.statFile.getValue(Stats.ENTITY_KILLED_BY.get(entity));
    }
}
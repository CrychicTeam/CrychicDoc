package dev.latvian.mods.kubejs.block;

import dev.latvian.mods.kubejs.level.BlockContainerJS;
import dev.latvian.mods.kubejs.player.PlayerEventJS;
import dev.latvian.mods.kubejs.typings.Info;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

@Info("Invoked when a player right clicks on a block.\n")
public class BlockRightClickedEventJS extends PlayerEventJS {

    private final Player player;

    private final InteractionHand hand;

    private final BlockPos pos;

    private final Direction direction;

    private BlockContainerJS block;

    private ItemStack item;

    public BlockRightClickedEventJS(Player player, InteractionHand hand, BlockPos pos, Direction direction) {
        this.player = player;
        this.hand = hand;
        this.pos = pos;
        this.direction = direction;
    }

    @Info("The player that right clicked the block.")
    @Override
    public Player getEntity() {
        return this.player;
    }

    @Info("The block that was right clicked.")
    public BlockContainerJS getBlock() {
        if (this.block == null) {
            this.block = new BlockContainerJS(this.player.m_9236_(), this.pos);
        }
        return this.block;
    }

    @Info("The hand that was used to right click the block.")
    public InteractionHand getHand() {
        return this.hand;
    }

    @Info("The position of the block that was right clicked.")
    public ItemStack getItem() {
        if (this.item == null) {
            this.item = this.player.m_21120_(this.hand);
        }
        return this.item;
    }

    @Info("The face of the block being right clicked.")
    public Direction getFacing() {
        return this.direction;
    }
}
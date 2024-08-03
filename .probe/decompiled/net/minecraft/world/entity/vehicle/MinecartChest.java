package net.minecraft.world.entity.vehicle;

import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

public class MinecartChest extends AbstractMinecartContainer {

    public MinecartChest(EntityType<? extends MinecartChest> entityTypeExtendsMinecartChest0, Level level1) {
        super(entityTypeExtendsMinecartChest0, level1);
    }

    public MinecartChest(Level level0, double double1, double double2, double double3) {
        super(EntityType.CHEST_MINECART, double1, double2, double3, level0);
    }

    @Override
    protected Item getDropItem() {
        return Items.CHEST_MINECART;
    }

    @Override
    public int getContainerSize() {
        return 27;
    }

    @Override
    public AbstractMinecart.Type getMinecartType() {
        return AbstractMinecart.Type.CHEST;
    }

    @Override
    public BlockState getDefaultDisplayBlockState() {
        return (BlockState) Blocks.CHEST.defaultBlockState().m_61124_(ChestBlock.FACING, Direction.NORTH);
    }

    @Override
    public int getDefaultDisplayOffset() {
        return 8;
    }

    @Override
    public AbstractContainerMenu createMenu(int int0, Inventory inventory1) {
        return ChestMenu.threeRows(int0, inventory1, this);
    }

    @Override
    public void stopOpen(Player player0) {
        this.m_9236_().m_214171_(GameEvent.CONTAINER_CLOSE, this.m_20182_(), GameEvent.Context.of(player0));
    }

    @Override
    public InteractionResult interact(Player player0, InteractionHand interactionHand1) {
        InteractionResult $$2 = this.m_268996_(player0);
        if ($$2.consumesAction()) {
            this.m_146852_(GameEvent.CONTAINER_OPEN, player0);
            PiglinAi.angerNearbyPiglins(player0, true);
        }
        return $$2;
    }
}
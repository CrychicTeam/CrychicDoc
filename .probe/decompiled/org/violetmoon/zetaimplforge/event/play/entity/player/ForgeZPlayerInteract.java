package org.violetmoon.zetaimplforge.event.play.entity.player;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import org.violetmoon.zeta.event.play.entity.player.ZPlayerInteract;

public class ForgeZPlayerInteract implements ZPlayerInteract {

    private final PlayerInteractEvent e;

    public ForgeZPlayerInteract(PlayerInteractEvent e) {
        this.e = e;
    }

    @Override
    public Player getEntity() {
        return this.e.getEntity();
    }

    @Override
    public InteractionHand getHand() {
        return this.e.getHand();
    }

    @Override
    public BlockPos getPos() {
        return this.e.getPos();
    }

    @Override
    public Level getLevel() {
        return this.e.getLevel();
    }

    @Override
    public boolean isCanceled() {
        return this.e.isCanceled();
    }

    @Override
    public void setCanceled(boolean cancel) {
        this.e.setCanceled(cancel);
    }

    @Override
    public void setCancellationResult(InteractionResult result) {
        this.e.setCancellationResult(result);
    }

    public static class EntityInteract extends ForgeZPlayerInteract implements ZPlayerInteract.EntityInteract {

        private final PlayerInteractEvent.EntityInteract e;

        public EntityInteract(PlayerInteractEvent.EntityInteract e) {
            super(e);
            this.e = e;
        }

        @Override
        public Entity getTarget() {
            return this.e.getTarget();
        }
    }

    public static class EntityInteractSpecific extends ForgeZPlayerInteract implements ZPlayerInteract.EntityInteractSpecific {

        private final PlayerInteractEvent.EntityInteractSpecific e;

        public EntityInteractSpecific(PlayerInteractEvent.EntityInteractSpecific e) {
            super(e);
            this.e = e;
        }

        @Override
        public Entity getTarget() {
            return this.e.getTarget();
        }
    }

    public static class RightClickBlock extends ForgeZPlayerInteract implements ZPlayerInteract.RightClickBlock {

        private final PlayerInteractEvent.RightClickBlock e;

        public RightClickBlock(PlayerInteractEvent.RightClickBlock e) {
            super(e);
            this.e = e;
        }
    }

    public static class RightClickItem extends ForgeZPlayerInteract implements ZPlayerInteract.RightClickItem {

        private final PlayerInteractEvent.RightClickItem e;

        public RightClickItem(PlayerInteractEvent.RightClickItem e) {
            super(e);
            this.e = e;
        }

        @Override
        public ItemStack getItemStack() {
            return this.e.getItemStack();
        }
    }
}
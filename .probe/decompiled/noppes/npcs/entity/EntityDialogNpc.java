package noppes.npcs.entity;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import noppes.npcs.CustomEntities;

public class EntityDialogNpc extends EntityNPCInterface {

    public EntityDialogNpc(Level world) {
        super(CustomEntities.entityCustomNpc, world);
    }

    @Override
    public boolean isInvisibleTo(Player player) {
        return true;
    }

    @Override
    public boolean isInvisible() {
        return true;
    }

    @Override
    public void tick() {
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        return InteractionResult.FAIL;
    }
}
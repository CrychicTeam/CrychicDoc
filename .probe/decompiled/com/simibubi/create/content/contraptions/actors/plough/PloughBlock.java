package com.simibubi.create.content.contraptions.actors.plough;

import com.mojang.authlib.GameProfile;
import com.simibubi.create.content.contraptions.actors.AttachedActorBlock;
import java.util.UUID;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.common.util.FakePlayer;

public class PloughBlock extends AttachedActorBlock {

    public PloughBlock(BlockBehaviour.Properties p_i48377_1_) {
        super(p_i48377_1_);
    }

    static class PloughFakePlayer extends FakePlayer {

        public static final GameProfile PLOUGH_PROFILE = new GameProfile(UUID.fromString("9e2faded-eeee-4ec2-c314-dad129ae971d"), "Plough");

        public PloughFakePlayer(ServerLevel world) {
            super(world, PLOUGH_PROFILE);
        }
    }
}
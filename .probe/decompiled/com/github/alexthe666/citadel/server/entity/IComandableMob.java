package com.github.alexthe666.citadel.server.entity;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;

public interface IComandableMob {

    int getCommand();

    void setCommand(int var1);

    default InteractionResult playerSetCommand(Player owner, Animal ourselves) {
        if (!owner.m_9236_().isClientSide) {
            int command = (this.getCommand() + 1) % 3;
            this.setCommand(command);
            this.sendCommandMessage(owner, command, ourselves.m_7755_());
            if (ourselves instanceof TamableAnimal) {
                ((TamableAnimal) ourselves).setOrderedToSit(command == 1);
            }
        }
        return InteractionResult.PASS;
    }

    default void sendCommandMessage(Player owner, int command, Component name) {
    }
}
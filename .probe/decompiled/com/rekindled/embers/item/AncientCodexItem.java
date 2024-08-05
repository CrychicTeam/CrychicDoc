package com.rekindled.embers.item;

import com.rekindled.embers.datagen.EmbersSounds;
import com.rekindled.embers.gui.GuiCodex;
import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class AncientCodexItem extends Item {

    public AncientCodexItem(Item.Properties pProperties) {
        super(pProperties);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (level.isClientSide) {
            Minecraft.getInstance().setScreen(GuiCodex.instance);
        }
        level.playSound(player, player, EmbersSounds.CODEX_OPEN.get(), SoundSource.MASTER, 0.75F, 1.0F);
        return InteractionResultHolder.success(player.m_21120_(hand));
    }
}
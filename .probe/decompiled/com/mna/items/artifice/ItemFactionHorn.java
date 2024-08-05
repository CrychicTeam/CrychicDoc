package com.mna.items.artifice;

import com.mna.Registries;
import com.mna.api.faction.IFaction;
import com.mna.api.items.TieredItem;
import com.mna.capabilities.playerdata.progression.PlayerProgressionProvider;
import com.mna.entities.faction.util.FactionRaid;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.IForgeRegistry;

public class ItemFactionHorn extends TieredItem {

    private final ResourceLocation faction;

    private IFaction _cachedFaction;

    public ItemFactionHorn(ResourceLocation faction) {
        super(new Item.Properties().rarity(Rarity.EPIC));
        this.faction = faction;
    }

    public IFaction getFaction() {
        if (this._cachedFaction == null) {
            this._cachedFaction = (IFaction) ((IForgeRegistry) Registries.Factions.get()).getValue(this.faction);
        }
        return this._cachedFaction;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level world = context.getLevel();
        if (world.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            context.getPlayer().getCapability(PlayerProgressionProvider.PROGRESSION).ifPresent(p -> {
                if (p.getTier() < 5) {
                    context.getPlayer().m_213846_(Component.translatable("item.mna.faction_horn.low_tier"));
                } else if (p.getAlliedFaction() != null && p.getAlliedFaction().equals(this.getFaction())) {
                    context.getPlayer().getCooldowns().addCooldown(this, 18000);
                    if (this.getHornSound() != null) {
                        world.playSound(null, (double) context.getClickedPos().m_123341_(), (double) context.getClickedPos().m_123342_(), (double) context.getClickedPos().m_123343_(), this.getHornSound(), SoundSource.PLAYERS, 1.0F, (float) (0.9F + 0.2F * Math.random()));
                    }
                    this.spawnAllies(world, context.getPlayer(), context.getClickedPos().above());
                } else {
                    context.getPlayer().m_213846_(Component.translatable("item.mna.faction_horn.wrong_faction"));
                }
            });
            return InteractionResult.SUCCESS;
        }
    }

    private void spawnAllies(Level world, Player player, BlockPos pos) {
        int strength = 150;
        FactionRaid fre = new FactionRaid(world, null, strength);
        fre.m_6034_((double) pos.m_123341_() + 0.5, (double) pos.m_123342_(), (double) pos.m_123343_() + 0.5);
        fre.setFaction(this.faction);
        fre.setProtective(player);
        world.m_7967_(fre);
    }

    private SoundEvent getHornSound() {
        IFaction faction = (IFaction) ((IForgeRegistry) Registries.Factions.get()).getValue(this.faction);
        return faction != null ? faction.getHornSound() : null;
    }

    @Override
    public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
    }
}
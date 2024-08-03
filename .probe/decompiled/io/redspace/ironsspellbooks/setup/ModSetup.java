package io.redspace.ironsspellbooks.setup;

import io.redspace.ironsspellbooks.block.alchemist_cauldron.AlchemistCauldronBlock;
import io.redspace.ironsspellbooks.block.alchemist_cauldron.AlchemistCauldronInteraction;
import io.redspace.ironsspellbooks.block.alchemist_cauldron.AlchemistCauldronTile;
import io.redspace.ironsspellbooks.capabilities.magic.MagicEvents;
import io.redspace.ironsspellbooks.compat.CompatHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.entity.DispenserBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class ModSetup {

    public static void setup() {
        IEventBus bus = MinecraftForge.EVENT_BUS;
        bus.addGenericListener(Entity.class, MagicEvents::onAttachCapabilitiesPlayer);
        bus.addListener(MagicEvents::onRegisterCapabilities);
        bus.addListener(MagicEvents::onWorldTick);
    }

    public static void init(FMLCommonSetupEvent event) {
        Messages.register();
        CompatHandler.init();
        event.enqueueWork(() -> DispenserBlock.registerBehavior(Items.GLASS_BOTTLE.asItem(), new OptionalDispenseItemBehavior() {

            private final DefaultDispenseItemBehavior defaultDispenseItemBehavior = new DefaultDispenseItemBehavior();

            final DispenseItemBehavior oldBehavior = (DispenseItemBehavior) DispenserBlock.DISPENSER_REGISTRY.get(Items.GLASS_BOTTLE);

            private ItemStack takeLiquid(BlockSource p_123447_, ItemStack p_123448_, ItemStack p_123449_) {
                p_123448_.shrink(1);
                if (p_123448_.isEmpty()) {
                    p_123447_.getLevel().m_142346_(null, GameEvent.FLUID_PICKUP, p_123447_.getPos());
                    return p_123449_.copy();
                } else {
                    if (p_123447_.<DispenserBlockEntity>getEntity().addItem(p_123449_.copy()) < 0) {
                        this.defaultDispenseItemBehavior.dispense(p_123447_, p_123449_.copy());
                    }
                    return p_123448_;
                }
            }

            @Override
            public ItemStack execute(BlockSource blockSource, ItemStack itemStack) {
                this.m_123573_(false);
                ServerLevel serverlevel = blockSource.getLevel();
                BlockPos blockpos = blockSource.getPos().relative((Direction) blockSource.getBlockState().m_61143_(DispenserBlock.FACING));
                BlockState blockstate = serverlevel.m_8055_(blockpos);
                if (AlchemistCauldronBlock.getLevel(blockstate) > 0 && serverlevel.m_7702_(blockpos) instanceof AlchemistCauldronTile cauldron) {
                    ItemStack resultStack = ((AlchemistCauldronInteraction) cauldron.interactions.get(itemStack.getItem())).interact(blockstate, serverlevel, blockpos, AlchemistCauldronBlock.getLevel(blockstate), itemStack);
                    if (resultStack != null) {
                        this.m_123573_(true);
                        cauldron.setChanged();
                        return this.takeLiquid(blockSource, itemStack, resultStack);
                    }
                }
                return this.oldBehavior.dispense(blockSource, itemStack);
            }
        }));
    }
}
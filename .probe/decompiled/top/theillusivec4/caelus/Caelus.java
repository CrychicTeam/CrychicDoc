package top.theillusivec4.caelus;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import top.theillusivec4.caelus.api.CaelusApi;
import top.theillusivec4.caelus.common.CaelusApiImpl;
import top.theillusivec4.caelus.common.network.CaelusNetwork;

@Mod("caelus")
public class Caelus {

    public static final String MOD_ID = "caelus";

    public Caelus() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        CaelusApiImpl.ATTRIBUTES.register(eventBus);
        eventBus.addListener(this::setup);
        eventBus.addListener(this::attributeSetup);
        MinecraftForge.EVENT_BUS.addListener(this::playerTick);
    }

    private void attributeSetup(EntityAttributeModificationEvent evt) {
        for (EntityType<? extends LivingEntity> type : evt.getTypes()) {
            evt.add(type, CaelusApi.getInstance().getFlightAttribute());
        }
    }

    private void setup(FMLCommonSetupEvent evt) {
        CaelusNetwork.setup();
    }

    private void playerTick(TickEvent.PlayerTickEvent evt) {
        Player player = evt.player;
        AttributeInstance attributeInstance = player.m_21051_(CaelusApi.getInstance().getFlightAttribute());
        if (attributeInstance != null) {
            AttributeModifier elytraModifier = CaelusApi.getInstance().getElytraModifier();
            attributeInstance.removeModifier(elytraModifier);
            ItemStack stack = player.getItemBySlot(EquipmentSlot.CHEST);
            if (stack.canElytraFly(player) && !attributeInstance.hasModifier(elytraModifier)) {
                attributeInstance.addTransientModifier(elytraModifier);
            }
        }
    }
}
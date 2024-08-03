package net.liopyu.entityjs.util;

import com.mrcrayfish.guns.common.Gun;
import com.mrcrayfish.guns.common.ProjectileManager;
import com.mrcrayfish.guns.entity.MissileEntity;
import com.mrcrayfish.guns.entity.ProjectileEntity;
import com.mrcrayfish.guns.item.GunItem;
import dev.architectury.platform.Platform;
import net.liopyu.entityjs.entities.nonliving.modded.CGMProjectileEntityJS;
import net.liopyu.entityjs.item.CGMProjectileItemBuilder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@EventBusSubscriber(modid = "entityjs", bus = Bus.MOD)
public class SubEvents {

    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        if (Platform.isModLoaded("cgm") && Platform.isModLoaded("framework")) {
            event.enqueueWork(() -> registerCGMEntities());
        }
    }

    private static void registerCGMEntities() {
        for (CGMProjectileItemBuilder itemBuilder : CGMProjectileItemBuilder.thisList) {
            ProjectileManager.getInstance().registerFactory(itemBuilder.get(), (worldIn, entity, weapon, item1, modifiedGun) -> (ProjectileEntity) newCGMProjectileEntity(itemBuilder, worldIn, entity, weapon, item1, modifiedGun));
        }
    }

    private static Object newCGMProjectileEntity(CGMProjectileItemBuilder itemBuilder, Level worldIn, LivingEntity entity, ItemStack weapon, GunItem item1, Gun modifiedGun) {
        return new CGMProjectileEntityJS(itemBuilder.parent, (EntityType<? extends MissileEntity>) itemBuilder.parent.get(), worldIn, entity, weapon, item1, modifiedGun);
    }
}
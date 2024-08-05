package com.yungnickyoung.minecraft.yungsapi.services;

import com.yungnickyoung.minecraft.yungsapi.YungsApiCommon;
import com.yungnickyoung.minecraft.yungsapi.api.autoregister.AutoRegister;
import com.yungnickyoung.minecraft.yungsapi.autoregister.AutoRegisterField;
import com.yungnickyoung.minecraft.yungsapi.autoregister.AutoRegisterFieldRouter;
import com.yungnickyoung.minecraft.yungsapi.autoregister.AutoRegistrationManager;
import com.yungnickyoung.minecraft.yungsapi.module.BlockEntityTypeModuleForge;
import com.yungnickyoung.minecraft.yungsapi.module.BlockModuleForge;
import com.yungnickyoung.minecraft.yungsapi.module.CommandModuleForge;
import com.yungnickyoung.minecraft.yungsapi.module.CompostModuleForge;
import com.yungnickyoung.minecraft.yungsapi.module.CreativeModeTabModuleForge;
import com.yungnickyoung.minecraft.yungsapi.module.CriteriaModuleForge;
import com.yungnickyoung.minecraft.yungsapi.module.EntityTypeModuleForge;
import com.yungnickyoung.minecraft.yungsapi.module.FeatureModuleForge;
import com.yungnickyoung.minecraft.yungsapi.module.ItemModuleForge;
import com.yungnickyoung.minecraft.yungsapi.module.MobEffectModuleForge;
import com.yungnickyoung.minecraft.yungsapi.module.PlacementModifierTypeModuleForge;
import com.yungnickyoung.minecraft.yungsapi.module.PostLoadModuleForge;
import com.yungnickyoung.minecraft.yungsapi.module.PotionModuleForge;
import com.yungnickyoung.minecraft.yungsapi.module.SoundEventModuleForge;
import com.yungnickyoung.minecraft.yungsapi.module.StructurePieceTypeModuleForge;
import com.yungnickyoung.minecraft.yungsapi.module.StructurePlacementTypeModuleForge;
import com.yungnickyoung.minecraft.yungsapi.module.StructurePoolElementTypeModuleForge;
import com.yungnickyoung.minecraft.yungsapi.module.StructureProcessorTypeModuleForge;
import com.yungnickyoung.minecraft.yungsapi.module.StructureTypeModuleForge;
import java.lang.annotation.ElementType;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.forgespi.language.ModFileScanData;
import net.minecraftforge.forgespi.language.ModFileScanData.AnnotationData;
import org.objectweb.asm.Type;

public class ForgeAutoRegisterHelper implements IAutoRegisterHelper {

    @Override
    public void collectAllAutoRegisterFieldsInPackage(String packageName) {
        Map<Type, String> classToNamespaceMap = new HashMap();
        List<AnnotationData> annotations = ModList.get().getAllScanData().stream().map(ModFileScanData::getAnnotations).flatMap(Collection::stream).filter(a -> a.annotationType().equals(Type.getType(AutoRegister.class))).toList();
        annotations.stream().filter(data -> data.targetType() == ElementType.TYPE).forEach(data -> classToNamespaceMap.put(data.clazz(), (String) data.annotationData().get("value")));
        annotations.stream().filter(data -> data.targetType() == ElementType.FIELD).forEach(data -> {
            String modId = (String) classToNamespaceMap.get(data.clazz());
            if (modId == null) {
                YungsApiCommon.LOGGER.error("Missing class AutoRegister annotation for field {}", data.memberName());
            } else {
                Class<?> clazz;
                try {
                    clazz = Class.forName(data.clazz().getClassName(), false, AutoRegistrationManager.class.getClassLoader());
                } catch (ClassNotFoundException var10) {
                    YungsApiCommon.LOGGER.error("Unable to find class containing AutoRegister field {}. This shouldn't happen!", data.memberName());
                    YungsApiCommon.LOGGER.error("If you're using AutoRegister on a field, make sure the containing class is also using the AutoRegister annotation with your mod ID as the value.");
                    throw new RuntimeException(var10);
                }
                Field f;
                try {
                    f = clazz.getDeclaredField(data.memberName());
                } catch (NoSuchFieldException var9) {
                    YungsApiCommon.LOGGER.error("Unable to find AutoRegister field with name {} in class {}. This shouldn't happen!", data.memberName(), clazz.getName());
                    throw new RuntimeException(var9);
                }
                Object o;
                try {
                    o = f.get(null);
                } catch (IllegalAccessException var8) {
                    YungsApiCommon.LOGGER.error("Unable to get value for AutoRegister field {}. This shouldn't happen!", data.memberName());
                    throw new RuntimeException(var8);
                }
                String name = (String) data.annotationData().get("value");
                AutoRegisterField autoRegisterField = new AutoRegisterField(o, new ResourceLocation(modId, name));
                AutoRegisterFieldRouter.queueField(autoRegisterField);
            }
        });
    }

    @Override
    public void invokeAllAutoRegisterMethods(String packageName) {
        List<Method> methods = new ArrayList();
        List<AnnotationData> annotations = ModList.get().getAllScanData().stream().map(ModFileScanData::getAnnotations).flatMap(Collection::stream).filter(a -> a.annotationType().equals(Type.getType(AutoRegister.class))).toList();
        annotations.stream().filter(data -> data.targetType() == ElementType.METHOD).forEach(data -> {
            Class<?> clazz;
            try {
                clazz = Class.forName(data.clazz().getClassName(), false, AutoRegistrationManager.class.getClassLoader());
            } catch (ClassNotFoundException var6) {
                YungsApiCommon.LOGGER.error("Unable to find class containing AutoRegister method {}. This shouldn't happen!", data.memberName());
                YungsApiCommon.LOGGER.error("If you're using AutoRegister on a method, make sure the containing class is also using the AutoRegister annotation with your mod ID as the value.");
                throw new RuntimeException(var6);
            }
            Method m;
            try {
                m = clazz.getDeclaredMethod(data.memberName().substring(0, data.memberName().indexOf("(")));
            } catch (NoSuchMethodException var5) {
                YungsApiCommon.LOGGER.error("Unable to find AutoRegister method with name {} in class {}. This shouldn't happen!", data.memberName(), clazz.getName());
                throw new RuntimeException(var5);
            }
            m.setAccessible(true);
            methods.add(m);
        });
        PostLoadModuleForge.METHODS.addAll(methods);
        PostLoadModuleForge.init();
    }

    @Override
    public void processQueuedAutoRegEntries() {
        SoundEventModuleForge.processEntries();
        StructurePieceTypeModuleForge.processEntries();
        StructurePoolElementTypeModuleForge.processEntries();
        CriteriaModuleForge.processEntries();
        StructureTypeModuleForge.processEntries();
        FeatureModuleForge.processEntries();
        PlacementModifierTypeModuleForge.processEntries();
        CreativeModeTabModuleForge.processEntries();
        ItemModuleForge.processEntries();
        BlockModuleForge.processEntries();
        BlockEntityTypeModuleForge.processEntries();
        StructureProcessorTypeModuleForge.processEntries();
        StructurePlacementTypeModuleForge.processEntries();
        EntityTypeModuleForge.processEntries();
        MobEffectModuleForge.processEntries();
        PotionModuleForge.processEntries();
        CommandModuleForge.processEntries();
    }

    @Override
    public void registerBrewingRecipe(Supplier<Potion> inputPotion, Supplier<Item> ingredient, Supplier<Potion> outputPotion) {
        PotionModuleForge.BrewingRecipe recipe = new PotionModuleForge.BrewingRecipe(inputPotion, ingredient, outputPotion);
        PotionModuleForge.BREWING_RECIPES.add(recipe);
    }

    @Override
    public void addCompostableItem(Supplier<Item> ingredient, float compostChance) {
        CompostModuleForge.COMPOSTABLES.put((ItemLike) ingredient.get(), compostChance);
    }
}
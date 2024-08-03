package com.yungnickyoung.minecraft.yungsapi.autoregister;

import com.yungnickyoung.minecraft.yungsapi.services.Services;
import java.util.ArrayList;
import java.util.List;

public class AutoRegistrationManager {

    public static final List<AutoRegisterField> STRUCTURE_TYPES = new ArrayList();

    public static final List<AutoRegisterField> STRUCTURE_POOL_ELEMENT_TYPES = new ArrayList();

    public static final List<AutoRegisterField> STRUCTURE_PROCESSOR_TYPES = new ArrayList();

    public static final List<AutoRegisterField> STRUCTURE_PIECE_TYPES = new ArrayList();

    public static final List<AutoRegisterField> STRUCTURE_PLACEMENT_TYPES = new ArrayList();

    public static final List<AutoRegisterField> FEATURES = new ArrayList();

    public static final List<AutoRegisterField> PLACEMENT_MODIFIER_TYPES = new ArrayList();

    public static final List<AutoRegisterField> CRITERION_TRIGGERS = new ArrayList();

    public static final List<AutoRegisterField> BLOCKS = new ArrayList();

    public static final List<AutoRegisterField> ITEMS = new ArrayList();

    public static final List<AutoRegisterField> BLOCK_ENTITY_TYPES = new ArrayList();

    public static final List<AutoRegisterField> CREATIVE_MODE_TABS = new ArrayList();

    public static final List<AutoRegisterField> ENTITY_TYPES = new ArrayList();

    public static final List<AutoRegisterField> MOB_EFFECTS = new ArrayList();

    public static final List<AutoRegisterField> POTIONS = new ArrayList();

    public static final List<AutoRegisterField> SOUND_EVENTS = new ArrayList();

    public static final List<AutoRegisterField> COMMANDS = new ArrayList();

    public static void initAutoRegPackage(String packageName) {
        Services.AUTO_REGISTER.collectAllAutoRegisterFieldsInPackage(packageName);
        Services.AUTO_REGISTER.processQueuedAutoRegEntries();
        Services.AUTO_REGISTER.invokeAllAutoRegisterMethods(packageName);
    }
}
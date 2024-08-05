package net.minecraft.world.level.block.state.properties;

import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import java.util.Set;
import java.util.stream.Stream;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.SoundType;

public record BlockSetType(String f_271253_, boolean f_278463_, SoundType f_271136_, SoundEvent f_271502_, SoundEvent f_271141_, SoundEvent f_271425_, SoundEvent f_271258_, SoundEvent f_271234_, SoundEvent f_271481_, SoundEvent f_271194_, SoundEvent f_271394_) {

    private final String name;

    private final boolean canOpenByHand;

    private final SoundType soundType;

    private final SoundEvent doorClose;

    private final SoundEvent doorOpen;

    private final SoundEvent trapdoorClose;

    private final SoundEvent trapdoorOpen;

    private final SoundEvent pressurePlateClickOff;

    private final SoundEvent pressurePlateClickOn;

    private final SoundEvent buttonClickOff;

    private final SoundEvent buttonClickOn;

    private static final Set<BlockSetType> VALUES = new ObjectArraySet();

    public static final BlockSetType IRON = register(new BlockSetType("iron", false, SoundType.METAL, SoundEvents.IRON_DOOR_CLOSE, SoundEvents.IRON_DOOR_OPEN, SoundEvents.IRON_TRAPDOOR_CLOSE, SoundEvents.IRON_TRAPDOOR_OPEN, SoundEvents.METAL_PRESSURE_PLATE_CLICK_OFF, SoundEvents.METAL_PRESSURE_PLATE_CLICK_ON, SoundEvents.STONE_BUTTON_CLICK_OFF, SoundEvents.STONE_BUTTON_CLICK_ON));

    public static final BlockSetType GOLD = register(new BlockSetType("gold", false, SoundType.METAL, SoundEvents.IRON_DOOR_CLOSE, SoundEvents.IRON_DOOR_OPEN, SoundEvents.IRON_TRAPDOOR_CLOSE, SoundEvents.IRON_TRAPDOOR_OPEN, SoundEvents.METAL_PRESSURE_PLATE_CLICK_OFF, SoundEvents.METAL_PRESSURE_PLATE_CLICK_ON, SoundEvents.STONE_BUTTON_CLICK_OFF, SoundEvents.STONE_BUTTON_CLICK_ON));

    public static final BlockSetType STONE = register(new BlockSetType("stone", true, SoundType.STONE, SoundEvents.IRON_DOOR_CLOSE, SoundEvents.IRON_DOOR_OPEN, SoundEvents.IRON_TRAPDOOR_CLOSE, SoundEvents.IRON_TRAPDOOR_OPEN, SoundEvents.STONE_PRESSURE_PLATE_CLICK_OFF, SoundEvents.STONE_PRESSURE_PLATE_CLICK_ON, SoundEvents.STONE_BUTTON_CLICK_OFF, SoundEvents.STONE_BUTTON_CLICK_ON));

    public static final BlockSetType POLISHED_BLACKSTONE = register(new BlockSetType("polished_blackstone", true, SoundType.STONE, SoundEvents.IRON_DOOR_CLOSE, SoundEvents.IRON_DOOR_OPEN, SoundEvents.IRON_TRAPDOOR_CLOSE, SoundEvents.IRON_TRAPDOOR_OPEN, SoundEvents.STONE_PRESSURE_PLATE_CLICK_OFF, SoundEvents.STONE_PRESSURE_PLATE_CLICK_ON, SoundEvents.STONE_BUTTON_CLICK_OFF, SoundEvents.STONE_BUTTON_CLICK_ON));

    public static final BlockSetType OAK = register(new BlockSetType("oak"));

    public static final BlockSetType SPRUCE = register(new BlockSetType("spruce"));

    public static final BlockSetType BIRCH = register(new BlockSetType("birch"));

    public static final BlockSetType ACACIA = register(new BlockSetType("acacia"));

    public static final BlockSetType CHERRY = register(new BlockSetType("cherry", true, SoundType.CHERRY_WOOD, SoundEvents.CHERRY_WOOD_DOOR_CLOSE, SoundEvents.CHERRY_WOOD_DOOR_OPEN, SoundEvents.CHERRY_WOOD_TRAPDOOR_CLOSE, SoundEvents.CHERRY_WOOD_TRAPDOOR_OPEN, SoundEvents.CHERRY_WOOD_PRESSURE_PLATE_CLICK_OFF, SoundEvents.CHERRY_WOOD_PRESSURE_PLATE_CLICK_ON, SoundEvents.CHERRY_WOOD_BUTTON_CLICK_OFF, SoundEvents.CHERRY_WOOD_BUTTON_CLICK_ON));

    public static final BlockSetType JUNGLE = register(new BlockSetType("jungle"));

    public static final BlockSetType DARK_OAK = register(new BlockSetType("dark_oak"));

    public static final BlockSetType CRIMSON = register(new BlockSetType("crimson", true, SoundType.NETHER_WOOD, SoundEvents.NETHER_WOOD_DOOR_CLOSE, SoundEvents.NETHER_WOOD_DOOR_OPEN, SoundEvents.NETHER_WOOD_TRAPDOOR_CLOSE, SoundEvents.NETHER_WOOD_TRAPDOOR_OPEN, SoundEvents.NETHER_WOOD_PRESSURE_PLATE_CLICK_OFF, SoundEvents.NETHER_WOOD_PRESSURE_PLATE_CLICK_ON, SoundEvents.NETHER_WOOD_BUTTON_CLICK_OFF, SoundEvents.NETHER_WOOD_BUTTON_CLICK_ON));

    public static final BlockSetType WARPED = register(new BlockSetType("warped", true, SoundType.NETHER_WOOD, SoundEvents.NETHER_WOOD_DOOR_CLOSE, SoundEvents.NETHER_WOOD_DOOR_OPEN, SoundEvents.NETHER_WOOD_TRAPDOOR_CLOSE, SoundEvents.NETHER_WOOD_TRAPDOOR_OPEN, SoundEvents.NETHER_WOOD_PRESSURE_PLATE_CLICK_OFF, SoundEvents.NETHER_WOOD_PRESSURE_PLATE_CLICK_ON, SoundEvents.NETHER_WOOD_BUTTON_CLICK_OFF, SoundEvents.NETHER_WOOD_BUTTON_CLICK_ON));

    public static final BlockSetType MANGROVE = register(new BlockSetType("mangrove"));

    public static final BlockSetType BAMBOO = register(new BlockSetType("bamboo", true, SoundType.BAMBOO_WOOD, SoundEvents.BAMBOO_WOOD_DOOR_CLOSE, SoundEvents.BAMBOO_WOOD_DOOR_OPEN, SoundEvents.BAMBOO_WOOD_TRAPDOOR_CLOSE, SoundEvents.BAMBOO_WOOD_TRAPDOOR_OPEN, SoundEvents.BAMBOO_WOOD_PRESSURE_PLATE_CLICK_OFF, SoundEvents.BAMBOO_WOOD_PRESSURE_PLATE_CLICK_ON, SoundEvents.BAMBOO_WOOD_BUTTON_CLICK_OFF, SoundEvents.BAMBOO_WOOD_BUTTON_CLICK_ON));

    public BlockSetType(String p_272860_) {
        this(p_272860_, true, SoundType.WOOD, SoundEvents.WOODEN_DOOR_CLOSE, SoundEvents.WOODEN_DOOR_OPEN, SoundEvents.WOODEN_TRAPDOOR_CLOSE, SoundEvents.WOODEN_TRAPDOOR_OPEN, SoundEvents.WOODEN_PRESSURE_PLATE_CLICK_OFF, SoundEvents.WOODEN_PRESSURE_PLATE_CLICK_ON, SoundEvents.WOODEN_BUTTON_CLICK_OFF, SoundEvents.WOODEN_BUTTON_CLICK_ON);
    }

    public BlockSetType(String f_271253_, boolean f_278463_, SoundType f_271136_, SoundEvent f_271502_, SoundEvent f_271141_, SoundEvent f_271425_, SoundEvent f_271258_, SoundEvent f_271234_, SoundEvent f_271481_, SoundEvent f_271194_, SoundEvent f_271394_) {
        this.name = f_271253_;
        this.canOpenByHand = f_278463_;
        this.soundType = f_271136_;
        this.doorClose = f_271502_;
        this.doorOpen = f_271141_;
        this.trapdoorClose = f_271425_;
        this.trapdoorOpen = f_271258_;
        this.pressurePlateClickOff = f_271234_;
        this.pressurePlateClickOn = f_271481_;
        this.buttonClickOff = f_271194_;
        this.buttonClickOn = f_271394_;
    }

    private static BlockSetType register(BlockSetType p_273033_) {
        VALUES.add(p_273033_);
        return p_273033_;
    }

    public static Stream<BlockSetType> values() {
        return VALUES.stream();
    }
}
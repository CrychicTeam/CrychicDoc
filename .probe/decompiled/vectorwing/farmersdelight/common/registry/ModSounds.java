package vectorwing.farmersdelight.common.registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSounds {

    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, "farmersdelight");

    public static final RegistryObject<SoundEvent> BLOCK_STOVE_CRACKLE = SOUNDS.register("block.stove.crackle", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation("farmersdelight", "block.stove.crackle")));

    public static final RegistryObject<SoundEvent> BLOCK_COOKING_POT_BOIL = SOUNDS.register("block.cooking_pot.boil", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation("farmersdelight", "block.cooking_pot.boil")));

    public static final RegistryObject<SoundEvent> BLOCK_COOKING_POT_BOIL_SOUP = SOUNDS.register("block.cooking_pot.boil_soup", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation("farmersdelight", "block.cooking_pot.boil_soup")));

    public static final RegistryObject<SoundEvent> BLOCK_CUTTING_BOARD_KNIFE = SOUNDS.register("block.cutting_board.knife", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation("farmersdelight", "block.cutting_board.knife")));

    public static final RegistryObject<SoundEvent> BLOCK_CABINET_OPEN = SOUNDS.register("block.cabinet.open", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation("farmersdelight", "block.cabinet.open")));

    public static final RegistryObject<SoundEvent> BLOCK_CABINET_CLOSE = SOUNDS.register("block.cabinet.close", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation("farmersdelight", "block.cabinet.close")));

    public static final RegistryObject<SoundEvent> BLOCK_SKILLET_SIZZLE = SOUNDS.register("block.skillet.sizzle", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation("farmersdelight", "block.skillet.sizzle")));

    public static final RegistryObject<SoundEvent> BLOCK_SKILLET_ADD_FOOD = SOUNDS.register("block.skillet.add_food", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation("farmersdelight", "block.skillet.add_food")));

    public static final RegistryObject<SoundEvent> ITEM_SKILLET_ATTACK_STRONG = SOUNDS.register("item.skillet.attack.strong", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation("farmersdelight", "item.skillet.attack.strong")));

    public static final RegistryObject<SoundEvent> ITEM_SKILLET_ATTACK_WEAK = SOUNDS.register("item.skillet.attack.weak", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation("farmersdelight", "item.skillet.attack.weak")));

    public static final RegistryObject<SoundEvent> ITEM_TOMATO_PICK_FROM_BUSH = SOUNDS.register("block.tomato_bush.pick_tomatoes", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation("farmersdelight", "block.tomato_bush.pick_tomatoes")));

    public static final RegistryObject<SoundEvent> ENTITY_ROTTEN_TOMATO_THROW = SOUNDS.register("entity.rotten_tomato.throw", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation("farmersdelight", "entity.rotten_tomato.throw")));

    public static final RegistryObject<SoundEvent> ENTITY_ROTTEN_TOMATO_HIT = SOUNDS.register("entity.rotten_tomato.hit", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation("farmersdelight", "entity.rotten_tomato.hit")));
}
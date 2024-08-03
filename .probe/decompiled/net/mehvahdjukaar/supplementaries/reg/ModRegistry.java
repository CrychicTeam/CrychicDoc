package net.mehvahdjukaar.supplementaries.reg;

import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import net.mehvahdjukaar.moonlight.api.item.WoodBasedBlockItem;
import net.mehvahdjukaar.moonlight.api.misc.RegSupplier;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.mehvahdjukaar.moonlight.api.set.wood.WoodType;
import net.mehvahdjukaar.supplementaries.Supplementaries;
import net.mehvahdjukaar.supplementaries.common.block.blocks.AshLayerBlock;
import net.mehvahdjukaar.supplementaries.common.block.blocks.AshenBasaltBlock;
import net.mehvahdjukaar.supplementaries.common.block.blocks.BambooSpikesBlock;
import net.mehvahdjukaar.supplementaries.common.block.blocks.BellowsBlock;
import net.mehvahdjukaar.supplementaries.common.block.blocks.BlackboardBlock;
import net.mehvahdjukaar.supplementaries.common.block.blocks.BlazeRodBlock;
import net.mehvahdjukaar.supplementaries.common.block.blocks.BlockGeneratorBlock;
import net.mehvahdjukaar.supplementaries.common.block.blocks.BookPileBlock;
import net.mehvahdjukaar.supplementaries.common.block.blocks.BookPileHorizontalBlock;
import net.mehvahdjukaar.supplementaries.common.block.blocks.BubbleBlock;
import net.mehvahdjukaar.supplementaries.common.block.blocks.CageBlock;
import net.mehvahdjukaar.supplementaries.common.block.blocks.ClockBlock;
import net.mehvahdjukaar.supplementaries.common.block.blocks.CogBlock;
import net.mehvahdjukaar.supplementaries.common.block.blocks.CrankBlock;
import net.mehvahdjukaar.supplementaries.common.block.blocks.CrystalDisplayBlock;
import net.mehvahdjukaar.supplementaries.common.block.blocks.DoormatBlock;
import net.mehvahdjukaar.supplementaries.common.block.blocks.EndLampBlock;
import net.mehvahdjukaar.supplementaries.common.block.blocks.EndermanSkullBlock;
import net.mehvahdjukaar.supplementaries.common.block.blocks.EndermanSkullWallBlock;
import net.mehvahdjukaar.supplementaries.common.block.blocks.FaucetBlock;
import net.mehvahdjukaar.supplementaries.common.block.blocks.FeatherBlock;
import net.mehvahdjukaar.supplementaries.common.block.blocks.FlaxBaleBlock;
import net.mehvahdjukaar.supplementaries.common.block.blocks.FlaxBlock;
import net.mehvahdjukaar.supplementaries.common.block.blocks.FlintBlock;
import net.mehvahdjukaar.supplementaries.common.block.blocks.FlippedBlock;
import net.mehvahdjukaar.supplementaries.common.block.blocks.FlowerBoxBlock;
import net.mehvahdjukaar.supplementaries.common.block.blocks.FodderBlock;
import net.mehvahdjukaar.supplementaries.common.block.blocks.FrameBlock;
import net.mehvahdjukaar.supplementaries.common.block.blocks.FrameBraceBlock;
import net.mehvahdjukaar.supplementaries.common.block.blocks.GlobeBlock;
import net.mehvahdjukaar.supplementaries.common.block.blocks.GobletBlock;
import net.mehvahdjukaar.supplementaries.common.block.blocks.GoldDoorBlock;
import net.mehvahdjukaar.supplementaries.common.block.blocks.GoldTrapdoorBlock;
import net.mehvahdjukaar.supplementaries.common.block.blocks.GunpowderBlock;
import net.mehvahdjukaar.supplementaries.common.block.blocks.HourGlassBlock;
import net.mehvahdjukaar.supplementaries.common.block.blocks.IronGateBlock;
import net.mehvahdjukaar.supplementaries.common.block.blocks.ItemShelfBlock;
import net.mehvahdjukaar.supplementaries.common.block.blocks.JarBlock;
import net.mehvahdjukaar.supplementaries.common.block.blocks.JarBoatBlock;
import net.mehvahdjukaar.supplementaries.common.block.blocks.LockBlock;
import net.mehvahdjukaar.supplementaries.common.block.blocks.NetheriteDoorBlock;
import net.mehvahdjukaar.supplementaries.common.block.blocks.NetheriteTrapdoorBlock;
import net.mehvahdjukaar.supplementaries.common.block.blocks.NoticeBoardBlock;
import net.mehvahdjukaar.supplementaries.common.block.blocks.PancakeBlock;
import net.mehvahdjukaar.supplementaries.common.block.blocks.PedestalBlock;
import net.mehvahdjukaar.supplementaries.common.block.blocks.PlanterBlock;
import net.mehvahdjukaar.supplementaries.common.block.blocks.PresentBlock;
import net.mehvahdjukaar.supplementaries.common.block.blocks.PulleyBlock;
import net.mehvahdjukaar.supplementaries.common.block.blocks.RakedGravelBlock;
import net.mehvahdjukaar.supplementaries.common.block.blocks.RedstoneIlluminatorBlock;
import net.mehvahdjukaar.supplementaries.common.block.blocks.RelayerBlock;
import net.mehvahdjukaar.supplementaries.common.block.blocks.RopeBlock;
import net.mehvahdjukaar.supplementaries.common.block.blocks.RopeKnotBlock;
import net.mehvahdjukaar.supplementaries.common.block.blocks.SackBlock;
import net.mehvahdjukaar.supplementaries.common.block.blocks.SafeBlock;
import net.mehvahdjukaar.supplementaries.common.block.blocks.SconceBlock;
import net.mehvahdjukaar.supplementaries.common.block.blocks.SconceLeverBlock;
import net.mehvahdjukaar.supplementaries.common.block.blocks.SconceWallBlock;
import net.mehvahdjukaar.supplementaries.common.block.blocks.SignPostBlock;
import net.mehvahdjukaar.supplementaries.common.block.blocks.SoapBlock;
import net.mehvahdjukaar.supplementaries.common.block.blocks.SpeakerBlock;
import net.mehvahdjukaar.supplementaries.common.block.blocks.SpringLauncherArmBlock;
import net.mehvahdjukaar.supplementaries.common.block.blocks.SpringLauncherBlock;
import net.mehvahdjukaar.supplementaries.common.block.blocks.SpringLauncherHeadBlock;
import net.mehvahdjukaar.supplementaries.common.block.blocks.StatueBlock;
import net.mehvahdjukaar.supplementaries.common.block.blocks.StickBlock;
import net.mehvahdjukaar.supplementaries.common.block.blocks.StructureTempBlock;
import net.mehvahdjukaar.supplementaries.common.block.blocks.SugarBlock;
import net.mehvahdjukaar.supplementaries.common.block.blocks.TrappedPresentBlock;
import net.mehvahdjukaar.supplementaries.common.block.blocks.TurnTableBlock;
import net.mehvahdjukaar.supplementaries.common.block.blocks.UrnBlock;
import net.mehvahdjukaar.supplementaries.common.block.blocks.WildFlaxBlock;
import net.mehvahdjukaar.supplementaries.common.block.blocks.WindVaneBlock;
import net.mehvahdjukaar.supplementaries.common.block.tiles.BambooSpikesBlockTile;
import net.mehvahdjukaar.supplementaries.common.block.tiles.BellowsBlockTile;
import net.mehvahdjukaar.supplementaries.common.block.tiles.BlackboardBlockTile;
import net.mehvahdjukaar.supplementaries.common.block.tiles.BlockGeneratorBlockTile;
import net.mehvahdjukaar.supplementaries.common.block.tiles.BookPileBlockTile;
import net.mehvahdjukaar.supplementaries.common.block.tiles.BubbleBlockTile;
import net.mehvahdjukaar.supplementaries.common.block.tiles.CageBlockTile;
import net.mehvahdjukaar.supplementaries.common.block.tiles.ClockBlockTile;
import net.mehvahdjukaar.supplementaries.common.block.tiles.DoormatBlockTile;
import net.mehvahdjukaar.supplementaries.common.block.tiles.EndermanSkullBlockTile;
import net.mehvahdjukaar.supplementaries.common.block.tiles.FaucetBlockTile;
import net.mehvahdjukaar.supplementaries.common.block.tiles.FlagBlockTile;
import net.mehvahdjukaar.supplementaries.common.block.tiles.FlowerBoxBlockTile;
import net.mehvahdjukaar.supplementaries.common.block.tiles.FrameBlockTile;
import net.mehvahdjukaar.supplementaries.common.block.tiles.GlobeBlockTile;
import net.mehvahdjukaar.supplementaries.common.block.tiles.GobletBlockTile;
import net.mehvahdjukaar.supplementaries.common.block.tiles.HourGlassBlockTile;
import net.mehvahdjukaar.supplementaries.common.block.tiles.ItemShelfBlockTile;
import net.mehvahdjukaar.supplementaries.common.block.tiles.JarBlockTile;
import net.mehvahdjukaar.supplementaries.common.block.tiles.JarBoatTile;
import net.mehvahdjukaar.supplementaries.common.block.tiles.KeyLockableTile;
import net.mehvahdjukaar.supplementaries.common.block.tiles.NoticeBoardBlockTile;
import net.mehvahdjukaar.supplementaries.common.block.tiles.PedestalBlockTile;
import net.mehvahdjukaar.supplementaries.common.block.tiles.PresentBlockTile;
import net.mehvahdjukaar.supplementaries.common.block.tiles.PulleyBlockTile;
import net.mehvahdjukaar.supplementaries.common.block.tiles.RopeKnotBlockTile;
import net.mehvahdjukaar.supplementaries.common.block.tiles.SackBlockTile;
import net.mehvahdjukaar.supplementaries.common.block.tiles.SafeBlockTile;
import net.mehvahdjukaar.supplementaries.common.block.tiles.SignPostBlockTile;
import net.mehvahdjukaar.supplementaries.common.block.tiles.SpeakerBlockTile;
import net.mehvahdjukaar.supplementaries.common.block.tiles.SpringLauncherArmBlockTile;
import net.mehvahdjukaar.supplementaries.common.block.tiles.StatueBlockTile;
import net.mehvahdjukaar.supplementaries.common.block.tiles.StructureTempBlockTile;
import net.mehvahdjukaar.supplementaries.common.block.tiles.TrappedPresentBlockTile;
import net.mehvahdjukaar.supplementaries.common.block.tiles.TurnTableBlockTile;
import net.mehvahdjukaar.supplementaries.common.block.tiles.UrnBlockTile;
import net.mehvahdjukaar.supplementaries.common.block.tiles.WindVaneBlockTile;
import net.mehvahdjukaar.supplementaries.common.entities.BombEntity;
import net.mehvahdjukaar.supplementaries.common.items.AltimeterItem;
import net.mehvahdjukaar.supplementaries.common.items.AntiqueInkItem;
import net.mehvahdjukaar.supplementaries.common.items.BambooSpikesTippedItem;
import net.mehvahdjukaar.supplementaries.common.items.BlackboardItem;
import net.mehvahdjukaar.supplementaries.common.items.BombItem;
import net.mehvahdjukaar.supplementaries.common.items.BubbleBlockItem;
import net.mehvahdjukaar.supplementaries.common.items.BubbleBlowerItem;
import net.mehvahdjukaar.supplementaries.common.items.CageItem;
import net.mehvahdjukaar.supplementaries.common.items.CandyItem;
import net.mehvahdjukaar.supplementaries.common.items.DispenserMinecartItem;
import net.mehvahdjukaar.supplementaries.common.items.EndermanHeadItem;
import net.mehvahdjukaar.supplementaries.common.items.FluteItem;
import net.mehvahdjukaar.supplementaries.common.items.HatStandItem;
import net.mehvahdjukaar.supplementaries.common.items.JarItem;
import net.mehvahdjukaar.supplementaries.common.items.KeyItem;
import net.mehvahdjukaar.supplementaries.common.items.PancakeItem;
import net.mehvahdjukaar.supplementaries.common.items.QuiverItem;
import net.mehvahdjukaar.supplementaries.common.items.RopeArrowItem;
import net.mehvahdjukaar.supplementaries.common.items.RopeItem;
import net.mehvahdjukaar.supplementaries.common.items.SackItem;
import net.mehvahdjukaar.supplementaries.common.items.SafeItem;
import net.mehvahdjukaar.supplementaries.common.items.SignPostItem;
import net.mehvahdjukaar.supplementaries.common.items.SliceMapItem;
import net.mehvahdjukaar.supplementaries.common.items.SlingshotItem;
import net.mehvahdjukaar.supplementaries.common.items.SoapItem;
import net.mehvahdjukaar.supplementaries.common.items.SugarCubeItem;
import net.mehvahdjukaar.supplementaries.common.items.TimberFrameItem;
import net.mehvahdjukaar.supplementaries.common.items.WrenchItem;
import net.mehvahdjukaar.supplementaries.common.items.loot.CurseLootFunction;
import net.mehvahdjukaar.supplementaries.common.items.loot.RandomArrowFunction;
import net.mehvahdjukaar.supplementaries.common.misc.OverencumberedEffect;
import net.mehvahdjukaar.supplementaries.common.misc.StasisEnchantment;
import net.mehvahdjukaar.supplementaries.configs.CommonConfigs;
import net.mehvahdjukaar.supplementaries.integration.CompatHandler;
import net.mehvahdjukaar.supplementaries.integration.FarmersDelightCompat;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;

public class ModRegistry {

    public static final Supplier<LootItemFunctionType> CURSE_LOOT_FUNCTION = RegHelper.register(Supplementaries.res("curse_loot"), () -> new LootItemFunctionType(new CurseLootFunction.Serializer()), Registries.LOOT_FUNCTION_TYPE);

    public static final Supplier<LootItemFunctionType> RANDOM_ARROW_FUNCTION = RegHelper.register(Supplementaries.res("random_arrows"), () -> new LootItemFunctionType(new RandomArrowFunction.Serializer()), Registries.LOOT_FUNCTION_TYPE);

    public static final Supplier<PaintingVariant> BOMB_PAINTING = RegHelper.registerPainting(Supplementaries.res("bombs"), () -> new PaintingVariant(32, 32));

    public static final Supplier<Enchantment> STASIS_ENCHANTMENT = RegHelper.registerAsync(Supplementaries.res("stasis"), StasisEnchantment::new, Registries.ENCHANTMENT);

    public static final Supplier<MobEffect> OVERENCUMBERED = RegHelper.registerEffect(Supplementaries.res("overencumbered"), OverencumberedEffect::new);

    public static final Supplier<Item> RED_MERCHANT_SPAWN_EGG_ITEM = RegUtils.regItem("red_merchant_spawn_egg", () -> PlatHelper.newSpawnEgg(ModEntities.RED_MERCHANT, 7997711, 16052704, new Item.Properties()));

    public static final Supplier<Item> DISPENSER_MINECART_ITEM = RegUtils.regItem("dispenser_minecart", () -> new DispenserMinecartItem(new Item.Properties().stacksTo(1)));

    public static final Supplier<Item> BOMB_ITEM = RegUtils.regItem("bomb", () -> new BombItem(new Item.Properties()));

    public static final Supplier<Item> BOMB_ITEM_ON = RegUtils.regItem("bomb_projectile", () -> new BombItem(new Item.Properties()));

    public static final Supplier<Item> BOMB_BLUE_ITEM = RegUtils.regItem("bomb_blue", () -> new BombItem(new Item.Properties(), BombEntity.BombType.BLUE, true));

    public static final Supplier<Item> BOMB_BLUE_ITEM_ON = RegUtils.regItem("bomb_blue_projectile", () -> new BombItem(new Item.Properties(), BombEntity.BombType.BLUE, false));

    public static final Supplier<Item> BOMB_SPIKY_ITEM = RegUtils.regItem("bomb_spiky", () -> new BombItem(new Item.Properties(), BombEntity.BombType.SPIKY, false));

    public static final Supplier<Item> BOMB_SPIKY_ITEM_ON = RegUtils.regItem("bomb_spiky_projectile", () -> new BombItem(new Item.Properties(), BombEntity.BombType.SPIKY, false));

    public static final Supplier<Item> ROPE_ARROW_ITEM = RegUtils.regItem("rope_arrow", () -> new RopeArrowItem(new Item.Properties().defaultDurability((Integer) CommonConfigs.Tools.ROPE_ARROW_CAPACITY.get())));

    public static final Supplier<Item> BUBBLE_BLOWER = RegUtils.regItem("bubble_blower", () -> new BubbleBlowerItem(new Item.Properties().stacksTo(1).durability(250)));

    public static final Supplier<Item> SLINGSHOT_ITEM = RegUtils.regItem("slingshot", () -> new SlingshotItem(new Item.Properties().stacksTo(1).durability(192)));

    public static final Supplier<Item> FLUTE_ITEM = RegUtils.regItem("flute", () -> new FluteItem(new Item.Properties().stacksTo(1).durability(64)));

    public static final Supplier<KeyItem> KEY_ITEM = RegUtils.regItem("key", () -> new KeyItem(new Item.Properties()));

    public static final Supplier<Item> CANDY_ITEM = RegUtils.regItem("candy", () -> new CandyItem(new Item.Properties()));

    public static final Supplier<Item> ANTIQUE_INK = RegUtils.regItem("antique_ink", () -> new AntiqueInkItem(new Item.Properties()));

    public static final Supplier<Item> WRENCH = RegUtils.regItem("wrench", () -> new WrenchItem(new Item.Properties().stacksTo(1).durability(200)));

    public static final Supplier<QuiverItem> QUIVER_ITEM = RegUtils.regItem("quiver", () -> new QuiverItem(new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));

    public static final Supplier<Item> DEPTH_METER_ITEM = RegUtils.regItem("altimeter", () -> new AltimeterItem(new Item.Properties()));

    public static final Supplier<Item> SLICE_MAP = RegUtils.regItem("slice_map", () -> new SliceMapItem(new Item.Properties()));

    public static final Supplier<Block> SIGN_POST = RegUtils.regBlock("sign_post", () -> {
        BlockBehaviour.Properties p = BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS).strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion();
        return new SignPostBlock(p);
    });

    public static final Supplier<BlockEntityType<SignPostBlockTile>> SIGN_POST_TILE = RegUtils.regTile("sign_post", () -> PlatHelper.newBlockEntityType(SignPostBlockTile::new, (Block) SIGN_POST.get()));

    public static final Map<WoodType, SignPostItem> SIGN_POST_ITEMS = new Object2ObjectLinkedOpenHashMap();

    public static final Map<DyeColor, Supplier<Block>> FLAGS = RegUtils.registerFlags("flag");

    public static final Supplier<BlockEntityType<FlagBlockTile>> FLAG_TILE = RegUtils.regTile("flag", () -> PlatHelper.newBlockEntityType(FlagBlockTile::new, (Block[]) FLAGS.values().stream().map(Supplier::get).toArray(Block[]::new)));

    public static final Map<DyeColor, Supplier<Block>> PRESENTS = RegUtils.registerPresents("present", PresentBlock::new);

    public static final Supplier<BlockEntityType<PresentBlockTile>> PRESENT_TILE = RegUtils.regTile("present", () -> PlatHelper.newBlockEntityType(PresentBlockTile::new, (Block[]) PRESENTS.values().stream().map(Supplier::get).toArray(Block[]::new)));

    public static final Map<DyeColor, Supplier<Block>> TRAPPED_PRESENTS = RegUtils.registerPresents("trapped_present", TrappedPresentBlock::new);

    public static final Supplier<BlockEntityType<TrappedPresentBlockTile>> TRAPPED_PRESENT_TILE = RegUtils.regTile("trapped_present", () -> PlatHelper.newBlockEntityType(TrappedPresentBlockTile::new, (Block[]) TRAPPED_PRESENTS.values().stream().map(Supplier::get).toArray(Block[]::new)));

    public static final Supplier<PlanterBlock> PLANTER = RegUtils.regWithItem("planter", () -> CompatHandler.FARMERS_DELIGHT ? FarmersDelightCompat.makePlanterRich() : new PlanterBlock(BlockBehaviour.Properties.copy(Blocks.TERRACOTTA).mapColor(MapColor.TERRACOTTA_RED).strength(2.0F, 6.0F)));

    public static final Supplier<Block> PEDESTAL = RegUtils.regWithItem("pedestal", () -> new PedestalBlock(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS)));

    public static final Supplier<BlockEntityType<PedestalBlockTile>> PEDESTAL_TILE = RegUtils.regTile("pedestal", () -> PlatHelper.newBlockEntityType(PedestalBlockTile::new, (Block) PEDESTAL.get()));

    public static final Supplier<Block> NOTICE_BOARD = RegUtils.regWithItem("notice_board", () -> new NoticeBoardBlock(BlockBehaviour.Properties.copy(Blocks.BARREL)), 300);

    public static final Supplier<BlockEntityType<NoticeBoardBlockTile>> NOTICE_BOARD_TILE = RegUtils.regTile("notice_board", () -> PlatHelper.newBlockEntityType(NoticeBoardBlockTile::new, (Block) NOTICE_BOARD.get()));

    public static final Supplier<Block> SAFE = RegUtils.regBlock("safe", () -> new SafeBlock(BlockBehaviour.Properties.copy(Blocks.NETHERITE_BLOCK).pushReaction(PushReaction.BLOCK)));

    public static final Supplier<BlockEntityType<SafeBlockTile>> SAFE_TILE = RegUtils.regTile("safe", () -> PlatHelper.newBlockEntityType(SafeBlockTile::new, (Block) SAFE.get()));

    public static final Supplier<Item> SAFE_ITEM = RegUtils.regItem("safe", () -> new SafeItem((Block) SAFE.get(), new Item.Properties().stacksTo(1).fireResistant()));

    public static final Supplier<Block> CAGE = RegUtils.regBlock("cage", () -> new CageBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).pushReaction(PushReaction.DESTROY).strength(3.0F, 6.0F).sound(SoundType.METAL)));

    public static final Supplier<BlockEntityType<CageBlockTile>> CAGE_TILE = RegUtils.regTile("cage", () -> PlatHelper.newBlockEntityType(CageBlockTile::new, (Block) CAGE.get()));

    public static final Supplier<Item> CAGE_ITEM = RegUtils.regItem("cage", () -> new CageItem((Block) CAGE.get(), new Item.Properties().stacksTo(16)));

    public static final Supplier<Block> JAR = RegUtils.regBlock("jar", () -> new JarBlock(BlockBehaviour.Properties.copy(Blocks.GLASS).pushReaction(PushReaction.DESTROY).strength(0.5F, 1.0F).sound(ModSounds.JAR).noOcclusion()));

    public static final Supplier<BlockEntityType<JarBlockTile>> JAR_TILE = RegUtils.regTile("jar", () -> PlatHelper.newBlockEntityType(JarBlockTile::new, (Block) JAR.get()));

    public static final Supplier<Item> JAR_ITEM = RegUtils.regItem("jar", () -> new JarItem((Block) JAR.get(), new Item.Properties().stacksTo(16)));

    public static final Supplier<Block> SACK = RegUtils.regBlock("sack", () -> new SackBlock(BlockBehaviour.Properties.copy(Blocks.WHITE_WOOL).mapColor(MapColor.WOOD).pushReaction(PushReaction.DESTROY).strength(0.8F).sound(ModSounds.SACK)));

    public static final Supplier<BlockEntityType<SackBlockTile>> SACK_TILE = RegUtils.regTile("sack", () -> PlatHelper.newBlockEntityType(SackBlockTile::new, (Block[]) SackBlock.SACK_BLOCKS.toArray(Block[]::new)));

    public static final Supplier<Item> SACK_ITEM = RegUtils.regItem("sack", () -> new SackItem((Block) SACK.get(), new Item.Properties().stacksTo(1)));

    public static final Supplier<Block> BLACKBOARD = RegUtils.regBlock("blackboard", () -> new BlackboardBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLACK).strength(2.0F, 3.0F)));

    public static final Supplier<BlockEntityType<BlackboardBlockTile>> BLACKBOARD_TILE = RegUtils.regTile("blackboard", () -> PlatHelper.newBlockEntityType(BlackboardBlockTile::new, (Block) BLACKBOARD.get()));

    public static final Supplier<Item> BLACKBOARD_ITEM = RegUtils.regItem("blackboard", () -> new BlackboardItem((Block) BLACKBOARD.get(), new Item.Properties()));

    public static final Supplier<Block> GLOBE = RegUtils.regBlock("globe", () -> new GlobeBlock(BlockBehaviour.Properties.of().noOcclusion().mapColor(MapColor.TERRACOTTA_ORANGE).sound(SoundType.METAL).strength(2.0F, 4.0F).requiresCorrectToolForDrops()));

    public static final Supplier<Item> GLOBE_ITEM = RegUtils.regItem("globe", () -> new BlockItem((Block) GLOBE.get(), new Item.Properties().rarity(Rarity.RARE)));

    public static final Supplier<Block> GLOBE_SEPIA = RegUtils.regBlock("globe_sepia", () -> new GlobeBlock(BlockBehaviour.Properties.copy((BlockBehaviour) GLOBE.get())));

    public static final Supplier<Item> GLOBE_SEPIA_ITEM = RegUtils.regItem("globe_sepia", () -> new BlockItem((Block) GLOBE_SEPIA.get(), new Item.Properties().rarity(Rarity.RARE)));

    public static final Supplier<BlockEntityType<GlobeBlockTile>> GLOBE_TILE = RegUtils.regTile("globe", () -> PlatHelper.newBlockEntityType(GlobeBlockTile::new, (Block) GLOBE.get(), (Block) GLOBE_SEPIA.get()));

    public static final Supplier<Block> SCONCE = RegUtils.regBlock("sconce", () -> new SconceBlock(BlockBehaviour.Properties.of().noCollission().pushReaction(PushReaction.DESTROY).instabreak().sound(SoundType.LANTERN), 14, () -> ParticleTypes.FLAME));

    public static final Supplier<Block> SCONCE_WALL = RegUtils.regBlock("sconce_wall", () -> new SconceWallBlock(BlockBehaviour.Properties.copy((BlockBehaviour) SCONCE.get()).dropsLike((Block) SCONCE.get()), () -> ParticleTypes.FLAME));

    public static final Supplier<Item> SCONCE_ITEM = RegUtils.regItem("sconce", () -> new StandingAndWallBlockItem((Block) SCONCE.get(), (Block) SCONCE_WALL.get(), new Item.Properties(), Direction.DOWN));

    public static final Supplier<Block> SCONCE_SOUL = RegUtils.regBlock("sconce_soul", () -> new SconceBlock(BlockBehaviour.Properties.copy((BlockBehaviour) SCONCE.get()), 10, () -> ParticleTypes.SOUL_FIRE_FLAME));

    public static final Supplier<Block> SCONCE_WALL_SOUL = RegUtils.regBlock("sconce_wall_soul", () -> new SconceWallBlock(BlockBehaviour.Properties.copy((BlockBehaviour) SCONCE_SOUL.get()).dropsLike((Block) SCONCE_SOUL.get()), () -> ParticleTypes.SOUL_FIRE_FLAME));

    public static final Supplier<Item> SCONCE_ITEM_SOUL = RegUtils.regItem("sconce_soul", () -> new StandingAndWallBlockItem((Block) SCONCE_SOUL.get(), (Block) SCONCE_WALL_SOUL.get(), new Item.Properties(), Direction.DOWN));

    public static final List<Supplier<Item>> SCONCES = new ArrayList(List.of(SCONCE_ITEM, SCONCE_ITEM_SOUL));

    public static final Supplier<Block> SCONCE_GREEN = RegUtils.regBlock("sconce_green", () -> new SconceBlock(BlockBehaviour.Properties.copy((BlockBehaviour) SCONCE.get()), 14, ModParticles.GREEN_FLAME));

    public static final Supplier<Block> SCONCE_WALL_GREEN = RegUtils.regBlock("sconce_wall_green", () -> new SconceWallBlock(BlockBehaviour.Properties.copy((BlockBehaviour) SCONCE.get()).dropsLike((Block) SCONCE_GREEN.get()), ModParticles.GREEN_FLAME));

    public static final Supplier<Item> SCONCE_ITEM_GREEN = RegUtils.regItem("sconce_green", () -> new StandingAndWallBlockItem((Block) SCONCE_GREEN.get(), (Block) SCONCE_WALL_GREEN.get(), new Item.Properties(), Direction.DOWN));

    public static final Map<DyeColor, Supplier<Block>> CANDLE_HOLDERS = RegUtils.registerCandleHolders(Supplementaries.res("candle_holder"));

    public static final Supplier<RopeBlock> ROPE = RegUtils.regBlock("rope", () -> new RopeBlock(BlockBehaviour.Properties.copy(Blocks.BROWN_WOOL).sound(ModSounds.ROPE).strength(0.25F).speedFactor(0.7F).noOcclusion()));

    public static final Supplier<Block> ROPE_KNOT = RegUtils.regBlock("rope_knot", () -> new RopeKnotBlock(BlockBehaviour.Properties.copy(Blocks.OAK_FENCE).dynamicShape()));

    public static final Supplier<Item> ROPE_ITEM = RegUtils.regItem("rope", () -> new RopeItem((Block) ROPE.get(), new Item.Properties()));

    public static final Supplier<BlockEntityType<RopeKnotBlockTile>> ROPE_KNOT_TILE = RegUtils.regTile("rope_knot", () -> PlatHelper.newBlockEntityType(RopeKnotBlockTile::new, (Block) ROPE_KNOT.get()));

    public static final Supplier<Block> BAMBOO_SPIKES = RegUtils.regBlock("bamboo_spikes", () -> new BambooSpikesBlock(BlockBehaviour.Properties.of().pushReaction(PushReaction.NORMAL).mapColor(MapColor.SAND).ignitedByLava().sound(SoundType.SCAFFOLDING).isRedstoneConductor((a, b, c) -> false).strength(2.0F).noOcclusion()));

    public static final Supplier<BlockEntityType<BambooSpikesBlockTile>> BAMBOO_SPIKES_TILE = RegUtils.regTile("bamboo_spikes", () -> PlatHelper.newBlockEntityType(BambooSpikesBlockTile::new, (Block) BAMBOO_SPIKES.get()));

    public static final Supplier<Item> BAMBOO_SPIKES_ITEM = RegUtils.regItem("bamboo_spikes", () -> new WoodBasedBlockItem((Block) BAMBOO_SPIKES.get(), new Item.Properties(), 150));

    public static final Supplier<Item> BAMBOO_SPIKES_TIPPED_ITEM = RegUtils.regItem("bamboo_spikes_tipped", () -> new BambooSpikesTippedItem((Block) BAMBOO_SPIKES.get(), new Item.Properties().defaultDurability(16)));

    public static final Supplier<Block> GOBLET = RegUtils.regWithItem("goblet", () -> new GobletBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BARS).pushReaction(PushReaction.DESTROY).strength(1.5F, 2.0F).sound(SoundType.METAL)));

    public static final Supplier<BlockEntityType<GobletBlockTile>> GOBLET_TILE = RegUtils.regTile("goblet", () -> PlatHelper.newBlockEntityType(GobletBlockTile::new, (Block) GOBLET.get()));

    public static final Supplier<Block> HOURGLASS = RegUtils.regWithItem("hourglass", () -> new HourGlassBlock(BlockBehaviour.Properties.copy(Blocks.GOLD_BLOCK).sound(SoundType.METAL).strength(2.0F, 4.0F).requiresCorrectToolForDrops()));

    public static final Supplier<BlockEntityType<HourGlassBlockTile>> HOURGLASS_TILE = RegUtils.regTile("hourglass", () -> PlatHelper.newBlockEntityType(HourGlassBlockTile::new, (Block) HOURGLASS.get()));

    public static final Supplier<Block> ITEM_SHELF = RegUtils.regWithItem("item_shelf", () -> new ItemShelfBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS).sound(SoundType.WOOD).strength(0.75F, 0.1F).noOcclusion().noCollission()), 100);

    public static final Supplier<BlockEntityType<ItemShelfBlockTile>> ITEM_SHELF_TILE = RegUtils.regTile("item_shelf", () -> PlatHelper.newBlockEntityType(ItemShelfBlockTile::new, (Block[]) ItemShelfBlock.ITEM_SHELF_BLOCKS.toArray(Block[]::new)));

    public static final Supplier<Block> DOORMAT = RegUtils.regWithItem("doormat", () -> new DoormatBlock(BlockBehaviour.Properties.copy(Blocks.BROWN_CARPET).mapColor(MapColor.WOOD).strength(0.1F).noOcclusion()), 134);

    public static final Supplier<BlockEntityType<DoormatBlockTile>> DOORMAT_TILE = RegUtils.regTile("doormat", () -> PlatHelper.newBlockEntityType(DoormatBlockTile::new, (Block) DOORMAT.get()));

    public static final Supplier<Block> RAKED_GRAVEL = RegUtils.regWithItem("raked_gravel", () -> new RakedGravelBlock(BlockBehaviour.Properties.copy(Blocks.GRAVEL).isViewBlocking((w, s, p) -> true).isSuffocating((w, s, p) -> true)));

    public static final Supplier<Block> COG_BLOCK = RegUtils.regWithItem("cog_block", () -> new CogBlock(BlockBehaviour.Properties.copy(Blocks.COPPER_BLOCK).strength(3.0F, 6.0F).sound(SoundType.COPPER).requiresCorrectToolForDrops()));

    public static final Supplier<Block> RELAYER = RegUtils.regWithItem("relayer", () -> new RelayerBlock(BlockBehaviour.Properties.copy(Blocks.OBSERVER).isRedstoneConductor((s, l, p) -> false)));

    public static final Supplier<Block> SPRING_LAUNCHER = RegUtils.regWithItem("spring_launcher", () -> new SpringLauncherBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(4.0F, 5.0F).sound(SoundType.METAL).pushReaction(PushReaction.BLOCK).requiresCorrectToolForDrops().isRedstoneConductor((state, reader, pos) -> !(Boolean) state.m_61143_(SpringLauncherBlock.EXTENDED)).isSuffocating((state, reader, pos) -> !(Boolean) state.m_61143_(SpringLauncherBlock.EXTENDED)).isViewBlocking((state, reader, pos) -> !(Boolean) state.m_61143_(SpringLauncherBlock.EXTENDED))));

    public static final Supplier<Block> SPRING_LAUNCHER_HEAD = RegUtils.regBlock("spring_launcher_head", () -> new SpringLauncherHeadBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(4.0F, 5.0F).pushReaction(PushReaction.BLOCK).sound(SoundType.METAL).requiresCorrectToolForDrops().noLootTable().jumpFactor(1.18F)));

    public static final Supplier<Block> SPRING_LAUNCHER_ARM = RegUtils.regBlock("spring_launcher_arm", () -> new SpringLauncherArmBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(50.0F, 50.0F).sound(SoundType.METAL).noOcclusion().noLootTable()));

    public static final Supplier<BlockEntityType<SpringLauncherArmBlockTile>> SPRING_LAUNCHER_ARM_TILE = RegUtils.regTile("spring_launcher_arm", () -> PlatHelper.newBlockEntityType(SpringLauncherArmBlockTile::new, (Block) SPRING_LAUNCHER_ARM.get()));

    public static final Supplier<SpeakerBlock> SPEAKER_BLOCK = RegUtils.regWithItem("speaker_block", () -> new SpeakerBlock(BlockBehaviour.Properties.copy(Blocks.NOTE_BLOCK).strength(1.0F, 2.0F).sound(SoundType.WOOD)), 300);

    public static final Supplier<BlockEntityType<SpeakerBlockTile>> SPEAKER_BLOCK_TILE = RegUtils.regTile("speaker_block", () -> PlatHelper.newBlockEntityType(SpeakerBlockTile::new, (Block) SPEAKER_BLOCK.get()));

    public static final Supplier<Block> TURN_TABLE = RegUtils.regWithItem("turn_table", () -> new TurnTableBlock(BlockBehaviour.Properties.copy(Blocks.STONE).strength(0.75F, 2.0F)));

    public static final Supplier<BlockEntityType<TurnTableBlockTile>> TURN_TABLE_TILE = RegUtils.regTile("turn_table", () -> PlatHelper.newBlockEntityType(TurnTableBlockTile::new, (Block) TURN_TABLE.get()));

    public static final Supplier<Block> REDSTONE_ILLUMINATOR = RegUtils.regWithItem("redstone_illuminator", () -> new RedstoneIlluminatorBlock(BlockBehaviour.Properties.copy(Blocks.SEA_LANTERN).isValidSpawn((s, w, p, g) -> true).strength(0.3F, 0.3F)));

    public static final Supplier<Block> PULLEY_BLOCK = RegUtils.regWithItem("pulley_block", () -> new PulleyBlock(BlockBehaviour.Properties.copy(Blocks.BARREL)), 300);

    public static final Supplier<BlockEntityType<PulleyBlockTile>> PULLEY_BLOCK_TILE = RegUtils.regTile("pulley_block", () -> PlatHelper.newBlockEntityType(PulleyBlockTile::new, (Block) PULLEY_BLOCK.get()));

    public static final Supplier<Block> LOCK_BLOCK = RegUtils.regWithItem("lock_block", () -> new LockBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).requiresCorrectToolForDrops().strength(5.0F).isRedstoneConductor((blockState, blockGetter, blockPos) -> false).sound(SoundType.METAL)));

    public static final Supplier<Block> BELLOWS = RegUtils.regWithItem("bellows", () -> new BellowsBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS).mapColor(MapColor.COLOR_BROWN).dynamicShape().strength(3.0F, 3.0F).sound(SoundType.WOOD).noOcclusion()), 300);

    public static final Supplier<BlockEntityType<BellowsBlockTile>> BELLOWS_TILE = RegUtils.regTile("bellows", () -> PlatHelper.newBlockEntityType(BellowsBlockTile::new, (Block) BELLOWS.get()));

    public static final Supplier<Block> CLOCK_BLOCK = RegUtils.regWithItem("clock_block", () -> new ClockBlock(BlockBehaviour.Properties.copy(Blocks.DARK_OAK_PLANKS).strength(3.0F, 6.0F).lightLevel(state -> 1)));

    public static final Supplier<BlockEntityType<ClockBlockTile>> CLOCK_BLOCK_TILE = RegUtils.regTile("clock_block", () -> PlatHelper.newBlockEntityType(ClockBlockTile::new, (Block) CLOCK_BLOCK.get()));

    public static final Supplier<Block> CRYSTAL_DISPLAY = RegUtils.regWithItem("crystal_display", () -> new CrystalDisplayBlock(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE).sound(SoundType.POLISHED_DEEPSLATE).strength(0.5F, 0.5F)));

    public static final Supplier<Block> SCONCE_LEVER = RegUtils.regWithItem("sconce_lever", () -> new SconceLeverBlock(BlockBehaviour.Properties.copy((BlockBehaviour) SCONCE.get()), () -> ParticleTypes.FLAME));

    public static final Supplier<Block> CRANK = RegUtils.regWithItem("crank", () -> new CrankBlock(BlockBehaviour.Properties.of().mapColor(MapColor.NONE).pushReaction(PushReaction.DESTROY).strength(0.6F, 0.6F).noCollission().noOcclusion()));

    public static final Supplier<Block> WIND_VANE = RegUtils.regWithItem("wind_vane", () -> new WindVaneBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BARS).strength(5.0F, 6.0F).requiresCorrectToolForDrops().noOcclusion()));

    public static final Supplier<BlockEntityType<WindVaneBlockTile>> WIND_VANE_TILE = RegUtils.regTile("wind_vane", () -> PlatHelper.newBlockEntityType(WindVaneBlockTile::new, (Block) WIND_VANE.get()));

    public static final Supplier<Block> FAUCET = RegUtils.regWithItem("faucet", () -> new FaucetBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BARS).strength(3.0F, 4.8F).noOcclusion()));

    public static final Supplier<BlockEntityType<FaucetBlockTile>> FAUCET_TILE = RegUtils.regTile("faucet", () -> PlatHelper.newBlockEntityType(FaucetBlockTile::new, (Block) FAUCET.get()));

    public static final Supplier<Block> GOLD_DOOR = RegUtils.regWithItem("gold_door", () -> new GoldDoorBlock(BlockBehaviour.Properties.copy(Blocks.GOLD_BLOCK).noOcclusion()));

    public static final Supplier<Block> GOLD_TRAPDOOR = RegUtils.regWithItem("gold_trapdoor", () -> new GoldTrapdoorBlock(BlockBehaviour.Properties.copy((BlockBehaviour) GOLD_DOOR.get()).isValidSpawn((a, b, c, d) -> false)));

    public static final Supplier<Block> NETHERITE_DOOR = RegUtils.regBlock("netherite_door", () -> new NetheriteDoorBlock(BlockBehaviour.Properties.copy(Blocks.NETHERITE_BLOCK).noOcclusion()));

    public static final Supplier<Item> NETHERITE_DOOR_ITEM = RegUtils.regItem("netherite_door", () -> new BlockItem((Block) NETHERITE_DOOR.get(), new Item.Properties().fireResistant()));

    public static final Supplier<Block> NETHERITE_TRAPDOOR = RegUtils.regBlock("netherite_trapdoor", () -> new NetheriteTrapdoorBlock(BlockBehaviour.Properties.copy((BlockBehaviour) NETHERITE_DOOR.get()).noOcclusion().isValidSpawn((a, b, c, d) -> false)));

    public static final Supplier<Item> NETHERITE_TRAPDOOR_ITEM = RegUtils.regItem("netherite_trapdoor", () -> new BlockItem((Block) NETHERITE_TRAPDOOR.get(), new Item.Properties().fireResistant()));

    public static final Supplier<BlockEntityType<KeyLockableTile>> KEY_LOCKABLE_TILE = RegUtils.regTile("key_lockable_tile", () -> PlatHelper.newBlockEntityType(KeyLockableTile::new, (Block) NETHERITE_DOOR.get(), (Block) NETHERITE_TRAPDOOR.get(), (Block) LOCK_BLOCK.get()));

    public static final Supplier<Block> IRON_GATE = RegUtils.regWithItem("iron_gate", () -> new IronGateBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BARS), false));

    public static final Supplier<Block> GOLD_GATE = RegUtils.regWithItem("gold_gate", () -> new IronGateBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BARS), true));

    public static final Supplier<Block> CHECKER_BLOCK = RegUtils.regWithItem("checker_block", () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE).mapColor(MapColor.QUARTZ).requiresCorrectToolForDrops().strength(1.5F, 6.0F)));

    public static final Supplier<Block> CHECKER_SLAB = RegUtils.regWithItem("checker_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy((BlockBehaviour) CHECKER_BLOCK.get())));

    public static final Supplier<Item> PANCAKE_ITEM = RegUtils.regItem("pancake", () -> new PancakeItem(15, (SoundEvent) ModSounds.PANCAKE_MUSIC.get(), new Item.Properties(), 228));

    public static final Supplier<Block> PANCAKE = RegUtils.regBlock("pancake", () -> new PancakeBlock(BlockBehaviour.Properties.copy(Blocks.CAKE).mapColor(MapColor.TERRACOTTA_ORANGE).strength(0.5F).sound(SoundType.WOOL)));

    public static final Supplier<Block> FLAX = RegUtils.regBlock("flax", () -> new FlaxBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT).noCollission().randomTicks().offsetType(BlockBehaviour.OffsetType.NONE).instabreak().sound(SoundType.CROP)));

    public static final Supplier<Item> FLAX_ITEM = RegUtils.regItem("flax", () -> new Item(new Item.Properties()));

    public static final Supplier<Item> FLAX_SEEDS_ITEM = RegUtils.regItem("flax_seeds", () -> new ItemNameBlockItem((Block) FLAX.get(), new Item.Properties()));

    public static final Supplier<Block> FLAX_WILD = RegUtils.regWithItem("wild_flax", () -> new WildFlaxBlock(BlockBehaviour.Properties.copy(Blocks.TALL_GRASS).offsetType(BlockBehaviour.OffsetType.NONE)));

    public static final Supplier<Block> FLAX_POT = RegUtils.regBlock("potted_flax", () -> PlatHelper.newFlowerPot(() -> (FlowerPotBlock) Blocks.FLOWER_POT, FLAX, BlockBehaviour.Properties.copy(Blocks.FLOWER_POT)));

    public static final Supplier<Block> FODDER = RegUtils.regWithItem("fodder", () -> new FodderBlock(BlockBehaviour.Properties.copy(Blocks.MOSS_BLOCK).pushReaction(PushReaction.NORMAL)));

    public static final Supplier<Block> FLAX_BLOCK = RegUtils.regWithItem("flax_block", () -> new FlaxBaleBlock(BlockBehaviour.Properties.copy(Blocks.HAY_BLOCK).mapColor(MapColor.TERRACOTTA_LIGHT_GREEN)));

    public static final Supplier<Block> JAR_BOAT = RegUtils.regWithItem("jar_boat", () -> new JarBoatBlock(BlockBehaviour.Properties.copy(Blocks.GLASS).pushReaction(PushReaction.DESTROY)));

    public static final Supplier<BlockEntityType<JarBoatTile>> JAR_BOAT_TILE = RegUtils.regTile("jar_boat", () -> PlatHelper.newBlockEntityType(JarBoatTile::new, (Block) JAR_BOAT.get()));

    public static final Supplier<Block> STRUCTURE_TEMP = RegUtils.regBlock("structure_temp", () -> new StructureTempBlock(BlockBehaviour.Properties.of().strength(0.0F).noLootTable().noCollission().noOcclusion()));

    public static final Supplier<BlockEntityType<StructureTempBlockTile>> STRUCTURE_TEMP_TILE = RegUtils.regTile("structure_temp", () -> PlatHelper.newBlockEntityType(StructureTempBlockTile::new, (Block) STRUCTURE_TEMP.get()));

    public static final Supplier<Block> BLOCK_GENERATOR = RegUtils.regBlock("block_generator", () -> new BlockGeneratorBlock(BlockBehaviour.Properties.copy((BlockBehaviour) STRUCTURE_TEMP.get()).lightLevel(s -> 14)));

    public static final Supplier<BlockEntityType<BlockGeneratorBlockTile>> BLOCK_GENERATOR_TILE = RegUtils.regTile("block_generator", () -> PlatHelper.newBlockEntityType(BlockGeneratorBlockTile::new, (Block) BLOCK_GENERATOR.get()));

    public static final Supplier<Block> STICK_BLOCK = RegUtils.regBlock("stick", () -> new StickBlock(BlockBehaviour.Properties.of().ignitedByLava().pushReaction(PushReaction.DESTROY).mapColor(MapColor.NONE).strength(0.25F, 0.0F).sound(SoundType.WOOD), 60));

    public static final Supplier<Block> BLAZE_ROD_BLOCK = RegUtils.regBlock("blaze_rod", () -> new BlazeRodBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_YELLOW).strength(0.25F, 0.0F).lightLevel(state -> 12).emissiveRendering((p, w, s) -> true).sound(SoundType.GILDED_BLACKSTONE)));

    public static final RegSupplier<Block> DAUB = RegUtils.regWithItem("daub", () -> new Block(BlockBehaviour.Properties.of().sound(SoundType.PACKED_MUD).mapColor(DyeColor.WHITE).strength(1.5F, 3.0F)));

    public static final RegSupplier<Block> DAUB_FRAME = RegUtils.regWithItem("daub_frame", () -> new Block(BlockBehaviour.Properties.copy(DAUB.get())));

    public static final RegSupplier<Block> DAUB_BRACE = RegUtils.regWithItem("daub_brace", () -> new FlippedBlock(BlockBehaviour.Properties.copy(DAUB.get())));

    public static final RegSupplier<Block> DAUB_CROSS_BRACE = RegUtils.regWithItem("daub_cross_brace", () -> new Block(BlockBehaviour.Properties.copy(DAUB.get())));

    public static final RegSupplier<FrameBlock> TIMBER_FRAME = RegUtils.regBlock("timber_frame", () -> {
        BlockBehaviour.Properties p = BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS).strength(0.1F, 0.0F).noCollission().instabreak().sound(SoundType.SCAFFOLDING);
        return new FrameBlock(p);
    });

    public static final Supplier<Item> TIMBER_FRAME_ITEM = RegUtils.regItem("timber_frame", () -> new TimberFrameItem(TIMBER_FRAME.get(), new Item.Properties(), 200));

    public static final Supplier<FrameBraceBlock> TIMBER_BRACE = RegUtils.regBlock("timber_brace", () -> {
        BlockBehaviour.Properties p = BlockBehaviour.Properties.copy(TIMBER_FRAME.get());
        return new FrameBraceBlock(p);
    });

    public static final Supplier<Item> TIMBER_BRACE_ITEM = RegUtils.regItem("timber_brace", () -> new TimberFrameItem((Block) TIMBER_BRACE.get(), new Item.Properties(), 200));

    public static final Supplier<FrameBlock> TIMBER_CROSS_BRACE = RegUtils.regBlock("timber_cross_brace", () -> {
        BlockBehaviour.Properties p = BlockBehaviour.Properties.copy(TIMBER_FRAME.get());
        return new FrameBlock(p);
    });

    public static final Supplier<Item> TIMBER_CROSS_BRACE_ITEM = RegUtils.regItem("timber_cross_brace", () -> new TimberFrameItem((Block) TIMBER_CROSS_BRACE.get(), new Item.Properties(), 200));

    public static final Supplier<BlockEntityType<FrameBlockTile>> TIMBER_FRAME_TILE = RegUtils.regTile("timber_frame", () -> PlatHelper.newBlockEntityType(FrameBlockTile::new, (Block[]) FrameBlock.FRAMED_BLOCKS.toArray(Block[]::new)));

    public static final Map<RegHelper.VariantType, Supplier<Block>> LAPIS_BRICKS_BLOCKS = RegHelper.registerFullBlockSet(Supplementaries.res("lapis_bricks"), BlockBehaviour.Properties.copy(Blocks.LAPIS_BLOCK).sound(SoundType.DEEPSLATE_TILES).strength(2.0F, 2.0F));

    public static final Map<RegHelper.VariantType, Supplier<Block>> ASH_BRICKS_BLOCKS = RegHelper.registerFullBlockSet(Supplementaries.res("ash_bricks"), Blocks.STONE_BRICKS);

    public static final Map<RegHelper.VariantType, Supplier<Block>> STONE_TILE_BLOCKS = RegHelper.registerFullBlockSet(Supplementaries.res("stone_tile"), Blocks.STONE_BRICKS);

    public static final Map<RegHelper.VariantType, Supplier<Block>> BLACKSTONE_TILE_BLOCKS = RegHelper.registerFullBlockSet(Supplementaries.res("blackstone_tile"), Blocks.BLACKSTONE);

    public static final Supplier<Block> STONE_LAMP = RegUtils.regWithItem("stone_lamp", () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE).mapColor(MapColor.COLOR_YELLOW).strength(1.5F, 6.0F).lightLevel(s -> 15).sound(SoundType.STONE)));

    public static final Supplier<Block> BLACKSTONE_LAMP = RegUtils.regWithItem("blackstone_lamp", () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.BLACKSTONE).mapColor(MapColor.COLOR_YELLOW).strength(1.5F, 6.0F).lightLevel(s -> 15).sound(SoundType.STONE)));

    public static final Supplier<Block> DEEPSLATE_LAMP = RegUtils.regWithItem("deepslate_lamp", () -> new Block(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE_BRICKS).lightLevel(s -> 15)));

    public static final Supplier<Block> END_STONE_LAMP = RegUtils.regWithItem("end_stone_lamp", () -> new EndLampBlock(BlockBehaviour.Properties.copy(Blocks.END_STONE).lightLevel(s -> 15)));

    public static final Supplier<Block> FLOWER_BOX = RegUtils.regWithItem("flower_box", () -> {
        BlockBehaviour.Properties p = BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS).strength(0.5F);
        return new FlowerBoxBlock(p);
    });

    public static final Supplier<BlockEntityType<FlowerBoxBlockTile>> FLOWER_BOX_TILE = RegUtils.regTile("flower_box", () -> PlatHelper.newBlockEntityType(FlowerBoxBlockTile::new, (Block) FLOWER_BOX.get()));

    public static final Supplier<Block> STATUE = RegUtils.regWithItem("statue", () -> new StatueBlock(BlockBehaviour.Properties.copy(Blocks.STONE).strength(2.0F)));

    public static final Supplier<BlockEntityType<StatueBlockTile>> STATUE_TILE = RegUtils.regTile("statue", () -> PlatHelper.newBlockEntityType(StatueBlockTile::new, (Block) STATUE.get()));

    public static final Supplier<Block> FEATHER_BLOCK = RegUtils.regWithItem("feather_block", () -> new FeatherBlock(BlockBehaviour.Properties.copy(Blocks.WHITE_WOOL).strength(0.5F).dynamicShape().noCollission()));

    public static final Supplier<Block> FLINT_BLOCK = RegUtils.regWithItem("flint_block", () -> new FlintBlock(BlockBehaviour.Properties.copy(Blocks.COAL_BLOCK).strength(2.0F, 7.5F)));

    public static final Supplier<Block> SUGAR_CUBE = RegUtils.regBlock("sugar_cube", () -> new SugarBlock(BlockBehaviour.Properties.of().mapColor(MapColor.SNOW).strength(0.5F).sound(SoundType.SAND)));

    public static final Supplier<Item> SUGAR_CUBE_ITEM = RegUtils.regItem("sugar_cube", () -> new SugarCubeItem((Block) SUGAR_CUBE.get(), new Item.Properties()));

    public static final Supplier<Block> GUNPOWDER_BLOCK = RegUtils.regWithItem("gunpowder", () -> new GunpowderBlock(BlockBehaviour.Properties.copy(Blocks.REDSTONE_WIRE).sound(SoundType.SAND)));

    public static final Supplier<Block> BOOK_PILE = RegUtils.regBlock("book_pile", () -> new BookPileBlock(BlockBehaviour.Properties.of().noOcclusion().mapColor(MapColor.NONE).strength(0.5F).sound(ModSounds.BOOKS)));

    public static final Supplier<Block> BOOK_PILE_H = RegUtils.regBlock("book_pile_horizontal", () -> new BookPileHorizontalBlock(BlockBehaviour.Properties.copy((BlockBehaviour) BOOK_PILE.get())));

    public static final Supplier<BlockEntityType<BookPileBlockTile>> BOOK_PILE_TILE = RegUtils.regTile("book_pile", () -> PlatHelper.newBlockEntityType(BookPileBlockTile::new, (Block) BOOK_PILE.get(), (Block) BOOK_PILE_H.get()));

    public static final Supplier<Block> URN = RegUtils.regWithItem("urn", () -> new UrnBlock(BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_BROWN).sound(SoundType.DECORATED_POT_CRACKED).strength(0.1F, 0.0F)));

    public static final Supplier<BlockEntityType<UrnBlockTile>> URN_TILE = RegUtils.regTile("urn", () -> PlatHelper.newBlockEntityType(UrnBlockTile::new, (Block) URN.get()));

    public static final Supplier<Block> ASH_BLOCK = RegUtils.regWithItem("ash", () -> new AshLayerBlock(BlockBehaviour.Properties.of().pushReaction(PushReaction.DESTROY).replaceable().mapColor(MapColor.COLOR_GRAY).sound(SoundType.SAND).randomTicks().strength(0.1F).forceSolidOff().isViewBlocking((state, l, p) -> (Integer) state.m_61143_(AshLayerBlock.LAYERS) >= 8).requiresCorrectToolForDrops()));

    public static final Supplier<Item> ASH_BRICK_ITEM = RegUtils.regItem("ash_brick", () -> new Item(new Item.Properties()));

    public static final Supplier<Item> SOAP = RegUtils.regItem("soap", () -> new SoapItem(new Item.Properties()));

    public static final Supplier<Block> SOAP_BLOCK = RegUtils.regWithItem("soap_block", () -> new SoapBlock(BlockBehaviour.Properties.of().friction(0.94F).instrument(NoteBlockInstrument.DIDGERIDOO).mapColor(DyeColor.PINK).pushReaction(PushReaction.PUSH_ONLY).strength(1.25F, 4.0F).sound(SoundType.CORAL_BLOCK)));

    public static final Supplier<BubbleBlock> BUBBLE_BLOCK = RegUtils.regBlock("bubble_block", () -> new BubbleBlock(BlockBehaviour.Properties.of().sound(ModSounds.BUBBLE_BLOCK).mapColor(MapColor.COLOR_PINK).noOcclusion().pushReaction(PushReaction.DESTROY).forceSolidOff().isSuffocating((a, b, c) -> false).isViewBlocking((a, b, c) -> false).isRedstoneConductor((a, b, c) -> false).instabreak()));

    public static final Supplier<Item> BUBBLE_BLOCK_ITEM = RegUtils.regItem("bubble_block", () -> new BubbleBlockItem((Block) BUBBLE_BLOCK.get(), new Item.Properties()));

    public static final Supplier<BlockEntityType<BubbleBlockTile>> BUBBLE_BLOCK_TILE = RegUtils.regTile("bubble_block", () -> PlatHelper.newBlockEntityType(BubbleBlockTile::new, (Block) BUBBLE_BLOCK.get()));

    public static final Supplier<EndermanSkullBlock> ENDERMAN_SKULL_BLOCK = RegUtils.regBlock("enderman_head", () -> new EndermanSkullBlock(BlockBehaviour.Properties.copy(Blocks.WITHER_SKELETON_SKULL).instrument(NoteBlockInstrument.CUSTOM_HEAD)));

    public static final Supplier<EndermanSkullWallBlock> ENDERMAN_SKULL_BLOCK_WALL = RegUtils.regBlock("enderman_wall_head", () -> new EndermanSkullWallBlock(BlockBehaviour.Properties.copy(Blocks.WITHER_SKELETON_SKULL).instrument(NoteBlockInstrument.CUSTOM_HEAD)));

    public static final Supplier<Item> ENDERMAN_SKULL_ITEM = RegUtils.regItem("enderman_head", () -> new EndermanHeadItem((Block) ENDERMAN_SKULL_BLOCK.get(), (Block) ENDERMAN_SKULL_BLOCK_WALL.get(), new Item.Properties().rarity(Rarity.UNCOMMON)));

    public static final Supplier<BlockEntityType<EndermanSkullBlockTile>> ENDERMAN_SKULL_TILE = RegUtils.regTile("enderman_head", () -> PlatHelper.newBlockEntityType(EndermanSkullBlockTile::new, (Block) ENDERMAN_SKULL_BLOCK.get(), (Block) ENDERMAN_SKULL_BLOCK_WALL.get()));

    public static final Supplier<Block> ASHEN_BASALT = RegUtils.regBlock("ashen_basalt", () -> new AshenBasaltBlock(BlockBehaviour.Properties.copy(Blocks.BASALT)));

    public static final Supplier<Item> HAT_STAND = RegUtils.regItem("hat_stand", () -> new HatStandItem(new Item.Properties()));

    public static void init() {
        CompatHandler.initOptionalRegistries();
        RegUtils.initDynamicRegistry();
    }

    private static boolean isDisabled(String name) {
        return !CommonConfigs.isEnabled(name);
    }
}
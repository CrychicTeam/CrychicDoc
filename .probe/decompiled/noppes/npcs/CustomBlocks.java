package noppes.npcs;

import net.minecraft.Util;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ObjectHolder;
import net.minecraftforge.registries.RegisterEvent;
import noppes.npcs.blocks.BlockBorder;
import noppes.npcs.blocks.BlockBuilder;
import noppes.npcs.blocks.BlockCarpentryBench;
import noppes.npcs.blocks.BlockCopy;
import noppes.npcs.blocks.BlockMailbox;
import noppes.npcs.blocks.BlockNpcRedstone;
import noppes.npcs.blocks.BlockScripted;
import noppes.npcs.blocks.BlockScriptedDoor;
import noppes.npcs.blocks.BlockWaypoint;
import noppes.npcs.blocks.tiles.TileBlockAnvil;
import noppes.npcs.blocks.tiles.TileBorder;
import noppes.npcs.blocks.tiles.TileBuilder;
import noppes.npcs.blocks.tiles.TileCopy;
import noppes.npcs.blocks.tiles.TileMailbox;
import noppes.npcs.blocks.tiles.TileRedstoneBlock;
import noppes.npcs.blocks.tiles.TileScripted;
import noppes.npcs.blocks.tiles.TileScriptedDoor;
import noppes.npcs.blocks.tiles.TileWaypoint;
import noppes.npcs.items.ItemNpcBlock;
import noppes.npcs.items.ItemScriptedDoor;

@EventBusSubscriber(bus = Bus.MOD, modid = "customnpcs")
public class CustomBlocks {

    @ObjectHolder(registryName = "block", value = "customnpcs:npcredstoneblock")
    public static Block redstone;

    @ObjectHolder(registryName = "item", value = "customnpcs:npcredstoneblock")
    public static Item redstone_item;

    @ObjectHolder(registryName = "block", value = "customnpcs:npcmailbox")
    public static Block mailbox;

    @ObjectHolder(registryName = "block", value = "customnpcs:npcmailbox2")
    public static Block mailbox2;

    @ObjectHolder(registryName = "block", value = "customnpcs:npcmailbox3")
    public static Block mailbox3;

    @ObjectHolder(registryName = "block", value = "customnpcs:npcwaypoint")
    public static Block waypoint;

    @ObjectHolder(registryName = "item", value = "customnpcs:npcwaypoint")
    public static Item waypoint_item;

    @ObjectHolder(registryName = "block", value = "customnpcs:npcborder")
    public static Block border;

    @ObjectHolder(registryName = "item", value = "customnpcs:npcborder")
    public static Item border_item;

    @ObjectHolder(registryName = "block", value = "customnpcs:npcscripted")
    public static Block scripted;

    @ObjectHolder(registryName = "item", value = "customnpcs:npcscripted")
    public static Item scripted_item;

    @ObjectHolder(registryName = "block", value = "customnpcs:npcscripteddoor")
    public static Block scripted_door;

    @ObjectHolder(registryName = "item", value = "customnpcs:npcscripteddoortool")
    public static Item scripted_door_item;

    @ObjectHolder(registryName = "block", value = "customnpcs:npcbuilderblock")
    public static Block builder;

    @ObjectHolder(registryName = "item", value = "customnpcs:npcbuilderblock")
    public static Item builder_item;

    @ObjectHolder(registryName = "block", value = "customnpcs:npccopyblock")
    public static Block copy;

    @ObjectHolder(registryName = "item", value = "customnpcs:npccopyblock")
    public static Item copy_item;

    @ObjectHolder(registryName = "block", value = "customnpcs:npccarpentybench")
    public static Block carpenty;

    @ObjectHolder(registryName = "block_entity_type", value = "customnpcs:tileblockanvil")
    public static BlockEntityType<TileBlockAnvil> tile_anvil;

    @ObjectHolder(registryName = "block_entity_type", value = "customnpcs:tilenpcborder")
    public static BlockEntityType<TileBorder> tile_border;

    @ObjectHolder(registryName = "block_entity_type", value = "customnpcs:tilenpcbuilder")
    public static BlockEntityType<TileBuilder> tile_builder;

    @ObjectHolder(registryName = "block_entity_type", value = "customnpcs:tilenpccopy")
    public static BlockEntityType<TileCopy> tile_copy;

    @ObjectHolder(registryName = "block_entity_type", value = "customnpcs:tilemailbox")
    public static BlockEntityType<TileMailbox> tile_mailbox;

    @ObjectHolder(registryName = "block_entity_type", value = "customnpcs:tileredstoneblock")
    public static BlockEntityType<TileRedstoneBlock> tile_redstoneblock;

    @ObjectHolder(registryName = "block_entity_type", value = "customnpcs:tilenpcscripted")
    public static BlockEntityType<TileScripted> tile_scripted;

    @ObjectHolder(registryName = "block_entity_type", value = "customnpcs:tilenpcscripteddoor")
    public static BlockEntityType<TileScriptedDoor> tile_scripteddoor;

    @ObjectHolder(registryName = "block_entity_type", value = "customnpcs:tilewaypoint")
    public static BlockEntityType<TileWaypoint> tile_waypoint;

    @SubscribeEvent
    public static void registerBlocks(RegisterEvent event) {
        if (event.getRegistryKey() == ForgeRegistries.Keys.BLOCKS) {
            event.<BlockNpcRedstone>getForgeRegistry().register("customnpcs:npcredstoneblock", new BlockNpcRedstone());
            event.<BlockMailbox>getForgeRegistry().register("customnpcs:npcmailbox", new BlockMailbox(0));
            event.<BlockMailbox>getForgeRegistry().register("customnpcs:npcmailbox2", new BlockMailbox(1));
            event.<BlockMailbox>getForgeRegistry().register("customnpcs:npcmailbox3", new BlockMailbox(2));
            event.<BlockWaypoint>getForgeRegistry().register("customnpcs:npcwaypoint", new BlockWaypoint());
            event.<BlockBorder>getForgeRegistry().register("customnpcs:npcborder", new BlockBorder());
            event.<BlockScripted>getForgeRegistry().register("customnpcs:npcscripted", new BlockScripted());
            event.<BlockScriptedDoor>getForgeRegistry().register("customnpcs:npcscripteddoor", new BlockScriptedDoor());
            event.<BlockBuilder>getForgeRegistry().register("customnpcs:npcbuilderblock", new BlockBuilder());
            event.<BlockCopy>getForgeRegistry().register("customnpcs:npccopyblock", new BlockCopy());
            event.<BlockCarpentryBench>getForgeRegistry().register("customnpcs:npccarpentybench", new BlockCarpentryBench());
        }
        if (event.getRegistryKey() == ForgeRegistries.Keys.ITEMS) {
            event.<Item>getForgeRegistry().register("customnpcs:npcredstoneblock", createItem(redstone));
            event.<Item>getForgeRegistry().register("customnpcs:npcmailbox", createItem(mailbox));
            event.<Item>getForgeRegistry().register("customnpcs:npcmailbox2", createItem(mailbox2));
            event.<Item>getForgeRegistry().register("customnpcs:npcmailbox3", createItem(mailbox3));
            event.<Item>getForgeRegistry().register("customnpcs:npcwaypoint", createItem(waypoint));
            event.<Item>getForgeRegistry().register("customnpcs:npcborder", createItem(border));
            event.<Item>getForgeRegistry().register("customnpcs:npcscripted", createItem(scripted));
            event.<ItemScriptedDoor>getForgeRegistry().register("customnpcs:npcscripteddoortool", new ItemScriptedDoor(scripted_door));
            event.<Item>getForgeRegistry().register("customnpcs:npcbuilderblock", createItem(builder));
            event.<Item>getForgeRegistry().register("customnpcs:npccopyblock", createItem(copy));
            event.<Item>getForgeRegistry().register("customnpcs:npccarpentybench", createItem(carpenty));
        }
        if (event.getRegistryKey() == ForgeRegistries.Keys.BLOCK_ENTITY_TYPES) {
            event.<BlockEntityType<?>>getForgeRegistry().register("customnpcs:tileblockanvil", createTile("tileblockanvil", TileBlockAnvil::new, carpenty));
            event.<BlockEntityType<?>>getForgeRegistry().register("customnpcs:tilenpcborder", createTile("tilenpcborder", TileBorder::new, border));
            event.<BlockEntityType<?>>getForgeRegistry().register("customnpcs:tilenpcbuilder", createTile("tilenpcbuilder", TileBuilder::new, builder));
            event.<BlockEntityType<?>>getForgeRegistry().register("customnpcs:tilenpccopy", createTile("tilenpccopy", TileCopy::new, copy));
            event.<BlockEntityType<?>>getForgeRegistry().register("customnpcs:tilemailbox", createTile("tilemailbox", TileMailbox::new, mailbox, mailbox2, mailbox3));
            event.<BlockEntityType<?>>getForgeRegistry().register("customnpcs:tileredstoneblock", createTile("tileredstoneblock", TileRedstoneBlock::new, redstone));
            event.<BlockEntityType<?>>getForgeRegistry().register("customnpcs:tilenpcscripted", createTile("tilenpcscripted", TileScripted::new, scripted));
            event.<BlockEntityType<?>>getForgeRegistry().register("customnpcs:tilenpcscripteddoor", createTile("tilenpcscripteddoor", TileScriptedDoor::new, scripted_door));
            event.<BlockEntityType<?>>getForgeRegistry().register("customnpcs:tilewaypoint", createTile("tilewaypoint", TileWaypoint::new, waypoint));
        }
    }

    private static BlockEntityType<?> createTile(String key, BlockEntityType.BlockEntitySupplier factoryIn, Block... blocks) {
        BlockEntityType.Builder<BlockEntity> builder = BlockEntityType.Builder.of(factoryIn, blocks);
        return builder.build(Util.fetchChoiceType(References.BLOCK_ENTITY, key));
    }

    public static Item createItem(Block block) {
        Item item = new ItemNpcBlock(block, new Item.Properties());
        return item;
    }
}
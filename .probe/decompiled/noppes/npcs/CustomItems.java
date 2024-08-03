package noppes.npcs;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ObjectHolder;
import net.minecraftforge.registries.RegisterEvent;
import noppes.npcs.items.ItemMounter;
import noppes.npcs.items.ItemNbtBook;
import noppes.npcs.items.ItemNpcCloner;
import noppes.npcs.items.ItemNpcMovingPath;
import noppes.npcs.items.ItemNpcScripter;
import noppes.npcs.items.ItemNpcWand;
import noppes.npcs.items.ItemScripted;
import noppes.npcs.items.ItemSoulstoneEmpty;
import noppes.npcs.items.ItemSoulstoneFilled;
import noppes.npcs.items.ItemTeleporter;

@EventBusSubscriber(bus = Bus.MOD, modid = "customnpcs")
public class CustomItems {

    @ObjectHolder(registryName = "item", value = "customnpcs:npcwand")
    public static Item wand = null;

    @ObjectHolder(registryName = "item", value = "customnpcs:npcmobcloner")
    public static Item cloner = null;

    @ObjectHolder(registryName = "item", value = "customnpcs:npcscripter")
    public static Item scripter = null;

    @ObjectHolder(registryName = "item", value = "customnpcs:npcmovingpath")
    public static Item moving = null;

    @ObjectHolder(registryName = "item", value = "customnpcs:npcmounter")
    public static Item mount = null;

    @ObjectHolder(registryName = "item", value = "customnpcs:npcteleporter")
    public static Item teleporter = null;

    @ObjectHolder(registryName = "item", value = "customnpcs:scripted_item")
    public static ItemScripted scripted_item = null;

    @ObjectHolder(registryName = "item", value = "customnpcs:nbt_book")
    public static ItemNbtBook nbt_book = null;

    @ObjectHolder(registryName = "item", value = "customnpcs:npcsoulstoneempty")
    public static final Item soulstoneEmpty = null;

    @ObjectHolder(registryName = "item", value = "customnpcs:npcsoulstonefilled")
    public static final Item soulstoneFull = null;

    @SubscribeEvent
    public static void registerBlocks(RegisterEvent event) {
        if (event.getRegistryKey() == ForgeRegistries.Keys.ITEMS) {
            event.<ItemNpcWand>getForgeRegistry().register("customnpcs:npcwand", new ItemNpcWand());
            event.<ItemNpcCloner>getForgeRegistry().register("customnpcs:npcmobcloner", new ItemNpcCloner());
            event.<ItemNpcScripter>getForgeRegistry().register("customnpcs:npcscripter", new ItemNpcScripter());
            event.<ItemNpcMovingPath>getForgeRegistry().register("customnpcs:npcmovingpath", new ItemNpcMovingPath());
            event.<ItemMounter>getForgeRegistry().register("customnpcs:npcmounter", new ItemMounter());
            event.<ItemTeleporter>getForgeRegistry().register("customnpcs:npcteleporter", new ItemTeleporter());
            event.<ItemSoulstoneEmpty>getForgeRegistry().register("customnpcs:npcsoulstoneempty", new ItemSoulstoneEmpty());
            event.<ItemSoulstoneFilled>getForgeRegistry().register("customnpcs:npcsoulstonefilled", new ItemSoulstoneFilled());
            event.<ItemScripted>getForgeRegistry().register("customnpcs:scripted_item", new ItemScripted(new Item.Properties().stacksTo(1)));
            event.<ItemNbtBook>getForgeRegistry().register("customnpcs:nbt_book", new ItemNbtBook());
        }
    }

    public static void registerDispenser() {
        DispenserBlock.registerBehavior(soulstoneFull, new DefaultDispenseItemBehavior() {

            @Override
            public ItemStack execute(BlockSource source, ItemStack item) {
                Direction enumfacing = (Direction) source.getBlockState().m_61143_(DispenserBlock.FACING);
                double x = source.x() + (double) enumfacing.getStepX();
                double z = source.z() + (double) enumfacing.getStepZ();
                ItemSoulstoneFilled.Spawn(null, item, source.getLevel(), new BlockPos((int) x, (int) source.y(), (int) z));
                item.split(1);
                return item;
            }
        });
    }
}
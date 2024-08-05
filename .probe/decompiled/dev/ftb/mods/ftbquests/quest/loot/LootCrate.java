package dev.ftb.mods.ftbquests.quest.loot;

import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import dev.ftb.mods.ftblibrary.config.NameMap;
import dev.ftb.mods.ftblibrary.icon.Color4I;
import dev.ftb.mods.ftbquests.item.FTBQuestsItems;
import dev.ftb.mods.ftbquests.quest.QuestObjectBase;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

public final class LootCrate {

    private static final Pattern NON_ALPHANUM = Pattern.compile("[^a-z0-9_]");

    private static final Pattern MULTI_UNDERSCORE = Pattern.compile("_{2,}");

    public static Map<String, LootCrate> LOOT_CRATES = new LinkedHashMap();

    private final RewardTable table;

    private String stringID;

    private String itemName = "";

    private Color4I color = Color4I.WHITE;

    private boolean glow = false;

    private EntityWeight drops = new EntityWeight();

    public LootCrate(RewardTable table, boolean initFromTable) {
        this.table = table;
        if (initFromTable) {
            this.initFromTable();
        } else {
            this.stringID = table.toString();
        }
    }

    public void initFromTable() {
        this.stringID = buildStringID(this.table);
        LootCrate.Defaults def = LootCrate.Defaults.NAME_MAP.getNullable(this.stringID);
        if (def != null) {
            this.color = Color4I.rgb(def.color);
            this.glow = def.glow;
            this.drops.passive = def.passive;
            this.drops.monster = def.monster;
            this.drops.boss = def.boss;
        }
    }

    private static String buildStringID(RewardTable table) {
        Matcher matcher = NON_ALPHANUM.matcher(table.getTitle().getString().toLowerCase());
        Matcher matcher1 = MULTI_UNDERSCORE.matcher(matcher.replaceAll("_"));
        return matcher1.replaceAll("_");
    }

    public RewardTable getTable() {
        return this.table;
    }

    public String getItemName() {
        return this.itemName;
    }

    public Color4I getColor() {
        return this.color;
    }

    public boolean isGlow() {
        return this.glow;
    }

    public EntityWeight getDrops() {
        return this.drops;
    }

    public void writeData(CompoundTag nbt) {
        nbt.putString("string_id", this.stringID);
        if (!this.itemName.isEmpty()) {
            nbt.putString("item_name", this.itemName);
        }
        nbt.putInt("color", this.color.rgb());
        if (this.glow) {
            nbt.putBoolean("glow", true);
        }
        CompoundTag nbt1 = new CompoundTag();
        this.drops.writeData(nbt1);
        nbt.put("drops", nbt1);
    }

    public void readData(CompoundTag nbt) {
        this.stringID = nbt.getString("string_id");
        this.itemName = nbt.getString("item_name");
        this.color = Color4I.rgb(nbt.getInt("color"));
        this.glow = nbt.getBoolean("glow");
        this.drops.readData(nbt.getCompound("drops"));
    }

    public void writeNetData(FriendlyByteBuf data) {
        data.writeUtf(this.stringID, 32767);
        data.writeUtf(this.itemName, 32767);
        data.writeInt(this.color.rgb());
        data.writeBoolean(this.glow);
        this.drops.writeNetData(data);
    }

    public void readNetData(FriendlyByteBuf data) {
        this.stringID = data.readUtf(32767);
        this.itemName = data.readUtf(32767);
        this.color = Color4I.rgb(data.readInt());
        this.glow = data.readBoolean();
        this.drops.readNetData(data);
    }

    public void fillConfigGroup(ConfigGroup config) {
        config.addString("id", this.stringID, v -> this.stringID = v, "", Pattern.compile("[a-z0-9_]+"));
        config.addString("item_name", this.itemName, v -> this.itemName = v, "");
        config.addColor("color", this.color, v -> this.color = v, Color4I.WHITE);
        config.addBool("glow", this.glow, v -> this.glow = v, true);
        ConfigGroup d = config.getOrCreateSubgroup("drops");
        d.setNameKey("ftbquests.loot.entitydrops");
        d.addInt("passive", this.drops.passive, v -> this.drops.passive = v, 0, 0, Integer.MAX_VALUE).setNameKey("ftbquests.loot.entitytype.passive");
        d.addInt("monster", this.drops.monster, v -> this.drops.monster = v, 0, 0, Integer.MAX_VALUE).setNameKey("ftbquests.loot.entitytype.monster");
        d.addInt("boss", this.drops.boss, v -> this.drops.boss = v, 0, 0, Integer.MAX_VALUE).setNameKey("ftbquests.loot.entitytype.boss");
    }

    public String getStringID() {
        return this.stringID.isEmpty() ? QuestObjectBase.getCodeString(this.table) : this.stringID;
    }

    public ItemStack createStack() {
        ItemStack stack = new ItemStack((ItemLike) FTBQuestsItems.LOOTCRATE.get());
        stack.addTagElement("type", StringTag.valueOf(this.getStringID()));
        return stack;
    }

    public static Collection<ItemStack> allCrateStacks() {
        return LOOT_CRATES.values().stream().map(LootCrate::createStack).toList();
    }

    private static enum Defaults {

        COMMON("common", 9607578, 350, 10, 0, false), UNCOMMON("uncommon", 3648105, 200, 90, 0, false), RARE("rare", 38143, 50, 200, 0, false), EPIC("epic", 8388863, 9, 10, 10, false), LEGENDARY("legendary", 16761159, 1, 1, 190, true);

        private final String name;

        private final int color;

        private final int passive;

        private final int monster;

        private final int boss;

        private final boolean glow;

        static final NameMap<LootCrate.Defaults> NAME_MAP = NameMap.of(COMMON, values()).create();

        private Defaults(String name, int color, int passive, int monster, int boss, boolean glow) {
            this.name = name;
            this.color = color;
            this.passive = passive;
            this.monster = monster;
            this.boss = boss;
            this.glow = glow;
        }
    }
}
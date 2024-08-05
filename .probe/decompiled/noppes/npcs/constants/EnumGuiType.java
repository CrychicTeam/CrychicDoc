package noppes.npcs.constants;

import net.minecraft.resources.ResourceLocation;

public enum EnumGuiType {

    MainMenuDisplay,
    MainMenuInv(true),
    MainMenuStats,
    ManageFactions,
    MainMenuAdvanced,
    MainMenuGlobal,
    MainMenuAI,
    ManageTransport,
    ManageBanks(true),
    ManageDialogs,
    ManageQuests,
    ManageRecipes(true),
    ManageLinked,
    PlayerFollowerHire(true),
    PlayerFollower(true),
    PlayerBankSmall(true),
    PlayerBankUnlock(true),
    PlayerBankUprade(true),
    PlayerBankLarge(true),
    PlayerMailbox,
    PlayerMailman(true),
    PlayerTrader(true),
    PlayerAnvil(true),
    SetupItemGiver(true),
    SetupTrader(true),
    SetupFollower(true),
    PlayerTransporter,
    RedstoneBlock,
    SetupTransporter,
    MobSpawner,
    SetupBank(true),
    QuestReward(true),
    QuestItem(true),
    NpcRemote,
    MovingPath,
    MobSpawnerAdd,
    Waypoint,
    MerchantAdd(true),
    MobSpawnerMounter,
    NpcDimensions,
    Border,
    Script,
    ScriptBlock,
    ScriptDoor,
    Companion,
    CompanionInv(true),
    CompanionTalent,
    CompanionTrader,
    BuilderBlock,
    CopyBlock,
    ScriptPlayers,
    ScriptItem,
    NbtBook,
    CustomGui(true);

    public boolean hasContainer = false;

    public final ResourceLocation resource = new ResourceLocation("customnpcs", "gui" + this.ordinal());

    private EnumGuiType() {
    }

    private EnumGuiType(boolean hasContainer) {
        this();
        this.hasContainer = hasContainer;
    }

    public static EnumGuiType getEnum(ResourceLocation location) {
        for (EnumGuiType type : values()) {
            if (type.resource.equals(location)) {
                return type;
            }
        }
        return null;
    }
}
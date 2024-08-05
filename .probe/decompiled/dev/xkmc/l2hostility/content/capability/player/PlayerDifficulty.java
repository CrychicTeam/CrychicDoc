package dev.xkmc.l2hostility.content.capability.player;

import dev.xkmc.l2hostility.compat.curios.CurioCompat;
import dev.xkmc.l2hostility.content.capability.chunk.ChunkCapSyncToClient;
import dev.xkmc.l2hostility.content.capability.chunk.ChunkDifficulty;
import dev.xkmc.l2hostility.content.capability.chunk.SectionDifficulty;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.item.curio.core.CurseCurioItem;
import dev.xkmc.l2hostility.content.item.spawner.TraitSpawnerBlockEntity;
import dev.xkmc.l2hostility.content.logic.DifficultyLevel;
import dev.xkmc.l2hostility.content.logic.LevelEditor;
import dev.xkmc.l2hostility.content.logic.MobDifficultyCollector;
import dev.xkmc.l2hostility.content.logic.TraitManager;
import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.data.LangData;
import dev.xkmc.l2hostility.init.registrate.LHItems;
import dev.xkmc.l2hostility.init.registrate.LHMiscs;
import dev.xkmc.l2library.capability.player.PlayerCapabilityHolder;
import dev.xkmc.l2library.capability.player.PlayerCapabilityNetworkHandler;
import dev.xkmc.l2library.capability.player.PlayerCapabilityTemplate;
import dev.xkmc.l2library.util.code.GenericItemStack;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.SerialClass.SerialField;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.TreeSet;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.GameRules;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import org.jetbrains.annotations.Nullable;

@SerialClass
public class PlayerDifficulty extends PlayerCapabilityTemplate<PlayerDifficulty> {

    public static final Capability<PlayerDifficulty> CAPABILITY = CapabilityManager.get(new CapabilityToken<PlayerDifficulty>() {
    });

    public static final PlayerCapabilityHolder<PlayerDifficulty> HOLDER = new PlayerCapabilityHolder<>(new ResourceLocation("l2hostility", "player"), CAPABILITY, PlayerDifficulty.class, PlayerDifficulty::new, PlayerCapabilityNetworkHandler::new);

    @SerialField
    private final DifficultyLevel difficulty = new DifficultyLevel();

    @SerialField
    public int maxRankKilled = 0;

    @SerialField
    public int rewardCount = 0;

    @SerialField
    public final TreeSet<ResourceLocation> dimensions = new TreeSet();

    @Nullable
    public ChunkDifficulty prevChunk;

    public static void register() {
    }

    @Override
    public void onClone(boolean isWasDeath) {
        if (isWasDeath) {
            if (!LHConfig.COMMON.keepInventoryRuleKeepDifficulty.get() || !this.world.getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY)) {
                if (LHConfig.COMMON.deathDecayDimension.get()) {
                    this.dimensions.clear();
                }
                if (LHConfig.COMMON.deathDecayTraitCap.get() && this.maxRankKilled > 0) {
                    this.maxRankKilled--;
                }
                this.difficulty.decay();
            }
        }
    }

    @Override
    public void tick() {
        if (this.player instanceof ServerPlayer sp) {
            Optional<ChunkDifficulty> opt = ChunkDifficulty.at(this.player.m_9236_(), this.player.m_20183_());
            if (opt.isPresent()) {
                ChunkDifficulty currentChunk = (ChunkDifficulty) opt.get();
                if (this.prevChunk != currentChunk) {
                    L2Hostility.HANDLER.toClientPlayer(new ChunkCapSyncToClient(currentChunk), sp);
                    this.prevChunk = currentChunk;
                }
                SectionDifficulty sec = ((ChunkDifficulty) opt.get()).getSection(this.player.m_20183_().m_123342_());
                if (sec.activePos != null && this.player.m_9236_().isLoaded(sec.activePos) && this.player.m_9236_().getBlockEntity(sec.activePos) instanceof TraitSpawnerBlockEntity spawner) {
                    spawner.track(this.player);
                }
            }
            if (this.dimensions.add(this.player.m_9236_().dimension().location())) {
                this.sync();
            }
        }
    }

    public void sync() {
        HOLDER.network.toClientSyncAll((ServerPlayer) this.player);
    }

    public void apply(MobDifficultyCollector instance) {
        instance.setPlayer(this.player);
        instance.acceptBonus(this.getLevel());
        instance.setTraitCap(this.getRankCap());
        if (CurioCompat.hasItemInCurio(this.player, (Item) LHItems.CURSE_PRIDE.get())) {
            instance.traitCostFactor(LHConfig.COMMON.prideTraitFactor.get());
            instance.setFullChance();
        }
        if (CurioCompat.hasItemInCurio(this.player, (Item) LHItems.ABYSSAL_THORN.get())) {
            instance.traitCostFactor(0.0);
            instance.setFullChance();
            instance.setFullDrop();
        }
    }

    public int getRankCap() {
        return TraitManager.getTraitCap(this.maxRankKilled, this.difficulty);
    }

    public void addKillCredit(MobTraitCap cap) {
        double growFactor = 1.0;
        for (GenericItemStack<CurseCurioItem> stack : CurseCurioItem.getFromPlayer(this.player)) {
            growFactor *= stack.item().getGrowFactor(stack.stack(), this, cap);
        }
        this.difficulty.grow(growFactor, cap);
        cap.traits.values().stream().max(Comparator.naturalOrder()).ifPresent(integer -> this.maxRankKilled = Math.max(this.maxRankKilled, integer));
        if (this.getLevel().getLevel() > this.rewardCount * 10) {
            this.rewardCount++;
            this.player.getInventory().add(LHItems.HOSTILITY_ORB.asStack());
        }
        this.sync();
    }

    public int getRewardCount() {
        return this.rewardCount;
    }

    public DifficultyLevel getLevel() {
        return DifficultyLevel.merge(this.difficulty, this.getExtraLevel());
    }

    private int getDimCount() {
        return Math.max(0, this.dimensions.size() - 1);
    }

    private int getExtraLevel() {
        int ans = 0;
        ans += this.getDimCount() * LHConfig.COMMON.dimensionFactor.get();
        return ans + (int) this.player.m_21133_((Attribute) LHMiscs.ADD_LEVEL.get());
    }

    public List<Component> getPlayerDifficultyDetail() {
        int item = (int) this.player.m_21133_((Attribute) LHMiscs.ADD_LEVEL.get());
        int dim = this.getDimCount() * LHConfig.COMMON.dimensionFactor.get();
        return List.of(LangData.INFO_PLAYER_ADAPTIVE_LEVEL.get(this.difficulty.level).withStyle(ChatFormatting.GRAY), LangData.INFO_PLAYER_ITEM_LEVEL.get(item).withStyle(ChatFormatting.GRAY), LangData.INFO_PLAYER_DIM_LEVEL.get(dim).withStyle(ChatFormatting.GRAY), LangData.INFO_PLAYER_EXT_LEVEL.get(this.difficulty.extraLevel).withStyle(ChatFormatting.GRAY));
    }

    public LevelEditor getLevelEditor() {
        return new LevelEditor(this.difficulty, this.getExtraLevel());
    }
}
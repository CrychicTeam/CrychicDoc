package com.github.alexthe666.alexsmobs.tileentity;

import com.github.alexthe666.alexsmobs.AlexsMobs;
import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.message.MessageUpdateTransmutablesToDisplay;
import com.github.alexthe666.alexsmobs.misc.AMAdvancementTriggerRegistry;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import com.github.alexthe666.alexsmobs.misc.TransmutationData;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.Map.Entry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

public class TileEntityTransmutationTable extends BlockEntity {

    private static final ResourceLocation COMMON_ITEMS = new ResourceLocation("alexsmobs", "gameplay/transmutation_table_common");

    private static final ResourceLocation UNCOMMON_ITEMS = new ResourceLocation("alexsmobs", "gameplay/transmutation_table_uncommon");

    private static final ResourceLocation RARE_ITEMS = new ResourceLocation("alexsmobs", "gameplay/transmutation_table_rare");

    public int ticksExisted;

    private int totalTransmuteCount = 0;

    private final Map<UUID, TransmutationData> playerToData = new HashMap();

    private final ItemStack[] possiblities = new ItemStack[3];

    private static final Random RANDOM = new Random();

    private UUID rerollPlayerUUID = null;

    public TileEntityTransmutationTable(BlockPos pos, BlockState state) {
        super(AMTileEntityRegistry.TRANSMUTATION_TABLE.get(), pos, state);
    }

    public static void commonTick(Level level, BlockPos pos, BlockState state, TileEntityTransmutationTable entity) {
        entity.tick();
    }

    private static ItemStack createFromLootTable(Player player, ResourceLocation loc) {
        if (player.m_9236_().isClientSide) {
            return ItemStack.EMPTY;
        } else {
            LootTable loottable = player.m_9236_().getServer().getLootData().m_278676_(loc);
            List<ItemStack> loots = loottable.getRandomItems(new LootParams.Builder((ServerLevel) player.m_9236_()).withParameter(LootContextParams.THIS_ENTITY, player).create(LootContextParamSets.EMPTY));
            return loots.isEmpty() ? ItemStack.EMPTY : (ItemStack) loots.get(0);
        }
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.totalTransmuteCount = tag.getInt("TotalCount");
        ListTag list = new ListTag();
        for (Entry<UUID, TransmutationData> entry : this.playerToData.entrySet()) {
            CompoundTag innerTag = new CompoundTag();
            innerTag.putUUID("UUID", (UUID) entry.getKey());
            innerTag.put("TransmutationData", ((TransmutationData) entry.getValue()).saveAsNBT());
            list.add(innerTag);
        }
        tag.put("PlayerTransmutationData", list);
        for (int i = 0; i < 3; i++) {
            if (tag.contains("Possibility" + i)) {
                this.possiblities[i] = ItemStack.of(tag.getCompound("Possiblity" + i));
            }
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("TotalCount", this.totalTransmuteCount);
        ListTag list = tag.getList("PlayerTransmutationData", 10);
        if (!list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                CompoundTag compoundtag = list.getCompound(i);
                UUID uuid = compoundtag.getUUID("UUID");
                if (uuid != null) {
                    this.playerToData.put(uuid, TransmutationData.fromNBT(compoundtag.getCompound("TransmutationData")));
                }
            }
        }
        for (int ix = 0; ix < 3; ix++) {
            if (this.possiblities[ix] != null && !this.possiblities[ix].isEmpty()) {
                tag.put("Possiblity" + ix, this.possiblities[ix].serializeNBT());
            }
        }
    }

    private void randomizeResults(Player player) {
        this.rollPossiblity(player, 0);
        this.rollPossiblity(player, 1);
        this.rollPossiblity(player, 2);
        int dataIndex = RANDOM.nextInt(2);
        if (this.playerToData.containsKey(player.m_20148_()) && !AMConfig.limitTransmutingToLootTables) {
            TransmutationData data = (TransmutationData) this.playerToData.get(player.m_20148_());
            if ((double) RANDOM.nextFloat() < Math.min(0.01875F * data.getTotalWeight(), 0.2F)) {
                ItemStack stack = data.getRandomItem(RANDOM);
                if (stack != null && !stack.isEmpty()) {
                    this.possiblities[dataIndex] = stack;
                }
            }
        }
        AlexsMobs.sendMSGToAll(new MessageUpdateTransmutablesToDisplay(player.m_19879_(), this.possiblities[0], this.possiblities[1], this.possiblities[2]));
    }

    public void rollPossiblity(Player player, int i) {
        if (player != null && !player.m_9236_().isClientSide && player.m_9236_() instanceof ServerLevel) {
            int safeIndex = Mth.clamp(i, 0, 2);
            this.possiblities[safeIndex] = createFromLootTable(player, switch(safeIndex) {
                default ->
                    COMMON_ITEMS;
                case 1 ->
                    UNCOMMON_ITEMS;
                case 2 ->
                    RARE_ITEMS;
            });
        }
    }

    public boolean hasPossibilities() {
        for (int i = 0; i < 3; i++) {
            if (this.possiblities[i] == null || this.possiblities[i].isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public ItemStack getPossibility(int i) {
        int safeIndex = Mth.clamp(i, 0, 2);
        ItemStack possible = this.possiblities[safeIndex];
        return possible == null ? ItemStack.EMPTY : possible;
    }

    public void postTransmute(Player player, ItemStack from, ItemStack to) {
        TransmutationData data;
        if (this.playerToData.containsKey(player.m_20148_())) {
            data = (TransmutationData) this.playerToData.get(player.m_20148_());
        } else {
            data = new TransmutationData();
        }
        data.onTransmuteItem(from, to);
        this.playerToData.put(player.m_20148_(), data);
        this.totalTransmuteCount = this.totalTransmuteCount + from.getCount();
        if (player instanceof ServerPlayer && this.totalTransmuteCount >= 1000) {
            AMAdvancementTriggerRegistry.TRANSMUTE_1000_ITEMS.trigger((ServerPlayer) player);
        }
        this.setRerollPlayerUUID(player.m_20148_());
    }

    public void tick() {
        this.ticksExisted++;
        if (this.rerollPlayerUUID != null) {
            Player player = this.f_58857_.m_46003_(this.rerollPlayerUUID);
            if (player != null) {
                this.f_58857_.playSound(null, this.m_58899_(), AMSoundRegistry.TRANSMUTE_ITEM.get(), SoundSource.BLOCKS, 1.0F, 0.9F + player.m_217043_().nextFloat() * 0.2F);
                this.randomizeResults(player);
            }
            this.rerollPlayerUUID = null;
        }
    }

    public void setRerollPlayerUUID(UUID uuid) {
        this.rerollPlayerUUID = uuid;
    }
}
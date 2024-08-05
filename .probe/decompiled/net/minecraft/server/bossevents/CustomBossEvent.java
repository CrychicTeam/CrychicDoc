package net.minecraft.server.bossevents;

import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;

public class CustomBossEvent extends ServerBossEvent {

    private final ResourceLocation id;

    private final Set<UUID> players = Sets.newHashSet();

    private int value;

    private int max = 100;

    public CustomBossEvent(ResourceLocation resourceLocation0, Component component1) {
        super(component1, BossEvent.BossBarColor.WHITE, BossEvent.BossBarOverlay.PROGRESS);
        this.id = resourceLocation0;
        this.m_142711_(0.0F);
    }

    public ResourceLocation getTextId() {
        return this.id;
    }

    @Override
    public void addPlayer(ServerPlayer serverPlayer0) {
        super.addPlayer(serverPlayer0);
        this.players.add(serverPlayer0.m_20148_());
    }

    public void addOfflinePlayer(UUID uUID0) {
        this.players.add(uUID0);
    }

    @Override
    public void removePlayer(ServerPlayer serverPlayer0) {
        super.removePlayer(serverPlayer0);
        this.players.remove(serverPlayer0.m_20148_());
    }

    @Override
    public void removeAllPlayers() {
        super.removeAllPlayers();
        this.players.clear();
    }

    public int getValue() {
        return this.value;
    }

    public int getMax() {
        return this.max;
    }

    public void setValue(int int0) {
        this.value = int0;
        this.m_142711_(Mth.clamp((float) int0 / (float) this.max, 0.0F, 1.0F));
    }

    public void setMax(int int0) {
        this.max = int0;
        this.m_142711_(Mth.clamp((float) this.value / (float) int0, 0.0F, 1.0F));
    }

    public final Component getDisplayName() {
        return ComponentUtils.wrapInSquareBrackets(this.m_18861_()).withStyle(p_136276_ -> p_136276_.withColor(this.m_18862_().getFormatting()).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal(this.getTextId().toString()))).withInsertion(this.getTextId().toString()));
    }

    public boolean setPlayers(Collection<ServerPlayer> collectionServerPlayer0) {
        Set<UUID> $$1 = Sets.newHashSet();
        Set<ServerPlayer> $$2 = Sets.newHashSet();
        for (UUID $$3 : this.players) {
            boolean $$4 = false;
            for (ServerPlayer $$5 : collectionServerPlayer0) {
                if ($$5.m_20148_().equals($$3)) {
                    $$4 = true;
                    break;
                }
            }
            if (!$$4) {
                $$1.add($$3);
            }
        }
        for (ServerPlayer $$6 : collectionServerPlayer0) {
            boolean $$7 = false;
            for (UUID $$8 : this.players) {
                if ($$6.m_20148_().equals($$8)) {
                    $$7 = true;
                    break;
                }
            }
            if (!$$7) {
                $$2.add($$6);
            }
        }
        for (UUID $$9 : $$1) {
            for (ServerPlayer $$10 : this.m_8324_()) {
                if ($$10.m_20148_().equals($$9)) {
                    this.removePlayer($$10);
                    break;
                }
            }
            this.players.remove($$9);
        }
        for (ServerPlayer $$11 : $$2) {
            this.addPlayer($$11);
        }
        return !$$1.isEmpty() || !$$2.isEmpty();
    }

    public CompoundTag save() {
        CompoundTag $$0 = new CompoundTag();
        $$0.putString("Name", Component.Serializer.toJson(this.f_18840_));
        $$0.putBoolean("Visible", this.m_8323_());
        $$0.putInt("Value", this.value);
        $$0.putInt("Max", this.max);
        $$0.putString("Color", this.m_18862_().getName());
        $$0.putString("Overlay", this.m_18863_().getName());
        $$0.putBoolean("DarkenScreen", this.m_18864_());
        $$0.putBoolean("PlayBossMusic", this.m_18865_());
        $$0.putBoolean("CreateWorldFog", this.m_18866_());
        ListTag $$1 = new ListTag();
        for (UUID $$2 : this.players) {
            $$1.add(NbtUtils.createUUID($$2));
        }
        $$0.put("Players", $$1);
        return $$0;
    }

    public static CustomBossEvent load(CompoundTag compoundTag0, ResourceLocation resourceLocation1) {
        CustomBossEvent $$2 = new CustomBossEvent(resourceLocation1, Component.Serializer.fromJson(compoundTag0.getString("Name")));
        $$2.m_8321_(compoundTag0.getBoolean("Visible"));
        $$2.setValue(compoundTag0.getInt("Value"));
        $$2.setMax(compoundTag0.getInt("Max"));
        $$2.m_6451_(BossEvent.BossBarColor.byName(compoundTag0.getString("Color")));
        $$2.m_5648_(BossEvent.BossBarOverlay.byName(compoundTag0.getString("Overlay")));
        $$2.m_7003_(compoundTag0.getBoolean("DarkenScreen"));
        $$2.m_7005_(compoundTag0.getBoolean("PlayBossMusic"));
        $$2.m_7006_(compoundTag0.getBoolean("CreateWorldFog"));
        ListTag $$3 = compoundTag0.getList("Players", 11);
        for (int $$4 = 0; $$4 < $$3.size(); $$4++) {
            $$2.addOfflinePlayer(NbtUtils.loadUUID($$3.get($$4)));
        }
        return $$2;
    }

    public void onPlayerConnect(ServerPlayer serverPlayer0) {
        if (this.players.contains(serverPlayer0.m_20148_())) {
            this.addPlayer(serverPlayer0);
        }
    }

    public void onPlayerDisconnect(ServerPlayer serverPlayer0) {
        super.removePlayer(serverPlayer0);
    }
}
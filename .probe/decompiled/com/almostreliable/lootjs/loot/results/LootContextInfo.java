package com.almostreliable.lootjs.loot.results;

import com.almostreliable.lootjs.LootJSPlatform;
import com.almostreliable.lootjs.LootModificationsAPI;
import com.almostreliable.lootjs.core.ILootContextData;
import com.almostreliable.lootjs.core.LootJSParamSets;
import com.almostreliable.lootjs.util.LootContextUtils;
import com.almostreliable.lootjs.util.Utils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import javax.annotation.Nullable;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

public class LootContextInfo {

    LootInfoCollector collector = new LootInfoCollector();

    private LootContextInfo() {
    }

    @Nullable
    public static LootContextInfo create(LootContext context) {
        if (!LootModificationsAPI.LOOT_MODIFICATION_LOGGING) {
            return null;
        } else {
            LootContextInfo lci = new LootContextInfo();
            lci.add("LootTable", Utils.quote(LootJSPlatform.INSTANCE.getQueriedLootTableId(context)));
            ILootContextData data = context.getParamOrNull(LootJSParamSets.DATA);
            if (data != null) {
                lci.add("LootType", data.getLootContextType().name());
                lci.updateLoot(data.getGeneratedLoot());
            }
            Vec3 origin = context.getParamOrNull(LootContextParams.ORIGIN);
            lci.addOptional("Position", origin, Utils::formatPosition);
            lci.addOptional("Block", context.getParamOrNull(LootContextParams.BLOCK_STATE));
            lci.addOptional("Explosion", context.getParamOrNull(LootContextParams.EXPLOSION_RADIUS));
            lci.addOptional("Entity", context.getParamOrNull(LootContextParams.THIS_ENTITY), Utils::formatEntity);
            lci.addOptional("Killer Entity", context.getParamOrNull(LootContextParams.KILLER_ENTITY), Utils::formatEntity);
            lci.addOptional("Direct Killer", context.getParamOrNull(LootContextParams.DIRECT_KILLER_ENTITY), Utils::formatEntity);
            ServerPlayer player = LootContextUtils.getPlayerOrNull(context);
            if (player != null) {
                lci.addOptional("Player", player, Utils::formatEntity);
                lci.addOptional("Player Pos", player.m_20182_(), Utils::formatPosition);
                if (origin != null) {
                    lci.addOptional("Distance", String.format("%.2f", player.m_20182_().distanceTo(origin)));
                }
                lci.addItem("MainHand", player.m_6844_(EquipmentSlot.MAINHAND));
                lci.addItem("OffHand", player.m_6844_(EquipmentSlot.OFFHAND));
                lci.addItem("Head", player.m_6844_(EquipmentSlot.HEAD));
                lci.addItem("Chest", player.m_6844_(EquipmentSlot.CHEST));
                lci.addItem("Legs", player.m_6844_(EquipmentSlot.LEGS));
                lci.addItem("Feet", player.m_6844_(EquipmentSlot.FEET));
            }
            return lci;
        }
    }

    private void add(String left, String right) {
        this.collector.addOrPush(new Info.RowInfo(left, right));
    }

    private <T> void addOptional(String left, @Nullable T t, Function<T, String> formatter) {
        if (t != null) {
            this.add(left, (String) formatter.apply(t));
        }
    }

    private <T> void addOptional(String left, @Nullable T t) {
        if (t != null) {
            this.add(left, t.toString());
        }
    }

    private void addItem(String left, ItemStack itemStack) {
        if (!itemStack.isEmpty()) {
            this.add(left, Utils.formatItemStack(itemStack));
        }
    }

    public void updateLoot(Collection<ItemStack> loot) {
        LootContextInfo.LootComposite lootComposite = (LootContextInfo.LootComposite) this.collector.getFirstLayer().stream().filter(LootContextInfo.LootComposite.class::isInstance).findFirst().orElse(null);
        if (lootComposite == null) {
            LootContextInfo.LootComposite lc = new LootContextInfo.LootComposite();
            this.collector.add(lc);
            this.updateLoot(loot, lc.getBefore());
        } else {
            this.updateLoot(loot, lootComposite.getAfter());
        }
    }

    private void updateLoot(Collection<ItemStack> loot, Info.Composite composite) {
        for (ItemStack itemStack : loot) {
            composite.addChildren(new Info.TitledInfo(Utils.formatItemStack(itemStack)));
        }
    }

    public LootInfoCollector getCollector() {
        return this.collector;
    }

    public static class LootComposite extends Info.Composite {

        private final Info.Composite before = new Info.Composite("Before");

        private final Info.Composite after = new Info.Composite("After");

        public LootComposite() {
            super("Loot");
            this.children.add(this.before);
            this.children.add(this.after);
        }

        @Override
        public void addChildren(Info info) {
            throw new UnsupportedOperationException("LootComposite cannot add custom children");
        }

        @Override
        public Collection<Info> getChildren() {
            List<Info> c = new ArrayList();
            c.add(this.before.getChildren().isEmpty() ? new Info.TitledInfo("before {}") : this.before);
            c.add(this.after.getChildren().isEmpty() ? new Info.TitledInfo("after {}") : this.after);
            return c;
        }

        public Info.Composite getBefore() {
            return this.before;
        }

        public Info.Composite getAfter() {
            return this.after;
        }
    }
}
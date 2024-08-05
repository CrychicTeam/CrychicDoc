package dev.xkmc.modulargolems.content.item.card;

import dev.xkmc.l2library.util.nbt.ItemCompoundTag;
import dev.xkmc.modulargolems.init.data.MGLangData;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class UuidFilterCard extends ClickEntityFilterCard<UUID> {

    private static final String KEY = "idList";

    public UuidFilterCard(Item.Properties properties) {
        super(properties);
    }

    protected UUID getValue(LivingEntity entity) {
        return entity.m_20148_();
    }

    protected Component getName(UUID uuid) {
        return Component.literal(uuid.toString().substring(0, 8));
    }

    @Override
    public List<UUID> getList(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        List<UUID> ans = new ArrayList();
        if (tag != null && tag.contains("idList")) {
            for (Tag e : tag.getList("idList", 11)) {
                ans.add(NbtUtils.loadUUID(e));
            }
            return ans;
        } else {
            return ans;
        }
    }

    @Override
    public void setList(ItemStack stack, List<UUID> list) {
        ListTag tag = ItemCompoundTag.of(stack).getSubList("idList", 11).getOrCreate();
        tag.clear();
        for (UUID uuid : list) {
            tag.add(NbtUtils.createUUID(uuid));
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
        List<UUID> ids = this.getList(stack);
        if (ids.size() > 0 && !Screen.hasShiftDown()) {
            for (UUID e : ids) {
                list.add(this.getName(e));
            }
            list.add(MGLangData.TARGET_SHIFT.get());
        } else {
            list.add(MGLangData.TARGET_UUID_ADD.get());
            list.add(MGLangData.TARGET_UUID_REMOVE.get());
            list.add(MGLangData.TARGET_REMOVE.get());
        }
    }
}
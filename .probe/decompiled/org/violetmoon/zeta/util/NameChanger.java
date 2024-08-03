package org.violetmoon.zeta.util;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class NameChanger {

    protected static Map<Block, String> originalBlockNames = new IdentityHashMap();

    protected static Map<Block, NameChanger.NameChangeRequests> changedBlockNames = new IdentityHashMap();

    protected static Map<Item, String> originalItemNames = new IdentityHashMap();

    protected static Map<Item, NameChanger.NameChangeRequests> changedItemNames = new IdentityHashMap();

    public void changeBlock(Block toChange, String newTranslationKey, boolean enabled) {
        changeBlockStatic(toChange, newTranslationKey, enabled);
    }

    public void changeItem(Item toChange, String newTranslationKey, boolean enabled) {
        changeItemStatic(toChange, newTranslationKey, enabled);
    }

    protected static synchronized void changeBlockStatic(Block toChange, String newTranslationKey, boolean enabled) {
        originalBlockNames.computeIfAbsent(toChange, Block::m_7705_);
        NameChanger.NameChangeRequests changeRequests = (NameChanger.NameChangeRequests) changedBlockNames.computeIfAbsent(toChange, __ -> new NameChanger.NameChangeRequests());
        if (enabled) {
            changeRequests.add(newTranslationKey);
        } else {
            changeRequests.remove(newTranslationKey);
        }
        toChange.descriptionId = changeRequests.lastOrElse((String) originalBlockNames.get(toChange));
        if (changeRequests.isEmpty()) {
            changedBlockNames.remove(toChange);
        }
    }

    protected static synchronized void changeItemStatic(Item toChange, String newTranslationKey, boolean enabled) {
        originalItemNames.computeIfAbsent(toChange, Item::m_5524_);
        NameChanger.NameChangeRequests changeRequests = (NameChanger.NameChangeRequests) changedItemNames.computeIfAbsent(toChange, __ -> new NameChanger.NameChangeRequests());
        if (enabled) {
            changeRequests.add(newTranslationKey);
        } else {
            changeRequests.remove(newTranslationKey);
        }
        toChange.descriptionId = changeRequests.lastOrElse((String) originalItemNames.get(toChange));
        if (changeRequests.isEmpty()) {
            changedItemNames.remove(toChange);
        }
    }

    protected static class NameChangeRequests {

        List<String> list = new ArrayList(1);

        public void add(String value) {
            this.remove(value);
            this.list.add(value);
        }

        public void remove(String value) {
            this.list.remove(value);
        }

        public boolean isEmpty() {
            return this.list.isEmpty();
        }

        public String lastOrElse(String orElse) {
            return this.list.isEmpty() ? orElse : (String) this.list.get(this.list.size() - 1);
        }
    }
}
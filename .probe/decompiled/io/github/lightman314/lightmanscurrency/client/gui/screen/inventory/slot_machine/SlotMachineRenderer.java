package io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.slot_machine;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import io.github.lightman314.lightmanscurrency.LCConfig;
import io.github.lightman314.lightmanscurrency.api.misc.IEasyTickable;
import io.github.lightman314.lightmanscurrency.api.misc.client.rendering.EasyGuiGraphics;
import io.github.lightman314.lightmanscurrency.api.network.LazyPacketData;
import io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.SlotMachineScreen;
import io.github.lightman314.lightmanscurrency.common.menus.SlotMachineMenu;
import io.github.lightman314.lightmanscurrency.common.traders.slot_machine.SlotMachineEntry;
import io.github.lightman314.lightmanscurrency.common.traders.slot_machine.SlotMachineTraderData;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import net.minecraft.client.gui.Font;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;

public final class SlotMachineRenderer implements IEasyTickable {

    public static final ResourceLocation GUI_TEXTURE = new ResourceLocation("lightmanscurrency", "textures/gui/container/slot_machine_overlay.png");

    private final SlotMachineScreen screen;

    private final SlotMachineMenu menu;

    private int animationTick = 0;

    private int restTick = 0;

    public final NonNullList<SlotMachineLine> lines = NonNullList.create();

    private final List<SlotMachineRenderBlock> possibleBlocks = new ArrayList();

    private int totalWeight = 0;

    public Font getFont() {
        return this.screen.getFont();
    }

    private SlotMachineTraderData getTrader() {
        return this.menu.getTrader();
    }

    private RandomSource getRandom() {
        return this.menu.player.m_9236_().random;
    }

    public static int GetAnimationTime() {
        return Math.max(LCConfig.CLIENT.slotMachineAnimationTime.get(), 20);
    }

    public static int GetRestTime() {
        return Math.max(LCConfig.CLIENT.slotMachineAnimationRestTime.get(), 1);
    }

    public SlotMachineRenderer(SlotMachineScreen screen) {
        this.screen = screen;
        this.menu = (SlotMachineMenu) this.screen.m_6262_();
        this.recollectPossibleBlocks();
        this.initializeLines();
    }

    public SlotMachineRenderBlock getRandomBlock() {
        if (this.totalWeight <= 0) {
            return SlotMachineRenderBlock.empty();
        } else {
            int rand = this.getRandom().nextInt(this.totalWeight) + 1;
            for (SlotMachineRenderBlock block : this.possibleBlocks) {
                rand -= block.weight;
                if (rand <= 0) {
                    return block;
                }
            }
            return SlotMachineRenderBlock.empty();
        }
    }

    private void recollectPossibleBlocks() {
        SlotMachineTraderData trader = this.getTrader();
        this.possibleBlocks.clear();
        this.totalWeight = 0;
        if (trader != null) {
            for (SlotMachineEntry entry : trader.getValidEntries()) {
                for (ItemStack item : entry.items) {
                    this.possibleBlocks.add(SlotMachineRenderBlock.forItem(entry.getWeight(), item));
                    this.totalWeight = this.totalWeight + entry.getWeight();
                }
            }
        }
        if (this.possibleBlocks.isEmpty()) {
            this.possibleBlocks.add(SlotMachineRenderBlock.empty());
        }
    }

    private void initializeLines() {
        while (this.lines.size() < 4) {
            this.lines.add(new SlotMachineLine(this));
        }
        SlotMachineTraderData trader = this.getTrader();
        if (trader != null) {
            List<ItemStack> previousRewards = SlotMachineEntry.splitDisplayItems(trader.getLastRewards());
            for (int i = 0; i < 4; i++) {
                if (i < previousRewards.size()) {
                    this.lines.get(i).initialize(SlotMachineRenderBlock.forItem(0, (ItemStack) previousRewards.get(i)));
                } else {
                    this.lines.get(i).initialize();
                }
            }
        } else {
            for (SlotMachineLine line : this.lines) {
                line.initialize();
            }
        }
    }

    @Override
    public void tick() {
        SlotMachineTraderData trader = this.getTrader();
        if (trader != null && trader.areEntriesChanged()) {
            this.recollectPossibleBlocks();
        }
        if (this.menu.hasPendingReward() && this.animationTick == 0) {
            this.startAnimation();
            this.animationTick++;
        } else if (this.animationTick > 0) {
            if (this.animationTick >= GetAnimationTime()) {
                this.restTick++;
                if (this.restTick >= GetRestTime()) {
                    this.animationTick = 0;
                    this.restTick = 0;
                    if (!this.menu.hasPendingReward()) {
                        this.menu.SendMessageToServer(LazyPacketData.builder().setBoolean("AnimationsCompleted", true));
                    }
                }
            } else {
                this.animationTick();
            }
        }
    }

    private void animationTick() {
        this.animationTick++;
        for (SlotMachineLine line : this.lines) {
            line.animationTick();
        }
        if (this.animationTick >= GetAnimationTime()) {
            this.menu.getAndRemoveNextReward();
            this.menu.SendMessageToServer(LazyPacketData.builder().setBoolean("GiveNextReward", true));
        }
    }

    private void startAnimation() {
        SlotMachineMenu.RewardCache pendingReward = this.menu.getNextReward();
        List<SlotMachineRenderBlock> resultBlocks = new ArrayList(4);
        List<ItemStack> displayItems = SlotMachineEntry.splitDisplayItems(pendingReward.getDisplayItems());
        for (int i = 0; i < 4; i++) {
            if (i < displayItems.size()) {
                ItemStack item = (ItemStack) displayItems.get(i);
                if (item.isEmpty()) {
                    resultBlocks.add(SlotMachineRenderBlock.empty());
                } else {
                    resultBlocks.add(SlotMachineRenderBlock.forItem(0, item));
                }
            } else {
                resultBlocks.add(SlotMachineRenderBlock.empty());
            }
        }
        for (SlotMachineLine line : this.lines) {
            line.unlock();
        }
        RandomSource rand = this.getRandom();
        List<SlotMachineLine> randomLines = new ArrayList(this.lines);
        List<Integer> lockDelay = Lists.newArrayList(new Integer[] { GetAnimationTime() - 1 });
        while (lockDelay.size() < randomLines.size()) {
            lockDelay.add(rand.nextInt(GetAnimationTime() - 11, GetAnimationTime() - 1));
        }
        while (!randomLines.isEmpty()) {
            SlotMachineLine line = randomLines.size() > 1 ? (SlotMachineLine) randomLines.remove(rand.nextInt(randomLines.size())) : (SlotMachineLine) randomLines.remove(0);
            line.lockAtResult((SlotMachineRenderBlock) resultBlocks.remove(0), (Integer) lockDelay.remove(0));
        }
    }

    public void render(@Nonnull EasyGuiGraphics gui) {
        int startX = this.screen.getXSize() / 2 - 4 * SlotMachineLine.BLOCK_SIZE / 2;
        int y = 10;
        for (int i = 0; i < 4; i++) {
            this.lines.get(i).render(gui, startX, y);
            startX += SlotMachineLine.BLOCK_SIZE;
        }
        RenderSystem.setShaderTexture(0, GUI_TEXTURE);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        gui.pushPose().TranslateToForeground();
        gui.blit(GUI_TEXTURE, 0, 0, 0, 0, this.screen.getXSize(), this.screen.getYSize());
        gui.popPose();
    }
}
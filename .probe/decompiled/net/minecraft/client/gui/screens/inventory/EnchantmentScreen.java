package net.minecraft.client.gui.screens.inventory;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.model.BookModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

public class EnchantmentScreen extends AbstractContainerScreen<EnchantmentMenu> {

    private static final ResourceLocation ENCHANTING_TABLE_LOCATION = new ResourceLocation("textures/gui/container/enchanting_table.png");

    private static final ResourceLocation ENCHANTING_BOOK_LOCATION = new ResourceLocation("textures/entity/enchanting_table_book.png");

    private final RandomSource random = RandomSource.create();

    private BookModel bookModel;

    public int time;

    public float flip;

    public float oFlip;

    public float flipT;

    public float flipA;

    public float open;

    public float oOpen;

    private ItemStack last = ItemStack.EMPTY;

    public EnchantmentScreen(EnchantmentMenu enchantmentMenu0, Inventory inventory1, Component component2) {
        super(enchantmentMenu0, inventory1, component2);
    }

    @Override
    protected void init() {
        super.init();
        this.bookModel = new BookModel(this.f_96541_.getEntityModels().bakeLayer(ModelLayers.BOOK));
    }

    @Override
    public void containerTick() {
        super.containerTick();
        this.tickBook();
    }

    @Override
    public boolean mouseClicked(double double0, double double1, int int2) {
        int $$3 = (this.f_96543_ - this.f_97726_) / 2;
        int $$4 = (this.f_96544_ - this.f_97727_) / 2;
        for (int $$5 = 0; $$5 < 3; $$5++) {
            double $$6 = double0 - (double) ($$3 + 60);
            double $$7 = double1 - (double) ($$4 + 14 + 19 * $$5);
            if ($$6 >= 0.0 && $$7 >= 0.0 && $$6 < 108.0 && $$7 < 19.0 && ((EnchantmentMenu) this.f_97732_).clickMenuButton(this.f_96541_.player, $$5)) {
                this.f_96541_.gameMode.handleInventoryButtonClick(((EnchantmentMenu) this.f_97732_).f_38840_, $$5);
                return true;
            }
        }
        return super.mouseClicked(double0, double1, int2);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics0, float float1, int int2, int int3) {
        int $$4 = (this.f_96543_ - this.f_97726_) / 2;
        int $$5 = (this.f_96544_ - this.f_97727_) / 2;
        guiGraphics0.blit(ENCHANTING_TABLE_LOCATION, $$4, $$5, 0, 0, this.f_97726_, this.f_97727_);
        this.renderBook(guiGraphics0, $$4, $$5, float1);
        EnchantmentNames.getInstance().initSeed((long) ((EnchantmentMenu) this.f_97732_).getEnchantmentSeed());
        int $$6 = ((EnchantmentMenu) this.f_97732_).getGoldCount();
        for (int $$7 = 0; $$7 < 3; $$7++) {
            int $$8 = $$4 + 60;
            int $$9 = $$8 + 20;
            int $$10 = ((EnchantmentMenu) this.f_97732_).costs[$$7];
            if ($$10 == 0) {
                guiGraphics0.blit(ENCHANTING_TABLE_LOCATION, $$8, $$5 + 14 + 19 * $$7, 0, 185, 108, 19);
            } else {
                String $$11 = $$10 + "";
                int $$12 = 86 - this.f_96547_.width($$11);
                FormattedText $$13 = EnchantmentNames.getInstance().getRandomName(this.f_96547_, $$12);
                int $$14 = 6839882;
                if (($$6 < $$7 + 1 || this.f_96541_.player.f_36078_ < $$10) && !this.f_96541_.player.m_150110_().instabuild) {
                    guiGraphics0.blit(ENCHANTING_TABLE_LOCATION, $$8, $$5 + 14 + 19 * $$7, 0, 185, 108, 19);
                    guiGraphics0.blit(ENCHANTING_TABLE_LOCATION, $$8 + 1, $$5 + 15 + 19 * $$7, 16 * $$7, 239, 16, 16);
                    guiGraphics0.drawWordWrap(this.f_96547_, $$13, $$9, $$5 + 16 + 19 * $$7, $$12, ($$14 & 16711422) >> 1);
                    $$14 = 4226832;
                } else {
                    int $$15 = int2 - ($$4 + 60);
                    int $$16 = int3 - ($$5 + 14 + 19 * $$7);
                    if ($$15 >= 0 && $$16 >= 0 && $$15 < 108 && $$16 < 19) {
                        guiGraphics0.blit(ENCHANTING_TABLE_LOCATION, $$8, $$5 + 14 + 19 * $$7, 0, 204, 108, 19);
                        $$14 = 16777088;
                    } else {
                        guiGraphics0.blit(ENCHANTING_TABLE_LOCATION, $$8, $$5 + 14 + 19 * $$7, 0, 166, 108, 19);
                    }
                    guiGraphics0.blit(ENCHANTING_TABLE_LOCATION, $$8 + 1, $$5 + 15 + 19 * $$7, 16 * $$7, 223, 16, 16);
                    guiGraphics0.drawWordWrap(this.f_96547_, $$13, $$9, $$5 + 16 + 19 * $$7, $$12, $$14);
                    $$14 = 8453920;
                }
                guiGraphics0.drawString(this.f_96547_, $$11, $$9 + 86 - this.f_96547_.width($$11), $$5 + 16 + 19 * $$7 + 7, $$14);
            }
        }
    }

    private void renderBook(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        float $$4 = Mth.lerp(float3, this.oOpen, this.open);
        float $$5 = Mth.lerp(float3, this.oFlip, this.flip);
        Lighting.setupForEntityInInventory();
        guiGraphics0.pose().pushPose();
        guiGraphics0.pose().translate((float) int1 + 33.0F, (float) int2 + 31.0F, 100.0F);
        float $$6 = 40.0F;
        guiGraphics0.pose().scale(-40.0F, 40.0F, 40.0F);
        guiGraphics0.pose().mulPose(Axis.XP.rotationDegrees(25.0F));
        guiGraphics0.pose().translate((1.0F - $$4) * 0.2F, (1.0F - $$4) * 0.1F, (1.0F - $$4) * 0.25F);
        float $$7 = -(1.0F - $$4) * 90.0F - 90.0F;
        guiGraphics0.pose().mulPose(Axis.YP.rotationDegrees($$7));
        guiGraphics0.pose().mulPose(Axis.XP.rotationDegrees(180.0F));
        float $$8 = Mth.clamp(Mth.frac($$5 + 0.25F) * 1.6F - 0.3F, 0.0F, 1.0F);
        float $$9 = Mth.clamp(Mth.frac($$5 + 0.75F) * 1.6F - 0.3F, 0.0F, 1.0F);
        this.bookModel.setupAnim(0.0F, $$8, $$9, $$4);
        VertexConsumer $$10 = guiGraphics0.bufferSource().getBuffer(this.bookModel.m_103119_(ENCHANTING_BOOK_LOCATION));
        this.bookModel.renderToBuffer(guiGraphics0.pose(), $$10, 15728880, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        guiGraphics0.flush();
        guiGraphics0.pose().popPose();
        Lighting.setupFor3DItems();
    }

    @Override
    public void render(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        float3 = this.f_96541_.getFrameTime();
        this.m_280273_(guiGraphics0);
        super.render(guiGraphics0, int1, int2, float3);
        this.m_280072_(guiGraphics0, int1, int2);
        boolean $$4 = this.f_96541_.player.m_150110_().instabuild;
        int $$5 = ((EnchantmentMenu) this.f_97732_).getGoldCount();
        for (int $$6 = 0; $$6 < 3; $$6++) {
            int $$7 = ((EnchantmentMenu) this.f_97732_).costs[$$6];
            Enchantment $$8 = Enchantment.byId(((EnchantmentMenu) this.f_97732_).enchantClue[$$6]);
            int $$9 = ((EnchantmentMenu) this.f_97732_).levelClue[$$6];
            int $$10 = $$6 + 1;
            if (this.m_6774_(60, 14 + 19 * $$6, 108, 17, (double) int1, (double) int2) && $$7 > 0 && $$9 >= 0 && $$8 != null) {
                List<Component> $$11 = Lists.newArrayList();
                $$11.add(Component.translatable("container.enchant.clue", $$8.getFullname($$9)).withStyle(ChatFormatting.WHITE));
                if (!$$4) {
                    $$11.add(CommonComponents.EMPTY);
                    if (this.f_96541_.player.f_36078_ < $$7) {
                        $$11.add(Component.translatable("container.enchant.level.requirement", ((EnchantmentMenu) this.f_97732_).costs[$$6]).withStyle(ChatFormatting.RED));
                    } else {
                        MutableComponent $$12;
                        if ($$10 == 1) {
                            $$12 = Component.translatable("container.enchant.lapis.one");
                        } else {
                            $$12 = Component.translatable("container.enchant.lapis.many", $$10);
                        }
                        $$11.add($$12.withStyle($$5 >= $$10 ? ChatFormatting.GRAY : ChatFormatting.RED));
                        MutableComponent $$14;
                        if ($$10 == 1) {
                            $$14 = Component.translatable("container.enchant.level.one");
                        } else {
                            $$14 = Component.translatable("container.enchant.level.many", $$10);
                        }
                        $$11.add($$14.withStyle(ChatFormatting.GRAY));
                    }
                }
                guiGraphics0.renderComponentTooltip(this.f_96547_, $$11, int1, int2);
                break;
            }
        }
    }

    public void tickBook() {
        ItemStack $$0 = ((EnchantmentMenu) this.f_97732_).m_38853_(0).getItem();
        if (!ItemStack.matches($$0, this.last)) {
            this.last = $$0;
            do {
                this.flipT = this.flipT + (float) (this.random.nextInt(4) - this.random.nextInt(4));
            } while (this.flip <= this.flipT + 1.0F && this.flip >= this.flipT - 1.0F);
        }
        this.time++;
        this.oFlip = this.flip;
        this.oOpen = this.open;
        boolean $$1 = false;
        for (int $$2 = 0; $$2 < 3; $$2++) {
            if (((EnchantmentMenu) this.f_97732_).costs[$$2] != 0) {
                $$1 = true;
            }
        }
        if ($$1) {
            this.open += 0.2F;
        } else {
            this.open -= 0.2F;
        }
        this.open = Mth.clamp(this.open, 0.0F, 1.0F);
        float $$3 = (this.flipT - this.flip) * 0.4F;
        float $$4 = 0.2F;
        $$3 = Mth.clamp($$3, -0.2F, 0.2F);
        this.flipA = this.flipA + ($$3 - this.flipA) * 0.9F;
        this.flip = this.flip + this.flipA;
    }
}
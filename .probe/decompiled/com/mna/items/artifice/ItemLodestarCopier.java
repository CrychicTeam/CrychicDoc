package com.mna.items.artifice;

import com.mna.ManaAndArtifice;
import com.mna.api.capabilities.IPlayerProgression;
import com.mna.api.items.TieredItem;
import com.mna.blocks.artifice.LodestarBlock;
import com.mna.blocks.decoration.ParticleEmitterBlock;
import com.mna.blocks.tileentities.LodestarTile;
import com.mna.blocks.tileentities.ParticleEmitterTile;
import com.mna.capabilities.playerdata.progression.PlayerProgressionProvider;
import com.mna.inventory.ItemInventoryBase;
import com.mna.items.ItemInit;
import com.mna.items.base.IRadialInventorySelect;
import com.mna.network.ClientMessageDispatcher;
import com.mna.network.ServerMessageDispatcher;
import com.mojang.brigadier.StringReader;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.lwjgl.glfw.GLFW;

public class ItemLodestarCopier extends TieredItem implements IRadialInventorySelect {

    private static final String NBT_MODE = "copy_mode";

    private static final String NBT_LODESTAR_DATA = "stored_lodestar";

    private static final String NBT_PARTICLE_EMITTER_DATA = "stored_particle_data";

    private static final String NBT_TEMPLATE_1 = "{commands:[{connections:[{index:0,target:\"9c09665d-f5ef-40e7-a9d5-402fff7a1108\"},{index:1,target:\"9ef04eaa-443f-4de6-99bb-d9a28f097b36\"}],id:\"7395fa1f-f10b-4b20-877d-419ad8adfb10\",parameters:[{parameter_id:\"chop.area\"}],start:1b,task:\"mna:chop\",x:31,y:56},{connections:[{index:0,target:\"a0a2150c-4172-4de8-aa26-3d3ea823eabe\"},{index:1,target:\"a0a2150c-4172-4de8-aa26-3d3ea823eabe\"}],id:\"9c09665d-f5ef-40e7-a9d5-402fff7a1108\",parameters:[{parameter_id:\"collect.area\"},{filter:{blacklist:{Items:[]},blacklist_damage:0b,blacklist_tag:0b,whitelist:{Items:[]},whitelist_damage:0b,whitelist_tag:0b},parameter_id:\"collect.filter\"},{Count:0b,id:\"minecraft:air\",parameter_id:\"collect.filter_single\"}],start:0b,task:\"mna:gather_items\",x:106,y:39},{connections:[],id:\"a0a2150c-4172-4de8-aa26-3d3ea823eabe\",parameters:[{parameter_id:\"teinteract.point\",point:{}}],start:0b,task:\"mna:place_item\",x:228,y:43},{connections:[{index:0,target:\"a7d52990-77a8-4899-9b0c-8ad3fd0b38f9\"}],id:\"9ef04eaa-443f-4de6-99bb-d9a28f097b36\",parameters:[{parameter_id:\"teinteract.point\",point:{}},{filter:{blacklist:{Items:[]},blacklist_damage:0b,blacklist_tag:0b,whitelist:{Items:[]},whitelist_damage:0b,whitelist_tag:0b},parameter_id:\"take.filter\"},{Count:1b,id:\"minecraft:oak_sapling\",parameter_id:\"take.stack\"},{parameter_id:\"take.random\",value:0b},{max:128,min:0,parameter_id:\"take.quantity\",value:0}],start:0b,task:\"mna:take_item\",x:98,y:131},{connections:[{index:0,target:\"a0a2150c-4172-4de8-aa26-3d3ea823eabe\"},{index:1,target:\"a0a2150c-4172-4de8-aa26-3d3ea823eabe\"}],id:\"a7d52990-77a8-4899-9b0c-8ad3fd0b38f9\",parameters:[{parameter_id:\"plant.area\"}],start:0b,task:\"mna:plant\",x:191,y:130}],groups:[{h:64,l:\"Chop Success, Collect Items\",w:196,x:87,y:30},{h:55,l:\"Chop Failed, Try Replanting\",w:175,x:93,y:123}]}";

    private static final String NBT_TEMPLATE_2 = "{commands:[{connections:[{index:0,target:\"f8f495c4-2c30-4451-b11f-d39ea6035ba7\"},{index:1,target:\"7d81cf64-cc5d-44d5-a7cb-f161383cff29\"}],id:\"196c85eb-e705-494d-95f4-377a336350dc\",parameters:[{parameter_id:\"teinteract.point\",point:{}},{filter:{blacklist:{Items:[]},blacklist_damage:0b,blacklist_tag:0b,whitelist:{Items:[]},whitelist_damage:0b,whitelist_tag:0b},parameter_id:\"take.filter\"},{Count:1b,id:\"minecraft:wheat_seeds\",parameter_id:\"take.stack\"},{parameter_id:\"take.random\",value:0b},{max:128,min:0,parameter_id:\"take.quantity\",value:0}],start:1b,task:\"mna:take_item\",x:59,y:62},{connections:[{index:0,target:\"f0b90d1e-a751-4539-be5c-7cffa7da9bb0\"},{index:1,target:\"7d81cf64-cc5d-44d5-a7cb-f161383cff29\"}],id:\"f8f495c4-2c30-4451-b11f-d39ea6035ba7\",parameters:[{parameter_id:\"plant.area\"}],start:0b,task:\"mna:plant\",x:130,y:63},{connections:[],id:\"f0b90d1e-a751-4539-be5c-7cffa7da9bb0\",parameters:[{parameter_id:\"teinteract.point\",point:{}}],start:0b,task:\"mna:place_item\",x:253,y:58},{connections:[{index:0,target:\"4badc1b5-383b-4ef1-9b15-9b60ce1af97d\"},{index:1,target:\"4badc1b5-383b-4ef1-9b15-9b60ce1af97d\"}],id:\"7d81cf64-cc5d-44d5-a7cb-f161383cff29\",parameters:[{parameter_id:\"harvest.area\"}],start:0b,task:\"mna:harvest\",x:123,y:148},{connections:[{index:0,target:\"f0b90d1e-a751-4539-be5c-7cffa7da9bb0\"},{index:1,target:\"f0b90d1e-a751-4539-be5c-7cffa7da9bb0\"}],id:\"4badc1b5-383b-4ef1-9b15-9b60ce1af97d\",parameters:[{parameter_id:\"collect.area\"},{filter:{blacklist:{Items:[]},blacklist_damage:0b,blacklist_tag:0b,whitelist:{Items:[]},whitelist_damage:0b,whitelist_tag:0b},parameter_id:\"collect.filter\"},{Count:0b,id:\"minecraft:air\",parameter_id:\"collect.filter_single\"}],start:0b,task:\"mna:gather_items\",x:181,y:148}],groups:[{h:56,l:\"Got a seed, plant it and put away the excess\",w:92,x:118,y:58},{h:63,l:\"Didn't get a seed or couldn't plant, harvest and collect\",w:158,x:117,y:133},{h:50,l:\"Always empty the inventory at the end before starting again.\",w:62,x:246,y:52}]}";

    private static final String NBT_TEMPLATE_3 = "{commands:[{connections:[{index:0,target:\"be5e6d6b-6b2f-442b-91bb-030a7862ff01\"}],id:\"04892f9b-d140-491a-8659-2c2f2217a505\",parameters:[{parameter_id:\"fish.point\",point:{}}],start:1b,task:\"mna:fish\",x:14,y:45},{connections:[{index:0,target:\"9911f330-5272-4a96-9734-5250a022c95d\"},{index:1,target:\"9911f330-5272-4a96-9734-5250a022c95d\"}],id:\"be5e6d6b-6b2f-442b-91bb-030a7862ff01\",parameters:[{parameter_id:\"collect.area\"},{filter:{blacklist:{Items:[]},blacklist_damage:0b,blacklist_tag:0b,whitelist:{Items:[]},whitelist_damage:0b,whitelist_tag:0b},parameter_id:\"collect.filter\"},{Count:0b,id:\"minecraft:air\",parameter_id:\"collect.filter_single\"}],start:0b,task:\"mna:gather_items\",x:74,y:46},{connections:[],id:\"9911f330-5272-4a96-9734-5250a022c95d\",parameters:[{parameter_id:\"teinteract.point\",point:{}}],start:0b,task:\"mna:place_item\",x:135,y:45}],groups:[]}";

    private static final String NBT_TEMPLATE_4 = "{commands:[{connections:[{index:0,target:\"d8114403-8eeb-4d9f-b3ac-899244273a22\"}],id:\"1228f099-772c-4225-b2e4-0129bbd59543\",parameters:[{parameter_id:\"teinteract.point\",point:{}},{filter:{blacklist:{Items:[]},blacklist_damage:0b,blacklist_tag:0b,whitelist:{Items:[]},whitelist_damage:0b,whitelist_tag:0b},parameter_id:\"take.filter\"},{Count:1b,id:\"mna:rune_pattern\",parameter_id:\"take.stack\"},{parameter_id:\"take.random\",value:0b},{max:128,min:0,parameter_id:\"take.quantity\",value:0}],start:1b,task:\"mna:take_item\",x:81,y:99},{connections:[{index:0,target:\"17dd34a9-909b-418c-ba15-dda990ebe149\"}],id:\"d8114403-8eeb-4d9f-b3ac-899244273a22\",parameters:[{parameter_id:\"teinteract.point\",point:{}}],start:0b,task:\"mna:place_item\",x:135,y:124},{connections:[{index:0,target:\"c521a2a4-1806-465e-b2f4-1b5ab54cf29c\"},{index:1,target:\"c521a2a4-1806-465e-b2f4-1b5ab54cf29c\"}],id:\"17dd34a9-909b-418c-ba15-dda990ebe149\",parameters:[{parameter_id:\"runescribe.point\",point:{}},{Count:0b,id:\"minecraft:air\",parameter_id:\"runescribe.recipe\"}],start:0b,task:\"mna:runescribe\",x:220,y:135},{connections:[{index:0,target:\"88cd05d0-fdb3-4247-8ebb-92afc9962973\"}],id:\"c521a2a4-1806-465e-b2f4-1b5ab54cf29c\",parameters:[{parameter_id:\"teinteract.point\",point:{}},{filter:{blacklist:{Items:[]},blacklist_damage:0b,blacklist_tag:0b,whitelist:{Items:[]},whitelist_damage:0b,whitelist_tag:0b},parameter_id:\"take.filter\"},{Count:0b,id:\"minecraft:air\",parameter_id:\"take.stack\"},{parameter_id:\"take.random\",value:0b},{max:128,min:0,parameter_id:\"take.quantity\",value:0}],start:0b,task:\"mna:take_item\",x:291,y:157},{connections:[],id:\"88cd05d0-fdb3-4247-8ebb-92afc9962973\",parameters:[{parameter_id:\"teinteract.point\",point:{}}],start:0b,task:\"mna:place_item\",x:341,y:182}],groups:[{h:77,l:\"Collect a rune and place it into a scribing table\",w:117,x:75,y:93},{h:50,l:\"Scribe the rune\",w:50,x:221,y:126},{h:75,l:\"Take the rune from the scribing table and put it away\",w:105,x:289,y:153}]}";

    public ItemLodestarCopier() {
        super(new Item.Properties().stacksTo(1).fireResistant().setNoRepair());
    }

    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        Level world = context.getLevel();
        BlockState clickedState = world.getBlockState(context.getClickedPos());
        if (!(clickedState.m_60734_() instanceof LodestarBlock) && !(clickedState.m_60734_() instanceof ParticleEmitterBlock)) {
            return InteractionResult.PASS;
        } else {
            if (!world.isClientSide) {
                if (isPasteMode(stack)) {
                    this.restoreLogic((ServerPlayer) context.getPlayer(), (ServerLevel) world, context.getClickedPos(), stack);
                } else {
                    this.copyLogic((ServerPlayer) context.getPlayer(), (ServerLevel) world, context.getClickedPos(), stack);
                }
            }
            return InteractionResult.sidedSuccess(world.isClientSide);
        }
    }

    @Override
    public boolean isFoil(ItemStack pStack) {
        return isPasteMode(pStack);
    }

    @Override
    public void appendHoverText(ItemStack pStack, Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        if (isPasteMode(pStack)) {
            Component tileEntityName = Component.translatable(isLodestarData(pStack) ? "block.mna.lodestar" : "block.mna.particle_emitter").withStyle(ChatFormatting.AQUA);
            pTooltipComponents.add(Component.translatable("item.mna.lodestar_copier.paste", tileEntityName).withStyle(ChatFormatting.AQUA));
        } else {
            pTooltipComponents.add(Component.translatable("item.mna.lodestar_copier.copy").withStyle(ChatFormatting.AQUA));
        }
        IRadialInventorySelect.super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    public void copyLogic(ServerPlayer player, ServerLevel world, BlockPos pos, ItemStack storeIn) {
        BlockEntity be = world.m_7702_(pos);
        if (be != null) {
            if (be instanceof LodestarTile) {
                CompoundTag tag = ((LodestarTile) be).getLogic();
                this.setCopiedLogic(tag, storeIn, false);
                player.sendSystemMessage(Component.translatable("block.mna.lodestar.logic_copied"));
            } else if (be instanceof ParticleEmitterTile) {
                CompoundTag tag = ((ParticleEmitterTile) be).getData().getTag();
                this.setCopiedLogic(tag, storeIn, true);
                player.sendSystemMessage(Component.translatable("block.mna.particle_emitter.data_copied"));
            }
        }
    }

    public void restoreLogic(ServerPlayer player, ServerLevel world, BlockPos pos, ItemStack readFrom) {
        if (readFrom.hasTag() && (readFrom.getTag().contains("stored_lodestar") || readFrom.getTag().contains("stored_particle_data"))) {
            BlockEntity be = world.m_7702_(pos);
            if (be != null) {
                if (be instanceof LodestarTile && readFrom.getOrCreateTag().contains("stored_lodestar")) {
                    CompoundTag tag = readFrom.getOrCreateTag().getCompound("stored_lodestar");
                    ((LodestarTile) be).setLogic(tag, true);
                    player.sendSystemMessage(Component.translatable("block.mna.lodestar.logic_restored"));
                } else if (be instanceof ParticleEmitterTile && readFrom.getOrCreateTag().contains("stored_particle_data")) {
                    CompoundTag tag = readFrom.getOrCreateTag().getCompound("stored_particle_data");
                    ((ParticleEmitterTile) be).setData(tag);
                    ServerMessageDispatcher.sendAuraSyncMessage((ParticleEmitterTile) be);
                    player.sendSystemMessage(Component.translatable("block.mna.particle_emitter.data_restored"));
                } else {
                    player.sendSystemMessage(Component.translatable("item.mna.lodestar_copier.no_data"));
                }
            }
        } else {
            player.sendSystemMessage(Component.translatable("item.mna.lodestar_copier.no_data"));
        }
    }

    public void setCopiedLogic(CompoundTag tag, ItemStack storeIn, boolean isParticleEmitter) {
        if (isParticleEmitter) {
            storeIn.getOrCreateTag().put("stored_particle_data", tag);
            storeIn.getOrCreateTag().remove("stored_lodestar");
        } else {
            storeIn.getOrCreateTag().put("stored_lodestar", tag);
            storeIn.getOrCreateTag().remove("stored_particle_data");
        }
        storeIn.getOrCreateTag().putInt("copy_mode", ItemLodestarCopier.Modes.Paste.ordinal());
    }

    private CompoundTag StringToTag(String data) throws Exception {
        StringReader reader = new StringReader(data);
        TagParser tagParser = new TagParser(reader);
        return tagParser.readStruct();
    }

    private void pasteTemplate(String templateData, ItemStack stack) {
        try {
            stack.getOrCreateTag().put("stored_lodestar", this.StringToTag(templateData));
        } catch (Exception var4) {
            ManaAndArtifice.LOGGER.error(var4);
        }
    }

    private ItemLodestarCopier.Modes[] getModesForPlayer(@Nullable Player player) {
        if (player == null) {
            return ItemLodestarCopier.Modes.values();
        } else {
            ArrayList<ItemLodestarCopier.Modes> list = new ArrayList();
            int tier = 0;
            IPlayerProgression p = (IPlayerProgression) player.getCapability(PlayerProgressionProvider.PROGRESSION).orElse(null);
            if (p != null) {
                tier = p.getTier();
            }
            ItemLodestarCopier.Modes[] vals = ItemLodestarCopier.Modes.values();
            for (int i = 0; i < vals.length; i++) {
                if (vals[i].tier <= tier) {
                    list.add(vals[i]);
                }
            }
            return (ItemLodestarCopier.Modes[]) list.toArray(new ItemLodestarCopier.Modes[0]);
        }
    }

    @Override
    public int capacity() {
        return ItemLodestarCopier.Modes.values().length;
    }

    @Override
    public int capacity(Player player) {
        return player == null ? this.capacity() : this.getModesForPlayer(player).length;
    }

    @Override
    public void setIndex(Player player, InteractionHand hand, ItemStack stack, int index) {
        ItemLodestarCopier.Modes[] forPlayer = this.getModesForPlayer(player);
        ItemLodestarCopier.Modes mode = forPlayer[Math.abs(index) % forPlayer.length];
        switch(mode) {
            case Template_1:
                this.pasteTemplate("{commands:[{connections:[{index:0,target:\"9c09665d-f5ef-40e7-a9d5-402fff7a1108\"},{index:1,target:\"9ef04eaa-443f-4de6-99bb-d9a28f097b36\"}],id:\"7395fa1f-f10b-4b20-877d-419ad8adfb10\",parameters:[{parameter_id:\"chop.area\"}],start:1b,task:\"mna:chop\",x:31,y:56},{connections:[{index:0,target:\"a0a2150c-4172-4de8-aa26-3d3ea823eabe\"},{index:1,target:\"a0a2150c-4172-4de8-aa26-3d3ea823eabe\"}],id:\"9c09665d-f5ef-40e7-a9d5-402fff7a1108\",parameters:[{parameter_id:\"collect.area\"},{filter:{blacklist:{Items:[]},blacklist_damage:0b,blacklist_tag:0b,whitelist:{Items:[]},whitelist_damage:0b,whitelist_tag:0b},parameter_id:\"collect.filter\"},{Count:0b,id:\"minecraft:air\",parameter_id:\"collect.filter_single\"}],start:0b,task:\"mna:gather_items\",x:106,y:39},{connections:[],id:\"a0a2150c-4172-4de8-aa26-3d3ea823eabe\",parameters:[{parameter_id:\"teinteract.point\",point:{}}],start:0b,task:\"mna:place_item\",x:228,y:43},{connections:[{index:0,target:\"a7d52990-77a8-4899-9b0c-8ad3fd0b38f9\"}],id:\"9ef04eaa-443f-4de6-99bb-d9a28f097b36\",parameters:[{parameter_id:\"teinteract.point\",point:{}},{filter:{blacklist:{Items:[]},blacklist_damage:0b,blacklist_tag:0b,whitelist:{Items:[]},whitelist_damage:0b,whitelist_tag:0b},parameter_id:\"take.filter\"},{Count:1b,id:\"minecraft:oak_sapling\",parameter_id:\"take.stack\"},{parameter_id:\"take.random\",value:0b},{max:128,min:0,parameter_id:\"take.quantity\",value:0}],start:0b,task:\"mna:take_item\",x:98,y:131},{connections:[{index:0,target:\"a0a2150c-4172-4de8-aa26-3d3ea823eabe\"},{index:1,target:\"a0a2150c-4172-4de8-aa26-3d3ea823eabe\"}],id:\"a7d52990-77a8-4899-9b0c-8ad3fd0b38f9\",parameters:[{parameter_id:\"plant.area\"}],start:0b,task:\"mna:plant\",x:191,y:130}],groups:[{h:64,l:\"Chop Success, Collect Items\",w:196,x:87,y:30},{h:55,l:\"Chop Failed, Try Replanting\",w:175,x:93,y:123}]}", stack);
                stack.getOrCreateTag().putInt("copy_mode", ItemLodestarCopier.Modes.Paste.ordinal());
                break;
            case Template_2:
                this.pasteTemplate("{commands:[{connections:[{index:0,target:\"f8f495c4-2c30-4451-b11f-d39ea6035ba7\"},{index:1,target:\"7d81cf64-cc5d-44d5-a7cb-f161383cff29\"}],id:\"196c85eb-e705-494d-95f4-377a336350dc\",parameters:[{parameter_id:\"teinteract.point\",point:{}},{filter:{blacklist:{Items:[]},blacklist_damage:0b,blacklist_tag:0b,whitelist:{Items:[]},whitelist_damage:0b,whitelist_tag:0b},parameter_id:\"take.filter\"},{Count:1b,id:\"minecraft:wheat_seeds\",parameter_id:\"take.stack\"},{parameter_id:\"take.random\",value:0b},{max:128,min:0,parameter_id:\"take.quantity\",value:0}],start:1b,task:\"mna:take_item\",x:59,y:62},{connections:[{index:0,target:\"f0b90d1e-a751-4539-be5c-7cffa7da9bb0\"},{index:1,target:\"7d81cf64-cc5d-44d5-a7cb-f161383cff29\"}],id:\"f8f495c4-2c30-4451-b11f-d39ea6035ba7\",parameters:[{parameter_id:\"plant.area\"}],start:0b,task:\"mna:plant\",x:130,y:63},{connections:[],id:\"f0b90d1e-a751-4539-be5c-7cffa7da9bb0\",parameters:[{parameter_id:\"teinteract.point\",point:{}}],start:0b,task:\"mna:place_item\",x:253,y:58},{connections:[{index:0,target:\"4badc1b5-383b-4ef1-9b15-9b60ce1af97d\"},{index:1,target:\"4badc1b5-383b-4ef1-9b15-9b60ce1af97d\"}],id:\"7d81cf64-cc5d-44d5-a7cb-f161383cff29\",parameters:[{parameter_id:\"harvest.area\"}],start:0b,task:\"mna:harvest\",x:123,y:148},{connections:[{index:0,target:\"f0b90d1e-a751-4539-be5c-7cffa7da9bb0\"},{index:1,target:\"f0b90d1e-a751-4539-be5c-7cffa7da9bb0\"}],id:\"4badc1b5-383b-4ef1-9b15-9b60ce1af97d\",parameters:[{parameter_id:\"collect.area\"},{filter:{blacklist:{Items:[]},blacklist_damage:0b,blacklist_tag:0b,whitelist:{Items:[]},whitelist_damage:0b,whitelist_tag:0b},parameter_id:\"collect.filter\"},{Count:0b,id:\"minecraft:air\",parameter_id:\"collect.filter_single\"}],start:0b,task:\"mna:gather_items\",x:181,y:148}],groups:[{h:56,l:\"Got a seed, plant it and put away the excess\",w:92,x:118,y:58},{h:63,l:\"Didn't get a seed or couldn't plant, harvest and collect\",w:158,x:117,y:133},{h:50,l:\"Always empty the inventory at the end before starting again.\",w:62,x:246,y:52}]}", stack);
                stack.getOrCreateTag().putInt("copy_mode", ItemLodestarCopier.Modes.Paste.ordinal());
                break;
            case Template_3:
                this.pasteTemplate("{commands:[{connections:[{index:0,target:\"be5e6d6b-6b2f-442b-91bb-030a7862ff01\"}],id:\"04892f9b-d140-491a-8659-2c2f2217a505\",parameters:[{parameter_id:\"fish.point\",point:{}}],start:1b,task:\"mna:fish\",x:14,y:45},{connections:[{index:0,target:\"9911f330-5272-4a96-9734-5250a022c95d\"},{index:1,target:\"9911f330-5272-4a96-9734-5250a022c95d\"}],id:\"be5e6d6b-6b2f-442b-91bb-030a7862ff01\",parameters:[{parameter_id:\"collect.area\"},{filter:{blacklist:{Items:[]},blacklist_damage:0b,blacklist_tag:0b,whitelist:{Items:[]},whitelist_damage:0b,whitelist_tag:0b},parameter_id:\"collect.filter\"},{Count:0b,id:\"minecraft:air\",parameter_id:\"collect.filter_single\"}],start:0b,task:\"mna:gather_items\",x:74,y:46},{connections:[],id:\"9911f330-5272-4a96-9734-5250a022c95d\",parameters:[{parameter_id:\"teinteract.point\",point:{}}],start:0b,task:\"mna:place_item\",x:135,y:45}],groups:[]}", stack);
                stack.getOrCreateTag().putInt("copy_mode", ItemLodestarCopier.Modes.Paste.ordinal());
                break;
            case Template_4:
                this.pasteTemplate("{commands:[{connections:[{index:0,target:\"d8114403-8eeb-4d9f-b3ac-899244273a22\"}],id:\"1228f099-772c-4225-b2e4-0129bbd59543\",parameters:[{parameter_id:\"teinteract.point\",point:{}},{filter:{blacklist:{Items:[]},blacklist_damage:0b,blacklist_tag:0b,whitelist:{Items:[]},whitelist_damage:0b,whitelist_tag:0b},parameter_id:\"take.filter\"},{Count:1b,id:\"mna:rune_pattern\",parameter_id:\"take.stack\"},{parameter_id:\"take.random\",value:0b},{max:128,min:0,parameter_id:\"take.quantity\",value:0}],start:1b,task:\"mna:take_item\",x:81,y:99},{connections:[{index:0,target:\"17dd34a9-909b-418c-ba15-dda990ebe149\"}],id:\"d8114403-8eeb-4d9f-b3ac-899244273a22\",parameters:[{parameter_id:\"teinteract.point\",point:{}}],start:0b,task:\"mna:place_item\",x:135,y:124},{connections:[{index:0,target:\"c521a2a4-1806-465e-b2f4-1b5ab54cf29c\"},{index:1,target:\"c521a2a4-1806-465e-b2f4-1b5ab54cf29c\"}],id:\"17dd34a9-909b-418c-ba15-dda990ebe149\",parameters:[{parameter_id:\"runescribe.point\",point:{}},{Count:0b,id:\"minecraft:air\",parameter_id:\"runescribe.recipe\"}],start:0b,task:\"mna:runescribe\",x:220,y:135},{connections:[{index:0,target:\"88cd05d0-fdb3-4247-8ebb-92afc9962973\"}],id:\"c521a2a4-1806-465e-b2f4-1b5ab54cf29c\",parameters:[{parameter_id:\"teinteract.point\",point:{}},{filter:{blacklist:{Items:[]},blacklist_damage:0b,blacklist_tag:0b,whitelist:{Items:[]},whitelist_damage:0b,whitelist_tag:0b},parameter_id:\"take.filter\"},{Count:0b,id:\"minecraft:air\",parameter_id:\"take.stack\"},{parameter_id:\"take.random\",value:0b},{max:128,min:0,parameter_id:\"take.quantity\",value:0}],start:0b,task:\"mna:take_item\",x:291,y:157},{connections:[],id:\"88cd05d0-fdb3-4247-8ebb-92afc9962973\",parameters:[{parameter_id:\"teinteract.point\",point:{}}],start:0b,task:\"mna:place_item\",x:341,y:182}],groups:[{h:77,l:\"Collect a rune and place it into a scribing table\",w:117,x:75,y:93},{h:50,l:\"Scribe the rune\",w:50,x:221,y:126},{h:75,l:\"Take the rune from the scribing table and put it away\",w:105,x:289,y:153}]}", stack);
                stack.getOrCreateTag().putInt("copy_mode", ItemLodestarCopier.Modes.Paste.ordinal());
                break;
            case Paste:
                stack.getOrCreateTag().putInt("copy_mode", ItemLodestarCopier.Modes.Paste.ordinal());
                break;
            case Copy:
                stack.getOrCreateTag().putInt("copy_mode", ItemLodestarCopier.Modes.Copy.ordinal());
                break;
            case Copy_to_Clipboard:
                if (player.m_9236_().isClientSide()) {
                    if (!stack.hasTag() || !stack.getTag().contains("stored_lodestar") && !stack.getTag().contains("stored_particle_data")) {
                        player.m_213846_(Component.translatable("item.mna.lodestar_copier.no_data"));
                        return;
                    }
                    String dataString = "";
                    String userResponse = "";
                    if (stack.getTag().contains("stored_lodestar")) {
                        userResponse = "item.mna.lodestar_copier.clipboard_copied.lodestar";
                        dataString = stack.getTag().get("stored_lodestar").toString();
                    } else {
                        userResponse = "item.mna.lodestar_copier.clipboard_copied.particle_emitter";
                        dataString = stack.getTag().get("stored_particle_data").toString();
                    }
                    GLFW.glfwSetClipboardString(Minecraft.getInstance().getWindow().getWindow(), dataString);
                    player.m_213846_(Component.translatable(userResponse));
                }
                break;
            case Paste_From_Clipboard:
                if (player.m_9236_().isClientSide()) {
                    String clipboardData = GLFW.glfwGetClipboardString(Minecraft.getInstance().getWindow().getWindow());
                    try {
                        CompoundTag tag = this.StringToTag(clipboardData);
                        if (tag.contains("commands")) {
                            ClientMessageDispatcher.sendPatterningPrismCopyMessage(tag, hand, false);
                        } else {
                            if (!tag.contains("type")) {
                                player.m_213846_(Component.translatable("item.mna.lodestar_copier.data_paste.validation_failed"));
                                return;
                            }
                            ClientMessageDispatcher.sendPatterningPrismCopyMessage(tag, hand, true);
                        }
                        player.m_213846_(Component.translatable("item.mna.lodestar_copier.data_paste.success"));
                    } catch (Exception var9) {
                        player.m_213846_(Component.translatable("item.mna.lodestar_copier.data_paste.failed"));
                    }
                }
        }
    }

    @Override
    public void setIndex(ItemStack stack, int index) {
        ManaAndArtifice.LOGGER.info("ItemLodestarCopier >> setIndex variant called with no world parameter, will no-op.");
    }

    @Override
    public int getIndex(ItemStack stack) {
        if (!stack.hasTag()) {
            return ItemLodestarCopier.Modes.Copy.ordinal();
        } else {
            int index = stack.getTag().getInt("copy_mode");
            ItemLodestarCopier.Modes mode = ItemLodestarCopier.Modes.values()[Math.abs(index) % ItemLodestarCopier.Modes.values().length];
            return mode == ItemLodestarCopier.Modes.Paste ? ItemLodestarCopier.Modes.Paste.ordinal() : ItemLodestarCopier.Modes.Copy.ordinal();
        }
    }

    @Override
    public IItemHandlerModifiable getInventory(ItemStack stackEquipped, Player player) {
        ItemInventoryBase inv = new ItemInventoryBase(stackEquipped, this.capacity(player));
        ItemLodestarCopier.Modes[] vals = ItemLodestarCopier.Modes.values();
        int tier = 0;
        IPlayerProgression p = (IPlayerProgression) player.getCapability(PlayerProgressionProvider.PROGRESSION).orElse(null);
        if (p != null) {
            tier = p.getTier();
        }
        int invIdx = 0;
        if (vals[0].tier <= tier) {
            inv.setStackInSlot(invIdx++, new ItemStack(ItemInit.RITUAL_FOCUS_MINOR.get()).setHoverName(Component.translatable("item.mna.lodestar_copier.actions.copy")));
        }
        if (vals[1].tier <= tier) {
            inv.setStackInSlot(invIdx++, new ItemStack(ItemInit.RITUAL_FOCUS_GREATER.get()).setHoverName(Component.translatable("item.mna.lodestar_copier.actions.paste")));
        }
        if (vals[2].tier <= tier) {
            inv.setStackInSlot(invIdx++, new ItemStack(Items.OAK_SAPLING).setHoverName(Component.translatable("item.mna.lodestar_copier.actions.template_1")));
        }
        if (vals[3].tier <= tier) {
            inv.setStackInSlot(invIdx++, new ItemStack(Items.WHEAT_SEEDS).setHoverName(Component.translatable("item.mna.lodestar_copier.actions.template_2")));
        }
        if (vals[4].tier <= tier) {
            inv.setStackInSlot(invIdx++, new ItemStack(Items.FISHING_ROD).setHoverName(Component.translatable("item.mna.lodestar_copier.actions.template_3")));
        }
        if (vals[5].tier <= tier) {
            inv.setStackInSlot(invIdx++, new ItemStack(ItemInit.RUNESMITH_CHISEL.get()).setHoverName(Component.translatable("item.mna.lodestar_copier.actions.template_4")));
        }
        if (vals[6].tier <= tier) {
            inv.setStackInSlot(invIdx++, new ItemStack(ItemInit.__DEBUG.get()).setHoverName(Component.translatable("item.mna.lodestar_copier.actions.copy_clipboard")));
        }
        if (vals[7].tier <= tier) {
            inv.setStackInSlot(invIdx++, new ItemStack(ItemInit.__DEBUG.get()).setHoverName(Component.translatable("item.mna.lodestar_copier.actions.paste_clipboard")));
        }
        return inv;
    }

    private static boolean isPasteMode(ItemStack stack) {
        return stack.hasTag() && stack.getTag().getInt("copy_mode") == ItemLodestarCopier.Modes.Paste.ordinal();
    }

    private static boolean isLodestarData(ItemStack stack) {
        return stack.hasTag() && stack.getTag().contains("stored_lodestar");
    }

    private static enum Modes {

        Copy(1),
        Paste(1),
        Template_1(1),
        Template_2(1),
        Template_3(1),
        Template_4(2),
        Copy_to_Clipboard(1),
        Paste_From_Clipboard(1);

        private int tier;

        private Modes(int tier) {
            this.tier = tier;
        }
    }
}
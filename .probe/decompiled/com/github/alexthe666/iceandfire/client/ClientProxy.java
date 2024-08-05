package com.github.alexthe666.iceandfire.client;

import com.github.alexthe666.iceandfire.CommonProxy;
import com.github.alexthe666.iceandfire.client.gui.GuiMyrmexAddRoom;
import com.github.alexthe666.iceandfire.client.gui.GuiMyrmexStaff;
import com.github.alexthe666.iceandfire.client.gui.bestiary.GuiBestiary;
import com.github.alexthe666.iceandfire.client.particle.ParticleBlood;
import com.github.alexthe666.iceandfire.client.particle.ParticleDragonFlame;
import com.github.alexthe666.iceandfire.client.particle.ParticleDragonFrost;
import com.github.alexthe666.iceandfire.client.particle.ParticleDreadPortal;
import com.github.alexthe666.iceandfire.client.particle.ParticleDreadTorch;
import com.github.alexthe666.iceandfire.client.particle.ParticleGhostAppearance;
import com.github.alexthe666.iceandfire.client.particle.ParticleHydraBreath;
import com.github.alexthe666.iceandfire.client.particle.ParticlePixieDust;
import com.github.alexthe666.iceandfire.client.particle.ParticleSerpentBubble;
import com.github.alexthe666.iceandfire.client.particle.ParticleSirenAppearance;
import com.github.alexthe666.iceandfire.client.particle.ParticleSirenMusic;
import com.github.alexthe666.iceandfire.client.render.entity.layer.LayerDragonArmor;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.util.MyrmexHive;
import com.github.alexthe666.iceandfire.enums.EnumParticles;
import com.github.alexthe666.iceandfire.event.ClientEvents;
import com.github.alexthe666.iceandfire.event.PlayerRenderEvents;
import com.mojang.blaze3d.platform.InputConstants;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = "iceandfire", value = { Dist.CLIENT })
public class ClientProxy extends CommonProxy {

    public static Set<UUID> currentDragonRiders = new HashSet();

    private static MyrmexHive referedClientHive = null;

    private int previousViewType = 0;

    private int thirdPersonViewDragon = 0;

    private Entity referencedMob = null;

    private BlockEntity referencedTE = null;

    public static MyrmexHive getReferedClientHive() {
        return referedClientHive;
    }

    @Override
    public void setReferencedHive(MyrmexHive hive) {
        referedClientHive = hive;
    }

    @Override
    public void init() {
        IafKeybindRegistry.init();
        MinecraftForge.EVENT_BUS.register(new PlayerRenderEvents());
        MinecraftForge.EVENT_BUS.register(new ClientEvents());
    }

    @Override
    public void postInit() {
    }

    @Override
    public void clientInit() {
        super.clientInit();
        IafClientSetup.clientInit();
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void spawnDragonParticle(EnumParticles name, double x, double y, double z, double motX, double motY, double motZ, EntityDragonBase entityDragonBase) {
        ClientLevel world = Minecraft.getInstance().level;
        if (world != null) {
            Particle particle = null;
            if (name == EnumParticles.DragonFire) {
                particle = new ParticleDragonFlame(world, x, y, z, motX, motY, motZ, entityDragonBase, 0);
            } else if (name == EnumParticles.DragonIce) {
                particle = new ParticleDragonFrost(world, x, y, z, motX, motY, motZ, entityDragonBase, 0);
            }
            if (particle != null) {
                Minecraft.getInstance().particleEngine.add(particle);
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void spawnParticle(EnumParticles name, double x, double y, double z, double motX, double motY, double motZ, float size) {
        ClientLevel world = Minecraft.getInstance().level;
        if (world != null) {
            Particle particle = null;
            switch(name) {
                case DragonFire:
                    particle = new ParticleDragonFlame(world, x, y, z, motX, motY, motZ, size);
                    break;
                case DragonIce:
                    particle = new ParticleDragonFrost(world, x, y, z, motX, motY, motZ, size);
                    break;
                case Dread_Torch:
                    particle = new ParticleDreadTorch(world, x, y, z, motX, motY, motZ, size);
                    break;
                case Dread_Portal:
                    particle = new ParticleDreadPortal(world, x, y, z, motX, motY, motZ, size);
                    break;
                case Blood:
                    particle = new ParticleBlood(world, x, y, z);
                    break;
                case If_Pixie:
                    particle = new ParticlePixieDust(world, x, y, z, (float) motX, (float) motY, (float) motZ);
                    break;
                case Siren_Appearance:
                    particle = new ParticleSirenAppearance(world, x, y, z, (int) motX);
                    break;
                case Ghost_Appearance:
                    particle = new ParticleGhostAppearance(world, x, y, z, (int) motX);
                    break;
                case Siren_Music:
                    particle = new ParticleSirenMusic(world, x, y, z, motX, motY, motZ, 1.0F);
                    break;
                case Serpent_Bubble:
                    particle = new ParticleSerpentBubble(world, x, y, z, motX, motY, motZ, 1.0F);
                    break;
                case Hydra:
                    particle = new ParticleHydraBreath(world, x, y, z, (float) motX, (float) motY, (float) motZ);
            }
            if (particle != null) {
                Minecraft.getInstance().particleEngine.add(particle);
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void openBestiaryGui(ItemStack book) {
        Minecraft.getInstance().setScreen(new GuiBestiary(book));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void openMyrmexStaffGui(ItemStack staff) {
        Minecraft.getInstance().setScreen(new GuiMyrmexStaff(staff));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void openMyrmexAddRoomGui(ItemStack staff, BlockPos pos, Direction facing) {
        Minecraft.getInstance().setScreen(new GuiMyrmexAddRoom(staff, pos, facing));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public Object getFontRenderer() {
        return Minecraft.getInstance().font;
    }

    @Override
    public int getDragon3rdPersonView() {
        return this.thirdPersonViewDragon;
    }

    @Override
    public void setDragon3rdPersonView(int view) {
        this.thirdPersonViewDragon = view;
    }

    @Override
    public int getPreviousViewType() {
        return this.previousViewType;
    }

    @Override
    public void setPreviousViewType(int view) {
        this.previousViewType = view;
    }

    @Override
    public void updateDragonArmorRender(String clear) {
        LayerDragonArmor.clearCache(clear);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public boolean shouldSeeBestiaryContents() {
        return InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), 340) || InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), 344);
    }

    @Override
    public Entity getReferencedMob() {
        return this.referencedMob;
    }

    @Override
    public void setReferencedMob(Entity dragonBase) {
        this.referencedMob = dragonBase;
    }

    @Override
    public BlockEntity getRefrencedTE() {
        return this.referencedTE;
    }

    @Override
    public void setRefrencedTE(BlockEntity tileEntity) {
        this.referencedTE = tileEntity;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public Player getClientSidePlayer() {
        return Minecraft.getInstance().player;
    }
}
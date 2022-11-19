package net.anvilcraft.classiccasting.render;

import java.util.Collection;

import dev.tilera.auracore.client.AuraManagerClient;
import dev.tilera.auracore.client.AuraManagerClient.NodeStats;
import net.anvilcraft.classiccasting.UtilsFX;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

public class ItemAuraCompassRenderer implements IItemRenderer {
    private float angleHD;
    private float angleVD;

    public ItemAuraCompassRenderer() {
        this.angleHD = 0.0f;
        this.angleVD = 0.0f;
    }

    public boolean
    handleRenderType(final ItemStack item, final IItemRenderer.ItemRenderType type) {
        return type == IItemRenderer.ItemRenderType.EQUIPPED_FIRST_PERSON;
    }

    public boolean shouldUseRenderHelper(
        final IItemRenderer.ItemRenderType type,
        final ItemStack item,
        final IItemRenderer.ItemRendererHelper helper
    ) {
        return false;
    }

    public void renderItem(
        final IItemRenderer.ItemRenderType type,
        final ItemStack item,
        final Object... data
    ) {
        final Entity player = (Entity) data[1];
        final Minecraft mc = Minecraft.getMinecraft();
        final float par1 = mc.timer.renderPartialTicks;
        final float pep = mc.entityRenderer.itemRenderer.prevEquippedProgress;
        final float ep = mc.entityRenderer.itemRenderer.equippedProgress;
        final float var2 = pep + (ep - pep) * par1;
        final float var3 = 0.8f;
        final EntityPlayer var4 = (EntityPlayer) mc.thePlayer;
        final float var5 = ((Entity) var4).prevRotationPitch
            + (((Entity) var4).rotationPitch - ((Entity) var4).prevRotationPitch) * par1;
        if (player.getEntityId() != var4.getEntityId()
            || RenderManager.instance.playerViewY == 180.0f
            || mc.gameSettings.thirdPersonView != 0
            || mc.renderViewEntity.isPlayerSleeping() || mc.gameSettings.hideGUI) {
            if (item.getItem().requiresMultipleRenderPasses()) {
                this.renderItemStack(var4, item, 0, false);
                for (int x = 1; x < item.getItem().getRenderPasses(item.getItemDamage());
                     ++x) {
                    final int var6 = item.getItem().getColorFromItemStack(item, x);
                    final float var7 = (var6 >> 16 & 0xFF) / 255.0f;
                    final float var8 = (var6 >> 8 & 0xFF) / 255.0f;
                    final float var9 = (var6 & 0xFF) / 255.0f;
                    GL11.glColor4f(var7, var8, var9, 1.0f);
                    this.renderItemStack(var4, item, x, false);
                }
            } else {
                this.renderItemStack(var4, item, 0, false);
            }
        } else {
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glEnable(32826);
            GL11.glScalef(2.5f, 2.5f, 2.5f);
            float var10 = var4.getSwingProgress(par1);
            float var11 = MathHelper.sin(var10 * var10 * 3.1415927f);
            float var12 = MathHelper.sin(MathHelper.sqrt_float(var10) * 3.1415927f);
            GL11.glRotatef(-var12 * 80.0f, -1.0f, 0.0f, 0.0f);
            GL11.glRotatef(-var12 * 20.0f, 0.0f, 0.0f, -1.0f);
            GL11.glRotatef(-var11 * 20.0f, 0.0f, -1.0f, 0.0f);
            GL11.glRotatef(45.0f, 0.0f, -1.0f, 0.0f);
            GL11.glTranslatef(
                -0.7f * var3, -(-0.65f * var3 - (1.0f - var2) * 0.6f), 0.9f * var3
            );
            GL11.glPushMatrix();
            float var13 = mc.theWorld.getLightBrightness(
                MathHelper.floor_double(((Entity) var4).posX),
                MathHelper.floor_double(((Entity) var4).posY),
                MathHelper.floor_double(((Entity) var4).posZ)
            );
            var13 = 1.0f;
            int var14 = mc.theWorld.getLightBrightnessForSkyBlocks(
                MathHelper.floor_double(((Entity) var4).posX),
                MathHelper.floor_double(((Entity) var4).posY),
                MathHelper.floor_double(((Entity) var4).posZ),
                0
            );
            final int var15 = var14 % 65536;
            int var16 = var14 / 65536;
            OpenGlHelper.setLightmapTextureCoords(
                OpenGlHelper.lightmapTexUnit, var15 / 1.0f, var16 / 1.0f
            );
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            var14 = item.getItem().getColorFromItemStack(item, 0);
            var10 = (var14 >> 16 & 0xFF) / 255.0f;
            var11 = (var14 >> 8 & 0xFF) / 255.0f;
            var12 = (var14 & 0xFF) / 255.0f;
            GL11.glColor4f(var13 * var10, var13 * var11, var13 * var12, 1.0f);
            var11 = MathHelper.sin(var10 * 3.1415927f);
            var12 = MathHelper.sin(MathHelper.sqrt_float(var10) * 3.1415927f);
            GL11.glTranslatef(
                -var12 * 0.4f,
                MathHelper.sin(MathHelper.sqrt_float(var10) * 3.1415927f * 2.0f) * 0.2f,
                -var11 * 0.2f
            );
            var10 = 1.0f - var5 / 45.0f + 0.1f;
            if (var10 < 0.0f) {
                var10 = 0.0f;
            }
            if (var10 > 1.0f) {
                var10 = 1.0f;
            }
            var10 = -MathHelper.cos(var10 * 3.1415927f) * 0.5f + 0.5f;
            GL11.glTranslatef(
                0.0f,
                0.0f * var3 - (1.0f - var2) * 1.2f - var10 * 0.5f + 0.04f,
                -0.9f * var3
            );
            GL11.glRotatef(90.0f, 0.0f, 1.0f, 0.0f);
            GL11.glRotatef(var10 * -85.0f, 0.0f, 0.0f, 1.0f);
            GL11.glEnable(32826);
            mc.renderEngine.bindTexture(mc.thePlayer.getLocationSkin());
            for (var16 = 0; var16 < 2; ++var16) {
                final int var17 = var16 * 2 - 1;
                GL11.glPushMatrix();
                GL11.glTranslatef(-0.0f, -0.6f, 1.1f * var17);
                GL11.glRotatef((float) (-45 * var17), 1.0f, 0.0f, 0.0f);
                GL11.glRotatef(-90.0f, 0.0f, 0.0f, 1.0f);
                GL11.glRotatef(59.0f, 0.0f, 0.0f, 1.0f);
                GL11.glRotatef((float) (-65 * var17), 0.0f, 1.0f, 0.0f);
                final Render var18
                    = RenderManager.instance.getEntityRenderObject((Entity) mc.thePlayer);
                final RenderPlayer var19 = (RenderPlayer) var18;
                final float var20 = 1.0f;
                GL11.glScalef(var20, var20, var20);
                var19.renderFirstPersonArm((EntityPlayer) mc.thePlayer);
                GL11.glPopMatrix();
            }
            var11 = var4.getSwingProgress(par1);
            var12 = MathHelper.sin(var11 * var11 * 3.1415927f);
            final float var21 = MathHelper.sin(MathHelper.sqrt_float(var11) * 3.1415927f);
            GL11.glRotatef(-var12 * 20.0f, 0.0f, 1.0f, 0.0f);
            GL11.glRotatef(-var21 * 20.0f, 0.0f, 0.0f, 1.0f);
            GL11.glRotatef(-var21 * 80.0f, 1.0f, 0.0f, 0.0f);
            final float var22 = 0.38f;
            GL11.glScalef(var22, var22, var22);
            GL11.glRotatef(90.0f, 0.0f, 1.0f, 0.0f);
            GL11.glRotatef(180.0f, 0.0f, 0.0f, 1.0f);
            GL11.glTranslatef(-1.0f, -1.0f, 0.0f);
            this.renderAuraCompass(
                item, (EntityPlayer) mc.thePlayer, mc.renderEngine, var10
            );
            GL11.glPopMatrix();
        }
    }

    public void renderAuraCompass(
        final ItemStack item,
        final EntityPlayer player,
        final TextureManager re,
        final float var20
    ) {
        final Minecraft mc = Minecraft.getMinecraft();
        final EntityPlayer var21 = (EntityPlayer) mc.thePlayer;
        GL11.glPushMatrix();
        GL11.glScalef(2.0f, 2.0f, 2.0f);
        this.renderItemStack(var21, item, 0, false);
        GL11.glPushMatrix();
        GL11.glTranslatef(0.1f, 0.1f, 0.0f);
        GL11.glScalef(0.8f, 0.8f, 0.8f);
        GL11.glTranslatef(0.5f, 0.5f, 0.0f);
        double closestX = Float.MAX_VALUE;
        double closestY = Float.MAX_VALUE;
        double closestZ = Float.MAX_VALUE;
        double closestDistance = Double.MAX_VALUE;
        boolean foundSomething = false;
        final Collection<NodeStats> col = AuraManagerClient.auraClientList.values();
        for (final NodeStats l : col) {
            final int dim = l.dimension;
            if (((Entity) player).dimension == dim) {
                final double px = l.x;
                final double py = l.y;
                final double pz = l.z;
                final double xd = px - ((Entity) player).posX;
                final double yd = py - ((Entity) player).posY;
                final double zd = pz - ((Entity) player).posZ;
                final double distSq = xd * xd + yd * yd + zd * zd;
                if (distSq >= closestDistance) {
                    continue;
                }
                closestDistance = distSq;
                closestX = px;
                closestY = py;
                closestZ = pz;
                foundSomething = true;
            }
        }
        final float bob
            = MathHelper.sin(((Entity) player).ticksExisted / 5.0f) * 0.015f + 0.015f;
        if (foundSomething) {
            final double var22 = closestX - ((Entity) player).posX;
            final double var23 = closestY - ((Entity) player).posY + bob;
            final double var24 = closestZ - ((Entity) player).posZ;
            final float var25 = MathHelper.sqrt_double(var22 * var22 + var24 * var24);
            float angleH = (float) (Math.atan2(var24, var22) * 180.0 / 3.141592653589793)
                - ((Entity) player).rotationYaw + 90.0f;
            float angleV = (float) (Math.atan2(var25, var23) * 180.0 / 3.141592653589793)
                - ((Entity) player).rotationPitch;
            if (angleH < 0.0f) {
                angleH += 360.0f;
            }
            if (angleH > 360.0f) {
                angleH -= 360.0f;
            }
            if (angleH > this.angleHD) {
                this.angleHD += (angleH - this.angleHD) / 33.0f;
            } else if (angleH < this.angleHD) {
                this.angleHD -= (this.angleHD - angleH) / 33.0f;
            }
            angleV += var20 * -85.0f;
            if (angleV < -180.0f) {
                angleV += 360.0f;
            }
            if (angleV > 180.0f) {
                angleV -= 360.0f;
            }
            if (angleV > this.angleVD) {
                this.angleVD += (angleV - this.angleVD) / 33.0f;
            } else if (angleV < this.angleVD) {
                this.angleVD -= (this.angleVD - angleV) / 33.0f;
            }
        } else {
            if (this.angleHD > 0.0f) {
                this.angleHD -= this.angleHD / 33.0f;
            }
            if (this.angleVD > 0.0f) {
                this.angleVD -= this.angleVD / 33.0f;
            } else if (this.angleVD < 0.0f) {
                this.angleVD += this.angleVD / 33.0f;
            }
        }
        GL11.glTranslatef(0.0f, bob, 0.0f);
        GL11.glRotatef(this.angleHD, 0.0f, 0.0f, 1.0f);
        GL11.glRotatef(this.angleVD, 1.0f, 0.0f, 0.0f);
        GL11.glTranslatef(-0.5f, -0.5f, 0.0f);
        this.renderItemStack(var21, item, 1, true);
        GL11.glPopMatrix();
        GL11.glPopMatrix();
    }

    public void renderItemStack(
        final EntityLivingBase par1EntityLiving,
        final ItemStack par2ItemStack,
        final int par3,
        final boolean glint
    ) {
        GL11.glPushMatrix();
        UtilsFX.render3DItem(
            par2ItemStack, par3, 1.0f, par1EntityLiving.getBrightnessForRender(0.0f)
        );
        GL11.glPopMatrix();
    }
}

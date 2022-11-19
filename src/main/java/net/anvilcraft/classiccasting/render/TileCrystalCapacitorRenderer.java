package net.anvilcraft.classiccasting.render;

import java.awt.Color;
import java.util.Random;

import net.anvilcraft.classiccasting.tiles.TileCrystalCapacitor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.renderers.models.ModelCube;

public class TileCrystalCapacitorRenderer extends TileEntitySpecialRenderer {
    private ModelCube model;

    public TileCrystalCapacitorRenderer() {
        this.model = new ModelCube();
    }

    private void drawCrystal(
        final float x,
        final float y,
        final float z,
        final Random rand,
        final int color,
        final float size
    ) {
        final EntityPlayer p = (EntityPlayer) Minecraft.getMinecraft().thePlayer;
        final float shade
            = MathHelper.sin(
                  (((Entity) p).ticksExisted + rand.nextInt(10))
                  / (10.0f + rand.nextFloat())
              ) * 0.05f
            + 0.95f;
        final Color c = new Color(color);
        final float r = c.getRed() / 220.0f;
        final float g = c.getGreen() / 220.0f;
        final float b = c.getBlue() / 220.0f;
        GL11.glPushMatrix();
        GL11.glEnable(2977);
        GL11.glEnable(3042);
        GL11.glEnable(32826);
        GL11.glBlendFunc(770, 771);
        GL11.glTranslatef(
            x + (1.0f - size) / 2.0f, y + (1.0f - size) / 2.0f, z + (1.0f - size) / 2.0f
        );
        GL11.glScalef(size, size, size);
        final int var19 = (int) (210.0f * shade);
        final int var20 = var19 % 65536;
        final int var21 = var19 / 65536;
        OpenGlHelper.setLightmapTextureCoords(
            OpenGlHelper.lightmapTexUnit, var20 / 1.0f, var21 / 1.0f
        );
        GL11.glColor4f(r, g, b, 1.0f);
        this.model.render();
        GL11.glScalef(1.0f, 1.0f, 1.0f);
        GL11.glDisable(32826);
        GL11.glDisable(3042);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glPopMatrix();
    }

    @Override
    public void renderTileEntityAt(
        final TileEntity te, final double x, final double y, final double z, final float f
    ) {
        GL11.glPushMatrix();
        final EntityPlayer p = (EntityPlayer) Minecraft.getMinecraft().thePlayer;
        final TileCrystalCapacitor tco = (TileCrystalCapacitor) te;
        final Color col
            = new Color(1.0f, 1.0f - tco.storedVis / (float) tco.maxVis * 0.8f, 1.0f);
        this.bindTexture(
            new ResourceLocation("classiccasting", "textures/models/crystalcapacitor.png")
        );
        final Random rand = new Random(tco.xCoord + tco.yCoord * tco.zCoord);
        for (int xx = 0; xx < 2; ++xx) {
            for (int zz = 0; zz < 2; ++zz) {
                for (int yy = 0; yy < 2; ++yy) {
                    final int t = ((Entity) p).ticksExisted;
                    final float bob
                        = MathHelper.sin(t / (10 + xx * yy + te.xCoord % 8.0f)) * 0.02f;
                    final float weave
                        = MathHelper.sin(t / (10 + yy * zz + te.yCoord % 6.0f)) * 0.02f;
                    final float wobble
                        = MathHelper.sin(t / (10 + zz * xx + te.zCoord % 4.0f)) * 0.02f;
                    this.drawCrystal(
                        (float) x + bob + xx * 0.4f - 0.2f,
                        (float) y + wobble + yy * 0.4f - 0.2f,
                        (float) z + weave + zz * 0.4f - 0.2f,
                        rand,
                        col.getRGB(),
                        0.3f
                    );
                }
            }
        }
        GL11.glPopMatrix();
        GL11.glDisable(3042);
    }
}

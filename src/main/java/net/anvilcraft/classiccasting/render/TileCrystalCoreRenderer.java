package net.anvilcraft.classiccasting.render;

import java.awt.Color;
import java.util.Random;

import dev.tilera.auracore.api.CrystalColors;
import net.anvilcraft.classiccasting.render.models.ModelCrystal;
import net.anvilcraft.classiccasting.tiles.TileCrystalCore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class TileCrystalCoreRenderer extends TileEntitySpecialRenderer {
    private ModelCrystal model;

    public TileCrystalCoreRenderer() {
        this.model = new ModelCrystal();
    }

    private void drawCrystal(
        final float x,
        final float y,
        final float z,
        final float a1,
        final float a2,
        final float a3,
        final Random rand,
        final int color,
        final float size,
        final float speed
    ) {
        final EntityPlayer p = (EntityPlayer) Minecraft.getMinecraft().thePlayer;
        final float shade
            = MathHelper.sin(
                  (((Entity) p).ticksExisted + rand.nextInt(10))
                  / (5.0f + rand.nextFloat())
              ) * 0.075f
            + 0.925f;
        final Color c = new Color(color);
        final float r = c.getRed() / 220.0f;
        final float g = c.getGreen() / 220.0f;
        final float b = c.getBlue() / 220.0f;
        GL11.glPushMatrix();
        GL11.glEnable(2977);
        GL11.glEnable(3042);
        GL11.glEnable(32826);
        GL11.glBlendFunc(770, 771);
        GL11.glTranslatef(x + 0.5f, y + 0.5f, z + 0.5f);
        GL11.glRotatef(a3, 0.0f, 0.0f, 1.0f);
        GL11.glRotatef(a1, 0.0f, 1.0f, 0.0f);
        GL11.glRotatef(a2, 1.0f, 0.0f, 0.0f);
        GL11.glTranslatef(0.0f, -0.1f + speed / 4.0f, 0.0f);
        GL11.glScalef(
            (0.15f + rand.nextFloat() * 0.075f) * size,
            (0.5f + rand.nextFloat() * 0.1f) * size,
            (0.15f + rand.nextFloat() * 0.05f) * size
        );
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
        final TileCrystalCore tco = (TileCrystalCore) te;
        GL11.glPushMatrix();
        this.bindTexture(
            new ResourceLocation("classiccasting", "textures/models/crystal.png")
        );
        final Random rand = new Random(tco.xCoord + tco.yCoord * tco.zCoord);
        int col = 0;
        GL11.glPushMatrix();
        for (int a = 0; a < 20; ++a) {
            ++col;
            if (a % 5 == 0) {
                ++col;
            }
            if (col > 5) {
                col = 1;
            }
            final int color = CrystalColors.colors[col == 5 ? 7 : col];
            final float angle1
                = (tco.active ? (f * tco.speed) : 0.0f) + tco.rotation + 18 * a;
            final float angle2 = (float) (30 * (1 + a % 5));
            this.drawCrystal(
                (float) x,
                (float) y + tco.speed,
                (float) z,
                angle1,
                angle2,
                ((tco.active ? (f * tco.speed) : 0.0f) + tco.rotation) * 2.0f,
                rand,
                color,
                0.7f,
                tco.speed
            );
        }
        GL11.glPopMatrix();
        final int age = ((Entity) Minecraft.getMinecraft().renderViewEntity).ticksExisted;
        GL11.glPushMatrix();
        GL11.glTranslated(x + 0.5, y + 0.5 + tco.speed, z + 0.5);
        final int q = Minecraft.getMinecraft().gameSettings.fancyGraphics ? 20 : 10;
        final Tessellator tessellator = Tessellator.instance;
        RenderHelper.disableStandardItemLighting();
        final float f2 = age / 500.0f;
        final float f4 = 0.0f;
        final Random random = new Random(245L);
        GL11.glDisable(3553);
        GL11.glShadeModel(7425);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 1);
        GL11.glDisable(3008);
        GL11.glEnable(2884);
        GL11.glDepthMask(false);
        GL11.glPushMatrix();
        for (int i = 0; i < q; ++i) {
            GL11.glRotatef(random.nextFloat() * 360.0f, 1.0f, 0.0f, 0.0f);
            GL11.glRotatef(random.nextFloat() * 360.0f, 0.0f, 1.0f, 0.0f);
            GL11.glRotatef(random.nextFloat() * 360.0f, 0.0f, 0.0f, 1.0f);
            GL11.glRotatef(random.nextFloat() * 360.0f, 1.0f, 0.0f, 0.0f);
            GL11.glRotatef(random.nextFloat() * 360.0f, 0.0f, 1.0f, 0.0f);
            GL11.glRotatef(random.nextFloat() * 360.0f + f2 * 360.0f, 0.0f, 0.0f, 1.0f);
            tessellator.startDrawing(6);
            float fa = random.nextFloat() * 20.0f + 5.0f + f4 * 10.0f;
            float f5 = random.nextFloat() * 2.0f + 1.0f + f4 * 2.0f;
            fa /= 30.0f / (Math.min(age, 10) / 10.0f);
            f5 /= 30.0f / (Math.min(age, 10) / 10.0f);
            tessellator.setColorRGBA_I(16777215, (int) (255.0f * (1.0f - f4)));
            tessellator.addVertex(0.0, 0.0, 0.0);
            tessellator.setColorRGBA_I(16764159, 0);
            tessellator.addVertex(-0.866 * f5, (double) fa, (double) (-0.5f * f5));
            tessellator.addVertex(0.866 * f5, (double) fa, (double) (-0.5f * f5));
            tessellator.addVertex(0.0, (double) fa, (double) (1.0f * f5));
            tessellator.addVertex(-0.866 * f5, (double) fa, (double) (-0.5f * f5));
            tessellator.draw();
        }
        GL11.glPopMatrix();
        GL11.glDepthMask(true);
        GL11.glDisable(2884);
        GL11.glDisable(3042);
        GL11.glShadeModel(7424);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glEnable(3553);
        GL11.glEnable(3008);
        RenderHelper.enableStandardItemLighting();
        GL11.glBlendFunc(770, 771);
        GL11.glPopMatrix();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glPopMatrix();
    }
}

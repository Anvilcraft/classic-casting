package net.anvilcraft.classiccasting;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class UtilsFX {
    public static void render3DItem(
        final ItemStack par2ItemStack,
        final int par3,
        final float scale,
        final int brightness
    ) {
        final Minecraft mc = Minecraft.getMinecraft();
        GL11.glPushMatrix();
        Block block = null;
        if (par2ItemStack.getItem() instanceof ItemBlock) {
            block = Block.getBlockFromItem(par2ItemStack.getItem());
        }
        if (block != null && par2ItemStack.getItemSpriteNumber() == 0
            && RenderBlocks.renderItemIn3d(block.getRenderType())) {
            mc.renderEngine.bindTexture(TextureMap.locationBlocksTexture);
            RenderBlocks.getInstance().renderBlockAsItem(
                block, par2ItemStack.getItemDamage(), 1.0f
            );
        } else {
            final IIcon icon = Minecraft.getMinecraft().renderViewEntity.getItemIcon(
                par2ItemStack, par3
            );
            if (icon == null) {
                GL11.glPopMatrix();
                return;
            }
            if (par2ItemStack.getItemSpriteNumber() == 0) {
                mc.renderEngine.bindTexture(TextureMap.locationBlocksTexture);
            } else {
                mc.renderEngine.bindTexture(TextureMap.locationItemsTexture);
            }
            final Tessellator tessellator = Tessellator.instance;
            final float f = icon.getMinU();
            final float f2 = icon.getMaxU();
            final float f3 = icon.getMinV();
            final float f4 = icon.getMaxV();
            GL11.glEnable(32826);
            final float f7 = scale;
            GL11.glScalef(f7, f7, f7);
            renderItemIn2D(
                tessellator,
                f2,
                f3,
                f,
                f4,
                icon.getIconWidth(),
                icon.getIconHeight(),
                0.0625f,
                brightness
            );
            if (par2ItemStack != null && par2ItemStack.hasEffect(0) && par3 == 0) {
                GL11.glDepthFunc(514);
                GL11.glDisable(2896);
                // TODO: WTF
                mc.renderEngine.bindTexture(new ResourceLocation("%blur%/misc/glint.png")
                );
                GL11.glEnable(3042);
                GL11.glBlendFunc(768, 1);
                final float f8 = 0.76f;
                GL11.glColor4f(0.5f * f8, 0.25f * f8, 0.8f * f8, 1.0f);
                GL11.glMatrixMode(5890);
                GL11.glPushMatrix();
                final float f9 = 0.125f;
                GL11.glScalef(f9, f9, f9);
                float f10 = Minecraft.getSystemTime() % 3000L / 3000.0f * 8.0f;
                GL11.glTranslatef(f10, 0.0f, 0.0f);
                GL11.glRotatef(-50.0f, 0.0f, 0.0f, 1.0f);
                renderItemIn2D(
                    tessellator, 0.0f, 0.0f, 1.0f, 1.0f, 256, 256, 0.0625f, brightness
                );
                GL11.glPopMatrix();
                GL11.glPushMatrix();
                GL11.glScalef(f9, f9, f9);
                f10 = Minecraft.getSystemTime() % 4873L / 4873.0f * 8.0f;
                GL11.glTranslatef(-f10, 0.0f, 0.0f);
                GL11.glRotatef(10.0f, 0.0f, 0.0f, 1.0f);
                renderItemIn2D(
                    tessellator, 0.0f, 0.0f, 1.0f, 1.0f, 256, 256, 0.0625f, brightness
                );
                GL11.glPopMatrix();
                GL11.glMatrixMode(5888);
                GL11.glDisable(3042);
                GL11.glEnable(2896);
                GL11.glDepthFunc(515);
            }
            GL11.glDisable(32826);
        }
        GL11.glPopMatrix();
    }

    public static void renderItemIn2D(
        Tessellator tes,
        float p_78439_1_,
        float p_78439_2_,
        float p_78439_3_,
        float p_78439_4_,
        int p_78439_5_,
        int p_78439_6_,
        float p_78439_7_,
        int brightness
    ) {
        tes.startDrawingQuads();
        tes.setBrightness(brightness);
        tes.setNormal(0.0F, 0.0F, 1.0F);
        tes.addVertexWithUV(0.0D, 0.0D, 0.0D, (double) p_78439_1_, (double) p_78439_4_);
        tes.addVertexWithUV(1.0D, 0.0D, 0.0D, (double) p_78439_3_, (double) p_78439_4_);
        tes.addVertexWithUV(1.0D, 1.0D, 0.0D, (double) p_78439_3_, (double) p_78439_2_);
        tes.addVertexWithUV(0.0D, 1.0D, 0.0D, (double) p_78439_1_, (double) p_78439_2_);
        tes.draw();
        tes.startDrawingQuads();
        tes.setBrightness(brightness);
        tes.setNormal(0.0F, 0.0F, -1.0F);
        tes.addVertexWithUV(
            0.0D,
            1.0D,
            (double) (0.0F - p_78439_7_),
            (double) p_78439_1_,
            (double) p_78439_2_
        );
        tes.addVertexWithUV(
            1.0D,
            1.0D,
            (double) (0.0F - p_78439_7_),
            (double) p_78439_3_,
            (double) p_78439_2_
        );
        tes.addVertexWithUV(
            1.0D,
            0.0D,
            (double) (0.0F - p_78439_7_),
            (double) p_78439_3_,
            (double) p_78439_4_
        );
        tes.addVertexWithUV(
            0.0D,
            0.0D,
            (double) (0.0F - p_78439_7_),
            (double) p_78439_1_,
            (double) p_78439_4_
        );
        tes.draw();
        float f5 = 0.5F * (p_78439_1_ - p_78439_3_) / (float) p_78439_5_;
        float f6 = 0.5F * (p_78439_4_ - p_78439_2_) / (float) p_78439_6_;
        tes.startDrawingQuads();
        tes.setBrightness(brightness);
        tes.setNormal(-1.0F, 0.0F, 0.0F);
        int k;
        float f7;
        float f8;

        for (k = 0; k < p_78439_5_; ++k) {
            f7 = (float) k / (float) p_78439_5_;
            f8 = p_78439_1_ + (p_78439_3_ - p_78439_1_) * f7 - f5;
            tes.addVertexWithUV(
                (double) f7,
                0.0D,
                (double) (0.0F - p_78439_7_),
                (double) f8,
                (double) p_78439_4_
            );
            tes.addVertexWithUV(
                (double) f7, 0.0D, 0.0D, (double) f8, (double) p_78439_4_
            );
            tes.addVertexWithUV(
                (double) f7, 1.0D, 0.0D, (double) f8, (double) p_78439_2_
            );
            tes.addVertexWithUV(
                (double) f7,
                1.0D,
                (double) (0.0F - p_78439_7_),
                (double) f8,
                (double) p_78439_2_
            );
        }

        tes.draw();
        tes.startDrawingQuads();
        tes.setBrightness(brightness);
        tes.setNormal(1.0F, 0.0F, 0.0F);
        float f9;

        for (k = 0; k < p_78439_5_; ++k) {
            f7 = (float) k / (float) p_78439_5_;
            f8 = p_78439_1_ + (p_78439_3_ - p_78439_1_) * f7 - f5;
            f9 = f7 + 1.0F / (float) p_78439_5_;
            tes.addVertexWithUV(
                (double) f9,
                1.0D,
                (double) (0.0F - p_78439_7_),
                (double) f8,
                (double) p_78439_2_
            );
            tes.addVertexWithUV(
                (double) f9, 1.0D, 0.0D, (double) f8, (double) p_78439_2_
            );
            tes.addVertexWithUV(
                (double) f9, 0.0D, 0.0D, (double) f8, (double) p_78439_4_
            );
            tes.addVertexWithUV(
                (double) f9,
                0.0D,
                (double) (0.0F - p_78439_7_),
                (double) f8,
                (double) p_78439_4_
            );
        }

        tes.draw();
        tes.startDrawingQuads();
        tes.setBrightness(brightness);
        tes.setNormal(0.0F, 1.0F, 0.0F);

        for (k = 0; k < p_78439_6_; ++k) {
            f7 = (float) k / (float) p_78439_6_;
            f8 = p_78439_4_ + (p_78439_2_ - p_78439_4_) * f7 - f6;
            f9 = f7 + 1.0F / (float) p_78439_6_;
            tes.addVertexWithUV(
                0.0D, (double) f9, 0.0D, (double) p_78439_1_, (double) f8
            );
            tes.addVertexWithUV(
                1.0D, (double) f9, 0.0D, (double) p_78439_3_, (double) f8
            );
            tes.addVertexWithUV(
                1.0D,
                (double) f9,
                (double) (0.0F - p_78439_7_),
                (double) p_78439_3_,
                (double) f8
            );
            tes.addVertexWithUV(
                0.0D,
                (double) f9,
                (double) (0.0F - p_78439_7_),
                (double) p_78439_1_,
                (double) f8
            );
        }

        tes.draw();
        tes.startDrawingQuads();
        tes.setBrightness(brightness);
        tes.setNormal(0.0F, -1.0F, 0.0F);

        for (k = 0; k < p_78439_6_; ++k) {
            f7 = (float) k / (float) p_78439_6_;
            f8 = p_78439_4_ + (p_78439_2_ - p_78439_4_) * f7 - f6;
            tes.addVertexWithUV(
                1.0D, (double) f7, 0.0D, (double) p_78439_3_, (double) f8
            );
            tes.addVertexWithUV(
                0.0D, (double) f7, 0.0D, (double) p_78439_1_, (double) f8
            );
            tes.addVertexWithUV(
                0.0D,
                (double) f7,
                (double) (0.0F - p_78439_7_),
                (double) p_78439_1_,
                (double) f8
            );
            tes.addVertexWithUV(
                1.0D,
                (double) f7,
                (double) (0.0F - p_78439_7_),
                (double) p_78439_3_,
                (double) f8
            );
        }

        tes.draw();
    }
}

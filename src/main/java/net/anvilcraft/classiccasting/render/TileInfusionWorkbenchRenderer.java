package net.anvilcraft.classiccasting.render;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.anvilcraft.classiccasting.CCBlocks;
import net.anvilcraft.classiccasting.UtilsFX;
import net.anvilcraft.classiccasting.items.wands.ItemWandCasting;
import net.anvilcraft.classiccasting.tiles.TileInfusionWorkbench;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class TileInfusionWorkbenchRenderer extends TileEntitySpecialRenderer {
    public void renderTileEntityAt(
        final TileInfusionWorkbench table,
        final double par2,
        final double par4,
        final double par6,
        final float par8
    ) {
        if (table.getWorldObj() != null && table.getStackInSlot(10) != null) {
            final float bob
                = MathHelper.sin(
                      ((Entity) Minecraft.getMinecraft().renderViewEntity).ticksExisted
                      / 14.0f
                  ) * 0.03f
                + 0.03f;
            final float weave
                = MathHelper.sin(
                      ((Entity) Minecraft.getMinecraft().renderViewEntity).ticksExisted
                      / 10.0f
                  ) * 0.5f
                + 0.5f;
            GL11.glPushMatrix();
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            GL11.glTranslatef(
                (float) par2 + 0.625f, (float) par4 + 1.1f + bob, (float) par6 + 0.625f
            );
            GL11.glRotatef(85.0f + weave * 10.0f, 1.0f, 0.0f, 0.0f);
            UtilsFX.render3DItem(
                table.getStackInSlot(10),
                0,
                0.75f,
                CCBlocks.infusionWorkbench.getMixedBrightnessForBlock(
                    (IBlockAccess) table.getWorldObj(),
                    table.xCoord,
                    table.yCoord + 1,
                    table.zCoord
                )
            );
            GL11.glPopMatrix();
        }
    }

    @Override
    public void renderTileEntityAt(
        final TileEntity par1TileEntity,
        final double par2,
        final double par4,
        final double par6,
        final float par8
    ) {
        this.renderTileEntityAt(
            (TileInfusionWorkbench) par1TileEntity, par2, par4, par6, par8
        );
    }
}

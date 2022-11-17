package net.anvilcraft.classiccasting.render;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.anvilcraft.classiccasting.render.models.ModelAlembic;
import net.anvilcraft.classiccasting.tiles.TileAlembic;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class TileAlembicRenderer extends TileEntitySpecialRenderer {
    private ModelAlembic model;

    public TileAlembicRenderer() {
        this.model = new ModelAlembic();
    }

    public void renderTileEntityAt(
        final TileAlembic tile,
        final double par2,
        final double par4,
        final double par6,
        final float par8
    ) {
        int md = 0;
        if (tile.getWorldObj() != null) {
            md = tile.getBlockMetadata();
        }
        GL11.glPushMatrix();
        this.bindTexture(
            new ResourceLocation("classiccasting", "textures/models/alembic.png")
        );
        GL11.glTranslatef((float) par2 + 0.5f, (float) par4 + 1.5f, (float) par6 + 0.5f);
        GL11.glRotatef(180.0f, 1.0f, 0.0f, 0.0f);
        switch (md) {
            case 0: {
                GL11.glRotatef(180.0f, 0.0f, 1.0f, 0.0f);
                break;
            }
            case 2: {
                GL11.glRotatef(90.0f, 0.0f, 1.0f, 0.0f);
                break;
            }
            case 3: {
                GL11.glRotatef(270.0f, 0.0f, 1.0f, 0.0f);
                break;
            }
        }
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.model.renderAll();
        GL11.glPopMatrix();
    }

    @Override
    public void renderTileEntityAt(
        final TileEntity par1TileEntity,
        final double par2,
        final double par4,
        final double par6,
        final float par8
    ) {
        this.renderTileEntityAt((TileAlembic) par1TileEntity, par2, par4, par6, par8);
    }
}

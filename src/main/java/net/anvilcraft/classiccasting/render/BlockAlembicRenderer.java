package net.anvilcraft.classiccasting.render;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.anvilcraft.classiccasting.blocks.BlockAlembic;
import net.anvilcraft.classiccasting.tiles.TileAlembic;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.renderers.block.BlockRenderer;

public class BlockAlembicRenderer
    extends BlockRenderer implements ISimpleBlockRenderingHandler {
    public static int RI;

    @Override
    public void renderInventoryBlock(
        final Block block,
        final int metadata,
        final int modelID,
        final RenderBlocks renderer
    ) {
        GL11.glRotatef(90.0f, 0.0f, 1.0f, 0.0f);
        GL11.glTranslatef(-0.3f, -0.75f, -0.5f);
        TileEntityRendererDispatcher.instance.renderTileEntityAt(
            (TileEntity) new TileAlembic(), 0.0, 0.0, 0.0, 0.0f
        );
        GL11.glEnable(32826);
    }

    @Override
    public boolean renderWorldBlock(
        final IBlockAccess world,
        final int x,
        final int y,
        final int z,
        final Block block,
        final int modelId,
        final RenderBlocks renderer
    ) {
        final TileEntity te2 = world.getTileEntity(x, y, z);
        if (te2 != null && te2 instanceof TileAlembic && ((TileAlembic) te2).amount > 0) {
            final float level = ((TileAlembic) te2).amount
                / (float) ((TileAlembic) te2).maxAmount * 0.5625f;
            final Tessellator t = Tessellator.instance;
            t.setColorOpaque_I(((TileAlembic) te2).aspect.getColor());
            t.setBrightness(200);
            block.setBlockBounds(0.275f, 0.25f, 0.275f, 0.725f, 0.25f + level, 0.725f);
            renderer.setRenderBoundsFromBlock(block);
            BlockRenderer.renderAllSides(
                world, x, y, z, block, renderer, ((BlockAlembic) block).iconGlow, true
            );
            GL11.glColor3f(1.0f, 1.0f, 1.0f);
        }
        renderer.clearOverrideBlockTexture();
        block.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
        renderer.setRenderBoundsFromBlock(block);
        return true;
    }

    @Override
    public boolean shouldRender3DInInventory(int meta) {
        return true;
    }

    @Override
    public int getRenderId() {
        return RI;
    }
}

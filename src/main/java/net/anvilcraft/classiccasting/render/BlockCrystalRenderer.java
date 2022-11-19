package net.anvilcraft.classiccasting.render;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.anvilcraft.classiccasting.blocks.BlockCrystal;
import net.anvilcraft.classiccasting.tiles.TileCrystalCapacitor;
import net.anvilcraft.classiccasting.tiles.TileCrystalCore;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.renderers.block.BlockRenderer;

public class BlockCrystalRenderer
    extends BlockRenderer implements ISimpleBlockRenderingHandler {
    public static int RI;

    @Override
    public void renderInventoryBlock(
        final Block block,
        final int metadata,
        final int modelID,
        final RenderBlocks renderer
    ) {
        if (metadata == 0) {
            GL11.glRotatef(90.0f, 0.0f, 1.0f, 0.0f);
            GL11.glTranslatef(-0.5f, -0.5f, -0.5f);
            final TileCrystalCore tc2 = new TileCrystalCore();
            tc2.blockMetadata = 0;
            TileEntityRendererDispatcher.instance.renderTileEntityAt(
                (TileEntity) tc2, 0.0, 0.0, 0.0, 0.0f
            );
            GL11.glEnable(32826);
        } else if (metadata == 1) {
            block.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
            renderer.setRenderBoundsFromBlock(block);
            BlockRenderer.drawFaces(
                renderer, block, ((BlockCrystal) block).iconFrame, false
            );
            this.drawFacesInside(renderer, block, ((BlockCrystal) block).iconFrame);
            GL11.glTranslatef(-0.5f, -0.5f, -0.5f);
            final TileCrystalCapacitor tc3 = new TileCrystalCapacitor();
            tc3.blockMetadata = 0;
            tc3.storedVis = 50;
            TileEntityRendererDispatcher.instance.renderTileEntityAt(
                (TileEntity) tc3, 0.0, 0.0, 0.0, 0.0f
            );
            GL11.glEnable(32826);
        }
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
        final int md = world.getBlockMetadata(x, y, z);
        if (md == 1) {
            final IIcon icon = ((BlockCrystal) block).iconFrame;
            BlockRenderer.setBrightness(world, x, y, z, block);
            block.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
            renderer.setRenderBoundsFromBlock(block);
            renderer.renderStandardBlock(block, x, y, z);
            renderer.renderFaceXPos(
                block,
                (double) (x - 1.0f + BlockRenderer.W2 - 0.01f),
                (double) y,
                (double) z,
                icon
            );
            renderer.renderFaceXNeg(
                block,
                (double) (x + 1.0f - BlockRenderer.W2 + 0.01f),
                (double) y,
                (double) z,
                icon
            );
            renderer.renderFaceZPos(
                block,
                (double) x,
                (double) y,
                (double) (z - 1.0f + BlockRenderer.W2 - 0.01f),
                icon
            );
            renderer.renderFaceZNeg(
                block,
                (double) x,
                (double) y,
                (double) (z + 1.0f - BlockRenderer.W2 + 0.01f),
                icon
            );
            renderer.renderFaceYPos(
                block,
                (double) x,
                (double) (y - 1.0f + BlockRenderer.W2 - 0.01f),
                (double) z,
                icon
            );
            renderer.renderFaceYNeg(
                block,
                (double) x,
                (double) (y + 1.0f - BlockRenderer.W2 + 0.01f),
                (double) z,
                icon
            );
            renderer.clearOverrideBlockTexture();
            block.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
            renderer.setRenderBoundsFromBlock(block);
            return true;
        }
        return false;
    }

    void
    drawFacesInside(final RenderBlocks renderblocks, final Block block, final IIcon i1) {
        final Tessellator tessellator = Tessellator.instance;
        GL11.glTranslatef(-0.5f, -0.5f, -0.5f);
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0f, -1.0f, 0.0f);
        renderblocks.renderFaceYNeg(block, 0.0, 1.0 - BlockRenderer.W2, 0.0, i1);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0f, 1.0f, 0.0f);
        renderblocks.renderFaceYPos(block, 0.0, -1.0 + BlockRenderer.W2, 0.0, i1);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0f, 0.0f, 1.0f);
        renderblocks.renderFaceXNeg(block, 1.0 - BlockRenderer.W2, 0.0, 0.0, i1);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0f, 0.0f, -1.0f);
        renderblocks.renderFaceXPos(block, -1.0 + BlockRenderer.W2, 0.0, 0.0, i1);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(1.0f, 0.0f, 0.0f);
        renderblocks.renderFaceZNeg(block, 0.0, 0.0, 1.0 - BlockRenderer.W2, i1);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(-1.0f, 0.0f, 0.0f);
        renderblocks.renderFaceZPos(block, 0.0, 0.0, -1.0 + BlockRenderer.W2, i1);
        tessellator.draw();
        GL11.glTranslatef(0.5f, 0.5f, 0.5f);
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

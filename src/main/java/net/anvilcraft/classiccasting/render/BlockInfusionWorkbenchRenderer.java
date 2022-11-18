package net.anvilcraft.classiccasting.render;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.anvilcraft.classiccasting.blocks.BlockInfusionWorkbench;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.renderers.block.BlockRenderer;

public class BlockInfusionWorkbenchRenderer
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
            block.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
            renderer.setRenderBoundsFromBlock(block);
            BlockRenderer.drawFaces(
                renderer, block, block.getBlockTextureFromSide(0), false
            );
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
        BlockRenderer.setBrightness(world, x, y, z, block);
        final int metadata = world.getBlockMetadata(x, y, z);
        block.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
        renderer.setRenderBoundsFromBlock(block);
        renderer.renderStandardBlock(block, x, y, z);
        if (metadata != 0 && metadata != 7) {
            final Tessellator t = Tessellator.instance;
            t.setColorOpaque_I(12583104);
            t.setBrightness(180);
            BlockRenderer.renderAllSidesInverted(
                world,
                x,
                y,
                z,
                block,
                renderer,
                ((BlockInfusionWorkbench) block).iconGlow,
                false
            );
            block.setBlockBounds(0.02f, 0.02f, 0.02f, 0.98f, 0.98f, 0.98f);
            renderer.setRenderBoundsFromBlock(block);
            BlockRenderer.renderAllSides(
                world,
                x,
                y,
                z,
                block,
                renderer,
                ((BlockInfusionWorkbench) block).iconGlow,
                false
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

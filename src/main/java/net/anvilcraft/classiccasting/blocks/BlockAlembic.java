package net.anvilcraft.classiccasting.blocks;

import java.util.List;

import dev.tilera.auracore.aura.AuraManager;
import net.anvilcraft.classiccasting.ClassicCastingTab;
import net.anvilcraft.classiccasting.render.BlockAlembicRenderer;
import net.anvilcraft.classiccasting.tiles.TileAlembic;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.lib.CustomSoundType;
import thaumcraft.common.tiles.TileCrucible;

public class BlockAlembic extends BlockContainer {
    public IIcon iconGlow;

    public BlockAlembic() {
        super(Material.iron);
        this.setHardness(3.0f);
        this.setResistance(17.0f);
        this.setStepSound(new CustomSoundType("jar", 1.0f, 1.0f));
        this.setBlockName("classiccasting:alembic");
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
        this.setCreativeTab(ClassicCastingTab.INSTANCE);
    }

    @Override
    public void registerBlockIcons(IIconRegister ir) {
        this.iconGlow = ir.registerIcon("thaumcraft:animatedglow");
    }

    @Override
    public int getRenderType() {
        return BlockAlembicRenderer.RI;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public void setBlockBoundsBasedOnState(
        final IBlockAccess world, final int i, final int j, final int k
    ) {
        this.setBlockBounds(0.25f, 0.0f, 0.25f, 0.75f, 0.875f, 0.75f);
    }

    @Override
    public void addCollisionBoxesToList(
        final World world,
        final int i,
        final int j,
        final int k,
        final AxisAlignedBB axisalignedbb,
        final List arraylist,
        final Entity par7Entity
    ) {
        this.setBlockBounds(0.25f, 0.0f, 0.25f, 0.75f, 0.875f, 0.75f);
        super.addCollisionBoxesToList(
            world, i, j, k, axisalignedbb, arraylist, par7Entity
        );
    }

    @Override
    public boolean isBlockSolid(
        final IBlockAccess world, final int i, final int j, final int k, final int side
    ) {
        return side != ForgeDirection.DOWN.ordinal()
            && side != ForgeDirection.UP.ordinal();
    }

    @Override
    public int damageDropped(final int metadata) {
        return 0;
    }

    @Override
    public TileEntity createTileEntity(final World world, final int metadata) {
        return new TileAlembic();
    }

    @Override
    public TileEntity createNewTileEntity(final World var1, int meta) {
        return this.createNewTileEntity(var1, meta);
    }

    @Override
    public void onNeighborBlockChange(
        final World par1World,
        final int par2,
        final int par3,
        final int par4,
        final Block par5
    ) {
        final TileEntity te = par1World.getTileEntity(par2, par3, par4);
        if (te != null && te instanceof TileCrucible) {
            ((TileCrucible) te).getBellows();
        }
        super.onNeighborBlockChange(par1World, par2, par3, par4, par5);
    }

    @Override
    public void breakBlock(
        final World par1World,
        final int par2,
        final int par3,
        final int par4,
        final Block par5,
        final int par6
    ) {
        final TileEntity te = par1World.getTileEntity(par2, par3, par4);
        if (te != null && ((TileAlembic) te).amount > 0) {
            AuraManager.addFluxToClosest(
                par1World,
                par2 + 0.5f,
                par3 + 0.5f,
                par4 + 0.5f,
                new AspectList().add(((TileAlembic) te).aspect, ((TileAlembic) te).amount)
            );
        }
        super.breakBlock(par1World, par2, par3, par4, par5, par6);
    }
}

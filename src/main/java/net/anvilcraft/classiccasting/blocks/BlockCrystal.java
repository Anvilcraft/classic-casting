package net.anvilcraft.classiccasting.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import dev.tilera.auracore.client.FXSparkle;
import net.anvilcraft.classiccasting.ClassicCastingTab;
import net.anvilcraft.classiccasting.render.BlockCrystalRenderer;
import net.anvilcraft.classiccasting.tiles.TileCrystalCapacitor;
import net.anvilcraft.classiccasting.tiles.TileCrystalCore;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagShort;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import thaumcraft.common.blocks.JarStepSound;
import thaumcraft.common.config.ConfigItems;

public class BlockCrystal extends BlockContainer {
    public IIcon icon;
    public IIcon iconFrame;

    public BlockCrystal() {
        super(Material.glass);
        this.setHardness(0.7f);
        this.setResistance(1.0f);
        this.setLightLevel(0.4f);
        this.setStepSound(new JarStepSound("crystal", 1.0f, 1.0f));
        this.setBlockName("classiccasting:crystal");
        this.setCreativeTab(ClassicCastingTab.INSTANCE);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(
        final Item par1, final CreativeTabs par2CreativeTabs, final List par3List
    ) {
        // crystal core
        par3List.add(new ItemStack(par1, 1, 0));

        // crystal capacitor
        par3List.add(new ItemStack(par1, 1, 1));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(final IIconRegister ir) {
        this.icon = ir.registerIcon("classiccasting:crystal");
        this.iconFrame = ir.registerIcon("classiccasting:crystalframe");
    }

    @Override
    public IIcon getIcon(final int par1, final int meta) {
        if (meta == 1) {
            return this.iconFrame;
        }
        return null;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(
        final World world, final int i, final int j, final int k, final Random random
    ) {
        final int md = world.getBlockMetadata(i, j, k);
        if (md == 1 || random.nextInt(4) != 0) {
            return;
        }
        float mod = 0.2f;
        final TileEntity te = world.getTileEntity(i, j, k);
        if (te != null && te instanceof TileCrystalCore) {
            mod += ((TileCrystalCore) te).speed;
        }
        final int c = random.nextInt(5);
        final FXSparkle ef2 = new FXSparkle(
            world,
            i + 0.2f + world.rand.nextFloat() * 0.6f,
            j + mod + world.rand.nextFloat() * 0.6f,
            k + 0.2f + world.rand.nextFloat() * 0.6f,
            1.0f,
            c,
            3
        );
        ((Entity) ef2).noClip = true;
        Minecraft.getMinecraft().effectRenderer.addEffect((EntityFX) ef2);
    }

    @Override
    public int colorMultiplier(
        final IBlockAccess par1iBlockAccess,
        final int par2,
        final int par3,
        final int par4
    ) {
        final int md = par1iBlockAccess.getBlockMetadata(par2, par3, par4);
        if (md < 1) {
            return 0;
        }
        return super.colorMultiplier(par1iBlockAccess, par2, par3, par4);
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
    public int getRenderType() {
        return BlockCrystalRenderer.RI;
    }

    @Override
    public TileEntity createTileEntity(final World world, final int metadata) {
        if (metadata == 0) {
            return new TileCrystalCore();
        }
        if (metadata == 1) {
            return new TileCrystalCapacitor();
        }
        return super.createTileEntity(world, metadata);
    }

    @Override
    public TileEntity createNewTileEntity(final World var1, int meta) {
        return this.createTileEntity(var1, meta);
    }

    @Override
    public ArrayList<ItemStack> getDrops(
        final World world,
        final int x,
        final int y,
        final int z,
        final int md,
        final int fortune
    ) {
        final ArrayList<ItemStack> ret = new ArrayList<>();
        if (md == 0) {
            ret.add(new ItemStack(Items.nether_star));
            int total = 0;
            for (int t = 0; t < 5; ++t) {
                final int q = 2 + world.rand.nextInt(3);
                total += q;
                for (int a = 0; a < q; ++a) {
                    ret.add(new ItemStack(ConfigItems.itemShard, 1, t));
                }
            }
            if (20 - total > 0) {
                ret.add(new ItemStack(ConfigItems.itemShard, 20 - total, 5));
            }
            return ret;
        }
        if (md == 1) {
            return new ArrayList<>();
        }
        return super.getDrops(world, x, y, z, md, fortune);
    }

    // TODO: WTF
    //@Override
    //public int idDropped(final int par1, final Random par2Random, final int par3) {
    //    if (par1 == 6) {
    //        return Item.netherStar.field_77779_bT;
    //    }
    //    return super.idDropped(par1, par2Random, par3);
    //}

    @Override
    public int damageDropped(int meta) {
        return meta;
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
        if (par6 == 1) {
            final TileEntity te = par1World.getTileEntity(par2, par3, par4);
            if (te != null && te instanceof TileCrystalCapacitor) {
                if (((TileCrystalCapacitor) te).storedVis > 0) {
                    final ItemStack drop = new ItemStack((Block) this, 1, 1);
                    drop.setTagInfo(
                        "amount", new NBTTagShort(((TileCrystalCapacitor) te).storedVis)
                    );
                    this.dropBlockAsItem(par1World, par2, par3, par4, drop);
                } else {
                    this.dropBlockAsItem(
                        par1World, par2, par3, par4, new ItemStack((Block) this, 1, 1)
                    );
                }
            }
        }
        super.breakBlock(par1World, par2, par3, par4, par5, par6);
    }
}

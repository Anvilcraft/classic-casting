package net.anvilcraft.classiccasting.blocks;

import java.util.List;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.anvilcraft.classiccasting.tiles.TileInfusionWorkbench;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import thaumcraft.common.Thaumcraft;

public class BlockInfusionWorkbench extends BlockContainer {
    public IIcon[] icon;

    public BlockInfusionWorkbench() {
        super(Material.rock);
        this.icon = new IIcon[7];
        this.setHardness(4.0f);
        this.setResistance(100.0f);
        this.setStepSound(BlockInfusionWorkbench.soundTypeStone);
        this.setBlockName("blockInfusionWorkbench");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(final IIconRegister ir) {
        this.icon[0] = ir.registerIcon("thaumcraft:infusionbase");
        for (int a = 1; a <= 6; ++a) {
            this.icon[a] = ir.registerIcon("thaumcraft:infusion" + a);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(
        final Item par1, final CreativeTabs par2CreativeTabs, final List par3List
    ) {
        par3List.add(new ItemStack(par1, 1, 0));
    }

    @Override
    public IIcon getIcon(final int side, final int md) {
    Label_0214: {
        switch (md) {
            case 1: {
                switch (side) {
                    case 0:
                    case 1: {
                        return this.icon[1];
                    }
                    case 2:
                    case 5: {
                        return this.icon[6];
                    }
                    case 3:
                    case 4: {
                        return this.icon[5];
                    }
                    default: {
                        break Label_0214;
                    }
                }
            }
            case 2: {
                switch (side) {
                    case 0:
                    case 1: {
                        return this.icon[2];
                    }
                    case 2:
                    case 3: {
                        return this.icon[5];
                    }
                    case 4:
                    case 5: {
                        return this.icon[6];
                    }
                    default: {
                        break Label_0214;
                    }
                }
            }
            case 3: {
                switch (side) {
                    case 0:
                    case 1: {
                        return this.icon[3];
                    }
                    case 2:
                    case 3: {
                        return this.icon[5];
                    }
                    case 4:
                    case 5: {
                        return this.icon[6];
                    }
                    default: {
                        break Label_0214;
                    }
                }
            }
            case 4: {
                switch (side) {
                    case 0:
                    case 1: {
                        return this.icon[4];
                    }
                    case 2:
                    case 5: {
                        return this.icon[5];
                    }
                    case 3:
                    case 4: {
                        return this.icon[6];
                    }
                    default: {
                        break Label_0214;
                    }
                }
            }
        }
    }
        return this.icon[0];
    }

    @Override
    public TileEntity createTileEntity(final World world, final int metadata) {
        if (metadata == 1) {
            return new TileInfusionWorkbench();
        }
        return null;
    }

    @Override
    public boolean isOpaqueCube() {
        return true;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public int getRenderType() {
        // TODO: WTF
        //return Config.blockInfusionWorkbenchRI;
        return 0;
    }

    @Override
    public boolean isBlockSolid(
        final IBlockAccess world, final int x, final int y, final int z, final int side
    ) {
        return true;
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
        this.dropItems(par1World, par2, par3, par4);
        super.breakBlock(par1World, par2, par3, par4, par5, par6);
    }

    private void dropItems(final World world, final int x, final int y, final int z) {
        final Random rand = new Random();
        final TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (!(tileEntity instanceof IInventory)) {
            return;
        }
        final IInventory inventory = (IInventory) tileEntity;
        for (int i = 0; i < inventory.getSizeInventory(); ++i) {
            if (i != 9) {
                final ItemStack item = inventory.getStackInSlot(i);
                if (item != null && item.stackSize > 0) {
                    final float rx = rand.nextFloat() * 0.8f + 0.1f;
                    final float ry = rand.nextFloat() * 0.8f + 0.1f;
                    final float rz = rand.nextFloat() * 0.8f + 0.1f;
                    final EntityItem entityItem = new EntityItem(
                        world,
                        (double) (x + rx + 0.5f),
                        (double) (y + ry),
                        (double) (z + rz + 0.5f),
                        item.copy()
                    );
                    if (item.hasTagCompound()) {
                        entityItem.getEntityItem().setTagCompound(
                            (NBTTagCompound) item.getTagCompound().copy()
                        );
                    }
                    final float factor = 0.05f;
                    ((Entity) entityItem).motionX = rand.nextGaussian() * factor;
                    ((Entity) entityItem).motionY
                        = rand.nextGaussian() * factor + 0.20000000298023224;
                    ((Entity) entityItem).motionZ = rand.nextGaussian() * factor;
                    world.spawnEntityInWorld((Entity) entityItem);
                    item.stackSize = 0;
                }
            }
        }
    }

    @Override
    public int damageDropped(final int par1) {
        return 0;
    }

    @Override
    public void onNeighborBlockChange(
        final World world, final int x, final int y, final int z, final Block par5
    ) {
        final TileEntity tile = world.getTileEntity(x, y, z);
        final int md = world.getBlockMetadata(x, y, z);
        switch (md) {
            case 1: {
                if (world.getBlock(x + 1, y, z) != this
                    || world.getBlockMetadata(x + 1, y, z) != 2
                    || world.getBlock(x, y, z + 1) != this
                    || world.getBlockMetadata(x, y, z + 1) != 3
                    || world.getBlock(x + 1, y, z + 1) != this
                    || world.getBlockMetadata(x + 1, y, z + 1) != 4) {
                    this.dropItems(world, x, y, z);
                    world.setBlock(x, y, z, this, 0, 3);
                    world.addBlockEvent(x, y, z, this, 2, 6);
                    break;
                }
                break;
            }
            case 2: {
                if (world.getBlock(x - 1, y, z) != this
                    || world.getBlockMetadata(x - 1, y, z) != 1
                    || world.getBlock(x - 1, y, z + 1) != this
                    || world.getBlockMetadata(x - 1, y, z + 1) != 3
                    || world.getBlock(x, y, z + 1) != this
                    || world.getBlockMetadata(x, y, z + 1) != 4) {
                    world.setBlock(x, y, z, this, 0, 3);
                    world.addBlockEvent(x, y, z, this, 2, 6);
                    break;
                }
                break;
            }
            case 3: {
                if (world.getBlock(x, y, z - 1) != this
                    || world.getBlockMetadata(x, y, z - 1) != 1
                    || world.getBlock(x + 1, y, z - 1) != this
                    || world.getBlockMetadata(x + 1, y, z - 1) != 2
                    || world.getBlock(x + 1, y, z) != this
                    || world.getBlockMetadata(x + 1, y, z) != 4) {
                    world.setBlock(x, y, z, this, 0, 3);
                    world.addBlockEvent(x, y, z, this, 2, 6);
                    break;
                }
                break;
            }
            case 4: {
                if (world.getBlock(x - 1, y, z - 1) != this
                    || world.getBlockMetadata(x - 1, y, z - 1) != 1
                    || world.getBlock(x, y, z - 1) != this
                    || world.getBlockMetadata(x, y, z - 1) != 2
                    || world.getBlock(x - 1, y, z) != this
                    || world.getBlockMetadata(x - 1, y, z) != 3) {
                    world.setBlock(x, y, z, this, 0, 3);
                    world.addBlockEvent(x, y, z, this, 2, 6);
                    break;
                }
                break;
            }
        }
        super.onNeighborBlockChange(world, x, y, z, par5);
    }

    @Override
    public boolean onBlockActivated(
        final World world,
        final int x,
        final int y,
        final int z,
        final EntityPlayer player,
        final int idk,
        final float what,
        final float these,
        final float are
    ) {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        final int md = world.getBlockMetadata(x, y, z);
        if (md == 0 || player.isSneaking()) {
            return false;
        }
        if (world.isRemote) {
            return true;
        }
        if (tileEntity != null && tileEntity instanceof TileInfusionWorkbench) {
            player.openGui((Object) Thaumcraft.instance, 14, world, x, y, z);
            return true;
        }
        switch (md) {
            case 2: {
                tileEntity = world.getTileEntity(x - 1, y, z);
                if (tileEntity != null && tileEntity instanceof TileInfusionWorkbench) {
                    player.openGui((Object) Thaumcraft.instance, 14, world, x - 1, y, z);
                    return true;
                }
                return false;
            }
            case 3: {
                tileEntity = world.getTileEntity(x, y, z - 1);
                if (tileEntity != null && tileEntity instanceof TileInfusionWorkbench) {
                    player.openGui((Object) Thaumcraft.instance, 14, world, x, y, z - 1);
                    return true;
                }
                return false;
            }
            case 4: {
                tileEntity = world.getTileEntity(x - 1, y, z - 1);
                if (tileEntity != null && tileEntity instanceof TileInfusionWorkbench) {
                    player.openGui(
                        (Object) Thaumcraft.instance, 14, world, x - 1, y, z - 1
                    );
                    return true;
                }
                return false;
            }
            default: {
                return false;
            }
        }
    }

    @Override
    public TileEntity createNewTileEntity(final World var1, int meta) {
        return null;
    }

    @Override
    public boolean isBeaconBase(
        final IBlockAccess world,
        final int x,
        final int y,
        final int z,
        final int beaconX,
        final int beaconY,
        final int beaconZ
    ) {
        final int md = world.getBlockMetadata(x, y, z);
        return md == 0;
    }

    @Override
    public boolean onBlockEventReceived(
        final World par1World,
        final int par2,
        final int par3,
        final int par4,
        final int par5,
        final int par6
    ) {
        if (par5 == 1) {
            if (par1World.isRemote) {
                Thaumcraft.proxy.blockSparkle(par1World, par2, par3, par4, par6, 5);
            }
            return true;
        }
        if (par5 == 2) {
            for (int var5 = 0; var5 < 8; ++var5) {
                par1World.spawnParticle(
                    "largesmoke",
                    par2 + Math.random(),
                    par3 + Math.random(),
                    par4 + Math.random(),
                    0.0,
                    0.0,
                    0.0
                );
            }
            par1World.playSoundEffect(
                (double) par2,
                (double) par3,
                (double) par4,
                "random.fizz",
                0.5f,
                2.6f + (par1World.rand.nextFloat() - par1World.rand.nextFloat()) * 0.8f
            );
            return true;
        }
        return super.onBlockEventReceived(par1World, par2, par3, par4, par5, par6);
    }
}

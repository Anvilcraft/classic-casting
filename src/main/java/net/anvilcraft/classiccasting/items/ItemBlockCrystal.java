package net.anvilcraft.classiccasting.items;

import java.util.List;

import net.anvilcraft.classiccasting.ClassicCastingTab;
import net.anvilcraft.classiccasting.tiles.TileCrystalCapacitor;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemBlockCrystal extends ItemBlock {
    public ItemBlockCrystal(final Block par1) {
        super(par1);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
        this.setCreativeTab(ClassicCastingTab.INSTANCE);
        this.setUnlocalizedName("classiccasting:crystal");
    }

    @Override
    public int getMetadata(final int par1) {
        return par1;
    }

    @Override
    public String getUnlocalizedName(final ItemStack par1ItemStack) {
        return super.getUnlocalizedName() + "." + par1ItemStack.getItemDamage();
    }

    @Override
    public boolean placeBlockAt(
        final ItemStack stack,
        final EntityPlayer player,
        final World world,
        final int x,
        final int y,
        final int z,
        final int side,
        final float hitX,
        final float hitY,
        final float hitZ,
        final int metadata
    ) {
        final boolean placed = super.placeBlockAt(
            stack, player, world, x, y, z, side, hitX, hitY, hitZ, metadata
        );
        if (placed && metadata == 1 && stack.hasTagCompound()) {
            final int amount = stack.stackTagCompound.getShort("amount");
            final TileCrystalCapacitor tcc
                = (TileCrystalCapacitor) world.getTileEntity(x, y, z);
            if (tcc != null) {
                tcc.storedVis = (short) amount;
            }
        }
        return placed;
    }

    @Override
    public void addInformation(
        final ItemStack item,
        final EntityPlayer par2EntityPlayer,
        final List list,
        final boolean par4
    ) {
        if (item.hasTagCompound()) {
            final int amount = item.stackTagCompound.getShort("amount");
            list.add("Holds " + amount + " vis");
        }
    }
}

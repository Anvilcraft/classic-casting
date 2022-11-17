package net.anvilcraft.classiccasting.tiles;

import net.anvilcraft.classiccasting.items.wands.ItemWandCasting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;

public abstract class TileMagicWorkbench extends TileEntity implements ISidedInventory {
    public ItemStack[] stackList;
    public Container eventHandler;
    protected int count;

    public TileMagicWorkbench() {
        this.stackList = new ItemStack[11];
    }

    @Override
    public int getSizeInventory() {
        return this.stackList.length;
    }

    @Override
    public ItemStack getStackInSlot(final int par1) {
        return (par1 >= this.getSizeInventory()) ? null : this.stackList[par1];
    }

    // TODO: WTF
    //@Override
    //public ItemStack getStackInRowAndColumn(final int par1, final int par2) {
    //    if (par1 >= 0 && par1 < 3) {
    //        final int var3 = par1 + par2 * 3;
    //        return this.getStackInSlot(var3);
    //    }
    //    return null;
    //}

    @Override
    public ItemStack getStackInSlotOnClosing(final int par1) {
        if (this.stackList[par1] != null) {
            final ItemStack var2 = this.stackList[par1];
            this.stackList[par1] = null;
            return var2;
        }
        return null;
    }

    @Override
    public ItemStack decrStackSize(final int par1, final int par2) {
        if (this.stackList[par1] == null) {
            return null;
        }
        if (this.stackList[par1].stackSize <= par2) {
            final ItemStack var3 = this.stackList[par1];
            this.stackList[par1] = null;
            if (this.eventHandler != null) {
                this.eventHandler.onCraftMatrixChanged((IInventory) this);
            }
            return var3;
        }
        final ItemStack var3 = this.stackList[par1].splitStack(par2);
        if (this.stackList[par1].stackSize == 0) {
            this.stackList[par1] = null;
        }
        if (this.eventHandler != null) {
            this.eventHandler.onCraftMatrixChanged((IInventory) this);
        }
        return var3;
    }

    @Override
    public void setInventorySlotContents(final int par1, final ItemStack par2ItemStack) {
        this.stackList[par1] = par2ItemStack;
        if (this.eventHandler != null) {
            this.eventHandler.onCraftMatrixChanged((IInventory) this);
        }
    }

    public void
    setInventorySlotContentsSoftly(final int par1, final ItemStack par2ItemStack) {
        this.stackList[par1] = par2ItemStack;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public void markDirty() {
        super.markDirty();
    }

    @Override
    public boolean isUseableByPlayer(final EntityPlayer par1EntityPlayer) {
        return true;
    }

    @Override
    public void readFromNBT(final NBTTagCompound par1NBTTagCompound) {
        super.readFromNBT(par1NBTTagCompound);
        final NBTTagList var2 = par1NBTTagCompound.getTagList("inventory", 10);
        this.stackList = new ItemStack[this.getSizeInventory()];
        for (int var3 = 0; var3 < var2.tagCount(); ++var3) {
            final NBTTagCompound var4 = (NBTTagCompound) var2.getCompoundTagAt(var3);
            final int var5 = var4.getByte("slot") & 0xFF;
            if (var5 >= 0 && var5 < this.stackList.length) {
                this.stackList[var5] = ItemStack.loadItemStackFromNBT(var4);
            }
        }
    }

    @Override
    public void writeToNBT(final NBTTagCompound par1NBTTagCompound) {
        super.writeToNBT(par1NBTTagCompound);
        final NBTTagList var2 = new NBTTagList();
        for (int var3 = 0; var3 < this.stackList.length; ++var3) {
            if (this.stackList[var3] != null) {
                final NBTTagCompound var4 = new NBTTagCompound();
                var4.setByte("slot", (byte) var3);
                this.stackList[var3].writeToNBT(var4);
                var2.appendTag((NBTBase) var4);
            }
        }
        par1NBTTagCompound.setTag("inventory", (NBTBase) var2);
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        if (!this.worldObj.isRemote) {
            ++this.count;
            if (this.getStackInSlot(10) != null
                && this.getStackInSlot(10).getItem() instanceof ItemWandCasting
                && this.count
                        % ((ItemWandCasting) this.getStackInSlot(10).getItem())
                              .getRechargeInterval()
                    == 0
                && ((ItemWandCasting) this.getStackInSlot(10).getItem())
                       .recharge(
                           this.getStackInSlot(10),
                           this.worldObj,
                           this.count,
                           this.xCoord,
                           this.yCoord,
                           this.zCoord
                       )) {
                this.setInventorySlotContents(10, this.getStackInSlot(10));
            }
        }
    }

    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }

    @Override
    public boolean isItemValidForSlot(final int i, final ItemStack itemstack) {
        return true;
    }
}

package net.anvilcraft.classiccasting.container;

import net.anvilcraft.classiccasting.research.ClassicResearchTableExtension;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerResearchTable extends Container {
    public ClassicResearchTableExtension extension;

    public ContainerResearchTable(
        InventoryPlayer iinventory, ClassicResearchTableExtension extension
    ) {
        this.extension = extension;

        for (int a = 0; a < 5; ++a) {
            this.addSlotToContainer(new Slot(extension, a, 15, 16 + 24 * a));
        }

        this.addSlotToContainer(new SlotResearchTableOutput(extension, 5, 75, 65));
        this.addSlotToContainer(new SlotResearchTablePaper(extension, 6, 75, 97));
        this.bindPlayerInventory(iinventory);
    }

    protected void bindPlayerInventory(InventoryPlayer inventoryPlayer) {
        int i;
        for (i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlotToContainer(
                    new Slot(inventoryPlayer, j + i * 9 + 9, 40 + j * 18, 160 + i * 18)
                );
            }
        }

        for (i = 0; i < 9; ++i) {
            this.addSlotToContainer(new Slot(inventoryPlayer, i, 40 + i * 18, 218));
        }
    }

    public boolean enchantItem(EntityPlayer par1EntityPlayer, int button) {
        if (button == 0) {
            this.extension.researcher = par1EntityPlayer;
            this.extension.startResearch();
            return true;
        } else if (button == 1) {
            this.extension.toggleSafe();
            return true;
        } else {
            return false;
        }
    }

    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int slot) {
        ItemStack stack = null;
        Slot slotObject = (Slot) super.inventorySlots.get(slot);
        if (slotObject != null && slotObject.getHasStack()) {
            ItemStack stackInSlot = slotObject.getStack();
            stack = stackInSlot.copy();
            if (slot < 7) {
                if (!this.mergeItemStack(
                        stackInSlot, 7, super.inventorySlots.size(), true
                    )) {
                    return null;
                }
            } else if (!this.mergeItemStack(stackInSlot, 0, 7, false)) {
                return null;
            }

            if (stackInSlot.stackSize == 0) {
                slotObject.putStack((ItemStack) null);
            } else {
                slotObject.onSlotChanged();
            }
        }

        return stack;
    }

    protected boolean
    mergeItemStack(ItemStack par1ItemStack, int par2, int par3, boolean par4) {
        boolean var5 = false;
        int var6 = par2;
        if (par4) {
            var6 = par3 - 1;
        }

        Slot var7;
        ItemStack var8;
        if (par1ItemStack.isStackable()) {
            while (par1ItemStack.stackSize > 0
                   && (!par4 && var6 < par3 || par4 && var6 >= par2)) {
                var7 = (Slot) super.inventorySlots.get(var6);
                var8 = var7.getStack();
                if (var8 != null && var7.isItemValid(par1ItemStack)
                    && var8.getItem() == par1ItemStack.getItem()
                    && (!par1ItemStack.getHasSubtypes()
                        || par1ItemStack.getItemDamage() == var8.getItemDamage())
                    && ItemStack.areItemStackTagsEqual(par1ItemStack, var8)) {
                    int var9 = var8.stackSize + par1ItemStack.stackSize;
                    if (var9 <= par1ItemStack.getMaxStackSize()) {
                        par1ItemStack.stackSize = 0;
                        var8.stackSize = var9;
                        var7.onSlotChanged();
                        var5 = true;
                    } else if (var8.stackSize < par1ItemStack.getMaxStackSize()) {
                        par1ItemStack.stackSize
                            -= par1ItemStack.getMaxStackSize() - var8.stackSize;
                        var8.stackSize = par1ItemStack.getMaxStackSize();
                        var7.onSlotChanged();
                        var5 = true;
                    }
                }

                if (par4) {
                    --var6;
                } else {
                    ++var6;
                }
            }
        }

        if (par1ItemStack.stackSize > 0) {
            if (par4) {
                var6 = par3 - 1;
            } else {
                var6 = par2;
            }

            while (!par4 && var6 < par3 || par4 && var6 >= par2) {
                var7 = (Slot) super.inventorySlots.get(var6);
                var8 = var7.getStack();
                if (var8 == null && var7.isItemValid(par1ItemStack)) {
                    var7.putStack(par1ItemStack.copy());
                    var7.onSlotChanged();
                    par1ItemStack.stackSize = 0;
                    var5 = true;
                    break;
                }

                if (par4) {
                    --var6;
                } else {
                    ++var6;
                }
            }
        }

        return var5;
    }

    public boolean canInteractWith(EntityPlayer player) {
        return this.extension.isUseableByPlayer(player);
    }
}

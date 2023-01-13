package net.anvilcraft.classiccasting.container;

import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

class SlotResearchTablePaper extends Slot {
    public SlotResearchTablePaper(
        IInventory par2IInventory, int par3, int par4, int par5
    ) {
        super(par2IInventory, par3, par4, par5);
    }

    public boolean isItemValid(ItemStack par1ItemStack) {
        return par1ItemStack.isItemEqual(new ItemStack(Items.paper));
    }
}

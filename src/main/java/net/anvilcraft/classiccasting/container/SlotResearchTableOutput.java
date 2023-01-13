package net.anvilcraft.classiccasting.container;

import net.anvilcraft.classiccasting.CCItems;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

class SlotResearchTableOutput extends Slot {
    public SlotResearchTableOutput(
        IInventory par2IInventory, int par3, int par4, int par5
    ) {
        super(par2IInventory, par3, par4, par5);
    }

    public boolean isItemValid(ItemStack par1ItemStack) {
        return par1ItemStack.getItem() == CCItems.researchNotes
            && par1ItemStack.getItemDamage() < 64;
    }
}

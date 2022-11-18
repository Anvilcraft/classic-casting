package net.anvilcraft.classiccasting.container;

import dev.tilera.auracore.api.IWand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotWand extends Slot {
    public SlotWand(IInventory par2IInventory, int par3, int par4, int par5) {
        super(par2IInventory, par3, par4, par5);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return stack != null && stack.getItem() != null
            && stack.getItem() instanceof IWand
            // This is so vanilla thaumcraft wands don't work.
            && ((IWand) stack.getItem()).getMaxVis(stack) > 0;
    }

    @Override
    public int getSlotStackLimit() {
        return 1;
    }

    @Override
    public void onPickupFromSlot(EntityPlayer par1EntityPlayer, ItemStack par2ItemStack) {
        super.onPickupFromSlot(par1EntityPlayer, par2ItemStack);
    }
}

package net.anvilcraft.classiccasting.container;

import net.anvilcraft.classiccasting.CCItems;
import net.anvilcraft.classiccasting.research.CCResearchManager;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.lib.research.ResearchManager;
import thaumcraft.common.lib.research.ResearchNoteData;

class SlotResearchTableOutput extends Slot {
    public SlotResearchTableOutput(
        IInventory par2IInventory, int par3, int par4, int par5
    ) {
        super(par2IInventory, par3, par4, par5);
    }

    public boolean isItemValid(ItemStack par1ItemStack) {
        return (par1ItemStack.getItem() == CCItems.researchNotes || par1ItemStack.getItem() == ConfigItems.itemResearchNotes)
            && par1ItemStack.getItemDamage() < 64;
    }

    @Override
    public void putStack(ItemStack note) {
        if (note != null && note.getItem() == ConfigItems.itemResearchNotes) {
            ResearchNoteData data = ResearchManager.getData(note);
            if (data != null && !CCResearchManager.isDiscoverable(data.key)) {
                note = CCResearchManager.createNote(new ItemStack(CCItems.researchNotes), data.key);
            } else {
                note = new ItemStack(Items.paper);
            }
        }
        super.putStack(note);
    }
}

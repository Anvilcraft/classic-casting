package net.anvilcraft.classiccasting.container;

import cpw.mods.fml.common.FMLCommonHandler;
import dev.tilera.auracore.api.crafting.IInfusionRecipe;
import net.anvilcraft.classiccasting.WandManager;
import net.anvilcraft.classiccasting.recipes.InfusionCraftingManager;
import net.anvilcraft.classiccasting.tiles.TileInfusionWorkbench;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IAspectSource;

public class SlotCraftingInfusionWorkbench extends SlotCrafting {
    private final IInventory craftMatrix;
    private EntityPlayer thePlayer;

    public SlotCraftingInfusionWorkbench(
        final EntityPlayer par1EntityPlayer,
        final IInventory par2IInventory,
        final IInventory par3IInventory,
        final int par4,
        final int par5,
        final int par6
    ) {
        super(par1EntityPlayer, par2IInventory, par3IInventory, par4, par5, par6);
        this.thePlayer = par1EntityPlayer;
        this.craftMatrix = par2IInventory;
    }

    @Override
    public void
    onPickupFromSlot(final EntityPlayer par1EntityPlayer, final ItemStack par1ItemStack) {
        FMLCommonHandler.instance().firePlayerCraftingEvent(
            this.thePlayer, par1ItemStack, this.craftMatrix
        );
        this.onCrafting(par1ItemStack);
        int cost;
        //int cost = ThaumcraftCraftingManager.findMatchingArcaneRecipeCost(
        //    this.craftMatrix, this.thePlayer
        //);
        //if (cost == 0) {
        IInfusionRecipe rec = InfusionCraftingManager.INSTANCE.findMatchingInfusionRecipe(
            this.craftMatrix, this.thePlayer
        );
        if (rec != null) {
            cost = rec.getCost();
            final AspectList tags = rec.getAspects();
            if (tags != null && tags.size() > 0) {
                final TileInfusionWorkbench tiwb
                    = (TileInfusionWorkbench) this.craftMatrix;
                for (final Aspect tag : tags.getAspects()) {
                    final IAspectSource as = tiwb.foundAspects.getSource(tag);
                    if (as != null) {
                        as.takeFromContainer(tag, tags.getAmount(tag));
                    }
                }
            }
            //}
            if (cost > 0) {
                WandManager.spendCharge(
                    this.craftMatrix.getStackInSlot(10), par1EntityPlayer, cost
                );
            }
        }
        for (int var2 = 0; var2 < 9; ++var2) {
            final ItemStack var3 = this.craftMatrix.getStackInSlot(var2);
            if (var3 != null) {
                this.craftMatrix.decrStackSize(var2, 1);
                if (var3.getItem().hasContainerItem(var3)) {
                    ItemStack var4 = var3.getItem().getContainerItem(var3);
                    if (var4.isItemStackDamageable()
                        && var4.getItemDamage() > var4.getMaxDamage()) {
                        MinecraftForge.EVENT_BUS.post(
                            new PlayerDestroyItemEvent(this.thePlayer, var4)
                        );
                        var4 = null;
                    }
                    if (var4 != null
                        && (!var3.getItem().doesContainerItemLeaveCraftingGrid(var3)
                            || !this.thePlayer.inventory.addItemStackToInventory(var4))) {
                        if (this.craftMatrix.getStackInSlot(var2) == null) {
                            this.craftMatrix.setInventorySlotContents(var2, var4);
                        } else {
                            this.thePlayer.entityDropItem(var4, 1.5f);
                        }
                    }
                }
            }
        }
    }
}

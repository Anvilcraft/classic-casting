package net.anvilcraft.classiccasting.container;

import dev.tilera.auracore.api.IWand;
import dev.tilera.auracore.api.crafting.IInfusionRecipe;
import dev.tilera.auracore.crafting.AuracoreCraftingManager;
import net.anvilcraft.classiccasting.WandManager;
import net.anvilcraft.classiccasting.tiles.TileInfusionWorkbench;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.IArcaneRecipe;
import thaumcraft.common.container.ContainerDummy;
import thaumcraft.common.items.wands.ItemWandCasting;
import thaumcraft.common.tiles.TileMagicWorkbench;

public class ContainerInfusionWorkbench extends Container {
    private TileInfusionWorkbench tileEntity;
    private InventoryPlayer ip;

    public ContainerInfusionWorkbench(
        final InventoryPlayer par1InventoryPlayer, final TileInfusionWorkbench e
    ) {
        this.tileEntity = e;
        this.tileEntity.eventHandler = this;
        this.ip = par1InventoryPlayer;
        this.addSlotToContainer((Slot) new SlotCraftingInfusionWorkbench(
            par1InventoryPlayer.player,
            (IInventory) this.tileEntity,
            (IInventory) this.tileEntity,
            9,
            132,
            28
        ));
        this.addSlotToContainer((Slot
        ) new SlotWand((IInventory) this.tileEntity, 10, 132, 61));
        for (int var6 = 0; var6 < 3; ++var6) {
            for (int var7 = 0; var7 < 3; ++var7) {
                this.addSlotToContainer(new Slot(
                    (IInventory) this.tileEntity,
                    var7 + var6 * 3,
                    36 + var7 * 20,
                    8 + var6 * 20
                ));
            }
        }
        for (int var6 = 0; var6 < 3; ++var6) {
            for (int var7 = 0; var7 < 9; ++var7) {
                this.addSlotToContainer(new Slot(
                    (IInventory) par1InventoryPlayer,
                    var7 + var6 * 9 + 9,
                    8 + var7 * 18,
                    106 + var6 * 18
                ));
            }
        }
        for (int var6 = 0; var6 < 9; ++var6) {
            this.addSlotToContainer(
                new Slot((IInventory) par1InventoryPlayer, var6, 8 + var6 * 18, 164)
            );
        }
        this.onCraftMatrixChanged((IInventory) this.tileEntity);
    }

    public void onCraftMatrixChanged(final IInventory par1IInventory) {
        this.tileEntity.dispTags = new AspectList();
        final InventoryCrafting ic
            = new InventoryCrafting((Container) new ContainerDummy(), 3, 3);
        for (int a = 0; a < 9; ++a) {
            ic.setInventorySlotContents(a, this.tileEntity.getStackInSlot(a));
        }
        this.tileEntity.setInventorySlotContentsSoftly(
            9,
            CraftingManager.getInstance().findMatchingRecipe(
                ic, this.tileEntity.getWorldObj()
            )
        );
        TileMagicWorkbench bridge
            = AuracoreCraftingManager.createBridgeInventory(this.tileEntity, 0, 9);
        if (this.tileEntity.getStackInSlot(9) == null
            && this.tileEntity.getStackInSlot(10) != null
            && this.tileEntity.getStackInSlot(10).getItem() instanceof IWand) {
            IArcaneRecipe recipe = AuracoreCraftingManager.findMatchingArcaneRecipe(
                bridge, this.ip.player
            );
            if (recipe != null
                && WandManager.hasCharge(
                    this.tileEntity.getStackInSlot(10),
                    this.ip.player,
                    AuracoreCraftingManager.getArcaneRecipeVisCost(recipe, bridge)
                )) {
                this.tileEntity.setInventorySlotContentsSoftly(
                    9, recipe.getCraftingResult(bridge)
                );
            }
        }

        if (this.tileEntity.getStackInSlot(9) == null
            && this.tileEntity.getStackInSlot(10) != null) {
            IInfusionRecipe rec = AuracoreCraftingManager.findMatchingInfusionRecipe(
                bridge, this.ip.player
            );

            if (rec != null
                && WandManager.hasCharge(
                    this.tileEntity.getStackInSlot(10), this.ip.player, rec.getCost()
                )
                && this.tileEntity.doSourcesMatch(rec.getAspects())) {
                this.tileEntity.setInventorySlotContentsSoftly(9, rec.getRecipeOutput());
            }
        }
    }

    public void onContainerClosed(final EntityPlayer par1EntityPlayer) {
        super.onContainerClosed(par1EntityPlayer);
        if (!this.tileEntity.getWorldObj().isRemote) {
            this.tileEntity.eventHandler = null;
        }
    }

    public boolean canInteractWith(final EntityPlayer par1EntityPlayer) {
        return this.tileEntity.getWorldObj().getTileEntity(
                   this.tileEntity.xCoord, this.tileEntity.yCoord, this.tileEntity.zCoord
               )
            == this.tileEntity
            && par1EntityPlayer.getDistanceSq(
                   this.tileEntity.xCoord + 0.5,
                   this.tileEntity.yCoord + 0.5,
                   this.tileEntity.zCoord + 0.5
               )
            <= 64.0;
    }

    public ItemStack
    transferStackInSlot(final EntityPlayer par1EntityPlayer, final int par1) {
        ItemStack var2 = null;
        final Slot var3 = (Slot) super.inventorySlots.get(par1);
        if (var3 != null && var3.getHasStack()) {
            final ItemStack var4 = var3.getStack();
            var2 = var4.copy();
            if (par1 == 0) {
                if (!this.mergeItemStack(var4, 11, 47, true)) {
                    return null;
                }
                var3.onSlotChange(var4, var2);
            } else if (par1 >= 11 && par1 < 38) {
                if (var4.getItem() instanceof ItemWandCasting) {
                    if (!this.mergeItemStack(var4, 1, 2, false)) {
                        return null;
                    }
                    var3.onSlotChange(var4, var2);
                } else if (!this.mergeItemStack(var4, 38, 47, false)) {
                    return null;
                }
            } else if (par1 >= 38 && par1 < 47) {
                if (!this.mergeItemStack(var4, 11, 38, false)) {
                    return null;
                }
            } else if (!this.mergeItemStack(var4, 11, 47, false)) {
                return null;
            }
            if (var4.stackSize == 0) {
                var3.putStack((ItemStack) null);
            } else {
                var3.onSlotChanged();
            }
            if (var4.stackSize == var2.stackSize) {
                return null;
            }
            var3.onPickupFromSlot(this.ip.player, var4);
        }
        return var2;
    }

    public ItemStack slotClick(
        final int par1,
        final int par2,
        final int par3,
        final EntityPlayer par4EntityPlayer
    ) {
        return super.slotClick(par1, par2, par3, par4EntityPlayer);
    }
}

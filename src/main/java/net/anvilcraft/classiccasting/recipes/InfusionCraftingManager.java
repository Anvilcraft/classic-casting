package net.anvilcraft.classiccasting.recipes;

import java.util.ArrayList;
import java.util.List;

import dev.tilera.auracore.api.crafting.IInfusionRecipe;
import net.anvilcraft.classiccasting.CCBlocks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

public class InfusionCraftingManager {
    public static InfusionCraftingManager INSTANCE = new InfusionCraftingManager();
    public List<IInfusionRecipe> recipes = new ArrayList<>();

    public InfusionCraftingManager() {
        this.recipes.add(new IInfusionRecipe() {
            @Override
            public boolean matches(IInventory var1, World var2, EntityPlayer var3) {
                return var1.getStackInSlot(0) != null
                    && var1.getStackInSlot(0).getItem()
                    == Items.gold_nugget;
            }

            @Override
            public ItemStack getCraftingResult(IInventory var1) {
                return this.getRecipeOutput();
            }

            @Override
            public int getRecipeSize() {
                return 9;
            }

            @Override
            public ItemStack getRecipeOutput() {
                return new ItemStack(CCBlocks.alembic);
            }

            @Override
            public int getCost() {
                return 10;
            }

            @Override
            public AspectList getAspects() {
                return new AspectList().add(Aspect.GREED, 8);
            }

            @Override
            public String getKey() {
                return "alembus";
            }

            @Override
            public String getResearch() {
                return null;
            }
        });
    }

    public IInfusionRecipe findMatchingInfusionRecipe(IInventory inv, EntityPlayer pl) {
        for (IInfusionRecipe recipe : this.recipes) {
            if (recipe.matches(inv, pl.worldObj, pl)) {
                return recipe;
            }
        }

        return null;
    }
}

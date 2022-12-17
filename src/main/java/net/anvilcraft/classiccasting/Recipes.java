package net.anvilcraft.classiccasting;

import dev.tilera.auracore.api.Aspects;
import net.minecraft.item.ItemStack;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.AspectList;

public class Recipes {
    
    public static void init() {
        Research.arcaneRecipes.put("ArcaneStone", ThaumcraftApi.addArcaneCraftingRecipe("MAGBLOCKS", new ItemStack(CCBlocks.infusionWorkbench, 8), new AspectList().add(Aspects.VIS, 20), "TST", "SSS", "TST", 'S', "stone", 'T', "ingotThaumium"));
    }

}

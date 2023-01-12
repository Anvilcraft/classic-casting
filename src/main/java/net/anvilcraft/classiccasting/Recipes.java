package net.anvilcraft.classiccasting;

import dev.tilera.auracore.api.Aspects;
import dev.tilera.auracore.api.AuracoreRecipes;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.config.ConfigBlocks;

public class Recipes {
    public static void init() {
        Research.arcaneRecipes.put(
            "ArcaneStone",
            ThaumcraftApi.addArcaneCraftingRecipe(
                "MAGBLOCKS",
                new ItemStack(CCBlocks.infusionWorkbench, 8),
                new AspectList().add(Aspects.VIS, 20),
                "TST",
                "SSS",
                "TST",
                'S',
                "stone",
                'T',
                "ingotThaumium"
            )
        );

        Research.infusionRecipes.put(
            "CrystalCore",
            AuracoreRecipes.addInfusionCraftingRecipe(
                "CRYSTALCORE",
                "CRYSTALCORE",
                50,
                new AspectList()
                    .add(Aspect.VOID, 8)
                    .add(Aspect.MAGIC, 8)
                    .add(Aspects.FLUX, 8)
                    .add(Aspect.ELDRITCH, 8),
                new ItemStack(CCBlocks.crystal, 1),
                new Object[] { " C ",
                               "CNC",
                               " C ",
                               Character.valueOf('C'),
                               new ItemStack(ConfigBlocks.blockCrystal, 1, 9),
                               Character.valueOf('N'),
                               Items.nether_star }
            )
        );
    }
}

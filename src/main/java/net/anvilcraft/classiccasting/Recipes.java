package net.anvilcraft.classiccasting;

import dev.tilera.auracore.api.Aspects;
import dev.tilera.auracore.api.AuracoreRecipes;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.config.ConfigItems;

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

        Research.arcaneRecipes.put(
            "AuraCompass",
            ThaumcraftApi.addArcaneCraftingRecipe(
                "AURACOMPASS",
                new ItemStack(CCItems.auraCompass),
                new AspectList().add(Aspects.VIS, 20),
                " G ",
                "GCG",
                " G ",
                'G',
                Items.gold_ingot,
                'C',
                new ItemStack(ConfigItems.itemShard, 1, 2)
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

        Research.infusionRecipes.put(
            "CrystalCapacitor",
            AuracoreRecipes.addInfusionCraftingRecipe(
                "CRYSTALCAPACITOR",
                "CRYSTALCAPACITOR",
                100,
                new AspectList()
                    .add(Aspect.EXCHANGE, 16)
                    .add(Aspect.MAGIC, 16)
                    .add(Aspect.CRYSTAL, 16),
                new ItemStack(CCBlocks.crystal, 1, 1),
                "CCC",
                "CWC",
                "CCC",
                'C',
                new ItemStack(ConfigItems.itemShard, 1, 8),
                'W',
                // TODO: this is arcane wood, but that doesn't really exist yet.
                // Just pretend the planks are magical
                new ItemStack(Blocks.planks, 1, 0)
            )
        );

        Research.infusionRecipes.put(
            "AdeptWand",
            AuracoreRecipes.addInfusionCraftingRecipe(
                "ADEPTWAND",
                "UNIFIEDTHAUMICFIELDTHEORY",
                50,
                new AspectList().add(Aspect.MAGIC, 16),
                new ItemStack(CCItems.wandCastingAdept),
                " A ",
                "WSF",
                " E ",
                'S',
                new ItemStack(CCItems.wandCastingApprentice, 1, 32767),
                'A',
                new ItemStack(ConfigItems.itemShard, 1, 0),
                'F',
                new ItemStack(ConfigItems.itemShard, 1, 1),
                'W',
                new ItemStack(ConfigItems.itemShard, 1, 2),
                'E',
                new ItemStack(ConfigItems.itemShard, 1, 3)
            )
        );

        Research.infusionRecipes.put(
            "WandFire",
            AuracoreRecipes.addInfusionCraftingRecipe(
                "WANDFIRE",
                "WANDFIRE",
                50,
                new AspectList().add(Aspect.FIRE, 16).add(Aspect.WEAPON, 4),
                new ItemStack(CCItems.wandFire),
                "SS",
                "WS",
                'W',
                new ItemStack(CCItems.wandCastingApprentice, 1, 32767),
                'S',
                new ItemStack(ConfigItems.itemShard, 1, 1)
            )
        );

        Research.infusionRecipes.put(
            "WandFrost",
            AuracoreRecipes.addInfusionCraftingRecipe(
                "WANDFROST",
                "WANDFROST",
                50,
                new AspectList().add(Aspect.COLD, 16).add(Aspect.WEAPON, 4),
                new ItemStack(CCItems.wandFrost),
                "SS",
                "WS",
                'W',
                new ItemStack(CCItems.wandCastingApprentice, 1, 32767),
                'S',
                new ItemStack(ConfigItems.itemShard, 1, 2)
            )
        );

        Research.infusionRecipes.put(
            "WandTrade",
            AuracoreRecipes.addInfusionCraftingRecipe(
                "WANDETRADE",
                "WANDETRADE",
                50,
                new AspectList().add(Aspect.EXCHANGE, 16).add(Aspect.TOOL, 4),
                new ItemStack(CCItems.wandTrade),
                "SS",
                "WS",
                'W',
                new ItemStack(CCItems.wandCastingApprentice, 1, 32767),
                'S',
                new ItemStack(ConfigItems.itemShard, 1, 4)
            )
        );

        Research.infusionRecipes.put(
            "WandExcavate",
            AuracoreRecipes.addInfusionCraftingRecipe(
                "WANDEXCAVATE",
                "WANDEXCAVATE",
                50,
                new AspectList().add(Aspect.METAL, 16).add(Aspect.TOOL, 4),
                new ItemStack(CCItems.wandExcavation),
                "SS",
                "WS",
                'W',
                new ItemStack(CCItems.wandCastingApprentice, 1, 32767),
                'S',
                new ItemStack(ConfigItems.itemShard, 1, 3)
            )
        );

        Research.infusionRecipes.put(
            "WandLightning",
            AuracoreRecipes.addInfusionCraftingRecipe(
                "WANDLIGHTNING",
                "WANDLIGHTNING",
                50,
                // TODO: this used to be Aspect.POWER. is ENERGY correct?
                new AspectList().add(Aspect.ENERGY, 16).add(Aspect.WEAPON, 4),
                new ItemStack(CCItems.wandLightning),
                "SS",
                "WS",
                'W',
                new ItemStack(CCItems.wandCastingApprentice, 1, 32767),
                'S',
                new ItemStack(ConfigItems.itemShard, 1, 0)
            )
        );
    }
}

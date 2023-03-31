package net.anvilcraft.classiccasting;

import dev.tilera.auracore.api.Aspects;
import dev.tilera.auracore.api.AuracoreRecipes;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.ShapedOreRecipe;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.config.ConfigItems;

public class Recipes {
    @SuppressWarnings("unchecked")
    public static void init() {
        CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(
            CCItems.wandCastingApprentice,
            "  C",
            " S ",
            "G  ",
            'C',
            new ItemStack(ConfigItems.itemShard, 1, 32767),
            'S',
            "stickWood",
            'G',
            "nuggetGold"
        ));

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
            "ArcaneWood1",
            ThaumcraftApi.addArcaneCraftingRecipe(
                "MAGBLOCKS", 
                new ItemStack(CCBlocks.blockArcaneWood, 2), 
                new AspectList().add(Aspects.VIS, 20), 
                "WWW",
                "WWW",
                "WWW",
                'W',
                "logWood"
            )
        );

        Research.arcaneRecipes.put(
            "ArcaneWood2",
            ThaumcraftApi.addArcaneCraftingRecipe(
                "MAGBLOCKS", 
                new ItemStack(CCBlocks.blockArcaneWood, 4), 
                new AspectList().add(Aspects.VIS, 20), 
                "WW",
                "WW",
                'W',
                new ItemStack(ConfigBlocks.blockMagicalLog, 1, 0)
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
                CCBlocks.blockArcaneWood
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
                "WANDTRADE",
                "WANDTRADE",
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

        Research.infusionRecipes.put(
            "FluxFilter",
            AuracoreRecipes.addInfusionCraftingRecipe(
                "FLUXFILTER",
                "BASICFLUX",
                25,
                new AspectList().add(Aspects.PURE, 8).add(Aspect.EXCHANGE, 8),
                new ItemStack(ConfigItems.itemResource, 1, 8),
                "GFG",
                'F',
                new ItemStack(ConfigBlocks.blockMagicalLog, 1, 1),
                'G',
                Items.gold_ingot
            )
        );

        Research.infusionRecipes.put(
            "Alembic",
            AuracoreRecipes.addInfusionCraftingRecipe(
                "BASICFLUX",
                "ALEMBIC",
                75,
                new AspectList()
                    .add(Aspect.AIR, 8)
                    .add(Aspect.WATER, 8)
                    .add(Aspect.CRYSTAL, 8),
                new ItemStack(CCBlocks.alembic, 1, 1),
                "GFG",
                "J G",
                "B  ",
                'F',
                new ItemStack(ConfigItems.itemResource, 1, 8),
                'J',
                new ItemStack(ConfigBlocks.blockJar, 1, 0),
                'B',
                Items.brewing_stand,
                'G',
                Items.gold_ingot
            )
        );

        Research.infusionRecipes.put(
            "MageWand",
            AuracoreRecipes.addInfusionCraftingRecipe(
                "WANDMAGE",
                "THETHEORYOFEVERYTHING",
                250,
                new AspectList().add(Aspect.MAGIC, 32),
                new ItemStack(CCItems.wandCastingMage),
                " N",
                "S ",
                'S',
                new ItemStack(CCItems.wandCastingAdept, 1, 32767),
                'N',
                Items.nether_star
            )
        );

        Research.infusionRecipes.put(
            "WandHellrod",
            AuracoreRecipes.addInfusionCraftingRecipe(
                "HELLROD",
                "HELLROD",
                250,
                new AspectList()
                    .add(Aspects.EVIL, 32)
                    .add(Aspect.FIRE, 32)
                    .add(Aspect.BEAST, 32),
                new ItemStack(CCItems.wandHellrod),
                " GN",
                " SG",
                "W  ",
                'S',
                new ItemStack(CCItems.wandFire, 1, 32767),
                'W',
                new ItemStack(CCItems.wandCastingAdept, 1, 32767),
                'N',
                Blocks.tnt,
                'G',
                Items.gold_ingot
            )
        );

        Research.infusionRecipes.put(
            "PortableHole",
            AuracoreRecipes.addInfusionCraftingRecipe(
                "PORTABLEHOLE",
                "PORTABLEHOLE",
                200,
                new AspectList()
                    .add(Aspect.VOID, 24)
                    .add(Aspect.ELDRITCH, 24)
                    .add(Aspect.EXCHANGE, 16),
                new ItemStack(CCItems.portableHole),
                " C ",
                "CEC",
                " C ",
                'C',
                new ItemStack(ConfigItems.itemResource, 1, 7),
                'E',
                Items.ender_pearl
            )
        );
        addClusters();
    }

    public static void addClusters() {
        for (int i = 0; i <= 10; i++) {
            if (i == 6 || i == 7 || i == 9) continue;
            int k = i > 6 ? i - 1 : i;
            Research.clusters[k] = AuracoreRecipes.addShapelessInfusionCraftingRecipe(
                "CRYSTALCLUSTER" + i, 
                "CRYSTALCLUSTER", 
                100, 
                new AspectList()
                    .add(Aspect.CRYSTAL, 8)
                    .add(Aspect.MAGIC, 8)
                    .add(Aspect.EXCHANGE, 8), 
                new ItemStack(ConfigBlocks.blockCrystal, 1, i), 
                new ItemStack(ConfigItems.itemShard, 1, k),
                new ItemStack(ConfigItems.itemShard, 1, k),
                new ItemStack(ConfigItems.itemShard, 1, k),
                new ItemStack(ConfigItems.itemShard, 1, k),
                new ItemStack(ConfigItems.itemShard, 1, k),
                new ItemStack(ConfigItems.itemShard, 1, k)
            );
        }
        Research.clusters[6] = AuracoreRecipes.addShapelessInfusionCraftingRecipe(
            "CRYSTALCLUSTER6", 
            "CRYSTALCLUSTER", 
            100, 
            new AspectList()
                .add(Aspect.CRYSTAL, 8)
                .add(Aspect.MAGIC, 8)
                .add(Aspect.EXCHANGE, 8), 
            new ItemStack(ConfigBlocks.blockCrystal, 1, 6), 
            new ItemStack(ConfigItems.itemShard, 1, 0),
            new ItemStack(ConfigItems.itemShard, 1, 1),
            new ItemStack(ConfigItems.itemShard, 1, 2),
            new ItemStack(ConfigItems.itemShard, 1, 3),
            new ItemStack(ConfigItems.itemShard, 1, 4),
            new ItemStack(ConfigItems.itemShard, 1, 5)
        );
        Research.clusters[8] = AuracoreRecipes.addShapelessInfusionCraftingRecipe(
            "CRYSTALCLUSTER9", 
            "CRYSTALCLUSTER", 
            100, 
            new AspectList()
                .add(Aspect.CRYSTAL, 8)
                .add(Aspect.MAGIC, 8)
                .add(Aspect.EXCHANGE, 8), 
            new ItemStack(ConfigBlocks.blockCrystal, 1, 9), 
            new ItemStack(ConfigItems.itemShard, 1, 0),
            new ItemStack(ConfigItems.itemShard, 1, 1),
            new ItemStack(ConfigItems.itemShard, 1, 2),
            new ItemStack(ConfigItems.itemShard, 1, 3),
            new ItemStack(ConfigItems.itemShard, 1, 7)
        );
    }

    @SuppressWarnings("unchecked")
    public static void removeClusters() {
        CraftingManager.getInstance().getRecipeList().removeIf((r) -> isCrystalCluster(r));
    }

    public static boolean isCrystalCluster(Object recipe) {
        if (!(recipe instanceof IRecipe)) return false;
        IRecipe r = (IRecipe) recipe;
        ItemStack output = r.getRecipeOutput();
        if (output == null) return false;
        Item item = output.getItem();
        return Block.getBlockFromItem(item) == ConfigBlocks.blockCrystal;
    }
}

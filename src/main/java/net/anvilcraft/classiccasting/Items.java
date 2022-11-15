package net.anvilcraft.classiccasting;

import cpw.mods.fml.common.registry.GameRegistry;
import net.anvilcraft.classiccasting.items.ItemPortableHole;
import net.anvilcraft.classiccasting.items.wands.ItemHellrod;
import net.anvilcraft.classiccasting.items.wands.ItemWandCastingAdept;
import net.anvilcraft.classiccasting.items.wands.ItemWandCastingApprentice;
import net.anvilcraft.classiccasting.items.wands.ItemWandCastingMage;
import net.anvilcraft.classiccasting.items.wands.ItemWandExcavation;
import net.anvilcraft.classiccasting.items.wands.ItemWandFire;
import net.anvilcraft.classiccasting.items.wands.ItemWandFrost;
import net.anvilcraft.classiccasting.items.wands.ItemWandLightning;
import net.anvilcraft.classiccasting.items.wands.ItemWandTrade;
import net.minecraft.item.Item;

public class Items {
    public static Item portableHole;

    public static Item wandCastingApprentice;
    public static Item wandCastingAdept;
    public static Item wandCastingMage;

    public static Item wandExcavation;
    public static Item wandFire;
    public static Item wandFrost;
    public static Item wandHellrod;
    public static Item wandLightning;
    public static Item wandTrade;

    public static void init() {
        portableHole = new ItemPortableHole();

        wandCastingApprentice = new ItemWandCastingApprentice();
        wandCastingAdept = new ItemWandCastingAdept();
        wandCastingMage = new ItemWandCastingMage();

        wandExcavation = new ItemWandExcavation();
        wandFire = new ItemWandFire();
        wandFrost = new ItemWandFrost();
        wandHellrod = new ItemHellrod();
        wandLightning = new ItemWandLightning();
        wandTrade = new ItemWandTrade();

        GameRegistry.registerItem(portableHole, "portableHole");

        GameRegistry.registerItem(wandCastingApprentice, "wandCastingApprentice");
        GameRegistry.registerItem(wandCastingAdept, "wandCastingAdept");
        GameRegistry.registerItem(wandCastingMage, "wandCastingMage");

        GameRegistry.registerItem(wandExcavation, "wandExcavation");
        GameRegistry.registerItem(wandFire, "wandFire");
        GameRegistry.registerItem(wandFrost, "wandFrost");
        GameRegistry.registerItem(wandHellrod, "wandHellrod");
        GameRegistry.registerItem(wandLightning, "wandLightning");
        GameRegistry.registerItem(wandTrade, "wandTrade");
    }
}

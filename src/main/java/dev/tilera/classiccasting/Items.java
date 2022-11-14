package dev.tilera.classiccasting;

import cpw.mods.fml.common.registry.GameRegistry;
import dev.tilera.classiccasting.items.ItemPortableHole;
import dev.tilera.classiccasting.items.wands.ItemHellrod;
import dev.tilera.classiccasting.items.wands.ItemWandCastingAdept;
import dev.tilera.classiccasting.items.wands.ItemWandCastingApprentice;
import dev.tilera.classiccasting.items.wands.ItemWandCastingMage;
import dev.tilera.classiccasting.items.wands.ItemWandExcavation;
import dev.tilera.classiccasting.items.wands.ItemWandFire;
import dev.tilera.classiccasting.items.wands.ItemWandFrost;
import dev.tilera.classiccasting.items.wands.ItemWandLightning;
import dev.tilera.classiccasting.items.wands.ItemWandTrade;
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

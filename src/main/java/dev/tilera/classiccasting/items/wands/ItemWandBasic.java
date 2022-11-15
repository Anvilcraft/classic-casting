package dev.tilera.classiccasting.items.wands;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import dev.tilera.classiccasting.ClassicCastingTab;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemWandBasic extends Item {
    public ItemWandBasic() {
        super();
        super.maxStackSize = 1;
        super.canRepair = false;
        this.setCreativeTab(ClassicCastingTab.INSTANCE);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean isFull3D() {
        return true;
    }

    @Override
    public int getItemEnchantability() {
        return 5;
    }

    @Override
    public boolean shouldRotateAroundWhenRendering() {
        return true;
    }

    @Override
    public boolean isDamageable() {
        return true;
    }

    @Override
    public boolean isRepairable() {
        return false;
    }

    @Override
    public EnumRarity getRarity(final ItemStack itemstack) {
        return EnumRarity.rare;
    }

    public ItemStack
    damageWand(final ItemStack itemstack, final EntityPlayer p, final int amount) {
        //final int var3 = EnchantmentHelper.getEnchantmentLevel(
        //    Config.enchFrugal.effectId, itemstack
        //);
        //for (int a = 0; a < amount; ++a) {
        //    if (EnchantmentFrugal.doDamage(itemstack, var3, Item.itemRand)) {
        //        itemstack.damageItem(1, (EntityLiving) p);
        //    }
        //}
        itemstack.damageItem(1, p);
        return itemstack;
    }

    public int getPotency(final ItemStack itemstack) {
        //return EnchantmentHelper.getEnchantmentLevel(
        //    Config.enchPotency.effectId, itemstack
        //);
        return 1;
    }

    public int getTreasure(final ItemStack itemstack) {
        //return EnchantmentHelper.getEnchantmentLevel(
        //    Config.enchWandFortune.effectId, itemstack
        //);
        return 1;
    }

    public boolean canCharge(final ItemStack itemstack) {
        //return EnchantmentHelper.getEnchantmentLevel(
        //           Config.enchCharging.effectId, itemstack
        //       )
        //    > 0;
        return true;
    }
}

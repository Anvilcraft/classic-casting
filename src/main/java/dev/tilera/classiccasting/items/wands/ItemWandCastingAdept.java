package dev.tilera.classiccasting.items.wands;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ItemWandCastingAdept extends ItemWandCasting {
    public IIcon icon;

    public ItemWandCastingAdept() {
        super();
        super.maxStackSize = 1;
        super.canRepair = false;
        this.setUnlocalizedName("classiccasting:wandCastingAdept");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(final IIconRegister ir) {
        this.icon = ir.registerIcon("classiccasting:wandadept");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(final int par1) {
        return this.icon;
    }

    @Override
    public EnumRarity getRarity(final ItemStack itemstack) {
        return EnumRarity.rare;
    }

    @Override
    public int getMaxVis(ItemStack i) {
        return 250;
    }

    @Override
    public int getRechargeInterval() {
        return 7;
    }
}

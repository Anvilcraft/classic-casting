package dev.tilera.classiccasting.items.wands;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import dev.tilera.auracore.aura.AuraManager;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import thaumcraft.common.entities.projectile.EntityFrostShard;

public class ItemWandFrost extends ItemWandBasic {
    public IIcon icon;

    public ItemWandFrost() {
        super();
        this.setMaxDamage(2000);
        this.setUnlocalizedName("classiccasting:wandFrost");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(final IIconRegister ir) {
        this.icon = ir.registerIcon("classiccasting:wandfrost");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(final int par1) {
        return this.icon;
    }

    @Override
    public void onUpdate(
        final ItemStack is,
        final World w,
        final Entity e,
        final int par4,
        final boolean par5
    ) {
        if (!this.canCharge(is)) {
            return;
        }
        if (!w.isRemote && e.ticksExisted % 50 == 0 && is.getItemDamage() > 0
            && AuraManager.decreaseClosestAura(w, e.posX, e.posY, e.posZ, 1)) {
            is.damageItem(-5, (EntityLiving) e);
            if (is.getItemDamage() < 0) {
                is.setItemDamage(0);
            }
        }
    }

    @Override
    public ItemStack
    onItemRightClick(final ItemStack itemstack, final World world, final EntityPlayer p) {
        final EntityFrostShard shard
            = new EntityFrostShard(world, p, 1.0f + this.getPotency(itemstack) / 2.0f);
        if (!world.isRemote && world.spawnEntityInWorld((Entity) shard)) {
            this.damageWand(itemstack, p, 1);
        }
        world.playSoundAtEntity(
            (Entity) p, "thaumcraft:ice", 0.4f, 1.0f + world.rand.nextFloat() * 0.1f
        );
        p.swingItem();
        return itemstack;
    }
}

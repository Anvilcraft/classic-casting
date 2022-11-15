package net.anvilcraft.classiccasting.items.wands;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import dev.tilera.auracore.aura.AuraManager;
import net.anvilcraft.classiccasting.Utils;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagShort;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import thaumcraft.common.entities.monster.EntityFireBat;

public class ItemHellrod extends ItemWandBasic {
    public IIcon icon;

    public ItemHellrod() {
        super();
        this.setMaxDamage(1);
        this.setUnlocalizedName("classiccasting:hellrod");
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(final IIconRegister ir) {
        this.icon = ir.registerIcon("classiccasting:hellrod");
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(final int par1) {
        return this.icon;
    }

    public void onUpdate(
        final ItemStack is,
        final World w,
        final Entity e,
        final int par4,
        final boolean par5
    ) {
        final int tcount = this.canCharge(is) ? 25 : 50;
        if (is.hasTagCompound() && is.stackTagCompound.hasKey("charges")) {
            if (!w.isRemote && e.ticksExisted % tcount == 0) {
                final short charges = is.stackTagCompound.getShort("charges");
                if (charges < 9
                    && AuraManager.decreaseClosestAura(w, e.posX, e.posY, e.posZ, 6)) {
                    is.setTagInfo("charges", new NBTTagShort((short) (charges + 1)));
                }
            }
        } else {
            is.setTagInfo("charges", new NBTTagShort((short) 0));
        }
    }

    public ItemStack
    onItemRightClick(final ItemStack itemstack, final World world, final EntityPlayer p) {
        if (itemstack.hasTagCompound() && itemstack.stackTagCompound.hasKey("charges")) {
            final short charges = itemstack.stackTagCompound.getShort("charges");
            if (charges <= 0) {
                return itemstack;
            }
            final Entity pointedEntity = Utils.getPointedEntity(
                ((Entity) p).worldObj, p, 32.0, EntityFireBat.class
            );
            double px = ((Entity) p).posX;
            double py = ((Entity) p).posY;
            double pz = ((Entity) p).posZ;
            py = ((Entity) p).boundingBox.minY + ((Entity) p).height / 2.0f + 0.25;
            px -= MathHelper.cos(((Entity) p).rotationYaw / 180.0f * 3.141593f) * 0.16f;
            py -= 0.05000000014901161;
            pz -= MathHelper.sin(((Entity) p).rotationYaw / 180.0f * 3.141593f) * 0.16f;
            final Vec3 vec3d = p.getLook(1.0f);
            px += vec3d.xCoord * 0.5;
            py += vec3d.yCoord * 0.5;
            pz += vec3d.zCoord * 0.5;
            if (pointedEntity != null && pointedEntity instanceof EntityLiving) {
                if (!world.isRemote) {
                    if (pointedEntity instanceof EntityPlayer
                        && !MinecraftServer.getServer().isPVPEnabled()) {
                        return itemstack;
                    }
                    final EntityFireBat firebat = new EntityFireBat(world);
                    firebat.setLocationAndAngles(
                        px,
                        py + ((Entity) firebat).height,
                        pz,
                        ((Entity) p).rotationYaw,
                        0.0f
                    );
                    firebat.setTarget(pointedEntity);
                    firebat.setIsSummoned(true);
                    firebat.setIsBatHanging(false);
                    firebat.damBonus = this.getPotency(itemstack);
                    // TODO: WTF
                    //firebat.initCreature();
                    if (world.spawnEntityInWorld((Entity) firebat)) {
                        world.playAuxSFX(2004, (int) px, (int) py, (int) pz, 0);
                        itemstack.setTagInfo(
                            "charges", new NBTTagShort((short) (charges - 1))
                        );
                    }
                } else {
                    world.spawnParticle("explode", px, py, pz, 0.0, 0.0, 0.0);
                }
                world.playSoundAtEntity(
                    (Entity) p,
                    "thaumcraft:wandfail",
                    0.4f,
                    0.9f + world.rand.nextFloat() * 0.2f
                );
                p.swingItem();
            }
        }
        return itemstack;
    }
}

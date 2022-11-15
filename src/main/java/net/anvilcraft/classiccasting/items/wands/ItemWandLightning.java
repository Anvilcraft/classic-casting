package net.anvilcraft.classiccasting.items.wands;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import dev.tilera.auracore.aura.AuraManager;
import net.anvilcraft.classiccasting.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import thaumcraft.client.fx.bolt.FXLightningBolt;
import thaumcraft.common.Thaumcraft;

public class ItemWandLightning extends ItemWandBasic {
    public IIcon icon;
    long soundDelay;
    private int chargecount;

    public ItemWandLightning() {
        super();
        this.soundDelay = 0L;
        this.chargecount = 0;
        this.setMaxStackSize(1);
        this.setMaxDamage(2000);
        this.setUnlocalizedName("classiccasting:wandLightning");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(final IIconRegister ir) {
        this.icon = ir.registerIcon("classiccasting:wandlightning");
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

    public static void shootLightning(
        final World world,
        final EntityLivingBase entityplayer,
        final double xx,
        final double yy,
        final double zz,
        final boolean offset
    ) {
        double px = ((Entity) entityplayer).posX;
        double py = ((Entity) entityplayer).posY;
        double pz = ((Entity) entityplayer).posZ;
        if (entityplayer.getEntityId()
            != Minecraft.getMinecraft().thePlayer.getEntityId()) {
            py = ((Entity) entityplayer).boundingBox.minY
                + ((Entity) entityplayer).height / 2.0f + 0.25;
        }
        px -= MathHelper.cos(((Entity) entityplayer).rotationYaw / 180.0f * 3.141593f)
            * 0.16f;
        py -= 0.05000000014901161;
        pz -= MathHelper.sin(((Entity) entityplayer).rotationYaw / 180.0f * 3.141593f)
            * 0.16f;
        final Vec3 vec3d = entityplayer.getLook(1.0f);
        px += vec3d.xCoord * 0.25;
        py += vec3d.yCoord * 0.25;
        pz += vec3d.zCoord * 0.25;
        final FXLightningBolt bolt = new FXLightningBolt(
            world, px, py, pz, xx, yy, zz, world.rand.nextLong(), 6, 0.5f, 5
        );
        bolt.defaultFractal();
        bolt.setType(2);
        bolt.setWidth(0.125f);
        bolt.finalizeBolt();
    }

    @Override
    public int getMaxItemUseDuration(final ItemStack itemstack) {
        return 50;
    }

    @Override
    public ItemStack
    onItemRightClick(final ItemStack itemstack, final World world, final EntityPlayer p) {
        if (!((Entity) p).worldObj.isRemote && this.chargecount > 0 && !p.isUsingItem()) {
            if (this.chargecount > itemstack.getMaxDamage() - itemstack.getItemDamage()) {
                this.chargecount
                    = itemstack.getMaxDamage() - itemstack.getItemDamage() + 1;
            }
            this.damageWand(itemstack, p, this.chargecount);
            p.inventoryContainer.detectAndSendChanges();
            this.chargecount = 0;
        }
        p.setItemInUse(itemstack, this.getMaxItemUseDuration(itemstack));
        return itemstack;
    }

    @Override
    public void
    onUsingTick(final ItemStack stack, final EntityPlayer p, final int count) {
        final Entity pointedEntity
            = Utils.getPointedEntity(((Entity) p).worldObj, p, 20.0, 1.1f);
        boolean zapped = false;
        if (this.soundDelay < System.currentTimeMillis()) {
            if (!((Entity) p).worldObj.isRemote) {
                ((Entity) p)
                    .worldObj.playSoundEffect(
                        ((Entity) p).posX,
                        ((Entity) p).posY,
                        ((Entity) p).posZ,
                        "thaumcraft:shock",
                        0.25f,
                        1.0f
                    );
            }
            this.soundDelay = System.currentTimeMillis() + 100L;
            zapped = true;
        }
        if (((Entity) p).worldObj.isRemote) {
            if (zapped) {
                final MovingObjectPosition mop
                    = Utils.getTargetBlock(((Entity) p).worldObj, p, false);
                final Vec3 v = p.getLook(2.0f);
                double px = ((Entity) p).posX + v.xCoord * 10.0;
                double py = ((Entity) p).posY + v.yCoord * 10.0;
                double pz = ((Entity) p).posZ + v.zCoord * 10.0;
                if (mop != null) {
                    px = mop.hitVec.xCoord;
                    py = mop.hitVec.yCoord;
                    pz = mop.hitVec.zCoord;
                    for (int a = 0; a < 5; ++a) {
                        Thaumcraft.proxy.sparkle(
                            (float) px
                                + (((Entity) p).worldObj.rand.nextFloat()
                                   - ((Entity) p).worldObj.rand.nextFloat())
                                    * 0.3f,
                            (float) py
                                + (((Entity) p).worldObj.rand.nextFloat()
                                   - ((Entity) p).worldObj.rand.nextFloat())
                                    * 0.3f,
                            (float) pz
                                + (((Entity) p).worldObj.rand.nextFloat()
                                   - ((Entity) p).worldObj.rand.nextFloat())
                                    * 0.3f,
                            2.0f + ((Entity) p).worldObj.rand.nextFloat(),
                            2,
                            0.05f + ((Entity) p).worldObj.rand.nextFloat() * 0.05f
                        );
                    }
                }
                if (pointedEntity != null) {
                    px = pointedEntity.posX;
                    py = pointedEntity.boundingBox.minY + pointedEntity.height / 2.0f;
                    pz = pointedEntity.posZ;
                    for (int a = 0; a < 5; ++a) {
                        Thaumcraft.proxy.sparkle(
                            (float) px
                                + (((Entity) p).worldObj.rand.nextFloat()
                                   - ((Entity) p).worldObj.rand.nextFloat())
                                    * 0.6f,
                            (float) py
                                + (((Entity) p).worldObj.rand.nextFloat()
                                   - ((Entity) p).worldObj.rand.nextFloat())
                                    * 0.6f,
                            (float) pz
                                + (((Entity) p).worldObj.rand.nextFloat()
                                   - ((Entity) p).worldObj.rand.nextFloat())
                                    * 0.6f,
                            2.0f + ((Entity) p).worldObj.rand.nextFloat(),
                            2,
                            0.05f + ((Entity) p).worldObj.rand.nextFloat() * 0.05f
                        );
                    }
                }
                shootLightning(((Entity) p).worldObj, p, px, py, pz, true);
            }
        } else {
            if (pointedEntity != null) {
                pointedEntity.attackEntityFrom(
                    DamageSource.causePlayerDamage(p), 3 + this.getPotency(stack)
                );
                if (((Entity) p)
                        .worldObj.rand.nextInt(
                            16 - Math.min(15, this.getPotency(stack) * 2)
                        )
                    == 0) {
                    pointedEntity.onStruckByLightning((EntityLightningBolt) null);
                }
            }
            ++this.chargecount;
        }
        final int charges = this.getMaxItemUseDuration(stack) - count;
        if (charges > stack.getMaxDamage() - stack.getItemDamage()) {
            p.stopUsingItem();
        }
    }

    @Override
    public void onPlayerStoppedUsing(
        final ItemStack stack,
        final World world,
        final EntityPlayer player,
        final int count
    ) {
        this.chargecount = 0;
        int charges = this.getMaxItemUseDuration(stack) - count;
        if (charges > stack.getMaxDamage() - stack.getItemDamage()) {
            charges = stack.getMaxDamage() - stack.getItemDamage() + 1;
        }
        this.damageWand(stack, player, charges);
        player.inventoryContainer.detectAndSendChanges();
    }

    @Override
    public EnumAction getItemUseAction(final ItemStack itemstack) {
        return EnumAction.bow;
    }

    @Override
    public boolean hitEntity(
        final ItemStack par1ItemStack,
        final EntityLivingBase target,
        final EntityLivingBase player
    ) {
        target.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer) player), 4);
        this.damageWand(par1ItemStack, (EntityPlayer) player, 1);
        if (!((Entity) target).worldObj.isRemote) {
            ((Entity) target)
                .worldObj.playSoundEffect(
                    ((Entity) target).posX,
                    ((Entity) target).posY,
                    ((Entity) target).posZ,
                    "thaumcraft:shock",
                    0.25f,
                    1.0f
                );
        }
        return true;
    }
}

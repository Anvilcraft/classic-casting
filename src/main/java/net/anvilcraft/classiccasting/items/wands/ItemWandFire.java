package net.anvilcraft.classiccasting.items.wands;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import dev.tilera.auracore.aura.AuraManager;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import thaumcraft.client.lib.UtilsFX;

public class ItemWandFire extends ItemWandBasic {
    public IIcon icon;
    long soundDelay;
    private int chargecount;

    public ItemWandFire() {
        super();
        this.soundDelay = 0L;
        this.chargecount = 0;
        this.setMaxDamage(2000);
        this.setUnlocalizedName("classiccasting:wandFire");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(final IIconRegister ir) {
        this.icon = ir.registerIcon("classiccasting:wandfire");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(final int par1) {
        return this.icon;
    }

    @Override
    public int getMaxItemUseDuration(final ItemStack itemstack) {
        return 50;
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
        final int range = 17;
        final Vec3 vec3d = p.getLook((float) range);
        if (!((Entity) p).worldObj.isRemote
            && this.soundDelay < System.currentTimeMillis()) {
            ((Entity) p)
                .worldObj.playSoundAtEntity(
                    (Entity) p, "thaumcraft:fireloop", 0.25f, 1.0f
                );
            this.soundDelay = System.currentTimeMillis() + 500L;
        }
        if (((Entity) p).worldObj.isRemote) {
            UtilsFX.shootFire(((Entity) p).worldObj, p, true, range, false);
        } else {
            ++this.chargecount;
            this.getTargets(stack, ((Entity) p).worldObj, vec3d, p, range);
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
        this.damageWand(stack, player, this.chargecount);
        player.inventoryContainer.detectAndSendChanges();
    }

    @Override
    public boolean hitEntity(
        final ItemStack par1ItemStack,
        final EntityLivingBase target,
        final EntityLivingBase player
    ) {
        if (!((Entity) player).worldObj.isRemote) {
            if (target instanceof EntityPlayer
                && !MinecraftServer.getServer().isPVPEnabled()) {
                return true;
            }
            target.setFire(5);
            this.damageWand(par1ItemStack, (EntityPlayer) player, 1);
            ((Entity) target)
                .worldObj.playSoundEffect(
                    ((Entity) target).posX,
                    ((Entity) target).posY,
                    ((Entity) target).posZ,
                    "random.fizz",
                    1.0f,
                    Item.itemRand.nextFloat() * 0.4f + 0.8f
                );
        }
        return true;
    }

    private void getTargets(
        final ItemStack itemstack,
        final World world,
        final Vec3 tvec,
        final EntityPlayer p,
        final double range
    ) {
        Entity pointedEntity = null;
        final Vec3 vec3d = Vec3.createVectorHelper(
            ((Entity) p).posX, ((Entity) p).posY, ((Entity) p).posZ
        );
        final Vec3 vec3d2 = vec3d.addVector(
            tvec.xCoord * range, tvec.yCoord * range, tvec.zCoord * range
        );
        final float f1 = 1.0f;
        final List<Entity> list = world.getEntitiesWithinAABBExcludingEntity(
            (Entity) p,
            ((Entity) p)
                .boundingBox
                .addCoord(tvec.xCoord * range, tvec.yCoord * range, tvec.zCoord * range)
                .expand((double) f1, (double) f1, (double) f1)
        );
        for (int i = 0; i < list.size(); ++i) {
            final Entity entity = list.get(i);
            if (entity.canBeCollidedWith()) {
                final float f2 = Math.max(1.0f, entity.getCollisionBorderSize());
                final AxisAlignedBB axisalignedbb = entity.boundingBox.expand(
                    (double) f2, (double) (f2 * 1.25f), (double) f2
                );
                final MovingObjectPosition movingobjectposition
                    = axisalignedbb.calculateIntercept(vec3d, vec3d2);
                if (movingobjectposition != null) {
                    pointedEntity = entity;
                    if (pointedEntity != null && p.canEntityBeSeen(pointedEntity)
                        && !pointedEntity.isImmuneToFire()
                        && (!(pointedEntity instanceof EntityPlayer)
                            || MinecraftServer.getServer().isPVPEnabled())) {
                        pointedEntity.setFire(4 + this.getPotency(itemstack));
                        pointedEntity.attackEntityFrom(
                            (DamageSource) new EntityDamageSource("inFire", (Entity) p),
                            2 + this.getPotency(itemstack)
                        );
                    }
                }
            }
        }
    }

    @Override
    public EnumAction getItemUseAction(final ItemStack itemstack) {
        return EnumAction.bow;
    }
}

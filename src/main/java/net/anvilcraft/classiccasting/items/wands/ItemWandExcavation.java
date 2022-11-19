package net.anvilcraft.classiccasting.items.wands;

import java.util.HashMap;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import dev.tilera.auracore.aura.AuraManager;
import net.anvilcraft.classiccasting.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import thaumcraft.common.Thaumcraft;

public class ItemWandExcavation extends ItemWandBasic {
    public IIcon icon;
    static HashMap<String, Long> soundDelay;
    static HashMap<String, Object> beam;
    static HashMap<String, Float> breakcount;
    static HashMap<String, Integer> lastX;
    static HashMap<String, Integer> lastY;
    static HashMap<String, Integer> lastZ;
    static HashMap<String, Integer> mined;

    public ItemWandExcavation() {
        super();
        this.setMaxDamage(2000);
        this.setUnlocalizedName("classiccasting:wandExcavation");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(final IIconRegister ir) {
        this.icon = ir.registerIcon("classiccasting:wandexcavation");
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
        String pp = "R" + p.getDisplayName();
        if (!((Entity) p).worldObj.isRemote) {
            pp = "S" + p.getDisplayName();
        }
        if (ItemWandExcavation.mined.get(pp) == null) {
            ItemWandExcavation.mined.put(pp, 0);
        }
        if (!((Entity) p).worldObj.isRemote && ItemWandExcavation.mined.get(pp) != null
            && ItemWandExcavation.mined.get(pp) > 0 && !p.isUsingItem()) {
            if (ItemWandExcavation.mined.get(pp)
                > itemstack.getMaxDamage() - itemstack.getItemDamage()) {
                ItemWandExcavation.mined.put(
                    pp, itemstack.getMaxDamage() - itemstack.getItemDamage() + 1
                );
            }
            this.damageWand(itemstack, p, ItemWandExcavation.mined.get(pp));
            p.inventoryContainer.detectAndSendChanges();
            ItemWandExcavation.mined.put(pp, 0);
        }
        p.setItemInUse(itemstack, this.getMaxItemUseDuration(itemstack));
        return itemstack;
    }

    @Override
    public void
    onUsingTick(final ItemStack stack, final EntityPlayer p, final int count) {
        String pp = "R" + p.getDisplayName();
        if (!((Entity) p).worldObj.isRemote) {
            pp = "S" + p.getDisplayName();
        }
        if (ItemWandExcavation.mined.get(pp) == null) {
            ItemWandExcavation.mined.put(pp, 0);
        }
        if (ItemWandExcavation.soundDelay.get(pp) == null) {
            ItemWandExcavation.soundDelay.put(pp, 0L);
        }
        if (ItemWandExcavation.breakcount.get(pp) == null) {
            ItemWandExcavation.breakcount.put(pp, 0.0f);
        }
        if (ItemWandExcavation.lastX.get(pp) == null) {
            ItemWandExcavation.lastX.put(pp, 0);
        }
        if (ItemWandExcavation.lastY.get(pp) == null) {
            ItemWandExcavation.lastY.put(pp, 0);
        }
        if (ItemWandExcavation.lastZ.get(pp) == null) {
            ItemWandExcavation.lastZ.put(pp, 0);
        }
        final MovingObjectPosition mop
            = Utils.getTargetBlock(((Entity) p).worldObj, p, false, 10.0f);
        final Vec3 v = p.getLookVec();
        double tx = ((Entity) p).posX + v.xCoord * 10.0;
        double ty = ((Entity) p).posY + v.yCoord * 10.0;
        double tz = ((Entity) p).posZ + v.zCoord * 10.0;
        int impact = 0;
        if (mop != null) {
            tx = mop.hitVec.xCoord;
            ty = mop.hitVec.yCoord;
            tz = mop.hitVec.zCoord;
            impact = 5;
            if (!((Entity) p).worldObj.isRemote
                && ItemWandExcavation.soundDelay.get(pp) < System.currentTimeMillis()) {
                ((Entity) p)
                    .worldObj.playSoundEffect(
                        tx, ty, tz, "thaumcraft:rumble", 0.3f, 1.0f
                    );
                ItemWandExcavation.soundDelay.put(pp, System.currentTimeMillis() + 1200L);
            }
        } else {
            ItemWandExcavation.soundDelay.put(pp, 0L);
        }
        if (((Entity) p).worldObj.isRemote) {
            ItemWandExcavation.beam.put(
                pp,
                Thaumcraft.proxy.beamCont(
                    ((Entity) p).worldObj,
                    p,
                    tx,
                    ty,
                    tz,
                    2,
                    65382,
                    false,
                    (impact > 0) ? 2.0f : 0.0f,
                    ItemWandExcavation.beam.get(pp),
                    impact
                )
            );
        }
        if (mop != null && mop.typeOfHit == MovingObjectType.BLOCK) {
            final Block bi
                = ((Entity) p).worldObj.getBlock(mop.blockX, mop.blockY, mop.blockZ);
            final int md
                = ((Entity) p)
                      .worldObj.getBlockMetadata(mop.blockX, mop.blockY, mop.blockZ);
            final float hardness
                = bi.getBlockHardness(
                      ((Entity) p).worldObj, mop.blockX, mop.blockY, mop.blockZ
                  )
                / bi.getPlayerRelativeBlockHardness(
                    p, ((Entity) p).worldObj, mop.blockX, mop.blockY, mop.blockZ
                )
                / 100.0f;
            if (hardness >= 0.0f) {
                final int pot = this.getPotency(stack);
                float speed = 0.1f + pot * 0.1f;
                if (bi.getMaterial() == Material.rock
                    || bi.getMaterial() == Material.grass
                    || bi.getMaterial() == Material.ground
                    || bi.getMaterial() == Material.sand) {
                    speed = 1.0f + pot * 0.25f;
                }
                if (bi == Blocks.obsidian) {
                    speed = 50.0f + pot * 5;
                }
                if (ItemWandExcavation.lastX.get(pp) == mop.blockX
                    && ItemWandExcavation.lastY.get(pp) == mop.blockY
                    && ItemWandExcavation.lastZ.get(pp) == mop.blockZ) {
                    final float bc = ItemWandExcavation.breakcount.get(pp);
                    if (((Entity) p).worldObj.isRemote && bc > 0.0f && bi != Blocks.air) {
                        final int progress = (int) (bc / hardness * 9.0f);
                        Thaumcraft.proxy.excavateFX(
                            mop.blockX,
                            mop.blockY,
                            mop.blockZ,
                            p,
                            Block.getIdFromBlock(bi),
                            md,
                            progress
                        );
                    }
                    if (((Entity) p).worldObj.isRemote) {
                        if (bc >= hardness) {
                            ((Entity) p)
                                .worldObj.playAuxSFX(
                                    2001,
                                    mop.blockX,
                                    mop.blockY,
                                    mop.blockZ,
                                    Block.getIdFromBlock(bi) + (md << 12)
                                );
                            ItemWandExcavation.breakcount.put(pp, 0.0f);
                        } else {
                            ItemWandExcavation.breakcount.put(pp, bc + speed);
                        }
                    } else if (bc >= hardness) {
                        final int fortune
                            = ((ItemWandExcavation) stack.getItem()).getTreasure(stack);
                        bi.dropBlockAsItem(
                            ((Entity) p).worldObj,
                            mop.blockX,
                            mop.blockY,
                            mop.blockZ,
                            md,
                            fortune
                        );
                        ((Entity) p)
                            .worldObj.setBlock(
                                mop.blockX, mop.blockY, mop.blockZ, Blocks.air, 0, 3
                            );
                        ItemWandExcavation.lastX.put(pp, Integer.MAX_VALUE);
                        ItemWandExcavation.lastY.put(pp, Integer.MAX_VALUE);
                        ItemWandExcavation.lastZ.put(pp, Integer.MAX_VALUE);
                        ItemWandExcavation.breakcount.put(pp, 0.0f);
                        ItemWandExcavation.mined.put(
                            pp, ItemWandExcavation.mined.get(pp) + 1
                        );
                    } else {
                        ItemWandExcavation.breakcount.put(pp, bc + speed);
                    }
                } else {
                    ItemWandExcavation.lastX.put(pp, mop.blockX);
                    ItemWandExcavation.lastY.put(pp, mop.blockY);
                    ItemWandExcavation.lastZ.put(pp, mop.blockZ);
                    ItemWandExcavation.breakcount.put(pp, 0.0f);
                }
            }
        } else {
            ItemWandExcavation.lastX.put(pp, Integer.MAX_VALUE);
            ItemWandExcavation.lastY.put(pp, Integer.MAX_VALUE);
            ItemWandExcavation.lastZ.put(pp, Integer.MAX_VALUE);
            ItemWandExcavation.breakcount.put(pp, 0.0f);
        }
        if (ItemWandExcavation.mined.get(pp)
            > stack.getMaxDamage() - stack.getItemDamage()) {
            p.stopUsingItem();
        }
    }

    @Override
    public void onPlayerStoppedUsing(
        final ItemStack stack, final World world, final EntityPlayer p, final int count
    ) {
        String pp = "R" + p.getDisplayName();
        if (!((Entity) p).worldObj.isRemote) {
            pp = "S" + p.getDisplayName();
        }
        if (ItemWandExcavation.mined.get(pp) == null) {
            ItemWandExcavation.mined.put(pp, 0);
        }
        if (ItemWandExcavation.soundDelay.get(pp) == null) {
            ItemWandExcavation.soundDelay.put(pp, 0L);
        }
        if (ItemWandExcavation.breakcount.get(pp) == null) {
            ItemWandExcavation.breakcount.put(pp, 0.0f);
        }
        if (ItemWandExcavation.lastX.get(pp) == null) {
            ItemWandExcavation.lastX.put(pp, 0);
        }
        if (ItemWandExcavation.lastY.get(pp) == null) {
            ItemWandExcavation.lastY.put(pp, 0);
        }
        if (ItemWandExcavation.lastZ.get(pp) == null) {
            ItemWandExcavation.lastZ.put(pp, 0);
        }
        if (ItemWandExcavation.mined.get(pp)
            > stack.getMaxDamage() - stack.getItemDamage()) {
            ItemWandExcavation.mined.put(
                pp, stack.getMaxDamage() - stack.getItemDamage() + 1
            );
        }
        this.damageWand(stack, p, ItemWandExcavation.mined.get(pp));
        p.inventoryContainer.detectAndSendChanges();
        ItemWandExcavation.beam.put(pp, null);
        ItemWandExcavation.lastX.put(pp, Integer.MAX_VALUE);
        ItemWandExcavation.lastY.put(pp, Integer.MAX_VALUE);
        ItemWandExcavation.lastZ.put(pp, Integer.MAX_VALUE);
        ItemWandExcavation.breakcount.put(pp, 0.0f);
        ItemWandExcavation.mined.put(pp, 0);
    }

    @Override
    public EnumAction getItemUseAction(final ItemStack itemstack) {
        return EnumAction.bow;
    }

    static {
        ItemWandExcavation.soundDelay = new HashMap<>();
        ItemWandExcavation.beam = new HashMap<>();
        ItemWandExcavation.breakcount = new HashMap<>();
        ItemWandExcavation.lastX = new HashMap<>();
        ItemWandExcavation.lastY = new HashMap<>();
        ItemWandExcavation.lastZ = new HashMap<>();
        ItemWandExcavation.mined = new HashMap<>();
    }
}

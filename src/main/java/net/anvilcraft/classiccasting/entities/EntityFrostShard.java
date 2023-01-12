package net.anvilcraft.classiccasting.entities;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.S2BPacketChangeGameState;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.entities.monster.EntityFireBat;

public class EntityFrostShard extends Entity implements IProjectile {
    private int xTile;
    private int yTile;
    private int zTile;
    private Block inTile;
    private int inData;
    private boolean inGround;
    public Entity shootingEntity;
    public int ticksInGround;
    private int ticksInAir;
    private double damage;
    private int knockbackStrength;

    public EntityFrostShard(final World par1World) {
        super(par1World);
        this.xTile = -1;
        this.yTile = -1;
        this.zTile = -1;
        this.inTile = Blocks.air;
        this.inData = 0;
        this.inGround = false;
        this.ticksInAir = 0;
        this.damage = 1.0;
        super.renderDistanceWeight = 10.0;
        this.setSize(0.5f, 0.5f);
    }

    public EntityFrostShard(
        final World par1World, final double par2, final double par4, final double par6
    ) {
        super(par1World);
        this.xTile = -1;
        this.yTile = -1;
        this.zTile = -1;
        this.inTile = Blocks.air;
        this.inData = 0;
        this.inGround = false;
        this.ticksInAir = 0;
        this.damage = 1.0;
        super.renderDistanceWeight = 10.0;
        this.setSize(0.25f, 0.25f);
        this.setPosition(par2, par4, par6);
        super.yOffset = 0.0f;
    }

    public EntityFrostShard(
        final World par1World,
        final EntityLivingBase par2EntityLiving,
        final EntityLivingBase par3EntityLiving,
        final float par4,
        final float par5
    ) {
        super(par1World);
        this.xTile = -1;
        this.yTile = -1;
        this.zTile = -1;
        this.inTile = Blocks.air;
        this.inData = 0;
        this.inGround = false;
        this.ticksInAir = 0;
        this.damage = 1.0;
        super.renderDistanceWeight = 10.0;
        this.shootingEntity = (Entity) par2EntityLiving;
        super.posY = ((Entity) par2EntityLiving).posY + par2EntityLiving.getEyeHeight()
            - 0.10000000149011612;
        final double var6
            = ((Entity) par3EntityLiving).posX - ((Entity) par2EntityLiving).posX;
        final double var7 = ((Entity) par3EntityLiving).boundingBox.minY
            + ((Entity) par3EntityLiving).height / 3.0f - super.posY;
        final double var8
            = ((Entity) par3EntityLiving).posZ - ((Entity) par2EntityLiving).posZ;
        final double var9 = MathHelper.sqrt_double(var6 * var6 + var8 * var8);
        if (var9 >= 1.0E-7) {
            final float var10
                = (float) (Math.atan2(var8, var6) * 180.0 / 3.141592653589793) - 90.0f;
            final float var11
                = (float) (-(Math.atan2(var7, var9) * 180.0 / 3.141592653589793));
            final double var12 = var6 / var9;
            final double var13 = var8 / var9;
            this.setLocationAndAngles(
                ((Entity) par2EntityLiving).posX + var12,
                super.posY,
                ((Entity) par2EntityLiving).posZ + var13,
                var10,
                var11
            );
            super.yOffset = 0.0f;
            final float var14 = (float) var9 * 0.2f;
            this.setThrowableHeading(var6, var7 + var14, var8, par4, par5);
        }
    }

    public EntityFrostShard(
        final World par1World, final EntityLivingBase par2EntityLiving, final float par3
    ) {
        super(par1World);
        this.xTile = -1;
        this.yTile = -1;
        this.zTile = -1;
        this.inTile = Blocks.air;
        this.inData = 0;
        this.inGround = false;
        this.ticksInAir = 0;
        this.damage = 1.0;
        super.renderDistanceWeight = 10.0;
        this.shootingEntity = (Entity) par2EntityLiving;
        this.setSize(0.25f, 0.25f);
        this.setLocationAndAngles(
            ((Entity) par2EntityLiving).posX,
            ((Entity) par2EntityLiving).posY + par2EntityLiving.getEyeHeight(),
            ((Entity) par2EntityLiving).posZ,
            ((Entity) par2EntityLiving).rotationYaw,
            ((Entity) par2EntityLiving).rotationPitch
        );
        super.posX -= MathHelper.cos(super.rotationYaw / 180.0f * 3.1415927f) * 0.16f;
        super.posY -= 0.1500000001490116;
        super.posZ -= MathHelper.sin(super.rotationYaw / 180.0f * 3.1415927f) * 0.16f;
        final Vec3 vec3d = par2EntityLiving.getLook(1.0f);
        super.posX += vec3d.xCoord;
        super.posY += vec3d.yCoord;
        super.posZ += vec3d.zCoord;
        this.setPosition(super.posX, super.posY, super.posZ);
        super.yOffset = 0.0f;
        super.motionX = -MathHelper.sin(super.rotationYaw / 180.0f * 3.1415927f)
            * MathHelper.cos(super.rotationPitch / 180.0f * 3.1415927f);
        super.motionZ = MathHelper.cos(super.rotationYaw / 180.0f * 3.1415927f)
            * MathHelper.cos(super.rotationPitch / 180.0f * 3.1415927f);
        super.motionY = -MathHelper.sin(super.rotationPitch / 180.0f * 3.1415927f);
        this.setThrowableHeading(
            super.motionX, super.motionY, super.motionZ, par3 * 1.5f, 1.0f
        );
    }

    @Override
    protected void entityInit() {}

    @Override
    public void setThrowableHeading(
        double par1, double par3, double par5, final float par7, final float par8
    ) {
        final float var9
            = MathHelper.sqrt_double(par1 * par1 + par3 * par3 + par5 * par5);
        par1 /= var9;
        par3 /= var9;
        par5 /= var9;
        par1 += super.rand.nextGaussian() * 0.007499999832361937 * par8;
        par3 += super.rand.nextGaussian() * 0.007499999832361937 * par8;
        par5 += super.rand.nextGaussian() * 0.007499999832361937 * par8;
        par1 *= par7;
        par3 *= par7;
        par5 *= par7;
        super.motionX = par1;
        super.motionY = par3;
        super.motionZ = par5;
        final float var10 = MathHelper.sqrt_double(par1 * par1 + par5 * par5);
        final float n = (float) (Math.atan2(par1, par5) * 180.0 / 3.141592653589793);
        super.rotationYaw = n;
        super.prevRotationYaw = n;
        final float n2 = (float) (Math.atan2(par3, var10) * 180.0 / 3.141592653589793);
        super.rotationPitch = n2;
        super.prevRotationPitch = n2;
        this.ticksInGround = 0;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void setPositionAndRotation2(
        final double par1,
        final double par3,
        final double par5,
        final float par7,
        final float par8,
        final int par9
    ) {
        this.setPosition(par1, par3, par5);
        this.setRotation(par7, par8);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void setVelocity(final double par1, final double par3, final double par5) {
        super.motionX = par1;
        super.motionY = par3;
        super.motionZ = par5;
        if (super.prevRotationPitch == 0.0f && super.prevRotationYaw == 0.0f) {
            final float var7 = MathHelper.sqrt_double(par1 * par1 + par5 * par5);
            final float n = (float) (Math.atan2(par1, par5) * 180.0 / 3.141592653589793);
            super.rotationYaw = n;
            super.prevRotationYaw = n;
            final float n2 = (float) (Math.atan2(par3, var7) * 180.0 / 3.141592653589793);
            super.rotationPitch = n2;
            super.prevRotationPitch = n2;
            super.prevRotationPitch = super.rotationPitch;
            super.prevRotationYaw = super.rotationYaw;
            this.setLocationAndAngles(
                super.posX, super.posY, super.posZ, super.rotationYaw, super.rotationPitch
            );
            this.ticksInGround = 0;
        }
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (super.prevRotationPitch == 0.0f && super.prevRotationYaw == 0.0f) {
            final float var1 = MathHelper.sqrt_double(
                super.motionX * super.motionX + super.motionZ * super.motionZ
            );
            final float n = (float
            ) (Math.atan2(super.motionX, super.motionZ) * 180.0 / 3.141592653589793);
            super.rotationYaw = n;
            super.prevRotationYaw = n;
            final float n2
                = (float) (Math.atan2(super.motionY, var1) * 180.0 / 3.141592653589793);
            super.rotationPitch = n2;
            super.prevRotationPitch = n2;
        }
        final Block var2 = super.worldObj.getBlock(this.xTile, this.yTile, this.zTile);
        if (var2 != Blocks.air) {
            var2.setBlockBoundsBasedOnState(
                (IBlockAccess) super.worldObj, this.xTile, this.yTile, this.zTile
            );
            final AxisAlignedBB var3 = var2.getCollisionBoundingBoxFromPool(
                super.worldObj, this.xTile, this.yTile, this.zTile
            );
            if (var3 != null
                && var3.isVecInside(
                    Vec3.createVectorHelper(super.posX, super.posY, super.posZ)
                )) {
                this.inGround = true;
            }
        }
        if (this.inGround) {
            final Block var4
                = super.worldObj.getBlock(this.xTile, this.yTile, this.zTile);
            final int var5
                = super.worldObj.getBlockMetadata(this.xTile, this.yTile, this.zTile);
            if (var4 == this.inTile && var5 == this.inData) {
                ++this.ticksInGround;
                if (this.ticksInGround == 200) {
                    this.setDead();
                }
            } else {
                this.inGround = false;
                super.motionX *= super.rand.nextFloat() * 0.2f;
                super.motionY *= super.rand.nextFloat() * 0.2f;
                super.motionZ *= super.rand.nextFloat() * 0.2f;
                this.ticksInGround = 0;
                this.ticksInAir = 0;
            }
        } else {
            Thaumcraft.proxy.sparkle(
                (float) super.posX - 0.1f + super.rand.nextFloat() * 0.2f,
                (float) super.posY + super.rand.nextFloat() * 0.2f,
                (float) super.posZ - 0.1f + super.rand.nextFloat() * 0.2f,
                0.3f,
                6,
                0.005f
            );
            ++this.ticksInAir;
            Vec3 var6 = Vec3.createVectorHelper(super.posX, super.posY, super.posZ);
            Vec3 var7 = Vec3.createVectorHelper(
                super.posX + super.motionX,
                super.posY + super.motionY,
                super.posZ + super.motionZ
            );
            MovingObjectPosition var8 = super.worldObj.rayTraceBlocks(var6, var7, false);
            var6 = Vec3.createVectorHelper(super.posX, super.posY, super.posZ);
            var7 = Vec3.createVectorHelper(
                super.posX + super.motionX,
                super.posY + super.motionY,
                super.posZ + super.motionZ
            );
            if (var8 != null) {
                var7 = Vec3.createVectorHelper(
                    var8.hitVec.xCoord, var8.hitVec.yCoord, var8.hitVec.zCoord
                );
            }
            Entity var9 = null;
            final List<Entity> var10
                = super.worldObj.getEntitiesWithinAABBExcludingEntity(
                    this,
                    super.boundingBox
                        .addCoord(super.motionX, super.motionY, super.motionZ)
                        .expand(1.0, 1.0, 1.0)
                );
            double var11 = 0.0;
            for (int var12 = 0; var12 < var10.size(); ++var12) {
                final Entity var13 = var10.get(var12);
                if (var13.canBeCollidedWith() && this.ticksInAir > 2) {
                    final float var14 = 0.3f;
                    final AxisAlignedBB var15 = var13.boundingBox.expand(
                        (double) var14, (double) var14, (double) var14
                    );
                    final MovingObjectPosition var16
                        = var15.calculateIntercept(var6, var7);
                    if (var16 != null) {
                        final double var17 = var6.distanceTo(var16.hitVec);
                        if (var17 < var11 || var11 == 0.0) {
                            var9 = var13;
                            var11 = var17;
                        }
                    }
                }
            }
            if (var9 != null) {
                var8 = new MovingObjectPosition(var9);
            }
            if (var8 != null) {
                if (var8.entityHit != null) {
                    float var18 = MathHelper.sqrt_double(
                        super.motionX * super.motionX + super.motionY * super.motionY
                        + super.motionZ * super.motionZ
                    );
                    if (var8.entityHit instanceof EntityBlaze
                        || var8.entityHit instanceof EntityFireBat
                        || var8.entityHit instanceof EntityEnderman) {
                        var18 = (float) (int) (this.damage * 2.0);
                    }
                    final int var19 = MathHelper.ceiling_double_int(var18 * this.damage);
                    DamageSource var20 = null;
                    if (this.shootingEntity == null) {
                        var20 = DamageSource.causeThrownDamage(
                            (Entity) this, (Entity) this
                        );
                    } else {
                        var20 = DamageSource.causeThrownDamage(
                            (Entity) this, this.shootingEntity
                        );
                    }
                    if (var8.entityHit.attackEntityFrom(var20, var19)) {
                        if (var8.entityHit instanceof EntityLivingBase) {
                            final EntityLivingBase var21
                                = (EntityLivingBase) var8.entityHit;
                            if (this.knockbackStrength > 0) {
                                final float var22 = MathHelper.sqrt_double(
                                    super.motionX * super.motionX
                                    + super.motionZ * super.motionZ
                                );
                                if (var22 > 0.0f) {
                                    var8.entityHit.addVelocity(
                                        super.motionX * this.knockbackStrength
                                            * 0.6000000238418579 / var22,
                                        0.1,
                                        super.motionZ * this.knockbackStrength
                                            * 0.6000000238418579 / var22
                                    );
                                }
                            }
                            // TODO: WTF
                            //try {
                            //    if (this.shootingEntity != null && var21 != null) {
                            //        EnchantmentThorns.func_92095_b(
                            //            this.shootingEntity, var21, super.rand
                            //        );
                            //    }
                            //} catch (final Exception ex) {}

                            // play ding sound
                            if (this.shootingEntity != null
                                && var8.entityHit != this.shootingEntity
                                && var8.entityHit instanceof EntityPlayer
                                && this.shootingEntity instanceof EntityPlayerMP) {
                                ((EntityPlayerMP) this.shootingEntity)
                                    .playerNetServerHandler.sendPacket(
                                        new S2BPacketChangeGameState(6, 0.0F)
                                    );
                            }
                        }
                        this.playSound(
                            "damage.hit",
                            1.0f,
                            1.2f / (super.rand.nextFloat() * 0.2f + 0.9f)
                        );
                        if (!(var8.entityHit instanceof EntityEnderman)) {
                            this.setDead();
                        }
                    } else {
                        super.motionX *= -0.10000000149011612;
                        super.motionY *= -0.10000000149011612;
                        super.motionZ *= -0.10000000149011612;
                        super.rotationYaw += 180.0f;
                        super.prevRotationYaw += 180.0f;
                        this.ticksInAir = 0;
                    }
                } else {
                    this.xTile = var8.blockX;
                    this.yTile = var8.blockY;
                    this.zTile = var8.blockZ;
                    this.inTile
                        = super.worldObj.getBlock(this.xTile, this.yTile, this.zTile);
                    this.inData = super.worldObj.getBlockMetadata(
                        this.xTile, this.yTile, this.zTile
                    );
                    if (var8.sideHit == 1
                        || (Math.abs(super.motionX) < 0.10000000149011612
                            && Math.abs(super.motionY) < 0.10000000149011612
                            && Math.abs(super.motionZ) < 0.10000000149011612)) {
                        super.motionX = (float) (var8.hitVec.xCoord - super.posX);
                        super.motionY = (float) (var8.hitVec.yCoord - super.posY);
                        super.motionZ = (float) (var8.hitVec.zCoord - super.posZ);
                        final float var18 = MathHelper.sqrt_double(
                            super.motionX * super.motionX + super.motionY * super.motionY
                            + super.motionZ * super.motionZ
                        );
                        super.posX -= super.motionX / var18 * 0.05000000074505806;
                        super.posY -= super.motionY / var18 * 0.05000000074505806;
                        super.posZ -= super.motionZ / var18 * 0.05000000074505806;
                        this.inGround = true;
                    } else {
                        final ForgeDirection dir
                            = ForgeDirection.getOrientation(var8.sideHit);
                        super.motionX = dir.offsetX * Math.abs(super.motionX);
                        super.motionY = dir.offsetY * Math.abs(super.motionY);
                        super.motionZ = dir.offsetZ * Math.abs(super.motionZ);
                        super.motionX *= 0.10000000149011612
                            + (super.rand.nextFloat() - super.rand.nextFloat()) * 0.1f;
                        super.motionY *= 0.10000000149011612
                            + (super.rand.nextFloat() - super.rand.nextFloat()) * 0.1f;
                        super.motionZ *= 0.10000000149011612
                            + (super.rand.nextFloat() - super.rand.nextFloat()) * 0.1f;
                        this.ticksInAir = 0;
                    }
                    if (this.inTile != Blocks.air) {
                        if (this.inTile.getBlockHardness(
                                super.worldObj, this.xTile, this.yTile, this.zTile
                            ) > -1.0f
                            && this.inTile.getBlockHardness(
                                   super.worldObj, this.xTile, this.yTile, this.zTile
                               ) <= 0.33f) {
                            this.inTile.dropBlockAsItem(
                                super.worldObj,
                                this.xTile,
                                this.yTile,
                                this.zTile,
                                this.inData,
                                0
                            );
                            final int md = super.worldObj.getBlockMetadata(
                                this.xTile, this.yTile, this.zTile
                            );
                            super.worldObj.setBlock(
                                this.xTile, this.yTile, this.zTile, Blocks.air, 0, 3
                            );
                            super.worldObj.playAuxSFX(
                                2001,
                                this.xTile,
                                this.yTile,
                                this.zTile,
                                Block.getIdFromBlock(this.inTile) + (md << 12)
                            );
                        } else {
                            this.inTile.onEntityCollidedWithBlock(
                                super.worldObj,
                                this.xTile,
                                this.yTile,
                                this.zTile,
                                (Entity) this
                            );
                            this.playSound(
                                this.inTile.stepSound.getBreakSound(),
                                1.0f,
                                1.2f / (super.rand.nextFloat() * 0.2f + 0.9f)
                            );
                        }
                    }
                }
            }
            super.posX += super.motionX;
            super.posY += super.motionY;
            super.posZ += super.motionZ;
            float var18 = MathHelper.sqrt_double(
                super.motionX * super.motionX + super.motionZ * super.motionZ
            );
            super.rotationYaw = (float
            ) (Math.atan2(super.motionX, super.motionZ) * 180.0 / 3.141592653589793);
            super.rotationPitch
                = (float) (Math.atan2(super.motionY, var18) * 180.0 / 3.141592653589793);
            while (super.rotationPitch - super.prevRotationPitch < -180.0f) {
                super.prevRotationPitch -= 360.0f;
            }
            while (super.rotationPitch - super.prevRotationPitch >= 180.0f) {
                super.prevRotationPitch += 360.0f;
            }
            while (super.rotationYaw - super.prevRotationYaw < -180.0f) {
                super.prevRotationYaw -= 360.0f;
            }
            while (super.rotationYaw - super.prevRotationYaw >= 180.0f) {
                super.prevRotationYaw += 360.0f;
            }
            super.rotationPitch = super.prevRotationPitch
                + (super.rotationPitch - super.prevRotationPitch) * 0.2f;
            super.rotationYaw = super.prevRotationYaw
                + (super.rotationYaw - super.prevRotationYaw) * 0.2f;
            float var23 = 0.99f;
            final float var14 = 0.05f;
            if (this.isInWater()) {
                for (int var24 = 0; var24 < 4; ++var24) {
                    final float var22 = 0.25f;
                    super.worldObj.spawnParticle(
                        "bubble",
                        super.posX - super.motionX * var22,
                        super.posY - super.motionY * var22,
                        super.posZ - super.motionZ * var22,
                        super.motionX,
                        super.motionY,
                        super.motionZ
                    );
                }
                var23 = 0.1f;
            }
            super.motionX *= var23;
            super.motionY *= var23;
            super.motionZ *= var23;
            super.motionY -= var14;
            this.setPosition(super.posX, super.posY, super.posZ);

            // doBlockCollisions
            this.func_145775_I();
        }
        final int xx = MathHelper.floor_double(super.prevPosX);
        final int yy = MathHelper.floor_double(super.prevPosY);
        final int zz = MathHelper.floor_double(super.prevPosZ);
        if (super.worldObj.getBlock(xx, yy, zz) == Blocks.water) {
            for (int var25 = 0; var25 < 4; ++var25) {
                final float var26 = 0.25f;
                super.worldObj.spawnParticle(
                    "bubble",
                    super.posX - super.motionX * var26,
                    super.posY - super.motionY * var26,
                    super.posZ - super.motionZ * var26,
                    super.motionX,
                    super.motionY,
                    super.motionZ
                );
            }
            super.worldObj.setBlock(xx, yy, zz, Blocks.ice, 0, 3);
            this.setDead();
        }
    }

    @Override
    public void writeEntityToNBT(final NBTTagCompound par1NBTTagCompound) {
        par1NBTTagCompound.setShort("xTile", (short) this.xTile);
        par1NBTTagCompound.setShort("yTile", (short) this.yTile);
        par1NBTTagCompound.setShort("zTile", (short) this.zTile);
        par1NBTTagCompound.setInteger("inTile", Block.getIdFromBlock(this.inTile));
        par1NBTTagCompound.setByte("inData", (byte) this.inData);
        par1NBTTagCompound.setByte("inGround", (byte) (byte) (this.inGround ? 1 : 0));
        par1NBTTagCompound.setDouble("damage", this.damage);
    }

    @Override
    public void readEntityFromNBT(final NBTTagCompound par1NBTTagCompound) {
        this.xTile = par1NBTTagCompound.getShort("xTile");
        this.yTile = par1NBTTagCompound.getShort("yTile");
        this.zTile = par1NBTTagCompound.getShort("zTile");
        this.inTile = Block.getBlockById(par1NBTTagCompound.getInteger("inTile"));
        this.inData = (par1NBTTagCompound.getByte("inData") & 0xFF);
        this.inGround = (par1NBTTagCompound.getByte("inGround") == 1);
        if (par1NBTTagCompound.hasKey("damage")) {
            this.damage = par1NBTTagCompound.getDouble("damage");
        }
    }

    @Override
    protected boolean canTriggerWalking() {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public float getShadowSize() {
        return 0.0f;
    }

    public void setDamage(final double par1) {
        this.damage = par1;
    }

    public double getDamage() {
        return this.damage;
    }

    public void setKnockbackStrength(final int par1) {
        this.knockbackStrength = par1;
    }

    @Override
    public boolean canAttackWithItem() {
        return false;
    }
}

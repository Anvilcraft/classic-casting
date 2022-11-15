package net.anvilcraft.classiccasting;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class Utils {
    public static Entity getPointedEntity(
        final World world,
        final EntityPlayer entityplayer,
        final double range,
        final float padding
    ) {
        return getPointedEntity(world, entityplayer, range, padding, false);
    }

    public static Entity getPointedEntity(
        final World world,
        final EntityPlayer entityplayer,
        final double range,
        final float padding,
        final boolean nonCollide
    ) {
        Entity pointedEntity = null;
        final double d = range;
        final Vec3 vec3d = Vec3.createVectorHelper(
            ((Entity) entityplayer).posX,
            ((Entity) entityplayer).posY + entityplayer.getEyeHeight(),
            ((Entity) entityplayer).posZ
        );
        final Vec3 vec3d2 = entityplayer.getLookVec();
        final Vec3 vec3d3
            = vec3d.addVector(vec3d2.xCoord * d, vec3d2.yCoord * d, vec3d2.zCoord * d);
        final float f1 = padding;
        final List<Entity> list = world.getEntitiesWithinAABBExcludingEntity(
            (Entity) entityplayer,
            ((Entity) entityplayer)
                .boundingBox
                .addCoord(vec3d2.xCoord * d, vec3d2.yCoord * d, vec3d2.zCoord * d)
                .expand((double) f1, (double) f1, (double) f1)
        );
        double d2 = 0.0;
        for (int i = 0; i < list.size(); ++i) {
            final Entity entity = list.get(i);
            if (entity.canBeCollidedWith() || nonCollide) {
                if (world.rayTraceBlocks(
                        Vec3.createVectorHelper(
                            ((Entity) entityplayer).posX,
                            ((Entity) entityplayer).posY + entityplayer.getEyeHeight(),
                            ((Entity) entityplayer).posZ
                        ),
                        Vec3.createVectorHelper(
                            entity.posX, entity.posY + entity.getEyeHeight(), entity.posZ
                        ),
                        false
                    )
                    == null) {
                    final float f2 = Math.max(0.8f, entity.getCollisionBorderSize());
                    final AxisAlignedBB axisalignedbb = entity.boundingBox.expand(
                        (double) f2, (double) f2, (double) f2
                    );
                    final MovingObjectPosition movingobjectposition
                        = axisalignedbb.calculateIntercept(vec3d, vec3d3);
                    if (axisalignedbb.isVecInside(vec3d)) {
                        if (0.0 < d2 || d2 == 0.0) {
                            pointedEntity = entity;
                            d2 = 0.0;
                        }
                    } else if (movingobjectposition != null) {
                        final double d3 = vec3d.distanceTo(movingobjectposition.hitVec);
                        if (d3 < d2 || d2 == 0.0) {
                            pointedEntity = entity;
                            d2 = d3;
                        }
                    }
                }
            }
        }
        return pointedEntity;
    }

    public static Entity getPointedEntity(
        final World world,
        final EntityPlayer entityplayer,
        final double range,
        final Class<? extends Entity> clazz
    ) {
        Entity pointedEntity = null;
        final double d = range;
        final Vec3 vec3d = Vec3.createVectorHelper(
            ((Entity) entityplayer).posX,
            ((Entity) entityplayer).posY + entityplayer.getEyeHeight(),
            ((Entity) entityplayer).posZ
        );
        final Vec3 vec3d2 = entityplayer.getLookVec();
        final Vec3 vec3d3
            = vec3d.addVector(vec3d2.xCoord * d, vec3d2.yCoord * d, vec3d2.zCoord * d);
        final float f1 = 1.1f;
        final List<Entity> list = world.getEntitiesWithinAABBExcludingEntity(
            (Entity) entityplayer,
            ((Entity) entityplayer)
                .boundingBox
                .addCoord(vec3d2.xCoord * d, vec3d2.yCoord * d, vec3d2.zCoord * d)
                .expand((double) f1, (double) f1, (double) f1)
        );
        double d2 = 0.0;
        for (int i = 0; i < list.size(); ++i) {
            final Entity entity = list.get(i);
            if (entity.canBeCollidedWith()
                && world.rayTraceBlocks(
                       Vec3.createVectorHelper(
                           ((Entity) entityplayer).posX,
                           ((Entity) entityplayer).posY + entityplayer.getEyeHeight(),
                           ((Entity) entityplayer).posZ
                       ),
                       Vec3.createVectorHelper(
                           entity.posX, entity.posY + entity.getEyeHeight(), entity.posZ
                       ),
                       false
                   ) == null) {
                if (!clazz.isInstance(entity)) {
                    final float f2 = Math.max(0.8f, entity.getCollisionBorderSize());
                    final AxisAlignedBB axisalignedbb = entity.boundingBox.expand(
                        (double) f2, (double) f2, (double) f2
                    );
                    final MovingObjectPosition movingobjectposition
                        = axisalignedbb.calculateIntercept(vec3d, vec3d3);
                    if (axisalignedbb.isVecInside(vec3d)) {
                        if (0.0 < d2 || d2 == 0.0) {
                            pointedEntity = entity;
                            d2 = 0.0;
                        }
                    } else if (movingobjectposition != null) {
                        final double d3 = vec3d.distanceTo(movingobjectposition.hitVec);
                        if (d3 < d2 || d2 == 0.0) {
                            pointedEntity = entity;
                            d2 = d3;
                        }
                    }
                }
            }
        }
        return pointedEntity;
    }

    public static MovingObjectPosition
    getTargetBlock(final World world, final EntityPlayer player, final boolean par3) {
        final float var4 = 1.0f;
        final float var5 = ((Entity) player).prevRotationPitch
            + (((Entity) player).rotationPitch - ((Entity) player).prevRotationPitch)
                * var4;
        final float var6 = ((Entity) player).prevRotationYaw
            + (((Entity) player).rotationYaw - ((Entity) player).prevRotationYaw) * var4;
        final double var7 = ((Entity) player).prevPosX
            + (((Entity) player).posX - ((Entity) player).prevPosX) * var4;
        final double var8 = ((Entity) player).prevPosY
            + (((Entity) player).posY - ((Entity) player).prevPosY) * var4 + 1.62
            - ((Entity) player).yOffset;
        final double var9 = ((Entity) player).prevPosZ
            + (((Entity) player).posZ - ((Entity) player).prevPosZ) * var4;
        final Vec3 var10 = Vec3.createVectorHelper(var7, var8, var9);
        final float var11 = MathHelper.cos(-var6 * 0.017453292f - 3.1415927f);
        final float var12 = MathHelper.sin(-var6 * 0.017453292f - 3.1415927f);
        final float var13 = -MathHelper.cos(-var5 * 0.017453292f);
        final float var14 = MathHelper.sin(-var5 * 0.017453292f);
        final float var15 = var12 * var13;
        final float var16 = var11 * var13;
        final double var17 = 10.0;
        final Vec3 var18 = var10.addVector(var15 * var17, var14 * var17, var16 * var17);
        return world.rayTraceBlocks(var10, var18, par3);
    }

    public static MovingObjectPosition getTargetBlock(
        final World world,
        final double x,
        final double y,
        final double z,
        final float yaw,
        final float pitch,
        final boolean par3,
        final double range
    ) {
        final Vec3 var13 = Vec3.createVectorHelper(x, y, z);
        final float var14 = MathHelper.cos(-yaw * 0.017453292f - 3.1415927f);
        final float var15 = MathHelper.sin(-yaw * 0.017453292f - 3.1415927f);
        final float var16 = -MathHelper.cos(-pitch * 0.017453292f);
        final float var17 = MathHelper.sin(-pitch * 0.017453292f);
        final float var18 = var15 * var16;
        final float var19 = var14 * var16;
        final double var20 = range;
        final Vec3 var21 = var13.addVector(var18 * var20, var17 * var20, var19 * var20);
        return world.rayTraceBlocks(var13, var21, par3);
    }

    public static int isPlayerCarrying(final EntityPlayer player, final ItemStack stack) {
        for (int var2 = 0; var2 < player.inventory.mainInventory.length; ++var2) {
            if (player.inventory.mainInventory[var2] != null
                && player.inventory.mainInventory[var2].isItemEqual(stack)) {
                return var2;
            }
        }
        return -1;
    }

    public static boolean
    isBlockExposed(final World world, final int x, final int y, final int z) {
        return !world.getBlock(x, y, z + 1).isOpaqueCube()
            || !world.getBlock(x, y, z - 1).isOpaqueCube()
            || !world.getBlock(x + 1, y, z).isOpaqueCube()
            || !world.getBlock(x - 1, y, z).isOpaqueCube()
            || !world.getBlock(x, y + 1, z).isOpaqueCube()
            || !world.getBlock(x, y - 1, z).isOpaqueCube();
    }
}

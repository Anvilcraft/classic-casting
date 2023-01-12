package net.anvilcraft.classiccasting.items;

import net.anvilcraft.classiccasting.CCBlocks;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.common.config.ConfigBlocks;

public class ItemBlockAlembic extends ItemBlock {
    public ItemBlockAlembic(final Block par1) {
        super(par1);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    @Override
    public int getMetadata(final int par1) {
        return par1;
    }

    @Override
    public boolean onItemUse(
        final ItemStack stack,
        final EntityPlayer player,
        final World world,
        int x,
        final int y,
        int z,
        final int side,
        final float par8,
        final float par9,
        final float par10
    ) {
        final Block bi = world.getBlock(x, y, z);
        final int md = world.getBlockMetadata(x, y, z);
        if (bi == ConfigBlocks.blockMetalDevice && md == 0) {
            if (side == 0 || side == 1) {
                return false;
            }
            if (side == 2) {
                --z;
            }
            if (side == 3) {
                ++z;
            }
            if (side == 4) {
                --x;
            }
            if (side == 5) {
                ++x;
            }
        }
        if (stack.stackSize == 0) {
            return false;
        }
        if (!player.canPlayerEdit(x, y, z, side, stack)) {
            return false;
        }
        if (y == 255 && this.field_150939_a.getMaterial().isSolid()) {
            return false;
        }
        final Block var11 = world.getBlock(x, y, z);
        if (world.isAirBlock(x, y, z) || var11.isReplaceable(world, x, y, z)
            || var11 == Blocks.vine || var11 == Blocks.tallgrass
            || var11 == Blocks.deadbush || var11 == Blocks.snow) {
            for (int a = 2; a < 6; ++a) {
                final ForgeDirection dir = ForgeDirection.getOrientation(a);
                final int xx = x + dir.offsetX;
                final int yy = y + dir.offsetY;
                final int zz = z + dir.offsetZ;
                final Block bid = world.getBlock(xx, yy, zz);
                final int meta = world.getBlockMetadata(xx, yy, zz);
                if (bid == ConfigBlocks.blockMetalDevice && meta == 0
                    && this.placeBlockAt(
                        stack,
                        player,
                        world,
                        x,
                        y,
                        z,
                        side,
                        par8,
                        par9,
                        par10,
                        stack.getItemDamage()
                    )) {
                    final Block var12 = this.field_150939_a;
                    world.playSoundEffect(
                        (double) (x + 0.5f),
                        (double) (y + 0.5f),
                        (double) (z + 0.5f),
                        var12.stepSound.func_150496_b(),
                        (var12.stepSound.getVolume() + 1.0f) / 2.0f,
                        var12.stepSound.getPitch() * 0.8f
                    );
                    --stack.stackSize;
                    world.setBlock(
                        x, y, z, CCBlocks.alembic, dir.getOpposite().ordinal() - 2, 3
                    );
                    return true;
                }
            }
        }
        return false;
    }
}

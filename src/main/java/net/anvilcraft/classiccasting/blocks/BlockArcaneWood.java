package net.anvilcraft.classiccasting.blocks;

import net.anvilcraft.classiccasting.ClassicCastingTab;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.world.IBlockAccess;

public class BlockArcaneWood extends Block {
    public BlockArcaneWood() {
        super(Material.wood);
        this.setHardness(2.0f);
        this.setResistance(10.0f);
        this.setStepSound(soundTypeWood);
        this.setBlockName("classiccasting:blockArcaneWood");
        this.setCreativeTab(ClassicCastingTab.INSTANCE);
    }

    @Override
    public void registerBlockIcons(IIconRegister register) {
        this.blockIcon = register.registerIcon("classiccasting:arcanewoodblock");
    }

    @Override
    public int damageDropped(int md) {
        return md;
    }

    @Override
    public boolean isBeaconBase(
        IBlockAccess arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6
    ) {
        return true;
    }
}

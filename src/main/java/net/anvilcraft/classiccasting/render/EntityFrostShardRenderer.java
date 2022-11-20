package net.anvilcraft.classiccasting.render;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.anvilcraft.classiccasting.entities.EntityFrostShard;
import net.anvilcraft.classiccasting.render.models.ModelCrystal;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class EntityFrostShardRenderer extends Render {
    private ModelCrystal model;

    public EntityFrostShardRenderer() {
        this.model = new ModelCrystal();
    }

    public void renderShard(
        final EntityFrostShard shard,
        final double par2,
        final double par4,
        final double par6,
        final float par8,
        final float par9
    ) {
        this.bindTexture(
            new ResourceLocation("classiccasting", "textures/models/frostshard.png")
        );
        GL11.glPushMatrix();
        GL11.glEnable(32826);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glTranslatef(
            (float) par2,
            (float) par4 - 0.1f - shard.ticksInGround / 200.0f * 0.2f,
            (float) par6
        );
        final Random rnd = new Random(shard.getEntityId());
        GL11.glRotatef(
            shard.prevRotationYaw + (shard.rotationYaw - shard.prevRotationYaw) * par9,
            0.0f,
            1.0f,
            0.0f
        );
        GL11.glRotatef(
            shard.prevRotationPitch
                + (shard.rotationPitch - shard.prevRotationPitch) * par9,
            0.0f,
            0.0f,
            1.0f
        );
        GL11.glPushMatrix();
        GL11.glScalef(
            0.1f + rnd.nextFloat() * 0.1f,
            0.1f + rnd.nextFloat() * 0.1f,
            0.1f + rnd.nextFloat() * 0.1f
        );
        GL11.glColor4f(1.0f, 1.0f, 1.0f, (200.0f - shard.ticksInGround) / 150.0f);
        this.model.render();
        GL11.glScalef(1.0f, 1.0f, 1.0f);
        GL11.glPopMatrix();
        GL11.glRotatef(45.0f, 0.0f, 1.0f, 0.0f);
        GL11.glPushMatrix();
        GL11.glScalef(
            0.1f + rnd.nextFloat() * 0.1f,
            0.1f + rnd.nextFloat() * 0.1f,
            0.1f + rnd.nextFloat() * 0.1f
        );
        GL11.glColor4f(1.0f, 1.0f, 1.0f, (200.0f - shard.ticksInGround) / 150.0f);
        this.model.render();
        GL11.glScalef(1.0f, 1.0f, 1.0f);
        GL11.glPopMatrix();
        GL11.glDisable(3042);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glDisable(32826);
        GL11.glPopMatrix();
    }

    public void doRender(
        final Entity par1Entity,
        final double par2,
        final double par4,
        final double par6,
        final float par8,
        final float par9
    ) {
        this.renderShard((EntityFrostShard) par1Entity, par2, par4, par6, par8, par9);
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity arg0) {
        return null;
    }
}

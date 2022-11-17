package net.anvilcraft.classiccasting.render.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import org.lwjgl.opengl.GL11;

public class ModelAlembic extends ModelBase {
    ModelRenderer Core;
    ModelRenderer Filter;
    ModelRenderer Base;
    ModelRenderer Shape1;
    ModelRenderer Shape2;

    public ModelAlembic() {
        super.textureWidth = 64;
        super.textureHeight = 32;
        (this.Core = new ModelRenderer((ModelBase) this, 0, 0))
            .addBox(0.0f, 0.0f, 0.0f, 8, 10, 8);
        this.Core.setRotationPoint(-4.0f, 10.0f, -4.0f);
        this.Core.setTextureSize(64, 32);
        this.Core.mirror = true;
        this.setRotation(this.Core, 0.0f, 0.0f, 0.0f);
        (this.Shape1 = new ModelRenderer((ModelBase) this, 56, 0))
            .addBox(0.0f, 0.0f, 0.0f, 2, 13, 2);
        this.Shape1.setRotationPoint(-1.0f, 5.0f, 11.0f);
        this.Shape1.setTextureSize(64, 32);
        this.Shape1.mirror = true;
        this.setRotation(this.Shape1, 0.0f, 0.0f, 0.0f);
        (this.Shape2 = new ModelRenderer((ModelBase) this, 56, 0))
            .addBox(0.0f, 0.0f, 0.0f, 2, 15, 2);
        this.Shape2.setRotationPoint(-1.0f, 5.0f, -1.0f);
        this.Shape2.setTextureSize(64, 32);
        this.Shape2.mirror = true;
        this.setRotation(this.Shape2, 0.0f, 0.0f, 0.0f);
        (this.Filter = new ModelRenderer((ModelBase) this, 36, 24))
            .addBox(0.0f, 0.0f, 0.0f, 10, 4, 4);
        this.Filter.setRotationPoint(-2.0f, 4.0f, 11.0f);
        this.Filter.setTextureSize(64, 32);
        this.Filter.mirror = true;
        this.setRotation(this.Filter, 0.0f, 1.570796f, 0.0f);
        (this.Base = new ModelRenderer((ModelBase) this, 0, 20))
            .addBox(0.0f, 0.0f, 0.0f, 8, 4, 8);
        this.Base.setRotationPoint(-4.0f, 20.0f, -4.0f);
        this.Base.setTextureSize(64, 32);
        this.Base.mirror = true;
        this.setRotation(this.Base, 0.0f, 0.0f, 0.0f);
    }

    public void renderAll() {
        this.Shape1.render(0.0625f);
        this.Shape2.render(0.0625f);
        this.Base.render(0.0625f);
        this.Filter.render(0.0625f);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        this.Core.render(0.0625f);
        GL11.glDisable(3042);
    }

    private void
    setRotation(final ModelRenderer model, final float x, final float y, final float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }
}

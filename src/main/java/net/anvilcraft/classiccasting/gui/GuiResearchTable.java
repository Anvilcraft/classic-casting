package net.anvilcraft.classiccasting.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import dev.tilera.auracore.api.Aspects;
import net.anvilcraft.classiccasting.CCItems;
import net.anvilcraft.classiccasting.container.ContainerResearchTable;
import net.anvilcraft.classiccasting.research.ClassicResearchTableExtension;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.client.lib.UtilsFX;

public class GuiResearchTable extends GuiContainer {
    public float xSize_lo;
    public float ySize_lo;
    public long butcount = 0L;
    public ClassicResearchTableExtension extension;
    public FontRenderer galFontRenderer;
    public float popupScale = 0.05F;
    ArrayList<Coord2D> coords = new ArrayList<>();
    ArrayList<Aspect> aspects = new ArrayList<>();

    public GuiResearchTable(EntityPlayer player, ClassicResearchTableExtension e) {
        super(new ContainerResearchTable(player.inventory, e));
        this.extension = e;
        this.xSize = 240;
        this.ySize = 242;
        this.galFontRenderer = Minecraft.getMinecraft().standardGalacticFontRenderer;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2) {}

    @Override
    public void drawScreen(int par1, int par2, float par3) {
        super.drawScreen(par1, par2, par3);
        this.xSize_lo = (float) par1;
        this.ySize_lo = (float) par2;
        int var5 = this.guiLeft;
        int var6 = this.guiTop;
        // decompiler, wtf
        //int mposx = false;
        //int mposy = false;
        int mposx = par1 - (var5 + 51);
        int mposy = par2 - (var6 + 130);
        String text;
        int w;
        if (mposx >= 0 && mposy >= 0 && mposx < 29 && mposy < 7) {
            GL11.glPushMatrix();
            GL11.glTranslatef((float) (var5 + 65), (float) (var6 + 142), 0.0F);
            GL11.glScalef(0.65F, 0.65F, 0.0F);
            text = "Cursory";
            if (!this.extension.safe) {
                text = "Thorough";
            }

            w = this.fontRendererObj.getStringWidth(text) / 2;
            this.fontRendererObj.drawString(text, -w, 0, 11447982);
            GL11.glScalef(1.0F, 1.0F, 1.0F);
            GL11.glPopMatrix();
        }

        int a;
        for (a = 0; a < 5; ++a) {
            if (this.extension.tags[a] != null) {
                mposx = par1 - (var5 + 48);
                mposy = par2 - (var6 + 32 + 16 * a);
                if (mposx >= 0 && mposy >= 0 && mposx < 16 && mposy < 16) {
                    UtilsFX.drawCustomTooltip(
                        this,
                        GuiContainer.itemRender,
                        this.fontRendererObj,
                        Arrays.asList(
                            this.extension.tags[a].getName(),
                            this.extension.tags[a].getLocalizedDescription()
                        ),
                        par1,
                        par2,
                        11
                    );
                }
            }
        }

        if (this.aspects.size() > 0) {
            for (a = 0; a < this.aspects.size(); ++a) {
                mposx = par1 - ((Coord2D) this.coords.get(a)).x;
                mposy = par2 - ((Coord2D) this.coords.get(a)).y;
                if (mposx >= 0 && mposy >= 0 && mposx < 16 && mposy < 16) {
                    ArrayList<String> l = new ArrayList<>();
                    l.add(this.aspects.get(a).getName());
                    l.add(this.aspects.get(a).getLocalizedDescription());
                    if (this.extension.data.getTagProgress(this.aspects.get(a))
                        > 0.333332F) {
                        l.add(
                            (int
                            ) (this.extension.data.getTagProgress(this.aspects.get(a))
                               * 100.0F)
                            + "% complete"
                        );
                    }

                    UtilsFX.drawCustomTooltip(
                        this,
                        GuiContainer.itemRender,
                        this.fontRendererObj,
                        l,
                        par1,
                        par2,
                        11
                    );
                }
            }

            mposx = par1 - (var5 + 103);
            mposy = par2 - (var6 + 12);
            if (this.popupScale < 0.48F && mposx >= 0 && mposy >= 0 && mposx < 17
                && mposy < 17) {
                this.popupScale *= 1.25F;
                if (this.popupScale > 0.48F) {
                    this.popupScale = 0.48F;
                }
            }

            if (this.popupScale > 0.05F
                && (mposx < 0 || mposy < 0
                    || (float) mposx > 17.0F * this.popupScale * 15.5F
                    || (float) mposy > 17.0F * this.popupScale * 15.5F)) {
                this.popupScale *= 0.75F;
                if (this.popupScale < 0.05F) {
                    this.popupScale = 0.05F;
                }
            }

            GL11.glPushMatrix();
            GL11.glTranslatef((float) (var5 + 105), (float) (var6 + 14), 505.0F);
            GL11.glDisable(2896);
            GL11.glScalef(this.popupScale, this.popupScale, 0.0F);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.mc.renderEngine.bindTexture(
                // TODO: texture might not exist
                new ResourceLocation("thaumcraft", "textures/misc/parchment.png")
            );
            this.drawTexturedModalRect(0, 0, 0, 0, 256, 256);
            GL11.glScalef(1.5F, 1.5F, 0.0F);
            // TODO: this used to be the longer description from TC3 that has been yoinked
            // from TC4. We should somehow hack this in for ClassicCasting researches
            text = ResearchCategories.getResearch(this.extension.data.key).getText();
            int y;
            if (this.extension.data.getTotalProgress() >= 0.5F) {
                w = this.fontRendererObj.getStringWidth(text);
                y = (int
                ) ((float) w / 130.0F * ((float) this.galFontRenderer.FONT_HEIGHT / 2.0F)
                );
                this.fontRendererObj.drawSplitString(text, 22, 80 - y, 130, 0);
            } else {
                w = this.galFontRenderer.getStringWidth(text);
                y = (int
                ) ((float) w / 130.0F * ((float) this.galFontRenderer.FONT_HEIGHT / 2.0F)
                );
                this.galFontRenderer.drawSplitString(text, 22, 80 - y, 130, 0);
            }

            GL11.glEnable(2896);
            GL11.glPopMatrix();
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
        int var5 = this.guiLeft;
        int var6 = this.guiTop;
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(
            new ResourceLocation("classiccasting", "textures/gui/gui_researchtable.png")
        );
        this.drawTexturedModalRect(var5, var6, 0, 0, this.xSize, this.ySize);
        // WTF
        if (this.butcount > System.currentTimeMillis() || this.extension.tags[0] == null
            || (this.extension.contents[5] != null || this.extension.contents[6] == null)
                && (this.extension.contents[5] == null
                    || this.extension.contents[5].getItem() != CCItems.researchNotes
                    || this.extension.contents[5].getItemDamage() >= 64)) {
            this.drawTexturedModalRect(var5 + 76, var6 + 40, 241, 15, 15, 15);
        } else {
            this.drawTexturedModalRect(var5 + 76, var6 + 40, 241, 0, 15, 15);
        }

        if (this.extension.safe) {
            this.drawTexturedModalRect(var5 + 51, var6 + 130, 109, 246, 29, 7);
        } else {
            this.drawTexturedModalRect(var5 + 51, var6 + 130, 87, 246, 29, 7);
        }

        for (int a = 0; a < 5; ++a) {
            if (this.extension.tags[a] != null) {
                float op = 1.0F;
                if (this.extension.data != null
                    && this.extension.data.failedTags != null
                    // TODO: 64 makes no sense in todays TC version
                    //&& this.extension.data.failedTags.size() == 64
                    && this.extension.data.failedTags.getAmount(this.extension.tags[a])
                        >= 10) {
                    op = 0.15F;
                }

                UtilsFX.drawTag(
                    var5 + 48,
                    var6 + 32 + 16 * a,
                    this.extension.tags[a],
                    this.extension.tagAmounts[a],
                    Math.round(this.extension.tagBonus[a]),
                    0.0,
                    771,
                    op
                );
            }
        }

        this.drawResearchData(var5 + 167, var6 + 76);
    }

    private void drawResearchData(int x, int y) {
        this.coords = new ArrayList<>();
        this.aspects = new ArrayList<>();
        if (this.extension.data != null) {
            for (Aspect a : this.extension.data.tags.aspects.keySet()) {
                if (this.extension.data.getTagProgress(a) > 0.0F
                    || this.extension.data.getTotalProgress() >= 0.5F) {
                    if (!(this.extension.data.getTagProgress(a) >= 0.3F)
                        && !(this.extension.data.getTotalProgress() >= 0.75F)) {
                        this.aspects.add(Aspects.OBSCURE);
                    } else {
                        this.aspects.add(a);
                    }
                }
            }

            if (this.aspects.size() > 0) {
                float pieSlice = (float) (360 / this.aspects.size());
                float currentRot = -90.0F;

                int b;
                for (int a = 0; a < this.aspects.size(); ++a) {
                    float progmod = 15.0F
                        + 40.0F * this.extension.data.getTagProgress(this.aspects.get(a));
                    b = (int
                        ) ((float) x
                           + MathHelper.cos(currentRot / 180.0F * 3.1415927F) * progmod)
                        - 8;
                    int yy
                        = (int
                          ) ((float) y
                             + MathHelper.sin(currentRot / 180.0F * 3.1415927F) * progmod)
                        - 8;
                    currentRot += pieSlice;
                    this.coords.add(new Coord2D(b, yy));
                }

                new Random();

                for (int a = 0; a < this.aspects.size(); ++a) {
                    Aspect tt = this.aspects.get(a);
                    if (this.extension.data.getTagProgress(tt) < 0.2F
                        && this.extension.data.getTotalProgress() < 0.75F) {
                        tt = Aspects.OBSCURE;
                    }

                    UtilsFX.drawTag(
                        ((Coord2D) this.coords.get(a)).x,
                        ((Coord2D) this.coords.get(a)).y,
                        tt,
                        0,
                        0,
                        0.0
                    );
                }

                if (this.aspects.size() > 1) {
                    for (int a = 0; a < this.aspects.size(); ++a) {
                        this.drawLine(
                            x,
                            y,
                            ((Coord2D) this.coords.get(a)).x + 8,
                            ((Coord2D) this.coords.get(a)).y + 8,
                            1.0F
                                - (this.extension.data.getTagProgress(this.aspects.get(a))
                                   + this.extension.data.getTotalProgress())
                                    / 2.0F,
                            0.5F
                        );
                        if (this.aspects.size() > 2) {
                            for (b = a + 1; b < this.aspects.size(); ++b) {
                                this.drawLine(
                                    ((Coord2D) this.coords.get(b)).x + 8,
                                    ((Coord2D) this.coords.get(b)).y + 8,
                                    ((Coord2D) this.coords.get(a)).x + 8,
                                    ((Coord2D) this.coords.get(a)).y + 8,
                                    1.0F
                                        - (this.extension.data.getTagProgress(
                                               this.aspects.get(a)
                                           )
                                           + this.extension.data.getTotalProgress())
                                            / 2.0F,
                                    0.5F
                                );
                            }
                        }
                    }
                }

                for (int a = 0; a < this.aspects.size(); ++a) {
                    if (this.extension.data.getTagProgress(this.aspects.get(a)) >= 1.0F) {
                        this.drawLine(
                            ((Coord2D) this.coords.get(a)).x - 1,
                            ((Coord2D) this.coords.get(a)).y - 1,
                            ((Coord2D) this.coords.get(a)).x - 1,
                            ((Coord2D) this.coords.get(a)).y + 17,
                            0.3F,
                            1.0F
                        );
                        this.drawLine(
                            ((Coord2D) this.coords.get(a)).x - 1,
                            ((Coord2D) this.coords.get(a)).y - 1,
                            ((Coord2D) this.coords.get(a)).x + 17,
                            ((Coord2D) this.coords.get(a)).y - 1,
                            0.3F,
                            1.0F
                        );
                        this.drawLine(
                            ((Coord2D) this.coords.get(a)).x + 17,
                            ((Coord2D) this.coords.get(a)).y - 1,
                            ((Coord2D) this.coords.get(a)).x + 17,
                            ((Coord2D) this.coords.get(a)).y + 17,
                            0.3F,
                            1.0F
                        );
                        this.drawLine(
                            ((Coord2D) this.coords.get(a)).x - 1,
                            ((Coord2D) this.coords.get(a)).y + 17,
                            ((Coord2D) this.coords.get(a)).x + 17,
                            ((Coord2D) this.coords.get(a)).y + 17,
                            0.3F,
                            1.0F
                        );
                    }
                }
            }
        }
    }

    private void drawLine(int x, int y, int x2, int y2, float instability, float op) {
        if (instability > 0.01F) {
            double dist = (double
            ) (MathHelper.sqrt_float((float) ((x - x2) * (x - x2) + (y - y2) * (y - y2)))
               * instability);
            double xd = (double) (x2 - x) / dist;
            double yd = (double) (y2 - y) / dist;
            Random rand = new Random();
            int xr = 0;
            int yr = 0;
            int inc = (int) Math.floor(dist - 1.0);

            int a;
            for (a = 0; a < inc; ++a) {
                int xrn = rand.nextInt(2) - rand.nextInt(2);
                int yrn = rand.nextInt(2) - rand.nextInt(2);
                this.drawLine(
                    (int) ((double) x + xd * (double) a) + xr,
                    (int) ((double) y + yd * (double) a) + yr,
                    (int) ((double) x + xd * (double) (a + 1)) + xrn,
                    (int) ((double) y + yd * (double) (a + 1)) + yrn,
                    op
                );
                xr = xrn;
                yr = yrn;
            }

            this.drawLine(
                (int) ((double) x + xd * (double) a) + xr,
                (int) ((double) y + yd * (double) a) + yr,
                x2,
                y2,
                op
            );
        } else {
            this.drawLine(x, y, x2, y2, op);
        }
    }

    private void drawLine(int x, int y, int x2, int y2, float op) {
        int count = Minecraft.getMinecraft().thePlayer.ticksExisted;
        float bob = MathHelper.sin(((float) count + (float) x2) / 10.0F) * 0.15F + 0.15F;
        float bob2
            = MathHelper.sin(((float) count + (float) x + (float) y2) / 11.0F) * 0.15F
            + 0.15F;
        float bob3 = MathHelper.sin(((float) count + (float) y) / 12.0F) * 0.15F + 0.15F;
        Tessellator var12 = Tessellator.instance;
        GL11.glPushMatrix();
        GL11.glDisable(3553);
        GL11.glDisable(2896);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 1);
        var12.startDrawing(3);
        var12.setColorRGBA_F(bob, bob2, bob3, op);
        var12.addVertex((double) x, (double) y, 0.0);
        var12.addVertex((double) x2, (double) y2, 0.0);
        var12.draw();
        GL11.glDisable(3042);
        GL11.glDisable(32826);
        GL11.glEnable(2896);
        GL11.glEnable(3553);
        GL11.glPopMatrix();
    }

    @Override
    protected void mouseClicked(int par1, int par2, int par3) {
        super.mouseClicked(par1, par2, par3);
        int var4 = (this.width - this.xSize) / 2;
        int var5 = (this.height - this.ySize) / 2;
        int var7 = par1 - (var4 + 76);
        int var8 = par2 - (var5 + 40);
        if (this.butcount > System.currentTimeMillis() || var7 < 0 || var8 < 0
            || var7 >= 15 || var8 >= 15 || this.extension.tags[0] == null
            || (this.extension.contents[5] != null || this.extension.contents[6] == null)
                && (this.extension.contents[5] == null
                    || this.extension.contents[5].getItem() != CCItems.researchNotes
                    || this.extension.contents[5].getItemDamage() >= 64)) {
            var7 = par1 - (var4 + 51);
            var8 = par2 - (var5 + 130);
            if (this.butcount <= System.currentTimeMillis() && var7 >= 0 && var8 >= 0
                && var7 < 29 && var8 < 7) {
                this.mc.playerController.sendEnchantPacket(
                    this.inventorySlots.windowId, 1
                );
                this.extension.toggleSafe();
            }
        } else {
            this.mc.playerController.sendEnchantPacket(this.inventorySlots.windowId, 0);
            this.extension.startResearch();
            this.butcount = System.currentTimeMillis() + 150L;
        }
    }

    class Coord2D {
        int x;
        int y;

        Coord2D(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}

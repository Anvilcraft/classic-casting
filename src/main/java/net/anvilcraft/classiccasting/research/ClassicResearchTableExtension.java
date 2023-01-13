package net.anvilcraft.classiccasting.research;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dev.tilera.auracore.api.Aspects;
import dev.tilera.auracore.api.research.IResearchTable;
import dev.tilera.auracore.api.research.ResearchTableExtension;
import net.anvilcraft.classiccasting.CCItems;
import net.anvilcraft.classiccasting.ClassicCasting;
import net.anvilcraft.classiccasting.GuiType;
import net.anvilcraft.classiccasting.items.ItemResearchNotes;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.util.AxisAlignedBB;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.lib.crafting.ThaumcraftCraftingManager;

public class ClassicResearchTableExtension
    extends ResearchTableExtension implements IInventory {
    public ItemStack[] contents = new ItemStack[7];
    public EntityPlayer researcher = null;
    public boolean safe = true;
    public int baseChance = 15;
    public int baseLoss = 25;
    public boolean recalc = false;
    public Aspect[] tags = new Aspect[5];
    public short[] tagAmounts = new short[5];
    public float[] tagBonus = new float[5];
    private float maxTagBonus = 5.0f;
    public ResearchNoteData data = null;

    public ClassicResearchTableExtension(IResearchTable researchTable) {
        super(researchTable);
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        nbt.setBoolean("Safe", this.safe);
        NBTTagList var2 = new NBTTagList();
        for (int var3 = 0; var3 < this.contents.length; ++var3) {
            if (this.contents[var3] == null)
                continue;
            NBTTagCompound var4 = new NBTTagCompound();
            var4.setByte("Slot", (byte) var3);
            this.contents[var3].writeToNBT(var4);
            var2.appendTag(var4);
        }
        nbt.setTag("Inventory", var2);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        this.safe = nbt.getBoolean("Safe");
        NBTTagList var2 = nbt.getTagList("Inventory", 10);
        this.contents = new ItemStack[this.getSizeInventory()];
        for (int var3 = 0; var3 < var2.tagCount(); ++var3) {
            NBTTagCompound var4 = var2.getCompoundTagAt(var3);
            int var5 = var4.getByte("Slot") & 0xFF;
            if (var5 < 0 || var5 >= this.contents.length)
                continue;
            this.contents[var5] = ItemStack.loadItemStackFromNBT((NBTTagCompound) var4);
        }
    }

    @Override
    public void writeToPacket(NBTTagCompound nbt) {
        this.writeToNBT(nbt);
    }

    @Override
    public void readFromPacket(NBTTagCompound nbt) {
        this.readFromNBT(nbt);
    }

    @Override
    public void onTick() {
        if (this.recalc) {
            this.recalculateTags();
            this.gatherResults();
            this.recalcBaseChance();
            this.recalc = false;
        }
    }

    @Override
    public void markDirty() {
        this.gatherResults();
        this.recalc = true;
    }

    @Override
    public boolean openGUI(EntityPlayer player) {
        player.openGui(
            ClassicCasting.INSTANCE,
            GuiType.RESEARCH_TABLE.ordinal(),
            this.getWorld(),
            this.getXCoord(),
            this.getYCoord(),
            this.getZCoord()
        );
        return true;
    }

    @Override
    public String getNBTKey() {
        return "ccextension";
    }

    private void doResearch() {
        short[] amounts = this.tagAmounts;
        for (int a = 0; a < 5; ++a) {
            int n = a;
            amounts[n] = (short) (amounts[n] + Math.round(this.tagBonus[a]));
        }
        if (this.contents[5] == null) {
            String key = CCResearchManager.findMatchingResearch(
                this.researcher, this.tags, amounts
            );
            if (key != null) {
                CCResearchManager.createResearchNoteForTable(this, key);
                if (this.contents[5] != null) {
                    CCResearchManager.progressTableResearch(
                        this.getWorld(),
                        this.researcher,
                        this,
                        this.contents[5],
                        this.baseChance,
                        this.baseLoss,
                        this.tags,
                        amounts
                    );
                    if (CCResearchManager.getData(this.contents[5]).getTotalProgress()
                        == 1.0f) {
                        this.contents[5].setItemDamage(
                            this.contents[5].getItemDamage() + 64
                        );
                    }
                }
            }
        } else if (this.contents[5].getItem() == CCItems.researchNotes && this.contents[5].getItemDamage() < 64) {
            CCResearchManager.progressTableResearch(
                this.getWorld(),
                this.researcher,
                this,
                this.contents[5],
                this.baseChance,
                this.baseLoss,
                this.tags,
                amounts
            );
            if (CCResearchManager.getData(this.contents[5]).getTotalProgress() == 1.0f) {
                this.contents[5].setItemDamage(this.contents[5].getItemDamage() + 64);
            }
        }
        this.researcher = null;
    }

    public void startResearch() {
        this.getWorld().playSoundEffect(
            (double) this.getXCoord(),
            (double) this.getYCoord(),
            (double) this.getZCoord(),
            "random.click",
            0.15f,
            0.8f
        );
        if (!this.getWorld().isRemote) {
            this.doResearch();
            int chance = this.baseLoss;
            for (int a = 0; a < 5; ++a) {
                if (this.contents[a] == null
                    || this.getWorld().rand.nextInt(100) >= chance)
                    continue;
                --this.contents[a].stackSize;
                if (this.contents[a].stackSize != 0)
                    continue;
                this.contents[a] = null;
            }
        }
        this.getWorld().markBlockForUpdate(
            this.getXCoord(), this.getYCoord(), this.getZCoord()
        );
        this.markDirty();
    }

    public void toggleSafe() {
        this.safe = !this.safe;
        this.recalcBaseChance();
        this.getWorld().playSoundEffect(
            (double) this.getXCoord(),
            (double) this.getYCoord(),
            (double) this.getZCoord(),
            "step.wood",
            0.3f,
            1.2f
        );
    }

    public void gatherResults() {
        this.data = null;
        if (this.contents[5] != null
            && this.contents[5].getItem() instanceof ItemResearchNotes) {
            this.data = CCResearchManager.getData(this.contents[5]);
        }
    }

    private void recalcBaseChance() {
        /*this.baseChance = Config.resBaseSafeChance;
        this.baseLoss = Config.resBaseSafeLoss;
        if (!this.safe) {
            this.baseChance = Config.resBaseUnsafeChance;
            this.baseLoss = Config.resBaseUnsafeLoss;
        }*/
    }

    private void recalculateTags() {
        AspectList ot = new AspectList();
        for (int a = 0; a < 5; ++a) {
            this.tags[a] = null;
            this.tagAmounts[a] = 0;
            this.tagBonus[a] = 0.0f;
            if (this.contents[a] == null)
                continue;
            AspectList t = ThaumcraftCraftingManager.getObjectTags(this.contents[a]);
            if ((t = ThaumcraftCraftingManager.getBonusTags(this.contents[a], t)) == null
                || t.size() <= 0)
                continue;
            for (Aspect tag : t.getAspects()) {
                ot.merge(tag, t.getAmount(tag));
            }
        }
        if (ot.size() == 0) {
            return;
        }
        ArrayList<Aspect> sortedTags = new ArrayList<Aspect>();
    block2:
        for (Aspect tag : ot.getAspects()) {
            if (sortedTags.size() == 0) {
                sortedTags.add(tag);
                continue;
            }
            if (ot.getAmount(tag) > ot.getAmount((Aspect) ((Object) sortedTags.get(0)))) {
                sortedTags.add(0, tag);
                continue;
            }
            for (int a = 1; a < sortedTags.size(); ++a) {
                if (ot.getAmount(tag)
                    <= ot.getAmount((Aspect) ((Object) sortedTags.get(a))))
                    continue;
                sortedTags.add(a, tag);
                continue block2;
            }
            sortedTags.add(tag);
        }
        if (sortedTags.size() > 0) {
            for (int a = 0; a < 5; ++a) {
                if (sortedTags.size() > a && sortedTags.get(a) != null) {
                    this.tags[a] = sortedTags.get(a);
                    this.tagAmounts[a]
                        = (short) ot.getAmount((Aspect) ((Object) sortedTags.get(a)));
                    continue;
                }
                this.tags[a] = null;
                this.tagAmounts[a] = 0;
            }
        }
        this.recalculateBonus();
    }

    private void recalculateBonus() {
        List<EntityLivingBase> ents;
        if (!this.getWorld().isDaytime()
            && this.getWorld().getBlockLightValue(
                   this.getXCoord(), this.getYCoord() + 1, this.getZCoord()
               ) < 4
            && !this.getWorld().canBlockSeeTheSky(
                this.getXCoord(), this.getYCoord() + 1, this.getZCoord()
            )) {
            this.incrementTagBonus(1.0f, Arrays.asList(new Aspect[] { Aspect.DARKNESS }));
        }
        if (this.getWorld().isDaytime()
            && this.getWorld().getBlockLightValue(
                   this.getXCoord(), this.getYCoord() + 1, this.getZCoord()
               ) > 11
            && this.getWorld().canBlockSeeTheSky(
                this.getXCoord(), this.getYCoord() + 1, this.getZCoord()
            )) {
            this.incrementTagBonus(1.0f, Arrays.asList(new Aspect[] { Aspect.LIGHT }));
        }
        if ((float) this.getYCoord() > (float) this.getWorld().getActualHeight() * 0.5f) {
            this.incrementTagBonus(1.0f, Arrays.asList(new Aspect[] { Aspect.AIR }));
        }
        if ((float) this.getYCoord()
            > (float) this.getWorld().getActualHeight() * 0.66f) {
            this.incrementTagBonus(1.0f, Arrays.asList(new Aspect[] { Aspect.AIR }));
        }
        if ((float) this.getYCoord()
            > (float) this.getWorld().getActualHeight() * 0.75f) {
            this.incrementTagBonus(1.0f, Arrays.asList(new Aspect[] { Aspect.AIR }));
        }
        if (this.getWorld().isRaining()) {
            this.incrementTagBonus(1.0f, Arrays.asList(new Aspect[] { Aspect.WEATHER }));
        }
        if (this.getWorld().isThundering()) {
            this.incrementTagBonus(
                1.0f,
                Arrays.asList(new Aspect[] { Aspect.WEATHER, Aspect.ENERGY, Aspect.AIR })
            );
        }
        if ((ents = this.getWorld().getEntitiesWithinAABB(
                 EntityLivingBase.class,
                 AxisAlignedBB
                     .getBoundingBox(
                         (double) this.getXCoord(),
                         (double) this.getYCoord(),
                         (double) this.getZCoord(),
                         (double) (this.getXCoord() + 1),
                         (double) (this.getYCoord() + 1),
                         (double) (this.getZCoord() + 1)
                     )
                     .expand(15.0, 15.0, 15.0)
             ))
                .size()
            > 0) {
            for (Object ent : ents) {
                try {
                    if (((EntityLivingBase) ent).isAirBorne) {
                        this.incrementTagBonus(
                            0.5f, Arrays.asList(new Aspect[] { Aspect.FLIGHT })
                        );
                    }
                    if (((EntityLivingBase) ent).getMaxHealth()
                            - ((EntityLivingBase) ent).getHealth()
                        > 0) {
                        this.incrementTagBonus(
                            0.5f, Arrays.asList(new Aspect[] { Aspect.FLESH })
                        );
                    }
                    if (((EntityLivingBase) ent).isEntityUndead()) {
                        this.incrementTagBonus(
                            0.3f,
                            Arrays.asList(new Aspect[] { Aspect.DEATH, Aspects.EVIL })
                        );
                        this.incrementTagBonus(
                            0.2f, Arrays.asList(new Aspect[] { Aspect.SOUL })
                        );
                    }
                    if (((EntityLivingBase) ent).isPotionActive(Potion.poison)) {
                        this.incrementTagBonus(
                            0.5f, Arrays.asList(new Aspect[] { Aspect.POISON })
                        );
                    }
                    if (((EntityLivingBase) ent).isPotionActive(Potion.regeneration)) {
                        this.incrementTagBonus(
                            0.5f, Arrays.asList(new Aspect[] { Aspect.HEAL })
                        );
                    }
                    if (((EntityLivingBase) ent).isPotionActive(Potion.resistance)) {
                        this.incrementTagBonus(
                            0.5f, Arrays.asList(new Aspect[] { Aspect.ARMOR })
                        );
                    }
                    if (((EntityLivingBase) ent).isPotionActive(Potion.nightVision)) {
                        this.incrementTagBonus(
                            0.5f, Arrays.asList(new Aspect[] { Aspects.VISION })
                        );
                    }
                    if (((EntityLivingBase) ent).isPotionActive(Potion.confusion)) {
                        this.incrementTagBonus(
                            0.5f, Arrays.asList(new Aspect[] { Aspects.FLUX })
                        );
                    }
                    if (ent instanceof EntitySpider) {
                        this.incrementTagBonus(
                            0.3f, Arrays.asList(new Aspect[] { Aspects.INSECT })
                        );
                    }
                    if (ent instanceof EntityAnimal) {
                        this.incrementTagBonus(
                            0.2f, Arrays.asList(new Aspect[] { Aspect.BEAST })
                        );
                    }
                    if (ent instanceof EntitySheep) {
                        this.incrementTagBonus(
                            0.2f, Arrays.asList(new Aspect[] { Aspect.CLOTH })
                        );
                    }
                    if (ent instanceof EntityPlayer) {
                        this.incrementTagBonus(
                            0.4f, Arrays.asList(new Aspect[] { Aspects.CONTROL })
                        );
                    }
                    if (ent instanceof EntityVillager) {
                        this.incrementTagBonus(
                            0.3f, Arrays.asList(new Aspect[] { Aspects.CONTROL })
                        );
                        this.incrementTagBonus(
                            0.5f, Arrays.asList(new Aspect[] { Aspect.EXCHANGE })
                        );
                    }
                    if (ent instanceof EntityGolem) {
                        this.incrementTagBonus(
                            0.5f,
                            Arrays.asList(new Aspect[] { Aspect.ARMOR, Aspect.WEAPON })
                        );
                    }
                    if (ent instanceof EntityBlaze) {
                        this.incrementTagBonus(
                            0.3f, Arrays.asList(new Aspect[] { Aspect.FIRE })
                        );
                    }
                    if (!(ent instanceof EntityCreeper))
                        continue;
                    this.incrementTagBonus(
                        0.3f,
                        Arrays.asList(new Aspect[] { Aspects.DESTRUCTION, Aspects.FLUX })
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        for (int x = -10; x <= 10; ++x) {
            for (int z = -10; z <= 10; ++z) {
                for (int y = -10; y <= 10; ++y) {
                    if (y + this.getYCoord() <= 0
                        || y + this.getYCoord() >= this.getWorld().getActualHeight())
                        continue;
                    Block bi = this.getWorld().getBlock(
                        x + this.getXCoord(), y + this.getYCoord(), z + this.getZCoord()
                    );
                    int md = this.getWorld().getBlockMetadata(
                        x + this.getXCoord(), y + this.getYCoord(), z + this.getZCoord()
                    );
                    Material bm = bi.getMaterial();
                    if (bi == Blocks.jukebox) {
                        this.incrementTagBonus(
                            0.5f, Arrays.asList(new Aspect[] { Aspects.SOUND })
                        );
                        continue;
                    }
                    if (bi == Blocks.beacon) {
                        this.incrementTagBonus(
                            1.0f,
                            Arrays.asList(new Aspect[] { Aspects.CONTROL, Aspect.LIGHT })
                        );
                        continue;
                    }
                    if (bi == Blocks.skull) {
                        this.incrementTagBonus(
                            0.5f,
                            Arrays.asList(new Aspect[] { Aspect.MIND, Aspects.EVIL })
                        );
                        continue;
                    }
                    if (bi == Blocks.anvil) {
                        this.incrementTagBonus(
                            0.5f,
                            Arrays.asList(new Aspect[] { Aspect.CRAFT, Aspect.TOOL })
                        );
                        continue;
                    }
                    if (bi == Blocks.diamond_block) {
                        this.incrementTagBonus(
                            1.0f,
                            Arrays.asList(new Aspect[] { Aspect.CRYSTAL,
                                                         Aspects.VALUABLE })
                        );
                        continue;
                    }
                    if (bi == Blocks.glass_pane) {
                        this.incrementTagBonus(
                            0.1f, Arrays.asList(new Aspect[] { Aspects.VISION })
                        );
                        continue;
                    }
                    if (bi == Blocks.glass) {
                        this.incrementTagBonus(
                            0.1f, Arrays.asList(new Aspect[] { Aspect.CRYSTAL })
                        );
                        continue;
                    }
                    if (bi == Blocks.red_flower) {
                        this.incrementTagBonus(
                            0.2f, Arrays.asList(new Aspect[] { Aspects.FLOWER })
                        );
                        continue;
                    }
                    if (bi == Blocks.yellow_flower) {
                        this.incrementTagBonus(
                            0.2f, Arrays.asList(new Aspect[] { Aspects.FLOWER })
                        );
                        continue;
                    }
                    if (bi == Blocks.log) {
                        this.incrementTagBonus(
                            0.1f, Arrays.asList(new Aspect[] { Aspect.TREE })
                        );
                        continue;
                    }
                    if (bi == Blocks.crafting_table) {
                        this.incrementTagBonus(
                            0.2f,
                            Arrays.asList(new Aspect[] { Aspect.CRAFT, Aspect.TOOL })
                        );
                        continue;
                    }
                    if (bi == Blocks.stone_pressure_plate
                        || bi == Blocks.wooden_pressure_plate) {
                        this.incrementTagBonus(
                            0.2f, Arrays.asList(new Aspect[] { Aspect.TRAP })
                        );
                        continue;
                    }
                    if (bi == ConfigBlocks.blockJar && md == 1) {
                        this.incrementTagBonus(
                            0.5f,
                            Arrays.asList(new Aspect[] { Aspect.MIND, Aspects.EVIL })
                        );
                        continue;
                    }
                    if (bi == ConfigBlocks.blockJar) {
                        this.incrementTagBonus(
                            0.33f, Arrays.asList(new Aspect[] { Aspect.TRAP })
                        );
                        continue;
                    }
                    if (bi == Blocks.iron_block) {
                        this.incrementTagBonus(
                            0.75f, Arrays.asList(new Aspect[] { Aspect.METAL })
                        );
                        continue;
                    }
                    if (bi == Blocks.gold_block) {
                        this.incrementTagBonus(
                            0.75f, Arrays.asList(new Aspect[] { Aspects.VALUABLE })
                        );
                        continue;
                    }
                    if (bi == Blocks.brown_mushroom_block
                        || bi == Blocks.red_mushroom_block || bi == Blocks.brown_mushroom
                        || bi == Blocks.red_mushroom) {
                        this.incrementTagBonus(
                            0.2f, Arrays.asList(new Aspect[] { Aspects.FUNGUS })
                        );
                        continue;
                    }
                    if (bi == Blocks.bookshelf) {
                        this.incrementTagBonus(
                            0.5f, Arrays.asList(new Aspect[] { Aspect.MIND })
                        );
                        continue;
                    }
                    if (bi == Blocks.mob_spawner) {
                        this.incrementTagBonus(
                            1.0f,
                            Arrays.asList(new Aspect[] {
                                Aspect.BEAST, Aspect.CRAFT, Aspect.TRAP })
                        );
                        continue;
                    }
                    if (bi == Blocks.chest) {
                        this.incrementTagBonus(
                            0.5f, Arrays.asList(new Aspect[] { Aspect.VOID })
                        );
                        continue;
                    }
                    if (bi == Blocks.farmland) {
                        this.incrementTagBonus(
                            0.2f, Arrays.asList(new Aspect[] { Aspect.CROP })
                        );
                        continue;
                    }
                    if (bi == Blocks.end_portal_frame) {
                        this.incrementTagBonus(
                            0.5f, Arrays.asList(new Aspect[] { Aspect.ELDRITCH })
                        );
                        continue;
                    }
                    if (bi == Blocks.ender_chest) {
                        this.incrementTagBonus(
                            1.0f, Arrays.asList(new Aspect[] { Aspect.VOID })
                        );
                        continue;
                    }
                    if (bm == Material.lava) {
                        this.incrementTagBonus(
                            0.2f,
                            Arrays.asList(new Aspect[] { Aspect.FIRE, Aspects.ROCK })
                        );
                        continue;
                    }
                    if (bm == Material.fire) {
                        this.incrementTagBonus(
                            0.5f, Arrays.asList(new Aspect[] { Aspect.FIRE })
                        );
                        continue;
                    }
                    if (bi == Blocks.bedrock) {
                        this.incrementTagBonus(
                            0.1f,
                            Arrays.asList(new Aspect[] {
                                Aspect.DARKNESS, Aspects.ROCK, Aspect.EARTH })
                        );
                        continue;
                    }
                    if (bi == Blocks.deadbush) {
                        this.incrementTagBonus(
                            0.5f, Arrays.asList(new Aspect[] { Aspect.DEATH })
                        );
                        continue;
                    }
                    if (bi == Blocks.enchanting_table) {
                        this.incrementTagBonus(
                            1.0f, Arrays.asList(new Aspect[] { Aspect.MAGIC })
                        );
                        continue;
                    }
                    if (bm == Material.water) {
                        this.incrementTagBonus(
                            0.2f, Arrays.asList(new Aspect[] { Aspect.WATER })
                        );
                        continue;
                    }
                    if (bi == Blocks.redstone_wire) {
                        this.incrementTagBonus(
                            0.15f,
                            Arrays.asList(new Aspect[] { Aspect.ENERGY, Aspect.MECHANISM }
                            )
                        );
                        continue;
                    }
                    if (bi == Blocks.unpowered_repeater
                        || bi == Blocks.powered_repeater) {
                        this.incrementTagBonus(
                            0.25f,
                            Arrays.asList(new Aspect[] { Aspect.ENERGY, Aspect.MECHANISM }
                            )
                        );
                        continue;
                    }
                    if (bm == Material.portal) {
                        this.incrementTagBonus(
                            0.5f,
                            Arrays.asList(new Aspect[] { Aspect.ELDRITCH, Aspect.VOID })
                        );
                        continue;
                    }
                    if (bm == Material.cake) {
                        this.incrementTagBonus(
                            0.25f, Arrays.asList(new Aspect[] { Aspect.LIFE })
                        );
                        continue;
                    }
                    if (bm == Material.web) {
                        this.incrementTagBonus(
                            0.2f, Arrays.asList(new Aspect[] { Aspects.INSECT })
                        );
                        continue;
                    }
                    if (bm == Material.tnt) {
                        this.incrementTagBonus(
                            0.25f,
                            Arrays.asList(new Aspect[] { Aspects.DESTRUCTION,
                                                         Aspect.FIRE })
                        );
                        continue;
                    }
                    if (bi == Blocks.piston) {
                        this.incrementTagBonus(
                            0.3f,
                            Arrays.asList(new Aspect[] { Aspect.MECHANISM, Aspect.MOTION }
                            )
                        );
                        continue;
                    }
                    if (bi == Blocks.emerald_block) {
                        this.incrementTagBonus(
                            1.0f, Arrays.asList(new Aspect[] { Aspect.MAGIC })
                        );
                        continue;
                    }
                    if (bm == Material.dragonEgg) {
                        this.incrementTagBonus(
                            2.0f,
                            Arrays.asList(new Aspect[] {
                                Aspect.ELDRITCH, Aspects.EVIL, Aspect.MAGIC })
                        );
                        continue;
                    }
                    if (bm == Material.ice) {
                        this.incrementTagBonus(
                            0.2f, Arrays.asList(new Aspect[] { Aspect.COLD })
                        );
                        continue;
                    }
                    if (bm == Material.plants) {
                        this.incrementTagBonus(
                            0.2f, Arrays.asList(new Aspect[] { Aspect.PLANT })
                        );
                        continue;
                    }
                    if (bm == Material.redstoneLight) {
                        this.incrementTagBonus(
                            0.25f, Arrays.asList(new Aspect[] { Aspect.LIGHT })
                        );
                        continue;
                    }
                    if (bi == Blocks.brewing_stand) {
                        this.incrementTagBonus(
                            0.5f, Arrays.asList(new Aspect[] { Aspect.MAGIC })
                        );
                        continue;
                    }
                    if (bi == Blocks.cauldron) {
                        this.incrementTagBonus(
                            0.5f, Arrays.asList(new Aspect[] { Aspect.WATER })
                        );
                        continue;
                    }
                    if (bi != ConfigBlocks.blockMagicalLog || (md & 1) != 1)
                        continue;
                    this.incrementTagBonus(
                        0.15f, Arrays.asList(new Aspect[] { Aspects.PURE })
                    );
                }
            }
        }
    }

    private void incrementTagBonus(float amt, List<Aspect> taglist) {
        for (int a = 0; a < 5; ++a) {
            if (this.tags[a] == null || !taglist.contains(this.tags[a])
                || !(this.tagBonus[a] + amt <= this.maxTagBonus))
                continue;
            int n = a;
            this.tagBonus[n] = this.tagBonus[n] + amt;
        }
    }

    @Override
    public int getSizeInventory() {
        return 7;
    }

    @Override
    public ItemStack getStackInSlot(int var1) {
        return this.contents[var1];
    }

    @Override
    public ItemStack decrStackSize(int var1, int var2) {
        if (this.contents[var1] != null) {
            if (this.contents[var1].stackSize <= var2) {
                ItemStack var3 = this.contents[var1];
                this.contents[var1] = null;
                this.markDirty();
                return var3;
            }
            ItemStack var3 = this.contents[var1].splitStack(var2);
            if (this.contents[var1].stackSize == 0) {
                this.contents[var1] = null;
            }
            this.markDirty();
            return var3;
        }
        return null;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int var1) {
        if (this.contents[var1] != null) {
            ItemStack var2 = this.contents[var1];
            this.contents[var1] = null;
            return var2;
        }
        return null;
    }

    @Override
    public void setInventorySlotContents(int var1, ItemStack var2) {
        this.contents[var1] = var2;
        if (var2 != null && var2.stackSize > this.getInventoryStackLimit()) {
            var2.stackSize = this.getInventoryStackLimit();
        }
        this.markDirty();
    }

    @Override
    public String getInventoryName() {
        return "Research Table";
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer var1) {
        return this.getWorld().getTileEntity(
                   this.getXCoord(), this.getYCoord(), this.getZCoord()
               ) != this.getResearchTable()
            ? false
            : var1.getDistanceSq(
                  (double) this.getXCoord() + 0.5,
                  (double) this.getYCoord() + 0.5,
                  (double) this.getZCoord() + 0.5
              ) <= 64.0;
    }

    @Override
    public void openInventory() {
        this.recalc = true;
    }

    @Override
    public void closeInventory() {}

    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack) {
        return true;
    }
}

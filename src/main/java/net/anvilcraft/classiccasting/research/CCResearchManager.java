package net.anvilcraft.classiccasting.research;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import net.anvilcraft.classiccasting.CCItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchCategoryList;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.lib.research.ResearchManager;

public class CCResearchManager {

    public static Set<String> discoverable = new HashSet<>();

    public static ItemStack createNote(ItemStack stack, String key) {
        ResearchItem research = ResearchCategories.getResearch(key);
        if (research == null)
            return null;
        if (stack.stackTagCompound == null) {
            stack.setTagCompound(new NBTTagCompound());
        }
        if (!stack.stackTagCompound.hasKey("tcr")) {
            stack.stackTagCompound.setTag("tcr", new NBTTagCompound());
        }
        NBTTagCompound var3 = (NBTTagCompound) stack.stackTagCompound.getTag("tcr");
        var3.setString("project", key);
        research.tags.writeToNBT(var3, "tags");
        NBTTagCompound progress = new NBTTagCompound();
        for (Aspect a : research.tags.getAspects()) {
            progress.setInteger(a.getTag(), 0);
        }
        var3.setTag("progress", progress);
        new AspectList().writeToNBT(var3, "failed");
        return stack;
    }

    public static ResearchNoteData getData(ItemStack stack) {
        ResearchNoteData var1 = new ResearchNoteData();
        if (stack.stackTagCompound == null) {
            return var1;
        }
        if (!stack.stackTagCompound.hasKey("tcr")) {
            return var1;
        }
        NBTTagCompound var3 = stack.stackTagCompound.getCompoundTag("tcr");
        if (!var3.hasKey("project") || !var3.hasKey("tags") || !var3.hasKey("progress")
            || !var3.hasKey("failed")) {
            return var1;
        }

        var1.key = var3.getString("project");
        var1.tags.readFromNBT(var3, "tags");
        NBTTagCompound progress = var3.getCompoundTag("progress");
        for (Aspect a : var1.tags.getAspects()) {
            var1.progress.put(a, progress.getInteger(a.getTag()));
        }
        var1.failedTags.readFromNBT(var3, "failed");
        // WTF?
        /*
        boolean busted = false;
        for (Aspect tt : var1.tags.getAspects()) {
            if (tt != null) continue;
            busted = true;
            break;
        }
        if (!busted && var1.tags.length != ResearchList.getResearchTags(var1.key).length)
        { busted = true;
        }
        if (busted) {
            byte[] tt = var1.tags;
            int[] tp = var1.progress;
            var1.tags = ResearchList.getResearchTags(var1.key);
            var1.progress = new int[ResearchList.getResearchTags(var1.key).length];
            var1.failedTags = new byte[64];
            for (int a = 0; a < var1.tags.length; ++a) {
                for (int b = 0; b < tt.length; ++b) {
                    if (var1.tags[a] != tt[b]) continue;
                    var1.progress[a] = tp[b];
                }
            }
        }*/
        return var1;
    }

    public static void updateData(ItemStack stack, ResearchNoteData data) {
        if (stack.stackTagCompound == null) {
            stack.setTagCompound(new NBTTagCompound());
        }
        if (!stack.stackTagCompound.hasKey("tcr")) {
            stack.stackTagCompound.setTag("tcr", new NBTTagCompound());
            return;
        }
        NBTTagCompound var3 = (NBTTagCompound) stack.stackTagCompound.getTag("tcr");

        var3.setString("project", data.key);
        data.tags.writeToNBT(var3, "tags");
        NBTTagCompound progress = new NBTTagCompound();
        for (Aspect a : data.tags.getAspects()) {
            if (data.progress.containsKey(a)) {
                progress.setInteger(a.getTag(), data.progress.get(a));
            } else {
                progress.setInteger(a.getTag(), 0);
            }
        }
        var3.setTag("progress", progress);
        data.failedTags.writeToNBT(var3, "failed");
    }

    public static void
    createResearchNoteForTable(ClassicResearchTableExtension table, String key) {
        if (table.getStackInSlot(5) == null && table.getStackInSlot(6) != null
            && table.getStackInSlot(6).isItemEqual(new ItemStack(Items.paper))) {
            table.decrStackSize(6, 1);
            ResearchItem item = ResearchCategories.getResearch(key);
            //int md = ResearchList.getResearchPrimaryTag(key);
            int md = 0;
            table.setInventorySlotContents(
                5,
                CCResearchManager.createNote(
                    new ItemStack(CCItems.researchNotes, 1, md), key
                )
            );
            table.markDirty();
        }
    }

    public static String
    findMatchingResearch(EntityPlayer player, Aspect[] tags, short[] tagAmounts) {
        int bestMatchNum = 0;
        String bestMatch = null;
        for (ResearchCategoryList cat : ResearchCategories.researchCategories.values()) {
            for (ResearchItem research : cat.research.values()) {
                if (research.isStub() || research.isLost()
                    || ResearchManager.isResearchComplete(
                        player.getDisplayName(), research.key
                    )
                    || !ResearchManager.doesPlayerHaveRequisites(
                        player.getDisplayName(), research.key
                    )
                    || !isDiscoverable(research.key))
                    continue;
                int match = 0;
                for (int q = 0; q < 5; ++q) {
                    if (tags[q] == null || tagAmounts[q] <= 0
                        || research.tags.getAmount(tags[q]) <= 0)
                        continue;
                    ++match;
                }
                if (match <= 0 || match <= bestMatchNum)
                    continue;
                bestMatchNum = match;
                bestMatch = research.key;
            }
        }
        return bestMatch;
    }

    public static boolean progressTableResearch(
        World world,
        EntityPlayer researcher,
        ClassicResearchTableExtension table,
        ItemStack note,
        int baseChance,
        int baseLoss,
        Aspect[] inTags,
        short[] inTagAmounts
    ) {
        AspectList tags;
        String key;
        boolean progressed = false;
        if (baseLoss <= 0) {
            baseLoss = 1;
        }
        if (note.stackTagCompound == null
            && (key = CCResearchManager.findLostResearch(researcher)) != null) {
            note = CCResearchManager.createNote(note, key);
        }
        ResearchNoteData data = CCResearchManager.getData(note);
        ResearchItem research = ResearchCategories.getResearch(data.key);
        if (research == null)
            return false;
        if ((tags = research.tags.copy()) != null) {
            boolean found;
            for (Aspect tag : inTags) {
                if (tag == null)
                    continue;
                found = false;
                for (Aspect entry : tags.getAspects()) {
                    if (entry != tag)
                        continue;
                    found = true;
                    break;
                }
                if (found || data.failedTags.getAmount(tag) >= 100)
                    continue;
                data.failedTags.add(
                    tag, Math.round((float) (2 + world.rand.nextInt(baseLoss)) / 10.0f)
                );
            }
            for (Aspect tag : tags.getAspects()) {
                found = false;
                int entry;
                for (entry = 0; entry < inTags.length; ++entry) {
                    if (inTags[entry] != tag)
                        continue;
                    found = true;
                    break;
                }
                if (!found)
                    continue;
                float chance = (float) baseChance / 100.0f;
                int tries = CCResearchManager.researchIterations(inTagAmounts[entry]);
                if (tries == 0) {
                    tries = 1;
                    chance = (float) baseChance / 2.0f;
                }
                for (int a = 0; a < tries; ++a) {
                    if (!(world.rand.nextFloat() <= chance))
                        continue;
                    if (data.tags.getAmount(tag) <= 0
                        || data.progress.get(tag) >= research.tags.getAmount(tag))
                        break;
                    data.progress.put(tag, data.progress.get(tag) + 1);
                    chance *= 0.9f;
                    progressed = true;
                    continue;
                }
            }
            CCResearchManager.updateData(note, data);
        } else {
            table.contents[5]
                = new ItemStack(ConfigItems.itemResource, 7 + world.rand.nextInt(3), 9);
        }
        return progressed;
    }

    private static int researchIterations(int amount) {
        return 1 + amount / 2;
    }

    public static String findLostResearch(EntityPlayer player) {
        String bestMatch = null;
        ArrayList<String> choices = new ArrayList<String>();
        for (ResearchCategoryList cat : ResearchCategories.researchCategories.values()) {
            for (ResearchItem research : cat.research.values()) {
                if (research.isStub() || !research.isLost()
                    || ResearchManager.isResearchComplete(
                        player.getDisplayName(), research.key
                    )
                    || !ResearchManager.doesPlayerHaveRequisites(
                        player.getDisplayName(), research.key
                    ))
                    continue;
                choices.add(research.key);
            }
        }
        if (choices.size() > 0) {
            bestMatch
                = (String) choices.get(player.worldObj.rand.nextInt(choices.size()));
        }
        return bestMatch;
    }

    public static boolean isDiscoverable(String key) {
        if (discoverable.contains(key)) {
            return true;
        } else {
            ResearchItem item = ResearchCategories.getResearch(key);
            return item != null && (item.category == "ALCHEMY" || item.category == "ARTIFICE" || item.category == "GOLEMANCY");
        }
    }

    public static void addDiscoverableResearch(ResearchItem research) {
        discoverable.add(research.key);
    }
}

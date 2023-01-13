package net.anvilcraft.classiccasting.research;

import java.util.HashMap;
import java.util.Map;

import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchItem;

public class ResearchNoteData {
    public String key;
    public AspectList tags = new AspectList();
    public Map<Aspect, Integer> progress = new HashMap<>();
    public AspectList failedTags = new AspectList();

    public float getTotalProgress() {
        ResearchItem rr = ResearchCategories.getResearch(this.key);
        if (rr == null) {
            return 0.0f;
        }
        float totala = 0.0f;
        float totalb = 0.0f;
        for (Aspect a : tags.getAspects()) {
            totala += (float)this.progress.get(a);
            totalb += (float)rr.tags.getAmount(a);
        }
        return totala / totalb;
    }

    public float getTagProgress(Aspect tag) {
        ResearchItem rr = ResearchCategories.getResearch(this.key);
        if (rr == null) {
            return 0.0f;
        }
        for (Aspect a : tags.getAspects()) {
            if (this.tags == null || tag != a) continue;
            return (float)this.progress.get(a) / (float)rr.tags.getAmount(a);
        }
        return 0.0f;
    }
}

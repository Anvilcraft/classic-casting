package net.anvilcraft.classiccasting;

public enum GuiType {
    INFUSION_WORKBENCH,
    RESEARCH_TABLE;

    public static GuiType get(int id) {
        if (id < 0 || id >= GuiType.values().length)
            return null;

        return GuiType.values()[id];
    }
}

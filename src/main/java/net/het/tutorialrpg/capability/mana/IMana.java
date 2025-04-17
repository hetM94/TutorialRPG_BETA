package net.het.tutorialrpg.capability.mana;

public interface IMana {
    int getMana();
    void setMana(int mana);

    int getMaxMana();
    void setMaxMana(int maxMana);

    void addMana(int amount);
    void consumeMana(int amount);
}

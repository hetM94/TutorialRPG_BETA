// src/main/java/net/het/tutorialrpg/system/mana/IMana.java

package net.het.tutorialrpg.system.mana;

public interface IMana {
    int getMana();
    void setMana(int mana);

    int getMaxMana();
    void setMaxMana(int maxMana);

    void addMana(int amount);
    void consumeMana(int amount);
}

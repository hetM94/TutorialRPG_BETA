// src/main/java/net/het/tutorialrpg/system/mana/Mana.java

package net.het.tutorialrpg.system.mana;

public class Mana implements IMana {
    private int mana;
    private int maxMana;

    public Mana(int initialMana, int initialMaxMana) {
        // Ensure maxMana ≥ 0, then clamp initialMana to [0, maxMana]
        this.maxMana = Math.max(0, initialMaxMana);
        this.mana    = Math.max(0, Math.min(initialMana, this.maxMana));
    }

    @Override
    public int getMana() {
        return mana;
    }

    @Override
    public void setMana(int mana) {
        // Clamp to [0, maxMana]
        this.mana = Math.max(0, Math.min(mana, maxMana));
    }

    @Override
    public int getMaxMana() {
        return maxMana;
    }

    @Override
    public void setMaxMana(int maxMana) {
        // Update maxMana and ensure current mana is not above it
        this.maxMana = Math.max(0, maxMana);
        if (mana > this.maxMana) {
            mana = this.maxMana;
        }
    }

    @Override
    public void addMana(int amount) {
        // Delegate to setMana for proper clamping
        setMana(this.mana + amount);
    }

    @Override
    public void consumeMana(int amount) {
        // Delegate to setMana for proper clamping
        setMana(this.mana - amount);
    }
}

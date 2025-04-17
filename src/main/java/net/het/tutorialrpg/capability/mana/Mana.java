package net.het.tutorialrpg.capability.mana;



public class Mana implements IMana {
    private int mana, maxMana;

    public Mana() {
        this(0, 100); // Default values
    }

    public Mana(int initialMana, int initialMaxMana) {
        setMaxMana(initialMaxMana);
        setMana(initialMana);
    }

    @Override public int getMana() { return mana; }
    @Override public int getMaxMana() { return maxMana; }


    @Override public void setMana(int mana) {
        this.mana = Math.max(0, Math.min(mana, maxMana));
    }
    @Override public void setMaxMana(int maxMana) {
        this.maxMana = Math.max(0, maxMana);
        if (this.mana > this.maxMana) this.mana = this.maxMana;
    }


    @Override public void addMana(int amount) {
        if (amount > 0) setMana(mana + amount);
    }
    @Override public void consumeMana(int amount) {
        if (amount > 0) setMana(mana - amount);
    }
}

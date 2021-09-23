package net.vadamdev.kbeditor.utils;

public class KBInfo {
    private float meleeHorizontal, meleeVertical, speedHorizontal, speedVertical;

    public KBInfo(float meleeHorizontal, float meleeVertical, float speedHorizontal, float speedVertical) {
        this.meleeHorizontal = meleeHorizontal;
        this.meleeVertical = meleeVertical;
        this.speedHorizontal = speedHorizontal;
        this.speedVertical = speedVertical;
    }

    public float getMeleeHorizontal() {
        return meleeHorizontal;
    }

    public float getMeleeVertical() {
        return meleeVertical;
    }

    public float getSpeedHorizontal() {
        return speedHorizontal;
    }

    public float getSpeedVertical() {
        return speedVertical;
    }
}

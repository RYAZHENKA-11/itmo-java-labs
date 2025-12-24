package ru.shortystory.characters.parts;

import java.util.Objects;
import java.util.Random;

public class Face {
    private Nose nose = new Nose(44.44);
    private Lip lips = new Lip(18.0);

    public Nose getNose() {
        return nose;
    }

    public Lip getLips() {
        return lips;
    }

    public void makeFancyGrimace() {
        Random random = new Random();
        double compressionAmount = 0.5 + random.nextDouble();
        double stretchAmount = 0.5 + random.nextDouble();

        nose.compress(compressionAmount);
        lips.stretch(stretchAmount);

        System.out.printf("Строит рожи перед зеркалом: нос сжат на %.1f мм, губа оттянута на %.1f мм.%n",
                compressionAmount, stretchAmount);
    }

    @Override
    public String toString() {
        return "Face{" +
                "nose=" + nose +
                ", lips=" + lips +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Face face = (Face) o;
        return Objects.equals(nose, face.nose) && Objects.equals(lips, face.lips);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nose, lips);
    }
}
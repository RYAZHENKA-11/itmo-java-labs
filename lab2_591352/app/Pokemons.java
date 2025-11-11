package app;

import ru.ifmo.se.pokemon.Type;
import ru.ifmo.se.pokemon.Pokemon;

final class Bruxish extends Pokemon {
    public Bruxish(String name) {
        super(name, 100);
        this.setType(Type.WATER, Type.PSYCHIC);
        this.setStats(68, 105, 70, 70, 70, 92);
        this.setMove(new Thunder(), new Confide(), new ChargeBeam(), new DreamEater());
    }
}

class Kabuto extends Pokemon {
    public Kabuto(String name) {
        super(name, 100);
        this.setType(Type.ROCK, Type.WATER);
        this.setStats(30, 80, 90, 55, 45, 55);
        this.setMove(new Confide(), new AerialAce(), new Facade());
    }
}

final class Kabutops extends Kabuto {
    public Kabutops(String name) {
        super(name);
        this.setStats(60, 115, 105, 65, 70, 80);
        this.setMove(new Confide(), new AerialAce(), new Facade(), new SwordsDance());
    }
}

class Bounsweet extends Pokemon {
    public Bounsweet(String name) {
        super(name, 100);
        this.setType(Type.GRASS);
        this.setStats(42, 30, 38, 30, 38, 32);
        this.setMove(new Confide(), new MagicalLeaf());
    }
}

class Steenee extends Bounsweet {
    public Steenee(String name) {
        super(name);
        this.setStats(52, 40, 48, 40, 48, 62);
        this.setMove(new Confide(), new MagicalLeaf(), new PlayNice());
    }
}

final class Tsareena extends Steenee {
    public Tsareena(String name) {
        super(name);
        this.setStats(72, 120, 98, 50, 98, 72);
        this.setMove(new Confide(), new MagicalLeaf(), new PlayNice(), new DoubleTeam());
    }
}
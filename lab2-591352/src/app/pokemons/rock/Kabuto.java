package app.pokemons.rock;

import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Type;
import app.moves.status.Confide;
import app.moves.physical.AerialAce;
import app.moves.physical.Facade;

public class Kabuto extends Pokemon {
    public Kabuto(String name) {
        super(name, 100);
        this.setType(Type.ROCK, Type.WATER);
        this.setStats(30, 80, 90, 55, 45, 55);
        this.setMove(new Confide(), new AerialAce(), new Facade());
    }
}

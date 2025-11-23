package app.pokemons.grass;

import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Type;
import app.moves.status.Confide;
import app.moves.special.MagicalLeaf;

public class Bounsweet extends Pokemon {
    public Bounsweet(String name) {
        super(name, 100);
        this.setType(Type.GRASS);
        this.setStats(42, 30, 38, 30, 38, 32);
        this.setMove(new Confide(), new MagicalLeaf());
    }
}

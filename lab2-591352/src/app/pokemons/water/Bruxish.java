package app.pokemons.water;

import ru.ifmo.se.pokemon.Type;
import ru.ifmo.se.pokemon.Pokemon;
import app.moves.special.Thunder;
import app.moves.status.Confide;
import app.moves.special.ChargeBeam;
import app.moves.special.DreamEater;

final public class Bruxish extends Pokemon {
    public Bruxish(String name) {
        super(name, 100);
        this.setType(Type.WATER, Type.PSYCHIC);

        this.setStats(68, 105, 70, 70, 70, 92);
        this.setMove(new Thunder(), new Confide(), new ChargeBeam(), new DreamEater());
    }
}

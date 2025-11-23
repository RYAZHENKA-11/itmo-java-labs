package app.pokemons.rock;

import app.moves.status.SwordsDance;

final public class Kabutops extends Kabuto {
    public Kabutops(String name) {
        super(name);
        this.setStats(60, 115, 105, 65, 70, 80);
        this.addMove(new SwordsDance());
    }
}

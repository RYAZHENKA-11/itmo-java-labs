package app.pokemons.grass;

import app.moves.status.DoubleTeam;

final public class Tsareena extends Steenee {
    public Tsareena(String name) {
        super(name);
        this.setStats(72, 120, 98, 50, 98, 72);
        this.addMove(new DoubleTeam());
    }
}

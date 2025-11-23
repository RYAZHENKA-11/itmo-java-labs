package app.pokemons.grass;

import app.moves.status.PlayNice;

public class Steenee extends Bounsweet {
    public Steenee(String name) {
        super(name);
        this.setStats(52, 40, 48, 40, 48, 62);
        this.addMove(new PlayNice());
    }
}

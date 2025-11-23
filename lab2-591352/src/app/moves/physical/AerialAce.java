package app.moves.physical;

import ru.ifmo.se.pokemon.PhysicalMove;
import ru.ifmo.se.pokemon.Type;

public final class AerialAce extends PhysicalMove {
    public AerialAce() {
        super(Type.FLYING, 60, Double.POSITIVE_INFINITY);
    }

    @Override
    protected String describe() {
        return "использует Aerial Ace";
    }
}

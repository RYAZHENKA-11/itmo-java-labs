package app.moves.special;

import ru.ifmo.se.pokemon.SpecialMove;
import ru.ifmo.se.pokemon.Type;

final public class MagicalLeaf extends SpecialMove {
    public MagicalLeaf() {
        super(Type.GRASS, 60, Double.POSITIVE_INFINITY);
    }

    @Override
    protected String describe() {
        return "использует Magical Leaf";
    }
}

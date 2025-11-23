package app.moves.physical;

import ru.ifmo.se.pokemon.PhysicalMove;
import ru.ifmo.se.pokemon.Type;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Status;

final public class Facade extends PhysicalMove {
    public Facade() {
        super(Type.NORMAL, 70, 100);
    }

    @Override
    protected void applyOppDamage(Pokemon p, double dmg) {
        if (p.getCondition() == Status.BURN || p.getCondition() == Status.POISON || p.getCondition() == Status.PARALYZE)
            super.applyOppDamage(p, dmg * 2);
        else
            super.applyOppDamage(p, dmg);
    }

    @Override
    protected String describe() {
        return "использует Facade";
    }
}

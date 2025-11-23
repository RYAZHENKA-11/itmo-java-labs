package app.moves.special;

import ru.ifmo.se.pokemon.SpecialMove;
import ru.ifmo.se.pokemon.Type;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Effect;
import ru.ifmo.se.pokemon.Status;

final public class Thunder extends SpecialMove {
    public Thunder() {
        super(Type.ELECTRIC, 110, 70);
    }

    @Override
    protected void applyOppEffects(Pokemon p) {
        Effect paralyzing_effect = new Effect();
        paralyzing_effect.chance(0.3);
        paralyzing_effect.condition(Status.PARALYZE);
        p.addEffect(paralyzing_effect);
    }

    @Override
    protected String describe() {
        return "использует Thunder";
    }
}

package app.moves.status;

import ru.ifmo.se.pokemon.StatusMove;
import ru.ifmo.se.pokemon.Type;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Effect;
import ru.ifmo.se.pokemon.Stat;

final public class DoubleTeam extends StatusMove {
    public DoubleTeam() {
        super(Type.NORMAL, 0, 0);
    }

    @Override
    protected void applySelfEffects(Pokemon p) {
        Effect evasion_buff = new Effect();
        evasion_buff.stat(Stat.EVASION, 1);
        p.addEffect(evasion_buff);
    }

    @Override
    protected String describe() {
        return "использует Double Team";
    }
}

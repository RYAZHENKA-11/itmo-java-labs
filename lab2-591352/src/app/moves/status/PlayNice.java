package app.moves.status;

import ru.ifmo.se.pokemon.StatusMove;
import ru.ifmo.se.pokemon.Type;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Effect;
import ru.ifmo.se.pokemon.Stat;

final public class PlayNice extends StatusMove {
    public PlayNice() {
        super(Type.NORMAL, 0, 0);
    }

    @Override
    protected void applyOppEffects(Pokemon p) {
        Effect attack_debuff = new Effect();
        attack_debuff.stat(Stat.ATTACK, -1);
        p.addEffect(attack_debuff);
    }

    @Override
    protected String describe() {
        return "использует Play Nice";
    }
}

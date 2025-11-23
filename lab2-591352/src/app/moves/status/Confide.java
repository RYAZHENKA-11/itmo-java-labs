package app.moves.status;

import ru.ifmo.se.pokemon.StatusMove;
import ru.ifmo.se.pokemon.Type;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Effect;
import ru.ifmo.se.pokemon.Stat;

final public class Confide extends StatusMove {
    public Confide() {
        super(Type.NORMAL, 0, 0);
    }

    @Override
    protected void applyOppEffects(Pokemon p) {
        Effect special_attack_debuff = new Effect();
        special_attack_debuff.stat(Stat.SPECIAL_ATTACK, -1);
        p.addEffect(special_attack_debuff);
    }

    @Override
    protected String describe() {
        return "использует Confide";
    }
}

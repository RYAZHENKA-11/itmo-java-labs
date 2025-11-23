package app.moves.status;

import ru.ifmo.se.pokemon.StatusMove;
import ru.ifmo.se.pokemon.Type;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Effect;
import ru.ifmo.se.pokemon.Stat;

final public class SwordsDance extends StatusMove {
    public SwordsDance() {
        super(Type.NORMAL, 0, 0);
    }

    @Override
    protected void applySelfEffects(Pokemon p) {
        Effect attack_buff = new Effect();
        attack_buff.stat(Stat.ATTACK, 2);
        p.addEffect(attack_buff);
    }

    @Override
    protected String describe() {
        return "использует Swords Dance";
    }
}

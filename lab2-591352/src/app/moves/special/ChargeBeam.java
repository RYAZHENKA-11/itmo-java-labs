package app.moves.special;

import ru.ifmo.se.pokemon.SpecialMove;
import ru.ifmo.se.pokemon.Type;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Effect;
import ru.ifmo.se.pokemon.Stat;

final public class ChargeBeam extends SpecialMove {
    public ChargeBeam() {
        super(Type.ELECTRIC, 50, 90);
    }

    @Override
    protected void applySelfEffects(Pokemon p) {
        Effect special_attack_buff = new Effect();
        special_attack_buff.chance(0.7);
        special_attack_buff.stat(Stat.SPECIAL_ATTACK, 1);
        p.addEffect(special_attack_buff);
    }

    @Override
    protected String describe() {
        return "использует Charge Beam";
    }
}

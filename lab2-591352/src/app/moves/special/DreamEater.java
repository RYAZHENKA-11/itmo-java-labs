package app.moves.special;

import ru.ifmo.se.pokemon.SpecialMove;
import ru.ifmo.se.pokemon.Type;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Status;
import ru.ifmo.se.pokemon.Effect;
import ru.ifmo.se.pokemon.Stat;

final public class DreamEater extends SpecialMove {
    public DreamEater() {
        super(Type.PSYCHIC, 100, 100);
    }

    private double heal;

    @Override
    protected void applyOppDamage(Pokemon p, double dmg) {
        if (p.getCondition() == Status.SLEEP) {
            super.applyOppDamage(p, dmg);
            heal = dmg / 2;
        } else
            heal = 0;
    }

    @Override
    protected void applySelfEffects(Pokemon p) {
        Effect hp_buff = new Effect();
        hp_buff.stat(Stat.HP, (int) (p.getHP() + heal));
        p.addEffect(hp_buff);
    }

    @Override
    protected String describe() {
        return "использует Dream Eater";
    }
}

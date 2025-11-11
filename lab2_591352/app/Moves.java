package app;

import ru.ifmo.se.pokemon.Status;
import ru.ifmo.se.pokemon.Effect;
import ru.ifmo.se.pokemon.PhysicalMove;
import ru.ifmo.se.pokemon.SpecialMove;
import ru.ifmo.se.pokemon.Stat;
import ru.ifmo.se.pokemon.StatusMove;
import ru.ifmo.se.pokemon.Type;
import ru.ifmo.se.pokemon.Pokemon;

final class Thunder extends SpecialMove {
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

final class Confide extends StatusMove {
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

final class ChargeBeam extends SpecialMove {
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

final class DreamEater extends SpecialMove {
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

final class AerialAce extends PhysicalMove {
    public AerialAce() {
        super(Type.FLYING, 60, Double.POSITIVE_INFINITY);
    }

    @Override
    protected String describe() {
        return "использует Aerial Ace";
    }
}

final class Facade extends PhysicalMove {
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

final class SwordsDance extends StatusMove {
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

final class MagicalLeaf extends SpecialMove {
    public MagicalLeaf() {
        super(Type.GRASS, 60, Double.POSITIVE_INFINITY);
    }

    @Override
    protected String describe() {
        return "использует Magical Leaf";
    }
}

final class PlayNice extends StatusMove {
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

final class DoubleTeam extends StatusMove {
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
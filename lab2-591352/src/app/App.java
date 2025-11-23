package app;

import ru.ifmo.se.pokemon.Battle;
import app.pokemons.water.Bruxish;
import app.pokemons.rock.Kabuto;
import app.pokemons.rock.Kabutops;
import app.pokemons.grass.Bounsweet;
import app.pokemons.grass.Steenee;
import app.pokemons.grass.Tsareena;

public class App {
    public static void main(String[] args) {
        Battle b = new Battle();
        Bruxish p1 = new Bruxish("ChatGPT");
        Kabuto p2 = new Kabuto("Deepseek");
        Kabutops p3 = new Kabutops("Gemini");
        Bounsweet p4 = new Bounsweet("Grok");
        Steenee p5 = new Steenee("Claude");
        Tsareena p6 = new Tsareena("GigaChat");
        b.addAlly(p1);
        b.addAlly(p2);
        b.addAlly(p3);
        b.addFoe(p4);
        b.addFoe(p5);
        b.addFoe(p6);
        b.go();
    }
}

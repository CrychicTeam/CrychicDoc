package noppes.npcs.entity.data;

import java.util.Random;

public class DataPeople {

    private static final Random r = new Random();

    private static final DataPeople.Person[] people = new DataPeople.Person[] { new DataPeople.Person("Noppes", "Creator", "customnpcs:textures/entity/importantpeople/noppes.png"), new DataPeople.Person("Dati", "Patreon", "customnpcs:textures/entity/importantpeople/dati.png"), new DataPeople.Person("Animekin", "Patreon", "customnpcs:textures/entity/importantpeople/animekin.png"), new DataPeople.Person("Vin0m", "Patreon", ""), new DataPeople.Person("Birb", "Patreon", ""), new DataPeople.Person("Flashback", "Patreon", ""), new DataPeople.Person("Ronan", "Patreon", ""), new DataPeople.Person("Shivaxi ", "Patreon", ""), new DataPeople.Person("GreatOrator", "Patreon", ""), new DataPeople.Person("Aphmau", "Patreon", ""), new DataPeople.Person("Kithoras", "Patreon", ""), new DataPeople.Person("Daniel N", "Patreon", ""), new DataPeople.Person("G1RCraft", "Patreon", ""), new DataPeople.Person("Joanie H", "Patreon", ""), new DataPeople.Person("Jaffra", "Patreon", ""), new DataPeople.Person("Orphie", "Patreon", ""), new DataPeople.Person("PPap", "Patreon", ""), new DataPeople.Person("RED9936", "Patreon", ""), new DataPeople.Person("NekoTune", "Patreon", ""), new DataPeople.Person("JusCallMeNico", "Patreon", "") };

    public static DataPeople.Person get() {
        return people[r.nextInt(people.length)];
    }

    static class Person {

        public final String name;

        public final String title;

        public final String skin;

        public Person(String name, String title, String skin) {
            this.name = name;
            this.title = title;
            this.skin = skin;
        }
    }
}
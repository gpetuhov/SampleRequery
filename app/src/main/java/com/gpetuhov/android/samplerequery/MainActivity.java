package com.gpetuhov.android.samplerequery;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.gpetuhov.android.samplerequery.requery.Person;
import com.gpetuhov.android.samplerequery.requery.Pet;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.reactivex.Observable;
import io.requery.Persistable;
import io.requery.reactivex.ReactiveEntityStore;
import io.requery.reactivex.ReactiveResult;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    public static final String LOG_TAG = MainActivity.class.getName();

    private ReactiveEntityStore<Persistable> data;

    private TextView textView;

    Observable<ReactiveResult<Pet>> queryResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.screen_text);

        Timber.tag(LOG_TAG);

        // Get data source from the application instance
        data = ((SampleRequeryApp) getApplication()).getData();

        // Query for number of persons and pets in the database
        int personCount = data.count(Person.class).get().value();
        int petCount = data.count(Pet.class).get().value();
        Timber.d("Number of persons in DB = " + personCount);
        Timber.d("Number of pets in DB = " + petCount);

        // Here we query in ordinary style, but we may also query in reactive style like this:
//        data.count(Pet.class).get().single()
//                .subscribe(
//                        Integer -> {
//                            Timber.d("Number of pets in DB = " + Integer);
//                        }
//                );

        // If no persons, fill the tables with dummy data
        if (0 == personCount) {
            Timber.d("Filling tables with dummy data");
            fillTables()
                    .subscribe(
                            persons -> {
                                // This will run, when fillTables() completes
                                // Query for all pets in the database
                                Timber.d("Filling complete. Listing all pets:");
                                List<Pet> pets = data.select(Pet.class).get().toList();
                                printPets(pets);
                            }
                    );
        }

        // Query for all pets with age greater than 3
        // Such query will NOT be updated on data change in the table!
        List<Pet> pets = data.select(Pet.class).where(Pet.AGE.gt(3)).get().toList();
        Timber.d("Pets with age > 3");
        printPets(pets);

        // Add new pet into database
        addRandomPet()
                .subscribe(
                        pet -> {
                            // This will run, when new pet is added.
                            // This will NOT print new pet, because ordinary query is not updated.
                            printPets(pets);
                        }
                );
    }

    private void printPets(List<Pet> pets) {
        for (Pet pet : pets) {
            printPet(pet);
        }
    }

    private void printPet(Pet pet) {
        Timber.d(pet.getType() + ", " + pet.getName() + ", " + pet.getAge());
    }

    private Observable<Pet> addRandomPet() {
        Random random = new Random();
        int name = random.nextInt(1000);

        Pet pet = new Pet();
        pet.setType("dog");
        pet.setName("Name" + name);
        pet.setAge(5);

        Timber.d("Adding pet:");
        printPet(pet);
        return data.insert(pet).toObservable();
    }

    // Fills in database tables with dummy data.
    // Returns Observable, so that we could be notified, when the operation completes.
    private Observable<Iterable<Person>> fillTables() {
        // Create pets
        Pet dog = new Pet();
        dog.setType("dog");
        dog.setName("Butch");
        dog.setAge(5);

        Pet cat = new Pet();
        cat.setType("cat");
        cat.setName("Bob");
        cat.setAge(3);

        Pet cow = new Pet();
        cow.setType("cow");
        cow.setName("Milka");
        cow.setAge(7);

        // New empty list of persons
        List<Person> persons = new ArrayList<>();

        // Create persons and add them to list
        Person mike = new Person();
        mike.setName("Mike");
        mike.getPets().add(dog);
        mike.getPets().add(cow);
        persons.add(mike);

        Person jane = new Person();
        jane.setName("Jane");
        jane.getPets().add(cat);
        persons.add(jane);

        // Insert list of persons into database
        // (pets, associated with persons will be inserted also)
        return data.insert(persons).toObservable();
    }
}
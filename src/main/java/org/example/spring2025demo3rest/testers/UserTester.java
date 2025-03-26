package org.example.spring2025demo3rest.testers;

import org.example.spring2025demo3rest.pojos.Auto;
import org.example.spring2025demo3rest.pojos.Home;
import org.example.spring2025demo3rest.pojos.User;

import java.time.LocalDate;

/**
 * A simple tester class for manually verifying the creation and basic usage of
 * {@link User}, {@link Home}, and {@link Auto} objects.
 *
 * This class demonstrates:
 * - Object instantiation
 * - Field setting via setters
 * - Entity relationships (Home and Auto associated with a User)
 *
 * Note: This is not a database test. No repository or persistence layer is involved.
 */
public class UserTester {

    public static void main(String[] args) {
        System.out.println("=== Basic Object Creation Test ===");

        //create an instantiation and tester for User object
        User user = new User();
        user.setName("Test Testerson");
        user.setEmail("tester@example.com");

        System.out.printf("User created -> Name: %s, Email: %s\n", user.getName(), user.getEmail());

        //create an instantiation and tester for Home object
        Home home = new Home();
        home.setValue(250000);
        home.setDateBuilt(LocalDate.of(2015, 6, 15));
        home.setUser(user); // Link user

        System.out.printf("Home created -> Value: $%d, Date Built: %s, Linked User: %s\n",
                home.getValue(), home.getDateBuilt(), home.getUser().getName());

        //create an instantiation and tester for Auto object
        Auto auto = new Auto();
        auto.setMake("Honda");
        auto.setModel("Civic");
        auto.setYear(2020);
        auto.setUser(user); // Link user

        System.out.printf("Auto created -> Make: %s, Model: %s, Year: %d, Linked User: %s\n",
                auto.getMake(), auto.getModel(), auto.getYear(), auto.getUser().getName());

        System.out.println("=== Done ===");
    }
}


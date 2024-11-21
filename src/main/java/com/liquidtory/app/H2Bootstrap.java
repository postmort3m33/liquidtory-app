package com.liquidtory.app;

import com.liquidtory.app.repository.InventorySubmissionRepository;
import com.liquidtory.app.repository.LiquorBottleRepository;
import com.liquidtory.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class H2Bootstrap implements CommandLineRunner {

    // User Repo
    @Autowired
    UserRepository userRepository;

    // Liquor Repo
    @Autowired
    LiquorBottleRepository liquorBottleRepository;

    // Inventory Submissions
    @Autowired
    InventorySubmissionRepository inventorySubmissionRepository;

    @Override
    public void run(String... args) throws Exception {

        /*

        ///////////////
        // Add Users //
        ///////////////

        // Create Password Encrypter
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

        ///////////////
        // Add Admin //
        ///////////////

        // User 1
        String encodedPassword1 = encoder.encode("admin");

        // Create and Add user
        UserEntity user1 = new UserEntity("Admin", "Account","admin", encodedPassword1, "ADMIN");

        // Save to Repo
        userRepository.save(user1);

        ////////////////
        // Add User 1 //
        ////////////////

        // User 1
        String encodedPassword2 = encoder.encode("password");

        // Create and Add user
        UserEntity user2 = new UserEntity("John", "Smith","jsmith", encodedPassword2, "USER");

        // Save to Repo
        userRepository.save(user2);

        ////////////////
        // Add User 2 //
        ////////////////

        // User 1
        String encodedPassword3 = encoder.encode("password");

        // Create and Add user
        UserEntity user3 = new UserEntity("Jane", "Doe","jdoe", encodedPassword2, "USER");

        // Save to Repo
        userRepository.save(user3);

        //////////////////////////////////////
        // Add Pre-defined Liquor Bottles.. //
        //////////////////////////////////////

        // Liquor Bottle List
        List<LiquorBottle> liquorBottles = new ArrayList<>();

        liquorBottles.add(new LiquorBottle("Jameson Orange", 750L));
        liquorBottles.add(new LiquorBottle("Jameson Irish Whiskey", 750L));
        liquorBottles.add(new LiquorBottle("Malibu Original", 750L));
        liquorBottles.add(new LiquorBottle("Malibu Black", 750L));
        liquorBottles.add(new LiquorBottle("Malibu Pineapple", 750L));
        liquorBottles.add(new LiquorBottle("Bacardi Superior", 750L));
        liquorBottles.add(new LiquorBottle("Bacardi Gold", 750L));
        liquorBottles.add(new LiquorBottle("Bacardi Black", 750L));
        liquorBottles.add(new LiquorBottle("Absolut Original", 750L));
        liquorBottles.add(new LiquorBottle("Absolut Citron", 750L));
        liquorBottles.add(new LiquorBottle("Absolut Lime", 750L));
        liquorBottles.add(new LiquorBottle("SKYY Original", 750L));
        liquorBottles.add(new LiquorBottle("Grey Goose Original", 750L));
        liquorBottles.add(new LiquorBottle("Tito's Handmade", 750L));
        liquorBottles.add(new LiquorBottle("Jack Daniels Old No 7", 750L));
        liquorBottles.add(new LiquorBottle("Jack Daniels Tennessee Honey", 750L));
        liquorBottles.add(new LiquorBottle("Jack Daniels Gentleman Jack", 750L));
        liquorBottles.add(new LiquorBottle("Jack Daniels Black Label", 750L));
        liquorBottles.add(new LiquorBottle("Jim Beam Original", 750L));
        liquorBottles.add(new LiquorBottle("Jim Beam Black", 750L));
        liquorBottles.add(new LiquorBottle("Jim Beam Honey", 750L));
        liquorBottles.add(new LiquorBottle("Jim Beam Devil's Cut", 750L));
        liquorBottles.add(new LiquorBottle("Maker's Mark Original", 750L));
        liquorBottles.add(new LiquorBottle("Maker's Mark 46", 750L));
        liquorBottles.add(new LiquorBottle("Crown Royal Original", 750L));
        liquorBottles.add(new LiquorBottle("Crown Royal Deluxe", 750L));
        liquorBottles.add(new LiquorBottle("Crown Royal Regal Apple", 750L));
        liquorBottles.add(new LiquorBottle("Crown Royal Vanilla", 750L));
        liquorBottles.add(new LiquorBottle("Crown Royal Black", 750L));
        liquorBottles.add(new LiquorBottle("Wild Turkey 101", 750L));
        liquorBottles.add(new LiquorBottle("Wild Turkey Rare Breed", 750L));
        liquorBottles.add(new LiquorBottle("Woodford Reserve Bourbon", 750L));
        liquorBottles.add(new LiquorBottle("Woodford Reserve Double Oaked", 750L));
        liquorBottles.add(new LiquorBottle("Johhnie Walker Black Label", 750L));
        liquorBottles.add(new LiquorBottle("Johnnie Walker Red Label", 750L));
        liquorBottles.add(new LiquorBottle("Macallan 12 Year Old Sherry Oak", 750L));
        liquorBottles.add(new LiquorBottle("Glenfiddich 12 Year Old", 750L));

        // Save to Repo
        liquorBottleRepository.saveAll(liquorBottles);

        // Make an Initial Submission
        InventorySubmission submission = new InventorySubmission(LocalDateTime.now(), 1L, Collections.emptyList());
        inventorySubmissionRepository.save(submission);

        */

    }
}

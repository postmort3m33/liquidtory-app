package com.liquidtory.app;

import com.liquidtory.app.entity.BarEntity;
import com.liquidtory.app.entity.InventorySubmission;
import com.liquidtory.app.entity.LiquorBottle;
import com.liquidtory.app.entity.UserEntity;
import com.liquidtory.app.repository.BarRepository;
import com.liquidtory.app.repository.InventorySubmissionRepository;
import com.liquidtory.app.repository.LiquorBottleRepository;
import com.liquidtory.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    // Bar Repo
    @Autowired
    BarRepository barRepository;

    // Other Vars
    private boolean runBootstrap = false;

    @Override
    public void run(String... args) throws Exception {

        // Running bootstrap?
        if (runBootstrap) {

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

            liquorBottles.add(new LiquorBottle("Jameson Orange", 1000L));
            liquorBottles.add(new LiquorBottle("Jameson Irish Whiskey", 1000L));
            liquorBottles.add(new LiquorBottle("Malibu Original", 1000L));
            liquorBottles.add(new LiquorBottle("Malibu Black", 1000L));
            liquorBottles.add(new LiquorBottle("Malibu Pineapple", 1000L));
            liquorBottles.add(new LiquorBottle("Bacardi Superior", 1000L));
            liquorBottles.add(new LiquorBottle("Bacardi Gold", 1000L));
            liquorBottles.add(new LiquorBottle("Bacardi Black", 1000L));
            liquorBottles.add(new LiquorBottle("Absolut Original", 1000L));
            liquorBottles.add(new LiquorBottle("Absolut Citron", 1000L));
            liquorBottles.add(new LiquorBottle("Absolut Lime", 1000L));
            liquorBottles.add(new LiquorBottle("SKYY Original", 1000L));
            liquorBottles.add(new LiquorBottle("Grey Goose Original", 1000L));
            liquorBottles.add(new LiquorBottle("Tito's Handmade", 1000L));
            liquorBottles.add(new LiquorBottle("Jack Daniels Old No 7", 1000L));
            liquorBottles.add(new LiquorBottle("Jack Daniels Tennessee Honey", 1000L));
            liquorBottles.add(new LiquorBottle("Jack Daniels Gentleman Jack", 1000L));
            liquorBottles.add(new LiquorBottle("Jack Daniels Black Label", 1000L));
            liquorBottles.add(new LiquorBottle("Jim Beam Original", 1000L));
            liquorBottles.add(new LiquorBottle("Jim Beam Black", 1000L));
            liquorBottles.add(new LiquorBottle("Jim Beam Honey", 1000L));
            liquorBottles.add(new LiquorBottle("Jim Beam Devil's Cut", 1000L));
            liquorBottles.add(new LiquorBottle("Maker's Mark Original", 1000L));
            liquorBottles.add(new LiquorBottle("Maker's Mark 46", 1000L));
            liquorBottles.add(new LiquorBottle("Crown Royal Original", 1000L));
            liquorBottles.add(new LiquorBottle("Crown Royal Deluxe", 1000L));
            liquorBottles.add(new LiquorBottle("Crown Royal Regal Apple", 1000L));
            liquorBottles.add(new LiquorBottle("Crown Royal Vanilla", 1000L));
            liquorBottles.add(new LiquorBottle("Crown Royal Black", 1000L));
            liquorBottles.add(new LiquorBottle("Wild Turkey 101", 1000L));
            liquorBottles.add(new LiquorBottle("Wild Turkey Rare Breed", 1000L));
            liquorBottles.add(new LiquorBottle("Woodford Reserve Bourbon", 1000L));
            liquorBottles.add(new LiquorBottle("Woodford Reserve Double Oaked", 1000L));
            liquorBottles.add(new LiquorBottle("Johhnie Walker Black Label", 1000L));
            liquorBottles.add(new LiquorBottle("Johnnie Walker Red Label", 1000L));
            liquorBottles.add(new LiquorBottle("Macallan 12 Year Old Sherry Oak", 1000L));
            liquorBottles.add(new LiquorBottle("Glenfiddich 12 Year Old", 1000L));

            // Save to Repo
            liquorBottleRepository.saveAll(liquorBottles);

            /////////////////////
            // Create the Bars //
            /////////////////////

            // Inside
            BarEntity insideBar = new BarEntity("Inside");
            barRepository.save(insideBar);

            // Outside
            BarEntity outsideBar = new BarEntity("Outside");
            barRepository.save(outsideBar);

        }
    }
}

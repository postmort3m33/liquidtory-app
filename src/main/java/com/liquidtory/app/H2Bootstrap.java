package com.liquidtory.app;

import com.liquidtory.app.entity.*;
import com.liquidtory.app.repository.*;
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

    // Company Repo
    @Autowired
    CompanyEntityRepository companyEntityRepository;

    // Other Vars
    private boolean runBootstrap = false;

    @Override
    public void run(String... args) throws Exception {

        // Running bootstrap?
        if (runBootstrap) {

            //////////////////////
            // Add Root Company //
            //////////////////////

            CompanyEntity rootCompany = new CompanyEntity("Liquidtory", "N/A", "jtouchstone6141@yahoo.com", "409-434-2933", "N/A");

            // Save it
            companyEntityRepository.save(rootCompany);

            ///////////////
            // Add Users //
            ///////////////

            // Create Password Encrypter
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

            //////////////////////
            // Add Root Account //
            //////////////////////

            // User 1
            String encodedPassword1 = encoder.encode("M0dels13");

            // Create and Add user
            UserEntity user1 = new UserEntity("Joshua", "Touchstone","admin", encodedPassword1, rootCompany, "ROOT");

            // Save to Repo
            userRepository.save(user1);

            ////////////////////////
            // Add Aspens Company //
            ////////////////////////

            CompanyEntity aspensCompany = new CompanyEntity("Aspens Bar & Grill", "817 Clear Lake Rd, Kemah, TX 77565", "N/A", "409-771-7833", "281-549-6384");

            // Save
            companyEntityRepository.save(aspensCompany);

            // Add Company Admin
            String encodedPassword2 = encoder.encode("password");
            UserEntity companyAdmin = new UserEntity("Vincent", "Pandanell","vpandanell", encodedPassword2, aspensCompany, "ADMIN");

            // Save User
            userRepository.save(companyAdmin);

            //////////////////////////////////////
            // Add Pre-defined Liquor Bottles.. //
            //////////////////////////////////////

            // Jack Daniels 750ML
            LiquorBottle jackDaniel750 = new LiquorBottle("Jack Daniels Old No 7", 750L, 24.77, 8.00);
            List<LiquorBottleDimension> dimensions = new ArrayList<>();
            dimensions.add(new LiquorBottleDimension(0.00,100.00, jackDaniel750));
            dimensions.add(new LiquorBottleDimension(64.7,100.00, jackDaniel750));
            dimensions.add(new LiquorBottleDimension(73.00,45.10, jackDaniel750));
            dimensions.add(new LiquorBottleDimension(100.00,34.36, jackDaniel750));
            jackDaniel750.setDimensions(dimensions);
            liquorBottleRepository.save(jackDaniel750);


            /*
            // Liquor Bottle List
            List<LiquorBottle> liquorBottles = new ArrayList<>();
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
            liquorBottleRepository.saveAll(liquorBottles);
            */
        }
    }
}

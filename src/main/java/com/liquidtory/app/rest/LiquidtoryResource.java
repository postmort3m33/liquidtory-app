package com.liquidtory.app.rest;

import com.liquidtory.app.dto.*;
import com.liquidtory.app.entity.*;
import com.liquidtory.app.repository.*;
import com.liquidtory.app.security.JwtUtil;
import com.liquidtory.app.security.MyUserDetailsService;
import com.liquidtory.app.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class LiquidtoryResource {

    // Repos
    @Autowired
    UserRepository userRepository;

    // Bottles
    @Autowired
    LiquorBottleRepository liquorBottleRepository;

    // Inventory Items
    @Autowired
    LiquorBottleItemRepository liquorBottleItemRepository;

    // Inventory Submissions
    @Autowired
    InventorySubmissionRepository inventorySubmissionRepository;

    // Bar Repo
    @Autowired
    BarRepository barRepository;

    // Security Stuff
    @Autowired
    AuthenticationManager authManager;

    @Autowired
    MyUserDetailsService myUserDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    EmailService emailService;

    ////////////
    // Login  //
    ////////////

    // Authenticate
    @RequestMapping(path = "/authenticate", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> authenticateLogin(@RequestBody AuthenticationRequest authRequest) {

        // Convert username to lowercase
        String lowercaseUsername = authRequest.getUsername().toLowerCase();

        // authorize the credentials
        try {
            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(lowercaseUsername, authRequest.getPassword())
            );
        } catch (BadCredentialsException e) {

            // Debug
            System.out.println("Bad Credentials");

            // Empty Auth Response
            AuthenticationResponse emptyResponse = new AuthenticationResponse("");

            // Send Empty AuthResponse
            return new ResponseEntity<>(emptyResponse, HttpStatus.UNAUTHORIZED);
        }

        // Generate Token..
        final UserDetails userDetails = myUserDetailsService.loadUserByUsername(lowercaseUsername);
        final String jwt = jwtUtil.generateToken(userDetails);

        // Create authResponse
        AuthenticationResponse authResponse = new AuthenticationResponse(jwt);

        // Return it.
        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }

    // Get User Info
    @RequestMapping(path = "/user", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserInfoResponse> getUserInfo(@RequestHeader("Authorization") String currentToken) {

        /////////////////////////
        // Get User From Token //
        /////////////////////////

        // Extract token..
        String extractedToken = currentToken.substring(7);

        // Extract username from token
        String username = jwtUtil.extractUsername(extractedToken);

        // Return null if none is found
        if (username == null) {
            return null;
        }

        // Get User Entity
        UserEntity userEntity = userRepository.findByUsername(username);

        // Create UserInfoREsponse
        UserInfoResponse userInfoResponse = new UserInfoResponse(userEntity.getFirstName(), userEntity.getLastName());

        // Return
        return new ResponseEntity<>(userInfoResponse, HttpStatus.OK);
    }

    ////////////////////
    // Liquor Bottles //
    ////////////////////

    // Get all Liquor Bottles
    @RequestMapping(path = "/liquor", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<LiquorBottleResponse>> getAllLiquorBottles(@RequestHeader("Authorization") String currentToken) {

        ///////////////////////////////////////////
        // Make Sure this is Admin in the future //
        ///////////////////////////////////////////

        // Get all Bottles
        List<LiquorBottle> liquorBottles = liquorBottleRepository.findAll();

        // Stream to Dtos
        List<LiquorBottleResponse> liquorBottleResponses = liquorBottles.stream()
                .map(bottle -> new LiquorBottleResponse(
                        bottle.getId(),
                        bottle.getName(),
                        bottle.getCapacityML()
                )).toList();

        // Return them
        return new ResponseEntity<>(liquorBottleResponses, HttpStatus.OK);
    }

    // Create New Liquor Bottle
    @RequestMapping(path = "/liquor", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createNewLiquorBottle(@RequestBody LiquorBottleRequest liquorBottleRequest) {

        // Create New Liquor Bottle..
        LiquorBottle liquorBottle = new LiquorBottle(liquorBottleRequest.getName(), liquorBottleRequest.getCapacityML());

        // Save it..
        liquorBottleRepository.save(liquorBottle);

        // Return it.
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /////////////////////////
    // Liquor Bottle Items //
    /////////////////////////

    // Get all Liquor Bottles Items
    @RequestMapping(path = "/inventory/liquor", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<LiquorBottleItemDto>> getAllLiquorBottleItems(@RequestHeader("Authorization") String currentToken) {

        ///////////////////////////////////////////
        // Make Sure this is Admin in the future //
        ///////////////////////////////////////////

        // Get all Bottles
        List<LiquorBottleItem> liquorBottleItems = liquorBottleItemRepository.findAll();

        // Check if the list is null or empty, return an empty list if so
        if (liquorBottleItems.isEmpty()) {
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);
        }

        // Stream to Dtos
        List<LiquorBottleItemDto> liquorBottleItemResponses = liquorBottleItems.stream()
                .map(item -> new LiquorBottleItemDto(
                        item.getLiquorBottle().getId(),
                        item.getCurrentML(),
                        item.getBar().getId()
                )).toList();

        // Return them
        return new ResponseEntity<>(liquorBottleItemResponses, HttpStatus.OK);
    }

    ///////////////////////////
    // Invenbtory Submission //
    ///////////////////////////

    // Inventory Submission Snapshot
    @RequestMapping(path = "/inventory/submit", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<InventorySubmissionResponse> submitInventory(
            @RequestHeader("Authorization") String currentToken,
            @RequestBody InventorySubmissionRequest inventorySubmissionRequest) {

        /////////////////////////
        // Get User From Token //
        /////////////////////////

        // Extract token..
        String extractedToken = currentToken.substring(7);

        // Extract username from token
        String username = jwtUtil.extractUsername(extractedToken);

        // Return null if none is found
        if (username == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // Get User Entity
        UserEntity userEntity = userRepository.findByUsername(username);

        /////////////////
        // Get the Bar //
        /////////////////

        // the Bar
        BarEntity bar;

        // Determine which Bar..
        if (inventorySubmissionRequest.getIsOutside()) {

            // Set outside
            bar = barRepository.findByName("Outside");

        } else {

            // Set inside
            bar = barRepository.findByName("Inside");
        }

        //////////////////////
        // Update Inventory //
        //////////////////////

        // Reset Inventory for this Bar..
        bar.removeLiquorBottleItems();

        // Create New List..
        List<LiquorBottleItem> liquorBottleItems = new ArrayList<>();

        // Turn into Liquor Bottles.
        for (LiquorBottleItemDto dto: inventorySubmissionRequest.getLiquorBottleItemsToSubmit()) {

            // Find the bottle
            Optional<LiquorBottle> liquorBottleOpt = liquorBottleRepository.findById(dto.getLiquorBottleId());

            // If presetn..
            if (liquorBottleOpt.isPresent()) {

                // Create Real bottle
                LiquorBottle liquorBottle = liquorBottleOpt.get();

                // Create new one to add.
                LiquorBottleItem newItem = new LiquorBottleItem(liquorBottle, dto.getCurrentML());

                // Add to List
                liquorBottleItems.add(newItem);

            } else {

                // Leave
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }

        // Add all Liquor Bottle Items to Bar..
        for (LiquorBottleItem item: liquorBottleItems) {

            // Add to Bar
            bar.addLiquorBottleItem(item);
        }

        ////////////////////////////////
        // Create Inventory Submisson //
        ////////////////////////////////

        // List of Inventory Snapshots
        List<InventorySnapshot> inventorySnapshots = liquorBottleItems.stream()
                .map(bottleItem -> new InventorySnapshot(
                        bottleItem.getLiquorBottle().getId(),
                        bottleItem.getCurrentML()
                )).toList();

        // Create New Submission with US Central Time Zone
        ZoneId usCentralZone = ZoneId.of("America/Chicago");
        LocalDateTime currentCentralTime = LocalDateTime.now(usCentralZone);

        // Create New Submission
        InventorySubmission submission = new InventorySubmission(currentCentralTime, userEntity.getId(), bar, inventorySnapshots);

        // Save it
        inventorySubmissionRepository.save(submission);

        // Add this Submission to Last for this Bar
        bar.setLastSubmissionId(submission.getId());

        // Save it
        barRepository.save(bar);

        /////////////////////////////////////
        // Send back an Inventory Response //
        /////////////////////////////////////

        // Formatter
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd hh:mm a");

        // Formatted Date
        String formattedDate = submission.getTimestamp().format(formatter);

        // Now send it back
        InventorySubmissionResponse response = new InventorySubmissionResponse(
                userEntity.getFirstName(),
                userEntity.getLastName(),
                bar.getId(),
                formattedDate
        );

        // Return
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    // Get Last Submission
    @RequestMapping(path = "/inventory/submit/last", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<InventorySubmissionResponse>> getLastInventorySubmission(
            @RequestHeader("Authorization") String currentToken) {

        ///////////////////////////////////////////
        // Make sure this is Admin in the future //
        ///////////////////////////////////////////

        // Init All Last Submissions..
        List<InventorySubmission> allLastSubmissions = new ArrayList<>();

        // Get all the Bars..
        List<BarEntity> allBars = barRepository.findAll();

        // Leave if Empty..
        if (allBars.isEmpty()) { return new ResponseEntity<>(HttpStatus.BAD_REQUEST); }

        // Loop through Bars..
        for (BarEntity bar: allBars) {

            // Get last Submission Id
            Long lastSubmissionId = bar.getLastSubmissionId();

            // Skip if lastSubmissionId is null
            if (lastSubmissionId == null) {
                continue;
            }

            // Find submission
            Optional<InventorySubmission> inventorySubmissionOpt = inventorySubmissionRepository.findById(lastSubmissionId);

            // If found..
            if (inventorySubmissionOpt.isPresent()) {

                // Get Real one..
                InventorySubmission inventorySubmission = inventorySubmissionOpt.get();

                // Add to List.
                allLastSubmissions.add(inventorySubmission);

            } else {

                // Bad
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }

        /////////////////////////////////
        // Set Inventory Sub Responses //
        /////////////////////////////////

        // make List
        List<InventorySubmissionResponse> inventoryResponses = new ArrayList<>();

        for (InventorySubmission submission: allLastSubmissions) {

            // Get User
            Optional<UserEntity> userOpt = userRepository.findById(submission.getUserId());

            // If present
            if (userOpt.isPresent()) {

                // Get User
                UserEntity user = userOpt.get();

                // Formatter
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd hh:mm a");

                // Formated Date
                String formattedDate = submission.getTimestamp().format(formatter);

                // Make new Response
                InventorySubmissionResponse inventoryResponse = new InventorySubmissionResponse(user.getFirstName(), user.getLastName(), submission.getBar().getId(), formattedDate);

                // Add to List
                inventoryResponses.add(inventoryResponse);

            } else {

                continue;
            }
        }

        // Now return it..
        return new ResponseEntity<>(inventoryResponses, HttpStatus.OK);

    }
}
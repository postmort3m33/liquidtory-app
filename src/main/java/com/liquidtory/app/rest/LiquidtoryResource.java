package com.liquidtory.app.rest;

import com.liquidtory.app.dto.*;
import com.liquidtory.app.entity.*;
import com.liquidtory.app.repository.*;
import com.liquidtory.app.security.JwtUtil;
import com.liquidtory.app.security.MyUserDetailsService;
import com.liquidtory.app.services.EmailService;
import com.liquidtory.app.constants.LiquorConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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

    // Admin Action Repo
    @Autowired
    AdminInventoryActionRepository adminInventoryActionRepository;

    // Company Repo
    @Autowired
    CompanyEntityRepository companyEntityRepository;

    // Security Stuff
    @Autowired
    AuthenticationManager authManager;

    @Autowired
    MyUserDetailsService myUserDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    EmailService emailService;

    //////////////////////
    // Company Endpoint //
    //////////////////////

    // Create New Company
    @RequestMapping(path = "/company", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createCompany(
            @RequestHeader("Authorization") String currentToken,
            @RequestBody CompanyEntityRequest companyEntityRequest) {

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

        // If not Admin leave..
        if (!userEntity.getRole().equalsIgnoreCase("ROOT")) {

            // LEave
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        ///////////////////////////////////////////
        // Does this Company Name already Exist? //
        ///////////////////////////////////////////

        if (companyEntityRepository.existsByName(companyEntityRequest.getName())) {

            // Return bad response
            return new ResponseEntity<>(HttpStatus.CONFLICT);

        }

        ////////////////////////////
        // Create the new Company //
        ////////////////////////////

        // New
        CompanyEntity newCompany = new CompanyEntity(
                companyEntityRequest.getName(),
                companyEntityRequest.getAddress(),
                companyEntityRequest.getEmail(),
                companyEntityRequest.getOwnerPhone(),
                companyEntityRequest.getBusinessPhone()
        );

        // Save It
        companyEntityRepository.save(newCompany);

        // Return OK
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    // Get all Companies
    @RequestMapping(path = "/company", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CompanyEntityResponse>> getAllCompanies(@RequestHeader("Authorization") String currentToken) {

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

        // If not Admin leave..
        if (!userEntity.getRole().equalsIgnoreCase("ROOT")) {

            // LEave
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        // List of Companies
        List<CompanyEntity> allCompanies = companyEntityRepository.findAll();

        // List of Responses
        List<CompanyEntityResponse> companyEntityResponses = allCompanies.stream()
                .map(company -> new CompanyEntityResponse(
                        company.getId(),
                        company.getName()
                )).toList();

        // Return the List
        return new ResponseEntity<>(companyEntityResponses, HttpStatus.OK);
    }

    // Get all Company users
    @RequestMapping(path = "/company/users", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CompanyUserResponse>> getAllCompanyUsers(@RequestHeader("Authorization") String currentToken) {

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

        ///////////////////////////////////////////////
        // Get Company and Create Array OF Responses //
        ///////////////////////////////////////////////

        // The Company
        CompanyEntity thisCompany = userEntity.getCompany();

        // Get all Users
        List<UserEntity> companyUsers = thisCompany.getUsers();

        // Stream Responses
        List<CompanyUserResponse> companyUserResponses = companyUsers.stream()
                .map(user -> new CompanyUserResponse(
                        user.getId(),
                        user.getFirstName(),
                        user.getLastName(),
                        user.getUsername(),
                        user.getRole()
                )).toList();

        // Return the List
        return new ResponseEntity<>(companyUserResponses, HttpStatus.OK);
    }

    //////////////////
    // Bar Endpoint //
    //////////////////

    // Create New Bar
    @RequestMapping(path = "/bar", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createBar(
            @RequestHeader("Authorization") String currentToken,
            @RequestBody BarEntityRequest barEntityRequest) {

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

        // If not Admin leave..
        if (userEntity.getRole().equalsIgnoreCase("USER")) {

            // LEave
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        // Get Company
        CompanyEntity thisCompany = userEntity.getCompany();

        ///////////////////////////////////////////
        // Check if this bar name already exists //
        ///////////////////////////////////////////

        // Get all Bars
        List<BarEntity> allBars = thisCompany.getBars();

        // Loop through them..
        for (BarEntity bar: allBars) {

            // If exits
            if (bar.getName().equalsIgnoreCase(barEntityRequest.getName())) {

                // Leave
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
        }

        ////////////////////////
        // Create the new Bar //
        ////////////////////////

        // New
        BarEntity newBar = new BarEntity(
                barEntityRequest.getName(),
                thisCompany
        );

        // Save It
        barRepository.save(newBar);

        // Return OK
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    // Get Bars for this Company
    @RequestMapping(path = "/bar", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<BarEntityResponse>> getAllBarsForCompany(@RequestHeader("Authorization") String currentToken) {

        // Global Function Vars
        String userRole;

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

        // Set role..
        userRole = userEntity.getRole();

        ////////////////////////////////////
        // Loop Bars and Create Responses //
        ////////////////////////////////////

        // Get Company
        CompanyEntity thisCompany = userEntity.getCompany();

        // Get all Bars
        List<BarEntity> allBars = thisCompany.getBars();

        // Responses
        List<BarEntityResponse> barEntityResponses = new ArrayList<>();

        // The Loop
        for (BarEntity bar: allBars) {

            // Vars
            InventorySubmission lastSubmission = null;
            LastInventorySubmissionResponse lastSubmissionResponse = null;
            List<LiquorBottleItemDto> liquorBottleItemResponses = new ArrayList<>();

            /////////////////////////
            // Get Last Submission //
            /////////////////////////

            // Get Last submission Id
            Long lastSubmissionId = bar.getLastSubmissionId();

            // If its null, skip this one..
            if (!(lastSubmissionId == null) && !userRole.equalsIgnoreCase("USER")) {

                // Search for by ID
                Optional<InventorySubmission> lastSubmissionOpt = inventorySubmissionRepository.findById(bar.getLastSubmissionId());

                // If Found
                if (lastSubmissionOpt.isPresent()) {

                    // Real one
                    lastSubmission = lastSubmissionOpt.get();
                }

                // If a submission was found..
                if (!(lastSubmission == null)) {

                    // Formatter
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd hh:mm a");

                    // Formated Date
                    String formattedDate = lastSubmission.getTimestamp().format(formatter);

                    // Convert to Response
                    lastSubmissionResponse = new LastInventorySubmissionResponse(
                            lastSubmission.getFirstName(),
                            lastSubmission.getLastName(),
                            bar.getId(),
                            formattedDate
                    );
                }

            }

            /////////////////////////////
            // Get Liquor Bottle Items //
            /////////////////////////////

            // If this is not a user..
            if (!userRole.equalsIgnoreCase("USER")) {

                List<LiquorBottleItem> liquorBottleItems = bar.getLiquorBottleItems();

                // Stream to Dtos
                liquorBottleItemResponses = liquorBottleItems.stream()
                        .map(item -> new LiquorBottleItemDto(
                                item.getLiquorBottle().getId(),
                                item.getCurrentML(),
                                item.getBar().getId()
                        )).toList();
            }

            ///////////////////////////
            // Now Make Bar Response //
            ///////////////////////////

            // New Response
            BarEntityResponse newResponse = new BarEntityResponse(bar.getId(), bar.getName(), lastSubmissionResponse, liquorBottleItemResponses);

            // Add it
            barEntityResponses.add(newResponse);
        }

        // Return the List
        return new ResponseEntity<>(barEntityResponses, HttpStatus.OK);
    }

    ////////////////
    // Login/User //
    ////////////////

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
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // Get User Entity
        UserEntity userEntity = userRepository.findByUsername(username);

        // Create UserInfoREsponse
        UserInfoResponse userInfoResponse = new UserInfoResponse(userEntity.getFirstName(), userEntity.getLastName(), userEntity.getCompany().getName());

        // Return
        return new ResponseEntity<>(userInfoResponse, HttpStatus.OK);
    }

    // Create New User
    @RequestMapping(path = "/user", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createUser(
            @RequestHeader("Authorization") String currentToken,
            @RequestBody UserInfoRequest userInfoRequest) {

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

        // If not Admin or ROOT leave..
        if (userEntity.getRole().equalsIgnoreCase("USER")) {

            // LEave
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        ////////////////////////////
        // Get the Company Entity //
        ////////////////////////////

        //Var
        CompanyEntity companyEntity = null;

        // Is there a company Id?
        if (userInfoRequest.getCompanyId() == null) {

            // This User creation came from a Company admin
            // Company is gotten from the Users Entity from the token
            companyEntity = userEntity.getCompany();

        } else {

            // This User creation came from the ROOT account
            // Get Company from company ID

            Optional<CompanyEntity> companyEntityOpt = companyEntityRepository.findById(userInfoRequest.getCompanyId());

            // If found
            if (companyEntityOpt.isPresent()) {

                // Get actual
                companyEntity = companyEntityOpt.get();
            }
        }

        // Did we get something?
        if (companyEntity == null) {

            // Leave
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        ////////////////////////////////////////
        // Check if username is already taken //
        ////////////////////////////////////////

        if (userRepository.existsByUsername(userInfoRequest.getUsername())) {

            // Return bad response
            return new ResponseEntity<>(HttpStatus.CONFLICT);

        }

        /////////////////////////
        // Create the new User //
        /////////////////////////

        // Create password encoder
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

        // Encrypt password
        String encodedPassword = encoder.encode(userInfoRequest.getPassword());

        // New User
        UserEntity newUser = new UserEntity(
                userInfoRequest.getFirstName(),
                userInfoRequest.getLastName(),
                userInfoRequest.getUsername(),
                encodedPassword,
                companyEntity,
                userInfoRequest.getRole()
        );

        // Save it
        userRepository.save(newUser);

        // Return OK
        return new ResponseEntity<>(HttpStatus.CREATED);

    }

    // Change User Password
    @RequestMapping(path = "/user/changepassword", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> changeUserPassword(
            @RequestHeader("Authorization") String currentToken,
            @RequestBody UserChangePasswordRequest userChangePasswordRequest) {

        ////////////////////////////////
        // Make sure this is an ADMIN //
        ////////////////////////////////

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

        // If not Admin leave..
        if (!userEntity.getRole().equalsIgnoreCase("ADMIN")) {

            // LEave
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        ///////////////////
        // FUTURE UPDATE //
        // Make sure this userId belongs to the user requesting the changes Company //
        //////////////////////////////////////////////////////////////////////////////

        ///////////
        // Logic //
        ///////////

        // Get user from UserID
        Optional<UserEntity> userToUpdateOpt = userRepository.findById(userChangePasswordRequest.getUserId());

        // If found
        if (userToUpdateOpt.isPresent()) {

            // Get it..
            UserEntity userToUpdate = userToUpdateOpt.get();

            // Create password encoder
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

            // Encrypt password
            String encodedPassword = encoder.encode(userChangePasswordRequest.getPassword1());

            // Update the User Info.
            userToUpdate.setPassword(encodedPassword);

            // Save it
            userRepository.save(userToUpdate);

        } else {

            // Bad
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // Return
        return new ResponseEntity<>(HttpStatus.OK);
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
                        bottle.getCapacityML(),
                        bottle.getHeightCM(),
                        bottle.getDiameterBottomCM(),
                        bottle.getDimensions().stream()
                                .map(dimension -> new LiquorBottleDimensionDto(
                                        dimension.getHeight(),
                                        dimension.getRadius()
                                )).toList()
                )).toList();

        // Return them
        return new ResponseEntity<>(liquorBottleResponses, HttpStatus.OK);
    }

    // Create New Liquor Bottle
    @RequestMapping(path = "/liquor", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createNewLiquorBottle(
            @RequestHeader("Authorization") String currentToken,
            @RequestBody LiquorBottleRequest liquorBottleRequest) {

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

        // If not Admin or ROOT leave..
        if (!userEntity.getRole().equalsIgnoreCase("ROOT")) {

            // LEave
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        //////////////////////////////
        // Does this already Exist? //
        //////////////////////////////

        if (liquorBottleRepository.existsByNameAndCapacityML(liquorBottleRequest.getName(), liquorBottleRequest.getCapacityML())) {

            // Return Conflict
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        //////////////////////////////
        // Create New Liquor Bottle //
        //////////////////////////////

        // new One
        LiquorBottle liquorBottle;

        // Was this a Simple bottle?
        if (liquorBottleRequest.getIsSimple()) {

            // New Simple Bottle..
            liquorBottle = new LiquorBottle(liquorBottleRequest.getName(), liquorBottleRequest.getCapacityML());

        } else {

            // Create New Liquor Bottle..
            liquorBottle = new LiquorBottle(
                    liquorBottleRequest.getName(),
                    liquorBottleRequest.getCapacityML(),
                    liquorBottleRequest.getHeightCM(),
                    liquorBottleRequest.getDiameterBottomCM()
            );

            // Stream Dimension Dtos to Dimension Entities
            List<LiquorBottleDimension> dimensionEntities = liquorBottleRequest.getDimensions().stream()
                    .map(dto -> new LiquorBottleDimension(
                            dto.getHeight(),
                            dto.getRadius(),
                            liquorBottle
                    )).toList();

            // Set dimensions on Liquor Bottle
            liquorBottle.setDimensions(dimensionEntities);

        }

        // Save it..
        liquorBottleRepository.save(liquorBottle);

        // Return it.
        return new ResponseEntity<>(HttpStatus.CREATED);

    }

    ///////////////////////////
    // Inventory Submissions //
    ///////////////////////////

    // Inventory Submission Snapshot
    @RequestMapping(path = "/inventory/submit", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LastInventorySubmissionResponse> submitInventory(
            @RequestHeader("Authorization") String currentToken,
            @RequestBody InventorySubmissionRequest inventorySubmissionRequest) {

        //////////////////
        // Current Time //
        /////////////////

        ZoneId usCentralZone = ZoneId.of("America/Chicago");
        LocalDateTime currentCentralTime = LocalDateTime.now(usCentralZone);

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

        // The bar to get
        BarEntity bar;

        // the Bar
        Optional<BarEntity> barOpt = barRepository.findById(inventorySubmissionRequest.getBarId());

        // If found..
        if (barOpt.isPresent()) {

            // Get real bar
            bar = barOpt.get();

        } else {

            // Leave
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        }

        /////////////////////////////////////////////////////////
        // Calculate How Many Shots used since Last Submission //
        /////////////////////////////////////////////////////////

        // Vars to use
        Long currentBarMls = 0L;
        Long thisSubmissionTotalMls = 0L;

        // Get the current Ml's at the bar
        for (LiquorBottleItem barItem: bar.getLiquorBottleItems()) {

            // Add to it
            currentBarMls += barItem.getCurrentML();

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

                // Shot Calculation
                thisSubmissionTotalMls += dto.getCurrentML();

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

        ///////////////////////////////
        // Finalize Shot Calculation //
        ///////////////////////////////

        // Debugging..
        Long totalShotsUsed = Math.round((double)(currentBarMls - thisSubmissionTotalMls) / LiquorConstants.mlPerShot);

        /////////////////////////////////
        // Create Inventory Submission //
        /////////////////////////////////

        // List of Inventory Snapshots
        List<InventorySnapshot> inventorySnapshots = liquorBottleItems.stream()
                .map(bottleItem -> new InventorySnapshot(
                        bottleItem.getLiquorBottle().getId(),
                        bottleItem.getCurrentML()
                )).toList();

        // Create New Submission
        InventorySubmission submission = new InventorySubmission(currentCentralTime, userEntity.getFirstName(), userEntity.getLastName(), bar, totalShotsUsed, inventorySnapshots);

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
        LastInventorySubmissionResponse response = new LastInventorySubmissionResponse(
                userEntity.getFirstName(),
                userEntity.getLastName(),
                bar.getId(),
                formattedDate
        );

        // Return
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Get all Inventory Submissions..
    @RequestMapping(path = "/inventory/submit", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<InventorySubmissionResponse>> getInventorySubmissionsByDate(
            @RequestHeader("Authorization") String currentToken,
            @RequestParam(value = "from")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate from,
            @RequestParam(value = "to")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate to) {

        ///////////////////////////////////////////
        // Make sure this is Admin in the future //
        ///////////////////////////////////////////

        // Convert LocalDate to LocalDateTime for range comparison
        LocalDateTime fromDateTime = from.atStartOfDay(); // Start of 'from' day
        LocalDateTime toDateTime = to.atTime(LocalTime.MAX); // End of 'to' day

        // Get Submissions within Dat Range
        List<InventorySubmission> allSubmissions = inventorySubmissionRepository.findAllByTimestampBetween(fromDateTime, toDateTime);

        /////////////////////////////////
        // Set Inventory Sub Responses //
        /////////////////////////////////

        // make List
        List<InventorySubmissionResponse> inventoryResponses = new ArrayList<>();

        // Loop to create Responses
        for (InventorySubmission submission: allSubmissions) {

            //////////////////////
            // Stream Snapshots //
            //////////////////////

            // New List
            List<InventorySnapshotDto> snapshotDtos = new ArrayList<>();

            //////////////////////////////////////////////////////////////////////////////////////
            // This controls whether Inventory SnapShots are included in the Excel Export Sheet //
            //////////////////////////////////////////////////////////////////////////////////////

            /*

            // Loop
            for (InventorySnapshot snapshot: submission.getInventorySnapshots()) {

                // GEt Liquor Bottle
                Optional<LiquorBottle> liquorBottleOpt = liquorBottleRepository.findById(snapshot.getLiquorBottleId());

                // If
                if (liquorBottleOpt.isPresent()) {

                    // Get iut
                    LiquorBottle liquorBottle = liquorBottleOpt.get();

                    // Add new One
                    snapshotDtos.add(new InventorySnapshotDto(liquorBottle.getName(), snapshot.getCurrentML()));
                } else {

                    continue;
                }
            }

            */

            ////////////////////////////////
            // Finish Submission Response //
            ////////////////////////////////

            // Formatter
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd hh:mm a");

            // Formated Date
            String formattedDate = submission.getTimestamp().format(formatter);

            // Make new Response
            InventorySubmissionResponse inventoryResponse = new InventorySubmissionResponse(
                    submission.getId(),
                    submission.getFirstName(),
                    submission.getLastName(),
                    submission.getBar().getName(),
                    formattedDate,
                    submission.getNumShotsUsed(),
                    snapshotDtos
            );

            // Add to List
            inventoryResponses.add(inventoryResponse);

        }

        // Now return it..
        return new ResponseEntity<>(inventoryResponses, HttpStatus.OK);

    }

    // Get Admin Action Submissions within Date Range..
    @RequestMapping(path = "/inventory/admin", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AdminActionResponse>> getAdminSubmissionsByDate(
            @RequestHeader("Authorization") String currentToken,
            @RequestParam(value = "from")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate from,
            @RequestParam(value = "to")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate to) {

        ///////////////////////////////////////////
        // Make sure this is Admin in the future //
        ///////////////////////////////////////////

        // Convert LocalDate to LocalDateTime for range comparison
        LocalDateTime fromDateTime = from.atStartOfDay(); // Start of 'from' day
        LocalDateTime toDateTime = to.atTime(LocalTime.MAX); // End of 'to' day

        // Get Submissions within Dat Range
        List<AdminInventoryAction> allAdminSubmissions = adminInventoryActionRepository.findAllByTimestampBetween(fromDateTime, toDateTime);

        /////////////////////////////////
        // Set Inventory Sub Responses //
        /////////////////////////////////

        // make List
        List<AdminActionResponse> adminActionResponses = new ArrayList<>();

        // Loop to create Responses
        for (AdminInventoryAction action: allAdminSubmissions) {

            // If not successful, skip..
            //if (!action.getSuccessful()) { continue; }

            ////////////////////////////////
            // Finish Submission Response //
            ////////////////////////////////

            // Formatter
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd hh:mm a");

            // Formatted Date
            String formattedDate = action.getTimestamp().format(formatter);

            // Get the Liquor Bottle
            Optional<LiquorBottle> liquorBottleOpt = liquorBottleRepository.findById(action.getLiquorBottleId());

            // If found
            if (liquorBottleOpt.isPresent()) {

                // Get Real One..
                LiquorBottle liquorBottle = liquorBottleOpt.get();

                // Make Description String
                String bottleDesc = liquorBottle.getName() + " " + action.getAmountMls() + "/" + liquorBottle.getCapacityML().toString() + "mL";

                // Make new Response
                AdminActionResponse adminActionResponse = new AdminActionResponse(
                        action.getId(),
                        action.getFirstName(),
                        action.getLastName(),
                        action.getBar().getName(),
                        formattedDate,
                        action.getActionType(),
                        bottleDesc,
                        action.getNotes()
                );

                // Add to List
                adminActionResponses.add(adminActionResponse);

            } else {

                // Bad Request
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }

        // Now return it..
        return new ResponseEntity<>(adminActionResponses, HttpStatus.OK);

    }

    ///////////////////
    // Admin Actions //
    ///////////////////
    // Add Admin Inventory Action
    @RequestMapping(path = "/inventory/admin", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> submitAdminAction(
            @RequestHeader("Authorization") String currentToken,
            @RequestBody AdminActionRequest adminActionRequest) {

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

        // If not Admin leave..
        if (!userEntity.getRole().equalsIgnoreCase("ADMIN")) {

            // LEave
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        /////////////
        // Get Bar //
        /////////////

        // The Bar
        BarEntity bar;

        // Find by Id
        Optional<BarEntity> barOpt = barRepository.findById(adminActionRequest.getBarId());

        // If Bar is found..
        if (barOpt.isPresent()) {

            // Actual
            bar = barOpt.get();

        } else {

            // Leave
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        /////////////////////////////////
        // Perform Action on Inventory //
        /////////////////////////////////

        // List of Admin Actions to Submit..
        List<AdminInventoryAction> pendingActions = new ArrayList<>();

        // Was it successful
        Boolean success = false;

        // Finds Liquor Bottle
        Optional<LiquorBottle> liquorBottleOpt = liquorBottleRepository.findById(adminActionRequest.getLiquorBottleId());

        // If found
        if (liquorBottleOpt.isPresent()) {

            // Real one
            LiquorBottle liquorBottle = liquorBottleOpt.get();

            // If full..
            if (adminActionRequest.getFullOrPartial().equalsIgnoreCase("FULL")) {

                // Loop by numbottles..
                for (int i = 0; i < adminActionRequest.getNumFullBottles(); i++) {

                    // New Full Bottle
                    LiquorBottleItem liquorBottleItem = new LiquorBottleItem(liquorBottle, liquorBottle.getCapacityML());

                    // If add
                    if (adminActionRequest.getActionType().equalsIgnoreCase("ADD_BOTTLE")) {

                        // Add this to the bar..
                        success = bar.addLiquorBottleItem(liquorBottleItem);

                    } else if (adminActionRequest.getActionType().equalsIgnoreCase("REMOVE_BOTTLE")) {

                        // Add this to the bar..
                        success = bar.removeLiquorBottleItem(liquorBottleItem);
                    }

                    /////////////////////////////
                    // Create New Admin Action //
                    /////////////////////////////

                    // Save a new Admin Action
                    AdminInventoryAction adminInventoryAction = new AdminInventoryAction(
                            LocalDateTime.now(ZoneId.of("America/Chicago")),
                            adminActionRequest.getActionType(),
                            adminActionRequest.getLiquorBottleId(),
                            liquorBottleItem.getCurrentML(),
                            userEntity.getFirstName(),
                            userEntity.getLastName(),
                            bar,
                            adminActionRequest.getNotes(),
                            success
                    );

                    // Add to pending action..
                    pendingActions.add(adminInventoryAction);

                    // If it failed abandon loop..
                    if (!success) {

                        break;

                    }
                }

            } else if (adminActionRequest.getFullOrPartial().equalsIgnoreCase("PARTIAL")) {

                // New Partial Bottle
                LiquorBottleItem liquorBottleItem = new LiquorBottleItem(
                        liquorBottle,
                        (long) (liquorBottle.getCapacityML() * (adminActionRequest.getPartialAmount() / 100.0))
                );

                // If add
                if (adminActionRequest.getActionType().equalsIgnoreCase("ADD_BOTTLE")) {

                    // Add this to the bar..
                    success = bar.addLiquorBottleItem(liquorBottleItem);

                } else if (adminActionRequest.getActionType().equalsIgnoreCase("REMOVE_BOTTLE")) {

                    // Add this to the bar..
                    success = bar.removeLiquorBottleItem(liquorBottleItem);
                }

                /////////////////////////////
                // Create New Admin Action //
                /////////////////////////////

                // Save a new Admin Action
                AdminInventoryAction adminInventoryAction = new AdminInventoryAction(
                        LocalDateTime.now(ZoneId.of("America/Chicago")),
                        adminActionRequest.getActionType(),
                        adminActionRequest.getLiquorBottleId(),
                        liquorBottleItem.getCurrentML(),
                        userEntity.getFirstName(),
                        userEntity.getLastName(),
                        bar,
                        adminActionRequest.getNotes(),
                        success
                );

                // Add to pending action..
                pendingActions.add(adminInventoryAction);

            }
        } else {

            // Bottle Does not exist for whatever reason
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // Save All Actions
        adminInventoryActionRepository.saveAll(pendingActions);

        // Only save if all operations were successful
        if (success) {

            // Save Bar
            barRepository.save(bar);

            // Good return
            return new ResponseEntity<>(HttpStatus.OK);

        } else {

            // Bad
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
package org.example.spring2025demo3rest.controllers;

import org.example.spring2025demo3rest.dataaccess.AutoRepository;
import org.example.spring2025demo3rest.dataaccess.HomeRepository;
import org.example.spring2025demo3rest.dataaccess.UserRepository;
import org.example.spring2025demo3rest.pojos.Auto;
import org.example.spring2025demo3rest.pojos.Home;
import org.example.spring2025demo3rest.pojos.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Main REST controller for managing Users, Homes, and Autos.
 * Provides endpoints for full CRUD operations for each entity.
 */
@Controller
@RequestMapping(path = RESTNouns.VERSION_1)
public class MainController {

    //Wire the ORM
    @Autowired private UserRepository userRepository;
    @Autowired private HomeRepository homeRepository;
    @Autowired private AutoRepository autoRepository;

    /***************************************************************** USERS ************************************************************************************/

    /**
     * Get all users in the system.
     * @return Iterable of all users
     */
    @GetMapping(path = RESTNouns.USER)
    public @ResponseBody Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Get a specific user by ID.
     * @param userId the ID of the user
     * @return Optional user if found
     */
    @GetMapping(path = RESTNouns.USER + RESTNouns.ID)
    public @ResponseBody Optional<User> getUser(@PathVariable("id") Long userId) {
        return userRepository.findById(userId);
    }

    /**
     * Create a new user.
     * @param name name of the user
     * @param email email of the user
     * @return created user object
     */
    @PostMapping(path = RESTNouns.USER)
    public @ResponseBody User createUser(
            @RequestParam String name, @RequestParam String email) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        return userRepository.save(user);
    }

    /**
     * Delete a user by ID.
     * @param userId ID of the user to delete
     * @return message indicating success or failure
     */
    @DeleteMapping(path = RESTNouns.USER + RESTNouns.ID)
    public @ResponseBody String deleteUser(@PathVariable("id") Long userId) {
        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
            return "User with ID " + userId + " deleted successfully.";
        } else {
            return "User with ID " + userId + " not found.";
        }
    }

    /**
     * Update a user's name and email by ID.
     * @param userId ID of the user
     * @param name new name
     * @param email new email
     * @return message indicating result
     */
    @PutMapping(path = RESTNouns.USER + RESTNouns.ID)
    public @ResponseBody String updateUser(
            @PathVariable("id") Long userId, @RequestParam String name, @RequestParam String email){
        if (userRepository.existsById(userId)) {
            Optional<User> user = userRepository.findById(userId);
            if(user.isPresent()){
                user.get().setName(name);
                user.get().setEmail(email);
            }
            userRepository.save(user.get());
            return "User with ID " + userId + " updated successfully.";
        } else {
            return "User with ID " + userId + " not found.";
        }

    }

    /********************************************************************* HOMES ******************************************************************************************/


    /**
     * Get all homes belonging to a specific user.
     * @param userId ID of the user
     * @return list of homes
     */
    @GetMapping(path = RESTNouns.USER +  RESTNouns.ID + RESTNouns.HOME)
    public @ResponseBody Iterable<Home> getAllHomesByUser(@PathVariable("id") Long userId) {
        Iterable<Home> homes = null;
        if (userRepository.existsById(userId)) {
            Optional<User> user = userRepository.findById(userId);
            if(user.isPresent()){
               // homeRepository
                homes = homeRepository.getAllByUserId(userId);
            }
        }
        //TODO handle errors
        return homes;
    }

    /**
     * Get a home by its ID.
     * @param homeId ID of the home
     * @return optional home
     */
    @GetMapping(path = RESTNouns.HOME + RESTNouns.ID)
    public @ResponseBody Optional<Home> getHomeById(@PathVariable("id") Long homeId) {
        return homeRepository.findById(homeId);
    }

    /**
     * Update a home's value and dateBuilt.
     * @param homeId ID of the home
     * @param dateBuilt new date built
     * @param value new value
     * @return message indicating update result
     */
    @PutMapping(path = RESTNouns.HOME + RESTNouns.ID)
    public @ResponseBody String updateHome(
            @PathVariable("id") Long homeId,
            @RequestParam LocalDate dateBuilt,
            @RequestParam int value) {

        Optional<Home> optionalHome = homeRepository.findById(homeId);
        if (optionalHome.isPresent()) {
            Home home = optionalHome.get();
            home.setDateBuilt(dateBuilt);
            home.setValue(value);
            homeRepository.save(home);
            return "Home with ID " + homeId + " updated successfully.";
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Home with ID " + homeId + " not found.");
        }
    }

    /**
     * Delete a home by ID.
     * @param homeId ID of the home
     * @return message indicating result
     */
    @DeleteMapping(path = RESTNouns.HOME + RESTNouns.ID)
    public @ResponseBody String deleteHome(@PathVariable("id") Long homeId) {
        if (homeRepository.existsById(homeId)) {
            homeRepository.deleteById(homeId);
            return "Home with ID " + homeId + " deleted successfully.";
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Home with ID " + homeId + " not found.");
        }
    }



    /**
     * Create a new home for a user.
     * @param userId ID of the user
     * @param dateBuilt construction date
     * @param value home value
     * @return newly created Home object
     */
    @PostMapping(path = RESTNouns.USER + RESTNouns.ID + RESTNouns.HOME)
    public @ResponseBody Home createHomeByUser(
            @PathVariable("id") Long userId,
            @RequestParam LocalDate dateBuilt, @RequestParam int value) {
//
        Home home = null;
        if (userRepository.existsById(userId)) {
            Optional<User> user = userRepository.findById(userId);
            home = new Home();
            home.setValue(value);
            home.setDateBuilt(dateBuilt);
            home.setUser(user.get());
            homeRepository.save(home);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        return home;
    }

    /********************************************************************* AUTOS ******************************************************************************************/

    /**
     * Get all autos belonging to a user.
     * @param userId ID of the user
     * @return list of autos
     */
    @GetMapping(path = RESTNouns.USER + RESTNouns.ID + RESTNouns.AUTO)
    public @ResponseBody Iterable<Auto> getAllAutosByUser(@PathVariable("id") Long userId) {
        if (userRepository.existsById(userId)) {
            return autoRepository.getAllByUserId(userId);
        } else {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
    }

    /**
     * Create a new auto record for a user.
     * @param userId ID of the user
     * @param make vehicle make
     * @param model vehicle model
     * @param year model year
     * @return created Auto object
     */
    @PostMapping(path = RESTNouns.USER + RESTNouns.ID + RESTNouns.AUTO)
    public @ResponseBody Auto createAutoByUser(
            @PathVariable("id") Long userId,
            @RequestParam String make,
            @RequestParam String model,
            @RequestParam Integer year) {

        if (userRepository.existsById(userId)) {
            Optional<User> user = userRepository.findById(userId);
            if (user.isPresent()) {
                Auto auto = new Auto();
                auto.setMake(make);
                auto.setModel(model);
                auto.setYear(year);
                auto.setUser(user.get());
                return autoRepository.save(auto);
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
            }
        }
        return null; // Handle errors better
    }

    /**
     * Get an auto by its ID.
     * @param autoId ID of the auto
     * @return optional auto object
     */
    @GetMapping(path = RESTNouns.AUTO + RESTNouns.ID)
    public @ResponseBody Optional<Auto> getAuto(@PathVariable("id") Long autoId) {
        return autoRepository.findById(autoId);
    }

    /**
     * Update an auto’s details.
     * @param autoId ID of the auto
     * @param make new make
     * @param model new model
     * @param year new year
     * @return result message
     */
    @PutMapping(path = RESTNouns.AUTO + RESTNouns.ID)
    public @ResponseBody String updateAuto(
            @PathVariable("id") Long autoId,
            @RequestParam String make,
            @RequestParam String model,
            @RequestParam Integer year) {

        Optional<Auto> optionalAuto = autoRepository.findById(autoId);
        if (optionalAuto.isPresent()) {
            Auto auto = optionalAuto.get();
            auto.setMake(make);
            auto.setModel(model);
            auto.setYear(year);
            autoRepository.save(auto);
            return "Auto updated successfully.";
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Auto not found");
    }

    /**
     * Delete an auto by ID.
     * @param autoId ID of the auto
     * @return message indicating result
     */
    @DeleteMapping(path = RESTNouns.AUTO + RESTNouns.ID)
    public @ResponseBody String deleteAuto(@PathVariable("id") Long autoId) {
        if (autoRepository.existsById(autoId)) {
            autoRepository.deleteById(autoId);
            return "Auto deleted successfully.";
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Auto not found");
    }

}

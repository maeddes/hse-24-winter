package com.example.backendspringboot;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/shopping")
@CrossOrigin(origins = "*")
public class ShoppingController {

    @Autowired
    private ShoppingItemRepository shoppingItemRepository;

    // our fake persistence backend
    //private List<ShoppingItem> shoppingList = new ArrayList<>();

    public ShoppingController() {
        //shoppingList.add(new ShoppingItem("Apples", 5));
        //shoppingList.add(new ShoppingItem("Bananas", 3));
    }

    @Operation(summary = "Retrieve all shopping items", description = "Fetches all shopping items in the list.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of items.")
    @GetMapping
    public ResponseEntity<List<ShoppingItem>> getAllItems() {
        return ResponseEntity.ok(shoppingItemRepository.findAll());
    }

    @Operation(summary = "Retrieve a shopping item by name", description = "Fetches a shopping item based on the provided name.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Item found and retrieved successfully."),
        @ApiResponse(responseCode = "404", description = "Item not found.")
    })
    @GetMapping("/{name}")
    public ResponseEntity<ShoppingItem> getItemByName(
            @Parameter(description = "Name of the shopping item to retrieve", required = true)
            @PathVariable String name) {
                return shoppingItemRepository.findByName(name)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());

    }

    @Operation(summary = "Add a new shopping item", description = "Adds a new shopping item to the list. Or updates an existing one.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Item successfully created."),
        @ApiResponse(responseCode = "200", description = "Existing item successfully updated.")
    })
    @PostMapping
    public ResponseEntity<ShoppingItem> addItem(
            @Parameter(description = "Shopping item to add", required = true)
            @RequestBody ShoppingItem newItem) {

    // Check if an item with the same name already exists
    Optional<ShoppingItem> existingItemOpt = shoppingItemRepository.findByName(newItem.getName());

        if (existingItemOpt.isPresent()) {
            // If it exists, update the amount
            ShoppingItem existingItem = existingItemOpt.get();
            existingItem.setAmount(existingItem.getAmount() + newItem.getAmount());
            shoppingItemRepository.save(existingItem);
            return ResponseEntity.ok(existingItem);
        } else {
            // If it doesn't exist, save it as a new item
            ShoppingItem savedItem = shoppingItemRepository.save(newItem);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedItem);
        }
    }

    @Operation(summary = "Update an existing shopping item", description = "Updates the amount of an existing item.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Item updated successfully."),
        @ApiResponse(responseCode = "404", description = "Item not found.")
    })
    @PutMapping("/{name}")
    public ResponseEntity<ShoppingItem> updateItem(
            @Parameter(description = "Name of the item to update", required = true)
            @PathVariable String name,
            @Parameter(description = "Updated item details", required = true)
            @RequestBody ShoppingItem updatedItem) {
                return shoppingItemRepository.findByName(name)
                .map(existingItem -> {
                    existingItem.setAmount(updatedItem.getAmount());
                    shoppingItemRepository.save(existingItem);
                    return ResponseEntity.ok(existingItem);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Delete a shopping item", description = "Removes an item from the list based on the provided name.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Item deleted successfully."),
        @ApiResponse(responseCode = "404", description = "Item not found.")
    })
    @DeleteMapping("/{name}")
    @Transactional
    public ResponseEntity<Void> deleteItem(
            @Parameter(description = "Name of the item to delete", required = true)
            @PathVariable String name) {

                if (shoppingItemRepository.existsByName(name)) {
                    shoppingItemRepository.deleteByName(name);
                    return ResponseEntity.noContent().build();
                } else {
                    return ResponseEntity.notFound().build();
                }

    }
}

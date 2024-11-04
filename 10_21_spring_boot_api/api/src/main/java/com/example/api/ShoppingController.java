package com.example.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/shopping")
@CrossOrigin(origins = "*")
public class ShoppingController {

    private List<ShoppingItem> shoppingList = new ArrayList<>();

    public ShoppingController() {
        shoppingList.add(new ShoppingItem("Apples", 5));
        shoppingList.add(new ShoppingItem("Bananas", 3));
    }

    @Operation(summary = "Retrieve all shopping items", description = "Fetches all shopping items in the list.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of items.")
    @GetMapping
    public ResponseEntity<List<ShoppingItem>> getAllItems() {
        return ResponseEntity.ok(shoppingList);
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
        Optional<ShoppingItem> item = shoppingList.stream()
                .filter(i -> i.getName().equalsIgnoreCase(name))
                .findFirst();

        return item.map(ResponseEntity::ok)
                   .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    @Operation(summary = "Add a new shopping item", description = "Adds a new shopping item to the list.")
    @ApiResponse(responseCode = "201", description = "Item successfully created.")
    @PostMapping
    public ResponseEntity<ShoppingItem> addItem(
            @Parameter(description = "Shopping item to add", required = true)
            @RequestBody ShoppingItem newItem) {
        shoppingList.add(newItem);
        return ResponseEntity.status(HttpStatus.CREATED).body(newItem);
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
        Optional<ShoppingItem> existingItem = shoppingList.stream()
                .filter(i -> i.getName().equalsIgnoreCase(name))
                .findFirst();

        if (existingItem.isPresent()) {
            ShoppingItem item = existingItem.get();
            item.setAmount(updatedItem.getAmount());
            return ResponseEntity.ok(item);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @Operation(summary = "Delete a shopping item", description = "Removes an item from the list based on the provided name.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Item deleted successfully."),
        @ApiResponse(responseCode = "404", description = "Item not found.")
    })
    @DeleteMapping("/{name}")
    public ResponseEntity<Void> deleteItem(
            @Parameter(description = "Name of the item to delete", required = true)
            @PathVariable String name) {
        boolean removed = shoppingList.removeIf(item -> item.getName().equalsIgnoreCase(name));

        if (removed) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}

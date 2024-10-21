package com.example.api;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/shopping")
public class ShoppingController {

    private List<ShoppingItem> shoppingList = new ArrayList<>();

    // Initialize some sample data
    public ShoppingController() {
        shoppingList.add(new ShoppingItem("Apples", 5));
        shoppingList.add(new ShoppingItem("Bananas", 3));
    }

    // GET all shopping items
    @GetMapping
    public List<ShoppingItem> getAllItems() {
        return shoppingList;
    }

    // GET a single shopping item by name
    @GetMapping("/{name}")
    public ShoppingItem getItemByName(@PathVariable String name) {
        return shoppingList.stream()
                .filter(item -> item.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Item not found"));
    }

    // POST (create) a new shopping item
    @PostMapping
    public ShoppingItem addItem(@RequestBody ShoppingItem newItem) {
        shoppingList.add(newItem);
        return newItem;
    }

    // PUT (update) an existing shopping item
    @PutMapping("/{name}")
    public ShoppingItem updateItem(@PathVariable String name, @RequestBody ShoppingItem updatedItem) {
        ShoppingItem existingItem = shoppingList.stream()
                .filter(item -> item.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Item not found"));

        existingItem.setAmount(updatedItem.getAmount());
        return existingItem;
    }

    // DELETE a shopping item
    @DeleteMapping("/{name}")
    public String deleteItem(@PathVariable String name) {
        shoppingList.removeIf(item -> item.getName().equalsIgnoreCase(name));
        return "Item deleted";
    }
}

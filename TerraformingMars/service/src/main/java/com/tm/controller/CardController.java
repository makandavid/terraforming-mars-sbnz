package com.tm.controller;

import com.tm.entity.CardEntity;
import com.tm.service.CardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cards")
@CrossOrigin(origins = "*")
public class CardController {

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @GetMapping
    public List<CardEntity> getAllCards() {
        return cardService.getAllCards();
    }

    @GetMapping("/{name}")
    public ResponseEntity<CardEntity> getCard(@PathVariable String name) {
        return cardService.getAllCards()
                .stream()
                .filter(c -> c.getName().equalsIgnoreCase(name))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}

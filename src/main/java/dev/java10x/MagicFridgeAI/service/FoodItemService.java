package dev.java10x.MagicFridgeAI.service;

import dev.java10x.MagicFridgeAI.model.FoodItem;
import dev.java10x.MagicFridgeAI.repository.FoodItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FoodItemService {

    private FoodItemRepository repository;

    public FoodItemService(FoodItemRepository repository) {
        this.repository = repository;
    }

    public FoodItem salvar(FoodItem foodItem) {
        return repository.save(foodItem);
    }

    public List<FoodItem> listar() {
        return repository.findAll();
    }

    public Optional<FoodItem> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public FoodItem atualizar(FoodItem foodItem) {
        return repository.save(foodItem);
    }

    public void excluir(Long id) {
        repository.deleteById(id);
    }


}

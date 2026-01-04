package com.gocart.service;

import com.gocart.entity.Store;
import com.gocart.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;

    public List<Store> getAllStores() {
        return storeRepository.findAll();
    }

    public Optional<Store> getStoreById(String id) {
        return storeRepository.findById(id);
    }

    public Optional<Store> getStoreByUserId(String userId) {
        return storeRepository.findByUserId(userId);
    }

    public Optional<Store> getStoreByUsername(String username) {
        return storeRepository.findByUsername(username);
    }

    public Store createStore(Store store) {
        if (store.getId() == null || store.getId().isEmpty()) {
            store.setId(UUID.randomUUID().toString());
        }
        if (store.getStatus() == null) {
            store.setStatus("pending");
        }
        if (store.getIsActive() == null) {
            store.setIsActive(false);
        }
        return storeRepository.save(store);
    }

    public Store updateStore(String id, Store storeDetails) {
        return storeRepository.findById(id).map(store -> {
            if (storeDetails.getName() != null) {
                store.setName(storeDetails.getName());
            }
            if (storeDetails.getDescription() != null) {
                store.setDescription(storeDetails.getDescription());
            }
            if (storeDetails.getUsername() != null) {
                store.setUsername(storeDetails.getUsername());
            }
            if (storeDetails.getAddress() != null) {
                store.setAddress(storeDetails.getAddress());
            }
            if (storeDetails.getStatus() != null) {
                store.setStatus(storeDetails.getStatus());
            }
            if (storeDetails.getIsActive() != null) {
                store.setIsActive(storeDetails.getIsActive());
            }
            if (storeDetails.getLogo() != null) {
                store.setLogo(storeDetails.getLogo());
            }
            if (storeDetails.getEmail() != null) {
                store.setEmail(storeDetails.getEmail());
            }
            if (storeDetails.getContact() != null) {
                store.setContact(storeDetails.getContact());
            }
            return storeRepository.save(store);
        }).orElseThrow(() -> new RuntimeException("Store not found"));
    }

    public void deleteStore(String id) {
        storeRepository.deleteById(id);
    }
}

package com.gocart.service;

import com.gocart.entity.Address;
import com.gocart.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository addressRepository;

    public List<Address> getAllAddresses() {
        return addressRepository.findAll();
    }

    public Optional<Address> getAddressById(String id) {
        return addressRepository.findById(id);
    }

    public List<Address> getAddressesByUserId(String userId) {
        return addressRepository.findByUserId(userId);
    }

    public Address createAddress(Address address) {
        if (address.getId() == null || address.getId().isEmpty()) {
            address.setId(UUID.randomUUID().toString());
        }
        return addressRepository.save(address);
    }

    public Address updateAddress(String id, Address addressDetails) {
        return addressRepository.findById(id).map(address -> {
            if (addressDetails.getName() != null) {
                address.setName(addressDetails.getName());
            }
            if (addressDetails.getEmail() != null) {
                address.setEmail(addressDetails.getEmail());
            }
            if (addressDetails.getStreet() != null) {
                address.setStreet(addressDetails.getStreet());
            }
            if (addressDetails.getCity() != null) {
                address.setCity(addressDetails.getCity());
            }
            if (addressDetails.getState() != null) {
                address.setState(addressDetails.getState());
            }
            if (addressDetails.getZip() != null) {
                address.setZip(addressDetails.getZip());
            }
            if (addressDetails.getCountry() != null) {
                address.setCountry(addressDetails.getCountry());
            }
            if (addressDetails.getPhone() != null) {
                address.setPhone(addressDetails.getPhone());
            }
            return addressRepository.save(address);
        }).orElseThrow(() -> new RuntimeException("Address not found"));
    }

    public void deleteAddress(String id) {
        addressRepository.deleteById(id);
    }
}

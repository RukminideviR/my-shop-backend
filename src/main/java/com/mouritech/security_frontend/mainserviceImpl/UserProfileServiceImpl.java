package com.mouritech.security_frontend.mainserviceImpl;

import com.mouritech.security_frontend.dto.UserProfileDTO;
import com.mouritech.security_frontend.entity.User;
import com.mouritech.security_frontend.mainservice.UserProfileService;
import com.mouritech.security_frontend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserProfileServiceImpl implements UserProfileService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserProfileDTO getProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserProfileDTO dto = new UserProfileDTO();
        dto.setFullName(user.getFirstName() + " " + user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getMobileNumber());
        dto.setAddress(user.getAddress());
        dto.setVillage(user.getVillage());
        dto.setDistrict(user.getDistrict());
        dto.setState(user.getState());
        dto.setPincode(user.getPincode());
        return dto;
    }

    @Override
    public UserProfileDTO updateProfile(String email, UserProfileDTO dto) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setFirstName(dto.getFullName().split(" ")[0]);
        user.setLastName(dto.getFullName().split(" ")[1]);
        user.setMobileNumber(dto.getPhone());
        user.setAddress(dto.getAddress());
        user.setVillage(dto.getVillage());
        user.setDistrict(dto.getDistrict());
        user.setState(dto.getState());
        user.setPincode(dto.getPincode());

        userRepository.save(user);
        return dto;
    }
}

package com.cms.cdl.utils;

import com.cms.cdl.dto.user_dto.ExperienceDTO;
import com.cms.cdl.dto.user_dto.UserDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Period;
import java.util.List;

@Component
public class UserOperations {

    @Value("${user.fetch.byUserIdAPI}")
    private String userByUserIdAPI;

    @Value("${user.fetch.getAllUserAPI}")
    private String getAllUserAPI;

    @Value("${user.fetch.byLocationAPI}")
    private String userByLocationAPI;

    public UserDTO getUserByUserId(long userId){
        WebClient webClient = WebClient.create();

        Mono<UserDTO> response = webClient.get()
                .uri(userByUserIdAPI+userId)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<UserDTO>() {});
        UserDTO userDTO = response.block();
        if(userDTO != null){

            // fetching old company experience

        String oldCompaniesExp = calculateOldCompanyExp(userDTO);
        userDTO.setTotalExperience(oldCompaniesExp);

            return userDTO;
        }
        else {
            return null;
        }
    }

    public List<UserDTO> getAllUsers(){
        WebClient webClient = WebClient.create();

        Mono<List<UserDTO>> responseList = webClient.get()
                .uri(getAllUserAPI)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<UserDTO>>() {});

        List<UserDTO> userDTOList = responseList.block();

        if(userDTOList!=null){
            return userDTOList;
        }
        else {
            return null;
        }
    }

    public UserDTO getUserByLocation(String location){
        WebClient webClient = WebClient.create();

        Mono<UserDTO> response = webClient.get()
                .uri(userByUserIdAPI+location)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<UserDTO>() {});

        UserDTO userDTO = response.block();

        if(userDTO!=null){
            return userDTO;
        }
        else {
            return null;
        }
    }

    public String calculateOldCompanyExp(UserDTO userDTO){
        int totalMonths = 0;
        int totalYears = 0;
        String totalExp = "";

        for (ExperienceDTO experience : userDTO.getExperienceDTOS()) {
            Period period = Period.between(experience.getDateOfJoining(), experience.getDateOfReliving());
            totalYears += period.getYears();
            totalMonths += period.getMonths();
        }


        totalYears += totalMonths / 12;
        totalMonths = totalMonths % 12;

        totalExp = totalYears + " Years " + totalMonths + " Months";

        return totalExp;
    }

}

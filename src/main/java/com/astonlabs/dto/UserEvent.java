package com.astonlabs.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEvent {
    private String operation; // тут будет created\deleted!!!
    private String email;


}



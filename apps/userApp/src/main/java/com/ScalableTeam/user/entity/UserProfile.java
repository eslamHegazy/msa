package com.ScalableTeam.user.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class UserProfile {
    @Id
    String userId;
    String email;
    String password;
    String profilePhotoLink;
}

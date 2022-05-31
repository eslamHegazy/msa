package com.ScalableTeam.user.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class UserProfile {
    @Id
    String userId;
    String email;
    String password;
    String profilePhotoLink;
}

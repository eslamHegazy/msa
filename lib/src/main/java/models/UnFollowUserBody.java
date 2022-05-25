package models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UnFollowUserBody {
    private String userID;
    private String requestedUnFollowUserID;
}

package models.reddit;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@NoArgsConstructor
public class AssignModeratorsForm {
    private String adminId;
    private String channelNameId;
    private String moderatorId;

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getChannelNameId() {
        return channelNameId;
    }

    public void setChannelNameId(String channelNameId) {
        this.channelNameId = channelNameId;
    }

    public String getModeratorId() {
        return moderatorId;
    }

    public void setModeratorId(String moderatorId) {
        this.moderatorId = moderatorId;
    }
}

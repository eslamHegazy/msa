package mocks;

import com.ScalableTeam.models.reddit.AssignModeratorsForm;
import com.ScalableTeam.models.reddit.CreateChannelForm;

public class AssignModeratorsFormMock {
    private static String channelNameId = ChannelMock.getChannelNameId();
    private static String adminId = UserMock.getId();
    public static String moderatorId=ModeratorMock.getId();

    public static AssignModeratorsForm getAssignModeratorsForm() {
        return AssignModeratorsForm.builder().channelNameId(channelNameId).adminId(adminId).moderatorId(moderatorId).build();
    }

    public static String getChannelNameId() {
        return channelNameId;
    }

    public static String getAdminId() {
        return adminId;
    }
}

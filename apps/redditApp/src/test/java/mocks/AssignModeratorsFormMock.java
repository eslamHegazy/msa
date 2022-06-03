package mocks;

import com.ScalableTeam.models.reddit.AssignModeratorsForm;

public class AssignModeratorsFormMock {
    public static String moderatorId = ModeratorMock.getId();
    private static String channelNameId = ChannelMock2.getChannelNameId();
    private static String adminId = UserMock.getId();

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

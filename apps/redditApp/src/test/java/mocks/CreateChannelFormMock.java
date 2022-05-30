package mocks;

import com.ScalableTeam.models.reddit.CreateChannelForm;

public class CreateChannelFormMock {
    private static String channelNameId = ChannelMock.getChannelNameId();
    private static String adminId = UserMock.getId();

    public static CreateChannelForm getCreateChannelForm() {
        return CreateChannelForm.builder().channelNameId(channelNameId).adminId(adminId).build();
    }

    public static String getChannelNameId() {
        return channelNameId;
    }

    public static String getAdminId() {
        return adminId;
    }
}

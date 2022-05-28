package models.reddit;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@NoArgsConstructor
public class CreateChannelForm {
    private String adminId;
    private String channelNameId;

//    public CreateChannelForm(String adminId, String channelNameId) {
//        this.adminId = adminId;
//        this.channelNameId = channelNameId;
//    }

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

    @Override
    public String toString(){
        return "Create Channel Form"+"Channel Name: "+ channelNameId +" adminId: "+ adminId;
    }
}

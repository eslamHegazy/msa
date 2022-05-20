package com.ScalableTeam.reddit.app.moderation;

import com.ScalableTeam.reddit.app.requestForms.AssignModeratorsForm;
import com.ScalableTeam.reddit.app.requestForms.BanUserForm;
import com.ScalableTeam.reddit.app.requestForms.CreateChannelForm;
import com.ScalableTeam.reddit.app.requestForms.ViewReportsForm;
import com.ScalableTeam.reddit.config.GeneralConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class ModerationController {

    @Autowired
    private ViewReportsService viewReportsService;

    @Autowired
    private BanUserService banUserService;

    @Autowired
    private GeneralConfig generalConfig;
    @RequestMapping(method = RequestMethod.GET,value="/viewReports")
    private String viewReports(@RequestBody ViewReportsForm viewReportsForm) throws Exception {
        log.info(generalConfig.getCommands().get("viewReports ") + "Controller", viewReportsForm);
        return viewReportsService.execute(viewReportsForm);
    }
    @RequestMapping(method = RequestMethod.PUT,value = "/banUser")
    private String banUser(@RequestBody BanUserForm banUserForm) throws Exception {
        log.info(generalConfig.getCommands().get("banUser") + " Controller ", banUserForm);
        return banUserService.execute(banUserForm);
    }


}

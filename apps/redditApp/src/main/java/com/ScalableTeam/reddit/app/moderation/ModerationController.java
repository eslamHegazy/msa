package com.ScalableTeam.reddit.app.moderation;

import com.ScalableTeam.reddit.app.requestForms.AssignModeratorsForm;
import com.ScalableTeam.reddit.app.requestForms.BanUserForm;
import com.ScalableTeam.reddit.app.requestForms.CreateChannelForm;
import com.ScalableTeam.reddit.app.requestForms.ViewReportsForm;
import com.ScalableTeam.reddit.config.GeneralConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class ModerationController {

    @Autowired
    private ViewReportsService viewReportsService;

    @Autowired
    private BanUserService banUserService;

    @Autowired
    private GeneralConfig generalConfig;
    @RequestMapping(method = RequestMethod.GET,value="/viewReports/{modId}")
    private String viewReports(@PathVariable String modId, @RequestParam String redditId) throws Exception {
        ViewReportsForm viewReportsForm = new ViewReportsForm();
        viewReportsForm.setModId(modId);
        viewReportsForm.setRedditId(redditId);
        log.info(generalConfig.getCommands().get("viewReports ") + "Controller", viewReportsForm);
        return viewReportsService.execute(viewReportsForm);
    }
    @RequestMapping(method = RequestMethod.PUT,value = "/banUser")
    private String banUser(@RequestBody BanUserForm banUserForm) throws Exception {
        log.info(generalConfig.getCommands().get("banUser") + " Controller ", banUserForm);
        return banUserService.execute(banUserForm);
    }


}

package com.ScalableTeam.reddit.app.reportPost;

import com.ScalableTeam.reddit.app.adminServices.AssignModeratorsService;
import com.ScalableTeam.reddit.app.requestForms.CreateChannelForm;
import com.ScalableTeam.reddit.app.requestForms.ReportPostForm;
import com.ScalableTeam.reddit.config.GeneralConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class ReportPostController {

    @Autowired
    private ReportPostService reportPostService;
    @Autowired
    private GeneralConfig generalConfig;

    @RequestMapping(method = RequestMethod.POST,value = "/reportPost")
    private String reportPost(@RequestBody ReportPostForm reportPostForm) throws Exception {
        log.info(generalConfig.getCommands().get("reportPost") + "Controller", reportPostForm);
        return reportPostService.execute(reportPostForm);
    }
}

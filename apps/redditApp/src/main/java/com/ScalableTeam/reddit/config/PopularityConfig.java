package com.ScalableTeam.reddit.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "popular")
@Data
public class PopularityConfig {
    private int postsUpvoteThreshold;
    private int channelFollowersThreshold;
}

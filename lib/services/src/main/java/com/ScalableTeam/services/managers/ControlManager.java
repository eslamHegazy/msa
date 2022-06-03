package com.ScalableTeam.services.managers;

import com.ScalableTeam.utils.ReflectionUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

@Component
@Slf4j
@AllArgsConstructor
public class ControlManager {
    private final ConsumerManager consumerManager;
    private final ClassManager classManager;
    private final List<BaseDatabasePooling> dbManager;

    public void handleControllerMessage(JSONObject message) {
        try {
            String command = message.getString("command");
            String app = message.getString("app");
            log.info("{} has received a message from the controller!\n{}\n", app, message);
            Method method = ReflectionUtils.getMethod(ControlManager.class, command);
            boolean hasArgs = message.has("args") && message.optJSONArray("args") != null;
            System.out.println(hasArgs);
            if (hasArgs) {
                method.invoke(this, message.optJSONArray("args"));
            } else {
                method.invoke(this);
            }
        } catch (JSONException | IllegalAccessException | InvocationTargetException | NullPointerException e) {
            e.printStackTrace();
            log.error("Could not process command sent by controller.");
        }
    }

    public void set_max_thread_count(JSONArray args) {
        try {
            consumerManager.changeMaxThreadCount(args.getInt(0));
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Could not execute setting max threads count.");
        }
    }

    public void set_min_thread_count(JSONArray args) {
        try {
            consumerManager.changeMinThreadCount(args.getInt(0));
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Could not execute setting min threads count.");
        }
    }

    public void set_max_db_connections_count(JSONArray args) {
        try {
            for (BaseDatabasePooling bdp : dbManager) {
                if (bdp.canChangeMaxPoolSize()) {
                    bdp.changeMaxPoolSize(args.getInt(0));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Could not execute setting max db connection pool size.");
        }
    }

    public void set_max_db_connections_timeout(JSONArray args) {
        try {
            for (BaseDatabasePooling bdp : dbManager) {
                if (bdp.canChangeMaxConnectionTimeout()) {
                    bdp.changeMaxConnectionTimeout(args.getLong(0));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Could not execute setting max db connection timeout.");
        }
    }

    public void set_min_idle_db_connections_count(JSONArray args) {
        try {
            for (BaseDatabasePooling bdp : dbManager) {
                if (bdp.canChangeMinIdleConnectionSize()) {
                    bdp.changeMinIdleConnectionSize(args.getInt(0));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Could not execute setting min idle db connection pool size.");
        }
    }

    public void set_max_lifetime_db_connections(JSONArray args) {
        try {
            for (BaseDatabasePooling bdp : dbManager) {
                if (bdp.canChangeMaxLifetime()) {
                    bdp.changeMaxLifetime(args.getLong(0));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Could not execute setting max lifetime for db connections.");
        }
    }

    public void add_command(JSONArray args) {
        try {
            String commandName = args.getString(0);
            String array = args.getString(1);
            byte[] b = org.apache.commons.codec.binary.Base64.decodeBase64(array);
            classManager.addCommand(commandName, b);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Could not add command.");
        }
    }

    public void delete_command(JSONArray args) {
        try {
            String commandName = args.getString(0);
            classManager.deleteCommand(commandName);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Could not add command.");
        }
    }

    public void freeze() {
        try {
            consumerManager.stopAcceptingNewRequests();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Could not freeze app.");
        }
    }

    public void resume() {
        try {
            consumerManager.startAcceptingNewRequests();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Could not resume app.");
        }
    }
}

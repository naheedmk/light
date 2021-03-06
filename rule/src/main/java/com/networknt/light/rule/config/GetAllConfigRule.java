package com.networknt.light.rule.config;

import com.networknt.light.rule.Rule;

import java.util.Map;

/**
 * Created by steve on 2/18/2016.
 *
 * AccessLevel R [owner]
 */
public class GetAllConfigRule extends AbstractConfigRule implements Rule {
    public boolean execute (Object ...objects) throws Exception {
        Map<String, Object> inputMap = (Map<String, Object>) objects[0];
        Map<String, Object> data = (Map<String, Object>) inputMap.get("data");
        String hostConfigs = getAllConfig();
        if(hostConfigs != null) {
            inputMap.put("result", hostConfigs);
            return true;
        } else {
            inputMap.put("result", "No config can be found.");
            inputMap.put("responseCode", 404);
            return false;
        }
    }
}

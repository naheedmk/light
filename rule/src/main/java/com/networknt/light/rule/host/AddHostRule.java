package com.networknt.light.rule.host;

import com.networknt.light.rule.Rule;
import com.networknt.light.rule.host.AbstractHostRule;
import com.networknt.light.util.ServiceLocator;

import java.util.List;
import java.util.Map;

/**
 * Created by Steve Hu on 2015-01-19.
 *
 * This is only give you a way to update virtualhost.json in users directory, it won't
 * inject the new host into the virtualhosthandler. The server must be restarted in order
 * to load the newly added site.
 * TODO dynamically add a new host into virtualhosthandler without shutdonw server.
 */
public class AddHostRule extends AbstractHostRule implements Rule {
    public boolean execute (Object ...objects) throws Exception {
        Map<String, Object> inputMap = (Map<String, Object>)objects[0];
        Map<String, Object> data = (Map<String, Object>)inputMap.get("data");
        Map<String, Object> payload = (Map<String, Object>) inputMap.get("payload");
        String error = null;
        if(payload == null) {
            error = "Login is required";
            inputMap.put("responseCode", 401);
        } else {
            Map<String, Object> user = (Map<String, Object>)payload.get("user");
            List roles = (List)user.get("roles");
            if(!roles.contains("owner")) {
                error = "Role owner is required to add host";
                inputMap.put("responseCode", 403);
            } else {
                // check if the host exists or not.
                Map<String, Object> hostMap = ServiceLocator.getInstance().getHostMap();
                if(hostMap.containsKey(data.get("id"))) {
                    // host exists
                    error = "Id for the host exists";
                    inputMap.put("responseCode", 400);
                } else {
                    // TODO add host into virtualhost.json here in the command or in event?
                    Map eventMap = getEventMap(inputMap);
                    Map<String, Object> eventData = (Map<String, Object>)eventMap.get("data");
                    inputMap.put("eventMap", eventMap);
                    eventData.put("id", data.get("id"));
                    eventData.put("base", data.get("base"));
                    eventData.put("transferMinSize", data.get("transferMinSize"));
                    eventData.put("createDate", new java.util.Date());
                    eventData.put("createUserId", user.get("userId"));
                }
            }
        }
        if(error != null) {
            inputMap.put("error", error);
            return false;
        } else {
            return true;
        }
    }
}
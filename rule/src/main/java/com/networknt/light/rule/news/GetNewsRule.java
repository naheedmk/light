package com.networknt.light.rule.news;

import com.networknt.light.rule.AbstractBfnRule;
import com.networknt.light.rule.Rule;

/**
 * Created by steve on 28/12/14.
 */
public class GetNewsRule extends AbstractBfnRule implements Rule {
    public boolean execute (Object ...objects) throws Exception {
        return getBfn("news", objects);
    }
}
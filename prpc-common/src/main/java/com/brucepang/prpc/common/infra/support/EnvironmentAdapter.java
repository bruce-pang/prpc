/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.brucepang.prpc.common.infra.support;


import com.brucepang.prpc.common.infra.InfraAdapter;
import com.brucepang.prpc.extension.Activate;
import com.brucepang.prpc.extension.inject.ScopeModelAware;
import com.brucepang.prpc.scope.model.ApplicationModel;
import com.brucepang.prpc.util.StrUtil;
import com.brucepang.prpc.config.ConfigurationUtils;

import java.util.HashMap;
import java.util.Map;

import static com.brucepang.prpc.common.constants.CommonConstants.*;


@Activate
public class EnvironmentAdapter implements InfraAdapter, ScopeModelAware {

    private ApplicationModel applicationModel;

    @Override
    public void setApplicationModel(ApplicationModel applicationModel) {
        this.applicationModel = applicationModel;
    }

    /**
     * 1. OS Environment: PRPC_LABELS=tag=pre;key=value
     * 2. JVM Options: -Denv_keys = PRPC_KEY1, PRPC_KEY2
     *
     * @param params information of this PRPC process, currently includes application name and host address.
     */
    @Override
    public Map<String, String> getExtraAttributes(Map<String, String> params) {
        Map<String, String> parameters = new HashMap<>();

        String rawLabels = ConfigurationUtils.getProperty(applicationModel, PRPC_LABELS);
        if (StrUtil.isNotEmpty(rawLabels)) {
            String[] labelPairs = SEMICOLON_SPLIT_PATTERN.split(rawLabels);
            for (String pair : labelPairs) {
                String[] label = EQUAL_SPLIT_PATTERN.split(pair);
                if (label.length == 2) {
                    parameters.put(label[0], label[1]);
                }
            }
        }

        String rawKeys = ConfigurationUtils.getProperty(applicationModel, PRPC_ENV_KEYS);
        if (StrUtil.isNotEmpty(rawKeys)) {
            String[] keys = COMMA_SPLIT_PATTERN.split(rawKeys);
            for (String key : keys) {
                String value = ConfigurationUtils.getProperty(applicationModel, key);
                if (value != null) {
                    // since 3.2
                    parameters.put(key.toLowerCase(), value);
                    // upper-case key kept for compatibility
                    parameters.put(key, value);
                }
            }
        }
        return parameters;
    }

    @Override
    public String getAttribute(String key) {
        return ConfigurationUtils.getProperty(applicationModel, key);
    }
}

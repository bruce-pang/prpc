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

package com.brucepang.prpc;


public interface Constants {

    String BUFFER_KEY = "buffer";

    /**
     * default buffer size is 8k.
     */
    int DEFAULT_BUFFER_SIZE = 8 * 1024;

    int MAX_BUFFER_SIZE = 16 * 1024;

    int MIN_BUFFER_SIZE = 1 * 1024;

    /**
     * max size of channel. default value is zero that means unlimited.
     */
    String ACCEPTS_KEY = "accepts";

    int DEFAULT_ACCEPTS = 0;

    String CONNECT_QUEUE_CAPACITY = "connect.queue.capacity";

    String CONNECT_QUEUE_WARNING_SIZE = "connect.queue.warning.size";

    int DEFAULT_CONNECT_QUEUE_WARNING_SIZE = 1000;

    String CHARSET_KEY = "charset";

    String DEFAULT_CHARSET = "UTF-8";

    String SERVER_KEY = "server";

    String CLIENT_KEY = "client";
}

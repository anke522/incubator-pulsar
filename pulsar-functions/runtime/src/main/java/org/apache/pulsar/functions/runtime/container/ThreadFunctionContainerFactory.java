/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.pulsar.functions.runtime.container;

import org.apache.pulsar.functions.runtime.instance.JavaInstanceConfig;
import org.apache.pulsar.functions.runtime.functioncache.FunctionCacheManager;
import org.apache.pulsar.functions.runtime.functioncache.FunctionCacheManagerImpl;

/**
 * Thread based function container factory implementation.
 */
public class ThreadFunctionContainerFactory implements FunctionContainerFactory {

    private final ThreadGroup threadGroup;
    protected final FunctionCacheManager fnCache;
    private int maxBufferedTuples;

    public ThreadFunctionContainerFactory(int maxBufferedTuples) {
        this.fnCache = new FunctionCacheManagerImpl();
        this.threadGroup = new ThreadGroup(
            "Pulsar Function Container Threads");
        this.maxBufferedTuples = maxBufferedTuples;
    }

    @Override
    public ThreadFunctionContainer createContainer(JavaInstanceConfig instanceConfig, String jarFile) {
        return new ThreadFunctionContainer(
            instanceConfig,
            maxBufferedTuples,
            fnCache,
            threadGroup,
            jarFile);
    }

    @Override
    public void close() {
        threadGroup.interrupt();
        fnCache.close();
    }
}
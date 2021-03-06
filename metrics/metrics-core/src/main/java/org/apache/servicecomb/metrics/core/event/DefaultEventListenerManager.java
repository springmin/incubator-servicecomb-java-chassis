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

package org.apache.servicecomb.metrics.core.event;

import org.apache.servicecomb.foundation.common.event.EventListener;
import org.apache.servicecomb.foundation.common.utils.EventUtils;
import org.apache.servicecomb.metrics.common.MetricsDimension;
import org.apache.servicecomb.metrics.core.MetricsConfig;
import org.apache.servicecomb.metrics.core.event.dimension.StatusConvertorFactory;
import org.apache.servicecomb.metrics.core.monitor.RegistryMonitor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.netflix.config.DynamicPropertyFactory;

@Component
public class DefaultEventListenerManager implements EventListenerManager {

  @Autowired
  public DefaultEventListenerManager(RegistryMonitor registryMonitor, StatusConvertorFactory convertorFactory) {
    this(registryMonitor, convertorFactory, DynamicPropertyFactory
        .getInstance().getStringProperty(MetricsConfig.METRICS_DIMENSION_STATUS_OUTPUT_LEVEL,
            MetricsDimension.DIMENSION_STATUS_OUTPUT_LEVEL_SUCCESS_FAILED).get());
  }

  public DefaultEventListenerManager(RegistryMonitor registryMonitor, StatusConvertorFactory convertorFactory,
      String outputLevel) {
    this.registerEventListener(new InvocationStartedEventListener(registryMonitor));
    this.registerEventListener(new InvocationStartProcessingEventListener(registryMonitor));
    this.registerEventListener(
        new InvocationFinishedEventListener(registryMonitor, convertorFactory.getConvertor(outputLevel)));
  }

  @Override
  public void registerEventListener(EventListener listener) {
    EventUtils.registerEventListener(listener);
  }
}

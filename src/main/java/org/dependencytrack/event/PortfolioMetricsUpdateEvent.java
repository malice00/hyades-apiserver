/*
 * This file is part of Dependency-Track.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (c) Steve Springett. All Rights Reserved.
 */
package org.dependencytrack.event;

import alpine.event.framework.Event;
import alpine.event.framework.SingletonCapableEvent;

import java.util.UUID;

/**
 * Defines an {@link Event} used to trigger portfolio metrics updates.
 *
 * @since 4.6.0
 */
public class PortfolioMetricsUpdateEvent extends SingletonCapableEvent {

    public static final UUID CHAIN_IDENTIFIER = UUID.fromString("1d652235-9480-4033-a24e-c748811e24d6");

    private final boolean forceRefresh;

    public PortfolioMetricsUpdateEvent() {
        this(true);
    }

    public PortfolioMetricsUpdateEvent(final boolean forceRefresh) {
        this.setChainIdentifier(CHAIN_IDENTIFIER);
        this.setSingleton(true);
        this.forceRefresh = forceRefresh;
    }

    public boolean isForceRefresh() {
        return forceRefresh;
    }

}

/*
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
package org.apache.sling.commons.messaging.mail.internal;

import java.util.Objects;
import java.util.UUID;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import org.apache.sling.commons.messaging.mail.MessageIdProvider;
import org.jetbrains.annotations.NotNull;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provider for message identifiers.<br>Creates a <code>Message-ID</code> in form of <code>&lt;UUID&gt;.&lt;current time in milliseconds&gt;@&lt;host&gt;</code> where <code>host</code> is configurable.
 *
 * @see MimeMessage
 */
@Component(
    property = {
        Constants.SERVICE_DESCRIPTION + "=Apache Sling Commons Messaging Mail – Simple Message ID Provider",
        Constants.SERVICE_VENDOR + "=The Apache Software Foundation"
    }
)
@Designate(
    ocd = SimpleMessageIdProviderConfiguration.class,
    factory = true
)
public final class SimpleMessageIdProvider implements MessageIdProvider {

    private SimpleMessageIdProviderConfiguration configuration;

    private final Logger logger = LoggerFactory.getLogger(SimpleMessageIdProvider.class);

    public SimpleMessageIdProvider() { //
    }

    @Activate
    @SuppressWarnings("unused")
    private void activate(final SimpleMessageIdProviderConfiguration configuration) {
        logger.debug("activating");
        this.configuration = configuration;
    }

    @Modified
    @SuppressWarnings("unused")
    private void modified(final SimpleMessageIdProviderConfiguration configuration) {
        logger.debug("modifying");
        this.configuration = configuration;
    }

    @Deactivate
    @SuppressWarnings("unused")
    private void deactivate() {
        logger.debug("deactivating");
    }

    @Override
    public @NotNull String getMessageId(@NotNull final MimeMessage message) throws MessagingException {
        final var configuration = this.configuration;
        Objects.requireNonNull(configuration, "Configuration must not be null");
        return String.format("%s.%s@%s", UUID.randomUUID(), System.currentTimeMillis(), configuration.host());
    }

}

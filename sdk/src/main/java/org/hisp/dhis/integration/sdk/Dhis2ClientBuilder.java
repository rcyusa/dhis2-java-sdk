/*
 * Copyright (c) 2004-2022, University of Oslo
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * Neither the name of the HISP project nor the names of its contributors may
 * be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.hisp.dhis.integration.sdk;

import org.hisp.dhis.integration.sdk.api.Dhis2Client;
import org.hisp.dhis.integration.sdk.api.converter.ConverterFactory;
import org.hisp.dhis.integration.sdk.api.security.SecurityContext;
import org.hisp.dhis.integration.sdk.internal.converter.JacksonConverterFactory;
import org.hisp.dhis.integration.sdk.internal.security.BasicCredentialsSecurityContext;
import org.hisp.dhis.integration.sdk.internal.security.PersonalAccessTokenSecurityContext;

public class Dhis2ClientBuilder
{
    private final SecurityContext securityContext;

    private final String baseApiUrl;

    private final int maxIdleConnections;

    private final long keepAliveDurationMs;

    private final long callTimeoutMs;

    private final long readTimeoutMs;

    private final long writeTimeoutMs;

    private final long connectTimeoutMs;

    private ConverterFactory converterFactory = new JacksonConverterFactory();

    public static Dhis2ClientBuilder newClient( String baseApiUrl, String username, String password )
    {
        return newClient( baseApiUrl, username, password, 5, 300000 );
    }

    public static Dhis2ClientBuilder newClient( String baseApiUrl, String personalAccessToken )
    {
        return newClient( baseApiUrl, personalAccessToken, 5, 300000, 0, 10000, 10000, 10000 );
    }

    public static Dhis2ClientBuilder newClient( String baseApiUrl, String username, String password,
        int maxIdleConnections, long keepAliveDurationMs )
    {
        return new Dhis2ClientBuilder( baseApiUrl, new BasicCredentialsSecurityContext( username, password ),
            maxIdleConnections, keepAliveDurationMs, 0, 10000, 10000, 10000 );
    }

    public static Dhis2ClientBuilder newClient( String baseApiUrl, String personalAccessToken, int maxIdleConnections,
        long keepAliveDurationMs, long callTimeoutMs, long readTimeoutMs, long writeTimeoutMs, long connectTimeoutMs )
    {
        return new Dhis2ClientBuilder( baseApiUrl, new PersonalAccessTokenSecurityContext( personalAccessToken ),
            maxIdleConnections, keepAliveDurationMs, callTimeoutMs, readTimeoutMs, writeTimeoutMs, connectTimeoutMs );
    }

    public static Dhis2ClientBuilder newClient( String baseApiUrl, SecurityContext securityContext,
        int maxIdleConnections, long keepAliveDurationMs, long callTimeoutMs, long readTimeoutMs, long writeTimeoutMs,
        long connectTimeoutMs )
    {
        return new Dhis2ClientBuilder( baseApiUrl, securityContext, maxIdleConnections, keepAliveDurationMs, callTimeoutMs,
            readTimeoutMs, writeTimeoutMs, connectTimeoutMs );
    }

    private Dhis2ClientBuilder( String baseApiUrl, SecurityContext securityContext, int maxIdleConnections,
        long keepAliveDurationMs, long callTimeoutMs, long readTimeoutMs, long writeTimeoutMs, long connectTimeout )
    {
        this.baseApiUrl = baseApiUrl.trim();
        this.securityContext = securityContext;
        this.maxIdleConnections = maxIdleConnections;
        this.keepAliveDurationMs = keepAliveDurationMs;
        this.callTimeoutMs = callTimeoutMs;
        this.readTimeoutMs = readTimeoutMs;
        this.writeTimeoutMs = writeTimeoutMs;
        this.connectTimeoutMs = connectTimeout;
    }

    public Dhis2ClientBuilder withConverterFactory( ConverterFactory converterFactory )
    {
        this.converterFactory = converterFactory;
        return this;
    }

    public Dhis2Client build()
    {
        StringBuilder apiPathStringBuilder = new StringBuilder();
        apiPathStringBuilder.append( baseApiUrl );
        if ( !baseApiUrl.endsWith( "/" ) )
        {
            apiPathStringBuilder.append( "/" );
        }
        return new DefaultDhis2Client( apiPathStringBuilder.toString(), securityContext, converterFactory,
            maxIdleConnections, keepAliveDurationMs, callTimeoutMs, readTimeoutMs, writeTimeoutMs, connectTimeoutMs );
    }
}

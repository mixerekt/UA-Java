/* ========================================================================
 * Copyright (c) 2005-2015 The OPC Foundation, Inc. All rights reserved.
 *
 * OPC Foundation MIT License 1.00
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 *
 * The complete license agreement can be found here:
 * http://opcfoundation.org/License/MIT/1.00/
 * ======================================================================*/

package org.opcfoundation.ua.core;

import lombok.*;
import org.opcfoundation.ua.builtintypes.*;
import org.opcfoundation.ua.utils.*;

import java.util.*;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Data
public class EndpointConfiguration implements Structure {

    public static final ExpandedNodeId ID = new ExpandedNodeId(Identifiers.EndpointConfiguration);
    public static final ExpandedNodeId BINARY = new ExpandedNodeId(Identifiers.EndpointConfiguration_Encoding_DefaultBinary);
    public static final ExpandedNodeId XML = new ExpandedNodeId(Identifiers.EndpointConfiguration_Encoding_DefaultXml);

    /**
     * Creates an instance of a configuration with reasonable default values.
     *
     * @return configuration
     */
    public static EndpointConfiguration defaults() {
        EndpointConfiguration configuration = new EndpointConfiguration();

        configuration.operationTimeout = 120000;
        configuration.useBinaryEncoding = true;
        configuration.maxArrayLength = UnsignedShort.MAX_VALUE.intValue();
        configuration.maxByteStringLength = UnsignedShort.MAX_VALUE.intValue() * 16;
        configuration.maxMessageSize = UnsignedShort.MAX_VALUE.intValue() * 64;
        configuration.maxStringLength = UnsignedShort.MAX_VALUE.intValue();
        configuration.maxBufferSize = UnsignedShort.MAX_VALUE.intValue();
        configuration.channelLifetime = 120000;
        configuration.securityTokenLifetime = 3600000;

        return configuration;
    }

    protected Integer operationTimeout;
    protected Boolean useBinaryEncoding;
    protected Integer maxStringLength;
    protected Integer maxByteStringLength;
    protected Integer maxArrayLength;
    protected Integer maxMessageSize;
    protected Integer maxBufferSize;
    protected Integer channelLifetime;
    protected Integer securityTokenLifetime;

    @SneakyThrows
    public static EndpointConfiguration newInstanceFrom(EndpointConfiguration source) {
        Objects.requireNonNull(source);

        return (EndpointConfiguration) source.clone();
    }

    @Override
    public ExpandedNodeId getTypeId() {
        return ID;
    }

    @Override
    public ExpandedNodeId getXmlEncodeId() {
        return XML;
    }

    @Override
    public ExpandedNodeId getBinaryEncodeId() {
        return BINARY;
    }

    @Override
    public String toString() {
        return "EndpointConfiguration: " + ObjectUtils.printFieldsDeep(this);
    }

}

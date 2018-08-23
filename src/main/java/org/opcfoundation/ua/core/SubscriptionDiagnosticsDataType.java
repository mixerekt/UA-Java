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
public class SubscriptionDiagnosticsDataType implements Structure {

    public static final ExpandedNodeId ID = new ExpandedNodeId(Identifiers.SubscriptionDiagnosticsDataType);
    public static final ExpandedNodeId BINARY = new ExpandedNodeId(Identifiers.SubscriptionDiagnosticsDataType_Encoding_DefaultBinary);
    public static final ExpandedNodeId XML = new ExpandedNodeId(Identifiers.SubscriptionDiagnosticsDataType_Encoding_DefaultXml);

    protected NodeId sessionId;
    protected UnsignedInteger subscriptionId;
    protected UnsignedByte priority;
    protected Double publishingInterval;
    protected UnsignedInteger maxKeepAliveCount;
    protected UnsignedInteger maxLifetimeCount;
    protected UnsignedInteger maxNotificationsPerPublish;
    protected Boolean publishingEnabled;
    protected UnsignedInteger modifyCount;
    protected UnsignedInteger enableCount;
    protected UnsignedInteger disableCount;
    protected UnsignedInteger republishRequestCount;
    protected UnsignedInteger republishMessageRequestCount;
    protected UnsignedInteger republishMessageCount;
    protected UnsignedInteger transferRequestCount;
    protected UnsignedInteger transferredToAltClientCount;
    protected UnsignedInteger transferredToSameClientCount;
    protected UnsignedInteger publishRequestCount;
    protected UnsignedInteger dataChangeNotificationsCount;
    protected UnsignedInteger eventNotificationsCount;
    protected UnsignedInteger notificationsCount;
    protected UnsignedInteger latePublishRequestCount;
    protected UnsignedInteger currentKeepAliveCount;
    protected UnsignedInteger currentLifetimeCount;
    protected UnsignedInteger unacknowledgedMessageCount;
    protected UnsignedInteger discardedMessageCount;
    protected UnsignedInteger monitoredItemCount;
    protected UnsignedInteger disabledMonitoredItemCount;
    protected UnsignedInteger monitoringQueueOverflowCount;
    protected UnsignedInteger nextSequenceNumber;
    protected UnsignedInteger eventQueueOverFlowCount;

    @SneakyThrows
    public static SubscriptionDiagnosticsDataType newInstanceFrom(SubscriptionDiagnosticsDataType source) {
        Objects.requireNonNull(source);

        return (SubscriptionDiagnosticsDataType) source.clone();
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
        return "SubscriptionDiagnosticsDataType: " + ObjectUtils.printFieldsDeep(this);
    }

}

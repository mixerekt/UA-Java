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
public class SessionDiagnosticsDataType implements Structure {

    public static final ExpandedNodeId ID = new ExpandedNodeId(Identifiers.SessionDiagnosticsDataType);
    public static final ExpandedNodeId BINARY = new ExpandedNodeId(Identifiers.SessionDiagnosticsDataType_Encoding_DefaultBinary);
    public static final ExpandedNodeId XML = new ExpandedNodeId(Identifiers.SessionDiagnosticsDataType_Encoding_DefaultXml);

    protected NodeId sessionId;
    protected String sessionName;
    protected ApplicationDescription clientDescription;
    protected String serverUri;
    protected String endpointUrl;
    protected String[] localeIds;
    protected Double actualSessionTimeout;
    protected UnsignedInteger maxResponseMessageSize;
    protected DateTime clientConnectionTime;
    protected DateTime clientLastContactTime;
    protected UnsignedInteger currentSubscriptionsCount;
    protected UnsignedInteger currentMonitoredItemsCount;
    protected UnsignedInteger currentPublishRequestsInQueue;
    protected ServiceCounterDataType totalRequestCount;
    protected UnsignedInteger unauthorizedRequestCount;
    protected ServiceCounterDataType readCount;
    protected ServiceCounterDataType historyReadCount;
    protected ServiceCounterDataType writeCount;
    protected ServiceCounterDataType historyUpdateCount;
    protected ServiceCounterDataType callCount;
    protected ServiceCounterDataType createMonitoredItemsCount;
    protected ServiceCounterDataType modifyMonitoredItemsCount;
    protected ServiceCounterDataType setMonitoringModeCount;
    protected ServiceCounterDataType setTriggeringCount;
    protected ServiceCounterDataType deleteMonitoredItemsCount;
    protected ServiceCounterDataType createSubscriptionCount;
    protected ServiceCounterDataType modifySubscriptionCount;
    protected ServiceCounterDataType setPublishingModeCount;
    protected ServiceCounterDataType publishCount;
    protected ServiceCounterDataType republishCount;
    protected ServiceCounterDataType transferSubscriptionsCount;
    protected ServiceCounterDataType deleteSubscriptionsCount;
    protected ServiceCounterDataType addNodesCount;
    protected ServiceCounterDataType addReferencesCount;
    protected ServiceCounterDataType deleteNodesCount;
    protected ServiceCounterDataType deleteReferencesCount;
    protected ServiceCounterDataType browseCount;
    protected ServiceCounterDataType browseNextCount;
    protected ServiceCounterDataType translateBrowsePathsToNodeIdsCount;
    protected ServiceCounterDataType queryFirstCount;
    protected ServiceCounterDataType queryNextCount;
    protected ServiceCounterDataType registerNodesCount;
    protected ServiceCounterDataType unregisterNodesCount;

    @SneakyThrows
    public static SessionDiagnosticsDataType newInstanceFrom(SessionDiagnosticsDataType source) {
        Objects.requireNonNull(source);

        return (SessionDiagnosticsDataType) source.clone();
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
        return "SessionDiagnosticsDataType: " + ObjectUtils.printFieldsDeep(this);
    }

}

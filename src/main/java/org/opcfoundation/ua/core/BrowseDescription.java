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
public class BrowseDescription implements Structure {

    public static final ExpandedNodeId ID = new ExpandedNodeId(Identifiers.BrowseDescription);
    public static final ExpandedNodeId BINARY = new ExpandedNodeId(Identifiers.BrowseDescription_Encoding_DefaultBinary);
    public static final ExpandedNodeId XML = new ExpandedNodeId(Identifiers.BrowseDescription_Encoding_DefaultXml);

    protected NodeId nodeId;
    protected BrowseDirection browseDirection;
    protected NodeId referenceTypeId;
    protected Boolean includeSubtypes;
    protected UnsignedInteger nodeClassMask;
    protected UnsignedInteger resultMask;

    public NodeId getNodeId() {
        return nodeId;
    }

    public void setNodeId(NodeId nodeId) {
        this.nodeId = nodeId;
    }

    public BrowseDirection getBrowseDirection() {
        return browseDirection;
    }

    public void setBrowseDirection(BrowseDirection browseDirection) {
        this.browseDirection = browseDirection;
    }

    public NodeId getReferenceTypeId() {
        return referenceTypeId;
    }

    public void setReferenceTypeId(NodeId referenceTypeId) {
        this.referenceTypeId = referenceTypeId;
    }

    public Boolean getIncludeSubtypes() {
        return includeSubtypes;
    }

    public void setIncludeSubtypes(Boolean includeSubtypes) {
        this.includeSubtypes = includeSubtypes;
    }

    public UnsignedInteger getNodeClassMask() {
        return nodeClassMask;
    }

    public void setNodeClassMask(UnsignedInteger nodeClassMask) {
        this.nodeClassMask = nodeClassMask;
    }

    public UnsignedInteger getResultMask() {
        return resultMask;
    }

    public void setResultMask(UnsignedInteger resultMask) {
        this.resultMask = resultMask;
    }

    public void setNodeClassMask(EnumSet<NodeClass> nodeClasses) {
        int result = 0;
        for (NodeClass c : nodeClasses)
            result |= c.getValue();
        nodeClassMask = UnsignedInteger.valueOf(result);
    }

    public void setNodeClassMask(NodeClass... nodeClasses) {
        int result = 0;
        for (NodeClass c : nodeClasses)
            result |= c.getValue();
        nodeClassMask = UnsignedInteger.valueOf(result);
    }

    public void setResultMask(EnumSet<BrowseResultMask> resultMask) {
        int result = 0;
        for (BrowseResultMask c : resultMask)
            result |= c.getValue();
        this.resultMask = UnsignedInteger.valueOf(result);
    }

    public void setResultMask(BrowseResultMask... resultMask) {
        int result = 0;
        for (BrowseResultMask c : resultMask)
            result |= c.getValue();
        this.resultMask = UnsignedInteger.valueOf(result);
    }

    public ExpandedNodeId getTypeId() {
        return ID;
    }

    public ExpandedNodeId getXmlEncodeId() {
        return XML;
    }

    public ExpandedNodeId getBinaryEncodeId() {
        return BINARY;
    }

    public String toString() {
        return "BrowseDescription: " + ObjectUtils.printFieldsDeep(this);
    }

}

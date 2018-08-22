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
import org.opcfoundation.ua.common.*;
import org.opcfoundation.ua.transport.security.*;
import org.opcfoundation.ua.utils.*;

import java.util.*;


/**
 * Endpoint Description
 *
 * @see EndpointUtil for utility methods
 */

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Data
public class EndpointDescription implements Structure {

    public static final ExpandedNodeId ID = new ExpandedNodeId(Identifiers.EndpointDescription);
    public static final ExpandedNodeId BINARY = new ExpandedNodeId(Identifiers.EndpointDescription_Encoding_DefaultBinary);
    public static final ExpandedNodeId XML = new ExpandedNodeId(Identifiers.EndpointDescription_Encoding_DefaultXml);

    protected String endpointUrl;
    protected ApplicationDescription server;
    protected ByteString serverCertificate;
    protected MessageSecurityMode securityMode;
    protected String securityPolicyUri;
    protected UserTokenPolicy[] userIdentityTokens;
    protected String transportProfileUri;
    protected UnsignedByte securityLevel;

    @SneakyThrows
    public static EndpointDescription newInstanceFrom(EndpointDescription source) {
        Objects.requireNonNull(source);

        return (EndpointDescription) source.clone();
    }

    /**
     * Tests whether the stack and the endpoint supports given token type.
     * This verifies that the stack knows the encryption algorithms of the
     * token type.
     *
     * @param type
     * @return true, if token type is supported
     */
    public boolean supportsUserTokenType(UserTokenType type) {
        return findUserTokenPolicy(type) != null;
    }

    /**
     * Finds UserTokenPolicy of given type that this stack can encrypt
     *
     * @param type
     * @return user token policy or null
     */
    public UserTokenPolicy findUserTokenPolicy(UserTokenType type) {

        if (userIdentityTokens == null) return null;
        for (UserTokenPolicy p : userIdentityTokens) {

            // Ensure the stack knows the policy
            try {
                String securityPolicyUri = p.getSecurityPolicyUri();
                SecurityPolicy.getSecurityPolicy(securityPolicyUri);
            } catch (ServiceResultException e) {
                continue;
            }

            if (p.getTokenType() != type) continue;

            return p;
        }
        return null;
    }

    /**
     * Finds the user token policy with the specified id.
     *
     * @param policyId policy id
     * @return user token policy or null
     */
    public UserTokenPolicy findUserTokenPolicy(String policyId) {

        if (userIdentityTokens == null) return null;
        //TODO how to determine right policyId's? Now policyId == Token name
        for (UserTokenPolicy policy : userIdentityTokens)
            if (policy != null) {
                final String p = policy.getPolicyId();
                if (p != null && p.equals(policyId))
                    return policy;
            }
        return null;
    }

    public boolean needsCertificate() {
        return getSecurityMode().hasSigning() ||
                EndpointUtil.containsSecureUserTokenPolicy(getUserIdentityTokens());
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
        return "EndpointDescription: " + ObjectUtils.printFieldsDeep(this);
    }

}

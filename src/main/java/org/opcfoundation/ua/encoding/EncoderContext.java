/* Copyright (c) 1996-2015, OPC Foundation. All rights reserved.
   The source code in this file is covered under a dual-license scenario:
     - RCL: for OPC Foundation members in good-standing
     - GPL V2: everybody else
   RCL license terms accompanied with this source code. See http://opcfoundation.org/License/RCL/1.00/
   GNU General Public License as published by the Free Software Foundation;
   version 2 of the License are accompanied with this source code. See http://opcfoundation.org/License/GPLv2
   This source code is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
*/

package org.opcfoundation.ua.encoding;

import lombok.*;
import org.opcfoundation.ua.builtintypes.*;
import org.opcfoundation.ua.common.*;
import org.opcfoundation.ua.encoding.binary.*;
import org.opcfoundation.ua.utils.*;

import java.lang.reflect.*;
import java.util.*;

/**
 * <p>EncoderContext class.</p>
 */
@ToString

public class EncoderContext {

    private static final EncoderContext DEFAULT_INSTANCE = new EncoderContext(NamespaceTable.getDefaultInstance(), null, StackUtils.getDefaultSerializer());

    @Getter
    @Setter
    private IEncodeableSerializer encodeableSerializer;

    @Getter
    @Setter
    public int maxMessageSize = 4 * 1024 * 1024 * 1024;

    @Getter
    @Setter
    private NamespaceTable namespaceTable;

    @Getter
    @Setter
    private ServerTable serverTable;

    @Getter
    @Setter
    private int maxStringLength = 0;

    @Getter
    @Setter
    private int maxByteStringLength = 0;

    @Getter
    @Setter
    private int maxArrayLength = 0;

    /**
     * <p>Getter for the field <code>DEFAULT_INSTANCE</code>.</p>
     *
     * @return a {@link org.opcfoundation.ua.encoding.EncoderContext} object.
     */
    public static EncoderContext getDefaultInstance() {
        return DEFAULT_INSTANCE;
    }

    /**
     * <p>Constructor for EncoderContext.</p>
     *
     * @param namespaceTable       a {@link org.opcfoundation.ua.common.NamespaceTable} object.
     * @param serverTable          a {@link org.opcfoundation.ua.common.ServerTable} object.
     * @param encodeableSerializer a {@link org.opcfoundation.ua.encoding.binary.IEncodeableSerializer} object.
     * @param maxMessageSize       a int.
     */
    public EncoderContext(NamespaceTable namespaceTable,
                          ServerTable serverTable,
                          IEncodeableSerializer encodeableSerializer,
                          int maxMessageSize) {

        this.encodeableSerializer = encodeableSerializer;
        this.namespaceTable = namespaceTable;
        this.serverTable = serverTable;
        this.maxMessageSize = maxMessageSize;
    }

    /**
     * <p>Constructor for EncoderContext.</p>
     *
     * @param namespaceTable       a {@link org.opcfoundation.ua.common.NamespaceTable} object.
     * @param serverTable          a {@link org.opcfoundation.ua.common.ServerTable} object.
     * @param encodeableSerializer a {@link org.opcfoundation.ua.encoding.binary.IEncodeableSerializer} object.
     */
    public EncoderContext(NamespaceTable namespaceTable,
                          ServerTable serverTable,
                          IEncodeableSerializer encodeableSerializer) {

        this.encodeableSerializer = encodeableSerializer;
        this.namespaceTable = namespaceTable;
        this.serverTable = serverTable;
    }

    /**
     * <p>decode.</p>
     *
     * @param values an array of {@link org.opcfoundation.ua.builtintypes.ExtensionObject} objects.
     * @return a {@link java.lang.Object} object.
     * @throws org.opcfoundation.ua.encoding.DecodingException if any.
     */
    public Object decode(ExtensionObject[] values) throws DecodingException {

        return decode(values, null);
    }

    /**
     * <p>decode.</p>
     *
     * @param values         an array of {@link org.opcfoundation.ua.builtintypes.ExtensionObject} objects.
     * @param namespaceTable a {@link org.opcfoundation.ua.common.NamespaceTable} object.
     * @return a {@link java.lang.Object} object.
     * @throws org.opcfoundation.ua.encoding.DecodingException if any.
     */
    public Object decode(ExtensionObject[] values, NamespaceTable namespaceTable) throws DecodingException {

        Object value;
        int n = values.length;
        Structure[] newValue = new Structure[n];
        for (int i = 0; i < n; i++) {
            ExtensionObject obj = values[i];
            if (obj != null) newValue[i] = obj.decode(this, namespaceTable);
        }
        value = newValue;
        if (n > 0) {
            Class<? extends Structure> valueClass = null;
            for (int i = 0; i < n; i++)
                if (newValue[i] != null) {
                    Class<? extends Structure> newClass = newValue[i]
                            .getClass();
                    if (valueClass == null)
                        valueClass = newClass;
                    else if (!newClass.isAssignableFrom(valueClass))
                        if (valueClass.isAssignableFrom(newClass))
                            valueClass = newClass;
                        else {
                            valueClass = null;
                            break;
                        }
                }
            if (valueClass != null)
                value = Arrays.copyOf(newValue, n, ((Structure[]) Array
                        .newInstance(valueClass, 0)).getClass());
        }
        return value;
    }

    /**
     * <p>getEncodeableNodeId.</p>
     *
     * @param clazz a {@link java.lang.Class} object.
     * @param type  a {@link org.opcfoundation.ua.encoding.EncodeType} object.
     * @return a {@link org.opcfoundation.ua.builtintypes.NodeId} object.
     * @throws org.opcfoundation.ua.common.ServiceResultException if any.
     */
    public NodeId getEncodeableNodeId(Class<? extends IEncodeable> clazz, EncodeType type) throws ServiceResultException {

        return namespaceTable.toNodeId(encodeableSerializer.getNodeId(clazz, type));
    }

    /**
     * <p>getEncodeableClass.</p>
     *
     * @param id a {@link org.opcfoundation.ua.builtintypes.NodeId} object.
     * @return a {@link java.lang.Class} object.
     */
    public Class<? extends IEncodeable> getEncodeableClass(NodeId id) {

        return encodeableSerializer.getClass(namespaceTable.toExpandedNodeId(id));
    }

    /**
     * <p>toNodeId.</p>
     *
     * @param id a {@link org.opcfoundation.ua.builtintypes.ExpandedNodeId} object.
     * @return a {@link org.opcfoundation.ua.builtintypes.NodeId} object.
     * @throws org.opcfoundation.ua.encoding.EncodingException if any.
     */
    public NodeId toNodeId(ExpandedNodeId id) throws EncodingException {
        try {
            return namespaceTable.toNodeId(id);
        } catch (ServiceResultException e) {
            throw new EncodingException("Could not get namespace index for given id");
        }
    }
}

package org.opcfoundation.ua.utils;

import lombok.extern.slf4j.*;
import org.opcfoundation.ua.builtintypes.*;

/**
 * A base class for Structure implementations. Main use case for extending this class
 * is the default implementation for .clone which does not throw {@link CloneNotSupportedException}
 * in .clone method signature as Structure as of GH#65 extends Cloneable making it easier for
 * classes extending this class to just call super.clone without a try-catch block.
 */
@Slf4j
public abstract class AbstractStructure implements Structure {

    @Override
    public AbstractStructure clone() {
        try {
            return (AbstractStructure) super.clone();
        } catch (CloneNotSupportedException e) {
            //It should be technically impossible for this call to fail as
            //Structure extends Cloneable, but handing this just in case.
            log.error("Got a CloneNotSupportedException, should be impossible", e);
            throw new Error("Every Structure implementation shall be Cloneable", e);
        }
    }


}

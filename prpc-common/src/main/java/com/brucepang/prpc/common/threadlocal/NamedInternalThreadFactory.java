package com.brucepang.prpc.common.threadlocal;

import com.brucepang.prpc.util.NamedThreadFactory;

/**
 * NamedInternalThreadFactory
 */
public class NamedInternalThreadFactory extends NamedThreadFactory {

    public NamedInternalThreadFactory() {
        super();
    }

    public NamedInternalThreadFactory(String prefix) {
        super(prefix, false);
    }

    public NamedInternalThreadFactory(String prefix, boolean daemon) {
        super(prefix, daemon);
    }


}

package com.brucepang.prpc.common.bytecode;

import javassist.LoaderClassPath;
import javassist.NotFoundException;

import java.io.InputStream;
import java.net.URL;

/**
 * Ensure javassist will load PRPC's class from PRPC's classLoader
 */
public class PrpcLoaderClassPath extends LoaderClassPath {
    public PrpcLoaderClassPath() {
        super(PrpcLoaderClassPath.class.getClassLoader());
    }

    @Override
    public InputStream openClassfile(String classname) throws NotFoundException {
        if (!classname.startsWith("com.brucepang.prpc") && !classname.startsWith("grpc.health") && !classname.startsWith("com.google")) {
            return null;
        }
        return super.openClassfile(classname);
    }

    @Override
    public URL find(String classname) {
        if (!classname.startsWith("com.brucepang.prpc")) {
            return null;
        }
        return super.find(classname);
    }
}

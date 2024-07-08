package com.brucepang.prpc.common.compiler.support;

import javax.tools.*;
import java.io.*;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author BrucePang
 */
public class JdkCompiler extends AbstractCompiler{

    @Override
    protected Class<?> doCompile(String name ,String sourceCode, ClassLoader classLoader) throws Throwable {
        int i = name.lastIndexOf('.');
        String packageName = i < 0 ? "" : name.substring(0, i);
        String className = i < 0 ? name : name.substring(i + 1);
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(new DiagnosticCollector<>(), null, null);
        JavaFileObjectImpl file = new JavaFileObjectImpl(className, sourceCode);
        fileManager.setLocation(StandardLocation.SOURCE_PATH,
                Optional.of(classLoader)
                        .filter(URLClassLoader.class::isInstance)
                        .map(URLClassLoader.class::cast)
                        .map(URLClassLoader::getURLs)
                        .map(Arrays::stream)
                        .orElseGet(Stream::empty)
                        .map(URL::getFile)
                        .map(File::new)
                        .collect(Collectors.toList())
                );
        Iterable<? extends JavaFileObject> compilationUnits = Arrays.asList(file);
        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager,
                new DiagnosticCollector<JavaFileObject>(),
                Arrays.asList(
                        "-source", "1.8", "-target", "1.8"
                ),
                null,
                compilationUnits);
        boolean success = task.call();
        if (!success) {
            throw new IllegalArgumentException("Compilation failed.");
        }
        // todo
        URLClassLoader loader = URLClassLoader.newInstance(new URL[]{new File("").toURI().toURL()});
        return Class.forName(className, true, loader);
    }

    /**
     * A file object used to represent source coming from a string.
     */
    private static final class JavaFileObjectImpl extends SimpleJavaFileObject {

        private final CharSequence source;
        private ByteArrayOutputStream bytecode;

        public JavaFileObjectImpl(final String baseName, final CharSequence source) {
            super(ClassUtils.toURI(baseName + ClassUtils.JAVA_EXTENSION), Kind.SOURCE);
            this.source = source;
        }

        JavaFileObjectImpl(final String name, final Kind kind) {
            super(ClassUtils.toURI(name), kind);
            source = null;
        }

        public JavaFileObjectImpl(URI uri, Kind kind) {
            super(uri, kind);
            source = null;
        }

        @Override
        public CharSequence getCharContent(final boolean ignoreEncodingErrors) throws UnsupportedOperationException {
            if (source == null) {
                throw new UnsupportedOperationException("source == null");
            }
            return source;
        }

        @Override
        public InputStream openInputStream() {
            return new ByteArrayInputStream(getByteCode());
        }

        @Override
        public OutputStream openOutputStream() {
            return bytecode = new ByteArrayOutputStream();
        }

        public byte[] getByteCode() {
            return bytecode.toByteArray();
        }
    }


}

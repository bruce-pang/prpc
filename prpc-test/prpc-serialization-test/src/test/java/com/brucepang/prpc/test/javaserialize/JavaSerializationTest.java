package com.brucepang.prpc.test.javaserialize;

import com.brucepang.prpc.common.serialize.JavaSerialization;
import com.brucepang.prpc.util.Assert;
import org.junit.Test;

import java.io.IOException;

/**
 * Java serialization test
 */
public class JavaSerializationTest {


    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {
        JavaSerialization javaSerialization = new JavaSerialization();

        // Test object to serialize
        String testString = "Hello, World!";

        // Serialize the object
        byte[] serializedData = javaSerialization.serialize(testString);

        // Deserialize the object
        String deserializedString = javaSerialization.deserialize(serializedData, String.class);

        // Check if the original object and the deserialized object are equal
        Assert.assertEquals(testString, deserializedString);
    }
}

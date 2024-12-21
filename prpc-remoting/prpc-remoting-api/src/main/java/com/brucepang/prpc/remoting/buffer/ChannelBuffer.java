package com.brucepang.prpc.remoting.buffer;

import java.nio.ByteBuffer;

/**
 * ChannelBuffer is a byte buffer for netty channel.
 */
public interface ChannelBuffer {

    /**
     * Returns the number of bytes (octets) this buffer can contain.
     *
     * @return the capacity of this buffer
     */
    int capacity();

    /**
     * Sets the readerIndex and writerIndex of this buffer in one shot.
     *
     * @param readerIndex the new reader index
     * @param writerIndex the new writer index
     */
    void setIndex(int readerIndex, int writerIndex);

    /**
     * Returns the readerIndex of this buffer.
     *
     * @return the reader index of this buffer
     */
    int readerIndex();

    /**
     * Sets the readerIndex of this buffer.
     *
     * @param readerIndex the new reader index
     */
    void readerIndex(int readerIndex);

    /**
     * Returns the writerIndex of this buffer.
     *
     * @return the writer index of this buffer
     */
    int writerIndex();

    /**
     * Sets the writerIndex of this buffer.
     *
     * @param writerIndex the new writer index
     */
    void writerIndex(int writerIndex);

    /**
     * Returns the number of readable bytes which is equal to
     * {@code (this.writerIndex - this.readerIndex)}.
     *
     * @return the number of readable bytes
     */
    int readableBytes();

    /**
     * Returns the number of writable bytes which is equal to
     * {@code (this.capacity - this.writerIndex)}.
     *
     * @return the number of writable bytes
     */
    int writableBytes();

    /**
     * Gets a byte at the current {@code readerIndex} and increases
     * the {@code readerIndex} by {@code 1} in this buffer.
     *
     * @return the byte at the current readerIndex
     */
    byte readByte();

    /**
     * Writes the specified byte into this buffer at the current
     * {@code writerIndex} and increases the {@code writerIndex} by {@code 1}.
     *
     * @param data the byte to write
     */
    void writeByte(int data);

    /**
     * Gets a byte array at the current {@code readerIndex} and increases
     * the {@code readerIndex} by the length of the array in this buffer.
     *
     * @param dst the destination byte array
     */
    void readBytes(byte[] dst);

    /**
     * Writes the specified byte array into this buffer at the current
     * {@code writerIndex} and increases the {@code writerIndex} by the length of the array.
     *
     * @param src the source byte array
     */
    void writeBytes(byte[] src);

    /**
     * Transfers this buffer's data to the specified destination starting at
     * the current {@code readerIndex} and increases the {@code readerIndex}
     * by the number of the transferred bytes.
     *
     * @param dst the destination buffer
     */
    void readBytes(ByteBuffer dst);

    /**
     * Transfers the specified source buffer's data to this buffer starting at
     * the current {@code writerIndex} and increases the {@code writerIndex}
     * by the number of the transferred bytes.
     *
     * @param src the source buffer
     */
    void writeBytes(ByteBuffer src);
}
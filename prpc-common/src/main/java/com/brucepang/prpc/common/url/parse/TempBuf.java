package com.brucepang.prpc.common.url.parse;

final class TempBuf {

        private final char[] chars;

        private final byte[] bytes;

        TempBuf(int bufSize) {
            this.chars = new char[bufSize];
            this.bytes = new byte[bufSize];
        }

        public char[] charBuf(int size) {
            char[] chars = this.chars;
            if (size <= chars.length) {
                return chars;
            }
            return new char[size];
        }

        public byte[] byteBuf(int size) {
            byte[] bytes = this.bytes;
            if (size <= bytes.length) {
                return bytes;
            }
            return new byte[size];
        }
    }
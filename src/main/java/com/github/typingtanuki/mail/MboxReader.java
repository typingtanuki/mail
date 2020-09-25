package com.github.typingtanuki.mail;

import com.google.common.collect.Lists;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

import static com.github.typingtanuki.mail.Formatter.*;

public final class MboxReader {
    private static CharsetDecoder decoder;

    private MboxReader() {
        super();
    }

    public static List<String> read(Path path) throws IOException {
        init();

        progress("Processing " + path + "...");
        try (RandomAccessFile file = new RandomAccessFile(path.toFile(), "r")) {
            try (FileChannel channel = file.getChannel()) {
                int fileSize = (int) channel.size();

                if (fileSize <= 0) {
                    skipped();
                    return Collections.emptyList();
                }

                ByteBuffer buffer = ByteBuffer.allocate(fileSize);
                channel.read(buffer);
                buffer.flip();

                CharBuffer chars = decoder.decode(buffer);
                buffer.clear();

                List<String> out = Lists.newArrayList(chars.toString().split("[\\r\\n]+"));
                finished("" + out.size() + " lines");
                return out;
            }
        }
    }

    private static void init() {
        if (decoder != null) {
            decoder.reset();
            return;
        }
        decoder = Charset.forName("ISO-2022-JP").newDecoder();
        decoder.onMalformedInput(CodingErrorAction.IGNORE);
        decoder.onUnmappableCharacter(CodingErrorAction.IGNORE);
    }
}

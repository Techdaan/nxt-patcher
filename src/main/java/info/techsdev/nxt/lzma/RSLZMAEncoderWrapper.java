package info.techsdev.nxt.lzma;

import lzma.sdk.lzma.Encoder;
import org.cservenak.streams.Coder;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Wrapper for the LZMA encoder. The only difference is that this wrapper writes the uncompressed file size to the
 * LZMA header
 */
public class RSLZMAEncoderWrapper implements Coder {

    /**
     * The wrapped encoder
     */
    private final Encoder encoder;

    /**
     * The uncompressed file size
     */
    private final int length;

    /**
     * Creates a new encoder wrapper
     *
     * @param encoder The encoder to wrap
     * @param length  The uncompressed file size of the file to compress
     */
    public RSLZMAEncoderWrapper(final Encoder encoder, int length) {
        this.encoder = encoder;
        this.length = length;
    }

    @Override
    public void code(InputStream in, OutputStream out) throws IOException {
        encoder.writeCoderProperties(out);

        for (int i = 0; i < 8; i++) {
            out.write((int) ((long) length >>> (8 * i)) & 0xFF);
        }

        encoder.code(in, out, -1, -1, null);
    }
}

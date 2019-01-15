package info.techsdev.nxt.lzma;

import lzma.sdk.lzma.Encoder;
import lzma.streams.LzmaEncoderWrapper;
import org.cservenak.streams.CoderOutputStream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * LZMA output stream modified to work for RuneScape's NXT launcher
 */
public class RSLZMAOutputStream extends CoderOutputStream {

    public RSLZMAOutputStream(OutputStream out, Encoder lzmaEncoder, int length)
            throws IOException {
        super(out, new RSLZMAEncoderWrapper(lzmaEncoder, length));
    }

    public RSLZMAOutputStream(OutputStream out, LzmaEncoderWrapper wrapper, int length)
            throws IOException {
        super(out, wrapper);
    }

    public static RSLZMAOutputStream create(OutputStream out, Encoder encoder, int length) throws IOException {
        encoder.setDictionarySize(1 << 23);
        encoder.setEndMarkerMode(true);
        encoder.setMatchFinder(1);
        encoder.setNumFastBytes(0x20);

        return new RSLZMAOutputStream(out, encoder, length);
    }

    /**
     * Compresses a block of bytes with a RS LZMA compression
     *
     * @param data The data to compress
     * @return The compressed data
     */
    public static byte[] compress(byte[] data) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        RSLZMAOutputStream out = create(baos, new Encoder(), data.length);
        out.write(data);
        out.flush();
        out.close();

        return baos.toByteArray();
    }

}

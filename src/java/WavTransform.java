import org.apache.commons.math3.transform.*;
import org.apache.commons.math3.complex.*;
import java.io.*;

public class WavTransform {
    static final int WINDOW_SIZE = 4;
    static final int BlOCK_LEN = 1024;
    static final FastFourierTransformer fftTransfromer = new FastFourierTransformer(DftNormalization.STANDARD);

    public static void fromFile(String path, String outPath) throws IOException, WavFileException {
        final WavFile f = WavFile.openWavFile(new File(path));

        final double[][] splitFrames = new double[f.getNumChannels()][BlOCK_LEN];
        int read = BlOCK_LEN;
        final WavFile writeableFile = WavFile.newWavFile(new File(outPath), f.getNumChannels(), f.getNumFrames(), f.getValidBits(), f.getSampleRate());

        while (read == BlOCK_LEN && f.getFramesRemaining() != 0) {

            read = splitChannels(splitFrames, f);

            for (int i = 0; i < splitFrames.length; i ++) {
                applyTransform(splitFrames[i]);
            
            }

            writeableFile.writeFrames(splitFrames, read);
            
        }

        f.close();
        writeableFile.close();

    }

    private static void applyTransform(double[] data) {
        Complex[] intermediate = fftTransfromer.transform(data, TransformType.FORWARD);
        applyFilter(intermediate);
        Complex[] arr = fftTransfromer.transform(intermediate, TransformType.INVERSE);

        for (int i = 0; i < arr.length; i ++) {
            data[i] = arr[i].getReal();
        }
    }

    private static void applyFilter(Complex[] arr) {
        Complex zero = new Complex(0, 0);
        for (int i = 0; i < arr.length/16; i ++) {
            arr[i] = zero;
        }
        for (int i = 15*arr.length/16; i < arr.length; i ++) {
            arr[i] = zero;
        }
    }

    private static int splitChannels(final double[][] buffer, final WavFile source) throws IOException, WavFileException {
        final int chans = source.getNumChannels();
        if (buffer.length == 0) return 0;

        
        int read = source.readFrames(buffer, BlOCK_LEN);
        if (read == BlOCK_LEN)
            return 0;
        else {
            
            for (int i = read - 1; i < BlOCK_LEN; i++) {
                for (int x = 0; x < chans; x++) {
                    buffer[x][i] = 0;
                }
            }

            return read;
        }
    }

    // private static double[] mergeChannels(double[][] data, int frames, int channels) {
    //     double[][] buffer = new double[channels * frames];
    //     int index = 0;
    //     for (int i = 0; i < frames; i++) {
    //         for (int x = 0; x < channels; x++) {
    //             buffer[index] = data[x][i];
    //             index++;
    //         }
    //     }

    //     return buffer;

    // }

}

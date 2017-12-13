import com.ohua.lang.Tuple;
import org.apache.commons.math3.transform.*;
import org.apache.commons.math3.complex.*;
import java.io.*;
import java.util.LinkedList;
import java.util.List;

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

//    public static List<Tuple<double[][], Integer>> loadFrames(WavFile f) throws IOException, WavFileException {
     public static List<Object[]> loadFrames(WavFile f) throws IOException, WavFileException {
      double[][] splitFrames = null;
      int read = BlOCK_LEN;

      List<Object[]> r = new LinkedList<>();
//      System.out.println("Num channels: " + f.getNumChannels());
      while (read == BlOCK_LEN && f.getFramesRemaining() != 0) {
        splitFrames = new double[f.getNumChannels()][BlOCK_LEN];
        read = splitChannels(splitFrames, f);
        r.add(new Object[] {splitFrames, read});
      }
      return r;
    }

    public static void writeFrames(List<Tuple<double[][], Integer>> resultFrames, WavFile outFile) throws IOException, WavFileException {
      for(Tuple<double[][], Integer> block : resultFrames){
        outFile.writeFrames(block.first(), block.second());
      }
    }

    public static WavFile openWavFile(String path) throws IOException, WavFileException {
      return WavFile.openWavFile(new File(path));
    }

    public static WavFile createTargetFile(WavFile f, String outPath) throws IOException, WavFileException {
      return WavFile.newWavFile(new File(outPath), f.getNumChannels(), f.getNumFrames(), f.getValidBits(), f.getSampleRate());
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
        
        for (int i = read - 1; i < BlOCK_LEN; i++) {
            for (int x = 0; x < chans; x++) {
                buffer[x][i] = 0;
            }
        }

        return read;
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

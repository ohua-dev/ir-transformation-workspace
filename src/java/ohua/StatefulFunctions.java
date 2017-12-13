package ohua;

import com.ohua.lang.Tuple;
import com.ohua.lang.defsfn;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;

import java.util.Arrays;

public abstract class StatefulFunctions{

  public static class FFT{
    // private state (note the state of the transformer class!)
    final FastFourierTransformer fftTransfromer = new FastFourierTransformer(DftNormalization.STANDARD);

    @defsfn
    public Complex[] fft(double[] data) {
      return fftTransfromer.transform(data, TransformType.FORWARD);
    }
  }

  public static class iFFT{
    // private state (note the state of the transformer class!)
    final FastFourierTransformer fftTransfromer = new FastFourierTransformer(DftNormalization.STANDARD);

    @defsfn
    public Object ifft(Complex[] data){
      Complex[] arr = fftTransfromer.transform(data, TransformType.INVERSE);
      return Arrays.stream(arr).mapToDouble(Complex::getReal).toArray();
    }
  }

  public static class Filter{
    // stateless

    @defsfn
    public Complex[] filter(Complex[] arr) {
      Complex zero = new Complex(0, 0);
      for (int i = 0; i < arr.length/16; i ++) {
        arr[i] = zero;
      }
      for (int i = 15*arr.length/16; i < arr.length; i ++) {
        arr[i] = zero;
      }
      return arr;
    }
  }

  public static class ChannelMerger{
    // stateless

    @defsfn
    public Tuple mergeChannels(double[] channel1, double[] channel2, int read){
      return new Tuple(new double[][]{ channel1, channel2 },read);
    }
  }

}
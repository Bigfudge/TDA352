import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigInteger;


public class ElGamal {

  public static String decodeMessage(BigInteger m) {
    return new String(m.toByteArray());
  }  

  public static void main(String[] arg) {
    String filename = "input.txt";
    try {
      BufferedReader br = new BufferedReader(new FileReader(filename));
      BigInteger p = new BigInteger(br.readLine().split("=")[1]);
      BigInteger g = new BigInteger(br.readLine().split("=")[1]);
      BigInteger y = new BigInteger(br.readLine().split("=")[1]);
      String line = br.readLine().split("=")[1];
      String date = line.split(" ")[0];
      String time = line.split(" ")[1];
      int year  = Integer.parseInt(date.split("-")[0]);
      int month = Integer.parseInt(date.split("-")[1]);
      int day   = Integer.parseInt(date.split("-")[2]);
      int hour   = Integer.parseInt(time.split(":")[0]);
      int minute = Integer.parseInt(time.split(":")[1]);
      int second = Integer.parseInt(time.split(":")[2]);
      BigInteger c1 = new BigInteger(br.readLine().split("=")[1]);
      BigInteger c2 = new BigInteger(br.readLine().split("=")[1]);
      br.close();
      BigInteger m = recoverSecret(p, g, y, year, month, day, hour, minute,
          second, c1, c2);
      System.out.println("Recovered message: " + m);
      System.out.println("Decoded text: " + decodeMessage(m));
    } catch (Exception err) {
      System.err.println("Error handling file.");
      err.printStackTrace();
      System.exit(1);
    }
  }
  
  public static BigInteger recoverSecret(BigInteger p, BigInteger g,
      BigInteger y, int year, int month, int day, int hour, int minute,
      int second, BigInteger c1, BigInteger c2) {
      BigInteger tmp = BigInteger.ZERO;
      BigInteger r = null;
      for (int i =0;i<1000 ;i++ ) {
        r = getRand(year, month, day, hour, minute, second, i);
        if(c1.equals(g.modPow(r,p))){
          break;
        }
      }
    BigInteger inv = y.modPow(r.negate(),p);

    return c2.multiply(inv).mod(p);
  }
  private static BigInteger getRand(int year, int month, int day, int hour, int minute, int second, int milli){
      BigInteger y=  BigInteger.valueOf(year);
      BigInteger ye= BigInteger.TEN.pow(10);

      BigInteger m=  BigInteger.valueOf(month);
      BigInteger me= BigInteger.TEN.pow(8);
      
      BigInteger d=  BigInteger.valueOf(day);
      BigInteger de= BigInteger.TEN.pow(6);
      
      BigInteger h=  BigInteger.valueOf(hour);
      BigInteger he= BigInteger.TEN.pow(4);
      
      BigInteger min=  BigInteger.valueOf(minute);
      BigInteger mine= BigInteger.TEN.pow(2);
      
      BigInteger s=  BigInteger.valueOf(second);      
      BigInteger mil=  BigInteger.valueOf(milli);

      BigInteger res= y.multiply(ye)
                      .add(m.multiply(me))
                      .add(d.multiply(de))
                      .add(h.multiply(he))
                      .add(min.multiply(mine))
                      .add(s)
                      .add(mil);
      return res;
  }
}

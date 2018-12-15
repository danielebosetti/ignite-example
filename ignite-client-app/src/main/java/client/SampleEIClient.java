package client;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Run the same sample but for assetType=EI, different pmsCodes and a different set of fields
 * 
 * Extra setup:
 * ALTER TABLE product add column somefield2 VARCHAR(50);
 * 
 * Note:
 * ALTER TABLE product drop column somefield2;
 * does not send a delete for this field (it sends nothing at all) in fact the underlying value objects still contain this field.
 * 
 * rerunning:
 * ALTER TABLE product add column somefield2 VARCHAR(50);
 * 
 * Does not clear this - any data is still present from before it was removed.
 * 
 * @author twilliamson
 *
 */
public class SampleEIClient extends SampleCurrenciesClient {

  public SampleEIClient(String string, List<String> asList) {
    super(string, asList);
  }

  public static void main(String[] args) {
    SampleEIClient igniteClientSample = new SampleEIClient("EI", Arrays.asList(new String[] {"SOMEFIELD1","SOMEFIELD2"}));
    igniteClientSample.init();
    igniteClientSample.start();
  }
  
  @Override
  public void start() {
    long tickId = 0l;
    String pmsCode = null;
    while (shouldRun) {
      tickId++;
      if(tickId % 2 == 0)
        pmsCode = "X-1233";
      else
        pmsCode = "X-1235";
      
      System.out.println(new Date() + ":\t" + tickId + "\t" + pmsCode + "\t" + getFieldForPMSCode(pmsCode, "SOMEFIELD1") + "\t" + getFieldForPMSCode(pmsCode, "SOMEFIELD2"));
      
      try {Thread.sleep(1000l);} catch(InterruptedException e) {}
    }
  }
  
  
  
}
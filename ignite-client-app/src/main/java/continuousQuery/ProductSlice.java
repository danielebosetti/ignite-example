package continuousQuery;

import javax.cache.event.CacheEntryEvent;
import org.apache.ignite.Ignite;
import org.apache.ignite.binary.BinaryObject;
import org.apache.ignite.binary.BinaryObjectBuilder;
import org.apache.ignite.lang.IgniteClosure;
import org.apache.ignite.resources.IgniteInstanceResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProductSlice
    implements IgniteClosure<CacheEntryEvent<? extends Object, ? extends BinaryObject>, BinaryObject> {

  @IgniteInstanceResource
  private Ignite ignite;
  
  private static final long serialVersionUID = 1L;
  private static final Logger log = LoggerFactory.getLogger("test.ProductSlice");
  
  public ProductSlice() {
    log.info("created");
  }

  @Override
  public BinaryObject apply(CacheEntryEvent<? extends Object, ? extends BinaryObject> e) {
    System.out.println("ProductSlice.apply: e="+e);
    BinaryObject bo = e.getValue();
    String pmsCode = bo.field("pmsCode");
    BinaryObjectBuilder builder = ignite.binary().builder("domain.Product");
    builder.setField("pmsCode", pmsCode);

    BinaryObject res = builder.build();
    return res;
  }

}

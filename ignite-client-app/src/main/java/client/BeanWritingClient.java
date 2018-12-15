package client;

import java.util.Date;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.CacheConfiguration;
import domain.Product;
import util.CacheUtil;

/**
 * simple client creates the cache if it does not exist
 * 
 * writes products to the product cache
 */
public class BeanWritingClient {

  public static void main(String[] args) {

    try (Ignite ig = Ignition.start("ignite-client.xml")) {

      for (int i = 0; i < 4; i++) {
        String key = "pmsCode" + i;
        String desc = "my-desc-" + i;
        String assetType = (i % 2 == 0) ? "FX" : "EQ";

        writeItems(ig, key, desc, assetType);
      }
    }
  }

  public static void dumpCacheInfo(Ignite ig) {
    IgniteCache<Object, Object> cache = CacheUtil.getProductCacheSql(ig);

    @SuppressWarnings("unchecked")
    CacheConfiguration<?, ?> conf = cache.getConfiguration(CacheConfiguration.class);
    System.out.println("cache mode=" + conf.getCacheMode());
    System.out.println("cache conf=" + conf);
  }

  public static void writeItems(Ignite ig, String key, String desc, String assetType) {
    IgniteCache<Object, Object> cache = CacheUtil.getProductCacheSql(ig);

    Product product = new Product(key);
    product.setDescription(desc);
    product.setAssetType(assetType);
    product.setUpdateTime(new Date());
    System.out.println("caching " + key + "=" + product);
    cache.put(key, product);
  }
}

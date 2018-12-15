package client.compute;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.lang.IgniteCallable;

public class HelloWorldCompute {

  @SuppressWarnings({"rawtypes", "unchecked"})
  public static void main(String[] args) {
    try (Ignite ig = Ignition.start("ignite-client.xml")) {
      
      IgniteCallable ic = new IgniteCallable() {
        private static final long serialVersionUID = 1L;
        @Override
        public Object call() throws Exception {
          System.out.println("hello world");
          return null;
        }
      };
      
      ig.compute().call( ic );
    }
  }
}
